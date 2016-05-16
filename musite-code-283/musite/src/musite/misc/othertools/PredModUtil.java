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
import java.net.URL;
import java.io.InputStream;
import musite.util.IOUtil;

/**
 *
 * @author LucasYao
 */
public class PredModUtil {

    public static void runResultsByPredMod(String inputfile, String outputfile)
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
            ArrayList<Integer> posposition = new ArrayList<Integer>();
            ArrayList<Double> posconfidence = new ArrayList<Double>();
            ArrayList<Integer> negposition = new ArrayList<Integer>();
            ArrayList<Double> negconfidence = new ArrayList<Double>();
            predictByPredMod(protein,posposition,posconfidence,negposition,negconfidence);
            for(int i=0;i<posposition.size();i++)
            {
                String temp = protein.getAccession()+" "+posposition.get(i)
                        +" "+posconfidence.get(i)+"\r\n";
                fout.write(temp.getBytes());
            }
        }
        fout.close();

        }
        catch(Exception e){}
    }

    public static void predictByPredMod(Protein protein, ArrayList<Integer> posposition,ArrayList<Double> posconfidence,ArrayList<Integer> negposition,ArrayList<Double> negconfidence)
    {
        try {
            String seq = protein.getSequence();
            String url = "http://ds9.rockefeller.edu/cgi-bin/basu/myscript_w8.cgi?H3=NONE&H4=NONE&H2A=NONE&H2B=NONE&DNA=NONE&TRANS=NONE&ProtInput="+seq+"&acet=1&group1=3&Submit2=Submit";
 
            URL u = new URL(url);

            InputStream is = u.openStream();

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

  
            String posstring = "<FONT size=\"-1\" color=\"red\">";
            String negstring = "<FONT size=\"-1\" color=\"blue\">";
            String strfont = "</FONT>";
            String tempinfo;
            int index=0;

            index = html.indexOf(posstring);
            while(index>=0)
            {

                    index = html.indexOf(posstring,index);
                    if(index<0)break;
                    int startindex = index+posstring.length();
                    int endindex = html.indexOf(strfont, index);
                    tempinfo = html.substring(startindex, endindex);
                    int i = Integer.parseInt(tempinfo.trim());
                    posposition.add(i);
                    index = endindex;

                    index = html.indexOf(posstring,index);
                    if(index<0)break;
                    startindex = index+posstring.length();
                    endindex = html.indexOf(strfont, index);
                    tempinfo = html.substring(startindex, endindex);
                    double d = Double.parseDouble(tempinfo.trim());
                    posconfidence.add(d);
                    index = endindex;

            }

            index = html.indexOf(negstring);
            while(index>=0)
            {

                    index = html.indexOf(negstring,index);
                    if(index<0)break;
                    int startindex = index+negstring.length();
                    int endindex = html.indexOf(strfont, index);
                    tempinfo = html.substring(startindex, endindex);
                    int i = Integer.parseInt(tempinfo.trim());
                    negposition.add(i);
                    index = endindex;

                    index = html.indexOf(negstring,index);
                    if(index<0)break;
                    startindex = index+negstring.length();
                    endindex = html.indexOf(strfont, index);
                    tempinfo = html.substring(startindex, endindex);
                    Double d = Double.parseDouble(tempinfo.trim());
                    negconfidence.add(d);
                    index = endindex;

            }




/*
            for(int i= 0 ; i < posposition.size(); i++)
            {
                System.out.println(posposition.get(i));
                System.out.println(posconfidence.get(i));
                System.out.println("");
            }
            System.out.println("-----------");
            for(int i= 0 ; i < negposition.size(); i++)
            {
                System.out.println(negposition.get(i));
                System.out.println(negconfidence.get(i));
                System.out.println("");
            }

 */

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
