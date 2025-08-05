package com.namoadigital.prj001.ge_os.verification_group

import com.namoadigital.prj001.core.form_os.domain.repository.GeOsRepository
import com.namoadigital.prj001.core.form_os.domain.usecase.GeOsScanVerificationGroupUseCase
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem
import com.namoadigital.prj001.model.masterdata.ge_os.vg.GeOsVg
import com.namoadigital.prj001.model.masterdata.product_serial.verification_group.VgStatus
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub


@RunWith(JUnit4::class)
class GeOsScanVerificationGroupUseCaseTest {

    private lateinit var repository: GeOsRepository
    private lateinit var useCase: GeOsScanVerificationGroupUseCase
    private val currentDate = ToolBox_Inf.getDateLastMinute("2025-04-10 00:00:00 -0300")
    private val futureDate = "2025-05-01 00:00:00 -0300"
    private val pastDate = "2025-03-01 00:00:00 -0300"


    @Before
    fun setup() {
        repository = mock()
        useCase = GeOsScanVerificationGroupUseCase(repository)
    }

    @Test
    fun `testar status MANUALLY_FORCED_DATE quando manual_date está presente`() = runBlocking {

        // Dados de entrada
        val input = GeOsScanVerificationGroupUseCase.Input(
            customFormType = 1,
            customFormCode = 101,
            customFormVersion = 1,
            customFormData = 987654321,
            productCode = 2L,
            serialCode = 12L,
            valueSuffix = null,
            restrictionDecimal = null,
            measureConsider = 1000f,
            dateConsider = currentDate,
            ticketPrefix = null,
            ticketCode = null,
            isBlockExecution = false
        )

        // VG 1: manual_date não é nulo → status deve ser MANUALLY_FORCED_DATE
        val mockVgList = listOf(
            GeOsVg(
                customerCode = 147,
                customFormType = 1,
                customFormCode = 101,
                customFormVersion = 1,
                customFormData = 987654321,
                productCode = 2,
                serialCode = 12,
                vgCode = 1,
                nextCycleMeasure = 1100f,
                nextCycleMeasureDate = futureDate,
                nextCycleLimitDate = futureDate,
                vgStatus = VgStatus.MANUALLY_FORCED_DATE.status,
                targetDate = futureDate,
                manualDate = futureDate // Deve forçar o status NORMAL
            ),
            // VG 2: manual_date é nulo → status deve seguir lógica normal (medição < limite → MEASURE_ALERT)
            GeOsVg(
                customerCode = 147,
                customFormType = 1,
                customFormCode = 101,
                customFormVersion = 1,
                customFormData = 987654321,
                productCode = 2,
                serialCode = 12,
                vgCode = 2,
                nextCycleMeasure = 900f,
                nextCycleMeasureDate = futureDate,
                nextCycleLimitDate = futureDate,
                vgStatus = VgStatus.NORMAL.status,
                targetDate = futureDate,
                manualDate = null // Deve seguir lógica normal
            ),
            GeOsVg(
                customerCode = 147,
                customFormType = 1,
                customFormCode = 101,
                customFormVersion = 1,
                customFormData = 987654321,
                productCode = 2,
                serialCode = 12,
                vgCode = 1,
                nextCycleMeasure = 1100f,
                nextCycleMeasureDate = pastDate,
                nextCycleLimitDate = pastDate,
                vgStatus = VgStatus.MANUALLY_FORCED_DATE.status,
                targetDate = pastDate,
                manualDate = pastDate // Deve forçar o status MANUALLY_FORCED_DATE
            ),
            // VG 4: medição abaixo do limite (900 < 1000)
            GeOsVg(
                customerCode = 147,
                customFormType = 1,
                customFormCode = 101,
                customFormVersion = 1,
                customFormData = 987654321,
                productCode = 2,
                serialCode = 12,
                vgCode = 1,
                nextCycleMeasure = 900f,
                nextCycleMeasureDate = futureDate,
                nextCycleLimitDate = futureDate,
                vgStatus = VgStatus.NORMAL.status, //MEASURE_ALERT
                targetDate = futureDate
            ),
        )

        // Mock do repositório
        repository.stub {
            on {
                createGeOsVg(
                    input.customFormType,
                    input.customFormCode,
                    input.customFormVersion,
                    input.customFormData,
                    input.productCode,
                    input.serialCode,
                    input.valueSuffix,
                    input.restrictionDecimal
                )
            } doReturn mockVgList
        }

        val result = useCase(input)

        // VG 1: status deve ser MANUALLY_FORCED_DATE
        assertEquals(VgStatus.NORMAL.status, result[0].vgStatus)

        // VG 2: status deve ser MEASURE_ALERT (por conta da medição abaixo)
        assertEquals(VgStatus.MEASURE_ALERT.status, result[1].vgStatus)

        // VG 3: status deve ser MANUALLY_FORCED_DATE (por conta do manual_date)
        assertEquals(VgStatus.MANUALLY_FORCED_DATE.status, result[2].vgStatus)

        // VG 4: Deve tornar-se MEASURE_ALERT (medição abaixo do limiar)
        assertEquals(VgStatus.MEASURE_ALERT.status, result[3].vgStatus)
    }


