# UP Entregas v2.0.2 — Sistema Visual Unificado

Esta versão corrige a divergência visual que existia entre a Home e as demais páginas.

## Padrão aplicado em todo o app

- fundo navy escuro em gradiente, como a Home;
- cards azul-petróleo com borda azul e profundidade discreta;
- botão principal lime com texto escuro e alto contraste;
- botão secundário escuro com borda legível;
- textos principais claros e textos secundários azul-cinza;
- listas do menu, histórico, carteira, permissões, suporte, perfil, veículo e documentos redesenhadas pelo mesmo sistema visual;
- nenhum texto azul fica sobre fundo azul sem contraste.

## Login e boas-vindas

- logo agora é livre/transparente, sem quadrado branco e sem parecer avatar de usuário;
- tela de login segue o mesmo fundo, cards e botões da Home;
- boas-vindas e login usam o carrossel dinâmico do GADM;
- se não houver imagem ativa no GADM, o fundo navy padrão continua funcionando;
- coleções já aceitas: `app_carousel_banners`, `carrosselApp`, `bannersApp`, `appBanners`, `bannersEntregador`, `carrossel_entregador`, `entregadorBanners`.

## Permissões antes do login

Na primeira entrada, depois da boas-vindas e antes do login/cadastro, o app apresenta uma tela por permissão:

1. notificações;
2. localização;
3. alerta em tela cheia;
4. bateria sem restrição.

Cada tela possui botão para liberar e botão para avançar. A conclusão é salva localmente para não repetir a sequência em toda abertura.

## Cadastro

CPF, telefone e data não exibem mais cartão técnico de prévia. O campo fica livre durante a digitação e a máscara é aplicada dentro do próprio campo somente quando todos os números necessários foram digitados.

## Versão

- `versionCode`: 202
- `versionName`: 2.0.2
