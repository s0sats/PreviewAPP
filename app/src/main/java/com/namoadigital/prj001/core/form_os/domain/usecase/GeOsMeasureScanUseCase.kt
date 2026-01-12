package com.namoadigital.prj001.core.form_os.domain.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.form_os.domain.repository.GeOsRepository
import com.namoadigital.prj001.model.masterdata.ge_os.GeOs
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItemStatusColor
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItemStatusModificationType
import com.namoadigital.prj001.model.masterdata.ge_os.vg.GeOsVg

class GeOsMeasureScanUseCase constructor(
    val repository: GeOsRepository,
    val statusScanUseCase: GeOsStatusScanUseCase,
): UseCaseWithoutFlow<GeOsMeasureScanUseCase.Input, List<GeOsDeviceItem>> {

    data class Input(
        val geOs: GeOs,
        val productCode: Long,
        val serialCode: Long,
        val isContinuousForm: Boolean,
        val expiredGeOsVgList: List<GeOsVg>
    )

    override fun invoke(input: Input): List<GeOsDeviceItem> {

        val (
            geOs,
            productCode,
            serialCode,
            isContinuousForm,
            expiredGeOsVgList,
        ) = input


        val createGeOsDeviceItemList = repository.createGeOsDeviceItemList(
            geOs.customer_code,
            geOs.custom_form_type,
            geOs.custom_form_code,
            geOs.custom_form_version,
            geOs.custom_form_data,
            productCode.toInt(),
            serialCode.toInt(),
            geOs.value_sufix,
            geOs.restriction_decimal,
        )

        val measureConsider = geOs.getMeasureConsider()
        val dateStartLastMinute = geOs.getDateConsider()

        createGeOsDeviceItemList.forEach { item ->
            //

            statusScanUseCase.invoke(
                GeOsStatusScanUseCase.Input(
                    geOs,
                    item,
                    measureConsider,
                    dateStartLastMinute,
                    isContinuousForm,
                )
            )

            //
            item.status_modification_type = GeOsDeviceItemStatusModificationType.ITEM
            //
            when (item.item_check_status) {
                GeOsDeviceItem.ITEM_CHECK_STATUS_NORMAL -> {
                    item.color_item = GeOsDeviceItemStatusColor.GRAY
                    if (!isContinuousForm
                        || item.partitioned_execution == 0) {
                        if (item.vg_code != null) {
                            val index = expiredGeOsVgList.binarySearch {
                                it.vgCode.compareTo(item.vg_code)
                            }
                            if (index >= 0) {
                                item.item_check_status = expiredGeOsVgList[index].vgStatus
                                item.color_item = GeOsDeviceItemStatusColor.BLUE
                                item.status_modification_type =
                                    GeOsDeviceItemStatusModificationType.VERIFICATION_GROUP
                            }
                        } else if (item.next_cycle_measure == null && item.next_cycle_limit_date == null) {//sem ciclo
                            item.color_item = GeOsDeviceItemStatusColor.BLUE
                        }
                    }
                }

                GeOsDeviceItem.ITEM_CHECK_STATUS_MANUAL_ALERT -> {
                    item.color_item = GeOsDeviceItemStatusColor.RED
                }

                else -> {
                    if (item.isCritical) {
                        item.color_item = GeOsDeviceItemStatusColor.YELLOW
                    } else {
                        item.color_item = GeOsDeviceItemStatusColor.BLUE
                    }
                }
            }
        }

        return createGeOsDeviceItemList
    }

}