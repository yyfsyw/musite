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

import musite.io.fasta.parser.DefaultHeaderParser;
import musite.io.fasta.parser.HeaderParser;
import musite.io.fasta.parser.HeaderRule;
import musite.io.fasta.FastaVisitor;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Jianjiong Gao
 */
public class DisorderPredictionFastaVisitor implements FastaVisitor {

    private final Map<String,List<Double>> mapAccDisorder;
    private final HeaderParser parser;
    private final DisorderPredictor predictor;

    public DisorderPredictionFastaVisitor(final Map<String,List<Double>> mapAccDisorder,
                                          final HeaderRule rule,
                                          final DisorderPredictor predictor) {
        if (mapAccDisorder==null || rule==null || predictor==null) {
            throw new NullPointerException();
        }

        this.mapAccDisorder = mapAccDisorder;
        parser = new DefaultHeaderParser(rule);
        this.predictor = predictor;
    }

    /**
     * 
     * @param header
     * @param sequence
     */
    public void visit(final String header, final String sequence) {
        if (header==null || sequence==null) {
            throw new NullPointerException("Null header or sequence.");
        }

        parser.parse(header);
        String accession = parser.getAccession();
        if (accession != null) {
            String seq;

            // remove * in the end
            if (sequence.endsWith("*")) {
                seq = sequence.substring(0,sequence.length()-1);
            } else {
                seq = sequence;
            }

            List<Double> res = predictor.predict(seq);
            if (res != null) {
                mapAccDisorder.put(accession, res);
            }
        }
    }

}
