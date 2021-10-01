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
import com.namoadigital.prj001.model.GeOsDevice
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class GeOsDeviceDao(
    context: Context,
    mDB_NAME: String,
    mDB_VERSION: Int
) : BaseDao(
    context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI
), DaoWithReturn<GeOsDevice> {
    companion object{
        const val TABLE = "ge_os_device"
        const val CUSTOMER_CODE = "customer_code"
        const val CUSTOM_FORM_TYPE = "custom_form_type"
        const val CUSTOM_FORM_CODE = "custom_form_code"
        const val CUSTOM_FORM_VERSION = "custom_form_version"
        const val CUSTOM_FORM_DATA = "custom_form_data"
        const val PRODUCT_CODE = "product_code"
        const val SERIAL_CODE = "serial_code"
        const val DEVICE_TP_CODE = "device_tp_code"
        const val DEVICE_TP_ID = "device_tp_id"
        const val DEVICE_TP_DESC = "device_tp_desc"
        const val ORDER_SEQ = "order_seq"
        const val TRACKING_NUMBER = "tracking_number"

    }

    private val toGeOsDeviceMapper: Mapper<Cursor, GeOsDevice>
    private val toContentValuesMapper: Mapper<GeOsDevice, ContentValues>

    init {
        this.toGeOsDeviceMapper = CursorToGeOsDeviceMapper()
        this.toContentValuesMapper = GeOsDeviceToContentValuesMapper()
    }

    @Throws(java.lang.Exception::class)
    private fun getWherePkClause(item: GeOsDevice?): StringBuilder{
        item?.let{
            return java.lang.StringBuilder()
                .append("""
                        ${CUSTOMER_CODE} = '${item.customer_code}'  
                        AND ${CUSTOM_FORM_TYPE} = '${item.custom_form_type}'                           
                        AND ${CUSTOM_FORM_CODE} = '${item.custom_form_code}'                           
                        AND ${CUSTOM_FORM_VERSION} = '${item.custom_form_version}'                           
                        AND ${CUSTOM_FORM_DATA} = '${item.custom_form_data}'                           
                        AND ${PRODUCT_CODE} = '${item.product_code}'                           
                        AND ${SERIAL_CODE} = '${item.serial_code}'                           
                        AND ${DEVICE_TP_CODE} = '${item.device_tp_code}'                                             
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }


    override fun addUpdate(item: GeOsDevice?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()

        try {
            daoObjReturn.table = TABLE
            curAction = DaoObjReturn.UPDATE
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(item)
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(item), sbWhere.toString(), null).toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(item))
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
        closeDB()
        //
        return daoObjReturn
    }

    override fun addUpdate(items: MutableList<GeOsDevice>?, status: Boolean): DaoObjReturn {
        return addUpdate(items, status,null)
    }

    fun addUpdate(items: MutableList<GeOsDevice>?, status: Boolean, dbInstance: SQLiteDatabase?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        if (dbInstance == null) {
            openDB()
        } else {
            db = dbInstance
        }

        try {
            daoObjReturn.table = TABLE
            curAction = DaoObjReturn.UPDATE

            //
            if (dbInstance == null) {
                db.beginTransaction()
            }

            if (status) {
                db.delete(TABLE, null, null)
            }

            items?.forEach { item ->
                val sbWhere: StringBuilder = getWherePkClause(item)
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(item), sbWhere.toString(), null).toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(item))
                }
            }
            //
            //Se db não foi passado, finaliza transaction com sucesso
            if (dbInstance == null) {
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

    override fun getByString(sQuery: String?): GeOsDevice? {
        var item: GeOsDevice? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                item = toGeOsDeviceMapper.map(cursor)
            }
            //
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return item
    }

    override fun getByStringHM(sQuery: String?): HMAux? {
        var item: HMAux? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                item = CursorToHMAuxMapper.mapN(cursor)
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return  item
    }

    override fun query(sQuery: String?): MutableList<GeOsDevice> {
        val items = mutableListOf<GeOsDevice>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                val uAux = toGeOsDeviceMapper.map(cursor)
                items.add(uAux)
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return items
    }

    override fun query_HM(sQuery: String?): MutableList<HMAux> {
        val items = mutableListOf<HMAux>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                items.add(CursorToHMAuxMapper.mapN(cursor))
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return items
    }

    class CursorToGeOsDeviceMapper : Mapper<Cursor, GeOsDevice> {
        override fun map(cursor: Cursor?): GeOsDevice? {
            cursor?.let {
                with(cursor){
                    return GeOsDevice(
                        customer_code = getLong(getColumnIndex(CUSTOMER_CODE)),
                        custom_form_type = getInt(getColumnIndex(CUSTOM_FORM_TYPE)),
                        custom_form_code = getInt(getColumnIndex(CUSTOM_FORM_CODE)),
                        custom_form_version = getInt(getColumnIndex(CUSTOM_FORM_VERSION)),
                        custom_form_data = getInt(getColumnIndex(CUSTOM_FORM_DATA)),
                        product_code = getInt(getColumnIndex(PRODUCT_CODE)),
                        serial_code = getInt(getColumnIndex(SERIAL_CODE)),
                        device_tp_code = getInt(getColumnIndex(DEVICE_TP_CODE)),
                        device_tp_id = getString(getColumnIndex(DEVICE_TP_ID)),
                        device_tp_desc = getString(getColumnIndex(DEVICE_TP_DESC)),
                        order_seq = getInt(getColumnIndex(ORDER_SEQ)),
                        tracking_number = getString(getColumnIndex(TRACKING_NUMBER))
                    )
                }
            }
            return null
        }
    }

    class GeOsDeviceToContentValuesMapper : Mapper<GeOsDevice, ContentValues> {
        override fun map(geOs: GeOsDevice?): ContentValues {
            val contentValues = ContentValues()
            geOs?.let {
                with(contentValues){
                    if(it.customer_code > -1){
                        put(CUSTOMER_CODE, it.customer_code)
                    }
                    //
                    if(it.custom_form_type > -1){
                        put(CUSTOM_FORM_TYPE,it.custom_form_type)
                    }
                    //
                    put(CUSTOM_FORM_CODE,it.custom_form_code)
                    //
                    put(CUSTOM_FORM_VERSION,it.custom_form_version)
                    //
                    put(CUSTOM_FORM_DATA,it.custom_form_data)
                    //
                    put(PRODUCT_CODE,it.product_code)
                    //
                    put(SERIAL_CODE, it.serial_code)
                    //
                    put(DEVICE_TP_CODE, it.device_tp_code)
                    //
                    put(DEVICE_TP_ID, it.device_tp_id)
                    //
                    put(DEVICE_TP_DESC, it.device_tp_desc)
                    //
                    put(ORDER_SEQ, it.order_seq)
                    //
                    put(TRACKING_NUMBER, it.tracking_number)
                }
            }
            return contentValues
        }

    }
}