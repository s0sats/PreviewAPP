package com.namoadigital.prj001.ui.act093.usecases

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.isFailed
import com.namoadigital.prj001.core.IResult.Companion.isLoading
import com.namoadigital.prj001.core.IResult.Companion.isSuccess
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.core.extension.namoaCatch
import com.namoadigital.prj001.model.MD_Product_Serial_Tp_Device_Item
import com.namoadigital.prj001.model.MD_Product_Serial_Tp_Device_Item_Material
import com.namoadigital.prj001.model.MdDeviceTp
import com.namoadigital.prj001.ui.act093.data.repository.InfoSerialRepository
import com.namoadigital.prj001.ui.act093.model.DeviceTpModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last


class GetListDeviceUseCase constructor(
    private val repository: InfoSerialRepository
) : UseCases<Unit, MutableList<DeviceTpModel>> {
    override suspend fun invoke(input: Unit): Flow<IResult<MutableList<DeviceTpModel>>> {
        return repository.getListItems()
    }
}