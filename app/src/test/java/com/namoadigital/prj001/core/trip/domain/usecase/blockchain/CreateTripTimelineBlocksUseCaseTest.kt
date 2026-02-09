package com.namoadigital.prj001.core.trip.domain.usecase.blockchain

import com.google.gson.GsonBuilder
import com.namoadigital.prj001.core.blockchain.CreateTripTimelineBlocksUseCase
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.core.trip.domain.model.blockchain.TimelineBlockType
import com.namoadigital.prj001.core.trip.domain.model.blockchain.TripTimelineBlock
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.TripStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.io.File
import java.text.Normalizer

class CreateTripTimelineBlocksUseCaseTest {

    @Mock
    private lateinit var tripRepository: TripRepository

    @Mock
    private lateinit var destinationRepository: TripDestinationRepository

    private lateinit var createTripTimelineBlocksUseCase: CreateTripTimelineBlocksUseCase

    private val gson = GsonBuilder().setPrettyPrinting().create()
    private val baseOutputDir = File("build/reports/tests")

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        createTripTimelineBlocksUseCase =
            CreateTripTimelineBlocksUseCase(tripRepository, destinationRepository)
        baseOutputDir.mkdirs()
    }

    // helpers para criar dados de teste
    private fun createDummyTrip(
        originDate: String? = "2023-01-01 10:00:00 +00:00",
        startDate: String? = "2023-01-01 12:00:00 +00:00",
        doneDate: String? = null,
        tripStatus: TripStatus = TripStatus.START
    ): FSTrip {
        return FSTrip(
            customerCode = 1L,
            tripPrefix = 1,
            tripCode = 2,
            tripUserCode = 1,
            tripUserName = "Test User",
            tripStatus = tripStatus.name,
            requireDestinationFleetData = 0,
            scn = 1,
            requireFleetData = 0,
            originDate = originDate,
            startDate = startDate,
            doneDate = doneDate
        )
    }

    private fun createDummyDestination(
        seq: Int,
        arrivedDate: String?,
        departedDate: String?
    ): FsTripDestination {
        return FsTripDestination(
            destinationSeq = seq,
            destinationType = "SITE",
            destinationSiteCode = 1,
            destinationSiteDesc = "Site Description",
            destinationRegionCode = null,
            destinationRegionDesc = null,
            ticketPrefix = null,
            ticketCode = null,
            ticketId = null,
            destinationStatus = "PENDING",
            latitude = 0.0,
            longitude = 0.0,
            arrivedDate = arrivedDate,
            arrivedLat = null,
            arrivedLon = null,
            arrivedType = null,
            arrivedFleetOdometer = null,
            arrivedFleetPhoto = null,
            arrivedFleetPhotoName = null,
            arrivedFleetPhotoChanged = 0,
            departedDate = departedDate,
            departedLat = null,
            departedLon = null,
            departedType = null,
            countryId = null,
            state = null,
            city = null,
            district = null,
            street = null,
            streetnumber = null,
            complement = null,
            zipCode = null,
            plusCode = null,
            contactName = null,
            contactPhone = null,
            siteMainUser = null,
            minDate = null,
            serialCnt = 0,
            actions = mutableListOf()
        )
    }

    // converte TripTimelineBlock em mapa serializável
    private fun TripTimelineBlock.toSerializableMap(): Map<String, Any?> {
        return mapOf(
            "type" to this.type.name,
            "startDate" to this.startDate,
            "endDate" to this.endDate,
            "destinationSeq" to this.destinationSeq
        )
    }

    // sanitiza nome de arquivo a partir do nome do teste
    private fun sanitizeFileName(name: String): String {
        // remove acentos, espaços e caracteres inválidos
        var sanitized = Normalizer.normalize(name, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
        sanitized = sanitized.replace("[^A-Za-z0-9._-]".toRegex(), "_")
        if (sanitized.length > 150) sanitized = sanitized.substring(0, 150)
        return sanitized
    }

    // grava um arquivo JSON por teste
    private fun dumpBlocksToJsonFile(testName: String, blocks: List<TripTimelineBlock>) {
        val fileName = sanitizeFileName(testName) + ".json"
        val outFile = File(baseOutputDir, fileName)
        val serializable = blocks.map { it.toSerializableMap() }
        outFile.writeText(gson.toJson(serializable), Charsets.UTF_8)
    }

    @Test
    fun `invoke with no trip returns empty list`() {
        `when`(tripRepository.getTrip()).thenReturn(null)

        val result = createTripTimelineBlocksUseCase(Unit)

        dumpBlocksToJsonFile("invoke with no trip returns empty list", result)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `invoke with trip without originDate returns empty list`() {
        val trip = createDummyTrip(originDate = null)
        `when`(tripRepository.getTrip()).thenReturn(trip)

        val result = createTripTimelineBlocksUseCase(Unit)

        dumpBlocksToJsonFile("invoke with trip without originDate returns empty list", result)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `invoke for trip not started returns open PRE_TRIP block`() {
        val trip = createDummyTrip(startDate = null, tripStatus = TripStatus.PENDING)
        `when`(tripRepository.getTrip()).thenReturn(trip)
        `when`(destinationRepository.getListDestinations(1, 2)).thenReturn(emptyList())

        val result = createTripTimelineBlocksUseCase(Unit)

        dumpBlocksToJsonFile("invoke for trip not started returns open PRE_TRIP block", result)

        assertEquals(1, result.size)
        with(result.first()) {
            assertEquals(TimelineBlockType.PRE_TRIP, type)
            assertEquals("2023-01-01 10:00:00 +00:00", startDate)
            assertEquals(null, endDate)
        }
    }

    @Test
    fun `invoke for started trip with no destinations returns open TRANSFER block`() {
        val trip = createDummyTrip()
        `when`(tripRepository.getTrip()).thenReturn(trip)
        `when`(destinationRepository.getListDestinations(1, 2)).thenReturn(emptyList())

        val result = createTripTimelineBlocksUseCase(Unit)

        dumpBlocksToJsonFile("invoke for started trip with no destinations returns open TRANSFER block", result)

        assertEquals(2, result.size)
        assertEquals(TimelineBlockType.PRE_TRIP, result[0].type)
        assertEquals("2023-01-01 10:00:00 +00:00", result[0].startDate)
        assertEquals("2023-01-01 12:00:00 +00:00", result[0].endDate)
        assertEquals(TimelineBlockType.TRANSFER, result[1].type)
        assertEquals("2023-01-01 12:00:00 +00:00", result[1].startDate)
        assertEquals(null, result[1].endDate)
    }

    @Test
    fun `invoke with full trip creates complete timeline`() {
        val trip = createDummyTrip(
            doneDate = "2023-01-02 18:00:00 +00:00",
            tripStatus = TripStatus.DONE
        )

        val destination1 = createDummyDestination(
            1,
            "2023-01-01 14:00:00 +00:00",
            "2023-01-01 18:00:00 +00:00"
        )

        val destination2 = createDummyDestination(
            2,
            "2023-01-02 08:00:00 +00:00",
            "2023-01-02 12:00:00 +00:00"
        )

        `when`(tripRepository.getTrip()).thenReturn(trip)
        `when`(destinationRepository.getListDestinations(1, 2)).thenReturn(listOf(destination1, destination2))

        val result = createTripTimelineBlocksUseCase(Unit)

        dumpBlocksToJsonFile("invoke with full trip creates complete timeline", result)

        assertEquals(6, result.size)

        // PRE_TRIP
        assertEquals(
            TripTimelineBlock(
                TimelineBlockType.PRE_TRIP,
                "2023-01-01 10:00:00 +00:00",
                "2023-01-01 12:00:00 +00:00"
            ), result[0]
        )

        // TRANSFER to dest 1
        assertEquals(
            TripTimelineBlock(
                TimelineBlockType.TRANSFER,
                "2023-01-01 12:00:00 +00:00",
                "2023-01-01 14:00:00 +00:00",
                1
            ), result[1]
        )

        // ON_SITE at dest 1
        assertEquals(
            TripTimelineBlock(
                TimelineBlockType.ON_SITE,
                "2023-01-01 14:00:00 +00:00",
                "2023-01-01 18:00:00 +00:00",
                1
            ), result[2]
        )

        // TRANSFER to dest 2
        assertEquals(
            TripTimelineBlock(
                TimelineBlockType.TRANSFER,
                "2023-01-01 18:00:00 +00:00",
                "2023-01-02 08:00:00 +00:00",
                2
            ), result[3]
        )

        // ON_SITE at dest 2
        assertEquals(
            TripTimelineBlock(
                TimelineBlockType.ON_SITE,
                "2023-01-02 08:00:00 +00:00",
                "2023-01-02 12:00:00 +00:00",
                2
            ), result[4]
        )

        // RETURN_TRIP
        assertEquals(
            TripTimelineBlock(
                TimelineBlockType.RETURN_TRIP,
                "2023-01-02 12:00:00 +00:00",
                "2023-01-02 18:00:00 +00:00"
            ), result[5]
        )
    }

    @Test
    fun `invoke with ongoing trip in transit to destination returns open TRANSFER block`() {
        val trip = createDummyTrip()
        val destination1 = createDummyDestination(1, "2023-01-01 14:00:00 +00:00", "2023-01-01 18:00:00 +00:00")
        val destination2 = createDummyDestination(2, null, null) // Not arrived yet

        `when`(tripRepository.getTrip()).thenReturn(trip)
        `when`(destinationRepository.getListDestinations(1, 2)).thenReturn(listOf(destination1, destination2))

        val result = createTripTimelineBlocksUseCase(Unit)

        dumpBlocksToJsonFile("invoke with ongoing trip in transit to destination returns open TRANSFER block", result)

        assertEquals(4, result.size)
        assertEquals(TimelineBlockType.ON_SITE, result[2].type)
        with(result.last()) {
            assertEquals(TimelineBlockType.TRANSFER, type)
            assertEquals("2023-01-01 18:00:00 +00:00", startDate)
            assertEquals(null, endDate)
            assertEquals(2, destinationSeq)
        }
    }
}
