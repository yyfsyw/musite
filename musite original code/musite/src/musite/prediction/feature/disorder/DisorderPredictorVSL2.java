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

package musite.prediction.feature.disorder;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import musite.MusiteInit;

import musite.io.fasta.FastaSequenceVisitor;

import musite.util.ExecUtil;
import musite.util.IOUtil;
import musite.util.ProteinSequenceUtil;

/**
 *
 * @author Jianjiong Gao
 */
public class DisorderPredictorVSL2 implements DisorderPredictor {
    private String dirVSL2;
    private FastaSequenceVisitor seqVisitor = null;

    public DisorderPredictorVSL2(final String dirVSL2) {
        if (dirVSL2==null)
            throw new IllegalArgumentException();

        File file = new File(dirVSL2);
        if (!file.exists()) {
            throw new IllegalArgumentException(dirVSL2 + " does not exist.");
        }

        this.dirVSL2 = dirVSL2;
    }

    public void setSequenceVisitor(FastaSequenceVisitor seqVisitor) {
        this.seqVisitor = seqVisitor;
    }

    /**
     *
     * @param sequence
     * @return
     */
    public List<Double> predict(final String sequence) {
        if (sequence==null)
            return null;

        String seq = sequence.toUpperCase();

        List<Integer> idxNotAA = new ArrayList();
        Pattern notAA = Pattern.compile("[^"+ProteinSequenceUtil.ALPHABET+"]");
        Matcher m = notAA.matcher(seq);
        while (m.find()) {
            int idx = m.start();
            idxNotAA.add(idx);
        }

        if (!idxNotAA.isEmpty()) {
            seq = m.replaceAll("");
        }

        List<Double> res = predictNoX(seq);
        if (res==null) {
            return null;
        }

        // predict for non amino acid
        if (!idxNotAA.isEmpty()) {
            for (int idx : idxNotAA) {
                if (idx==0) {
                    res.add(res.get(0));
                } else if (idx==res.size()) {
                    res.add(res.get(idx-1));
                } else {
                    res.add((res.get(idx-1)+res.get(idx))/2.0);
                }
            }
        }

        if (res.size()!=sequence.length()) {
            System.err.println("Disorder prediction result is not the same size " +
                    "as seuqence, something is wrong.");
            return null;
        }

        return res;
    }

    private List<Double> predictNoX(final String sequence) {
        if (seqVisitor!=null) {
            seqVisitor.visit(sequence);
        }
        
        long currTime = System.currentTimeMillis();
        String dirTmpSeq = MusiteInit.TMP_DIR+File.separator
                +currTime+"seq.tmp";
        String dirTmpRes = MusiteInit.TMP_DIR+File.separator
                +currTime+"res.tmp";
        try {
            IOUtil.writeStringAscii(sequence.toUpperCase(), dirTmpSeq);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        List<Double> res = null;
        if (runVSL2(dirTmpSeq, dirTmpRes)) {
            res = parseVSL2Res(dirTmpRes, sequence.length());
        }
        
        try {
            IOUtil.deleteFile(dirTmpSeq);
            IOUtil.deleteFile(dirTmpRes);
        } catch (Exception ex) {
            ex.printStackTrace(); // skip--not essential
        }

        return res;

    }

    protected boolean runVSL2(final String dirSeq, final String dirRes) {
        String cmd = "java -jar " + dirVSL2 + " -s:" + dirSeq;
//        ProcessBuilder pb = new ProcessBuilder("java","-jar",dirVSL2,"-s:\""+dirSeq+"\"");
//        pb.redirectErrorStream(true);

        Runtime runTime = Runtime.getRuntime();

        try {
            Process process = runTime.exec(cmd);//pb.start();

            java.io.FileOutputStream os = new java.io.FileOutputStream(dirRes);
            ExecUtil.redirect(process, System.err, os);

            int retcode = process.waitFor();

            os.close();

            return retcode==0;
            
//            if (retcode==0) {
//                InputStream is = process.getInputStream();
//                InputStreamReader reader = new InputStreamReader(is);
//                IOUtil.writeListAscii(IOUtil.readStringListAscii(reader), dirRes);
//                return true;
//            } else {
//                return false;
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    protected List<Double> parseVSL2Res(final String dirRes, int nAA) {
        List<String> strs;
        try {
            strs = IOUtil.readStringListAscii(dirRes);
        } catch(IOException ex) {
            ex.printStackTrace();
            return null;
        }

        List<Double> res = new ArrayList<Double>(nAA);

        Pattern p = Pattern.compile("([0-9]+)\t[a-zA-Z]\t([0-1]\\056[0-9]+)\t[D\\056]");

        for (String str : strs) {
            Matcher m = p.matcher(str);
            if (m.matches()) {
                int idx = Integer.parseInt(m.group(1))-1;
                double score = Double.parseDouble(m.group(2));
                res.add(idx, score);
            }
        }

        return res;
    }

}
