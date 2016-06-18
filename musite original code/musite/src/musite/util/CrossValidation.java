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
import java.util.Random;

/**
 *
 * @author Jianjiong Gao
 */
public class CrossValidation {

    public static List<Integer> getKFolds(final int N,
                                          final int K) {
        return getKFolds(N, K, new Random());
    }

    public static List<Integer> getKFolds(final int N,
                                          final int K,
                                          final Random rand) {
        if (rand == null) {
            throw new NullPointerException();
        }

        if (K<=0 || N<K) {
            throw new IllegalArgumentException();
        }

        List<Integer> fold = new ArrayList<Integer>(N);

        if (K==1) {
            CollectionUtil.fillList(fold, 0, N);
            return fold;
        }

        for (int i=0; i<K; i++) {
            for (int j=0; j<N/K; j++) {
                fold.add(i*(N/K)+j,i);
            }
        }

        for (int i=0; i<N%K; i++) {
            fold.add((N/K)*K+i,i);
        }

        Collections.shuffle(fold, rand);

        return fold;
    }

    public static Pair<List,List> seperateTrainingTestSet(final List data,
                                                          final double testRatio) {
        return seperateTrainingTestSet(data, testRatio, new Random());
    }

    public static Pair<List,List> seperateTrainingTestSet(final List data,
                                                          final double testRatio,
                                                          final Random rand) {
        if (data==null || rand==null) {
            throw new NullPointerException();
        }

        if (testRatio<0 || testRatio>1) {
            throw new IllegalArgumentException();
        }

        int n = data.size();
        int ntest = (int)Math.round(n*testRatio);

        return seperateTrainingTestSet(data, ntest, rand);
    }

    public static Pair<List,List> seperateTrainingTestSet(final List data,
                                                          final int ntest) {
        return seperateTrainingTestSet(data, ntest, new Random());
    }

    public static Pair<List,List> seperateTrainingTestSet(final List data,
                                                          final int ntest,
                                                          final Random rand) {
        if (data==null || rand==null) {
            throw new NullPointerException();
        }

        int n = data.size();

        if (ntest<0 || ntest>n) {
            throw new IllegalArgumentException("ntest="+n+"; n="+n);
        }

        List train = new ArrayList(data);
        List test = new ArrayList(ntest);

        List<Integer> idxTest = SamplingUtil.resampleWithoutReplacement(n, ntest, rand);
        Collections.sort(idxTest);

        for (int i=ntest-1; i>=0; i--) {
            test.add(train.remove((int) idxTest.get(i)));
        }

        return new Pair<List,List>(train,test);
    }

    public static Pair<List,List> seperateTrainingTestSet(final List data,
                                                          final List<Integer> idxFolds,
                                                          final int testFold) {
        if (data==null || idxFolds==null) {
            throw new NullPointerException();
        }
        
        int n = data.size();
        if (n!=idxFolds.size()) {
            throw  new IllegalArgumentException();
        }

        List train = new ArrayList(n);
        List test = new ArrayList(n);

        for (int i=0; i<n; i++) {
            if (idxFolds.get(i)==testFold) {
                test.add(data.get(i));
            } else {
                train.add(data.get(i));
            }
        }

        return new Pair<List,List>(train,test);
    }
}
