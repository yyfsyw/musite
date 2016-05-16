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

import java.io.OutputStream;

import musite.io.MusiteIOUtil;
import musite.io.Writer;

/**
 *
 */
public class WriteTask<C> extends AbstractTask {
    private final C c;
    private final Writer writer;
    private final OutputStream os;
    private final String file;

    public WriteTask(final C c, Writer writer, OutputStream os) {
        super("Write proteins");
        this.c = c;
        this.writer = writer;
        this.os = os;
        file = null;
    }

    public WriteTask(final C c, Writer writer, String file) {
        super("Write proteins");
        this.c = c;
        this.writer = writer;
        this.os = null;
        this.file = file;
    }

    /**
     * Executes Task.
     */
    //@Override
    public void run() {
            try {
                    taskMonitor.setPercentCompleted(-1);
                    
                    taskMonitor.setStatus("Writing...");

                    if (os!=null)
                        MusiteIOUtil.write(writer, os, c);
                    else if (file!=null)
                        MusiteIOUtil.write(writer, file, c);

                    taskMonitor.setPercentCompleted(100);
                    taskMonitor.setStatus("read.");

                    success = true;
            } catch (Exception e) {
                    taskMonitor.setPercentCompleted(100);
                    taskMonitor.setStatus("Failed to read.\n"+e.getMessage());
                    e.printStackTrace();
                    return;
            }
    }

}
