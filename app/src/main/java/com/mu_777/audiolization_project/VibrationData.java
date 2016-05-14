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
    private boolean TEST_FLAG = false;

    public VibrationData() {
        init(0.0);
    }

    public VibrationData(int maxDataSize) {
        mMaxSize = maxDataSize;
        init(0.0);
    }

    private void init(double initVal) {
        int size = mData.size();
        for (int i = 0; i < mMaxSize - size; i++) {
            if (TEST_FLAG) {
                mData.add(getTestData(i));
            } else {
                mData.add(initVal);
            }
        }
    }

    public boolean add(double newData) {
        if (TEST_FLAG) {
            return true;
        }
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

    public int getDataSize() {
        return mData.size();
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

    private double getTestData(int idx) {
        double a = 10.0;
        double f = 2.0;
        double ret = a * Math.sin(((double) idx / (double) mMaxSize) * 2.0 * Math.PI * f);
        return ret;
    }
}
