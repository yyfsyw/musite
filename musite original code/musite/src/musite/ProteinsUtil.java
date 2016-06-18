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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import musite.util.AminoAcid;
import musite.util.MultiMap;
import musite.util.SamplingUtil;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jianjiong Gao
 */
public class ProteinsUtil {

    private ProteinsUtil() {
        
    }

    /**
     *
     * @param proteins
     * @param ptm
     * @return
     */
    public static int countProteins(Proteins proteins, PTM ptm) {
        return countProteins(proteins, ptm, null);
    }

    /**
     * 
     * @param proteins
     * @param ptm
     * @param enzymes
     * @return
     */
    public static int countProteins(Proteins proteins, PTM ptm, Set<String> enzymes) {
        return countProteins(proteins, ptm, enzymes, null);
    }

    /**
     * 
     * @param proteins
     * @param ptm
     * @param enzymes
     * @param aminoAcids
     * @return
     */
    public static int countProteins(Proteins proteins, PTM ptm, Set<String> enzymes, Set<AminoAcid> aminoAcids) {
        if (proteins==null)
            throw new IllegalArgumentException();

        if (ptm==null)
            return proteins.proteinCount();

        int count = 0;

        Iterator<Protein> it = proteins.proteinIterator();
        while (it.hasNext()) {
            Set<Integer> sites = PTMAnnotationUtil.getSites(it.next(), ptm, aminoAcids, enzymes);
            if (sites!=null && !sites.isEmpty()) {
                count++;
            }
        }

        return count;
    }

    /**
     *
     * @param proteins
     * @param aminoAcids
     * @param ptm
     * @return
     */
    public static int countSites(Proteins proteins, PTM ptm) {
        return countSites(proteins, ptm.getAminoAcids(), ptm);
    }

    /**
     * 
     * @param proteins
     * @param aminoAcids
     * @return
     */
    public static int countSites(Proteins proteins, Set<AminoAcid> aminoAcids) {
        return countSites(proteins, aminoAcids, null);
    }

    /**
     * 
     * @param proteins
     * @param aminoAcids
     * @param ptm
     * @return
     */
    public static int countSites(Proteins proteins, Set<AminoAcid> aminoAcids, PTM ptm) {
        return countSites(proteins, aminoAcids, ptm, null);
    }

    /**
     *
     * @param proteins
     * @param ptm
     * @param enzyme
     * @return
     */
    public static int countSites(Proteins proteins, Set<AminoAcid> aminoAcids,
            PTM ptm, Set<String> enzymes) {
        if (proteins==null || aminoAcids==null || aminoAcids.isEmpty())
            throw new IllegalArgumentException();

        int count = 0;
        if (ptm==null) { // residue
            Iterator<Protein> it = proteins.proteinIterator();
            Set<Character> aas = AminoAcid.oneLetters(aminoAcids);
            while (it.hasNext()) {
                Protein protein = it.next();
                String proteinSeq = protein.getSequence().toUpperCase();
                for (char aa : aas) {
                    count += StringUtils.countMatches(proteinSeq, ""+aa);
                }
            }
        } else {
            Iterator<Protein> it = proteins.proteinIterator();
            while (it.hasNext()) {
                Set<Integer> sites = PTMAnnotationUtil.getSites(it.next(), ptm, aminoAcids, enzymes);
                if (sites!=null && !sites.isEmpty()) {
                    count += sites.size();
                }
            }
        }

        return count;
    }

    public static Map<String,Integer> countSitesByEnzymes(Proteins proteins, PTM ptm) {
        if (proteins==null || ptm==null)
            throw new IllegalArgumentException();

        Map<String,Integer> map = new HashMap();
        Iterator<Protein> it = proteins.proteinIterator();
        while (it.hasNext()) {
            MultiMap<Integer,Map<String, Object>> mm =
                    PTMAnnotationUtil.extractPTMAnnotation(it.next(), ptm);
            if (mm!=null) {
                Map<String,Set<Integer>> mapEnzymeSites = new HashMap();
                mapEnzymeSites.put(null, new HashSet()); // no enzyme annotation
                for (Map.Entry<Integer,Collection<Map<String, Object>>> entry : mm.entrySet()) {
                    int site = entry.getKey();
                    for (Map<String, Object> m : entry.getValue()) {
                        Object obj = m.get(PTMAnnotationUtil.PTM_ENZYME);
                        if (obj==null)
                            mapEnzymeSites.get(null).add(site);
                        else if (obj instanceof String) {
                            String enzyme = (String)obj;
                            Set<Integer> sites = mapEnzymeSites.get(enzyme);
                            if (sites==null) {
                                sites = new HashSet();
                                mapEnzymeSites.put(enzyme, sites);
                            }
                            sites.add(site);
                        }
                    }
                }

                for (Map.Entry<String,Set<Integer>> entry : mapEnzymeSites.entrySet()) {
                    String enzyme = entry.getKey();
                    Set<Integer> sites = entry.getValue();
                    Integer count = map.get(enzyme);
                    if (count==null) {
                        map.put(enzyme, sites.size());
                    } else {
                        map.put(enzyme, count+sites.size());
                    }
                }
            }
        }

        return map;
    }

