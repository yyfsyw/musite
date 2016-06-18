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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

/**
 *
 * @author Jianjiong Gao
 */
public class MultiTreeMap<K,V> extends TreeMap<K,Collection<V>> 
                                       implements MultiMap<K,V> {

    public V add(K key, V value) {
        Collection<V> values = super.get(key);
        if (values==null) {
            values = new ArrayList<V>();
            super.put(key, values);
        }
        boolean added = values.add(value);
        return added ? value : null;
    }

    public List<V> allValues() {
        List<V> ret = new ArrayList<V>();
        for(Collection<V> c : super.values()) {
            ret.addAll(c);
        }
        return ret;
    }

    public List<K> allKeys() {
        List<K> ret = new ArrayList<K>();
        for(K k : super.keySet()) {
            int nv = super.get(k).size();
            for (int i=0; i<nv; i++) {
                ret.add(k);
            }
        }
        return ret;
    }

    public List<K> firstNKeys(int n) {
        if (n<=0) {
            throw new IllegalArgumentException();
        }
        List<K> ret = new ArrayList<K>(n);
        int i=0;
        for(K k : super.keySet()) {
            int nv = super.get(k).size();
            for (int j=0; j<nv; j++) {
                if (i == n) break;
                ret.add(k);
                i++;
            }
            if (i==n) break;
        }

        if (i!=n) {
            throw new IndexOutOfBoundsException();
        }

        return ret;
    }

    public List<V> firstNValues(int n) {
        if (n<=0) {
            throw new IllegalArgumentException();
        }
        List<V> ret = new ArrayList<V>(n);
        int i=0;
        for(Collection<V> c : super.values()) {
            for (V v : c) {
                if (i == n) break;
                ret.add(v);
                i++;
            }
            if (i==n) break;
        }

        if (i!=n) {
            throw new IndexOutOfBoundsException();
        }
        
        return ret;
    }

}
