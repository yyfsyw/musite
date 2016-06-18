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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import musite.util.AminoAcid;
import musite.util.CollectionUtil;

/**
 *
 * @author Jianjiong Gao
 */
public enum PTM {
    PHOSPHORYLATION("Phosphorylation",
        EnumSet.of(AminoAcid.SERINE,
                   AminoAcid.THREONINE,
                   AminoAcid.TYROSINE,
                   AminoAcid.HISTIDINE,
                   AminoAcid.ASPARTIC_ACID,
                   AminoAcid.ARGININE,
                   AminoAcid.CYSTEINE),
        CollectionUtil.getSet("4-aspartylphosphate",
                    "Phosphoarginine",
                    "Phosphocysteine",
                    "Phosphohistidine",
                    "Phosphoserine",
                    "Phosphothreonine",
                    "Phosphotyrosine",
                    "Pros-phosphohistidine",
                    "Tele-phosphohistidine")),
    ACETYLATION("Acetylation",
        EnumSet.of(AminoAcid.ALANINE,
                   AminoAcid.ARGININE,
                   AminoAcid.ASPARTIC_ACID,
                   AminoAcid.CYSTEINE,
                   AminoAcid.GLUTAMIC_ACID,
                   AminoAcid.GLYCINE,
                   AminoAcid.LYSINE,
                   AminoAcid.METHIONINE,
                   AminoAcid.PROLINE,
                   AminoAcid.SERINE,
                   AminoAcid.THREONINE,
                   AminoAcid.TYROSINE,
                   AminoAcid.VALINE),
        CollectionUtil.getSet("N-acetylalanine",
                    "N2-acetylarginine",
                    "N-acetylaspartate",
                    "N-acetylcysteine",
                    "N-acetylglutamate",
                    "N-acetylglycine",
                    "N6-acetyllysine",
                    "N-acetylmethionine",
                    "N-acetylproline",
                    "N-acetylserine",
                    "N-acetylthreonine",
                    "N-acetyltyrosine",
                    "N-acetylvaline",
                    "O-acetylserine",
                    "O-acetylthreonine")), //TODO: N-terminal methionine
    AMIDATION("Amidation",
        EnumSet.of(AminoAcid.ALANINE,
                   AminoAcid.ARGININE,
                   AminoAcid.ASPARTIC_ACID,
                   AminoAcid.ASPARAGINE,
                   AminoAcid.CYSTEINE,
                   AminoAcid.GLUTAMIC_ACID,
                   AminoAcid.GLUTAMINE,
                   AminoAcid.GLYCINE,
                   AminoAcid.HISTIDINE,
                   AminoAcid.ISOLEUCINE,
                   AminoAcid.LEUCINE,
                   AminoAcid.LYSINE,
                   AminoAcid.METHIONINE,
                   AminoAcid.PHENYLALANINE,
                   AminoAcid.PROLINE,
                   AminoAcid.SERINE,
                   AminoAcid.THREONINE,
                   AminoAcid.TRYPTOPHAN,
                   AminoAcid.TYROSINE,
                   AminoAcid.VALINE),
        CollectionUtil.getSet("Alanine amide",
                    "Arginine amide",
                    "Aspartic acid 1-amide",
                    "Asparagine amide",
                    "Cysteine amide",
                    "Glutamic acid 1-amide",
                    "Glutamine amide",
                    "Glycine amide",
                    "Histidine amide",
                    "Isoleucine amide",
                    "Leucine amide",
                    "Lysine amide",
                    "Methionine amide",
                    "Phenylalanine amide",
                    "Proline amide",
                    "Serine amide",
                    "Threonine amide",
                    "Tryptophan amide",
                    "Tyrosine amide",
                    "Valine amide")),
    BETA_METHYLTHIOLATION("Beta-methylthiolation",
        EnumSet.of(AminoAcid.ASPARTIC_ACID),
        CollectionUtil.getSet("3-methylthioaspartic acid")),
    S_DIACYLGLYCEROL_CYSTEINE("S-diacylglycerol cysteine",
        EnumSet.of(AminoAcid.CYSTEINE),
        CollectionUtil.getSet("S-diacylglycerol cysteine")),
    GERANYL_GERANYLATION("Geranyl-geranylation",
        EnumSet.of(AminoAcid.CYSTEINE),
        CollectionUtil.getSet("S-geranylgeranyl cysteine")),
    HYDROXYLATION("Hydroxylation",
        EnumSet.of(AminoAcid.ALANINE,
                   AminoAcid.ASPARAGINE,
                   AminoAcid.ASPARTIC_ACID,
                   AminoAcid.PROLINE,
                   AminoAcid.TRYPTOPHAN,
                   AminoAcid.ARGININE,
                   AminoAcid.PROLINE,
                   AminoAcid.LYSINE,
                   AminoAcid.VALINE,
                   AminoAcid.PROLINE),
        CollectionUtil.getSet("3',4'-dihydroxyphenylalanine (DOPA)",
                    "3-hydroxyasparagine",
                    "3-hydroxyaspartate",
                    "3-hydroxyproline",
                    "3-hydroxytryptophan",
                    "4-hydroxyarginine",
                    "4-hydroxyproline",
                    "5-hydroxylysine",
                    "D-4-hydroxyvaline",
                    "Hydroxyproline")),
    METHYLATION("Methylation",
        EnumSet.of(AminoAcid.ARGININE,
                   AminoAcid.GLUTAMINE,
                   AminoAcid.CYSTEINE,
                   AminoAcid.GLUTAMIC_ACID,
                   AminoAcid.LEUCINE,
                   AminoAcid.LYSINE,
                   AminoAcid.HISTIDINE,
                   AminoAcid.ALANINE,
                   AminoAcid.ISOLEUCINE,
                   AminoAcid.LEUCINE,
                   AminoAcid.METHIONINE,
                   AminoAcid.ALANINE,
                   AminoAcid.PROLINE,
                   AminoAcid.TYROSINE,
                   AminoAcid.ASPARAGINE,
                   AminoAcid.ARGININE,
                   AminoAcid.GLUTAMINE,
                   AminoAcid.LYSINE,
                   AminoAcid.ARGININE,
                   AminoAcid.HISTIDINE,
                   AminoAcid.CYSTEINE,
                   AminoAcid.HISTIDINE),
        CollectionUtil.getSet("5-methylarginine",
                    "2-methylglutamine",
                    "Cysteine methyl ester",
                    "Glutamate methyl ester (Glu)",
                    "Leucine methyl ester",
                    "Lysine methyl ester",
                    "Methylhistidine",
                    "N-methylalanine",
                    "N-methylisoleucine",
                    "N-methylleucine",
                    "N-methylmethionine",
                    "N-methylphenylalanine",
                    "N-methylproline",
                    "N-methyltyrosine",
                    "N4-methylasparagine",
                    "N5-methylarginine",
                    "N5-methylglutamine",
                    "N6-methyllysine",
                    "Omega-N-methylarginine",
                    "Pros-methylhistidine",
                    "S-methylcysteine",
                    "Tele-methylhistidine")), //TODO: methylation for both terminals
    MYRISTOYLATION("Myristoylation",
        EnumSet.of(AminoAcid.GLYCINE,
                   AminoAcid.LYSINE,
                   AminoAcid.CYSTEINE),
        CollectionUtil.getSet("N-myristoyl glycine",
                    "N(6)-myristoyl lysine",
                    "S-myristoyl cysteine")),
    PALMITOYLATION("Palmitoylation",
        EnumSet.of(AminoAcid.CYSTEINE,
                   AminoAcid.LYSINE,
                   AminoAcid.SERINE,
                   AminoAcid.THREONINE,
                   AminoAcid.CYSTEINE),
        CollectionUtil.getSet("N-palmitoyl cysteine",
                    "N(6)-palmitoyl lysine",
                    "O-palmitoyl serine",
                    "O-palmitoyl threonine",
                    "S-palmitoyl cysteine")),
    SULFATION("Sulfation", 
        EnumSet.of(AminoAcid.SERINE,
                   AminoAcid.THREONINE,
                   AminoAcid.TYROSINE),
        CollectionUtil.getSet("Sulfoserine",
                    "Sulfothreonine",
                    "Sulfotyrosine")),
    TRIMETHYLATION("Trimethylation",
        EnumSet.of(AminoAcid.LYSINE,
                   AminoAcid.ALANINE),
        CollectionUtil.getSet("N6-methylated lysine",
                    "N6,N6,N6-trimethyllysine",
                    "N,N,N-trimethylalanine")),

