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

package musite.io.text;
import musite.prediction.*;
import musite.PTM;
import musite.util.AminoAcid;
import musite.io.MusiteIOUtil;
import musite.Proteins;
import musite.io.xml.ProteinsXMLReader;
import musite.io.xml.PredictionResultXMLWriter;
import musite.util.IOUtil;
import musite.io.Reader;
import java.util.Collections;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.IOException;
import java.util.Set;

/**
 *
 * @author LucasYao
 */
public class PredictionResultsTxtReader implements Reader<PredictionResult> {
    final private Proteins proteins;
    final private String modelname;
    final private PTM ptm;
    final Set<AminoAcid> aminoAcids;
    private double minscore;
    private double maxscore;

    public PredictionResultsTxtReader(Proteins proteins, String modelname,PTM ptm, Set<AminoAcid> aminoAcids) {
        this.proteins = proteins;
        this.modelname = modelname;
        this.ptm = ptm;
        this.aminoAcids = aminoAcids;
        this.minscore = 0;
        this.maxscore = 0;
    }

    public PredictionResult read(InputStream is) throws IOException
    {
           
           PredictionModelImpl pm = new PredictionModelImpl.Builder().name(modelname).ptm(ptm).aminoAcids(aminoAcids).build();
           PredictionResultImpl pr = new PredictionResultImpl();
           pr.addModel(pm);

//           ProteinsXMLReader reader = ProteinsXMLReader.createReader();
//           Proteins proteins = MusiteIOUtil.read(reader, proteinsxml);

          
           pr.addAll(this.proteins, true, null, Proteins.ConfictHandleOption.RENAME);

           InputStreamReader in = new InputStreamReader(is);
           BufferedReader bufRead = new BufferedReader(in);
           String line;
           line = bufRead.readLine();
           while(line!=null && !line.trim().equals(""))
           {
               //System.out.println(line.trim());
               String result[] = line.trim().split(" ");
               String accession = result[0];
               int site = Integer.parseInt(result[1]);
               double pred = Double.parseDouble(result[2]);
               pr.setPrediction(pm, accession, site-1, pred);
               if(pred>maxscore)maxscore = pred;
               if(pred<minscore)minscore = pred;
               line = bufRead.readLine();
           }
           
           //PredictionResultXMLWriter writer = PredictionResultXMLWriter.createWriter();
           //MusiteIOUtil.write(writer, outputfile, pr);
           System.out.println("minscore="+minscore+"  maxscore="+maxscore);
           return pr;
    }

}
