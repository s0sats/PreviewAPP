package com.namoadigital.prj001.trip

import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.core.trip.domain.usecase.destination.CheckNextStatusTripUseCase
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.TripStatus
import com.namoadigital.prj001.model.trip.toDescription
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub


@RunWith(JUnit4::class)
class CheckNextStatusTripUseCaseTest {


    private val repository = mock<TripRepository>()
    private val destinationRepository = mock<TripDestinationRepository>()

    @Test
    fun `Caso o status da Trip seja TRANSIT e passa DestinationStatus CANCELLED deve retornar WAITING_DESTINATION`() {
        repository.stub {
            onBlocking { getTrip() } doReturn fsTripMock.copy(
                tripStatus = TripStatus.TRANSIT.toDescription()
            )
        }

        val useCase = CheckNextStatusTripUseCase(
            repository,
            destinationRepository
        ).invoke(DestinationStatus.CANCELLED)

        assertEquals(TripStatus.WAITING_DESTINATION, useCase)
    }


    @Test
    fun `Caso o status da Trip seja TRANSIT e passa DestinationStatus ARRIVED deve retornar ON_SITE`() {
        repository.stub {
            onBlocking { getTrip() } doReturn fsTripMock.copy(
                tripStatus = TripStatus.TRANSIT.toDescription()
            )
        }

        val useCase = CheckNextStatusTripUseCase(
            repository,
            destinationRepository
        ).invoke(DestinationStatus.ARRIVED)

        assertEquals(TripStatus.ON_SITE, useCase)
    }


    @Test
    fun `Caso o status da Trip seja ON_SITE e passa DestinationStatus DEPARTED e não tenha próximo destino deve retornar WAITING_DESTINATION`() {
        repository.stub {
            onBlocking { getTrip() } doReturn fsTripMock.copy(
                tripStatus = TripStatus.ON_SITE.toDescription()
            )
        }

        val useCase = CheckNextStatusTripUseCase(
            repository,
            destinationRepository
        ).invoke(DestinationStatus.DEPARTED)

        assertEquals(TripStatus.WAITING_DESTINATION, useCase)
    }


    @Test
    fun `Caso o status da Trip seja ON_SITE e passa DestinationStatus DEPARTED e tenha próximo destino deve retornar TRANSIT`() {
        repository.stub {
            onBlocking { getTrip() } doReturn fsTripMock.copy(
                tripStatus = TripStatus.ON_SITE.toDescription()
            )
        }

        destinationRepository.stub {
            onBlocking {
                getDestinationByStatus(
                    fsTripMock.customerCode,
                    fsTripMock.tripPrefix,
                    fsTripMock.tripCode,
                    DestinationStatus.PENDING
                )
            } doReturn destinationMock
        }

        val useCase = CheckNextStatusTripUseCase(
            repository,
            destinationRepository
        ).invoke(DestinationStatus.DEPARTED)

        assertEquals(TripStatus.TRANSIT, useCase)
    }

    @Test
    fun `Caso o status da Trip seja OVER_NIGHT e passa DestinationStatus DEPARTED e não tenha próximo destino deve retornar WAITING_DESTINATION`() {
        repository.stub {
            onBlocking { getTrip() } doReturn fsTripMock.copy(
                tripStatus = TripStatus.OVER_NIGHT.toDescription()
            )
        }

        val useCase = CheckNextStatusTripUseCase(
            repository,
            destinationRepository
        ).invoke(DestinationStatus.DEPARTED)

        assertEquals(TripStatus.WAITING_DESTINATION, useCase)
    }


    @Test
    fun `Caso o status da Trip seja OVER_NIGHT e passa DestinationStatus DEPARTED e tenha próximo destino deve retornar TRANSIT`() {
        repository.stub {
            onBlocking { getTrip() } doReturn fsTripMock.copy(
                tripStatus = TripStatus.OVER_NIGHT.toDescription()
            )
        }

        destinationRepository.stub {
            onBlocking {
                getDestinationByStatus(
                    fsTripMock.customerCode,
                    fsTripMock.tripPrefix,
                    fsTripMock.tripCode,
                    DestinationStatus.PENDING
                )
            } doReturn destinationMock
        }

        val useCase = CheckNextStatusTripUseCase(
            repository,
            destinationRepository
        ).invoke(DestinationStatus.DEPARTED)

        assertEquals(TripStatus.TRANSIT, useCase)
    }

