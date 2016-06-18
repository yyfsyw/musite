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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import musite.util.MultiMap;
import musite.util.MultiTreeMap;

/**
 *
 * @author Jianjiong Gao
 */
public final class ResidueAnnotationUtil {

    public static final String RESIDUE_ANNOTATION = "residue-annotation";

    private ResidueAnnotationUtil(){}

    /**
     * Annotate one residue.
     * @param protein protein.
     * @param location residue location starting from 0.
     * @param annType type of annotation.
     */
    public static void annotate(final Protein protein, final int location,
            final String annType) {
        annotate(protein, location, annType, null);
    }

    /**
     * Annotate one residue.
     * @param protein protein.
     * @param location residue location starting from 0.
     * @param annType type of annotation.
     * @param annotation annotation content.
     */
    public static void annotate(final Protein protein, final int location,
            final String annType, final Map<String,Object> annotation) {
        if (protein==null || annType==null) {
            throw new IllegalArgumentException();
        }

        Map<String,MultiMap<Integer,Map<String,Object>>> annotations
                = (Map) protein.getInfo(RESIDUE_ANNOTATION);
        if (annotations==null) {
            annotations = new HashMap();
            protein.putInfo(RESIDUE_ANNOTATION, annotations);
        }

        MultiMap<Integer,Map<String,Object>> mm = annotations.get(annType);
        if (mm==null) {
            mm = new MultiTreeMap();
            annotations.put(annType, mm);
        }

        mm.add(location, annotation);
    }

    /**
     * Remove all annotations.
     * @param protein protein.
     */
    public static void removeAnnotations(final Protein protein) {
        if (protein==null) {
            throw new IllegalArgumentException("Null protein");
        }
        
        protein.removeInfo(RESIDUE_ANNOTATION);
    }

    /**
     * 
     * @param protein
     * @param annType
     */
    public static void removeAnnotations(final Protein protein, final String annType) {
        removeAnnotations(protein, annType, null);
    }

    /**
     *
     * @param protein
     * @param annType
     * @param filter
     */
    public static void removeAnnotations(final Protein protein, final String annType,
            final AnnotationFilter filter) {
        if (protein==null || annType==null) {
            throw new IllegalArgumentException();
        }

        Map<String,MultiMap<Integer,Map<String,Object>>> annotations
                = (Map) protein.getInfo(RESIDUE_ANNOTATION);
        if (annotations==null)
            return;

        if (filter==null)
            annotations.remove(annType);
        else {
            MultiMap<Integer,Map<String,Object>> mm = annotations.get(annType);
            if (mm==null)
                return;
            Iterator<Integer> itSite = mm.keySet().iterator();
            while (itSite.hasNext()) {
                Integer site = itSite.next();
                Collection<Map<String,Object>> anns = mm.get(site);
                Iterator<Map<String,Object>> itAnn = anns.iterator();
                while (itAnn.hasNext()) {
                    if (filter.filter(site, itAnn.next())) {
                        itAnn.remove();
                    }
                }
                if (anns.isEmpty()) {
                    itSite.remove();
                }
            }
            if (mm.isEmpty())
                annotations.remove(annType);
        }

        if (annotations.isEmpty())
            protein.removeInfo(RESIDUE_ANNOTATION);
    }

    public static void retainAnnotations(final Protein protein, final String annType,
            final AnnotationFilter filter) {
        removeAnnotations(protein, annType, invertAnnotationFilter(filter));
    }

    public static Set<String> getAnnotationTypes(final Protein protein) {
        if (protein==null) {
            throw new IllegalArgumentException();
        }

        Map<String,MultiMap<Integer,Map<String,Object>>> annotations
                = (Map) protein.getInfo(RESIDUE_ANNOTATION);
        if (annotations==null)
            return null;

        return Collections.unmodifiableSet(annotations.keySet());
    }

    /**
     * Extract annotation.
     * @param protein protein.
     * @param location residue location starting from 0.
     * @param annType type of annotation.
     * @return the annotations of annType for residue at location in protein.
     */
    public static Collection<Map<String, Object>> extractAnnotation(final Protein protein,
            final int location, final String annType) {
        return extractAnnotation(protein, location, annType, null);
    }

    /**
     * Extract annotation.
     * @param protein protein.
     * @param location residue location starting from 0.
     * @param annType type of annotation.
     * @param filter.
     * @return the annotations of annType for residue at location in protein.
     */
    public static Collection<Map<String, Object>> extractAnnotation(final Protein protein,
            final int location, final String annType, final AnnotationFilter filter) {
        MultiMap<Integer,Map<String, Object>> mm = extractAnnotation(protein, annType, filter);
        if (mm==null)
            return null;

        return mm.get(location);
    }

