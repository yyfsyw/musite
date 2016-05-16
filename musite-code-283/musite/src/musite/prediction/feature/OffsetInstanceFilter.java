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

package musite.prediction.feature;

import musite.Protein;

/**
 *
 * @author Jianjiong Gao
 */
public class OffsetInstanceFilter implements InstanceFilter {
    private final int leftOffset;
    private final int rightOffset;

    public OffsetInstanceFilter(int offset) {
        this(offset, offset);
    }

    /**
     * both left & right are inclusive.
     * @param leftOffset
     * @param rightOffset
     */
    public OffsetInstanceFilter(int leftOffset, int rightOffset) {
        if (leftOffset<0 || rightOffset<0)
            throw new IllegalArgumentException("Offset cannot be negative.");

        this.leftOffset = leftOffset;
        this.rightOffset = rightOffset;
    }

    /**
     * {@inheritDoc}
     */
    public boolean filter(Instance ins) {
        if (ins==null)
            return false;

        InstanceTag tag = ins.getInstanceTag();
        if (!(tag instanceof ProteinResidueInstannceTag))
            return false;

        ProteinResidueInstannceTag prTag = (ProteinResidueInstannceTag)tag;
        Protein protein = prTag.getProtein();
        String seq = protein.getSequence();
        if (seq==null)
            return false;

        int site = prTag.getPosition();

        int left = site - leftOffset;
        int right = site + rightOffset;
        return left>=0 && right<seq.length();
    }
}
