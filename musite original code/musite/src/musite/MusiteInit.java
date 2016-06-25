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

package musite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.Properties;

import musite.prediction.PredictionModelManager;

/**
 *
 * @author Jianjiong Gao
 */
public final class MusiteInit {

    public static void init() {
        initializeFolders();
        initializeModels();
    }
/*
using final string to divide cateogoried and get direction from the begining
it is much easier to read from local file directly
the remaining code are basicaly init& error cheng and  then  throw message/set up
*/
    public static final String BASE_DIR = ".";
    public static final String TMP_DIR = BASE_DIR+File.separator+"tmp";
    public static final String RESOURCE_DIR = BASE_DIR+File.separator+"resource";
    public static final String MATRIX_DIR = RESOURCE_DIR+File.separator+"matrix";
    public static final String THIRD_PARTY_DIR = BASE_DIR+File.separator+"3rd_party";
    public static final String MODEL_DIR = BASE_DIR+File.separator+"model";
    public static final String PROPS_DIR = BASE_DIR+File.separator+"props";
    public static final String GLOBAL_PROPS_FILE = PROPS_DIR+File.separator+"global.param";
    public static final String TRAINING_PARAM_PROPS_FILE = PROPS_DIR+File.separator+"training.param";

    public static final String MODEL_APPEDIX = "model";

    private static final String globalPropsVersion = "1.0";

    private static void initializeFolders() {
        File modelDirFile = new File(MODEL_DIR);
        if (!modelDirFile.exists()) {
            if (!modelDirFile.mkdir()) {
                //TODO: error processing
            }
        }

        File propsDirFile = new  File(PROPS_DIR);
        if (!propsDirFile.exists()) {
            if (!propsDirFile.mkdir()) {
                //TODO: error processing
            }
        }

        File tmpDirFile = new File(TMP_DIR);
        if (!tmpDirFile.exists()) {
            if (!tmpDirFile.mkdir()) {
                //TODO: error processing
            }
        }
    }

    public static String defaultPath = BASE_DIR;

