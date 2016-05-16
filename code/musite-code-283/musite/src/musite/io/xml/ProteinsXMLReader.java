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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import musite.ProteinImpl;
import musite.Proteins;
import musite.ProteinsImpl;

import musite.io.Reader;

import musite.util.StringUtil;

import org.apache.commons.lang.StringEscapeUtils;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.ElementPath;
import org.dom4j.ElementHandler;
import org.dom4j.Node;

import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 *
 * @author Jianjiong Gao
 */
public class ProteinsXMLReader implements Reader<Proteins> {
    protected Proteins data;
    protected boolean nullData;
    protected Map<String, Reader> proteinFieldReaders;
    protected Set<String> fieldFilter;
    protected boolean fieldFilterInclude;
    protected Map<String, ElementHandler> saxReaderHandler;
    protected String root = "musite";
    protected Proteins.ProteinFilter proteinFilter = null;

    /**
     * Create a reader with data stored in a new ProteinsImpl and read the residue
     * annotations.
     * @return a ProteinXMLReader.
     */
    public static ProteinsXMLReader createReader() {
        return createReader(true);
    }

    /**
     * Create a reader with data stored in a new ProteinsImpl.
     * @param residueAnnotation true if reading the residue annotations.
     * @return a ProteinXMLReader.
     */
    public static ProteinsXMLReader createReader(boolean residueAnnotation) {
        return createReader(null, residueAnnotation);
    }

    /**
     * Create a instance.
     * @param proteins store the data.
     * @param residueAnnotation true if reading the residue annotations.
     * @return a ProteinXMLReader.
     */
    public static ProteinsXMLReader createReader(Proteins proteins,
            boolean residueAnnotation) {
        ProteinsXMLReader reader = new ProteinsXMLReader(proteins);
        if (residueAnnotation)
            reader.putProteinFieldReader(
                musite.ResidueAnnotationUtil.RESIDUE_ANNOTATION,
                new ProteinResidueAnnotationReader());
        return reader;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public void setProteinFilter(Proteins.ProteinFilter proteinFilter) {
        this.proteinFilter  = proteinFilter;
    }

    public ProteinsXMLReader(Proteins proteins) {
        this.data = proteins;
        nullData = proteins==null;
        proteinFieldReaders = new HashMap();        
        fieldFilter = null;
        saxReaderHandler = new HashMap();
        String path = "/protein-list/protein";
        if (root!=null)
            path = "/"+root+path;
        addSaxReaderHandler(path, new ElementHandler() {
            public void onStart(ElementPath path) {
            }
            public void onEnd(ElementPath path) {
                ProteinImpl protein = new ProteinImpl();

                Element elem = path.getCurrent();
                Iterator<Element> itr = elem.elementIterator();
                while(itr.hasNext())
                {
                    Element field = (Element) itr.next();
                    String name = field.getQualifiedName();
                    if (fieldFilter!=null && fieldFilterInclude!=fieldFilter.contains(name))
                        continue;

                    Object obj;

                    Reader fieldReader = proteinFieldReaders.get(name);
                    if (fieldReader!=null) {
                        try {
                            String text = nodeContentToString(field);//field.getTextTrim();
                            InputStream bais = StringUtil.toStream(text);
                            obj = fieldReader.read(bais);
                            bais.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            continue;
                        }
                    } else {
                        obj = StringEscapeUtils.unescapeXml(field.getTextTrim());
                    }

                    protein.putInfo(name, obj);
                }

                //System.out.println(protein.getAccession());

                if (proteinFilter==null || proteinFilter.filter(protein))
                    data.addProtein(protein);

                int count = data.proteinCount();
                if (count%1000==0)
                    System.out.println(count);

                // prune the tree
                elem.detach();
            }
        });
    }

    public void putProteinFieldReader(String field, Reader reader) {
        if (field==null)
            throw new IllegalArgumentException();
        proteinFieldReaders.put(field, reader);
    }

    public void setFieldFilter(Set<String> fields, boolean fieldFilterInclude) {
        this.fieldFilter = fields;
        this.fieldFilterInclude = fieldFilterInclude;
    }

    public void setFieldFilter(String field, boolean fieldFilterInclude) {
        fieldFilter = new HashSet(1);
        fieldFilter.add(field);
        this.fieldFilterInclude = fieldFilterInclude;
    }

    public void addSaxReaderHandler(String path, ElementHandler eh) {
        if (path==null || eh==null)
            throw new IllegalArgumentException();

        saxReaderHandler.put(path, eh);
    }

    public Proteins read(InputStream is) throws IOException {
        if (is==null) {
            throw new IllegalArgumentException();
        }

        if (nullData)
            data = new ProteinsImpl();
        
        SAXReader saxReader = new SAXReader();

        for (Map.Entry<String, ElementHandler> entry : saxReaderHandler.entrySet()) {
            saxReader.addHandler(entry.getKey(), entry.getValue());
        }

        BufferedInputStream bis = new BufferedInputStream(is);

        try {
            System.out.println("Reading...");
            saxReader.read(bis);
            System.out.println(""+data.proteinCount()+" protein were read.");
        } catch (DocumentException e) {
            throw new IOException(e.getMessage());
        }

        return data;
    }

    public static String nodeContentToString(Node node) throws IOException {
        java.io.StringWriter strWriter = new java.io.StringWriter();
        XMLWriter writer = new XMLWriter(strWriter);
        writer.write(node);
        String str = strWriter.toString();
        
        return str;
    }
}
