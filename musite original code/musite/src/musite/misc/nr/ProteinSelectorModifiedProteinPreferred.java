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

import java.util.List;
import java.util.Set;

import musite.Protein;
import musite.PTM;
import musite.PTMAnnotationUtil;

/**
 *
 * @author Jianjiong Gao
 */
public class ProteinSelectorModifiedProteinPreferred implements ProteinSelector {
    private final PTM ptm;
    private final Set<String> enzymes;

    public ProteinSelectorModifiedProteinPreferred() {
        this(null); // all ptm
    }

    public ProteinSelectorModifiedProteinPreferred(PTM ptm) {
        this(ptm, null);
    }

    public ProteinSelectorModifiedProteinPreferred(PTM ptm, Set<String> enzymes) {
        this.ptm = ptm;
        this.enzymes = enzymes;
    }

    /**
     * {@inheritDoc}
     */
    public Protein select(List<Protein> proteinCluster) {
        if (proteinCluster==null || proteinCluster.isEmpty()) {
            return null;
        }

        Protein ret = proteinCluster.get(0);
        Set<Integer> sites = PTMAnnotationUtil.getSites(ret, ptm, null, enzymes);
        int maxSites = sites==null?0:sites.size();
        String sequence = ret.getSequence();
        int maxLen = sequence.length();

        int n = proteinCluster.size();
        for (int i=1; i<n; i++) {
            Protein pro = proteinCluster.get(i);
            sites = PTMAnnotationUtil.getSites(pro, ptm, null, enzymes);
            sequence = pro.getSequence();
            if (sites!=null && sites.size()>maxSites) {
                maxSites = sites.size();
                maxLen = sequence.length();
                ret = pro;
            } else if ((sites==null&&maxSites==0)
                    ||(sites!=null&&sites.size()==maxSites)) {
                if (sequence.length()>maxLen) {
                    maxLen = sequence.length();
                    ret = pro;
                }
            }
        }

        return ret;
    }
}
