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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Jianjiong Gao
 */
public class CollectionUtil {
    public static void fillList(List l, Object obj, int n) {
        for (int i=0; i<n; i++) {
            l.add(obj);
        }
    }

    public static List subList(List l, int begin, int end) {
        if (l==null) {
            throw new java.lang.NullPointerException();
        }

        if (begin<0 || begin>=end || end>l.size()) {
            throw new java.lang.IndexOutOfBoundsException();
        }

        List ret = new ArrayList();
        for (int i=begin; i<end; i++) {
            ret.add(l.get(i));
        }

        return ret;
    }

    public static Set getSet(Object ... objs) {
        if (objs.length==0) {
            return Collections.emptySet();
        } else if (objs.length==1) {
            return Collections.singleton(objs[0]);
        } else {
            return new HashSet(Arrays.asList(objs));
        }

    }

    public static List getList(Object ... objs) {
        if (objs.length==0) {
            return Collections.emptyList();
        } else if (objs.length==1) {
            return Collections.singletonList(objs[0]);
        } else {
            return new ArrayList(Arrays.asList(objs));
        }
    }
}
