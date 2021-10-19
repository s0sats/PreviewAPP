package com.namoadigital.prj001.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import androidx.core.database.getStringOrNull
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.database.CursorToHMAuxMapper
import com.namoadigital.prj001.database.Mapper
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.GeOsDeviceItemHist
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class GeOsDeviceItemHistDao(
    context: Context,
    mDB_NAME: String,
    mDB_VERSION: Int
) : BaseDao(
    context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI
), DaoWithReturn<GeOsDeviceItemHist> {

    companion object {
        const val TABLE = "ge_os_device_item_hist"
        const val CUSTOMER_CODE = "customer_code"
        const val CUSTOM_FORM_TYPE = "custom_form_type"
        const val CUSTOM_FORM_CODE = "custom_form_code"
        const val CUSTOM_FORM_VERSION = "custom_form_version"
        const val CUSTOM_FORM_DATA = "custom_form_data"
        const val PRODUCT_CODE = "product_code"
        const val SERIAL_CODE = "serial_code"
        const val DEVICE_TP_CODE = "device_tp_code"
        const val ITEM_CHECK_CODE = "item_check_code"
        const val ITEM_CHECK_SEQ = "item_check_seq"
        const val SEQ = "seq"
        const val EXEC_TYPE = "exec_type"
        const val EXEC_VALUE = "exec_value"
        const val EXEC_DATE = "exec_date"
        const val EXEC_COMMENT = "exec_comment"
        const val EXEC_MATERIAL = "exec_material"

    }

    private val toGeOsDeviceItemHistMapper: Mapper<Cursor, GeOsDeviceItemHist>
    private val toContentValuesMapper: Mapper<GeOsDeviceItemHist, ContentValues>

    init {
        this.toGeOsDeviceItemHistMapper = CursorToGeOsDeviceItemHistMapper()
        this.toContentValuesMapper = GeOsDeviceItemHistToContentValuesMapper()
    }

    @Throws(java.lang.Exception::class)
    private fun getWherePkClause(item: GeOsDeviceItemHist?): StringBuilder {
        item?.let {
            return java.lang.StringBuilder()
                .append(
                    """
                        $CUSTOMER_CODE = '${item.customer_code}'  
                        AND $CUSTOM_FORM_TYPE = '${item.custom_form_type}'                           
                        AND $CUSTOM_FORM_CODE = '${item.custom_form_code}'                           
                        AND $CUSTOM_FORM_VERSION = '${item.custom_form_version}'                           
                        AND $CUSTOM_FORM_DATA = '${item.custom_form_data}'                           
                        AND $PRODUCT_CODE = '${item.product_code}'                           
                        AND $SERIAL_CODE = '${item.serial_code}'                           
                        AND $DEVICE_TP_CODE = '${item.device_tp_code}'                           
                        AND $ITEM_CHECK_CODE = '${item.item_check_code}'                           
                        AND $ITEM_CHECK_SEQ = '${item.item_check_seq}'                         
                        AND $SEQ = '${item.seq}'                         
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    override fun addUpdate(item: GeOsDeviceItemHist?): DaoObjReturn {
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

    override fun addUpdate(items: MutableList<GeOsDeviceItemHist>?, status: Boolean): DaoObjReturn {
        return addUpdate(items, status, null)
    }

    fun addUpdate(items: MutableList<GeOsDeviceItemHist>?, status: Boolean, dbInstance: SQLiteDatabase?): DaoObjReturn {
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

    override fun getByString(sQuery: String?): GeOsDeviceItemHist? {
        var item: GeOsDeviceItemHist? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                item = toGeOsDeviceItemHistMapper.map(cursor)
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

    override fun query(sQuery: String?): MutableList<GeOsDeviceItemHist> {
        val items = mutableListOf<GeOsDeviceItemHist>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                val uAux = toGeOsDeviceItemHistMapper.map(cursor)
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

    class CursorToGeOsDeviceItemHistMapper : Mapper<Cursor, GeOsDeviceItemHist> {
        override fun map(cursor: Cursor?): GeOsDeviceItemHist? {
            cursor?.let {
                with(cursor) {
                    return GeOsDeviceItemHist(
                        customer_code = getLong(getColumnIndex(CUSTOMER_CODE)),
                        custom_form_type = getInt(getColumnIndex(CUSTOM_FORM_TYPE)),
                        custom_form_code = getInt(getColumnIndex(CUSTOM_FORM_CODE)),
                        custom_form_version = getInt(getColumnIndex(CUSTOM_FORM_VERSION)),
                        custom_form_data = getInt(getColumnIndex(CUSTOM_FORM_DATA)),
                        product_code = getInt(getColumnIndex(PRODUCT_CODE)),
                        serial_code = getInt(getColumnIndex(SERIAL_CODE)),
                        device_tp_code = getInt(getColumnIndex(DEVICE_TP_CODE)),
                        item_check_code = getInt(getColumnIndex(ITEM_CHECK_CODE)),
                        item_check_seq = getInt(getColumnIndex(ITEM_CHECK_SEQ)),
                        seq = getInt(getColumnIndex(SEQ)),
                        exec_type = getString(getColumnIndex(EXEC_TYPE)),
                        exec_value = getFloat(getColumnIndex(EXEC_VALUE)),
                        exec_date = getString(getColumnIndex(EXEC_DATE)),
                        exec_comment = getStringOrNull(getColumnIndex(EXEC_COMMENT)),
                        exec_material = getInt(getColumnIndex(EXEC_MATERIAL)),
                    )
                }
            }
            return null
        }
    }

    class GeOsDeviceItemHistToContentValuesMapper : Mapper<GeOsDeviceItemHist, ContentValues> {
        override fun map(item: GeOsDeviceItemHist?): ContentValues {
            val contentValues = ContentValues()
            item?.let {
                with(contentValues) {
                    if (it.customer_code > -1) {
                        put(CUSTOMER_CODE, it.customer_code)
                    }
                    //
                    if (it.custom_form_type > -1) {
                        put(CUSTOM_FORM_TYPE, it.custom_form_type)
                    }
                    //
                    put(CUSTOM_FORM_CODE, it.custom_form_code)
                    put(CUSTOM_FORM_VERSION, it.custom_form_version)
                    put(CUSTOM_FORM_DATA, it.custom_form_data)
                    put(PRODUCT_CODE, it.product_code)
                    put(SERIAL_CODE, it.serial_code)
                    put(DEVICE_TP_CODE, it.device_tp_code)
                    put(ITEM_CHECK_CODE, it.item_check_code)
                    put(ITEM_CHECK_SEQ, it.item_check_seq)
                    put(SEQ, it.seq)
                    put(EXEC_TYPE, it.exec_type)
                    put(EXEC_VALUE, it.exec_value)
                    put(EXEC_DATE, it.exec_date)
                    put(EXEC_COMMENT, it.exec_comment)
                    put(EXEC_MATERIAL, it.exec_material)
                    //
                }
            }
            return contentValues
        }

    }
}