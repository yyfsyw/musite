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

package musite.misc.othertools;

import musite.io.text.PredictionResultsTxtReader;
import musite.io.MusiteIOUtil;
import musite.Proteins;
import musite.Protein;
import musite.PTM;
import musite.io.xml.ProteinsXMLReader;
import musite.io.xml.PredictionResultXMLWriter;
import musite.io.xml.PredictionResultXMLReader;
import musite.prediction.PredictionResult;
import musite.PTMAnnotationUtil;
import musite.prediction.test.TestResultEvaluator;
import musite.util.AminoAcid;
import musite.prediction.PredictionModel;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.Iterator;
import java.util.Map;
import java.util.HashSet;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
/**
 *
 * @author LucasYao
 */
public class AllToolsUtil {

    public static void writeXMLfromResults(String inputproteinfile, String inputresultfile, PTM resultptm,Set<AminoAcid> aminoAcids, String modelname) throws IOException{

        ProteinsXMLReader reader = ProteinsXMLReader.createReader();
        Proteins proteins = MusiteIOUtil.read(reader, inputproteinfile);

        Collection<Protein> proteincollection = proteins.proteins();
        Iterator<Protein> iterator = proteincollection.iterator();



        //int procount = 0;
        while(iterator.hasNext()){
            //System.out.println(++procount);
            Protein protein = iterator.next();
            //do something to protein;
            PTMAnnotationUtil.removePTMAnnotation(protein, resultptm);

            }

        PredictionResultsTxtReader prreader = new PredictionResultsTxtReader(proteins,modelname, resultptm, aminoAcids);
        PredictionResult pr = MusiteIOUtil.read(prreader, inputresultfile);
        PredictionResultXMLWriter writer = PredictionResultXMLWriter.createWriter();
        String outpredfile = inputresultfile.replaceAll(".txt", ".pred.xml");
        MusiteIOUtil.write(writer, outpredfile, pr);

        
    }

    public static void calculateROC(String predictionxml, String truthproteinfile, String outputfile, PTM resultptm, Set<AminoAcid> aas, int numofdots) throws IOException{
        ProteinsXMLReader reader = ProteinsXMLReader.createReader();
        Proteins proteins = MusiteIOUtil.read(reader, truthproteinfile);

        PredictionResultXMLReader pdreader = PredictionResultXMLReader.createReader();
        PredictionResult pr = MusiteIOUtil.read(pdreader, predictionxml);
        Iterator<PredictionModel> pditer = pr.getModels().iterator();
        int size = pr.getModels().size();
        PredictionModel pd = pditer.next();

        double minscore = pr.getMinPredictionScore(pd);
        double maxscore = pr.getMaxPredictionScore(pd);
        double step = (maxscore - minscore)/(numofdots+1);
        String threashold = "Threashold = ";
        String sensitivity = "Sensitivity = ";
        String specifity = "Specifity = ";

        for(int i = 0; i<numofdots+2; i++)
        {
        double curthred = minscore+i*step;
        pr.setThreshold(pd, curthred);

        TestResultEvaluator evaluator = new TestResultEvaluator();
        double[] b = evaluator.evaluate(proteins, pr, pd, resultptm, aas, true);
        threashold = threashold + " " + curthred;
        sensitivity = sensitivity + " " + b[0];
        specifity = specifity + " " + b[1];
        }

        FileWriter fw = new FileWriter(outputfile);
        BufferedWriter fout = new BufferedWriter(fw);
        fout.write(threashold);
        fout.newLine();
        fout.write(sensitivity);
        fout.newLine();
        fout.write(specifity);

        fout.close();
    }

        public static void calculateROC(String predictionxml, String truthproteinfile, String outputfile, PTM resultptm, Set<AminoAcid> aas, int numofdots, double minscore, double maxscore) throws IOException{
        ProteinsXMLReader reader = ProteinsXMLReader.createReader();
        Proteins proteins = MusiteIOUtil.read(reader, truthproteinfile);

        PredictionResultXMLReader pdreader = PredictionResultXMLReader.createReader();
        PredictionResult pr = MusiteIOUtil.read(pdreader, predictionxml);
        Iterator<PredictionModel> pditer = pr.getModels().iterator();
        int size = pr.getModels().size();
        PredictionModel pd = pditer.next();


        double step = (maxscore - minscore)/(numofdots+1);
        String threashold = "Threashold = ";
        String sensitivity = "Sensitivity = ";
        String specifity = "Specifity = ";
        String negspecifity = "1 - Specifity = ";

        for(int i = 0; i<numofdots+2; i++)
        {
        double curthred = minscore+i*step;
        pr.setThreshold(pd, curthred);

        TestResultEvaluator evaluator = new TestResultEvaluator();
        double[] b = evaluator.evaluate(proteins, pr, pd, resultptm, aas, true);
        threashold = threashold + " " + curthred;
        sensitivity = sensitivity + " " + b[0];
        specifity = specifity + " " + b[1];
        double c = 1-b[1];
        negspecifity = negspecifity + " " + c;
        }

        FileWriter fw = new FileWriter(outputfile);
        BufferedWriter fout = new BufferedWriter(fw);
        fout.write(threashold);
        fout.newLine();
        fout.write(sensitivity);
        fout.newLine();
        fout.write(specifity);
        fout.newLine();
        fout.write(negspecifity);

        fout.close();
    }

    public static void calculateROC(String predictionxml, String truthproteinfile, String outputfile, PTM resultptm, Set<AminoAcid> aas) throws IOException{
        ProteinsXMLReader reader = ProteinsXMLReader.createReader();
        Proteins proteins = MusiteIOUtil.read(reader, truthproteinfile);

        PredictionResultXMLReader pdreader = PredictionResultXMLReader.createReader();
        PredictionResult pr = MusiteIOUtil.read(pdreader, predictionxml);
        Iterator<PredictionModel> pditer = pr.getModels().iterator();
        int size = pr.getModels().size();
        PredictionModel pd = pditer.next();


        Set<String> accessnames = proteins.getProteinsAccessions();
        Iterator<String> iterset = accessnames.iterator();
        Set<Double> uniquescore = new HashSet<Double>();
        while(iterset.hasNext())
        {
            String accessname = iterset.next();
            Map<Integer, Double> sitemap = pr.getPredictions(pd, accessname);

            if(sitemap!=null)
            {
                uniquescore.addAll(sitemap.values());
            }
        }
        ArrayList<Double> scorearray = new ArrayList<Double>();
        scorearray.addAll(uniquescore);
        Collections.sort(scorearray);


        String threashold = "Threashold = ";
        String sensitivity = "Sensitivity = ";
        String specifity = "Specifity = ";

        for(int i = 0; i<scorearray.size(); i++)
        {
        double curthred = scorearray.get(i);
        pr.setThreshold(pd, curthred);

        TestResultEvaluator evaluator = new TestResultEvaluator();
        double[] b = evaluator.evaluate(proteins, pr, pd, resultptm, aas, true);
        threashold = threashold + " " + curthred;
        sensitivity = sensitivity + " " + b[0];
        specifity = specifity + " " + b[1];
        }

        FileWriter fw = new FileWriter(outputfile);
        BufferedWriter fout = new BufferedWriter(fw);
        fout.write(threashold);
        fout.newLine();
        fout.write(sensitivity);
        fout.newLine();
        fout.write(specifity);

        fout.close();
    }

}
