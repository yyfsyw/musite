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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Jianjiong Gao
 */
public class DisorderReaderFastaVisitor implements FastaVisitor {

    private final Map<String,List<Double>> mapAccDisorder;
    private final HeaderParser headParser;
    private Set<String> accessionFilter;

    private String seperator = ";";

    public DisorderReaderFastaVisitor(final Map<String,List<Double>> mapAccDisorder,
                                      final HeaderRule headRule) {
        if (mapAccDisorder==null || headRule==null) {
            throw new NullPointerException();
        }

        this.mapAccDisorder = mapAccDisorder;
        headParser = new DefaultHeaderParser(headRule);
        accessionFilter = null;
    }

    public void setSeperator(String seperator) {
        if (seperator==null)
            throw new IllegalArgumentException();
        this.seperator = seperator;
    }

    public void setAccessionFilter(Set<String> accessions) {
        this.accessionFilter = accessions;
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

        headParser.parse(header);
        String accession = headParser.getAccession();
        if (accession != null && (accessionFilter==null || accessionFilter.contains(accession))) {
            List<Double> res = parseSequence(sequence);
            if (res != null) {
                mapAccDisorder.put(accession, res);
            }
        }
    }

    private List<Double> parseSequence(final String sequence) {
        String[] strs = sequence.split(seperator);
        int n = strs.length;

        List<Double> res = new ArrayList<Double>(n);
        try {
            for (String str : strs) {
                res.add(Double.parseDouble(str));
            }
            return res;
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
