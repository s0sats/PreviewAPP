package com.namoadigital.prj001.core.trip.domain.usecase.blockchain

import com.google.gson.GsonBuilder
import com.namoadigital.prj001.core.blockchain.FindTimelineBlockUseCase
import com.namoadigital.prj001.core.trip.domain.model.blockchain.FindBlockResult
import com.namoadigital.prj001.core.trip.domain.model.blockchain.TimelineBlockType
import com.namoadigital.prj001.core.trip.domain.model.blockchain.TripTimelineBlock
import com.namoadigital.prj001.util.ToolBox_Inf
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File
import java.text.Normalizer
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class FindTimelineBlockUseCaseTest {

    private lateinit var findTimelineBlockUseCase: FindTimelineBlockUseCase

    // pasta separada para esses dumps
    private val baseOutputDir = File("build/reports/find_timeline_tests")
    private val gson = GsonBuilder().setPrettyPrinting().create()

    // formatter para "yyyy-MM-dd HH:mm:ss +00:00" — usa XXX para aceitar "+00:00"
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss XXX")
        .withZone(ZoneOffset.UTC)

    /**
     * Pequena estrutura para agrupar intervalos de teste.
     * end pode ser null para indicar intervalo aberto/infinito (quando aplicável).
     */
    private data class TestRange(val start: String, val end: String?)

    private object TestRanges {
        val VALID_INSIDE_BLOCK = TestRange(
            "2023-01-01 13:00:00 +00:00",
            "2023-01-01 14:00:00 +00:00"
        )

        val MATCH_BLOCK_BOUNDARIES = TestRange(
            "2023-01-01 10:00:00 +00:00",
            "2023-01-01 12:00:00 +00:00"
        )

        val OPEN_ENDED_BLOCK = TestRange(
            "2023-01-02 09:00:00 +00:00",
            "2023-01-02 10:00:00 +00:00"
        )

        // intervalo que atravessa um bloco virtual (TRANSFER) e um real (ON_SITE)
        val SPANNING_VIRTUAL_AND_REAL_BLOCKS = TestRange(
            "2023-01-01 11:30:00 +00:00",
            "2023-01-01 12:30:00 +00:00"
        )

        val OUTSIDE_ANY_BLOCK = TestRange(
            "2023-01-01 07:00:00 +00:00",
            "2023-01-01 07:30:00 +00:00"
        )

        val INVALID_RANGE = TestRange(
            "2023-01-01 14:00:00 +00:00",
            "2023-01-01 13:00:00 +00:00"
        )

        val INSIDE_VIRTUAL_BLOCK_ONLY = TestRange("2023-01-01 10:30:00 +00:00", "2023-01-01 11:30:00 +00:00")

        // Novo range para o Caso B
        val SPANNING_TWO_REAL_BLOCKS = TestRange("2023-01-01 11:30:00 +00:00", "2023-01-01 18:30:00 +00:00")

    }

    private val timeline = listOf(
        TripTimelineBlock(TimelineBlockType.PRE_TRIP, "2023-01-01 08:00:00 +00:00", "2023-01-01 10:00:00 +00:00"), // Bloco 0
        TripTimelineBlock(TimelineBlockType.TRANSFER, "2023-01-01 10:00:00 +00:00", "2023-01-01 12:00:00 +00:00"), // Bloco 1 (Virtual)
        TripTimelineBlock(TimelineBlockType.ON_SITE, "2023-01-01 12:00:00 +00:00", "2023-01-01 18:00:00 +00:00", 1), // Bloco 2 (Real)
        TripTimelineBlock(TimelineBlockType.TRANSFER, "2023-01-01 18:00:00 +00:00", "2023-01-02 08:00:00 +00:00"), // Bloco 3
        TripTimelineBlock(TimelineBlockType.ON_SITE, "2023-01-02 08:00:00 +00:00", null, 2) // Bloco 4 (aberto)
    )

    @Before
    fun setUp() {
        // mock estático do método que converte datas para millis
        mockkStatic("com.namoadigital.prj001.util.ToolBox_Inf")

        // simplificado: nossas datas já estão no formato esperado pelo formatter
        every { ToolBox_Inf.dateToMilliseconds(any()) } answers {
            val dateStr = firstArg<String>()
            Instant.from(formatter.parse(dateStr)).toEpochMilli()
        }

        findTimelineBlockUseCase = FindTimelineBlockUseCase()
        baseOutputDir.mkdirs()
    }

    @After
    fun tearDown() {
        unmockkStatic("com.namoadigital.prj001.util.ToolBox_Inf")
    }

    // Serializa TripTimelineBlock para mapa simples para JSON
    private fun TripTimelineBlock.toSerializableMap(): Map<String, Any?> {
        return mapOf(
            "type" to this.type.name,
            "startDate" to this.startDate,
            "endDate" to this.endDate,
            "destinationSeq" to this.destinationSeq
        )
    }

    private fun sanitizeFileName(name: String): String {
        var sanitized = Normalizer.normalize(name, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
        sanitized = sanitized.replace("[^A-Za-z0-9._-]".toRegex(), "_")
        if (sanitized.length > 150) sanitized = sanitized.take(150)
        return sanitized
    }

    /**
     * Gera um JSON contendo:
     * - timeline: array com os blocos
     * - query: objeto com startDate/endDate
     * - result: { status: "Success"|"NotFound"|"OverlapError"|"InvalidDateRange", ... }
     */
    private fun dumpTestResultToJsonFile(
        testName: String,
        timelineBlocks: List<TripTimelineBlock>,
        startDate: String,
        endDate: String?,
        result: FindBlockResult
    ) {
        val fileName = sanitizeFileName(testName) + ".json"
        val outFile = File(baseOutputDir, fileName)

        val timelineSerializable = timelineBlocks.map { it.toSerializableMap() }

        val resultMap: Map<String, Any?> = when (result) {
            is FindBlockResult.Success -> mapOf(
                "status" to "Success",
                "block" to result.block.toSerializableMap()
            )
            is FindBlockResult.NotFound -> mapOf(
                "status" to "NotFound",
                "previous" to result.previous?.toSerializableMap(),
                "next" to result.next?.toSerializableMap()
            )
            is FindBlockResult.OverlapError -> mapOf(
                "status" to "OverlapError",
                "first" to result.overlapStart?.toSerializableMap(),
                "last" to result.overlapEnd?.toSerializableMap()
            )
            is FindBlockResult.InvalidDateRange -> mapOf(
                "status" to "InvalidDateRange"
            )
        }

        val full = mapOf(
            "query" to mapOf("startDate" to startDate, "endDate" to endDate),
            "timeline" to timelineSerializable,
            "result" to resultMap
        )

        outFile.writeText(gson.toJson(full), Charsets.UTF_8)
    }

    @Test
    fun `invoke with valid interval fully inside a block returns Success`() {
        val range = TestRanges.VALID_INSIDE_BLOCK

        val result = findTimelineBlockUseCase(
            FindTimelineBlockUseCase.Input(
                startDate = range.start,
                endDate = range.end,
                timeline = timeline
            )
        )

        dumpTestResultToJsonFile(
            "invoke com intervalo válido totalmente dentro de um bloco retorna Success",
            timeline,
            range.start,
            range.end,
            result
        )

        assertTrue(result is FindBlockResult.Success)
        assertEquals(timeline[2], (result as FindBlockResult.Success).block)
    }

    @Test
    fun `invoke with interval matching block boundaries returns Success`() {
        val range = TestRanges.MATCH_BLOCK_BOUNDARIES

        val result = findTimelineBlockUseCase(
            FindTimelineBlockUseCase.Input(
                startDate = range.start,
                endDate = range.end,
                timeline = timeline
            )
        )

        dumpTestResultToJsonFile(
            "invoke com intervalo coincidente com limites do bloco retorna Success",
            timeline,
            range.start,
            range.end,
            result
        )

        assertTrue(result is FindBlockResult.Success)
        assertEquals(timeline[1], (result as FindBlockResult.Success).block)
    }

    @Test
    fun `invoke with interval in open-ended block returns Success`() {
        val range = TestRanges.OPEN_ENDED_BLOCK

        val result = findTimelineBlockUseCase(
            FindTimelineBlockUseCase.Input(
                startDate = range.start,
                endDate = range.end,
                timeline = timeline
            )
        )

        dumpTestResultToJsonFile(
            "invoke com intervalo em bloco aberto retorna Success",
            timeline,
            range.start,
            range.end,
            result
        )

        assertTrue(result is FindBlockResult.Success)
        assertEquals(timeline[4], (result as FindBlockResult.Success).block)
    }

    @Test
    fun `invoke with interval spanning VIRTUAL and REAL block returns Success with REAL block`() {
        val range = TestRanges.SPANNING_VIRTUAL_AND_REAL_BLOCKS

        val result = findTimelineBlockUseCase(
            FindTimelineBlockUseCase.Input(
                startDate = range.start,
                endDate = range.end,
                timeline = timeline
            )
        )

        dumpTestResultToJsonFile(
            "invoke com intervalo atravessando bloco VIRTUAL e REAL retorna Success com bloco REAL",
            timeline,
            range.start,
            range.end,
            result
        )

        assertTrue("O resultado deveria ser Success, pois o UseCase prioriza o bloco real", result is FindBlockResult.Success)
        assertEquals("O bloco retornado deveria ser o ON_SITE", timeline[2], (result as FindBlockResult.Success).block)
    }

    @Test
    fun `invoke with interval outside any block returns NotFound`() {
        val range = TestRanges.OUTSIDE_ANY_BLOCK

        val result = findTimelineBlockUseCase(
            FindTimelineBlockUseCase.Input(
                startDate = range.start,
                endDate = range.end,
                timeline = timeline
            )
        )

        dumpTestResultToJsonFile(
            "invoke com intervalo fora de qualquer bloco retorna NotFound",
            timeline,
            range.start,
            range.end,
            result
        )

        assertTrue(result is FindBlockResult.NotFound)
    }

    @Test
    fun `invoke with invalid date range returns InvalidDateRange`() {
        val range = TestRanges.INVALID_RANGE

        val result = findTimelineBlockUseCase(
            FindTimelineBlockUseCase.Input(
                startDate = range.start,
                endDate = range.end,
                timeline = timeline
            )
        )

        dumpTestResultToJsonFile(
            "invoke com intervalo inválido retorna InvalidDateRange",
            timeline,
            range.start,
            range.end,
            result
        )

        assertTrue(result is FindBlockResult.InvalidDateRange)
    }

    private val overlappingTimeline = listOf(
        TripTimelineBlock(TimelineBlockType.PRE_TRIP, "2023-01-01 08:00:00 +00:00", "2023-01-01 12:00:00 +00:00"), // Real 1
        TripTimelineBlock(TimelineBlockType.ON_SITE, "2023-01-01 11:00:00 +00:00", "2023-01-01 14:00:00 +00:00", 1) // Real 2 (Sobreposto)
    )

    @Test
    fun `(CASO A) invoke with interval intersecting only VIRTUAL block returns NotFound`() {
        val range = TestRanges.INSIDE_VIRTUAL_BLOCK_ONLY

        val result = findTimelineBlockUseCase(
            FindTimelineBlockUseCase.Input(
                startDate = range.start,
                endDate = range.end,
                timeline = timeline
            )
        )

        dumpTestResultToJsonFile(
            "CASO_A_intersecta_apenas_virtual_retorna_NotFound",
            timeline, range.start, range.end, result
        )

        assertTrue("O resultado deveria ser NotFound, pois está no 'vazio' de um TRANSFER", result is FindBlockResult.NotFound)
    }

    /**
     * Testa o **CASO B**: O intervalo intersecta DOIS blocos reais (`PRE_TRIP` e `ON_SITE`).
     * Isso indica uma timeline corrompida. O resultado esperado é `OverlapError`.
     */
    @Test
    fun `(CASO B) invoke with interval intersecting multiple REAL blocks returns OverlapError`() {
        val range = TestRanges.SPANNING_TWO_REAL_BLOCKS

        val result = findTimelineBlockUseCase(
            FindTimelineBlockUseCase.Input(
                startDate = range.start,
                endDate = range.end,
                timeline = overlappingTimeline
            )
        )

        dumpTestResultToJsonFile(
            "CASO_B_intersecta_dois_reais_retorna_OverlapError",
            overlappingTimeline, range.start, range.end, result
        )

        assertTrue("O resultado deveria ser OverlapError, pois a timeline é inválida", result is FindBlockResult.OverlapError)
    }

    /**
     * Testa o **CASO C**: O intervalo intersecta um bloco real (`ON_SITE`) e um virtual (`TRANSFER`).
     * O resultado esperado é `Success` com o bloco real, que tem prioridade.
     */
    @Test
    fun `(CASO C) invoke with interval spanning VIRTUAL and REAL block returns Success`() {
        val range = TestRanges.SPANNING_VIRTUAL_AND_REAL_BLOCKS

        val result = findTimelineBlockUseCase(
            FindTimelineBlockUseCase.Input(
                startDate = range.start,
                endDate = range.end,
                timeline = timeline
            )
        )

        dumpTestResultToJsonFile(
            "CASO_C_intersecta_virtual_e_real_retorna_Success_com_real",
            timeline, range.start, range.end, result
        )

        assertTrue("O resultado deveria ser Success", result is FindBlockResult.Success)
        assertEquals("O bloco retornado deve ser o ON_SITE, pois tem prioridade", timeline[2], (result as FindBlockResult.Success).block)
    }
}