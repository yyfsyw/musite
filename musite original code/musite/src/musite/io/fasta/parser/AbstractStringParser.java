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

package musite.io.fasta.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Jianjiong Gao
 */
public abstract class AbstractStringParser implements StringParser {
    protected Map<String,Object> map; //map field name to field value

    public AbstractStringParser() {
        map = new HashMap();
    }

    public abstract void parse(String str);

    public Object getField(String fieldName) {
        if (fieldName==null) {
            throw new NullPointerException("Null fieldName.");
        }

        return map.get(fieldName);
    }

    protected Object addField(String fieldName, Object obj) {
        if (fieldName==null || obj==null) {
            throw new NullPointerException("");
        }
        return map.put(fieldName, obj);
    }

    protected Object removeField(String fieldName) {
        if (fieldName==null) {
            throw new NullPointerException("");
        }
        return map.remove(fieldName);
    }

    protected Set<String> allFields() {
        return map.keySet();
    }
}
