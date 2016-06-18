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

import musite.util.ExecUtil;
import musite.util.IOUtil;

/**
 *
 * @author Jianjiong Gao
 */
public class BlastClustProteinCluster implements ProteinCluster {
    private String dirBlastClust;
    private String dirBlosum62;
    private int similarityThreshold;
    private double minimumCoverage;
    private boolean requireBoth;
    private int minLength;

    public BlastClustProteinCluster(
            final String dirBlastClust,
            final String dirBlosum62,
            final int similarityThreshold,
            final double minimumCoverage,
            final boolean requireBoth) {
        this(dirBlastClust, dirBlosum62, similarityThreshold, minimumCoverage, requireBoth, 20);
    }

    public BlastClustProteinCluster( final String dirBlastClust,
            final String dirBlosum62, final int similarityThreshold,
            final double minimumCoverage, final boolean requireBoth,
            final int minLength) {
        if (dirBlastClust==null || dirBlosum62==null) {
            throw new IllegalArgumentException();
        }

        this.dirBlastClust = dirBlastClust;
        this.dirBlosum62 = dirBlosum62;
        this.similarityThreshold = similarityThreshold;
        this.minimumCoverage = minimumCoverage;
        this.requireBoth = requireBoth;
        this.minLength = minLength;
    }

    public List<List<Protein>> build(Proteins proteins) throws Exception {
        long currTime = System.currentTimeMillis();
        String tmpFasta = MusiteInit.TMP_DIR+File.separator+currTime+".fasta";
        String tmpClust = tmpFasta+".clust";

        writeFasta(proteins, tmpFasta);
        runBlastClust(tmpFasta, tmpClust);
        List<List<Protein>> res = parseResult(proteins, tmpClust);

        try {
            IOUtil.deleteFile(tmpFasta);
            IOUtil.deleteFile(tmpClust);
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
    }

    protected void runBlastClust(String fasta, String clust) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(dirBlastClust,
                "-i",fasta, "-o",clust, "-b",(requireBoth?"T":"F"),
                "-S",""+similarityThreshold, "-L",""+minimumCoverage);
        //pb.environment().put(".ncbirc", dirBlosum62);
        pb.directory(new File(dirBlosum62));
        //pb.redirectErrorStream(true);

        Process process = pb.start();

        ExecUtil.redirect(process, System.err, System.out);
        int retcode = process.waitFor();
        System.err.flush();
        System.out.flush();
        if (retcode!=0) {
            throw new Exception("Error when running BLASTClust.");
        }

        try {
            // delete error.log
            IOUtil.deleteFile(dirBlosum62+File.separator+"error.log");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected List<List<Protein>> parseResult(Proteins proteins, String clust) {
        List<String> lines;
        try {
            lines = IOUtil.readStringListAscii(clust);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        int n = lines.size();
        List<List<Protein>> res = new ArrayList(n);
        for (int i=0; i<n; i++) {
            String[] strs = lines.get(i).split(" ");
            int ns = strs.length;
            List<Protein> list = new ArrayList(ns);
            for (int j=0; j<ns; j++) {
                list.add(proteins.getProtein(strs[j].trim()));
            }
            res.add(list);
        }

        return res;
    }
}
