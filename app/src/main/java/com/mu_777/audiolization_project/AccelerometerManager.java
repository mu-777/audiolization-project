package com.mu_777.audiolization_project;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.List;

/**
 * Created by ryosuke on 2016/05/07.
 * REF: http://tomoima525.hatenablog.com/entry/2014/01/13/152559
 */
public class AccelerometerManager implements SensorEventListener {
    private static final String TAG = "AccelerometerManager";

    private final int DATA_SIZE = 512;
    private VibrationData mVibrationData;

    public AccelerometerManager(SensorManager manager) {
        List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensors.size() > 0) {
            manager.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
        }

        mVibrationData = new VibrationData(DATA_SIZE);
    }

    public void stopSensor(SensorManager manager) {
        if (manager != null) {
            manager.unregisterListener(this);
        }
        manager = null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        mVibrationData.add(event.values[2]);
//        gx = event.values[0];
//        gy = event.values[1];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public double[] getAccelerometerDataList() {
        return mVibrationData.getDataList();
    }
}
