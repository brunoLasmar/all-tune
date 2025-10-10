package com.alltune.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import kotlin.concurrent.thread

class AudioEmitterModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    private val SAMPLE_RATE = 44100

    override fun getName() = "AudioEmitter"

    @ReactMethod
    fun playSound(frequency: Double, durationInMillis: Int, promise: Promise) {
        // executa tudo em uma thread separada para não travar a interface do app
        thread {
            try {
                val numSamples = durationInMillis * SAMPLE_RATE / 1000
                val buffer = ShortArray(numSamples)

                // calcula cada ponto da onda sonora para a frequência (frequency) desejada e o converte para um formato de áudio digital
                for (i in 0 until numSamples) {
                    val angle = 2.0 * Math.PI * i / (SAMPLE_RATE / frequency)
                    val sample = (Math.sin(angle) * 32767).toInt().toShort()
                    buffer[i] = sample
                }

                // configura o player de áudio nativo (AudioTrack)
                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(SAMPLE_RATE)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(buffer.size * 2) // 2 bytes por Short
                    .build()

                // toca o som e libera os recursos
                audioTrack.play()
                audioTrack.write(buffer, 0, numSamples)
                audioTrack.stop()
                audioTrack.release()

                // retorna sucesso para o JS
                promise.resolve("Som gerado e tocado com sucesso!")

            } catch (e: Exception) {
                promise.reject("AUDIO_ERROR", "Falha ao gerar o som", e)
            }
        }
    }
}
