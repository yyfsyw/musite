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

import cytoscape.task.Task;
import cytoscape.task.TaskMonitor;

/**
 *
 * @author Jianjiong Gao
 */
public abstract class AbstractTask implements Task {
    protected String title;
    protected TaskMonitor taskMonitor;
    protected boolean success;
    protected Object obj;

    public AbstractTask(String title) {
        this.title= title;
        success = false;
        obj = null;
    }

    abstract public void run();

    public boolean success() {
        return success;
    }

    public Object getResultObject() {
        return obj;
    }

    /**
     * Halts the Task: Not Currently Implemented.
     */
    public void halt() {

    }

    /**
     * Sets the Task Monitor.
     *
     * @param taskMonitor
     *            TaskMonitor Object.
     */
    public void setTaskMonitor(TaskMonitor taskMonitor) throws IllegalThreadStateException {
            this.taskMonitor = taskMonitor;
    }

    /**
     * Gets the Task Title.
     *
     * @return Task Title.
     */
    public String getTitle() {
        return title;
    }
}
