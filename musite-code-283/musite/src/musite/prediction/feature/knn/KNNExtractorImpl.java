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

package musite.prediction.feature.knn;

import java.io.Serializable;

import java.util.List;

import musite.prediction.feature.Instance;

import musite.util.MultiTreeMap;
import musite.util.CollectionUtil;

/**
 *
 * @author Jianjiong Gao
 */
public final class KNNExtractorImpl implements KNNExtractor, Serializable {
    private static final long serialVersionUID = 1515666963108117172L;

    private static class SerializationProxy implements Serializable {
        private static final long serialVersionUID = -5132797107077034118L;

        private final ClosenessCalculator closenessCalculator;
        SerializationProxy(KNNExtractorImpl actualObj) {
            this.closenessCalculator = actualObj.closenessCalculator;
        }

        private Object readResolve() {
            return new KNNExtractorImpl(closenessCalculator);
        }
    }

    private Object writeReplace() {
        return new SerializationProxy(this);
    }

    private void readObject(java.io.ObjectInputStream stream) throws java.io.InvalidObjectException {
        throw new java.io.InvalidObjectException("Proxy required");
    }

    private ClosenessCalculator closenessCalculator;
    transient private List<Integer> sortedIndices;
    transient private List<Double> sortedCloseness;
    
    public KNNExtractorImpl(final ClosenessCalculator closenessCalculator) {
        if (closenessCalculator==null)
            throw new IllegalArgumentException();
        this.closenessCalculator = closenessCalculator;
    }

    public ClosenessCalculator getClosenessCalculator() {
        return closenessCalculator;
    }

    /**
     * 
     * @param instance
     * @param instances
     */
    public void setInstanceAndNeighbors(final Instance instance,
                                   final List<Instance> instances) {
        if (instance==null || instances==null) {
            throw new NullPointerException();
        }

        if (closenessCalculator instanceof UniSimMatrixSequenceClosenessCalculator) {
            UniSimMatrixSequenceClosenessCalculator cc = (UniSimMatrixSequenceClosenessCalculator)closenessCalculator;
            if (!cc.isMatrixSet()) { // note that we only set matrix for once
                int windowOffset = cc.getWindowOffset();
                cc.setSimilarityMatrix(SubstituteMatrixUtils.genSubstituteMatrix(instances, windowOffset));
            }
        }
        else if(closenessCalculator instanceof MultiSimMatrixSequenceClosenessCalculator) {
            MultiSimMatrixSequenceClosenessCalculator cc = (MultiSimMatrixSequenceClosenessCalculator)closenessCalculator;
            if (!cc.isMatrixSet()) { // note that we only set matrix for once
                int windowOffset = cc.getWindowOffset();
                SimilarityMatrix[] matrices = SubstituteMatrixUtils.genSubstituteMatrices(instances, windowOffset);
                double[][][] simMatrixByAscii = new double[2*windowOffset][128][128];
                for(int i = 0; i<2*windowOffset; i++)
                {
                    SubstituteMatrixUtils.SimilarityMatrixPrint(matrices[i]);
                    simMatrixByAscii[i] = MatrixUtils.reindexByASCii(matrices[i]);
                }
                cc.setSimilarityMatrix(simMatrixByAscii);                
            }
        }

        int N = instances.size();

        MultiTreeMap<Double,Integer> mmap = new MultiTreeMap<Double,Integer>();

        for (int i=0; i<N; i++) {
            Instance ins = instances.get(i);

            double closeness;
            if (instance.getInstanceTag().equals(ins.getInstanceTag())) {
                closeness = 0; // exclude self match
                               //TODO: is this the right logic
            } else {
                closeness = closenessCalculator.calculate(ins, instance);
            }
            mmap.add(1-closeness, i); // distance = 1-closeness
        }

        sortedIndices = mmap.allValues();
        sortedCloseness = mmap.allKeys();
    }

    /**
     *
     * @return
     */
    public List<Integer> getKNNIndices(final int K) {
        if (sortedIndices==null) {
            throw new java.lang.IllegalStateException();
        }
        return CollectionUtil.subList(sortedIndices, 0, K);
    }

    /**
     *
     * @return
     */
    public List<Integer> getKNNIndex() {
        if (sortedIndices==null) {
            throw new java.lang.IllegalStateException();
        }
        return sortedIndices;
    }

    /**
     *
     * @return
     */
    public List<Double> getKNNDistances(final int K)  {
        if (sortedIndices==null) {
            throw new java.lang.IllegalStateException();
        }
        return CollectionUtil.subList(sortedCloseness, 0, K);
    }

    /**
     *
     * @return
     */
    public List<Double> getKNNDistances()  {
        if (sortedIndices==null) {
            throw new java.lang.IllegalStateException();
        }

        return sortedCloseness;
    }

}
