# UP Entregador — pacote compatível com GADM

Pacote nativo Android em Kotlin + Jetpack Compose para o entregador, pronto para commitar no GitHub e gerar APK pelo workflow.

## Versão

`6.28.0-up-fiel-operacional`

## O que este pacote entrega

- App com identidade **UP Entregador**.
- Ícone do app incluído em `res/drawable/up_app_icon.png` e `mipmap-*`.
- Manifest atualizado com nome **UP Entregador** e launcher icon novo.
- Visual vermelho operacional com cards brancos, tipografia legível e fundo claro.
- Telas reais em Compose para:
  - Login / cadastro.
  - Permissões do app.
  - Início operacional.
  - Corridas aguardando.
  - Nova corrida urgente.
  - Corrida em andamento.
  - Histórico.
  - Carteira.
  - Mais / Perfil / Pix-Banco / Operação / Suporte.
- Integração mantida com GADM/Firebase.

## Compatibilidade operacional com GADM

O app escuta ofertas também em `corridas`, além de `rides`, `rotas_entrega` e `pedidos`.

Aceita ofertas criadas pelo GADM com campos como:

- `status = OFERTA`
- `status = OFERTA_RECEBIDA`
- `statusCorrida = OFERTA_RECEBIDA`
- `statusOfertaEntregador = OFERTA`
- `liberadoParaEntregador = true`
- `ofertaLiberada = true`

Ao aceitar, recusar, expirar, coletar, sair para entrega, registrar ocorrência ou finalizar, o app sincroniza espelhos em `pedidos`, `corridas` e `rides` quando existir referência.

## Financeiro

A taxa de entrega cobrada do cliente não é usada automaticamente como repasse do entregador. O app mostra valor de corrida/repasse somente quando vier de campos próprios, como `valorCorrida`, `valorRepasseEntregador`, `repassePiloto` e equivalentes já tratados no repositório.

## Como gerar APK pelo GitHub

1. Suba estes arquivos no repositório.
2. No GitHub, abra a aba **Actions**.
3. Rode o workflow **Android APK - UP Entregador**.
4. Baixe o artefato `UP-Entregador-debug-apk`.

## Observação

O pacote foi preparado para commit. Em ambiente local, é necessário Android SDK/Gradle configurado para rodar `assembleDebug`.
