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

import musite.Proteins;
import musite.MusiteClassify;

import musite.prediction.PredictionResult;

import musite.prediction.PredictionModel;

/**
 *
 */
public class MusiteClassifyTask extends AbstractTask {
    private final PredictionModel model;
    private final Proteins proteins;
    private final MusiteClassify classify;

    public MusiteClassifyTask(final PredictionModel model,
            final Proteins proteins, final MusiteClassify classify) {
        super("Predicting sites for "+model.getName());
        this.model = model;
        this.proteins = proteins;
        this.classify = classify;
    }
    
    /**
     * Executes Task.
     */
    //@Override
    public void run() {
            try {
                    taskMonitor.setPercentCompleted(-1);

                    taskMonitor.setStatus("Predicting...");
                    classify.setTaskMonitor(taskMonitor);

                    obj = classify.classify(model,proteins);

                    taskMonitor.setPercentCompleted(100);
                    taskMonitor.setStatus("Successfully predicted");

                    success = true;
            } catch (Exception e) {
                    taskMonitor.setPercentCompleted(100);
                    taskMonitor.setStatus("Failed to predict.\n"+e.getMessage());
                    e.printStackTrace();
                    return;
            }
    }

    public PredictionResult getResult() {
        return (PredictionResult)obj;
    }

}
