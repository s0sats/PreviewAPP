package com.namoadigital.prj001.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import androidx.core.database.getFloatOrNull
import androidx.core.database.getStringOrNull
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.database.CursorToHMAuxMapper
import com.namoadigital.prj001.database.Mapper
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.GeOsDeviceItem
import com.namoadigital.prj001.model.GeOsDeviceMaterial
import com.namoadigital.prj001.sql.GeOsDeviceMaterialSql_002
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class GeOsDeviceItemDao(
    context: Context,
    mDB_NAME: String,
    mDB_VERSION: Int
) : BaseDao(
    context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI
), DaoWithReturn<GeOsDeviceItem> {
    companion object {
        const val TABLE = "ge_os_device_item"
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
        const val ITEM_CHECK_ID = "item_check_id"
        const val ITEM_CHECK_DESC = "item_check_desc"
        const val APPLY_MATERIAL = "apply_material"
        const val VERIFICATION_INSTRUCTION = "verification_instruction"
        const val REQUIRE_JUSTIFY_PROBLEM = "require_justify_problem"
        const val CRITICAL_ITEM = "critical_item"
        const val ORDER_SEQ = "order_seq"
        const val STRUCTURE = "structure"
        const val MANUAL_DESC = "manual_desc"
        const val NEXT_CYCLE_MEASURE = "next_cycle_measure"
        const val NEXT_CYCLE_MEASURE_DATE = "next_cycle_measure_date"
        const val NEXT_CYCLE_LIMIT_DATE = "next_cycle_limit_date"
        const val VALUE_SUFIX = "value_sufix"
        const val ITEM_CHECK_STATUS = "item_check_status"
        const val TARGET_DATE = "target_date"
        const val EXEC_TYPE = "exec_type"
        const val EXEC_DATE = "exec_date"
        const val EXEC_COMMENT = "exec_comment"
        const val EXEC_PHOTO1 = "exec_photo1"
        const val EXEC_PHOTO2 = "exec_photo2"
        const val EXEC_PHOTO3 = "exec_photo3"
        const val EXEC_PHOTO4 = "exec_photo4"
        const val STATUS_ANSWER = "status_answer"
    }

    private val toGeOsDeviceItemMapper: Mapper<Cursor, GeOsDeviceItem>
    private val toContentValuesMapper: Mapper<GeOsDeviceItem, ContentValues>

    init {
        this.toGeOsDeviceItemMapper = CursorToGeOsDeviceItemMapper()
        this.toContentValuesMapper = GeOsDeviceItemToContentValuesMapper()
    }

    @Throws(java.lang.Exception::class)
    private fun getWherePkClause(item: GeOsDeviceItem?): StringBuilder {
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
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    override fun addUpdate(item: GeOsDeviceItem?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()
        //
        try {
            daoObjReturn.table = TABLE
            curAction = DaoObjReturn.UPDATE
            //
            db.beginTransaction()
            //
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(item)
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(item), sbWhere.toString(), null).toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(item))
            }
            //
            item?.materialList.let {
                daoObjReturn = tryAddUpdateMaterials(item!!,it?: mutableListOf<GeOsDeviceMaterial>(), db)
                //Se erro durante insert, dispara exception abortando o processamento.
                if (daoObjReturn.hasError()) {
                    throw java.lang.Exception(daoObjReturn.rawMessage)
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
            db.endTransaction()
            daoObjReturn.action = curAction
            daoObjReturn.actionReturn = addUpdateRet
        }
        //
        closeDB()
        //
        return daoObjReturn
    }


    override fun addUpdate(items: MutableList<GeOsDeviceItem>?, status: Boolean): DaoObjReturn {
        return addUpdate(items, status,null)
    }

    fun addUpdate(items: MutableList<GeOsDeviceItem>?, status: Boolean, dbInstance: SQLiteDatabase?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        if (dbInstance == null) {
            openDB()
        } else {
            db = dbInstance
        }
        //
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
                daoObjReturn = tryAddUpdateMaterials(item!!, item.materialList, db)
                //Se erro durante insert, dispara exception abortando o processamento.
                if (daoObjReturn.hasError()) {
                    throw java.lang.Exception(daoObjReturn.rawMessage)
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
        //
        if (dbInstance == null) {
            closeDB()
        }
        //
        return daoObjReturn
    }
    //
    private fun tryAddUpdateMaterials(
        geOsDeviceItem: GeOsDeviceItem,
        materials: MutableList<GeOsDeviceMaterial>,
        db: SQLiteDatabase
    ): DaoObjReturn{
        val geOsDeviceMaterialDao = geOsDeviceMaterialDao()
        //Tenta remover a lista atual
        val daoObjReturn = geOsDeviceMaterialDao.removeAllForGeOsDeviceItem(
            getWherePkClause(geOsDeviceItem).toString(),
            db
        )
        //Se erro, reporta
        if(daoObjReturn.hasError()){
            return daoObjReturn
        }
        //Senão tem erro, atualiza.
        return geOsDeviceMaterialDao.addUpdate(materials, false, db)
    }


    private fun geOsDeviceMaterialDao(): GeOsDeviceMaterialDao {
        return GeOsDeviceMaterialDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        )
    }
    //
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

    override fun getByString(sQuery: String?): GeOsDeviceItem? {
        var item: GeOsDeviceItem? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                item = toGeOsDeviceItemMapper.map(cursor)
                item?.apply {
                        getMaterialList(this)
                }
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

    private fun getMaterialList(geOsDeviceItem: GeOsDeviceItem) {
        val geOsDeviceMaterialDao = geOsDeviceMaterialDao()
        geOsDeviceItem.materialList.clear()
        //
        geOsDeviceItem
            .materialList
            .addAll(geOsDeviceMaterialDao.query(
                    GeOsDeviceMaterialSql_002(
                        geOsDeviceItem.customer_code,
                        geOsDeviceItem.custom_form_type,
                        geOsDeviceItem.custom_form_code,
                        geOsDeviceItem.custom_form_version,
                        geOsDeviceItem.custom_form_data,
                        geOsDeviceItem.product_code,
                        geOsDeviceItem.serial_code,
                        geOsDeviceItem.device_tp_code,
                        geOsDeviceItem.item_check_code,
                        geOsDeviceItem.item_check_seq
                    ).toSqlQuery()
                )
        )
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

    override fun query(sQuery: String?): MutableList<GeOsDeviceItem> {
        val items = mutableListOf<GeOsDeviceItem>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                val uAux = toGeOsDeviceItemMapper.map(cursor)
                uAux?.apply {
                    getMaterialList(this)
                }
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

    /**
     * Fun que remove o item e os materiais ela vinculados.
     */
    fun removeFull(geOsDeviceItem: GeOsDeviceItem) : DaoObjReturn{
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        val curAction = DaoObjReturn.DELETE
        daoObjReturn.table = TABLE
        //
        val wherePkClause = getWherePkClause(geOsDeviceItem).toString()

        openDB()
        try {
            db.beginTransaction()
            //
            addUpdateRet += db.delete(TABLE,wherePkClause,null)
            addUpdateRet += db.delete(GeOsDeviceMaterialDao.TABLE,wherePkClause,null)
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
            db.endTransaction()
            daoObjReturn.action = curAction
            daoObjReturn.actionReturn = addUpdateRet
        }
        //
        closeDB()
        return daoObjReturn

    }

    class CursorToGeOsDeviceItemMapper : Mapper<Cursor, GeOsDeviceItem> {
        override fun map(cursor: Cursor?): GeOsDeviceItem? {
            cursor?.let {
                with(cursor) {
                    return GeOsDeviceItem(
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
                        item_check_id = getString(getColumnIndex(ITEM_CHECK_ID)),
                        item_check_desc = getString(getColumnIndex(ITEM_CHECK_DESC)),
                        apply_material = getString(getColumnIndex(APPLY_MATERIAL)),
                        verification_instruction = getStringOrNull(getColumnIndex(VERIFICATION_INSTRUCTION)),
                        require_justify_problem = getInt(getColumnIndex(REQUIRE_JUSTIFY_PROBLEM)),
                        critical_item = getInt(getColumnIndex(CRITICAL_ITEM)),
                        order_seq = getInt(getColumnIndex(ORDER_SEQ)),
                        structure = getInt(getColumnIndex(STRUCTURE)),
                        manual_desc = getStringOrNull(getColumnIndex(MANUAL_DESC)),
                        next_cycle_measure = getFloatOrNull(getColumnIndex(NEXT_CYCLE_MEASURE)),
                        next_cycle_measure_date = getStringOrNull(getColumnIndex(NEXT_CYCLE_MEASURE_DATE)),
                        next_cycle_limit_date = getStringOrNull(getColumnIndex(NEXT_CYCLE_LIMIT_DATE)),
                        value_sufix = getStringOrNull(getColumnIndex(VALUE_SUFIX)),
                        item_check_status = getString(getColumnIndex(ITEM_CHECK_STATUS)),
                        target_date = getStringOrNull(getColumnIndex(TARGET_DATE)),
                        exec_type = getStringOrNull(getColumnIndex(EXEC_TYPE)),
                        exec_date = getStringOrNull(getColumnIndex(EXEC_DATE)),
                        exec_comment = getStringOrNull(getColumnIndex(EXEC_COMMENT)),
                        exec_photo1 = getStringOrNull(getColumnIndex(EXEC_PHOTO1)),
                        exec_photo2 = getStringOrNull(getColumnIndex(EXEC_PHOTO2)),
                        exec_photo3 = getStringOrNull(getColumnIndex(EXEC_PHOTO3)),
                        exec_photo4 = getStringOrNull(getColumnIndex(EXEC_PHOTO4)),
                        status_answer = getStringOrNull(getColumnIndex(STATUS_ANSWER))
                    )
                }
            }
            return null
        }
    }

    //
    class GeOsDeviceItemToContentValuesMapper : Mapper<GeOsDeviceItem, ContentValues> {
        override fun map(geOs: GeOsDeviceItem?): ContentValues {
            val contentValues = ContentValues()
            geOs?.let {
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
                    //
                    put(CUSTOM_FORM_VERSION, it.custom_form_version)
                    //
                    put(CUSTOM_FORM_DATA, it.custom_form_data)
                    //
                    put(PRODUCT_CODE, it.product_code)
                    //
                    put(SERIAL_CODE, it.serial_code)
                    //
                    put(DEVICE_TP_CODE, it.device_tp_code)
                    //
                    put(ITEM_CHECK_CODE, it.item_check_code)
                    //
                    put(ITEM_CHECK_SEQ, it.item_check_seq)
                    //
                    put(ITEM_CHECK_ID, it.item_check_id)
                    //
                    put(ITEM_CHECK_DESC, it.item_check_desc)
                    //
                    put(APPLY_MATERIAL, it.apply_material)
                    //
                    put(VERIFICATION_INSTRUCTION, it.verification_instruction)
                    //
                    put(REQUIRE_JUSTIFY_PROBLEM, it.require_justify_problem)
                    //
                    put(CRITICAL_ITEM, it.critical_item)
                    //
                    put(ORDER_SEQ, it.order_seq)
                    //
                    put(STRUCTURE, it.structure)
                    //
                    put(MANUAL_DESC, it.manual_desc)
                    //
                    put(NEXT_CYCLE_MEASURE, it.next_cycle_measure)
                    //
                    put(NEXT_CYCLE_MEASURE_DATE, it.next_cycle_measure_date)
                    //
                    put(NEXT_CYCLE_LIMIT_DATE, it.next_cycle_limit_date)
                    //
                    put(VALUE_SUFIX, it.value_sufix)
                    //
                    put(ITEM_CHECK_STATUS, it.item_check_status)
                    //
                    put(TARGET_DATE, it.target_date)
                    //
                    put(EXEC_TYPE, it.exec_type)
                    //
                    put(EXEC_DATE, it.exec_date)
                    //
                    put(EXEC_COMMENT, it.exec_comment)
                    //
                    put(EXEC_PHOTO1, it.exec_photo1)
                    //
                    put(EXEC_PHOTO2, it.exec_photo2)
                    //
                    put(EXEC_PHOTO3, it.exec_photo3)
                    //
                    put(EXEC_PHOTO4, it.exec_photo4)
                    //
                    put(STATUS_ANSWER, it.status_answer)
                }
            }
            return contentValues
        }
    }
}