    @Test
    fun `testar mudanças de status do VG baseadas em condições de medição e data`() = runBlocking {

        // Dados de entrada para o teste
        val input = GeOsScanVerificationGroupUseCase.Input(
            customFormType = 1,
            customFormCode = 101,
            customFormVersion = 1,
            customFormData = 987654321,
            productCode = 2L,
            serialCode = 12L,
            valueSuffix = null,
            restrictionDecimal = null,
            measureConsider = 1000f,
            dateConsider = currentDate,
            ticketPrefix = null,
            ticketCode = null,
            isBlockExecution = false
        )

        // Mock de VGs com diferentes cenários
        val mockVgList = listOf(

            // VG 1: medição abaixo do limite (900 < 1000)
            GeOsVg(
                customerCode = 147,
                customFormType = 1,
                customFormCode = 101,
                customFormVersion = 1,
                customFormData = 987654321,
                productCode = 2,
                serialCode = 12,
                vgCode = 1,
                nextCycleMeasure = 900f,
                nextCycleMeasureDate = futureDate,
                nextCycleLimitDate = futureDate,
                vgStatus = VgStatus.NORMAL.status, //MEASURE_ALERT
                targetDate = futureDate
            ),

            // VG 2: Data limite no passado
            GeOsVg(
                customerCode = 147,
                customFormType = 1,
                customFormCode = 101,
                customFormVersion = 1,
                customFormData = 987654321,
                productCode = 2,
                serialCode = 12,
                vgCode = 2,
                nextCycleMeasure = 1100f,
                nextCycleMeasureDate = pastDate,
                nextCycleLimitDate = pastDate,
                vgStatus = VgStatus.NORMAL.status, //LIMIT_DATE_REACHED
                targetDate = pastDate
            ),

            // VG 3: Apenas data limite no passado (sem medição)
            GeOsVg(
                customerCode = 147,
                customFormType = 1,
                customFormCode = 101,
                customFormVersion = 1,
                customFormData = 987654321,
                productCode = 2,
                serialCode = 12,
                vgCode = 3,
                nextCycleMeasure = null,
                nextCycleMeasureDate = null,
                nextCycleLimitDate = pastDate,
                vgStatus = VgStatus.NORMAL.status, //LIMIT_DATE_REACHED
                targetDate = pastDate
            ),

            // VG 4: Já em estado de alerta de medição
            GeOsVg(
                customerCode = 147,
                customFormType = 1,
                customFormCode = 101,
                customFormVersion = 1,
                customFormData = 987654321,
                productCode = 2,
                serialCode = 12,
                vgCode = 4,
                nextCycleMeasure = 900f,
                nextCycleMeasureDate = null,
                nextCycleLimitDate = futureDate,
                vgStatus = VgStatus.MEASURE_ALERT.status, //MEASURE_ALERT
                targetDate = futureDate
            ),

            // VG 5: Data limite atingida mas medição acima do limite
            GeOsVg(
                customerCode = 147,
                customFormType = 1,
                customFormCode = 101,
                customFormVersion = 1,
                customFormData = 987654321,
                productCode = 2,
                serialCode = 12,
                vgCode = 5,
                nextCycleMeasure = 1100f,
                nextCycleMeasureDate = futureDate,
                nextCycleLimitDate = null,
                vgStatus = GeOsDeviceItem.ITEM_CHECK_STATUS_PROJECTED_DATE_REACHED, //LIMIT_DATE_REACHED
                targetDate = futureDate
            ),
        )

        // Configura o mock do repositório
        repository.stub {
            on {
                createGeOsVg(
                    input.customFormType,
                    input.customFormCode,
                    input.customFormVersion,
                    input.customFormData,
                    input.productCode,
                    input.serialCode,
                    input.valueSuffix,
                    input.restrictionDecimal
                )
            } doReturn mockVgList
        }

        val result = useCase(input)

        // VG 1: Deve tornar-se MEASURE_ALERT (medição abaixo do limiar)
        assertEquals(VgStatus.MEASURE_ALERT.status, result[0].vgStatus)

        // VG 2: Deve tornar-se LIMIT_DATE_REACHED (data no passado)
        assertEquals(VgStatus.LIMIT_DATE_REACHED.status, result[1].vgStatus)

        // VG 3: Deve tornar-se LIMIT_DATE_REACHED (apenas data limite no passado)
        assertEquals(VgStatus.LIMIT_DATE_REACHED.status, result[2].vgStatus)

        // VG 4: Deve manter MEASURE_ALERT (medição abaixo do limiar)
        assertEquals(VgStatus.MEASURE_ALERT.status, result[3].vgStatus)

        // VG 5: Deve tornar-se NORMAL (medição acima do limiar sobrepõe status de data)
        assertEquals(VgStatus.NORMAL.status, result[4].vgStatus)
    }

