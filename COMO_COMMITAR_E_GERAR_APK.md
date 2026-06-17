# Como commitar e gerar APK — UP Entregador v6.35.0

```bash
cd ~
rm -rf up_v635
unzip -o "/sdcard/Download/UP_Entregador_v6.35.0_montserrat_carrossel_gadm_commitavel.zip" -d up_v635
cp -a ~/up_v635/. ~/up-entregador/
cd ~/up-entregador
git status
git add .
git commit -m "feat: aplica Montserrat e carrossel GADM v6.35.0"
git push origin main
```

Depois baixe o APK no GitHub Actions e desinstale a versão antiga antes de instalar.
