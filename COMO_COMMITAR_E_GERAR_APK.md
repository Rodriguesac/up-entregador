# Como commitar e gerar APK — UP Entregador v6.34.0

```bash
cd ~
rm -rf up_v634
unzip -o "/sdcard/Download/UP_Entregador_v6.34.0_profissional_splash_operacao_commitavel.zip" -d up_v634
cp -a ~/up_v634/. ~/up-entregador/
cd ~/up-entregador
git status
git add .
git commit -m "feat: aplica v6.34.0 profissional splash e operacao"
git push origin main
```

Validar no GitHub Actions: `compileDebugKotlin` e `assembleDebug`.
