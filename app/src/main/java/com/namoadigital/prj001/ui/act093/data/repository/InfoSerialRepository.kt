package com.namoadigital.prj001.ui.act093.data.repository

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.model.MdDeviceTp
import com.namoadigital.prj001.model.masterdata.product_serial.verification_group.MDProductSerialVg
import com.namoadigital.prj001.ui.act092.model.SerialModel
import com.namoadigital.prj001.ui.act093.model.DeviceTpModel
import kotlinx.coroutines.flow.Flow

interface InfoSerialRepository {

    suspend fun getInfoSerial(): Flow<IResult<MD_Product_Serial>>
    suspend fun geMeasureRestrictionDecimal(customerCode: Long, measureTpCode: Int): Int

    suspend fun getPreferences(): Flow<IResult<SerialModel>>
    suspend fun getValueSuffixProduct(customerCode: Long, code: Int): Flow<IResult<String?>>
    suspend fun getListItems(): Flow<IResult<MutableList<DeviceTpModel>>>
    suspend fun getListDeviceTp(): Flow<IResult<List<MdDeviceTp>>>
    fun getPrefSerialRestrictionDecimal(): Int
    fun getMDProductSerialVg(vgCode:Int): MDProductSerialVg?
}