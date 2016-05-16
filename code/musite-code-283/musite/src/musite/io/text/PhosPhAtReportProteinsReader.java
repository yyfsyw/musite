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

package musite.io.text;

import java.io.InputStream;
import java.io.IOException;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import musite.Proteins;
import musite.Protein;
import musite.PTM;
import musite.PTMAnnotationUtil;

import musite.io.Reader;

import musite.util.IOUtil;

/**
 *
 * @author Jianjiong Gao
 */
public class PhosPhAtReportProteinsReader implements Reader<Proteins> {
    private class Filter {
        Set<Integer> charges;
        Double ppm;
        Boolean ambiguous;
        Set<String> pubmed;
    }

    private final Proteins proteins;
    private final Filter filter;

    public PhosPhAtReportProteinsReader(final Proteins proteins) {
        if (proteins==null) {
            throw new NullPointerException();
        }
        
        this.proteins = proteins;
        this.filter = new Filter();
    }

    public void setFilter(final Double ppm,
                     final Boolean ambiguous,
                     final String[] pubmed,
                     final int[] charges) {
        if (charges!=null) {
            filter.charges = new HashSet<Integer>();
            for (int ch : charges) {
                filter.charges.add(ch);
            }
        } else {
            filter.charges = null;
        }

        filter.ppm = ppm;

        filter.ambiguous = ambiguous;

        if (pubmed!=null) {
            filter.pubmed = new HashSet<String>();
            for (String pm : pubmed) {
                filter.pubmed.add(pm);
            }
        } else {
            filter.pubmed = null;
        }

    }

    public Proteins read(InputStream is) throws IOException {
        List<String> strs = IOUtil.readStringListAscii(is);

        int n = strs.size();
        for (int i=1; i<n; i++) {
            String str = strs.get(i);
            String[] ss = str.split("\\t");
            String acc = ss[0];
            String pepseq = ss[1];
            String pepwithmod = ss[2];
            int charge = Integer.parseInt(ss[4]);
            double ppm;
            if (ss.length<7 || ss[6]==null || ss[6].length()==0 ) {
                ppm = Double.POSITIVE_INFINITY;
                System.err.println("line "+i+ ":<7");
            } else if (ss[6].equals("NULL")) {
                ppm = Double.POSITIVE_INFINITY;
            } else {
                ppm = Double.parseDouble(ss[6]);
            }

            String pubmed;
            if (ss.length<10) {
                pubmed="";
                System.err.println("line "+i+ ":<10");
            } else {
                pubmed = ss[9];
            }

            if (filter.charges!=null) {
                if (!filter.charges.contains(charge)) continue;
            }
            if (filter.ppm!=null) {
                if (ppm>filter.ppm) continue;
            }
            if (filter.pubmed!=null) {
                if (!filter.pubmed.contains(pubmed)) continue;
            }

            Protein protein = proteins.getProtein(acc);
            if (protein==null) {
                System.err.println("Cannot find protein "+acc);
                continue;
            }

            String proseq = protein.getSequence();


            int peploc = proseq.indexOf(pepseq);
            if (peploc==-1) {
                System.err.println("Protein sequence not match:"+i);
                continue;
            }

            Pattern p = Pattern.compile("\\050([^\\051]+)\\051");
            Matcher m = p.matcher(pepwithmod);
            while (true) {
                if (!m.find()) break;
                String s = m.group(1);
                if (Pattern.matches("p[STY]", s) ) {
                    int site = peploc+m.start(1)-1;
                    try {
                        PTMAnnotationUtil.annotate(protein, site, PTM.PHOSPHORYLATION);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    if ((filter.ambiguous==null||filter.ambiguous==true)
                            && Pattern.matches("[sty]", s)) {
                        int site = peploc+m.start(1)-1;
                        try {
                            PTMAnnotationUtil.annotate(protein, site, PTM.PHOSPHORYLATION);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                m.reset(m.replaceFirst("#"));
            }
        }

        return proteins;
    }
}
