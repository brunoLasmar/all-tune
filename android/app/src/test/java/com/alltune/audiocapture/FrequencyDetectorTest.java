/*
package com.alltune.audiocapture;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FrequencyDetectorTest {

    private FrequencyDetector frequencyDetector;
    private static final int SAMPLE_RATE = 44100;

    @Before
    public void setUp() {
        // Inicializa o detector antes de cada teste
        frequencyDetector = new FrequencyDetector();
    }

    /**
     * Gera um buffer de áudio com uma onda senoidal de uma frequência específica.

    private short[] generateSineWave(double frequencyHz, int bufferSize) {
        short[] buffer = new short[bufferSize];
        for (int i = 0; i < bufferSize; i++) {
            double sample = Math.sin(2 * Math.PI * i * frequencyHz / SAMPLE_RATE);
            buffer[i] = (short) (sample * Short.MAX_VALUE);
        }
        return buffer;
    }

    @Test
    public void detectFrequency_For440HzSineWave_ReturnsCorrectFrequency() {
        // Arrange
        double targetFrequency = 440.0; // A nota Lá (A4)
        int bufferSize = 4096; // Um tamanho de buffer comum para FFT
        short[] testBuffer = generateSineWave(targetFrequency, bufferSize);
        double expectedFrequency = 440.0;
        // A resolução da FFT é a taxa de amostragem dividida pelo tamanho do buffer
        // 44100 / 4096 ≈ 10.77 Hz. A margem de erro (delta) deve ser um pouco maior que isso.
        double delta = 11.0;

        // Act
        double detectedFrequency = frequencyDetector.detectFrequency(testBuffer, bufferSize);

        // Assert
        assertEquals("A frequência detectada para 440Hz deve estar próxima do esperado",
                expectedFrequency, detectedFrequency, delta);
    }

    @Test
    public void detectFrequency_ForSilentBuffer_ReturnsZeroOrVeryLowFrequency() {
        // Arrange
        short[] silentBuffer = new short[4096]; // Buffer preenchido com zeros

        // Act
        double detectedFrequency = frequencyDetector.detectFrequency(silentBuffer, silentBuffer.length);

        // Assert
        // Para um buffer de silêncio, o pico de FFT pode ser aleatório, mas geralmente no índice 0 ou 1.
        // É mais seguro verificar se a frequência é muito baixa.
        assertTrue("Para um buffer silencioso, a frequência deve ser muito baixa", detectedFrequency < 20.0);
    }
}
*/