# Rodrigues Entregador — Home Profissional em Jetpack Compose

Este pacote cria uma Home profissional no padrão visual aprovado:

- Fundo navy premium.
- Cards brancos limpos.
- Verde-lima como destaque.
- Métricas de ganhos, corridas e finalizadas.
- Carteira/repasse.
- Card de corrida atual com coleta, entrega, distância, tempo e pagamento.
- Botão de aceitar/recusar quando status = OFERTA_RECEBIDA.
- Tratamento de status para evitar tela presa em ocorrência sem necessidade.

## Onde colocar

Copie a pasta:

```text
app/src/main/java/com/rodriguesacai/entregador/ui/professional
```

para dentro do projeto Android do app entregador.

## Como testar rápido

No arquivo onde hoje você chama a Home antiga, use temporariamente:

```kotlin
import com.rodriguesacai.entregador.ui.professional.ProfessionalHomeScreen
import com.rodriguesacai.entregador.ui.professional.ProfessionalHomeUiState

ProfessionalHomeScreen(
    state = ProfessionalHomeUiState.preview(),
    onOpenDetails = { corrida ->
        // abrir detalhes da corrida
    },
    onAcceptRide = { corrida ->
        // aceitar corrida no Firebase
    },
    onRejectRide = { corrida ->
        // recusar corrida no Firebase
    },
    onNavigate = { aba ->
        // navegar: Início, Corridas, Carteira, Histórico, Mais
    }
)
```

## Como ligar no Firebase real

Depois de ler o documento da coleção `corridas`, converta assim:

```kotlin
import com.rodriguesacai.entregador.ui.professional.CorridaStatusMapper

val corridaUi = CorridaStatusMapper.fromFirestore(
    id = document.id,
    data = document.data
)
```

Depois monte o state:

```kotlin
val state = ProfessionalHomeUiState(
    nomeEntregador = "Diego",
    inicialEntregador = "D",
    online = true,
    mensagemStatus = if (corridaUi.ocorrenciaAtiva) "Ocorrência em análise" else "Pronto para novas corridas",
    corridaAtual = corridaUi,
    resumo = ResumoGanhosUiModel(
        ganhosHoje = 246.80,
        corridasHoje = 8,
        finalizadasHoje = 6,
        metaDiaria = 300.00,
        saldoCarteira = 126.75,
        proximoRepasse = "Hoje, 18:00"
    )
)
```

## Importante

Esta entrega é a camada visual e o mapper de status. Para ficar 100% funcionando, o ViewModel atual do app precisa alimentar `ProfessionalHomeUiState` com os dados reais do Firebase.
