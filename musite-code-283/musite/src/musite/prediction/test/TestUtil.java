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

import java.util.Collections;

import musite.io.MusiteIOUtil;
import musite.io.xml.ProteinsXMLReader;
import musite.io.xml.ProteinsXMLWriter;
import musite.*;


/**
 *
 * @author LucasYao
 */
public class TestUtil {
    
    public static void createDataSetsWithoutReplacement(String inputxmlname, int samplenum, String ptm, final PTM ptmresidue)
    {
        try
        {
        ProteinsXMLReader reader = ProteinsXMLReader.createReader();
        Proteins proteins = MusiteIOUtil.read(reader, inputxmlname);

        final ResidueAnnotationUtil.AnnotationFilter annFilter =
                //PTMAnnotationUtil.createKeywordsFilter(Collections.singleton("N6-acetyllysine"), true);
                PTMAnnotationUtil.createKeywordsFilter(Collections.singleton(ptm), true);

        Proteins.ProteinFilter positivefilter = new Proteins.ProteinFilter() {
            public boolean filter(Protein protein) {
                return PTMAnnotationUtil.hasSites(protein, Collections.singleton(ptmresidue), annFilter);
            }
        };
        Proteins positivetest = ProteinsUtil.sampleProteins(proteins, samplenum, positivefilter);
        
        Proteins.ProteinFilter negativefilter = new Proteins.ProteinFilter() {
            public boolean filter(Protein protein) {
                return !PTMAnnotationUtil.hasSites(protein, Collections.singleton(ptmresidue), annFilter);
            }
        };
        Proteins negativetest = ProteinsUtil.sampleProteins(proteins, samplenum, negativefilter);

        //proteins.filterProteins(filter);
        Proteins testproteins = new ProteinsImpl();
        testproteins.addAll(positivetest, true, null, Proteins.ConfictHandleOption.RENAME);
        testproteins.addAll(negativetest, true, null, Proteins.ConfictHandleOption.RENAME);

        String testoutxml = inputxmlname.replace(".xml",".test.xml");
        ProteinsXMLWriter writer = ProteinsXMLWriter.createWriter();
        MusiteIOUtil.write(writer, testoutxml, testproteins);

        String trainoutxml = inputxmlname.replace(".xml",".train.xml");
        proteins.removeProteins(testproteins.getProteinsAccessions());
        MusiteIOUtil.write(writer, trainoutxml, proteins);
        }
        catch(Exception e){}
    }

    public static void createDataSetsWithoutReplacement(String inputxmlname, int samplenum, String ptm, final int lengthlimit,final PTM ptmresidue)
    {
        try
        {
        ProteinsXMLReader reader = ProteinsXMLReader.createReader();
        Proteins proteins = MusiteIOUtil.read(reader, inputxmlname);

        final ResidueAnnotationUtil.AnnotationFilter annFilter =
                //PTMAnnotationUtil.createKeywordsFilter(Collections.singleton("N6-acetyllysine"), true);
                PTMAnnotationUtil.createKeywordsFilter(Collections.singleton(ptm), true);

        Proteins.ProteinFilter positivefilter = new Proteins.ProteinFilter() {
            public boolean filter(Protein protein) {
                return PTMAnnotationUtil.hasSites(protein, Collections.singleton(ptmresidue), annFilter)
                        && (protein.getSequence().length()<=lengthlimit);
            }
        };
        Proteins positivetest = ProteinsUtil.sampleProteins(proteins, samplenum, positivefilter);

        Proteins.ProteinFilter negativefilter = new Proteins.ProteinFilter() {
            public boolean filter(Protein protein) {
                return !PTMAnnotationUtil.hasSites(protein, Collections.singleton(ptmresidue), annFilter)
                        && (protein.getSequence().length()<=lengthlimit);
            }
        };
        Proteins negativetest = ProteinsUtil.sampleProteins(proteins, samplenum, negativefilter);

        //proteins.filterProteins(filter);
        Proteins testproteins = new ProteinsImpl();
        testproteins.addAll(positivetest, true, null, Proteins.ConfictHandleOption.RENAME);
        testproteins.addAll(negativetest, true, null, Proteins.ConfictHandleOption.RENAME);

        String testoutxml = inputxmlname.replace(".xml",".test.xml");
        ProteinsXMLWriter writer = ProteinsXMLWriter.createWriter();
        MusiteIOUtil.write(writer, testoutxml, testproteins);

        String trainoutxml = inputxmlname.replace(".xml",".train.xml");
        proteins.removeProteins(testproteins.getProteinsAccessions());
        MusiteIOUtil.write(writer, trainoutxml, proteins);
        }
        catch(Exception e){}
    }

    public static void createBalencedDataSetsContainAllPositive(String inputxmlname, String ptm, final PTM ptmresidue)
    {
        try
        {
        ProteinsXMLReader reader = ProteinsXMLReader.createReader();
        Proteins proteins = MusiteIOUtil.read(reader, inputxmlname);

        final ResidueAnnotationUtil.AnnotationFilter annFilter =
                PTMAnnotationUtil.createKeywordsFilter(Collections.singleton(ptm), true);

        Proteins.ProteinFilter positivefilter = new Proteins.ProteinFilter() {
            public boolean filter(Protein protein) {
                return PTMAnnotationUtil.hasSites(protein, Collections.singleton(ptmresidue), annFilter);

            }
        };

       Proteins balencedset = ProteinsUtil.sampleProteins(proteins, proteins.proteins().size(), positivefilter);
/*
        Proteins balencedset = new ProteinsImpl();
        balencedset.addAll(proteins, true, null, Proteins.ConfictHandleOption.RENAME);
        balencedset.filterProteins(positivefilter);
*/
        proteins.removeProteins(balencedset.getProteinsAccessions());

        Proteins.ProteinFilter negativefilter = new Proteins.ProteinFilter() {
            public boolean filter(Protein protein) {
                return !PTMAnnotationUtil.hasSites(protein, Collections.singleton(ptmresidue), annFilter);

            }
        };
        Proteins negativeset = ProteinsUtil.sampleProteins(proteins, balencedset.proteins().size(), negativefilter);

        balencedset.addAll(negativeset, true, null, Proteins.ConfictHandleOption.RENAME);

        String testoutxml = inputxmlname.replace(".xml",".balenced.xml");
        ProteinsXMLWriter writer = ProteinsXMLWriter.createWriter();
        MusiteIOUtil.write(writer, testoutxml, balencedset);

        }
        catch(Exception e){}
    }


    public static void selectProteinByDescription(String inputxmlname, final String description)
    {
        try
        {
            ProteinsXMLReader reader = ProteinsXMLReader.createReader();
            Proteins proteins = MusiteIOUtil.read(reader, inputxmlname);

            Proteins.ProteinFilter descriptionfilter = new Proteins.ProteinFilter() {
            public boolean filter(Protein protein) {

                if(protein.getDescription().contains(description))
                    return true;
                else
                    return false;
             }
            };
            proteins.filterProteins(descriptionfilter);
            
            String outxml = inputxmlname.replace(".xml","."+description+".xml");
            proteins.removeProteins(proteins.getProteinsAccessions());
            ProteinsXMLWriter writer = ProteinsXMLWriter.createWriter();
            MusiteIOUtil.write(writer, outxml, proteins);
        }
        catch(Exception e){}
    }



}
