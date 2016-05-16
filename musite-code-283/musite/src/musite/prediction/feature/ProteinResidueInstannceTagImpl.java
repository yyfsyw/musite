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
public class ProteinResidueInstannceTagImpl
        extends InstanceTagImpl implements ProteinResidueInstannceTag {
    private static final long serialVersionUID = 8137353643230468243L;

    public static final String PROTEIN_TAG = "protein";
    public static final String POSITION_TAG = "position";

    public ProteinResidueInstannceTagImpl(Protein protein, int position) {
        addTag(PROTEIN_TAG, protein);
        addTag(POSITION_TAG, position);
    }

    public Protein getProtein() {
        return (Protein) getTag(PROTEIN_TAG);
    }

    public int getPosition() {
        return (Integer) getTag(POSITION_TAG);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ProteinResidueInstannceTagImpl))
            return false;

        ProteinResidueInstannceTagImpl anotherTag = (ProteinResidueInstannceTagImpl)obj;
        if (!anotherTag.getProtein().equals(this.getProtein()))
            return false;

        if (anotherTag.getPosition()!=this.getPosition())
            return false;

        return true;
    }

    public String toString() {
        return getProtein().getAccession()+":"+getPosition();
    }

    public int hashCode() {
        return toString().hashCode();
    }
}
