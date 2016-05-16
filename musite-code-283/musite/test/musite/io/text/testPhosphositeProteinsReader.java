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

package musite.io.text;


import java.io.IOException;

import junit.framework.TestCase;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import musite.Proteins;
import musite.Protein;
import musite.PTM;
import musite.io.MusiteIOUtil;
import musite.io.xml.ProteinsXMLReader;
import musite.io.xml.ProteinsXMLWriter;
import musite.util.AminoAcid;

import musite.misc.othertools.NCBIUtil;

/**
 *
 * @author LucasYao
 */
public class testPhosphositeProteinsReader extends TestCase {

    /*
    public void testPhosphositeProteinsReader() throws IOException {
        String extrainputxml = "Data/download_phosphosite/extrasequences.xml";
        String inputxml = "Data/uniprot_sprot.xml";
        String outputxml = inputxml + ".phosphosite.xml";

        ProteinsXMLReader reader = ProteinsXMLReader.createReader();
        Proteins proteins1 = MusiteIOUtil.read(reader, extrainputxml);
        Proteins proteins = MusiteIOUtil.read(reader, inputxml);
        proteins.addAll(proteins1, true, null, Proteins.ConfictHandleOption.RENAME);
//        Collection<Protein> proteincollection = proteins.proteins();
//        Iterator<Protein> iterator = proteincollection.iterator();


        System.out.println("Processing-------UBIQUITINATION");
        HashMap<AminoAcid,String> map1= new HashMap<AminoAcid,String>();
        map1.put(AminoAcid.LYSINE, "Ubiquitinated lysine");
        PhosphositeProteinsReader Ubiquitineproteinsreader = new PhosphositeProteinsReader(proteins,PTM.UBIQUITINATION, map1);
        String inputfile = "Data/download_phosphosite/Ubiquitination_site_dataset_modified";
        Proteins proteins2 = MusiteIOUtil.read(Ubiquitineproteinsreader, inputfile);

        System.out.println("Processing-------OGLCNAC");
        HashMap<AminoAcid,String> map2= new HashMap<AminoAcid,String>();
        map2.put(AminoAcid.SERINE, "O-GlcNAc serine");
        map2.put(AminoAcid.THREONINE, "O-GlcNAc threonine");
        PhosphositeProteinsReader OGLCNACproteinsreader = new PhosphositeProteinsReader(proteins2,PTM.OGLCNAC, map2);
        String inputfile2 = "Data/download_phosphosite/O-GlcNAc_site_dataset_modified";
        Proteins proteins3 = MusiteIOUtil.read(OGLCNACproteinsreader, inputfile2);

        System.out.println("Processing-------SUMOYLATION");
        HashMap<AminoAcid,String> map3= new HashMap<AminoAcid,String>();
        map3.put(AminoAcid.LYSINE, "Sumoylated lysine");
        PhosphositeProteinsReader Sumoylproteinsreader = new PhosphositeProteinsReader(proteins3,PTM.SUMOYLATION, map3);
        String inputfile3 = "Data/download_phosphosite/Sumoylation_site_dataset_modified";
        Proteins proteinsfinal = MusiteIOUtil.read(Sumoylproteinsreader, inputfile3);


        ProteinsXMLWriter writer = ProteinsXMLWriter.createWriter();
        MusiteIOUtil.write(writer, outputxml, proteinsfinal);
     }
     * *
     */

    public void testGetSequenceFromNCBI() throws IOException
    {
        String id = "NP_001005271";
        String seq = NCBIUtil.getSequenceFromNCBI(id);
    }


}
