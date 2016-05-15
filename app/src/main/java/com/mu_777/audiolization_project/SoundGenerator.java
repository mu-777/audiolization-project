package com.mu_777.audiolization_project;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.audiofx.AudioEffect;

/**
 * Created by ryosuke on 2016/05/15.
 */

// Ref http://dev.classmethod.jp/smartphone/andoid_sound_generator_xmas/

public class SoundGenerator {

    private AudioTrack mAudioTrack;
    private int mSampleRate;
    private int mBufferSize;

    public SoundGenerator(int bufferSize) {
        this(44000, bufferSize);
    }

    public SoundGenerator(int sampleRate, int bufferSize) {
        mSampleRate = sampleRate;
        mBufferSize = bufferSize;

        mAudioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC,  // 音楽ストリームを設定
                mSampleRate, // サンプルレート
                AudioFormat.CHANNEL_OUT_MONO, // モノラル
                AudioFormat.ENCODING_DEFAULT,   // オーディオデータフォーマットPCM16とかPCM8とか
                mBufferSize, // バッファ・サイズ
                AudioTrack.MODE_STREAM); // Streamモード。データを書きながら再生する
    }

    public void playSoundBytes(byte[] bytes) {
        mAudioTrack.write(bytes, 0, bytes.length);
    }

    public void playSoundFrequency(double freq) {
        byte[] buffer = new byte[(int) Math.ceil(mBufferSize)];
        for (int i = 0; i < buffer.length; i++) {
            double wave = i / (mSampleRate / freq) * (Math.PI * 2);
            wave = Math.sin(wave);
            buffer[i] = (byte) (wave > 0.0 ? Byte.MAX_VALUE : Byte.MIN_VALUE);
        }
        mAudioTrack.write(buffer, 0, mBufferSize);
    }

    public void init() {
        startSound();
    }

    public void startSound() {
        mAudioTrack.play();
    }

    public void stopSound() {
        mAudioTrack.pause();
    }

    public void destroy() {
        // 再生中だったら停止してリリース
        if (mAudioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
            mAudioTrack.stop();
            mAudioTrack.release();
        }
    }

    public AudioTrack getAudioTrack() {
        return mAudioTrack;
    }

}
