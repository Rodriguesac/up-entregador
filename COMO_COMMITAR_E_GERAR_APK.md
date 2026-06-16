# Como commitar e gerar APK — UP Entregador v6.33.0

```bash
cd ~
rm -rf up_v633
unzip -o "/sdcard/Download/UP_Entregador_v6.33.0_operacao_completa_tela_cheia_commitavel.zip" -d up_v633
cp -a ~/up_v633/. ~/up-entregador/
cd ~/up-entregador
git status
git add .
git commit -m "feat: aplica operacao completa e tela cheia v6.33.0"
git push origin main
```

Depois confira o GitHub Actions.
