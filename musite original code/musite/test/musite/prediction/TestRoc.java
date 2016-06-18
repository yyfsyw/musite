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

package musite.prediction;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import musite.Protein;
import musite.Proteins;
import musite.PTM;

import musite.prediction.feature.Instance;
import musite.prediction.feature.InstanceFilter;
import musite.prediction.feature.InstancesExtractorFromProteins;
import musite.prediction.feature.ProteinResidueInstannceTag;

import musite.io.MusiteIOUtil;
import musite.io.xml.PredictionResultXMLReader;
import musite.io.xml.ProteinsXMLReader;

import musite.util.AminoAcid;
import musite.util.CollectionUtil;

/**
 *
 * @author Jianjiong Gao
 */
public class TestRoc extends TestCase {

    public void testPredictionRoc() throws IOException {
        String dirRes = "testData/musite-test.pred.xml";
        //String dirRes = "data/S.cerevisiae/S.cerevisiae.uniport.complete.proteome.v57.14.20100209.general.ser.thr.pred.xml.gz";
        PredictionResultXMLReader resReader = PredictionResultXMLReader.createReader();
        PredictionResult result = MusiteIOUtil.read(resReader, dirRes);

        String dirPro = "testData/musite-test.xml";
        //String dirPro = "data/S.cerevisiae/S.cerevisiae.uniport.complete.proteome.v57.14.20100209.experimental.phopsho.musite.xml";
        ProteinsXMLReader proReader = ProteinsXMLReader.createReader();
        Proteins proteins = MusiteIOUtil.read(proReader, dirPro);

        for (PredictionModel model : result.getModels()) {
            Set<AminoAcid> aminoAcids = model.getSupportedAminoAcid();
            PTM ptm = model.getSupportedPTM();

            // extract instances
            InstancesExtractorFromProteins posInsExtractor = new InstancesExtractorFromProteins(proteins, aminoAcids);
            posInsExtractor.setExtractOption(InstancesExtractorFromProteins.ExtractOption.MODIFIED_SITES, ptm);

            InstancesExtractorFromProteins negInsExtractor = new InstancesExtractorFromProteins(proteins, aminoAcids);
            negInsExtractor.setExtractOption(InstancesExtractorFromProteins.ExtractOption.UNMODIFIED_SITES, ptm);

            int proteinCount = proteins.proteinCount();
            if (proteinCount>500) {
                final double extractRatio = 500.0/proteinCount; // to save memory
                InstanceFilter negInsFilter = new InstanceFilter() {
                    public boolean filter(Instance ins) {
                        return Math.random()<extractRatio;
                    }
                };
                negInsExtractor.setInstanceFilter(negInsFilter);
            }

            System.out.println("Extracting positive instance...");
            List<Instance> instances_positive = posInsExtractor.fetch(-1);
            System.out.println("  "+instances_positive.size()+" instances were extracted");

            System.out.println("Extracting unlabeled instance...");
            List<Instance> instances_unlabeled = negInsExtractor.fetch(-1);
            System.out.println("  "+instances_unlabeled.size()+" instances were extracted");
            if (instances_unlabeled.size()>1000)
                instances_unlabeled = musite.util.SamplingUtil.resampleWithoutReplacement(instances_unlabeled, 1000);

            List<Boolean> label = new ArrayList(instances_positive.size()+instances_unlabeled.size());
            CollectionUtil.fillList(label, Boolean.TRUE, instances_positive.size());
            CollectionUtil.fillList(label, Boolean.FALSE, instances_unlabeled.size());

            List<Double> prediction = new ArrayList(instances_positive.size()+instances_unlabeled.size());
            for (Instance ins : instances_positive) {
                prediction.add(getPrediction(result, model, ins));
            }
            for (Instance ins : instances_unlabeled) {
                prediction.add(getPrediction(result, model, ins));
            }

            PredictionRoc roc = new PredictionRoc(label, prediction);

            System.out.println(model.getName());
            System.out.println("auc:"+roc.auc());
            double[][] tp_fps = roc.roc();
            for (int i=0; i<tp_fps.length; i++) {
                double[] tp_fp = tp_fps[i];
                System.out.println(""+tp_fp[0]+"\t"+tp_fp[1]);
            }
        }

    }

    private Double getPrediction(PredictionResult result, PredictionModel model, Instance ins) {
        ProteinResidueInstannceTag tag = (ProteinResidueInstannceTag)ins.getInstanceTag();
        Protein protein = tag.getProtein();
        int site = tag.getPosition();
        return result.getPrediction(model, protein.getAccession(), site);
    }
}