    ADP_RIBOSYLATION("ADP-ribosylation",
        EnumSet.of(AminoAcid.ARGININE,
                   AminoAcid.ASPARAGINE,
                   AminoAcid.CYSTEINE,
                   AminoAcid.SERINE),
        CollectionUtil.getSet("ADP-ribosylarginine",
                    "ADP-ribosylasparagine",
                    "ADP-ribosylcysteine",
                    "ADP-ribosylserine",
                    "PolyADP-ribosyl glutamic acid")),
    S_ARCHAEOL("S-archaeol",
        EnumSet.of(AminoAcid.CYSTEINE),
        CollectionUtil.getSet("S-archaeol cysteine")),
    BROMINATION("Bromination",
        EnumSet.of(AminoAcid.HISTIDINE,
                   AminoAcid.TRYPTOPHAN),
        CollectionUtil.getSet("Bromohistidine",
                    "6'-bromotryptophan")),
    CHOLESTEROL("Cholesterol",
        EnumSet.of(AminoAcid.GLYCINE),
        CollectionUtil.getSet("Cholesterol glycine ester")),
    DEAMIDATION("Deamidation",
        EnumSet.of(AminoAcid.ASPARAGINE,
                   AminoAcid.GLUTAMINE),
        CollectionUtil.getSet("Deamidated asparagine",
                    "Deamidated glutamine")),
    N_DECANOATE("n-Decanoate",
        EnumSet.of(AminoAcid.SERINE,
                   AminoAcid.THREONINE),
        CollectionUtil.getSet("O-decanoyl serine",
                    "O-decanoyl threonine")),
    DIHYDROXYLATION("Dihydroxylation",
        EnumSet.of(AminoAcid.ARGININE,
                   AminoAcid.PROLINE,
                   AminoAcid.LYSINE),
        CollectionUtil.getSet("3,4-dihydroxyarginine",
                    "3,4-dihydroxyproline",
                    "4,5-dihydroxylysine")),
    DIMETHYLATION("Dimethylation",
        EnumSet.of(AminoAcid.ARGININE,
                   AminoAcid.ASPARAGINE,
                   AminoAcid.LYSINE,
                   AminoAcid.ARGININE,
                   AminoAcid.ARGININE,
                   AminoAcid.PROLINE),
        CollectionUtil.getSet("Asymmetric dimethylarginine",
                    "N4,N4-dimethylasparagine",
                    "N6,N6-dimethyllysine",
                    "Omega-N-methylated arginine",
                    "Symmetric dimethylarginine",
                    "N,N-dimethylproline")),
    FAD("FAD",
        EnumSet.of(AminoAcid.TYROSINE,
                   AminoAcid.HISTIDINE,
                   AminoAcid.CYSTEINE,
                   AminoAcid.HISTIDINE),
        CollectionUtil.getSet("O-8alpha-FAD tyrosine",
                    "Pros-8alpha-FAD histidine",
                    "S-8alpha-FAD cysteine",
                    "Tele-8alpha-FAD histidine")),
    FMN_CONJUGATION("FMN conjugation",
        EnumSet.of(AminoAcid.CYSTEINE,
                   AminoAcid.CYSTEINE,
                   AminoAcid.SERINE,
                   AminoAcid.THREONINE,
                   AminoAcid.HISTIDINE),
        CollectionUtil.getSet("S-4a-FMN cysteine",
                    "S-6-FMN cysteine",
                    "FMN phosphoryl serine",
                    "FMN phosphoryl threonine",
                    "Tele-8alpha-FMN histidine")),
    FORMYLATION("Formylation",
        EnumSet.of(AminoAcid.METHIONINE,
                   AminoAcid.GLYCINE,
                   AminoAcid.LYSINE),
        CollectionUtil.getSet("N-formylmethionine",
                    "N-formylglycine",
                    "N6-formyllysine")),
    GAMMA_CARBOXYGLUTAMIC_ACID("Gamma-carboxyglutamic acid",
        EnumSet.of(AminoAcid.GLUTAMIC_ACID),
        CollectionUtil.getSet("4-carboxyglutamate")),
    S_NITROSYLATION("S-Nitrosylation",
        EnumSet.of(AminoAcid.CYSTEINE),
        CollectionUtil.getSet("S-nitrosocysteine")),
    N_OCTANOATE("n-Octanoate",
        EnumSet.of(AminoAcid.SERINE,
                   AminoAcid.THREONINE),
        CollectionUtil.getSet("O-octanoyl serine",
                    "O-octanoyl threonine")),

