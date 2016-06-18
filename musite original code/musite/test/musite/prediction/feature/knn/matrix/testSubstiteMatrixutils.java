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




package musite.prediction.feature.knn.matrix;
import junit.framework.TestCase;
import musite.prediction.feature.knn.*;
import java.util.ArrayList;

/**
 *
 * @author LucasYao
 */
public class testSubstiteMatrixutils extends TestCase{

    public void testGenMatrix()
    {
        ArrayList<String> list = new ArrayList<String>();
        list.add("BABA");
        list.add("AAAC");
        list.add("AACC");
        list.add("AABA");
        list.add("AACC");
        list.add("AABC");
        SimilarityMatrix matrix = SubstituteMatrixUtils.genSubstituteMatrix(list);
    }
}
