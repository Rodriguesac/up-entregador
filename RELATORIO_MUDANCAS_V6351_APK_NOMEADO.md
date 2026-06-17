# Relatório v6.35.1 — APK Debug Nomeado

## Objetivo
Evitar confusão entre APK antigo e APK novo baixado do GitHub Actions.

## Mudanças reais
- `app/build.gradle.kts`: versionCode 881 e versionName `6.35.1-apk-debug-nomeado`.
- `AppVersion.kt`: versão exibida no app atualizada para v6.35.1.
- `.github/workflows/android-apk.yml`: agora extrai a versão do Gradle, renomeia o APK debug e sobe artifact com versão no nome.

## Resultado esperado no Actions
Artifact:
`UP_Entregador_v6.35.1-apk-debug-nomeado_APK`

Arquivo APK dentro do artifact:
`UP_Entregador_v6.35.1-apk-debug-nomeado_debug.apk`

## O que não foi alterado
Esta versão não muda operação, tela ou Firebase; é uma correção de rastreabilidade do APK e versão.
