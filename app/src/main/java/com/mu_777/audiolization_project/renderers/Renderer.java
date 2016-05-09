/**
 * Copyright 2011, Felix Palmer
 * <p/>
 * Licensed under the MIT license:
 * http://creativecommons.org/licenses/MIT/
 */
package com.mu_777.audiolization_project.renderers;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.mu_777.audiolization_project.fft.FFTData;


abstract public class Renderer {
    // Have these as members, so we don't have to re-create them each time
    protected float[] mPoints;
    protected float[] mFFTPoints;

    public Renderer() {
    }

    abstract public void onRender(Canvas canvas, FFTData data, Rect rect);

    /**
     * Render the FFT data onto the canvas
     * @param canvas - Canvas to draw on
     * @param data - Data to render
     * @param rect - Rect to render into
     */
    final public void render(Canvas canvas, FFTData data, Rect rect) {
        if (mFFTPoints == null || mFFTPoints.length < data.bytes.length * 4) {
            mFFTPoints = new float[data.bytes.length * 4];
        }

        onRender(canvas, data, rect);
    }
}
