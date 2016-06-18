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

/**
 *
 * @author LucasYao
 */
public class testPAILUtil extends TestCase{

    public void testGetresultsFromPAILUtil() throws IOException {
        String infile = "data/uniprot_corehistone_reviewed.human.with.disorder.N6acetyllysine.xml";
        String outfile = "data/PAILresults_corehistone.txt";

        PAILUtil.runResultsByPAIL(infile, outfile);
        AllToolsUtil.writeXMLfromResults(infile, outfile,PTM.ACETYLATION, Collections.singleton(AminoAcid.LYSINE),"modelpail");

    }

    public void testCalculateROC() throws IOException {
        String infile = "data/uniprot_corehistone_reviewed.human.with.disorder.N6acetyllysine.xml";
        String predxmlfile = "data/PAILresults_corehistone.pred.xml";
        String outfile = "data/PAILROC_corehistone.txt";


        //AllToolsUtil.calculateROC(predxmlfile, infile, outfile, PTM.ACETYLATION, Collections.singleton(AminoAcid.LYSINE), 10, 0, 21.63);
        AllToolsUtil.calculateROC(predxmlfile, infile, outfile, PTM.ACETYLATION, Collections.singleton(AminoAcid.LYSINE));

    }

}
