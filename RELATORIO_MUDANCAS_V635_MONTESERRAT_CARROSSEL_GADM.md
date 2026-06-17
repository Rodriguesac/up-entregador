# Relatório v6.35.0 — Montserrat + Carrossel GADM + Tipografia

## Alterações reais
- VersionCode atualizado para 880.
- VersionName atualizado para `6.35.0-montserrat-carrossel-gadm`.
- `AppVersion.kt` atualizado para exibir `UP Entregador • v6.35.0` no Login/Boas-vindas e `Versão 6.35.0 • Montserrat + Carrossel GADM` nas telas internas.
- Fonte oficial configurada como Montserrat via Google Fonts Provider no `RodriguesFonts.kt`.
- Escala global de fonte ajustada para 1.18 e textos comuns aumentados no código para leitura melhor sem ficar desproporcional.
- Bottom navigation, cabeçalho, cards, menu Mais, banners, preferências e componentes principais receberam tamanhos mais fortes e legíveis.
- Carrossel ampliado para buscar coleções compatíveis com GADM/Gestor: `gadm_app_carousel`, `gadmBanners`, `banners_gadm`, `gestor_banners`, `carrosselGestor`, `configAppBanners`, `mobile_banners`, `entregador_carrossel`, além das coleções anteriores.
- Banners agora aceitam campos do GADM: imagem, modo visual, cor de fundo, cor de destaque, cor de texto, ícone, título, descrição, badge e botão.
- Visual ajustado para reduzir excesso de amarelo, mantendo verde-lima e azul elétrico como identidade principal.
- Mapa atualizado para a paleta oficial: verde #B7E51E, azul #1E4FFF e alerta #FFB020.

## Campos aceitos no carrossel/GADM
- `title`, `titulo`, `nome`, `headline`
- `description`, `descricao`, `descrição`, `subtitle`, `subtitulo`, `texto`, `mensagem`
- `imageUrl`, `imagemUrl`, `urlImagem`, `image`, `imagem`, `bannerUrl`, `fotoUrl`
- `badge`, `selo`, `tag`, `categoria`
- `buttonText`, `ctaText`, `textoBotao`, `textoBotão`, `cta`
- `backgroundColor`, `bgColor`, `corFundo`, `fundo`, `background`, `cardColor`
- `accentColor`, `corDestaque`, `corPrimaria`, `primaryColor`, `accent`, `corBotao`
- `textColor`, `corTexto`, `foreground`, `titleColor`
- `displayMode`, `modoExibicao`, `modoExibição`, `layout`, `bannerMode`
- `imageOnly`, `somenteImagem`, `soImagem`, `apenasImagem`, `artePronta`

## Pendência honesta
A fonte Montserrat foi configurada por Google Fonts Provider, sem embutir arquivo `.ttf/.otf` no ZIP. Arquivo de fonte não foi incluído no pacote.
O build final precisa ser validado pelo GitHub Actions.
