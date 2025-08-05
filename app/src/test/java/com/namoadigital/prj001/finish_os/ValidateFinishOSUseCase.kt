package com.namoadigital.prj001.finish_os

import app.cash.turbine.test
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.model.MeMeasureTp
import com.namoadigital.prj001.model.masterdata.ge_os.GeOs
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_os.GeOsRepository
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.measure_tp.MeasureTpRepository
import com.namoadigital.prj001.ui.act011.finish_os.di.model.NewServiceChoose
import com.namoadigital.prj001.ui.act011.finish_os.di.model.ResponsibleStop
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.ValidateFinishOSUseCase
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.ge_os.GetGeOsByIdUseCase
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.measure.GetMeasureTpByCode
import com.namoadigital.prj001.ui.act011.finish_os.ui.screen_component.MachinesStatus
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishState
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishValidation
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub

@RunWith(JUnit4::class)
class ValidateFinishOSUseCase {

    private val geOsRepository: GeOsRepository = mock()
    private val measureTpRepository: MeasureTpRepository = mock()


    @Test
    fun `test ValidateFinishOS`() = runBlocking {

        val geOsUseCaseMocked = mock(GetGeOsByIdUseCase::class.java)
        val getMeasureByCode = mock(GetMeasureTpByCode::class.java)

        geOsRepository.stub {
            onBlocking {
                getGeOsById(
                    formType = geOsByIdMocked.custom_form_type,
                    formCode = geOsByIdMocked.custom_form_code,
                    formVersion = geOsByIdMocked.custom_form_version,
                    formData = geOsByIdMocked.custom_form_data.toLong()
                )
            } doReturn flowOf(IResult.success(geOsByIdMocked))
        }

        measureTpRepository.stub {
            onBlocking {
                getMeasureTpByCode(meMeasureTp.measureTpCode)
            } doReturn flowOf(IResult.success(meMeasureTp))
        }

        geOsUseCaseMocked.stub {
            onBlocking {
                invoke(
                    GetGeOsByIdUseCase.Param(
                        formType = geOsByIdMocked.custom_form_type,
                        formCode = geOsByIdMocked.custom_form_code,
                        formVersion = geOsByIdMocked.custom_form_version,
                        formData = geOsByIdMocked.custom_form_data.toLong()
                    )
                )
            } doReturn flowOf(IResult.success(geOsByIdMocked))
        }

        getMeasureByCode.stub {
            onBlocking {
                invoke(meMeasureTp.measureTpCode)
            } doReturn flowOf(IResult.success(meMeasureTp))
        }


        val useCase = ValidateFinishOSUseCase(
            geOsUseCaseMocked,
            getMeasureByCode
        )

        val flow = useCase.invoke(
            ValidateFinishOSUseCase.Param(
                validation = FinishValidation(
                    initialMachineStatus = MachinesStatus.NO_STOPPED,
                    infoOs = FinishValidation.InfoOS(
                        dateStart = "2024-10-20 00:00:00",
                        dateEnd = "2024-10-21 00:00:00",
                    ),
                    finalMachineStopped = ResponsibleStop.NO_STOPPED,
                    backupMachine = null,
                    hasNewService = NewServiceChoose.FINALIZED,
                    partialExecutionOS = null,
                    validAfterMachineStopped = false,
                ),
                primaryKey = FinishState.FormPrimaryKey(
                    typeCode = 10,
                    code = 101,
                    versionCode = 1,
                    formData = 987654321
                )
            )
        )

        flow.test {
            assert(awaitItem() is IResult.isSuccess)
            awaitComplete()
        }

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
        last_measure_date = "2023-11-10 23:59:01",
        last_cycle_value = 23.3f,
        so_edit_start_end = 1,
        so_order_type_code_default = 324,
        so_allow_change_order_type = 1,
        so_allow_backup = 1,
        device_tp_code_main = 1,
        allowFormInThePast = 1,
        process_vg = null
    )

    val meMeasureTp = MeMeasureTp(
        customerCode = 123456789L,
        measureTpCode = 42,
        measureTpId = "measure_id_123",
        measureTpDesc = "Sample Measure Description",
        valueSufix = "cm",
        restrictionType = "min_max",
        restrictionMin = 0.5,
        restrictionMax = 10.0,
        restrictionDecimal = 2,
        valueCycleSize = 5.5f,
        cycleTolerance = 3,
        without_measure = 0
    )
}