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

import javax.swing.filechooser.FileFilter;

public class FileExtensionsFilter extends FileFilter {
	private ArrayList<String> acceptedExtensions;
	private String description;
	
	public FileExtensionsFilter(ArrayList<String> exts, String desc) {
		acceptedExtensions = exts;
		description = desc;
	}
	
	public boolean accept(File arg0) {
		if (arg0.isDirectory()) return true;
                String name = arg0.getName();
		if (name==null) return false;
                name = name.toLowerCase();
		int n = acceptedExtensions.size();
		for (int i=0; i<n; i++) {
			if (name.endsWith(acceptedExtensions.get(i).toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	public String getDescription() {
		return description;
	}

	
}
