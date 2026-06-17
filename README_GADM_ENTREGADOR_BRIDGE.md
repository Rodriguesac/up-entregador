# Rodrigues UP Entregas — pacote compatível com GADM

Este pacote foi ajustado para conversar melhor com o GADM.

## Ajustes principais

- App Entregador passa a escutar também `corridas`, além de `rides`, `rotas_entrega` e `pedidos`.
- Aceita ofertas criadas pelo GADM com `status = OFERTA`, `status = OFERTA_RECEBIDA`, `statusCorrida = OFERTA_RECEBIDA`, `statusOfertaEntregador = OFERTA`, `liberadoParaEntregador = true` e `ofertaLiberada = true`.
- Ao aceitar, recusar, expirar, coletar, sair para entrega ou finalizar, o app sincroniza espelhos em `pedidos`, `corridas` e `rides` quando existir referência.
- Notificação de nova corrida leva forma de pagamento, valor a cobrar, troco e maquininha para a tela urgente.
- Fonte/tamanho: escala geral aumentada para 1.10 no tema nativo e fonte oficial ajustada para legibilidade.

## Versão

`1.0`

## Observação

Depois de subir no GitHub, gere um APK novo pelo workflow e instale no aparelho do entregador. O GADM precisa continuar criando a missão em `rides`/`corridas`/`pedidos` conforme o ZIP 19.


## v1.0

Correção financeira: taxa de entrega cobrada do cliente não é usada como repasse do entregador. O app só mostra valor da corrida quando vier de campos de repasse/corrida (`valorCorrida`, `valorRepasseEntregador`, `repassePiloto`, etc.).


## v1.0
- Visual redesenhado com base em app de entregador: vermelho operacional, cards brancos, fundo claro e mapa mais limpo.
- Corrigido aviso de saída bloqueada quando a rota já está liberada.
- `NA_COLETA`/`COLETANDO` permanece missão ativa e não libera o entregador indevidamente.
- Ganhos do dia somam missões do dia com repasse, mesmo antes do fechamento final.
- Distância e tempo buscam campos de `logistica`, `entrega`, `rota` e `calculo`.
