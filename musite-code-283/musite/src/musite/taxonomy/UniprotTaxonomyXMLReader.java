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

package musite.taxonomy;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.io.File;

import org.dom4j.Element;
import org.dom4j.ElementPath;
import org.dom4j.ElementHandler;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.Document;
import org.dom4j.Attribute;


import musite.io.Reader;

/**
 *
 * @author LucasYao
 */
public class UniprotTaxonomyXMLReader implements Reader<TaxonomyTree>{


    TaxonomyNode currentNode = null;
    public void process()
    {
        currentNode = null;
    }
    public TaxonomyTree read(InputStream is) throws IOException {
        if (is==null) {
            throw new IllegalArgumentException();
        }
        final TaxonomyTree tree = new TaxonomyTree();
        SAXReader saxReader = new SAXReader();
        final TaxonomyNode currentNode = new TaxonomyNode();


        // entry
        saxReader.addHandler( "/RDF/Description", new ElementHandler() {
            public void onStart(ElementPath path) {
                currentNode.clearMembers();
                Element element = path.getCurrent();
                Attribute attribute = element.attribute("about");
                String TaxonomyID = attribute.getValue().replaceAll(UniprotTaxonomySettings.ID_ADDRESS, "");
                currentNode.setIdentifier(TaxonomyID);
 
            }
            public void onEnd(ElementPath path) {
                // process an element

                
                //create a new node
                TaxonomyNode node = tree.getTaxonomyNode(currentNode.getIdentifier());
                if(node==null)
                {
                 node = new TaxonomyNode();
                 currentNode.copyMembersTo(node);
                 tree.addtoNodelist(node);
                }
                else
                {
                    currentNode.copyMembersTo(node);
                }


                //change the parent from currentNode to node
                ArrayList<TaxonomyNode> parentlist = node.getParents();
                for(int i=0; i<parentlist.size(); i++)
                {
                    TaxonomyNode parent = parentlist.get(i);
                    parent.getChildren().add(node);
                }
                

                // prune the tree
                Element row = path.getCurrent();
                row.detach();
            }
        });

        // type
        saxReader.addHandler("/RDF/Description/type",
                new ElementHandler() {
            public void onStart(ElementPath path) {
                // do nothing
            }

            public void onEnd(ElementPath path) {
                    Element typeElement=(Element)path.getCurrent();
                    Attribute typeAttribute = typeElement.attribute("resource");
                    String Type = typeAttribute.getValue().replaceAll(UniprotTaxonomySettings.TYPE_ADDRESS, "");
                    currentNode.setType(Type);

            }
        });

        // rank
        saxReader.addHandler("/RDF/Description/rank",
                new ElementHandler() {
            public void onStart(ElementPath path) {
                // do nothing
            }

            public void onEnd(ElementPath path) {
                    Element rankElement=(Element)path.getCurrent();
                    Attribute rankAttribute = rankElement.attribute("resource");
                    String Rank = rankAttribute.getValue().replaceAll(UniprotTaxonomySettings.RANK_ADDRESS, "");
                    currentNode.setRank(Rank);

            }
        });

        // scientificName
        saxReader.addHandler("/RDF/Description/scientificName",
                new ElementHandler() {
            public void onStart(ElementPath path) {
                // do nothing
            }

            public void onEnd(ElementPath path) {
                    Element scientificnameElement=(Element)path.getCurrent();
                    String ScientificName = scientificnameElement.getText();
                    currentNode.setScientificName(ScientificName);
            }
        });
        // otherName
        saxReader.addHandler("/RDF/Description/otherName",
                new ElementHandler() {
            public void onStart(ElementPath path) {
                // do nothing
            }

            public void onEnd(ElementPath path) {
                    Element othernameElement=(Element)path.getCurrent();
                    String tempname = othernameElement.getText();
                    currentNode.addOthernames(tempname);

            }
        });

        // partOfLineage
        saxReader.addHandler("/RDF/Description/partOfLineage",
                new ElementHandler() {
            public void onStart(ElementPath path) {
                // do nothing
            }

            public void onEnd(ElementPath path) {
                   Element lineageElement=(Element)path.getCurrent();
                    String temptext = lineageElement.getText();
                    boolean partOfLineage;
                    if(temptext.equals("true"))
                    {
                        partOfLineage = true;
                    }
                    else
                        partOfLineage = false;
                    currentNode.setPartOfLineage(partOfLineage);

            }
        });

        // Add parent
        saxReader.addHandler("/RDF/Description/subClassOf",
                new ElementHandler() {
            public void onStart(ElementPath path) {
                // do nothing
            }

            public void onEnd(ElementPath path) {
                    Element subclassElement=(Element)path.getCurrent();
                    Attribute subclassAttribute = subclassElement.attribute("resource");
                    String subclassID = subclassAttribute.getValue().replaceAll(UniprotTaxonomySettings.ID_ADDRESS, "");
                    TaxonomyNode parent = tree.getTaxonomyNode(subclassID);
                    if(parent!=null)
                    {
                        currentNode.addParentOnly(parent);
                    }
                    else
                    {
                        parent = new TaxonomyNode();
                        parent.setIdentifier(subclassID);
                        tree.addtoNodelist(parent);
                        currentNode.addParentOnly(parent);
                    }

            }
        });

        
        BufferedInputStream bis = new BufferedInputStream(is);

        Document doc;
        try {
            doc = saxReader.read(bis);
        } catch (DocumentException e) {
            throw new IOException(e.getMessage());
        }

        tree.searchRoot();
        return tree;
    }
}
