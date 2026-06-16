## v6.34.0 — Profissional Splash + Operação

Entrega focada em implementação real, sem chamar simulação de recurso real.

### Alterações reais
- Atualizado `versionCode` para 870 e `versionName` para `6.34.0-profissional-splash-operacao`.
- Mantido `AppVersion.kt` como fonte única de versão visível no Login, Welcome e Menu Mais/Sobre.
- Implementado Splash nativo real Android com `androidx.core:core-splashscreen`, tema `Theme.UP.Splash` e `installSplashScreen()` na `MainActivity`.
- Adicionada tela Welcome/Boas-vindas em Compose com animação de montagem da marca UP: U, P, seta, trilho/brilho e botões Entrar/Criar cadastro.
- Welcome aparece somente para usuário sem sessão e que ainda não concluiu boas-vindas; usuário logado vai direto para Home.
- Tema padrão claro preservado e suporte escuro melhorado por cores dinâmicas em componentes principais.
- Home em corrida/ocorrência reduz concorrência visual: cards de demanda/carrossel não aparecem durante corrida ativa.
- Ocorrência usa tom amarelo/laranja, não verde.
- Distância/tempo vazios passam a mostrar `Calculando rota...` ou `Calculando`, não traço solto.
- Textos antigos `Rota #`, `Destravar rota` e `Mapa da rota` não aparecem no código principal.
- Tela cheia urgente continua documentada como Full Screen Intent; não é declarada como overlay real porque não usa `SYSTEM_ALERT_WINDOW`.

### Validar no GitHub Actions
- `compileDebugKotlin`
- `assembleDebug`
- Tema de Splash no Manifest/XML
- Abertura do app sem tela preta
- Login/Welcome mostrando `UP Entregador • v6.34.0`
