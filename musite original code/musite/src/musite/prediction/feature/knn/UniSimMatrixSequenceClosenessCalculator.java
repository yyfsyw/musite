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

import musite.prediction.feature.Instance;
import musite.prediction.feature.InstanceUtil;

/**
 *
 * @author Jianjiong Gao
 */
public final class UniSimMatrixSequenceClosenessCalculator implements ClosenessCalculator, Serializable {
    private static final long serialVersionUID = -82979909064557117L;

    private static class SerializationProxy implements Serializable {
        private static final long serialVersionUID = 4156745873462469437L;

    private final double[][] matrix;
    private final int windowOffset;
        SerializationProxy(UniSimMatrixSequenceClosenessCalculator actualObj) {
            this.matrix = actualObj.matrix;
            this.windowOffset = actualObj.windowOffset;
        }

        private Object readResolve() {
            return new UniSimMatrixSequenceClosenessCalculator(matrix, windowOffset);
        }
    }

    private Object writeReplace() {
        return new SerializationProxy(this);
    }

    private void readObject(java.io.ObjectInputStream stream) throws java.io.InvalidObjectException {
        throw new java.io.InvalidObjectException("Proxy required");
    }
    
    private double[][] matrix;
    private final int windowOffset;

    /**
     * Adaptive window
     * @param windowOffset
     */
    public UniSimMatrixSequenceClosenessCalculator(final int windowOffset) {
        this((double[][])null, windowOffset);
    }

    public UniSimMatrixSequenceClosenessCalculator(final SimilarityMatrix simMatrix,
            final int windowOffset) {
        this(MatrixUtils.reindexByASCii(simMatrix), windowOffset);
    }

    public UniSimMatrixSequenceClosenessCalculator(final double[][] simMatrixByAscii,
            final int windowOffset) {
        if (simMatrixByAscii!=null && (simMatrixByAscii.length!=128
                || simMatrixByAscii[0].length!=128)) {
            throw new IllegalArgumentException();
        }

        if (windowOffset<=0) {
            throw new IllegalArgumentException();
        }

        this.matrix = simMatrixByAscii;
        this.windowOffset = windowOffset;
    }

    public int getWindowOffset() {
        return windowOffset;
    }

    public boolean isMatrixSet() {
        return matrix != null;
    }

    public void setSimilarityMatrix(final SimilarityMatrix simMatrix) {
        if (simMatrix==null)
            this.matrix = null;
        this.matrix = MatrixUtils.reindexByASCii(simMatrix);
    }

    /**
     * 
     * @param instance1
     * @param instance2
     * @return
     */
    public double calculate(final Instance instance1,
                            final Instance instance2) {
        if (matrix==null) {
            throw new java.lang.IllegalStateException("Set matrix first!");
        }

        if (instance1==null || instance2==null) {
            throw new java.lang.NullPointerException();
        }
       
        String seq1 = InstanceUtil.extractSurroundingSequence(instance1, 
                windowOffset, true);

        String seq2 = InstanceUtil.extractSurroundingSequence(instance2, 
                windowOffset, true);

        if (seq1==null || seq2==null)
            throw new IllegalArgumentException();

        double return_this = 0;
        for (int i=0; i<windowOffset; i++) {
            // ignore the center
                return_this += matrix[seq1.charAt(i)][seq2.charAt(i)];
                return_this += matrix[seq1.charAt(windowOffset+i+1)][seq2.charAt(windowOffset+i+1)];
        }

        return_this /= 2*windowOffset;

        return return_this;
    }
}
