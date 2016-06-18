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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import musite.Proteins;
import musite.ProteinsImpl;
import musite.Protein;
import musite.PTM;
import musite.PTMAnnotationUtil;

import musite.io.Reader;

import musite.util.IOUtil;

/**
 *
 * @author Jianjiong Gao
 */
public class PhosphoELMProteinsReader implements Reader<Proteins> {
    private Proteins data;

    public PhosphoELMProteinsReader() {
        this(null);
    }

    public PhosphoELMProteinsReader(Proteins data) {
        this.data = data==null?new ProteinsImpl():data;
    }

    public Proteins read(InputStream is) throws IOException {
        List<String> lines = IOUtil.readStringListAscii(is);

        int n = lines.size();
        for (int i=1; i<n; i++) {
            String line = lines.get(i);
            String[] strs = line.split("\t");
            String acc = strs[0];
            String seq = strs[1];
            String pos = strs[2];

            Set<String> enzymes = new HashSet();
            if (strs[5].trim().length()>0) {
                for (String str : strs[5].trim().split(";")) {
                    enzymes.add(str.trim());
                }
            }

            Protein protein = data.getProtein(acc);
            if (protein==null) {
                protein = new musite.ProteinImpl(acc,seq,null,null,null);
                data.addProtein(protein);
            }

            int site = Integer.valueOf(pos)-1;

            if (enzymes==null)
                try {
                    PTMAnnotationUtil.annotate(protein, site, PTM.PHOSPHORYLATION, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            else {
                for (String enzyme : enzymes) {
                    try {
                        PTMAnnotationUtil.annotate(protein, site, PTM.PHOSPHORYLATION, enzyme);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return data;
    }
}
