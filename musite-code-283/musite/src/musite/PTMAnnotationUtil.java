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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import musite.ResidueAnnotationUtil.AnnotationFilter;

import musite.util.AminoAcid;
import musite.util.MultiMap;

/**
 *
 * @author Jianjiong Gao
 */
public final class PTMAnnotationUtil {

    public static final String ANNOTATION_TYPE_PTM = "ptm";
    public static final String PTM_TYPE = "type";
    public static final String PTM_ENZYME = "enzyme";
    
    private PTMAnnotationUtil() {};

    /**
     * Annotate PTM sites.
     * @param protein protein.
     * @param location residue location starting from 0.
     * @param ptm PTM type.
     */
    public static void annotate(final Protein protein, final Set<Integer> locations,
            final PTM ptm) {
        annotate(protein, locations, ptm, null);
    }

    /**
     * Annotate PTM sites with enzyme.
     * @param protein protein.
     * @param location residue location starting from 0.
     * @param ptm PTM type.
     * @param enzyme enzyme.
     */
    public static void annotate(final Protein protein, final Set<Integer> locations,
            final PTM ptm, final String enzyme) {
        annotate(protein, locations, ptm, enzyme, null);
    }

    /**
     * Annotate PTM sites with enzyme.
     * @param protein protein.
     * @param location residue location starting from 0.
     * @param ptm PTM type.
     * @param enzyme enzyme.
     * @param comments comments such as "by similarity".
     */
    public static void annotate(final Protein protein, final Set<Integer> locations,
            final PTM ptm, final String enzyme, final Map<String, Object> otherInfo) {
        if (ptm==null) {
            throw new IllegalArgumentException("Null PTM");
        }

        for (int location : locations) {
            annotate(protein, location, ptm, enzyme, otherInfo);
        }
    }

    /**
     * Annotate a PTM site.
     * @param protein protein.
     * @param location residue location starting from 0.
     * @param ptm PTM type.
     */
    public static void annotate(final Protein protein, final int location, final PTM ptm) {
        annotate(protein, location, ptm, null);
    }

    /**
     * Annotate a PTM site with enzyme.
     * @param protein protein.
     * @param location residue location starting from 0.
     * @param ptm PTM type.
     * @param enzyme enzyme.
     */
    public static void annotate(final Protein protein, final int location,
            final PTM ptm, final String enzyme) {
        annotate(protein, location, ptm, enzyme, null);
    }

    /**
     * Annotate a PTM site with enzyme.
     * @param protein protein.
     * @param location residue location starting from 0.
     * @param ptm PTM type.
     * @param enzyme enzyme.
     * @param comments comments such as "by similarity".
     */
    public static void annotate(final Protein protein, final int location,
            final PTM ptm, final String enzyme, final Map<String, Object> otherInfo) {
        if (ptm==null) {
            throw new IllegalArgumentException("Null PTM");
        }

        String sequence = protein.getSequence();
        if (sequence!=null) {
            if (location<0 || location>=sequence.length()) {
                throw new IndexOutOfBoundsException();
            }

            char residue = sequence.charAt(location);
            if (!ptm.getAminoAcidOneLetters().contains(residue))
                if (!ptm.getAminoAcidOneLetters().contains(Character.toUpperCase(residue)))
                    throw new java.lang.IllegalStateException(
                            "Protein:"+protein.getAccession()+
                            ", location:"+location+
                            "--"+residue+" is not a site for "+ptm.name());
        } else {
            // TODO: how to deal?
        }

        Map<String,Object> annotation = new HashMap();
        annotation.put(PTM_TYPE, ptm.name());
        if (enzyme!=null)
            annotation.put(PTM_ENZYME, enzyme);
        if (otherInfo!=null)
            annotation.putAll(otherInfo);            
        
        ResidueAnnotationUtil.annotate(protein, location, ANNOTATION_TYPE_PTM, annotation);
    }

    public static void removePTMAnnotation(final Protein protein, final PTM ptm) {
        removePTMAnnotation(protein, ptm, null);
    }

