package com.namoadigital.prj001.dao.util

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.BaseDao
import com.namoadigital.prj001.dao.DaoWithReturn
import com.namoadigital.prj001.dao.DaoWithReturnSharedDbInstance
import com.namoadigital.prj001.database.CursorToHMAuxMapper
import com.namoadigital.prj001.database.Mapper
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

abstract class BaseDaoWithReturn<T>(
    context: Context,
    private val tableName: String,
    mDBName: String,
    mDBVersion: Int,
    mDBMode: String
) : BaseDao(context, mDBName, mDBVersion, mDBMode),
    DaoWithReturn<T>,
    DaoWithReturnSharedDbInstance<T> {

    private val toCursorToModelMapper: Mapper<Cursor, T>
        get() = CursorToModelMapper()
    private val toContentValuesMapper: Mapper<T, ContentValues>
        get() = ModelToContentValuesMapper()


    @SuppressLint("Range")
    abstract fun cursorToModel(cursor: Cursor): T?

    abstract fun modelToContentValues(model: T?, contentValues: ContentValues): ContentValues

    protected abstract fun getWherePkClause(item: T?): StringBuilder

    override fun addUpdate(item: T?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()

        try {
            daoObjReturn.table = tableName
            curAction = DaoObjReturn.UPDATE
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(item)
            //Tenta update e armazena retorno
            addUpdateRet =
                db.update(tableName, toContentValuesMapper.map(item), sbWhere.toString(), null)
                    .toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(tableName, null, toContentValuesMapper.map(item))
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


    override fun addUpdate(items: MutableList<T>?, status: Boolean): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()

        try {
            daoObjReturn.table = tableName
            curAction = DaoObjReturn.UPDATE

            db.beginTransaction()

            if (status) {
                db.delete(tableName, null, null)
            }

            items?.forEach { item ->
                val sbWhere: StringBuilder = getWherePkClause(item)
                //Tenta update e armazena retorno
                addUpdateRet =
                    db.update(tableName, toContentValuesMapper.map(item), sbWhere.toString(), null)
                        .toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(tableName, null, toContentValuesMapper.map(item))
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


    private fun addUpdate(sQuery: String?, dbInstance: SQLiteDatabase? = null): DaoObjReturn {
        val daoObjReturn = DaoObjReturn()
        if (dbInstance == null) {
            openDB()
        } else {
            db = dbInstance
        }
        try {
            db.execSQL(sQuery)
            if (dbInstance == null) {
                db.setTransactionSuccessful()
            }
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e);
            daoObjReturn.setError(true)
        } finally {
            if (dbInstance == null) {
                db.endTransaction()
            }
        }

        if (dbInstance == null) {
            closeDB()
        }

        return daoObjReturn
    }

    override fun addUpdate(sQuery: String?) {
        addUpdate(sQuery, null)
    }


    override fun getByString(sQuery: String): T? {
        var model: T? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                model = toCursorToModelMapper.map(cursor)
            }
            //
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return model
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

    override fun addUpdate(item: T, dbInstance: SQLiteDatabase?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        if (dbInstance == null) {
            openDB()
        } else {
            db = dbInstance
        }

        try {
            daoObjReturn.table = tableName
            curAction = DaoObjReturn.UPDATE

            if (dbInstance == null) {
                db.beginTransaction()
            }

            val sbWhere: StringBuilder = getWherePkClause(item)
            //Tenta update e armazena retorno
            addUpdateRet =
                db.update(tableName, toContentValuesMapper.map(item), sbWhere.toString(), null)
                    .toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(tableName, null, toContentValuesMapper.map(item))
            }
            //
            if (dbInstance == null) {
                db.setTransactionSuccessful()
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
            //Atualiza ação realizada no metodo e informação de qtd de registros alterado (update)
            //ou rowId do ultimo insert.
            if (dbInstance == null) {
                db.endTransaction()
            }
            daoObjReturn.action = curAction
            daoObjReturn.actionReturn = addUpdateRet
        }
        if(dbInstance == null){
            closeDB()
        }
        //
        return daoObjReturn
    }

    override fun addUpdate(
        items: MutableList<T>?,
        status: Boolean,
        dbInstance: SQLiteDatabase?
    ): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        if (dbInstance == null) {
            openDB()
        } else {
            db = dbInstance
        }

        try {
            daoObjReturn.table = tableName
            curAction = DaoObjReturn.UPDATE
            //Se db não foi passado, inicializa transaction
            if (dbInstance == null) {
                db.beginTransaction()
            }
            if (status) {
                db.delete(tableName, null, null)
            }

            items?.forEach { item ->
                //Where para update
                val sbWhere: StringBuilder = getWherePkClause(item)
                //Tenta update e armazena retorno
                addUpdateRet =
                    db.update(tableName, toContentValuesMapper.map(item), sbWhere.toString(), null)
                        .toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(tableName, null, toContentValuesMapper.map(item))
                }
            }
            //
            if (dbInstance == null) {
                db.setTransactionSuccessful()
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
            //Atualiza ação realizada no metodo e informação de qtd de registros alterado (update)
            //ou rowId do ultimo insert.
            if (dbInstance == null) {
                db.endTransaction()
            }
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

    override fun remove(item: T, dbInstance: SQLiteDatabase?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var sqlRet: Long = 0
        val curAction = DaoObjReturn.DELETE
        //
        if (dbInstance == null) {
            openDB()
        } else {
            db = dbInstance
        }

        try {
            daoObjReturn.table = tableName
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(item)
            sqlRet = db.delete(tableName, sbWhere.toString(), null).toLong()
        } catch (e: SQLiteException) {
            //Chama metodo que baseado na exception gera obj de retorno setado como erro
            //e contendo msg de erro tratada.
            daoObjReturn = ToolBox_Con.getSQLiteErrorCodeDescription(e.message)
            //
            ToolBox_Inf.registerException(
                javaClass.name,
                java.lang.Exception(
                    """
            ${e.message}
            ${daoObjReturn.errorMsg}
            """.trimIndent()
                )
            )
        } catch (e: java.lang.Exception) {
            //Seta obj de retorno com flag de erro e gera arquivo de exception
            daoObjReturn.setError(true)
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
            //Atualiza ação realizada no metodo e informação de qtd de registros alterados.
            daoObjReturn.action = curAction
            daoObjReturn.actionReturn = sqlRet
        }
        //
        if (dbInstance == null) {
            closeDB()
        }
        return daoObjReturn
    }

    override fun query(sQuery: String?): MutableList<T> {
        val model = mutableListOf<T>()
        openDB()

        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                val uAux = toCursorToModelMapper.map(cursor)
                model.add(uAux)
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return model
    }

    override fun query_HM(sQuery: String?): MutableList<HMAux> {
        val model: MutableList<HMAux> = ArrayList()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                model.add(CursorToHMAuxMapper.mapN(cursor))
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return model
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
            ToolBox_Inf.registerException(javaClass.name, e);
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

    override fun remove(sQuery: String?) {
        remove(sQuery, null)
    }

    fun deleteAll() {
        return remove("DELETE FROM $tableName")
    }

    fun deleteAll(dbInstance: SQLiteDatabase?): DaoObjReturn {
        return remove("DELETE FROM $tableName", dbInstance)
    }

    protected inner class ModelToContentValuesMapper : Mapper<T, ContentValues> {
        override fun map(from: T?): ContentValues {
            return modelToContentValues(from, ContentValues())
        }

    }

    protected inner class CursorToModelMapper : Mapper<Cursor, T> {
        @SuppressLint("Range")
        override fun map(cursor: Cursor?): T? {
            cursor?.let {
                return cursorToModel(cursor)
            }
            return null
        }
    }
}