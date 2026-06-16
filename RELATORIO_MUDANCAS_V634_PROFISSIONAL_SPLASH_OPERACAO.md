# Relatório v6.34.0 — Profissional Splash + Operação

## Versão
- versionCode: 870
- versionName: `6.34.0-profissional-splash-operacao`
- AppVersion: `UP Entregador • v6.34.0`

## Splash real
Implementado com:
- dependência `androidx.core:core-splashscreen:1.0.1`
- `installSplashScreen()` na `MainActivity`
- `Theme.UP.Splash` em `styles.xml`
- `MainActivity` usando `android:theme="@style/Theme.UP.Splash"`

## Boas-vindas animada
Criada `WelcomeAnimatedScreen` em Compose com:
- fundo azul-marinho/roxo
- letra U surgindo
- letra P surgindo
- seta subindo
- trilho/brilho discreto
- botões Entrar e Criar cadastro

Regra: Welcome aparece para usuário sem sessão e não concluído. Usuário logado entra direto na Home.

## Tema claro/escuro
- Padrão: claro.
- Preferência salva em `AppSettings`.
- `ThemeRuntime` aplica cores dinâmicas nos componentes principais: fundo, cards, bordas, textos, chips e botões.

## Operação/Home
- Home com corrida ativa deixa de competir com demanda/carrossel.
- Ocorrência não usa hero verde.
- Métricas vazias não exibem `—`.
- Continua usando nomenclatura Corrida/Entregas/Ref.

## Tela cheia/overlay
Esta versão usa Full Screen Intent/tela cheia urgente. Não foi implementado overlay real `SYSTEM_ALERT_WINDOW`; portanto o pacote não afirma que existe overlay real.

## Pendências honestas
- Build Android completo depende do GitHub Actions.
- Overlay real sobre outros apps exigiria implementação separada com permissão de sobreposição, foreground service e view flutuante.


## Validação pré-ZIP executada

### Versões antigas no código
Comando: `grep -R "6.29.1\|6.30\|6.31\|6.32\|6.33" -n app/src/main/java app/build.gradle.kts`
Resultado: sem ocorrências.

### Textos antigos de rota no código
Comando: `grep -R "Rota #\|rota #\|Destravar rota\|Mapa da rota" -n app/src/main/java`
Resultado: sem ocorrências.

### Versão nova
Encontrado `6.34.0` em:
- `AppVersion.kt`
- `app/build.gradle.kts`

### Splash real
Encontrado no código:
- `androidx.core:core-splashscreen:1.0.1`
- `installSplashScreen()`
- `Theme.UP.Splash`
- `windowSplashScreenBackground`
- `windowSplashScreenAnimatedIcon`
- `MainActivity` com `android:theme="@style/Theme.UP.Splash"`

### Tema claro/escuro
Encontrado no código:
- `AppSettings.THEME_LIGHT`
- `AppSettings.THEME_DARK`
- `getThemeMode` / `setThemeMode`
- `ThemeRuntime` com cores dinâmicas nos componentes principais
- opção `Tema do app` no Menu Mais

### Tamanho
- ZIP base v6.33.0: 5.17 MB
- Projeto extraído/modificado antes do ZIP: 5.57 MB
- APK local: não gerado neste ambiente, pois não há Android SDK/Gradle wrapper executável local.
