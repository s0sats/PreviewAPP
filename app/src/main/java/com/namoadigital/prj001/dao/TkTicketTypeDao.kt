package com.namoadigital.prj001.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.database.CursorToHMAuxMapper
import com.namoadigital.prj001.database.Mapper
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.TkTicketType
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class TkTicketTypeDao(
    val context: Context,
    val mDB_NAME: String,
    val mDB_VERSION: Int
) : BaseDao(
    context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI
), DaoWithReturn<TkTicketType> {

    companion object {
        const val TABLE = "tk_ticket_type"
        const val CUSTOMER_CODE = "customer_code"
        const val TICKET_TYPE_CODE = "ticket_type_code"
        const val TICKET_TYPE_ID = "ticket_type_id"
        const val TICKET_TYPE_DESC = "ticket_type_desc"
        const val ALL_SITE = "all_site"
        const val ALL_OPERATION = "all_operation"
        const val ALL_PRODUCT = "all_product"
        const val TAG_OPERATIONAL_CODE = "tag_operational_code"

    }

    private val toTkTicketTypeMapper: Mapper<Cursor, TkTicketType>
    private val toContentValuesMapper: Mapper<TkTicketType, ContentValues>

    init {
        toTkTicketTypeMapper = CursorToTkTicketTypeMapper()
        toContentValuesMapper = TkTicketTypeToContentValuesMapper()
    }

    override fun addUpdate(tkTicketType: TkTicketType?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()

        try {
            daoObjReturn.table = TABLE
            curAction = DaoObjReturn.UPDATE
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(tkTicketType)
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tkTicketType), sbWhere.toString(), null).toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tkTicketType))
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
    private fun getWherePkClause(tkTicketType: TkTicketType?): StringBuilder {
        tkTicketType?.let{
            return java.lang.StringBuilder()
                .append("""
                        ${CUSTOMER_CODE} = '${tkTicketType.customer_code}'  
                        AND ${TICKET_TYPE_CODE} = '${tkTicketType.ticket_type_code}'
                      
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    override fun addUpdate(tkTicketChaces: MutableList<TkTicketType>?, status: Boolean): DaoObjReturn {
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

            tkTicketChaces?.forEach { tkTicketType->
                //Where para update
                val sbWhere: StringBuilder = getWherePkClause(tkTicketType)
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tkTicketType), sbWhere.toString(), null).toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tkTicketType))
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

    override fun getByString(sQuery: String): TkTicketType? {
        var tkTicketType: TkTicketType? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                tkTicketType = toTkTicketTypeMapper.map(cursor)
            }
            //
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return tkTicketType
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

    override fun query(sQuery: String?): MutableList<TkTicketType> {
        val tkTicketTypes = mutableListOf<TkTicketType>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery!!, null)
            while (cursor.moveToNext()) {
                val uAux = toTkTicketTypeMapper.map(cursor)
                tkTicketTypes.add(uAux)
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return tkTicketTypes
    }

    override fun query_HM(sQuery: String?): MutableList<HMAux> {
        val tkTicketTypes: MutableList<HMAux> = ArrayList()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery!!, null)
            while (cursor.moveToNext()) {
                tkTicketTypes.add(CursorToHMAuxMapper.mapN(cursor))
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return tkTicketTypes
    }


    private inner class TkTicketTypeToContentValuesMapper : Mapper<TkTicketType, ContentValues> {
        override fun map(tkTicketType: TkTicketType?): ContentValues {
            val contentValues = ContentValues()
            //
            tkTicketType?.let{
                with(contentValues){
                    if(it.customer_code > -1){
                        put(CUSTOMER_CODE, it.customer_code)
                    }
                    if(it.ticket_type_code > -1){
                        put(TICKET_TYPE_CODE,it.ticket_type_code)
                    }
                    put(TICKET_TYPE_ID,it.ticket_type_id)
                    put(TICKET_TYPE_DESC, it.ticket_type_desc)
                    put(ALL_SITE, it.all_site)
                    put(ALL_OPERATION, it.all_operation)
                    put(ALL_PRODUCT, it.all_product)
                    if(it.tag_operational_code > -1) {
                        put(TAG_OPERATIONAL_CODE, it.tag_operational_code)
                    }
                }
            }
            //
            return contentValues
        }
    }

    private inner class CursorToTkTicketTypeMapper : Mapper<Cursor, TkTicketType> {
        override fun map(cursor: Cursor?): TkTicketType? {
            cursor?.let {
                with(cursor) {
                    return TkTicketType(
                        customer_code = getLong(getColumnIndex(CUSTOMER_CODE)),
                        ticket_type_code = getInt(getColumnIndex(TICKET_TYPE_CODE)),
                        ticket_type_id = getString(getColumnIndex(TICKET_TYPE_ID)),
                        ticket_type_desc = getString(getColumnIndex(TICKET_TYPE_DESC)),
                        all_site = getInt(getColumnIndex(ALL_SITE)),
                        all_operation = getInt(getColumnIndex(ALL_OPERATION)),
                        all_product = getInt(getColumnIndex(ALL_PRODUCT)),
                        tag_operational_code = getInt(getColumnIndex(TAG_OPERATIONAL_CODE))
                    )
                }
            }
            //
            return null
        }
    }
}