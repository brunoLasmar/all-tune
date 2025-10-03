package com.tunerapp;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class AudioCaptureModule extends ReactContextBaseJavaModule {
    private static final String MODULE_NAME = "AudioCapture";

    private static final int SAMPLE_RATE = 44100;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord audioRecord;
    private boolean isRecording = false;
    private Thread recordingThread;

    public AudioCaptureModule(@NonNull ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @NonNull
    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @ReactMethod
    public void start() {
        if (isRecording) return;

        int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, bufferSize);

        audioRecord.startRecording();
        isRecording = true;

        recordingThread = new Thread(() -> {
            short[] buffer = new short[bufferSize];
            while (isRecording) {
                int read = audioRecord.read(buffer, 0, buffer.length);
                if (read > 0) {
                    double frequency = detectFrequency(buffer, read);
                    sendFrequencyEvent(frequency);
                }
            }
        }, "AudioRecorder Thread");

        recordingThread.start();
    }

    @ReactMethod
    public void stop() {
        if (!isRecording) return;
        isRecording = false;

        try {
            if (audioRecord != null) {
                audioRecord.stop();
                audioRecord.release();
                audioRecord = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (recordingThread != null) {
            recordingThread.interrupt();
            recordingThread = null;
        }
    }

    private void sendFrequencyEvent(double frequency) {
        WritableMap params = Arguments.createMap();
        params.putDouble("frequency", frequency);
        getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit("onFrequency", params);
    }

    /** FFT simples para detectar frequência dominante */
    private double detectFrequency(short[] buffer, int read) {
        int n = read;
        double[] real = new double[n];
        double[] imag = new double[n];

        for (int i = 0; i < n; i++) {
            real[i] = buffer[i];
            imag[i] = 0.0;
        }

        fft(real, imag);

        double maxMag = -1;
        int maxIndex = -1;
        for (int i = 0; i < n / 2; i++) {
            double mag = real[i] * real[i] + imag[i] * imag[i];
            if (mag > maxMag) {
                maxMag = mag;
                maxIndex = i;
            }
        }

        return maxIndex * SAMPLE_RATE / (double) n;
    }

    // Implementação simples de FFT Cooley–Tukey
    private void fft(double[] real, double[] imag) {
        int n = real.length;
        if (n <= 1) return;

        double[] evenReal = new double[n / 2];
        double[] evenImag = new double[n / 2];
        double[] oddReal = new double[n / 2];
        double[] oddImag = new double[n / 2];

        for (int i = 0; i < n / 2; i++) {
            evenReal[i] = real[2 * i];
            evenImag[i] = imag[2 * i];
            oddReal[i] = real[2 * i + 1];
            oddImag[i] = imag[2 * i + 1];
        }

        fft(evenReal, evenImag);
        fft(oddReal, oddImag);

        for (int k = 0; k < n / 2; k++) {
            double cos = Math.cos(-2 * Math.PI * k / n);
            double sin = Math.sin(-2 * Math.PI * k / n);

            double tReal = cos * oddReal[k] - sin * oddImag[k];
            double tImag = sin * oddReal[k] + cos * oddImag[k];

            real[k] = evenReal[k] + tReal;
            imag[k] = evenImag[k] + tImag;
            real[k + n / 2] = evenReal[k] - tReal;
            imag[k + n / 2] = evenImag[k] - tImag;
        }
    }
}
