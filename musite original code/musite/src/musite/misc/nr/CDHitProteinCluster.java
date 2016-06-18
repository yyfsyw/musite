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

package musite.misc.nr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import musite.MusiteInit;
import musite.Protein;
import musite.Proteins;
import musite.ProteinsImpl;

import musite.io.fasta.DefaultProteinsFastaWriter;
import musite.io.fasta.FastaTravelerImpl;
import musite.io.fasta.FastaVisitor;

import musite.util.ExecUtil;
import musite.util.IOUtil;

/**
 *
 * @author Jianjiong Gao
 */
public class CDHitProteinCluster implements ProteinCluster {
    private String dirCDHit;
    private int identityThreshold;
    private int minLength;

    public CDHitProteinCluster(final String dirCDHit, final int identityThreshold) {
        this(dirCDHit, identityThreshold, 20);
    }

    public CDHitProteinCluster(
            final String dirCDHit,
            final int identityThreshold,
            final int minLength) {
        if (identityThreshold<40 || identityThreshold>100)
            throw new IllegalArgumentException();
        this.dirCDHit = dirCDHit;
        this.identityThreshold = identityThreshold;
        this.minLength = minLength;
    }

    public List<List<Protein>> build(Proteins proteins) throws Exception {
        long currTime = System.currentTimeMillis();
        String tmpFasta = MusiteInit.TMP_DIR+File.separator+currTime+".fasta";
        String tmpClust = tmpFasta+".cdhit";

        writeFasta(proteins, tmpFasta);
        runCDHit(tmpFasta, tmpClust);
        List<List<Protein>> res = parseResult(proteins, tmpClust+".clstr");

        try {
            IOUtil.deleteFile(tmpFasta);
            IOUtil.deleteFile(tmpClust);
            IOUtil.deleteFile(tmpClust+".clstr");
            IOUtil.deleteFile(tmpClust+".bak.clstr");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    protected void writeFasta(Proteins proteins, String fasta) throws IOException {
        Set<String> fields = new HashSet();
        fields.add(Protein.ACCESSION);
        fields.add(Protein.SEQUENCE);
        Proteins proteinsCopy = new ProteinsImpl(proteins, true, fields);

        proteinsCopy.filterProteins(new Proteins.ProteinFilter() {
            public boolean filter(Protein protein) {
                String seq = protein.getSequence();
                return seq!=null && seq.length()>=minLength;
            }
        });
        
        DefaultProteinsFastaWriter writer = new DefaultProteinsFastaWriter();
        FileOutputStream fos = new FileOutputStream(fasta);
        writer.write(fos, proteinsCopy);
        fos.close();
    }

    protected void runCDHit(String fasta, String clust) throws Exception {
        int n;
        if (identityThreshold<50)
            n = 2;
        else if(identityThreshold<60)
            n = 3;
        else if(identityThreshold<70)
            n = 4;
        else
            n = 5;

//        ProcessBuilder pb = new ProcessBuilder(dirCDHit,
//                "-i",fasta, "-o",clust,"-c",""+identityThreshold/100.0,"-n",""+n,
//                "-d","500","-M","1000");

        String cmd = dirCDHit
                +" -i "+fasta
                +" -o "+clust
                +" -c "+identityThreshold/100.0
                +" -n "+n
                +" -d 500 -M 1000";
        
        Process process = Runtime.getRuntime().exec(cmd);//pb.start();

        ExecUtil.redirect(process, System.err, System.out);

        int retcode = process.waitFor();
        System.err.flush();
        System.out.flush();
        if (retcode!=0) {
            throw new Exception("Error when running CDHit.");
        }
    }

    protected List<List<Protein>> parseResult(final Proteins proteins, final String clust)
            throws IOException {
        final List<List<Protein>> res = new ArrayList();
        FastaVisitor visitor = new FastaVisitor() {
            public void visit(String header, String sequence) {
                if (sequence==null)
                    return;
                String[] strs = sequence.split("\n");

                List<Protein> list = new ArrayList(strs.length);
                for (String str : strs) {

                    int s = str.indexOf(">");
                    if (s==-1 || s>=str.length()-1)
                        continue;
                    
                    int t = str.indexOf("...");
                    String acc;
                    if (t==-1)
                        acc = str.substring(s+1);
                    else
                        acc = str.substring(s+1, t);
                    
                    Protein protein = proteins.getProtein(acc);
                    if (protein!=null)
                        list.add(protein);
                }

                if (!list.isEmpty())
                    res.add(list);
            }
        };

        FastaTravelerImpl traveler = new FastaTravelerImpl(visitor);

        FileInputStream is = new FileInputStream(clust);
        traveler.travel(is);

        is.close();

        return res;
    }
}
