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

package musite.prediction.feature;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jianjiong Gao
 */
public class InstanceTagImpl implements InstanceTag, Serializable {
    private static final long serialVersionUID = -3631666337604136268L;
    
    protected Map<String,Object> tagMap;
    
    public InstanceTagImpl() {
        tagMap = new HashMap();
    }

    public Map<String,Object> getTagMap() {
        return tagMap;
    }

    public Object getTag(String tagName) {
        return tagMap.get(tagName);
    }

    public Object addTag(String tagName, Object obj) {
        return tagMap.put(tagName, obj);
    }

    public Object removeTag(String tagName) {
        return tagMap.remove(tagName);
    }

    @Override
    public boolean equals(Object o) {
        if (o==null) {
            throw new NullPointerException();
        }

        if (! (o instanceof InstanceTagImpl)) {
            throw new java.lang.IllegalArgumentException();
        }

        InstanceTagImpl anotherTag = (InstanceTagImpl)o;
        Map<String,Object> anotherMap = anotherTag.getTagMap();
        
        if (tagMap.size()!=anotherMap.size()) {
            return false;
        }

        for (Map.Entry<String,Object> entry : tagMap.entrySet()) {
            String tagName = entry.getKey();
            Object tagValue = entry.getValue();

            Object anotherTagValue = anotherMap.get(tagName);
            if (anotherTagValue==null) {
                return false;
            }

            if (tagValue!=anotherTagValue) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
