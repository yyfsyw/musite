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

package musite.ui.util;

import java.util.HashSet;
import java.util.LinkedHashMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * A JTabbedPane which has a close ('X') icon on each tab.
 *
 * To add a tab, use the method addTab(String, Component)
 *
 * To have an extra icon on each tab (e.g. like in JBuilder, showing the file type) use
 * the method addTab(String, Component, Icon). Only clicking the 'X' closes the tab.
 */
public class JTabbedPaneWithCloseIcons extends JTabbedPane implements MouseListener {
    public interface TabActionListener {
        public void actionOnTab(int tabNumber);
    }

    private LinkedHashMap<String,TabActionListener> tabActionListener;
    private HashSet<Component> closableTabs;

    public static String DELETE_ACTION = "Delete";
    public static String RENAME_ACTION = "Rename";

    public JTabbedPaneWithCloseIcons() {
        super();
        tabActionListener = new LinkedHashMap();
        closableTabs = new HashSet();
        addMouseListener(this);
        addTabActionListener(DELETE_ACTION, new TabActionListener() {
            public void actionOnTab(int tabNumber) {
                JTabbedPaneWithCloseIcons.this.removeTabAt(tabNumber);
            }
        });
    }

    public void addTabActionListener(String action, TabActionListener listener) {
        this.tabActionListener.put(action, listener);
    }

    public void addClosableTab(String title, Component component) {
        this.addClosableTab(title, null, component);
    }

    public void addClosableTab(String title, Icon extraIcon, Component component) {
        super.addTab(title, new CloseTabIcon(extraIcon), component);
        closableTabs.add(component);
    }

    public void removeTabAt(int index) {
        closableTabs.remove(getComponentAt(index));
        super.removeTabAt(index);
    }

    public void mouseClicked(MouseEvent e) {
        int tabNumber = getTabNumberOnCloseIcon(e.getX(),e.getY());
        if (tabNumber>=0 && tabActionListener.get(DELETE_ACTION)!=null) {
            tabActionListener.get(DELETE_ACTION).actionOnTab(tabNumber);
        }
    }

    public void mouseEntered(MouseEvent e) {
//        int tabNumber = getTabNumber(e.getX(),e.getY());
//        if (tabNumber>=0) {
//            CloseTabIcon icon = (CloseTabIcon)getIconAt(tabNumber);
//            icon.setColor(Color.PINK);
//            repaint(icon.getBounds());
//        }
    }

    public void mouseExited(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {
        popup(e);
    }
    
    public void mouseReleased(MouseEvent e) {
        popup(e);
    }

    private int getTabNumberOnCloseIcon(int x, int y) {
        int tabNumber=getUI().tabForCoordinate(this, x, y);
        if (tabNumber < 0) return -1;

        Icon icon = getIconAt(tabNumber);
        if (!(icon instanceof CloseTabIcon))
            return -1;

        Rectangle rect=((CloseTabIcon)icon).getBounds();
        return rect.contains(x, y) ? tabNumber:-1;
    }

    private void popup(MouseEvent e) {
        if(!e.isPopupTrigger()) return;

        int tabNumber = getUI().tabForCoordinate(this, e.getX(), e.getY());
        if (tabNumber<0) return;

        if (!closableTabs.contains(getComponentAt(tabNumber))) return;

        TabPopupMenu tabMenu = new TabPopupMenu(tabNumber);
        tabMenu.show(this, e.getX(), e.getY());
    }

    /**
     * The class which generates the 'X' icon for the tabs. The constructor
     * accepts an icon which is extra to the 'X' icon, so you can have tabs
     * like in JBuilder. This value is null if no extra icon is required.
     */
    private class CloseTabIcon implements Icon {
        private int x_pos;
        private int y_pos;
        private int width;
        private int height;
        private Icon fileIcon;

        private Color color;

        public CloseTabIcon(Icon fileIcon) {
            this.fileIcon=fileIcon;
            width=16;
            height=16;
            color = Color.BLACK;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            this.x_pos=x;
            this.y_pos=y;

            Color col=g.getColor();

            g.setColor(color);
            int y_p=y+2;
            g.drawLine(x+1, y_p, x+12, y_p);
            g.drawLine(x+1, y_p+13, x+12, y_p+13);
            g.drawLine(x, y_p+1, x, y_p+12);
            g.drawLine(x+13, y_p+1, x+13, y_p+12);
            g.drawLine(x+3, y_p+3, x+10, y_p+10);
            g.drawLine(x+3, y_p+4, x+9, y_p+10);
            g.drawLine(x+4, y_p+3, x+10, y_p+9);
            g.drawLine(x+10, y_p+3, x+3, y_p+10);
            g.drawLine(x+10, y_p+4, x+4, y_p+10);
            g.drawLine(x+9, y_p+3, x+3, y_p+9);
            g.setColor(col);
            if (fileIcon != null) {
                fileIcon.paintIcon(c, g, x+width, y_p);
            }
        }

        public int getIconWidth() {
            return width + (fileIcon != null? fileIcon.getIconWidth() : 0);
        }

        public int getIconHeight() {
            return height;
        }

        public Rectangle getBounds() {
            return new Rectangle(x_pos, y_pos, width, height);
        }
    }

    private class TabPopupMenu extends JPopupMenu {
        public TabPopupMenu(final int tabNum) {
            for (final String action : tabActionListener.keySet()) {
                JMenuItem mi = new JMenuItem(action);
                mi.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                         tabActionListener.get(action).actionOnTab(tabNum);
                    }
                });
                add(mi);
            }
        }
    }
}
