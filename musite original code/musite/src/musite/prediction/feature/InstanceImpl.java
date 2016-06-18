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

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Jianjiong Gao
 */
public class InstanceImpl implements Instance, Serializable {
    private static final long serialVersionUID = -8382160640127314807L;
    
    protected final Map<String, List<Double>> features;
    protected final InstanceTag tag;

    public InstanceImpl() {
        this(null);
    }

    public InstanceImpl(final InstanceTag tag) {
        this.tag = tag;
        features = new LinkedHashMap<String, List<Double>>(); // keep the order
    }

    /**
     *
     * @return
     */
    public List<Double> getFeatures() {
        List<Double> fs = new ArrayList<Double>();

        Collection<List<Double>> values = this.features.values();
        for (List<Double> f : values) {
            fs.addAll(f);
        }

        return fs;
    }

    public Map<String, List<Double>> getFeatureMap() {
        return features;
    }

    public List<Double> getFeatures(final Set<String> featureNames) {
        if (featureNames==null) {
            throw new NullPointerException();
        }

        if (!features.keySet().containsAll(featureNames)) {
            throw new java.lang.IllegalArgumentException("Wrong featureTags");
        }

        List<Double> fs = new ArrayList<Double>();
        for (String featureTag : featureNames) {
            List<Double> f = this.features.get(featureTag);
            fs.addAll(f);
        }

        return fs;
    }

    /**
     *
     * @param featureTag
     * @param features
     */
    public void putFeatures(final String featureName,
                                     final List<Double> feature) {
        if (featureName==null || feature==null) {
            throw new NullPointerException();
        }

        this.features.put(featureName, feature);
    }

    /**
     *
     * @param features
     */
    public void putFeatures(Map<String, List<Double>> features) {
        if (features==null)
            return;

        this.features.putAll(features);
    }

    /**
     *
     * @param featureTag
     * @return
     */
    public boolean removeFeatures(final String featureName) {
        if (featureName==null) {
            throw new NullPointerException();
        }

        Object rem = features.remove(featureName);
        return rem!=null;
    }

    /**
     *
     * @return
     */
    public void removeAllFeatures() {
        features.clear();
    }

    /**
     * 
     * @return
     */
    public InstanceTag getInstanceTag() {
        return tag;
    }

    /**
     *
     * @return
     */
    public Set<String> getFeatureNames() {
        return features.keySet();
    }
}
