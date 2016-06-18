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

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import musite.PTM;

import musite.prediction.PredictionModel;
import musite.prediction.PredictionResult;
import musite.prediction.SpecificityEstimator;
import musite.prediction.SpecificityEstimatorImpl;

import musite.util.AminoAcid;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jianjiong Gao
 */
public class PredictionResultXMLWriter extends AbstractXMLWriter<PredictionResult> {
    public static PredictionResultXMLWriter createWriter() {
        PredictionResultXMLWriter writer = new PredictionResultXMLWriter();
        return writer;
    }

    private ProteinsXMLWriter proteinsWriter;
    private String root = "musite";

    public PredictionResultXMLWriter() {
        this(null);
    }
    
    public PredictionResultXMLWriter(ProteinsXMLWriter proteinsWriter) {
        this.proteinsWriter = proteinsWriter!=null?proteinsWriter:ProteinsXMLWriter.createWriterNoHeader();
        this.proteinsWriter.setRoot(null);
    }

    public void setRoot(String root) {
        this.root = root;
    }

    /**
     * {@inheritDoc}
     */
    public void write(final OutputStream os, final PredictionResult data) throws IOException {
        if (data==null) {
            throw new IllegalArgumentException();
        }

        int indent = getIndent();
        String prefix0 = StringUtils.repeat("\t", indent);
        String prefix1 = prefix0+"\t";

        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bufWriter = new BufferedWriter(osw);
        
        if (root!=null)
            bufWriter.write(prefix0+"<"+root+">\n");
        
        bufWriter.write(prefix0+"<model-list>\n");

        CollectionXMLWriter collectionWriter = new CollectionXMLWriter();
        //collectionWriter.setIndent(indent+3);
        Set<PredictionModel> models = data.getModels();
        for (PredictionModel model : models) {
            String name = model.getName();
            bufWriter.write(prefix0+"<model name=\""+name+"\">\n");

            PTM ptm = model.getSupportedPTM();
            if (ptm!=null)
                bufWriter.write(prefix1+"<ptm>"+StringEscapeUtils.escapeXml(ptm.name())+"</ptm>\n");

            Set<AminoAcid> aminoAcids = model.getSupportedAminoAcid();
            if (aminoAcids!=null && !aminoAcids.isEmpty()) {
                bufWriter.write(prefix1+"<amino-acids>");
                Iterator<AminoAcid> it = aminoAcids.iterator();
                bufWriter.write(StringEscapeUtils.escapeXml(it.next().name()));
                while (it.hasNext())
                    bufWriter.write(StringEscapeUtils.escapeXml(";"+it.next().name()));
                bufWriter.write("</amino-acids>\n");
            }

            SpecificityEstimator se = model.getSpecEstimator();
            if (se instanceof SpecificityEstimatorImpl) {
                bufWriter.write(prefix1+"<spec-estimate-data>");
                bufWriter.flush();
                SpecificityEstimatorImpl sei = (SpecificityEstimatorImpl)se;
                List<Double> train = sei.trainingPredictions();
                collectionWriter.write(os, train);
                bufWriter.write("</spec-estimate-data>\n");
            }

            String comment = model.getComment();
            if (comment!=null && comment.length()>0) {
                comment = comment.replaceAll("\n", "%EOL%");
                bufWriter.write(prefix1+"<comment>");
                bufWriter.write(StringEscapeUtils.escapeXml(comment));
                bufWriter.write("</comment>\n");
            }

            bufWriter.write(prefix0+"</model>\n");
        }

        bufWriter.write("</model-list>\n");
        bufWriter.flush();

        proteinsWriter.write(os, data);

        if (root!=null)
            bufWriter.write(prefix0+"</"+root+">");
        bufWriter.flush();
    }
}
