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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import musite.PTM;

import musite.io.Reader;

import musite.prediction.PredictionModelImpl;
import musite.prediction.PredictionResult;
import musite.prediction.PredictionResultImpl;
import musite.prediction.SpecificityEstimatorImpl;

import musite.util.AminoAcid;

import org.apache.commons.lang.StringEscapeUtils;

import org.dom4j.Element;
import org.dom4j.ElementPath;
import org.dom4j.ElementHandler;

/**
 *
 * @author Jianjiong Gao
 */
public class PredictionResultXMLReader implements Reader<PredictionResult> {
    protected PredictionResult data;
    protected String root = "musite";

    public static PredictionResultXMLReader createReader() {
        return new PredictionResultXMLReader();
    }

    public static PredictionResultXMLReader createReader(PredictionResult result) {
        return new PredictionResultXMLReader(result);
    }

    public PredictionResultXMLReader() {
        this(null);
    }

    public PredictionResultXMLReader(PredictionResult result) {
        this(result, null);
    }

    public PredictionResultXMLReader(PredictionResult result,
            ProteinsXMLReader proteinsReader) {
        this.data = result;        
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public PredictionResult read(InputStream is) throws IOException {
        if (is==null) {
            throw new IllegalArgumentException();
        }

        final PredictionResult result = data==null?new PredictionResultImpl():data;

        ProteinsXMLReader proteinsReader = ProteinsXMLReader.createReader(result, false);
        proteinsReader.setRoot(root);
        
        ProteinResidueAnnotationReader annReader = new ProteinResidueAnnotationReader();
        annReader.putAnnotationFieldReader(musite.prediction.PredictionResult.ANNOTATION_FIELD_SCORE, 
                SimpleFieldXMLReader.createDoubleCollectionReader());
        proteinsReader.putProteinFieldReader(musite.ResidueAnnotationUtil.RESIDUE_ANNOTATION, annReader);

        String path = "/model-list/model";
        if (root!=null)
            path = "/"+root+path;
        proteinsReader.addSaxReaderHandler(path, new ElementHandler() {
            public void onStart(ElementPath path) {
            }
            public void onEnd(ElementPath path) {
                Element elem = path.getCurrent();
                String name = StringEscapeUtils.unescapeXml(elem.attributeValue("name"));

                PTM ptm = null;
                String strPTM = StringEscapeUtils.unescapeXml(elem.elementText("ptm"));
                if (strPTM!=null)
                    ptm = PTM.valueOf(strPTM);

                Set<AminoAcid> aas = null;
                String strAAs = StringEscapeUtils.unescapeXml(elem.elementText("amino-acids"));
                if (strAAs!=null) {
                    String[] strs = strAAs.replaceAll("\n", "").split(";");
                    int n = strs.length;
                    aas = new HashSet(n);
                    for (int i=0; i<n; i++) {
                        String str = strs[i].trim();
                        aas.add(AminoAcid.valueOf(str));
                    }
                }

                SpecificityEstimatorImpl si = null;
                String strSI = StringEscapeUtils.unescapeXml(elem.elementText("spec-estimate-data"));
                if (strSI!=null) {
                    String[] strs = strSI.replaceAll("\n", "").split(";");
                    List<Double> train = new ArrayList();
                    for (String str : strs) {
                        train.add(Double.valueOf(str.trim()));
                    }
                    si = new SpecificityEstimatorImpl(train);
                }

                String comment = StringEscapeUtils.unescapeXml(elem.elementText("comment"));
                if (comment!=null)
                    comment = comment.replaceAll("%EOL%", "\n");

                PredictionModelImpl model = new PredictionModelImpl.Builder()
                        .name(name).ptm(ptm).aminoAcids(aas).specEstimator(si).comment(comment).build();

                result.addModel(model);

                // prune the tree
                elem.detach();
            }
        });
        
        proteinsReader.read(is);

        return result;
    }
}
