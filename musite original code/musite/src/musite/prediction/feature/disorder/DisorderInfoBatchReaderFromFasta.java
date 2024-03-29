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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import musite.io.fasta.parser.DefaultHeaderRule;
import musite.io.fasta.parser.HeaderRule;
import musite.io.fasta.FastaTravelerImpl;

/**
 *
 * @author Jianjiong Gao
 */
public class DisorderInfoBatchReaderFromFasta
        implements DisorderInfoBatchReader {
    private final DisorderReaderFastaVisitor visitor;

    private final Map<String,List<Double>> mapAccDisorder;

    private String seperator = ";";

    public DisorderInfoBatchReaderFromFasta() {
        this(new DefaultHeaderRule("(.+)"));
    }

    public DisorderInfoBatchReaderFromFasta(final HeaderRule headerRule) {
        mapAccDisorder = new HashMap<String,List<Double>>();
        visitor = new DisorderReaderFastaVisitor(mapAccDisorder, headerRule);
    }
    
    public void setSeperator(String seperator) {
        if (seperator==null)
            throw new IllegalArgumentException();
        this.seperator = seperator;
    }

    public void setAccessionFilter(Set<String> accessions) {
        visitor.setAccessionFilter(accessions);
    }

    /**
     *
     * @return map from accession to disorder score
     */
    public Map<String,List<Double>> read(InputStream is) throws IOException {
        mapAccDisorder.clear();
        
        visitor.setSeperator(seperator);
        
        FastaTravelerImpl traveler = new FastaTravelerImpl(visitor);
        traveler.travel(is);
        
        return mapAccDisorder;
    }
}
