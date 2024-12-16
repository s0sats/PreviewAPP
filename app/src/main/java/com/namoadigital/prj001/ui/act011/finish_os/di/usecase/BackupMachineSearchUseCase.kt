package com.namoadigital.prj001.ui.act011.finish_os.di.usecase

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.core.data.local.repository.serial.ProductSerialRepository
import com.namoadigital.prj001.extensions.coroutines.namoaCatch
import com.namoadigital.prj001.ui.act011.finish_os.di.model.FinishFormBackupMachineList
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishState.FormPrimaryKey
import kotlinx.coroutines.flow.Flow

class BackupMachineSearchUseCase(
    private val productSerialRepository: ProductSerialRepository,
) : UseCases<BackupMachineSearchUseCase.Param, FinishFormBackupMachineList> {

    data class Param(
        val formPk: FormPrimaryKey,
        val autoSelection: Boolean,
        val serialId: String?,
    )

    override suspend fun invoke(input: Param): Flow<IResult<FinishFormBackupMachineList>> {
        return productSerialRepository.getBackupSerialList(
                formPk = input.formPk,
                autoSelection = input.autoSelection,
                serialId = input.serialId,
        ).namoaCatch(this::class.java.simpleName)
    }

}