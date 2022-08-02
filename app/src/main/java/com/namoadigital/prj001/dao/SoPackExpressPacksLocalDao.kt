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
import com.namoadigital.prj001.model.SoPackExpressPacksLocal
import com.namoadigital.prj001.model.SoPackExpressServicesLocal
import com.namoadigital.prj001.sql.SoPackExpressServicesLocalSql002
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class SoPackExpressPacksLocalDao(
    val context: Context,
    val mDB_NAME: String,
    val mDB_VERSION: Int
) : BaseDao(
    context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI
), DaoWithReturn<SoPackExpressPacksLocal> {

    companion object {
        const val TABLE = "so_pack_express_packs_local"
        const val CUSTOMER_CODE = "customer_code"
        const val SITE_CODE = "site_code"
        const val OPERATION_CODE = "operation_code"
        const val PRODUCT_CODE = "product_code"
        const val EXPRESS_CODE = "express_code"
        const val EXPRESS_TMP = "express_tmp"
        const val PACK_CODE = "pack_code"
        const val PACK_SEQ = "pack_seq"
        const val PACK_SERVICE_DESC = "pack_service_desc"
        const val PACK_SERVICE_DESC_FULL = "pack_service_desc_full"
        const val PRICE_LIST_CODE = "price_list_code"
        const val QTY = "qty"
        const val TYPE_PS = "type_ps"
        const val COMMENTS = "comments"

    }

    private val toSoPackExpressPacksLocalMapper: Mapper<Cursor, SoPackExpressPacksLocal>
    private val toContentValuesMapper: Mapper<SoPackExpressPacksLocal, ContentValues>

    init {
        toSoPackExpressPacksLocalMapper = CursorToSoPackExpressPacksLocalMapper()
        toContentValuesMapper = SoPackExpressPacksLocalToContentValuesMapper()
    }


    override fun addUpdate(item: SoPackExpressPacksLocal?): DaoObjReturn {
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
            addUpdateRet = db.update(
                TABLE,
                toContentValuesMapper.map(item),
                sbWhere.toString(),
                null
            ).toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(
                    TABLE,
                    null,
                    toContentValuesMapper.map(item)
                )
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

    @Throws(java.lang.Exception::class)
    private fun getWherePkClause(soPackExpressPacksLocal: SoPackExpressPacksLocal?): StringBuilder {
        soPackExpressPacksLocal?.let {
            return java.lang.StringBuilder()
                .append(
                    """
                        ${CUSTOMER_CODE} = '${soPackExpressPacksLocal.customer_code}'  
                        AND ${SITE_CODE} = '${soPackExpressPacksLocal.site_code}'
                        AND ${OPERATION_CODE} = '${soPackExpressPacksLocal.operation_code}'
                        AND ${PRODUCT_CODE} = '${soPackExpressPacksLocal.product_code}'
                        AND ${EXPRESS_CODE} = '${soPackExpressPacksLocal.express_code}'
                        AND ${EXPRESS_TMP} = '${soPackExpressPacksLocal.express_tmp}'
                        AND ${PACK_CODE} = '${soPackExpressPacksLocal.pack_code}'
                        AND ${PACK_SEQ} = '${soPackExpressPacksLocal.pack_seq}'
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    override fun addUpdate(
        items: MutableList<SoPackExpressPacksLocal>?,
        status: Boolean
    ): DaoObjReturn {
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

    override fun getByString(sQuery: String?): SoPackExpressPacksLocal? {
        var soPackExpressPacksLocal: SoPackExpressPacksLocal? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                soPackExpressPacksLocal = toSoPackExpressPacksLocalMapper.map(cursor)
            }
            //
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return soPackExpressPacksLocal
    }

    override fun getByStringHM(sQuery: String?): HMAux? {
        var hmAux: HMAux? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                hmAux = CursorToHMAuxMapper.mapN(cursor)
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return hmAux
    }

    override fun query(sQuery: String?): MutableList<SoPackExpressPacksLocal> {
        val soPackExpressPacksLocal = mutableListOf<SoPackExpressPacksLocal>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                val uAux = toSoPackExpressPacksLocalMapper.map(cursor)
                soPackExpressPacksLocal.add(uAux)
                if(uAux != null){
                    getSoPackExpressPackServicesLocal(uAux)
                }
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return soPackExpressPacksLocal
    }

    private fun getSoPackExpressPackServicesLocal(soPackExpressPacksLocal: SoPackExpressPacksLocal) {
        val soPackExpressServicesLocalDao = SoPackExpressServicesLocalDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        )
        val services = soPackExpressServicesLocalDao.query(
            SoPackExpressServicesLocalSql002(
                soPackExpressPacksLocal.customer_code,
                soPackExpressPacksLocal.site_code,
                soPackExpressPacksLocal.operation_code,
                soPackExpressPacksLocal.product_code,
                soPackExpressPacksLocal.express_code,
                soPackExpressPacksLocal.express_tmp,
                soPackExpressPacksLocal.pack_code,
                soPackExpressPacksLocal.pack_seq
            ).toSqlQuery()
        )
        //
        soPackExpressPacksLocal.serviceList.addAll(services)
    }

    override fun query_HM(sQuery: String?): MutableList<HMAux> {
        val soPackExpressPacksLocal: MutableList<HMAux> = ArrayList()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                soPackExpressPacksLocal.add(CursorToHMAuxMapper.mapN(cursor))
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return soPackExpressPacksLocal
    }

    fun addUpdate(items: MutableList<SoPackExpressPacksLocal>?, status: Boolean, dbInstance: SQLiteDatabase?): DaoObjReturn {
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
            daoObjReturn.table = SoPackExpressPacksLocalDao.TABLE
            curAction = DaoObjReturn.UPDATE
            if(dbInstance == null) {
                db.beginTransaction()
            }
            if (status) {
                db.delete(SoPackExpressPacksLocalDao.TABLE, null, null)
            }

            items?.forEach { item ->
                //Where para update
                val sbWhere: StringBuilder = getWherePkClause(item)
                //Tenta update e armazena retorno
                addUpdateRet = db.update(
                    SoPackExpressPacksLocalDao.TABLE,
                    toContentValuesMapper.map(item),
                    sbWhere.toString(),
                    null
                ).toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db?.insertOrThrow(
                        SoPackExpressPacksLocalDao.TABLE,
                        null,
                        toContentValuesMapper.map(item)
                    )
                }
                if(item.serviceList.size >0) {
                    tryAddUpdateSoPackExpressServicesLocalDao(item.serviceList, false, db)
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
            if(dbInstance == null) {
                db.endTransaction()
            }
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

    private fun tryAddUpdateSoPackExpressServicesLocalDao(
        services: MutableList<SoPackExpressServicesLocal>,
        status: Boolean,
        dbInstance: SQLiteDatabase?
    ) {
        var soPackExpressServicesLocalDao = getSoPackExpressServicesLocalDao()
        soPackExpressServicesLocalDao.addUpdate(services, status,dbInstance)
    }

    /**
     * Fun que retorna o dao do historico.
     */
    private fun getSoPackExpressServicesLocalDao(): SoPackExpressServicesLocalDao {
        return SoPackExpressServicesLocalDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        )
    }

    //
    class CursorToSoPackExpressPacksLocalMapper : Mapper<Cursor, SoPackExpressPacksLocal> {
        override fun map(cursor: Cursor?): SoPackExpressPacksLocal? {
            cursor?.let {
                with(cursor) {
                    return SoPackExpressPacksLocal(
                        customer_code = getLong(getColumnIndex(CUSTOMER_CODE)),
                        site_code = getLong(getColumnIndex(SITE_CODE)),
                        operation_code = getLong(getColumnIndex(OPERATION_CODE)),
                        product_code = getLong(getColumnIndex(PRODUCT_CODE)),
                        express_code = getString(getColumnIndex(EXPRESS_CODE)),
                        express_tmp = getLong(getColumnIndex(EXPRESS_TMP)),
                        pack_code = getInt(getColumnIndex(PACK_CODE)),
                        pack_seq = getInt(getColumnIndex(PACK_SEQ)),
                        pack_service_desc = getString(getColumnIndex(PACK_SERVICE_DESC)),
                        pack_service_desc_full = getString(getColumnIndex(PACK_SERVICE_DESC_FULL)),
                        price_list_code = getInt(getColumnIndex(PRICE_LIST_CODE)),
                        qty = getInt(getColumnIndex(QTY)),
                        type_ps = getString(getColumnIndex(TYPE_PS)),
                        comments = getStringOrNull(getColumnIndex(COMMENTS))
                    )
                }
            }
            //
            return null
        }
    }

    private class SoPackExpressPacksLocalToContentValuesMapper :
        Mapper<SoPackExpressPacksLocal, ContentValues> {
        override fun map(soPackExpressPacksLocal: SoPackExpressPacksLocal?): ContentValues {
            val contentValues = ContentValues()
            //
            soPackExpressPacksLocal?.let {
                with(contentValues) {
                    if (soPackExpressPacksLocal.customer_code > -1) {
                        put(
                            CUSTOMER_CODE,
                            soPackExpressPacksLocal.customer_code
                        )
                    }
                    //
                    if (soPackExpressPacksLocal.site_code > -1) {
                        put(
                            SITE_CODE,
                            soPackExpressPacksLocal.site_code
                        )
                    }
                    //
                    if (soPackExpressPacksLocal.operation_code > -1) {
                        put(
                            OPERATION_CODE,
                            soPackExpressPacksLocal.operation_code
                        )
                    }
                    //
                    if (soPackExpressPacksLocal.product_code > -1) {
                        put(
                            PRODUCT_CODE,
                            soPackExpressPacksLocal.product_code
                        )
                    }
                    //
                    if (soPackExpressPacksLocal.express_code != null) {
                        put(
                            EXPRESS_CODE,
                            soPackExpressPacksLocal.express_code
                        )
                    }
                    //
                    if (soPackExpressPacksLocal.express_tmp > -1) {
                        put(
                            EXPRESS_TMP,
                            soPackExpressPacksLocal.express_tmp
                        )
                    }
                    //
                    if (soPackExpressPacksLocal.pack_code > -1) {
                        put(
                            PACK_CODE,
                            soPackExpressPacksLocal.pack_code
                        )
                    }
                    //
                    if (soPackExpressPacksLocal.pack_seq > -1) {
                        put(
                            PACK_SEQ,
                            soPackExpressPacksLocal.pack_seq
                        )
                    }
                    //
                    put(
                        PACK_SERVICE_DESC,
                        soPackExpressPacksLocal.pack_service_desc
                    )
                    //
                    put(
                        PACK_SERVICE_DESC_FULL,
                        soPackExpressPacksLocal.pack_service_desc_full
                    )
                    //
                    if (soPackExpressPacksLocal.price_list_code > -1) {
                        put(
                            PRICE_LIST_CODE,
                            soPackExpressPacksLocal.price_list_code
                        )
                    }
                    //
                    if (soPackExpressPacksLocal.qty > -1) {
                        put(
                            QTY,
                            soPackExpressPacksLocal.qty
                        )
                    }
                    //
                    if (soPackExpressPacksLocal.type_ps != null) {
                        put(
                            TYPE_PS,
                            soPackExpressPacksLocal.type_ps
                        )
                    }
                    //
                    put(
                        COMMENTS,
                        soPackExpressPacksLocal.comments
                    )
                    //
                }
            }
            //
            return contentValues
        }
    }
    //

}
