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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.io.FileOutputStream;
/**
 *
 * @author LucasYao
 */
public final class TaxonomyTree {

    private TaxonomyNode taxonomyroot;
    private HashMap<String, TaxonomyNode> nodelist;
    public TaxonomyTree()
    {
        taxonomyroot = null;
        nodelist = new HashMap<String, TaxonomyNode>();
    }
    public void searchRoot()
    {
        if(nodelist!=null && !nodelist.isEmpty())
        {
           for (Iterator iter = nodelist.entrySet().iterator(); iter.hasNext();) {
                Map.Entry entry = (Map.Entry) iter.next();
                //Object key = entry.getKey();
                TaxonomyNode node = (TaxonomyNode)entry.getValue();
                if(node.getParents().isEmpty())
                {
                    taxonomyroot = node;
                    break;
                }

            }
        }
        else taxonomyroot = null;
    }
    public void setRoot(TaxonomyNode rt)
    {
        this.taxonomyroot = rt;
    }
    public TaxonomyNode getRoot()
    {
        return this.taxonomyroot;
    }
    public void addtoNodelist(TaxonomyNode node)
    {
        this.nodelist.put(node.getIdentifier(), node);
    }
    public TaxonomyNode getTaxonomyNode(String id)
    {
        return this.nodelist.get(id);
    }

    public void printAllLeaves(TaxonomyNode start, FileOutputStream fout)
    {
       ArrayList<TaxonomyNode> children = start.getChildren();
       if(children.isEmpty())
       {
           String temp = start.getScientificName()+"\r\n";
           byte buf[] = temp.getBytes();
           try
           {
           fout.write(buf);
           }
           catch(Exception e){};
       }
       else
       {
           for(int i=0; i<children.size();i++)
           {
               printAllLeaves(children.get(i),fout);
           }
       }
    }

    public String printTree()
    {
        String s;
        s = "";
        String s1 = s.concat("new");
        s = "Hleep";
        s1 = s;
        s = "eee";

        return s1;
    }

}
