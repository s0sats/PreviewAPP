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
import com.namoadigital.prj001.model.MD_Product_Serial_Tp_Device_Item
import com.namoadigital.prj001.model.MD_Product_Serial_Tp_Device_Item_Hist
import com.namoadigital.prj001.sql.MD_Product_Serial_Tp_Device_ItemDao_Sql_001
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class MD_Product_Serial_Tp_Device_ItemDao(
    context: Context,
    mDB_NAME: String,
    mDB_VERSION: Int
) : BaseDao(
    context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI),
    DaoWithReturn<MD_Product_Serial_Tp_Device_Item>,
    DaoWithReturnSharedDbInstance<MD_Product_Serial_Tp_Device_Item>
{
    companion object{
        const val TABLE = "md_product_serial_tp_device_item"
        const val CUSTOMER_CODE = "customer_code"
        const val PRODUCT_CODE = "product_code"
        const val SERIAL_CODE = "serial_code"
        const val DEVICE_TP_CODE = "device_tp_code"
        const val ITEM_CHECK_CODE = "item_check_code"
        const val ITEM_CHECK_SEQ = "item_check_seq"
        const val APPLY_MATERIAL = "apply_material"
        const val VERIFICATION_INSTRUCTION = "verification_instruction"
        const val REQUIRE_JUSTIFY_PROBLEM = "require_justify_problem"
        const val CRITICAL_ITEM = "critical_item"
        const val ORDER_SEQ = "order_seq"
        const val STRUCTURE = "structure"
        const val MANUAL_DESC = "manual_desc"
        const val NEXT_CYCLE_MEASURE = "next_cycle_measure"
        const val NEXT_CYCLE_MEASURE_DATE = "next_cycle_measure_date"
        const val NEXT_CYCLE_LIMIT_DATE = "next_cycle_limit_date"
        const val ITEM_CHECK_STATUS = "item_check_status"
        const val TARGET_DATE = "target_date"
    }

    private val toMD_Product_Serial_Tp_Device_ItemMapper: Mapper<Cursor,MD_Product_Serial_Tp_Device_Item>
    private val toContentValuesMapper: Mapper<MD_Product_Serial_Tp_Device_Item,ContentValues>

    init {
        this.toMD_Product_Serial_Tp_Device_ItemMapper = CursorToMD_Product_Serial_Tp_Device_ItemMapper()
        this.toContentValuesMapper = MD_Product_Serial_Tp_Device_ItemToContentValuesMapper()
    }

    @Throws(java.lang.Exception::class)
    private fun getWherePkClause(MD_Product_Serial_Tp_Device_Item: MD_Product_Serial_Tp_Device_Item?): StringBuilder{
        MD_Product_Serial_Tp_Device_Item?.let{
            return java.lang.StringBuilder()
                .append("""
                        ${CUSTOMER_CODE} = '${MD_Product_Serial_Tp_Device_Item.customer_code}'  
                        AND ${PRODUCT_CODE} = '${MD_Product_Serial_Tp_Device_Item.product_code}'                           
                        AND ${SERIAL_CODE} = '${MD_Product_Serial_Tp_Device_Item.serial_code}'                           
                        AND ${DEVICE_TP_CODE} = '${MD_Product_Serial_Tp_Device_Item.device_tp_code}'                           
                        AND ${ITEM_CHECK_CODE} = '${MD_Product_Serial_Tp_Device_Item.item_check_code}'                           
                        AND ${ITEM_CHECK_SEQ} = '${MD_Product_Serial_Tp_Device_Item.item_check_seq}'                           
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
        
    }

    override fun addUpdate(mdProductSerialTpDeviceItem: MD_Product_Serial_Tp_Device_Item?): DaoObjReturn {
        return addUpdate(mdProductSerialTpDeviceItem,null)
    }

    override fun addUpdate(
        mdProductSerialTpDeviceItem: MD_Product_Serial_Tp_Device_Item?,
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
            val sbWhere: StringBuilder = getWherePkClause(mdProductSerialTpDeviceItem)
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(mdProductSerialTpDeviceItem), sbWhere.toString(), null).toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(mdProductSerialTpDeviceItem))
            }
            //Tenta inserir steps
            //LUCHE - 21/07/2020
            //Ctrl será dependendo do step e não do ticket.
            //Tenta inserir action
//                TK_Ticket_CtrlDao ticketCtrlDao = new TK_Ticket_CtrlDao(
//                    context,
//                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//                    Constant.DB_VERSION_CUSTOM
//                );
//                //Chama insertUpdate do Ctrl,passando db como param aguardando retorno.
//                daoObjReturn = ticketCtrlDao.addUpdate(tk_ticket.getCtrl(), false, db);
//                //Se erro durante insert, dispara exception abortando o processamento.
//                if (daoObjReturn.hasError()) {
//                    throw new Exception(daoObjReturn.getRawMessage());
//                }
            //Tenta inserir historico
            mdProductSerialTpDeviceItem?.let {
                /**
                 * Como hist é um valor setado dentro do init ou via delegate, quando é carregado direto do json
                 * o valor setado e null. Isso acontece pois é feito via reflections no Gson e que não tem
                 * suporte a essa features do Kotlin
                 */
                if(it.hist != null && it.hist.isNotEmpty() ){
                    daoObjReturn = tryAddUpdateHist(it.hist, db)
                    //Se erro durante insert, dispara exception abortando o processamento.
                    if (daoObjReturn.hasError()) {
                        throw java.lang.Exception(daoObjReturn.rawMessage)
                    }
                }
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

    override fun addUpdate(mdProductSerialTpDeviceItems: MutableList<MD_Product_Serial_Tp_Device_Item>?, status: Boolean): DaoObjReturn {
        return addUpdate(mdProductSerialTpDeviceItems, status,null)
    }

    override fun addUpdate(
        mdProductSerialTpDeviceItems: MutableList<MD_Product_Serial_Tp_Device_Item>?,
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

            mdProductSerialTpDeviceItems?.forEach { mdProductSerialTpDeviceItem ->
                val sbWhere: StringBuilder = getWherePkClause(mdProductSerialTpDeviceItem)
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(mdProductSerialTpDeviceItem), sbWhere.toString(), null).toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(mdProductSerialTpDeviceItem))
                }
                //Tenta inserir historico
                mdProductSerialTpDeviceItem.let {
                    /**
                     * Como hist é um valor setado dentro do init ou via delegate, quando é carregado direto do json
                     * o valor setado e null. Isso acontece pois é feito via reflections no Gson e que não tem
                     * suporte a essa features do Kotlin
                     */
                    if(it.hist != null && it.hist.isNotEmpty()){
                        daoObjReturn = tryAddUpdateHist(it.hist, db)
                        //Se erro durante insert, dispara exception abortando o processamento.
                        if (daoObjReturn.hasError()) {
                            throw java.lang.Exception(daoObjReturn.rawMessage)
                        }
                    }
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

    override fun remove(
        mdProductSerialTpDeviceItem: MD_Product_Serial_Tp_Device_Item?,
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
            val sbWhere: StringBuilder = getWherePkClause(mdProductSerialTpDeviceItem)
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

    override fun getByString(sQuery: String?): MD_Product_Serial_Tp_Device_Item? {
        var mdProductSerialTpDeviceItem: MD_Product_Serial_Tp_Device_Item? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                mdProductSerialTpDeviceItem = toMD_Product_Serial_Tp_Device_ItemMapper.map(cursor)
                mdProductSerialTpDeviceItem?.let {
                    getItemHist(it)
                }
            }
            //
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return mdProductSerialTpDeviceItem
    }

    override fun getByStringHM(sQuery: String?): HMAux? {
        var mdProductSerialTpDeviceItem: HMAux? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                mdProductSerialTpDeviceItem = CursorToHMAuxMapper.mapN(cursor)
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return  mdProductSerialTpDeviceItem
    }

    override fun query(sQuery: String?): MutableList<MD_Product_Serial_Tp_Device_Item> {
        val mdProductSerialTpDeviceItems = mutableListOf<MD_Product_Serial_Tp_Device_Item>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                val uAux = toMD_Product_Serial_Tp_Device_ItemMapper.map(cursor)
                uAux?.let {
                    getItemHist(it)
                }
                mdProductSerialTpDeviceItems.add(uAux)
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return mdProductSerialTpDeviceItems
    }

    override fun query_HM(sQuery: String?): MutableList<HMAux> {
        val mdProductSerialTpDeviceItems = mutableListOf<HMAux>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                mdProductSerialTpDeviceItems.add(CursorToHMAuxMapper.mapN(cursor))
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return mdProductSerialTpDeviceItems
    }

    /**
     * Fun que tenta o insert do historico.
     */
    private fun tryAddUpdateHist(hist: MutableList<MD_Product_Serial_Tp_Device_Item_Hist>, db: SQLiteDatabase?): DaoObjReturn {
        return getItemHistDao().addUpdate(hist,false,db)
    }

    /**
     * Fun que seleciona o historico relacionados ao item
     */
    private fun getItemHist(mdProductSerialTpDeviceItem: MD_Product_Serial_Tp_Device_Item) {
        val histDao = getItemHistDao()
        //
        mdProductSerialTpDeviceItem.hist = histDao.query(
            MD_Product_Serial_Tp_Device_ItemDao_Sql_001(
                mdProductSerialTpDeviceItem.customer_code,
                mdProductSerialTpDeviceItem.product_code,
                mdProductSerialTpDeviceItem.serial_code,
                mdProductSerialTpDeviceItem.device_tp_code,
                mdProductSerialTpDeviceItem.item_check_code,
                mdProductSerialTpDeviceItem.item_check_seq
            ).toSqlQuery()
        )
    }

    /**
     * Fun que retorna o dao do historico.
     */
    private fun getItemHistDao(): MD_Product_Serial_Tp_Device_Item_HistDao {
        return MD_Product_Serial_Tp_Device_Item_HistDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        )
    }

    private class CursorToMD_Product_Serial_Tp_Device_ItemMapper : Mapper<Cursor, MD_Product_Serial_Tp_Device_Item> {
        override fun map(cursor: Cursor?): MD_Product_Serial_Tp_Device_Item? {
            cursor?.let {
                with(cursor){
                    return MD_Product_Serial_Tp_Device_Item(
                        customer_code = getLong(getColumnIndex(CUSTOMER_CODE)),
                        product_code = getLong(getColumnIndex(PRODUCT_CODE)),
                        serial_code = getLong(getColumnIndex(SERIAL_CODE)),
                        device_tp_code = getInt(getColumnIndex(DEVICE_TP_CODE)),
                        item_check_code = getInt(getColumnIndex(ITEM_CHECK_CODE)) ,
                        item_check_seq = getInt(getColumnIndex(ITEM_CHECK_SEQ)) ,
                        apply_material = getString(getColumnIndex(APPLY_MATERIAL)) ,
                        verification_instruction = getString(getColumnIndex(VERIFICATION_INSTRUCTION)),
                        require_justify_problem = getInt(getColumnIndex(REQUIRE_JUSTIFY_PROBLEM)) ,
                        critical_item = getInt(getColumnIndex(CRITICAL_ITEM)),
                        order_seq = getInt(getColumnIndex(ORDER_SEQ)),
                        structure = getInt(getColumnIndex(STRUCTURE)),
                        manual_desc = getString(getColumnIndex(MANUAL_DESC)),
                        next_cycle_measure = getDouble(getColumnIndex(NEXT_CYCLE_MEASURE)),
                        next_cycle_measure_date = getString(getColumnIndex(NEXT_CYCLE_MEASURE_DATE)),
                        next_cycle_limit_date = getString(getColumnIndex(NEXT_CYCLE_LIMIT_DATE)),
                        item_check_status = getString(getColumnIndex(ITEM_CHECK_STATUS)),
                        target_date = getString(getColumnIndex(TARGET_DATE))
                    )
                }
            }
            return null
        }
    }

    private class MD_Product_Serial_Tp_Device_ItemToContentValuesMapper : Mapper<MD_Product_Serial_Tp_Device_Item, ContentValues> {
        override fun map(mdProductSerialTpDeviceItem: MD_Product_Serial_Tp_Device_Item?): ContentValues {
            val contentValues = ContentValues()
            //
            mdProductSerialTpDeviceItem?.let {
                with(contentValues){
                    if(mdProductSerialTpDeviceItem.customer_code > -1){
                        put(CUSTOMER_CODE,mdProductSerialTpDeviceItem.customer_code)
                    }
                    if(mdProductSerialTpDeviceItem.product_code > -1){
                        put(PRODUCT_CODE,mdProductSerialTpDeviceItem.product_code)
                    }
                    if(mdProductSerialTpDeviceItem.serial_code > -1){
                        put(SERIAL_CODE,mdProductSerialTpDeviceItem.serial_code)
                    }
                    if(mdProductSerialTpDeviceItem.device_tp_code > -1){
                        put(DEVICE_TP_CODE,mdProductSerialTpDeviceItem.device_tp_code)
                    }
                    if(mdProductSerialTpDeviceItem.item_check_code > -1){
                        put(ITEM_CHECK_CODE,mdProductSerialTpDeviceItem.item_check_code)
                    }
                    if(mdProductSerialTpDeviceItem.item_check_seq > -1){
                        put(ITEM_CHECK_SEQ,mdProductSerialTpDeviceItem.item_check_seq)
                    }
                    if(mdProductSerialTpDeviceItem.apply_material != null){
                        put(APPLY_MATERIAL,mdProductSerialTpDeviceItem.apply_material)
                    }
                    put(VERIFICATION_INSTRUCTION,mdProductSerialTpDeviceItem.verification_instruction)

                    if(mdProductSerialTpDeviceItem.require_justify_problem > -1){
                        put(REQUIRE_JUSTIFY_PROBLEM,mdProductSerialTpDeviceItem.require_justify_problem)
                    }
                    if(mdProductSerialTpDeviceItem.critical_item > -1){
                        put(CRITICAL_ITEM,mdProductSerialTpDeviceItem.critical_item)
                    }
                    if(mdProductSerialTpDeviceItem.order_seq > -1){
                        put(ORDER_SEQ,mdProductSerialTpDeviceItem.order_seq)
                    }
                    if(mdProductSerialTpDeviceItem.structure > -1){
                        put(STRUCTURE,mdProductSerialTpDeviceItem.structure)
                    }
                    put(MANUAL_DESC,mdProductSerialTpDeviceItem.manual_desc)

                    if(mdProductSerialTpDeviceItem.next_cycle_measure != null){
                        put(NEXT_CYCLE_MEASURE,mdProductSerialTpDeviceItem.next_cycle_measure)
                    }
                    put(NEXT_CYCLE_MEASURE_DATE,mdProductSerialTpDeviceItem.next_cycle_measure_date)
                    put(NEXT_CYCLE_LIMIT_DATE,mdProductSerialTpDeviceItem.next_cycle_limit_date)
                    if(mdProductSerialTpDeviceItem.item_check_status != null){
                        put(ITEM_CHECK_STATUS,mdProductSerialTpDeviceItem.item_check_status)
                    }
                    put(TARGET_DATE,mdProductSerialTpDeviceItem.target_date)
                }
            }
            //
            return contentValues
        }
    }
}