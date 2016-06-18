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
public class TaxonomyXMLReader implements Reader<TaxonomyTree>{


        public TaxonomyTree read(InputStream is) throws IOException {
        if (is==null) {
            throw new IllegalArgumentException();
        }

        TaxonomyTree tree = new TaxonomyTree();
        
        SAXReader saxReader = new SAXReader();
        //BufferedInputStream bis = new BufferedInputStream(is);
        //Document document = saxReader.read(new File("c:/catalog/catalog.xml"));


        try
        {
        Document document = saxReader.read(new File("D:/Yao/NetBeansProjects/musite/testData/taxonomy-ancestor_51967.rdf"));
        List list = document.selectNodes("//rdf:RDF" );
        Iterator iter = list.iterator();
            while(iter.hasNext()){
               String TaxonomyID = null;
               String Type = null;
               String Rank = null;
               String ScientificName = null;
               ArrayList<String> OtherNames = new ArrayList<String>();
               boolean partOfLineage = false;

                Element element = (Element)iter.next();
                Attribute attribute = element.attribute("rdf:about");
                TaxonomyID = attribute.getValue().replaceAll(UniprotTaxonomySettings.ID_ADDRESS, "");

                Iterator typeIterator = element.elementIterator("rdf:type");
                while(typeIterator.hasNext()){
                    Element typeElement=(Element)typeIterator.next();
                    Attribute typeAttribute = typeElement.attribute("rdf:resource");
                    Type = typeAttribute.getValue().replaceAll(UniprotTaxonomySettings.TYPE_ADDRESS, "");
                }
                Iterator rankIterator = element.elementIterator("rank");
                while(rankIterator.hasNext()){
                    Element rankElement=(Element)rankIterator.next();
                    Attribute rankAttribute = rankElement.attribute("rdf:resource");
                    Rank = rankAttribute.getValue().replaceAll(UniprotTaxonomySettings.RANK_ADDRESS, "");
                }
                Iterator scientificnameIterator = element.elementIterator("scientificName");
                while(scientificnameIterator.hasNext()){
                    Element scientificnameElement=(Element)scientificnameIterator.next();
                    ScientificName = scientificnameElement.getText();
                }
                Iterator othernameIterator = element.elementIterator("otherName");
                while(othernameIterator.hasNext()){
                    Element othernameElement=(Element)othernameIterator.next();
                    String tempname = othernameElement.getText();
                    OtherNames.add(tempname);
                }
                Iterator lineageIterator = element.elementIterator("partOfLineage");
                while(lineageIterator.hasNext()){
                    Element lineageElement=(Element)lineageIterator.next();
                    String temptext = lineageElement.getText();
                    if(temptext.equals("true"))
                    {
                        partOfLineage = true;
                    }
                    else
                        partOfLineage = false;
                }
                
                //create a new node
                TaxonomyNode node = tree.getTaxonomyNode(TaxonomyID);
                if(node==null)
                {
                 node = new TaxonomyNode();
                 node.setIdentifier(TaxonomyID);
                 tree.addtoNodelist(node);
                }

                node.setOtherNames(OtherNames);
                node.setRank(Rank);
                node.setScientificName(ScientificName);
                node.setType(Type);
                node.setPartOfLineage(partOfLineage);


                //add the parent node
                Iterator subclassIterator = element.elementIterator("rdfs:subClassOf");
                while(subclassIterator.hasNext()){
                    Element subclassElement=(Element)subclassIterator.next();
                    Attribute subclassAttribute = subclassElement.attribute("rdf:resource");
                    String subclassID = subclassAttribute.getValue().replaceAll(UniprotTaxonomySettings.ID_ADDRESS, "");
                    TaxonomyNode parent = tree.getTaxonomyNode(subclassID);
                    if(parent!=null)
                    {
                        node.addParent(parent);
                    }
                    else
                    {
                        parent = new TaxonomyNode();
                        parent.setIdentifier(subclassID);
                        tree.addtoNodelist(parent);
                        node.addParent(parent);
                    }

                }
            }

        }
        catch(Exception e){}


        return tree;
    }

}
