package com.rodriguesacai.entregador.ui.professional

/**
 * Modelos limpos para a Home profissional do Rodrigues Entregador.
 * A tela NÃO deve renderizar diretamente Map<String, Any> do Firebase.
 * Primeiro converta Firebase -> CorridaUiModel, depois renderize.
 */
enum class CorridaUiStatus {
    SEM_CORRIDA,
    OFERTA_RECEBIDA,
    ACEITA,
    INDO_COLETA,
    CHEGUEI_COLETA,
    AGUARDANDO_SAIDA,
    LIBERADA_PARA_SAIDA,
    INDO_ENTREGA,
    CHEGUEI_ENTREGA,
    OCORRENCIA,
    FINALIZADA,
    CANCELADA
}

data class CorridaUiModel(
    val id: String = "",
    val pedidoId: String = "",
    val codigoRetirada: String = "----",
    val status: CorridaUiStatus = CorridaUiStatus.SEM_CORRIDA,
    val titulo: String = "Pronto para novas corridas",
    val subtitulo: String = "Fique online para receber pedidos.",
    val lojaNome: String = "Rodrigues Açaí e Cia.",
    val lojaEndereco: String = "Endereço da loja",
    val clienteNome: String = "Cliente",
    val entregaEndereco: String = "Endereço protegido até a saída",
    val bairro: String = "",
    val distanciaKm: Double? = null,
    val tempoEstimadoMin: Int? = null,
    val pagamentoLabel: String = "Não informado",
    val pagamentoDescricao: String = "Confirme com o gestor antes de finalizar.",
    val valorCorrida: Double = 0.0,
    val totalPedido: Double = 0.0,
    val quantidadeEntregas: Int = 1,
    val enderecoProtegido: Boolean = true,
    val ocorrenciaAtiva: Boolean = false
)

data class ResumoGanhosUiModel(
    val ganhosHoje: Double = 0.0,
    val corridasHoje: Int = 0,
    val finalizadasHoje: Int = 0,
    val metaDiaria: Double = 150.0,
    val saldoCarteira: Double = 0.0,
    val proximoRepasse: String = "Hoje, 18:00"
)

data class ProfessionalHomeUiState(
    val nomeEntregador: String = "Diego",
    val inicialEntregador: String = "D",
    val online: Boolean = true,
    val mensagemStatus: String = "Pronto para novas corridas",
    val corridaAtual: CorridaUiModel? = null,
    val resumo: ResumoGanhosUiModel = ResumoGanhosUiModel(),
    val demandaAlta: Boolean = true,
    val carregando: Boolean = false,
    val erro: String? = null
) {
    companion object {
        fun preview() = ProfessionalHomeUiState(
            nomeEntregador = "Diego",
            inicialEntregador = "D",
            online = true,
            mensagemStatus = "Pronto para novas corridas",
            corridaAtual = CorridaUiModel(
                id = "corrida_preview",
                pedidoId = "1158",
                codigoRetirada = "1158",
                status = CorridaUiStatus.ACEITA,
                titulo = "Corrida em andamento",
                subtitulo = "Siga para a coleta e faça uma ótima entrega.",
                lojaNome = "Rodrigues Açaí e Cia.",
                lojaEndereco = "Av. Brasil, 1234 • Centro",
                clienteNome = "João Silva",
                entregaEndereco = "Rua das Flores, 567 • Mata do Jacinto",
                bairro = "Mata do Jacinto",
                distanciaKm = 5.7,
                tempoEstimadoMin = 18,
                pagamentoLabel = "Online (Pix)",
                pagamentoDescricao = "Nada a cobrar do cliente.",
                valorCorrida = 6.0,
                totalPedido = 56.30,
                quantidadeEntregas = 1,
                enderecoProtegido = false,
                ocorrenciaAtiva = false
            ),
            resumo = ResumoGanhosUiModel(
                ganhosHoje = 246.80,
                corridasHoje = 8,
                finalizadasHoje = 6,
                metaDiaria = 300.0,
                saldoCarteira = 126.75,
                proximoRepasse = "Hoje, 18:00"
            ),
            demandaAlta = true
        )
    }
}
