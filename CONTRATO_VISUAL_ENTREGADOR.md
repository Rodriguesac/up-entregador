# Contrato Visual e Funcional — UP Entregas v2.0

## Identidade

- Nome do app: UP Entregas.
- Logo oficial: UP branco com asas amarelas e texto ENTREGAS amarelo.
- Paleta oficial:
  - Azul bandeira / azul forte: #002776
  - Azul escuro: #001B52
  - Amarelo ouro: #FFCC29
  - Branco: #FFFFFF
  - Cinza de fundo: #F4F7FB

## Fluxo inicial

1. Abriu o app.
2. Splash azul com logo central ocupando aproximadamente 35% da largura da tela.
3. Três pontos carregando abaixo da logo.
4. Verifica sessão local/Firebase.
5. Se não fez boas-vindas: mostra tela de boas-vindas.
6. Se não logado: mostra login/cadastro.
7. Se cadastrado e pendente: mostra “Em análise”.
8. Se aprovado no GADM: libera Home.

## Login

- Apenas CPF + senha.
- CPF com máscara 000.000.000-00.
- CPF com validação matemática real.
- Senha numérica, mínimo 7 dígitos.
- Teclado padrão numérico do Android.

## Cadastro tela a tela

1. Nome completo.
2. Telefone / WhatsApp.
3. CPF.
4. Data de nascimento.
5. Veículo: Moto / Carro / Bicicleta.
6. Placa.
7. Marca e modelo.
8. CNH.
9. Selfie pendente para análise.
10. Comprovante pendente para análise.
11. Chave Pix.
12. Senha numérica.

## Validações

- Nome: mínimo 2 nomes, cada nome com pelo menos 2 letras, sem números.
- CPF: cálculo real dos dígitos verificadores.
- Telefone: máscara (67) 99999-9999.
- Senha: numérica, mínimo 7 dígitos.

## GADM

O cadastro grava em:

- `entregadores/{cpf}`
- `solicitacoesEntregadores/{cpf}`

Status inicial:

- `statusCadastro = PENDENTE`
- `statusAprovacao = PENDENTE`
- `aprovado = false`

Quando o GADM aprovar:

- `statusAprovacao = APROVADO`
- `aprovado = true`

O app libera a Home automaticamente.


## v2.0
- Cadastro sem máscara durante digitação para CPF, telefone e data de nascimento.
- CPF validado por cálculo real antes de avançar.
- Telefone digitado livre e formatado como prévia abaixo do campo.
- Pergunta obrigatória: Esse número é WhatsApp? Sim/Não.
- Login CPF + senha numérica com teclado padrão Android.
