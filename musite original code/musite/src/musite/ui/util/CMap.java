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

public interface CMap
{
    /**
    * @param row logical cell row
    * @param column logical cell column
    * @return number of columns spanned a cell
    */
    int span (int row, int column);
    /**
    * @param row logical cell row
    * @param column logical cell column
    * @return the index of a visible cell covering a logical cell
    */
    int visibleCell(int row, int column);
}