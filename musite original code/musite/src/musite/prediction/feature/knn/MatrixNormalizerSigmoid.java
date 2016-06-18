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
public class MatrixNormalizerSigmoid implements MatrixNormalizer, Serializable {
    private static final long serialVersionUID = -613765475885275086L;

        final double m;

        public MatrixNormalizerSigmoid(final double m) {
                this.m = m;
        }
        
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

                double[][] matrix_extend = extend(matrix,m);
                                
                double[][] return_this = new double[n][n];
                for (int r=0; r<n; r++) {
                        for (int c=0; c<n; c++) {
                                return_this[r][c] = logsigmoid(matrix_extend[r][c]);
                        }
                }
                
                return return_this;
        }

        protected double[][] extend(final double[][] matrix, double m) {
                int n = matrix.length;
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

                if (max<0 || min>0) {
                        throw new java.lang.IllegalStateException();
                }

                double[][] return_this = new double[n][n];
                for (int r=0; r<n; r++) {
                        for (int c=0; c<n; c++) {
                                double d = matrix[r][c];
                                if (d<0) {
                                        return_this[r][c] = d*m/max;
                                } else {
                                        return_this[r][c] = -d*m/min;
                                }
                        }
                }

                return return_this;
        }

        public double logsigmoid(double x) {
                return 1.0/(1+Math.exp(-x));
        }
}
