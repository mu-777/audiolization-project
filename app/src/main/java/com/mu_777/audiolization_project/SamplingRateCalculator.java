package com.mu_777.audiolization_project;

import java.util.ArrayList;

/**
 * Created by ryosuke on 2016/05/15.
 */
public class SamplingRateCalculator {

    private int mSamplingRateCalcSize;
    private long mPrevTime = 0l;
    private ArrayList<Long> mDiffTimeArr = new ArrayList<Long>();

    public SamplingRateCalculator() {
        this(1000);
    }

    public SamplingRateCalculator(int size) {
        mSamplingRateCalcSize = size;
    }

    public void update() {
        if (mPrevTime == 0l) {
            mPrevTime = System.currentTimeMillis();
        }
        for (int i = mDiffTimeArr.size(); i > mSamplingRateCalcSize - 1; i--) {
            mDiffTimeArr.remove(0);
        }

        long currentTime = System.currentTimeMillis();
        mDiffTimeArr.add(currentTime - mPrevTime);
        mPrevTime = currentTime;
    }

    public double getSamplingRateHz() {
        int size = mDiffTimeArr.size();
        long sum = 0l;
        for (int i = 0; i < size; i++) {
            sum += mDiffTimeArr.get(i);
        }
        return 1000.0 / (double) (sum / size);
    }
}
