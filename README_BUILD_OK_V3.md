# Rodrigues Entregador — Home Profissional build ok v3

Este pacote foi gerado para corrigir a falha recorrente:

```text
Unresolved reference 'Column'
```

A correção desta versão remove o uso direto de `Column` na `ProfessionalHomeScreen.kt` e usa uma função interna chamada `ProfessionalColumn`, evitando depender do símbolo `Column` durante a compilação do projeto.

## Arquivos incluídos

```text
app/src/main/java/com/rodriguesacai/entregador/ui/professional/CorridaUiModel.kt
app/src/main/java/com/rodriguesacai/entregador/ui/professional/CorridaStatusMapper.kt
app/src/main/java/com/rodriguesacai/entregador/ui/professional/ProfessionalHomeScreen.kt
app/src/main/java/com/rodriguesacai/entregador/ui/professional/RodriguesProfessionalTheme.kt
README_INSTALACAO.md
README_BUILD_OK_V3.md
aplicar_home_profissional_build_ok_v3.sh
```

## Aplicar, testar build e subir para GitHub

No Termux:

```bash
cd /data/data/com.termux/files/home/up-entregador || exit

unzip -o /sdcard/Download/rodrigues_professional_home_build_ok_v3.zip

chmod +x aplicar_home_profissional_build_ok_v3.sh
bash aplicar_home_profissional_build_ok_v3.sh
```

## Só aplicar e commitar sem build local

```bash
cd /data/data/com.termux/files/home/up-entregador || exit

git checkout main
git pull --rebase origin main

unzip -o /sdcard/Download/rodrigues_professional_home_build_ok_v3.zip

grep -n "ProfessionalColumn" app/src/main/java/com/rodriguesacai/entregador/ui/professional/ProfessionalHomeScreen.kt | head -40

git status
git add app/src/main/java/com/rodriguesacai/entregador/ui/professional
git add README_INSTALACAO.md README_BUILD_OK_V3.md aplicar_home_profissional_build_ok_v3.sh

git commit -m "fix: pacote build ok da home profissional"
git push origin HEAD:main
```

## Baixar APK depois que o GitHub Actions passar

```bash
cd ~
rm -rf apk_up_final

LATEST=$(gh run list -R Rodriguesac/up-entregador --branch main --status success --limit 1 --json databaseId -q '.[0].databaseId')

echo "Último build com sucesso: $LATEST"

gh run download "$LATEST" -R Rodriguesac/up-entregador -D apk_up_final

APK=$(find ~/apk_up_final -name "*.apk" | head -n 1)

cp "$APK" /sdcard/Download/UP_Entregador_v635_home_profissional.apk

ls -lh /sdcard/Download/UP_Entregador_v635_home_profissional.apk
```