    @Test
    fun `testar status do VG quando todas condições são normais`() = runBlocking {

        // Dados de entrada
        val input = GeOsScanVerificationGroupUseCase.Input(
            customFormType = 1,
            customFormCode = 101,
            customFormVersion = 1,
            customFormData = 987654321,
            productCode = 2L,
            serialCode = 12L,
            valueSuffix = null,
            restrictionDecimal = null,
            measureConsider = 1000f,
            dateConsider = currentDate,
            ticketPrefix = null,
            ticketCode = null,
            isBlockExecution = false
        )

        // Mock de VG com condições normais
        val mockVgList = listOf(
            GeOsVg(
                customerCode = 147,
                customFormType = 1,
                customFormCode = 101,
                customFormVersion = 1,
                customFormData = 987654321,
                productCode = 2,
                serialCode = 12,
                vgCode = 1,
                nextCycleMeasure = 1100f,
                nextCycleMeasureDate = futureDate,
                nextCycleLimitDate = futureDate,
                vgStatus = VgStatus.NORMAL.status,
                targetDate = futureDate
            )
        )

        // Configura o mock
        repository.stub {
            on {
                createGeOsVg(
                    input.customFormType,
                    input.customFormCode,
                    input.customFormVersion,
                    input.customFormData,
                    input.productCode,
                    input.serialCode,
                    input.valueSuffix,
                    input.restrictionDecimal
                )
            } doReturn mockVgList
        }

        // Executa e verifica
        val result = useCase.invoke(input)

        assertEquals(1, result.size)
        assertEquals(VgStatus.NORMAL.status, result[0].vgStatus)
    }

