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
public class NCBIUtil {

    public static String getSequenceFromNCBI(String SearchID)
    {
        String sequence="";

        try{

            String url = "http://www.ncbi.nlm.nih.gov/protein/"+SearchID;

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
            is.close();


            String start ="<span class=\"rprtlinks\">      <a class=\"dblinks\" href=\"";
            String end = "\" name=\"EntrezSystem2.PEntrez.Protein.Sequence_ResultsPanel.SequenceViewer.Sequence_ViewerTitle.ReportShortCut\"";
            int startindex = html.indexOf(start);
            int endindex = html.indexOf(end);
            if(startindex>=0 && endindex>=0 && startindex<endindex)
            {
                startindex += start.length();
                String tempinfo = html.substring(startindex, endindex);
                getFastaSequencefromNCBI(tempinfo);
                //System.out.println(tempinfo);
            }
            else
            {
                System.out.println("Cannot find: "+SearchID);
            }




        }
       catch (Exception e) {
            e.printStackTrace();
        }
        return sequence;
    }

    public static String getFastaSequencefromNCBI(String fastaaddress)
    {
        String sequence="";
        try
        {
            String url = "http://www.ncbi.nlm.nih.gov"+fastaaddress;

            URL u = new URL(url);

            InputStream is = u.openStream();

            java.util.List<String> lines = IOUtil.readStringListAscii(
                    new java.io.InputStreamReader(is));
            FileOutputStream fout = new FileOutputStream("temp2.html");
            for (String line:lines) {

                fout.write(line.getBytes());
            }
            fout.close();

            FileReader fin = new FileReader("temp2.html");
            BufferedReader bufRead = new BufferedReader(fin);
            String line;
            String html="";
            do{

               line = bufRead.readLine();
               html = html + line;
             }while(line!=null);
            bufRead.close();
            fin.close();

            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return sequence;
    }

}
