# Contrato visual e operacional — UP Entregador

Este pacote é a versão **6.30.0-ideal-premium-home-operacional** do app nativo Android do entregador, em Kotlin + Jetpack Compose.

## Regra visual oficial
- Produto: **UP Entregador**.
- Cor principal: vermelho operacional `#EA1D2C`, com gradiente vermelho/laranja em áreas fortes.
- Verde fica reservado para status positivo, disponível, concluído e valores recebidos.
- Laranja fica reservado para atenção, pico, pendências e ocorrências.
- Fundo claro, cards brancos, cantos arredondados, sombras discretas e texto grande/legível.
- Bottom navigation oficial: **Início, Corridas, Carteira, Histórico, Mais**.
- Telas devem ser possíveis em Compose real, sem mockup impossível, sem textos técnicos para o entregador.

## Fluxo operacional oficial
1. Login / Solicitar cadastro / Cadastro em análise / Criar senha.
2. Permissões do app: notificações, localização, alerta urgente, bateria e Internet/GPS.
3. Home operacional: disponibilidade, ganhos de hoje, corridas, finalizadas, demanda, meta, carrossel e atalhos.
4. Corridas: aguardando corrida quando disponível.
5. Nova corrida urgente: contador, valor, coleta, entrega, distância, tempo, mapa, pagamento, aceitar/recusar.
6. Corrida em andamento: rota ativa, mapa, paradas, pagamento, navegação, próxima ação e ocorrência.
7. Histórico: filtros e lista de concluídas, recusadas, ocorrências e expiradas.
8. Carteira: saldo, pendente, total a receber, resumo financeiro, Pix/Banco e últimos repasses.
9. Perfil/Mais: dados, Pix/Banco, preferências de operação, permissões, suporte/destravar e sair.

## Integração GADM
- O app continua escutando ofertas e rotas do GADM nas estruturas já compatíveis: `rides`, `corridas`, `rotas_entrega` e `pedidos`.
- A oferta deve chegar como missão liberada ao entregador e aparecer na tela urgente.
- Aceitar, recusar, expirar, coletar, sair para entrega, ocorrência e finalizar sincronizam espelhos quando há referência.
- A taxa de entrega cobrada do cliente continua separada do repasse do entregador. O app mostra repasse/corrida apenas quando vier dos campos próprios de entregador.

## Ajustes desta entrega
- Branding alterado para **UP Entregador**.
- Ícone real do UP adicionado como launcher e logo interno.
- Login redesenhado com hero vermelho e logo UP.
- Contrato visual atualizado para vermelho operacional, não verde/navy antigo.
- Versão atualizada para `6.30.0-ideal-premium-home-operacional`.
- Workflow de GitHub Actions incluído para gerar APK debug ao commitar.
