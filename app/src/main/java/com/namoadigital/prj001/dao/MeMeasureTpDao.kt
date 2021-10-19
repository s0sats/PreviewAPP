package com.namoadigital.prj001.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.database.CursorToHMAuxMapper
import com.namoadigital.prj001.database.Mapper
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.MeMeasureTp
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class MeMeasureTpDao(
    val context: Context,
    val mDB_NAME: String,
    val mDB_VERSION: Int
) : BaseDao(
    context, mDB_NAME, mDB_VERSION, ConstantBaseApp.DB_MODE_MULTI
), DaoWithReturn<MeMeasureTp> {


    companion object{
        const val TABLE = "me_measure_tp"
        const val CUSTOMER_CODE = "customer_code"
        const val MEASURE_TP_CODE = "measure_tp_code"
        const val MEASURE_TP_ID = "measure_tp_id"
        const val MEASURE_TP_DESC = "measure_tp_desc"
        const val VALUE_SUFIX = "value_sufix"
        const val RESTRICTION_TYPE = "restriction_type"
        const val RESTRICTION_MIN = "restriction_min"
        const val RESTRICTION_MAX = "restriction_max"
        const val RESTRICTION_DECIMAL = "restriction_decimal"
        const val VALUE_CYCLE_SIZE = "value_cycle_size"
        const val CYCLE_TOLERANCE = "cycle_tolerance"

    }

    private val toMeMeasureTpMapper: Mapper<Cursor, MeMeasureTp>
    private val toContentValuesMapper: Mapper<MeMeasureTp, ContentValues>

    init {
        toMeMeasureTpMapper = CursorToMeMeasureTpMapper()
        toContentValuesMapper = MeMeasureTpToContentValuesMapper()
    }

    override fun addUpdate(item: MeMeasureTp?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()

        try {
            daoObjReturn.table = MeMeasureTpDao.TABLE
            curAction = DaoObjReturn.UPDATE
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(item)
            //Tenta update e armazena retorno
            addUpdateRet = db.update(MeMeasureTpDao.TABLE, toContentValuesMapper.map(item), sbWhere.toString(), null).toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(MeMeasureTpDao.TABLE, null, toContentValuesMapper.map(item))
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

    private fun getWherePkClause(item: MeMeasureTp?): StringBuilder {
        item?.let{
            return java.lang.StringBuilder()
                .append("""
                        ${CUSTOMER_CODE} = '${item.customerCode}'  
                        AND ${MEASURE_TP_CODE} = '${item.measureTpCode}'                           
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    override fun addUpdate(items: MutableList<MeMeasureTp>?, status: Boolean): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()

        try {
            daoObjReturn.table = MeMeasureTpDao.TABLE
            curAction = DaoObjReturn.UPDATE

            db.beginTransaction()

            if (status) {
                db.delete(MeMeasureTpDao.TABLE, null, null)
            }

            items?.forEach { item ->
                val sbWhere: StringBuilder = getWherePkClause(item)
                //Tenta update e armazena retorno
                addUpdateRet = db.update(MeMeasureTpDao.TABLE, toContentValuesMapper.map(item), sbWhere.toString(), null).toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(MeMeasureTpDao.TABLE, null, toContentValuesMapper.map(item))
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

    override fun getByString(sQuery: String?): MeMeasureTp? {
        var item: MeMeasureTp? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                item = toMeMeasureTpMapper.map(cursor)
            }
            //
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return item
    }

    override fun getByStringHM(sQuery: String?): HMAux? {
        var item: HMAux? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                item = CursorToHMAuxMapper.mapN(cursor)
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return  item
    }

    override fun query(sQuery: String?): MutableList<MeMeasureTp> {
        val items = mutableListOf<MeMeasureTp>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                val uAux = toMeMeasureTpMapper.map(cursor)
                items.add(uAux)
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return items
    }

    override fun query_HM(sQuery: String?): MutableList<HMAux> {
        val items = mutableListOf<HMAux>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
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

    inner class MeMeasureTpToContentValuesMapper : Mapper<MeMeasureTp, ContentValues> {
        override fun map(meMeasureTp: MeMeasureTp?): ContentValues {
            val contentValues = ContentValues()
            meMeasureTp?.let {
                with(contentValues) {
                    if (meMeasureTp.customerCode > -1) {
                        put(CUSTOMER_CODE, meMeasureTp.customerCode)
                    }
                    if (meMeasureTp.measureTpCode > -1) {
                        put(MEASURE_TP_CODE, meMeasureTp.measureTpCode)
                    }
                    put(MEASURE_TP_ID, meMeasureTp.measureTpId)
                    put(MEASURE_TP_DESC, meMeasureTp.measureTpDesc)
                    put(VALUE_SUFIX, meMeasureTp.valueSufix)
                    put(RESTRICTION_TYPE, meMeasureTp.restrictionType)
                    put(RESTRICTION_MIN, meMeasureTp.restrictionMin)
                    put(RESTRICTION_MAX, meMeasureTp.restrictionMax)
                    put(RESTRICTION_DECIMAL, meMeasureTp.restrictionDecimal)
                    put(VALUE_CYCLE_SIZE, meMeasureTp.valueCycleSize)
                    put(CYCLE_TOLERANCE, meMeasureTp.cycleTolerance)
                }
            }
            return contentValues
        }

    }

    inner class CursorToMeMeasureTpMapper : Mapper<Cursor, MeMeasureTp> {
        override fun map(cursor: Cursor?): MeMeasureTp? {
            cursor?.let {
                with(cursor) {
                    return MeMeasureTp(
                        getLong(getColumnIndex(CUSTOMER_CODE)),
                        getInt(getColumnIndex(MEASURE_TP_CODE)),
                        getString(getColumnIndex(MEASURE_TP_ID)),
                        getString(getColumnIndex(MEASURE_TP_DESC)),
                        getStringOrNull(getColumnIndex(VALUE_SUFIX)),
                        getStringOrNull(getColumnIndex(RESTRICTION_TYPE)),
                        getIntOrNull(getColumnIndex(RESTRICTION_MIN)),
                        getIntOrNull(getColumnIndex(RESTRICTION_MAX)),
                        getIntOrNull(getColumnIndex(RESTRICTION_DECIMAL)),
                        getIntOrNull(getColumnIndex(VALUE_CYCLE_SIZE)),
                        getIntOrNull(getColumnIndex(CYCLE_TOLERANCE))
                    )
                }
            }
            //
            return null
        }
    }
}