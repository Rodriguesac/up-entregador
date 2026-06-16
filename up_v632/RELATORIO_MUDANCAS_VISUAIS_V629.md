# Relatório de mudanças — UP Entregador v6.31.0

Esta versão foi feita para aproximar o app real em Kotlin + Jetpack Compose do modelo visual operacional aprovado.

## Mudanças visuais principais

- Login recriado com hero vermelho, logo UP grande, título centralizado, campos limpos e botões mais próximos do mockup.
- Tela de permissões recriada com progresso visual `2 de 5 liberadas`, cards clicáveis e botão `Liberar permissões essenciais`.
- Home operacional aproximada do modelo:
  - Header com avatar, nome e ações.
  - Card/pill vermelho `Disponível` com switch.
  - Ganhos em verde, métricas de corridas/finalizadas e ícone de ocultar valores.
  - Insights de demanda e meta diária.
  - Banner vermelho estilo indicação/ganhos.
  - Atalhos em uma linha: Histórico, Carteira, Mapa e Suporte.
- Aba Corridas/Aguardando corrida redesenhada com ícone central e card de dicas.
- Nova corrida urgente recriada com faixa vermelha, contador, valor grande, cards de coleta/entrega, mapa, pagamento e botões Aceitar/Recusar.
- Tela fullscreen de oferta urgente também foi redesenhada para ficar coerente com a tela dentro do app.
- Rota ativa ajustada com header escuro, chips de distância/tempo, mapa, paradas, pagamento e próxima ação.
- Carteira e Histórico mantidos no padrão operacional vermelho, com tipografia e espaçamentos revisados.

## Mudanças técnicas

- `versionCode`: 840.
- `versionName`: `6.32.0-fluxo-corrida-overlay-neon`.
- `DriverRepository.APP_VERSION`: `6.32.0-fluxo-corrida-overlay-neon`.
- Escala de fonte fixa ajustada para `1.06f` em `RodriguesNativeTheme`, evitando fonte minúscula sem estourar layout.
- Componentes globais em `EntregadorDesign.kt` receberam refinamento de tamanho, raio e botão.

## Arquivos mais alterados

- `app/src/main/java/com/rodriguesacai/entregador/ui/DriverHomeScreen.kt`
- `app/src/main/java/com/rodriguesacai/entregador/ui/UrgentRideScreen.kt`
- `app/src/main/java/com/rodriguesacai/entregador/RodriguesNativeTheme.kt`
- `app/src/main/java/com/rodriguesacai/entregador/ui/design/EntregadorDesign.kt`
- `CONTRATO_VISUAL_ENTREGADOR.md`
- `README_GADM_ENTREGADOR_BRIDGE.md`
- `CHANGELOG_UP_ENTREGADOR.md`
- `COMO_COMMITAR_E_GERAR_APK.md`

## Observação de build

O pacote está pronto para commit e possui workflow GitHub Actions para gerar APK debug. Neste ambiente não há Android SDK/Gradle local configurado para executar `assembleDebug` aqui dentro.
