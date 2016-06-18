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

package musite.util;

import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Jianjiong Gao
 */
public class ExecUtil {

        public static void redirect(Process process,
                                    OutputStream errtrm,
                                    OutputStream outtrm) {
            if (errtrm!=null)
                new Thread(new AsyncPipe(process.getErrorStream(), errtrm)).start();
            if (outtrm!=null)
                new Thread(new AsyncPipe(process.getInputStream(), outtrm)).start();
        }

        public static void redirect(Process process, byte[] bufferin) {
            final OutputStream ostream = process.getOutputStream();
            try {
                    ostream.write(bufferin, 0, bufferin.length);
                    ostream.close();
            } catch (Exception e) {
                    e.printStackTrace();
            }
        }

        public static void redirect(Process process,
                                    OutputStream errtrm,
                                    OutputStream outtrm,
                                    byte[] bufferin) {
                redirect(process, errtrm, outtrm);
                redirect(process, bufferin);
        }

}

class AsyncPipe implements Runnable {
        public AsyncPipe(InputStream istrm, OutputStream ostrm)
        {
            istrm_ = istrm;
            ostrm_= ostrm;
        }

        public void run()
        {
            try
            {
                final byte[] buffer = new byte[1024];
                for (int length = 0; (length = istrm_.read(buffer)) != -1;)
                {
                    ostrm_.write(buffer, 0, length);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        private final OutputStream ostrm_;
        private final InputStream istrm_;
}