package com.namoadigital.prj001.ge_os.device_item

import com.namoadigital.prj001.core.form_os.domain.repository.GeOsRepository
import com.namoadigital.prj001.core.form_os.domain.usecase.GeOsMeasureScanUseCase
import com.namoadigital.prj001.core.form_os.domain.usecase.GeOsStatusScanUseCase

import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.model.masterdata.ge_os.GeOs
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem.Companion.ITEM_CHECK_STATUS_LIMIT_DATE_REACHED
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem.Companion.ITEM_CHECK_STATUS_MEASURE_ALERT
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem.Companion.ITEM_CHECK_STATUS_NORMAL
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItemStatusColor
import com.namoadigital.prj001.model.masterdata.ge_os.vg.GeOsVg
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub

@RunWith(JUnit4::class)
class GeOsMeasureScanUseCaseTest {

    private lateinit var geOsStatusScanUseCase: GeOsStatusScanUseCase
    private lateinit var repository: GeOsRepository
    private lateinit var currentDate: String
    private lateinit var futureDate: String
    private lateinit var pastDate: String

    @Before
    fun setup() {
        geOsStatusScanUseCase = GeOsStatusScanUseCase()
        repository = mock()
        currentDate = "2025-04-09 00:00:00 -0300"
        futureDate = "2025-05-01 00:00:00 -0300"
        pastDate = "2025-03-01 00:00:00 -0300"
    }

    @Test
    fun `um alerta manual, um date limit reached e um normal`() {
        // Mock de VGs com diferentes cenários
        val mockVgList = getMockVgList(futureDate, pastDate)

        // Configura o mock do repositório
        repository.stub {
            on {
                createGeOsDeviceItemList(
                    geOs.customer_code,
                    geOs.custom_form_type,
                    geOs.custom_form_code,
                    geOs.custom_form_version,
                    geOs.custom_form_data,
                    mdProductSerialMocked.product_code.toInt(),
                    mdProductSerialMocked.serial_code.toInt(),
                    geOs.value_sufix,
                    geOs.restriction_decimal
                )
            } doReturn getDeviceItemList()
        }


        val deviceItemList = GeOsMeasureScanUseCase(
            repository,
            geOsStatusScanUseCase
        ).invoke(
            GeOsMeasureScanUseCase.Input(
                geOs = geOs,
                productCode = mdProductSerialMocked.product_code,
                serialCode = mdProductSerialMocked.serial_code,
                isContinuousForm = false,
                geOsVgList = mockVgList
            )
        )

        assertEquals(ITEM_CHECK_STATUS_MEASURE_ALERT, deviceItemList[0].item_check_status)
        assertEquals(ITEM_CHECK_STATUS_LIMIT_DATE_REACHED, deviceItemList[1].item_check_status)
        assertEquals(ITEM_CHECK_STATUS_NORMAL, deviceItemList[2].item_check_status)
    }

    private fun getDeviceItemList(): MutableList<GeOsDeviceItem> {
        val list = mutableListOf<GeOsDeviceItem>()
        list.add(
            geOsDeviceItem(
                800f,
                "2025-07-16 00:00:00 -0300",
                "2025-07-16 00:00:00 -0300",
            )
        )
        //
        list.add(
            geOsDeviceItem(
                1100f,
                "2024-07-16 00:00:00 -0300",
                "2024-07-16 00:00:00 -0300",
            )
        )
        //
        list.add(
            geOsDeviceItem(
                1100f,
                "2025-07-16 00:00:00 -0300",
                "2025-07-16 00:00:00 -0300",
            )
        )
        //
        return list
    }