    /**
     * Extract annotations of one type.
     * @param protein protein.
     * @param annType type of annotation.
     * @return the annotations of annType for residue at location in protein.
     */
    public static MultiMap<Integer,Map<String, Object>> extractAnnotation(final Protein protein,
            final String annType) {
        return extractAnnotation(protein, annType, null);
    }

    /**
     * Extract annotations based on filter criteria.
     * @param protein protein.
     * @param filter filter.
     * @return the annotations:
     *      key: location;
     *      value: annotations.
     */
    public static MultiMap<Integer,Map<String, Object>> extractAnnotation(
            final Protein protein, final String annType, final AnnotationFilter filter) {
        if (protein==null || annType==null) {
            throw new IllegalArgumentException();
        }

        Map<String,MultiMap<Integer,Map<String,Object>>> annotations
                = (Map) protein.getInfo(RESIDUE_ANNOTATION);
        if (annotations==null)
            return null;

        MultiMap<Integer,Map<String,Object>> mm = annotations.get(annType);
        if (mm==null)
            return null;

        if (filter==null)
            return mm;

        MultiMap<Integer,Map<String, Object>> result = new MultiTreeMap();
        for (Map.Entry<Integer, Collection<Map<String, Object>>> entry : mm.entrySet()) {
            Integer loc = entry.getKey();
            for (Map<String, Object> m : entry.getValue()) {
                if (filter.filter(loc, m)) {
                    result.add(loc, m);
                }
            }
        }

        return result;
    }

    /**
     * 
     * @param protein
     * @param annType
     * @return
     */
    public static boolean hasAnnotation(
            final Protein protein, final String annType) {
        return hasAnnotation(protein, annType, null);
    }

    /**
     * 
     * @param protein
     * @param annType
     * @param filter
     * @return
     */
    public static boolean hasAnnotation(
            final Protein protein, final String annType, final AnnotationFilter filter) {
        if (protein==null || annType==null) {
            throw new IllegalArgumentException();
        }

        Map<String,MultiMap<Integer,Map<String,Object>>> annotations
                = (Map) protein.getInfo(RESIDUE_ANNOTATION);
        if (annotations==null)
            return false;

        MultiMap<Integer,Map<String,Object>> mm = annotations.get(annType);
        if (mm==null)
            return false;

        if (filter==null)
            return true;

        for (Map.Entry<Integer, Collection<Map<String, Object>>> entry : mm.entrySet()) {
            Integer loc = entry.getKey();
            for (Map<String, Object> m : entry.getValue()) {
                if (filter.filter(loc, m)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     *
     * @param protein
     * @param annType
     * @param filter
     * @param nr true if one position only count once even if multiple annotations exist
     * @return annotation position histogram starting from both terminals
     */
    public static List<Integer>[] getAnnotationPositionHistogram(final Proteins proteins,
            final String annType, final AnnotationFilter filter, boolean nr) {
        List<Integer>[] res = new List[2];
        res[0] = new ArrayList<Integer>();
        res[1] = new ArrayList<Integer>();

        Iterator<Protein> proIter = proteins.proteinIterator();
        while (proIter.hasNext()) {
            Protein pro = proIter.next();
            MultiMap<Integer,Map<String, Object>> annotation =
                    extractAnnotation(pro, annType, filter);
            if (annotation==null)
                continue;

            int length = pro.getSequence().length();
            for (Map.Entry<Integer,Collection<Map<String, Object>>> entry : annotation.entrySet()) {
                int loc = entry.getKey();
                while (res[0].size()<=loc) {
                    res[0].add(0);
                }
                while (res[1].size()<length-loc) {
                    res[1].add(0);
                }

                int delta = nr ? 1 : entry.getValue().size();
                res[0].set(loc, res[0].get(loc)+delta);
                res[1].set(length-loc-1, res[1].get(length-loc-1)+delta);
            }
        }
        return res;
    }

    public static AnnotationFilter createAnnotationFilter(final String annType,
            final Set<String> anns, final boolean include) {
        if (annType==null || anns==null)
            throw new IllegalArgumentException();
        return new AnnotationFilter() {
                    public boolean filter(int location, Map<String, Object> annotation) {
                        String en = (String)annotation.get(annType);
                        return anns.contains(en)==include;
                    }
        };
    }

    public static AnnotationFilter invertAnnotationFilter(final AnnotationFilter filter) {
        return new AnnotationFilter() {
            public boolean filter(int loc, Map<String, Object> annotation) {
                return !filter.filter(loc, annotation);
            }
        };
    }

    /**
     * Interface for filtering annotations.
     */
    public interface AnnotationFilter {
        /**
         *
         * @loc location.
         * @param annotation residue annotation.
         * @return true if satisfying.
         */
        public boolean filter(int loc, Map<String, Object> annotation);
    }
}
