package com.namoadigital.prj001.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import androidx.core.database.getStringOrNull
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.database.CursorToHMAuxMapper
import com.namoadigital.prj001.database.Mapper
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.SoPackExpressServicesLocal
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class SoPackExpressServicesLocalDao(
    val context: Context,
    val mDB_NAME: String,
    val mDB_VERSION: Int
) : BaseDao(
    context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI
), DaoWithReturn<SoPackExpressServicesLocal> {

    companion object {
        const val TABLE = "tk_ticket_type_sites"
        const val CUSTOMER_CODE = "customer_code"
        const val SITE_CODE = "site_code"
        const val OPERATION_CODE = "operation_code"
        const val PRODUCT_CODE = "product_code"
        const val EXPRESS_CODE = "express_code"
        const val EXPRESS_TMP = "express_tmp"
        const val PACK_CODE = "pack_code"
        const val PACK_SEQ = "pack_seq"
        const val SERVICE_CODE = "service_code"
        const val SERVICE_SEQ = "service_seq"
        const val SERVICE_DESC = "service_desc"
        const val SERVICE_DESC_FULL = "service_desc_full"
        const val PRICE = "price"
        const val MANUAL_PRICE = "manual_price"
        const val QTY = "qty"
        const val COMMENTS = "comments"

    }

    private val toSoPackExpressServicesLocalMapper: Mapper<Cursor, SoPackExpressServicesLocal>
    private val toContentValuesMapper: Mapper<SoPackExpressServicesLocal, ContentValues>

    init {
        toSoPackExpressServicesLocalMapper = CursorToSoPackExpressServicesLocalMapper()
        toContentValuesMapper = SoPackExpressServicesLocalToContentValuesMapper()
    }


    override fun addUpdate(item: SoPackExpressServicesLocal?): DaoObjReturn {
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
    private fun getWherePkClause(soPackExpressServicesLocal: SoPackExpressServicesLocal?): StringBuilder {
        soPackExpressServicesLocal?.let {
            return java.lang.StringBuilder()
                .append(
                    """
                        ${CUSTOMER_CODE} = '${soPackExpressServicesLocal.customer_code}'  
                        AND ${SITE_CODE} = '${soPackExpressServicesLocal.site_code}'
                        AND ${OPERATION_CODE} = '${soPackExpressServicesLocal.operation_code}'
                        AND ${PRODUCT_CODE} = '${soPackExpressServicesLocal.product_code}'
                        AND ${EXPRESS_CODE} = '${soPackExpressServicesLocal.express_code}'
                        AND ${EXPRESS_TMP} = '${soPackExpressServicesLocal.express_tmp}'
                        AND ${PACK_CODE} = '${soPackExpressServicesLocal.pack_code}'
                        AND ${PACK_SEQ} = '${soPackExpressServicesLocal.pack_seq}'
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    override fun addUpdate(
        items: MutableList<SoPackExpressServicesLocal>?,
        status: Boolean
    ): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()

        try {
            daoObjReturn.table = TkTicketTypeSiteDao.TABLE
            curAction = DaoObjReturn.UPDATE

            db.beginTransaction()

            if (status) {
                db.delete(TkTicketTypeSiteDao.TABLE, null, null)
            }

            items?.forEach { item ->
                //Where para update
                val sbWhere: StringBuilder = getWherePkClause(item)
                //Tenta update e armazena retorno
                addUpdateRet = db.update(
                    TkTicketTypeSiteDao.TABLE,
                    toContentValuesMapper.map(item),
                    sbWhere.toString(),
                    null
                ).toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(
                        TkTicketTypeSiteDao.TABLE,
                        null,
                        toContentValuesMapper.map(item)
                    )
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

    override fun getByString(sQuery: String?): SoPackExpressServicesLocal? {
        var soPackExpressServicesLocal: SoPackExpressServicesLocal? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                soPackExpressServicesLocal = toSoPackExpressServicesLocalMapper.map(cursor)
            }
            //
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return soPackExpressServicesLocal
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

    override fun query(sQuery: String?): MutableList<SoPackExpressServicesLocal> {
        val tkTicketTypeProducts = mutableListOf<SoPackExpressServicesLocal>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                val uAux = toSoPackExpressServicesLocalMapper.map(cursor)
                tkTicketTypeProducts.add(uAux)
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return tkTicketTypeProducts
    }

    override fun query_HM(sQuery: String?): MutableList<HMAux> {
        val tkTicketTypeProducts: MutableList<HMAux> = ArrayList()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                tkTicketTypeProducts.add(CursorToHMAuxMapper.mapN(cursor))
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return tkTicketTypeProducts
    }

    //
    class CursorToSoPackExpressServicesLocalMapper : Mapper<Cursor, SoPackExpressServicesLocal> {
        override fun map(cursor: Cursor?): SoPackExpressServicesLocal? {
            cursor?.let {
                with(cursor) {
                    return SoPackExpressServicesLocal(
                        customer_code = getLong(getColumnIndex(CUSTOMER_CODE)),
                        site_code = getLong(getColumnIndex(SITE_CODE)),
                        operation_code = getLong(getColumnIndex(OPERATION_CODE)),
                        product_code = getLong(getColumnIndex(PRODUCT_CODE)),
                        express_code = getString(getColumnIndex(EXPRESS_CODE)),
                        express_tmp = getLong(getColumnIndex(EXPRESS_TMP)),
                        pack_code = getInt(getColumnIndex(PACK_CODE)),
                        pack_seq = getInt(getColumnIndex(PACK_SEQ)),
                        service_code = getInt(getColumnIndex(SERVICE_CODE)),
                        service_seq = getInt(getColumnIndex(SERVICE_SEQ)),
                        service_desc = getString(getColumnIndex(SERVICE_DESC)),
                        service_desc_full = getString(getColumnIndex(SERVICE_DESC_FULL)),
                        price = getDouble(getColumnIndex(PRICE)),
                        manual_price = getInt(getColumnIndex(MANUAL_PRICE)),
                        qty = getInt(getColumnIndex(QTY)),
                        comments = getStringOrNull(getColumnIndex(COMMENTS))
                    )
                }
            }
            //
            return null
        }
    }

    private class SoPackExpressServicesLocalToContentValuesMapper :
        Mapper<SoPackExpressServicesLocal, ContentValues> {
        override fun map(soPackExpressServicesLocal: SoPackExpressServicesLocal?): ContentValues {
            val contentValues = ContentValues()
            //
            soPackExpressServicesLocal?.let {
                with(contentValues) {
                    if (soPackExpressServicesLocal.customer_code > -1) {
                        put(
                            CUSTOMER_CODE,
                            soPackExpressServicesLocal.customer_code
                        )
                    }
                    //
                    if (soPackExpressServicesLocal.site_code > -1) {
                        put(
                            SITE_CODE,
                            soPackExpressServicesLocal.site_code
                        )
                    }
                    //
                    if (soPackExpressServicesLocal.operation_code > -1) {
                        put(
                            OPERATION_CODE,
                            soPackExpressServicesLocal.operation_code
                        )
                    }
                    //
                    if (soPackExpressServicesLocal.product_code > -1) {
                        put(
                            PRODUCT_CODE,
                            soPackExpressServicesLocal.product_code
                        )
                    }
                    //
                    put(
                        EXPRESS_CODE,
                        soPackExpressServicesLocal.express_code
                    )
                    //
                    if (soPackExpressServicesLocal.express_tmp > -1) {
                        put(
                            EXPRESS_TMP,
                            soPackExpressServicesLocal.express_tmp
                        )
                    }
                    //
                    if (soPackExpressServicesLocal.pack_code > -1) {
                        put(
                            PACK_CODE,
                            soPackExpressServicesLocal.pack_code
                        )
                    }
                    //
                    if (soPackExpressServicesLocal.pack_seq > -1) {
                        put(
                            PACK_SEQ,
                            soPackExpressServicesLocal.pack_seq
                        )
                    }
                    //
                    if (soPackExpressServicesLocal.service_code > -1) {
                        put(
                            SERVICE_CODE,
                            soPackExpressServicesLocal.service_code
                        )
                    }
                    //
                    if (soPackExpressServicesLocal.service_seq > -1) {
                        put(
                            SERVICE_SEQ,
                            soPackExpressServicesLocal.service_seq
                        )
                    }
                    //
                    put(
                        SERVICE_DESC,
                        soPackExpressServicesLocal.service_desc
                    )
                    //
                    put(
                        SERVICE_DESC_FULL,
                        soPackExpressServicesLocal.service_desc_full
                    )
                    //
                    if (soPackExpressServicesLocal.price > -1) {
                        put(
                            PRICE,
                            soPackExpressServicesLocal.price
                        )
                    }
                    //
                    if (soPackExpressServicesLocal.manual_price > -1) {
                        put(
                            MANUAL_PRICE,
                            soPackExpressServicesLocal.manual_price
                        )
                    }
                    //
                    if (soPackExpressServicesLocal.qty > -1) {
                        put(
                            QTY,
                            soPackExpressServicesLocal.qty
                        )
                    }
                    //
                    put(
                        COMMENTS,
                        soPackExpressServicesLocal.comments
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
