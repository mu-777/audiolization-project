package com.mu_777.audiolization_project;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ryosuke on 2016/05/07.
 */
public class VibrationData<E> extends ArrayList<E> {

    private int mMaxSize = 1024;

    public VibrationData() {
        init();
    }

    public VibrationData(int maxDataSize) {
        mMaxSize = maxDataSize;
        init();
    }

    private void init() {
    }

    @Override
    public boolean add(E object) {
        checkExpired();
        return super.add(filter(object));
    }

    private void checkExpired() {
        if (super.size() >= mMaxSize) {
            super.removeRange(0, super.size() - mMaxSize);
        }
    }

    public double[] getDataList() {
        double[] ret = new double[mMaxSize];
        Iterator itr = super.iterator();
        for (int i = 0; i < mMaxSize; i++) {
            double val = itr.hasNext() ? (double) itr.next() : 0.0;
            ret[i] = val;
        }
        return ret;
    }

    private E filter(E newData) {
        return newData;
    }
}
