package com.namoadigital.prj001.sql.transaction.form

import android.content.Context
import com.namoadigital.prj001.dao.BaseDao
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao
import com.namoadigital.prj001.dao.MD_SiteDao
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.model.GE_Custom_Form_Data
import com.namoadigital.prj001.model.GE_Custom_Form_Local
import com.namoadigital.prj001.model.MD_Schedule_Exec
import com.namoadigital.prj001.model.MD_Site
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_002
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class TransactionWsSave(
    context: Context,
    val formDataDao: GE_Custom_Form_DataDao,
    val formLocalDao: GE_Custom_Form_LocalDao,
    val scheduleExecDao: MD_Schedule_ExecDao,
    val siteDao: MD_SiteDao,
    val serialDao: MD_Product_SerialDao,
    val fsTripDao: FSTripDao,
    mDB_NAME: String = ToolBox_Con.customDBPath(
        ToolBox_Con.getPreference_Customer_Code(
            context
        )
    ),
    mDB_VERSION: Int = Constant.DB_VERSION_CUSTOM
) : BaseDao(
    context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI
) {

    @Throws(Exception::class)
    fun saveForms(
        formLocals: List<GE_Custom_Form_Local>,
        formSchedules: List<MD_Schedule_Exec>,
        formDatas: List<GE_Custom_Form_Data>,
        siteExecution:List<MD_Site>
    ):Boolean{
        openDB()
        var transactionResult = true
        db.beginTransaction()
        //
        try {
            //
            formDatas.forEach {
                val daoObjReturn =
                    formDataDao.addUpdateWithReturnAndSharedDbInstance(it, db)

                if (daoObjReturn.hasError()) {
                    transactionResult = false
                }
                //
                val serial = serialDao.getByString(
                    MD_Product_Serial_Sql_002(
                        it.customer_code,
                        it.product_code,
                        it.serial_id
                    ).toSqlQuery(),
                    db
                )
                //
                /**
                 * BARRIONUEVO 24-01-2024
                 * Utilizado order_type_code para identificar se o formData eh form os
                 */
                serial?.let { serial ->

                    if (serial.has_item_check == 1
                        && it.order_type_code != null
                    ) {
                        serial.syncStructure = 1
                        val serialResult = serialDao.addUpdateWithDaoObjReturn(serial, db)

                        if (serialResult.hasError()) {
                            transactionResult = false
                        }
                    }
                }
            }
            //
            val formLocalResult =
                formLocalDao.addUpdateThrowExceptionWithSharedDbInstance(formLocals, false, db)
            //

            if (formLocalResult.hasError()) {
                transactionResult = false
            }
            //
            val scheduleResult = scheduleExecDao.addUpdate(formSchedules, false, db)

            if (scheduleResult.hasError()) {
                transactionResult = false
            }
            //
            if (siteExecution.size > 0) {
                var siteResult =
                    siteDao.addUpdateWithReturnAndSharedDbInstance(siteExecution, db)

                if (siteResult.hasError()) {
                    transactionResult = false
                }
            }
            //
            fsTripDao.setSyncRequired()
            //
            if (transactionResult) {
                db.setTransactionSuccessful()
            }
        }catch (e:Exception){
            transactionResult = false
            ToolBox_Inf.registerException(javaClass.name, e)
        }finally {
            db.endTransaction()
            closeDB()
        }
        return transactionResult
    }


}