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

package musite.prediction.test;

import java.util.Iterator;
import java.util.Set;
import java.util.Collections;
import java.util.HashSet;

import musite.Protein;
import musite.Proteins;
import musite.PTM;
import musite.PTMAnnotationUtil;

import musite.prediction.PredictionModel;
import musite.prediction.PredictionResult;

import musite.util.AminoAcid;
import musite.util.StringUtil;

/**
 *
 * @author gjj
 */
public class TestResultEvaluator {

    public double[] evaluate(Proteins groundTruth, PredictionResult predictions, PredictionModel model,
            PTM ptm, Set<AminoAcid> aas, boolean skipWOPred) {
        int p=0, n=0, tp=0, fp=0;

        Set<Character> chs = AminoAcid.oneLetters(aas);

        Iterator<Protein> it = groundTruth.proteinIterator();
        while (it.hasNext()) {
            Protein protein = it.next();
            String acc = protein.getAccession();
            String seq = protein.getSequence();
            Set<Integer> ix = StringUtil.findAll(seq, chs, true);
            Set<Integer> trueSites = PTMAnnotationUtil.getSites(groundTruth.getProtein(acc));
            if (trueSites==null)
                trueSites = Collections.emptySet();
            else
                trueSites.retainAll(ix);

            if (skipWOPred&&predictions.getPredictedSites(model, acc)==null)
                continue;

            Set<Integer> predictedSites = predictions.getPredictedSites(model, acc).keySet();
            if (predictedSites==null)
                predictedSites = new HashSet<Integer>(0);
            else
                predictedSites.retainAll(ix);

            p += trueSites.size();
            n += ix.size() - trueSites.size();

            int pp = predictedSites.size();

            predictedSites.retainAll(trueSites);
            tp += predictedSites.size();
            fp += pp - predictedSites.size();
        }

        double tpr = p==0 ? Double.POSITIVE_INFINITY : tp*1.0/p;
        double fpr = n==0 ? Double.POSITIVE_INFINITY : 1-fp*1.0/n;
        return new double[]{tpr, fpr};
    }
}
