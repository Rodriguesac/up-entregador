# UP Entregas — Integração Android / Supabase independente (2.0.3-rc1)

## Situação
**PR de desenvolvimento, não publicar para entregadores ainda.**
O aplicativo Android compila no GitHub Actions, mas não foi validado de
ponta a ponta com motoboy autenticado e pedido real.

## Código implementado
- Isolamento por modo: legado por CPF/Firestore; UP independente por
  Firebase Authentication email/senha + `up_couriers.state=APPROVED`.
- Cadastro próprio: criação/entrada em conta Firebase Auth e inscrição
  na Edge Function `driver-registration-independent` (aprovação obrigatória).
- Consulta de ofertas e corridas individuais na `driver-api-independent`,
  com polling a cada 10 segundos.
- Disponibilidade online apenas após confirmação da API; restauração
  do estado online ao abrir sessão autorizada.
- FCM: token do aparelho registrado no UP, nunca em coleção Firestore
  antiga quando o perfil é UP independente.
- Ações protegidas: aceite, recusa, expiração, ocorrência e etapas
  ACEITA → INDO À LOJA → NA LOJA → RETIRADO → NO CLIENTE → ENTREGUE.
- Confirmação de retirada com código da loja, se configurado no pedido.
- Confirmação da entrega com código do cliente, se configurado, mais
  digitação e confirmação do valor realmente recebido.
- GPS em primeiro e segundo plano por serviço foreground:
  corrida com ID `supabase:UUID` só envia para Supabase UP.
  Pontos de localização desatualizados não são enviados como atuais.
- Histórico do entregador e carteira financeira consultados apenas pela
  API autenticada do UP; valores de pedidos NÃO são exibidos como ganhos.
- Telemetria de bateria a cada 2 minutos; informa equipamento,
  dinheiro disponível e maquininha, para uso do servidor.
- Não atribui tarifa automática, não finge recebimento, nem
  concede permissões a entregador não aprovado.
- Não mistura dados de clientes com a base independente da loja.

## Itens pendentes para liberação de produção
1. **Firebase do UP:** confirmar que o `google-services.json` Android
   corresponde ao valor `UP_FIREBASE_PROJECT_ID` no Supabase UP e que
   os entregadores aprovados têm a identidade correta. O arquivo atual
   é do projeto `rodrigues-d6566`; confirmar intenção de migração para
   projeto Firebase próprio do UP antes de publicar. O Supabase tem um
   projeto independente, mas isso não garante identidade Firebase própria.
2. **Credenciais do backend:** verificar as flags de ativação do motorista,
   registro, FCM e rastreamento; e a configuração das chaves compartilhadas.
   Não copiar segredos para APK nem para repositório.
3. **Teste no Android real:** registro/aprovação; login; online; FCM;
   entrega atribuída; GPS ativo com tela apagada; recusa e offline.
4. **Cliente da loja:** conferir se o site efetivamente chama
   `up-customer-ticket` e `up-customer-tracking` e renderiza o mapa
   com coordenadas atualizadas após a retirada. Sem teste real, o cliente
   **não deve receber promessa de rastreamento funcionando**.
5. **Códigos e pagamento:** verificar que criação da corrida preenche
   códigos de retirada/entrega (ou política alternativa segura), e
   garantir que valor `collection_amount` seja apenas dinheiro a receber
   no destino, não o total pago online.
6. **Status retornando à loja:** aceitar, coletar, entregar/cancelar; testar
   assinaturas e ordem de eventos sem criar cobranças indevidas.
7. **APK distribuição:** build release assinado para atualização do app;
   APK debug do CI é só para teste.
8. **Dados de cadastro:** as telas antigas solicitam CNH/selfie/PIX,
   mas a função de inscrição independente aceita somente dados limitados.
   Adicionar upload privado de documentos e revisão de cadastro antes de
   divulgar o novo registro a entregadores.
9. **Rastreamento antifraude:** calibrar precisão, privacidade,
   dados desatualizados e comportamento offline; teste real da moto.

## Segurança
- API do motoboy aceita token do Firebase validado no servidor e somente
  a própria corrida; nunca recebe `service_role` do APK.
- Atualização de GPS é recusada se o motoboy não for titular da corrida
  ou status não permitir rastreamento.
- O mapa do cliente continua exigindo ticket assinado, vinculado ao pedido.
- Sem pedido real, nenhum ponto deve ser mostrado como rastreamento ao vivo.
- Nenhum pedido real foi criado e nenhum GPS real foi simulado.
- A loja recebeu uma migração isolada no Supabase: `up_store_outbox_finish`
  marca `tracking_enabled=true` apenas depois do ACK de criação da corrida UP.
- A função `up-status-receiver` da loja foi publicada para marcar
  `tracking_enabled=false` quando o job ficar `DELIVERED`, `CANCELLED`
  ou `FAILED`. A autenticação HMAC original foi preservada.
- Nenhuma alteração retroativa foi feita nos pedidos existentes.

## Compilação pelo celular
Abra a aba **Actions** no GitHub, escolha a execução mais recente
**Build Android Debug APK** da PR e baixe o artefato
`UP-Entregas-v2.0.3-rc1-debug-apk` após a execução ficar verde.
O arquivo vem em ZIP; extraia o APK. Não instalar sobre uma versão
assinada diferente sem verificar compatibilidade de assinatura.
