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

package musite.misc.othertools;

import java.util.Collection;
import java.util.Iterator;
import musite.Proteins;
import musite.Protein;
import musite.io.MusiteIOUtil;
import musite.io.xml.ProteinsXMLReader;


import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.util.ArrayList;

import java.io.InputStream;
import musite.util.IOUtil;
import musite.util.ClientHttpRequest;
/**
 *
 * @author LucasYao
 */
public class PAILUtil {


    public static void runResultsByPAIL(String inputfile, String outputfile)
    {
        try
        {
        ProteinsXMLReader reader = ProteinsXMLReader.createReader();
        Proteins proteins = MusiteIOUtil.read(reader, inputfile);
        Collection<Protein> proteincollection = proteins.proteins();
        Iterator<Protein> iterator = proteincollection.iterator();

        FileOutputStream fout = new FileOutputStream(outputfile);
        int procount = 0;
        while(iterator.hasNext()){
            System.out.println(++procount);
            Protein protein = iterator.next();
            //do something to protein;
            ArrayList<Integer> position = new ArrayList<Integer>();
            ArrayList<Double> score = new ArrayList<Double>();
            predictByPAIL(protein, position, score);
            for(int i=0;i<position.size();i++)
            {
                String temp = protein.getAccession()+" "+position.get(i)
                        +" "+score.get(i)+"\r\n";
                fout.write(temp.getBytes());
            }
        }
        fout.close();

        }
        catch(Exception e){}
    }

    public static void predictByPAIL(Protein protein, ArrayList<Integer> position, ArrayList<Double> score)
    {
        try {
            String seq = protein.getSequence();

            InputStream is = ClientHttpRequest.post(
                              new java.net.URL("http://bdmpail.biocuckoo.org/results.php"),
                              new Object[] {
                                            "FastaData", seq,
                                            "CutOffNow", "0.0",
                                            "Submit","Submit"
                                           });

            java.util.List<String> lines = IOUtil.readStringListAscii(
                    new java.io.InputStreamReader(is));
            FileOutputStream fout = new FileOutputStream("temp.html");
            for (String line:lines) {
                fout.write(line.getBytes());
            }
            fout.close();

            FileReader fin = new FileReader("temp.html");
            BufferedReader bufRead = new BufferedReader(fin);
            String line;
            String html="";
            do{

               line = bufRead.readLine();
               html = html + line;
             }while(line!=null);
            bufRead.close();
            fin.close();


            String startstring = "<td width=100><font face=\"Courier New, Courier, mono\" color='#000099' ><div align=\"center\">";
            String endstring = "</div></font></td>";
            String tempinfo;
            int index=0;

            index = html.indexOf(startstring);
            while(index>=0)
            {
                    index = html.indexOf(startstring,index);
                    if(index<0)break;
                    int startindex = index+startstring.length();
                    int endindex = html.indexOf(endstring, index);
                    tempinfo = html.substring(startindex, endindex);
                    int i = Integer.parseInt(tempinfo.trim());
                    position.add(i);
                    index = endindex;

                    index = html.indexOf(startstring,index);
                    if(index<0)break;
                    startindex = index+startstring.length();
                    endindex = html.indexOf(endstring, index);
                    tempinfo = html.substring(startindex, endindex);
                    double d = Double.parseDouble(tempinfo.trim());
                    score.add(d);
                    index = endindex;

                    index = html.indexOf(startstring,index);
                    if(index<0)break;
                    startindex = index+startstring.length();
                    endindex = html.indexOf(endstring, index);
                    index = endindex;

            }

/*            for(int i= 0 ; i < position.size(); i++)
            {
                System.out.println(position.get(i));
                System.out.println(score.get(i));
                System.out.println("");
            }
 
 */

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
