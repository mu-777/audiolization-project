package com.mu_777.audiolization_project;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//REF: http://tb-lab.hatenablog.jp/entry/2015/02/14/210611


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";

    int SAMPLING_RATE = 44100;
    int FFT_SIZE = 4096;
    private double dB_baseline = Math.pow(2, 15) * FFT_SIZE * Math.sqrt(2);
    double resol = ((SAMPLING_RATE / (double) FFT_SIZE));

    SensorManager mSensorManager;
    AccelerometerManager mAccelerometerManager;

    AudioRecord audioRec = null;
    boolean bIsRecording = false;
    int bufSize;
    Thread fft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bufSize = AudioRecord.getMinBufferSize(SAMPLING_RATE,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometerManager = new AccelerometerManager(mSensorManager);

        // AudioRecordの作成
//        audioRec = new AudioRecord(MediaRecorder.AudioSource.MIC,
//                SAMPLING_RATE, AudioFormat.CHANNEL_IN_MONO,
//                AudioFormat.ENCODING_PCM_16BIT, bufSize * 2);
//        audioRec.startRecording();
        bIsRecording = true;

        //フーリエ解析スレッド
        fft = new Thread(new Runnable() {
            @Override
            public void run() {
//                byte buf[] = new byte[bufSize * 2];
//                while (bIsRecording) {
//                    audioRec.read(buf, 0, buf.length);
//
//                    //エンディアン変換
//                    //FFT4gにはビッグエンディアンで入れないといけない
//                    ByteBuffer bf = ByteBuffer.wrap(buf);
//                    bf.order(ByteOrder.LITTLE_ENDIAN);
//                    short[] s = new short[bufSize];
//                    for (int i = bf.position(); i < bf.capacity() / 2; i++) {
//                        s[i] = bf.getShort();
//                    }
//
//                    //FFTクラスの作成と値の引き渡し
//                    FFT4g fft = new FFT4g(FFT_SIZE);
//                    double[] FFTdata = new double[FFT_SIZE];
//                    for (int i = 0; i < FFT_SIZE; i++) {
//                        FFTdata[i] = (double) s[i];
//                    }
//                    fft.rdft(1, FFTdata);
//
//                    // デシベルの計算
//                    double[] dbfs = new double[FFT_SIZE / 2];
//                    double max_db = -120d;
//                    int max_i = 0;
//                    for (int i = 0; i < FFT_SIZE; i += 2) {
//                        dbfs[i / 2] = (int) (20 * Math.log10(Math.sqrt(Math.pow(FFTdata[i], 2)
//                                + Math.pow(FFTdata[i + 1], 2)) / dB_baseline));
//                        if (max_db < dbfs[i / 2]) {
//                            max_db = dbfs[i / 2];
//                            max_i = i / 2;
//                        }
//                    }
//
//                    //音量が最大の周波数と，その音量を表示
//                    Log.d(TAG, "frequency:" + resol * max_i + "[Hz]   volume: " + max_db + "[dB]");
//                }
//                // 録音停止
//                audioRec.stop();
//                audioRec.release();

                while (bIsRecording) {
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
                    Log.d(TAG, "  Size: " + dataSize +"  Top: " + accelDataList.get(0) +"  frequency:" + resol * max_i + "[Hz]  volume: " + max_db + "[dB]");
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
