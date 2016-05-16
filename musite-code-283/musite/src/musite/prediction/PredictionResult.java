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

import java.util.Map;
import java.util.Set;

import musite.Proteins;

/**
 *
 * @author Jianjiong Gao
 */
public interface PredictionResult extends Proteins {

    public static final String ANNOTATION_FIELD_SCORE = "score";
    public static final String XML_FIELD_MODEL = "model";

    /**
     *
     * @return models.
     */
    public Set<PredictionModel> getModels();

    /**
     * 
     * @param model
     */
    public void addModel(PredictionModel model);

    /**
     *
     * @param model
     * @param accession
     * @return
     */
    public Map<Integer,Double> getPredictions(PredictionModel model, String accession);

    /**
     *
     * @param model
     * @param accession
     * @param site
     * @return
     */
    public double getPrediction(PredictionModel model, String accession, int site);

    /**
     * Get sites whose scores are large than or equal to the threshold
     * @param model
     * @param accession
     * @return
     */
    public Map<Integer,Double> getPredictedSites(PredictionModel model, String accession);

    /**
     *
     * @param model
     * @return
     */
    public double getMaxPredictionScore(PredictionModel model);

    /**
     *
     * @param model
     * @return
     */
    public double getMinPredictionScore(PredictionModel model);

    /**
     * 
     * @param model
     * @return
     */
    public double getThreshold(PredictionModel model);

    /**
     * 
     * @param model
     * @param accession
     * @param site
     * @param prediction
     */
    public void setPrediction(PredictionModel model, String accession, int site, double prediction);

    /**
     * 
     * @param threshold
     */
    public void setThreshold(PredictionModel model, double threshold);

}
