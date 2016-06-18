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

package musite.ui.cmd;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.FlaggedOption;

import java.io.InputStream;
import java.io.IOException;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import musite.Musite;
import musite.MusiteClassify;
import musite.MusiteInit;
import musite.Protein;
import musite.Proteins;

import musite.prediction.feature.disorder.DisorderPredictorVSL2;
import musite.prediction.feature.disorder.DisorderUtil;

import musite.io.MusiteIOUtil;
import musite.io.fasta.ProteinsFastaReader;
import musite.io.fasta.ProteinsReaderFastaVisitor;
import musite.io.fasta.QueryProteinsReaderFastaVisitor;
import musite.io.fasta.parser.SequencePTMSiteTokenAppendedParser;

import musite.prediction.PredictionModel;
import musite.prediction.PredictionModelManager;
import musite.prediction.PredictionResult;
import musite.prediction.PredictionResultImpl;
import musite.prediction.SpecificityEstimator;

import musite.util.IOUtil;
import musite.util.StringUtil;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author gjj
 */
public class CmdLineTools {

    public static void listModelCmd() {
        printModels();
    }

    public static void predictCmd(String[] args)
            throws JSAPException, IOException, ClassNotFoundException {
        JSAP jsap = new JSAP();

        FlaggedOption opt = new FlaggedOption("input-file")
                                .setStringParser(JSAP.STRING_PARSER)
                                .setRequired(true)
                                .setShortFlag('i');
        opt.setHelp("The input file containing query sequences in FASTA format.");
        jsap.registerParameter(opt);

        opt = new FlaggedOption("output-file")
                                .setStringParser(JSAP.STRING_PARSER)
                                .setRequired(true)
                                .setShortFlag('o');
        opt.setHelp("The output file that the result will be written to.");
        jsap.registerParameter(opt);

        opt = new FlaggedOption("model-file")
                                .setStringParser(JSAP.STRING_PARSER)
                                .setRequired(true)
                                .setShortFlag('f');
        opt.setHelp("The prediciton model file.");
        jsap.registerParameter(opt);

        opt = new FlaggedOption("model-name")
                                .setStringParser(JSAP.STRING_PARSER)
                                .setRequired(false)
                                .setShortFlag('m');
        opt.setHelp("The prediction model name.");
        jsap.registerParameter(opt);

        JSAPResult config = parseCmdArgs(jsap, args, "predict");

        String inputFile = config.getString("input-file");
        String outputFile = config.getString("output-file");
        String modelFile = config.getString("model-file");
        String modelName = config.getString("model-name");

        String inputSeq = IOUtil.readStringAscii(inputFile);
        String retult = predict(inputSeq, modelFile, modelName);
        IOUtil.writeStringAscii(retult, outputFile);
    }

    private static JSAPResult parseCmdArgs(JSAP jsap, String[] args, String command) {
        JSAPResult config = jsap.parse(args);

        if (!config.success()) {
            for (java.util.Iterator errs = config.getErrorMessageIterator();
                    errs.hasNext();) {
                System.err.println("Error: " + errs.next());
            }

            System.err.println();
            System.err.println("Usage: java -jar Musite.jar "+command);
            System.err.println("                "
                                + jsap.getUsage());
            System.err.println();
            System.err.println(jsap.getHelp());
            System.exit(1);
        }
        
        return config;
    }

    public static void printModels() {
        PredictionModelManager manager = Musite.getModelManager();
        Set<String> modelFiles = manager.modelFiles();
        for (String file : modelFiles) {
            List<PredictionModel> models;
            try {
                models = manager.loadModels(file);
            } catch(Exception e) {
                e.printStackTrace();
                continue;
            }

            System.out.println(file);

            for (PredictionModel model : models) {
                System.out.println(model.getName());
            }
        }
    }

