/**
 * Copyright 2011, Felix Palmer
 * <p/>
 * Licensed under the MIT license:
 * http://creativecommons.org/licenses/MIT/
 */

package com.mu_777.audiolization_project.types;

// Data class to explicitly indicate that these bytes are the FFT of audio data
public class FFTData {
    public FFTData(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] bytes;

    public double getMaxFrequency(double samplingRateHz) {
        int size = bytes.length;
        double[] dbfs = new double[size / 2];
        int max_i = 0;
        double max_db = -120d;
        for (int i = 0; i < size; i += 2) {
            dbfs[i / 2] = (int) (20 * Math.log10(Math.sqrt(Math.pow(bytes[i], 2) + Math.pow(bytes[i + 1], 2))));
            if (max_db < dbfs[i / 2]) {
                max_db = dbfs[i / 2];
                max_i = i / 2;
            }
        }
        double resolution = samplingRateHz / (double) size;
        return resolution * max_i;
    }
}