package com.mu_777.audiolization_project;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mu_777.audiolization_project.fft.FFT4g2;
import com.mu_777.audiolization_project.renderers.BarGraphRenderer;
import com.mu_777.audiolization_project.renderers.LineRenderer;

import java.util.ArrayList;

//REF: http://tb-lab.hatenablog.jp/entry/2015/02/14/210611


public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private SensorManager mSensorManager;
    private VisualizerView mVisualizerView;

    boolean mFftProcessingFlag = false;
    Thread fft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    protected void onPause() {
        cleanUp();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        cleanUp();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void init() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mVisualizerView = (VisualizerView) findViewById(R.id.visualizerView);
        mVisualizerView.linkToSensor(mSensorManager);
        addBarGraphRenderers();
        addLineRenderer();
    }

    private void cleanUp() {
        mVisualizerView.release();
    }

    // Methods for adding renderers to visualizer
    private void addBarGraphRenderers() {
        Paint paint = new Paint();
        paint.setStrokeWidth(12f);
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(200, 56, 138, 252));
        BarGraphRenderer barGraphRendererBottom = new BarGraphRenderer(4, paint, false, false);
        mVisualizerView.addRenderer(barGraphRendererBottom);

        Paint paint2 = new Paint();
        paint2.setStrokeWidth(12f);
        paint2.setAntiAlias(true);
        paint2.setColor(Color.argb(200, 181, 111, 233));
        BarGraphRenderer barGraphRendererTop = new BarGraphRenderer(4, paint2, true, true);
        mVisualizerView.addRenderer(barGraphRendererTop);
    }

    private void addLineRenderer() {
        Paint linePaint = new Paint();
        linePaint.setStrokeWidth(1f);
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.argb(88, 0, 128, 255));

        Paint lineFlashPaint = new Paint();
        lineFlashPaint.setStrokeWidth(5f);
        lineFlashPaint.setAntiAlias(true);
        lineFlashPaint.setColor(Color.argb(188, 255, 255, 255));
        LineRenderer lineRenderer = new LineRenderer(linePaint, lineFlashPaint, true);
        mVisualizerView.addRenderer(lineRenderer);
    }

    public void barPressed(View view) {
        addBarGraphRenderers();
    }

    public void linePressed(View view) {
        addLineRenderer();
    }

    public void clearPressed(View view) {
        mVisualizerView.clearRenderers();
    }
}
