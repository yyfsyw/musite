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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Jianjiong Gao
 */
public class ProteinSequenceUtil {

    public final static String ALPHABET = "ARNDCQEGHILKMFPSTWYV";

    public final static Set<Character> AMINOACIDS;
    static {
        AMINOACIDS = new HashSet();
        String alphabet = "ARNDCQEGHILKMFPSTWYV";
        for (char aa : alphabet.toCharArray()) {
            AMINOACIDS.add(aa);
        }
    }

    /**
     *
     * @param proteinSequence
     * @param aminoAcids
     * @return
     */
    public static List<Double> aminoAcidFrequencies(final String proteinSequence,
                                                final String aminoAcidAlphabet) {
        if (aminoAcidAlphabet==null) {
            throw new NullPointerException();
        }

//        if (!checkProteinSequence(aminoAcidAlphabet)) {
//            throw new IllegalArgumentException("Wrong amino acids.");
//        }

        Map<Character,Double> map = aminoAcidFrequencies(proteinSequence);
        int naa = aminoAcidAlphabet.length();
        List<Double> ret = new ArrayList(naa);
        for (char c : aminoAcidAlphabet.toCharArray()) {
             ret.add(map.get(c));
        }

        return ret;
    }

        /**
     *
     * @param proteinSequence
     * @param aminoAcids
     * @return
     */
    public static List<Double> aminoAcidBinaries(final String proteinSequence,
                                                final String aminoAcidAlphabet) {
        if (aminoAcidAlphabet==null) {
            throw new NullPointerException();
        }


        Map<Character,Double> map = aminoAcidFrequencies(proteinSequence);
        int naa = aminoAcidAlphabet.length();
        int nps = proteinSequence.length();
        List<Double> ret = new ArrayList(naa*nps);
        for(int i=0;i<nps;i++)
        {
            char cp = proteinSequence.charAt(i);
            for(int j=0;j<naa;j++)
            {
                char ca = aminoAcidAlphabet.charAt(j);
                if(cp==ca)
                    ret.add(1.0);
                else
                    ret.add(0.0);
            }
        }

        return ret;
    }



     /**
     *
     * @param proteinSequence
     * @param aminoAcids
     * @param substitutionmatrix
     * @return
     */
    public static List<Double> aminoAcidSubmatrix(final String proteinSequence,
                                                final String aminoAcidAlphabet,
                                                final double[][] matrix) {
        if (aminoAcidAlphabet==null) {
            throw new NullPointerException();
        }


        Map<Character,Double> map = aminoAcidFrequencies(proteinSequence);
        int naa = aminoAcidAlphabet.length();
        int nps = proteinSequence.length();
        List<Double> ret = new ArrayList(naa*nps);
        for(int i=0;i<nps;i++)
        {
            char cp = proteinSequence.charAt(i);
            for(int j=0;j<naa;j++)
            {
                char ca = aminoAcidAlphabet.charAt(j);
                ret.add(matrix[ca][cp]);
            }
        }

        return ret;
    }

    public static Map<Character,Double> aminoAcidFrequencies(final String proteinSequence) {
        if (proteinSequence==null) {
            throw new NullPointerException();
        }

//        if (!checkProteinSequence(proteinSequence)) {
//            throw new IllegalArgumentException("Wrong amino acids.");
//        }

        Map<Character,Double> map = new HashMap<Character,Double>();
        for (Character aa : AMINOACIDS) {
            map.put(aa, 0.0);
        }

        int count = 0;
        
        for (Character aa : proteinSequence.toUpperCase().toCharArray()) {
            if (!AMINOACIDS.contains(aa))
                continue;

            map.put(aa, map.get(aa)+1);
            count++;
        }

        if (count>0) {
            for (Character aa : map.keySet()) {
                map.put(aa, map.get(aa)/count);
            }
        }

        return map;
    }

    public static boolean checkProteinSequence(final String proteinSequence) {
        if (proteinSequence==null) {
            throw new NullPointerException();
        }

        for (char c : proteinSequence.toUpperCase().toCharArray()) {
            if (!AMINOACIDS.contains(c)) {
                return false;
            }
        }

        return true;
    }
}
