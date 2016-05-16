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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import musite.Protein;
import musite.ProteinsImpl;

import musite.PTMAnnotationUtil;
import musite.ResidueAnnotationUtil.AnnotationFilter;

import musite.util.MultiMap;

/**
 *
 * @author Jianjiong Gao
 */
public class PredictionResultImpl extends ProteinsImpl implements PredictionResult {

    private Map<PredictionModel, Double> mapModelThreshold;
    private Map<PredictionModel, Double> mapModelMinScore;
    private Map<PredictionModel, Double> mapModelMaxScore;

    public PredictionResultImpl() {
        mapModelThreshold = new HashMap();
        mapModelMinScore = new HashMap();
        mapModelMaxScore = new HashMap();
    }

    public Set<PredictionModel> getModels() {
        return mapModelThreshold.keySet();
    }

    public void addModel(PredictionModel model) {
        if (model==null)
            throw new IllegalArgumentException();
        mapModelThreshold.put(model, Double.POSITIVE_INFINITY);
        mapModelMinScore.put(model, Double.POSITIVE_INFINITY);
        mapModelMaxScore.put(model, Double.NEGATIVE_INFINITY);
    }

    public Map<Integer,Double> getPredictions(final PredictionModel model, final String accession) {
        Double min = mapModelMinScore.get(model);
        if (min.isInfinite())
            min = Double.NEGATIVE_INFINITY;
        return getPredictions(model, accession, min);
    }

    private Map<Integer,Double> getPredictions(final PredictionModel model, final String accession,
            final double threshold) {
        if (accession==null) {
            throw new IllegalArgumentException("accession cannot be null");
        }

        Protein protein = getProtein(accession);
        if (protein == null)
            return null;

        MultiMap<Integer,Map<String, Object>> mm = PTMAnnotationUtil.extractPTMAnnotation(protein,
                model.getSupportedPTM(), model.getSupportedAminoAcid(),
                new AnnotationFilter() {
            public boolean filter(int loc, Map<String, Object> annotation) {
                if (annotation==null)
                    return false;

                String modelName = (String)annotation.get(XML_FIELD_MODEL);
                if (modelName==null || !modelName.equals(model.getName()))
                    return false;

                Object score = annotation.get(ANNOTATION_FIELD_SCORE);
                if (score==null || !(score instanceof Double)
                        || (Double)score < threshold)
                    return false;

                return true;
            }
        });

        if (mm==null)
            return null;

        Map<Integer,Double> predictions = new TreeMap();
        for (Map.Entry<Integer,Collection<Map<String, Object>>> entry : mm.entrySet()) {
            int site = entry.getKey();
            Collection<Map<String, Object>> anns = entry.getValue();
            if (anns==null || anns.isEmpty()) {
                predictions.put(site, Double.NEGATIVE_INFINITY);
            } else {
                Map<String, Object> ann = anns.iterator().next();
                Object score = ann.get(ANNOTATION_FIELD_SCORE);
                predictions.put(site, (Double)score);
            }
        }

        return predictions;
    }

    public double getPrediction(final PredictionModel model, final String accession, final int site) {
        if (accession==null) {
            throw new IllegalArgumentException("accession cannot be null");
        }

        Protein protein = getProtein(accession);
        if (protein == null)
            return Double.NEGATIVE_INFINITY;

        Collection<Map<String,Object>> anns = PTMAnnotationUtil.extractPTMAnnotation(
                protein, site, model.getSupportedPTM(), new AnnotationFilter() {
            public boolean filter(int loc, Map<String, Object> annotation) {
                if (annotation==null)
                    return false;

                String modelName = (String)annotation.get(XML_FIELD_MODEL);
                if (modelName==null || !modelName.equals(model.getName()))
                    return false;

                return true;
            }
        });
        
        if (anns==null || anns.isEmpty())
            return Double.NEGATIVE_INFINITY;

        Map<String, Object> ann = anns.iterator().next();

        Object score = ann.get(ANNOTATION_FIELD_SCORE);
        if (score==null || !(score instanceof Double))
            return Double.NEGATIVE_INFINITY;

        return (Double)score;
    }

    public Map<Integer,Double> getPredictedSites(final PredictionModel model, final String accession) {
        return getPredictions(model, accession, getThreshold(model));
    }

    public double getMaxPredictionScore(PredictionModel model) {
        if (model==null || !mapModelMaxScore.containsKey(model)) {
            throw new IllegalArgumentException();
        }

        Double ret = mapModelMaxScore.get(model);
        if (ret.isInfinite()) {
            resetMinMaxScore();
            ret = mapModelMaxScore.get(model);
        }

        return ret;
    }

    public double getMinPredictionScore(PredictionModel model) {
        if (model==null || !mapModelMinScore.containsKey(model)) {
            throw new IllegalArgumentException();
        }
        
        Double ret = mapModelMinScore.get(model);
        if (ret.isInfinite()) {
            resetMinMaxScore();
            ret = mapModelMinScore.get(model);
        }

        return ret;
    }

    public double getThreshold(PredictionModel model) {
        if (model==null || !mapModelThreshold.containsKey(model)) {
            throw new IllegalArgumentException();
        }

        return mapModelThreshold.get(model);
    }

    public void setPrediction(PredictionModel model, String accession, int site, double prediction) {
        if (model==null || accession==null) {
            throw new IllegalArgumentException();
        }

        if (!mapModelThreshold.containsKey(model)) {
            throw new IllegalArgumentException();
        }

        Protein protein = mapAccessionProtein.get(accession);
        if (protein==null) {
            throw new IllegalStateException("Add protein first: "+accession);
        }

        String proteinSeq = protein.getSequence();
        if (proteinSeq==null) {
            throw new IllegalStateException("No sequence information available.");
        }

        if (proteinSeq.length()<=site || site<0) {
            System.err.println(accession+"\t"+site);
            throw new java.lang.IndexOutOfBoundsException();
        }


        Map<String, Object> ann = new HashMap();
        ann.put(ANNOTATION_FIELD_SCORE, prediction);
        ann.put(XML_FIELD_MODEL, model.getName());

        PTMAnnotationUtil.annotate(protein, site, model.getSupportedPTM(), null, ann);

        Double min = mapModelMinScore.get(model);
        Double max = mapModelMaxScore.get(model);
        if (prediction<min)
            mapModelMinScore.put(model, prediction);
        if (prediction>max)
            mapModelMaxScore.put(model, prediction);
    }

    public void setThreshold(PredictionModel model, double threshold) {
        if (model==null || !mapModelThreshold.containsKey(model)) {
            throw new IllegalArgumentException();
        }

        mapModelThreshold.put(model, threshold);
    }

    private void resetMinMaxScore() {
        Set<String> accs = getProteinsAccessions();
        for (PredictionModel model : mapModelThreshold.keySet()) {
            double min = Double.POSITIVE_INFINITY;
            double max = Double.NEGATIVE_INFINITY;
            for (String acc : accs) {
                Map<Integer,Double> preds = getPredictions(model, acc);
                if(preds==null)continue;
                for (double score : preds.values()) {
                    if (score<min)
                        min = score;
                    if (score>max)
                        max = score;
                }
            }
            mapModelMinScore.put(model, min);
            mapModelMaxScore.put(model, max);
        }
    }
}