    /**
     *
     * @param protein
     * @param ptm
     * @param filter
     */
    public static void removePTMAnnotation(final Protein protein, final PTM ptm,
            final AnnotationFilter filter) {
        removePTMAnnotation(protein, filter==null&&ptm==null ? null :
                        new AnnotationFilter() {
                            public boolean filter(int loc, Map<String, Object> annotation) {
                                if (filter!=null && !filter.filter(loc, annotation))
                                    return false;
                                if (ptm!=null) {
                                    String type = (String)annotation.get(PTM_TYPE);
                                    if (type==null)
                                        return false;
                                    if (!type.equals(ptm.name()))
                                        return false;
                                }
                                return true;
                            }
                });
    }
    /**
     *
     * @param protein
     * @param ptm
     */
    public static void removePTMAnnotation(final Protein protein,
            final AnnotationFilter filter) {
        if (protein==null) {
            throw new IllegalArgumentException("Null protein");
        }

        ResidueAnnotationUtil.removeAnnotations(protein, ANNOTATION_TYPE_PTM, filter);
    }

    public static void retainPTMAnnotation(final Protein protein, final PTM ptm) {
        retainPTMAnnotation(protein, ptm, null);
    }

    /**
     *
     * @param protein
     * @param ptm
     * @param filter
     */
    public static void retainPTMAnnotation(final Protein protein, final PTM ptm,
            final AnnotationFilter filter) {
        retainPTMAnnotation(protein, filter==null&&ptm==null ? null :
                        new AnnotationFilter() {
                            public boolean filter(int loc, Map<String, Object> annotation) {
                                if (filter!=null && !filter.filter(loc, annotation))
                                    return false;
                                if (ptm!=null) {
                                    String type = (String)annotation.get(PTM_TYPE);
                                    if (type==null)
                                        return false;
                                    if (!type.equals(ptm.name()))
                                        return false;
                                }
                                return true;
                            }
                });
    }
    /**
     *
     * @param protein
     * @param ptm
     */
    public static void retainPTMAnnotation(final Protein protein,
            final AnnotationFilter filter) {
        if (protein==null) {
            throw new IllegalArgumentException("Null protein");
        }

        ResidueAnnotationUtil.retainAnnotations(protein, ANNOTATION_TYPE_PTM, filter);
    }

    /**
     * 
     * @param protein
     * @return
     */
    public static boolean hasSites(final Protein protein) {
        return hasSites(protein, (AnnotationFilter)null);
    }

    /**
     * 
     * @param protein
     * @param ptms
     * @return
     */
    public static boolean hasSites(final Protein protein, final Set<PTM> ptms) {
        return hasSites(protein, ptms, null);
    }

    /**
     *
     * @param protein
     * @param ptms
     * @param filter
     * @return
     */
    public static boolean hasSites(final Protein protein, final Set<PTM> ptms,
            final AnnotationFilter filter) {
        return hasSites(protein, filter==null&&ptms==null ? null :
                        new AnnotationFilter() {
                            public boolean filter(int loc, Map<String, Object> annotation) {
                                if (filter!=null && !filter.filter(loc, annotation))
                                    return false;
                                if (ptms!=null) {
                                    String type = (String)annotation.get(PTM_TYPE);
                                    if (type==null)
                                        return false;
                                    if (!ptms.contains(PTM.valueOf(type)))
                                        return false;
                                }
                                return true;
                            }
                });
    }

    /**
     *
     * @param protein
     * @param filter
     * @return
     */
    public static boolean hasSites(final Protein protein,
            final AnnotationFilter filter) {
        if (protein==null) {
            throw new IllegalArgumentException("Null protein");
        }

        return ResidueAnnotationUtil.hasAnnotation(protein, ANNOTATION_TYPE_PTM, filter);
    }

    /**
     * Get all PTM sites.
     * @param protein protein.
     * @param aminoAcids amino acids.
     * @return PTM sites.
     */
    public static Set<Integer> getSites(final Protein protein) {
        return getSites(protein, (PTM)null);
    }

    /**
     * Get all sites of a ptm.
     * @param protein protein.
     * @param ptm PTM.
     * @return PTM sites.
     */
    public static Set<Integer> getSites(final Protein protein, final PTM ptm) {
        return getSites(protein, ptm, (AnnotationFilter)null);
    }

