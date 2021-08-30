package com.namoadigital.prj001.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.database.CursorToHMAuxMapper
import com.namoadigital.prj001.database.Mapper
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.MdDeviceTp
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class MdDeviceTpDao(
    context: Context,
    mDB_NAME: String,
    mDB_VERSION: Int
) : BaseDao(
    context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI ),
    DaoWithReturn<MdDeviceTp> {

    companion object{
        const val TABLE = "md_device_tp"
        const val CUSTOMER_CODE ="customer_code"
        const val DEVICE_TP_CODE = "device_tp_code"
        const val DEVICE_TP_ID = "device_tp_id"
        const val DEVICE_TP_DESC = "device_tp_desc"
    }

    private val toMdDeviceTpMapper: Mapper<Cursor,MdDeviceTp>
    private val toContentValuesMapper: Mapper<MdDeviceTp,ContentValues>

    init {
        this.toMdDeviceTpMapper = CursorToMdDeviceTpMapper()
        this.toContentValuesMapper = MdDeviceTpToContentValuesMapper()
    }

    @Throws(java.lang.Exception::class)
    private fun getWherePkClause(mdDeviceTp: MdDeviceTp?): StringBuilder{
        mdDeviceTp?.let{
            return java.lang.StringBuilder()
                .append("""
                        ${CUSTOMER_CODE} = '${mdDeviceTp.customerCode}'  
                        AND ${DEVICE_TP_CODE} = '${mdDeviceTp.deviceTpCode}'                           
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
        
    }

    override fun addUpdate(mdDeviceTp: MdDeviceTp?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()

        try {
            daoObjReturn.table = TABLE
            curAction = DaoObjReturn.UPDATE
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(mdDeviceTp)
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(mdDeviceTp), sbWhere.toString(), null).toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(mdDeviceTp))
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

    override fun addUpdate(mdDeviceTps: MutableList<MdDeviceTp>?, status: Boolean): DaoObjReturn {
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

            mdDeviceTps?.forEach { mdDeviceTp ->
                val sbWhere: StringBuilder = getWherePkClause(mdDeviceTp)
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(mdDeviceTp), sbWhere.toString(), null).toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(mdDeviceTp))
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

    override fun getByString(sQuery: String?): MdDeviceTp? {
        var mdDeviceTp: MdDeviceTp? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                mdDeviceTp = toMdDeviceTpMapper.map(cursor)
            }
            //
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return mdDeviceTp
    }

    override fun getByStringHM(sQuery: String?): HMAux? {
        var mdDeviceTp: HMAux? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                mdDeviceTp = CursorToHMAuxMapper.mapN(cursor)
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return  mdDeviceTp
    }

    override fun query(sQuery: String?): MutableList<MdDeviceTp> {
        val mdDeviceTps = mutableListOf<MdDeviceTp>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                val uAux = toMdDeviceTpMapper.map(cursor)
                mdDeviceTps.add(uAux)
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return mdDeviceTps
    }

    override fun query_HM(sQuery: String?): MutableList<HMAux> {
        val mdDeviceTps = mutableListOf<HMAux>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                mdDeviceTps.add(CursorToHMAuxMapper.mapN(cursor))
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return mdDeviceTps
    }

    private class CursorToMdDeviceTpMapper : Mapper<Cursor, MdDeviceTp> {
        override fun map(cursor: Cursor?): MdDeviceTp? {
            cursor?.let {
                with(cursor){
                    return MdDeviceTp(
                        customerCode = getLong(getColumnIndex(CUSTOMER_CODE)),
                        deviceTpCode = getInt(getColumnIndex(DEVICE_TP_CODE)),
                        deviceTpId = getString(getColumnIndex(DEVICE_TP_ID)),
                        deviceTpDesc = getString(getColumnIndex(DEVICE_TP_DESC)),
                    )
                }
            }
            return null
        }
    }

    private class MdDeviceTpToContentValuesMapper : Mapper<MdDeviceTp, ContentValues> {
        override fun map(mdDeviceTp: MdDeviceTp?): ContentValues {
            val contentValues = ContentValues()
            //
            mdDeviceTp?.let {
                with(contentValues){
                    if(mdDeviceTp.customerCode > -1){
                        put(CUSTOMER_CODE,mdDeviceTp.customerCode)
                    }
                    if(mdDeviceTp.deviceTpCode > -1){
                        put(DEVICE_TP_CODE,mdDeviceTp.deviceTpCode)
                    }
                    if(mdDeviceTp.deviceTpId != null){
                        put(DEVICE_TP_ID,mdDeviceTp.deviceTpId)
                    }
                    if(mdDeviceTp.deviceTpDesc != null){
                        put(DEVICE_TP_DESC,mdDeviceTp.deviceTpDesc)
                    }
                }
            }
            //
            return contentValues
        }
    }
}