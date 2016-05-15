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
                AudioManager.STREAM_MUSIC,  // ���y�X�g���[����ݒ�
                mSampleRate, // �T���v�����[�g
                AudioFormat.CHANNEL_OUT_MONO, // ���m����
                AudioFormat.ENCODING_DEFAULT,   // �I�[�f�B�I�f�[�^�t�H�[�}�b�gPCM16�Ƃ�PCM8�Ƃ�
                mBufferSize, // �o�b�t�@�E�T�C�Y
                AudioTrack.MODE_STREAM); // Stream���[�h�B�f�[�^�������Ȃ���Đ�����
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
        // �Đ������������~���ă����[�X
        if (mAudioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
            mAudioTrack.stop();
            mAudioTrack.release();
        }
    }

    public AudioTrack getAudioTrack() {
        return mAudioTrack;
    }

}
