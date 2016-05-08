package com.mu_777.audiolization_project;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryosuke on 2016/05/07.
 * REF: http://tomoima525.hatenablog.com/entry/2014/01/13/152559
 */
public class AccelerometerManager implements SensorEventListener {
    private static final String TAG = "AccelerometerManager";

    private final int DATA_SIZE = 256;
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

    private double filter(float[] vals) {
        float gx = vals[0];
        float gy = vals[1];
        float gz = vals[2];
        return Math.sqrt(gx * gx + gy * gy + gz * gz);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        mVibrationData.add(filter(event.values));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public ArrayList<Double> getAccelerometerDataList() {
        return mVibrationData.getDataArray();
    }
}
