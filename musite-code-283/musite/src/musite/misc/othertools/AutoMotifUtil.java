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
public class AutoMotifUtil {

    public static void runResultsByAutoMotif(String inputfile, String outputfile, String PredName, String logfile)
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
            predictByAutoMotif(protein, PredName, position, score, logfile);
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

    public static boolean predictByAutoMotif(Protein protein, String PredName, ArrayList<Integer> position, ArrayList<Double> score, String logfile)
    {
      try {
            String seq = protein.getSequence();

            InputStream is = ClientHttpRequest.post(
                              new java.net.URL("http://ams2.bioinfo.pl/cgi-bin/runquery.cgi"),
                              new Object[] {
                                            "svmquery", seq,
                                            "dbsearch","SVM",
                                            "nproc", "5",
                                            "processname",PredName,
                                            "run","Start",
                                            "softver","2.0",
                                            "authorid","1"

                                           });


            boolean flag = false;
            String urlstring="";
            do{
                if(flag == true)
                {
                    Thread.sleep(15*1000);
                    URL u = new URL(urlstring);
                    is = u.openStream();
                    flag = false;
                }

            InputStreamReader bin = new InputStreamReader(is);
            BufferedReader binr = new BufferedReader(bin);
                String line = binr.readLine();
                if(line==null)flag = true;

            FileOutputStream fout = new FileOutputStream("temp.html");
            while (line!=null) {
                fout.write(line.getBytes());
                if(line.contains("wait 30 s"))
                {
                    int a = line.indexOf("href=\"");
                    if(a>0)
                    {
                        int b = line.indexOf("html\">", a);
                        urlstring = line.substring(a+6, b+4);
                    }

                    flag = true;

                }
                line = binr.readLine();
            }
            fout.close();
            is.close();
            }while(flag == true);

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

            FileOutputStream flog = new FileOutputStream(logfile, true);
            if(html.contains("could not predict"))
            {
                String log = ">"+protein.getAccession()+"\r\n"+protein.getSequence()+"\r\n";
                flog.write(log.getBytes());
                return false;
            }
            flog.close();



            String startstring = "<tr align=\"right\" valign=\"middle\" bgcolor=\"#ECD7D7\">	       <td><div align=\"left\">";
            String realstartstring ="</div></td><td><div align=\"left\">";
            String patternstart = "</td><td>";
            String patternend = "</td>";
            String scorestart = "<td><b>";
            String scoreend = "</b></td>";
            int index = html.indexOf(startstring);
            while(index > 0)
            {
                int index1 = html.indexOf(realstartstring,index+startstring.length());
                if(index1>0)
                {
                    index1 = index1+realstartstring.length();
                    for(int count = 0; count<3;count++)
                    {
                    int index2 = html.indexOf(patternstart,index1);
                    index2 = index2+patternstart.length();
                    int index3 = html.indexOf(patternend,index2);
                        if(count==1)
                        {
                            String sitestr = html.substring(index2, index3);
                            int site = Integer.parseInt(sitestr);
                            position.add(site);
                        }
                    index1 = index3;
                    }
                    int index4 = html.indexOf(scorestart,index1);
                    index4 = index4+scorestart.length();
                    int index5 = html.indexOf(scoreend,index4);
                    String scorestr = html.substring(index4, index5);
                    double confscore = Double.parseDouble(scorestr);
                    score.add(confscore);
                    
                }
                index = html.indexOf(startstring,index+startstring.length());
            }



/*
           for(int i= 0 ; i < position.size(); i++)
            {
                System.out.println(position.get(i));
                System.out.println(score.get(i));
                System.out.println("");
            }
*/
 

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

}
