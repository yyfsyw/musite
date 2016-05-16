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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Jianjiong Gao
 */
public enum AminoAcid {
    ALANINE      ("Alanine", "Ala", 'A'),
    ARGININE     ("Arginine", "Arg", 'R'),
    ASPARAGINE   ("Asparagine", "Asn", 'N'),
    ASPARTIC_ACID("Aspartic acid", "Asp", 'D'),
    CYSTEINE     ("Cysteine", "Cys", 'C'),
    GLUTAMIC_ACID("Glutamic acid", "Glu", 'E'),
    GLUTAMINE    ("Glutamine", "Gln", 'Q'),
    GLYCINE      ("Glycine", "Gly", 'G'),
    HISTIDINE    ("Histidine", "His", 'H'),
    ISOLEUCINE   ("Isoleucine", "Ile", 'I'),
    LEUCINE      ("Leucine", "Leu", 'L'),
    LYSINE       ("Lysine", "Lys", 'K'),
    METHIONINE   ("Methionine", "Met", 'M'),
    PHENYLALANINE("Phenylalanine", "Phe", 'F'),
    PROLINE      ("Proline", "Pro", 'P'),
    SERINE       ("Serine", "Ser", 'S'),
    THREONINE    ("Threonine", "Thr", 'T'),
    TRYPTOPHAN   ("Tryptophan", "Trp", 'W'),
    TYROSINE     ("Tyrosine", "Tyr", 'Y'),
    VALINE       ("Valine", "Val", 'V');


    AminoAcid(final String fullName, final String threeLetters,
            final char oneLetter) {
        this.fullName = fullName;
        this.threeLetters = threeLetters;
        this.oneLetter = oneLetter;
    }

    public String getFullName() {
        return fullName;
    }

    public String getThreeLetters() {
        return threeLetters;
    }

    public char getOneLetter() {
        return oneLetter;
    }

    public static Set<Character> oneLetters(Set<AminoAcid> aminoAcids) {
        if (aminoAcids==null)
            return null;

        Set<Character> result = new HashSet(aminoAcids.size());
        for (AminoAcid aa : aminoAcids) {
            result.add(aa.getOneLetter());
        }

        return result;
    }

    public String toString() {
        return getFullName()+"("+getOneLetter()+")";
    }
    
    private final String fullName;
    private final String threeLetters;
    private final char oneLetter;

    private static final Map<Character, AminoAcid> ofOneLetter;
    private static final Map<String, AminoAcid> ofName;
    static {
        AminoAcid[] all = AminoAcid.values();
        ofOneLetter = new HashMap<Character, AminoAcid>(all.length);
        ofName = new HashMap<String, AminoAcid>(all.length*2);
        for (AminoAcid aa : all) {
            ofOneLetter.put(aa.getOneLetter(), aa);
            ofName.put(aa.getFullName(), aa);
            ofName.put(aa.getThreeLetters(), aa);
        }
    }

    /**
     *
     * @param oneLetter
     * @return
     */
    public static AminoAcid of(char oneLetter) {
        return ofOneLetter.get(Character.toUpperCase(oneLetter));
    }

    /**
     *
     * @param name full name, 3-letter code, or 1-letter code
     * @return
     */
    public static AminoAcid of(String name) {
        if (name==null)
            return null;

        if (name.length()==1)
            return of(name.charAt(0));

        return ofName.get(name); 
    }
}
