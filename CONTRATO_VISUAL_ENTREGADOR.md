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

## Etapa mega visual e permissões
- Cards de permissão devem ser clicáveis.
- Quando houver permissão Android que aceita prompt direto, o app deve chamar o questionário nativo: notificações e localização.
- Bateria deve tentar abrir o pedido nativo de ignorar otimização antes de mandar para configurações.
- Tela Home em restrição deve mostrar botão claro para liberar permissões, sem obrigar o entregador a procurar nas configurações.
- Oferta urgente deve seguir o contrato visual: faixa vermelha, contador, valor forte, mapa e botões Aceitar/Recusar.


## V6.21.5 - Tipografia e escala visual
- Fonte do app usa a família padrão do Android com escala visual travada em 1.0 dentro do app, evitando que a fonte grande do aparelho estoure cards, bottom navigation e títulos.
- Hierarquia ajustada por tipo de uso: títulos, subtítulos, cards, chips, menu inferior, valores e botões.
- Peso da fonte foi suavizado: Black/ExtraBold saiu das telas operacionais; Bold/SemiBold mantém leitura forte sem parecer gigante.


## V6.22.0 - Navy Premium Operacional

Tema oficial aplicado para deixar o app mais profissional e premium:
- Navy / azul petróleo como cor principal do produto.
- Verde reservado para status positivo: disponível, concluído, verificado e ações operacionais.
- Vermelho reservado para urgência, erro e recusa.
- Âmbar reservado para atenção e pendências.
- Fundo cinza claro, cards brancos, bordas suaves e sombras discretas.
- Bottom navigation e componentes globais deixam de usar verde dominante.
- Home, carteira, histórico, mais e permissões seguem a lógica premium com menos poluição visual.


## v6.23.0 Premium Real
- Tema Navy Premium mantido: navy como principal, verde apenas para sucesso/status, vermelho para urgência e âmbar para atenção.
- Home com insights operacionais: demanda, meta diária, ganhos, corrida ativa e atalhos.
- Carrossel real com banners padrão, banners remotos do Firebase, troca automática, arraste manual e indicadores dinâmicos.
- Tela Corridas com estado aguardando corrida premium.
- Carteira com dashboard financeiro estilo fintech.
- Histórico com filtros visuais e chips de status.
- Mais/Perfil com estatísticas profissionais do entregador.
- Tipografia do app com escala fixa e fontScale controlado no tema para não variar conforme o aparelho.
