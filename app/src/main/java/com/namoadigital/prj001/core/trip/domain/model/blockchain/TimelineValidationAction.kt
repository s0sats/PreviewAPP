package com.namoadigital.prj001.core.trip.domain.model.blockchain

import com.namoadigital.prj001.model.masterdata.ge_os.GeOs

/**
 * Define a ação que está sendo validada na linha do tempo.
 * É usado como o Input principal para o [com.namoadigital.prj001.core.blockchain.ValidateTimelineBlockUseCase].
 */
sealed class TimelineValidationAction {

    /**
     * Ação para validar a edição das datas de um Destino (um marco estrutural).
     * @param destinationSeq O identificador único do destino que está sendo editado.
     * @param newArrivedDate A nova data de chegada proposta.
     * @param newDepartedDate A nova data de partida proposta (pode ser nula).
     */
    data class DestinationEdit(
        val destinationSeq: Int,
        val newArrivedDate: String,
        val newDepartedDate: String?
    ) : TimelineValidationAction()

    /**
     * Ação para validar a edição da data de início da viagem.
     */
    data class TripStartDateEdit(
        val newStartDate: String
    ) : TimelineValidationAction()

    data class TripEndDateEdit(
        val newEndDate: String
    ) : TimelineValidationAction()

    data class TripOriginDateEdit(
        val newOriginDate: String,
    ) : TimelineValidationAction()

    data class ValidateEvent(
        val startDate: String,
        val endDate: String?,
        val eventSeq: Int,
        val withWaiting: Boolean,
    ) : TimelineValidationAction()

    data class ValidateForm(
        val startDate: String,
        val endDate: String?,
        val formPK: GeOs.FormPK? = null
    ) : TimelineValidationAction()





}