    public enum MergeOperation {
        UNION, INTERSECTION, DIFFERENCE;
    }

    /**
     * Union of proteins.
     * @param proteinsList
     * @return
     */
    public static Proteins mergeProteins(List<Proteins> proteinsList) {
        return mergeProteins(proteinsList, MergeOperation.UNION, MergeOperation.UNION);
    }

    /**
     * Merge proteins with the same accessions.
     * @param proteinsList
     * @param operation
     * @return Proteins.
     */
    public static Proteins mergeProteins(List<Proteins> proteinsList,
            MergeOperation operationOnProteins, MergeOperation operationOnSites) {
        if (proteinsList==null || proteinsList.size()<2) {
            throw new java.lang.IllegalArgumentException("Number of Proteins should be larger than 2.");
        }

        Proteins res = new ProteinsImpl();
        switch(operationOnProteins) {
            case UNION: {
                Proteins first = proteinsList.get(0);
                res.addAll(first, true, null, Proteins.ConfictHandleOption.RENAME);
                int n = proteinsList.size();
                Set<String> accs = new HashSet<String>(first.getProteinsAccessions());
                for (int i=1; i<n; i++) {
                    Proteins proteins = proteinsList.get(i);
                    accs.addAll(proteins.getProteinsAccessions());

                    for (String acc : accs) {
                        Protein protein = proteins.getProtein(acc);
                        Protein curr = res.getProtein(acc);
                        Protein merged = mergeProteins(curr, protein, operationOnSites);
                        if (merged!=null)
                            res.addProtein(merged, Proteins.ConfictHandleOption.OVERWRITE);
                    }
                }
                break;
            }
            case INTERSECTION: {
                Proteins first = proteinsList.get(0);
                int n = proteinsList.size();
                Iterator<Protein> it = first.proteinIterator();
                while (it.hasNext()) {
                    Protein protein = it.next();
                    String acc = protein.getAccession();
                    List<Protein> pros = new ArrayList();
                    for (int i=1; i<n; i++) {
                        Protein pro = proteinsList.get(i).getProtein(acc);
                        if (pro==null) break;
                        pros.add(pro);
                    }

                    if (pros.size()==n-1) {
                        Protein merged = protein;
                        for (Protein pro:pros) {
                            merged = mergeProteins(merged, pro, operationOnSites);
                        }
                        res.addProtein(merged);
                    }
                }
                break;
            }
            case DIFFERENCE: {
                if (proteinsList.size()>2) {
                    throw new java.lang.IllegalArgumentException("DIFFERENCE operation" +
                            "can only be performed on two proteins.");
                }

                Proteins first = proteinsList.get(0);
                Proteins second = proteinsList.get(1);
                Iterator<Protein> it = first.proteinIterator();
                while (it.hasNext()) {
                    Protein protein = it.next();
                    if (second.getProtein(protein.getAccession())==null) {
                        res.addProtein(protein);
                    }
                }
                break;
            }
            default:
                throw new java.lang.UnsupportedOperationException();
        }

        return res;
    }

    /**
     * Merge two proteins.
     * @param pro1 protein 1.
     * @param pro2 protein 2.
     * @return the merged protein.
     */
    public static Protein mergeProteins(Protein pro1, Protein pro2) {
        return mergeProteins(pro1, pro2, MergeOperation.UNION);
    }

