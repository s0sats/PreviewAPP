package com.namoadigital.prj001.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.database.CursorToHMAuxMapper
import com.namoadigital.prj001.database.Mapper
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.TkTicketCache
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.util.ArrayList

class TkTicketCacheDao(
        val context: Context,
        val mDB_NAME: String,
        val mDB_VERSION: Int
) : BaseDao(
        context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI
), DaoWithReturn<TkTicketCache> {

    companion object {
        const val TABLE = "tk_ticket_cache"
        const val CUSTOMER_CODE = "CUSTOMER_CODE"
        const val TICKET_PREFIX = "TICKET_PREFIX"
        const val TICKET_CODE = "TICKET_CODE"
        const val SCN = "SCN"
        const val USER_LEVEL_MIN = "USER_LEVEL_MIN"
        const val TICKET_ID = "TICKET_ID"
        const val TAG_OPERATIONAL_CODE = "TAG_OPERATIONAL_CODE"
        const val TAG_OPERATIONAL_ID = "TAG_OPERATIONAL_ID"
        const val TAG_OPERATIONAL_DESC = "TAG_OPERATIONAL_DESC"
        const val TYPE_CODE = "TYPE_CODE"
        const val TYPE_ID = "TYPE_ID"
        const val TYPE_DESC = "TYPE_DESC"
        const val USER_FOCUS = "USER_FOCUS"
        const val ORDER_BY = "ORDER_BY"
        const val OPEN_SITE_CODE = "OPEN_SITE_CODE"
        const val OPEN_SITE_DESC = "OPEN_SITE_DESC"
        const val OPEN_PRODUCT_DESC = "OPEN_PRODUCT_DESC"
        const val OPEN_SERIAL_ID = "OPEN_SERIAL_ID"
        const val CURRENT_STEP_ORDER = "CURRENT_STEP_ORDER"
        const val TICKET_STATUS = "TICKET_STATUS"
        const val ORIGIN_DESC = "ORIGIN_DESC"
        const val STEP_DESC = "STEP_DESC"
        const val FORECAST_START = "FORECAST_START"
        const val FORECAST_END = "FORECAST_END"
        const val STEP_COUNT = "STEP_COUNT"
        const val STEP_ORDER_SEQ = "STEP_ORDER_SEQ"
    }

    private val toTkTicketCacheMapper: Mapper<Cursor, TkTicketCache>
    private val toContentValuesMapper: Mapper<TkTicketCache, ContentValues>

    init {
        toTkTicketCacheMapper = CursorToTkTicketCacheMapper()
        toContentValuesMapper = TkTicketCacheToContentValuesMapper()
    }

    override fun addUpdate(tkTicketCache: TkTicketCache?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()

        try {
            daoObjReturn.table = TABLE
            curAction = DaoObjReturn.UPDATE
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(tkTicketCache)
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tkTicketCache), sbWhere.toString(), null).toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tkTicketCache))
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
    private fun getWherePkClause(tkTicketCache: TkTicketCache?): StringBuilder {
        tkTicketCache?.let{
            return java.lang.StringBuilder()
                    .append("""
                        ${CUSTOMER_CODE} = '${tkTicketCache.customer_code}'  
                        AND ${TICKET_PREFIX} = '${tkTicketCache.ticket_prefix}'
                        AND ${TICKET_CODE} = '${tkTicketCache.ticket_code}'
                        """.trimIndent()
                    )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    override fun addUpdate(tkTicketChaces: MutableList<TkTicketCache>?, status: Boolean): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()

        try {
            daoObjReturn.table = TABLE
            curAction = DaoObjReturn.UPDATE

            db.beginTransaction()

            if (status) {
                db.delete(TABLE, null, null)
            }

            tkTicketChaces?.forEach { tkTicketCache->
                //Where para update
                val sbWhere: StringBuilder = getWherePkClause(tkTicketCache)
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tkTicketCache), sbWhere.toString(), null).toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tkTicketCache))
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

    override fun getByString(sQuery: String): TkTicketCache? {
        var tkTicketCache: TkTicketCache? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                tkTicketCache = toTkTicketCacheMapper.map(cursor)
            }
            //
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return tkTicketCache
    }

    override fun getByStringHM(sQuery: String): HMAux? {
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

    override fun query(sQuery: String?): MutableList<TkTicketCache> {
        val tkTicketCaches = mutableListOf<TkTicketCache>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
               val uAux = toTkTicketCacheMapper.map(cursor)
                tkTicketCaches.add(uAux)
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return tkTicketCaches
    }

    override fun query_HM(sQuery: String?): MutableList<HMAux> {
        val tkTicketCaches: MutableList<HMAux> = ArrayList()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                tkTicketCaches.add(CursorToHMAuxMapper.mapN(cursor))
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return tkTicketCaches
    }


    private inner class TkTicketCacheToContentValuesMapper : Mapper<TkTicketCache, ContentValues> {
        override fun map(ticketCache: TkTicketCache?): ContentValues {
            val contentValues = ContentValues()
            //
            ticketCache?.let{
                with(contentValues){
                    if(ticketCache.customer_code > -1){
                        put(CUSTOMER_CODE, ticketCache.customer_code)
                    }
                    if(ticketCache.ticket_prefix > -1){
                        put(TICKET_PREFIX,ticketCache.ticket_prefix)
                    }
                    if(ticketCache.ticket_code > -1){
                        put(TICKET_CODE,ticketCache.ticket_code)
                    }
                    if(ticketCache.scn > -1){
                        put(SCN,ticketCache.scn)
                    }
                    if(ticketCache.user_level_min > -1){
                        put(USER_LEVEL_MIN,ticketCache.user_level_min)
                    }
                    put(TICKET_ID,ticketCache.ticket_id)
                    if(ticketCache.tag_operational_code > -1){
                        put(TAG_OPERATIONAL_CODE,ticketCache.tag_operational_code)
                    }
                    put(TAG_OPERATIONAL_ID,ticketCache.tag_operational_id)
                    put(TAG_OPERATIONAL_DESC,ticketCache.tag_operational_desc)
                    if(ticketCache.type_code > -1){
                        put(TYPE_CODE,ticketCache.type_code)
                    }
                    put(TYPE_ID ,ticketCache.type_id)
                    put(TYPE_DESC,ticketCache.type_desc)
                    if(ticketCache.user_focus > -1){
                        put(USER_FOCUS,ticketCache.user_focus)
                    }
                    put(ORDER_BY,ticketCache.order_by)
                    if(ticketCache.open_site_code > -1){
                        put(OPEN_SITE_CODE,ticketCache.open_site_code)
                    }
                    put(OPEN_SITE_DESC,ticketCache.open_site_desc)
                    put(OPEN_PRODUCT_DESC,ticketCache.open_product_desc)
                    put(OPEN_SERIAL_ID,ticketCache.open_serial_id)
                    if(ticketCache.current_step_order > -1){
                        put(CURRENT_STEP_ORDER,ticketCache.current_step_order)
                    }
                    put(TICKET_STATUS,ticketCache.ticket_status)
                    put(ORIGIN_DESC,ticketCache.origin_desc)
                    put(STEP_DESC,ticketCache.step_desc)
                    put(FORECAST_START,ticketCache.forecast_start)
                    put(FORECAST_END,ticketCache.forecast_end)
                    if(ticketCache.step_count > -1){
                        put(STEP_COUNT,ticketCache.step_count)
                    }
                    if(ticketCache.step_order_seq != null){
                        put(STEP_ORDER_SEQ,ticketCache.step_order_seq)
                    }
                }
            }
            //
            return contentValues
        }
    }

    private inner class CursorToTkTicketCacheMapper : Mapper<Cursor, TkTicketCache> {
        override fun map(cursor: Cursor?): TkTicketCache? {
            cursor?.let {
                with(cursor) {
                    return TkTicketCache(
                            customer_code = getInt(getColumnIndex(CUSTOMER_CODE)),
                            ticket_prefix = getInt(getColumnIndex(TICKET_PREFIX)),
                            ticket_code = getInt(getColumnIndex(TICKET_CODE)),
                            scn = getInt(getColumnIndex(SCN)),
                            user_level_min = getInt(getColumnIndex(USER_LEVEL_MIN)),
                            ticket_id = getString(getColumnIndex(TICKET_ID)),
                            tag_operational_code = getInt(getColumnIndex(TAG_OPERATIONAL_CODE)),
                            tag_operational_id = getString(getColumnIndex(TAG_OPERATIONAL_ID)),
                            tag_operational_desc = getString(getColumnIndex(TAG_OPERATIONAL_DESC)),
                            type_code = getInt(getColumnIndex(TYPE_CODE)),
                            type_id = getString(getColumnIndex(TYPE_ID)),
                            type_desc = getString(getColumnIndex(TYPE_DESC)),
                            user_focus = getInt(getColumnIndex(USER_FOCUS)),
                            order_by = getString(getColumnIndex(ORDER_BY)),
                            open_site_code = getInt(getColumnIndex(OPEN_SITE_CODE)),
                            open_site_desc = getString(getColumnIndex(OPEN_SITE_DESC)),
                            open_product_desc = getString(getColumnIndex(OPEN_PRODUCT_DESC)),
                            open_serial_id = getString(getColumnIndex(OPEN_SERIAL_ID)),
                            current_step_order = getInt(getColumnIndex(CURRENT_STEP_ORDER)),
                            ticket_status = getString(getColumnIndex(TICKET_STATUS)),
                            origin_desc = getString(getColumnIndex(ORIGIN_DESC)),
                            step_desc = getString(getColumnIndex(STEP_DESC)),
                            forecast_start = getString(getColumnIndex(FORECAST_START)),
                            forecast_end = getString(getColumnIndex(FORECAST_END))
                                    ?: null,//desnecessario
                            step_count = getInt(getColumnIndex(STEP_COUNT)),
                            step_order_seq = getInt(getColumnIndex(STEP_ORDER_SEQ)) ?: null
                    )
                }
            }
            //
            return null
        }
    }
}