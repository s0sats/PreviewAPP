package com.namoadigital.prj001.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.database.CursorToHMAuxMapper
import com.namoadigital.prj001.database.Mapper
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.MD_Product_Serial_Tp_Device_Item_Hist
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class MD_Product_Serial_Tp_Device_Item_HistDao(
    context: Context,
    mDB_NAME: String,
    mDB_VERSION: Int
) : BaseDao(
    context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI),
    DaoWithReturn<MD_Product_Serial_Tp_Device_Item_Hist>,
    DaoWithReturnSharedDbInstance<MD_Product_Serial_Tp_Device_Item_Hist>{

    companion object{
        const val TABLE = "md_product_serial_tp_device_item_hist"
        const val CUSTOMER_CODE = "customer_code"
        const val PRODUCT_CODE = "product_code"
        const val SERIAL_CODE = "serial_code"
        const val DEVICE_TP_CODE = "device_tp_code"
        const val ITEM_CHECK_CODE = "item_check_code"
        const val ITEM_CHECK_SEQ = "item_check_seq"
        const val SEQ = "SEQ"
        const val EXEC_TYPE = "EXEC_TYPE"
        const val EXEC_MEASURE = "EXEC_MEASURE"
        const val EXEC_DATE = "EXEC_DATE"
        const val EXEC_COMMENT = "EXEC_COMMENT"
        const val EXEC_MATERIAL = "EXEC_MATERIAL"
    }

    private val toMD_Product_Serial_Tp_Device_Item_HistMapper: Mapper<Cursor,MD_Product_Serial_Tp_Device_Item_Hist>
    private val toContentValuesMapper: Mapper<MD_Product_Serial_Tp_Device_Item_Hist,ContentValues>

    init {
        this.toMD_Product_Serial_Tp_Device_Item_HistMapper = CursorToMD_Product_Serial_Tp_Device_Item_HistMapper()
        this.toContentValuesMapper = MD_Product_Serial_Tp_Device_Item_HistToContentValuesMapper()
    }

    @Throws(java.lang.Exception::class)
    private fun getWherePkClause(mdProductSerialTpDeviceItemHist: MD_Product_Serial_Tp_Device_Item_Hist?): StringBuilder{
        mdProductSerialTpDeviceItemHist?.let{
            return java.lang.StringBuilder()
                .append("""
                        ${CUSTOMER_CODE} = '${mdProductSerialTpDeviceItemHist.customer_code}'  
                        AND ${PRODUCT_CODE} = '${mdProductSerialTpDeviceItemHist.product_code}'                           
                        AND ${SERIAL_CODE} = '${mdProductSerialTpDeviceItemHist.serial_code}'                           
                        AND ${DEVICE_TP_CODE} = '${mdProductSerialTpDeviceItemHist.device_tp_code}'                           
                        AND ${ITEM_CHECK_CODE} = '${mdProductSerialTpDeviceItemHist.item_check_code}'                           
                        AND ${ITEM_CHECK_SEQ} = '${mdProductSerialTpDeviceItemHist.item_check_seq}'                           
                        AND ${SEQ} = '${mdProductSerialTpDeviceItemHist.seq}'                           
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
        
    }

    override fun addUpdate(mdProductSerialTpDeviceItemHist: MD_Product_Serial_Tp_Device_Item_Hist?): DaoObjReturn {
        return addUpdate(mdProductSerialTpDeviceItemHist, null)
    }

    override fun addUpdate(mdProductSerialTpDeviceItemHist: MD_Product_Serial_Tp_Device_Item_Hist?, dbInstance: SQLiteDatabase?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        if(dbInstance == null) {
            openDB()
        }else{
            this.db = dbInstance
        }
        try {
            daoObjReturn.table = TABLE
            curAction = DaoObjReturn.UPDATE
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(mdProductSerialTpDeviceItemHist)
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(mdProductSerialTpDeviceItemHist), sbWhere.toString(), null).toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(mdProductSerialTpDeviceItemHist))
            }
        } catch (e: SQLiteException) {
            //Chama metodo que baseado na exception gera obj de retorno setado como erro
            //e contendo msg de erro tratada.
            daoObjReturn = ToolBox_Con.getSQLiteErrorCodeDescription(e.message)
            //Gera arquivo de exception usando dados da exception e do obj de retorno
            ToolBox_Inf.registerException(
                javaClass.name,
                Exception(
                    """
                ${e.message}
                ${daoObjReturn.errorMsg}
                """.trimIndent()
                )
            )
        } catch (e: Exception) {
            //Seta obj de retorno com flag de erro e gera arquivo de exception
            daoObjReturn.setError(true)
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
            daoObjReturn.action = curAction
            daoObjReturn.actionReturn = addUpdateRet
        }
        //
        if (dbInstance == null) {
            closeDB()
        }
        //
        return daoObjReturn
    }


    override fun addUpdate(mdProductSerialTpDeviceItemHists: MutableList<MD_Product_Serial_Tp_Device_Item_Hist>?, status: Boolean): DaoObjReturn {
        return addUpdate(mdProductSerialTpDeviceItemHists,status,null)
    }

    override fun addUpdate(
        mdProductSerialTpDeviceItemHists: MutableList<MD_Product_Serial_Tp_Device_Item_Hist>?,
        status: Boolean,
        dbInstance: SQLiteDatabase?
    ): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        //
        if (dbInstance == null) {
            openDB()
        } else {
            db = dbInstance
        }

        try {
            daoObjReturn.table = TABLE
            curAction = DaoObjReturn.UPDATE

            //Se db não foi passado, inicializa transaction
            if (dbInstance == null) {
                db.beginTransaction()
            }

            if (status) {
                db.delete(TABLE, null, null)
            }

            mdProductSerialTpDeviceItemHists?.forEach { mdProductSerialTpDeviceItemHist ->
                val sbWhere: StringBuilder = getWherePkClause(mdProductSerialTpDeviceItemHist)
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(mdProductSerialTpDeviceItemHist), sbWhere.toString(), null).toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(mdProductSerialTpDeviceItemHist))
                }
            }
            //
            if(dbInstance == null) {
                db.setTransactionSuccessful()
            }
        } catch (e: SQLiteException) {
            //Chama metodo que baseado na exception gera obj de retorno setado como erro
            //e contendo msg de erro tratada.
            daoObjReturn = ToolBox_Con.getSQLiteErrorCodeDescription(e.message)
            //Gera arquivo de exception usando dados da exception e do obj de retorno
            ToolBox_Inf.registerException(
                javaClass.name,
                Exception(
                    """
                ${e.message}
                ${daoObjReturn.errorMsg}
                """.trimIndent()
                )
            )
        } catch (e: Exception) {
            //Seta obj de retorno com flag de erro e gera arquivo de exception
            daoObjReturn.setError(true)
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
            //Atualiza ação realizada no metodo e informação de qtd de registros alterado (update)
            //ou rowId do ultimo insert.
            if (dbInstance == null) {
                db.endTransaction()
            }
            daoObjReturn.action = curAction
            daoObjReturn.actionReturn = addUpdateRet
        }
        //
        if (dbInstance == null) {
            closeDB()
        }
        //
        return daoObjReturn
    }

    override fun addUpdate(sQuery: String?) {
        openDB()
        try {
            db.execSQL(sQuery)
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e);
        } finally {
        }
        closeDB()
    }

    override fun remove(sQuery: String?) {
        openDB()
        try {
            db.execSQL(sQuery)
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e);
        } finally {
        }
        closeDB()
    }

    override fun remove(
        mdProductSerialTpDeviceItemHist: MD_Product_Serial_Tp_Device_Item_Hist?,
        dbInstance: SQLiteDatabase?
    ): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var sqlRet: Long = 0
        val curAction = DaoObjReturn.DELETE
        //
        if (dbInstance == null) {
            openDB()
        } else {
            db = dbInstance
        }
        try {
            daoObjReturn.table = TABLE
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(mdProductSerialTpDeviceItemHist)
            //Tenta update e armazena retorno
            sqlRet = db.delete(TABLE,sbWhere.toString(), null).toLong()
        } catch (e: SQLiteException) {
            //Chama metodo que baseado na exception gera obj de retorno setado como erro
            //e contendo msg de erro tratada.
            daoObjReturn = ToolBox_Con.getSQLiteErrorCodeDescription(e.message)
            //Gera arquivo de exception usando dados da exception e do obj de retorno
            ToolBox_Inf.registerException(
                javaClass.name,
                Exception(
                    """
                ${e.message}
                ${daoObjReturn.errorMsg}
                """.trimIndent()
                )
            )
        } catch (e: Exception) {
            //Seta obj de retorno com flag de erro e gera arquivo de exception
            daoObjReturn.setError(true)
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
            daoObjReturn.action = curAction
            daoObjReturn.actionReturn = sqlRet
        }
        //
        if (dbInstance == null) {
            closeDB()
        }
        return daoObjReturn
    }

    override fun getByString(sQuery: String?): MD_Product_Serial_Tp_Device_Item_Hist? {
        var mdProductSerialTpDeviceItemHist: MD_Product_Serial_Tp_Device_Item_Hist? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                mdProductSerialTpDeviceItemHist = toMD_Product_Serial_Tp_Device_Item_HistMapper.map(cursor)
            }
            //
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return mdProductSerialTpDeviceItemHist
    }

    override fun getByStringHM(sQuery: String?): HMAux? {
        var mdProductSerialTpDeviceItemHist: HMAux? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                mdProductSerialTpDeviceItemHist = CursorToHMAuxMapper.mapN(cursor)
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return  mdProductSerialTpDeviceItemHist
    }

    override fun query(sQuery: String?): MutableList<MD_Product_Serial_Tp_Device_Item_Hist> {
        val mdProductSerialTpDeviceItemHists = mutableListOf<MD_Product_Serial_Tp_Device_Item_Hist>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                val uAux = toMD_Product_Serial_Tp_Device_Item_HistMapper.map(cursor)
                mdProductSerialTpDeviceItemHists.add(uAux)
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return mdProductSerialTpDeviceItemHists
    }

    override fun query_HM(sQuery: String?): MutableList<HMAux> {
        val mdProductSerialTpDeviceItemHists = mutableListOf<HMAux>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                mdProductSerialTpDeviceItemHists.add(CursorToHMAuxMapper.mapN(cursor))
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return mdProductSerialTpDeviceItemHists
    }

    private class CursorToMD_Product_Serial_Tp_Device_Item_HistMapper : Mapper<Cursor, MD_Product_Serial_Tp_Device_Item_Hist> {
        override fun map(cursor: Cursor?): MD_Product_Serial_Tp_Device_Item_Hist? {
            cursor?.let {
                with(cursor){
                    return MD_Product_Serial_Tp_Device_Item_Hist(
                        customer_code = getLong(getColumnIndex(CUSTOMER_CODE)),
                        product_code = getLong(getColumnIndex(PRODUCT_CODE)),
                        serial_code = getLong(getColumnIndex(SERIAL_CODE)),
                        device_tp_code = getInt(getColumnIndex(DEVICE_TP_CODE)),
                        item_check_code = getInt(getColumnIndex(ITEM_CHECK_CODE)) ,
                        item_check_seq = getInt(getColumnIndex(ITEM_CHECK_SEQ)) ,
                        seq =  getInt(getColumnIndex(ITEM_CHECK_SEQ)) ,
                        exec_type = getString(getColumnIndex(EXEC_TYPE)) ,
                        exec_measure = getDouble(getColumnIndex(EXEC_MEASURE)) ,
                        exec_date = getString(getColumnIndex(EXEC_DATE)) ,
                        exec_comment = getString(getColumnIndex(EXEC_COMMENT)) ,
                        exec_material = getInt(getColumnIndex(EXEC_MATERIAL))
                    )
                }
            }
            return null
        }
    }

    private class MD_Product_Serial_Tp_Device_Item_HistToContentValuesMapper : Mapper<MD_Product_Serial_Tp_Device_Item_Hist, ContentValues> {
        override fun map(mdProductSerialTpDeviceItemHist: MD_Product_Serial_Tp_Device_Item_Hist?): ContentValues {
            val contentValues = ContentValues()
            //
            mdProductSerialTpDeviceItemHist?.let {
                with(contentValues){
                    if(mdProductSerialTpDeviceItemHist.customer_code > -1){
                        put(CUSTOMER_CODE,mdProductSerialTpDeviceItemHist.customer_code)
                    }
                    if(mdProductSerialTpDeviceItemHist.product_code > -1){
                        put(PRODUCT_CODE,mdProductSerialTpDeviceItemHist.product_code)
                    }
                    if(mdProductSerialTpDeviceItemHist.serial_code > -1){
                        put(SERIAL_CODE,mdProductSerialTpDeviceItemHist.serial_code)
                    }
                    if(mdProductSerialTpDeviceItemHist.device_tp_code > -1){
                        put(DEVICE_TP_CODE,mdProductSerialTpDeviceItemHist.device_tp_code)
                    }
                    if(mdProductSerialTpDeviceItemHist.item_check_code > -1){
                        put(ITEM_CHECK_CODE,mdProductSerialTpDeviceItemHist.item_check_code)
                    }
                    if(mdProductSerialTpDeviceItemHist.item_check_seq > -1){
                        put(ITEM_CHECK_SEQ,mdProductSerialTpDeviceItemHist.item_check_seq)
                    }
                    if(mdProductSerialTpDeviceItemHist.seq > -1){
                        put(SEQ,mdProductSerialTpDeviceItemHist.seq)
                    }
                    if(mdProductSerialTpDeviceItemHist.exec_type != null){
                        put(EXEC_TYPE,mdProductSerialTpDeviceItemHist.exec_type)
                    }
                    if(mdProductSerialTpDeviceItemHist.exec_measure > -1){
                        put(EXEC_MEASURE,mdProductSerialTpDeviceItemHist.exec_measure)
                    }
                    if(mdProductSerialTpDeviceItemHist.exec_date != null){
                        put(EXEC_DATE,mdProductSerialTpDeviceItemHist.exec_date)
                    }
                    put(EXEC_COMMENT,mdProductSerialTpDeviceItemHist.exec_comment)

                    if(mdProductSerialTpDeviceItemHist.exec_material > -1){
                        put(EXEC_MATERIAL,mdProductSerialTpDeviceItemHist.exec_material)
                    }
                }
            }
            //
            return contentValues
        }
    }
}