    public static final Properties globalProps = new Properties();
    static {
        File gFile = new File(GLOBAL_PROPS_FILE);
        boolean loaded = false;
        if (gFile.exists()) {
            try {
                globalProps.load(new FileInputStream(gFile));
                String version = globalProps.getProperty("Version");
                if (version!=null && globalPropsVersion.compareTo(version)==0) {
                    loaded = true;
                }
                
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }

        if (!loaded) {
            Properties props = defaultGlobalProps();
            globalProps.putAll(props);
            try {
                globalProps.store(new FileOutputStream(gFile), "Global Properties");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static final Properties trainingProps = defaultTrainingProps();
    static {
        File tFile = new File(TRAINING_PARAM_PROPS_FILE);
        boolean save = true;
        if (tFile.exists()) {
            try {
                Properties props = new Properties();
                props.load(new FileInputStream(tFile));
                trainingProps.putAll(props);
                save = false;
            } catch (Exception e) {
                //e.printStackTrace();                
            }
        }

        if (save) {
            try {
                trainingProps.store(new FileOutputStream(tFile), "Training Properties");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    public static void initializeModels() {
        PredictionModelManager manager = Musite.getModelManager();
        manager.clear();
        File modelDirFile = new File(MODEL_DIR);
        for (File file : modelDirFile.listFiles()) {
          if (file.isFile()) {
              String dir = file.getAbsolutePath();
              if (!dir.endsWith("."+MODEL_APPEDIX))
                  continue;

              try {
                  manager.addModelFile(file.getPath());
              } catch (java.io.FileNotFoundException e) {
                  e.printStackTrace();
              }

//              try {
//                  System.out.println(file.getAbsolutePath());
//                  java.util.List<musite.prediction.PredictionModel> model =
//                          new java.util.ArrayList(manager.loadModels(file.getAbsolutePath()));
//                  for (musite.prediction.PredictionModel m : model) {
//
//                  }
//                  musite.util.IOUtil.serializeObject(model, file.getAbsolutePath(), "gz");
//              } catch (Exception e) {
//                  e.printStackTrace();
//              }
          }
        }
    }

    public static final String GLOBAL_PROP_VERSION = "version";
    public static final String GLOBAL_PROP_VSL2_FILE = "disorder_predictor_vsl2";
    public static final String GLOBAL_PROP_SVM_TRAIN_FILE = "SVM_training";
    public static final String GLOBAL_PROP_SVM_CLASSIFY_FILE = "SVM_classify";
    public static final String GLOBAL_PROP_BLAST_CLUST_FILE = "BLASTclust";
    public static final String GLOBAL_PROP_CDHIT_FILE = "CDHit";

    public static Properties defaultGlobalProps() {
        Properties props = new Properties();
        props.setProperty(GLOBAL_PROP_VERSION, globalPropsVersion);
        //String thirdParty = MusiteInit.thirdPartyDirFile.getAbsolutePath();
        props.setProperty(GLOBAL_PROP_VSL2_FILE, THIRD_PARTY_DIR+File.separator+"VSL2.jar");
        if (org.apache.commons.lang.SystemUtils.IS_OS_WINDOWS) {
            props.setProperty(GLOBAL_PROP_SVM_TRAIN_FILE, THIRD_PARTY_DIR+File.separator+"svm_learn.exe");
            props.setProperty(GLOBAL_PROP_SVM_CLASSIFY_FILE, THIRD_PARTY_DIR+File.separator+"svm_classify.exe");
            //props.setProperty(GLOBAL_PROP_BLAST_CLUST_FILE, THIRD_PARTY_DIR+File.separator+"blastclust.exe");
            props.setProperty(GLOBAL_PROP_CDHIT_FILE, THIRD_PARTY_DIR+File.separator+"cd_hit.exe");
        } else if (org.apache.commons.lang.SystemUtils.IS_OS_MAC) { // linux
            props.setProperty(GLOBAL_PROP_SVM_TRAIN_FILE, THIRD_PARTY_DIR+File.separator+"svm_learn_osx");
            props.setProperty(GLOBAL_PROP_SVM_CLASSIFY_FILE, THIRD_PARTY_DIR+File.separator+"svm_classify_osx");
            //props.setProperty(GLOBAL_PROP_BLAST_CLUST_FILE, THIRD_PARTY_DIR+File.separator+"blastclust");
            props.setProperty(GLOBAL_PROP_CDHIT_FILE, THIRD_PARTY_DIR+File.separator+"cd_hit_osx");
        } else { // linux/unix
            props.setProperty(GLOBAL_PROP_SVM_TRAIN_FILE, THIRD_PARTY_DIR+File.separator+"svm_learn_linux");
            props.setProperty(GLOBAL_PROP_SVM_CLASSIFY_FILE, THIRD_PARTY_DIR+File.separator+"svm_classify_linux");
            //props.setProperty(GLOBAL_PROP_BLAST_CLUST_FILE, THIRD_PARTY_DIR+File.separator+"blastclust");
            props.setProperty(GLOBAL_PROP_CDHIT_FILE, THIRD_PARTY_DIR+File.separator+"cd_hit_linux");
        }
        return props;
    }

    public static void saveTrainingProps() {
        File tFile = new File(TRAINING_PARAM_PROPS_FILE);
        try {
            trainingProps.store(new FileOutputStream(tFile), "Training Properties");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final String TRAINING_PROPS_SVM_PARAMETERS = "SVM parameters";
    public static final String TRAINING_PROPS_NO_OF_BOOTS = "No. of boots for positive/negative set";
    public static final String TRAINING_PROPS_NO_OF_CLASSIFIERS = "No. of classifiers aggregated";
    public static final String TRAINING_PROPS_NEGATIVE_CONTROL_SIZE = "Number of negative data for specificity estimation";
    public static final String TRAINING_PROPS_USE_KNN_FEATURES = "KNN features?";
    public static final String TRAINING_PROPS_SUBSTITUTION_MATRIX = "KNN substitution matrix";
    public static final String TRAINING_PROPS_KNN_WINDOW_SIZE = "KNN window sizes";
    public static final String TRAINING_PROPS_KNN_NEIGHBOR_SIZE = "KNN neighbor sizes (% of bootstrapped sample)";
    public static final String TRAINING_PROPS_USE_DISORDER_FEATURES = "Disorder features?";
    public static final String TRAINING_PROPS_DISORDER_WINDOW_SIZES = "Disorder window sizes";
    public static final String TRAINING_PROPS_USE_FREQUENCY_FEATURES = "Frequency features?";
    public static final String TRAINING_PROPS_FREQUENCY_WINDOW_SIZE = "Frequency window size";
    public static final String TRAINING_PROPS_FREQUENCY_FEATURE_NUMBER = "Frequency feature number";
    public static final String TRAINING_PROPS_PADDING_TERMINALS = "Padding terminals?";

    public static Properties defaultTrainingProps() {
        Properties props = new Properties();
        props.setProperty(TRAINING_PROPS_SVM_PARAMETERS,"-t 2 -g 1 -c 10 -m 256");

        props.setProperty(TRAINING_PROPS_NO_OF_BOOTS,"2000");
        props.setProperty(TRAINING_PROPS_NO_OF_CLASSIFIERS,"5");

        props.setProperty(TRAINING_PROPS_NEGATIVE_CONTROL_SIZE, "10000");

        props.setProperty(TRAINING_PROPS_USE_KNN_FEATURES,"true");
        props.setProperty(TRAINING_PROPS_SUBSTITUTION_MATRIX,"blosum62");
        props.setProperty(TRAINING_PROPS_KNN_WINDOW_SIZE,"13");
        props.setProperty(TRAINING_PROPS_KNN_NEIGHBOR_SIZE, "0.25,0.5,1,2,4");

        props.setProperty(TRAINING_PROPS_USE_DISORDER_FEATURES,"true");
        props.setProperty(TRAINING_PROPS_DISORDER_WINDOW_SIZES,"1,5,13");

        props.setProperty(TRAINING_PROPS_USE_FREQUENCY_FEATURES,"true");
        props.setProperty(TRAINING_PROPS_FREQUENCY_WINDOW_SIZE,"13");
        props.setProperty(TRAINING_PROPS_FREQUENCY_FEATURE_NUMBER,"20");

        props.setProperty(TRAINING_PROPS_PADDING_TERMINALS,"true");
        return props;
    }
}
