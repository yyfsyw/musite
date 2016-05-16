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

package musite;

import cytoscape.task.TaskMonitor;

import java.io.IOException;

import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import musite.prediction.classifier.BinaryClassifier;
import musite.prediction.PredictionModel;
import musite.prediction.PredictionResult;
import musite.prediction.PredictionResultImpl;

import musite.prediction.feature.InstancesExtractorFromProteins;
import musite.prediction.feature.InstancesExtractorFromProteins.ExtractOption;
import musite.prediction.feature.Instance;
import musite.prediction.feature.InstanceFilter;
import musite.prediction.feature.ProteinResidueInstannceTag;
import musite.prediction.feature.OffsetInstanceFilter;

import musite.util.AminoAcid;
import musite.util.CollectionUtil;

/**
 *
 * @author Jianjiong Gao
 */
public class MusiteClassify {
    
    private TaskMonitor monitor;
    private boolean selectivePrediction = false;
    private int jobSize = -1;

    private PredictionResult result;

    public MusiteClassify() {
        this(null);
    }

    public MusiteClassify(PredictionResult result) {
        this.result = result;
    }
    
    public void setSelectivePrediction(boolean selectivePrediction) {
        this.selectivePrediction = selectivePrediction;
    }

//    public void setKeepSequenceInResult(boolean keepSeqInRes) {
//        this.keepSeqInRes = keepSeqInRes;
//    }

    public void setJobSize(int jobSize) {
        this.jobSize = jobSize;
    }

    public void setTaskMonitor(TaskMonitor monitor) {
        this.monitor = monitor;
    }

    private void setMonitorStatus(String status) {
        if (monitor!=null) {
            monitor.setStatus(status);
        }
    }

    private void setMonitorStatus(String status, int perc) {
        if (monitor!=null) {
            monitor.setStatus(status);
            monitor.setPercentCompleted(perc);
        }
    }

    public void interrupt() {

    }

    public PredictionResult classify(final PredictionModel model,
             Proteins proteins) throws IOException {
        if (model==null || proteins==null)
            throw new IllegalArgumentException();

        final Set<AminoAcid> aminoAcids = model.getSupportedAminoAcid();
        Properties props = model.getModelProperties();

        int maxOffset = 0;

        // knn param
        boolean useKNN = props.getProperty(MusiteInit.TRAINING_PROPS_USE_KNN_FEATURES).equalsIgnoreCase("true");
        int knnWindowOffset = Integer.parseInt(props.getProperty(MusiteInit.TRAINING_PROPS_KNN_WINDOW_SIZE))/2;
        if (useKNN && knnWindowOffset>maxOffset) {
            maxOffset = knnWindowOffset;
        }

        // disorder param
        boolean useDisorder = props.getProperty(MusiteInit.TRAINING_PROPS_USE_DISORDER_FEATURES).equalsIgnoreCase("true");
        String strDisWindows = props.getProperty(MusiteInit.TRAINING_PROPS_DISORDER_WINDOW_SIZES);
        String[] strs = strDisWindows.split(",");
        int[] disWindowOffsets = new int[strs.length];
        for (int i=0; i<strs.length; i++) {
            disWindowOffsets[i] = Integer.parseInt(strs[i].trim())/2;
            if (useDisorder && disWindowOffsets[i]>maxOffset) {
                maxOffset = disWindowOffsets[i];
            }
        }

        // frequency param
        boolean useFrequency = props.getProperty(MusiteInit.TRAINING_PROPS_USE_FREQUENCY_FEATURES).equalsIgnoreCase("true");
        int frequencyWindow = Integer.parseInt(props.getProperty(MusiteInit.TRAINING_PROPS_FREQUENCY_WINDOW_SIZE))/2;
        if (useFrequency && frequencyWindow>maxOffset) {
            maxOffset = frequencyWindow;
        }

        PredictionResult ret = result==null?new PredictionResultImpl():result;

        ret.addAll(proteins, true,
                CollectionUtil.getSet(Protein.ACCESSION, Protein.SEQUENCE, Protein.SYMBOL, Protein.DESCRIPTION, Protein.ORGANISM),
                Proteins.ConfictHandleOption.SKIP);
        ret.addModel(model);
        
        int njob = (proteins.proteinCount()+jobSize-1)/jobSize;

        int insCount = 0;

        InstancesExtractorFromProteins insExtractor = new InstancesExtractorFromProteins(proteins, aminoAcids);
        if (selectivePrediction) {
            insExtractor.setSitesExtractor(new InstancesExtractorFromProteins.SitesExtractor() {
                public Set<Integer> extract(Protein protein) {
                    Object obj = protein.getInfo("query");
                    if (obj==null || !(obj instanceof Collection))
                        return null;

                    String sequence = protein.getSequence().toUpperCase();

                    Set<Integer> sites = new TreeSet();
                    for (Object o : (Collection)obj) {
                        if (o instanceof Integer) {
                            if (aminoAcids.contains(AminoAcid.of(sequence.charAt((Integer)o)))) {
                                sites.add((Integer)o);
                            }
                        }
                    }

                    return sites;
                }
            });
        } else {
            insExtractor.setExtractOption(ExtractOption.ALL_SITES, null);
        }

        boolean predictTerminal = props.getProperty(MusiteInit.TRAINING_PROPS_PADDING_TERMINALS).equalsIgnoreCase("true");
        final InstanceFilter insFilter = predictTerminal?null:new OffsetInstanceFilter(maxOffset);
        insExtractor.setInstanceFilter(insFilter);
        
        for (int ijob=0; insExtractor.hasMore(); ijob++) {
            // extract instances
            System.out.println("Predicting...");
            int total = proteins.proteinCount();
            int start = ijob*jobSize+1;
            int end = (ijob+1)*jobSize;
            if (end>total)
                end = total;
            setMonitorStatus("Predicting sites in "+start+"-"+end+" of "+total+" proteins.", jobSize==-1?-1:(int)(ijob*100.0/njob));
            List<Instance> instances_unlabeled = insExtractor.fetch(jobSize);

            BinaryClassifier classifier = model.getClassifier();
            List<Double> prediction = classifier.classify(instances_unlabeled);

            int nins = prediction.size();
            insCount += nins;
            for (int i=0; i<nins; i++) {
                Instance ins = instances_unlabeled.get(i);
                ProteinResidueInstannceTag tag = (ProteinResidueInstannceTag)ins.getInstanceTag();
                String acc = tag.getProtein().getAccession();
                int site = tag.getPosition();

                Double pred = prediction.get(i);
                if (!pred.isInfinite())
                    ret.setPrediction(model, acc, site, pred);
            }
        }

        System.out.println("Predicted for "+insCount+" sites.");

        return ret;
    }

}
