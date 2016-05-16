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
package musite.io.fasta;

import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import musite.io.fasta.parser.DefaultHeaderParser;
import musite.io.fasta.parser.DefaultHeaderRule;

/**
 *
 * @author Jianjiong Gao
 */
public class FastaUtil {
 
    public static List<String> readFastaAccessions(final String fastaDir,
            final String accRegEx) throws IOException {
        if (fastaDir==null || accRegEx==null) {
            throw new NullPointerException();
        }

        List<String> accessions = new ArrayList<String>();
        FastaVisitor visitor = new AccessionRetrieverFastaVisitor(accessions, accRegEx);
        FileInputStream is = new FileInputStream(fastaDir);
        FastaTravelerImpl traveler = new FastaTravelerImpl(visitor);
        traveler.travel(is);
        is.close();
        
        return accessions;
    }
    
}

class AccessionRetrieverFastaVisitor implements FastaVisitor {
    private final List<String> accessions;
    private final DefaultHeaderParser parser;

    public AccessionRetrieverFastaVisitor(final List<String> accessions,
                                      final String accRegEx) {
        if (accessions==null || accRegEx==null) {
            throw new NullPointerException();
        }

        this.accessions = accessions;
        DefaultHeaderRule rule = new DefaultHeaderRule(accRegEx);
        parser = new DefaultHeaderParser(rule);
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
            accessions.add(accession);
        }
        
    }
}
