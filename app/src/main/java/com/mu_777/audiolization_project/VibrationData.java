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
    private final boolean TEST_FLAG = true;


    public VibrationData() {
    }

    public VibrationData(int maxDataSize) {
        mMaxSize = maxDataSize;
    }

    private void init(double initVal) {
        int size = mData.size();
        for (int i = 0; i < mMaxSize - size; i++) {
            if (TEST_FLAG) {
                mData.add(getTestValue(i));
            } else {
                mData.add(initVal);
            }
        }
    }

    public boolean add(double newData) {
        if (mData.size() < mMaxSize) {
            init(filter(newData));
        }
        if (TEST_FLAG) {
            return true;
        }
        checkExpired();
        return mData.add(filter(newData));
    }

    private void checkExpired() {
        mData.remove(0);
    }

    public ArrayList<Double> getDataArray() {
        return mData;
    }

    public double[] getDataList() {
        double[] ret = new double[mMaxSize];
        Iterator<Double> itr = mData.iterator();

        for (int i = 0; i < mMaxSize; i++) {
            double val = itr.hasNext() ? itr.next() : 0.0;
            ret[i] = val;
        }
        return ret;
    }

    private double filter(double newData) {
        return newData;
    }

    private double getTestValue(int idx) {
        double a = 10.0;
        double f = 1.0;
        return a * Math.sin((idx / mMaxSize) * (2.0 * Math.PI / f));
    }

}
