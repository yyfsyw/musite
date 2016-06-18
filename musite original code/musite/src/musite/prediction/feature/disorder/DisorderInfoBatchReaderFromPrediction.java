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

package musite.prediction.feature.disorder;

import java.io.InputStream;
import java.io.IOException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import musite.io.fasta.parser.HeaderRule;
import musite.io.fasta.FastaTravelerImpl;
import musite.io.fasta.FastaVisitor;

/**
 *
 * @author Jianjiong Gao
 */
public class DisorderInfoBatchReaderFromPrediction
        implements DisorderInfoBatchReader {
    private final HeaderRule headerRule;
    private final DisorderPredictor predictor;

    public DisorderInfoBatchReaderFromPrediction(final HeaderRule headerRule,
            final DisorderPredictor predictor) {
        if (headerRule==null || predictor==null) {
            throw new NullPointerException();
        }
        
        this.headerRule = headerRule;
        this.predictor = predictor;
    }
    /**
     *
     * @return map from accession to disorder score
     */
    public Map<String,List<Double>> read(InputStream is) throws IOException {
        Map<String,List<Double>> mapAccDisorder = new LinkedHashMap<String,List<Double>>();

        FastaVisitor visitor
                = new DisorderPredictionFastaVisitor(mapAccDisorder,
                                                     headerRule,
                                                     predictor);

        FastaTravelerImpl traveler = new FastaTravelerImpl(visitor);
        traveler.travel(is);
        
        return mapAccDisorder;
    }
}
