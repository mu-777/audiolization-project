package com.mu_777.audiolization_project;

import com.mu_777.audiolization_project.fft.FFT4g2;
import com.mu_777.audiolization_project.types.FFTData;
import com.mu_777.audiolization_project.types.RawData;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by ryosuke on 2016/05/07.
 */
public class VibrationData {

    private static final String TAG = "VibrationData";

    private FFT4g2 mFft;
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
        mFft = new FFT4g2(mMaxSize);

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

    public RawData getRawData() {
        int size = getDataSize();
        byte[] bytes = new byte[size];
        for (int i = 1; i < size; i++) {
            bytes[i] = mData.get(i).byteValue();
        }
        return new RawData(bytes);
    }

    public FFTData getFFTData() {
        Double[] fftData = mData.toArray(new Double[0]);
        mFft.rdft(1, fftData);
        int size = fftData.length;
        byte[] bytes = new byte[size];
        for (int i = 1; i < size; i++) {
            bytes[i] = fftData[i].byteValue();
        }

        return new FFTData(bytes);
    }

    private double filter(double newData) {
        return newData;
    }

    private double getTestData(int idx) {
        double a = 10.0;
        double f = 2.0;
        return a * Math.sin(((double) idx / (double) mMaxSize) * 2.0 * Math.PI * f);
    }
}
