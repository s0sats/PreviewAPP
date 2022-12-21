package com.namoadigital.prj001.ui.act093.data.repository

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.model.MD_Product_Serial_Tp_Device
import com.namoadigital.prj001.model.MdDeviceTp
import com.namoadigital.prj001.ui.act092.model.SerialModel
import kotlinx.coroutines.flow.Flow

interface InfoSerialRepository {

    suspend fun getInfoSerial(): Flow<IResult<MD_Product_Serial>>

    suspend fun getPreferences(): Flow<IResult<SerialModel>>
    suspend fun getValueSuffixProduct(customerCode: Long, code: Int): Flow<IResult<String?>>
    suspend fun getListItems(): Flow<IResult<List<MD_Product_Serial_Tp_Device>>>
    suspend fun getListDeviceTp(): Flow<IResult<List<MdDeviceTp>>>
}