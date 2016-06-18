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

import java.io.FileOutputStream;
import java.io.IOException;
import musite.io.Reader;
import musite.io.MusiteIOUtil;

import junit.framework.TestCase;
/**
 *
 * @author LucasYao
 */
public class TestTaxonomyXML extends TestCase{

        public void testReadTaxonomyXML() throws IOException {
        String xml = "testData/taxonomy-ancestor_51967.rdf";
         
        //TaxonomyXMLReader reader = new TaxonomyXMLReader();
        UniprotTaxonomyXMLReader reader = new UniprotTaxonomyXMLReader();
        TaxonomyTree taxonomyTree = MusiteIOUtil.read(reader, xml);

        FileOutputStream f1 = new FileOutputStream("bateria.txt");
        taxonomyTree.printAllLeaves(taxonomyTree.getRoot(), f1);

        f1.close();

        //ProteinsXMLWriter writer = ProteinsXMLWriter.createWriter();
        //MusiteIOUtil.write(writer, out, proteins);

        //new File(out).delete();
    }

}
