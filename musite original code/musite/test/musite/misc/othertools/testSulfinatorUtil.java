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

package musite.misc.othertools;
import junit.framework.TestCase;
import java.io.IOException;
import java.util.Collections;
import musite.PTM;
import musite.util.AminoAcid;

import java.util.ArrayList;
import musite.Protein;
import musite.ProteinImpl;

/**
 *
 * @author LucasYao
 */
public class testSulfinatorUtil extends TestCase{
/*        public void testGetresultsFromSulfinator() throws IOException {

            ArrayList<Integer> position = new ArrayList<Integer>();
            ArrayList<Double> score = new ArrayList<Double>();
            String seq="MALLWGLLVLSWSCLQGPCSVFSPVSAMEPLGRQLTSGPNQEQVSPLTLLKLGNQEPGGQTALKSPPGVCSRDPTPEQTHRLARAMMAFTADLFSLVAQTSTCPNLILSPLSVALALSHLALGAQNHTLQRLQQVLHAGSGPCLPHLLSRLCQDLGPGAFRLAARMYLQKGFPIKEDFLEQSEQLFGAKPVSLTGKQEDDLANINQWVKEATEGKIQEFLSGLPEDTVLLLLNAIHFQGFWRNKFDPSLTQRDSFHLDEQFTVPVEMMQARTYPLRWFLLEQPEIQVAHFPFKNNMSFVVLVPTHFEWNVSQVLANLSWDTLHPPLVWERPTKVRLPKLYLKHQMDLVATLSQLGLQELFQAPDLRGISEQSLVVSGVQHQSTLELSEVGVEAAAATSIAMSRMSLSSFSVNRPFLFFIFEDTTGLPLFVGSVRNPNPSAPRELKEQQDSPGNKDFLQSLKGFPRGDKLFGPDLKLVPPMEEDYPQFGSPK";
            Protein p = new ProteinImpl(null,seq,null,null,null);
            SulfinatorUtil.predictBySulfinator(p,  position, score,"temp.log.txt");
        }

 */

   public void testGetresultsFromSulfinator() throws IOException {
        String infile = "data/uniprot_sprot.Sulfotyrosine.balenced.with.disorder.xml";
        String outfile = "data/Sulfinator_Sulfotyrosine.txt";

        SulfinatorUtil.runResultsBySulfinator(infile, outfile,  "temp.log4.txt");
        AllToolsUtil.writeXMLfromResults(infile, outfile,PTM.SULFATION, Collections.singleton(AminoAcid.TYROSINE),"modelsulfinator");


    }

    public void testCalculateROC() throws IOException {
        String infile = "data/uniprot_sprot.Sulfotyrosine.balenced.with.disorder.xml";
        String predxmlfile = "data/Sulfinator_Sulfotyrosine.pred.xml";
        String outfile = "data/ROCSulfinator_Sulfotyrosine.txt";


       AllToolsUtil.calculateROC(predxmlfile, infile, outfile, PTM.SULFATION, Collections.singleton(AminoAcid.TYROSINE));

    }

}
