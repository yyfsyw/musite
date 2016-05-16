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

import java.io.InputStream;
import java.io.IOException;

import musite.io.Reader;

import musite.util.IOUtil;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Singleton class
 * @author Jianjiong Gao
 */
public class XMLEscapeReader implements Reader<Object> {

    private static XMLEscapeReader instance = new XMLEscapeReader();

    public static XMLEscapeReader getInstance() {
        return instance;
    }

    private XMLEscapeReader() {};

    public Object read(InputStream is) throws IOException {
        if (is==null)
            return null;

        String str = IOUtil.readStringAscii(is);
        return StringEscapeUtils.unescapeXml(str);
    }
}
