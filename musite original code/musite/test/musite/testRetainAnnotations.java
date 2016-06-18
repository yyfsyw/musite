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

package musite;
import junit.framework.TestCase;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import musite.Proteins;
import musite.Protein;
import musite.PTM;
import musite.io.MusiteIOUtil;
import musite.io.xml.ProteinsXMLReader;
import musite.io.xml.ProteinsXMLWriter;
/**
 *
 * @author LucasYao
 */
public class testRetainAnnotations extends TestCase{

    public static void testOnRetainAnnotations1()
    {
        try
        {
       String inputfile = "data/uniprot_sprot.phosphosite.human.mouse.rat.xml";
       String outputfile = "data/uniprot_sprot.phosphosite.human.mouse.rat.Ubiquitination.xml";
//         String inputfile = "data/uniprot_corehistone_reviewed.human.with.disorder.xml";
//         String outputfile = "data/uniprot_corehistone_reviewed.human.with.disorder.N6acetyllysine.xml";

        ProteinsXMLReader reader = ProteinsXMLReader.createReader();
        Proteins proteins = MusiteIOUtil.read(reader, inputfile);
        Collection<Protein> proteincollection = proteins.proteins();
        Iterator<Protein> iterator = proteincollection.iterator();

        final ResidueAnnotationUtil.AnnotationFilter annFilter =
                PTMAnnotationUtil.createKeywordsFilter(Collections.singleton("Ubiquitinated lysine"), true);


        int procount = 0;
        while(iterator.hasNext()){
            System.out.println(++procount);
            Protein protein = iterator.next();
            //do something to protein;
            PTMAnnotationUtil.retainPTMAnnotation(protein, PTM.UBIQUITINATION, annFilter);

            }
  

        ProteinsXMLWriter writer = ProteinsXMLWriter.createWriter();
        MusiteIOUtil.write(writer, outputfile, proteins);
        }
        catch(Exception e){}


    }

    public static void testOnRetainAnnotations2()
    {
        try
        {
       String inputfile = "data/uniprot_sprot.phosphosite.human.mouse.rat.xml";
       String outputfile = "data/uniprot_sprot.phosphosite.human.mouse.rat.OGLCNACserine.xml";
//         String inputfile = "data/uniprot_corehistone_reviewed.human.with.disorder.xml";
//         String outputfile = "data/uniprot_corehistone_reviewed.human.with.disorder.N6acetyllysine.xml";

        ProteinsXMLReader reader = ProteinsXMLReader.createReader();
        Proteins proteins = MusiteIOUtil.read(reader, inputfile);
        Collection<Protein> proteincollection = proteins.proteins();
        Iterator<Protein> iterator = proteincollection.iterator();

        final ResidueAnnotationUtil.AnnotationFilter annFilter =
                PTMAnnotationUtil.createKeywordsFilter(Collections.singleton("O-GlcNAc serine"), true);


        int procount = 0;
        while(iterator.hasNext()){
            System.out.println(++procount);
            Protein protein = iterator.next();
            //do something to protein;
            PTMAnnotationUtil.retainPTMAnnotation(protein, PTM.OGLCNAC, annFilter);

            }


        ProteinsXMLWriter writer = ProteinsXMLWriter.createWriter();
        MusiteIOUtil.write(writer, outputfile, proteins);
        }
        catch(Exception e){}


    }

    public static void testOnRetainAnnotations3()
    {
        try
        {
       String inputfile = "data/uniprot_sprot.phosphosite.human.mouse.rat.xml";
       String outputfile = "data/uniprot_sprot.phosphosite.human.mouse.rat.OGLCNACthreonine.xml";
//         String inputfile = "data/uniprot_corehistone_reviewed.human.with.disorder.xml";
//         String outputfile = "data/uniprot_corehistone_reviewed.human.with.disorder.N6acetyllysine.xml";

        ProteinsXMLReader reader = ProteinsXMLReader.createReader();
        Proteins proteins = MusiteIOUtil.read(reader, inputfile);
        Collection<Protein> proteincollection = proteins.proteins();
        Iterator<Protein> iterator = proteincollection.iterator();

        final ResidueAnnotationUtil.AnnotationFilter annFilter =
                PTMAnnotationUtil.createKeywordsFilter(Collections.singleton("O-GlcNAc threonine"), true);


        int procount = 0;
        while(iterator.hasNext()){
            System.out.println(++procount);
            Protein protein = iterator.next();
            //do something to protein;
            PTMAnnotationUtil.retainPTMAnnotation(protein, PTM.OGLCNAC, annFilter);

            }


        ProteinsXMLWriter writer = ProteinsXMLWriter.createWriter();
        MusiteIOUtil.write(writer, outputfile, proteins);
        }
        catch(Exception e){}


    }

    public static void testOnRetainAnnotations4()
    {
        try
        {
       String inputfile = "data/uniprot_sprot.phosphosite.human.mouse.rat.xml";
       String outputfile = "data/uniprot_sprot.phosphosite.human.mouse.rat.SUMOYLATION.xml";
//         String inputfile = "data/uniprot_corehistone_reviewed.human.with.disorder.xml";
//         String outputfile = "data/uniprot_corehistone_reviewed.human.with.disorder.N6acetyllysine.xml";

        ProteinsXMLReader reader = ProteinsXMLReader.createReader();
        Proteins proteins = MusiteIOUtil.read(reader, inputfile);
        Collection<Protein> proteincollection = proteins.proteins();
        Iterator<Protein> iterator = proteincollection.iterator();

        final ResidueAnnotationUtil.AnnotationFilter annFilter =
                PTMAnnotationUtil.createKeywordsFilter(Collections.singleton("Sumoylated lysine"), true);


        int procount = 0;
        while(iterator.hasNext()){
            System.out.println(++procount);
            Protein protein = iterator.next();
            //do something to protein;
            PTMAnnotationUtil.retainPTMAnnotation(protein, PTM.SUMOYLATION, annFilter);

            }


        ProteinsXMLWriter writer = ProteinsXMLWriter.createWriter();
        MusiteIOUtil.write(writer, outputfile, proteins);
        }
        catch(Exception e){}


    }

}
