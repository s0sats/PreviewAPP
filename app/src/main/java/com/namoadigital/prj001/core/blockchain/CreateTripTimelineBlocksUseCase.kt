package com.namoadigital.prj001.core.blockchain

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.core.trip.domain.model.blockchain.TimelineBlockType
import com.namoadigital.prj001.core.trip.domain.model.blockchain.TripTimelineBlock
import com.namoadigital.prj001.core.trip.domain.model.enums.TimelineBlockTranslate
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.model.trip.TripStatus
import com.namoadigital.prj001.model.trip.toTripStatus
import javax.inject.Inject

/**
 * Cria uma lista cronológica de blocos de tempo (TripTimelineBlock) com base nos eventos de uma FSTrip.
 * A linha do tempo começa na `originDate` da viagem e lida com blocos em andamento.
 */
class CreateTripTimelineBlocksUseCase @Inject constructor(
    private val tripRepository: TripRepository,
    private val destinationRepository: TripDestinationRepository
) : UseCaseWithoutFlow<Unit, List<TripTimelineBlock>> {

    override operator fun invoke(input: Unit): List<TripTimelineBlock> {
        val trip = tripRepository.getTrip() ?: return emptyList()
        val destinations = destinationRepository.getListDestinations(
            trip.tripPrefix,
            trip.tripCode
        ).filter { it.destinationStatus != DestinationStatus.CANCELLED.name }


        // Se não houver data de criação, não é possível gerar a linha do tempo.
        val originDate = trip.originDate ?: return emptyList()

        val blocks = mutableListOf<TripTimelineBlock>()
        var lastEventDate = originDate
        val tripStatus = trip.tripStatus.toTripStatus()

        // Bloco 1: PRE_TRIP - Da criação ao início da viagem.
        // Viagem iniciada.
        trip.startDate?.takeIf {!it.isNullOrEmpty() }?.let {
            blocks.add(TripTimelineBlock(
                type = TimelineBlockType.PRE_TRIP(
                    startDate = originDate,
                    endDate = it
                ),
                description = TimelineBlockTranslate.CREATE_TRIP_DESCRIPTION.key
            ))
            lastEventDate = it
        } ?: run {
            // Viagem criada, mas não iniciada. Bloco PRE_TRIP em aberto.
            blocks.add(TripTimelineBlock(
                type = TimelineBlockType.PRE_TRIP(
                    startDate = lastEventDate,
                ),
                description = TimelineBlockTranslate.CREATE_TRIP_DESCRIPTION.key
            ))
            return blocks
        }

        val sortedDestinations = destinations.sortedBy { it.destinationSeq }

        for (destination in sortedDestinations) {
            // Bloco de TRANSFERÊNCIA para o destino atual
            // Se não chegou, está em transferência (bloco em aberto).
            destination.arrivedDate?.takeIf {!it.isNullOrEmpty() }?.let {
                blocks.add(
                    TripTimelineBlock(
                        type = TimelineBlockType.TRANSFER(
                            startDate = lastEventDate,
                            endDate = it
                        ),
                    )
                )
                lastEventDate = it
            } ?: run {
                // Em deslocamento para este destino. Bloco TRANSFER em aberto.
                blocks.add(
                    TripTimelineBlock(
                        type = TimelineBlockType.TRANSFER(
                            startDate = lastEventDate,
                        ),
                    )
                )
                return blocks
            }

            // Bloco NO LOCAL (ON_SITE) para o destino atual
            // Se partiu, não está mais no local.
            destination.departedDate?.takeIf {!it.isNullOrEmpty() }?.let {
                blocks.add(
                    TripTimelineBlock(
                        type = TimelineBlockType.ON_SITE(
                            startDate = lastEventDate,
                            endDate = it,
                            destinationSeq = destination.destinationSeq
                        ),
                        description = destination.destinationSiteDesc
                    )
                )
                lastEventDate = it
            } ?: run {
                // Chegou, mas não partiu. Bloco ON_SITE em aberto.
                blocks.add(
                    TripTimelineBlock(
                        type = TimelineBlockType.ON_SITE(
                            startDate = lastEventDate,
                            destinationSeq = destination.destinationSeq
                        ),
                        description = destination.destinationSiteDesc
                    )
                )
                return blocks
            }
        }

        // Bloco Final: Viagem de RETORNO ou VIAGEM FINALIZADA
        trip.doneDate?.takeIf { !it.isNullOrEmpty() }?.let {
            // Viagem finalizada.
            val finalBlockType =
                if (sortedDestinations.isNotEmpty()) TimelineBlockType.RETURN_TRIP(
                    startDate = lastEventDate,
                    endDate = it
                ) else TimelineBlockType.TRANSFER(
                    startDate = lastEventDate,
                    endDate = it
                )
            blocks.add(TripTimelineBlock(
                type = finalBlockType,
                description = TimelineBlockTranslate.END_TRIP_DESCRIPTION.key
            ))
        } ?: run {
            // Se a viagem não foi finalizada, o último bloco é um retorno/transferência em aberto.
            if (tripStatus != TripStatus.DONE) {
                val finalBlockType = if (sortedDestinations.isNotEmpty()) TimelineBlockType.RETURN_TRIP(
                    startDate = lastEventDate
                ) else TimelineBlockType.TRANSFER(
                    startDate = lastEventDate
                )
                blocks.add(TripTimelineBlock(
                    type = finalBlockType,
                ))
            }
        }

        return blocks
    }
}