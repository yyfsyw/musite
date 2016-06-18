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

package musite;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Jianjiong Gao
 */
public interface Proteins {

    public enum ConfictHandleOption {
        RENAME, SKIP, OVERWRITE;
    }

    /**
     *
     * @return number of proteins
     */
    public int proteinCount();

    /**
     *
     * @return all proteins
     */
    public Collection<Protein> proteins();

    /**
     * 
     * @param proteinAccession
     * @return true if the protein is contained
     * @throws NullPointerException if proteinAccession is null
     */
    public boolean isProteinContained(String proteinAccession);

    /**
     *
     * @return all protein accessions
     */
    public Set<String> getProteinsAccessions();

    /**
     *
     * @return iterator of proteins
     */
    public Iterator proteinIterator();

    /**
     * 
     * @param proteinAccession
     * @returnthe corresponding protein information
     * @throws NullPointerException if proteinAccession is null
     */
    public Protein getProtein(String proteinAccession);

    /**
     * Add a protein. If there exists a protein with the same name, rename
     * the protein to be added. This is the same as
     * addProtein(protein, ConfictHandleOption.RENAME).
     * @param protein protein to be added.
     */
    public void addProtein(Protein protein);

    /**
     * 
     * @param protein
     * @param option
     * @return false if protein already exist and option is skip.
     */
    public boolean addProtein(Protein protein, ConfictHandleOption option);

    /**
     *
     * @param proteins
     * @param deepCopy
     * @param fields
     * @param option
     */
    public void addAll(Proteins proteins, boolean deepCopy, 
            Set<String> fields, ConfictHandleOption option);

    /**
     * 
     * @param proteinAccession
     * @return true if exist and removed
     */
    public boolean removeProtein(String proteinAccession);

    /**
     *
     * @param accessions
     */
    public void retainProteins(Set<String> accessions);

    /**
     *
     * @param accessions
     */
    public void removeProteins(Set<String> accessions);

    /**
     * 
     * @param visitor
     */
    public void travelProteins(ProteinVisitor visitor);

    /**
     * keep the proteins filtered
     * @param proteins
     * @param filter
     * @param include
     */
    public void filterProteins(ProteinFilter filter);

    public interface ProteinFilter {
        /**
         *
         * @param protein
         * @return true if retain the protein; false if remove it.
         */
        public boolean filter(Protein protein);
    }

    public interface ProteinVisitor {
        /**
         * 
         * @param protein
         */
        public void visit(Protein protein);
    }
}
