package com.namoadigital.prj001.dao.trip

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import androidx.core.database.getStringOrNull
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.BaseDao
import com.namoadigital.prj001.dao.DaoWithReturn
import com.namoadigital.prj001.database.CursorToHMAuxMapper
import com.namoadigital.prj001.database.Mapper
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.trip.FSTripUser
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class FSTripUserDao(
    val context: Context,
    mDB_NAME: String = ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
    mDB_VERSION: Int = Constant.DB_VERSION_CUSTOM
) : BaseDao(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI), DaoWithReturn<FSTripUser> {
    private val toFSTripUser: Mapper<Cursor, FSTripUser>
    private val toContentValuesMapper: Mapper<FSTripUser, ContentValues>

    init {
        toFSTripUser = CursorToFSTripUser()
        toContentValuesMapper = FSTripUserToContentValuesMapper()
    }

    override fun addUpdate(fsTripUser: FSTripUser?): DaoObjReturn {
        return addUpdate(fsTripUser, null)
    }

    override fun addUpdate(items: MutableList<FSTripUser>?, status: Boolean): DaoObjReturn {
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
            items?.forEach { fsTripUser ->
                curAction = DaoObjReturn.UPDATE
                //Where para update
                val sbWhere: StringBuilder = getWherePkClause(fsTripUser)
                //Tenta update e armazena retorno
                addUpdateRet = db.update(
                    TABLE,
                    toContentValuesMapper.map(fsTripUser),
                    sbWhere.toString(),
                    null
                ).toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(
                        TABLE,
                        null,
                        toContentValuesMapper.map(fsTripUser)
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
            ToolBox_Inf.registerException(javaClass.name, e)
        }
        closeDB()
    }

    override fun remove(sQuery: String?) {
        remove(sQuery, null)
    }

    fun remove(sQuery: String?, dbInstance: SQLiteDatabase?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        val curAction = DaoObjReturn.DELETE
        if (dbInstance == null) {
            openDB()
        } else {
            db = dbInstance
        }

        if (dbInstance == null) {
            db.beginTransaction()
        }

        try {
            db.execSQL(sQuery)
            if (dbInstance == null) {
                db.setTransactionSuccessful()
            }
        } catch (e: java.lang.Exception) {
            daoObjReturn = ToolBox_Con.getSQLiteErrorCodeDescription(e.message)
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
            if (dbInstance == null) {
                db.endTransaction()
            }
            daoObjReturn.action = curAction
        }
        if (dbInstance == null) {
            closeDB()
        }
        return daoObjReturn
    }

    override fun getByString(sQuery: String?) = getByString(sQuery, null)

    fun getByString(sQuery: String?, dbInstance: SQLiteDatabase?): FSTripUser? {
        var fsTripUser: FSTripUser? = null
        if (dbInstance == null) {
            openDB()
        } else {
            this.db = dbInstance
        }
        lateinit var cursor: Cursor
        try {
            cursor = db.rawQuery(sQuery!!, null)
            while (cursor.moveToNext()) {
                fsTripUser = toFSTripUser.map(cursor)
            }
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
            cursor.close()
        }
        //
        if (dbInstance == null) {
            closeDB()
        }
        //
        return fsTripUser
    }

    override fun getByStringHM(sQuery: String?): HMAux? {
        var hmAux: HMAux? = null
        openDB()

        try {
            val cursor = db.rawQuery(sQuery!!, null)
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

    override fun query(sQuery: String?): MutableList<FSTripUser> {
        var items = mutableListOf<FSTripUser>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery!!, null)
            while (cursor.moveToNext()) {
                val uAux: FSTripUser = toFSTripUser.map(cursor)
                items.add(uAux)
            }
            //
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        //
        closeDB()
        return items
    }

    private fun queryForFullUpdate(sQuery: String?): MutableList<FSTripUser>? {
        var items = mutableListOf<FSTripUser>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery!!, null)
            while (cursor.moveToNext()) {
                val uAux: FSTripUser = toFSTripUser.map(cursor)
                items.add(uAux)
            }
            //
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
            return null
        } finally {
        }
        //
        closeDB()
        return items
    }

    override fun query_HM(sQuery: String?): MutableList<HMAux> {
        val items: MutableList<HMAux> = ArrayList()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery!!, null)
            while (cursor.moveToNext()) {
                items.add(CursorToHMAuxMapper.mapN(cursor))
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return items
    }

    @Throws(java.lang.Exception::class)
    private fun getWherePkClause(fsTripUser: FSTripUser?): StringBuilder {
        fsTripUser?.let {
            return java.lang.StringBuilder()
                .append(
                    """
                            $CUSTOMER_CODE = '${it.customerCode}'  
                        AND $TRIP_PREFIX = '${it.tripPrefix}'
                        AND $TRIP_CODE = '${it.tripCode}'
                        AND $USER_CODE = '${it.userCode}'
                        AND $USER_SEQ = '${it.userSeq}'
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    fun addUpdate(fsTripUser: FSTripUser?, dbInstance: SQLiteDatabase?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        if (dbInstance == null) {
            openDB()
        } else {
            db = dbInstance
        }
        //
        try {
            daoObjReturn.table = TABLE
            curAction = DaoObjReturn.UPDATE
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(fsTripUser)
            //Tenta update e armazena retorno
            addUpdateRet = db.update(
                TABLE,
                toContentValuesMapper.map(fsTripUser),
                sbWhere.toString(),
                null
            ).toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(
                    TABLE,
                    null,
                    toContentValuesMapper.map(fsTripUser)
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
        if (dbInstance == null) {
            closeDB()
        }
        //
        return daoObjReturn
    }


//    --------------- [QUERY] ---------------

    fun listAllUsers(
        tripPrefix: Int,
        tripCode: Int,
    ): List<FSTripUser> {

        val value = query(
            """
            SELECT * FROM $TABLE
            WHERE $TRIP_PREFIX = '$tripPrefix' 
            AND $TRIP_CODE = '$tripCode'
            ORDER BY $DATE_START ASC
        """.trimIndent()
        )

        return if (value.isEmpty()) emptyList() else value
    }

    fun getAllUsersForFullUpdate(
        tripPrefix: Int,
        tripCode: Int,
    ): List<FSTripUser>? {

        return queryForFullUpdate(
            """
            SELECT * FROM $TABLE
            WHERE $TRIP_PREFIX = '$tripPrefix' 
            AND $TRIP_CODE = '$tripCode'
            ORDER BY $DATE_START ASC
        """.trimIndent()
        )


    }

    @Throws(SQLiteException::class)
    fun removeUser(
        tripPrefix: Int,
        tripCode: Int,
        seq: Int,
        db: SQLiteDatabase? = null
    ) {
        remove(
            """
            DELETE FROM $TABLE
            WHERE $TRIP_PREFIX = '$tripPrefix'
            AND $TRIP_CODE = '$tripCode'
            AND $USER_SEQ = '$seq'
            
        """.trimIndent(), db
        ).let {
            if (it.hasError()) throw SQLiteException(it.errorMsg)
        }
    }

    @Throws(SQLiteException::class)
    fun updateUser(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        userCode: Int,
        userSeq: Int,
        userName: String,
        dateStart: String,
        dateEnd: String,
        db: SQLiteDatabase? = null
    ) {
        addUpdate(
            FSTripUser(
                customerCode = customerCode,
                tripPrefix = tripPrefix,
                tripCode = tripCode,
                userCode = userCode,
                userSeq = userSeq,
                userName = userName,
                dateStart = dateStart,
                dateEnd = dateEnd
            ), db
        ).let { if (it.hasError()) throw SQLiteException(it.errorMsg) }
    }

    fun getByCode(code: Int): FSTripUser? {
        return getByString("SELECT * FROM $TABLE WHERE $USER_CODE = $code")
    }

    fun getListUserByCode(code: Int): List<FSTripUser> {
        return query("SELECT * FROM $TABLE WHERE $USER_CODE = $code")
    }

    fun getListUserByTrip(
        tripPrefix: Int,
        tripCode: Int
    ): List<FSTripUser> {
        return query(
            "SELECT * FROM $TABLE " +
                    "WHERE $TRIP_PREFIX = $tripPrefix " +
                    "AND $TRIP_CODE = $tripCode"
        )
    }

    fun getListUserByCodeInTrip(
        tripPrefix: Int,
        tripCode: Int,
        code: Int
    ): List<FSTripUser> {
        return query(
            """
            SELECT * FROM $TABLE
             WHERE $TRIP_PREFIX = $tripPrefix
             AND $TRIP_CODE = $tripCode
             AND $USER_CODE = $code
        """.trimIndent()
        )
    }

    fun getNextUserSeq(tripPrefix: Int, tripCode: Int): Int {
        val value = getByStringHM(
            """
            SELECT ifnull(MAX($USER_SEQ), 0) + 1 AS $USER_SEQ FROM $TABLE
            WHERE $TRIP_PREFIX = $tripPrefix
            AND $TRIP_CODE = $tripCode
        """.trimIndent()
        )

        value?.let {
            return if (value.hasConsistentValue(USER_SEQ)) value[USER_SEQ]!!.toInt() else 1
        }
        return 1
    }

    @Throws(Exception::class)
    fun updateDoneUsers(endDate: String, db: SQLiteDatabase) {
        val sql = """
        UPDATE $TABLE
        SET $DATE_END = '$endDate'
        WHERE $DATE_END IS NULL
        """.trimIndent()

        db.execSQL(sql)
    }

    class CursorToFSTripUser : Mapper<Cursor, FSTripUser> {
        @SuppressLint("Range")
        override fun map(from: Cursor?): FSTripUser? {
            from?.let { cursor ->
                with(cursor) {
                    return FSTripUser(
                        customerCode = getLong(getColumnIndex(CUSTOMER_CODE)),
                        tripPrefix = getInt(getColumnIndex(TRIP_PREFIX)),
                        tripCode = getInt(getColumnIndex(TRIP_CODE)),
                        userCode = getInt(getColumnIndex(USER_CODE)),
                        userSeq = getInt(getColumnIndex(USER_SEQ)),
                        userName = getString(getColumnIndex(USER_NAME)),
                        dateStart = getString(getColumnIndex(DATE_START)),
                        dateEnd = getStringOrNull(getColumnIndex(DATE_END)),
                    )
                }
            }
            return null
        }
    }

    class FSTripUserToContentValuesMapper : Mapper<FSTripUser, ContentValues> {
        override fun map(from: FSTripUser?): ContentValues {
            val contentValues = ContentValues()

            from?.let { data ->
                with(contentValues) {
                    if (data.customerCode > -1) put(CUSTOMER_CODE, data.customerCode)
                    if (data.tripPrefix > -1) put(TRIP_PREFIX, data.tripPrefix)
                    if (data.tripCode > -1) put(TRIP_CODE, data.tripCode)
                    if (data.userCode > -1) put(USER_CODE, data.userCode)
                    if (data.userSeq > -1) put(USER_SEQ, data.userSeq)
                    if (data.userName != null) put(USER_NAME, data.userName)
                    if (data.dateStart != null) put(DATE_START, data.dateStart)
                    put(DATE_END, data.dateEnd)
                }
            }

            return contentValues
        }

    }

    companion object {

        const val TABLE = "fs_trip_user"
        const val CUSTOMER_CODE = "customer_code"
        const val TRIP_PREFIX = "trip_prefix"
        const val TRIP_CODE = "trip_code"
        const val USER_CODE = "user_code"
        const val USER_SEQ = "user_seq"
        const val USER_NICK = "user_nick"
        const val USER_NAME = "user_name"
        const val DATE_START = "date_start"
        const val DATE_END = "date_end"
        fun instance(context: Context) = FSTripUserDao(context)
    }
}