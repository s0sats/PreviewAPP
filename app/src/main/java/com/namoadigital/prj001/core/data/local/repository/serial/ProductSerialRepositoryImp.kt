package com.namoadigital.prj001.core.data.local.repository.serial

import android.content.Context
import com.google.gson.GsonBuilder
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.extensions.coroutines.dispatchersIO
import com.namoadigital.prj001.extensions.coroutines.flowCatch
import com.namoadigital.prj001.extensions.getCustomerCode
import com.namoadigital.prj001.extensions.loadGenericTranslation
import com.namoadigital.prj001.model.BaseSerialSearchItem
import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.model.T_MD_Product_Serial_Backup_Env
import com.namoadigital.prj001.model.T_MD_Product_Serial_Backup_Rec
import com.namoadigital.prj001.sql.Act087Sql_001
import com.namoadigital.prj001.ui.act011.finish_os.di.model.FinishFormBackupMachineList
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishState
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProductSerialRepositoryImp @Inject constructor(
    private val context: Context,
    private val dao: MD_Product_SerialDao,
) : ProductSerialRepository {

    private val genericTranslate by lazy {
        context.loadGenericTranslation()
    }


    override fun getListSerialsBySiteCode(siteCode: Int): List<MD_Product_Serial> {
        return dao.getListSerialsBySiteCode(siteCode)
    }

    override fun getProductSerialById(
        productCode: Long,
        serialId: String
    ): Flow<IResult<MD_Product_Serial?>> {
        return flow {
            emit(loading())

            val productSerial = dao.getProductSerialById(
                context.getCustomerCode(),
                productCode,
                serialId
            )

            emit(success(productSerial.firstOrNull()))

        }.flowCatch(this::class.java.simpleName).dispatchersIO()
    }

    override suspend fun getBackupSerialList(
        serialId: String?,
        autoSelection: Boolean,
        formPk: FinishState.FormPrimaryKey
    ): Flow<IResult<FinishFormBackupMachineList>> {
        return flow {
            //
            emit(loading())

            val serial = getCustomFormSerial(
                formTypeCode = formPk.typeCode,
                formCode = formPk.code,
                formVersionCode = formPk.versionCode,
                formData = formPk.formData,
            )
            serial?.let { serial ->
                if (ToolBox_Con.isOnline(context)) {
                    //
                    val gson = GsonBuilder().serializeNulls().create()
                    //
                    val env = T_MD_Product_Serial_Backup_Env(
                        serial.product_code,
                        serial.serial_code,
                        null,
                        serialId,
                        ToolBox_Con.getPreference_Site_Code(context).toInt()
                    )
                    //
                    env.app_code = Constant.PRJ001_CODE
                    env.app_version = Constant.PRJ001_VERSION
                    env.session_app = ToolBox_Con.getPreference_Session_App(context)
                    env.app_type = Constant.PKG_APP_TYPE_DEFAULT
                    //
                    val resultado = ToolBox_Con.connWebService(
                        Constant.WS_PRODUCT_SERIAL_BACKUP,
                        gson.toJson(env)
                    )
                    val rec = gson.fromJson(
                        resultado,
                        T_MD_Product_Serial_Backup_Rec::class.java
                    )

                    rec?.let{
                        val bkpSerialItemList: List<BaseSerialSearchItem.BackupMachineSerialItem>? =
                            it.records?.map { bkpOffline ->
                                BaseSerialSearchItem.BackupMachineSerialItem(
                                    bkpOffline.productCode,
                                    bkpOffline.productId,
                                    bkpOffline.productDesc,
                                    bkpOffline.serialCode,
                                    bkpOffline.serialId,
                                    bkpOffline.siteCode,
                                    bkpOffline.siteDesc
                                )
                            }
                        emit(success(FinishFormBackupMachineList(false, bkpSerialItemList)))
                    }
                } else {
                    val bkpSerialItemList: List<BaseSerialSearchItem.BackupMachineSerialItem>? = dao.query(
                        Act087Sql_001(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            serial.product_code,
                            serial.serial_id,
                            ToolBox_Inf.getNoAccentStringForGlobSql(serialId),
                            ToolBox_Con.getPreference_Site_Code(context).toInt()
                        ).toSqlQuery()
                    )?.map { bkpOffline ->
                        BaseSerialSearchItem.BackupMachineSerialItem(
                            bkpOffline.product_code.toInt(),
                            bkpOffline.product_id,
                            bkpOffline.product_desc,
                            bkpOffline.serial_code.toInt(),
                            bkpOffline.serial_id,
                            bkpOffline.site_code,
                            bkpOffline.site_desc
                        )
                    }

                    emit(success(FinishFormBackupMachineList(false, bkpSerialItemList)))
                }
            }?: emit(error(MD_Product_SerialDao.NOT_FOUND_ERROR))
        }.flowCatch(this::class.java.name).flowOn(Dispatchers.IO)
    }

    private fun getCustomFormSerial(
        formTypeCode: Int,
        formCode: Int,
        formVersionCode: Int,
        formData: Long
    ): MD_Product_Serial? {
        return dao.getByString(
            """
                SELECT *
                  FROM ${MD_Product_SerialDao.TABLE} serial
                 INNER JOIN ${GE_Custom_Form_DataDao.TABLE} form
                    ON serial.${MD_Product_SerialDao.CUSTOMER_CODE} = form.${GE_Custom_Form_DataDao.CUSTOMER_CODE}
                   AND serial.${MD_Product_SerialDao.PRODUCT_CODE} = form.${GE_Custom_Form_DataDao.PRODUCT_CODE}  
                   AND serial.${MD_Product_SerialDao.SERIAL_ID} = form.${GE_Custom_Form_DataDao.SERIAL_ID}
                 WHERE form.${GE_Custom_Form_DataDao.CUSTOMER_CODE} = ${
                ToolBox_Con.getPreference_Customer_Code(
                    context
                )
            }
                   AND form.${GE_Custom_Form_DataDao.CUSTOM_FORM_TYPE} = $formTypeCode  
                   AND form.${GE_Custom_Form_DataDao.CUSTOM_FORM_CODE} = $formCode 
                   AND form.${GE_Custom_Form_DataDao.CUSTOM_FORM_VERSION} = $formVersionCode  
                   AND form.${GE_Custom_Form_DataDao.CUSTOM_FORM_DATA} = $formData   
            """.trimIndent()
        )
    }
}