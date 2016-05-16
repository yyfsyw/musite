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

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * Adapted from http://www.swingwiki.org/howto:column_spanning
 * @author Jianjiong Gao
 */
public class CTable extends JTable {
    CMap map;

    public CTable() {
        this(null, new DefaultTableModel());
    }

    public CTable(CMap cmp, TableModel tbl) {
        super(tbl);
        map=cmp;
        setUI(new CTableUI());
    }

    public void setCMap(CMap map) {
        this.map = map;
    }

    public Rectangle getCellRect(int row, int column, boolean includeSpacing){
        // required because getCellRect is used in JTable constructor
        if (map==null) return super.getCellRect(row,column, includeSpacing);

        // add widths of all spanned logical cells
        int sk=map.visibleCell(row,column);
        Rectangle r1=super.getCellRect(row,sk,includeSpacing);
        if (map.span(row,sk)!=1)
        for (int i=1; i<map.span(row,sk); i++){
            r1.width+=getColumnModel().getColumn(sk+i).getWidth();
        }
        return r1;
    }

    public int columnAtPoint(Point p) {
        if (map==null) {
            return super.columnAtPoint(p);
        }
        
        int x=super.columnAtPoint(p);
        // -1 is returned by columnAtPoint if the point is not in the table
        if (x<0) return x;
        int y=super.rowAtPoint(p);
        return map.visibleCell(y,x);
    }
}