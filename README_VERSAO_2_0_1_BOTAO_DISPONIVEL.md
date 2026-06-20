# UP Entregas v2.0.1 — Botão Disponível/Indisponível

Correção imediata da v2.0.0.

## O que mudou

- Home agora mostra card operacional quando não existe corrida ativa ou oferta pendente.
- Se o entregador estiver offline, aparece botão grande: **Ficar disponível**.
- Se o entregador estiver online, aparece botão: **Ficar indisponível**.
- A aba Corridas também recebeu o botão, sem mandar o usuário voltar para Home.
- O botão grava no Firebase:
  - `online`
  - `status`
  - `statusOnline`
  - `statusOperacional`
  - `aceitaNovasOfertas`
- Versão Android atualizada para 2.0.1.

## Observação

Esta correção é obrigatória porque o entregador precisa conseguir entrar e sair da operação sem depender do GADM e sem ficar procurando função escondida.
