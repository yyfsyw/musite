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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import musite.Protein;
import musite.Proteins;
import musite.ResidueAnnotationUtil;

import musite.io.Writer;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jianjiong Gao
 */
public class ProteinsXMLWriter extends AbstractXMLWriter<Proteins> {
    protected final Map<String,Writer> proteinFieldWriters;
    protected Map<String, Boolean> proteinFieldIndent;
    protected List<String> priorityFields = null;
    protected Set<String> proteinFieldFilter = null;
    protected boolean proteinFieldInclude = true;
    protected String root = "musite";

    public static ProteinsXMLWriter createWriter() {
        ProteinsXMLWriter writer = createWriterNoHeader();
        return writer;
    }

    public static ProteinsXMLWriter createWriterNoHeader() {
        ProteinsXMLWriter writer = new ProteinsXMLWriter();
        writer.putProteinFieldWriter(
                musite.ResidueAnnotationUtil.RESIDUE_ANNOTATION,
                new ProteinResidueAnnotationWriter(),
                true);
        List<String> fields = new ArrayList();
        fields.add(Protein.ACCESSION);
        fields.add(Protein.SYMBOL);
        fields.add(Protein.ORGANISM);
        fields.add(Protein.DESCRIPTION);
        fields.add(Protein.SEQUENCE);
        fields.add(ResidueAnnotationUtil.RESIDUE_ANNOTATION);
        writer.setPriorityProteinFields(fields);
        return writer;
    }

    public ProteinsXMLWriter() {
        proteinFieldWriters = new HashMap();
        proteinFieldIndent = new HashMap();
    }

    public void putProteinFieldWriter(String field, Writer writer) {
        putProteinFieldWriter(field, writer, false);
    }

    public void putProteinFieldWriter(String field, Writer writer,
            boolean indent) {
        if (field==null)
            throw new IllegalArgumentException();
        proteinFieldWriters.put(field, writer);
        proteinFieldIndent.put(field, indent);
    }

    public void setProteinFieldFilter(Set<String> proteinFields, boolean include) {
        this.proteinFieldFilter = proteinFields;
        this.proteinFieldInclude = include;
    }

    public void setProteinFieldFilter(String proteinField, boolean include) {
        proteinFieldFilter = new HashSet(1);
        proteinFieldFilter.add(proteinField);
        this.proteinFieldInclude = include;
    }

    public void setPriorityProteinFields(List<String> priorityFields) {
        this.priorityFields = priorityFields;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    /**
     * {@inheritDoc}
     */
    public void write(final OutputStream os, final Proteins data) throws IOException {
        if (data==null) {
            throw new IllegalArgumentException();
        }

        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bufWriter = new BufferedWriter(osw);

        int indent = getIndent();
        String prefix0 = StringUtils.repeat("\t", indent);
        String prefix1 = prefix0+"\t";

        if (root!=null)
            bufWriter.write(prefix0+"<"+root+">\n");

        bufWriter.write(prefix0+"<protein-list>\n");

        Iterator<Protein> it = data.proteinIterator();
        while (it.hasNext()) {
            Protein protein = it.next();
            bufWriter.write(prefix0+"<protein>\n");

            Map<String, Object> infoMap = protein.getInfoMap();

            for (String field : fieldList(infoMap.keySet())) {
                if (proteinFieldFilter!=null &&
                        proteinFieldFilter.contains(field)!=proteinFieldInclude)
                    continue;
                
                Object value = infoMap.get(field);
                if (value==null)
                    continue;

                bufWriter.write(prefix1+"<"+field+">");

                Writer fieldWriter = proteinFieldWriters.get(field);
                if (fieldWriter == null) {
                    fieldWriter = XMLUtil.createDefaultWriter(value);
                    bufWriter.flush();
                    fieldWriter.write(os,value);
                } else {
                    Boolean isIndent = proteinFieldIndent.get(field);
                    if (isIndent!=null && isIndent)
                        bufWriter.write("\n");
                    bufWriter.flush();
                    if (isIndent!=null && isIndent && fieldWriter instanceof AbstractXMLWriter)
                        ((AbstractXMLWriter)fieldWriter).setIndent(indent+2);
                    fieldWriter.write(os,value);
                    if (isIndent!=null && isIndent)
                        bufWriter.write(prefix1);
                }                
                
                bufWriter.write("</"+field+">\n");
            }

            bufWriter.write(prefix0+"</protein>\n");
        }

        bufWriter.write(prefix0+"</protein-list>\n");

        if (root!=null)
            bufWriter.write(prefix0+"</"+root+">");

        bufWriter.flush();
    }

    private Collection<String> fieldList(Set<String> fields) {
        if (priorityFields==null) {
            return fields;
        } else {
            List<String> result = new ArrayList();
            result.addAll(priorityFields);
            result.retainAll(result);
            for (String field : fields) {
                if (!priorityFields.contains(field)) {
                    result.add(field);
                }
            }
            return result;
        }
    }
}
