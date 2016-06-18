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

import java.util.ArrayList;
import java.util.Collection;

import musite.io.Reader;

import musite.util.IOUtil;
import musite.util.StringConvertUtil;
import musite.util.StringUtil;

import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author Jianjiong Gao
 */
public class CollectionXMLReader<C> implements Reader<Collection<C>> {

    public static CollectionXMLReader<String> createStringCollectionReader() {
        return new CollectionXMLReader<String>(StringConvertUtil.getStringStringConverter());
    }

    public static CollectionXMLReader<Integer> createIntegerCollectionReader() {
        return new CollectionXMLReader<Integer>(StringConvertUtil.getIntegerStringConverter());
    }

    public static CollectionXMLReader<Double> createDoubleCollectionReader() {
        return new CollectionXMLReader<Double>(StringConvertUtil.getDoubleStringConverter());
    }

    public static CollectionXMLReader<Boolean> createBooleanCollectionReader() {
        return new CollectionXMLReader<Boolean>(StringConvertUtil.getBooleanStringConverter());
    }

    protected final StringConvertUtil.ConverterFromString<C> strConverter;

    public CollectionXMLReader(StringConvertUtil.ConverterFromString<C> conv) {
        if (conv == null)
            throw new IllegalArgumentException();
        this.strConverter = conv;
    }

    protected Collection<C> data = null;

    protected String seperator = ";";

    public void setSeperator(String seperator) {
        this.seperator = seperator;
    }

    /**
     * Set collection to contained the data to be read. Any collections such as
     * List & Set are allowable.
     * @param data collection.
     */
    public void setData(Collection data) {
        this.data = data;
    }

    /**
     *
     * @param is contains the header
     * @return
     * @throws IOException
     */
    public Collection<C> read(InputStream is) throws IOException {
        if (is==null)
            return null;

        Collection<C> result = data==null?new ArrayList():data;

        String str = StringEscapeUtils.unescapeXml(IOUtil.readStringAscii(is));

        int l = str.indexOf('>');
        int r = str.lastIndexOf('<');
        if (l==-1 || r==-1)
            throw new IllegalArgumentException();

        str = str.substring(l+1, r).replace("\n","");

        String[] strs = str.split(StringUtil.toOct(seperator));
        for (String s : strs) {
            C c = strConverter.convert(s);
            result.add(c);
        }

        return result;
    }
}
