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

import java.util.Map;
import java.util.Set;

/**
 * Container for protein information.
 * @author Jianjiong Gao
 */
public interface Protein {
    public static final String ACCESSION = "accession";
    public static final String DESCRIPTION = "description";
    public static final String ORGANISM = "organism";
    public static final String SYMBOL = "symbol";
    public static final String SEQUENCE = "sequence";
    
    /**
     * 
     * @return accession.
     */
    public String getAccession();

    /**
     * Set accession.
     * @param accession protein accession.
     */
    public void setAccession(String accession);

    /**
     *
     * @return protein symbols.
     */
    public String getSymbol();

    /**
     * set protein symbol.
     * @param name protein symbol.
     */
    public void setSymbol(String symbol);

    /**
     * 
     * @return organism protein organism.
     */
    public String getOrganism();

    /**
     * Set organism.
     * @param organism protein organism.
     */
    public void setOrganism(String species);

    /**
     * 
     * @return sequence protein sequence.
     */
    public String getSequence();

    /**
     * Set protein sequence.
     * @param sequence protein sequence.
     */
    public void setSequence(String sequence);

    /**
     * 
     * @return protein description.
     */
    public String getDescription();

    /**
     * 
     * @param description protein discription.
     */
    public void setDescription(String description);

    /**
     * Copy from another protein.
     * @param protein protein to be copied.
     * @param replace replace the current field with the same name if true.
     * @param infoTypes info types to be copied; copy all if null.
     */
    public void copyFrom(Protein protein, boolean replace, Set<String> infoTypes);

    /**
     *
     * @return protein information. Key: type of information; value: informaiton.
     */
    public Map<String,Object> getInfoMap();

    /**
     * Add protein information.
     * @param infoType type of information.
     * @param infoValue information.
     * @return the previous information with this type or null if not exist.
     * previously.
     */
    public Object putInfo(String infoType, Object infoValue);

    /**
     *
     * @param infoType type of information.
     * @return protein information of a specific type; null if not exist.
     */
    public Object getInfo(String infoType);

    /**
     *
     * @param infoType type of information.
     * @return the previous information of this type or null if not exist.
     */
    public Object removeInfo(String infoType);

    /**
     * 
     * @param infoTypes type of information.
     *
     * @return true if changed as a result.
     */
    public boolean retainInfo(Set<String> infoTypes);
}
