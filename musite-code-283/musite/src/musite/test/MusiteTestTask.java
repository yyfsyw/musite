/**
 * Musite
 * Copyright (C) 2010-2011 Digital Biology Laboratory, University Of Missouri
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

package musite.test;

import java.util.Properties;
import java.util.Set;

import musite.Proteins;
import musite.PTM;

import musite.ui.task.AbstractTask;

import musite.util.AminoAcid;

/**
 *
 */
public class MusiteTestTask extends AbstractTask {
    private Proteins proteins;
    private Proteins negValidData;
    private Properties props;
    private PTM ptm;
    private Set<AminoAcid> aminoAcids;
    private int kfold;
    private int repeat;
    private boolean negTestSameSizeOrPercentage;
    private int negSize;

    private final CrossValidationTest tester;

    private final LeaveOneOutTest looTester;

    public MusiteTestTask(final Proteins proteins,
                            final Proteins negValidData,
                            final PTM ptm,
                            final Set<AminoAcid> aminoAcids,
                            final Properties props,
                            final int negSize) {
            super("Testing");
            this.proteins = proteins;
            this.negValidData = negValidData;
            this.props = props;
            this.ptm = ptm;
            this.aminoAcids = aminoAcids;
            this.negSize = negSize;
            looTester = new LeaveOneOutTest();
            tester = null;
    }

    public MusiteTestTask(final Proteins proteins,
                            final PTM ptm,
                            final Set<AminoAcid> aminoAcids,
                            final Properties props,
                            final int kfold,
                            final int repeat,
                            final boolean negTestSameSizeOrPercentage) {
            super("Testing");
            this.proteins = proteins;
            this.props = props;
            this.ptm = ptm;
            this.aminoAcids = aminoAcids;
            this.kfold = kfold;
            this.repeat = repeat;
            this.negTestSameSizeOrPercentage = negTestSameSizeOrPercentage;
            tester = new CrossValidationTest();
            looTester = null;
    }

    /**
     * Executes Task.
     */
    //@Override
    public void run() {
            try {
                    taskMonitor.setPercentCompleted(-1);
                    
                    taskMonitor.setStatus("Test...");
                    if (tester!=null) {
                        tester.setTaskMonitor(taskMonitor);
                        obj = tester.test(proteins, ptm, aminoAcids, props, kfold, repeat, negTestSameSizeOrPercentage);
                    } else {
                        looTester.setTaskMonitor(taskMonitor);
                        obj = looTester.test(proteins, negValidData, ptm, aminoAcids, props, negSize);
                    }
                    taskMonitor.setPercentCompleted(100);
                    taskMonitor.setStatus("Testing result has been saved.");

                    success = true;
            } catch (Exception e) {
                    taskMonitor.setPercentCompleted(100);
                    taskMonitor.setStatus("Failed to train model.\n"+e.getMessage());
                    e.printStackTrace();
                    return;
            }
    }
}
