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

import java.util.List;

import musite.prediction.feature.KNNFeatureExtractor;
import musite.prediction.feature.Instance;

/**
 *
 * @author Jianjiong Gao
 */
public class KNNFeatureExtractorTrainingDataChangeHandler implements TrainingDataChangeHandler {
    protected final KNNFeatureExtractor knnFeatureExtractor;

    public KNNFeatureExtractorTrainingDataChangeHandler(final KNNFeatureExtractor knnFeatureExtractor) {
        this.knnFeatureExtractor = knnFeatureExtractor;
    }

    public void trainingDataChanged(List<Instance> positives, List<Instance> negatives) {
        knnFeatureExtractor.setTrainingData(positives, negatives);
    }
}
