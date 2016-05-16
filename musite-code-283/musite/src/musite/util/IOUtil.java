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

package musite.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Writer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class IOUtil {

    public static boolean fileExist(String str) {
        File file = new File(str);
        return file.exists();
    }

    public static boolean deleteFile(final String dir) {
        if (dir==null) {
            throw new NullPointerException();
        }

        File file = new File(dir);
        return file.delete();
    }

    public static String readStringAscii(InputStream is) throws IOException {
        return readStringAscii(new InputStreamReader(is));
    }

    public static String readStringAscii(String dir) throws IOException {
        Reader fin = new FileReader(dir);
        String ret = readStringAscii(fin);
        fin.close();
        return ret;
    }

    public static String readStringAscii(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        StringBuilder sb = new StringBuilder();
        String line = in.readLine();
        while (line!=null) {
            sb.append(line).append("\n");
            line = in.readLine();
        }
        in.close();
        return sb.toString();
    }

    public static void writeStringAscii(String str, String dir) throws IOException {
        Writer fout = new FileWriter(dir);
        writeStringAscii(str, fout);
        fout.close();
    }

    public static void writeStringAscii(String str, Writer writer) throws IOException {
        BufferedWriter out = new BufferedWriter(writer);
        out.write(str);
        out.close();
    }

    public static List<String> readStringListAscii(InputStream is) throws IOException {
        return readStringListAscii(new InputStreamReader(is));
    }
	
    public static List<String> readStringListAscii(String dir) throws IOException {
        Reader fin = new FileReader(dir);
        List<String> ret = readStringListAscii(fin);
        fin.close();
        return ret;
    }

    public static List<String> readStringListAscii(Reader reader) throws IOException {
        List<String> ret = new ArrayList<String>();
        BufferedReader in = new BufferedReader(reader);
        String line = in.readLine();
        while (line!=null) {
            ret.add(line);
            line = in.readLine();
        }
        in.close();
        return ret;
    }

    public static Set<String> readStringSetAscii(String dir) throws IOException {
        Reader fin = new FileReader(dir);
        Set<String> ret = readStringSetAscii(fin);
        fin.close();
        return ret;
    }

    public static Set<String> readStringSetAscii(Reader reader) throws IOException {
        Set<String> ret = new HashSet<String>();
        BufferedReader in = new BufferedReader(reader);
        String line = in.readLine();
        while (line!=null) {
            ret.add(line);
            line = in.readLine();
        }
        in.close();
        return ret;
    }

    public static List<Double> readDoubleListAscii(String dir) throws IOException {
        Reader fin = new FileReader(dir);
        List<Double> ret = readDoubleListAscii(fin);
        fin.close();
        return ret;
    }
	
    public static List<Double> readDoubleListAscii(Reader reader) throws IOException {
        List<Double> ret = new ArrayList<Double>();
        BufferedReader in = new BufferedReader(reader);
        String line = in.readLine();
        try {
            while (line!=null) {
                ret.add(Double.parseDouble(line));
                line = in.readLine();
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return null;
        }
        in.close();
        return ret;
    }

    public static void writeCollectionAscii(Collection c, String dir) throws IOException {
        if (c==null||dir==null) {
            throw new java.lang.IllegalArgumentException();
        }
        Writer fout = new FileWriter(dir);
        writeCollectionAscii(c, fout);
        fout.close();
    }

    public static void writeCollectionAscii(Collection c, Writer writer) throws IOException {
        if (c==null||writer==null) {
            throw new java.lang.IllegalArgumentException();
        }
        BufferedWriter out = new BufferedWriter(writer);
        for (Object o : c) {
            out.write(o.toString());
            out.newLine();
        }
        out.close();
    }

    public static void serializeObject(Object obj, String dir) throws IOException {
        if (obj==null || dir==null) {
            throw new NullPointerException();
        }

       FileOutputStream fos = new FileOutputStream(dir);
       ObjectOutputStream out;
       if (dir.endsWith(".gz")) {
           GZIPOutputStream gzos = new GZIPOutputStream(fos);
           out = new ObjectOutputStream(gzos);
       } else if (dir.endsWith(".zip")) {
           ZipOutputStream zos = new ZipOutputStream(fos);
           out = new ObjectOutputStream(zos);
       } else {
           out = new ObjectOutputStream(fos);
       }

       out.writeObject(obj);
       out.close();
    }

    public static void serializeObject(Object obj, String dir, String format) throws IOException {
        if (obj==null || dir==null) {
            throw new NullPointerException();
        }

       FileOutputStream fos = new FileOutputStream(dir);
       ObjectOutputStream out;
       if (format.equalsIgnoreCase("gz")) {
           GZIPOutputStream gzos = new GZIPOutputStream(fos);
           out = new ObjectOutputStream(gzos);
       } else if (format.equalsIgnoreCase("zip")) {
           ZipOutputStream zos = new ZipOutputStream(fos);
           out = new ObjectOutputStream(zos);
       } else {
           out = new ObjectOutputStream(fos);
       }

       out.writeObject(obj);
       out.close();
    }

    public static Object deserializeObject(String dir) throws IOException, ClassNotFoundException {
        if (dir==null) {
            throw new NullPointerException();
        }

       FileInputStream fis = new FileInputStream(dir);
       ObjectInputStream in;
       if (dir.toLowerCase().endsWith(".gz")) {
           GZIPInputStream gzis = new GZIPInputStream(fis);
           in = new ObjectInputStream(gzis);
       } else if (dir.toLowerCase().endsWith(".zip")) {
           ZipInputStream zis = new ZipInputStream(fis);
           in = new ObjectInputStream(zis);
       } else {
           in = new ObjectInputStream(fis);
       }
       
       Object obj = in.readObject();
       in.close();

       return obj;
    }

    public static Object deserializeObject(String dir, String format) throws IOException, ClassNotFoundException {
        if (dir==null) {
            throw new NullPointerException();
        }

       FileInputStream fis = new FileInputStream(dir);
       ObjectInputStream in;
       if (format.equalsIgnoreCase("gz")) {
           GZIPInputStream gzis = new GZIPInputStream(fis);
           in = new ObjectInputStream(gzis);
       } else if (format.equalsIgnoreCase("zip")) {
           ZipInputStream zis = new ZipInputStream(fis);
           in = new ObjectInputStream(zis);
       } else {
           in = new ObjectInputStream(fis);
       }

       Object obj = in.readObject();
       in.close();

       return obj;
    }

    public static List<String> listFilesInFolder(String folderDir) {
        File folder = new File(folderDir);
        if (!folder.isDirectory()) {
            return null;
        }

        List<String> files = new ArrayList();
        for (File file : folder.listFiles()) {
          if (file.isFile()) {
              files.add(file.getName());
          }
        }

        return files;
    }
    
}
