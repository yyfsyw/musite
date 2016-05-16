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

import java.util.Set;

import musite.Protein;

import musite.PTM;
import musite.PTMAnnotationUtil;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jianjiong Gao
 */
public class InstanceUtil {

    public static String extractSurroundingSequence(Instance instance, int windowOffset) {
        return extractSurroundingSequence(instance, windowOffset, true);
    }
    
    public static String extractSurroundingSequence(Instance instance, int windowOffset,
            boolean appendTerminals) {
        return extractSurroundingSequence(instance, windowOffset, windowOffset,
                appendTerminals, false, null);
    }

    public static String extractSurroundingSequence(Instance instance, int windowOffset,
            boolean appendTerminals, boolean markSites, PTM ptm) {
        return extractSurroundingSequence(instance, windowOffset, windowOffset,
                appendTerminals, markSites, ptm);
    }

    /**
     * append terminals
     * @param instance
     * @param leftOffset
     * @param rightOffset
     * @return
     */
    public static String extractSurroundingSequence(Instance instance, int leftOffset,
            int rightOffset) {
        return extractSurroundingSequence(instance, leftOffset, rightOffset, true, false, null);
    }

    /**
     *
     * @param instance
     * @param windowOffset
     * @param appendTerminals
     * @param markSites mart all sites as X#
     * @return
     */
    public static String extractSurroundingSequence(Instance instance, int leftOffset,
            int rightOffset, boolean appendTerminals, boolean markSites, PTM ptm) {
        if (instance==null)
            throw new IllegalArgumentException();

        if (leftOffset<0 || rightOffset<0)
            throw new IllegalArgumentException();
        
        InstanceTag tag = instance.getInstanceTag();
        if (!(tag instanceof ProteinResidueInstannceTag))
            return null;
        
        ProteinResidueInstannceTag prTag = (ProteinResidueInstannceTag)tag;
        Protein protein = prTag.getProtein();
        int site = prTag.getPosition();

        String proSeq = protein.getSequence();
        if (proSeq==null)
            return null;

        int s = site-leftOffset;
        int t = site+rightOffset+1;

        String ret = new String();

        if (s<0) {
            if (appendTerminals) {
                ret += StringUtils.repeat("*", -s);
                s = 0;
            } else {
                return null;
            }
        }

        if (t>proSeq.length()) {
            if (appendTerminals) {
                if (markSites) {
                    ret += extractMarkedPeptide(protein, ptm, s, proSeq.length());
                } else {
                    ret += proSeq.substring(s);
                }
                ret += StringUtils.repeat("*", t-proSeq.length());
            } else {
                return null;
            }
        } else {
            if (markSites) {
                ret += extractMarkedPeptide(protein, ptm, s, t);
            } else {
                ret += proSeq.substring(s, t);
            }
        }

        return ret;
    }

    private static String extractMarkedPeptide(Protein protein, PTM ptm, int start, int end) {
        Set<Integer> sites = PTMAnnotationUtil.getSites(protein, ptm);
        String seq = protein.getSequence();
        if (sites==null || sites.isEmpty()) {
            return seq.substring(start, end);
        }

        StringBuilder sb = new StringBuilder();
        int s=start;
        for (int site : sites) {
            if (site>=start && site<end) {
                int t=site;
                sb.append(seq.substring(s,t+1));
                sb.append("#");
                s=site+1;
            }
        }

        if (s<end) {
            sb.append(seq.substring(s, end));
        }

        return sb.toString();
    }
}
