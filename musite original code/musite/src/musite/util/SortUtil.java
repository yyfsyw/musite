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

package musite.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 *
 * @author Jianjiong Gao
 */
public class SortUtil {

        public static List<Map.Entry> getMapEntryListSortedByValue(Map map, boolean asc) {
                List<Map.Entry> entryList = new ArrayList<Map.Entry>(map.entrySet());
                List<Comparable> values = new ArrayList<Comparable>(map.values());
                List<Integer> indices = sortList(values,asc);
                return shuffle(entryList,indices);
        }

        /**
         * sort the list and return the index sequence
         * @param l
         * @param asc
         * @return
         */
        public static List<Integer> sortList(List l, boolean asc) {
                if (l==null) {
                        throw new java.lang.NullPointerException();
                }
                
                int n = l.size();
                List<Integer> return_this = new ArrayList<Integer>(n);
                for (int i=0; i<n; i++) {
                        return_this.add(i);
                }
                
                for (int i=0; i<n-1; i++) {
                        Comparable c1 = (Comparable)l.get(i);
                        int index = i;
                        for (int j=i+1; j<n; j++) {
                                Comparable c2 = (Comparable)l.get(j);
                                if (asc) {
                                        if (c2.compareTo(c1)<0) { 
                                                c1 = c2;
                                                index = j;
                                        }   
                                } else { // desc
                                        if (c2.compareTo(c1)>0) { 
                                                c1 = c2;
                                                index = j;
                                        }
                                }
                                
                        }
                        
                        if (i!=index) {
                                Collections.swap(l, i, index);
                                Collections.swap(return_this, i, index);
                        }
                }
                
                return return_this;
        }

        // shuffle according to the index
        public static List shuffle(final List l, final List<Integer> indices) {
                if (l==null||indices==null) {
                        throw new java.lang.NullPointerException();
                }
                
                int n = l.size();
                if (n!=indices.size()) {
                        throw new java.lang.IllegalArgumentException("l and indices must have the same size");
                }
                
                List newList = new ArrayList();
                
                for (int i=0; i<n; i++) {
                        newList.add(l.get(indices.get(i)));
                }
                
                return newList;
        }
}
