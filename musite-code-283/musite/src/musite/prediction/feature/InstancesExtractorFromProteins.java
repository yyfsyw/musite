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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import musite.Protein;
import musite.Proteins;
import musite.Proteins.ProteinFilter;

import musite.PTM;
import musite.PTMAnnotationUtil;
import musite.ResidueAnnotationUtil.AnnotationFilter;

import musite.util.StringUtil;
import musite.util.AminoAcid;

/**
 *
 * @author Jianjiong Gao
 */
public class InstancesExtractorFromProteins implements InstancesExtractor {
    protected final Iterator<Protein> proteinIter;
    protected final Set<Character> aas;

    protected PTM ptm;
    protected ExtractOption option;
    protected List<FeatureExtractor> extractors;
    protected ProteinFilter proteinFilter;
    protected InstanceFilter instanceFilter;

    protected SitesExtractor sitesExtractor;
    protected AnnotationFilter annotationFilter;

    public enum ExtractOption {
        MODIFIED_SITES, UNMODIFIED_SITES, ALL_SITES;
    }

    public InstancesExtractorFromProteins(final Proteins proteins,
            final Set<AminoAcid> aminoAcids) {
        if (proteins==null || aminoAcids==null)
            throw new IllegalArgumentException();
        
        this.proteinIter = proteins.proteinIterator();
        this.aas = AminoAcid.oneLetters(aminoAcids);

        ptm = null;
        option = ExtractOption.ALL_SITES;
        extractors = null;
        proteinFilter = null;
        instanceFilter = null;
        annotationFilter = null;

        sitesExtractor = new SitesExtractor() {
            public Set<Integer> extract(Protein protein) {
                String proteinSeq = protein.getSequence().toUpperCase();

                Set<Integer> sites = new TreeSet<Integer>();
                if (option == ExtractOption.ALL_SITES) {
                    sites.addAll(StringUtil.findAll(proteinSeq, aas, true));
                } else {
                    Set<Integer> pSites = PTMAnnotationUtil.getSites(protein, ptm, annotationFilter);
                    if (option == ExtractOption.MODIFIED_SITES) {
                        if (pSites==null) {
                            return null;
                        }
                        for (int site : pSites) {
                            if (aas.contains(proteinSeq.charAt(site))) {
                                sites.add(site);
                            }
                        }
                    } else {
                        sites.addAll(StringUtil.findAll(proteinSeq, aas, true));
                        if (option==ExtractOption.UNMODIFIED_SITES && pSites!=null) {
                            sites.removeAll(pSites);
                        }
                    }
                }

                return sites;
            }
        };
    }

    public void setExtractOption(ExtractOption option, PTM ptm) {
        setExtractOption(option, ptm, null);
    }

    public void setExtractOption(ExtractOption option, PTM ptm, AnnotationFilter filter) {
        if (option==null)
            throw new IllegalArgumentException("option cannot be null.");
        if (option!=ExtractOption.ALL_SITES && ptm==null)
            throw new IllegalArgumentException("PTM cannot be null for extracting modified or unmodified sites.");

        this.ptm = ptm;
        this.option = option;
        this.annotationFilter = filter;
    }

    public void setSitesExtractor(SitesExtractor sitesExtractor) {
        if (sitesExtractor==null)
            throw new IllegalArgumentException("SitesExtractor cannot be null.");
        this.sitesExtractor = sitesExtractor;
    }

    public void setFeatureExtractors(List<FeatureExtractor> extractors) {
        this.extractors = extractors;
    }

    public void setProteinFilter(ProteinFilter filter) {
        this.proteinFilter = filter;
    }

    public void setInstanceFilter(InstanceFilter filter) {
        this.instanceFilter = filter;
    }

    public boolean hasMore() {
        return proteinIter.hasNext();
    }

    /**
     *
     * @param n the number of entries to fetch, if n=-1, fetch all
     * @return
     */
    public List<Instance> fetch(int n) {
        List<Instance> instances = new ArrayList<Instance>();
        if (n<0) {
            while (hasMore()) {
                Protein protein = proteinIter.next();
                if (proteinFilter==null || proteinFilter.filter(protein))
                    instances.addAll(fetch(protein));
            }
        } else {
            for (int i=0; i<n && hasMore(); i++) {
                Protein protein = proteinIter.next();
                if (proteinFilter==null || proteinFilter.filter(protein))
                    instances.addAll(fetch(protein));
            }
        }
        return instances;
    }

    protected List<Instance> fetch(Protein protein) {
        List<Instance> instances = new ArrayList<Instance>();

        Set<Integer> sites = sitesExtractor.extract(protein);

        if (sites==null)
            return instances;

        for (int site : sites) {
            ProteinResidueInstannceTag tag = new ProteinResidueInstannceTagImpl(
                    protein, site);
            Instance instance = new InstanceImpl(tag);

            if (instanceFilter!=null && !instanceFilter.filter(instance))
                continue;
            
            instances.add(instance);

            if (extractors!=null) {
                for (FeatureExtractor extractor : extractors) {
                    extractor.extract(instance, true);
                }
            }
        }

        return instances;
    }

    public interface SitesExtractor {
        public Set<Integer> extract(Protein protein);
    }

}
