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

import java.io.InputStream;
import java.io.IOException;


import java.util.Collections;
import java.util.List;
import java.util.Map;

import java.util.HashMap;


import musite.Proteins;
import musite.ProteinsImpl;
import musite.Protein;
import musite.PTM;
import musite.PTMAnnotationUtil;
import musite.ResidueAnnotationUtil;

import musite.io.Reader;

import musite.util.IOUtil;
import musite.util.AminoAcid;
import musite.util.MultiMap;
import musite.util.MultiTreeMap;

/**
 *
 * @author LucasYao
 */
public class PhosphositeProteinsReader implements Reader<Proteins> {
    private Proteins data;
    private PTM presetPTM;
    private Map<AminoAcid,String> keywords;

    public PhosphositeProteinsReader() {
        this(null);
    }

    public PhosphositeProteinsReader(Proteins data) {
        this.data = data==null?new ProteinsImpl():data;
        this.presetPTM = null;
    }

    public PhosphositeProteinsReader(Proteins data, PTM ptm) {
        this.data = data==null?new ProteinsImpl():data;
        this.presetPTM = ptm;
    }

    public PhosphositeProteinsReader(Proteins data, PTM ptm, Map<AminoAcid,String> keywords) {
        this.data = data==null?new ProteinsImpl():data;
        this.presetPTM = ptm;
        this.keywords = keywords;
    }
    

    public Proteins read(InputStream is) throws IOException {
        List<String> lines = IOUtil.readStringListAscii(is);
        int n = lines.size();
        for(int i=1; i<n; i++)
        {
            String line = lines.get(i);
            if(line!=null && !line.trim().equals(""))
            {
                String result[] = line.trim().split("\t");

                String accession = result[1].trim().toUpperCase();
                String siteinfo = result[3].trim();
                String peptide = result[8].trim();
                char aa = siteinfo.charAt(0);
                String pos = siteinfo.substring(1);

                Protein protein = data.getProtein(accession);
                if(protein == null)
                {
                    System.out.println("cannot find protein: "+accession);
                }
                else
                {
                    int site = Integer.parseInt(pos)-1;
                    if(site<protein.getSequence().length()&&protein.getSequence().charAt(site)==aa)
                    {
                     annotate(protein, site, presetPTM);
                    }
                    else
                    {
                        int start = 0;
                        int end = peptide.length()-1;
                        while(peptide.charAt(start)=='_')
                            start++;
                        while(peptide.charAt(end)=='_')
                            end--;
                        String subpeptide = peptide.substring(start, end+1).toUpperCase();
                        int ind = protein.getSequence().indexOf(subpeptide);
                        if(ind>=0)
                        {
                            ind = ind + (peptide.length()-1)/2-start;
                            if(protein.getSequence().charAt(ind)==aa)
                             {

                                 annotate(protein, ind, presetPTM);
                             }
                            else
                            System.out.println("Error in adding anotation to protein: "+accession+ " "+siteinfo);
                        }
                        else
                        {
                            System.out.println("Error in finding peptides in protein: "+accession+ " "+siteinfo+ " "+peptide);

                        }
                    }
                }

                
            }
        }



        return data;
    }

    public void annotate(Protein protein, int site, PTM ptm)
    {
        AminoAcid aa = AminoAcid.of(protein.getSequence().charAt(site));

        Map<String,Object> annotation = new HashMap<String,Object>(Collections.singletonMap("keyword", keywords.get(aa)));
       
        PTMAnnotationUtil.annotate(protein, site, ptm, null, annotation);
          

    }

}
