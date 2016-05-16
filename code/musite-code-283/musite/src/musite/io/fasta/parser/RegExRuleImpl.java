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
public class RegExRuleImpl implements RegExRule {
    protected final Map<String,String> mapFieldRegExp;
    
    public RegExRuleImpl() {
        mapFieldRegExp = new HashMap<String,String>();
    }

    /**
     *
     * @param fieldName
     * @return
     */
    public String getFieldRegEx(final String fieldName) {
        if (fieldName==null) {
            throw new NullPointerException();
        }

        return mapFieldRegExp.get(fieldName);
    }

    /**
     *
     * @return
     */
    public Set<String> allFields() {
        return mapFieldRegExp.keySet();
    }

    public String removeField(String fieldName) {
        return mapFieldRegExp.remove(fieldName);
    }

    /**
     *
     * @param fieldName
     * @param regEx
     */
    public void setFieldRegEx(String fieldName, String regEx) {        
        mapFieldRegExp.put(fieldName, regEx);
    }
    
}
