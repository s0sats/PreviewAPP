package com.namoadigital.prj001.core.form_os.domain.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.dao.md.MDItemCheckLabelDao
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem.Companion.EXEC_TYPE_ALREADY_OK
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem.Companion.EXEC_TYPE_FIXED
import com.namoadigital.prj001.ui.act086.frg_verification.form_utils.FormItemCheckLabelIcon

class GetDeviceItemIconAndOptionUseCase (
    val mdItemCheckLabelDao: MDItemCheckLabelDao
) : UseCaseWithoutFlow<GetDeviceItemIconAndOptionUseCase.Input, FormItemCheckLabelIcon?> {


    data class Input(
        val geOsDeviceItem: GeOsDeviceItem,
        val execType: String
    )

    override fun invoke(input: Input): FormItemCheckLabelIcon {
        input.apply {
            //
            val labelCode = when (execType) {
                EXEC_TYPE_FIXED -> {
                    geOsDeviceItem.labelFixed
                }
                EXEC_TYPE_ALREADY_OK -> {
                    geOsDeviceItem.labelAlreadyOk
                }
                else -> {
                    0
                }
            }

            return mdItemCheckLabelDao.getItemCheckLabelIcons(labelCode)
        }
    }
}