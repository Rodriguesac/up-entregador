#!/usr/bin/env bash
# Emulador descartável. Nunca autentica usuário, cria corrida ou simula GPS de moto.
set -euo pipefail
mkdir -p smoke-output
echo "Compilando APK para teste de abertura em Android..."
gradle :app:assembleDebug --no-daemon
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb logcat -c
echo "Abrindo UP Entregas no emulador..."
adb shell am start -W -n com.rodriguesacai.entregador/.MainActivity
sleep 15
adb exec-out screencap -p > smoke-output/up-home-android.png
adb logcat -d -v time -s AndroidRuntime:E > smoke-output/android-runtime.txt || true
PROCESS="$(adb shell pidof com.rodriguesacai.entregador | tr -d '\r')"
if [ -z "$PROCESS" ]; then
  echo "::error::O processo UP Entregas encerrou ao iniciar."
  tail -n 60 smoke-output/android-runtime.txt || true
  exit 1
fi
if grep -q 'Process: com.rodriguesacai.entregador' smoke-output/android-runtime.txt; then
  echo "::error::Detectada exceção fatal do aplicativo UP Entregas."
  tail -n 80 smoke-output/android-runtime.txt
  exit 1
fi
echo "UP Entregas iniciou, permaneceu aberto e gerou screenshot no Android."
echo "IMPORTANTE: este teste não verifica login Firebase, entrega real, FCM ou GPS."
