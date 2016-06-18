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

import java.beans.PropertyChangeSupport;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import musite.util.IOUtil;


/**
 *
 * @author Jianjiong Gao
 */
public final class PredictionModelManager {
    private static PredictionModelManager manager;

    static {
        manager = new PredictionModelManager();
    }

    public static PredictionModelManager getManager() {
        return manager;
    }

    private PropertyChangeSupport pcs;
    private Map<String,List<PredictionModel>> mapFileModels;


    public static final String FILE_ADD = "FILE_ADD";
    public static final String FILE_REMOVE = "FILE_REMOVE";
    public static final String FILE_CLEAR = "FILE_CLEAR";
    public static final String FILE_RESET = "FILE_RESET";

    private PredictionModelManager() {
        pcs = new PropertyChangeSupport(this);
        mapFileModels = new HashMap();
    }

    public int modelFileCount() {
        return mapFileModels.size();
    }

    public Set<String> modelFiles() {
        return Collections.unmodifiableSet(mapFileModels.keySet());
    }

    public boolean isModelFileLoaded(String file) {
        if (file==null) {
            throw new java.lang.IllegalArgumentException();
        }

        return mapFileModels.get(file)!=null;
    }

    public List<PredictionModel> loadModels(String file, String modelName)
            throws IOException, ClassNotFoundException {
        List<PredictionModel> models = loadModels(file);
        if (models==null)
            return null;
        if (modelName==null)
            return models;

        List<PredictionModel> ret = new ArrayList();
        for (PredictionModel model : models) {
            if (model.getName().equals(modelName))
                ret.add(model);
        }

        return ret;
    }

    public List<PredictionModel> loadModels(String file)
            throws IOException, ClassNotFoundException {
        if (file==null)
            return null;
        List<PredictionModel> models = mapFileModels.get(file);
        if (models==null) {
            Object obj = IOUtil.deserializeObject(file, "gz");
            
            if (obj instanceof List) {
                models = (List<PredictionModel>)obj;
            } else if (obj instanceof PredictionModel[]) {
                models = new ArrayList(java.util.Arrays.asList((PredictionModel[])obj));
            } else if (obj instanceof PredictionModel) {
                models = new ArrayList();
                models.add((PredictionModel)obj);
            } else {
                return null;
            }
            mapFileModels.put(file, models);
            for (PredictionModel m : models) {
                m.setFile(file);
            }
        }
        return Collections.unmodifiableList(models);
    }

    public void unloadModels(String file) {
        if (file==null)
            return;
        List<PredictionModel> models = mapFileModels.get(file);
        if (models!=null) {
            mapFileModels.put(file, null);
        }
    }

//    public String getModelFile(String name) {
//        if (name==null)
//            return null;
//        return modelFiles.get(name);
//    }

    public void addModelFile(String file)
            throws FileNotFoundException {
        addModelFile(file, null);
    }

    public void addModelFile(String file, List<PredictionModel> models)
            throws FileNotFoundException {
        if (file==null)
            return;

        if (!IOUtil.fileExist(file)) {
            throw new FileNotFoundException();
        }
        
        if (models!=null) {
            mapFileModels.put(file, new ArrayList<PredictionModel>(models));

            for (PredictionModel m : models) {
                m.setFile(file);
            }
        } else {
            mapFileModels.put(file, null);
        }


        pcs.firePropertyChange(FILE_ADD, null, models);
    }
//
//    public void saveModelsToFile(String file) throws IOException {
//        if (file==null)
//            return;
//
//        PredictionModel[] model = mapFileModels.get(file);
//        if (model!=null) {
//            musite.util.IOUtil.serializeObject(model, file, "gz");
//        }
//    }

    public boolean removeModelFromFile(String file, PredictionModel model)
                throws IOException, ClassNotFoundException{
        if (file==null || model==null) {
            throw new IllegalArgumentException();
        }

        if (loadModels(file)==null) {
            return false;
        }

        List<PredictionModel> models = mapFileModels.get(file);
        return models.remove(model);
    }

    public void removeModelFile(String file) {
        if (file==null) return;
        mapFileModels.remove(file);
//        IOUtil.deleteFile(file);
        pcs.firePropertyChange(FILE_REMOVE, file, null);
    }

    public void clear() {
        mapFileModels.clear();
        pcs.firePropertyChange(FILE_CLEAR, null, null);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return pcs;
    }
}
