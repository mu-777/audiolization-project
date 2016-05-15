package com.mu_777.audiolization_project;

/**
 * Copyright 2011, Felix Palmer
 * <p/>
 * Licensed under the MIT license:
 * http://creativecommons.org/licenses/MIT/
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.hardware.SensorManager;
import android.media.audiofx.Visualizer;
import android.util.AttributeSet;
import android.view.View;

import com.mu_777.audiolization_project.fft.FFT4g2;
import com.mu_777.audiolization_project.types.FFTData;
import com.mu_777.audiolization_project.renderers.Renderer;
import com.mu_777.audiolization_project.types.RawData;
import com.mu_777.audiolization_project.MyApplication;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * A class that draws visualizations of data received from a
 * {@link Visualizer.OnDataCaptureListener#onWaveFormDataCapture } and
 * {@link Visualizer.OnDataCaptureListener#onFftDataCapture }
 */
public class VisualizerView extends View {
    private static final String TAG = "VisualizerView";

    private Rect mRect = new Rect();
    private Set<Renderer> mRenderers;

    private Paint mFlashPaint = new Paint();
    private Paint mFadePaint = new Paint();

    private SensorManager mSensorManager;
    private AccelerometerManager mAccelerometerManager;

    Bitmap mCanvasBitmap;
    Canvas mCanvas;

    boolean mFlash = false;

    public VisualizerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        init();
    }

    public VisualizerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VisualizerView(Context context) {
        this(context, null, 0);
    }

    private void init() {
        mFlashPaint.setColor(Color.argb(122, 255, 255, 255));
        mFadePaint.setColor(Color.argb(238, 255, 255, 0)); // Adjust alpha to change how quickly the image fades
        mFadePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));

        mRenderers = new HashSet<Renderer>();
    }

    public void linkToSensor(SensorManager sensorMgr) {
        mSensorManager = sensorMgr;

        mAccelerometerManager = new AccelerometerManager(mSensorManager);
    }

    public void addRenderer(Renderer renderer) {
        if (renderer != null) {
            mRenderers.add(renderer);
        }
    }

    public void clearRenderers() {
        mRenderers.clear();
    }

    /**
     * Call to release the resources used by VisualizerView. Like with the
     * MediaPlayer it is good practice to call this method
     */
    public void release() {
        mAccelerometerManager.stopSensor(mSensorManager);
    }

    /**
     * Call this to make the visualizer flash. Useful for flashing at the start
     * of a song/loop etc...
     */
    public void flash() {
        mFlash = true;
        invalidate();
    }

    private void renderGraphs() {
        VibrationData vibrationData = mAccelerometerManager.getVibrationData();

        RawData rawData = vibrationData.getRawData();
        invalidate();
        for (Renderer r : mRenderers) {
            r.render(mCanvas, rawData, mRect);
        }

        FFTData fftData = vibrationData.getFFTData();
        invalidate();
        for (Renderer r : mRenderers) {
            r.render(mCanvas, fftData, mRect);
        }
    }

    private void fadeOldGraphs(){
        // Fade out old contents
        mCanvas.drawPaint(mFadePaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Create canvas once we're ready to draw
        mRect.set(0, 0, getWidth(), getHeight());

        if (mCanvasBitmap == null) {
            mCanvasBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        }
        if (mCanvas == null) {
            mCanvas = new Canvas(mCanvasBitmap);
        }

        renderGraphs();
        fadeOldGraphs();


        if (mFlash) {
            mFlash = false;
            mCanvas.drawPaint(mFlashPaint);
        }

        canvas.drawBitmap(mCanvasBitmap, new Matrix(), null);
    }
}