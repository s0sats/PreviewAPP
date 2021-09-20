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
import com.namoadigital.prj001.model.GeOsDeviceMaterial
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class GeOsDeviceMaterialDao(
    context: Context,
    mDB_NAME: String,
    mDB_VERSION: Int
) : BaseDao(
    context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI
), DaoWithReturn<GeOsDeviceMaterial> {

    companion object{
        const val TABLE = "md_device_tp"
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
        const val MATERIAL_CODE = "material_code"
        const val MATERIAL_ID = "material_id"
        const val MATERIAL_DESC = "material_desc"
        const val MATERIAL_QTY = "material_qty"
    }

    private val toGeOsDeviceMaterialMapper: Mapper<Cursor, GeOsDeviceMaterial>
    private val toContentValuesMapper: Mapper<GeOsDeviceMaterial, ContentValues>

    init {
        this.toGeOsDeviceMaterialMapper = CursorToGeOsDeviceMaterialMapper()
        this.toContentValuesMapper = GeOsDeviceMaterialToContentValuesMapper()
    }

    @Throws(java.lang.Exception::class)
    private fun getWherePkClause(item: GeOsDeviceMaterial?): StringBuilder{
        item?.let{
            return java.lang.StringBuilder()
                .append("""
                        ${CUSTOMER_CODE} = '${item.customer_code}'  
                        AND ${CUSTOM_FORM_TYPE} = '${item.custom_form_type}'                           
                        AND ${CUSTOM_FORM_CODE} = '${item.custom_form_code}'                           
                        AND ${CUSTOM_FORM_VERSION} = '${item.custom_form_version}'                           
                        AND ${CUSTOM_FORM_DATA} = '${item.custom_form_data}'                           
                        AND ${PRODUCT_CODE} = '${item.custom_form_data}'                           
                        AND ${SERIAL_CODE} = '${item.custom_form_data}'                           
                        AND ${DEVICE_TP_CODE} = '${item.custom_form_data}'                           
                        AND ${ITEM_CHECK_CODE} = '${item.custom_form_data}'                           
                        AND ${ITEM_CHECK_SEQ} = '${item.custom_form_data}'                           
                        AND ${MATERIAL_CODE} = '${item.custom_form_data}'                           
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }


    override fun addUpdate(item: GeOsDeviceMaterial?): DaoObjReturn {
        return addUpdate(item, null)
    }

    fun addUpdate(item: GeOsDeviceMaterial?, dbInstance: SQLiteDatabase?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        if(dbInstance == null) {
            openDB()
            db.beginTransaction()
        }else{
            this.db = dbInstance
        }

        try {
            daoObjReturn.table = GeOsDeviceMaterialDao.TABLE
            curAction = DaoObjReturn.UPDATE
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(item)
            //Tenta update e armazena retorno
            addUpdateRet = db.update(GeOsDeviceMaterialDao.TABLE, toContentValuesMapper.map(item), sbWhere.toString(), null).toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(GeOsDeviceMaterialDao.TABLE, null, toContentValuesMapper.map(item))
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
        if(dbInstance == null) {
            closeDB()
        }
        //
        return daoObjReturn
    }

    fun addUpdate(items: MutableList<GeOsDeviceMaterial>?, status: Boolean, dbInstance: SQLiteDatabase?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()

        try {
            daoObjReturn.table = MdItemCheckDao.TABLE
            curAction = DaoObjReturn.UPDATE

            //Se db não foi passado, inicializa transaction
            if (dbInstance == null) {
                db.beginTransaction()
            }

            if (status) {
                db.delete(MdItemCheckDao.TABLE, null, null)
            }

            items?.forEach { item ->
                val sbWhere: StringBuilder = getWherePkClause(item)
                //Tenta update e armazena retorno
                addUpdateRet = db.update(MdItemCheckDao.TABLE, toContentValuesMapper.map(item), sbWhere.toString(), null).toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(MdItemCheckDao.TABLE, null, toContentValuesMapper.map(item))
                }
            }
            //
            db.setTransactionSuccessful()

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
            db.endTransaction()
            daoObjReturn.action = curAction
            daoObjReturn.actionReturn = addUpdateRet
        }

        closeDB()
        //
        return daoObjReturn

    }

    override fun addUpdate(items: MutableList<GeOsDeviceMaterial>?, status: Boolean): DaoObjReturn {
        return addUpdate(items, status, null)
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

    override fun getByString(sQuery: String?): GeOsDeviceMaterial? {
        var item: GeOsDeviceMaterial? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                item = toGeOsDeviceMaterialMapper.map(cursor)
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

    override fun query(sQuery: String?): MutableList<GeOsDeviceMaterial> {
        val items = mutableListOf<GeOsDeviceMaterial>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                val uAux = toGeOsDeviceMaterialMapper.map(cursor)
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

    class CursorToGeOsDeviceMaterialMapper : Mapper<Cursor, GeOsDeviceMaterial> {
        override fun map(cursor: Cursor?): GeOsDeviceMaterial? {
            cursor?.let {
                with(cursor){
                    return GeOsDeviceMaterial(
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
                        material_code = getInt(getColumnIndex(MATERIAL_CODE)),
                        material_id = getString(getColumnIndex(MATERIAL_ID)),
                        material_desc = getString(getColumnIndex(MATERIAL_DESC)),
                        material_qty = getFloat(getColumnIndex(MATERIAL_QTY))
                    )
                }
            }
            return null
        }
    }

    class GeOsDeviceMaterialToContentValuesMapper : Mapper<GeOsDeviceMaterial, ContentValues> {
        override fun map(geOsDeviceMaterial: GeOsDeviceMaterial?): ContentValues {
            val contentValues = ContentValues()
            geOsDeviceMaterial?.let {
                with(contentValues){
                    if(it.customer_code > -1){
                        put(CUSTOMER_CODE, it.customer_code)
                    }
                    //
                    if(it.custom_form_type > -1){
                        put(CUSTOM_FORM_TYPE,it.custom_form_type)
                    }
                    //
                    put(CUSTOM_FORM_CODE,it.custom_form_data)
                    put(CUSTOM_FORM_VERSION, it.custom_form_version)
                    put(CUSTOM_FORM_DATA, it.custom_form_data)
                    put(PRODUCT_CODE, it.product_code)
                    put(SERIAL_CODE, it.serial_code)
                    put(DEVICE_TP_CODE, it.device_tp_code)
                    put(ITEM_CHECK_CODE, it.item_check_code)
                    put(ITEM_CHECK_SEQ, it.item_check_seq)
                    put(MATERIAL_CODE, it.material_code)
                    put(MATERIAL_ID, it.material_id)
                    put(MATERIAL_DESC, it.material_desc)
                    put(MATERIAL_QTY, it.material_qty)
                    //
                }
            }
            return contentValues
        }

    }
}