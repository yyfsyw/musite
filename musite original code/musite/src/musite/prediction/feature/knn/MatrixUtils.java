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

import java.io.IOException;

import java.util.List;

import musite.util.IOUtil;

/**
 *
 * @author Jianjiong Gao
 */
public class MatrixUtils {
        
        /**
         * Reindex a matrix index by ascii
         * @param matrix
         * @param alphabet
         * @return
         */
        public static double[][] reindexByASCii(SimilarityMatrix simMatrix) {
                if (simMatrix==null) {
                        throw new java.lang.NullPointerException();
                }
                
                double[][] return_this = new double[128][128];
                for (int i=0; i<128; i++) {
                        for (int j=0; j<128; j++) {
                                return_this[i][j] = Double.NaN;
                        }
                }
                
                double[][] matrix = simMatrix.getMatrix();
                char[] alphabetUpper = simMatrix.getAlphabet().toUpperCase().toCharArray();
                char[] alphabetLower = simMatrix.getAlphabet().toLowerCase().toCharArray();
                int n = alphabetUpper.length;
                for (int i=0; i<n; i++) {
                        for (int j=0; j<n; j++) {
                                return_this[alphabetUpper[i]][alphabetUpper[j]] = matrix[i][j];
                                return_this[alphabetUpper[i]][alphabetLower[j]] = matrix[i][j];
                                return_this[alphabetLower[i]][alphabetUpper[j]] = matrix[i][j];
                                return_this[alphabetLower[i]][alphabetLower[j]] = matrix[i][j];
                        }
                }
                
                return return_this;
        }
        
        public static void normalizeMatrix(SimilarityMatrix matrix, MatrixNormalizer normalizer) {
            if (matrix==null)
                return;
            
            matrix.setMatrix(normalizer.normalize(matrix.getMatrix()));
        }

        public static SimilarityMatrix readBLASTMatrix(String dir) throws IOException {
            if (dir==null)
                return null;

            List<String> lines = IOUtil.readStringListAscii(dir);

            int startLine = 0;

            while (lines.get(startLine).startsWith("#"))
                startLine++;

            String alphabet = lines.get(startLine).replaceAll(" +", "");

            startLine++;

            int n = alphabet.length();

            double[][] matrix = new double[n][n];

            for (int i=0; i<n; i++) {
                String line = lines.get(startLine+i);
                String[] strs = line.split(" +");
                for (int j=0; j<n; j++) {
                    matrix[i][j] = Double.parseDouble(strs[j+1]);
                }
            }

            return new SimilarityMatrixImpl(matrix, alphabet);
        }
}
