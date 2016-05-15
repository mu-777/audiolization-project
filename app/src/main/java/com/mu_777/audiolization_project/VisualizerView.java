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
    Bitmap mCanvasBitmap;
    Canvas mCanvas;
    boolean mFlash = false;

    private SensorManager mSensorManager;
    private AccelerometerManager mAccelerometerManager;

    private int mDataSize = 256;

    private SoundGenerator mSoundGenerator;


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

        mSoundGenerator = new SoundGenerator(mDataSize);
        mSoundGenerator.init();
    }

    public void linkToSensor(SensorManager sensorMgr) {
        mSensorManager = sensorMgr;
        mAccelerometerManager = new AccelerometerManager(mSensorManager, mDataSize);
    }

    public void addRenderer(Renderer renderer) {
        if (renderer != null) {
            mRenderers.add(renderer);
        }
    }

    public void clearRenderers() {
        mRenderers.clear();
    }

    public void release() {
        mAccelerometerManager.stopSensor(mSensorManager);
    }

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

    private void fadeOldGraphs() {
        // Fade out old contents
        mCanvas.drawPaint(mFadePaint);
    }

    private void playSound() {
        VibrationData vibrationData = mAccelerometerManager.getVibrationData();
//        RawData rawData = vibrationData.getRawData();
//        mSoundGenerator.playSoundBytes(rawData.bytes);

        FFTData fftData = vibrationData.getFFTData();
        mSoundGenerator.playSoundFrequency(fftData.getMaxFrequency(mAccelerometerManager.getSamplingRateHz()));
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
        playSound();
        fadeOldGraphs();


        if (mFlash) {
            mFlash = false;
            mCanvas.drawPaint(mFlashPaint);
        }

        canvas.drawBitmap(mCanvasBitmap, new Matrix(), null);
    }
}