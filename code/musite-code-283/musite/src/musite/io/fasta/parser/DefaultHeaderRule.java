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
public class DefaultHeaderRule
        extends RegExRuleImpl
        implements HeaderRule {

    public static final String ACCESSION = Protein.ACCESSION;
    public static final String SYMBOL = Protein.SYMBOL;
    public static final String DESCRIPTION = Protein.DESCRIPTION;
    public static final String ORGANISM = Protein.ORGANISM;

    public DefaultHeaderRule(final String accRegEx) {
        this(accRegEx, null, null, null);
    }

    public DefaultHeaderRule(final String accRegEx, final String symbolRegEx,
            final String descRegEx, final String speciesRegEx) {
        super();

        if (accRegEx==null) {
            throw new NullPointerException();
        }

        setAccessionRegExp(accRegEx);

        if (symbolRegEx!=null)
            setSymbolRegExp(symbolRegEx);

        if (descRegEx!=null)
            setDescriptionRegExp(descRegEx);
        
        if (speciesRegEx!=null)
            setOrganismRegExp(speciesRegEx);
    }

    public String getAccessionRegExp() {
        return getFieldRegEx(ACCESSION);
    }

    public String getSymbolRegExp() {
        return getFieldRegEx(SYMBOL);
    }

    public String getDescriptionRegExp() {
        return getFieldRegEx(DESCRIPTION);
    }

    public String getOrganismRegExp() {
        return getFieldRegEx(ORGANISM);
    }

    protected void setAccessionRegExp(final String regExp) {
        if (regExp == null) {
            throw new NullPointerException();
        }

        setFieldRegEx(ACCESSION, regExp);
    }

    protected void setSymbolRegExp(final String regExp) {
        setFieldRegEx(SYMBOL, regExp);
    }

    protected void setDescriptionRegExp(final String regExp) {
        setFieldRegEx(DESCRIPTION, regExp);
    }

    protected void setOrganismRegExp(final String regExp) {
        setFieldRegEx(ORGANISM, regExp);
    }

}
