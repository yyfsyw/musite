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
import java.io.OutputStreamWriter;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Singleton class
 * @author Jianjiong Gao
 */
public class XMLEscapeWriter
        extends AbstractXMLWriter<Object> {

    public void write(OutputStream os, Object fieldValue) throws IOException {
        if (os==null || fieldValue==null)
            return;

        OutputStreamWriter osw = new OutputStreamWriter(os);

        String str = StringEscapeUtils.escapeXml(fieldValue.toString());

        for (int i=0; i<getIndent(); i++)
            osw.write("\t");

        osw.write(str);
        osw.flush();
    }
}