    UBIQUITINATION("Ubiquitination", EnumSet.of(AminoAcid.LYSINE), CollectionUtil.getSet("Ubiquitinated lysine")),
    SUMOYLATION("SUMOylation", EnumSet.of(AminoAcid.LYSINE), CollectionUtil.getSet("Sumoylated lysine")),
    OGLCNAC("O-GlcNAc glycosylation", EnumSet.of(AminoAcid.SERINE, AminoAcid.THREONINE), CollectionUtil.getSet("O-GlcNAc serine","O-GlcNAc threonine")),
//    S_NITROSYLATION("S-Nitrosylation", EnumSet.of(AminoAcid.CYSTEINE)),
//    GLYCOSYLATION("Glycosylation", EnumSet.of(AminoAcid.ASPARAGINE, AminoAcid.LYSINE, //hydroxylysine
//        AminoAcid.SERINE, AminoAcid.THREONINE)),
//    UNDEFINED("Undefined PTM", EnumSet.allOf(AminoAcid.class))
                   ;

    PTM(final String description, Set<AminoAcid> aminoAcids, Set<String> keywords) {
        this.description = description;
        this.aminoAcids = aminoAcids;
        this.keywords = keywords;
        oneLetters = null;
    }

    public String getDescription() {
        return description;
    }

    public Set<AminoAcid> getAminoAcids() {
        return aminoAcids;
    }

    public Set<String> getUniprotKeywords() {
        return keywords;
    }

    @Override
    public String toString() {
        return getDescription();
    }

    public Set<Character> getAminoAcidOneLetters() {
        if (oneLetters==null) {
            oneLetters = new HashSet();
            for (AminoAcid aminoAcid : aminoAcids) {
                char letter = aminoAcid.getOneLetter();
                oneLetters.add(letter);
            }
        }

        return oneLetters;
    }

    private final String description;
    private final Set<AminoAcid> aminoAcids;
    private Set<Character> oneLetters;
    private final Set<String> keywords;

    private static final Map<String, PTM> mapKeywordPTM;
    static {
        mapKeywordPTM = new HashMap<String, PTM>();
        for (PTM ptm : PTM.values()) {
            for (String keyword : ptm.keywords) {
                mapKeywordPTM.put(keyword, ptm);
            }
        }
    }

    public static PTM ofKeyword(String keyword) {
        return mapKeywordPTM.get(keyword);
    }
}