    @Test
    fun `Caso o status da Trip seja PENDING e passa DestinationStatus DEPARTED e não tenha próximo destino deve retornar WAITING_DESTINATION`() {
        repository.stub {
            onBlocking { getTrip() } doReturn fsTripMock.copy(
                tripStatus = TripStatus.PENDING.toDescription()
            )
        }

        val useCase = CheckNextStatusTripUseCase(
            repository,
            destinationRepository
        ).invoke(DestinationStatus.PENDING)

        assertEquals(TripStatus.WAITING_DESTINATION, useCase)
    }


    @Test
    fun `Caso o status da Trip seja PENDING e passa DestinationStatus DEPARTED e tenha próximo destino deve retornar TRANSIT`() {
        repository.stub {
            onBlocking { getTrip() } doReturn fsTripMock.copy(
                tripStatus = TripStatus.PENDING.toDescription()
            )
        }

        destinationRepository.stub {
            onBlocking {
                getDestinationByStatus(
                    fsTripMock.customerCode,
                    fsTripMock.tripPrefix,
                    fsTripMock.tripCode,
                    DestinationStatus.PENDING
                )
            } doReturn destinationMock
        }

        val useCase = CheckNextStatusTripUseCase(
            repository,
            destinationRepository
        ).invoke(DestinationStatus.PENDING)

        assertEquals(TripStatus.TRANSIT, useCase)
    }


    private val fsTripMock = FSTrip(
        customerCode = 123456789L,
        tripPrefix = 101,
        tripCode = 202,
        tripUserCode = 303,
        tripUserName = "João Silva",
        tripStatus = TripStatus.TRANSIT.toDescription(),
        fleetLicencePlate = "ABC1234",
        fleetStartOdometer = 50000L,
        fleetStartPhoto = "http://exemplo.com/foto_inicio.jpg",
        fleetStartPhotoName = "foto_inicio.jpg",
        fleetStartPhotoLocal = "local_foto_inicio",
        fleetEndOdometer = 50500L,
        fleetEndPhoto = "http://exemplo.com/foto_fim.jpg",
        fleetEndPhotoName = "foto_fim.jpg",
        fleetEndPhotoLocal = "local_foto_fim",
        originType = "DEPOSITO",
        originSiteCode = 404,
        originSiteDesc = "Depósito Central",
        originLat = -23.550520,
        originLon = -46.633308,
        originDate = "2024-05-28T10:00:00Z",
        positionLat = -23.551112,
        positionLon = -46.632334,
        positionDistanceMin = 2.5,
        requireDestinationFleetData = 1,
        positionDate = "2024-05-28T10:05:00Z",
        syncRequired = 0,
        scn = 505,
        requireFleetData = 1,
        distanceRefMinutes = 5,
        distanceRefMinutesTrans = 10,
        doneDate = "2024-05-28T11:00:00Z",
        users = mutableListOf(),
        events = mutableListOf(),
        destinations = mutableListOf()
    )

    private val destinationMock = FsTripDestination(
        destinationSeq = 1,
        destinationType = "SITE",
        destinationSiteCode = 101,
        destinationSiteDesc = "Centro de Distribuição",
        destinationRegionCode = 5,
        destinationRegionDesc = "Região Metropolitana",
        ticketPrefix = 202,
        ticketCode = 303,
        ticketId = "TICKET123",
        destinationStatus = DestinationStatus.PENDING.toDescription(),
        latitude = -23.550520,
        longitude = -46.633308,
        arrivedDate = "2024-05-28T10:30:00Z",
        arrivedLat = -23.551112,
        arrivedLon = -46.632334,
        arrivedType = "Depósito",
        arrivedFleetOdometer = 50500L,
        arrivedFleetPhoto = "http://exemplo.com/foto_chegada.jpg",
        arrivedFleetPhotoName = "foto_chegada.jpg",
        departedDate = "2024-05-28T11:00:00Z",
        departedLat = -23.552223,
        departedLon = -46.631360,
        departedType = "Cliente",
        countryId = "BR",
        state = "SP",
        city = "São Paulo",
        district = "Centro",
        street = "Rua Exemplo",
        streetnumber = "123",
        complement = "Apto 456",
        zipCode = "01234-567",
        plusCode = "ABC123",
        contactName = "Maria Silva",
        contactPhone = "+55 11 98765-4321",
        siteMainUser = 777,
        minDate = "2024-05-28T10:00:00Z",
        serialCnt = 2,
        actions = mutableListOf()
    )


}