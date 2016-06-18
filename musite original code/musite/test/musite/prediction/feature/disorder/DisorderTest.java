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

package musite.prediction.feature.disorder;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import musite.Protein;
import musite.Proteins;
import musite.PTM;
import musite.ResidueAnnotationUtil;
import musite.ResidueAnnotationUtil.AnnotationFilter;
import musite.io.MusiteIOUtil;
import musite.io.xml.ProteinsXMLReader;
import musite.util.AminoAcid;

import org.apache.commons.math.stat.StatUtils;
import org.apache.commons.math.stat.inference.TTest;
import org.apache.commons.math.stat.inference.TTestImpl;
import org.apache.commons.math.stat.descriptive.moment.StandardDeviation;

import org.junit.Test;

/**
 *
 * @author gjj
 */
public class DisorderTest {
//    @Test
    public void extractDisorder() throws IOException {
//        String xml = "testData/musite-test.xml";
        String xml = "H:\\Phosphorylation Study\\Soybean\\combined\\site-report.with.disorder.xml";
        ProteinsXMLReader reader = DisorderUtil.getDisorderXMLReader();
        Proteins proteins = MusiteIOUtil.read(reader, xml);
        List<Double> list = new ArrayList<Double>();
        PTM ptm = PTM.PHOSPHORYLATION;
        Set<AminoAcid> aas = EnumSet.of(AminoAcid.SERINE, AminoAcid.THREONINE);
//        Set<AminoAcid> aas = EnumSet.of(AminoAcid.TYROSINE);
        Iterator<Protein> it = proteins.proteinIterator();
        while (it.hasNext()) {
            Protein protein = it.next();
            List<Double> dis = DisorderUtil.extractDisorder(protein, ptm, aas);
            if (dis!=null)
                 list.addAll(dis);
        }
        System.out.println(list.toString());
    }

     @Test
     public void extractMean() throws IOException {
//         String xml = "testData/musite-test.xml";
         String xml = "data/Uniprot/uniprot_sprot.201009.ptm.musite.with.disorder.xml";

         ProteinsXMLReader reader = DisorderUtil.getDisorderXMLReader();
         Proteins proteins = MusiteIOUtil.read(reader, xml);

         TTest ttest = new TTestImpl();
         StandardDeviation std = new StandardDeviation();

         Map<AminoAcid,double[]> mapAAScores = new EnumMap<AminoAcid,double[]>(AminoAcid.class);
         for (AminoAcid aa : AminoAcid.values()) {
            List<Double> scores = new ArrayList<Double>();
            Iterator<Protein> it = proteins.proteinIterator();
            while (it.hasNext()) {
                Protein protein = it.next();
                List<Double> dis = DisorderUtil.extractDisorder(protein, aa);
                if (dis!=null)
                    scores.addAll(dis);
             }
             mapAAScores.put(aa, list2array(scores));
//            System.out.print(aa.toString()+":");
//            System.out.println(""+average(scores)+"("+scores.size()+")");
         }

         Map<String,AminoAcid> mapKeyAA = getMapKeywordAminoAcid();

         for (PTM ptm : PTM.values()) {
             Set<String> keywords = ptm.getUniprotKeywords();
             for (String keyword : keywords) {
                 AnnotationFilter filter = ResidueAnnotationUtil.
                        createAnnotationFilter("keyword", Collections.singleton(keyword), true);

                 double[] scores;
                 {
                     List<Double> list = new ArrayList<Double>();
                     Iterator<Protein> it = proteins.proteinIterator();
                     while (it.hasNext()) {
                         Protein protein = it.next();
                         List<Double> dis = DisorderUtil.extractDisorder(protein, ptm, filter);
                         if (dis!=null)
                             list.addAll(dis);
                     }
                     scores = list2array(list);
                 }

                 System.out.print(ptm.toString());
                 System.out.print("\t"+keyword);
                 if (scores.length==0)
                     System.out.println("\tno_site");
                 else {
                    System.out.print("\t"+scores.length);
                    double mean = StatUtils.mean(scores);
                    System.out.print("\t"+mean);
                    System.out.print("\t"+std.evaluate(scores, mean));

                    AminoAcid aa = mapKeyAA.get(keyword);
                    double[] bg = mapAAScores.get(aa);
                    System.out.print("\t"+aa.toString());
                    System.out.print("\t"+bg.length);
                    mean = StatUtils.mean(bg);
                    System.out.print("\t"+mean);
                    System.out.print("\t"+std.evaluate(bg, mean));

                    double pvalue = -1.0;
                    try {
                        pvalue = ttest.tTest(scores, bg);
                    } catch (Exception e) {
//                        e.printStackTrace();
                    }

                    if (pvalue!=-1.0)
                        System.out.print("\t"+pvalue);

                    System.out.println();
                 }
             }
         }
     }

     private double[] list2array(List<Double> list) {
         double[] res = new double[list.size()];
         for (int i=0; i<list.size(); i++) {
             res[i] = list.get(i);
         }
         return res;
     }

