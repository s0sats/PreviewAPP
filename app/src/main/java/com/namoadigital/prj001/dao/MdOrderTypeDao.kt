package com.namoadigital.prj001.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.database.CursorToHMAuxMapper
import com.namoadigital.prj001.database.Mapper
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.MdOrderType
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class MdOrderTypeDao(
    context: Context,
    mDB_NAME: String,
    mDB_VERSION: Int
) : BaseDao(
    context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI ),
    DaoWithReturn<MdOrderType> {

    companion object{
        const val TABLE = "md_order_type"
        const val CUSTOMER_CODE ="customer_code"
        const val ORDER_TYPE_CODE = "order_type_code"
        const val ORDER_TYPE_ID = "order_type_id"
        const val ORDER_TYPE_DESC = "order_type_desc"
        const val PROCESS_TYPE = "process_type"
        const val DISPLAY_OPTION = "display_option"
    }

    private val toMdOrderTypeMapper: Mapper<Cursor,MdOrderType>
    private val toContentValuesMapper: Mapper<MdOrderType,ContentValues>

    init {
        this.toMdOrderTypeMapper = CursorToMdOrderTypeMapper()
        this.toContentValuesMapper = MdOrderTypeToContentValuesMapper()
    }

    @Throws(java.lang.Exception::class)
    private fun getWherePkClause(mdOrderType: MdOrderType?): StringBuilder{
        mdOrderType?.let{
            return java.lang.StringBuilder()
                .append("""
                        ${CUSTOMER_CODE} = '${mdOrderType.customerCode}'  
                        AND ${ORDER_TYPE_CODE} = '${mdOrderType.orderTypeCode}'                           
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    override fun addUpdate(mdOrderType: MdOrderType?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()

        try {
            daoObjReturn.table = TABLE
            curAction = DaoObjReturn.UPDATE
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(mdOrderType)
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(mdOrderType), sbWhere.toString(), null).toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(mdOrderType))
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

    override fun addUpdate(mdOrderTypes: MutableList<MdOrderType>?, status: Boolean): DaoObjReturn {
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

            mdOrderTypes?.forEach { mdOrderType ->
                val sbWhere: StringBuilder = getWherePkClause(mdOrderType)
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(mdOrderType), sbWhere.toString(), null).toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(mdOrderType))
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

    override fun getByString(sQuery: String?): MdOrderType? {
        var mdOrderType: MdOrderType? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                mdOrderType = toMdOrderTypeMapper.map(cursor)
            }
            //
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return mdOrderType
    }

    override fun getByStringHM(sQuery: String?): HMAux? {
        var mdOrderType: HMAux? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                mdOrderType = CursorToHMAuxMapper.mapN(cursor)
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return  mdOrderType
    }

    override fun query(sQuery: String?): MutableList<MdOrderType> {
        val mdOrderTypes = mutableListOf<MdOrderType>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                val uAux = toMdOrderTypeMapper.map(cursor)
                mdOrderTypes.add(uAux)
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return mdOrderTypes
    }

    override fun query_HM(sQuery: String?): MutableList<HMAux> {
        val mdOrderTypes = mutableListOf<HMAux>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                mdOrderTypes.add(CursorToHMAuxMapper.mapN(cursor))
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return mdOrderTypes
    }

    private class CursorToMdOrderTypeMapper : Mapper<Cursor, MdOrderType> {
        override fun map(cursor: Cursor?): MdOrderType? {
            cursor?.let {
                with(cursor){
                    return MdOrderType(
                        customerCode = getLong(getColumnIndex(CUSTOMER_CODE)),
                        orderTypeCode = getInt(getColumnIndex(ORDER_TYPE_CODE)),
                        orderTypeId = getString(getColumnIndex(ORDER_TYPE_ID)),
                        orderTypeDesc = getString(getColumnIndex(ORDER_TYPE_DESC)),
                        processType = getString(getColumnIndex(PROCESS_TYPE)),
                        displayOption = getString(getColumnIndex(DISPLAY_OPTION))
                    )
                }
            }
            return null
        }
    }

    private class MdOrderTypeToContentValuesMapper : Mapper<MdOrderType, ContentValues> {
        override fun map(mdOrderType: MdOrderType?): ContentValues {
            val contentValues = ContentValues()
            //
            mdOrderType?.let {
                with(contentValues){
                    if(mdOrderType.customerCode > -1){
                        put(CUSTOMER_CODE,mdOrderType.customerCode)
                    }
                    if(mdOrderType.orderTypeCode > -1){
                        put(ORDER_TYPE_CODE,mdOrderType.orderTypeCode)
                    }
                    if(mdOrderType.orderTypeId != null){
                        put(ORDER_TYPE_ID,mdOrderType.orderTypeId)
                    }
                    if(mdOrderType.orderTypeDesc != null){
                        put(ORDER_TYPE_DESC,mdOrderType.orderTypeDesc)
                    }
                     if(mdOrderType.orderTypeDesc != null){
                        put(PROCESS_TYPE,mdOrderType.processType)
                    }
                    if(mdOrderType.displayOption != null){
                        put(DISPLAY_OPTION,mdOrderType.displayOption)
                    }
                }
            }
            //
            return contentValues
        }
    }
}