    public static String predict(final String inputSequence, final String modelFile,
            final String modelName) throws IOException, ClassNotFoundException {
        PredictionModelManager manager = Musite.getModelManager();
        List<PredictionModel> models = manager.loadModels(modelFile, modelName);
        if (models==null)
            return "";

        String seq = inputSequence.trim();
        if (!seq.startsWith(">"))
            seq = ">sequence\n"+seq;

        ProteinsReaderFastaVisitor visitor =
                new QueryProteinsReaderFastaVisitor.Builder()
                .sequenceParser(new  SequencePTMSiteTokenAppendedParser("?"))
                .build();
        ProteinsFastaReader reader = new ProteinsFastaReader(visitor);
        InputStream is = StringUtil.toStream(seq);
        Proteins proteins = MusiteIOUtil.read(reader, is);

        if (proteins==null || proteins.proteinCount()==0)
            return "";

        // selective prediction
        boolean selectivePrediction = false;
        Iterator<Protein> it = proteins.proteinIterator();
        while (it.hasNext()) {
            Protein protein = it.next();
            Collection<Integer> sites;
            try {
                sites = (Collection)protein.getInfo("query");
            } catch(Exception e) {
                continue;
            }
            if (sites!=null && !sites.isEmpty()) {
                selectivePrediction = true;
                break;
            }
        }

        // disorder
        for (PredictionModel  model : models) {
            if (model.getModelProperties()
                    .getProperty(MusiteInit.TRAINING_PROPS_USE_DISORDER_FEATURES)
                    .equalsIgnoreCase("true")) {
                // integrate disorder if used
                String dirVSL2 = MusiteInit.globalProps.getProperty(MusiteInit.GLOBAL_PROP_VSL2_FILE);
                DisorderPredictorVSL2 disorderPredictor = new DisorderPredictorVSL2(dirVSL2);
                DisorderUtil.integrateDisorder(proteins, disorderPredictor, true);
                break;
            }
        }

        // classify
        PredictionResult result = new PredictionResultImpl();
        MusiteClassify classify = new MusiteClassify(result);
        int jobSize = 10;
        classify.setJobSize(jobSize);
        classify.setSelectivePrediction(selectivePrediction);

        for (PredictionModel model : models) {
            classify.classify(model, proteins);
        }

        return printResult(result);
    }
    
    private static final int offset = 12;

    private static String printResult(PredictionResult result) {
        if (result==null || result.getModels().isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        sb.append("Position\tAmino Acid\tSurr. Sequence\tScore\tSpecificity\tModel\n");
        
        Iterator<Protein> it = result.proteinIterator();
        while (it.hasNext()) {
            Protein protein = it.next();
            String acc = protein.getAccession();

            sb.append('>');
            sb.append(protein.toString());
            sb.append('\n');

            for (PredictionModel model :result.getModels()) {
                Map<Integer, Double> preds = result.getPredictions(model, acc);
                if (preds==null || preds.isEmpty())
                    continue;

                String proteinSeq = protein.getSequence();
                int len = proteinSeq.length();

                for (Map.Entry<Integer, Double> entry : preds.entrySet()) {
                    int site = entry.getKey();

                    sb.append(site+1);
                    sb.append('\t');

                    if (site<offset) {
                        sb.append(StringUtils.repeat("*", offset-site));
                        sb.append(proteinSeq.substring(0,site));
                    } else {
                        sb.append(proteinSeq.substring(site-offset,site));
                    }

                    int end = site+offset+1;
                    if (end>len) {
                        sb.append(proteinSeq.substring(site,len));
                        sb.append(StringUtils.repeat("*", end-len));
                    } else {
                        sb.append(proteinSeq.substring(site,end));
                    }

                    sb.append('\t');
                    sb.append(proteinSeq.charAt(site));

                    sb.append('\t');

                    sb.append(preds.get(site));
                    sb.append('\t');

                    SpecificityEstimator est = model.getSpecEstimator();
                    sb.append(est.specificity(preds.get(site)));
                    sb.append('\t');

                    sb.append(model.getName());

                    sb.append('\n');
                }
            }
        }

        return sb.toString();
    }
}
