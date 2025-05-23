package com.namoadigital.prj001.core.form_os.domain.repository

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.model.masterdata.ge_os.GeOs
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem
import com.namoadigital.prj001.model.masterdata.ge_os.vg.GeOsVg
import kotlinx.coroutines.flow.Flow

interface GeOsRepository {
    fun createGeOsDeviceItemList(
        customerCode: Long,
        customFormType: Int,
        customFormCode: Int,
        customFormVersion: Int,
        customFormData: Int,
        productCode: Int,
        serialCode: Int,
        valueSufix: String?,
        restrictionDecimal: Int?
    ):MutableList<GeOsDeviceItem>

    fun setGeOsStructure(
        geOs: GeOs,
        mdSerial: MD_Product_Serial,
        geOsDeviceItens: List<GeOsDeviceItem>,
        geOsVgs: List<GeOsVg>,
    ): DaoObjReturn

    fun createGeOsVg(
        customFormType: Int,
        customFormCode: Int,
        customFormVersion: Int,
        customFormData: Int,
        productCode: Long,
        serialCode: Long,
        valueSuffix: String?,
        restrictionDecimal: Int?
    ): List<GeOsVg>


    suspend fun updateActiveFromGeOsVg(
        customerCode: Long,
        customFormType: Int,
        customFormCode: Int,
        customFormVersion: Int,
        customFormData: Int,
        productCode: Long,
        serialCode: Long,
        vgCode: Int,
        isActive: Int,
        isContinuousForm: Boolean,
    ) : Flow<IResult<GeOsVg>>

    @Throws(Exception::class)
    suspend fun getAllGeOsVgsByFormCode(
        customerCode: Long,
        customFormType: Int,
        customFormCode: Int,
        customFormVersion: Int,
        customFormData: Int,
        productCode: Long,
        serialCode: Long,
    ) : List<GeOsVg>

    @Throws(Exception::class)
    suspend fun getAllDeviceItems(
        customerCode: Long,
        customFormType: Int,
        customFormCode: Int,
        customFormVersion: Int,
        customFormData: Int,
        productCode: Long,
        serialCode: Long,
    ) : List<GeOsDeviceItem>
}