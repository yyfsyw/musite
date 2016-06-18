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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.util.List;
import java.util.Set;

import musite.Proteins;

import musite.io.Writer;

/**
 *
 * @author Jianjiong Gao
 */
public abstract class AbstractProteinsFastaWriter implements Writer<Proteins> {
    
    /**
     *
     * @param data
     */
    public void write(final OutputStream os, final Proteins data) throws IOException {
        if (os==null || data==null) {
            throw new IllegalArgumentException();
        }

        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bufWriter = new BufferedWriter(osw);

        Set<String> accessions = data.getProteinsAccessions();

        for (String acc : accessions) {
            bufWriter.write(formatHeader(data,acc));

            bufWriter.newLine();

            List<String> seqs = formatSequence(data,acc);
            if (seqs!=null) {
                for (String seq : seqs) {
                    bufWriter.write(seq);
                    bufWriter.newLine();
                }
            }
            bufWriter.newLine(); // add a empty line between entries;
        }

        bufWriter.flush();
    }

    protected abstract String formatHeader(final Proteins data, final String acc);

    protected abstract List<String> formatSequence(final Proteins data, final String acc);
}
