package com.mu_777.audiolization_project;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by ryosuke on 2016/05/07.
 */
public class VibrationData {

    private static final String TAG = "VibrationData";

    private ArrayList<Double> mData = new ArrayList<Double>();
    private int mMaxSize = 1024;

    public VibrationData() {
        super();
        init();
    }

    public VibrationData(int maxDataSize) {
        super();
        mMaxSize = maxDataSize;
        init();
    }

    private void init() {
    }

    public boolean add(double newData) {
        checkExpired();
        return mData.add(filter(newData));
    }

    private void checkExpired() {
        while (mData.size() >= mMaxSize) {
            mData.remove(0);
        }
    }

    public ArrayList<Double> getDataArray() {
        return mData;
    }

    public double[] getDataList() {
        double[] ret = new double[mMaxSize];
        Iterator<Double> itr = mData.iterator();

        for (int i = 0; i < mMaxSize; i++) {
            Log.d(TAG, Boolean.toString(itr.hasNext()));
            double val = itr.hasNext() ? itr.next() : 0.0;
            ret[i] = val;
        }
        return ret;
    }

    private double filter(double newData) {
        return newData;
    }
}
