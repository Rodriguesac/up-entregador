## v6.30.0 — ideal premium operacional

Rodada visual mais forte baseada no conceito livre de app de entregador premium:
- Home refeita para ter carrossel fixo em todos os estados.
- Status principal redesenhado com card hero, ícone, título e subtítulo legíveis.
- Home agora diferencia melhor: indisponível, disponível, permissões, nova oferta, corrida ativa, ocorrência e sem conexão.
- Quando há corrida ativa, a tela deixa de priorizar permissões e passa a priorizar continuidade da rota.
- Card de ganhos redesenhado com hierarquia mais clara.
- Insights e atalhos com textos maiores e melhor contraste.
- Card compacto de nova oferta na Home.
- Card de corrida ativa com CTA “Continuar corrida” ou “Ver detalhes” em ocorrência.
- Tipografia geral da Home ajustada para leitura em celular real sem truncar status.

# v6.30.0 — build fix

- Corrigido erro Kotlin em `UrgentRideScreen.kt`: anotação/modificador duplicado em `UrgentTop`.
- Mantidas as mudanças visuais e operacionais da v6.30.0.
- Atualizado `versionCode` para 830 e `versionName` para `6.30.0-ideal-premium-home-operacional`.

# Changelog — UP Entregador

## 6.30.0-ideal-premium-home-operacional

- Branding do aplicativo alterado para **UP Entregador**.
- Ícone novo do UP aplicado no launcher e no logo interno.
- Login ajustado para hero vermelho com logo grande e campos simples.
- Contrato visual atualizado para vermelho operacional, fundo claro e cards brancos.
- Fluxo Compose preservado e alinhado às 8 telas operacionais: Login, Permissões, Início, Corridas aguardando, Nova corrida urgente, Corrida em andamento, Histórico e Carteira.
- Workflow GitHub Actions incluído para gerar APK debug.