    @Test
    fun `testar transições de status do VG de alerta para normal`() = runBlocking {

        // Dados de entrada
        val input = GeOsScanVerificationGroupUseCase.Input(
            customFormType = 1,
            customFormCode = 101,
            customFormVersion = 1,
            customFormData = 987654321,
            productCode = 2L,
            serialCode = 12L,
            valueSuffix = null,
            restrictionDecimal = null,
            measureConsider = 1000f,
            dateConsider = currentDate,
            ticketPrefix = null,
            ticketCode = null,
            isBlockExecution = false
        )

        // Mock com VGs em estado de alerta
        val mockVgList = listOf(

            // VG 1: Em alerta mas com medição acima do limite
            GeOsVg(
                customerCode = 147,
                customFormType = 1,
                customFormCode = 101,
                customFormVersion = 1,
                customFormData = 987654321,
                productCode = 2,
                serialCode = 12,
                vgCode = 1,
                nextCycleMeasure = 1100f,
                nextCycleMeasureDate = futureDate,
                nextCycleLimitDate = futureDate,
                vgStatus = VgStatus.MEASURE_ALERT.status, //MEASURE_ALERT
                targetDate = futureDate
            ),

            // VG 2: Data limite atingida mas datas no futuro
            GeOsVg(
                customerCode = 147,
                customFormType = 1,
                customFormCode = 101,
                customFormVersion = 1,
                customFormData = 987654321,
                productCode = 2,
                serialCode = 12,
                vgCode = 2,
                nextCycleMeasure = null,
                nextCycleMeasureDate = futureDate,
                nextCycleLimitDate = futureDate,
                vgStatus = VgStatus.LIMIT_DATE_REACHED.status, //LIMIT_DATE_REACHED
                targetDate = futureDate
            )
        )

        // Configura o mock
        repository.stub {
            on {
                createGeOsVg(
                    input.customFormType,
                    input.customFormCode,
                    input.customFormVersion,
                    input.customFormData,
                    input.productCode,
                    input.serialCode,
                    input.valueSuffix,
                    input.restrictionDecimal
                )
            } doReturn mockVgList
        }

        // Executa e verifica
        val result = useCase.invoke(input)

        assertEquals(2, result.size)
        // VG 1: Deve transicionar de MEASURE_ALERT para NORMAL
        assertEquals(VgStatus.NORMAL.status, result[0].vgStatus)
        // VG 2: Deve manter LIMIT_DATE_REACHED (datas no futuro)
        assertEquals(VgStatus.NORMAL.status, result[1].vgStatus)
    }


