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

import musite.util.IOUtil;

/**
 *
 */
public class DeserializeTask extends AbstractTask {
    private final String dir;
    private final String format;

    public DeserializeTask(String dir, String format) {
            super ("Deserialize");
            this.dir = dir;
            this.format = format;
    }

    /**
     * Executes Task.
     */
    //@Override
    public void run() {
            try {
                    taskMonitor.setPercentCompleted(-1);
                    taskMonitor.setStatus("Loading...");
                    obj = IOUtil.deserializeObject(dir, format);
                    taskMonitor.setPercentCompleted(100);
                    taskMonitor.setStatus("Loaded.");

                    success = true;
            } catch (Exception e) {
                    taskMonitor.setPercentCompleted(100);
                    taskMonitor.setStatus("Failed to save.\n"+e.getMessage());
                    e.printStackTrace();
                    return;
            }
    }

    public Object getObject() {
        return obj;
    }
}
