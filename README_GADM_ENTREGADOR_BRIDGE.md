# Ponte GADM ↔ UP Entregas v1.0

Este app usa o Firebase do ecossistema Rodrigues/UP para conversar com o GADM.

## Cadastro

O app cria/atualiza:

- `entregadores/{cpf}`
- `solicitacoesEntregadores/{cpf}`

Campos importantes:

- `nome`, `nomeCompleto`
- `cpf`, `cpfLimpo`
- `telefone`, `telefoneLimpo`, `whatsapp`
- `dataNascimento`
- `tipoVeiculo`, `modalidade`
- `placa`
- `modeloVeiculo`
- `cnh`
- `chavePix`, `pix`
- `statusCadastro = PENDENTE`
- `statusAprovacao = PENDENTE`
- `aprovado = false`

O app salva sessão após cadastro e exibe a tela “Em análise”.

## Aprovação no GADM

Para liberar o entregador:

```txt
aprovado = true
statusAprovacao = APROVADO
statusCadastro = APROVADO
```

Para bloquear/reprovar:

```txt
aprovado = false
bloqueado = true
statusAprovacao = BLOQUEADO ou REPROVADO
```

## Corridas e rotas

O app continua ouvindo os documentos compatíveis com o GADM:

- `rides`
- `corridas`
- `rotas_entrega`
- `pedidos`

Campos de repasse aceitos:

- `valorCorrida`
- `valorRepasseEntregador`
- `valorRepasseMotoboy`
- `repasseFrota`
- `repassePiloto`
- `calculo.valorTotalMotoboy`

Taxa de entrega do cliente não é usada como repasse do entregador.
