# Rodrigues UP Entregas — versão 1.0

Pacote pronto para subir no GitHub e gerar build pelo GitHub Actions.

## O que está incluído

- App Android nativo Kotlin/Compose.
- Compatibilidade com GADM via Firebase.
- Escuta de `pedidos`, `corridas`, `rides` e `rotas_entrega`.
- Status de missão mantendo corrida ativa em coleta/rota.
- Separação entre taxa cobrada do cliente e repasse do entregador.
- Versão do app: `1.0`.

## Build local

```bash
gradle :app:assembleDebug --stacktrace
```

## Build no GitHub

O workflow está em `.github/workflows/android-debug.yml`.
Ao fazer push na branch `main`, ele gera o APK debug como artifact.

## APK gerado

Caminho no build:

```txt
app/build/outputs/apk/debug/app-debug.apk
```
