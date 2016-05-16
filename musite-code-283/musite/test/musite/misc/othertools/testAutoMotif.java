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
public class testAutoMotif extends TestCase{
/*        public void testGetresultsFromAutoMotif() throws IOException {
            ArrayList<Integer> position = new ArrayList<Integer>();
            ArrayList<Double> score = new ArrayList<Double>();
            String seq="MFPGCPRLWVLVVLGTSWVGWGSQGTEAAQLRQFYVAAQGISWSYRPEPTNSSLNLSVTSFKKIVYREYEPYFKKEKPQSTISGLLGPTLYAEVGDIIKVHFKNKADKPLSIHPQGIRYSKLSEGASYLDHTFPAEKMDDAVAPGREYTYEWSISEDSGPTHDDPPCLTHIYYSHENLIEDFNSGLIGPLLICKKGTLTEGGTQKTFDKQIVLLFAVFDESKSWSQSSSLMYTVNGYVNGTMPDITVCAHDHISWHLLGMSSGPELFSIHFNGQVLEQNHHKVSAITLVSATSTTANMTVGPEGKWIISSLTPKHLQAGMQAYIDIKNCPKKTRNLKKITREQRRHMKRWEYFIAAEEVIWDYAPVIPANMDKKYRSQHLDNFSNQIGKHYKKVMYTQYEDESFTKHTVNPNMKEDGILGPIIRAQVRDTLKIVFKNMASRPYSIYPHGVTFSPYEDEVNSSFTSGRNNTMIRAVQPGETYTYKWNILEFDEPTENDAQCLTRPYYSDVDIMRDIASGLIGLLLICKSRSLDRRGIQRAADIEQQAVFAVFDENKSWYLEDNINKFCENPDEVKRDDPKFYESNIMSTINGYVPESITTLGFCFDDTVQWHFCSVGTQNEILTIHFTGHSFIYGKRHEDTLTLFPMRGESVTVTMDNVGTWMLTSMNSSPRSKKLRLKFRDVKCIPDDDEDSYEIFEPPESTVMATRKMHDRLEPEDEESDADYDYQNRLAAALGIRSFRNSSLNQEEEEFNLTALALENGTEFVSSNTDIIVGSNYSSPSNISKFTVNNLAEPQKAPSHQQATTAGSPLRHLIGKNSVLNSSTAEHSSPYSEDPIEDPLQPDVTGIRLLSLGAGEFKSQEHAKHKGPKVERDQAAKHRFSWMKLLAHKVGRHLSQDTGSPSGMRPWEDLPSQDTGSPSRMRPWKDPPSDLLLLKQSNSSKILVGRWHLASEKGSYEIIQDTDEDTAVNNWLISPQNASRAWGESTPLANKPGKQSGHPKFPRVRHKSLQVRQDGGKSRLKKSQFLIKTRKKKKEKHTHHAPLSPRTFHPLRSEAYNTFSERRLKHSLVLHKSNETSLPTDLNQTLPSMDFGWIASLPDHNQNSSNDTGQASCPPGLYQTVPPEEHYQTFPIQDPDQMHSTSDPSHRSSSPELSEMLEYDRSHKSFPTDISQMSPSSEHEVWQTVISPDLSQVTLSPELSQTNLSPDLSHTTLSPELIQRNLSPALGQMPISPDLSHTTLSPDLSHTTLSLDLSQTNLSPELSQTNLSPALGQMPLSPDLSHTTLSLDFSQTNLSPELSHMTLSPELSQTNLSPALGQMPISPDLSHTTLSLDFSQTNLSPELSQTNLSPALGQMPLSPDPSHTTLSLDLSQTNLSPELSQTNLSPDLSEMPLFADLSQIPLTPDLDQMTLSPDLGETDLSPNFGQMSLSPDLSQVTLSPDISDTTLLPDLSQISPPPDLDQIFYPSESSQSLLLQEFNESFPYPDLGQMPSPSSPTLNDTFLSKEFNPLVIVGLSKDGTDYIEIIPKEEVQSSEDDYAEIDYVPYDDPYKTDVRTNINSSRDPDNIAAWYLRSNNGNRRNYYIAAEEISWDYSEFVQRETDIEDSDDIPEDTTYKKVVFRKYLDSTFTKRDPRGEYEEHLGILGPIIRAEVDDVIQVRFKNLASRPYSLHAHGLSYEKSSEGKTYEDDSPEWFKEDNAVQPNSSYTYVWHATERSGPESPGSACRAWAYYSAVNPEKDIHSGLIGPLLICQKGILHKDSNMPMDMREFVLLFMTFDEKKSWYYEKKSRSSWRLTSSEMKKSHEFHAINGMIYSLPGLKMYEQEWVRLHLLNIGGSQDIHVVHFHGQTLLENGNKQHQLGVWPLLPGSFKTLEMKASKPGWWLLNTEVGENQRAGMQTPFLIMDRDCRMPMGLSTGIISDSQIKASEFLGYWEPRLARLNNGGSYNAWSVEKLAAEFASKPWIQVDMQKEVIITGIQTQGAKHYLKSCYTTEFYVAYSSNQINWQIFKGNSTRNVMYFNGNSDASTIKENQFDPPIVARYIRISPTRAYNRPTLRLELQGCEVNGCSTPLGMENGKIENKQITASSFKKSWWGDYWEPFRARLNAQGRVNAWQAKANNNKQWLEIDLLKIKKITAIITQGCKSLSSEMYVKSYTIHYSEQGVEWKPYRLKSSMVDKIFEGNTNTKGHVKNFFNPPIISRFIRVIPKTWNQSIALRLELFGCDIY";
            Protein p = new ProteinImpl(null,seq,null,null,null);
            AutoMotifUtil.predictByAutoMotif(p, "Sulfotyrosine", position, score,"temp.log.txt");
        }
*/
/*
       public void testGetresultsFromAutoMotif() throws IOException {
        String infile = "data/uniprot_corehistone_reviewed.human.with.disorder.N6acetyllysine.xml";
        String outfile = "data/AutoMotif_acetyllysine_corehistone.txt";

        AutoMotifUtil.runResultsByAutoMotif(infile, outfile, "N6-acetyllysine", "temp.log.txt");
        AllToolsUtil.writeXMLfromResults(infile, outfile,PTM.ACETYLATION, "modelautomotif");


    }
     
*/
/*
     public void testCalculateROC() throws IOException {
        String infile = "data/uniprot_corehistone_reviewed.human.with.disorder.N6acetyllysine.xml";
        String predxmlfile = "data/AutoMotif_acetyllysine_corehistone.pred.xml";
        String outfile = "data/AutoMotifROC_acetyllysine_corehistone.txt";


        //AllToolsUtil.calculateROC(predxmlfile, infile, outfile, PTM.ACETYLATION, Collections.singleton(AminoAcid.LYSINE), 10, 0, 21.63);
        AllToolsUtil.calculateROC(predxmlfile, infile, outfile, PTM.ACETYLATION, Collections.singleton(AminoAcid.LYSINE));

    }
 */



/*
    public void testGetresultsFromAutoMotif2() throws IOException {
        String infile = "data/uniprot_sprot.Omega-N-methylarginine.balenced.with.disorder.xml";
        String outfile = "data/AutoMotif_Omega-N-methylarginine.txt";

        AutoMotifUtil.runResultsByAutoMotif(infile, outfile, "Omega-N-methylated arginine", "temp.log2.txt");
        AllToolsUtil.writeXMLfromResults(infile, outfile,PTM.METHYLATION,Collections.singleton(AminoAcid.ARGININE), "modelautomotif");


    }

    public void testCalculateROC2() throws IOException {
        String infile = "data/uniprot_sprot.Omega-N-methylarginine.balenced.with.disorder.xml";
        String predxmlfile = "data/AutoMotif_Omega-N-methylarginine.pred.xml";
        String outfile = "data/ROCAutoMotif_Omega-N-methylarginine.txt";


       AllToolsUtil.calculateROC(predxmlfile, infile, outfile, PTM.METHYLATION, Collections.singleton(AminoAcid.ARGININE));

    }


    public void testGetresultsFromAutoMotif3() throws IOException {
        String infile = "data/uniprot_sprot.Sulfotyrosine.balenced.with.disorder.xml";
        String outfile = "data/AutoMotif_Sulfotyrosine.txt";

        AutoMotifUtil.runResultsByAutoMotif(infile, outfile, "Sulfotyrosine", "temp.log3.txt");
        AllToolsUtil.writeXMLfromResults(infile, outfile,PTM.SULFATION, Collections.singleton(AminoAcid.TYROSINE), "modelautomotif");


    }

    public void testCalculateROC3() throws IOException {
        String infile = "data/uniprot_sprot.Sulfotyrosine.balenced.with.disorder.xml";
        String predxmlfile = "data/AutoMotif_Sulfotyrosine.pred.xml";
        String outfile = "data/ROCAutoMotif_Sulfotyrosine.txt";


       AllToolsUtil.calculateROC(predxmlfile, infile, outfile, PTM.SULFATION, Collections.singleton(AminoAcid.TYROSINE));

    }
*/
/*
    public void testGetresultsFromAutoMotif1() throws IOException {
        String infile = "data/uniprot_sprot.N6-acetyllysine.balenced.with.disorder.xml";
        String outfile = "data/AutoMotif_N6-acetyllysine.txt";

        AutoMotifUtil.runResultsByAutoMotif(infile, outfile, "N6-acetyllysine", "temp.log1.txt");
        AllToolsUtil.writeXMLfromResults(infile, outfile,PTM.ACETYLATION, "modelautomotif");


    }

    public void testCalculateROC1() throws IOException {
        String infile = "data/uniprot_sprot.N6-acetyllysine.balenced.with.disorder.xml";
        String predxmlfile = "data/AutoMotif_N6-acetyllysine.pred.xml";
        String outfile = "data/ROCAutoMotif_N6-acetyllysine.txt";


       AllToolsUtil.calculateROC(predxmlfile, infile, outfile, PTM.ACETYLATION, Collections.singleton(AminoAcid.LYSINE));

    }
*/

