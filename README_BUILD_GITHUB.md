# UP Entregas v1.0 — Build GitHub

Pacote completo para substituir o repositório do app do entregador.

## Inclui nesta versão

- Logo oficial UP Entregas aplicada no app e no ícone.
- SplashScreen azul com logo central e três pontos carregando.
- Tela de boas-vindas com fluxo Começar → Fazer Login / Novo por aqui.
- Login apenas com CPF + senha numérica.
- Cadastro tela a tela.
- Validação real de CPF.
- Máscara de CPF e telefone.
- Nome completo com no mínimo dois nomes válidos.
- Senha numérica com mínimo 7 dígitos usando teclado padrão Android.
- Cadastro fica logado e preso em “Em análise”.
- Quando aprovado no GADM, libera a Home automaticamente.
- Home com paleta azul, amarelo e branco.
- Modal de comunicado/promoção/notificação quando houver aviso ativo no Firebase.
- Compatibilidade com GADM em `entregadores`, `solicitacoesEntregadores`, `rides`, `corridas`, `rotas_entrega` e `pedidos`.

## Comandos no Termux

```bash
cd ~
rm -rf up-entregador-backup
cp -r up-entregador up-entregador-backup

cd up-entregador
find . -mindepth 1 ! -name ".git" ! -path "./.git/*" -exec rm -rf {} +
unzip /sdcard/Download/UP_Entregas_v1_0_REAL_GitHub_Build.zip -d .

git status
git add .
git commit -m "Versao 1.0 real UP Entregas"
git push origin main
```

Depois o GitHub Actions gera o APK debug.
