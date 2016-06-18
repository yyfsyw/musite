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

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jianjiong Gao
 */
public class TextUtil {

    public static Map<String,Integer> countFrequency(String text) throws IOException {
        Map<String,Integer> map = new HashMap();
        List<String> lines = IOUtil.readStringListAscii(text);
        for (String line : lines) {
            Integer count = map.get(line);
            if (count==null) {
                map.put(line, 1);
            } else {
                map.put(line, count+1);
            }
        }

        return map;
    }
}
