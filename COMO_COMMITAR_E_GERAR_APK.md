# Como commitar e gerar APK — UP Entregador v6.32.0

No Termux, depois de baixar o ZIP para `/sdcard/Download`:

```bash
cd ~
rm -rf up_v632
unzip -o "/sdcard/Download/UP_Entregador_v6.32.0_fluxo_corrida_overlay_neon_commitavel.zip" -d up_v632
cp -a ~/up_v632/. ~/up-entregador/
cd ~/up-entregador
git status
git add .
git commit -m "feat: aplica fluxo de corrida e overlay neon v6.32.0"
git push origin main
```

Depois do push, aguarde o GitHub Actions gerar o APK.
