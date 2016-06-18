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
import cytoscape.task.ui.JTaskConfig;
import cytoscape.task.util.TaskManager;

import java.awt.Frame;

/**
 *
 * @author Jianjiong Gao
 */
public final class TaskUtil {

    public static void execute(Task task) {
        execute(task, new TaskConfigBuilder().build());
    }

    public static void execute(Task task, Frame container) {
        execute(task, new TaskConfigBuilder().onwer(container).build());
    }

    public static void execute(Task task, JTaskConfig config) {
        TaskManager.executeTask(task, config);
    }

    public static class TaskConfigBuilder {
        private Frame onwer = musite.Musite.getDesktop();
        private boolean displayCloseButton = true;
        private boolean displayCancelButton = false;
        private boolean displayStatus = true;
        private boolean autoDispose = true;
        private boolean displayTimeElapsed = true;
        private boolean displayTimeRemaining = false;
        private boolean modal = true;
        private int millisToPopup = 100;

        public JTaskConfig build() {
            JTaskConfig config = new JTaskConfig();
            config.setOwner(onwer);
            config.displayCloseButton(displayCloseButton);
            config.displayCancelButton(displayCancelButton);
            config.displayStatus(displayStatus);
            config.setAutoDispose(autoDispose);
            config.displayTimeElapsed(displayTimeElapsed);
            config.displayTimeRemaining(displayTimeRemaining);
            config.setModal(modal);
            config.setMillisToPopup(millisToPopup);
            return config;
        }

        public TaskConfigBuilder onwer(Frame frame) {
            this.onwer = frame;
            return this;
        }

        public TaskConfigBuilder displayCloseButton(boolean displayCloseButton) {
            this.displayCloseButton = displayCloseButton;
            return this;
        }

        public TaskConfigBuilder displayCancelButton(boolean displayCancelButton) {
            this.displayCancelButton = displayCancelButton;
            return this;
        }

        public TaskConfigBuilder displayStatus(boolean displayStatus) {
            this.displayStatus = displayStatus;
            return this;
        }

        public TaskConfigBuilder autoDispose(boolean autoDispose) {
            this.autoDispose = autoDispose;
            return this;
        }

        public TaskConfigBuilder displayTimeElapsed(boolean displayTimeElapsed) {
            this.displayTimeElapsed = displayTimeElapsed;
            return this;
        }

        public TaskConfigBuilder displayTimeRemaining(boolean displayTimeRemaining) {
            this.displayTimeRemaining = displayTimeRemaining;
            return this;
        }

        public TaskConfigBuilder modal(boolean modal) {
            this.modal = modal;
            return this;
        }

        public TaskConfigBuilder millisToPopup(int millisToPopup) {
            this.millisToPopup = millisToPopup;
            return this;
        }
    }
}
