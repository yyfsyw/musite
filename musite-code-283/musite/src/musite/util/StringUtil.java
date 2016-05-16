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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Jianjiong Gao
 */
public class StringUtil {

    public static String toOct(String str) {
        if (str==null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            sb.append("\\0"+Integer.toOctalString(c));
        }

        return sb.toString();
    }

    public static InputStream toStream(String str) {
        if (str==null)
            return null;

        return new ByteArrayInputStream(str.getBytes());
    }

    public static String implode(Collection strs, String glue) {
        return implode(strs.toArray(new Object[strs.size()]),glue);
    }

    public static String implode(Object[] strs, String glue) {
        if (strs==null || glue==null) {
            throw new NullPointerException();
        }

        int n = strs.length;
        if (n==0) return new String();

        StringBuilder str = new StringBuilder(strs[0].toString());
        for (int i=1; i<n; i++) {
            str.append(glue+strs[i]);
        }

        return str.toString();
    }

    public static TreeSet<Integer> findAll(final String longStr,
            final String shortStr, boolean ignoreCase) {
        if (longStr==null || shortStr==null) {
            throw new NullPointerException();
        }

        int nl = longStr.length();
        int ns = shortStr.length();

        TreeSet<Integer> locs = new TreeSet<Integer>();
        String longStrCopy = ignoreCase ? longStr.toUpperCase() : longStr;
        String shortStrCopy = ignoreCase ? shortStr.toUpperCase() : shortStr;

        int idx = longStrCopy.indexOf(shortStrCopy);
        while (idx!=-1) {
            locs.add(idx);
            idx = longStrCopy.indexOf(shortStrCopy, idx+1);
        }

        return locs;
    }

    public static TreeSet<Integer> findAll(final String longStr,
            final Set<Character> chs, boolean ignoreCase) {
        if (longStr==null || chs==null) {
            throw new NullPointerException();
        }

        String longStrCopy = ignoreCase ? longStr.toUpperCase() : longStr;

        Set<String> shortStrs = new HashSet();
        for (Character ch : chs) {
            String shortStr = ignoreCase ? (""+ch).toUpperCase() : ""+ch;
            shortStrs.add(shortStr);
        }

        TreeSet<Integer> locs = new TreeSet();

        for (String shortStr : shortStrs) {
            locs.addAll(findAll(longStrCopy, shortStr, false));
        }

        return locs;
    }
}
