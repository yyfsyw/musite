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

import java.io.InputStream;

import musite.io.Reader;
import musite.io.MusiteIOUtil;

/**
 *
 */
public class ReadTask<C> extends AbstractTask {
    private final Reader<C> reader;
    private final InputStream is;
    private final String file;

    public ReadTask(final Reader<C> reader, String file) {
        super("reading");
        this.reader = reader;
        this.file = file;
        is = null;
    }
    
    public ReadTask(final Reader<C> reader, InputStream is) {
            super("reading");
            this.reader = reader;
            this.is = is;
            file = null;
    }

    /**
     * Executes Task.
     */
    //@Override
    public void run() {
            try {
                    taskMonitor.setPercentCompleted(-1);
                    
                    taskMonitor.setStatus("reading.");
                    if (is!=null)
                        obj = MusiteIOUtil.read(reader, is);
                    else if (file!=null)
                        obj = MusiteIOUtil.read(reader, file);

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

    public C getResultObject() {
        return (C)obj;
    }
}
