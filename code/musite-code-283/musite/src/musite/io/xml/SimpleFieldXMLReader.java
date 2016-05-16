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
import musite.util.StringConvertUtil;

import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author Jianjiong Gao
 */
public class SimpleFieldXMLReader<C> implements Reader<C> {

    public static SimpleFieldXMLReader<String> createStringCollectionReader() {
        return new SimpleFieldXMLReader<String>(StringConvertUtil.getStringStringConverter());
    }

    public static SimpleFieldXMLReader<Integer> createIntegerCollectionReader() {
        return new SimpleFieldXMLReader<Integer>(StringConvertUtil.getIntegerStringConverter());
    }

    public static SimpleFieldXMLReader<Double> createDoubleCollectionReader() {
        return new SimpleFieldXMLReader<Double>(StringConvertUtil.getDoubleStringConverter());
    }

    public static SimpleFieldXMLReader<Boolean> createBooleanCollectionReader() {
        return new SimpleFieldXMLReader<Boolean>(StringConvertUtil.getBooleanStringConverter());
    }

    protected final StringConvertUtil.ConverterFromString<C> strConverter;

    public SimpleFieldXMLReader(StringConvertUtil.ConverterFromString<C> conv) {
        if (conv == null)
            throw new IllegalArgumentException();
        this.strConverter = conv;
    }

    /**
     *
     * @param is contains the header
     * @return
     * @throws IOException
     */
    public C read(InputStream is) throws IOException {
        if (is==null)
            return null;

        String str = StringEscapeUtils.unescapeXml(IOUtil.readStringAscii(is));

        int l = str.indexOf('>');
        int r = str.lastIndexOf('<');
        if (l==-1 || r==-1)
            throw new IllegalArgumentException();

        str = str.substring(l+1, r).replace("\n","");

        return strConverter.convert(str);
    }
}
