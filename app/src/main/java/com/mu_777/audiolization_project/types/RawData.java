/**
 * Copyright 2011, Felix Palmer
 * <p/>
 * Licensed under the MIT license:
 * http://creativecommons.org/licenses/MIT/
 */

package com.mu_777.audiolization_project.types;

// Data class to explicitly indicate that these bytes are the FFT of audio data
public class RawData {
    public RawData(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] bytes;
}