    /**
     * Get PTM sites of a particular enzyme.
     * @param protein protein.
     * @param ptm PTM.
     * @param aminoAcids amino acids.
     * @param enzyme enzyme.
     * @return PTM sites of a particular enzyme contained in the protein.
     */
    public static Set<Integer> getSites(final Protein protein, final PTM ptm,
            final Set<AminoAcid> aminoAcids, final Set<String> enzymes) {
        MultiMap<Integer,Map<String, Object>> map = extractPTMAnnotation(protein, ptm, aminoAcids, enzymes);
        if (map==null)
            return null;

        return map.keySet();
    }

    /**
     * Get all sites of PTM
     * @param protein protein.
     * @param ptm PTM.
     * @param aminoAcids amino acids.
     * @return all PTM sites contained in the protein.
     */
    public static Set<Integer> getSites(final Protein protein, final PTM ptm,
            final Set<AminoAcid> aminoAcids) {
        return getSites(protein, ptm, aminoAcids, (AnnotationFilter)null);
    }

    /**
     *
     * @param protein
     * @param ptm
     * @param filter
     * @return
     */
    public static Set<Integer> getSites(final Protein protein, final PTM ptm,
            final AnnotationFilter filter) {
        return getSites(protein, ptm, null, filter);
    }

    /**
     * Get PTM sites satisfying the criteria.
     * @param protein protein.
     * @param ptm PTM.
     * @param aminoAcids amino acids.
     * @param filter filter.
     * @return PTM sites satisfying the criteria.
     */
    public static Set<Integer> getSites(final Protein protein, final PTM ptm,
            final Set<AminoAcid> aminoAcids, final AnnotationFilter filter) {
        MultiMap<Integer,Map<String, Object>> map = extractPTMAnnotation(protein, ptm, aminoAcids, filter);
        if (map==null)
            return null;

        return map.keySet();
    }

    /**
     * Get filtered sites.
     * @param protein
     * @param filter
     * @return filtered sites.
     */
    public static Set<Integer> getSites(final Protein protein, final AnnotationFilter filter) {
        MultiMap<Integer,Map<String, Object>> map = extractPTMAnnotation(protein, filter);
        if (map==null)
            return null;

        return map.keySet();
    }

    public static HashSet<String> extractEnzymes(final Protein protein, final PTM ptm) {
        MultiMap<Integer,Map<String, Object>> mm = extractPTMAnnotation(protein, ptm);
        if (mm==null)
            return null;

        HashSet<String> enzymes = new HashSet();
        for (Map<String, Object> ms : mm.allValues()) {
            Object obj = ms.get(PTM_ENZYME);
            if (obj instanceof String)
                enzymes.add((String)obj);
        }

        return enzymes;
    }

    /**
     * 
     * @param protein
     * @param ptm
     * @return
     */
    public static MultiMap<Integer,Map<String, Object>> extractPTMAnnotation(
            final Protein protein, final PTM ptm) {
        return extractPTMAnnotation(protein, ptm, (AnnotationFilter)null);
    }

    /**
     * 
     * @param protein
     * @param ptm
     * @param aminoAcids
     * @param enzymes
     * @return
     */
    public static MultiMap<Integer,Map<String, Object>> extractPTMAnnotation(
            final Protein protein, final PTM ptm, final Set<AminoAcid> aminoAcids,
            final Set<String> enzymes) {
        if (enzymes==null) {
            return extractPTMAnnotation(protein, ptm, aminoAcids);
        }

        return extractPTMAnnotation(protein, ptm, aminoAcids, createEnzymesFilter(enzymes, true));
    }

    /**
     * 
     * @param protein
     * @param ptm
     * @param aminoAcids
     * @return
     */
    public static MultiMap<Integer,Map<String, Object>> extractPTMAnnotation(
            final Protein protein, final PTM ptm, final Set<AminoAcid> aminoAcids) {
        return extractPTMAnnotation(protein, ptm, aminoAcids, (AnnotationFilter)null);
    }

    public static MultiMap<Integer,Map<String, Object>> extractPTMAnnotation(
            final Protein protein, final PTM ptm, final AnnotationFilter filter) {
        return extractPTMAnnotation(protein, ptm, null, filter);
    }

