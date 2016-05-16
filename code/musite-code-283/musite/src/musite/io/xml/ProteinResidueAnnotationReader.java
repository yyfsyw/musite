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

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import musite.io.Reader;

import musite.util.MultiMap;
import musite.util.MultiTreeMap;
import musite.util.StringUtil;

import org.apache.commons.lang.StringEscapeUtils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import org.dom4j.io.SAXReader;

/**
 *
 * @author Jianjiong Gao
 */
public class ProteinResidueAnnotationReader
        implements Reader<Map<String,MultiMap<Integer,Map<String,Object>>>> {
    public static final String SITE = "site";
    public static final String POSITION = "position";

    protected Map<String, Reader> annotationFieldReaders;

    public ProteinResidueAnnotationReader() {
        annotationFieldReaders = new HashMap();
    }

    public void putAnnotationFieldReader(String field, Reader reader) {
        if (field==null)
            throw new IllegalArgumentException();
        annotationFieldReaders.put(field, reader);
    }

    public Map<String,MultiMap<Integer,Map<String,Object>>> read(InputStream is)
            throws IOException {
        if (is==null)
            return null;

        final Map<String,MultiMap<Integer,Map<String,Object>>> result = new HashMap();

        SAXReader saxReader = new SAXReader();
        BufferedInputStream bis = new BufferedInputStream(is);

        Document document;
        try {
            document = saxReader.read(bis);
        } catch (DocumentException e) {
            throw new IOException(e.getMessage());
        }

        Element root = document.getRootElement();

        // iterate through child elements of root
        for ( Iterator i = root.elementIterator(); i.hasNext(); ) {
            Element annTypeElm = (Element) i.next();
            String annType = annTypeElm.getQualifiedName();
            MultiMap<Integer,Map<String,Object>> sites = result.get(annType);
            if (sites==null) {
                sites = new MultiTreeMap();
                result.put(annType, sites);
            }

            Iterator<Element> itSite = annTypeElm.elementIterator(SITE);
            while (itSite.hasNext()) {
                Element siteElm = (Element) itSite.next();
                String pos = siteElm.attributeValue(POSITION);
                if (pos==null) {
                    System.err.println("No site info");
                    continue;
                }
                int site = Integer.parseInt(pos)-1;
                Map<String,Object> annotations = new HashMap();
                sites.add(site, annotations);

                Iterator<Element> itAnn = siteElm.elementIterator();
                while (itAnn.hasNext()) {
                    Element annElm = (Element) itAnn.next();
                    String name = annElm.getQualifiedName();

                    Object obj = null;
                    if (annElm.hasContent()) {
                        Reader fieldReader = annotationFieldReaders.get(name);
                        if (fieldReader!=null) {
                            try {
                                String text = ProteinsXMLReader.nodeContentToString(annElm);
                                InputStream bais = StringUtil.toStream(text);
                                obj = fieldReader.read(bais);
                                bais.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                                continue;
                            }
                        } else {
                            obj = StringEscapeUtils.unescapeXml(annElm.getTextTrim());
                        }
                    }

                    annotations.put(name, obj);
                }
            }
        }

        return result;
    }
}
