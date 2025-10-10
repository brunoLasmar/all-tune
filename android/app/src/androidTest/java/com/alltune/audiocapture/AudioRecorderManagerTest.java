package com.alltune.audiocapture;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class AudioRecorderManagerTest {

    // Regra para conceder a permissão de gravação durante o teste
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.RECORD_AUDIO);

    @Test
    public void startAndStopRecording_CallsListenerAndStopsSafely() throws InterruptedException {
        // Arrange

        // Usamos um CountDownLatch para esperar a thread de gravação executar o callback
        final CountDownLatch latch = new CountDownLatch(1);

        // Usamos Mockito para criar um "falso" listener e poder verificar seu comportamento
        AudioRecorderManager.AudioDataListener mockListener = mock(AudioRecorderManager.AudioDataListener.class);

        // Capturamos os argumentos passados para o mockListener para fazer asserções sobre eles
        ArgumentCaptor<short[]> bufferCaptor = ArgumentCaptor.forClass(short[].class);
        ArgumentCaptor<Integer> sizeCaptor = ArgumentCaptor.forClass(Integer.class);

        // Quando o método onAudioDataReceived for chamado, nós notificamos o CountDownLatch
        doAnswer(invocation -> {
            latch.countDown(); // Sinaliza que o callback foi chamado
            return null;
        }).when(mockListener).onAudioDataReceived(bufferCaptor.capture(), sizeCaptor.capture());

        AudioRecorderManager recorderManager = new AudioRecorderManager(mockListener);

        // Act
        recorderManager.startRecording();
        org.junit.Assert.assertTrue("O gravador deve estar no estado 'gravando'", recorderManager.isRecording());

        // Espera no máximo 5 segundos para o callback ser chamado
        boolean callbackWasCalled = latch.await(5, TimeUnit.SECONDS);

        recorderManager.stopRecording();

        // Assert
        org.junit.Assert.assertTrue("O callback onAudioDataReceived deveria ter sido chamado", callbackWasCalled);
        org.junit.Assert.assertFalse("O gravador não deve mais estar no estado 'gravando' após parar", recorderManager.isRecording());

        // Verifica se o listener foi chamado pelo menos uma vez.
        verify(mockListener, atLeastOnce()).onAudioDataReceived(any(short[].class), anyInt());

        // Validações adicionais (opcional)
        org.junit.Assert.assertTrue("O tamanho do buffer lido deve ser maior que zero", sizeCaptor.getValue() > 0);
    }
}
