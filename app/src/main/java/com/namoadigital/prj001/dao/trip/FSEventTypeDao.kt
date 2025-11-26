package com.namoadigital.prj001.dao.trip

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import androidx.core.database.getIntOrNull
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.BaseDao
import com.namoadigital.prj001.dao.DaoWithReturn
import com.namoadigital.prj001.database.CursorToHMAuxMapper
import com.namoadigital.prj001.database.Mapper
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.trip.FSEventType
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class FSEventTypeDao(
    val context: Context,
    mDB_NAME: String = ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
    mDB_VERSION: Int = Constant.DB_VERSION_CUSTOM
) : BaseDao(
    context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI
), DaoWithReturn<FSEventType> {

    companion object {
        const val TABLE = "fs_event_type"
        const val CUSTOMER_CODE = "customer_code"
        const val EVENT_TYPE_CODE = "event_type_code"
        const val EVENT_TYPE_DESC = "event_type_desc"
        const val CONF_COST = "conf_cost"
        const val CONF_COMMENTS = "conf_comments"
        const val CONF_PHOTO = "conf_photo"
        const val WAIT_ALLOWED = "wait_allowed"
        const val WAIT_MAX_MINUTES = "wait_max_minutes"
    }

    private val toCursorToFSTripEventTypeMapperMapper: Mapper<Cursor, FSEventType>
    private val toContentValuesMapper: Mapper<FSEventType, ContentValues>

    init {
        toCursorToFSTripEventTypeMapperMapper = CursorToFSEventTypeMapper()
        toContentValuesMapper = FSEventTypeToContentValuesMapper()
    }

    override fun addUpdate(fsTripEventType: FSEventType?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()

        try {
            daoObjReturn.table = TABLE
            curAction = DaoObjReturn.UPDATE
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(fsTripEventType)
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(fsTripEventType), sbWhere.toString(), null).toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(fsTripEventType))
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
    private fun getWherePkClause(fsTripEventType: FSEventType?): StringBuilder {
        fsTripEventType?.let{
            return java.lang.StringBuilder()
                .append("""
                        $CUSTOMER_CODE = '${fsTripEventType.customerCode}'  
                        AND $EVENT_TYPE_CODE = '${fsTripEventType.eventTypeCode}'
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    override fun addUpdate(fsTripEventTypes: MutableList<FSEventType>?, status: Boolean): DaoObjReturn {
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

            fsTripEventTypes?.forEach { fsEventType->
                //Where para update
                val sbWhere: StringBuilder = getWherePkClause(fsEventType)
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(fsEventType), sbWhere.toString(), null).toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(fsEventType))
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

    override fun getByString(sQuery: String): FSEventType? {
        var fsTripEventType: FSEventType? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                fsTripEventType = toCursorToFSTripEventTypeMapperMapper.map(cursor)
            }
            //
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return fsTripEventType
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

    override fun query(sQuery: String?): MutableList<FSEventType> {
        val fsTripEventTypes = mutableListOf<FSEventType>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery!!, null)
            while (cursor.moveToNext()) {
                val uAux = toCursorToFSTripEventTypeMapperMapper.map(cursor)
                fsTripEventTypes.add(uAux)
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return fsTripEventTypes
    }

    override fun query_HM(sQuery: String?): MutableList<HMAux> {
        val fsEventTypes: MutableList<HMAux> = ArrayList()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery!!, null)
            while (cursor.moveToNext()) {
                fsEventTypes.add(CursorToHMAuxMapper.mapN(cursor))
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return fsEventTypes
    }

    fun getListEventType(customerCode: Long): List<FSEventType> {
        val value = query("""
            SELECT * FROM $TABLE
            WHERE $CUSTOMER_CODE = '$customerCode'
            ORDER BY $EVENT_TYPE_DESC ASC
        """.trimIndent())
        return value.ifEmpty { emptyList<FSEventType>() }
    }


    fun getEventTypeByCode(
        customerCode: Long,
        eventTypeCode: Int
    ): FSEventType? {
        return query("""
            SELECT * FROM $TABLE
            WHERE $CUSTOMER_CODE = '$customerCode'
            AND $EVENT_TYPE_CODE = '$eventTypeCode'
        """.trimIndent()).firstOrNull()
    }


    fun getEventType(typeCode: Int): FSEventType? {
        return getByString(
            """
            SELECT * FROM $TABLE
            WHERE $EVENT_TYPE_CODE =  '$typeCode'
        """.trimIndent()
        )
    }


    private inner class FSEventTypeToContentValuesMapper :
        Mapper<FSEventType, ContentValues> {
        override fun map(fsTripEventType: FSEventType?): ContentValues {
            val contentValues = ContentValues()
            //
            fsTripEventType?.let{
                with(contentValues){
                    if(it.customerCode > -1){
                        put(CUSTOMER_CODE, it.customerCode)
                    }
                    if(it.eventTypeCode > -1){
                        put(EVENT_TYPE_CODE,it.eventTypeCode)
                    }
                    put(EVENT_TYPE_DESC, it.eventTypeDesc)
                    put(CONF_COST, it.confCost)
                    put(CONF_COMMENTS, it.confComments)
                    put(CONF_PHOTO, it.confPhoto)
                    if(it.waitAllowed > -1) {
                        put(WAIT_ALLOWED, it.waitAllowed)
                    }
                    put(WAIT_MAX_MINUTES, it.waitMaxMinutes)

                }
            }
            //
            return contentValues
        }
    }

    private inner class CursorToFSEventTypeMapper : Mapper<Cursor, FSEventType> {
        override fun map(cursor: Cursor?): FSEventType? {
            cursor?.let {
                with(cursor) {
                    return FSEventType(
                        customerCode = getLong(getColumnIndex(CUSTOMER_CODE)),
                        eventTypeCode = getInt(getColumnIndex(EVENT_TYPE_CODE)),
                        eventTypeDesc = getString(getColumnIndex(EVENT_TYPE_DESC)),
                        confCost = getString(getColumnIndex(CONF_COST)),
                        confComments = getString(getColumnIndex(CONF_COMMENTS)),
                        confPhoto = getString(getColumnIndex(CONF_PHOTO)),
                        waitAllowed = getInt(getColumnIndex(WAIT_ALLOWED)),
                        waitMaxMinutes = getIntOrNull(getColumnIndex(WAIT_MAX_MINUTES)),
                    )
                }
            }
            //
            return null
        }
    }
}