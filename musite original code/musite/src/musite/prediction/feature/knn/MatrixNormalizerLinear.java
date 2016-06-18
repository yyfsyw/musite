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
public class MatrixNormalizerLinear implements MatrixNormalizer, Serializable {
    private static final long serialVersionUID = 7949339928081705416L;
        
        public double[][] normalize(final double[][] matrix) {
                if (matrix==null) {
                        throw new java.lang.NullPointerException();
                }
                
                int n = matrix.length;
                if (n==0) {
                        return matrix;
                }
                
                if (n!=matrix[0].length) {
                        throw new java.lang.IllegalArgumentException("'"+matrix+"' is non-square");
                }
                
                double min = matrix[0][0];
                double max = min;
                
                for (int r=0; r<n; r++) {
                        for (int c=0; c<n; c++) {
                                double d = matrix[r][c];
                                if (d<min) {
                                        min = d;
                                } else if (d>max) {
                                        max = d;
                                }
                        }
                }
                
                double[][] return_this = new double[n][n];
                for (int r=0; r<n; r++) {
                        for (int c=0; c<n; c++) {
                                return_this[r][c] = (matrix[r][c]-min)/(max-min);
                        }
                }
                
                return return_this;
        }
}
