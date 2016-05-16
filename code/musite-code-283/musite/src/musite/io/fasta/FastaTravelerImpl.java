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

package musite.io.fasta;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 *
 * @author Jianjiong Gao
 */
public class FastaTravelerImpl implements FastaTraveler {
    private final FastaVisitor visitor;
    private boolean removeSequenceEndOfLine = true;;

    public FastaTravelerImpl(final FastaVisitor visitor){
        if (visitor==null) {
            throw new NullPointerException();
        }

        this.visitor = visitor;
    }

    public void setRemoveSequenceEndOfLine(boolean removeSequenceEndOfLine) {
        this.removeSequenceEndOfLine = removeSequenceEndOfLine;
    }

    public void travel(final InputStream is) throws IOException {
        if (is==null) {
            return;
        }

        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader in = new BufferedReader(isr);

        String header = new String();
        StringBuilder sequence = new StringBuilder();

        int count = 0;

        String line = in.readLine();
        while (true) {
            while (line!=null && line.trim().length()==0) {
                line = in.readLine();
            }

            if (line==null) {
                if (header.length()>0 && sequence.length()>0) {
                    visitor.visit(header, sequence.toString());
                }

                break;
            }

            line = line.trim();

            if (line.startsWith(">")) { //header
                if (count++%1000==0) {
                    System.out.println(String.format("%10d", count));
                }

                if (header.length()>0 && sequence.length()>0) {
                    visitor.visit(header, sequence.toString());
                }

                header = line.substring(1);
                sequence = new StringBuilder();
            } else {
                sequence.append(line);
                if (!removeSequenceEndOfLine)
                    sequence.append("\n");
            }

            line = in.readLine();
        }
    }
}
