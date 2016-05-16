/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musite;

import java.io.IOException;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import musite.ResidueAnnotationUtil.AnnotationFilter;
import musite.io.MusiteIOUtil;
import musite.io.xml.ProteinsXMLReader;
/**
 *
 * @author gjj
 */
public class AnnotationHistogramTest {
     @Test
     public void histTest() throws IOException {
         String xml = "testData/musite-test.xml";
//         String xml = "data/Uniprot/uniprot_sprot.201009.ptm.musite.xml";

         ProteinsXMLReader reader = ProteinsXMLReader.createReader();
         Proteins proteins = MusiteIOUtil.read(reader, xml);

         for (PTM ptm : PTM.values()) {
             System.out.println(ptm.toString());
             Set<String> keywords = ptm.getUniprotKeywords();
             for (String keyword : keywords) {
                AnnotationFilter filter = ResidueAnnotationUtil.
                        createAnnotationFilter("keyword", Collections.singleton(keyword), true);
                List<Integer>[] hists = PTMAnnotationUtil.getPTMAnnotationPositionHistogram(proteins, ptm, filter, true);

                System.out.print("\t"+keyword);
                if (hists[0].isEmpty())
                    System.out.println("\tno_site");
                else {
                    System.out.print("\t"+sum(hists[0],hists[0].size()));
                    System.out.print("\t"+sum(hists[0],1));
                    System.out.print("\t"+sum(hists[0],2));
                    System.out.print("\t"+sum(hists[0],5));
                    System.out.print("\t"+sum(hists[0],10));
                    System.out.print("\t"+sum(hists[0],20));
                    System.out.print("\t"+sum(hists[1],1));
                    System.out.print("\t"+sum(hists[1],2));
                    System.out.print("\t"+sum(hists[1],5));
                    System.out.print("\t"+sum(hists[1],10));
                    System.out.print("\t"+sum(hists[1],20));
                    System.out.println();
                 }
             }
         }
     }

     public int sum(List<Integer> hist, int n) {
         int res = 0;
         for (int i=0; i<n && i<hist.size(); i++) {
             res += hist.get(i);
         }
         return res;
     }

}