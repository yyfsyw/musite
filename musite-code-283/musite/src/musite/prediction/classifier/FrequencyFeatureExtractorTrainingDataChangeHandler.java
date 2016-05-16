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

package musite.prediction.classifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import musite.prediction.feature.AminoAcidFrequencyFeatureExtractor;
import musite.prediction.feature.Instance;
import musite.prediction.feature.InstanceUtil;

import musite.util.ProteinSequenceUtil;
import musite.util.SortUtil;

/**
 *
 * @author Jianjiong Gao
 */
public class FrequencyFeatureExtractorTrainingDataChangeHandler implements TrainingDataChangeHandler {
    protected final AminoAcidFrequencyFeatureExtractor frequencyFeatureExtractor;
    protected final int numberFrequencyFeatures;
    protected final int windowOffset;

    public FrequencyFeatureExtractorTrainingDataChangeHandler(
            final AminoAcidFrequencyFeatureExtractor frequencyFeatureExtractor,
            final int numberFrequencyFeatures,
            final int windowOffset) {
        this.frequencyFeatureExtractor = frequencyFeatureExtractor;
        this.numberFrequencyFeatures = numberFrequencyFeatures;
        this.windowOffset = windowOffset;
    }

    public void trainingDataChanged(List<Instance> positives, List<Instance> negatives) {
        if (positives==null || positives.isEmpty() || negatives==null || negatives.isEmpty()) {
            throw new IllegalArgumentException();
        }

        String surrPos = surrSeqConcat(positives);
        String surrNeg = surrSeqConcat(negatives);
        
        Map<Character,Double> frePos = ProteinSequenceUtil.aminoAcidFrequencies(surrPos);
        Map<Character,Double> freNeg = ProteinSequenceUtil.aminoAcidFrequencies(surrNeg);

        Set<Character> aas = new HashSet(20);
        aas.addAll(frePos.keySet());
        aas.addAll(freNeg.keySet());

        List<Character> list = new ArrayList(aas);

        final double eps = 10e-10;

        int nl = aas.size();
        List<Double> freScore = new ArrayList(nl);
        for (int i=0; i<nl; i++) {
            Character aa = list.get(i);

            double pos = frePos.get(aa);
            if (pos==0)
                pos += eps;

            double neg = freNeg.get(aa);
            if (neg==0)
                neg += eps;

            freScore.add(Math.abs(Math.log(pos)-Math.log(neg)));
        }

        List<Integer> idx = SortUtil.sortList(freScore, false);

        StringBuilder alphabet = new StringBuilder();
        for (int i=0; i<numberFrequencyFeatures; i++) {
            alphabet.append(list.get(idx.get(i)));
        }

        frequencyFeatureExtractor.setAminoAcidAlphabet(alphabet.toString());
    }

    private String surrSeqConcat(List<Instance> instances) {
        int n = instances.size();
        StringBuilder result = new StringBuilder(instances.size()*(2*windowOffset+1));
        for (int i=0; i<n; i++) {
            String sequence = InstanceUtil.extractSurroundingSequence(instances.get(i), windowOffset);
            if (sequence!=null) {
                StringBuilder sb = new StringBuilder(sequence.toUpperCase());
                sb.deleteCharAt(windowOffset); // remove the center
                result.append(sb);
            }
        }
        return result.toString();
    }
}
