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
import java.util.Map;

import musite.Proteins;

import musite.prediction.feature.disorder.DisorderPredictor;
import musite.prediction.feature.disorder.DisorderUtil;


/**
 *
 */
public class DisorderPredictionTask extends AbstractTask {
    private final Proteins proteins;
    private final DisorderPredictor predictor;
    //private Map<String, List<Double>> disorder = null;

    public DisorderPredictionTask(final Proteins proteins,
                final DisorderPredictor predictor) {
        super("Predicting disorder");
        this.proteins = proteins;
        this.predictor = predictor;
    }

    /**
     * Executes Task.
     */
    //@Override
    public void run() {
            try {
                    taskMonitor.setPercentCompleted(-1);
                    
                    taskMonitor.setStatus("Predicting.");
                    obj = DisorderUtil.predictDisorder(proteins, predictor, true);

                    taskMonitor.setPercentCompleted(100);
                    taskMonitor.setStatus("Predicted.");
                    success = true;
            } catch (Exception e) {
                    taskMonitor.setPercentCompleted(100);
                    taskMonitor.setStatus("Failed to predict.\n"+e.getMessage());
                    e.printStackTrace();
                    return;
            }
    }


    public Map<String, List<Double>> getDisorder() {
        return (Map<String, List<Double>>)obj;
    }

}
