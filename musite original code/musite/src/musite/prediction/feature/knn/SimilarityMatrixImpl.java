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

/**
 *
 * @author Jianjiong Gao
 */
public final class SimilarityMatrixImpl implements SimilarityMatrix, Serializable {
    private static final long serialVersionUID = -568870291886104533L;

    private static class SerializationProxy implements Serializable {
        private static final long serialVersionUID = 4359644917962668748L;

        private double[][] matrix;
        private String alphabet;
        SerializationProxy(SimilarityMatrixImpl actualObj) {
            this.matrix = actualObj.matrix;
            this.alphabet = actualObj.alphabet;
        }

        private Object readResolve() {
            return new SimilarityMatrixImpl(matrix,alphabet);
        }
    }

    private Object writeReplace() {
        return new SerializationProxy(this);
    }

    private void readObject(java.io.ObjectInputStream stream) throws java.io.InvalidObjectException {
        throw new java.io.InvalidObjectException("Proxy required");
    }
        
    private double[][] matrix;
    private String alphabet;

    public SimilarityMatrixImpl(final double[][] matrix,
                                final String alphabet) {
        if (matrix==null || alphabet==null) {
                throw new java.lang.NullPointerException();
        }
        if (matrix.length!=alphabet.length()) {
                throw new java.lang.IllegalArgumentException("matrix and alphabet has different size");
        }

        this.matrix = matrix;

        this.alphabet = alphabet;
    }

    public double[][] getMatrix() {
           return matrix;
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    public String getAlphabet() {
            return alphabet;
    }

    public void setAlphabet(String alphabet) {
        this.alphabet = alphabet;
    }
}
