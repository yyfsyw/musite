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

package musite.prediction.test;

import junit.framework.TestCase;
import java.io.IOException;
import musite.PTM;


/**
 *
 * @author LucasYao
 */
public class TestUtilTest extends TestCase{

    /*
    public void testCreateTrainTestSets() throws IOException {
        String xml = "data/humantest.ace.xml";

        TestUtil.createDataSetsWithoutReplacement(xml, 2, "N6-acetyllysine");

    }
     
     */
/*
    public void testCreateTrainTestSets() throws IOException {
        String xml = "data/uniprot_sprot.human.with.disorder.N6acetyllysine.nr90.nr50.xml";

        TestUtil.createDataSetsWithoutReplacement(xml, 100, "N6-acetyllysine",390);

    }

 */

    /*
    public void testSelectProteinByDescription() throws IOException {
        String xml = "data/uniprot_sprot.human.acetylation.nr90.nr50.with.disorder.xml";

        TestUtil.selectProteinByDescription(xml, "Histone");

    }
     
     */
/*
        public void testCreateTrainTestSets() throws IOException {
        String xml = "data/uniprot_corehistone_reviewed.human.with.disorder.N6acetyllysine.xml";

        TestUtil.createDataSetsWithoutReplacement(xml, 22, "N6-acetyllysine");

    }
 
 */
    /*
        public void testCreateBalencedSet() throws IOException {

        String xml = "data/uniprot_sprot.N6-acetyllysine.xml";
        TestUtil.createBalencedDataSetsContainAllPositive(xml, "N6-acetyllysine", PTM.ACETYLATION);


        }

        public void testCreateBalencedSet2() throws IOException {

        String xml = "data/uniprot_sprot.Omega-N-methylarginine.xml";
        TestUtil.createBalencedDataSetsContainAllPositive(xml, "Omega-N-methylarginine", PTM.METHYLATION);


        }
        public void testCreateBalencedSet3() throws IOException {

        String xml = "data/uniprot_sprot.Sulfotyrosine.xml";
        TestUtil.createBalencedDataSetsContainAllPositive(xml, "Sulfotyrosine", PTM.SULFATION);


        }

        public void testCreateBalencedSet4() throws IOException {

        String xml = "data/uniprot_sprot.S-palmitoyl-cysteine.xml";
        TestUtil.createBalencedDataSetsContainAllPositive(xml, "S-palmitoyl cysteine", PTM.PALMITOYLATION);


        }
        public void testCreateBalencedSet5() throws IOException {

        String xml = "data/uniprot_sprot.N6-methyllysine.xml";
        TestUtil.createBalencedDataSetsContainAllPositive(xml, "N6-methyllysine", PTM.METHYLATION);


        }

     */
       public void testCreateBalencedSet6() throws IOException {

        String xml = "data/uniprot_sprot.phosphosite.human.mouse.rat.Ubiquitination.nr90.nr70.xml";
        TestUtil.createBalencedDataSetsContainAllPositive(xml, "Ubiquitinated lysine", PTM.UBIQUITINATION);


        }

        public void testCreateBalencedSet7() throws IOException {

        String xml = "data/uniprot_sprot.phosphosite.human.mouse.rat.SUMOYLATION.nr90.nr70.xml";
        TestUtil.createBalencedDataSetsContainAllPositive(xml, "Sumoylated lysine", PTM.SUMOYLATION);


        }

        public void testCreateBalencedSet8() throws IOException {

        String xml = "data/uniprot_sprot.phosphosite.human.mouse.rat.OGLCNACserine.nr90.nr70.xml";
        TestUtil.createBalencedDataSetsContainAllPositive(xml, "O-GlcNAc serine", PTM.OGLCNAC);


        }

        public void testCreateBalencedSet9() throws IOException {

        String xml = "data/uniprot_sprot.phosphosite.human.mouse.rat.OGLCNACthreonine.nr90.nr70.xml";
        TestUtil.createBalencedDataSetsContainAllPositive(xml, "O-GlcNAc threonine", PTM.OGLCNAC);


        }

}
