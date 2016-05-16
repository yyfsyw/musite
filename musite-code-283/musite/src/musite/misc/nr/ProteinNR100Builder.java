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

package musite.misc.nr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import musite.Protein;
import musite.Proteins;

import musite.ProteinsUtil;

/**
 *
 * @author Jianjiong Gao
 */
public class ProteinNR100Builder extends ProteinNRBuilderImpl {

    public ProteinNR100Builder() {
        super(new ProteinClusterSameSequence(), new ProteinSelectorMergeSites());
    }

    private static class ProteinClusterSameSequence implements ProteinCluster {
        public List<List<Protein>> build(Proteins proteins) {
            HashMap<String,List<Protein>> map = new HashMap();
            Iterator<Protein> it = proteins.proteinIterator();
            while (it.hasNext()) {
                Protein protein = it.next();
                String seq = protein.getSequence();

                List<Protein> curr = map.get(seq);
                if (curr==null) {
                    curr = new ArrayList();
                    map.put(seq, curr);
                }

                curr.add(protein);
            }

            return new ArrayList(map.values());
        }
    }

    private static class ProteinSelectorMergeSites implements ProteinSelector {
        /**
         * {@inheritDoc}
         */
        public Protein select(List<Protein> proteinCluster) {
            if (proteinCluster==null || proteinCluster.isEmpty()) {
                return null;
            }

            Protein ret = proteinCluster.get(0);

            int n = proteinCluster.size();
            for (int i=1; i<n; i++) {
                ret = ProteinsUtil.mergeProteins(ret, proteinCluster.get(i));
            }

            return ret;
        }
    }
}
