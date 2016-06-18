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

package musite;

import com.jgoodies.looks.Options;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;

import java.awt.Dimension;

import java.util.Arrays;

import javax.swing.UIManager;

import musite.ui.cmd.CmdLineTools;

import org.apache.commons.lang.SystemUtils;

/**
 *
 * @author Jianjiong Gao
 */
public class MusiteMain {
    /**
    * @param args the command line arguments
    */
    public static void main(String[] args) {
        init();

        if (args.length==0) {
            // gui mode
            setupLookAndFeel();

            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    Musite.getDesktop().setVisible(true);
                }
            });
        } else {
            if (args[0].equalsIgnoreCase("list-model")) {
                CmdLineTools.listModelCmd();
            } else if (args[0].equalsIgnoreCase("predict")) {
                try {
                    CmdLineTools.predictCmd(Arrays.copyOfRange(args, 1, args.length));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            } else {
                System.err.println("Unsupported command");
                System.exit(1);
            }
        }
    }

    private static void init() {
        MusiteInit.init();
//        MusiteInitTask task = new MusiteInitTask();
//        TaskUtil.execute(task);
//        boolean succ = task.success();
//
//        if (succ) {
//
//        }
    }


    private static  void setupLookAndFeel() {
        try {
            if (SystemUtils.IS_OS_WINDOWS) {
                /*
                 * For Windows: just use platform default look & feel.
                 */
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } else if (SystemUtils.IS_OS_MAC) {
                /*
                 * For Mac: move menue bar to OS X default bar (next to Apple
                 * icon)
                 */
                System.setProperty("apple.laf.useScreenMenuBar", "true");
            } else {
                /*
                 * For Unix platforms, use JGoodies Looks
                 */
                UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
                Plastic3DLookAndFeel.set3DEnabled(true);
                Plastic3DLookAndFeel
                                .setCurrentTheme(new com.jgoodies.looks.plastic.theme.SkyBluer());
                Plastic3DLookAndFeel
                                .setTabStyle(Plastic3DLookAndFeel.TAB_STYLE_METAL_VALUE);
                Plastic3DLookAndFeel.setHighContrastFocusColorsEnabled(true);

                Options.setDefaultIconSize(new Dimension(18, 18));
                Options.setHiResGrayFilterEnabled(true);
                Options.setPopupDropShadowEnabled(true);
                Options.setUseSystemFonts(true);

                UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
                UIManager.put(Options.USE_SYSTEM_FONTS_APP_KEY, Boolean.TRUE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