       public void testCalculateROC1() throws IOException {
        String infile = "data/aabinaryfeature/uniprot_sprot.human.with.disorder.N6acetyllysine.nr90.nr50.test.xml";
        String predxmlfile = "data/aabinaryfeature/uniprot_sprot.human.with.disorder.N6acetyllysine.nr90.nr50.test.aasubmatrix.pred.xml.gz";
        String outfile = "data/aabinaryfeature/aasubmatrixROCMusite_matrixrefine_N6-acetyllysine.txt";


       AllToolsUtil.calculateROC(predxmlfile, infile, outfile, PTM.ACETYLATION, Collections.singleton(AminoAcid.LYSINE));

    }

       public void testCalculateROC2() throws IOException {
        String infile = "data/aabinaryfeature/uniprot_sprot.Omega-N-methylarginine.balenced.with.disorder.xml";
        String predxmlfile = "data/aabinaryfeature/uniprot_sprot.Omega-N-methylarginine.balenced.with.disorder.aasubmatrix.pred.xml.gz";
        String outfile = "data/aabinaryfeature/aasubmatrixROCMusite_matrixrefine_Omega-N-methylarginine.txt";


       AllToolsUtil.calculateROC(predxmlfile, infile, outfile, PTM.METHYLATION, Collections.singleton(AminoAcid.ARGININE));

    }
}
