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
import java.io.InputStreamReader;
import musite.util.IOUtil;
import musite.util.ClientHttpRequest;
import java.net.URL;
/**
 *
 * @author LucasYao
 */
public class MeMoUtil {
    public static void runResultsByMeMo(String inputfile, String outputfile, String TypeName, String logfile)
    {

    }
    public static boolean predictByMeMo(Protein protein, String TypeName, ArrayList<Integer> position, ArrayList<Double> score, String logfile)
    {
       try {
            String seq = protein.getSequence();

            InputStream is = ClientHttpRequest.post(
                              new java.net.URL("http://www.bioinfo.tsinghua.edu.cn/~tigerchen/cgi-bin/memo.cgi?type=lys"),
                              new Object[] {"",
                                            seq,
                                           "","","lys","   ","","","","lys"
                                           });


            InputStreamReader bin = new InputStreamReader(is);
            BufferedReader binr = new BufferedReader(bin);
            String line = binr.readLine();

            FileOutputStream fout = new FileOutputStream("temp.html");
            while (line!=null) {
                fout.write(line.getBytes());
                line = binr.readLine();
            }
            fout.close();
            is.close();
            binr.close();
            bin.close();


            FileReader fin = new FileReader("temp.html");
            BufferedReader bufRead = new BufferedReader(fin);

            String html="";
            do{

               line = bufRead.readLine();
               html = html + line;
             }while(line!=null);
            bufRead.close();
            fin.close();

            FileOutputStream flog = new FileOutputStream(logfile, true);
            if(html.contains("could not predict"))
            {
                String log = ">"+protein.getAccession()+"\r\n"+protein.getSequence()+"\r\n";
                flog.write(log.getBytes());
                return false;
            }
            flog.close();


            String startstring = "<tbody><tr><td>Residue</td><td>Position</td><td>Flanking sequences</td></tr><font color=\"red\"></font><br><tr>";

            String patternstart = "<td>";
            String patternend = "</td>";

            String endstring = "</tbody></table>";

            int index = html.indexOf(startstring);
            int indexend = index;
            
            if(index > 0)
            {
                indexend = html.indexOf(endstring,index+startstring.length());
                System.out.print(html.subSequence(index, indexend));
                int index1 = html.indexOf(patternstart,index+startstring.length());
                int index2 = index1;
                int count = 0;
                while(index1>0 && index1<=indexend)
                {
                    index1 = index1+patternstart.length();
                    index2 = html.indexOf(patternend, index1);
                    String sitestr = html.substring(index1, index2);
                    if(count % 3 == 1 && !sitestr.equals("-"))
                    {
                    
                    int site = Integer.parseInt(sitestr);
                    position.add(site);
                    score.add(1.0);
                    }
                    count++;
                    index1 = html.indexOf(patternstart,index1+patternstart.length());

                }
            }




           for(int i= 0 ; i < position.size(); i++)
            {
                System.out.println(position.get(i));
                System.out.println(score.get(i));
                System.out.println("");
            }



            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

}
