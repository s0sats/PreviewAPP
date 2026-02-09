package com.namoadigital.prj001.core.trip.domain.model.blockchain

import com.namoadigital.prj001.model.masterdata.ge_os.GeOs
import com.namoadigital.prj001.util.ToolBox_Inf

/**
 * Representa um único bloco de tempo na linha do tempo de uma viagem.
 *
 * @param type O tipo de atividade (ex: Transferência, No Local).
 * @param startDate A data/hora do início do bloco.
 * @param endDate A data/hora do fim do bloco. Nulo significa que o bloco está em andamento.
 * @param destinationSeq Sequencial do destino associado, apenas para blocos ON_SITE.
 */
data class TripTimelineBlock(
    val type: TimelineBlockType,
    val description: String? = null
) {

    /** Converte a data de início para milissegundos. */
    fun startMs(): Long = ToolBox_Inf.dateToMilliseconds(type.startDate)

    /**
     * Converte a data de fim para milissegundos.
     * Caso o bloco esteja em aberto, retorna Long.MAX_VALUE.
     */
    fun endMs(): Long = type.endDate?.let { ToolBox_Inf.dateToMilliseconds(it) } ?: Long.MAX_VALUE
}

/**
 * Define os diferentes tipos de blocos que compõem a linha do tempo.
 */
sealed class TimelineBlockType(
    open val startDate: String,
    open val endDate: String? = null,
    val isBlock: Boolean = false
) {


    fun startMs() = ToolBox_Inf.dateToMilliseconds(startDate)
    fun endMs() = endDate?.let { ToolBox_Inf.dateToMilliseconds(it) }

    /**
     * Bloco que representa um evento pontual, como um abastecimento.
     */
    data class EVENT(
        override val startDate: String,
        override val endDate: String? = null,
        val eventSeq: Int,
        val withWaiting: Boolean
    ) : TimelineBlockType(startDate, endDate)

    /**
     * Bloco que representa o preenchimento de um formulário.
     */
    data class FORM(
        override val startDate: String,
        override val endDate: String? = null,
        val destinationSeq: Int? = null,
        val formPK: GeOs.FormPK?
    ) : TimelineBlockType(startDate, endDate)

    /**
     * Período entre criação da viagem e início efetivo.
     * Início: FSTrip.originDate
     * Fim: FSTrip.startDate
     */
    data class PRE_TRIP(
        override val startDate: String,
        override val endDate: String? = null,
    ) : TimelineBlockType(startDate, endDate, true)

    /**
     * Deslocamento entre origem/último destino e o próximo destino.
     * Início: FSTrip.startDate ou departedDate do destino anterior.
     * Fim: arrivedDate do destino atual.
     */
    data class TRANSFER(
        override val startDate: String,
        override val endDate: String? = null,
    ) : TimelineBlockType(startDate, endDate)

    /**
     * Período de permanência em um destino.
     * Início: arrivedDate.
     * Fim: departedDate.
     */
    data class ON_SITE(
        override val startDate: String,
        override val endDate: String? = null,
        val destinationSeq: Int,
    ) : TimelineBlockType(startDate, endDate, true)

    /**
     * Retorno ao destino final ou finalização da viagem.
     * Início: departedDate do último destino.
     * Fim: doneDate.
     */
    data class RETURN_TRIP(
        override val startDate: String,
        override val endDate: String? = null,
    ) : TimelineBlockType(startDate, endDate, true)
}


/**
 * Indica se o bloco é um tipo de deslocamento ("transit").
 * TRANSFER = indo para um destino
 * RETURN_TRIP = Finalização da Trip
 */
fun TimelineBlockType.isVirtual(): Boolean =
    this is TimelineBlockType.TRANSFER || this is TimelineBlockType.RETURN_TRIP

/**
 * Indica se o bloco é sólido (não é trânsito).
 * Usado quando você quer validar intervalos que precisam
 * pertencer exclusivamente a destinos ou pré-viagem.
 */
fun TimelineBlockType.isReal(): Boolean =
    this is TimelineBlockType.PRE_TRIP || this is TimelineBlockType.ON_SITE
