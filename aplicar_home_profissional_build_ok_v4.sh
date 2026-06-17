#!/usr/bin/env bash
set -euo pipefail
cd /data/data/com.termux/files/home/up-entregador || exit
ZIP="/sdcard/Download/rodrigues_professional_home_build_ok_v4.zip"
if [ ! -f "$ZIP" ]; then
  echo "ZIP não encontrado: $ZIP"
  exit 1
fi
unzip -o "$ZIP"
echo "Conferindo se sobrou Column original:"
grep -nE "(^|[^A-Za-z0-9_.])Column[[:space:]]*[({]|Column\." app/src/main/java/com/rodriguesacai/entregador/ui/professional/ProfessionalHomeScreen.kt || echo "OK: sem Column original"
git status
git add app/src/main/java/com/rodriguesacai/entregador/ui/professional
git add README_INSTALACAO.md README_BUILD_OK_V4.md aplicar_home_profissional_build_ok_v4.sh
git commit -m "fix: remove Column da home profissional"
git push origin HEAD:main
