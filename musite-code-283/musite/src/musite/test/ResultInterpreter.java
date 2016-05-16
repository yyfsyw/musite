/**
 * Musite
 * Copyright (C) 2010-2011 Digital Biology Laboratory, University Of Missouri
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

package musite.test;

import java.io.Serializable;

import java.util.Arrays;

/**
 *
 * @author gjj
 */
public class ResultInterpreter implements Serializable {
    private static final long serialVersionUID = -2287453690274188142L;

    private final double[] thresholds;
    private final double[] fprs;
    private final double[] tprs;
    private final double[] thresholds_rev;
    private final double[] fprs_rev;
    private final double[] tprs_rev;
    private final boolean fpr_asc;
    private final boolean tpr_asc;

    public ResultInterpreter(ROC roc) {
        thresholds = roc.getDefaultThresholds();
        thresholds_rev = reverse(thresholds);
        double[][] tpr_fpr = roc.roc(thresholds);
        fprs = tpr_fpr[1];
        fprs_rev = reverse(fprs);
        fpr_asc = isAscend(fprs);
        if (roc.getP()>0) {
            tprs = tpr_fpr[0];
            tprs_rev = reverse(tprs);
            tpr_asc = isAscend(tprs);
        } else {
            tprs = null;
            tprs_rev = null;
            tpr_asc = true; // meaningless
        }
    }

    public boolean tprAvailable() {
        return tprs!=null;
    }

    private double[] reverse(double[] ds) {
        int n= ds.length;
        double[] res = new double[n];
        for (int i=0; i<n; i++) {
            res[i] = ds[n-1-i];
        }
        return res;
    }

    public double getTPR(double threshold) {
        if (tprs==null)
            return -1.0;
        return map(threshold, thresholds, tprs);
    }

    public double getFPR(double threshold) {
        return map(threshold, thresholds, fprs);
    }

    public double getThreshold(double x, boolean tprOrFpr) {
        if (tprOrFpr) {
            if (tprs==null)
                return Double.NaN;

            if (tpr_asc)
                return map(x, tprs, thresholds);
            else
                return map(x, tprs_rev, thresholds_rev);
        } else {
            if (fpr_asc)
                return map(x, fprs, thresholds);
            else
                return map(x, fprs_rev, thresholds_rev);
        }

    }

    private double map(double x, double[] xs, double[] ys) {
        int n = xs.length;
        int idx = Arrays.binarySearch(xs, x);
        if (idx>=0) {
            int left = idx-1;
            int right = idx+1;
            while (left>=0 && xs[left]==x)
                left--;
            while (right<n && xs[right]==x)
                right++;
            return ys[(right+left)/2];
        } else {
            idx = -idx-1;
            if (idx==0) {
                return ys[0];
            } else if (idx==n) {
                return ys[n-1];
            } else {
                double x1 = xs[idx-1];
                double y1 = ys[idx-1];
                double x2 = xs[idx];
                double y2 = ys[idx];
                return (x-x1)*(y2-y1)/(x2-x1)+y1;
            }
        }
    }

    private boolean isAscend(double[] xs) {
        boolean asc = true;
        int n = xs.length;
        if (n>1) {
            for (int i=0; i<n; i++) {
                if (xs[i]<xs[i+1]) break; // ascend
                if (xs[i]>xs[i+1]) { // descend
                    asc = false;
                    break;
                }
            }
        }

        return asc;
    }

    public double[][] getOriginalROC() {
        if (tprs==null) {
            return new double[][]{fprs,thresholds};
        }

        return new double[][]{fprs,tprs,thresholds};
    }

    public double[][] getROCAtIntegralFprPercentage() {
        if (tprs==null) {
            return null;
        }

        double[][] res = new double[3][101];
        for (int i=0; i<=100; i++) {
            double fpr = i/100.0;
            double th = getThreshold(fpr, false);
            double tpr = getTPR(th);
            res[0][i] = fpr;
            res[1][i] = tpr;
            res[2][i] = th;
        }
        return res;
    }

    public double auc() {
        if (tprs==null) {
            return 0;
        }

        double area = 0;

        int n = tprs.length;
        for (int i=0; i<n-1; i++) {
            area += 0.5*(tprs[i]+tprs[i+1])*(fprs[i]-fprs[i+1]);
        }

        return area;
    }
}
