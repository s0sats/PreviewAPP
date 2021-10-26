package com.namoadigital.prj001.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.core.database.getFloatOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.database.CursorToHMAuxMapper
import com.namoadigital.prj001.database.Mapper
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.sql.GeOsDeviceCreation_Sql_001
import com.namoadigital.prj001.sql.GeOsDeviceItemCreation_Sql_001
import com.namoadigital.prj001.sql.GeOsDeviceItemHistCreation_Sql_001
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class GeOsDao(
    context: Context,
    mDB_NAME: String,
    mDB_VERSION: Int
) : BaseDao(
    context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI
), DaoWithReturn<GeOs> {

    companion object{
        const val TABLE = "ge_os"
        const val CUSTOMER_CODE = "customer_code"
        const val CUSTOM_FORM_TYPE = "custom_form_type"
        const val CUSTOM_FORM_CODE = "custom_form_code"
        const val CUSTOM_FORM_VERSION = "custom_form_version"
        const val CUSTOM_FORM_DATA = "custom_form_data"
        const val ORDER_TYPE_CODE = "order_type_code"
        const val ORDER_TYPE_ID = "order_type_id"
        const val ORDER_TYPE_DESC = "order_type_desc"
        const val PROCESS_TYPE = "process_type"
        const val DISPLAY_OPTION = "display_option"
        const val BACKUP_PRODUCT_CODE = "backup_product_code"
        const val BACKUP_PRODUCT_ID = "backup_product_id"
        const val BACKUP_PRODUCT_DESC = "backup_product_desc"
        const val BACKUP_SERIAL_CODE = "backup_serial_code"
        const val BACKUP_SERIAL_ID = "backup_serial_id"
        const val MEASURE_TP_CODE = "measure_tp_code"
        const val MEASURE_TP_ID = "measure_tp_id"
        const val MEASURE_TP_DESC = "measure_tp_desc"
        const val MEASURE_VALUE = "measure_value"
        const val MEASURE_CYCLE_VALUE = "measure_cycle_value"
        const val VALUE_SUFIX = "value_sufix"
        const val RESTRICTION_DECIMAL = "restriction_decimal"
        const val VALUE_CYCLE_SIZE = "value_cycle_size"
        const val CYCLE_TOLERANCE = "cycle_tolerance"
        const val DATE_START = "date_start"
        const val DATE_END = "date_end"
        const val LAST_MEASURE_VALUE = "last_measure_value"
        const val LAST_MEASURE_DATE = "last_measure_date"
        const val LAST_CYCLE_VALUE = "last_cycle_value"
        const val SO_EDIT_START_END = "so_edit_start_end"
        const val SO_ORDER_TYPE_CODE_DEFAULT = "so_order_type_code_default"
        const val SO_ALLOW_CHANGE_ORDER_TYPE = "so_allow_change_order_type"
        const val SO_ALLOW_BACKUP = "so_allow_backup"
        const val DEVICE_TP_CODE_MAIN = "device_tp_code_main"

    }

    private val toGeOsMapper: Mapper<Cursor, GeOs>
    private val toContentValuesMapper: Mapper<GeOs, ContentValues>

    init {
        this.toGeOsMapper = CursorToGeOsMapper()
        this.toContentValuesMapper = GeOsToContentValuesMapper()
    }

    @Throws(java.lang.Exception::class)
    private fun getWherePkClause(item: GeOs?): StringBuilder{
        item?.let{
            return java.lang.StringBuilder()
                .append("""
                        ${CUSTOMER_CODE} = '${item.customer_code}'  
                        AND ${CUSTOM_FORM_TYPE} = '${item.custom_form_type}'                           
                        AND ${CUSTOM_FORM_CODE} = '${item.custom_form_code}'                           
                        AND ${CUSTOM_FORM_VERSION} = '${item.custom_form_version}'                           
                        AND ${CUSTOM_FORM_DATA} = '${item.custom_form_data}'                           
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }


    override fun addUpdate(item: GeOs?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()

        try {
            daoObjReturn.table = TABLE
            curAction = DaoObjReturn.UPDATE
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(item)
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(item), sbWhere.toString(), null).toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(item))
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

    override fun addUpdate(items: MutableList<GeOs>?, status: Boolean): DaoObjReturn {
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

            items?.forEach { item ->
                val sbWhere: StringBuilder = getWherePkClause(item)
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(item), sbWhere.toString(), null).toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(item))
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

    override fun getByString(sQuery: String?): GeOs? {
        var item: GeOs? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                item = toGeOsMapper.map(cursor)
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

    override fun query(sQuery: String?): MutableList<GeOs> {
        val items = mutableListOf<GeOs>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery, null)
            while (cursor.moveToNext()) {
                val uAux = toGeOsMapper.map(cursor)
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

    class CursorToGeOsMapper : Mapper<Cursor, GeOs> {
        override fun map(cursor: Cursor?): GeOs? {
            cursor?.let {
                with(cursor){
                    return GeOs(
                        customer_code = getLong(getColumnIndex(CUSTOMER_CODE)),
                        custom_form_type = getInt(getColumnIndex(CUSTOM_FORM_TYPE)),
                        custom_form_code = getInt(getColumnIndex(CUSTOM_FORM_CODE)),
                        custom_form_version = getInt(getColumnIndex(CUSTOM_FORM_VERSION)),
                        custom_form_data = getInt(getColumnIndex(CUSTOM_FORM_DATA)),
                        order_type_code = getInt(getColumnIndex(ORDER_TYPE_CODE)),
                        order_type_id = getString(getColumnIndex(ORDER_TYPE_ID)),
                        order_type_desc = getString(getColumnIndex(ORDER_TYPE_DESC)),
                        process_type = getString(getColumnIndex(PROCESS_TYPE)),
                        display_option = getString(getColumnIndex(DISPLAY_OPTION)),
                        backup_product_code = getIntOrNull(getColumnIndex(BACKUP_PRODUCT_CODE)),
                        backup_product_id = getStringOrNull(getColumnIndex(BACKUP_PRODUCT_ID)),
                        backup_product_desc = getStringOrNull(getColumnIndex(BACKUP_PRODUCT_DESC)),
                        backup_serial_code = getIntOrNull(getColumnIndex(BACKUP_SERIAL_CODE)),
                        backup_serial_id = getStringOrNull(getColumnIndex(BACKUP_SERIAL_ID)),
                        measure_tp_code = getIntOrNull(getColumnIndex(MEASURE_TP_CODE)),
                        measure_tp_id = getStringOrNull(getColumnIndex(MEASURE_TP_ID)),
                        measure_tp_desc = getStringOrNull(getColumnIndex(MEASURE_TP_DESC)),
                        measure_value = getFloatOrNull(getColumnIndex(MEASURE_VALUE)),
                        measure_cycle_value = getFloatOrNull(getColumnIndex(MEASURE_CYCLE_VALUE)),
                        value_sufix = getStringOrNull(getColumnIndex(VALUE_SUFIX)),
                        restriction_decimal = getIntOrNull(getColumnIndex(RESTRICTION_DECIMAL)),
                        value_cycle_size =  getFloatOrNull(getColumnIndex(VALUE_CYCLE_SIZE)),
                        cycle_tolerance = getIntOrNull(getColumnIndex(CYCLE_TOLERANCE)),
                        date_start = getStringOrNull(getColumnIndex(DATE_START)),
                        date_end = getStringOrNull(getColumnIndex(DATE_END)),
                        last_measure_value = getFloatOrNull(getColumnIndex(LAST_MEASURE_VALUE)),
                        last_measure_date = getStringOrNull(getColumnIndex(LAST_MEASURE_DATE)),
                        last_cycle_value = getFloatOrNull(getColumnIndex(LAST_CYCLE_VALUE)),
                        so_edit_start_end = getInt(getColumnIndex(SO_EDIT_START_END)),
                        so_order_type_code_default = getIntOrNull(getColumnIndex(SO_ORDER_TYPE_CODE_DEFAULT)),
                        so_allow_change_order_type = getInt(getColumnIndex(SO_ALLOW_CHANGE_ORDER_TYPE)),
                        so_allow_backup = getInt(getColumnIndex(SO_ALLOW_BACKUP)),
                        device_tp_code_main = getIntOrNull(getColumnIndex(DEVICE_TP_CODE_MAIN))
                    )
                }
            }
            return null
        }
    }

    class GeOsToContentValuesMapper : Mapper<GeOs, ContentValues> {
        override fun map(geOs: GeOs?): ContentValues {
            val contentValues = ContentValues()
            geOs?.let {
                with(contentValues){
                    if(it.customer_code > -1){
                        put(CUSTOMER_CODE, it.customer_code)
                    }
                    //
                    if(it.custom_form_type > -1){
                        put(CUSTOM_FORM_TYPE,it.custom_form_type)
                    }
                    //
                    put(CUSTOM_FORM_CODE,it.custom_form_code)
                    //
                    put(CUSTOM_FORM_VERSION,it.custom_form_version)
                    //
                    put(CUSTOM_FORM_DATA,it.custom_form_data)
                    //
                    put(ORDER_TYPE_CODE,it.order_type_code)
                    //
                    put(ORDER_TYPE_ID, it.order_type_id)
                    //
                    put(ORDER_TYPE_DESC, it.order_type_desc)
                    //
                    put(PROCESS_TYPE, it.process_type)
                    //
                    put(DISPLAY_OPTION , it.display_option)
                    //
                    put(BACKUP_PRODUCT_CODE, it.backup_product_code)
                    put(BACKUP_PRODUCT_ID, it.backup_product_id)
                    put(BACKUP_PRODUCT_DESC, it.backup_product_desc)
                    //
                    put(BACKUP_SERIAL_CODE, it.backup_serial_code)
                    put(BACKUP_SERIAL_ID, it.backup_serial_id)
                    //
                    put(MEASURE_TP_CODE, it.measure_tp_code)
                    //
                    put(MEASURE_TP_ID,it.measure_tp_id)
                    //
                    put(MEASURE_TP_DESC, it.measure_tp_desc)
                    //
                    put(MEASURE_VALUE, it.measure_value)
                    //
                    put(MEASURE_CYCLE_VALUE, it.measure_cycle_value)
                    //
                    put(VALUE_SUFIX, it.value_sufix)
                    //
                    put(RESTRICTION_DECIMAL, it.restriction_decimal)
                    //
                    put(VALUE_CYCLE_SIZE, it.value_cycle_size)
                    //
                    put(CYCLE_TOLERANCE, it.cycle_tolerance)
                    //
                    put(DATE_START,it.date_start)
                    //
                    put(DATE_END,it.date_end)
                    //
                    put(LAST_MEASURE_VALUE, it.last_measure_value)
                    //
                    put(LAST_MEASURE_DATE, it.last_measure_date)
                    //
                    put(LAST_CYCLE_VALUE, it.last_cycle_value)
                    //
                    put(SO_EDIT_START_END, it.so_edit_start_end)
                    //
                    put(SO_ORDER_TYPE_CODE_DEFAULT, it.so_order_type_code_default)
                    //
                    put(SO_ALLOW_CHANGE_ORDER_TYPE, it.so_allow_change_order_type)
                    //
                    put(SO_ALLOW_BACKUP, it.so_allow_backup)
                    //
                    put(DEVICE_TP_CODE_MAIN, it.device_tp_code_main)
                }
            }
            return contentValues
        }
    }

    fun createGeOsStructure(geOs: GeOs, mdSerial: MD_Product_Serial): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        val geOsDeviceDao = GeOsDeviceDao(context,ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM)
        val geOsDeviceItemDao = GeOsDeviceItemDao(context,ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM)
        val geOsDeviceItemHistDao = GeOsDeviceItemHistDao(context,ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM)
        //
        val geOsDevices = geOsDeviceDao.query(
            GeOsDeviceCreation_Sql_001(
                geOs.customer_code,
                geOs.custom_form_type,
                geOs.custom_form_code,
                geOs.custom_form_version,
                geOs.custom_form_data,
                mdSerial.product_code.toInt(),
                mdSerial.serial_code.toInt()
            ).toSqlQuery()
        )
        val geOsDeviceItens = geOsDeviceItemDao.query(
            GeOsDeviceItemCreation_Sql_001(
                geOs.customer_code,
                geOs.custom_form_type,
                geOs.custom_form_code,
                geOs.custom_form_version,
                geOs.custom_form_data,
                mdSerial.product_code.toInt(),
                mdSerial.serial_code.toInt(),
                geOs.value_sufix,
                geOs.restriction_decimal
            ).toSqlQuery()
        )
        val geOsDeviceItemHist = geOsDeviceItemHistDao.query(
            GeOsDeviceItemHistCreation_Sql_001(
                geOs.customer_code,
                geOs.custom_form_type,
                geOs.custom_form_code,
                geOs.custom_form_version,
                geOs.custom_form_data,
                mdSerial.product_code.toInt(),
                mdSerial.serial_code.toInt()
            ).toSqlQuery()
        )
        try {
            //Chama fun que fará a primeira e segunda varredura.
            checkScan(geOs, geOsDeviceItens)
        }catch (e: Exception){
            ToolBox_Inf.registerException(javaClass.name,e)
            return daoObjReturn.apply {
                setError(true)
                rawMessage = "Erro ao aplicar varreduras:\n ${e.message}"
            }
        }
        //
        openDB()
        try {
            db.beginTransaction()
            daoObjReturn = addUpdate(geOs)
            if (daoObjReturn.hasError()) {
                throw Exception(daoObjReturn.errorMsg)
            }
            daoObjReturn = geOsDeviceDao.addUpdate(geOsDevices,false,db)
            if (daoObjReturn.hasError()) {
                throw Exception(daoObjReturn.errorMsg)
            }
            daoObjReturn = geOsDeviceItemDao.addUpdate(geOsDeviceItens,false,db)
            if (daoObjReturn.hasError()) {
                throw Exception(daoObjReturn.errorMsg)
            }
            daoObjReturn =  geOsDeviceItemHistDao.addUpdate(geOsDeviceItemHist,false,db)
            if (daoObjReturn.hasError()) {
                throw Exception(daoObjReturn.errorMsg)
            }
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
        return daoObjReturn
    }

    @Throws(java.lang.Exception::class)
    private fun checkScan(
        geOs: GeOs,
        geOsDeviceItens: MutableList<GeOsDeviceItem>
    ) {
        //Chama primeira varredura que modifica o status baseado nas configuração do proximo ciclo
        //programado.
        firstScan(geOs, geOsDeviceItens)
        //Chama segunda varredura que modifica o status baseado na propriedade display_option do
        // tipo de O.S selecionada
        secondScan(geOs, geOsDeviceItens)
    }

    @Throws(java.lang.Exception::class)
    private fun firstScan(
        geOs: GeOs,
        geOsDeviceItens: MutableList<GeOsDeviceItem>
    ) {
        //Primeira varredura
        //Testa qual valor deve ser usado, measure_value ou measure_cycle_value(measure cycle só existe
        //se for preventiva). Modificar no futuro?! replicar o measure_value no measure_cycle_value
        // quando não for PREVENTIVE ?!
        val measureConsider: Float =
            if (geOs.measure_cycle_value != null && geOs.measure_cycle_value!!.compareTo(-1f) > 0) {
                geOs.measure_cycle_value!!
            } else {
                0f
            }
        //Seta data inseriada pelo usr com 23:59:59.
        var dateStartLastMinute : String? = ToolBox_Inf.getDateLastMinute(geOs.date_start)
        //
        geOsDeviceItens.forEach { item ->
            /*
             * Se Status Normal, verifica se deve alterar o status para:
             *   PROJECTED_DATE_REACHED:
             *      - Se data prevista do proximo ciclo(next_cycle_measure_date) for menor que a
             *      data de inicio até o ultimo minuto(dateStartLastMinute)
             *   LIMIT_DATE_REACHED:
             *      - Se data limite do proximo ciclo (next_cycle_limit_date) for menor que a
             *      data de inicio até o ultimo minuto(dateStartLastMinute)
             *   MEASURE_ALERT:
             *      - Se o valor do proximo ciclo(next_cycle_measure) for menor ou igual ao valor
             *      medido pelo usr measureConsider
             */
            if(GeOsDeviceItem.ITEM_CHECK_STATUS_NORMAL.equals(item.item_check_status,true)){
                var newCheckStatus = GeOsDeviceItem.ITEM_CHECK_STATUS_NORMAL
                //Verifica se data projetada do proximo ciclo foi atingida
                if (item.next_cycle_measure_date != null
                    && ToolBox_Inf.getDateDiferenceInMilliseconds(item.next_cycle_measure_date,dateStartLastMinute) < 0
                ) {
                    newCheckStatus = GeOsDeviceItem.ITEM_CHECK_STATUS_PROJECTED_DATE_REACHED
                }

                //Verifica se data limite do proximo ciclo foi atingida
                if (item.next_cycle_limit_date != null
                    && ToolBox_Inf.getDateDiferenceInMilliseconds(item.next_cycle_limit_date,dateStartLastMinute) < 0
                ) {
                    newCheckStatus = GeOsDeviceItem.ITEM_CHECK_STATUS_LIMIT_DATE_REACHED
                }

                //Se valor medido, maior que o proximo ciclo,muda status.(Maior prioridade)
                if (item.next_cycle_measure != null
                    && item.next_cycle_measure.compareTo(measureConsider) <= 0
                ) {
                    newCheckStatus = GeOsDeviceItem.ITEM_CHECK_STATUS_MEASURE_ALERT
                }

                item.item_check_status = newCheckStatus
            }
            /*
             * Se Status PROJECTED_DATE_REACHED, verifica se deve alterar o status para:
             *   NORMAL:
             *      - Se o data projetada foi alcançada, mas o valor da medido pelo usr, é inferior
             *  ao proximo ciclo previsto(next_cycle_measure)
             */
            if(GeOsDeviceItem.ITEM_CHECK_STATUS_PROJECTED_DATE_REACHED.equals(item.item_check_status,true)){
                if (item.next_cycle_measure != null
                    && item.next_cycle_measure.compareTo(measureConsider) > 0
                ) {
                    item.item_check_status = GeOsDeviceItem.ITEM_CHECK_STATUS_NORMAL
                }
            }

            /*
           * Se Status LIMIT_DATE_REACHED, verifica se deve alterar o status para:
           *   NORMAL:
           *      - Se data de limite(next_cycle_limit_date) for maior que  data de inicio
           *  até o ultimo minuto(dateStartLastMinute)
           */
            if(GeOsDeviceItem.ITEM_CHECK_STATUS_LIMIT_DATE_REACHED.equals(item.item_check_status,true)){
                if (item.next_cycle_limit_date != null && geOs.date_start != null
                    && ToolBox_Inf.getDateDiferenceInMilliseconds(item.next_cycle_limit_date,dateStartLastMinute) > 0
                ) {
                    item.item_check_status = GeOsDeviceItem.ITEM_CHECK_STATUS_NORMAL
                }
            }
        }
    }

    /**
     * Fun que utiliza a propriedade DISPLAY_OPTION da MdOrderType selecionada para aplicar a visibilidade
     * no itens
     * Se SHOW_ALL:
     *   Pega os itens com status NORMAL, modifica para FORCED e remove a criticiade do item.
     * Se SHOW_ONLY_CRITICAL:
     *   Pega os itens com status NO_CYCLE, MEASURE_ALERT ,PROJECTED_DATE_REACHED e LIMIT_DATE_REACHED
     *   e rebaixa para o status NORMAL
     *
     */
    @Throws(java.lang.Exception::class)
    private fun secondScan(geOs: GeOs, geOsDeviceItens: MutableList<GeOsDeviceItem>) {
        geOsDeviceItens.forEach { item ->
            when(geOs.display_option){
                //Se show all, pega os itens em status normal e
                MdOrderType.DISPLAY_OPTION_SHOW_ALL ->{
                    if(item.item_check_status.equals(GeOsDeviceItem.ITEM_CHECK_STATUS_NORMAL ,true)){
                        item.item_check_status = GeOsDeviceItem.ITEM_CHECK_STATUS_FORCED
                        item.critical_item = 0
                    }

                }
                MdOrderType.DISPLAY_OPTION_SHOW_ONLY_CRITICAL ->{
                    if(item.critical_item == 0
                        && (    item.item_check_status.equals(GeOsDeviceItem.ITEM_CHECK_STATUS_NO_CYCLE ,true)
                                || item.item_check_status.equals(GeOsDeviceItem.ITEM_CHECK_STATUS_MEASURE_ALERT ,true)
                                || item.item_check_status.equals(GeOsDeviceItem.ITEM_CHECK_STATUS_PROJECTED_DATE_REACHED ,true)
                                || item.item_check_status.equals(GeOsDeviceItem.ITEM_CHECK_STATUS_LIMIT_DATE_REACHED ,true)
                        )
                    ){
                        item.item_check_status = GeOsDeviceItem.ITEM_CHECK_STATUS_NORMAL
                    }
                }
                else ->{}
            }

        }
    }

    private fun isNextCycleLimiteDateInFuture(
        next_cycle_limit_date: String,
        date_start: String
    ) = ToolBox_Inf.getDateDiferenceInMilliseconds(next_cycle_limit_date, date_start) > 0

    fun removeFull(geOs: GeOs): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        val curAction = DaoObjReturn.DELETE
        daoObjReturn.table = TABLE
        //
        val wherePkClause = getWherePkClause(geOs).toString()

        openDB()
        try {
            db.beginTransaction()
            //
            addUpdateRet += db.delete(TABLE,wherePkClause,null)
            addUpdateRet += db.delete(GeOsDeviceDao.TABLE,wherePkClause,null)
            addUpdateRet += db.delete(GeOsDeviceItemDao.TABLE,wherePkClause,null)
            addUpdateRet += db.delete(GeOsDeviceMaterialDao.TABLE,wherePkClause,null)
            addUpdateRet += db.delete(GeOsDeviceItemHistDao.TABLE,wherePkClause,null)
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
        return daoObjReturn
    }
}