# Relatório v6.33.0 — Operação Completa + Tela Cheia

## Checklist aplicado

1. Versão visível no Login/Boas-vindas
- Criado `AppVersion.kt` como fonte única da versão.
- Login exibe `UP Entregador • v6.33.0` no topo e `Versão 6.33.0 • Operação completa + tela cheia` no rodapé.
- Cadastro/boas-vindas/permissões também exibem versão.
- Menu Mais/Suporte usa `AppVersion.LABEL`.

2. Versão do build
- `versionCode = 860`.
- `versionName = 6.33.0-operacao-completa-tela-cheia`.
- `DriverRepository.APP_VERSION` usa `AppVersion.NAME`.

3. Nova corrida / tela cheia
- Mantido fluxo de alerta urgente em tela cheia via Full Screen Intent, `UrgentRideActivity` e `USE_FULL_SCREEN_INTENT`.
- Não foi declarado como overlay de bolha `SYSTEM_ALERT_WINDOW`; o nome correto neste pacote é **tela cheia urgente**.
- Tela mostra aceitar, recusar, contador e ver detalhes no mapa.
- Tela agora separa distância até loja e distância loja → cliente quando o GADM/Firebase enviar estes campos.

4. Home, Detalhes e Corrida
- Home permanece resumo operacional.
- Corrida em andamento continua como centro da operação.
- Detalhes da nova corrida mostram mapa `Você → Loja → Cliente`.
- Corrida ativa mostra mapa, próxima parada, pagamento, paradas e ação principal.

5. Corrida e entregas
- Linguagem visual usa `Corrida`, `Corrida agrupada`, `1 entrega`, `2 entregas`.
- Número do pedido/rota fica como `Ref. #...` em contexto discreto.
- Textos de erro user-facing trocaram `rota` por `corrida` quando aplicável.

6. Tema claro/escuro
- Tema padrão continua Claro.
- Menu Mais recebeu alternância Claro/Escuro.
- `MainActivity` recompõe o `RodriguesNativeTheme` quando o tema muda.
- Paleta neon clean foi preservada.

## Pendência declarada

Overlay real tipo bolha sobre qualquer app, com `SYSTEM_ALERT_WINDOW`, WindowManager e serviço próprio, não foi fingido neste pacote. O pacote usa tela cheia urgente, que é o caminho Android mais seguro para oferta de corrida.

## Validação obrigatória no GitHub Actions

- `compileDebugKotlin`.
- `assembleDebug`.
- Verificar `UrgentRideScreen.kt`, `NotificationHelper.kt`, `DriverRepository.kt` e `MainActivity.kt`.
- Conferir APK gerado com versão 6.33.0.
