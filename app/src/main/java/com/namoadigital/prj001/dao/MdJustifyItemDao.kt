package com.namoadigital.prj001.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.database.CursorToHMAuxMapper
import com.namoadigital.prj001.database.Mapper
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.MdJustifyItem
import com.namoadigital.prj001.sql.MdJustifyItemSqlSS
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class MdJustifyItemDao(
    context: Context,
    mDB_NAME: String,
    mDB_VERSION: Int
) : BaseDao(
    context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI ),
    DaoWithReturn<MdJustifyItem> {
    companion object{
        const val TABLE = "md_justify_item"
        const val CUSTOMER_CODE = "customer_code"
        const val JUSTIFY_GROUP_CODE = "justify_group_code"
        const val JUSTIFY_ITEM_CODE = "justify_item_code"
        const val JUSTIFY_ITEM_ID = "justify_item_id"
        const val JUSTIFY_ITEM_DESC = "justify_item_desc"
        const val REQUIRED_COMMENT = "required_comment"
        const val RESCHEDULE = "reschedule"
    }

    private val toMdJustifyItemMapper: Mapper<Cursor, MdJustifyItem>
    private val toContentValuesMapper: Mapper<MdJustifyItem,ContentValues>

    init {
        this.toMdJustifyItemMapper = CursorToMdJustifyItemMapper()
        this.toContentValuesMapper = MdJustifyItemToContentValuesMapper()
    }

    private fun getWherePkClause(mdJustifyItem: MdJustifyItem?): StringBuilder{
        mdJustifyItem?.let{
            return java.lang.StringBuilder()
                .append("""
                        ${CUSTOMER_CODE} = '${mdJustifyItem.customerCode}'  
                        AND ${JUSTIFY_GROUP_CODE} = '${mdJustifyItem.justifyGroupCode}'                           
                        AND ${JUSTIFY_ITEM_CODE} = '${mdJustifyItem.justifyItemCode}'                           
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }
    //
    override fun addUpdate(mdJustifyItem: MdJustifyItem?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()

        try {
            daoObjReturn.table = MdJustifyItemDao.TABLE
            curAction = DaoObjReturn.UPDATE
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(mdJustifyItem)
            //Tenta update e armazena retorno
            addUpdateRet = db.update(MdJustifyItemDao.TABLE, toContentValuesMapper.map(mdJustifyItem), sbWhere.toString(), null).toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(MdJustifyItemDao.TABLE, null, toContentValuesMapper.map(mdJustifyItem))
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

    override fun addUpdate(mdJustifyItems: MutableList<MdJustifyItem>?, status: Boolean): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()

        try {
            daoObjReturn.table = MdJustifyItemDao.TABLE
            curAction = DaoObjReturn.UPDATE

            db.beginTransaction()

            if (status) {
                db.delete(MdJustifyItemDao.TABLE, null, null)
            }

            mdJustifyItems?.forEach { mdJustifyItem ->
                val sbWhere: StringBuilder = getWherePkClause(mdJustifyItem)
                //Tenta update e armazena retorno
                addUpdateRet = db.update(MdJustifyItemDao.TABLE, toContentValuesMapper.map(mdJustifyItem), sbWhere.toString(), null).toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(MdJustifyItemDao.TABLE, null, toContentValuesMapper.map(mdJustifyItem))
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

    override fun getByString(sQuery: String?): MdJustifyItem? {
        var mdJustifyItem: MdJustifyItem? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                mdJustifyItem = toMdJustifyItemMapper.map(cursor)
            }
            //
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return mdJustifyItem
    }

    override fun getByStringHM(sQuery: String?): HMAux? {
        var mdJustifyItem: HMAux? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                mdJustifyItem = CursorToHMAuxMapper.mapN(cursor)
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return  mdJustifyItem
    }

    override fun query(sQuery: String?): MutableList<MdJustifyItem> {
        val mdJustifyItems = mutableListOf<MdJustifyItem>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                val uAux = toMdJustifyItemMapper.map(cursor)
                mdJustifyItems.add(uAux)
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return mdJustifyItems
    }

    override fun query_HM(sQuery: String?): MutableList<HMAux> {
        val mdJustifyItems = mutableListOf<HMAux>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                mdJustifyItems.add(CursorToHMAuxMapper.mapN(cursor))
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return mdJustifyItems
    }
    //
    private class CursorToMdJustifyItemMapper : Mapper<Cursor, MdJustifyItem> {
        override fun map(cursor: Cursor?): MdJustifyItem? {
            cursor?.let {
                with(cursor){
                    return MdJustifyItem(
                        getLong(getColumnIndex(CUSTOMER_CODE)),
                        getInt(getColumnIndex(JUSTIFY_GROUP_CODE)),
                        getInt(getColumnIndex(JUSTIFY_ITEM_CODE)),
                        getString(getColumnIndex(JUSTIFY_ITEM_ID)),
                        getString(getColumnIndex(JUSTIFY_ITEM_DESC)),
                        getInt(getColumnIndex(REQUIRED_COMMENT)),
                        getInt(getColumnIndex(RESCHEDULE))
                    )
                }
            }
            return null
        }
    }

    private class MdJustifyItemToContentValuesMapper : Mapper<MdJustifyItem, ContentValues> {
        override fun map(mdJustifyItem: MdJustifyItem?): ContentValues {
            val contentValues = ContentValues()
            //
            mdJustifyItem?.let {
                with(contentValues){
                    if(mdJustifyItem.customerCode > -1){
                        put(CUSTOMER_CODE,mdJustifyItem.customerCode)
                    }
                    if(mdJustifyItem.justifyGroupCode > -1){
                        put(JUSTIFY_GROUP_CODE,mdJustifyItem.justifyGroupCode)
                    }
                    put(JUSTIFY_ITEM_CODE,mdJustifyItem.justifyItemCode)
                    put(JUSTIFY_ITEM_ID,mdJustifyItem.justifyItemId)
                    put(JUSTIFY_ITEM_DESC, mdJustifyItem.justifyItemDesc)
                    put(REQUIRED_COMMENT, mdJustifyItem.requiredComment)
                    put(RESCHEDULE, mdJustifyItem.reschedule)
                }
            }
            //
            return contentValues
        }
    }


    fun getJustifyItems(customer_code: Long, justifyGroupCode: Int): ArrayList<HMAux> {
        val justifyItems = query_HM(
            MdJustifyItemSqlSS(
                customer_code,
                justifyGroupCode
            ).toSqlQuery()
        ) as ArrayList<HMAux>?
        return justifyItems ?: ArrayList()
    }

}