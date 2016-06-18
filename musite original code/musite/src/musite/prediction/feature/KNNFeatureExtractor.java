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

package musite.prediction.feature;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import musite.ProteinImpl;

import musite.prediction.feature.knn.ClosenessCalculator;
import musite.prediction.feature.knn.KNNExtractor;
import musite.prediction.feature.knn.KNNExtractorImpl;
import musite.prediction.feature.knn.UniSimMatrixSequenceClosenessCalculator;
import musite.prediction.feature.knn.MultiSimMatrixSequenceClosenessCalculator;

/**
 *
 * @author Jianjiong Gao
 */
public final class KNNFeatureExtractor
        implements FeatureExtractor, Serializable{
    private static final long serialVersionUID = 1321845359940459986L;

    private static class SerializationProxy implements Serializable {
        private static final long serialVersionUID = -3057553486106473991L;

        private final List<Instance> instances;
        private final int nPositive;
        private final TreeSet<Integer> Ks;
        private final KNNExtractor knnExtractor;
        SerializationProxy(KNNFeatureExtractor actualObj) {
            this.nPositive = actualObj.nPositive;
            this.Ks = actualObj.Ks;
            this.knnExtractor = actualObj.knnExtractor;
            if (knnExtractor instanceof KNNExtractorImpl) {
                ClosenessCalculator cc = ((KNNExtractorImpl)knnExtractor).getClosenessCalculator();
                if (cc instanceof UniSimMatrixSequenceClosenessCalculator) {
                    int windowOffset = ((UniSimMatrixSequenceClosenessCalculator)cc).getWindowOffset();
                    this.instances = rearrageInstances(actualObj.instances, windowOffset);
                } else if (cc instanceof MultiSimMatrixSequenceClosenessCalculator) {
                    int windowOffset = ((MultiSimMatrixSequenceClosenessCalculator)cc).getWindowOffset();
                    this.instances = rearrageInstances(actualObj.instances, windowOffset);
                } else {
                    this.instances = actualObj.instances;
                }
            } else {
                this.instances = actualObj.instances;
            }
        }

        // rearrange instances to save memory
        private List<Instance> rearrageInstances(final List<Instance> instances, final int windowOffset) {
            ProteinImpl newProtein = new ProteinImpl("p",null,null,null,null);//made up protein

            int width = 2*windowOffset+1;

            int n = instances.size();
            StringBuilder newProteinSeq = new StringBuilder(n*width);
            List<Instance> newInstances = new ArrayList(n);

            for (int i=0; i<n; i++) {
                // old instance and tag
                Instance ins = instances.get(i);

                InstanceTag tag = ins.getInstanceTag();
                if (!(tag instanceof ProteinResidueInstannceTag)) {
                    return null;
                }

                // new seq
                String seq = InstanceUtil.extractSurroundingSequence(ins, windowOffset);
                newProteinSeq.append(seq);

                // new tag
                ProteinResidueInstannceTag newTag = new ProteinResidueInstannceTagImpl(
                        newProtein, i*width+windowOffset);

                // new instance
                Instance newIns = new InstanceImpl(newTag);

                // TODO: do we need to copy the features? not for sequence-based knn.

                newInstances.add(newIns);
            }

            newProtein.setSequence(newProteinSeq.toString());

            return newInstances;
        }

        private Object readResolve() {
            KNNFeatureExtractor kfe = new KNNFeatureExtractor(Ks, knnExtractor);
            kfe.instances = instances;
            kfe.nPositive = nPositive;
            return kfe;
        }
    }

    private Object writeReplace() {
        return new SerializationProxy(this);
    }

    private void readObject(java.io.ObjectInputStream stream) throws java.io.InvalidObjectException {
        throw new java.io.InvalidObjectException("Proxy required");
    }
    
    private List<Instance> instances;
    private int nPositive;
    private final TreeSet<Integer> Ks;
    private final KNNExtractor knnExtractor;

    public KNNFeatureExtractor(final int[] Ks,
                               final KNNExtractor knnExtractor) {
        if (Ks==null || knnExtractor==null) {
            throw new NullPointerException();
        }

        if (Ks.length==0) {
            throw new IllegalArgumentException();
        }

        this.Ks = new TreeSet<Integer>();
        for (int K : Ks) {
            this.Ks.add(K);
        }

        this.knnExtractor = knnExtractor;
    }

    public KNNFeatureExtractor(final TreeSet<Integer> Ks,
                               final KNNExtractor knnExtractor) {
        if (Ks==null || knnExtractor==null) {
            throw new NullPointerException();
        }

        if (Ks.isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.Ks = Ks;
        this.knnExtractor = knnExtractor;
    }

    public void setTrainingData(final List<Instance> positives, final List<Instance> negatives) {
        if (positives==null || positives.isEmpty() || negatives==null || negatives.isEmpty()) {
            throw new IllegalArgumentException();
        }

        int n1 = positives.size();
        int n2 = negatives.size();

        int minK = this.Ks.first();
        int maxK = this.Ks.last();
        if (minK<=0 || maxK>n1+n2) {
            throw new IndexOutOfBoundsException();
        }

        instances = new ArrayList(n1+n2);
        instances.addAll(positives);
        instances.addAll(negatives);

        nPositive = n1;
    }


    /**
     * 
     * @param saveToInstance
     * @param instances
     * @param K
     */
    public List<Double> extract(final Instance instance, final boolean save) {
        if (instance==null) {
            throw new NullPointerException();
        }

        if (instances==null) {
            throw new IllegalStateException("Set training data first");
        }

        List<Double> features = new ArrayList<Double>();

        knnExtractor.setInstanceAndNeighbors(instance, instances);
        List<Integer> indices = knnExtractor.getKNNIndex();
        //List<Double> distances = knnExtractor.getKNNDistance();

        int count = 0;
        int prek = 0;
        for (int k : Ks) {
            for (int i=prek; i<k;i++){
                if (indices.get(i)<nPositive)
                    count ++ ;
            }

            features.add(count*1.0/k);
            prek = k;
        }

        if (save) {
            instance.putFeatures(featureTag(), features);
        }

        return features;
    }

    public static final String TAG = "knn";
    public String featureTag() {
        return TAG;
    }
}
