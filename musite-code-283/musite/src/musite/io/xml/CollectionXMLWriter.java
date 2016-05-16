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

package musite.io.xml;

import java.io.IOException;
import java.io.OutputStream;

import java.util.Collection;

import musite.util.StringUtil;

/**
 *
 * @author Jianjiong Gao
 */
public class CollectionXMLWriter<C> extends AbstractXMLWriter<Collection<C>> {

    protected String seperator = ";";

    private XMLEscapeWriter xmlEscapeWriter = new XMLEscapeWriter();

    public void setIndent(int indent) {
        super.setIndent(indent);
        xmlEscapeWriter.setIndent(indent);
    }

    public void setSeperator(String seperator) {
        this.seperator = seperator;
    }

    public void write(OutputStream os, Collection<C> list)
            throws IOException {
        if (os==null || list==null)
            return;

        String str = StringUtil.implode(list, seperator);

        xmlEscapeWriter.write(os, str);
    }
}
