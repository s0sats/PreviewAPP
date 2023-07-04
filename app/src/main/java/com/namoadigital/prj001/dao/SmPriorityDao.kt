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
import com.namoadigital.prj001.model.SmPriority
import com.namoadigital.prj001.sql.MdTagSql001
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class SmPriorityDao (
    val context: Context,
    val mDB_NAME: String,
    val mDB_VERSION: Int
) : BaseDao(
    context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI
), DaoWithReturn<SmPriority> {

    companion object {
        const val TABLE = "sm_priority"
        const val CUSTOMER_CODE = "customer_code"
        const val PRIORITY_CODE = "priority_code"
        const val PRIORITY_DESC = "priority_desc"
        const val PRIORITY_WEIGHT = "priority_weight"
        const val PRIORITY_DEFAULT = "priority_default"
        const val PRIORITY_COLOR = "priority_color"

    }

    private val toSmPriorityMapper: Mapper<Cursor, SmPriority>
    private val toContentValuesMapper: Mapper<SmPriority, ContentValues>

    init {
        toSmPriorityMapper = CursorToSmPriorityMapper()
        toContentValuesMapper = SmPriorityToContentValuesMapper()
    }


    override fun addUpdate(smPriority: SmPriority?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()

        try {
            daoObjReturn.table = SmPriorityDao.TABLE
            curAction = DaoObjReturn.UPDATE
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(smPriority)
            //Tenta update e armazena retorno
            addUpdateRet = db.update(SmPriorityDao.TABLE, toContentValuesMapper.map(smPriority), sbWhere.toString(), null).toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(SmPriorityDao.TABLE, null, toContentValuesMapper.map(smPriority))
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
    private fun getWherePkClause(smPriority: SmPriority?): StringBuilder {
        smPriority?.let {
            return java.lang.StringBuilder()
                .append("""
                        $CUSTOMER_CODE = '${smPriority.customer_code}'  
                        AND $PRIORITY_CODE = '${smPriority.priority_code}'
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    override fun addUpdate(smPrioritys: MutableList<SmPriority>?, status: Boolean): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()

        try {
            daoObjReturn.table = SmPriorityDao.TABLE
            //
            db.beginTransaction()

            if (status) {
                db.delete(SmPriorityDao.TABLE, null, null)
            }
            //
            smPrioritys?.forEach { smPriority ->
                curAction = DaoObjReturn.UPDATE
                //Where para update
                val sbWhere: StringBuilder = getWherePkClause(smPriority)
                //Tenta update e armazena retorno
                addUpdateRet = db.update(SmPriorityDao.TABLE, toContentValuesMapper.map(smPriority), sbWhere.toString(), null).toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(SmPriorityDao.TABLE, null, toContentValuesMapper.map(smPriority))
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
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
    }

    override fun remove(sQuery: String?) {
        openDB()
        try {
            db.execSQL(sQuery)
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
    }

    fun getByString(sQuery: String?, dbInstance: SQLiteDatabase?) : SmPriority?{
        var smPriority: SmPriority? = null
        if(dbInstance == null) {
            openDB()
        } else{
            this.db = dbInstance
        }
        lateinit var cursor: Cursor
        try {
            cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                smPriority = toSmPriorityMapper.map(cursor)
            }
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
            cursor.close()
        }
        //
        if(dbInstance == null) {
            closeDB()
        }
        //
        return smPriority
    }

    override fun getByString(sQuery: String?) = getByString(sQuery,null)


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

    override fun query(sQuery: String?): MutableList<SmPriority> {
        var smPrioritys = mutableListOf<SmPriority>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                val uAux: SmPriority = toSmPriorityMapper.map(cursor)
                smPrioritys.add(uAux)
            }
            //
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        //
        closeDB()
        return smPrioritys
    }

    fun getMdTagByPk(customer_code:Int, tag_code: Int): SmPriority? = this.getByString(MdTagSql001(customer_code,tag_code).toSqlQuery())

    override fun query_HM(sQuery: String?): MutableList<HMAux> {
        val smPrioritys: MutableList<HMAux> = ArrayList()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                smPrioritys.add(CursorToHMAuxMapper.mapN(cursor))
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return smPrioritys
    }

    //
    class CursorToSmPriorityMapper : Mapper<Cursor, SmPriority> {
        override fun map(cursor: Cursor?): SmPriority? {
            cursor?.let {
                with(cursor) {
                    return SmPriority(
                        customer_code = getLong(getColumnIndex(CUSTOMER_CODE)),
                        priority_code = getInt(getColumnIndex(PRIORITY_CODE)),
                        priority_desc = getString(getColumnIndex(PRIORITY_DESC)),
                        priority_weight = getInt(getColumnIndex(PRIORITY_WEIGHT)),
                        priority_default = getInt(getColumnIndex(PRIORITY_DEFAULT)),
                        priority_color = getString(getColumnIndex(PRIORITY_COLOR)),
                    )
                }
            }
            //
            return null
        }
    }

    private class SmPriorityToContentValuesMapper :
        Mapper<SmPriority, ContentValues> {
        override fun map(smPriority: SmPriority?): ContentValues {
            val contentValues = ContentValues()
            //
            smPriority?.let {
                with(contentValues) {
                    if (smPriority.customer_code > -1) {
                        put(
                            CUSTOMER_CODE,
                            smPriority.customer_code
                        )
                    }
                    //
                    if (smPriority.priority_code > -1) {
                        put(
                            PRIORITY_CODE,
                            smPriority.priority_code
                        )
                    }
                    //
                    put(
                        PRIORITY_DESC,
                        smPriority.priority_desc
                    )
                    //
                    if (smPriority.priority_weight > -1) {
                        put(
                            PRIORITY_WEIGHT,
                            smPriority.priority_weight
                        )
                    }
                    //
                    put(
                        PRIORITY_DEFAULT,
                        smPriority.priority_default
                    )
                    //
                    put(
                        PRIORITY_COLOR,
                        smPriority.priority_color
                    )
                    //
                }
            }
            //
            return contentValues
        }
    }
}