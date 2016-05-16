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

import java.util.Properties;
import java.util.Set;

import musite.MusiteTrain;
import musite.Proteins;

import musite.prediction.PredictionModel;

import musite.PTM;

import musite.util.AminoAcid;

/**
 *
 */
public class MusiteTrainTask extends AbstractTask {
    private final Proteins proteins;
    private final Properties props;
    private final PTM ptm;
    private final Set<AminoAcid> aminoAcids;

    private final MusiteTrain trainer;

    public MusiteTrainTask(final Proteins proteins,
                            final PTM ptm,
                            final Set<AminoAcid> aminoAcids,
                            final Properties props) {
            super("Model training");
            this.proteins = proteins;
            this.ptm = ptm;
            this.aminoAcids = aminoAcids;
            this.props = props;
            trainer = new MusiteTrain();
    }

    /**
     * Executes Task.
     */
    //@Override
    public void run() {
            try {
                    taskMonitor.setPercentCompleted(-1);
                    trainer.setTaskMonitor(taskMonitor);
                    
                    taskMonitor.setStatus("The model is being trained...");
                    obj = trainer.train(proteins, ptm, aminoAcids, props);

                    taskMonitor.setPercentCompleted(100);
                    taskMonitor.setStatus("The model has been successfully trained.");

                    success = true;
            } catch (Exception e) {
                    taskMonitor.setPercentCompleted(100);
                    taskMonitor.setStatus("Failed to train model.\n"+e.getMessage());
                    e.printStackTrace();
                    return;
            }
    }

    public PredictionModel getModel() {
        return (PredictionModel)obj;
    }

}
