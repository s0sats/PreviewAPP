package com.namoadigital.prj001.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.database.CursorToHMAuxMapper
import com.namoadigital.prj001.database.Mapper
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.MD_Product_Serial_Tp_Device
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class MD_Product_Serial_Tp_DeviceDao(
    context: Context,
    mDB_NAME: String,
    mDB_VERSION: Int
) : BaseDao(
    context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI ),
    DaoWithReturn<MD_Product_Serial_Tp_Device> {

    companion object{
        const val TABLE = "md_product_serial_tp_device"
        const val CUSTOMER_CODE = "customer_code"
        const val PRODUCT_CODE = "product_code"
        const val SERIAL_CODE = "serial_code"
        const val DEVICE_TP_CODE = "device_tp_code"
        const val TRACKING_NUMBER = "tracking_number"
    }

    private val toMD_Product_Serial_Tp_DeviceMapper: Mapper<Cursor,MD_Product_Serial_Tp_Device>
    private val toContentValuesMapper: Mapper<MD_Product_Serial_Tp_Device,ContentValues>

    init {
        this.toMD_Product_Serial_Tp_DeviceMapper = CursorToMD_Product_Serial_Tp_DeviceMapper()
        this.toContentValuesMapper = MD_Product_Serial_Tp_DeviceToContentValuesMapper()
    }

    @Throws(java.lang.Exception::class)
    private fun getWherePkClause(MD_Product_Serial_Tp_Device: MD_Product_Serial_Tp_Device?): StringBuilder{
        MD_Product_Serial_Tp_Device?.let{
            return java.lang.StringBuilder()
                .append("""
                        ${CUSTOMER_CODE} = '${MD_Product_Serial_Tp_Device.customer_code}'  
                        AND ${PRODUCT_CODE} = '${MD_Product_Serial_Tp_Device.product_code}'                           
                        AND ${SERIAL_CODE} = '${MD_Product_Serial_Tp_Device.serial_code}'                           
                        AND ${DEVICE_TP_CODE} = '${MD_Product_Serial_Tp_Device.device_tp_code}'                           
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
        
    }

    override fun addUpdate(mdProductSerialTpDevice: MD_Product_Serial_Tp_Device?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()

        try {
            daoObjReturn.table = TABLE
            curAction = DaoObjReturn.UPDATE
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(mdProductSerialTpDevice)
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(mdProductSerialTpDevice), sbWhere.toString(), null).toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(mdProductSerialTpDevice))
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

    override fun addUpdate(mdProductSerialTpDevices: MutableList<MD_Product_Serial_Tp_Device>?, status: Boolean): DaoObjReturn {
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

            mdProductSerialTpDevices?.forEach { mdProductSerialTpDevice ->
                val sbWhere: StringBuilder = getWherePkClause(mdProductSerialTpDevice)
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(mdProductSerialTpDevice), sbWhere.toString(), null).toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(mdProductSerialTpDevice))
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

    override fun getByString(sQuery: String?): MD_Product_Serial_Tp_Device? {
        var mdProductSerialTpDevice: MD_Product_Serial_Tp_Device? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                mdProductSerialTpDevice = toMD_Product_Serial_Tp_DeviceMapper.map(cursor)
            }
            //
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return mdProductSerialTpDevice
    }

    override fun getByStringHM(sQuery: String?): HMAux? {
        var mdProductSerialTpDevice: HMAux? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                mdProductSerialTpDevice = CursorToHMAuxMapper.mapN(cursor)
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return  mdProductSerialTpDevice
    }

    override fun query(sQuery: String?): MutableList<MD_Product_Serial_Tp_Device> {
        val mdProductSerialTpDevices = mutableListOf<MD_Product_Serial_Tp_Device>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                val uAux = toMD_Product_Serial_Tp_DeviceMapper.map(cursor)
                mdProductSerialTpDevices.add(uAux)
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return mdProductSerialTpDevices
    }

    override fun query_HM(sQuery: String?): MutableList<HMAux> {
        val mdProductSerialTpDevices = mutableListOf<HMAux>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                mdProductSerialTpDevices.add(CursorToHMAuxMapper.mapN(cursor))
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return mdProductSerialTpDevices
    }

    private class CursorToMD_Product_Serial_Tp_DeviceMapper : Mapper<Cursor, MD_Product_Serial_Tp_Device> {
        override fun map(cursor: Cursor?): MD_Product_Serial_Tp_Device? {
            cursor?.let {
                with(cursor){
                    return MD_Product_Serial_Tp_Device(
                        customer_code = getLong(getColumnIndex(CUSTOMER_CODE)),
                        product_code = getLong(getColumnIndex(PRODUCT_CODE)),
                        serial_code = getLong(getColumnIndex(SERIAL_CODE)),
                        device_tp_code = getInt(getColumnIndex(DEVICE_TP_CODE)),
                        tracking_number = getString(getColumnIndex(TRACKING_NUMBER))
                    )
                }
            }
            return null
        }
    }

    private class MD_Product_Serial_Tp_DeviceToContentValuesMapper : Mapper<MD_Product_Serial_Tp_Device, ContentValues> {
        override fun map(mdProductSerialTpDevice: MD_Product_Serial_Tp_Device?): ContentValues {
            val contentValues = ContentValues()
            //
            mdProductSerialTpDevice?.let {
                with(contentValues){
                    if(mdProductSerialTpDevice.customer_code > -1){
                        put(CUSTOMER_CODE,mdProductSerialTpDevice.customer_code)
                    }
                    if(mdProductSerialTpDevice.product_code > -1){
                        put(PRODUCT_CODE,mdProductSerialTpDevice.product_code)
                    }
                    if(mdProductSerialTpDevice.serial_code > -1){
                        put(SERIAL_CODE,mdProductSerialTpDevice.serial_code)
                    }
                    if(mdProductSerialTpDevice.device_tp_code > -1){
                        put(DEVICE_TP_CODE,mdProductSerialTpDevice.device_tp_code)
                    }
                    put(TRACKING_NUMBER,mdProductSerialTpDevice.tracking_number)
                }
            }
            //
            return contentValues
        }
    }
}