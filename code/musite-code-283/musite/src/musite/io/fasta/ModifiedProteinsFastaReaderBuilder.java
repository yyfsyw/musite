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

import musite.Proteins;
import musite.ProteinsImpl;

import musite.io.fasta.parser.DefaultHeaderRule;
import musite.io.fasta.parser.HeaderRule;
import musite.io.fasta.parser.SequencePTMSiteParser;
import musite.io.fasta.parser.SequencePTMSiteTokenAppendedParser;

import musite.PTM;

/**
 *
 * @author Jianjiong Gao
 */
public class ModifiedProteinsFastaReaderBuilder {
    private HeaderRule headerRule;
    private PTM ptm;
    private SequencePTMSiteParser sequenceParser;
    private Proteins proteins;
    private Proteins.ProteinFilter filter;

    public ModifiedProteinsFastaReaderBuilder() {
        ptm = null;
        headerRule = new DefaultHeaderRule("(.+)");
        sequenceParser = new SequencePTMSiteTokenAppendedParser("#");
        proteins = null;
        filter = null;
    }

    public ModifiedProteinsFastaReaderBuilder ptm(PTM ptm) {
        this.ptm = ptm;
        return this;
    }

    public ModifiedProteinsFastaReaderBuilder headerRule(HeaderRule headerRule) {
        this.headerRule = headerRule;
        return this;
    }

    public ModifiedProteinsFastaReaderBuilder sequenceParser(SequencePTMSiteParser sequenceParser) {
        this.sequenceParser = sequenceParser;
        return this;
    }

    public ModifiedProteinsFastaReaderBuilder proteins(Proteins proteins) {
        this.proteins = proteins;
        return this;
    }

    public ModifiedProteinsFastaReaderBuilder filter(Proteins.ProteinFilter filter) {
        this.filter = filter;
        return this;
    }

    public ProteinsFastaReader build() {
        return new ProteinsFastaReader(new ModifiedProteinsReaderFastaVisitor.Builder()
            .ptm(ptm).headerRule(headerRule).sequenceParser(sequenceParser)
            .proteins(proteins!=null?proteins:new ProteinsImpl()).filter(filter).build());
    }
}
