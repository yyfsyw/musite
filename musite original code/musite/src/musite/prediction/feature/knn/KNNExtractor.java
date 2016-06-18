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

import java.util.List;

import musite.prediction.feature.Instance;

/**
 *
 * @author Jianjiong Gao
 */
public interface KNNExtractor {

    /**
     * 
     * @param instance
     * @param instances
     * @return
     */
    public void setInstanceAndNeighbors(Instance instance, List<Instance> instances);

    /**
     *
     * @return the indices of k-nearest neighbors sorted by closeness, nearest come first.
     */
    public List<Integer> getKNNIndices(int K);

    /**
     *
     * @return the indices of all neighbors sorted by closeness, nearest come first.
     */
    public List<Integer> getKNNIndex();

    /**
     *
     * @return the distances of k-nearest neighbors sorted by closeness, nearest come first.
     */
    public List<Double> getKNNDistances(int K);

    /**
     *
     * @return the distances of all neighbors sorted by closeness, nearest come first.
     */
    public List<Double> getKNNDistances();

}
