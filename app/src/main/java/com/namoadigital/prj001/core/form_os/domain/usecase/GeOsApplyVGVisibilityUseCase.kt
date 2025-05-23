package com.namoadigital.prj001.core.form_os.domain.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem.Companion.ITEM_CHECK_STATUS_MANUALLY_FORCED_ITEM
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem.Companion.ITEM_CHECK_STATUS_MANUAL_ALERT
import com.namoadigital.prj001.model.masterdata.ge_os.vg.GeOsVg

class GeOsApplyVGVisibilityUseCase :
    UseCaseWithoutFlow<GeOsApplyVGVisibilityUseCase.Input, Unit> {
    data class Input(
        val geosVgs: List<GeOsVg>,
        val deviceItems: List<GeOsDeviceItem>,
        val isContinuousForm: Boolean,
    )

    override fun invoke(input: Input) {
        input.deviceItems.filter {
            it.vg_code != null
        }.forEach {
            val index = input.geosVgs.binarySearch { vg ->
                vg.vgCode.compareTo(it.vg_code!!)
            }
            if (index >= 0
                && it.item_check_status != ITEM_CHECK_STATUS_MANUAL_ALERT
                && it.item_check_status != ITEM_CHECK_STATUS_MANUALLY_FORCED_ITEM
            ){
                if(input.isContinuousForm
                    && it.partitioned_execution == 1){
                    it.is_visible = 1
                } else {
                    it.is_visible = input.geosVgs[index].isActive
                }
            }
        }
    }
}