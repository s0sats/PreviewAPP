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
import com.namoadigital.prj001.model.TkTicketOriginNc
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.util.*

class TkTicketOriginNcDao(
    val context: Context,
    val mDB_NAME: String,
    val mDB_VERSION: Int
) : BaseDao(
    context, mDB_NAME, mDB_VERSION, ConstantBaseApp.DB_MODE_MULTI
), DaoWithReturn<TkTicketOriginNc>,DaoWithReturnSharedDbInstance<TkTicketOriginNc>{

    companion object{
        const val TABLE = "tk_ticket_origin_nc"
        const val CUSTOMER_CODE = "customer_code"
        const val TICKET_PREFIX = "ticket_prefix"
        const val TICKET_CODE = "ticket_code"
        const val PAGE = "page"
        const val CUSTOM_FORM_DATA_TYPE = "custom_form_data_type"
        const val CUSTOM_FORM_ORDER = "custom_form_order"
        const val DESCRIPTION = "description"
        const val DATA_VALUE = "data_value"
        const val DATA_VALUE_TXT = "data_value_txt"
        const val DATA_VALUE_LOCAL = "data_value_local"
        const val DATA_PHOTO1_URL = "data_photo1_url"
        const val DATA_PHOTO1_URL_LOCAL = "data_photo1_url_local"
        const val DATA_PHOTO2_URL = "data_photo2_url"
        const val DATA_PHOTO2_URL_LOCAL	= "data_photo2_url_local"
        const val DATA_PHOTO3_URL = "data_photo3_url"
        const val DATA_PHOTO3_URL_LOCAL	= "data_photo3_url_local"
        const val DATA_PHOTO4_URL = "data_photo4_url"
        const val DATA_PHOTO4_URL_LOCAL	= "data_photo4_url_local"
        const val DATA_COMMENT = "data_comment"
        const val PICTURE_LINES = "picture_lines"
        const val PICTURE_COLUMNS = "picture_columns"
        const val PICTURE_COLOR = "picture_color"
        const val PICTURE_URL = "picture_url"
        const val PICTURE_URL_LOCAL = "picture_url_local"
    }

    private val toTkTicketOriginNcMapper: Mapper<Cursor,TkTicketOriginNc>
    private val toContentValuesMapper: Mapper<TkTicketOriginNc, ContentValues>

    init {
        toTkTicketOriginNcMapper = CursorTkTicketOriginNcMapper()
        toContentValuesMapper = TkTicketOriginNcToContentValuesMapper()
    }

    @Throws(java.lang.Exception::class)
    private fun getWherePkClause(tkTicketOriginNc: TkTicketOriginNc?): StringBuilder {
        tkTicketOriginNc?.let{
            return java.lang.StringBuilder()
                .append("""
                        ${CUSTOMER_CODE} = '${tkTicketOriginNc.getCustomerCode()}'  
                        AND ${TICKET_PREFIX} = '${tkTicketOriginNc.getTicketPrefix()}'
                        AND ${TICKET_CODE} = '${tkTicketOriginNc.getTicketCode()}'
                        AND ${PAGE} = '${tkTicketOriginNc.getPage()}'
                        AND ${CUSTOM_FORM_ORDER} = '${tkTicketOriginNc.getCustomFormOrder()}'                        
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    override fun addUpdate(tkTicketOriginNc: TkTicketOriginNc?): DaoObjReturn {
        return addUpdate(tkTicketOriginNc,null)
    }

    override fun addUpdate(tkTicketOriginNc: TkTicketOriginNc?, dbInstance: SQLiteDatabase?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        if(dbInstance == null) {
            openDB()
        }else{
            this.db = dbInstance
        }

        try {
            daoObjReturn.table = TABLE
            curAction = DaoObjReturn.UPDATE
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(tkTicketOriginNc)
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tkTicketOriginNc), sbWhere.toString(), null).toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tkTicketOriginNc))
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

    override fun addUpdate(tkTicketOrginNcs: MutableList<TkTicketOriginNc>?, status: Boolean): DaoObjReturn {
        return addUpdate(tkTicketOrginNcs, status,null)
    }

    override fun addUpdate(
        tkTicketOrginNcs: MutableList<TkTicketOriginNc>?,
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
            daoObjReturn.table = TABLE
            curAction = DaoObjReturn.UPDATE
            //Se db não foi passado, inicializa transaction
            if (dbInstance == null) {
                db.beginTransaction()
            }
            if (status) {
                db.delete(TABLE, null, null)
            }

            tkTicketOrginNcs?.forEach { tkTicketOriginNc->
                //Where para update
                val sbWhere: StringBuilder = getWherePkClause(tkTicketOriginNc)
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tkTicketOriginNc), sbWhere.toString(), null).toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tkTicketOriginNc))
                }
            }
            //
            if(dbInstance == null) {
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

    override fun remove(tkTicketOriginNc: TkTicketOriginNc?, dbInstance: SQLiteDatabase?): DaoObjReturn {
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
            daoObjReturn.table = TABLE
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(tkTicketOriginNc)
            sqlRet = db.delete(TABLE, sbWhere.toString(), null).toLong()
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

    override fun getByString(sQuery: String?): TkTicketOriginNc? {
        var tkTicketOriginNc: TkTicketOriginNc? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                tkTicketOriginNc = toTkTicketOriginNcMapper.map(cursor)
            }
            //
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return tkTicketOriginNc
    }

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

    override fun query(sQuery: String?): MutableList<TkTicketOriginNc> {
        val tkTicketOriginNcs = mutableListOf<TkTicketOriginNc>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                val uAux = toTkTicketOriginNcMapper.map(cursor)
                tkTicketOriginNcs.add(uAux)
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return tkTicketOriginNcs

    }

    override fun query_HM(sQuery: String?): MutableList<HMAux> {
        val tkTicketOriginNcs: MutableList<HMAux> = ArrayList()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                tkTicketOriginNcs.add(CursorToHMAuxMapper.mapN(cursor))
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return tkTicketOriginNcs
    }

    private inner class CursorTkTicketOriginNcMapper : Mapper<Cursor, TkTicketOriginNc> {
        override fun map(cursor: Cursor?): TkTicketOriginNc? {
          cursor?.let {
              with(cursor){
                  return TkTicketOriginNc(
                      customerCode = getInt(getColumnIndex(CUSTOMER_CODE)),
                      ticketPrefix = getInt(getColumnIndex(TICKET_PREFIX)),
                      ticketCode = getInt(getColumnIndex(TICKET_CODE)),
                      page = getInt(getColumnIndex(PAGE)),
                      customFormDataType = getString(getColumnIndex(CUSTOM_FORM_DATA_TYPE)),
                      customFormOrder = getInt(getColumnIndex(CUSTOM_FORM_ORDER)),
                      description = getString(getColumnIndex(DESCRIPTION)),
                      dataValue = getString(getColumnIndex(DATA_VALUE)),
                      dataValueTxt = getString(getColumnIndex(DATA_VALUE_TXT)),
                      dataValueLocal = getString(getColumnIndex(DATA_VALUE_LOCAL)),
                      dataPhoto1Url = getString(getColumnIndex(DATA_PHOTO1_URL)),
                      dataPhoto1UrlLocal = getString(getColumnIndex(DATA_PHOTO1_URL_LOCAL)),
                      dataPhoto2Url = getString(getColumnIndex(DATA_PHOTO2_URL)),
                      dataPhoto2UrlLocal = getString(getColumnIndex(DATA_PHOTO2_URL_LOCAL)),
                      dataPhoto3Url = getString(getColumnIndex(DATA_PHOTO3_URL)),
                      dataPhoto3UrlLocal = getString(getColumnIndex(DATA_PHOTO3_URL_LOCAL)),
                      dataPhoto4Url = getString(getColumnIndex(DATA_PHOTO4_URL)),
                      dataPhoto4UrlLocal = getString(getColumnIndex(DATA_PHOTO4_URL_LOCAL)),
                      dataComment = getString(getColumnIndex(DATA_COMMENT)),
                      pictureLines = getInt(getColumnIndex(PICTURE_LINES)),
                      pictureColumns = getInt(getColumnIndex(PICTURE_COLUMNS)),
                      pictureColor = getString(getColumnIndex(PICTURE_COLOR)),
                      pictureUrl = getString(getColumnIndex(PICTURE_URL)),
                      pictureUrlLocal = getString(getColumnIndex(PICTURE_URL_LOCAL))
                  )
              }
          }
           //
           return null
        }
    }

    private inner class TkTicketOriginNcToContentValuesMapper : Mapper<TkTicketOriginNc, ContentValues> {
        override fun map(tkTicketOriginNc: TkTicketOriginNc?): ContentValues {
            val contentValues = ContentValues()
            //
            tkTicketOriginNc?.let{
                with(contentValues){
                    if(tkTicketOriginNc.getCustomerCode() > -1){
                        put(CUSTOMER_CODE,tkTicketOriginNc.getCustomerCode())
                    }
                    if(tkTicketOriginNc.getTicketPrefix() > -1){
                        put(TICKET_PREFIX,tkTicketOriginNc.getTicketPrefix())
                    }
                    if(tkTicketOriginNc.getTicketCode() > -1){
                        put(TICKET_CODE,tkTicketOriginNc.getTicketCode())
                    }
                    if(tkTicketOriginNc.getPage() > -1){
                        put(PAGE,tkTicketOriginNc.getPage())
                    }
                    put(CUSTOM_FORM_DATA_TYPE,tkTicketOriginNc.getCustomFormDataType())
                    if(tkTicketOriginNc.getCustomFormOrder() > -1){
                        put(CUSTOM_FORM_ORDER,tkTicketOriginNc.getCustomFormOrder())
                    }
                    put(DESCRIPTION,tkTicketOriginNc.getDescription())
                    put(DATA_VALUE,tkTicketOriginNc.getDataValue())
                    put(DATA_VALUE_TXT,tkTicketOriginNc.getDataValueTxt())
                    put(DATA_VALUE_LOCAL,tkTicketOriginNc.getDataValueLocal())
                    put(DATA_PHOTO1_URL,tkTicketOriginNc.getDataPhoto1Url())
                    put(DATA_PHOTO1_URL_LOCAL,tkTicketOriginNc.getDataPhoto1UrlLocal())
                    put(DATA_PHOTO2_URL,tkTicketOriginNc.getDataPhoto2Url())
                    put(DATA_PHOTO2_URL_LOCAL,tkTicketOriginNc.getDataPhoto2UrlLocal())
                    put(DATA_PHOTO3_URL,tkTicketOriginNc.getDataPhoto3Url())
                    put(DATA_PHOTO3_URL_LOCAL,tkTicketOriginNc.getDataPhoto3UrlLocal())
                    put(DATA_PHOTO4_URL,tkTicketOriginNc.getDataPhoto4Url())
                    put(DATA_PHOTO4_URL_LOCAL,tkTicketOriginNc.getDataPhoto4UrlLocal())
                    put(DATA_COMMENT,tkTicketOriginNc.getDataComment())
                    put(PICTURE_LINES,tkTicketOriginNc.getPictureLines())
                    put(PICTURE_COLUMNS,tkTicketOriginNc.getPictureColumns())
                    put(PICTURE_COLOR,tkTicketOriginNc.getPictureColor())
                    put(PICTURE_URL,tkTicketOriginNc.getPictureUrl())
                    put(PICTURE_URL_LOCAL,tkTicketOriginNc.getPictureUrlLocal())
                }
            }
            //
            return contentValues
        }
    }

}