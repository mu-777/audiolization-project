package com.mu_777.audiolization_project;

import android.app.Application;

/**
 * Created by ryosuke on 2016/05/15.
 */
public class MyApplication extends Application {
    private static int mAccelDataSize = 1024;

    @Override
    public void onCreate() {
    }

    @Override
    public void onTerminate() {
    }

    public int getAccelDataSize() {
        return this.mAccelDataSize;
    }

    public void setAccelDataSize(int value) {
        this.mAccelDataSize = value;
    }


}
