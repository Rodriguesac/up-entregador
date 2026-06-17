#!/usr/bin/env bash
set -euo pipefail

REPO="/data/data/com.termux/files/home/up-entregador"
ZIP="/sdcard/Download/rodrigues_professional_home_build_ok_v3.zip"

cd "$REPO" || exit 1

git checkout main
git pull --rebase origin main

if [ ! -f "$ZIP" ]; then
  echo "ERRO: ZIP não encontrado em $ZIP"
  echo "Baixe o pacote v3 e deixe em /sdcard/Download."
  exit 1
fi

unzip -o "$ZIP"

FILE="app/src/main/java/com/rodriguesacai/entregador/ui/professional/ProfessionalHomeScreen.kt"

echo "===== Conferência ProfessionalColumn ====="
grep -n "ProfessionalColumn" "$FILE" | head -40

echo "===== Conferência: não pode existir import direto de Column ====="
if grep -n "import androidx.compose.foundation.layout.Column" "$FILE"; then
  echo "ERRO: import Column ainda existe."
  exit 1
else
  echo "OK: import Column removido."
fi

echo "===== Build local no Termux, se o Gradle estiver funcional ====="
chmod +x ./gradlew || true
./gradlew :app:assembleDebug --stacktrace

git status
git add app/src/main/java/com/rodriguesacai/entregador/ui/professional
git add README_INSTALACAO.md README_BUILD_OK_V3.md aplicar_home_profissional_build_ok_v3.sh || true

git commit -m "fix: pacote build ok da home profissional" || echo "Nada novo para commitar."
git push origin HEAD:main

echo "OK: pacote aplicado e enviado para main."
