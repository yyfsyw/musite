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

package musite.io.fasta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import musite.Protein;
import musite.Proteins;

/**
 *
 * @author Jianjiong Gao
 */
public class DefaultProteinsFastaWriter extends AbstractProteinsFastaWriter {
    protected int aaPerLine = 80;
    protected int aaPerWord = 80;

    protected ResidueAnnotator residueAnnotator;
    protected boolean displayNumber;

    public void setAAPerline(final int aaPerline) {
        this.aaPerLine = aaPerline;
    }

    public void setAAPerWord(final int aaPerWord) {
        if (aaPerWord<1||aaPerWord>aaPerLine) {
            throw new java.lang.IllegalArgumentException();
        }

        this.aaPerWord = aaPerWord;
    }

    public void setDisplayNumber(final boolean displayNumber) {
        this.displayNumber = displayNumber;
    }

    public void setResidueAnnotator(ResidueAnnotator residueAnnotator) {
        this.residueAnnotator = residueAnnotator;
    }

    protected String formatHeader(final Proteins data, final String acc) {
        Protein protein = data.getProtein(acc);
        if (protein==null) return null;

        return ">"+protein.toString();
    }

    protected List<String> formatSequence(final Proteins data, final String acc) {
        if (data==null || acc==null) {
            throw new java.lang.IllegalArgumentException();
        }

        String sequence = data.getProtein(acc).getSequence();
        if (sequence==null) {
            return null;
        }

        Protein protein = data.getProtein(acc);
        if (protein==null) return null;

        Set<Integer> sites = null;
        if (residueAnnotator!=null) {
            sites = residueAnnotator.indicesOfResidues(protein);
        }

        if (sites==null) {
            sites = new HashSet();
        }

        Iterator<Integer> it = sites.iterator();
        int loc = -1;
        if (it.hasNext()) {
            loc = it.next();
        }

        int len = sequence.length();
        int nseg = (len+aaPerLine-1)/aaPerLine;

        List<String> seqs = new ArrayList();

        StringBuilder line = new StringBuilder();
        StringBuilder word = new StringBuilder();

        for (int iseg=0; iseg<nseg; iseg++) {
            int lineStart = iseg*aaPerLine;
            int lineEnd = (iseg+1)*aaPerLine;
            if (lineEnd > len) {
                lineEnd = len;
            }

            //int nAdded = 0;
            line.setLength(0);
            if (displayNumber) {
                line.append(String.format("%-6d", lineStart+1));
                //nAdded += 6;
            }
            //sb.append(sequence.substring(lineStart, lineEnd));

            int nword = (lineEnd-lineStart+aaPerWord-1)/aaPerWord;
            for (int iword=0; iword<nword; iword++) {
                int wordStart = iword*aaPerWord+lineStart;
                int wordEnd = wordStart+aaPerWord;
                if (wordEnd>len) {
                    wordEnd = len;
                }

                word.setLength(0);
                word.append(sequence.substring(wordStart, wordEnd));

//                if (loc>0) {
                    int nAdded = 0;
                    while (wordStart<=loc && loc<wordEnd) {
                        int site = loc-wordStart+nAdded;

                        String ann = residueAnnotator.annotate(protein, loc);
                        word.replace(site, site+1, ann);
                        nAdded += ann.length()-1;

                        if (it.hasNext()) {
                            loc = it.next();
                        } else {
                            break;
                        }
                    }
//                }

                line.append(word);
                line.append(' ');
                //nAdded++; // space
            }

            line.deleteCharAt(line.length()-1);//remove the last space
            seqs.add(line.toString());
        }

        return seqs;
    }
}
