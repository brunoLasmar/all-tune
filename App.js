import React, { useState } from 'react';
import {
  SafeAreaView,
  StyleSheet,
  Text,
  View,
  TouchableOpacity,
  StatusBar
} from 'react-native';

// Lista de instrumentos que vamos suportar
const INSTRUMENTOS = ['Guitarra', 'Violão', 'Flauta', 'Baixo'];

const App = () => {
  // Estado para guardar qual instrumento está selecionado
  const [instrumentoSelecionado, setInstrumentoSelecionado] = useState('Guitarra');

  // Estado para a nota detectada (simulada por enquanto)
  const [notaAtual, setNotaAtual] = useState('E');

  // Estado para a afinação: -1 (muito baixo), 0 (afinado), 1 (muito alto)
  // Vamos simular isso também
  const [afinacao, setAfinacao] = useState(0);

  // Função para simular a detecção de uma nova nota (para teste)
  const simularDeteccao = (afinacaoValor) => {
    const notas = ['A', 'B', 'C', 'D', 'E', 'F', 'G'];
    setNotaAtual(notas[Math.floor(Math.random() * notas.length)]);
    setAfinacao(afinacaoValor);
  };

  // Função para renderizar os botões de seleção de instrumento
  const renderizarBotoesInstrumento = () => {
    return INSTRUMENTOS.map((instrumento) => (
      <TouchableOpacity
        key={instrumento}
        style={[
          styles.botaoInstrumento,
          instrumentoSelecionado === instrumento && styles.botaoInstrumentoSelecionado,
        ]}
        onPress={() => setInstrumentoSelecionado(instrumento)}
      >
        <Text style={styles.textoBotaoInstrumento}>{instrumento}</Text>
      </TouchableOpacity>
    ));
  };

  return (
    <SafeAreaView style={styles.container}>
      {/* Barra de status com texto claro */}
      <StatusBar barStyle="light-content" />

      {/* Título do App */}
      <Text style={styles.tituloApp}>All-Tune Afinador</Text>

      {/* Contêiner dos botões de instrumento */}
      <View style={styles.seletorInstrumentos}>
        {renderizarBotoesInstrumento()}
      </View>

      {/* Visor Principal do Afinador */}
      <View style={styles.visorContainer}>
        <Text style={styles.textoNota}>{notaAtual}</Text>
        <Text
          style={[
            styles.textoAfinacao,
            afinacao < 0 && styles.textoBaixo,
            afinacao > 0 && styles.textoAlto,
          ]}
        >
          {afinacao < 0 ? 'Muito Baixo' : afinacao > 0 ? 'Muito Alto' : 'Afinado!'}
        </Text>

        {/* Barra Indicadora de Afinação */}
        <View style={styles.indicadorBarraContainer}>
          <View
            style={[
              styles.indicadorPonto,
              { transform: [{ translateX: afinacao * 60 }] } // Desloca o ponto
            ]}
          />
          <View style={styles.indicadorCentro} />
        </View>
      </View>

      {/* Controles de Simulação (para teste, pode remover depois) */}
      <View style={styles.simulacaoContainer}>
        <TouchableOpacity style={styles.botaoSimulacao} onPress={() => simularDeteccao(-1)}>
          <Text style={styles.textoBotaoInstrumento}>Simular Baixo</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.botaoSimulacao} onPress={() => simularDeteccao(0)}>
          <Text style={styles.textoBotaoInstrumento}>Simular Afinado</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.botaoSimulacao} onPress={() => simularDeteccao(1)}>
          <Text style={styles.textoBotaoInstrumento}>Simular Alto</Text>
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );
};

// --- ESTILOS ---
const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#121212',
    alignItems: 'center',
    paddingTop: 40,
  },
  tituloApp: {
    fontSize: 28,
    fontWeight: 'bold',
    color: '#FFFFFF',
    marginBottom: 20,
  },
  seletorInstrumentos: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    width: '100%',
    paddingHorizontal: 10,
    marginBottom: 40,
  },
  botaoInstrumento: {
    paddingVertical: 10,
    paddingHorizontal: 15,
    borderRadius: 20,
    backgroundColor: '#333333',
  },
  botaoInstrumentoSelecionado: {
    backgroundColor: '#1DB954', // Verde Spotify para destaque
  },
  textoBotaoInstrumento: {
    color: '#FFFFFF',
    fontWeight: '600',
  },
  visorContainer: {
    width: 280,
    height: 280,
    borderRadius: 140, // Círculo perfeito
    backgroundColor: '#1E1E1E',
    justifyContent: 'center',
    alignItems: 'center',
    borderWidth: 4,
    borderColor: '#333333',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 10 },
    shadowOpacity: 0.8,
    shadowRadius: 15,
    elevation: 20,
  },
  textoNota: {
    fontSize: 120,
    fontWeight: 'bold',
    color: '#FFFFFF',
  },
  textoAfinacao: {
    fontSize: 22,
    fontWeight: '600',
    color: '#1DB954', // Verde quando afinado
  },
  textoBaixo: {
    color: '#F44336', // Vermelho para baixo
  },
  textoAlto: {
    color: '#FFC107', // Amarelo para alto
  },
  indicadorBarraContainer: {
    width: 150,
    height: 4,
    backgroundColor: '#444',
    borderRadius: 2,
    marginTop: 20,
    justifyContent: 'center',
    alignItems: 'center',
  },
  indicadorPonto: {
    width: 12,
    height: 12,
    borderRadius: 6,
    backgroundColor: '#FFFFFF',
    position: 'absolute',
  },
  indicadorCentro: {
    width: 4,
    height: 20,
    backgroundColor: '#1DB954',
    position: 'absolute',
  },
  simulacaoContainer: {
    flexDirection: 'row',
    marginTop: 60,
    width: '100%',
    justifyContent: 'space-around',
  },
  botaoSimulacao: {
    backgroundColor: '#555',
    padding: 10,
    borderRadius: 5,
  },
});

export default App;