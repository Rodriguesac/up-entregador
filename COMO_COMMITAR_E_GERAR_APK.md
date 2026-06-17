# Como commitar e gerar APK — UP Entregador v6.35.1

```bash
cd ~
rm -rf up_v6351
unzip -o "/sdcard/Download/UP_Entregador_v6.35.1_apk_debug_nomeado_commitavel.zip" -d up_v6351
cp -a ~/up_v6351/. ~/up-entregador/
cd ~/up-entregador
git status
git add .
git commit -m "fix: nomeia apk debug com versao v6.35.1"
git push origin main
```

Depois do GitHub Actions, baixe o artifact com nome `UP_Entregador_v6.35.1-apk-debug-nomeado_APK`. Dentro dele o APK virá como `UP_Entregador_v6.35.1-apk-debug-nomeado_debug.apk`.