    /**
     * 
     * @param protein
     * @param ptm
     * @param aminoAcids
     * @param filter
     * @return
     */
    public static MultiMap<Integer,Map<String, Object>> extractPTMAnnotation(
            final Protein protein, final PTM ptm, final Set<AminoAcid> aminoAcids,
            final AnnotationFilter filter) {
        final String sequence = protein.getSequence().toUpperCase();
        final Set<Character> aas = AminoAcid.oneLetters(aminoAcids);

        return extractPTMAnnotation(protein, filter==null&&aminoAcids==null&&ptm==null ? null :
                        new AnnotationFilter() {
                            public boolean filter(int loc, Map<String, Object> annotation) {
                                if (filter!=null && !filter.filter(loc, annotation))
                                    return false;
                                if (ptm!=null) {
                                    String type = (String)annotation.get(PTM_TYPE);
                                    if (type==null)
                                        return false;
                                    if (!type.equals(ptm.name()))
                                        return false;
                                }
                                if (aminoAcids!=null) {
                                    if (!aas.contains(sequence.charAt(loc)))
                                        return false;
                                }
                                return true;
                            }
                });
    }

    /**
     * 
     * @param protein
     * @param filter
     * @return
     */
    public static MultiMap<Integer,Map<String, Object>> extractPTMAnnotation(final Protein protein,
            final AnnotationFilter filter) {
        MultiMap<Integer,Map<String, Object>> map =
                ResidueAnnotationUtil.extractAnnotation(protein, ANNOTATION_TYPE_PTM, filter);
        return map;
    }

    public static Collection<Map<String, Object>> extractPTMAnnotation(
            final Protein protein, final int site) {
        return extractPTMAnnotation(protein, site, (AnnotationFilter)null);
    }

    public static Collection<Map<String, Object>> extractPTMAnnotation(
            final Protein protein, final int site, final PTM ptm) {
        return extractPTMAnnotation(protein, site, ptm, null);
    }

    public static Collection<Map<String, Object>> extractPTMAnnotation(
            final Protein protein, final int site, final PTM ptm, final AnnotationFilter filter) {

        return extractPTMAnnotation(protein, site, filter==null&&ptm==null ? null :
                        new AnnotationFilter() {
                            public boolean filter(int loc, Map<String, Object> annotation) {
                                if (filter!=null && !filter.filter(loc, annotation))
                                    return false;
                                if (ptm!=null) {
                                    String type = (String)annotation.get(PTM_TYPE);
                                    if (type==null)
                                        return false;
                                    if (!type.equals(ptm.name()))
                                        return false;
                                }
                                return true;
                            }
                });
    }
    

    public static Collection<Map<String, Object>> extractPTMAnnotation(
            final Protein protein, final int site, final AnnotationFilter filter) {
        return ResidueAnnotationUtil.extractAnnotation(protein, site, ANNOTATION_TYPE_PTM, filter);
    }

    public static List<Integer>[] getPTMAnnotationPositionHistogram(final Proteins proteins, final PTM ptm,
            final AnnotationFilter filter, boolean nr) {
        return ResidueAnnotationUtil.getAnnotationPositionHistogram(proteins, ANNOTATION_TYPE_PTM,
                filter==null&&ptm==null ? null :
                        new AnnotationFilter() {
                            public boolean filter(int loc, Map<String, Object> annotation) {
                                if (filter!=null && !filter.filter(loc, annotation))
                                    return false;
                                if (ptm!=null) {
                                    String type = (String)annotation.get(PTM_TYPE);
                                    if (type==null)
                                        return false;
                                    if (!type.equals(ptm.name()))
                                        return false;
                                }
                                return true;
                            }
                    },
               nr);
    }

    public static AnnotationFilter createEnzymesFilter(final Set<String> enzymes, final boolean include) {
        return ResidueAnnotationUtil.createAnnotationFilter(PTM_ENZYME, enzymes, include);
    }

    public static AnnotationFilter createKeywordsFilter(final Set<String> keywords, final boolean include) {
        return ResidueAnnotationUtil.createAnnotationFilter("keyword", keywords, include);
    }

}
