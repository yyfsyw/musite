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
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Jianjiong Gao
 */
public class SamplingUtil {

    /**
     *
     * @param N
     * @param n
     */
    public static List<Integer> resampleWithReplacement(final int N, final int n) {
        return resampleWithReplacement(N, n, new Random());
    }

    public static List<Integer> resampleWithReplacement(final int N, final int n, final Random rand) {
        return resampleWithReplacement(N, n, null, true, rand);
    }

    public static List<Integer> resampleWithReplacement(final int N, final int n,
            final Set<Integer> exclusiveIndicesFilter) {
        return resampleWithReplacement(N, n, exclusiveIndicesFilter, true);
    }

    public static List<Integer> resampleWithReplacement(final int N, final int n,
            final Set<Integer> indicesFilter, final boolean exclude) {
        return resampleWithReplacement(N, n, indicesFilter, exclude, new Random());
    }

    public static List<Integer> resampleWithReplacement(final int N, final int n,
            Set<Integer> indicesFilter, boolean exclude, final Random rand) {
        if (rand==null) {
            throw new NullPointerException();
        }

        if (N<=0 || n<=0) {
            throw new IllegalArgumentException();
        }

        List<Integer> indices = new ArrayList<Integer>(n);
        for (int i=0; i<n;) {
            int ix = rand.nextInt(N);
            if (indicesFilter!=null && indicesFilter.contains(ix)==exclude)
                continue;
            indices.add(ix);
            i++;
        }
        return indices;
    }

    public static List resampleWithReplacement(final List l, final int n) {
        return resampleWithReplacement(l, n, new Random());
    }

    public static List resampleWithReplacement(final List l, final int n, final Random rand) {
        return resampleWithReplacement(l, n, null, true, rand);
    }

    public static List resampleWithReplacement(final List l, final int n,
            Set<Integer> exclusiveIndicesFilter) {
        return resampleWithReplacement(l, n, exclusiveIndicesFilter, true);
    }

    public static List resampleWithReplacement(final List l, final int n,
            Set<Integer> indicesFilter, boolean exclude) {
        return resampleWithReplacement(l, n, indicesFilter, exclude, new Random());
    }

    public static List resampleWithReplacement(final List l, final int n,
            Set<Integer> indicesFilter, boolean exclude, final Random rand) {
        if (l==null || rand==null) {
            throw new NullPointerException();
        }

        int N = l.size();
        if (N<=0 || n<=0) {
            throw new IllegalArgumentException();
        }

        List ret = new ArrayList(n);
        for (int i=0; i<n; ) {
            int ix = rand.nextInt(N);
            if (indicesFilter!=null && indicesFilter.contains(ix)==exclude)
                continue;
            ret.add(l.get(ix));
            i++;
        }

        return ret;
    }

    public static List<Integer> resampleWithoutReplacement(final int N, final int n) {
        return resampleWithoutReplacement(N, n, new Random());
    }

    public static List<Integer> resampleWithoutReplacement(final int N, final int n, Random rand) {
        return resampleWithoutReplacement(N, n, null, true, rand);
    }

    public static List<Integer> resampleWithoutReplacement(final int N, final int n,
            Set<Integer> exclusiveIndicesFilter) {
        return resampleWithoutReplacement(N, n, exclusiveIndicesFilter, true);
    }

    public static List<Integer> resampleWithoutReplacement(final int N, final int n,
            Set<Integer> indicesFilter, boolean exclude) {
        return resampleWithoutReplacement(N, n, indicesFilter, exclude, new Random());
    }

    public static List<Integer> resampleWithoutReplacement(final int N, final int n,
            Set<Integer> indicesFilter, boolean exclude, final Random rand) {
        if (rand==null) {
            throw new NullPointerException();
        }

        if (N<0 || n<0) {
            throw new IllegalArgumentException();
        }

        HashSet<Integer> set = new HashSet<Integer>(n);
        while (set.size()<n) {
            int ix = rand.nextInt(N);
            if (indicesFilter!=null && indicesFilter.contains(ix)==exclude)
                continue;
            set.add(ix);
        }

        List<Integer> indices = new ArrayList<Integer>(set);
        return indices;
    }

    public static List resampleWithoutReplacement(final List l, final int n) {
        return resampleWithoutReplacement(l, n, new Random());
    }

    public static List resampleWithoutReplacement(final List l, final int n, final Random rand) {
        return resampleWithoutReplacement(l, n, null, true, rand);
    }

    public static List resampleWithoutReplacement(final List l, final int n,
            Set<Integer> exclusiveIndicesFilter) {
        return resampleWithoutReplacement(l, n, exclusiveIndicesFilter, true);
    }

    public static List resampleWithoutReplacement(final List l, final int n,
            Set<Integer> indicesFilter, boolean exclude) {
        return resampleWithoutReplacement(l, n, indicesFilter, exclude, new Random());
    }

    public static List resampleWithoutReplacement(final List l, final int n,
            Set<Integer> indicesFilter, boolean exclude, final Random rand) {
        if (l==null || rand==null) {
            throw new NullPointerException();
        }

        int N = l.size();
        if (N<=0 || n<=0) {
            throw new IllegalArgumentException();
        }

        List<Integer> idx = resampleWithoutReplacement(N,n, indicesFilter, exclude, rand);

        List ret = new ArrayList(n);
        for (int i=0; i<n; i++) {
            int ix = idx.get(i);
            ret.add(l.get(ix));
        }

        return ret;
    }

    /**
     * 
     * @param weights
     * @param n
     * @return
     */
    public static List<Integer> weightedResampleWithReplacement(final List<Double> weights,
                                                                final int n) {
        return weightedResampleWithReplacement(weights, n, new Random());
    }


    public static List<Integer> weightedResampleWithReplacement(final List<Double> weights,
                                                                final int n,
                                                                final Random rand) {
        if (weights==null || rand==null) {
            throw new NullPointerException();
        }

        if (weights.size()==0 || n<=0) {
            throw new IllegalArgumentException();
        }

        List<Integer> indices = new ArrayList<Integer>(n);
        WeightedRandomSelection randSel = new WeightedRandomSelection(weights,rand);
        for (int i=0; i<n; i++) {
            indices.add(randSel.nextIndex());
        }
        return indices;
    }

    public static List weightedResampleWithReplacement(final List l,
                                                       final List<Double> weights,
                                                       final int n) {
        return weightedResampleWithReplacement(l, weights, n, new Random());
    }

    public static List weightedResampleWithReplacement(final List l,
                                                       final List<Double> weights,
                                                       final int n,
                                                       final Random rand) {
        if (l==null || weights==null || rand==null) {
            throw new NullPointerException();
        }

        int nl = l.size();
        if (weights.size()!=nl || nl==0 || n<=0) {
            throw new IllegalArgumentException();
        }

        List ret = new ArrayList(n);
        WeightedRandomSelection randSel = new WeightedRandomSelection(weights, rand);
        for (int i=0; i<n; i++) {
            ret.add(l.get(randSel.nextIndex()));
        }
        return ret;
    }
}
