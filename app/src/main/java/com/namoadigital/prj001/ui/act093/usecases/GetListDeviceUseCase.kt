package com.namoadigital.prj001.ui.act093.usecases

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.ui.act093.data.repository.InfoSerialRepository
import com.namoadigital.prj001.ui.act093.model.DeviceTpModel
import kotlinx.coroutines.flow.Flow


class GetListDeviceUseCase constructor(
    private val repository: InfoSerialRepository
) : UseCases<Unit, MutableList<DeviceTpModel>> {
    override suspend fun invoke(input: Unit): Flow<IResult<MutableList<DeviceTpModel>>> {
        return repository.getListItems()
    }
}