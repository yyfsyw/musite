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

/**
 *
 * @author LucasYao
 */
public class testPredModUtil extends TestCase{


/*
    public void testGetresultsFromPredMod() throws IOException {
        String infile = "data/uniprot_corehistone_reviewed.human.with.disorder.N6acetyllysine.xml";
        String outfile = "data/predmodresults_corehistone.txt";

        PredModUtil.runResultsByPredMod(infile, outfile);
        AllToolsUtil.writeXMLfromResults(infile, outfile,PTM.ACETYLATION, "modelpremod");


    }

 
    public void testCalculateROC() throws IOException {
        String infile = "data/uniprot_corehistone_reviewed.human.with.disorder.N6acetyllysine.xml";
        String predxmlfile = "data/predmodresults_corehistone.pred.xml";
        String outfile = "data/predmodROC_corehistone.txt";


        //AllToolsUtil.calculateROC(predxmlfile, infile, outfile, PTM.ACETYLATION, Collections.singleton(AminoAcid.LYSINE), 10, 0.0, 37.0);
        AllToolsUtil.calculateROC(predxmlfile, infile, outfile, PTM.ACETYLATION, Collections.singleton(AminoAcid.LYSINE));

    }

*/

    public void testCalculateROCformusite1() throws IOException {
        String infile = "data/uniprot_sprot.N6-acetyllysine.balenced.with.disorder.xml";
        String predxmlfile = "data/Musite.N6-acetyllysine.balenced.with.disorder.pred2.xml.gz";
        String outfile = "data/ROC2Musite_N6-acetyllysine.txt";


        AllToolsUtil.calculateROC(predxmlfile, infile, outfile, PTM.ACETYLATION, Collections.singleton(AminoAcid.LYSINE),200);

    }
/*
        public void testCalculateROCformusite2() throws IOException {
        String infile = "data/uniprot_sprot.Omega-N-methylarginine.balenced.with.disorder.xml";
        String predxmlfile = "data/Musite.Omega-N-methylarginine.balenced.with.disorder.pred2.xml.gz";
        String outfile = "data/ROC2Musite_Omega-N-methylarginine.txt";


        AllToolsUtil.calculateROC(predxmlfile, infile, outfile, PTM.METHYLATION, Collections.singleton(AminoAcid.ARGININE));

    }

        public void testCalculateROCformusite3() throws IOException {
        String infile = "data/uniprot_sprot.Sulfotyrosine.balenced.with.disorder.xml";
        String predxmlfile = "data/Musite.Sulfotyrosine.balenced.with.disorder.pred2.xml.gz";
        String outfile = "data/ROC2Musite_Sulfotyrosine.txt";


        AllToolsUtil.calculateROC(predxmlfile, infile, outfile, PTM.SULFATION, Collections.singleton(AminoAcid.TYROSINE));

    }

        public void testCalculateROCformusite4() throws IOException {
        String infile = "data/uniprot_sprot.N6-methyllysine.balenced.with.disorder.xml";
        String predxmlfile = "data/Musite.N6-methyllysine.balenced.with.disorder.pred.xml.gz";
        String outfile = "data/ROCMusite_N6-methyllysine.txt";


        AllToolsUtil.calculateROC(predxmlfile, infile, outfile, PTM.METHYLATION, Collections.singleton(AminoAcid.LYSINE));

    }
        
        public void testCalculateROCformusite5() throws IOException {
        String infile = "data/uniprot_sprot.S-palmitoyl-cysteine.balenced.with.disorder.xml";
        String predxmlfile = "data/Musite.S-palmitoyl-cysteine.balenced.with.disorder.pred.xml.gz";
        String outfile = "data/ROCMusite_S-palmitoyl-cysteine.txt";


        AllToolsUtil.calculateROC(predxmlfile, infile, outfile, PTM.PALMITOYLATION, Collections.singleton(AminoAcid.CYSTEINE));

    }

*/

}


