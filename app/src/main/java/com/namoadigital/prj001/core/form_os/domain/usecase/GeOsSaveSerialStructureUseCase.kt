package com.namoadigital.prj001.core.form_os.domain.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.form_os.domain.repository.GeOsRepository
import com.namoadigital.prj001.core.form_os.domain.usecase.GeOsSaveSerialStructureUseCase.Input
import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.model.masterdata.ge_os.GeOs
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem
import com.namoadigital.prj001.model.masterdata.ge_os.vg.GeOsVg
import javax.inject.Inject

class GeOsSaveSerialStructureUseCase @Inject constructor(
    val respository: GeOsRepository,
) : UseCaseWithoutFlow<Input, Boolean> {


    data class Input(
        val geOs: GeOs,
        val mdSerial: MD_Product_Serial,
        val geOsDeviceItens: List<GeOsDeviceItem>,
        val geOsVgs: List<GeOsVg>,
    )

    override fun invoke(input: Input): Boolean {
        val geOsStructure = respository.setGeOsStructure(
            input.geOs,
            input.mdSerial,
            input.geOsDeviceItens,
            input.geOsVgs,
        )
        return !geOsStructure.hasError()
    }


}