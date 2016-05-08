package com.mu_777.audiolization_project;

import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

//REF: http://tb-lab.hatenablog.jp/entry/2015/02/14/210611


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";

    SensorManager mSensorManager;
    AccelerometerManager mAccelerometerManager;

    boolean mFftProcessingFlag = false;
    int bufSize;
    Thread fft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometerManager = new AccelerometerManager(mSensorManager);

        mFftProcessingFlag = true;

        //フーリエ解析スレッド
        fft = new Thread(new Runnable() {
            @Override
            public void run() {
                while (mFftProcessingFlag) {
                    ArrayList<Double> accelDataList;
                    accelDataList = mAccelerometerManager.getAccelerometerDataList();

                    int dataSize = accelDataList.size();
                    FFT4g2 fft = new FFT4g2(dataSize);
                    Double[] fftData = accelDataList.toArray(new Double[0]);
                    fft.rdft(1, fftData);

                    double[] dbfs = new double[dataSize / 2];
                    double max_db = -120d;
                    int max_i = 0;
                    for (int i = 0; i < dataSize; i += 2) {
                        dbfs[i / 2] = (int) (20 * Math.log10(Math.sqrt(Math.pow(fftData[i], 2)
                                + Math.pow(fftData[i + 1], 2)) / dB_baseline));
                        if (max_db < dbfs[i / 2]) {
                            max_db = dbfs[i / 2];
                            max_i = i / 2;
                        }
                    }

                    //音量が最大の周波数と，その音量を表示
                    Log.d(TAG, "  Size: " + dataSize + "  Top: " + accelDataList.get(0));
//                    Log.d(TAG, "  frequency:" + resol * max_i + "[Hz]  volume: " + max_db + "[dB]");
                }

            }
        });
        //スレッドのスタート
        fft.start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mAccelerometerManager.stopSensor(mSensorManager);
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
}
