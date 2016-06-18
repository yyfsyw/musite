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

package musite.io.xml;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Element;
import org.dom4j.ElementPath;
import org.dom4j.ElementHandler;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import musite.ProteinImpl;
import musite.Proteins;
import musite.ProteinsImpl;

import musite.io.Reader;

import musite.PTM;
import musite.PTMAnnotationUtil;

/**
 *
 * @author Jianjiong Gao
 */
public class UniProtXMLReader implements Reader<Proteins> {
    private Proteins data;
    private Set<PTM> ptmFilter;
    private Set<String> organismFilter;
//    private AnnotationFilter annotationFilter;
    private boolean includeBySimilarity = false,
            includeProbable = false, includePotential = false;

    private boolean keepAllIds = false;
    private Map<String,String> mapIdMainId = null;

    private static Set<String> UNIPROT_TYPES = musite.util.CollectionUtil.getSet(
            "modified residue", "lipid moiety-binding region");

    public UniProtXMLReader() {
        this(null);
    }

    public UniProtXMLReader(Proteins data) {
        this.data = data;
    }

    public void setPTMFilter(PTM ptm) {
        ptmFilter = new HashSet(1);
        ptmFilter.add(ptm);
    }

    public void setPTMFilter(Set<PTM> types) {
        this.ptmFilter = types;
    }

    public void setOrganismFilter(Set<String> organisms) {
        this.organismFilter = organisms;
    }

//    public void setAnnotationFilter(AnnotationFilter annotationFilter) {
//        this.annotationFilter = annotationFilter;
//    }

    public void setIncludeBySimilarity(boolean includeBySimilarity) {
        this.includeBySimilarity = includeBySimilarity;
    }

    public void setIncludeProbable(boolean includeProbable) {
        this.includeProbable = includeProbable;
    }

    public void setIncludePotential(boolean includePotential) {
        this.includePotential = includePotential;
    }

    public void setKeepAllIds(boolean keepAllIds) {
        this.keepAllIds = keepAllIds;
        if (keepAllIds)
            mapIdMainId = new HashMap();
    }

    public Map getIDMap() {
        return mapIdMainId;
    }

