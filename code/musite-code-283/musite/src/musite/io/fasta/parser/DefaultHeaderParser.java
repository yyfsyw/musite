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

package musite.io.fasta.parser;

import musite.Protein;

/**
 *
 * @author Jianjiong Gao
 */
public class DefaultHeaderParser
        extends RegExRuleBasedHeaderParser
        implements HeaderParser {

    public static final String ACCESSION = Protein.ACCESSION;
    public static final String SYMBOL = Protein.SYMBOL;
    public static final String DESCRIPTION = Protein.DESCRIPTION;
    public static final String ORGANISM = Protein.ORGANISM;

    public DefaultHeaderParser(final HeaderRule rule) {
        super(rule);
    }

    public String getAccession() {
        return (String)getField(ACCESSION);
    }

    public String getSymbol() {
        return (String)getField(SYMBOL);
    }

    public String getDescription() {
        return (String)getField(DESCRIPTION);
    }

    public String getOrganism() {
        return (String)getField(ORGANISM);
    }

}
