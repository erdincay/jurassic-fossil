/*
 * Copyright (c) 2010-2011 Jurassic Michele Marcon. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of Jurassic Michele Marcon nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jurassic;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.Timeline.RepeatBehavior;
import org.pushingpixels.trident.ease.Spline;
import org.pushingpixels.trident.interpolator.KeyFrames;
import org.pushingpixels.trident.interpolator.KeyTimes;
import org.pushingpixels.trident.interpolator.KeyValues;

/**
 *
 * @author mmarcon
 */
public class EvolvingDNA {

    int time = 0;
    int pos = 0;
    float opacity = 0;
    boolean isRollover = false;
    boolean isA = false;
    boolean isB = false;
    boolean isC = false;
    Timeline rolloverTimeline;
    Color colorFG = new Color(0, 200, 0);
    Color colorBG = new Color(0, 200, 0);

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public void setClicked(boolean thisIsClicked) {
        if (thisIsClicked) {
            isA = true;
        } else {
            if (isA) {
                isA = false;
                isB = true;
            } else {
                isB = false;
            }
        }
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setColorFG(Color c) {
        this.colorFG = c;
    }

    public void setColorBG(Color c) {
        this.colorBG = c;
    }

    public void setRollover(boolean isRollover) {
        if (this.isRollover == isRollover) {
            return;
        }
        this.isRollover = isRollover;
        if (this.isRollover) {
            this.rolloverTimeline.play();
        } else {
            rolloverTimeline.playReverse();
        }
    }

    EvolvingDNA(int pos) {
        this.pos = pos;
        Timeline evoTime = new Timeline(this);
        evoTime.addPropertyToInterpolate("time", 0, 100);
        //KeyValues fgValues=KeyValues.create(Color.green,Color.yellow,Color.red);
        KeyValues fgValues = KeyValues.create(new Color(0, 200, 0), Color.green, new Color(0, 200, 0));
        KeyTimes fgTimes = new KeyTimes(0.0f, 0.5f, 1.0f);
        //KeyValues bgValues=KeyValues.create(Color.red,Color.blue,Color.green);
        KeyValues bgValues = KeyValues.create(new Color(0, 200, 0), new Color(0, 100, 0), new Color(0, 200, 0));
        KeyTimes bgTimes = new KeyTimes(0.0f, 0.5f, 1.0f);
        evoTime.addPropertyToInterpolate("colorFG", new KeyFrames(fgValues, fgTimes));
        evoTime.addPropertyToInterpolate("colorBG", new KeyFrames(bgValues, bgTimes));
        evoTime.setDuration(2000);
        evoTime.setInitialDelay(pos * 300);
        evoTime.setEase(new Spline(0.7f));
        evoTime.playLoop(RepeatBehavior.LOOP);
        rolloverTimeline = new Timeline(this);
        rolloverTimeline.addPropertyToInterpolate("opacity", 0.0f, 1.0f);
        rolloverTimeline.setDuration(500);
    }

    public void drawEvolutionStep(Graphics2D g, int scrollPos) {
        int pos = this.pos + scrollPos;
        if (isA) {
            g.setColor(Color.GREEN);
            g.fillRoundRect(0, pos * 30, 1023, 30, 10, 10);
        }
        if (isB) {
            g.setColor(Color.YELLOW);
            g.fillRoundRect(0, pos * 30, 1023, 30, 10, 10);
        }
        if (isC) {
            g.setColor(Color.RED);
            g.fillRoundRect(0, pos * 30, 1023, 30, 10, 10);
        }
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g.setColor(Color.black);
        g.drawRoundRect(0, pos * 30, 1023, 30, 10, 10);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        //g.setPaint(new GradientPaint(time,0,Color.BLACK,100-time,0,Color.white));
        g.setColor(Color.BLACK);
        //g.fillOval(100 - time, pos * 30, 30, 30);
        //g.setColor(Color.BLACK);
        //g.fillRect(15 + time, pos * 30 + 10, 100 - time * 2, 10);
        //g.setColor(Color.red);
        g.setColor(colorBG);
        //g.setPaint(new GradientPaint(15+time,0,colorFG,100-time*2,0,colorBG));
        g.fillRect(15 + time, pos * 30 + 11, 100 - time * 2, 8);
        g.fillRect(115 - time, pos * 30 + 11, time * 2 - 95, 8);
        g.setColor(colorBG);
        g.fillOval(101 - time, pos * 30 + 1, 28, 28);
        //g.setColor(Color.BLACK);
        //g.fillOval(0 + time, pos * 30 + 0, 30, 30);
        g.setColor(colorFG);
        g.fillOval(1 + time, pos * 30 + 1, 28, 28);
    }
}
