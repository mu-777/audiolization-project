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

        // AudioRecord�̍쐬
        audioRec = new AudioRecord(MediaRecorder.AudioSource.MIC,
                SAMPLING_RATE, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufSize * 2);
        audioRec.startRecording();
        bIsRecording = true;

        //�t�[���G��̓X���b�h
        fft = new Thread(new Runnable() {
            @Override
            public void run() {
//                byte buf[] = new byte[bufSize * 2];
//                while (bIsRecording) {
//                    audioRec.read(buf, 0, buf.length);
//
//                    //�G���f�B�A���ϊ�
//                    //FFT4g�ɂ̓r�b�O�G���f�B�A���œ���Ȃ��Ƃ����Ȃ�
//                    ByteBuffer bf = ByteBuffer.wrap(buf);
//                    bf.order(ByteOrder.LITTLE_ENDIAN);
//                    short[] s = new short[bufSize];
//                    for (int i = bf.position(); i < bf.capacity() / 2; i++) {
//                        s[i] = bf.getShort();
//                    }
//
//                    //FFT�N���X�̍쐬�ƒl�̈����n��
//                    FFT4g fft = new FFT4g(FFT_SIZE);
//                    double[] FFTdata = new double[FFT_SIZE];
//                    for (int i = 0; i < FFT_SIZE; i++) {
//                        FFTdata[i] = (double) s[i];
//                    }
//                    fft.rdft(1, FFTdata);
//
//                    // �f�V�x���̌v�Z
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
//                    //���ʂ��ő�̎��g���ƁC���̉��ʂ�\��
//                    Log.d(TAG, "frequency:" + resol * max_i + "[Hz]   volume: " + max_db + "[dB]");
//                }
//                // �^����~
//                audioRec.stop();
//                audioRec.release();
                while (bIsRecording) {
                    double[] accelDataList;
                    accelDataList = mAccelerometerManager.getAccelerometerDataList();
//                    Log.d(TAG, Double.toString(accelDataList[0]));

                }

            }
        });
        //�X���b�h�̃X�^�[�g
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