    @Test
    fun `testar dados reais do VG com dados atualizados`() = runBlocking {
        val input = GeOsScanVerificationGroupUseCase.Input(
            customFormType = 1,
            customFormCode = 101,
            customFormVersion = 1,
            customFormData = 987654321,
            productCode = 2L,
            serialCode = 12L,
            valueSuffix = null,
            restrictionDecimal = null,
            measureConsider = 81f,
            dateConsider = currentDate,
            ticketPrefix = null,
            ticketCode = null,
            isBlockExecution = false
        )

        val mockVgList = listOf(
            GeOsVg(
                customerCode = 147,
                customFormType = 1,
                customFormCode = 101,
                customFormVersion = 1,
                customFormData = 987654321,
                productCode = 2,
                serialCode = 12,
                vgCode = 2,
                nextCycleMeasure = null,
                nextCycleMeasureDate = null,
                nextCycleLimitDate = "2025-04-09 16:25:00 -0300",
                vgStatus = VgStatus.LIMIT_DATE_REACHED.status,
                targetDate = "2025-04-09 16:25:00 -0300",
                manualDate = null,
                partitionedExecution = 0
            ),
            GeOsVg(
                customerCode = 147,
                customFormType = 1,
                customFormCode = 101,
                customFormVersion = 1,
                customFormData = 987654321,
                productCode = 2,
                serialCode = 12,
                vgCode = 3,
                nextCycleMeasure = 1000f,
                nextCycleMeasureDate = "2025-11-24 16:25:00 -0300",
                nextCycleLimitDate = null,
                vgStatus = VgStatus.MANUALLY_FORCED_DATE.status,
                targetDate = "2025-04-09 23:59:59 -0300",
                manualDate = "2025-04-09 23:59:59 -0300",
                partitionedExecution = 0
            ),
            GeOsVg(
                customerCode = 147,
                customFormType = 1,
                customFormCode = 101,
                customFormVersion = 1,
                customFormData = 987654321,
                productCode = 2,
                serialCode = 12,
                vgCode = 4,
                nextCycleMeasure = 50f,
                nextCycleMeasureDate = "2025-04-10 16:25:00 -0300",
                nextCycleLimitDate = null,
                vgStatus = VgStatus.NORMAL.status,
                targetDate = "2025-04-09 16:25:00 -0300",
                manualDate = null,
                partitionedExecution = 0
            ),
            GeOsVg(
                customerCode = 147,
                customFormType = 1,
                customFormCode = 101,
                customFormVersion = 1,
                customFormData = 987654321,
                productCode = 2,
                serialCode = 12,
                vgCode = 5,
                nextCycleMeasure = 84f,
                nextCycleMeasureDate = "2025-04-22 16:25:00 -0300",
                nextCycleLimitDate = "2025-05-07 16:25:00 -0300",
                vgStatus = VgStatus.NORMAL.status,
                targetDate = "2025-04-22 16:25:00 -0300",
                manualDate = null,
                partitionedExecution = 0
            ),
            GeOsVg(
                customerCode = 147,
                customFormType = 1,
                customFormCode = 101,
                customFormVersion = 1,
                customFormData = 987654321,
                productCode = 2,
                serialCode = 12,
                vgCode = 6,
                nextCycleMeasure = 60f,
                nextCycleMeasureDate = "2025-04-14 16:25:00 -0300",
                nextCycleLimitDate = null,
                vgStatus = VgStatus.NORMAL.status,
                targetDate = "2025-04-14 16:25:00 -0300",
                manualDate = null,
                partitionedExecution = 0
            ),
            GeOsVg(
                customerCode = 147,
                customFormType = 1,
                customFormCode = 101,
                customFormVersion = 1,
                customFormData = 987654321,
                productCode = 2,
                serialCode = 12,
                vgCode = 7,
                nextCycleMeasure = 70f,
                nextCycleMeasureDate = "2025-04-15 16:25:00 -0300",
                nextCycleLimitDate = null,
                vgStatus = VgStatus.NORMAL.status,
                targetDate = "2025-04-15 16:25:00 -0300",
                manualDate = null,
                partitionedExecution = 0
            ),
            GeOsVg(
                customerCode = 147,
                customFormType = 1,
                customFormCode = 101,
                customFormVersion = 1,
                customFormData = 987654321,
                productCode = 2,
                serialCode = 12,
                vgCode = 8,
                nextCycleMeasure = 80f,
                nextCycleMeasureDate = "2025-04-17 16:25:00 -0300",
                nextCycleLimitDate = null,
                vgStatus = VgStatus.NORMAL.status,
                targetDate = "2025-04-17 16:25:00 -0300",
                manualDate = null,
                partitionedExecution = 0
            ),
            GeOsVg(
                customerCode = 147,
                customFormType = 1,
                customFormCode = 101,
                customFormVersion = 1,
                customFormData = 987654321,
                productCode = 2,
                serialCode = 12,
                vgCode = 9,
                nextCycleMeasure = 500f,
                nextCycleMeasureDate = "2025-07-30 16:25:00 -0300",
                nextCycleLimitDate = null,
                vgStatus = VgStatus.NORMAL.status,
                targetDate = "2025-04-11 23:59:59 -0300",
                manualDate = "2025-04-11 23:59:59 -0300",
                partitionedExecution = 0
            )
        )

        repository.stub {
            on {
                createGeOsVg(
                    input.customFormType,
                    input.customFormCode,
                    input.customFormVersion,
                    input.customFormData,
                    input.productCode,
                    input.serialCode,
                    input.valueSuffix,
                    input.restrictionDecimal
                )
            } doReturn mockVgList
        }

        val result = useCase(input)

        assertEquals(VgStatus.LIMIT_DATE_REACHED.status, result[0].vgStatus)
        assertEquals(VgStatus.MANUALLY_FORCED_DATE.status, result[1].vgStatus)
        assertEquals(VgStatus.MEASURE_ALERT.status, result[2].vgStatus)
        assertEquals(VgStatus.NORMAL.status, result[3].vgStatus)
        assertEquals(VgStatus.MEASURE_ALERT.status, result[4].vgStatus)
        assertEquals(VgStatus.MEASURE_ALERT.status, result[5].vgStatus)
        assertEquals(VgStatus.MEASURE_ALERT.status, result[6].vgStatus)
        assertEquals(VgStatus.NORMAL.status, result[7].vgStatus)
    }
}