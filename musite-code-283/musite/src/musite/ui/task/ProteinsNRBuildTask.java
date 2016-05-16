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

import musite.misc.nr.ProteinNRBuilder;


/**
 *
 */
public class ProteinsNRBuildTask extends AbstractTask {
    private final ProteinNRBuilder builder;
    private final Proteins proteins;
    
    public ProteinsNRBuildTask(final ProteinNRBuilder builder,
            final Proteins proteins) {
            super("Building NR DB");
            this.builder = builder;
            this.proteins = proteins;
    }

    /**
     * Executes Task.
     */
    //@Override
    public void run() {
            try {
                    taskMonitor.setPercentCompleted(-1);
                    
                    taskMonitor.setStatus("Building NR DB.");
                    obj = builder.build(proteins);

                    taskMonitor.setPercentCompleted(100);
                    taskMonitor.setStatus("Building.");

                    success = true;
            } catch (Exception e) {
                    taskMonitor.setPercentCompleted(100);
                    taskMonitor.setStatus("Failed to build.\n"+e.getMessage());
                    e.printStackTrace();
                    return;
            }
    }

    public Proteins getNRProteins() {
        return (Proteins)obj;
    }

}
