package com.rodriguesacai.entregador.ui.professional

/**
 * Conversor defensivo para evitar a tela mostrar "pendente", "—" e "Não informado" sem necessidade.
 * Use este mapper no Repository/ViewModel após ler o documento do Firestore.
 */
object CorridaStatusMapper {

    fun mapStatus(raw: Any?): CorridaUiStatus {
        val s = raw?.toString()?.trim()?.uppercase().orEmpty()

        return when {
            s.isBlank() -> CorridaUiStatus.SEM_CORRIDA
            s.contains("OFERTA") || s.contains("NOVA") || s.contains("DISPONIVEL") -> CorridaUiStatus.OFERTA_RECEBIDA
            s == "ACEITA" || s.contains("ACEITO") -> CorridaUiStatus.ACEITA
            s.contains("INDO_COLETA") || s.contains("COLETANDO") -> CorridaUiStatus.INDO_COLETA
            s.contains("CHEGUEI_COLETA") -> CorridaUiStatus.CHEGUEI_COLETA
            s.contains("AGUARDANDO_SAIDA") || s.contains("AGUARDANDO GESTOR") -> CorridaUiStatus.AGUARDANDO_SAIDA
            s.contains("LIBERADA_PARA_SAIDA") || s.contains("SAIDA_LIBERADA") -> CorridaUiStatus.LIBERADA_PARA_SAIDA
            s.contains("INDO_ENTREGA") || s.contains("EM_ROTA") -> CorridaUiStatus.INDO_ENTREGA
            s.contains("CHEGUEI_ENTREGA") || s.contains("NO_LOCAL") -> CorridaUiStatus.CHEGUEI_ENTREGA
            s.contains("OCORRENCIA") -> CorridaUiStatus.OCORRENCIA
            s.contains("FINAL") || s.contains("ENTREGUE") -> CorridaUiStatus.FINALIZADA
            s.contains("CANCEL") -> CorridaUiStatus.CANCELADA
            else -> CorridaUiStatus.ACEITA
        }
    }

    fun fromFirestore(id: String, data: Map<String, Any?>): CorridaUiModel {
        val statusRaw = first(data, "status", "statusCorrida", "statusEntrega", "statusPedido", "statusAtual")
        val status = mapStatus(statusRaw)
        val codigo = string(data, "codigoRetirada", "codigoEntrega", "codigoCurto", "codigoPedido", "numeroPedido").takeIf { it.isNotBlank() } ?: "----"
        val formaPagamento = string(data, "pagamentoLabel", "formaPagamento", "metodoPagamento", "paymentMethod").ifBlank { "Não informado" }
        val enderecoProtegido = bool(data, "enderecoProtegido") ?: (status.ordinal < CorridaUiStatus.LIBERADA_PARA_SAIDA.ordinal)
        val ocorrenciaAtiva = bool(data, "ocorrenciaAtiva") == true || bool(data, "ocorrenciaAberta") == true || status == CorridaUiStatus.OCORRENCIA

        return CorridaUiModel(
            id = id,
            pedidoId = string(data, "pedidoId", "orderId", "idPedido"),
            codigoRetirada = codigo.onlyShortCode(),
            status = status,
            titulo = titleFor(status, ocorrenciaAtiva),
            subtitulo = subtitleFor(status, ocorrenciaAtiva),
            lojaNome = string(data, "lojaNome", "storeName", "nomeLoja").ifBlank { "Rodrigues Açaí e Cia." },
            lojaEndereco = string(data, "lojaEndereco", "storeAddress", "pickupAddress", "enderecoLoja").ifBlank { "Endereço da loja" },
            clienteNome = string(data, "clienteNome", "customerName", "nomeCliente").ifBlank { "Cliente" },
            entregaEndereco = if (enderecoProtegido) {
                "Endereço protegido até a saída"
            } else {
                string(data, "deliveryAddress", "enderecoEntrega", "enderecoCompleto", "clienteEnderecoCompleto", "destinoEndereco")
                    .ifBlank { "Endereço de entrega" }
            },
            bairro = string(data, "bairro", "bairroEntrega", "clienteBairro"),
            distanciaKm = double(data, "distanciaKm", "distancia"),
            tempoEstimadoMin = int(data, "tempoEstimadoMin", "tempoMin"),
            pagamentoLabel = pagamentoLabel(formaPagamento),
            pagamentoDescricao = pagamentoDescription(formaPagamento),
            valorCorrida = double(data, "valorCorrida", "valorEntrega", "valorRota", "taxaEntrega", "repasseEntregador") ?: 0.0,
            totalPedido = double(data, "totalPedido", "valorPedido", "valorTotal", "total") ?: 0.0,
            quantidadeEntregas = int(data, "quantidadeEntregas", "entregas", "qtdEntregas") ?: 1,
            enderecoProtegido = enderecoProtegido,
            ocorrenciaAtiva = ocorrenciaAtiva
        )
    }

