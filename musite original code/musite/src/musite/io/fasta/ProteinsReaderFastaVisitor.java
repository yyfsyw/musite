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

import musite.Protein;
import musite.ProteinImpl;
import musite.Proteins;
import musite.ProteinsImpl;

import musite.io.fasta.parser.DefaultHeaderParser;
import musite.io.fasta.parser.DefaultHeaderRule;
import musite.io.fasta.parser.DefaultSequenceParser;
import musite.io.fasta.parser.HeaderParser;
import musite.io.fasta.parser.HeaderRule;
import musite.io.fasta.parser.SequenceParser;


/**
 *
 * @author Jianjiong Gao
 */
public class ProteinsReaderFastaVisitor implements FastaVisitor {
    protected final Proteins proteins;
    protected final HeaderParser headerParser;
    protected final SequenceParser sequenceParser;
    protected final boolean include;
    protected final Proteins.ConfictHandleOption confictHandleOption;
    protected Proteins.ProteinFilter filter;

    public static class Builder {
        private HeaderParser headerParser;
        private SequenceParser sequenceParser;
        private boolean include;
        private Proteins.ConfictHandleOption confictHandleOption;
        private Proteins proteins;
        private Proteins.ProteinFilter filter;

        public Builder() {
            headerParser = new DefaultHeaderParser(new DefaultHeaderRule("(.+)"));
            sequenceParser = new DefaultSequenceParser();
            include = true;
            confictHandleOption = Proteins.ConfictHandleOption.RENAME;
            proteins = new ProteinsImpl();
            filter = null;
        }

        public Builder headerParser(HeaderParser headerParser) {
            if (headerParser==null)
                throw new IllegalArgumentException();
            this.headerParser = headerParser;
            return this;
        }

        public Builder headerRule(HeaderRule headerRule) {
            if (headerRule==null)
                throw new IllegalArgumentException();
            headerParser = new DefaultHeaderParser(headerRule);
            return this;
        }

        public Builder sequenceParser(SequenceParser sequenceParser) {
            if (sequenceParser==null)
                throw new IllegalArgumentException();
            this.sequenceParser = sequenceParser;
            return this;
        }

        public Builder confictHandleOption(Proteins.ConfictHandleOption confictHandleOption) {
            if (confictHandleOption==null)
                throw new IllegalArgumentException();
            this.confictHandleOption = confictHandleOption;
            return this;
        }

        public Builder proteins(Proteins proteins) {
            if (proteins==null)
                throw new IllegalArgumentException();
            this.proteins = proteins;
            return this;
        }

        public Builder filter(Proteins.ProteinFilter filter) {
            this.filter = filter;
            return this;
        }

        public ProteinsReaderFastaVisitor build() {
            return new ProteinsReaderFastaVisitor(this);
        }
    }

    protected ProteinsReaderFastaVisitor(Builder builder) {
        this.proteins = builder.proteins;
        this.headerParser = builder.headerParser;
        this.sequenceParser = builder.sequenceParser;
        this.filter = builder.filter;
        this.include = builder.include;
        this.confictHandleOption = builder.confictHandleOption;
    }

    public void setFilter(Proteins.ProteinFilter filter) {
        this.filter = filter;
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

        headerParser.parse(header);
        String accession = headerParser.getAccession();
        if (accession == null) {
            System.err.println("Null accession");
            return;
        }

        String symbol = headerParser.getSymbol();
        String description = headerParser.getDescription();
        String organism = headerParser.getOrganism();

        sequenceParser.parse(sequence);
        String proSeq = sequenceParser.getSequence();
        Protein protein = new ProteinImpl(accession, proSeq, symbol, description, organism);

        if (filter==null || filter.filter(protein))
            proteins.addProtein(protein, confictHandleOption);
    }

    public Proteins getProteins() {
        return proteins;
    }

}
