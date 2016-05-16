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

import musite.Protein;
import musite.Proteins;
import musite.PTM;
import musite.PTMAnnotationUtil;

import musite.io.Reader;

import musite.util.IOUtil;

/**
 *
 * @author Jianjiong Gao
 */
public class PTMAnnotationProteinsReader implements Reader<Proteins> {
    private Proteins data;
    private PTM ptm;

    public PTMAnnotationProteinsReader(Proteins data, PTM ptm) {
        this.data = data;
        this.ptm = ptm;
    }

    public Proteins read(InputStream is) throws IOException {
        List<String> lines = IOUtil.readStringListAscii(is);
        for (String line : lines) {
            String[] strs = line.split("\t");
            if (strs.length<2)
                continue;

            String acc = strs[0];
            Protein protein = data.getProtein(acc);
            if (protein == null) {
                System.err.println(acc+" does not exist in the data.");
                continue;
            }

            String strSite = strs[1];
            if (!strSite.matches("[0-9]+")) {
                System.err.println(strSite+" is not a number.");
                continue;
            }

            PTMAnnotationUtil.annotate(protein, Integer.parseInt(strSite)-1, ptm);
        }
        return data;
    }

//    public Proteins read(InputStream is) throws IOException {
//        FastaVisitor visitor = new FastaVisitor() {
//            public void visit(String header, String sequence) {
//                String acc = header;
//                Protein protein = data.getProtein(acc);
//                if (protein==null) {
//                    System.err.println(acc+"does not exist in the data.");
//                    return;
//                }
//
//                String[] strs = sequence.split("\n");
//                for (String str : strs) {
//                    int site;
//                    try {
//                        site = Integer.parseInt(str)-1;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        continue;
//                    }
//                    PTMAnnotationUtil.annotate(protein, site, ptm);
//                }
//            }
//        };
//
//        FastaTravelerImpl traveler = new FastaTravelerImpl(visitor);
//        traveler.travel(is);
//        return data;
//    }
}
