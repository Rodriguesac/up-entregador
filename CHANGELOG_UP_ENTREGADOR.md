## v6.33.0 — Fluxo Corrida + Tela Cheia Neon

- Redesenhada a tela urgente de nova corrida para aparecer como oferta sobre qualquer app via full-screen intent.
- Adicionado botão Ver detalhes no mapa dentro da oferta.
- Tela de corrida em andamento ganhou mapa maior e card de Próxima Parada.
- Adicionado card de entrega adicional para corrida agrupada quando chegar encaixe compatível.
- Aplicada paleta neon clean oficial: fundo #08164A, verde #B7E51E, amarelo #E8E61A, azul #1E4FFF, roxo #2A1E8A e branco.
- Atualizado versionCode para 860 e versionName para `6.33.0-operacao-completa-tela-cheia`.

## v6.31.0 — Corrida e Entregas operacional

- Troca do título principal `Rota #...` por `Corrida`, `Corrida agrupada`, `1 entrega`, `2 entregas` etc.
- Número do pedido/rota deixa de ser destaque principal e vira referência interna discreta.
- Tela Corridas agora prioriza: missão, quantidade de entregas, próxima parada, mapa e financeiro.
- Card de paradas mostra `Coleta na loja`, `Entrega 1 de N`, `Entrega 2 de N`.
- Mapa continua existindo, mas como apoio visual da corrida, não como nome da operação.
- Textos de suporte, operação e ocorrência ajustados para `corrida` em vez de `rota`.

## v6.31.0 — ideal premium operacional

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

# v6.31.0 — build fix

- Corrigido erro Kotlin em `UrgentRideScreen.kt`: anotação/modificador duplicado em `UrgentTop`.
- Mantidas as mudanças visuais e operacionais da v6.31.0.
- Atualizado `versionCode` para 860 e `versionName` para `6.33.0-operacao-completa-tela-cheia`.

# Changelog — UP Entregador

## 6.33.0-operacao-completa-tela-cheia

- Branding do aplicativo alterado para **UP Entregador**.
- Ícone novo do UP aplicado no launcher e no logo interno.
- Login ajustado para hero vermelho com logo grande e campos simples.
- Contrato visual atualizado para vermelho operacional, fundo claro e cards brancos.
- Fluxo Compose preservado e alinhado às 8 telas operacionais: Login, Permissões, Início, Corridas aguardando, Nova corrida urgente, Corrida em andamento, Histórico e Carteira.
- Workflow GitHub Actions incluído para gerar APK debug.