    public Proteins read(InputStream is) throws IOException {
        if (is==null) {
            throw new IllegalArgumentException();
        }

        final Proteins result = data==null?new ProteinsImpl():data;

        SAXReader saxReader = new SAXReader();

        final StringBuilder acc = new StringBuilder(30);
        final StringBuilder name = new StringBuilder(30);
        final StringBuilder fullName = new StringBuilder(200);
        final StringBuilder org = new StringBuilder(30);
        final StringBuilder seq = new StringBuilder(2000);
        final List<List> sites = new ArrayList(4); // location, ptm, enzyme, annotation
        final Set<String> accs = new HashSet();

        // entry
        saxReader.addHandler( "/uniprot/entry", new ElementHandler() {
            public void onStart(ElementPath path) {
                acc.setLength(0);
                fullName.setLength(0);
                seq.setLength(0);
                org.setLength(0);
                name.setLength(0);
                sites.clear();
                accs.clear();
            }
            public void onEnd(ElementPath path) {
                // process a element
                if (org.length()>0 && (organismFilter==null ||
                        organismFilter.contains(org.toString()))
                        && acc.length()>0 && seq.length()>0) {
                    String accession = acc.toString();
                    String sequence = seq.toString();

                    ProteinImpl protein = new ProteinImpl(acc.toString(),
                            sequence, name.length()==0?null:name.toString(),
                            fullName.length()==0?null:fullName.toString(),
                            org.length()==0?null:org.toString());
                    result.addProtein(protein);

                    for (List l : sites) {
                        Integer site = (Integer)l.get(0);
                        PTM ptm = (PTM)l.get(1);
                        String enzyme = (String)l.get(2);
                        if (enzyme!=null && enzyme.equalsIgnoreCase("autocatalysis")) {
                            enzyme = name.toString();
                        }

                        Map ann = (Map)l.get(3);
                        try {
                            PTMAnnotationUtil.annotate(protein, site, ptm, enzyme, ann);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (keepAllIds) {
                        for (String ac : accs) {
                            mapIdMainId.put(ac, accession);
                        }
                        if (!accs.isEmpty())
                        protein.putInfo("other-accessions", new HashSet(accs));
                    }

                    //System.out.println(accession);
                }

                // prune the tree
                Element row = path.getCurrent();
                row.detach();
            }
        });

        // accession
        saxReader.addHandler("/uniprot/entry/accession",
                new ElementHandler() {
            public void onStart(ElementPath path) {
                // do nothing
            }

            public void onEnd(ElementPath path) {
                if (acc.length()==0) {
                    Element el = path.getCurrent();
                    acc.append(el.getText());
//                    if (keepAllIds) {
//                        accs.add(acc.toString());
//                    }
                } else {
                    if (keepAllIds) {
                        accs.add(path.getCurrent().getText());
                    }
                }
                
            }
        });

        // name
        saxReader.addHandler("/uniprot/entry/name",
                new ElementHandler() {
            public void onStart(ElementPath path) {
                // do nothing
            }

            public void onEnd(ElementPath path) {
                if (name.length()>0)
                    return;

                Element el = path.getCurrent();
                name.append(el.getText());
            }
        });

        // full name
        saxReader.addHandler("/uniprot/entry/protein/recommendedName/fullName",
                new ElementHandler() {
            public void onStart(ElementPath path) {
                // do nothing
            }

            public void onEnd(ElementPath path) {
                if (fullName.length()>0)
                    return;

                Element el = path.getCurrent();
                fullName.append(el.getTextTrim());
            }
        });

        saxReader.addHandler("/uniprot/entry/organism/name",
                new ElementHandler() {
            public void onStart(ElementPath path) {
                // do nothing
            }

            public void onEnd(ElementPath path) {
                if (org.length()>0)
                    return;

                Element el = path.getCurrent();
                String attr = el.attributeValue("type");
                if (attr==null || !attr.equalsIgnoreCase("scientific")) {
                    return;
                }

                org.append(el.getText());
            }
        });

        saxReader.addHandler("/uniprot/entry/sequence",
                new ElementHandler() {
            public void onStart(ElementPath path) {
                // do nothing
            }

            public void onEnd(ElementPath path) {
                if (seq.length()>0)
                    return;

                Element el = path.getCurrent();
                seq.append(el.getText().replaceAll("\\p{Space}", ""));
            }
        });

        saxReader.addHandler("/uniprot/entry/feature",
                new ElementHandler() {
            public void onStart(ElementPath path) {
                // do nothing
            }

            public void onEnd(ElementPath path) {
                Element el = path.getCurrent();
                String type = el.attributeValue("type");
                if (type==null)
                    return;

                PTM ptm = null;
                String enzyme = null;
                String description = null;
                String keyword = null;

                if (UNIPROT_TYPES.contains(type.toLowerCase())) {
                    description = el.attributeValue("description");
                    if (description==null)
                        return;

                    String[] descs = description.split("; ");
                    for (String desc : descs) {
                        PTM tmp = PTM.ofKeyword(desc);
                        if (tmp != null) {
                            ptm = tmp;
                            keyword = desc;
                        } else if (desc.startsWith("by ")) {
                            enzyme = desc.substring(3);
                        }
                    }
                }
//                else if (type.equalsIgnoreCase("glycosylation site")) {
//                    description = el.attributeValue("description");
//                    ptm = PTM.GLYCOSYLATION;
//                }
//                else if (type.equalsIgnoreCase()) {
//                    description = el.attributeValue("description");
//                    String[] descs = description.split("; ");
//                    for (String desc : descs) {
//                        PTM tmp = PTM.ofKeyword(desc);
//                        if (tmp != null) {
//                            ptm = tmp;
//                            keyword = desc;
//                        } else if (desc.startsWith("by ")) {
//                            enzyme = desc.substring(3);
//                        }
//                    }
//                }

                if (ptm==null || (ptmFilter!=null && !ptmFilter.contains(ptm)))
                    return;

                String status = el.attributeValue("status");
                if (status!=null) {
                    if (!includeBySimilarity && status.equalsIgnoreCase("By similarity"))
                        return;
                    if (!includeProbable && status.equalsIgnoreCase("Probable"))
                        return;
                    if (!includePotential && status.equalsIgnoreCase("Potential"))
                        return;
                }

                int site = -1;

                List<Element> locs = el.elements("location");
                for (Element loc : locs) {
                    List<Element> poss = loc.elements("position");
                    for (Element pos : poss) {
                        String str = pos.attributeValue("position");
                        if (str==null) continue;

                        try {
                            site = Integer.parseInt(str)-1; //start from 0
                        } catch (NumberFormatException e) {
                            continue;
                        }
                    }
                }

                if (site!=-1) {
                    List l = new ArrayList();
                    l.add(site);
                    l.add(ptm);
                    l.add(enzyme);
                    Map<String,Object> m = new HashMap();
                    if (keyword!=null)
                        m.put("keyword", keyword);
                    if (description!=null)
                        m.put("description", description);
                    if (status!=null)
                        m.put("status", status);
                    l.add(m);
                    sites.add(l);
                }
            }
        });

        BufferedInputStream bis = new BufferedInputStream(is);

        try {
            saxReader.read(bis);
        } catch (DocumentException e) {
            throw new IOException(e.getMessage());
        }

        return result;
    };
}
