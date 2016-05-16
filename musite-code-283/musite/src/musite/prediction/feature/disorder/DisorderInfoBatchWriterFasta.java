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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Jianjiong Gao
 */
public class DisorderInfoBatchWriterFasta implements DisorderInfoBatchWriter {
    private final int bufSize = 10000;

    private String seperator = ";";

    public void setSeperator(String seperator) {
        if (seperator==null)
            throw new IllegalArgumentException();
        this.seperator = seperator;
    }

    public void write(OutputStream os, final Map<String,List<Double>> mapAccDisorder) throws IOException {
        if (mapAccDisorder==null) {
            throw new NullPointerException();
        }

        OutputStreamWriter fout = new OutputStreamWriter(os);
        BufferedWriter out = new BufferedWriter(fout);

        StringBuilder strscores = new StringBuilder(bufSize);

        Set<Map.Entry<String,List<Double>>> entrySet = mapAccDisorder.entrySet();
        for (Map.Entry<String,List<Double>> entry : entrySet) {
            out.write(">"+entry.getKey());
            out.newLine();

            strscores.setLength(0);

            List<Double> scores = entry.getValue();
            int n = scores.size();
            
            for (int i=0; i<n; i++) {
                double score = scores.get(i);
                strscores.append(score);
                strscores.append(seperator);
            }
            
            int len = strscores.length();
            strscores.delete(len-seperator.length(), len);

            out.write(strscores.toString());
            out.newLine();
        }

        out.flush();
        fout.flush();
    }
}
