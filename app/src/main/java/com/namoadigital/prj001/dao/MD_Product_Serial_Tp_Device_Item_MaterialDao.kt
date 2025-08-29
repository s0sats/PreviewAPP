package com.namoadigital.prj001.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import androidx.core.database.getStringOrNull
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.database.CursorToHMAuxMapper
import com.namoadigital.prj001.database.Mapper
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.MD_Product_Serial_Tp_Device_Item_Material
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.math.BigDecimal

class MD_Product_Serial_Tp_Device_Item_MaterialDao(
    context: Context,
    mDB_NAME: String,
    mDB_VERSION: Int
) : BaseDao(
    context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI),
    DaoWithReturn<MD_Product_Serial_Tp_Device_Item_Material>,
    DaoWithReturnSharedDbInstance<MD_Product_Serial_Tp_Device_Item_Material>{

    companion object{
        const val TABLE = "md_product_serial_tp_device_item_material"
        const val CUSTOMER_CODE = "customer_code"
        const val PRODUCT_CODE = "product_code"
        const val SERIAL_CODE = "serial_code"
        const val DEVICE_TP_CODE = "device_tp_code"
        const val ITEM_CHECK_CODE = "item_check_code"
        const val ITEM_CHECK_SEQ = "item_check_seq"
        const val MATERIAL_CODE = "material_code"
        const val QTY = "qty"
        const val ORIGIN = "origin"
    }
    
    private val toMD_Product_Serial_Tp_Device_Item_MaterialMapper: Mapper<Cursor, MD_Product_Serial_Tp_Device_Item_Material>
    private val toContentValuesMapper: Mapper<MD_Product_Serial_Tp_Device_Item_Material, ContentValues>

    init {
        this.toMD_Product_Serial_Tp_Device_Item_MaterialMapper = CursorToMD_Product_Serial_Tp_Device_Item_MaterialMapper()
        this.toContentValuesMapper = MD_Product_Serial_Tp_Device_Item_MaterialToContentValuesMapper()
    }

    @Throws(java.lang.Exception::class)
    private fun getWherePkClause(mdProductSerialTpDeviceItemMaterial: MD_Product_Serial_Tp_Device_Item_Material?): StringBuilder{
        mdProductSerialTpDeviceItemMaterial?.let{
            return java.lang.StringBuilder()
                .append("""
                        ${CUSTOMER_CODE} = '${mdProductSerialTpDeviceItemMaterial.customer_code}'  
                        AND ${PRODUCT_CODE} = '${mdProductSerialTpDeviceItemMaterial.product_code}'                           
                        AND ${SERIAL_CODE} = '${mdProductSerialTpDeviceItemMaterial.serial_code}'                           
                        AND ${DEVICE_TP_CODE} = '${mdProductSerialTpDeviceItemMaterial.device_tp_code}'                           
                        AND ${ITEM_CHECK_CODE} = '${mdProductSerialTpDeviceItemMaterial.item_check_code}'                           
                        AND ${ITEM_CHECK_SEQ} = '${mdProductSerialTpDeviceItemMaterial.item_check_seq}'                           
                        AND ${MATERIAL_CODE} = '${mdProductSerialTpDeviceItemMaterial.material_code}'                           
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    override fun addUpdate(itemMaterial: MD_Product_Serial_Tp_Device_Item_Material?): DaoObjReturn {
        return addUpdate(itemMaterial, null)
    }

    override fun addUpdate(
        itemMaterial: MD_Product_Serial_Tp_Device_Item_Material?,
        dbInstance: SQLiteDatabase?
    ): DaoObjReturn {
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
            val sbWhere: StringBuilder = getWherePkClause(itemMaterial)
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(itemMaterial), sbWhere.toString(), null).toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(itemMaterial))
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

    override fun addUpdate(
        itemMaterials: MutableList<MD_Product_Serial_Tp_Device_Item_Material>?,
        status: Boolean
    ): DaoObjReturn {
        return addUpdate(itemMaterials, status , null)
    }

    override fun addUpdate(
        itemMaterials: MutableList<MD_Product_Serial_Tp_Device_Item_Material>?,
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

            itemMaterials?.forEach { itemMaterial ->
                val sbWhere: StringBuilder = getWherePkClause(itemMaterial)
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(itemMaterial), sbWhere.toString(), null).toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(itemMaterial))
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

    override fun remove(
        itemMaterial: MD_Product_Serial_Tp_Device_Item_Material?,
        dbInstance: SQLiteDatabase?
    ): DaoObjReturn {
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
            val sbWhere: StringBuilder = getWherePkClause(itemMaterial)
            //Tenta update e armazena retorno
            sqlRet = db.delete(TABLE,sbWhere.toString(), null).toLong()
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
            daoObjReturn.actionReturn = sqlRet
        }
        //
        if (dbInstance == null) {
            closeDB()
        }
        return daoObjReturn
    }

    override fun getByString(sQuery: String?): MD_Product_Serial_Tp_Device_Item_Material? {
        var itemMaterial: MD_Product_Serial_Tp_Device_Item_Material? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery!!, null)
            while (cursor.moveToNext()) {
                itemMaterial = toMD_Product_Serial_Tp_Device_Item_MaterialMapper.map(cursor)
            }
            //
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return itemMaterial
    }

    override fun getByStringHM(sQuery: String?): HMAux? {
        var itemMaterial: HMAux? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery!!, null)
            while (cursor.moveToNext()) {
                itemMaterial = CursorToHMAuxMapper.mapN(cursor)
            }
            //
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return itemMaterial
    }

    override fun query(sQuery: String?): MutableList<MD_Product_Serial_Tp_Device_Item_Material> {
        val itemMaterials = mutableListOf<MD_Product_Serial_Tp_Device_Item_Material>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery!!, null)
            while (cursor.moveToNext()) {
                val itemMaterial = toMD_Product_Serial_Tp_Device_Item_MaterialMapper.map(cursor)
                itemMaterials.add(itemMaterial)
            }
            //
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return itemMaterials
    }

    override fun query_HM(sQuery: String?): MutableList<HMAux> {
        val itemMaterials = mutableListOf<HMAux>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery!!, null)
            while (cursor.moveToNext()) {
                itemMaterials.add(CursorToHMAuxMapper.mapN(cursor))
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return itemMaterials
    }

    private class CursorToMD_Product_Serial_Tp_Device_Item_MaterialMapper : Mapper<Cursor, MD_Product_Serial_Tp_Device_Item_Material> {
        override fun map(cursor: Cursor?): MD_Product_Serial_Tp_Device_Item_Material? {
            cursor?.let{
                with(it){
                    return MD_Product_Serial_Tp_Device_Item_Material(
                        customer_code = getLong(getColumnIndexOrThrow(CUSTOMER_CODE)),
                        product_code = getLong(getColumnIndexOrThrow(PRODUCT_CODE)),
                        serial_code = getLong(getColumnIndexOrThrow(SERIAL_CODE)),
                        device_tp_code = getInt(getColumnIndexOrThrow(DEVICE_TP_CODE)),
                        item_check_code = getInt(getColumnIndexOrThrow(ITEM_CHECK_CODE)) ,
                        item_check_seq = getInt(getColumnIndexOrThrow(ITEM_CHECK_SEQ)) ,
                        material_code = getInt(getColumnIndexOrThrow(MATERIAL_CODE)),
                        qty = BigDecimal(getDouble(getColumnIndexOrThrow(QTY))),
                        origin = getStringOrNull(getColumnIndexOrThrow(ORIGIN))
                    )
                }
            }
            return null
        }

    }

    class MD_Product_Serial_Tp_Device_Item_MaterialToContentValuesMapper : Mapper<MD_Product_Serial_Tp_Device_Item_Material, ContentValues> {
        override fun map(mdProductSerialTpDeviceItemMaterial: MD_Product_Serial_Tp_Device_Item_Material?): ContentValues {
            val contentValues = ContentValues()
            mdProductSerialTpDeviceItemMaterial?.let{
                with(contentValues){
                    if(mdProductSerialTpDeviceItemMaterial.customer_code > -1){
                        put(CUSTOMER_CODE,mdProductSerialTpDeviceItemMaterial.customer_code)
                    }
                    if(mdProductSerialTpDeviceItemMaterial.product_code > -1){
                        put(PRODUCT_CODE,mdProductSerialTpDeviceItemMaterial.product_code)
                    }
                    if(mdProductSerialTpDeviceItemMaterial.serial_code > -1){
                        put(SERIAL_CODE,mdProductSerialTpDeviceItemMaterial.serial_code)
                    }
                    if(mdProductSerialTpDeviceItemMaterial.device_tp_code > -1){
                        put(DEVICE_TP_CODE,mdProductSerialTpDeviceItemMaterial.device_tp_code)
                    }
                    if(mdProductSerialTpDeviceItemMaterial.item_check_code > -1){
                        put(ITEM_CHECK_CODE,mdProductSerialTpDeviceItemMaterial.item_check_code)
                    }
                    if(mdProductSerialTpDeviceItemMaterial.item_check_seq > -1){
                        put(ITEM_CHECK_SEQ,mdProductSerialTpDeviceItemMaterial.item_check_seq)
                    }
                    if(mdProductSerialTpDeviceItemMaterial.material_code > -1){
                        put(MATERIAL_CODE,mdProductSerialTpDeviceItemMaterial.material_code)
                    }
                    if(mdProductSerialTpDeviceItemMaterial.qty > BigDecimal(-1)){
                        put(QTY,mdProductSerialTpDeviceItemMaterial.qty.toFloat())
                    }
                    put(ORIGIN,mdProductSerialTpDeviceItemMaterial.origin)
                }
            }
            return contentValues
        }
    }
}