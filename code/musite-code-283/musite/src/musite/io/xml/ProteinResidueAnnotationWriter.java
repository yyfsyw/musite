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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import musite.io.Writer;

import musite.util.MultiMap;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jianjiong Gao
 */
public class ProteinResidueAnnotationWriter
        extends AbstractXMLWriter<Map<String,MultiMap<Integer,Map<String,Object>>>> {
    protected final Map<String,Writer> annotationFieldWriters;

    protected Set<String> fieldFilter = null;
    protected boolean fieldInclude = true;

    public static final String SITE = "site";
    public static final String POSITION = "position";
    
    public ProteinResidueAnnotationWriter() {
        this (null);
    }

    public ProteinResidueAnnotationWriter(final Map<String,Writer> fieldWriters) {
        this.annotationFieldWriters = fieldWriters==null?new HashMap():fieldWriters;
    }

    public void putAnnotationFieldWriter(String field, Writer writer) {
        if (field==null)
            throw new IllegalArgumentException();
        annotationFieldWriters.put(field, writer);
    }

    public void setProteinFieldFilter(Set<String> fields, boolean include) {
        this.fieldFilter = fields;
        this.fieldInclude = include;
    }

    public void setProteinFieldFilter(String field, boolean include) {
        fieldFilter = new HashSet(1);
        fieldFilter.add(field);
        this.fieldInclude = include;
    }

    public void write(OutputStream os, Map<String,MultiMap<Integer,Map<String,Object>>> residueAnnotations)
            throws IOException {
        if (os==null || residueAnnotations==null)
            return;

        String prefix = StringUtils.repeat("\t", getIndent());

        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bufWriter = new BufferedWriter(osw);

        for (Map.Entry<String,MultiMap<Integer,Map<String,Object>>> entry : residueAnnotations.entrySet()) {
            String type = entry.getKey();
            bufWriter.write(prefix+"<"+type+">\n");
            writeMultiMap(os, bufWriter, entry.getValue(), prefix+"\t");
            bufWriter.write(prefix+"</"+type+">\n");
        }

        bufWriter.flush();
        osw.flush();
    }

    private void writeMultiMap(OutputStream os, BufferedWriter bufWriter,
            MultiMap<Integer,Map<String,Object>> mm, String prefix) throws IOException {

        Set<Integer> sites = mm.keySet();
        for (Integer site : sites) {
            Collection<Map<String,Object>> sm = mm.get(site);
            for (Map<String,Object> m : sm) {
                bufWriter.write(prefix+"<"+SITE+" "+POSITION+"=\""+(site+1)+"\">\n"); // start from 1
                writeMap(os, bufWriter, m, prefix+"\t");
                bufWriter.write(prefix+"</"+SITE+">\n");
            }
        }

    }

    private void writeMap(OutputStream os, BufferedWriter bufWriter,
            Map<String,Object> m, String prefix) throws IOException {
        if (m==null)
            return;

        for (Map.Entry<String,Object> entry : m.entrySet()) {
            String k = entry.getKey();
            if (fieldFilter!=null && fieldFilter.contains(k)!=fieldInclude)
                continue;

            Object v = entry.getValue();

            bufWriter.write(prefix+"<"+k+">");

            Writer fieldWriter = annotationFieldWriters.get(k);
            if (fieldWriter == null) {
                fieldWriter = XMLUtil.createDefaultWriter(v);
                bufWriter.flush();
                fieldWriter.write(os,v);
            } else {
                bufWriter.write("\n");
                bufWriter.flush();
                if (fieldWriter instanceof AbstractXMLWriter)
                    ((AbstractXMLWriter)fieldWriter).setIndent(getIndent()+3);
                fieldWriter.write(os,v);
                bufWriter.write(prefix);
            }

            bufWriter.write("</"+k+">\n");
        }

    }
}
