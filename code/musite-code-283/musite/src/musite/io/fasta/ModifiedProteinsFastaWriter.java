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

import java.util.Set;
import java.util.TreeSet;

import musite.Protein;

import musite.PTM;
import musite.PTMAnnotationUtil;

/**
 *
 * @author Jianjiong Gao
 */
public class ModifiedProteinsFastaWriter
        extends DefaultProteinsFastaWriter {

    public ModifiedProteinsFastaWriter() {
        this(null);
    }

    public ModifiedProteinsFastaWriter(final PTM ptm) {
        setResidueAnnotator(new ResidueAnnotator() {
            public String annotate(Protein protein, int index) {
                return protein.getSequence().charAt(index)+"#";
            }

            public TreeSet<Integer> indicesOfResidues(Protein protein) {
                Set<Integer> sites = PTMAnnotationUtil.getSites(protein, ptm);
                if (sites==null) {
                    return new TreeSet();
                } else {
                    return new TreeSet(sites);
                }
            }
        });
    }
    
}
