package com.namoadigital.prj001.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.database.CursorToHMAuxMapper
import com.namoadigital.prj001.database.Mapper
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.TkTicketTypeOperation
import com.namoadigital.prj001.model.TkTicketTypeSite
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class TkTicketTypeOperationDao(
    val context: Context,
    val mDB_NAME: String,
    val mDB_VERSION: Int
) : BaseDao(
    context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI
), DaoWithReturn<TkTicketTypeOperation> {

    companion object {
        const val TABLE = "tk_ticket_type"
        const val CUSTOMER_CODE = "customer_code"
        const val TICKET_TYPE_CODE = "ticket_type_code"
        const val OPERATION_CODE = "operation_code"

    }

    private val toTkTicketTypeOperationMapper: Mapper<Cursor, TkTicketTypeOperation>
    private val toContentValuesMapper: Mapper<TkTicketTypeOperation, ContentValues>

    init {
        toTkTicketTypeOperationMapper = CursorToTkTicketTypeOperationMapper()
        toContentValuesMapper = TkTicketTypeOperationToContentValuesMapper()
    }

    override fun addUpdate(tkTicketTypeProduct: TkTicketTypeOperation?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()

        try {
            daoObjReturn.table = TABLE
            curAction = DaoObjReturn.UPDATE
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(tkTicketTypeProduct)
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tkTicketTypeProduct), sbWhere.toString(), null).toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tkTicketTypeProduct))
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
    private fun getWherePkClause(tkTicketTypeProduct: TkTicketTypeOperation?): StringBuilder {
        tkTicketTypeProduct?.let{
            return java.lang.StringBuilder()
                .append("""
                        ${CUSTOMER_CODE} = '${tkTicketTypeProduct.customer_code}'  
                        AND ${TICKET_TYPE_CODE} = '${tkTicketTypeProduct.ticket_type_code}'
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    override fun addUpdate(tkTicketChaces: MutableList<TkTicketTypeOperation>?, status: Boolean): DaoObjReturn {
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

            tkTicketChaces?.forEach { tkTicketTypeProduct->
                //Where para update
                val sbWhere: StringBuilder = getWherePkClause(tkTicketTypeProduct)
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tkTicketTypeProduct), sbWhere.toString(), null).toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tkTicketTypeProduct))
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

    override fun getByString(sQuery: String): TkTicketTypeOperation? {
        var tkTicketTypeProduct: TkTicketTypeOperation? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                tkTicketTypeProduct = toTkTicketTypeOperationMapper.map(cursor)
            }
            //
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return tkTicketTypeProduct
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

    override fun query(sQuery: String?): MutableList<TkTicketTypeOperation> {
        val tkTicketTypeProducts = mutableListOf<TkTicketTypeOperation>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                val uAux = toTkTicketTypeOperationMapper.map(cursor)
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


    private inner class TkTicketTypeOperationToContentValuesMapper :
        Mapper<TkTicketTypeOperation, ContentValues> {
        override fun map(tkTicketTypeProduct: TkTicketTypeOperation?): ContentValues {
            val contentValues = ContentValues()
            //
            tkTicketTypeProduct?.let{
                with(contentValues){
                    if(it.customer_code > -1){
                        put(CUSTOMER_CODE, it.customer_code)
                    }
                    if(it.ticket_type_code > -1){
                        put(TICKET_TYPE_CODE,it.ticket_type_code)
                    }
                    if(it.operation_code > -1) {
                        put(OPERATION_CODE, it.operation_code)
                    }
                }
            }
            //
            return contentValues
        }
    }

    private inner class CursorToTkTicketTypeOperationMapper : Mapper<Cursor, TkTicketTypeOperation> {
        override fun map(cursor: Cursor?): TkTicketTypeOperation? {
            cursor?.let {
                with(cursor) {
                    return TkTicketTypeOperation(
                        customer_code = getLong(getColumnIndex(CUSTOMER_CODE)),
                        ticket_type_code = getInt(getColumnIndex(TICKET_TYPE_CODE)),
                        operation_code = getInt(getColumnIndex(OPERATION_CODE))
                    )
                }
            }
            //
            return null
        }
    }
}