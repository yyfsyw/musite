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

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import musite.Proteins;

import musite.io.MusiteIOUtil;

import musite.prediction.PredictionResult;

import musite.PTM;

/**
 *
 * @author Jianjiong Gao
 */
public class TestXML extends TestCase {

    public void testReadUniprotXML() throws IOException {
        String xml = "testData/uniprot-test.xml";
        String out = xml + ".tmp";

        UniProtXMLReader reader = new UniProtXMLReader();
        reader.setPTMFilter(PTM.PHOSPHORYLATION);
        Proteins proteins = MusiteIOUtil.read(reader, xml);

        System.out.println(proteins.proteinCount());

        ProteinsXMLWriter writer = ProteinsXMLWriter.createWriter();
        MusiteIOUtil.write(writer, out, proteins);

        new File(out).delete();
    }

    public void testReadMusiteXML() throws IOException {
        String xml = "testData/musite-test.xml";
        String out = xml + ".gz";

        ProteinsXMLReader reader = ProteinsXMLReader.createReader();
        Proteins proteins = MusiteIOUtil.read(reader, xml);

        System.out.println(proteins.proteinCount());

        ProteinsXMLWriter writer = ProteinsXMLWriter.createWriter();
        MusiteIOUtil.write(writer, out, proteins);

        new File(out).delete();
    }

    public void testReadPredictionXML() throws IOException {
        String in = "testData/musite-test.pred.xml";
        String out = in + ".gz";

        PredictionResultXMLReader reader = PredictionResultXMLReader.createReader();
        PredictionResult result = MusiteIOUtil.read(reader, in);

        PredictionResultXMLWriter writer = PredictionResultXMLWriter.createWriter();
        MusiteIOUtil.write(writer, out, result);

        new File(out).delete();
    }
}
