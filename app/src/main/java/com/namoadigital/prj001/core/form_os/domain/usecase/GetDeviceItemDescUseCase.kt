package com.namoadigital.prj001.core.form_os.domain.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.form_os.domain.usecase.GetDeviceItemDescUseCase.Input
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItemStatusModificationType

class GetDeviceItemDescUseCase() :
    UseCaseWithoutFlow<Input, String?> {


    data class Input(
        val manual_desc: String?,
        val status_modification_type: GeOsDeviceItemStatusModificationType?,
        val item_check_desc: String,
        val item_check_desc_alt_vg: String?,
        val isNO_CYCLE: Boolean

    )



    override fun invoke(input: Input): String {
        val itemDesc:String
        input.apply {
            //
            if(manual_desc != null){
                itemDesc = manual_desc
            }else{
                if (GeOsDeviceItemStatusModificationType.VERIFICATION_GROUP == status_modification_type
                    && item_check_desc_alt_vg != null
                    && !isNO_CYCLE
                ){
                    itemDesc = item_check_desc_alt_vg
                }else{
                    itemDesc = item_check_desc
                }
            }
        }

        //
        return itemDesc
    }
}