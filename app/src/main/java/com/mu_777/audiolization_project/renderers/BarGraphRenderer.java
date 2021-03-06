/**
 * Copyright 2011, Felix Palmer
 * <p/>
 * Licensed under the MIT license:
 * http://creativecommons.org/licenses/MIT/
 */
package com.mu_777.audiolization_project.renderers;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.mu_777.audiolization_project.types.FFTData;
import com.mu_777.audiolization_project.types.RawData;


public class BarGraphRenderer extends Renderer {
    private static final String TAG = "BarGraphRenderer";

    private int mDivisions;
    private Paint mPaint;
    private boolean mTopFlag;
    private boolean mRawFlag = false;

    /**
     * Renders the FFT data as a series of lines, in histogram form
     *
     * @param divisions - must be a power of 2. Controls how many lines to draw
     * @param paint     - Paint to draw lines with
     * @param top       - whether to draw the lines at the top of the canvas, or the bottom
     */
    public BarGraphRenderer(int divisions, Paint paint, boolean top, boolean raw) {
        super();
        mDivisions = divisions;
        mPaint = paint;
        mTopFlag = top;
        mRawFlag = raw;
    }

    @Override
    public void onRender(Canvas canvas, RawData data, Rect rect) {
        if (!mRawFlag) {
            return;
        }
        for (int i = 0; i < data.bytes.length / mDivisions; i++) {
            mPoints[i * 4] = i * 4 * mDivisions;
            mPoints[i * 4 + 2] = i * 4 * mDivisions;
            int height = (int) Math.abs(data.bytes[mDivisions * i]);
            Log.d(TAG, String.valueOf(height));
            if (mTopFlag) {
                mPoints[i * 4 + 1] = 0;
                mPoints[i * 4 + 3] = height;
            } else {
                mPoints[i * 4 + 1] = rect.height();
                mPoints[i * 4 + 3] = rect.height() - height;
            }
        }

        canvas.drawLines(mPoints, mPaint);
    }

    @Override
    public void onRender(Canvas canvas, FFTData data, Rect rect) {
        if (mRawFlag) {
            return;
        }
        for (int i = 0; i < data.bytes.length / mDivisions; i++) {
            mFFTPoints[i * 4] = i * 4 * mDivisions;
            mFFTPoints[i * 4 + 2] = i * 4 * mDivisions;
            byte rfk = data.bytes[mDivisions * i];
            byte ifk = data.bytes[mDivisions * i + 1];
            float magnitude = (rfk * rfk + ifk * ifk);
            int dbValue = (int) (10 * Math.log10(magnitude));

            if (mTopFlag) {
                mFFTPoints[i * 4 + 1] = 0;
                mFFTPoints[i * 4 + 3] = (dbValue * 2 - 10);
            } else {
                mFFTPoints[i * 4 + 1] = rect.height();
                mFFTPoints[i * 4 + 3] = rect.height() - (dbValue * 2 - 10);
            }
        }

        canvas.drawLines(mFFTPoints, mPaint);
    }
}
