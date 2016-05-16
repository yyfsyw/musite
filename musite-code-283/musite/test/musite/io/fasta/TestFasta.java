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

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import musite.Proteins;

import musite.io.MusiteIOUtil;
import musite.io.fasta.parser.DefaultHeaderRule;

import musite.PTM;

/**
 *
 * @author Jianjiong Gao
 */
public class TestFasta extends TestCase {

    public void testReadAndWrite() throws IOException {
        String fasta = "testData/Human-test.fasta";
        String out = fasta + ".tmp";

        String regAcc = "([^ ]+)";
        String regSymbol = "\\| symbol: ([^ ,]+)";
        String regDesc = "\\| desc: ([^\\|]+) \\|";
        String regOrg = "\\| organism: ([^\\|]+)";

        DefaultHeaderRule hr = new DefaultHeaderRule(regAcc, regSymbol, regDesc, regOrg);
        ProteinsFastaReader reader = new ModifiedProteinsFastaReaderBuilder()
                .ptm(PTM.PHOSPHORYLATION).headerRule(hr).build();
        Proteins proteins = MusiteIOUtil.read(reader, fasta);

        System.out.println(proteins.proteinCount());

        ModifiedProteinsFastaWriter writer = new ModifiedProteinsFastaWriter(PTM.PHOSPHORYLATION);
        MusiteIOUtil.write(writer, out, proteins);

        new File(out).delete();
    }
}
