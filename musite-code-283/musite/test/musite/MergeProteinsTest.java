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

package musite;

import java.util.Collection;
import java.util.Set;

import junit.framework.TestCase;

import musite.util.CollectionUtil;

import static org.junit.Assert.*;

/**
 *
 * @author gjj
 */
public class MergeProteinsTest extends TestCase {
    public void testIntersection() {
        Proteins proteins1 = new ProteinsImpl();
        Proteins proteins2 = new ProteinsImpl();

        Protein pro = new ProteinImpl("pro1","SSSSSSSSSSS",null,null,null);
        PTMAnnotationUtil.annotate(pro, CollectionUtil.getSet(1,2), PTM.PHOSPHORYLATION);
        proteins1.addProtein(pro);

        pro = new ProteinImpl("pro1","SSSSSSSSSSS",null,null,null);
        PTMAnnotationUtil.annotate(pro, CollectionUtil.getSet(1,2,3), PTM.PHOSPHORYLATION);
        proteins2.addProtein(pro);

        pro = new ProteinImpl("pro2","SSSSSSSSSSS",null,null,null);
        PTMAnnotationUtil.annotate(pro, CollectionUtil.getSet(1,2,3,4), PTM.PHOSPHORYLATION);
        proteins2.addProtein(pro);

        Proteins merged = ProteinsUtil.mergeProteins(
                CollectionUtil.getList(proteins1, proteins2),
                ProteinsUtil.MergeOperation.INTERSECTION,
                ProteinsUtil.MergeOperation.INTERSECTION);

        assertTrue(merged.proteinCount()==1);
        assertTrue(merged.isProteinContained("pro1"));
        
        Set<Integer> sites = PTMAnnotationUtil.getSites(merged.getProtein("pro1"), PTM.PHOSPHORYLATION);
        assertTrue(sites.equals(CollectionUtil.getSet(1,2)));
    }

    public void testUnion() {
        Proteins proteins1 = new ProteinsImpl();
        Proteins proteins2 = new ProteinsImpl();

        Protein pro = new ProteinImpl("pro1","SSSSSSSSSSS",null,null,null);
        PTMAnnotationUtil.annotate(pro, CollectionUtil.getSet(1,2,4), PTM.PHOSPHORYLATION);
        proteins1.addProtein(pro);

        pro = new ProteinImpl("pro1","SSSSSSSSSSS",null,null,null);
        PTMAnnotationUtil.annotate(pro, CollectionUtil.getSet(1,2,3), PTM.PHOSPHORYLATION);
        proteins2.addProtein(pro);

        Proteins merged = ProteinsUtil.mergeProteins(CollectionUtil.getList(proteins1, proteins2));

        assertTrue(merged.proteinCount()==1);
        assertTrue(merged.isProteinContained("pro1"));

        Set<Integer> sites = PTMAnnotationUtil.getSites(merged.getProtein("pro1"), PTM.PHOSPHORYLATION);
        assertTrue(sites.equals(CollectionUtil.getSet(1,2,3,4)));

        Collection c = PTMAnnotationUtil.extractPTMAnnotation(merged.getProtein("pro1"), 1, PTM.PHOSPHORYLATION);
        assertTrue(c.size()==1);
    }

    public void testProteinDifference() {
        Proteins proteins1 = new ProteinsImpl();
        Proteins proteins2 = new ProteinsImpl();

        Protein pro = new ProteinImpl("pro1","SSSSSSSSSSS",null,null,null);
        PTMAnnotationUtil.annotate(pro, CollectionUtil.getSet(1,2), PTM.PHOSPHORYLATION);
        proteins1.addProtein(pro);

        pro = new ProteinImpl("pro2","SSSSSSSSSSS",null,null,null);
        PTMAnnotationUtil.annotate(pro, CollectionUtil.getSet(1,2,4), PTM.PHOSPHORYLATION);
        proteins1.addProtein(pro);

        pro = new ProteinImpl("pro2","SSSSSSSSSSS",null,null,null);
        PTMAnnotationUtil.annotate(pro, CollectionUtil.getSet(1,2,3), PTM.PHOSPHORYLATION);
        proteins2.addProtein(pro);

        Proteins merged = ProteinsUtil.mergeProteins(
                CollectionUtil.getList(proteins1, proteins2),
                ProteinsUtil.MergeOperation.DIFFERENCE,
                ProteinsUtil.MergeOperation.UNION);

        assertTrue(merged.proteinCount()==1);
        assertTrue(merged.isProteinContained("pro1"));

        Set<Integer> sites = PTMAnnotationUtil.getSites(merged.getProtein("pro1"), PTM.PHOSPHORYLATION);
        assertTrue(sites.equals(CollectionUtil.getSet(1,2)));

        Collection c = PTMAnnotationUtil.extractPTMAnnotation(merged.getProtein("pro1"), 1, PTM.PHOSPHORYLATION);
        assertTrue(c.size()==1);
    }

