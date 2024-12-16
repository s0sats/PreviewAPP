/*
package com.namoadigital.prj001.finish_os

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.model.GE_Custom_Form_Data
import com.namoadigital.prj001.model.GeOs
import com.namoadigital.prj001.model.GeOsDevice
import com.namoadigital.prj001.model.GeOsDeviceItem
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_custom_form.GeCustomFormRepository
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_os.GeOsRepository
import com.namoadigital.prj001.ui.act011.finish_os.di.model.FinishFormField
import com.namoadigital.prj001.ui.act011.finish_os.di.model.FinishOsData
import com.namoadigital.prj001.ui.act011.finish_os.di.model.FormSaveOs
import com.namoadigital.prj001.ui.act011.finish_os.di.model.ResponsibleStop
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.GetFinishOsDataUseCase
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.SaveFormOsUseCase
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.ge_custom.GetCustomFormDataByIdUseCase
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.ge_os.GeOsUseCase
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.ge_os.GetGeOsByIdUseCase
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.ge_os.GetMissingForecastAnswersUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub


@RunWith(JUnit4::class)
class GeOsAndGeCustomFormUseCaseTest {

    private lateinit var repository: GeOsRepository
    private lateinit var formRepository: GeCustomFormRepository
    private lateinit var useCase: GeOsUseCase
    private lateinit var getCustomFormDataUseCase: GetCustomFormDataByIdUseCase

    @Before
    fun setupTest() {
        repository = mock()
        formRepository = mock()
        useCase = GeOsUseCase(
            getGeOsById = GetGeOsByIdUseCase(repository),
            getMissingForecastAnswersUseCase = GetMissingForecastAnswersUseCase(repository)
        )
        getCustomFormDataUseCase = GetCustomFormDataByIdUseCase(formRepository)

        repository.stub {
            onBlocking {
                getGeOsById(
                    formType = formData.custom_form_type,
                    formCode = formData.custom_form_code,
                    formVersion = formData.custom_form_version,
                    formData = formData.custom_form_data
                )
            } doReturn flowOf(success(geOsByIdMocked))

            onBlocking {
                getAllDeviceById(
                    formType = formData.custom_form_type,
                    formCode = formData.custom_form_code,
                    formVersion = formData.custom_form_version,
                    formData = formData.custom_form_data
                )
            } doReturn flowOf(success(deviceList))

            onBlocking {
                getListDeviceItemById(
                    deviceList[0].custom_form_type,
                    deviceList[0].custom_form_code,
                    deviceList[0].custom_form_version,
                    deviceList[0].custom_form_data.toLong(),
                    deviceList[0].product_code.toLong(),
                    deviceList[0].serial_code.toLong(),
                    deviceList[0].device_tp_code
                )
            } doReturn flowOf(success(deviceItemList))
        }

        formRepository.stub {
            onBlocking {
                getCustomFormDataById(
                    formTypeCode = 1,
                    formCode = 101,
                    formVersionCode = 1,
                    formData = 987654321,
                )
            } doReturn flowOf(success(formData))

            onBlocking {
                saveFormOs(
                    customFormData = formData,
                    geOs = geOsByIdMocked
                )
            } doReturn flowOf(success(Unit))
        }
    }


    @Test
    fun `should return unanswered items`(): Unit = runBlocking {

        val flow = useCase.getMissingForecastAnswersUseCase(
            GetGeOsByIdUseCase.Param(
                formType = formData.custom_form_type,
                formCode = formData.custom_form_code,
                formVersion = formData.custom_form_version,
                formData = formData.custom_form_data
            )
        )

        val result = flow.last()

        result.success {
            assertEquals(deviceItemList, it)
        }

    }


    @Test
    fun `should return all finishData data`(): Unit = runBlocking {
        val useCaseTest =
            GetFinishOsDataUseCase(getCustomFormDataUseCase, useCase.getMissingForecastAnswersUseCase, mock(), mock(), mock(), mock())

        val flow = useCaseTest.invoke(
            GetFinishOsDataUseCase.Param(
                formTypeCode = formData.custom_form_type,
                formCode = formData.custom_form_code,
                formVersionCode = formData.custom_form_version,
                formData = formData.custom_form_data
            )
        )

        val result = flow.last()

        val finishExpected = FinishOsData(
            showBalloonVerify = true,
            infoOs = GetFinishOsDataUseCase.parseInfoOs(
                formDateStart = formData.date_start,
                isEditDate = false,
                formDateEnd = formData.date_end
            ),
            machineOsInitial = GetFinishOsDataUseCase.parseMachinesOs(formData).first,
            machineOsFinal = GetFinishOsDataUseCase.parseMachinesOs(formData).second,
            backupMachine = FinishFormField.BackupMachine(
                serialCode = formData.backup_serial_code.toString(),
                productCode = formData.backup_product_code
            )
        )

        result.success {
            assertEquals(finishExpected, it)
        }

    }

    @Test
    fun `deve retonar sucesso`() = runBlocking {
        val getFinishOsMocked = mock<GetFinishOsDataUseCase>()

        val finishExpected = FinishOsData(
            showBalloonVerify = true,
            infoOs = GetFinishOsDataUseCase.parseInfoOs(
                formDateStart = formData.date_start,
                isEditDate = false,
                formDateEnd = formData.date_end
            ),
            machineOsInitial = GetFinishOsDataUseCase.parseMachinesOs(formData).first,
            machineOsFinal = GetFinishOsDataUseCase.parseMachinesOs(formData).second,
            backupMachine = FinishFormField.BackupMachine(
                serialCode = formData.backup_serial_code,
                productCode = formData.backup_product_code
            )
        )

        getFinishOsMocked.stub {
            onBlocking {
                invoke(
                    GetFinishOsDataUseCase.Param(
                        formTypeCode = formData.custom_form_type,
                        formCode = formData.custom_form_code,
                        formVersionCode = formData.custom_form_version,
                        formData = formData.custom_form_data
                    )
                )
            } doReturn flowOf(success(finishExpected))
        }

        val useCaseTest = SaveFormOsUseCase(formRepository, repository)

        val flow = useCaseTest.invoke(
            FormSaveOs(
                formTypeCode = formData.custom_form_type,
                formCode = formData.custom_form_code,
                formVersionCode = formData.custom_form_version,
                formData = formData.custom_form_data,
                finishOsData = finishExpected
            )
        )

        val result = flow.last()

        assertEquals(IResult.isSuccess(Unit), result)
    }

    val geOsByIdMocked = GeOs(
        customer_code = 6214L,
        custom_form_type = 1,
        custom_form_code = 101,
        custom_form_version = 1,
        custom_form_data = 987654321,
        order_type_code = 324,
        order_type_id = "Order Type ID",
        order_type_desc = "Order Type Description",
        process_type = "Process Type",
        display_option = "Display Option",
        item_check_group_code = null,
        backup_product_code = 25345,
        backup_product_id = "Backup Product ID",
        backup_product_desc = "Backup Product Description",
        backup_serial_code = 645,
        backup_serial_id = "Backup Serial ID",
        measure_tp_code = 54,
        measure_tp_id = "Measure Type ID",
        measure_tp_desc = "Measure Type Description",
        measure_value = 10.00f,
        measure_cycle_value = 23.3f,
        value_sufix = null,
        restriction_decimal = null,
        value_cycle_size = null,
        cycle_tolerance = null,
        date_start = "2023-12-29 23:59:01",
        date_end = "2023-12-31 23:59:01",
        last_measure_value = 10.00f,
        last_measure_date = "2023-12-29 23:59:01",
        last_cycle_value = 23.3f,
        so_edit_start_end = 1,
        so_order_type_code_default = 324,
        so_allow_change_order_type = 1,
        so_allow_backup = 1,
        device_tp_code_main = 1,
    )

    val formData = GE_Custom_Form_Data().apply {
        customer_code = 6214L
        custom_form_type = 1
        custom_form_code = 101
        custom_form_version = 1
        custom_form_data = 987654321
        custom_form_status = "Completed"
        product_code = 54321L
        serial_id = "SN123456789"
        class_code = 1
        date_start = "2023-12-29 23:59:01"
        date_end = "2023-12-31 23:59:01"
        user_code = 56789
        site_code = "SITE001"
        operation_code = 300
        signature = "John Doe"
        signature_name = "John Doe"
        token = "abcdef123456"
        dataFields = ArrayList()
        location_type = "Warehouse"
        location_lat = "40.7128"
        location_lng = "-74.0060"
        so_prefix = 32
        so_code = 123456
        zone_code = 1
        local_code = 1
        schedule_prefix = 324
        schedule_code = 123
        schedule_exec = 235
        error_msg = "No errors"
        location_pendency = 0
        date_gps = "2023-08-01 10:00:00 +00:00"
        ticket_prefix = 231
        ticket_code = 13
        ticket_seq = 213
        ticket_seq_tmp = 1346
        pipeline_code = 465
        step_code = 654
        ticket_checkin_date = "2023-08-01 10:00:00 +00:00"
        tag_operational_code = 123
        sys_date_start = date_start
        sys_date_end = date_end
        order_type_code = 324
        backup_product_code = 25345
        backup_serial_code = 645
        device_tp_code = 54465
        measure_tp_code = 54
        measure_value = 10.00f
        measure_cycle_value = 23.3f
        finalized_service = 2
        initial_unavailability_reason = ResponsibleStop.THIRD_PARTY.value
        final_unavailability_reason = ResponsibleStop.MAINTENANCE.value
        initial_is_serial_stopped = 1
        final_is_serial_stopped = 1
        initial_stopped_date = "2023-12-20 23:59:01"
    }

    val deviceList = listOf(
        GeOsDevice(
            customer_code = 6214L,
            custom_form_type = 1,
            custom_form_code = 101,
            custom_form_version = 1,
            custom_form_data = 987654321,
            product_code = 2648,
            serial_code = 6231,
            device_tp_code = 1368,
            device_tp_id = "Whittney",
            device_tp_desc = "Hana",
            order_seq = 6985,
            tracking_number = null,
            show_empty = 5653
        )
    )

    val deviceItemList = listOf(
        GeOsDeviceItem(
            customer_code = 6214L,
            custom_form_type = 1,
            custom_form_code = 101,
            custom_form_version = 1,
            custom_form_data = 987654321,
            product_code = 2648,
            serial_code = 6231,
            device_tp_code = 1368,
            item_check_code = 2352,
            item_check_seq = 4661,
            item_check_id = "Jennie",
            item_check_desc = "Kari",
            item_check_group_code = null,
            apply_material = "Garet",
            verification_instruction = null,
            require_justify_problem = 6023,
            critical_item = 9927,
            change_adjust = 9277,
            order_seq = 3071,
            structure = 6131,
            manual_desc = null,
            next_cycle_measure = null,
            next_cycle_measure_date = null,
            next_cycle_limit_date = null,
            value_sufix = null,
            restriction_decimal = null,
            item_check_status = "Julissa",
            target_date = null,
            exec_type = null,
            exec_date = null,
            exec_comment = null,
            exec_photo1 = null,
            exec_photo2 = null,
            exec_photo3 = null,
            exec_photo4 = null,
            status_answer = null,
            has_expired_cycle = 2967,
            hide_days_in_alert = 70,
            materialList = mutableListOf(),
            partitioned_execution = 9782
        ),
    )
}*/
