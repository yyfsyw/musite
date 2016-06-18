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

package musite.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 *
 * @author Jianjiong Gao
 */
public final class MusiteIOUtil {

    private MusiteIOUtil() {}

    /**
     * 
     * @param reader
     * @param dir
     * @return
     * @throws IOException
     */
    public static <C> C read(Reader<C> reader, String dir) throws IOException {
        FileInputStream fis = new FileInputStream(dir);
        if (dir.toLowerCase().endsWith(".gz"))
            return read(reader,new GZIPInputStream(fis));
        else
            return read(reader, fis);
    }

    /**
     * Read and close the stream.
     * @param reader
     * @param is
     * @return
     * @throws IOException
     */
    public static <C> C read(Reader<C> reader, InputStream is) throws IOException {
        C c = reader.read(is);
        is.close();
        return c;
    }

    /**
     *
     * @param writer
     * @param dir
     * @param proteins
     * @throws IOException
     */
    public static <C> void write(Writer<C> writer, String dir, C c) throws IOException {
        FileOutputStream fos = new FileOutputStream(dir);
        if (dir.toLowerCase().endsWith(".gz"))
            write(writer, new GZIPOutputStream(fos), c);
        else
            write(writer, fos, c);
    }

    /**
     * Write and close the stream.
     * @param writer
     * @param os
     * @param proteins
     * @throws IOException
     */
    public static <C> void write(Writer<C> writer, OutputStream os, C c) throws IOException {
        writer.write(os, c);
        os.close();
    }

    
}
