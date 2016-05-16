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
public class SulfinatorUtil {
    public static void runResultsBySulfinator(String inputfile, String outputfile, String logfile)
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
            predictBySulfinator(protein,  position, score, logfile);

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

    public static boolean predictBySulfinator(Protein protein, ArrayList<Integer> position, ArrayList<Double> score, String logfile)
    {
              try {
            String seq = protein.getSequence();

            InputStream is = ClientHttpRequest.post(
                              new java.net.URL("http://www.expasy.ch/cgi-bin/sulfinator.pl"),
                              new Object[] {
                                            "sequence", seq,
                                            
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



            String startstring = "<br><br></b></tt></td><td width=\"25%\" valign=\"top\"><tt><b>";
   
            String patternstart = "<tt><b>";
            String patternend = "<br><br></b></tt>";

            int index = html.indexOf(startstring);
            int indexend = index;
            int countsite = 0;
            if(index > 0)
            {
                indexend = html.indexOf(startstring,index+startstring.length());
                int index1 = html.indexOf(patternstart,index);
                int index2 = index1;
                while(index1>0 && index1<=indexend)
                {
                    index1 = index1+patternstart.length();
                    index2 = html.indexOf(patternend, index1);
                    String sitestr = html.substring(index1, index2);
                    int site = Integer.parseInt(sitestr);
                    position.add(site);
                    countsite++;
                    index1 = html.indexOf(patternstart,index1+patternstart.length());

                }
                int index3 = indexend;
                int index4 = index3;
                for(int i = 0; i<countsite; i++)
                {
                    index3 = html.indexOf(patternstart,index3+patternstart.length());
                    index3 = index3 + patternstart.length();
                    index4 = html.indexOf(patternend, index3);
                    String evaluestr = html.substring(index3, index4);
                    evaluestr = evaluestr.substring(1,evaluestr.length()-1);
                    double evalue = -1*Double.parseDouble(evaluestr);
                    score.add(evalue);
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
