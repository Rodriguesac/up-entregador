# Mapa UP Entregas para o cliente — integração no site atual

O código `up-customer-map.js` está preparado. **Este repositório do aplicativo Android
NÃO publica automaticamente a página rodriguesacaiecia.web.app.**
O arquivo precisa ser copiado para a distribuição do SITE DO CLIENTE e
chamado dentro do histórico do pedido, depois do checkout.

## Requisitos
- URL da página: `https://rodriguesacaiecia.web.app`
  ou `https://rodriguesacaiecia.firebaseapp.com`.
- Pedido real com `raw_payload.up_bridge.mode = "isolated"` e
  `tracking_enabled = true`, habilitado exclusivamente quando o UP
  confirmar a criação da corrida.
- Entregador aprovado, corrida atribuída, em `TO_CUSTOMER` ou `AT_CUSTOMER`,
  GPS recente (< 120 segundos) no Supabase UP.
- As flags e o segredo HMAC precisam estar configurados NOS DOIS Supabase.
  Nunca coloque segredo de servidor no navegador.
- Conteúdo público somente com credencial do próprio pedido.

## Credenciais suportadas no cliente

**Escolha somente a modalidade que a tela atual já usa.**
Não use código/número público do pedido sozinho para autorizar o rastreamento.

1. Cliente por `guest-order`: identificador real do pedido + token secreto gerado
   na criação. Passe `orderId` e `accessToken`.
2. Recuperação em `public-order-lookup`: `lookupAccessToken` obtido após número
   do pedido + telefone verificado pelo servidor.
3. Cliente autenticado pelo Supabase: `orderId` + `authToken` de sessão válida;
   o servidor exige que `customer_id` seja o próprio usuário.
4. Fluxo legado com recibo secreto: `receiptToken`.

## Exemplo de ligação na página de histórico

O site precisa entregar o ES module junto com os arquivos estáticos e ter um
elemento `<div id="up-tracking-map"></div>` na página do pedido.

```js
import {mountUpCustomerTracking}
  from "/js/up-customer-map.js";

// Exemplo: pedido de convidado, já autenticado no fluxo de compra.
const stopTracking = mountUpCustomerTracking({
  container: document.getElementById("up-tracking-map"),
  orderId: order.firebase_order_id || order.supabaseId,
  accessToken: orderAccessToken,
  onStatus: status => {
    // "live", "no_recent_gps", "offline", "not_available", etc.
    console.info("UP tracking status:", status);
  }
});

// Ao sair do pedido ou fechar o componente:
stopTracking();
```

O arquivo atual fica em
`integrations/store/customer-tracking/up-customer-map.js` deste repositório.
Copie seu conteúdo (sem alterar a assinatura do módulo) para
`public/js/up-customer-map.js` do site cliente, ou para caminho equivalente
do projeto atualmente publicado. **Não será aplicado automaticamente.**

## Segurança / privacidade

- **Nunca** colocar recibo ou access token na URL, logs, analytics ou links.
- O token da loja é usado para obter ticket HMAC de curta validade (75 s).
- O ticket é a única credencial enviada para o projeto UP.
- Não exibir posição quando não houver GPS recente; não inventar coordenadas,
  rota, distância nem previsão.
- Ao encerrar a corrida, loja revoga `tracking_enabled`.
- Leaflet e OpenStreetMap são carregados somente após GPS válido. Em escala
  comercial, configurar fornecedor de tiles compatível com a demanda, políticas
  de privacidade e atribuição obrigatória.
- Não compartilhar a localização do entregador após a entrega.

## O que falta para a entrega final

A integração visual efetiva no Firebase Hosting precisa do código-fonte atual
do site do cliente ou permissão ao projeto de hospedagem e build correspondente.
Uma compilação Android verde **não valida** a autorização do site nem a
transmissão de GPS por motoboy real.
