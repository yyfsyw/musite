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

import musite.io.fasta.parser.SequencePTMSiteParser;

import java.util.Set;

import musite.Protein;

import musite.PTM;
import musite.PTMAnnotationUtil;

/**
 *
 * @author Jianjiong Gao
 */
public class ModifiedProteinsReaderFastaVisitor extends ProteinsReaderFastaVisitor {
    protected final PTM ptm;

    public static class Builder extends ProteinsReaderFastaVisitor.Builder {
        private PTM ptm;

        public Builder() {
            this.ptm = null;
        }

        public Builder ptm(PTM ptm) {
            this.ptm = ptm;
            return this;
        }

        public ModifiedProteinsReaderFastaVisitor build() {
            return new ModifiedProteinsReaderFastaVisitor(this);
        }
    }

    protected ModifiedProteinsReaderFastaVisitor(Builder builder) {
        super(builder);
        this.ptm = builder.ptm;
    }

    /**
     *
     * @param header
     * @param sequence
     */
    @Override
    public void visit(final String header, final String sequence) {
        super.visit(header, sequence);
        
        String accession = headerParser.getAccession();
        if (accession != null) {
            Protein protein = proteins.getProtein(accession);
            if (protein==null)
                return;
            
            Set<Integer> sites = ((SequencePTMSiteParser)sequenceParser).getSites();
            if (sites!=null) {
                PTMAnnotationUtil.annotate(protein, sites, ptm);
            }
        }
    }

}
