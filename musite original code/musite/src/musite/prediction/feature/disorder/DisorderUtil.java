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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import musite.Protein;
import musite.Proteins;
import musite.PTM;
import musite.PTMAnnotationUtil;
import musite.ResidueAnnotationUtil.AnnotationFilter;

import musite.io.xml.CollectionXMLReader;
import musite.io.xml.CollectionXMLWriter;
import musite.io.xml.ProteinsXMLReader;
import musite.io.xml.ProteinsXMLWriter;

import musite.util.AminoAcid;
import musite.util.StringUtil;

/**
 *
 * @author Jianjiong Gao
 */
public class DisorderUtil {

    public static final String DISORDER = "disorder";

    public static ProteinsXMLReader getDisorderXMLReader() {
        return getDisorderXMLReader((ProteinsXMLReader)null);
    }

    public static ProteinsXMLReader getDisorderXMLReader(Proteins proteins) {
        return getDisorderXMLReader(ProteinsXMLReader.createReader(proteins, true));
    }

    public static ProteinsXMLReader getDisorderXMLReader(ProteinsXMLReader reader) {
        if (reader==null)
            reader = ProteinsXMLReader.createReader();
        reader.putProteinFieldReader(DISORDER, CollectionXMLReader.createDoubleCollectionReader());
        return reader;
    }

    public static ProteinsXMLWriter getDisorderXMLWriter() {
        return getDisorderXMLWriter(null);
    }
    public static ProteinsXMLWriter getDisorderXMLWriter(ProteinsXMLWriter writer) {
        if (writer==null)
            writer = ProteinsXMLWriter.createWriter();
        writer.putProteinFieldWriter(DISORDER, new CollectionXMLWriter());
        return writer;
    }

    public static List<Double> extractDisorder(final Protein protein) {
        if (protein==null)
            throw new IllegalArgumentException();

        Object obj = protein.getInfo(DISORDER);
        if (obj instanceof List) {
            List<Double> disorder = (List<Double>)obj;
            return disorder;
        }

        return null;
    }

    public static List<Double> extractDisorder(
                final Protein protein,
                final PTM ptm,
                final Set<AminoAcid> aas) {
        List<Double> scores = extractDisorder(protein);
        if (scores==null)
            return null;

        Set<Integer> sites = PTMAnnotationUtil.getSites(protein, ptm, aas);
        if (sites==null)
            return null;

        List<Double> res = new ArrayList<Double>(sites.size());
        for (int site : sites) {
            res.add(scores.get(site));
        }

        return res;
    }

    public static List<Double> extractDisorder(
                final Protein protein,
                final PTM ptm,
                final AnnotationFilter filter) {
        List<Double> scores = extractDisorder(protein);
        if (scores==null)
            return null;

        Set<Integer> sites = PTMAnnotationUtil.getSites(protein, ptm, filter);
        if (sites==null)
            return null;

        List<Double> res = new ArrayList<Double>(sites.size());
        for (int site : sites) {
            res.add(scores.get(site));
        }

        return res;
    }

    public static List<Double> extractDisorder(final Protein protein, AminoAcid aa) {
        List<Double> scores = extractDisorder(protein);
        if (scores==null)
            return null;

        String seq = protein.getSequence();
        if (seq==null)
            return null;

        Set<Integer> locs = StringUtil.findAll(seq, ""+aa.getOneLetter(), true);
        List<Double> res = new ArrayList<Double>(locs.size());
        for (int loc : locs) {
            res.add(scores.get(loc));
        }

        return res;
    }

    public static void integrateDisorder(final Protein protein,
            List<Double> disorder) {
        if (protein==null || disorder==null)
            throw new IllegalArgumentException();

        String sequence = protein.getSequence();
        if (sequence!=null && sequence.length()!=disorder.size())
            throw new IllegalArgumentException("The number of disorder scores " +
                    "and the sequence length are not the same.");

        protein.putInfo(DISORDER, disorder);
    }

//    /**
//     *
//     * @param proteins
//     * @param disorderFasta
//     * @param headerRule
//     * @throws IOException
//     */
//    public static void integrateDisorder(final Proteins proteins,
//            final String disorderFasta, final HeaderRule headerRule)
//            throws IOException {
//        if (proteins==null || disorderFasta==null) {
//            throw new NullPointerException();
//        }
//
//        final DisorderInfoBatchReader disorderRetriever
//                = new DisorderInfoBatchReaderFromFasta(headerRule);
//
//        final Map<String,List<Double>> mapAccDisorder = disorderRetriever.read(
//                new java.io.FileInputStream(disorderFasta));
//
//        integrateDisorder(proteins, mapAccDisorder);
//    }

    /**
     * 
     * @param phosphoData
     * @param mapAccDisorder
     */
    public static void integrateDisorder(final Proteins proteins,
            final Map<String,List<Double>> mapAccDisorder) {
        if (proteins==null || mapAccDisorder==null) {
            throw new NullPointerException();
        }

        Iterator<Protein> it = proteins.proteinIterator();
        while (it.hasNext()) {
            Protein protein = it.next();
            List<Double> dis = mapAccDisorder.get(protein.getAccession());
            if (dis==null) {
                System.err.println("Disorder information is not available for "
                        +protein.getAccession());
                continue;
            }

            protein.putInfo(DISORDER, dis);

        }
    }

    public static Map<String,List<Double>> predictDisorder(
                final Proteins proteins,
                final DisorderPredictor disorderPredictor,
                final boolean skipExist) {
        Map<String,List<Double>> result = new LinkedHashMap();
        Iterator<Protein> it = proteins.proteinIterator();
        System.out.println(""+proteins.proteinCount());
        int i = 0;
        while (it.hasNext()) {
            if(++i%100==0)
                 System.out.println(""+i);

            Protein protein = it.next();
            String acc = protein.getAccession();
            Object obj = protein.getInfo(DISORDER);
            if (obj!=null && skipExist) {
                result.put(acc, (List)obj);
            } else {
                String seq = protein.getSequence();
                List<Double> dis = null;
                try {
                    dis = disorderPredictor.predict(seq);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (dis!=null) {
                    result.put(acc, dis);
                    //System.out.println(acc);
                } else {
                    System.err.println("Failed to predict disorder for "+acc);
                }
            }
        }

        return result;
    }

    public static Map<String,List<Double>> extractDisorder(
                final Proteins proteins) {
        Map<String,List<Double>> result = new LinkedHashMap();
        Iterator<Protein> it = proteins.proteinIterator();
        while (it.hasNext()) {
            Protein protein = it.next();
            String acc = protein.getAccession();
            Object obj = protein.getInfo(DISORDER);
            result.put(acc, (List)obj);
        }

        return result;
    }

    public static void integrateDisorder(
                final Proteins proteins,
                final DisorderPredictor disorderPredictor,
                final boolean skipExist) {
        Map<String,List<Double>> dis = predictDisorder(proteins, 
                disorderPredictor, skipExist);
        integrateDisorder(proteins, dis);
    }
}
