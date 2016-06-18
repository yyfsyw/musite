/**
 * Musite
 * Copyright (C) 2010 Digital Biology Laboratory, University Of Missouri
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package musite.prediction.classifier;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

import java.util.ArrayList;
import java.util.List;

import musite.MusiteInit;

import musite.prediction.feature.Instance;
import musite.prediction.feature.FeatureExtractor;

import musite.util.ExecUtil;
import musite.util.IOUtil;

/**
 *
 * @author Jianjiong Gao
 */
public final class SVMLightBinaryClassifier implements BinaryClassifier, Serializable {
    private static final long serialVersionUID = -8163571852149370448L;

    private static class SerializationProxy implements Serializable {
        private static final long serialVersionUID = -5174506133721282694L;

        private final List<FeatureExtractor> extractors;
        private final String svmLightOption;
        private final FeatureNormalizer featureNormalizer;
        private List<String> model;
        SerializationProxy(SVMLightBinaryClassifier actualObj) {
            if (actualObj.model==null)
                throw new IllegalStateException("Not trained yet.");
            this.extractors = actualObj.extractors;
            this.svmLightOption = actualObj.svmLightOption;
            this.featureNormalizer = actualObj.featureNormalizer;
            this.model = actualObj.model;
        }

        private Object readResolve() {
            SVMLightBinaryClassifier svm = new SVMLightBinaryClassifier(svmLightOption, featureNormalizer, extractors);
            svm.model = model;
            return svm;
        }
    }

    private Object writeReplace() {
        return new SerializationProxy(this);
    }

    private void readObject(java.io.ObjectInputStream stream) throws java.io.InvalidObjectException {
        throw new java.io.InvalidObjectException("Proxy required");
    }

    private transient final String dirSVMLightTrain;
    private transient final String dirSVMLightClassify;

    private final List<FeatureExtractor> extractors;

    private final String svmLightOption;
    private final FeatureNormalizer featureNormalizer;

    private List<String> model;

    private static final int sbBuf = 1000;

    public SVMLightBinaryClassifier(final String svmLightOption,
                                    final FeatureNormalizer featureNormalizer) {
        this(svmLightOption, featureNormalizer, null);
    }

    public SVMLightBinaryClassifier(final String svmLightOption,
            final FeatureNormalizer featureNormalizer, final List<FeatureExtractor> extractors) {
        this(MusiteInit.globalProps.getProperty(MusiteInit.GLOBAL_PROP_SVM_TRAIN_FILE),
                MusiteInit.globalProps.getProperty(MusiteInit.GLOBAL_PROP_SVM_CLASSIFY_FILE),
                svmLightOption, featureNormalizer, extractors);
    }

    public SVMLightBinaryClassifier(final String dirSVMLightTrain,
            final String dirSVMLightClassify, final String svmLightOption,
            final FeatureNormalizer featureNormalizer, final List<FeatureExtractor> extractors) {
        if (dirSVMLightTrain==null || dirSVMLightClassify==null || svmLightOption==null) {
            throw new NullPointerException();
        }

//        File file = new File(dirSVMLightTrain);
//        if (!file.exists()) {
//            throw new IllegalArgumentException(dirSVMLightTrain + " does not exist.");
//        }
//
//        file = new File(dirSVMLightClassify);
//        if (!file.exists()) {
//            throw new IllegalArgumentException(dirSVMLightClassify + " does not exist.");
//        }

        this.dirSVMLightTrain = dirSVMLightTrain;
        this.dirSVMLightClassify = dirSVMLightClassify;
        this.extractors = extractors;
        this.svmLightOption = svmLightOption;
        this.featureNormalizer = featureNormalizer;
        this.model = null;

    }

