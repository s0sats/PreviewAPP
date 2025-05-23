package com.namoadigital.prj001.ui.act011.group_verification.domain.usecase

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.core.form_os.domain.repository.GeOsRepository
import com.namoadigital.prj001.core.form_os.domain.usecase.GeOsSaveSerialStructureUseCase
import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.model.masterdata.ge_os.GeOs
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem
import com.namoadigital.prj001.model.masterdata.ge_os.vg.GeOsVg
import com.namoadigital.prj001.ui.act011.group_verification.domain.model.VerificationGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

class SaveSerialStructureUseCase @Inject constructor(
    private val repository: GeOsRepository
) : UseCases<SaveSerialStructureUseCase.Input, Boolean> {

    data class Input(
        val geOs: GeOs,
        val mdSerial: MD_Product_Serial,
        val geOsDeviceItems: List<GeOsDeviceItem>,
        val geOsVgs: List<GeOsVg>,
        val listGroups: List<VerificationGroup>
    )

    override suspend fun invoke(input: Input): Flow<IResult<Boolean>> = flow {
        emit(loading())

        val geOsVgsToSave: List<GeOsVg> = input.listGroups
            .mapNotNull { group ->
                group.vgCode?.let { code ->
                    input.geOsDeviceItems.find { it.vg_code == code }?.apply {
                        this.is_visible = if (group.selected) 1 else 0
                    }
                    input.geOsVgs.find { it.vgCode == code }?.apply {
                        this.isActive = if (group.selected) 1 else 0
                    }
                }
            }

        val geOsStructure = repository.setGeOsStructure(
            input.geOs,
            input.mdSerial,
            input.geOsDeviceItems,
            geOsVgsToSave,
        )

        if (!geOsStructure.hasError()) {
            emit(success(true))
        } else {
            emit(failed(Exception(geOsStructure.errorMsg)))
        }
    }.catch { e ->
        emit(failed(e))
    }.flowOn(Dispatchers.IO)
}
