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

import musite.Protein;
import musite.Proteins;
import musite.ProteinsImpl;
import musite.PTM;


/**
 *
 * @author Jianjiong Gao
 */
public class ProteinNRBuilderImpl implements ProteinNRBuilder {
    protected final ProteinCluster cluster;
    protected final ProteinSelector selector;

    public ProteinNRBuilderImpl(final ProteinCluster cluster) {
        this(cluster, (PTM)null);
    }

    public ProteinNRBuilderImpl(final ProteinCluster cluster, PTM ptm) {
        this (cluster, new ProteinSelectorModifiedProteinPreferred(ptm));
    }

    public ProteinNRBuilderImpl(final ProteinCluster cluster,
            final ProteinSelector selector) {
        this.cluster = cluster;
        this.selector = selector;
    }

    /**
     * {@inheritDoc}
     */
    public Proteins build(Proteins proteins) throws Exception {
        List<List<Protein>> clust = cluster.build(proteins);
        Proteins nrProteins = new ProteinsImpl();

        if (clust==null) {
            return null;
        }

        for (List<Protein> pros : clust) {
            nrProteins.addProtein(selector.select(pros));
        }

        return nrProteins;
    }
}
