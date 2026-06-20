# UP Entregas v2.0.1 — Produto Completo do Entregador

Esta versão muda a direção do projeto: deixa de ser pacote de hotfix e passa a organizar o app como produto operacional completo.

## Objetivo

O entregador precisa baixar, entrar, entender a situação e trabalhar sem depender de explicação manual.

## Bases mantidas

- Home no visual aprovado pelo usuário.
- Correção da ocorrência presa da v1.1.3.
- Estabilidade operacional da v1.2.0.
- Separação entre taxa do cliente e repasse do entregador.
- Modal do GADM com ação.

## Evoluções da v2.0

- Versão Android atualizada para 2.0.1.
- Mais telas no menu Mais: Veículo, Documentos, Comando GADM e Pronto para operar.
- Textos do app atualizados para a lógica de produto, não apenas tela isolada.
- Tela de aguardando corrida removendo termos fracos como região/demanda e reforçando GPS, alertas e contrato operacional.
- Checklist interno no app mostrando se a versão está preparada para operação real.
- Suporte atualizado para exibir a versão correta.

## Contrato operacional

- GADM comanda.
- App Entregador executa.
- Firebase registra.
- Cliente acompanha.

## Regra de status

Não podem voltar como corrida ativa:

- recusada
- expirada
- cancelada
- concluída
- entregue
- ocorrência resolvida
- ocorrência cancelada

## Financeiro

Campos de repasse aceitos:

- valorCorrida
- valorRepasseEntregador
- valorRepasseMotoboy
- repasseFrota
- repassePiloto
- valorTotalMotoboy

Campos que não são repasse:

- taxaEntrega
- frete
- valorEntrega
- taxaCliente

## Próxima etapa real

Depois da v2.0 instalada, a parte web/GADM deve receber os botões oficiais:

- Resolver ocorrência
- Cancelar corrida
- Liberar entregador
- Liberar saída
- Reenviar corrida
- Solicitar correção de documento
