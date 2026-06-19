# Contrato de Status — UP Entregas v2.0

## Missão ativa

Uma missão só é ativa quando ainda existe trabalho real do entregador:

- ACEITA
- A_CAMINHO_LOJA
- CHEGOU_LOJA
- NA_COLETA
- COLETANDO
- AGUARDANDO_LIBERACAO
- SAIDA_LIBERADA
- EM_ROTA
- ENTREGADOR_NO_LOCAL

## Missão encerrada

Nunca pode aparecer na Home como corrida ativa:

- RECUSADA
- EXPIRADA
- CANCELADA
- CONCLUIDA
- ENTREGUE
- FINALIZADA
- OCORRENCIA_RESOLVIDA
- OCORRENCIA_CANCELADA

## Oferta nova

A oferta nova precisa aparecer como tela de decisão, nunca como corrida ativa antes do aceite.

Botões:

- Aceitar
- Recusar

Regras:

- recusar não grava entregador como dono da corrida;
- expirar não vira ocorrência;
- oferta expirada volta para o GADM decidir;
- entregador continua disponível.

## Ocorrência

Ocorrência grave pode travar até o GADM resolver.
Ocorrência simples apenas registra o problema e pode liberar o entregador.

Campos recomendados:

- ocorrenciaAtiva
- statusOcorrencia
- motivoOcorrencia
- detalheOcorrencia
- resolvidaEm
- resolvidaPor

## Destravar entregador

Ao destravar, limpar ao mesmo tempo:

- pedidos
- corridas
- rides
- rotas_entrega
- entregadores

Campos a limpar:

- pedidoAtualId
- corridaAtualId
- rideAtualId
- rotaAtualId
- ocorrenciaAtual
- corridaAtiva
- emCorrida
