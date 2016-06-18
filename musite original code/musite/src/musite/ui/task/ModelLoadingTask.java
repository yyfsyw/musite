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

package musite.ui.task;

import java.util.List;

import musite.Musite;

import musite.prediction.PredictionModel;

/**
 *
 */
public class ModelLoadingTask extends AbstractTask {
    private String modelFile;

    
    public ModelLoadingTask(String modelFile) {
        super("Model loading");
        this.modelFile = modelFile;
    }

    /**
     * Executes Task.
     */
    //@Override
    public void run() {
        try {
            taskMonitor.setStatus("Loading "+modelFile+" ...");
            taskMonitor.setPercentCompleted(-1);
            obj = Musite.getModelManager().loadModels(modelFile);
            taskMonitor.setPercentCompleted(100);
            taskMonitor.setStatus("Loaded.");

            success = true;
        } catch (Exception e) {
            taskMonitor.setPercentCompleted(100);
            taskMonitor.setStatus("Failed to Load.\n"+e.getMessage());
            e.printStackTrace();
            return;
        }
    }

    public List<PredictionModel> getModel() {
        return (List<PredictionModel>)obj;
    }
}
