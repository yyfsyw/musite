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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import musite.PTM;

import musite.Proteins;
import musite.Protein;
import musite.PTMAnnotationUtil;

import musite.io.Reader;

import musite.util.IOUtil;

/**
 *
 * @author Jianjiong Gao
 */
public class PhosphoPeptideProteinsReader implements Reader<Proteins> {
    private Proteins data;
    private AmbiguityFilter amb = AmbiguityFilter.NONAMBIGOUS;
    private Map<String,String> idmap;

    public enum AmbiguityFilter {
        AMBIGUOUS, NONAMBIGOUS, BOTH;
    }

    public PhosphoPeptideProteinsReader(Proteins data) {
        this.data = data;
        setIDMap();
    }

    public void setAmbiguityFilter(AmbiguityFilter amb) {
        this.amb = amb;
    }

    private void setIDMap() {
        idmap = new HashMap();
        data.travelProteins(new Proteins.ProteinVisitor() {
            public void visit(Protein protein) {
                String acc = protein.getAccession();
                String otherAccs = (String)protein.getInfo("other-accessions");
                if (otherAccs!=null && otherAccs.length()>0) {
                    for (String other : otherAccs.split(";")) {
                        idmap.put(other, acc);
                    }
                }
            }
        });
    }

    public Proteins read(InputStream is) throws IOException {
        Map<String,Set<String>> mapAccPeps = readPeptide(is);
        for (Map.Entry<String,Set<String>> entry : mapAccPeps.entrySet()) {
            String acc = entry.getKey();
            if (acc==null||acc.length()==0)
                continue;

            if (idmap.get(acc)!=null)
                acc = idmap.get(acc);
            
            Set<String> peps = entry.getValue();
            Protein protein = data.getProtein(acc);
            if (protein==null) {
                System.err.println(acc+" does not exist.");
                continue;
            }

            Set<Integer> sites = readSites(protein, peps);
            PTMAnnotationUtil.annotate(protein, sites, PTM.PHOSPHORYLATION);
        }
        return data;
    }

    private Set<Integer> readSites(Protein protein, Set<String> peps) throws IOException {
        Set<Integer>  sites = new HashSet();

        String proSeq = protein.getSequence().toUpperCase();

        for (String pep : peps) {
            if (amb==AmbiguityFilter.AMBIGUOUS) {
                pep = pep.replaceAll("([A-Z])#", "\\1"); // determined sites in upper case
            } else if (amb==AmbiguityFilter.NONAMBIGOUS) {
                pep = pep.replaceAll("([a-z])#", "\\1"); // ambiguous site in lower case
            }

            pep = pep.toUpperCase();
            pep = pep.replaceAll("[\\.-]", "");

            int pepLoc = proSeq.indexOf(pep.replaceAll("#", ""));
            if (pepLoc==-1) {
                System.err.println("peptide not exist: "+protein.getAccession()+": "+pep);
                continue;
            }

            int idx = pep.indexOf('#');
            while (idx>-1) {
                sites.add(pepLoc+idx-1);
                pep = pep.substring(0,idx)+pep.substring(idx+1);
                idx = pep.indexOf('#', idx);
            }
        }

        return sites;
    }

    private Map<String,Set<String>> readPeptide(InputStream is) throws IOException {
        Map<String,Set<String>> map = new HashMap();
        List<String> lines = IOUtil.readStringListAscii(is);
        String acc = null;
        Set<String> peps = null;
        for (String line : lines) {
            if (line.startsWith(">")) {
                String[] strs = line.split("[\t ]",2);
                acc = strs[0].substring(1);
                peps = map.get(acc);
                if (peps==null) {
                    peps = new HashSet();
                    map.put(acc, peps);
                }
            } else {
                String[] strs = line.split("[\t ]",2);
                peps.add(strs[0]);
            }
        }
        
        return map;
    }



}
