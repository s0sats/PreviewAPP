package com.namoadigital.prj001.core.form_os.domain.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.model.masterdata.ge_os.GeOs
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem
import com.namoadigital.prj001.model.MdOrderType

class GeOsOrderTypeVisibilityScanUseCase :
    UseCaseWithoutFlow<GeOsOrderTypeVisibilityScanUseCase.Input, List<GeOsDeviceItem>> {
    data class Input(
        val formOsHeader: GeOs,
        val deviceItems: List<GeOsDeviceItem>
    )

    override fun invoke(input: Input): List<GeOsDeviceItem> {
        val (
            formOsHeader,
            deviceItems,
        ) = input

        deviceItems.filter {
            it.vg_code == null
        }.forEach { item ->
            when (formOsHeader.display_option) {
                //Se show all, pega os itens em status normal e
                MdOrderType.DISPLAY_OPTION_SHOW_ALL -> {
                    formOsHeader.item_check_group_code?.let {
                        if (item.item_check_status.equals(
                                GeOsDeviceItem.ITEM_CHECK_STATUS_NORMAL,
                                true
                            )
                            && it.equals(item.item_check_group_code)
                        ) {
                            item.is_visible = 1
                        }
                    } ?: if (
                        item.item_check_status.equals(GeOsDeviceItem.ITEM_CHECK_STATUS_NORMAL, true)
                    ) {
                        item.is_visible = 1
                    } else {

                    }

                }

                MdOrderType.DISPLAY_OPTION_SHOW_ONLY_CRITICAL -> {
                    if (item.critical_item == 1) {
                        item.is_visible = 1
                    } else {
                        if ((item.item_check_status.equals(
                                GeOsDeviceItem.ITEM_CHECK_STATUS_NO_CYCLE,
                                true
                            )
                                    || item.item_check_status.equals(
                                GeOsDeviceItem.ITEM_CHECK_STATUS_MEASURE_ALERT,
                                true
                            )
                                    || item.item_check_status.equals(
                                GeOsDeviceItem.ITEM_CHECK_STATUS_PROJECTED_DATE_REACHED,
                                true
                            )
                                    || item.item_check_status.equals(
                                GeOsDeviceItem.ITEM_CHECK_STATUS_LIMIT_DATE_REACHED,
                                true
                            )
                                    )
                        ) {
                            item.is_visible = 0
                        }
                    }
                }
                else -> {}
            }
            //
            if (item.partitioned_execution == 1) {
                item.is_visible = 1
            }
        }
        return deviceItems
    }

}