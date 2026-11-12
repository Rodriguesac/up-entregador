# UP Entregas — Plano-mestre de produto e execução
Atualizado: 2026-11-11. Estado: plano, não comprovação de conclusão.

## Objetivo
Plataforma independente, multi-loja, de entregas sob demanda. Rodrigues Açaí e Cia é uma loja-cliente, não a base operacional obrigatória.

## Produtos
1. Android do entregador: cadastro, aprovação, online, ofertas, aceite/recusa, chegada, retirada com autorização, navegação, entrega comprovada, ocorrências, carteira, notificações e atualizações.
2. Painel UP Administrador: lojas, entregadores, cobertura, taxas, distribuição, suporte, repasses, auditoria.
3. Portal e API da loja: criar/cancelar solicitações, cotação, acompanhamento, cobrança e webhook autenticado.
4. Integração com Gestor Rodrigues: "Despachar" = entrega própria/externa; "Despachar com UP" = nova solicitação à plataforma UP.

## Fonte de verdade e segurança (P0)
- O repositório Android atualmente tem acesso direto ao Firebase Firestore. Também existe projeto Supabase específico do UP. É necessária auditoria antes de afirmar que está independente.
- Definir UMA fonte de verdade para corridas, status, lojas e financeiro. Arquitetura alvo sugerida: Supabase UP para operação, Firebase FCM para push. Nunca migrar de uma vez sem reconciliação e reversão.
- Entidades: lojas, entregadores, ofertas, corridas, paradas, transições, tarifas, carteiras, repasses, ocorrências, notificações, credenciais de integração.
- Isolamento de lojas por store_id com RLS. API com autenticação e idempotência. Service role nunca dentro do APK. Aceite atômico, impossível dois entregadores aceitarem a mesma corrida.
- Lançamentos financeiros devem ser auditáveis; valores nunca inferidos de status visual.

## Máquina de estados da corrida
SOLICITADA -> OFERTADA -> ACEITA -> A_CAMINHO_LOJA -> NA_LOJA -> RETIRADA -> EM_ROTA -> NO_CLIENTE -> ENTREGUE
Recusada e expirada = estados de oferta, não de corrida; cancelada, ocorrência e devolução requerem regras e responsáveis próprios. Validar transições no servidor e guardar histórico.

## Padrão de interface
- Barra inferior: Início | Entrega | Ganhos | Conta; notificações no topo.
- Início: disponibilidade real, ganho do dia, corrida ativa e próxima ação.
- Entrega: destino da etapa atual, mapa confiável/fallback, navegação para loja ANTES do cliente, ação principal e ocorrência.
- Ganhos: ganhos/recebido/repasse/extrato e filtros hoje, semana, mês.
- Conta: cadastro, veículo, Pix, preferências, notificações, permissões e versão.
- Cards e modais compactos, contraste legível, estados claros.

## Entregáveis em ordem
P0 — Auditoria de frontend/backend, versão do APK, CI; corrigir mapa API KEY REQUIRED; proteger status e navegação; inspecionar PR #1.
P1 — Teste ponta a ponta de corrida real autorizada: solicitar -> ofertar -> aceitar -> chegar loja -> retirar -> entregar -> registrar financeiro. Testar conexão intermitente e aceite concorrente.
P2 — Painel administrador UP, regras de preços, gestão de lojas e entregadores, ocorrências, conciliação e repasses.
P3 — API para lojas independentes, webhooks assinados e integração com o Gestor Rodrigues sem acoplamento direto ao banco da loja.
P4 — APK assinado, versão, canal seguro de atualização, distribuição, métricas, backup, alertas, rollout progressivo e rollback.

## Critérios mínimos para anunciar pronto
1. APK gerado em pipeline, instalado em Android físico e versão identificada.
2. Endereços reais e corretos por etapa; mapa visível ou fallback funcional, sem destino inventado.
3. Apenas um entregador aceita cada corrida; estados inválidos bloqueados na API.
4. Loja A não pode ler loja B; dados, pagamentos e credenciais isolados.
5. Recebimentos, taxas, ganhos e repasses conciliados, com histórico.
6. Testes em operação autorizada, sem impactar terceiros por acidente.
7. Cada entrega vinculada a commit/PR, teste, data e rollback.

## Método de gestão
Main = estável. Mudanças em branches com pull requests, CI e evidências. Issues por prioridade e critério de aceitação. Reporte por fase: FEITO / PARCIAL / PENDENTE / BLOQUEADO, com links. Não considerar estética como prova de funcionamento.
