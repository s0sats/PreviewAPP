package com.namoadigital.prj001.core.data.local.repository.ge_os

import android.content.Context
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.isFailed
import com.namoadigital.prj001.core.IResult.Companion.isSuccess
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.core.form_os.domain.repository.GeOsRepository
import com.namoadigital.prj001.dao.GeOsDao
import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.dao.GeOsVgDao
import com.namoadigital.prj001.extensions.coroutines.namoaCatch
import com.namoadigital.prj001.extensions.getCustomerCode
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.model.masterdata.ge_os.GeOs
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem.Companion.ITEM_CHECK_STATUS_MANUALLY_FORCED_ITEM
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem.Companion.ITEM_CHECK_STATUS_MANUAL_ALERT
import com.namoadigital.prj001.model.masterdata.ge_os.vg.GeOsVg

import com.namoadigital.prj001.sql.GeOsDeviceItemCreation_Sql_001
import com.namoadigital.prj001.sql.transaction.DatabaseTransactionManager
import com.namoadigital.prj001.ui.act011.group_verification.VerificationGroupFragment.Companion.LOADING_UPDATE_GROUP_LBL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GeOsRepositoryImpl constructor(
    val context: Context,
    val dao: GeOsDao,
    val geOsDeviceItemDao: GeOsDeviceItemDao,
    val vgDao: GeOsVgDao
) : GeOsRepository {


    override fun createGeOsDeviceItemList(
        customerCode: Long,
        customFormType: Int,
        customFormCode: Int,
        customFormVersion: Int,
        customFormData: Int,
        productCode: Int,
        serialCode: Int,
        valueSufix: String?,
        restrictionDecimal: Int?
    ): MutableList<GeOsDeviceItem> {
        return geOsDeviceItemDao.query(
            GeOsDeviceItemCreation_Sql_001(
                customerCode,
                customFormType,
                customFormCode,
                customFormVersion,
                customFormData,
                productCode,
                serialCode,
                valueSufix,
                restrictionDecimal,
            ).toSqlQuery()
        )
    }

    override fun setGeOsStructure(
        geOs: GeOs,
        mdSerial: MD_Product_Serial,
        geOsDeviceItens: List<GeOsDeviceItem>,
        geOsVgs: List<GeOsVg>,
    ): DaoObjReturn {
        return dao.saveGeOsSctructure(
            geOs,
            mdSerial,
            geOsDeviceItens,
            geOsVgs,
        )
    }


    override fun createGeOsVg(
        customFormType: Int,
        customFormCode: Int,
        customFormVersion: Int,
        customFormData: Int,
        productCode: Long,
        serialCode: Long,
        valueSuffix: String?,
        restrictionDecimal: Int?
    ): List<GeOsVg> {
        return vgDao.createGeOsVg(
            context.getCustomerCode(),
            customFormType,
            customFormCode,
            customFormVersion,
            customFormData,
            productCode.toInt(),
            serialCode.toInt(),
            valueSuffix,
            restrictionDecimal
        )
    }

    override suspend fun updateActiveFromGeOsVg(
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
    ): Flow<IResult<GeOsVg>> = flow {
        emit(loading(message = LOADING_UPDATE_GROUP_LBL))
        DatabaseTransactionManager(context).executeTransaction { db ->
            db.execSQL(
                """
                    UPDATE ${GeOsVgDao.TABLE_NAME}
                    SET ${GeOsVgDao.IS_ACTIVE} = $isActive
                    WHERE ${GeOsVgDao.VG_CODE} = $vgCode
                         AND ${GeOsVgDao.CUSTOMER_CODE} = $customerCode
                         AND ${GeOsVgDao.CUSTOM_FORM_CODE} = $customFormCode
                         AND ${GeOsVgDao.CUSTOM_FORM_TYPE} = $customFormType
                         AND ${GeOsVgDao.CUSTOM_FORM_VERSION} = $customFormVersion
                         AND ${GeOsVgDao.CUSTOM_FORM_DATA} = $customFormData
                         AND ${GeOsVgDao.PRODUCT_CODE} = $productCode
                         AND ${GeOsVgDao.SERIAL_CODE} = $serialCode
                        
                """.trimIndent()
            )
            var partitionedFilter = ""

            if(isContinuousForm){
                partitionedFilter = """AND ${GeOsDeviceItemDao.PARTITIONED_EXECUTION} = 0"""
            }

            db.execSQL(
                """
                    UPDATE ${GeOsDeviceItemDao.TABLE}
                    SET ${GeOsDeviceItemDao.IS_VISIBLE} = $isActive
                    WHERE ${GeOsDeviceItemDao.VG_CODE} = $vgCode
                         AND ${GeOsVgDao.CUSTOMER_CODE} = $customerCode
                         AND ${GeOsDeviceItemDao.CUSTOM_FORM_TYPE} = $customFormType
                         AND ${GeOsDeviceItemDao.CUSTOM_FORM_CODE} = $customFormCode
                         AND ${GeOsDeviceItemDao.CUSTOM_FORM_VERSION} = $customFormVersion
                         AND ${GeOsDeviceItemDao.CUSTOM_FORM_DATA} = $customFormData 
                         AND ${GeOsDeviceItemDao.PRODUCT_CODE} = $productCode 
                         AND ${GeOsDeviceItemDao.SERIAL_CODE} = $serialCode 
                         AND ${GeOsDeviceItemDao.ITEM_CHECK_STATUS} NOT IN ('${ITEM_CHECK_STATUS_MANUAL_ALERT}', '${ITEM_CHECK_STATUS_MANUALLY_FORCED_ITEM}')
                         $partitionedFilter
                """.trimIndent()
            )
        }.isSuccess {
            val item = vgDao.getGeOsVgByForm(
                customerCode,
                customFormType,
                customFormCode,
                customFormVersion,
                customFormData.toLong(),
                vgCode
            )!!
            emit(success(item))
        }.isFailed { e ->
            emit(failed(e))
        }
    }.namoaCatch(GeOsRepositoryImpl::class).flowOn(Dispatchers.IO)

    override suspend fun getAllGeOsVgsByFormCode(
        customerCode: Long,
        customFormType: Int,
        customFormCode: Int,
        customFormVersion: Int,
        customFormData: Int,
        productCode: Long,
        serialCode: Long,
    ): List<GeOsVg> {
        return vgDao.getGeOsVgByFormCode(
            customerCode,
            customFormType,
            customFormCode,
            customFormVersion,
            customFormData.toLong(),
            productCode,
            serialCode
        )
    }

    @Throws(Exception::class)
    override suspend fun getAllDeviceItems(
        customerCode: Long,
        customFormType: Int,
        customFormCode: Int,
        customFormVersion: Int,
        customFormData: Int,
        productCode: Long,
        serialCode: Long,
    ): List<GeOsDeviceItem> {
        return geOsDeviceItemDao.getListDeviceItemByForm(
            customerCode,
            customFormType,
            customFormCode,
            customFormVersion,
            customFormData.toLong(),
            productCode,
            serialCode
        )
    }
}