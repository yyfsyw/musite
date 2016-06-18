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

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Jianjiong Gao
 */
public final class FeatureLinearNormalizer implements FeatureNormalizer, Serializable {
    private static final long serialVersionUID = -1515597963412629314L;

    private static class SerializationProxy implements Serializable {
        private static final long serialVersionUID = -3610424792357508196L;

        private final double uplimit;
        private final double downlimit;
        private double[] a;
        private double[] b;
        SerializationProxy(FeatureLinearNormalizer actualObj) {
            this.uplimit = actualObj.uplimit;
            this.downlimit = actualObj.downlimit;
            this.a = actualObj.a;
            this.b = actualObj.b;
        }

        private Object readResolve() {
            FeatureLinearNormalizer fn = new FeatureLinearNormalizer(downlimit, uplimit);
            fn.a = a;
            fn.b = b;
            return fn;
        }
    }

    private Object writeReplace() {
        return new SerializationProxy(this);
    }

    private void readObject(java.io.ObjectInputStream stream) throws java.io.InvalidObjectException {
        throw new java.io.InvalidObjectException("Proxy required");
    }
    
    private final double uplimit;
    private final double downlimit;
    private double[] a;
    private double[] b;

    public FeatureLinearNormalizer(final double downlimit,
                                   final double uplimit) {
        if (uplimit<=downlimit) {
            throw new IllegalArgumentException();
        }

        this.uplimit = uplimit;
        this.downlimit = downlimit;
    }

    public void normalize(final List<Double> features) {
        if (a==null) {
            throw new IllegalStateException("Run trainParameter first");
        }

        int n = features.size();
        if (n!=a.length) {
            throw new IllegalArgumentException();
        }

        for (int i=0; i<n; i++) {
            Double f = features.get(i);
            Double f_t = a[i]*f+b[i];
            features.set(i,f_t);
        }
    }

    public void trainParameter(final List<List<Double>> features) {
        if (features==null) {
            throw new NullPointerException();
        }

        int n = features.size();
        if (n==0) return;

        int len = features.get(0).size();
        double[] max = new double[len];
        Arrays.fill(max, Double.NEGATIVE_INFINITY);
        double[] min = new double[len];
        Arrays.fill(min, Double.POSITIVE_INFINITY);

        for (int i=0; i<n; i++) {
            List<Double> fs = features.get(i);
            if (fs.size()!=len) {
                throw new IllegalArgumentException();
            }

            for (int j=0; j<len; j++) {
                double f = fs.get(j);
                if (f>max[j]) {
                    max[j] = f;
                }
                if (f<min[j]) {
                    min[j] = f;
                }
            }
        }

        a = new double[len];
        b = new double[len];
        for (int j=0; j<len; j++) {
            a[j] = (max[j]-min[j])/(uplimit-downlimit);
            b[j] = max[j]-a[j]*uplimit;
        }
    }
    
}