     private Map<String,AminoAcid> getMapKeywordAminoAcid() {
        Map<String,AminoAcid> res = new HashMap<String,AminoAcid>();
        res.put("N-acetylalanine",AminoAcid.ALANINE);
        res.put("N2-acetylarginine",AminoAcid.ARGININE);
        res.put("N-acetylaspartate",AminoAcid.ASPARTIC_ACID);
        res.put("N-acetylcysteine",AminoAcid.CYSTEINE);
        res.put("N-acetylglutamate",AminoAcid.GLUTAMIC_ACID);
        res.put("N-acetylglycine",AminoAcid.GLYCINE);
        res.put("N6-acetyllysine",AminoAcid.LYSINE);
        res.put("N-acetylmethionine",AminoAcid.METHIONINE);
        res.put("N-acetylproline",AminoAcid.PROLINE);
        res.put("N-acetylserine",AminoAcid.SERINE);
        res.put("N-acetylthreonine",AminoAcid.THREONINE);
        res.put("N-acetyltyrosine",AminoAcid.TYROSINE);
        res.put("N-acetylvaline",AminoAcid.VALINE);
        res.put("O-acetylserine",AminoAcid.SERINE);
        res.put("O-acetylthreonine",AminoAcid.THREONINE);
        res.put("ADP-ribosylarginine",AminoAcid.ARGININE);
        res.put("ADP-ribosylasparagine",AminoAcid.ASPARAGINE);
        res.put("ADP-ribosylcysteine",AminoAcid.CYSTEINE);
        res.put("ADP-ribosylserine",AminoAcid.SERINE);
        res.put("PolyADP-ribosyl glutamic acid",AminoAcid.GLUTAMIC_ACID);
        res.put("Alanine amide",AminoAcid.ALANINE);
        res.put("Arginine amide",AminoAcid.ARGININE);
        res.put("Aspartic acid 1-amide",AminoAcid.ASPARTIC_ACID);
        res.put("Asparagine amide",AminoAcid.ASPARAGINE);
        res.put("Cysteine amide",AminoAcid.CYSTEINE);
        res.put("Glutamic acid 1-amide",AminoAcid.GLUTAMIC_ACID);
        res.put("Glutamine amide",AminoAcid.GLUTAMINE);
        res.put("Glycine amide",AminoAcid.GLYCINE);
        res.put("Histidine amide",AminoAcid.HISTIDINE);
        res.put("Isoleucine amide",AminoAcid.ISOLEUCINE);
        res.put("Leucine amide",AminoAcid.LEUCINE);
        res.put("Lysine amide",AminoAcid.LYSINE);
        res.put("Methionine amide",AminoAcid.METHIONINE);
        res.put("Phenylalanine amide",AminoAcid.PHENYLALANINE);
        res.put("Proline amide",AminoAcid.PROLINE);
        res.put("Serine amide",AminoAcid.SERINE);
        res.put("Threonine amide",AminoAcid.THREONINE);
        res.put("Tryptophan amide",AminoAcid.TRYPTOPHAN);
        res.put("Tyrosine amide",AminoAcid.TYROSINE);
        res.put("Valine amide",AminoAcid.VALINE);
        res.put("S-archaeol cysteine",AminoAcid.CYSTEINE);
        res.put("3-methylthioaspartic acid",AminoAcid.ASPARTIC_ACID);
        res.put("Bromohistidine",AminoAcid.HISTIDINE);
        res.put("6'-bromotryptophan",AminoAcid.TRYPTOPHAN);
        res.put("Cholesterol glycine ester",AminoAcid.GLYCINE);
        res.put("Deamidated asparagine",AminoAcid.ASPARAGINE);
        res.put("Deamidated glutamine",AminoAcid.GLUTAMINE);
        res.put("O-decanoyl serine",AminoAcid.SERINE);
        res.put("O-decanoyl threonine",AminoAcid.THREONINE);
        res.put("S-diacylglycerol cysteine",AminoAcid.CYSTEINE);
        res.put("3,4-dihydroxyarginine",AminoAcid.ARGININE);
        res.put("3,4-dihydroxyproline",AminoAcid.PROLINE);
        res.put("4,5-dihydroxylysine",AminoAcid.LYSINE);
        res.put("Asymmetric dimethylarginine",AminoAcid.ARGININE);
        res.put("N4,N4-dimethylasparagine",AminoAcid.ASPARAGINE);
        res.put("N6,N6-dimethyllysine",AminoAcid.LYSINE);
        res.put("Omega-N-methylated arginine",AminoAcid.ARGININE);
        res.put("Symmetric dimethylarginine",AminoAcid.ARGININE);
        res.put("N,N-dimethylproline",AminoAcid.PROLINE);
        res.put("O-8alpha-FAD tyrosine",AminoAcid.TYROSINE);
        res.put("Pros-8alpha-FAD histidine",AminoAcid.HISTIDINE);
        res.put("S-8alpha-FAD cysteine",AminoAcid.CYSTEINE);
        res.put("Tele-8alpha-FAD histidine",AminoAcid.HISTIDINE);
        res.put("S-4a-FMN cysteine",AminoAcid.CYSTEINE);
        res.put("S-6-FMN cysteine",AminoAcid.CYSTEINE);
        res.put("FMN phosphoryl serine",AminoAcid.SERINE);
        res.put("FMN phosphoryl threonine",AminoAcid.THREONINE);
        res.put("Tele-8alpha-FMN histidine",AminoAcid.HISTIDINE);
        res.put("N-formylmethionine",AminoAcid.METHIONINE);
        res.put("N-formylglycine",AminoAcid.GLYCINE);
        res.put("N6-formyllysine",AminoAcid.LYSINE);
        res.put("S-geranylgeranyl cysteine",AminoAcid.CYSTEINE);
        res.put("4-carboxyglutamate",AminoAcid.GLUTAMIC_ACID);
        res.put("3',4'-dihydroxyphenylalanine (DOPA)",AminoAcid.ALANINE);
        res.put("3-hydroxyasparagine",AminoAcid.ASPARAGINE);
        res.put("3-hydroxyaspartate",AminoAcid.ASPARTIC_ACID);
        res.put("3-hydroxyproline",AminoAcid.PROLINE);
        res.put("3-hydroxytryptophan",AminoAcid.TRYPTOPHAN);
        res.put("4-hydroxyarginine",AminoAcid.ARGININE);
        res.put("4-hydroxyproline",AminoAcid.PROLINE);
        res.put("5-hydroxylysine",AminoAcid.LYSINE);
        res.put("D-4-hydroxyvaline",AminoAcid.VALINE);
        res.put("Hydroxyproline",AminoAcid.PROLINE);
        res.put("5-methylarginine",AminoAcid.ARGININE);
        res.put("2-methylglutamine",AminoAcid.GLUTAMINE);
        res.put("Cysteine methyl ester",AminoAcid.CYSTEINE);
        res.put("Glutamate methyl ester (Glu)",AminoAcid.GLUTAMIC_ACID);
        res.put("Leucine methyl ester",AminoAcid.LEUCINE);
        res.put("Lysine methyl ester",AminoAcid.LYSINE);
        res.put("Methylhistidine",AminoAcid.HISTIDINE);
        res.put("N-methylalanine",AminoAcid.ALANINE);
        res.put("N-methylisoleucine",AminoAcid.ISOLEUCINE);
        res.put("N-methylleucine",AminoAcid.LEUCINE);
        res.put("N-methylmethionine",AminoAcid.METHIONINE);
        res.put("N-methylphenylalanine",AminoAcid.ALANINE);
        res.put("N-methylproline",AminoAcid.PROLINE);
        res.put("N-methyltyrosine",AminoAcid.TYROSINE);
        res.put("N4-methylasparagine",AminoAcid.ASPARAGINE);
        res.put("N5-methylarginine",AminoAcid.ARGININE);
        res.put("N5-methylglutamine",AminoAcid.GLUTAMINE);
        res.put("N6-methyllysine",AminoAcid.LYSINE);
        res.put("Omega-N-methylarginine",AminoAcid.ARGININE);
        res.put("Pros-methylhistidine",AminoAcid.HISTIDINE);
        res.put("S-methylcysteine",AminoAcid.CYSTEINE);
        res.put("Tele-methylhistidine",AminoAcid.HISTIDINE);
        res.put("N-myristoyl glycine",AminoAcid.GLYCINE);
        res.put("N(6)-myristoyl lysine",AminoAcid.LYSINE);
        res.put("S-myristoyl cysteine",AminoAcid.CYSTEINE);
        res.put("S-nitrosocysteine",AminoAcid.CYSTEINE);
        res.put("O-octanoyl serine",AminoAcid.SERINE);
        res.put("O-octanoyl threonine",AminoAcid.THREONINE);
        res.put("N-palmitoyl cysteine",AminoAcid.CYSTEINE);
        res.put("N(6)-palmitoyl lysine",AminoAcid.LYSINE);
        res.put("O-palmitoyl serine",AminoAcid.SERINE);
        res.put("O-palmitoyl threonine",AminoAcid.THREONINE);
        res.put("S-palmitoyl cysteine",AminoAcid.CYSTEINE);
        res.put("4-aspartylphosphate",AminoAcid.ASPARTIC_ACID);
        res.put("Phosphoarginine",AminoAcid.ARGININE);
        res.put("Phosphocysteine",AminoAcid.CYSTEINE);
        res.put("Phosphohistidine",AminoAcid.HISTIDINE);
        res.put("Phosphoserine",AminoAcid.SERINE);
        res.put("Phosphothreonine",AminoAcid.THREONINE);
        res.put("Phosphotyrosine",AminoAcid.TYROSINE);
        res.put("Pros-phosphohistidine",AminoAcid.HISTIDINE);
        res.put("Tele-phosphohistidine",AminoAcid.HISTIDINE);
        res.put("Sulfoserine",AminoAcid.SERINE);
        res.put("Sulfothreonine",AminoAcid.THREONINE);
        res.put("Sulfotyrosine",AminoAcid.TYROSINE);
        res.put("N6-methylated lysine",AminoAcid.LYSINE);
        res.put("N6,N6,N6-trimethyllysine",AminoAcid.LYSINE);
        res.put("N,N,N-trimethylalanine",AminoAcid.ALANINE);
        return res;
     }

}