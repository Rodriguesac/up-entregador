# Relatório v6.32.0 — Fluxo Corrida + Overlay Neon

Esta versão aplica a arquitetura operacional definida para o UP Entregador:

## Nova corrida sobre qualquer app
- A tela urgente foi redesenhada para funcionar como oferta de corrida em tela cheia.
- Mostra valor, tempo regressivo, distância/tempo, coleta, entrega e pagamento.
- Botões principais: Recusar, Aceitar e Ver detalhes no mapa.
- Visual com paleta oficial neon: #08164A, #2A1E8A, #B7E51E, #E8E61A, #1E4FFF e branco.

## Ver detalhes
- O botão Ver detalhes abre mapa e informações completas dentro da tela de oferta.
- Exibe fluxo visual Você → Loja → Cliente.
- Mostra pagamento, maquininha, valor a cobrar e troco quando existir.

## Corrida em andamento como centro da operação
- A tela Corridas ganhou mapa maior.
- Inclui card de Próxima Parada.
- Usa próximo estado operacional: ir para coleta, cheguei na loja, iniciar entrega, cheguei no cliente, finalizar.
- O botão principal usa a cor do estado da operação.

## Entrega adicional / agrupamento
- Quando existe corrida ativa e chega uma entrega compatível, o app mostra card “Entrega adicional encontrada”.
- O motoboy pode Ignorar ou Adicionar à corrida.
- A nomenclatura segue: Corrida, Corrida agrupada, 1 entrega, 2 entregas, Próxima parada.

## Tema e identidade
- Tema claro permanece padrão.
- Paleta visual do app foi migrada para a identidade neon clean.
- O vermelho fica reservado para recusar/erro/cancelamento.

## Versão
- versionCode: 850
- versionName: 6.32.0-fluxo-corrida-overlay-neon
