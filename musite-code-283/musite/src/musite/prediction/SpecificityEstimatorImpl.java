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

package musite.prediction;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * One assumptions: if prediction>=threhold, then hit.
 * @author Jianjiong Gao
 */
public class SpecificityEstimatorImpl implements SpecificityEstimator, Serializable {
    private static final long serialVersionUID = 2276227520532317467L;

    private List<Double> trainingPredictions;

    public SpecificityEstimatorImpl(final List<Double> predictionsForTrainingNegativeData) {
        if (predictionsForTrainingNegativeData == null
                && predictionsForTrainingNegativeData.isEmpty())
            throw new IllegalArgumentException();
        trainingPredictions = new ArrayList(predictionsForTrainingNegativeData);
        Collections.sort(trainingPredictions);
    }

    public double specificity(double threshold) {
        int n = trainingPredictions.size();
        int ix = Collections.binarySearch(trainingPredictions, threshold);
        if (ix>=0) { // exact match
            while (ix<n-2 && trainingPredictions.get(ix)==trainingPredictions.get(ix+1))
                ix++;
            return 1.0*(ix+1)/n;
        } else {
            if (ix==-1) { // smaller than the min
                return 0.0;
            } else if (ix==-n-1) { // larger than the max
                return 1.0;
            } else {
                double x1 = trainingPredictions.get(-ix-2);
                double x2 = trainingPredictions.get(-ix-1);
                return (-ix-((x2-threshold)/(x2-x1)))/n;
            }
        }
    }

    public double threshold(double specificity) {
        if (specificity<0 || specificity>1)
            throw new java.lang.IllegalArgumentException("specificity should be between 0 and 1.");
        int n = trainingPredictions.size();
        if (specificity<1.0/n)
            return trainingPredictions.get(0)-Double.MIN_VALUE;
        double d = n*specificity;
        double l = Math.floor(d);
        double y1 = trainingPredictions.get((int)l-1);
        if (l==d) // sp=1.0 will be handled
            return y1;

        double y2 = trainingPredictions.get((int)l);

        return y1+(d-l)*(y2-y1);
    }

    public List<Double> trainingPredictions() {
        return trainingPredictions;
    }
}
