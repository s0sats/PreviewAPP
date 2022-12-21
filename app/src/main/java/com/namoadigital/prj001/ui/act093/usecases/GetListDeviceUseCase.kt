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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last

class GetListDeviceUseCase constructor(
    private val repository: InfoSerialRepository
) : UseCases<Unit, DeviceTpModel> {
    override suspend fun invoke(input: Unit): Flow<IResult<DeviceTpModel>> {
        return flow {

            repository.getListItems()
                .collect {
                    it.isLoading { loading, message ->
                        emit(loading(loading, message))
                    }

                    it.isSuccess { device ->

                        val itemList = mutableListOf<MD_Product_Serial_Tp_Device_Item>()
                        device.map { m -> m.item }.forEach { list ->
                            list.forEach { item ->
                                itemList.add(item)
                            }
                        }

                        val materialList =
                            mutableListOf<MD_Product_Serial_Tp_Device_Item_Material>()
                        itemList.map { m -> m.material }.forEach { list ->
                            list.forEach { material ->
                                materialList.add(material)
                            }
                        }
                        var deviceTpList: List<MdDeviceTp>? = null
                        repository.getListDeviceTp().last().isSuccess { md ->
                            deviceTpList = md
                        }

                        var newList = mutableListOf<DeviceTpModel>()


                    }

                    it.isFailed { exception ->
                        emit(failed(exception))
                    }

                }
        }.namoaCatch("GetListDeviceUseCase")
    }
}