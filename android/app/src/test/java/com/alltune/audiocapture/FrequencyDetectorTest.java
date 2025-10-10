package com.alltune.audiocapture;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyDetectorTest {

    private FrequencyDetector frequencyDetector;
    private static final int SAMPLE_RATE = 44100;

    @Before
    public void setUp() {
        frequencyDetector = new FrequencyDetector();
    }

    /**
     * Gera um buffer de áudio com uma onda senoidal de uma frequência específica.
     * @param frequencyHz A frequência da onda a ser gerada.
     * @param bufferSize O tamanho do buffer.
     * @return Um array de short representando a onda.
     */
    private short[] generateSineWave(double frequencyHz, int bufferSize) {
        short[] buffer = new short[bufferSize];
        for (int i = 0; i < bufferSize; i++) {
            double sample = Math.sin(2 * Math.PI * i * frequencyHz / SAMPLE_RATE);
            // Converte para short (16-bit)
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
        double delta = 15.0; // Margem de erro em Hz. A FFT tem uma resolução limitada.
        // A resolução é SAMPLE_RATE / bufferSize = 44100 / 4096 ≈ 10.7Hz

        // Act
        double detectedFrequency = frequencyDetector.detectFrequency(testBuffer, bufferSize);

        // Assert
        org.junit.Assert.assertEquals("A frequência detectada para 440Hz deve estar próxima do esperado",
                expectedFrequency, detectedFrequency, delta);
    }

    @Test
    public void detectFrequency_ForSilentBuffer_ReturnsZero() {
        // Arrange
        short[] silentBuffer = new short[4096]; // Buffer zerado

        // Act
        double detectedFrequency = frequencyDetector.detectFrequency(silentBuffer, silentBuffer.length);

        // Assert
        org.junit.Assert.assertEquals("Para um buffer silencioso, a frequência deve ser 0", 0.0, detectedFrequency, 0.0);
    }

    // TODO: Adicionar mais testes para outras frequências (e.g., 261.63Hz - Dó central)
    // TODO: Testar com sinais mais complexos (ex: duas senoides somadas) para ver qual é detectada.
}
