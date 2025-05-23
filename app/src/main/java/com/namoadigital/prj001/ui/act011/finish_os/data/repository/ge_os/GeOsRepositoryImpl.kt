package com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_os

import android.content.Context
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.dao.GeOsDao
import com.namoadigital.prj001.dao.GeOsDeviceDao
import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.extensions.coroutines.dispatchersIO
import com.namoadigital.prj001.extensions.coroutines.flowCatch
import com.namoadigital.prj001.extensions.getCustomerCode
import com.namoadigital.prj001.model.masterdata.ge_os.GeOs
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDevice
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class GeOsRepositoryImpl @Inject constructor(
    private val context: Context,
    private val dao: GeOsDao,
    private val deviceDao: GeOsDeviceDao,
    private val deviceItemDao: GeOsDeviceItemDao
): GeOsRepository {

    override suspend fun getGeOsById(
        formType: Int,
        formCode: Int,
        formVersion: Int,
        formData: Long
    ): Flow<IResult<GeOs?>> {
        return flow {
            emit(loading())

            val geOs = dao.getGeOsById(
                context.getCustomerCode(),
                formType.toString(),
                formCode.toString(),
                formVersion.toString(),
                formData.toString()
            )

            emit(loading(false))
            emit(success(geOs))
        }.flowCatch(this::class.java.name).dispatchersIO()
    }


    override suspend fun getAllDeviceById(
        formType: Int,
        formCode: Int,
        formVersion: Int,
        formData: Long
    ) : Flow<IResult<List<GeOsDevice>>> {
        return flow {
            emit(loading())

            val list = deviceDao.getAllDeviceById(
                customerCode = context.getCustomerCode(),
                type = formType,
                code = formCode,
                version = formVersion,
                data = formData
            )

            emit(loading(false))
            emit(success(list))

        }.flowCatch(this::class.java.name).dispatchersIO()
    }


    override suspend fun getListDeviceItemById(
        formType: Int,
        formCode: Int,
        formVersion: Int,
        formData: Long,
        productCode: Long,
        serialCode: Long,
        deviceTpCode: Int
    ): Flow<IResult<List<GeOsDeviceItem>>> {
        return flow {
            emit(loading())

            val deviceItemsList = deviceItemDao.getListDeviceItemById(
                customerCode = context.getCustomerCode(),
                formType = formType,
                formCode = formCode,
                formVersion = formVersion,
                formData = formData,
                productCode = productCode,
                serialCode = serialCode,
                deviceTpCode = deviceTpCode
            )

            val list = deviceItemsList ?: emptyList()

            emit(loading(false))
            emit(success(list))
        }.flowCatch(this::class.java.name).dispatchersIO()
    }
}