    private fun getMockVgList(
        futureDate: String,
        pastDate: String
    ) = listOf(

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
            vgStatus = GeOsDeviceItem.ITEM_CHECK_STATUS_NORMAL, //MEASURE_ALERT
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
            vgStatus = GeOsDeviceItem.ITEM_CHECK_STATUS_NORMAL, //LIMIT_DATE_REACHED
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
            vgStatus = GeOsDeviceItem.ITEM_CHECK_STATUS_NORMAL, //LIMIT_DATE_REACHED
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
            vgStatus = GeOsDeviceItem.ITEM_CHECK_STATUS_MEASURE_ALERT, //MEASURE_ALERT
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
        )
    )

    private val mdProductSerialMocked = MD_Product_Serial(
    ).apply {
        this.customer_code = 147
        this.product_code = 2
        this.product_id = "2"
        this.product_desc = "Empilhadeira GLP"
        this.serial_code = 12
        this.serial_id = "DEV-0002"
        this.site_reason_code = null
        this.site_code = 5
        this.site_id = "5"
        this.site_desc = "Teste Dev"
        this.zone_code = 1
        this.zone_id = "5.1"
        this.zone_desc = "Teste Dev"
        this.local_code = 1
        this.local_id = "5.1.1"
        this.add_inf1 = null
        this.add_inf2 = null
        this.add_inf3 = null
        this.site_code_owner = 5
        this.brand_code = null
        this.brand_id = null
        this.brand_desc = null
        this.model_code = null
        this.model_id = null
        this.model_desc = null
        this.color_code = null
        this.color_id = null
        this.color_desc = null
        this.segment_code = null
        this.segment_id = null
        this.segment_desc = null
        this.category_price_code = null
        this.category_price_id = null
        this.category_price_desc = null
        this.flag_offline = 0
        this.class_code = 1
        this.class_id = "Dispon\u00edvel"
        this.class_type = "CLASS_SERIAL"
        this.class_color = "#44FF00"
        this.class_available = 1
        this.local_control = 1
        this.product_io_control = 0
        this.site_restriction = 0
        this.site_io_control = 0
        this.inbound_auto_create = 0
        this.device_tp_code_main = 13
        this.has_item_check = 1
        this.scn_item_check = 48
        this.inbound_prefix = null
        this.inbound_code = null
        this.inbound_id = null
        this.inbound_conf_date = null
        this.move_prefix = null
        this.move_code = null
        this.outbound_prefix = null
        this.outbound_code = null
        this.outbound_id = null
        this.horimeter = null
        this.horimeter_date = null
        this.horimeter_supplier_uid = null
        this.horimeter_supplier_desc = null
        this.measure_block_input_time = 0
        this.measure_alert_input_time = 1440
        this.unavailability_reason_option = 1
        this.measure_tp_code = 1
        this.last_measure_value = 630.toDouble()
        this.last_measure_date = "2025-02-20 00:00:00 -0300"
        this.last_cycle_value = 600.toFloat()
        this.last_cycle_date = "2025-02-18 00:00:00 -0300"
    }

    private val geOs = GeOs(
        customer_code = 1,
        custom_form_type = 1,
        custom_form_code = 1,
        custom_form_version = 1,
        custom_form_data = 1,
        order_type_code = 1,
        order_type_id = "1",
        order_type_desc = "asdasd",
        process_type = "asdasdsad",
        display_option = "asdasdsd",
        item_check_group_code = null,
        backup_product_code = null,
        backup_product_id = null,
        backup_product_desc = null,
        backup_serial_code = null,
        backup_serial_id = null,
        measure_tp_code = null,
        measure_tp_id = null,
        measure_tp_desc = null,
        measure_value = 1000f,
        measure_cycle_value = 1000f,
        value_sufix = null,
        restriction_decimal = null,
        value_cycle_size = null,
        cycle_tolerance = null,
        date_start = "2025-04-09 12:45:00 -0300",
        date_end = null,
        last_measure_value = null,
        last_measure_date = null,
        last_cycle_value = null,
        so_edit_start_end = 0,
        so_order_type_code_default = null,
        so_allow_change_order_type = 0,
        so_allow_backup = 0,
        device_tp_code_main = null,
        initial_is_serial_stopped = null,
        initial_stopped_date = null,
        initial_unavailability_reason = null,
        final_is_serial_stopped = null,
        final_unavailability_reason = null,
        allowFormInThePast = 0,
        process_vg = null
    )


    private fun geOsDeviceItem(
        next_cycle_measure: Float,
        next_cycle_measure_date: String,
        next_cycle_limit_date: String,
        status: String? = null
    ) = GeOsDeviceItem(
        customer_code = 1,
        custom_form_type = 1,
        custom_form_code = 1,
        custom_form_version = 1,
        custom_form_data = 1,
        product_code = 2,
        serial_code = 12,
        device_tp_code = 1,
        item_check_code = 1,
        item_check_seq = 1,
        item_check_id = "1",
        item_check_desc = "MEASURE_ALERT que virou NORMAL",
        item_check_desc_alt_vg = null,
        item_check_group_code = null,
        apply_material = GeOsDeviceItem.APPLY_MATERIAL_OPTIONAL,
        verification_instruction = null,
        require_justify_problem = 0,
        critical_item = 0,
        change_adjust = 0,
        order_seq = 1,
        structure = 0,
        already_ok_hide = 0,
        require_photo_fixed = 0,
        require_photo_alert = 0,
        require_photo_already_ok = 0,
        require_photo_not_verified = 0,
        vg_code = null,
        manual_desc = null,
        next_cycle_measure = next_cycle_measure,
        next_cycle_measure_date = next_cycle_measure_date,
        next_cycle_limit_date = next_cycle_limit_date,
        value_sufix = null,
        restriction_decimal = null,
        item_check_status = status ?: GeOsDeviceItem.ITEM_CHECK_STATUS_NORMAL,
        target_date = null,
        exec_type = null,
        exec_date = null,
        exec_comment = null,
        exec_photo1 = null,
        exec_photo2 = null,
        exec_photo3 = null,
        exec_photo4 = null,
        status_answer = null,
        has_expired_cycle = 0,
        hide_days_in_alert = 0,
        materialList = mutableListOf(),
        partitioned_execution = 0,
        vg_action = 0,
        is_visible = 0,
        color_item = GeOsDeviceItemStatusColor.GRAY,
        status_modification_type = null,
        automatic_selection_state = null,
        ticket_prefix = null,
        ticket_code = null,
    )

}