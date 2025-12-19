package com.namoadigital.prj001.ge_os.device_item

import com.namoadigital.prj001.core.form_os.domain.usecase.GeOsOrderTypeScanUseCase
import com.namoadigital.prj001.model.MdOrderType.Companion.DISPLAY_OPTION_SHOW_ALL
import com.namoadigital.prj001.model.MdOrderType.Companion.DISPLAY_OPTION_SHOW_ONLY_CRITICAL
import com.namoadigital.prj001.model.masterdata.ge_os.GeOs
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItemStatusColor
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GeOsOrderTypeScanUseCaseTest {

    private lateinit var geOsOrderTypeScanUseCase: GeOsOrderTypeScanUseCase


    @Before
    fun setup(){
        geOsOrderTypeScanUseCase = GeOsOrderTypeScanUseCase()
    }


    @Test
    fun `aplicar show all nos items`() {
        geOs.display_option = DISPLAY_OPTION_SHOW_ALL
        val output = geOsOrderTypeScanUseCase.invoke(
            GeOsOrderTypeScanUseCase.Input(
                geOs,
                getDeviceItemList(),
                false
            )
        )

        assertEquals(1, output[0].is_visible)
        assertEquals(1, output[1].is_visible)
        assertEquals(0, output[2].is_visible)
    }

    @Test
    fun `aplicar show only critical nos items`() {
        geOs.display_option = DISPLAY_OPTION_SHOW_ONLY_CRITICAL
        val output = geOsOrderTypeScanUseCase.invoke(
            GeOsOrderTypeScanUseCase.Input(
                geOs,
                getDeviceItemList(),
                false
            )
        )

        assertEquals(0, output[0].is_visible)
        assertEquals(0, output[1].is_visible)
        assertEquals(1, output[2].is_visible)
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
        display_option = "NORMAL",
        item_check_group_code = null,
        process_vg = null,
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
    )

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
                next_cycle_measure = 1100f,
                next_cycle_measure_date = "2025-07-16 00:00:00 -0300",
                next_cycle_limit_date = "2025-07-16 00:00:00 -0300",
                vg_code = 1,
                isCritical = 1
            )
        )
        //
        return list
    }

    private fun geOsDeviceItem(
        next_cycle_measure: Float,
        next_cycle_measure_date: String,
        next_cycle_limit_date: String,
        status: String? = null,
        vg_code: Int? = null,
        isCritical: Int = 0,
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
        critical_item = isCritical,
        change_adjust = 0,
        order_seq = 1,
        structure = 0,
        already_ok_hide = 0,
        require_photo_fixed = 0,
        require_photo_alert = 0,
        require_photo_already_ok = 0,
        require_photo_not_verified = 0,
        vg_code = vg_code,
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
        labelFixed = 1,
        labelAlreadyOk = 2,
    )
}