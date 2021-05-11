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
import com.namoadigital.prj001.model.MdTag
import com.namoadigital.prj001.sql.MdTagSql001
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.util.*

class MdTagDao(
        private val context: Context,
        private val mDB_NAME: String,
        private val mDB_VERSION: Int
) : BaseDao(
        context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI
), DaoWithReturn<MdTag> {

    companion object {
        const val TABLE = "md_tag"
        const val CUSTOMER_CODE = "customer_code"
        const val TAG_CODE = "tag_code"
        const val TAG_ID = "tag_id"
        const val TAG_DESC = "tag_desc"
    }

    private val toMdTagMapper: Mapper<Cursor, MdTag>
    private val toContentValuesMapper: Mapper<MdTag, ContentValues>

    init {

        toMdTagMapper = CursorToMdTagMapper()
        toContentValuesMapper = MdTagToContentValuesMapper()

    }

    override fun addUpdate(mdTag: MdTag?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()

        try {
            daoObjReturn.table = TABLE
            curAction = DaoObjReturn.UPDATE
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(mdTag)
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(mdTag), sbWhere.toString(), null).toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(mdTag))
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
    private fun getWherePkClause(mdTag: MdTag?): StringBuilder {
        mdTag?.let {
            return java.lang.StringBuilder()
                    .append("""
                        $CUSTOMER_CODE = '${mdTag.customer_code}'  
                        AND ${TAG_CODE} = '${mdTag.tag_code}'
                        """.trimIndent()
                    )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    override fun addUpdate(mdTags: MutableList<MdTag>?, status: Boolean): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()

        try {
            daoObjReturn.table = TABLE
            //
            db.beginTransaction()

            if (status) {
                db.delete(TABLE, null, null)
            }
            //
            mdTags?.forEach { mdTag ->
                curAction = DaoObjReturn.UPDATE
                //Where para update
                val sbWhere: StringBuilder = getWherePkClause(mdTag)
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(mdTag), sbWhere.toString(), null).toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(mdTag))
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

    fun getByString(sQuery: String?, dbInstance: SQLiteDatabase?) : MdTag?{
        var mdTag: MdTag? = null
        if(dbInstance == null) {
            openDB()
        } else{
            this.db = dbInstance
        }
        lateinit var cursor: Cursor
        try {
            cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                mdTag = toMdTagMapper.map(cursor)
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
        return mdTag
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

    override fun query(sQuery: String?): MutableList<MdTag> {
        var mdTags = mutableListOf<MdTag>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                val uAux: MdTag = toMdTagMapper.map(cursor)
                mdTags.add(uAux)
            }
            //
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        //
        closeDB()
        return mdTags
    }

    fun getMdTagByPk(customer_code:Int, tag_code: Int): MdTag? = this.getByString(MdTagSql001(customer_code,tag_code).toSqlQuery())

    override fun query_HM(sQuery: String?): MutableList<HMAux> {
        val mdTags: MutableList<HMAux> = ArrayList()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                mdTags.add(CursorToHMAuxMapper.mapN(cursor))
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return mdTags
    }

    private class MdTagToContentValuesMapper : Mapper<MdTag, ContentValues> {
        override fun map(mdTag: MdTag?): ContentValues {
            val contentValues = ContentValues()
            mdTag?.let {
                with(contentValues) {
                    if (mdTag.customer_code > -1) {
                        put(CUSTOMER_CODE, mdTag.customer_code)
                    }
                    if (mdTag.tag_code > -1) {
                        put(TAG_CODE, mdTag.tag_code)
                    }
                    put(TAG_ID, mdTag.tag_id)
                    put(TAG_DESC, mdTag.tag_desc)
                }
            }
            return contentValues
        }

    }

    private class CursorToMdTagMapper : Mapper<Cursor, MdTag> {
        override fun map(cursor: Cursor?): MdTag? {
            cursor?.let {
                with(cursor) {
                    return MdTag(
                            getInt(getColumnIndex(CUSTOMER_CODE)),
                            getInt(getColumnIndex(TAG_CODE)),
                            getString(getColumnIndex(TAG_ID)),
                            getString(getColumnIndex(TAG_DESC)),
                    )
                }
            }
            //
            return null
        }
    }
}