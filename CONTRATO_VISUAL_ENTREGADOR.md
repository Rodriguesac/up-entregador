# Contrato visual e de fluxo — Rodrigues Entregador

Este app deve seguir as telas de referência aprovadas pelo usuário como contrato visual.

## Regra geral
- Visual limpo, fundo claro e verde como cor principal.
- Cards arredondados, botões grandes e textos curtos.
- Sem debug, sem textos longos e sem informações duplicadas.
- Home mostra o estado operacional rapidamente.
- Corrida urgente faz o entregador decidir rápido.
- Corrida em andamento prioriza mapa, próxima ação e navegação.

## Fluxo oficial
1. Login / Solicitar cadastro / Cadastro em análise / Criar senha.
2. Permissões do app.
3. Home operacional.
4. Ficar disponível.
5. Receber corrida urgente.
6. Aceitar ou recusar.
7. Corrida em andamento.
8. Coleta.
9. Entrega.
10. Finalização / ocorrência.
11. Histórico.
12. Ganhos / Carteira / Repasse.
13. Perfil / Pix / Banco / Solicitações de alteração.
14. Estados especiais: erro Firebase, falha ao carregar, permissão negada, sincronização indisponível, sem internet, manutenção e atualização.

## Ajustes aplicados nesta etapa
- Bottom navigation com `Corridas` no lugar de `Rota`.
- Home com atalhos: Histórico, Ganhos, Mapa e Suporte.
- Login com texto `Bem-vindo(a)!` e botão `Solicitar cadastro`.
- Cadastro recebeu campos de e-mail e cidade.
- Cadastro salva e-mail/cidade no Firebase.
- Corrida em andamento foi limpa: mapa, paradas, distância/tempo, pagamento, abrir navegação, próxima ação e ocorrência.
