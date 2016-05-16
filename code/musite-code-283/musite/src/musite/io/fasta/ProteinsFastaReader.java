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

import java.io.InputStream;
import java.io.IOException;

import musite.Proteins;

import musite.io.Reader;
import musite.io.fasta.parser.HeaderRule;

/**
 *
 * @author Jianjiong Gao
 */
public class ProteinsFastaReader implements Reader<Proteins> {
    protected final ProteinsReaderFastaVisitor visitor;
    protected final FastaTraveler traveler;

    public ProteinsFastaReader() {
        this (new ProteinsReaderFastaVisitor.Builder().build());
    }

    public ProteinsFastaReader(final HeaderRule headerRule) {
        this (new ProteinsReaderFastaVisitor.Builder().headerRule(headerRule).build());
    }

    public ProteinsFastaReader(final HeaderRule headerRule,
            final Proteins proteins) {
        this (new ProteinsReaderFastaVisitor.Builder().headerRule(headerRule)
                .proteins(proteins).build());
    }

    public ProteinsFastaReader(final ProteinsReaderFastaVisitor visitor) {
        if (visitor==null) {
            throw new java.lang.IllegalArgumentException();
        }

        this.visitor = visitor;
        traveler = new FastaTravelerImpl(visitor);
    }

    public void setFilter(Proteins.ProteinFilter filter) {
        visitor.setFilter(filter);
    }


    /**
     * Reader proteins
     * @return data if successful, null otherwise.
     */
    public Proteins read(final InputStream is) throws IOException {
        if (is==null)
            return null;
        
        traveler.travel(is);
        return visitor.getProteins();
    }
}