    public void testSiteDifference() {
        Proteins proteins1 = new ProteinsImpl();
        Proteins proteins2 = new ProteinsImpl();

        Protein pro = new ProteinImpl("pro1","SSSSSSSSSSS",null,null,null);
        PTMAnnotationUtil.annotate(pro, CollectionUtil.getSet(1,2,4), PTM.PHOSPHORYLATION);
        proteins1.addProtein(pro);

        pro = new ProteinImpl("pro1","SSSSSSSSSSS",null,null,null);
        PTMAnnotationUtil.annotate(pro, CollectionUtil.getSet(1,2,3), PTM.PHOSPHORYLATION);
        proteins2.addProtein(pro);

        pro = new ProteinImpl("pro2","SSSSSSSSSSS",null,null,null);
        PTMAnnotationUtil.annotate(pro, CollectionUtil.getSet(1,2,3), PTM.PHOSPHORYLATION);
        proteins2.addProtein(pro);

        Proteins merged = ProteinsUtil.mergeProteins(
                CollectionUtil.getList(proteins1, proteins2),
                ProteinsUtil.MergeOperation.UNION,
                ProteinsUtil.MergeOperation.DIFFERENCE);

        Set<Integer> sites = PTMAnnotationUtil.getSites(merged.getProtein("pro1"), PTM.PHOSPHORYLATION);
        assertTrue(sites.equals(CollectionUtil.getSet(4)));

        sites = PTMAnnotationUtil.getSites(merged.getProtein("pro2"), PTM.PHOSPHORYLATION);
        assertTrue(sites==null || sites.isEmpty());
    }

    public void testSiteInteraction() {
        Proteins proteins1 = new ProteinsImpl();
        Proteins proteins2 = new ProteinsImpl();

        Protein pro = new ProteinImpl("pro1","SSSSSSSSSSS",null,null,null);
        PTMAnnotationUtil.annotate(pro, CollectionUtil.getSet(1,2,4), PTM.PHOSPHORYLATION);
        proteins1.addProtein(pro);

        pro = new ProteinImpl("pro2","SSSSSSSSSSS",null,null,null);
        PTMAnnotationUtil.annotate(pro, CollectionUtil.getSet(1,2,3), PTM.PHOSPHORYLATION);
        proteins1.addProtein(pro);

        pro = new ProteinImpl("pro1","SSSSSSSSSSS",null,null,null);
        PTMAnnotationUtil.annotate(pro, CollectionUtil.getSet(1,2,3), PTM.PHOSPHORYLATION);
        proteins2.addProtein(pro);

        pro = new ProteinImpl("pro3","SSSSSSSSSSS",null,null,null);
        PTMAnnotationUtil.annotate(pro, CollectionUtil.getSet(1,2,3), PTM.PHOSPHORYLATION);
        proteins2.addProtein(pro);

        Proteins merged = ProteinsUtil.mergeProteins(
                CollectionUtil.getList(proteins1, proteins2),
                ProteinsUtil.MergeOperation.UNION,
                ProteinsUtil.MergeOperation.INTERSECTION);

        Set<Integer> sites = PTMAnnotationUtil.getSites(merged.getProtein("pro1"), PTM.PHOSPHORYLATION);
        assertTrue(sites.equals(CollectionUtil.getSet(1,2)));

        sites = PTMAnnotationUtil.getSites(merged.getProtein("pro2"), PTM.PHOSPHORYLATION);
        assertTrue(sites==null || sites.isEmpty());

        sites = PTMAnnotationUtil.getSites(merged.getProtein("pro3"), PTM.PHOSPHORYLATION);
        assertTrue(sites==null || sites.isEmpty());
    }

}