    /**
     * 
     * @param pro1
     * @param pro2
     * @param operationOnSites
     * @return
     */
    public static Protein mergeProteins(final Protein pro1, final Protein pro2,
            final MergeOperation operationOnSites) {
        if (pro1==null && pro2==null)
            return null;

        if (pro2==null) {
            Protein pro = new ProteinImpl(pro1);
            if (operationOnSites == MergeOperation.INTERSECTION) {
                ResidueAnnotationUtil.removeAnnotations(pro);
            }
            return pro;
        }

        if (pro1==null) {
            Protein pro = new ProteinImpl(pro2);
            if(operationOnSites == MergeOperation.INTERSECTION
                    || operationOnSites == MergeOperation.DIFFERENCE) {
                ResidueAnnotationUtil.removeAnnotations(pro);
            }
            return pro;
        }

        Protein pro = new ProteinImpl(pro1);
//        ResidueAnnotationUtil.removeAnnotations(pro);
        //Protein pro = new ProteinImpl(pro1, musite.util.CollectionUtil
        //        .getSet(Protein.ACCESSION, Protein.SEQUENCE)); // common sites

        // merge sequence
        String seq = pro2.getSequence();
        if (seq!=null) {
            String currSeq = pro.getSequence();
            if (currSeq==null) {
                pro.setSequence(seq);
            }
//            else {
//                if (!currSeq.equalsIgnoreCase(seq)) {
//                    return pro2;
//                }
//            }
        }

        // merge residue annotations
        Set<String> anntypes = ResidueAnnotationUtil.getAnnotationTypes(pro2);
        if (anntypes!=null) {
            for (String anntype : anntypes) {
                final MultiMap<Integer,Map<String, Object>> mm =
                        ResidueAnnotationUtil.extractAnnotation(pro2, anntype);
                if (operationOnSites == MergeOperation.UNION) {
                    for (Map.Entry<Integer,Collection<Map<String, Object>>> entry : mm.entrySet()) {
                        int site = entry.getKey();
                        Collection<Map<String, Object>> current =
                                ResidueAnnotationUtil.extractAnnotation(pro, site, anntype);
                        for (Map<String, Object> m : entry.getValue()) {
                            if (current==null || !current.contains(m)) // remove redundant annotations
                                ResidueAnnotationUtil.annotate(pro, site, anntype, m);
                        }
                    }
                } else {
                    ResidueAnnotationUtil.removeAnnotations(pro, anntype,
                            new ResidueAnnotationUtil.AnnotationFilter() {
                        public boolean filter(int loc, Map<String, Object> annotation) {
                            Collection<Map<String, Object>> anns2 = mm.get(loc);
                            return (anns2!=null && anns2.contains(annotation))
                                    == (operationOnSites==MergeOperation.DIFFERENCE);
                        }
                    });
                }
            }
        }

        Set<String> info = new HashSet<String>(pro2.getInfoMap().keySet());
        info.remove(ResidueAnnotationUtil.RESIDUE_ANNOTATION);
        pro.copyFrom(pro2, false, info); // copy all fields except RESIDUE_ANNOTATION without replacement
        
        return pro;
    }

    /**
     *
     * @param proteinsList
     * @param ptm
     * @param aminoAcids
     * @return Map:
     *           Key: protein accession;
     *           Value: Map
     *                    Key: location
     *                    Value: index of the Proteins
     */
    public static Map<String, Map<Integer, Set<Integer>>> siteOverlap(
            List<Proteins> proteinsList, PTM ptm, Set<AminoAcid> aminoAcids) {
        if (proteinsList==null || proteinsList.size()<2) {
            throw new IllegalArgumentException();
        }

        Map<String, Map<Integer, Set<Integer>>> result = new HashMap();

        int n = proteinsList.size();
        for (int i=0; i<n; i++) {
            Proteins proteins = proteinsList.get(i);
            Iterator<Protein> it = proteins.proteinIterator();
            while (it.hasNext()) {
                Protein protein = it.next();
                String acc = protein.getAccession();
                Set<Integer> sites = musite.PTMAnnotationUtil.getSites(protein, ptm, aminoAcids);
                if (sites==null || sites.isEmpty())
                    continue;

                Map<Integer, Set<Integer>> map = result.get(acc);
                if (map==null) {
                    map = new TreeMap();
                    result.put(acc, map);
                }

                for (Integer site:sites) {
                    Set<Integer> set = map.get(site);
                    if (set==null) {
                        set = new TreeSet();
                        map.put(site, set);
                    }
                    set.add(i);
                }
            }
        }        

        return result;
    }

    public static Proteins sampleProteins(Proteins proteins, int n, Proteins.ProteinFilter filter) {
        Iterator<Protein> it = proteins.proteinIterator();
        List<Protein> list = new ArrayList<Protein>();

        while (it.hasNext()) {
            Protein protein = it.next();
            if (filter.filter(protein)) {
                list.add(protein);
            }
        }
        List<Protein> sample;
        if(list.size()>n)
            sample = SamplingUtil.resampleWithoutReplacement(list, n);
        else
            sample = list;
        Proteins res = new ProteinsImpl();
        for (Protein pro : sample) {
            res.addProtein(pro);
        }

        return res;
    }
}
