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

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import musite.prediction.feature.Instance;

import musite.util.SamplingUtil;

/**
 *
 * @author Jianjiong Gao
 */
public final class BootstrapBinaryClassifier implements BinaryClassifier, Serializable {
    private static final long serialVersionUID = -3743058559473448205L;

    private static class SerializationProxy implements Serializable {
        private static final long serialVersionUID = -5345107617243742042L;
        private BinaryClassifier actualClassifier;
        SerializationProxy(BootstrapBinaryClassifier actualObj) {
            this.actualClassifier = actualObj.actualClassifier;
        }

        private Object readResolve() {
            return new BootstrapBinaryClassifier(actualClassifier,1);
        }
    }

    private Object writeReplace() {
        return new SerializationProxy(this);
    }

    private void readObject(java.io.ObjectInputStream stream) throws java.io.InvalidObjectException {
        throw new java.io.InvalidObjectException("Proxy required");
    }
    
    private final BinaryClassifier actualClassifier;

    // no use for classification
    transient private final int nboots_pos;
    transient private final int nboots_neg;
    transient private final List<TrainingDataChangeHandler> trainingDataChangeHandlers;

    public BootstrapBinaryClassifier(final BinaryClassifier actualClassifier,
                                     final int nboots) {
        this(actualClassifier, nboots, nboots);
    }

    public BootstrapBinaryClassifier(final BinaryClassifier actualClassifier,
                                     final int nboots_pos,
                                     final int nboots_neg) {
        if (actualClassifier==null) {
            throw new java.lang.NullPointerException();
        }

        this.actualClassifier = actualClassifier;
        this.nboots_pos = nboots_pos;
        this.nboots_neg = nboots_neg;
        trainingDataChangeHandlers = new ArrayList();
    }

    public void addTrainingDataChangeHandler(final TrainingDataChangeHandler handler) {
        if (handler==null) {
            return;
        }
        
        trainingDataChangeHandlers.add(handler);
    }

    /**
     *
     * @param instances
     * @return a List of predicted values
     */
    public List<Double> classify(List<Instance> instances) {
        return actualClassifier.classify(instances);
    }

    /**
     *
     * @param instances
     * @param label
     * @return
     */
    public boolean train(List<Instance> positives, List<Instance> negatives) {
        if (positives==null || positives.isEmpty() || negatives==null || negatives.isEmpty()) {
            throw new IllegalArgumentException();
        }

        List<Instance> posIns = SamplingUtil.resampleWithReplacement(positives, nboots_pos);
        List<Instance> negIns = SamplingUtil.resampleWithReplacement(negatives, nboots_neg);

        for (TrainingDataChangeHandler handler : trainingDataChangeHandlers) {
            handler.trainingDataChanged(posIns, negIns);
        }

        return actualClassifier.train(posIns, negIns);
    }
}
