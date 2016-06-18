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

package musite.prediction.feature;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Jianjiong Gao
 */
public interface Instance {

    /**
     * Get all numerical features
     * @return
     */
    public List<Double> getFeatures();

    /**
     * 
     * @return
     */
    public List<Double> getFeatures(Set<String> featureNames);

    /**
     * 
     * @return
     */
    public Map<String, List<Double>> getFeatureMap();

    /**
     *
     * @param featureTag
     * @param features
     */
    public void putFeatures(String featureName, List<Double> features);

    /**
     *
     * @param features
     */
    public void putFeatures(Map<String, List<Double>> features);

    /**
     *
     * @param featureTag
     * @return
     */
    public boolean removeFeatures(String featureName);

    /**
     *
     * @return
     */
    public void removeAllFeatures();

    /**
     * 
     * @return
     */
    public InstanceTag getInstanceTag();

    /**
     *
     * @return
     */
    public Set<String> getFeatureNames();
}
