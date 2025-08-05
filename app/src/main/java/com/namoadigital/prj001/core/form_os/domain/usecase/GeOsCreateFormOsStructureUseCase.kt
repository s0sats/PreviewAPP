package com.namoadigital.prj001.core.form_os.domain.usecase

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.model.MdOrderType.Companion.PROCESS_TYPE_PREVENTIVE
import com.namoadigital.prj001.model.masterdata.ge_os.GeOs
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem
import com.namoadigital.prj001.model.masterdata.ge_os.ProcessVg
import com.namoadigital.prj001.model.masterdata.ge_os.vg.GeOsVg
import com.namoadigital.prj001.model.masterdata.product_serial.verification_group.VgStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GeOsCreateFormOsStructureUseCase  @Inject constructor(
    val scanVerificationGroupUseCase: GeOsScanVerificationGroupUseCase,
    val measureScanUseCase: GeOsMeasureScanUseCase,
    val orderTypeScanUseCase: GeOsOrderTypeScanUseCase,
    val geOsApplyVGVisibilityUseCase: GeOsApplyVGVisibilityUseCase,
): UseCases<GeOsCreateFormOsStructureUseCase.Input, GeOsCreateFormOsStructureUseCase.Output> {
    data class Input(
        val formOsHeader: GeOs,
        val serialObj: MD_Product_Serial,
        val isContinuousForm: Boolean,
        val ticketPrefix: Int?,
        val ticketCode: Int?,
    )

    data class Output(
        val geosVgs: List<GeOsVg>,
        val deviceItems: List<GeOsDeviceItem>,
    )

    override suspend fun invoke(input: Input): Flow<IResult<Output>> {
        return flow {
            val (
                formOsHeader,
                serialObj,
                isContinuousForm,
            ) = input


            val geosVgs = scanVerificationGroupUseCase.invoke(
                GeOsScanVerificationGroupUseCase.Input(
                    customFormType = input.formOsHeader.custom_form_type,
                    customFormCode = input.formOsHeader.custom_form_code,
                    customFormVersion = input.formOsHeader.custom_form_version,
                    customFormData = input.formOsHeader.custom_form_data,
                    productCode = input.serialObj.product_code,
                    serialCode = input.serialObj.serial_code,
                    valueSuffix = input.formOsHeader.value_sufix,
                    restrictionDecimal = input.formOsHeader.restriction_decimal,
                    measureConsider = input.formOsHeader.getMeasureConsider(),
                    dateConsider = input.formOsHeader.getDateConsider(),
                    ticketPrefix = input.ticketPrefix,
                    ticketCode = input.ticketCode,
                    isBlockExecution = ProcessVg.isBlockExecution(input.formOsHeader.process_vg),
                    isPreventiveOs = input.formOsHeader.process_type == PROCESS_TYPE_PREVENTIVE
                )
            )

            var deviceItems = measureScanUseCase.invoke(
                GeOsMeasureScanUseCase.Input(
                    formOsHeader,
                    serialObj.product_code,
                    serialObj.serial_code,
                    isContinuousForm,
                    geosVgs.filter {
                        it.vgStatus != VgStatus.NORMAL.status
                    },
                )
            )

            deviceItems = orderTypeScanUseCase.invoke(
                GeOsOrderTypeScanUseCase.Input(
                    formOsHeader,
                    deviceItems,
                    isContinuousForm
                )
            )


            geOsApplyVGVisibilityUseCase.invoke(
                GeOsApplyVGVisibilityUseCase.Input(
                    geosVgs,
                    deviceItems,
                    isContinuousForm,
                )
            )

            emit(
                IResult.success(
                    Output(
                        geosVgs,
                        deviceItems,
                    )
                )
            )
        }
    }


}