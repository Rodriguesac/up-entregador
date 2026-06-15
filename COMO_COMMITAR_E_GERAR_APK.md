# Como commitar e gerar o APK — UP Entregador

## 1. Subir no GitHub

Copie todo o conteúdo deste pacote para o repositório do app entregador e faça commit.

Sugestão de comandos:

```bash
git add .
git commit -m "feat: aplica UP Entregador visual fiel operacional v6.29.0"
git push
```

## 2. Gerar APK pelo GitHub Actions

Depois do push:

1. Abra o repositório no GitHub.
2. Vá em **Actions**.
3. Abra **Android APK - UP Entregador**.
4. Clique em **Run workflow** se não iniciar automaticamente.
5. Ao finalizar, baixe o artefato **UP-Entregador-debug-apk**.

## 3. Gerar APK localmente

Se tiver Android SDK e Gradle instalados:

```bash
gradle :app:assembleDebug --stacktrace
```

O APK sai em:

```text
app/build/outputs/apk/debug/
```

## 4. O que validar no celular

- O app deve aparecer como **UP Entregador**.
- O ícone deve aparecer com o logo UP.
- Login deve abrir com hero vermelho e campos CPF/telefone e senha.
- Permissões devem abrir prompt nativo quando possível.
- Home deve mostrar disponibilidade, ganhos, demanda, meta, carrossel e atalhos.
- Corridas deve mostrar aguardando corrida quando disponível.
- Nova corrida urgente deve mostrar contador, valor, mapa, aceitar e recusar.
- Corrida em andamento deve mostrar mapa, paradas, pagamento, próxima ação e ocorrência.
- Histórico e Carteira devem seguir o fluxo visual vermelho operacional.
