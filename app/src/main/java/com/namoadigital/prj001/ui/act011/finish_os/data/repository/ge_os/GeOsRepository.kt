package com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_os

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.model.masterdata.ge_os.GeOs
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDevice
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem
import kotlinx.coroutines.flow.Flow

interface GeOsRepository {

    suspend fun getGeOsById(
        formType: Int,
        formCode: Int,
        formVersion: Int,
        formData: Long
    ): Flow<IResult<GeOs?>>

    suspend fun getAllDeviceById(
        formType: Int,
        formCode: Int,
        formVersion: Int,
        formData: Long
    ): Flow<IResult<List<GeOsDevice>>>


    suspend fun getListDeviceItemById(
        formType: Int,
        formCode: Int,
        formVersion: Int,
        formData: Long,
        productCode: Long,
        serialCode: Long,
        deviceTpCode: Int
    ) : Flow<IResult<List<GeOsDeviceItem>>>
}