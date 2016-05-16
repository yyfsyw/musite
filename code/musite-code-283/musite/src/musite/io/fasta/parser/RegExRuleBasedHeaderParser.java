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
import java.util.Set;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 *
 * @author Jianjiong Gao
 */
public class RegExRuleBasedHeaderParser
                                extends AbstractStringParser {
    protected final RegExRule rule;

    public RegExRuleBasedHeaderParser(final RegExRule rule) {
        if (rule==null) {
            throw new NullPointerException();
        }
        
        this.rule = rule;
    }
    /**
     * 
     * @param header
     */
    public void parse(final String header) {
        if (header==null) {
            throw new NullPointerException("Null header.");
        }

        map = new HashMap();

        Set<String> fieldNames = rule.allFields();

        for (String fieldName : fieldNames) {
            String regex = rule.getFieldRegEx(fieldName);
            if (regex==null) continue;

            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(header);
            if (m.find()) {
                map.put(fieldName,m.group(1));
            } else {

            }
        }
    }

}
