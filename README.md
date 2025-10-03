# 🎵 AllTune

Um aplicativo mobile de **afinação musical** com arquitetura **MVVM-C** desenvolvido em **React Native** com integração nativa Android para captura de áudio via `AudioRecord`, processamento FFT e persistência em **SQLite**.

---

## 🚀 Funcionalidades
- Captura de áudio em tempo real pelo microfone (Android).
- Processamento FFT para detecção de frequências e notas.
- Exibição visual da nota/frequência.
- Histórico de afinações armazenado em SQLite.
- Arquitetura **MVVM-C** para separar responsabilidades.

---

## 🏗 Estrutura do Projeto
```bash
src/
├─ view/          # componentes React (UI)
├─ viewmodel/     # lógica da view (observables, state holders)
├─ controller/    # orquestrações (start/stop capture, persistência)
└─ model/
   ├─ audio/      # interfaces JS p/ áudio (bridge calls)
   ├─ native/     # wrappers p/ módulos nativos (Kotlin)
   ├─ db/         # acesso SQLite (JS wrapper)
   └─ entities/   # tipos e modelos (nota, historico)
```

## 🔧 Pré-requisitos
- Node.js (>= 18)
- JDK 11 ou 17
- Android Studio + SDK
- Git

---

## 📦 Instalação
```bash
# Clonar o repositório
git clone https://github.com/brunoLasmar/all-tuner.git
cd all-tuner

# Instalar dependências
yarn install
