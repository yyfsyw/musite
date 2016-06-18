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

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

import musite.util.Pair;

/**
 *
 * @author Jianjiong Gao
 */
public class GradientSliderUI  extends BasicSliderUI {
    private final JSlider slider;
    private final List<Pair<Double, Color>> scoreColors;
    private double minScore;
    private double maxScore;

    public GradientSliderUI(JSlider slider, TreeMap<Double, Color> mapScoreColor) {
        this(slider, mapScoreColor, 0.0, 1.0);
    }

    public GradientSliderUI(JSlider slider, TreeMap<Double, Color> mapScoreColor,
            double minScore, double maxScore) {
        super(slider);
        if (slider==null||mapScoreColor==null||mapScoreColor.size()<2) {
            throw new java.lang.IllegalArgumentException();
        }

        this.slider = slider;
        
        scoreColors = new ArrayList();
        for (double tick : mapScoreColor.keySet()) {
            if (tick<minScore || tick>maxScore) {
                throw new java.lang.IllegalArgumentException();
            }
            scoreColors.add(new Pair<Double,Color>(tick, mapScoreColor.get(tick)));
        }

        this.minScore = minScore;
        this.maxScore = maxScore;
    }

    public void paintThumb(Graphics g) {
//        Color old = g.getColor();
//        g.setColor(new Color(old.getRed(),old.getGreen(),old.getBlue(),100));
        super.paintThumb(g);
    }

    public void paintTrack(Graphics g) {
        int cy, cw;
        Rectangle trackBounds = trackRect;
        if (slider.getOrientation() == JSlider.HORIZONTAL) {
            Graphics2D g2 = (Graphics2D) g;
            cy = (trackBounds.height / 2) - 2;
            cw = trackBounds.width;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
            g2.translate(trackBounds.x, trackBounds.y + cy);

            //Background color
            g2.setPaint(Color.GRAY);
            g2.fillRect(0, -cy + 5, cw, cy);

            int trackLeft = 0;
            int trackRight = 0;
            trackRight = trackRect.width - 1;

            int middleOfThumb = 0;
            int fillLeft = 0;
            int fillRight = 0;

            // Coordination transformation
            middleOfThumb = thumbRect.x + (thumbRect.width / 2);
            middleOfThumb -= trackRect.x;

            if (!drawInverted()) {
                //fillLeft = !slider.isEnabled() ? trackLeft : trackLeft + 1;
                //fillRight = middleOfThumb;
                fillLeft = middleOfThumb;
                fillRight = trackRight;
            } else {
                fillLeft = middleOfThumb;
                fillRight = !slider.isEnabled() ? trackRight - 1 : trackRight - 2;
            }

            // Gradient for all the slider
            int nColor = scoreColors.size();

            double tickLeft = scoreColors.get(0).getFirst();
            Color colorLeft = scoreColors.get(0).getSecond();
            int areaLeft = (int)(cw*(tickLeft-minScore)/(maxScore-minScore));

            for (int ic=1; ic<nColor; ic++) {
                double tickRight = scoreColors.get(ic).getFirst();
                Color colorRight = scoreColors.get(ic).getSecond();
                int areaRight = (int)(cw*(tickRight-minScore)/(maxScore-minScore));

                Pair<Integer,Integer> lr = intersection(areaLeft,areaRight,fillLeft,fillRight);
                if (lr!=null) {
                    g2.setPaint(new GradientPaint(areaLeft, 0, colorLeft, areaRight, 0, colorRight, false));
                    g2.fillRect(lr.getFirst(), -cy + 5, lr.getSecond()-lr.getFirst()+1, cy);
                }

                tickLeft = tickRight;
                colorLeft = colorRight;
                areaLeft = areaRight;
            }
//
//            g2.setPaint(new GradientPaint(cw/2, 0, Color.BLACK, cw, 0, Color.WHITE, false));
//
//            // fill the region after thumb
//            //g2.fillRect(0, -cy + 5, fillRight - fillLeft, cy);
//            g2.fillRect(fillLeft, -cy + 5, fillRight - fillLeft+1, cy);
//
//            g2.setPaint(new GradientPaint(0, 0, Color.BLUE, cw/2, 0, Color.RED, false));
//            g2.fillRect(0, -cy + 5, fillLeft, cy);

            // background color
            g2.setPaint(slider.getBackground());
            g2.fillRect(10, 10, cw, 5);

//            g2.setPaint(Color.WHITE);
//            g2.drawLine(0, cy, cw - 1, cy);

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_OFF);
            g2.translate(-trackBounds.x, -(trackBounds.y + cy));
        } else {
            super.paintTrack(g);
        }
    }

    @Override
    protected void paintHorizontalLabel(Graphics g, int value, Component label) {
        super.paintHorizontalLabel(g, value, label);
    }

    @Override
    protected void paintVerticalLabel(Graphics g, int value, Component label) {
        super.paintVerticalLabel(g, value, label);
    }

    private Pair<Integer,Integer> intersection(int left1, int right1, int left2, int right2) {
        if (left1>=right2 || left2>=right1)
            return null;

        return new Pair(Math.max(left1, left2),Math.min(right1, right2));
    }
}