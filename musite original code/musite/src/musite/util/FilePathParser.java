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

import java.io.File;

import java.util.ArrayList;

public class FilePathParser {

	public static ArrayList<String> parse(String filePath) {
		ArrayList<String> ret = new ArrayList<String>(3);
		ret.add(getDir(filePath));
		ret.add(getName(filePath));
		ret.add(getExt(filePath));
		
		return ret;
	}
	
	public static String getDir(String filePath) {
		int i = filePath.lastIndexOf(File.separator);
		if (i==-1) return null;
		return filePath.substring(0,i);
	}

        public static String getNameWithExt(String filePath) {
                int i = filePath.lastIndexOf(File.separator);
		return filePath.substring(i+1);
        }
	
	public static String getName(String filePath) {
		int i = filePath.lastIndexOf(File.separator);
		int j = filePath.lastIndexOf('.');
		if (j==-1) return filePath.substring(i+1);
		return filePath.substring(i+1,j);		
	}

	public static String getExt(String filePath) {
		int j = filePath.lastIndexOf('.');
		if (j==-1) return null;
		return filePath.substring(j+1);
		
	}
}
