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

package musite.prediction.feature;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import musite.Proteins;
import musite.PTM;

import musite.prediction.feature.disorder.DisorderUtil;

import musite.io.MusiteIOUtil;
import musite.io.xml.ProteinsXMLReader;

import musite.util.AminoAcid;
import musite.util.CollectionUtil;
import musite.util.StringUtil;

/**
 *
 * @author Jianjiong Gao
 */
public class TestFeature extends TestCase {

    public void testDisorderFeature() throws IOException {
//        String dirPro = "testData/musite-test.with.disorder.xml";
////        String dirPro = "data/H.sapiens/H.sapiens.uniprot.v15.14.sprot.v57.14.20100209.phosphoelm.200904.nr50.PKC.with.disorder.musite.xml";
//        Set<AminoAcid> aminoAcids = CollectionUtil.getSet(AminoAcid.SERINE, AminoAcid.THREONINE);
//        PTM ptm = PTM.PHOSPHORYLATION;

        String dirPro = "data/H.sapiens/H.sapiens.uniprot.v15.14.sprot.v57.14.20100209.all.acetyl.musite.xml";
        Set<AminoAcid> aminoAcids = CollectionUtil.getSet(AminoAcid.LYSINE);
        PTM ptm = PTM.ACETYLATION;

        ProteinsXMLReader proReader = DisorderUtil.getDisorderXMLReader();
        Proteins proteins = MusiteIOUtil.read(proReader, dirPro);

        int offset = 0;
        int[] disWindowOffsets = new int[2*offset+1];
        for (int i=0; i<2*offset+1; i++ ) {
            disWindowOffsets[i] = i-offset;
        }
        DisorderFeatureExtractor disorderExtractor = new DisorderFeatureExtractor(disWindowOffsets, false);

        // extract instances
        InstancesExtractorFromProteins posInsExtractor = new InstancesExtractorFromProteins(proteins, aminoAcids);
        posInsExtractor.setExtractOption(InstancesExtractorFromProteins.ExtractOption.MODIFIED_SITES, ptm);
        posInsExtractor.setInstanceFilter(new OffsetInstanceFilter(offset));
        List<Instance> instances_positive = posInsExtractor.fetch(-1);
        System.out.println(instances_positive.size()+" sites were extracted.");

        String dirOut = dirPro+"disorder.txt";
        FileWriter writer = new FileWriter(dirOut);
        BufferedWriter out = new BufferedWriter(writer);
        for (Instance ins : instances_positive) {
            List diss = disorderExtractor.extract(ins, false);
            if (diss==null) continue;
            out.write(StringUtil.implode(diss,"\t"));
            out.newLine();
        }
        out.close();
        writer.close();

        new File(dirOut).delete();
    }
}
