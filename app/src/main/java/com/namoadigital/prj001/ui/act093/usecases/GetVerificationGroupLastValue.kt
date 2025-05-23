package com.namoadigital.prj001.ui.act093.usecases

import android.content.Context
import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.extensions.serial.formatLastValue
import com.namoadigital.prj001.ui.act093.data.repository.InfoSerialRepository

class GetVerificationGroupLastValue constructor(
    private val repository: InfoSerialRepository,
    private val context: Context
) : UseCaseWithoutFlow<GetVerificationGroupLastValue.Input, String?> {

    data class Input(
        val vg_code: Int,
        val value_suffix: String?,
    )
    override fun invoke(model: Input): String? {
        val prefSerialRestrictionDecimal = repository.getPrefSerialRestrictionDecimal()
        val vg = repository.getMDProductSerialVg(model.vg_code)
        return vg?.formatLastValue(context, prefSerialRestrictionDecimal, model.value_suffix)
    }


}