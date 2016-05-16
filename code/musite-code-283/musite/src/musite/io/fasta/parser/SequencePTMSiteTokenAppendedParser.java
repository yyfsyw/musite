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

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import musite.util.StringUtil;

/**
 *
 * @author Jianjiong Gao
 */
public class SequencePTMSiteTokenAppendedParser
        extends DefaultSequenceParser
        implements SequencePTMSiteParser {
    protected final String token;
    protected final String tokenOct;

    public SequencePTMSiteTokenAppendedParser(final String token) {
        if (token==null) {
            throw new IllegalArgumentException("Null token");
        }
        this.token = token;
        this.tokenOct = StringUtil.toOct(token);
    }

    @Override
    public void parse(final String sequence) {
        if (sequence==null) {
            throw new NullPointerException();
        }

        // remove spaces first
        String seq = sequence.replaceAll("[0-9\\p{Space}]", "");

        Set<Integer> sites = new HashSet();

        TreeSet<Integer> ix = StringUtil.findAll(seq, token, false);
        int count = 0;
        for (int i:ix) {
            int site = i-1-count*token.length();
            sites.add(site);
            count++;
        }

        if (!sites.isEmpty()) {
            setSites(sites);
            super.parse(seq.replaceAll(tokenOct, ""));
        } else {
            invalidate();
            super.parse(seq);
        }
    }
    
    /**
     *
     * @return
     */
    public Set<Integer> getSites() {
        return (Set<Integer>)getField(SITES);
    }

    protected void setSites(Set<Integer> sites) {
        addField(SITES, sites);
    }

    protected void invalidate() {
        removeField(SITES);
    }
}