    /**
     *
     * @param instances
     * @param positive
     * @return
     */
    public boolean train(List<Instance> positives, List<Instance> negatives) {
        if (positives==null || positives.isEmpty() || negatives==null || negatives.isEmpty()) {
            throw new IllegalArgumentException();
        }
        
        long currTime = System.currentTimeMillis();
        String tmpDirData = MusiteInit.TMP_DIR+File.separator
                +currTime+".data.tmp";
        String tmpDirModel = MusiteInit.TMP_DIR+File.separator
                +currTime+".model.tmp";

        boolean ret = false;

        if (prepareSVMLightTrainingData(tmpDirData, positives, negatives)) {
            if (runSVMLightTrain(tmpDirData, tmpDirModel)) {
                if (readModel(tmpDirModel)) {
                    ret = true;
                }
            }
        }

        try {
            IOUtil.deleteFile(tmpDirData);
            IOUtil.deleteFile(tmpDirModel);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return ret;
    }

    public List<Double> classify(final List<Instance> instances) {
        long currTime = System.currentTimeMillis();
        String tmpDirData = MusiteInit.TMP_DIR+File.separator
                +currTime+".data.tmp";
        String tmpDirModel = MusiteInit.TMP_DIR+File.separator
                +currTime+".model.tmp";
        String tmpDirResult = MusiteInit.TMP_DIR+File.separator
                +currTime+".res.tmp";

        if (instances==null) {
            throw new NullPointerException();
        }

        if (model==null) {
            throw new IllegalStateException("Train first!");
        }

        List<Double> ret = null;
        List<Boolean> succ = prepareSVMLightTestingData(tmpDirData, instances);
        if (succ!=null) {
            if (writeModel(tmpDirModel)) {
                if (runSVMLightClassify(tmpDirData,tmpDirModel,tmpDirResult)) {
                    ret = parseResult(tmpDirResult);
                    int n = succ.size();
                    for (int i=0; i<n; i++) {
                        if (!succ.get(i)) {
                            ret.set(i, Double.NEGATIVE_INFINITY);
                        }
                    }
                }
            }
        }

        try {
            IOUtil.deleteFile(tmpDirData);
            IOUtil.deleteFile(tmpDirModel);
            IOUtil.deleteFile(tmpDirResult);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return ret;
    }

    private boolean prepareSVMLightTrainingData(final String tmpDirData,
            List<Instance> positives, List<Instance> negatives) {
        int np = positives.size();
        int nn = negatives.size();
        List<List<Double>> features = new ArrayList<List<Double>>(np+nn);
        List<Boolean> positive = new ArrayList(np+nn);
        for (int i=0; i<np; i++) {
            List<Double> fs = extractFeature(positives.get(i));

            if (fs==null || fs.isEmpty()) {
                //System.out.println();
            } else {
                positive.add(Boolean.TRUE);
                features.add(fs);
            }
        }
        for (int i=0; i<nn; i++) {
            List<Double> fs = extractFeature(negatives.get(i));

            if (fs==null || fs.isEmpty()) {
                //System.out.println();
            } else {
                positive.add(Boolean.FALSE);
                features.add(fs);
            }
        }

        if (featureNormalizer!=null) {
            featureNormalizer.trainParameter(features);
        }

        int n = features.size();

        try {
            Writer fout = new FileWriter(tmpDirData);
            BufferedWriter out = new BufferedWriter(fout);

            StringBuilder str = new StringBuilder(sbBuf);
            
            for (int i=0; i<n; i++) {
                List<Double> fs = features.get(i);

                if (featureNormalizer!=null) {
                    featureNormalizer.normalize(fs);
                }

                str.setLength(0); // clear
                str.append(positive.get(i)?1:-1);

                int nf = fs.size();
                for (int itf=0; itf<nf; itf++) {
                    double f = fs.get(itf);
                    str.append(" "+(itf+1)+":"+f);
                }
                out.write(str.toString());
                out.newLine();
            }

            out.close();
            fout.close();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean runSVMLightTrain(final String tmpDirData,
            final String tmpDirModel) {
        String cmd = dirSVMLightTrain+
                     " "+svmLightOption+
                     " "+tmpDirData+
                     " "+tmpDirModel;
//        ProcessBuilder pb = new ProcessBuilder(dirSVMLightTrain, svmLightOption,
//                tmpDirData, tmpDirModel);
        //pb.redirectErrorStream(true);

        try {
            Process process = Runtime.getRuntime().exec(cmd);
//            Process process = pb.start();
            ExecUtil.redirect(process, System.err, System.out);
            int retcode = process.waitFor();
            System.err.flush();
            System.out.flush();
            return retcode==0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    private List<Boolean> prepareSVMLightTestingData(final String tmpDirData,
            final List<Instance> instances) {
        
        int n = instances.size();
        List<Boolean> succ = new ArrayList();

        try {
            Writer fout = new FileWriter(tmpDirData);
            BufferedWriter out = new BufferedWriter(fout);

            StringBuilder str = new StringBuilder(sbBuf);

            for (int i=0; i<n; i++) {
                List<Double> fs = extractFeature(instances.get(i));

                if (fs==null || fs.isEmpty()) {
                    out.write("0");
                    out.newLine();
                    succ.add(Boolean.FALSE);
                    continue;
                    //return false;
                }
                
                if (featureNormalizer!=null) {
                    featureNormalizer.normalize(fs);
                }
                
                str.setLength(0);
                str.append("0");

                int nf = fs.size();
                for (int itf=0; itf<nf; itf++) {
                    double f = fs.get(itf);
                    str.append(" "+(itf+1)+":"+f);
                }
                out.write(str.toString());
                out.newLine();
                succ.add(Boolean.TRUE);
            }

            out.close();
            fout.close();
            return succ;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private List<Double> extractFeature(Instance ins) {
        List<Double> res = ins.getFeatures();

        if (extractors!=null) {
            for (FeatureExtractor extractor : extractors) {
                List<Double> fs;
                try {
                    fs = extractor.extract(ins, false);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

                if (fs==null) {
                    return null;
                }
                
                res.addAll(fs);
            }
        } 

        for(Double d : res)
        {
            if(d.isNaN())
            {
                System.err.println("NaN feature....");
                return null;
            }
        }
        return res;
    }

    private boolean runSVMLightClassify(final String tmpDirData,
            final String tmpDirModel, final String tmpDirResult) {
        String cmd = dirSVMLightClassify+
                     " "+tmpDirData+
                     " "+tmpDirModel+
                     " "+tmpDirResult;
//        ProcessBuilder pb = new ProcessBuilder(dirSVMLightClassify, tmpDirData,
//                tmpDirModel, tmpDirResult);
//        pb.redirectErrorStream(true);

        try {
            Process process = Runtime.getRuntime().exec(cmd);
            ExecUtil.redirect(process, System.err, System.out);
//            Process process = pb.start();
            int retcode = process.waitFor();
            System.err.flush();
            System.out.flush();
            return retcode==0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private List<Double> parseResult(final String tmpDirResult) {
        try {
            return IOUtil.readDoubleListAscii(tmpDirResult);
        } catch(IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private boolean readModel(final String tmpDirModel) {
        try {
            model = IOUtil.readStringListAscii(tmpDirModel);
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean writeModel(final String tmpDirModel) {
        try {
            IOUtil.writeCollectionAscii(model, tmpDirModel);
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    
}
