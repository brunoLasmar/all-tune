package com.alltune.audiocapture;

import androidx.annotation.NonNull;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class AudioCaptureModule extends ReactContextBaseJavaModule {
    private static final String MODULE_NAME = "AudioCapture";
    private AudioRecorderManager recorderManager;
    private final FrequencyDetector frequencyDetector;

    public AudioCaptureModule(@NonNull ReactApplicationContext reactContext) {
        super(reactContext);
        // A dependência do detector de frequência é criada uma vez no construtor.
        this.frequencyDetector = new FrequencyDetector();
    }

    @NonNull
    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @ReactMethod
    public void start() {
        if (recorderManager != null && recorderManager.isRecording()) {
            return; // Já está gravando
        }

        // Define o que fazer quando o gravador nos entregar um buffer de áudio
        AudioRecorderManager.AudioDataListener listener = (buffer, readSize) -> {
            // Delega o cálculo pesado para a classe especialista
            double frequency = frequencyDetector.detectFrequency(buffer, readSize);
            sendFrequencyEvent(frequency);
        };

        // Cria e inicia o gerenciador de gravação, passando o listener
        recorderManager = new AudioRecorderManager(listener);
        recorderManager.startRecording();
    }

    @ReactMethod
    public void stop() {
        if (recorderManager != null) {
            recorderManager.stopRecording();
            recorderManager = null; // Libera a referência para o coletor de lixo
        }
    }

    private void sendFrequencyEvent(double frequency) {
        if (getReactApplicationContext().hasActiveCatalystInstance()) {
            WritableMap params = Arguments.createMap();
            params.putDouble("frequency", frequency);
            getReactApplicationContext()
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("onFrequency", params);
        }
    }
}
