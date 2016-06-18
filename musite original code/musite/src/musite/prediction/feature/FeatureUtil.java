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

package musite.prediction.feature;

import musite.Proteins;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Jianjiong Gao
 */
public class FeatureUtil {

    public static void exportFeatures(final String dirOut,
                                      final List<Instance> instances,
                                      final Proteins data) throws IOException  {
        exportFeatures(dirOut, instances, data, null);
    }

    public static void exportFeatures(final String dirOut,
                                      final List<Instance> instances,
                                      final Proteins data,
                                      final List<FeatureExtractor> extractors) throws IOException {
        if (dirOut==null || instances==null || data==null) {
            throw new NullPointerException();
        }

        Writer fout = new FileWriter(dirOut);
        BufferedWriter out = new BufferedWriter(fout);

        for (Instance ins : instances) {
            List<Double> features = new ArrayList<Double>();
                    
            if (extractors!=null) {
                for (FeatureExtractor extractor : extractors) {
                    features.addAll(extractor.extract(ins, false));
                }
            } else {
                features = ins.getFeatures();
            }

            for (double f : features) {
                out.write(""+f+"\t");
            }
            out.newLine();
        }

        out.close();
        fout.close();
    }
}
