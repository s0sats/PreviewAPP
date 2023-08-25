package com.namoadigital.prj001.dao

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.util.Log
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.database.CursorToHMAuxMapper
import com.namoadigital.prj001.database.Mapper
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.HistoricMaterialInputs
import com.namoadigital.prj001.model.MdProductSerialTpDeviceItemHistMat
import com.namoadigital.prj001.sql.MdProductSerialTpDeviceItemHistMat_Sql_GetInputs
import com.namoadigital.prj001.sql.MdProductSerialTpDeviceItemHistMat_Sql_GetInputs.Companion.mappingToHistoricMaterialInputs
import com.namoadigital.prj001.ui.base.NamoaFactory
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class MdProductSerialTpDeviceItemHistMatDao(
    context: Context,
    mDB_NAME: String = ToolBox_Con.customDBPath(
        ToolBox_Con.getPreference_Customer_Code(
            context
        )
    ),
    mDB_VERSION: Int = Constant.DB_VERSION_CUSTOM
) : BaseDao(
    context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI
), DaoWithReturn<MdProductSerialTpDeviceItemHistMat>,
    DaoWithReturnSharedDbInstance<MdProductSerialTpDeviceItemHistMat> {


    private val toMdProductSerialTpDeviceItemHistMatMapper: Mapper<Cursor, MdProductSerialTpDeviceItemHistMat>
    private val toContentValuesMapper: Mapper<MdProductSerialTpDeviceItemHistMat, ContentValues>


    init {
        toMdProductSerialTpDeviceItemHistMatMapper =
            CursorToMdProductSerialTpDeviceItemHistMatMapper()
        toContentValuesMapper = MdProductSerialTpDeviceItemHistMatContentValuesMapper()
    }


    override fun remove(
        item: MdProductSerialTpDeviceItemHistMat?,
        dbInstance: SQLiteDatabase?
    ): DaoObjReturn {
        return DaoObjReturn()
    }


    override fun addUpdate(
        item: MdProductSerialTpDeviceItemHistMat?,
        dbInstance: SQLiteDatabase?
    ): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet = 0L
        var cursorAction = DaoObjReturn.INSERT_OR_UPDATE
        val notIsSharedDB = dbInstance == null
        if (notIsSharedDB) {
            openDB()
        } else {
            db = dbInstance
        }

        try {
            daoObjReturn.table = TABLE
            cursorAction = DaoObjReturn.UPDATE
            val sbWhere = getWherePkClause(item)
            addUpdateRet =
                db.update(TABLE, toContentValuesMapper.map(item), sbWhere.toString(), null)
                    .toLong()
            if (addUpdateRet == 0L) {
                cursorAction = DaoObjReturn.INSERT
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(item))
            }
        } catch (e: SQLiteException) {
            daoObjReturn = ToolBox_Con.getSQLiteErrorCodeDescription(e.message)
            ToolBox_Inf.registerException(
                javaClass.name,
                SQLiteException(
                    """
                    ${e.message}
                    ${daoObjReturn.errorMsg}    
                    """.trimIndent()
                )
            )
        } catch (e: Exception) {
            daoObjReturn.setError(true)
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
            daoObjReturn.apply {
                action = cursorAction
                actionReturn = addUpdateRet
            }
            if (notIsSharedDB) closeDB()
        }

        return daoObjReturn
    }

    override fun addUpdate(model: MdProductSerialTpDeviceItemHistMat?): DaoObjReturn {
        return addUpdate(model, null)
    }

    override fun addUpdate(
        items: MutableList<MdProductSerialTpDeviceItemHistMat>?,
        status: Boolean,
        dbInstance: SQLiteDatabase?
    ): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet = 0L
        var cursorAction = DaoObjReturn.INSERT_OR_UPDATE
        val dbNotInitialized = dbInstance == null
        if (dbNotInitialized) {
            openDB()
        } else {
            db = dbInstance
        }

        try {

            daoObjReturn.table = TABLE
            cursorAction = DaoObjReturn.UPDATE

            if (dbInstance == null) db.beginTransaction()

            if (status) db.delete(TABLE, null, null)

            items?.forEach { model ->
                val sbWhere = getWherePkClause(model)

                addUpdateRet =
                    db.update(TABLE, toContentValuesMapper.map(model), sbWhere.toString(), null)
                        .toLong()
                if (addUpdateRet == 0L) {
                    cursorAction = DaoObjReturn.INSERT
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(model))
                }
            }

            if (dbNotInitialized) db.setTransactionSuccessful()
        } catch (e: SQLiteException) {
            daoObjReturn = ToolBox_Con.getSQLiteErrorCodeDescription(e.message)
            ToolBox_Inf.registerException(
                javaClass.name,
                SQLiteException(
                    """
                    ${e.message}
                    ${daoObjReturn.errorMsg}    
                    """.trimIndent()
                )
            )
        } catch (e: Exception) {
            daoObjReturn.setError(true)
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
            if (dbNotInitialized) db.endTransaction()
            daoObjReturn.apply {
                action = cursorAction
                actionReturn = addUpdateRet
            }
            if (dbNotInitialized) closeDB()
        }

        return daoObjReturn
    }

    override fun addUpdate(
        items: MutableList<MdProductSerialTpDeviceItemHistMat>?,
        status: Boolean
    ): DaoObjReturn {
        return addUpdate(items, status, null)
    }

    @Throws(Exception::class)
    private fun getWherePkClause(model: MdProductSerialTpDeviceItemHistMat?): StringBuilder {
        model?.let {
            return StringBuilder()
                .append(
                    """
                        $CUSTOMER_CODE = '${it.customer_code}'  
                        AND $PRODUCT_CODE = '${it.product_code}'
                        AND $SERIAL_CODE = '${it.serial_code}'
                        AND $ITEM_CHECK_SEQ = '${it.item_check_seq}'
                        AND $SEQ = '${it.seq}'
                        AND $MATERIAL_CODE = '${it.material_code}'
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }


    private fun log(message: String) = Log.e("ItemHistMatDao", message)

    override fun addUpdate(sQuery: String?) {
        log("159 -> ADD UPDATE $sQuery")
        execSQL { db.execSQL(sQuery) }
    }

    override fun remove(sQuery: String?) {
        log("164 -> REMOVE $sQuery")
        execSQL { db.execSQL(sQuery) }
    }

    override fun getByString(sQuery: String?) = getByString(sQuery, null)

    fun getByString(
        sQuery: String?,
        dbInstance: SQLiteDatabase? = null
    ): MdProductSerialTpDeviceItemHistMat? {
        log("174 -> GET BY STRING $sQuery")
        val dbNotInitialized = dbInstance == null
        if (dbNotInitialized) {
            openDB()
        } else {
            this.db = dbInstance
        }

        lateinit var cursor: Cursor
        var mdProductSerialTpDeviceItemHistMat: MdProductSerialTpDeviceItemHistMat? = null

        try {
            cursor = db.rawQuery(sQuery, null)
            if (cursor.moveToNext()) {
                mdProductSerialTpDeviceItemHistMat =
                    toMdProductSerialTpDeviceItemHistMatMapper.map(cursor)
            }
        } catch (e: Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
            cursor.close()
        }

        if (dbNotInitialized) {
            closeDB()
        }

        return mdProductSerialTpDeviceItemHistMat
    }


    override fun getByStringHM(sQuery: String?): HMAux? {
        var hmAux: HMAux? = null
        getCursor(sQuery) {
            hmAux = CursorToHMAuxMapper.mapN(it)
        }
        return hmAux
    }

    override fun query(sQuery: String?): MutableList<MdProductSerialTpDeviceItemHistMat> {
        val mdProductSerialTpDeviceItemHistMat = mutableListOf<MdProductSerialTpDeviceItemHistMat>()
        getCursor(sQuery) {
            toMdProductSerialTpDeviceItemHistMatMapper.map(it).let { mapper ->
                mdProductSerialTpDeviceItemHistMat.add(mapper)
            }
        }

        return mdProductSerialTpDeviceItemHistMat
    }

    override fun query_HM(sQuery: String?): MutableList<HMAux> {
        val tpDeviceItemHistMat = mutableListOf<HMAux>()

        getCursor(sQuery) {
            tpDeviceItemHistMat.add(CursorToHMAuxMapper.mapN(it))
        }

        return tpDeviceItemHistMat
    }


    private fun execSQL(block: () -> Unit) {
        openDB()
        try {
            block()
        } catch (e: Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
            closeDB()
        }
    }

    private fun getCursor(query: String?, block: (Cursor) -> Unit) {

        openDB()
        try {
            val cursor = db.rawQuery(query, null)
            while (cursor.moveToNext()) {
                block(cursor)
            }
            cursor.close()
        } catch (e: Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
            closeDB()
        }

    }


    @SuppressLint("Range")
    class CursorToMdProductSerialTpDeviceItemHistMatMapper :
        Mapper<Cursor, MdProductSerialTpDeviceItemHistMat> {
        override fun map(cursor: Cursor): MdProductSerialTpDeviceItemHistMat {
            with(cursor) {
                return MdProductSerialTpDeviceItemHistMat(
                    customer_code = getLong(getColumnIndex(CUSTOMER_CODE)),
                    product_code = getLong(getColumnIndex(PRODUCT_CODE)),
                    serial_code = getLong(getColumnIndex(SERIAL_CODE)),
                    device_tp_code = getInt(getColumnIndex(DEVICE_TP_CODE)),
                    item_check_seq = getInt(getColumnIndex(ITEM_CHECK_SEQ)),
                    item_check_code = getInt(getColumnIndex(ITEM_CHECK_CODE)),
                    seq = getInt(getColumnIndex(SEQ)),
                    material_code = getInt(getColumnIndex(MATERIAL_CODE)),
                    un = getString(getColumnIndex(UN)),
                    qty = getDouble(getColumnIndex(QTY)),
                    qty_planned = getDouble(getColumnIndex(QTY_PLANNED)),
                    material_action = getInt(getColumnIndex(MATERIAL_ACTION)),
                )
            }
        }
    }


    class MdProductSerialTpDeviceItemHistMatContentValuesMapper :
        Mapper<MdProductSerialTpDeviceItemHistMat, ContentValues> {
        override fun map(mapper: MdProductSerialTpDeviceItemHistMat): ContentValues {
            ContentValues().apply {
                if (mapper.customer_code > -1L) put(CUSTOMER_CODE, mapper.customer_code)
                if (mapper.product_code > -1L) put(PRODUCT_CODE, mapper.product_code)
                if (mapper.serial_code > -1L) put(SERIAL_CODE, mapper.serial_code)
                if (mapper.device_tp_code > -1) put(DEVICE_TP_CODE, mapper.device_tp_code)
                if (mapper.item_check_seq > -1) put(ITEM_CHECK_SEQ, mapper.item_check_seq)
                if (mapper.item_check_code > -1) put(ITEM_CHECK_CODE, mapper.item_check_code)
                if (mapper.seq > -1) put(SEQ, mapper.seq)
                if (mapper.material_code > -1) put(MATERIAL_CODE, mapper.material_code)
                put(UN, mapper.un)
                if (mapper.qty > -1) put(QTY, mapper.qty)
                if (mapper.qty_planned > -1) put(QTY_PLANNED, mapper.qty_planned)
                if (mapper.material_action > -1) put(MATERIAL_ACTION, mapper.material_action)

                return this
            }
        }
    }


    companion object {
        const val TABLE = "md_product_serial_tp_device_item_hist_mat"
        const val CUSTOMER_CODE = "customer_code"
        const val PRODUCT_CODE = "product_code"
        const val SERIAL_CODE = "serial_code"
        const val DEVICE_TP_CODE = "device_tp_code"
        const val ITEM_CHECK_SEQ = "item_check_seq"
        const val ITEM_CHECK_CODE = "item_check_code"
        const val SEQ = "seq"
        const val MATERIAL_CODE = "material_code"
        const val UN = "un"
        const val QTY = "qty"
        const val QTY_PLANNED = "qty_planned"
        const val MATERIAL_ACTION = "material_action"
    }

    class DatabaseFactory(
        val context: Context
    ) : NamoaFactory<MdProductSerialTpDeviceItemHistMatDao>() {
        override fun build(): MdProductSerialTpDeviceItemHistMatDao {
            return MdProductSerialTpDeviceItemHistMatDao(context)
        }
    }

//-------------------------------------------------\\

    fun getInputs(
        customerCode: Int,
        serialCode: Int,
        producCode: Int,
        deviceTpCode: Int,
        itemCheckSeq: Int,
        itemCheckCode: Int,
        seq: Int,
    ): List<HistoricMaterialInputs> {
        return query_HM(
            MdProductSerialTpDeviceItemHistMat_Sql_GetInputs(
                customerCode,
                serialCode,
                producCode,
                deviceTpCode,
                itemCheckSeq,
                itemCheckCode,
                seq
            ).toSqlQuery()
        ).mappingToHistoricMaterialInputs()
    }
}