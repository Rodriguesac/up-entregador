# UP Entregas v1.2.0 — Estabilidade Operacional

Esta versão parte da v1.1.3 e consolida o app para operação real com GADM.

## Ajustes incluídos

- Versão atualizada para 1.2.0.
- Mantém Home visual aprovada: azul escuro, lime/neon, corrida ativa grande e menu inferior.
- Corrige layout do resumo de corridas para não quebrar texto em telas menores.
- Fortalece leitura de status terminal/resolvido para evitar ocorrência antiga continuar presa.
- Mantém hotfix de ocorrência presa da v1.1.3.
- Mantém modal controlado pelo GADM com ação de abrir área correta do app.
- Mantém separação financeira: taxa cobrada do cliente não é repasse do entregador.
- Mantém cadastro sem máscara durante digitação e validação limpa.

## Contrato operacional mantido

- O GADM cria/oferta/encerra/destrava.
- O app executa e registra.
- Ocorrência resolvida/cancelada não deve ser exibida como corrida ativa.
- Rejeição e expiração não prendem entregador.
- Corrida ativa só aparece quando houver missão real ativa para o entregador logado.

## Comando de build

O GitHub Actions continuará usando:

```bash
gradle :app:assembleDebug --stacktrace
```
