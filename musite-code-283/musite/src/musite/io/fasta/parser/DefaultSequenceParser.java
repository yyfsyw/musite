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

import musite.ProteinImpl;

/**
 *
 * @author Jianjiong Gao
 */
public class DefaultSequenceParser 
        extends AbstractStringParser
        implements SequenceParser {

    public static final String SEQUENCE = ProteinImpl.SEQUENCE;

    public void parse(String sequence) {
        if (sequence==null) {
            throw new NullPointerException();
        }
        setSequence(sequence);
    }

    public String getSequence() {
        return (String)getField(SEQUENCE);
    }

    protected void setSequence(final String sequence) {
        addField(SEQUENCE, sequence);
    }
}
