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

import java.util.List;
import java.util.ArrayList;

import musite.prediction.feature.Instance;

/**
 *
 * @author Jianjiong Gao
 */
public final class AverageBoostingBinaryClassifier implements BoostingBinaryClassifier, Serializable {
    private static final long serialVersionUID = 4961850812349688572L;

    private static class SerializationProxy implements Serializable {
        private static final long serialVersionUID = 443234430414627487L;
        private final List<BinaryClassifier> classifiers;
        SerializationProxy(AverageBoostingBinaryClassifier actualObj) {
            if (actualObj.classifiers.isEmpty())
                throw new IllegalStateException("No classifier was added");
            this.classifiers = actualObj.classifiers;
        }

        private Object readResolve() {
            return new AverageBoostingBinaryClassifier(classifiers);
        }
    }

    private Object writeReplace() {
        return new SerializationProxy(this);
    }

    private void readObject(java.io.ObjectInputStream stream) throws java.io.InvalidObjectException {
        throw new java.io.InvalidObjectException("Proxy required");
    }

    private final List<BinaryClassifier> classifiers;

    public AverageBoostingBinaryClassifier() {
        this(new ArrayList());
    }

    public AverageBoostingBinaryClassifier(final List<BinaryClassifier> classifiers) {
        if (classifiers==null) {
            throw new NullPointerException();
        }
        
        this.classifiers = classifiers;
    }

    /**
     *
     * @param instances
     * @return a List of predicted values
     */
    public List<Double> classify(final List<Instance> instances) {
        if (instances==null) {
            throw new NullPointerException();
        }
        
        if (classifiers.isEmpty()) {
            throw new IllegalStateException("No classifier was added");
        }
        
        int ni = instances.size();
        
        int nc = classifiers.size();
        List<Double>[] res = new List[nc];

        for (int i=0; i<nc; i++) {
            BinaryClassifier classifier = classifiers.get(i);
            res[i] = classifier.classify(instances);
        }

        List<Double> ret = new ArrayList(ni);
        for (int i=0; i<ni; i++) {
            double sum = 0;
            for (int j=0; j<nc; j++) {
                sum += res[j].get(i);
            }
            ret.add(sum/nc);
        }

        return ret;
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

        if (classifiers==null) {
            throw new java.lang.IllegalStateException("Add at least one classifier first");
        }

        for (BinaryClassifier classifier : classifiers) {
            if (!classifier.train(positives, negatives)) {
                return false;
            }
        }

        return true;
    }


    /**
     *
     * @return
     */
    public boolean addClassifier(final BinaryClassifier classifier) {
        if (classifier==null) {
            throw new NullPointerException();
        }

        return classifiers.add(classifier);
    }

    /**
     *
     * @return
     */
    public boolean removeClassifier(final BinaryClassifier classifier) {
        if (classifier==null) {
            throw new NullPointerException();
        }

        return classifiers.remove(classifier);
    }

    /**
     *
     */
    public void removeAllClassifiers() {
        classifiers.clear();
    }
}
