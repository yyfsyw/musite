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

import java.util.List;


import musite.util.ProteinSequenceUtil;


/**
 *
 * @author LucasYao
 */
public class AminoAcidBinaryFeatureExtractor implements FeatureExtractor, Serializable {

        private static final long serialVersionUID = -6507052020284269319L;

    private static class SerializationProxy implements Serializable {
        private static final long serialVersionUID = 5058130718254945368L;
        private String aminoAcidAlphabet;
        private final int windowOffset;
        SerializationProxy(AminoAcidBinaryFeatureExtractor actualObj) {
            this.aminoAcidAlphabet = actualObj.aminoAcidAlphabet;
            this.windowOffset = actualObj.windowOffset;
        }

        private Object readResolve() {
            return new AminoAcidBinaryFeatureExtractor(windowOffset, aminoAcidAlphabet);
        }
    }

    private Object writeReplace() {
        return new SerializationProxy(this);
    }

    private void readObject(java.io.ObjectInputStream stream) throws java.io.InvalidObjectException {
        throw new java.io.InvalidObjectException("Proxy required");
    }

    private String aminoAcidAlphabet;
    private final int windowOffset;

    public AminoAcidBinaryFeatureExtractor(final int windowOffset) {
        this(windowOffset, null);
    }

    public AminoAcidBinaryFeatureExtractor(final int windowOffset,
                                              final String aminoAcidAlphabet) {
        if (windowOffset<0) {
            throw new IllegalArgumentException("Window size cannot be negative.");
        }

        if (aminoAcidAlphabet==null) {
            this.aminoAcidAlphabet = ProteinSequenceUtil.ALPHABET+"*";
        } else {
            this.aminoAcidAlphabet = aminoAcidAlphabet;
        }

        this.windowOffset = windowOffset;
    }

    public void setAminoAcidAlphabet(String aminoAcidAlphabet) {
        if (aminoAcidAlphabet==null) {
            throw new NullPointerException();
        }

        this.aminoAcidAlphabet = aminoAcidAlphabet;
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

        String sequence = InstanceUtil.extractSurroundingSequence(instance,
                windowOffset, true);
        if (sequence==null)
            return null;

        StringBuilder sb = new StringBuilder(sequence);
        sb.deleteCharAt(windowOffset); // remove the center

        List<Double> features = ProteinSequenceUtil.aminoAcidBinaries(
                sb.toString(), aminoAcidAlphabet);

        if (save) {
            instance.putFeatures(featureTag(), features);
        }

        return features;
    }

    public static final String TAG = "binary";

    /**
     *
     * @return
     */
    public String featureTag() {
        return TAG;
    }

}
