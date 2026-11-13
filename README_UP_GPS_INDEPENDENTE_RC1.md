# UP Entregas — GPS e corridas independentes (2.0.3-rc1)

## Objetivo do incremento
Remover a dependencia do Firestore legado para a localizacao de corridas do
Supabase UP, sem modificar as corridas e pedidos reais existentes.

## Implementado nesta branch
- Cliente Android para `driver-api-independent`, com Firebase ID token verificado no servidor,
  HTTPS, timeout e tratamento de erro.
- Um segundo modo de entrada por e-mail/senha Firebase Auth, sem alterar
  o login antigo por CPF. O backend exige entregador `APPROVED`.
- Leitura periodica, a cada dez segundos, das ofertas e corridas da API independente.
- Aceite, rejeicao, expiracao, chegada a loja, chegada ao cliente e ocorrencia
  roteados a API, sem gravacao concorrente de status no Firestore antigo.
- Quando a corrida tem ID `supabase:UUID`, GPS passa a ser enviado apenas a
  `driver-api-independent`, que valida identidade, vinculo e status da corrida.
- Cadastro, historico e financeiro legados permanecem inalterados.
- Finalizacao de corrida UP independente bloqueada ate codigo/comprovante e
  valores de recebimento serem enviados e validados com seguranca.
- Os dois bancos permanecem separados.

## Limitacoes e criterios de liberacao
**ESTA VERSAO AINDA NAO E PARA PUBLICAR A ENTREGADORES.** Nao houve
validacao de ponta a ponta por celular autenticado; o fluxo de registro
Firebase Auth independente e codigo de retirada/entrega ainda exige integracao.
O backend independente pode exigir `UP_FIREBASE_PROJECT_ID` compativel com
o `google-services.json` Android, alem das respectivas flags de ativacao.

Testes pendentes:
1. Compilacao Gradle com Android SDK (CI nesta PR).
2. Login real Firebase do entregador aprovado e token aceito pelo UP.
3. Uma oferta autorizada, aceite, chegada a loja e recebimento da nova corrida.
4. Localizacao Android em primeiro e segundo plano, coordenadas no Supabase UP,
   e `up-customer-tracking` sem mostrar dados antes da retirada.
5. Protecao de posse (entregador B nao pode alterar corrida de A).
6. Confirmacao de retirada por codigo, confirmacao de entrega, pagamento e
   retorno de status para loja, sem dados ficticios ou valores gerados automaticamente.
7. Retirar os componentes legados restantes apos reconciliacao comprovada.

## Seguranca
Nao ha service_role ou secrets no APK. O cliente nao aceita UID enviado
pela interface como criterio de autorizacao e nao pode atualizar GPS de
corrida nao atribuida. A API de clientes continua separada e protegida por
ticket de curta duracao.