    private fun titleFor(status: CorridaUiStatus, ocorrencia: Boolean): String {
        if (ocorrencia) return "Ocorrência em análise"
        return when (status) {
            CorridaUiStatus.OFERTA_RECEBIDA -> "Nova corrida disponível"
            CorridaUiStatus.ACEITA,
            CorridaUiStatus.INDO_COLETA,
            CorridaUiStatus.CHEGUEI_COLETA,
            CorridaUiStatus.AGUARDANDO_SAIDA -> "Corrida em andamento"
            CorridaUiStatus.LIBERADA_PARA_SAIDA,
            CorridaUiStatus.INDO_ENTREGA,
            CorridaUiStatus.CHEGUEI_ENTREGA -> "Entrega em andamento"
            CorridaUiStatus.FINALIZADA -> "Entrega finalizada"
            CorridaUiStatus.CANCELADA -> "Corrida cancelada"
            else -> "Pronto para novas corridas"
        }
    }

    private fun subtitleFor(status: CorridaUiStatus, ocorrencia: Boolean): String {
        if (ocorrencia) return "Aguarde o gestor resolver antes de continuar."
        return when (status) {
            CorridaUiStatus.OFERTA_RECEBIDA -> "Confira os detalhes antes de aceitar."
            CorridaUiStatus.AGUARDANDO_SAIDA -> "Mostre o código e aguarde a saída do gestor."
            CorridaUiStatus.LIBERADA_PARA_SAIDA -> "Saída liberada. Siga para o cliente."
            CorridaUiStatus.INDO_ENTREGA -> "Siga até o endereço do cliente."
            CorridaUiStatus.FINALIZADA -> "Corrida concluída com sucesso."
            else -> "Siga para a coleta e faça uma ótima entrega."
        }
    }

    private fun pagamentoLabel(raw: String): String {
        val p = raw.uppercase()
        return when {
            p.contains("PIX") -> "Online (Pix)"
            p.contains("ONLINE") -> "Pago online"
            p.contains("DINHEIRO") -> "Dinheiro"
            p.contains("MAQUIN") -> "Maquininha"
            p.contains("CART") -> "Cartão"
            raw.isBlank() -> "Não informado"
            else -> raw
        }
    }

    private fun pagamentoDescription(raw: String): String {
        val p = raw.uppercase()
        return when {
            p.contains("PIX") || p.contains("ONLINE") -> "Nada a cobrar do cliente."
            p.contains("DINHEIRO") -> "Cobrar no ato da entrega."
            p.contains("MAQUIN") || p.contains("CART") -> "Levar maquininha para receber."
            else -> "Confirme com o gestor antes de finalizar."
        }
    }

    private fun first(data: Map<String, Any?>, vararg keys: String): Any? = keys.firstNotNullOfOrNull { data[it] }

    private fun string(data: Map<String, Any?>, vararg keys: String): String =
        keys.firstNotNullOfOrNull { data[it]?.toString()?.trim()?.takeIf { v -> v.isNotBlank() } }.orEmpty()

    private fun double(data: Map<String, Any?>, vararg keys: String): Double? =
        keys.firstNotNullOfOrNull { key ->
            when (val v = data[key]) {
                is Number -> v.toDouble()
                is String -> v.replace(",", ".").toDoubleOrNull()
                else -> null
            }
        }

    private fun int(data: Map<String, Any?>, vararg keys: String): Int? =
        keys.firstNotNullOfOrNull { key ->
            when (val v = data[key]) {
                is Number -> v.toInt()
                is String -> v.toIntOrNull()
                else -> null
            }
        }

    private fun bool(data: Map<String, Any?>, key: String): Boolean? =
        when (val v = data[key]) {
            is Boolean -> v
            is String -> v.equals("true", true)
            else -> null
        }

    private fun String.onlyShortCode(): String {
        val digits = filter { it.isDigit() }
        return when {
            length <= 6 -> this
            digits.length >= 4 -> digits.takeLast(4)
            else -> takeLast(4)
        }
    }
}
