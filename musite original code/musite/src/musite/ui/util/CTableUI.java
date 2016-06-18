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

import java.awt.Component;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;


import javax.swing.JComponent;
import javax.swing.table.TableCellRenderer;
import javax.swing.plaf.basic.BasicTableUI;

public class CTableUI extends BasicTableUI {
    public void paint(Graphics g, JComponent c) {
        if (((CTable)table).map==null) {
            super.paint(g, c);
            return;
        }

        Rectangle r=g.getClipBounds();
        int firstRow=table.rowAtPoint(new Point(0,r.y));
        int lastRow=table.rowAtPoint(new Point(0,r.y+r.height));
        // -1 is a flag that the ending point is outside the table
        if (lastRow<0)
	    lastRow=table.getRowCount()-1;
        for (int i=firstRow; i<=lastRow; i++)
       	    paintRow(i,g);
    }

    private void paintRow(int row, Graphics g) {
        Rectangle r=g.getClipBounds();
        int columns = table.getColumnCount();
        for (int i=0; i<columns;i++)
        {
            Rectangle r1=table.getCellRect(row,i,true);
            if (r1.intersects(r)) // at least a part is visible
            {
                int sk=((CTable)table).map.visibleCell(row,i);
                paintCell(row,sk,g,r1);
                // increment the column counter
                i+=((CTable)table).map.span(row,sk)-1;
            }
        }
    }

    private void paintCell(int row, int column, Graphics g,Rectangle area)
    {
        int verticalMargin = table.getRowMargin();
        int horizontalMargin  = table.getColumnModel().getColumnMargin();

        area.setBounds(area.x + horizontalMargin/2,
                       area.y + verticalMargin/2,
                       area.width - horizontalMargin,
                       area.height - verticalMargin);

//        area.setBounds(area.x, area.y, area.width, area.height);

        if (table.isEditing() && table.getEditingRow()==row &&
            table.getEditingColumn()==column) {
            Component component = table.getEditorComponent();
            component.setBounds(area);
            component.validate();
        } else {
            TableCellRenderer renderer = table.getCellRenderer(row, column);
            Component component = table.prepareRenderer(renderer, row, column);
            if (component.getParent() == null)
                rendererPane.add(component);
            rendererPane.paintComponent(g, component, table, area.x, area.y,
                    area.width, area.height, true);
        }

        Color c = g.getColor();
        g.setColor(table.getGridColor());
        g.drawRect(area.x,area.y,area.width,area.height);
        g.setColor(c);
    }
}