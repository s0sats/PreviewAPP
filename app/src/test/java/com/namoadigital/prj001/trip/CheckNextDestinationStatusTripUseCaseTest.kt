package com.namoadigital.prj001.trip

import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.core.trip.domain.usecase.CheckNextDestinationStatusTripUseCase
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.TripStatus
import com.namoadigital.prj001.model.trip.toDescription
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub

@RunWith(JUnit4::class)
class CheckNextDestinationStatusTripUseCaseTest {

    private val repository = mock<TripRepository>()
    private val destinationRepository = mock<TripDestinationRepository>()

    @Test
    fun `Start Trip without destination`() {

        repository.stub {
            onBlocking { getTrip() } doReturn fsTripMock.copy(
                tripStatus = TripStatus.PENDING.toDescription()
            )
        }

//        destinationRepository.stub {
//            onBlocking {
//                getDestinationByStatus(
//                    fsTripMock.customerCode,
//                    fsTripMock.tripPrefix,
//                    fsTripMock.tripCode,
//                    DestinationStatus.PENDING
//                )
//            } doReturn destinationMock
//        }

        val checkNextDestinationStatusTripUseCase = CheckNextDestinationStatusTripUseCase(
            repository = repository,
            destinationRepository = destinationRepository
        )

        val output = checkNextDestinationStatusTripUseCase.invoke(
            CheckNextDestinationStatusTripUseCase.Input(
                tripStatus = TripStatus.START)
            )

        assert(output.nextTripStatus == TripStatus.WAITING_DESTINATION.toDescription())
    }
    @Test
    fun `Start Trip with destination`() {

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

        val checkNextDestinationStatusTripUseCase = CheckNextDestinationStatusTripUseCase(
            repository = repository,
            destinationRepository = destinationRepository
        )

        val output = checkNextDestinationStatusTripUseCase.invoke(
            CheckNextDestinationStatusTripUseCase.Input(
                tripStatus = TripStatus.START)
            )

        assert(output.nextTripStatus == TripStatus.TRANSIT.toDescription())
    }

    private val fsTripMock = FSTrip(
        customerCode = 123456789L,
        tripPrefix = 2024,
        tripCode = 1,
        tripUserCode = 303,
        tripUserName = "João Silva",
        tripStatus = TripStatus.PENDING.toDescription(),
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
        ticketPrefix = 2024,
        ticketCode = 1,
        ticketId = "TICKET123",
        destinationStatus = com.namoadigital.prj001.model.trip.DestinationStatus.PENDING.toDescription(),
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