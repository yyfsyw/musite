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

import java.io.Serializable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Jianjiong Gao
 */
public class ProteinImpl implements Protein, Serializable {
    private static final long serialVersionUID = -1838813824424087123L;

    protected final Map<String,Object> mapInfo;

    public ProteinImpl() {
        mapInfo = new HashMap();
    }

    public ProteinImpl(final Protein protein) {
        this(protein, null);
    }

    public ProteinImpl(final Protein protein, final Set<String> fields) {
        mapInfo = new HashMap();
        if (fields==null) {
            mapInfo.putAll(protein.getInfoMap());
        }
        else {
            Map<String,Object> mapCopy = protein.getInfoMap();
            Iterator<String> it = mapCopy.keySet().iterator();
            while (it.hasNext()) {
                String field = it.next();
                if (fields.contains(field)) {
                    mapInfo.put(field, mapCopy.get(field));
                }
            }
        }
    }
    
    public ProteinImpl(final String accession,
                           final String sequence,
                           final String symbol,
                           final String description,
                           final String organism) {
        mapInfo = new HashMap();
        if (accession!=null)
            setAccession(accession);

        if (symbol!=null)
            setSymbol(symbol);

        if (sequence!=null)
            setSequence(sequence);

        if (description!=null)
            setDescription(description);

        if (organism!=null)
            setOrganism(organism);
    }

    /**
     * {@inheritDoc}
     */
    public String getAccession() {
        return (String)mapInfo.get(ACCESSION);
    }

    /**
     * {@inheritDoc}
     */
    public void setAccession(final String accession) {
        mapInfo.put(ACCESSION, accession);
    }

    /**
     * {@inheritDoc}
     */
    public String getSymbol() {
        return (String)mapInfo.get(SYMBOL);
    }

    /**
     * {@inheritDoc}
     */
    public void setSymbol(final String symbol) {
       mapInfo.put(SYMBOL, symbol);
    }

    /**
     * {@inheritDoc}
     */
    public String getSequence() {
        return (String)mapInfo.get(SEQUENCE);
    }

    /**
     * {@inheritDoc}
     */
    public void setSequence(final String sequence) {
        mapInfo.put(SEQUENCE, sequence);
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return (String)mapInfo.get(DESCRIPTION);
    }

    /**
     * {@inheritDoc}
     */
    public void setDescription(final String description) {
        mapInfo.put(DESCRIPTION, description);
    }

    /**
     * {@inheritDoc}
     */
    public String getOrganism() {
        return (String)mapInfo.get(ORGANISM);
    }

    /**
     * {@inheritDoc}
     */
    public void setOrganism(String organism) {
        mapInfo.put(ORGANISM, organism);
    }

    /**
     * {@inheritDoc}
     */
    public void copyFrom(final Protein protein,
            final boolean replace, final Set<String> infoTypes) {
        if (protein==null)
            return;

        Map<String,Object> infoFrom = protein.getInfoMap();
        Set<String> copyTypes;
        if (infoTypes==null) {
            copyTypes = new HashSet(infoFrom.keySet());
        } else {
            copyTypes = new HashSet(infoTypes);
            copyTypes.retainAll(protein.getInfoMap().keySet());
        }

        if (!replace)
            copyTypes.removeAll(getInfoMap().keySet());


        for (String type : copyTypes) {
            this.putInfo(type, protein.getInfo(type));
        }
    }

    /**
     * {@inheritDoc}
     */
    public Map<String,Object> getInfoMap() {
        return mapInfo;
    }

    /**
     * {@inheritDoc}
     */
    public Object putInfo(final String infoType, final Object infoValue) {
        return mapInfo.put(infoType, infoValue);
    }

    /**
     * {@inheritDoc}
     */
    public Object getInfo(final String infoType) {
        return mapInfo.get(infoType);
    }

    /**
     * {@inheritDoc}
     */
    public Object removeInfo(final String infoType) {
        return mapInfo.remove(infoType);
    }

    /**
     * {@inheritDoc}
     */
    public boolean retainInfo(final Set<String> infoTypes) {
        return mapInfo.keySet().retainAll(infoTypes);
    }

//    @Override
//    public boolean equals(Object o) {
//        if (! (o instanceof Protein)) {
//            return false;
//        }
//
//        Protein another = (Protein)o;
//        return getAccession().compareTo(another.getAccession())==0;
//    }
//
//    @Override
//    public int hashCode() {
//        return getAccession().hashCode();
//    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(500);

        String accession = getAccession();
        if (accession!=null) {
            sb.append(accession);
        }

        String symbol = getSymbol();
        if (symbol!=null) {
            sb.append(" | symbols: "+symbol);
        }

        String desc = getDescription();
        if (desc!=null)
            sb.append(" | desc: "+desc);

        String organism = getOrganism();
        if (organism!=null)
            sb.append(" | organism: "+organism);

        return sb.toString();
    }
}
