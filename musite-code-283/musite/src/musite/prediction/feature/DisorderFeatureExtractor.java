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
import java.util.List;
import java.util.TreeSet;

import musite.Protein;

import musite.prediction.feature.disorder.DisorderUtil;

/**
 *
 * @author Jianjiong Gao
 */
public class DisorderFeatureExtractor implements FeatureExtractor, Serializable {
    private static final long serialVersionUID = -6518622392591723991L;

    private static class SerializationProxy implements Serializable {
        private static final long serialVersionUID = -5240970373521528265L;

        private final boolean useAverage;
        private final TreeSet<Integer> selectedWindowOffsets;
        public SerializationProxy(DisorderFeatureExtractor actualObj) {
            this.useAverage = actualObj.useAverage;
            this.selectedWindowOffsets = actualObj.selectedWindowOffsets;
        }

        private Object readResolve() {
            return new DisorderFeatureExtractor(selectedWindowOffsets, useAverage);
        }
    }

    private Object writeReplace() {
        return new SerializationProxy(this);
    }

    private void readObject(java.io.ObjectInputStream stream) throws java.io.InvalidObjectException {
        throw new java.io.InvalidObjectException("Proxy required");
    }

    private final boolean useAverage;
    private final TreeSet<Integer> selectedWindowOffsets;

    public DisorderFeatureExtractor (final int[] selectedWindowOffsets,
                                     final boolean useAverage) {

        if (selectedWindowOffsets==null) {
            throw new NullPointerException();
        }

        this.useAverage = useAverage;
        this.selectedWindowOffsets = new TreeSet<Integer>();
        for (int offset : selectedWindowOffsets) {
            this.selectedWindowOffsets.add(offset);
        }

    }

    public DisorderFeatureExtractor (final TreeSet<Integer> selectedWindowOffsets,
                                     final boolean useAverage) {

        if (selectedWindowOffsets==null) {
            throw new NullPointerException();
        }

        this.useAverage = useAverage;
        this.selectedWindowOffsets = selectedWindowOffsets;
    }

    /**
     * Extract features from protein at site and save to instance
     * @param protein
     * @param site
     * @param savetoInstance
     */
    public List<Double> extract(final Instance instance, final boolean save) {
        if (instance==null) {
            throw new NullPointerException();
        }

        InstanceTag tag = instance.getInstanceTag();
        if (!(tag instanceof ProteinResidueInstannceTag)) {
            return null;
        }

        ProteinResidueInstannceTag prTag = (ProteinResidueInstannceTag)tag;
        Protein protein = prTag.getProtein();
        int site = prTag.getPosition();

        List<Double> dis = DisorderUtil.extractDisorder(protein);
        if (dis==null)
            return null;

        int len = dis.size();
        int minOffset = selectedWindowOffsets.first();
        int maxOffset = selectedWindowOffsets.last();

        ArrayList<Double> features = new ArrayList<Double>();

        if (useAverage) {            
            if (minOffset<0) {
                throw new IndexOutOfBoundsException();
            }

            double sum = dis.get(site);
            if (selectedWindowOffsets.contains(0)) {
                features.add(sum);
            }

            int count = 1;
            for (int offset=1; offset<=maxOffset; offset++) {
                if (site-offset>=0) {
                    sum += dis.get(site-offset);
                    count++;
                }

                if (site+offset<len) {
                    sum += dis.get(site+offset);
                    count++;
                }

                if (selectedWindowOffsets.contains(offset)) {
                    double ave = sum/count;
                    features.add(ave);
                }
            }
        } else {
            for (int offset : selectedWindowOffsets) {
                if (site+offset<0) {
                    features.add(dis.get(0));
                } if (site+offset>=len) {
                    features.add(dis.get(len-1));
                } else {
                    features.add(dis.get(site+offset));
                }
            }
        }


        if (save) {
            instance.putFeatures(featureTag(), features);
        }

        return features;
    }

    public static final String TAG = "disorder";

    /**
     *
     * @return
     */
    public String featureTag() {
        return TAG;
    }
}
