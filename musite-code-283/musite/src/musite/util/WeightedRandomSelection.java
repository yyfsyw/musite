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

import java.util.List;
import java.util.Random;

/**
 * Adapted from http://forums.sun.com/thread.jspa?forumID=31&threadID=5221000
 * @author Jianjiong Gao
 */
public class WeightedRandomSelection {
  private final double c[]; // binary choice tree - kept heap ordered in array
  private final int n;
  private final Random rnd;

  public WeightedRandomSelection(final List<Double> mass){
      this(mass, new Random());
  }
  
  public WeightedRandomSelection(final List<Double> mass, final Random rnd){
      if (mass==null || rnd==null) {
          throw new NullPointerException();
      }

      n = mass.size();
      if (n==0) {
          throw new IllegalArgumentException();
      }

      for (double m : mass) {
          if (m<0) {
              throw new IllegalArgumentException("Only non-negative weights are allowed");
          }
      }

      this.rnd = rnd;

      c = new double[n];
      for(int i = n-1; i>0; i--){ // first calculate total mass from leaves to top
        int k = 2*i; // k is left child, k+1 is right
        c[i] = get(k, mass) + get(k+1, mass); // c is sum of mass of two children.
      }

      // since we aren't using c[0] for anything, we will keep total mass there
      c[0] = c[1]; // c[1] was the total mass of entire tree.

      // Now we fixup internal nodes to be only hold mass of the left child
      for(int i = 1; i<n; i++) {
          c[i] -= get(2*i + 1, mass);
      }
    }

  // helper routine - lets us treat c and mass as one large array.
  private double get(int i, List<Double> mass) {
      return (i<n)?c[i]:mass.get(i-n);
  }

  public int nextIndex(){
    double r = rnd.nextDouble()*c[0]; // r is uniformly distributed across total mass
    return reduce(r); // r reduces to a single index, this function simplifies testing
  }

  private int reduce(double r){
    int k = 1;
    while(k<n) {
        double m = c[k];
        k *= 2;
        if(r>=m) {
            r -= m;
            k++;
        }
    }
    return k-n;
  }
}
