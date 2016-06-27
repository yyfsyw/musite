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
import java.util.Collections;//"wrappers" and contains polymorphic algorithms
import java.util.List;

/**
 * One assumptions: if prediction>=threhold, then hit.
 * @author Jianjiong Gao
 */
public class SpecificityEstimatorImpl implements SpecificityEstimator, Serializable {
    private static final long serialVersionUID = 2276227520532317467L;//maybe for large range or error checking?

    private List<Double> trainingPredictions;

    public SpecificityEstimatorImpl(final List<Double> predictionsForTrainingNegativeData) {
        if (predictionsForTrainingNegativeData == null
                && predictionsForTrainingNegativeData.isEmpty())
            throw new IllegalArgumentException();
        trainingPredictions = new ArrayList(predictionsForTrainingNegativeData);
        Collections.sort(trainingPredictions);//sort into ascending order
    }

    public double specificity(double threshold) {
        int n = trainingPredictions.size();
        /*ix will get the index of the search key and it takes O(logn)
        if it is contained in the list; otherwise, (-(insertion point) - 1). 
        The insertion point is defined as the point at which the key would be inserted into the list: 
            ***the index of the first element greater than the key, 
            ***Or list.size() if all elements in the list are less than the specified key. 
        Note that this guarantees that the return value will be >= 0 if and only if the key is found.
        */
        int ix = Collections.binarySearch(trainingPredictions, threshold);
        if (ix>=0)
        { // exact match
        /*when it is exactly matched, find the "string" closer to the end of the total size*/
            while (ix<n-2 && trainingPredictions.get(ix)==trainingPredictions.get(ix+1))
                ix++;
            return 1.0*(ix+1)/n;//it's a way for accuracy estimation
        } 
        else 
        {
            if (ix==-1) 
            { // smaller than the min
                return 0.0;
            } 
        else if (ix==-n-1) 
            { // larger than the max
                return 1.0;
            } 
        else 
            {
            /*UNFINISH~~~*/
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
        if (specificity<1.0/n)//becasuse n(the size of string) should be greater than 1 => 1.0/n should less than 1.0
            return trainingPredictions.get(0)-Double.MIN_VALUE;// MIN_VALUE, which is 2^(-31)
        double d = n*specificity;
        double l = Math.floor(d);//\lfloor d \rfloor 
        double y1 = trainingPredictions.get((int)l-1);
        if (l==d) // sp=1.0 will be handled
            return y1;

        double y2 = trainingPredictions.get((int)l);

        return y1+(d-l)*(y2-y1);// similar to (x1,y1) & (x2,y2), then get ~~~
    }

    public List<Double> trainingPredictions() {
        return trainingPredictions;
    }
}
