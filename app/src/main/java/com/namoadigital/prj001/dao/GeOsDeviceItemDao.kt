package com.namoadigital.prj001.dao

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import androidx.core.database.getFloatOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.database.CursorToHMAuxMapper
import com.namoadigital.prj001.database.Mapper
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItemAutomaticSelectionState
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItemStatusColor
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItemStatusModificationType
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceMaterial
import com.namoadigital.prj001.sql.GeOsDeviceItem_Sql_006
import com.namoadigital.prj001.sql.GeOsDeviceMaterialSql_002
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class GeOsDeviceItemDao(
    context: Context,
    mDB_NAME: String = ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
    mDB_VERSION: Int = Constant.DB_VERSION_CUSTOM
) : BaseDao(
    context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI
), DaoWithReturn<GeOsDeviceItem> {
    companion object {
        const val TABLE = "ge_os_device_item"
        const val CUSTOMER_CODE = "customer_code"
        const val CUSTOM_FORM_TYPE = "custom_form_type"
        const val CUSTOM_FORM_CODE = "custom_form_code"
        const val CUSTOM_FORM_VERSION = "custom_form_version"
        const val CUSTOM_FORM_DATA = "custom_form_data"
        const val PRODUCT_CODE = "product_code"
        const val SERIAL_CODE = "serial_code"
        const val DEVICE_TP_CODE = "device_tp_code"
        const val ITEM_CHECK_CODE = "item_check_code"
        const val ITEM_CHECK_SEQ = "item_check_seq"
        const val ITEM_CHECK_ID = "item_check_id"
        const val ITEM_CHECK_DESC = "item_check_desc"
        const val ITEM_CHECK_DESC_ALT_VG = "item_check_desc_alt_vg"
        const val ITEM_CHECK_GROUP_CODE = "item_check_group_code"
        const val APPLY_MATERIAL = "apply_material"
        const val VERIFICATION_INSTRUCTION = "verification_instruction"
        const val REQUIRE_JUSTIFY_PROBLEM = "require_justify_problem"
        const val CRITICAL_ITEM = "critical_item"
        const val CHANGE_ADJUST = "change_adjust"
        const val ORDER_SEQ = "order_seq"
        const val STRUCTURE = "structure"
        const val ALREADY_OK_HIDE = "already_ok_hide"
        const val REQUIRE_PHOTO_FIXED = "require_photo_fixed"
        const val REQUIRE_PHOTO_ALERT = "require_photo_alert"
        const val REQUIRE_PHOTO_ALREADY_OK = "require_photo_already_ok"
        const val REQUIRE_PHOTO_NOT_VERIFIED = "require_photo_not_verified"
        const val VG_CODE = "vg_code"
        const val MANUAL_DESC = "manual_desc"
        const val NEXT_CYCLE_MEASURE = "next_cycle_measure"
        const val NEXT_CYCLE_MEASURE_DATE = "next_cycle_measure_date"
        const val NEXT_CYCLE_LIMIT_DATE = "next_cycle_limit_date"
        const val VALUE_SUFIX = "value_sufix"
        const val RESTRICTION_DECIMAL = "restriction_decimal"
        const val ITEM_CHECK_STATUS = "item_check_status"
        const val TARGET_DATE = "target_date"
        const val EXEC_TYPE = "exec_type"
        const val EXEC_DATE = "exec_date"
        const val EXEC_COMMENT = "exec_comment"
        const val EXEC_PHOTO1 = "exec_photo1"
        const val EXEC_PHOTO2 = "exec_photo2"
        const val EXEC_PHOTO3 = "exec_photo3"
        const val EXEC_PHOTO4 = "exec_photo4"
        const val STATUS_ANSWER = "status_answer"
        const val HAS_EXPIRED_CYCLE = "has_expired_cycle"
        const val HIDE_DAYS_IN_ALERT = "hide_days_in_alert"
        const val PARTITIONED_EXECUTION = "partitioned_execution"
        const val TICKET_PREFIX = "ticket_prefix"
        const val TICKET_CODE = "ticket_code"
        const val VG_ACTION = "vg_action"
        const val IS_VISIBLE = "is_visible"
        const val COLOR_ITEM = "color_item"
        const val STATUS_MODIFICATION_TYPE = "status_modification_type"
        const val AUTOMATIC_SELECTION_STATE = "automatic_selection_state"
    }

    private val toGeOsDeviceItemMapper: Mapper<Cursor, GeOsDeviceItem>
    private val toContentValuesMapper: Mapper<GeOsDeviceItem, ContentValues>

    init {
        this.toGeOsDeviceItemMapper = CursorToGeOsDeviceItemMapper()
        this.toContentValuesMapper = GeOsDeviceItemToContentValuesMapper()
    }

    @Throws(java.lang.Exception::class)
    private fun getWherePkClause(item: GeOsDeviceItem?): StringBuilder {
        item?.let {
            return java.lang.StringBuilder()
                .append(
                    """
                        $CUSTOMER_CODE = '${item.customer_code}'  
                        AND $CUSTOM_FORM_TYPE = '${item.custom_form_type}'                           
                        AND $CUSTOM_FORM_CODE = '${item.custom_form_code}'                           
                        AND $CUSTOM_FORM_VERSION = '${item.custom_form_version}'                           
                        AND $CUSTOM_FORM_DATA = '${item.custom_form_data}'                           
                        AND $PRODUCT_CODE = '${item.product_code}'                           
                        AND $SERIAL_CODE = '${item.serial_code}'                           
                        AND $DEVICE_TP_CODE = '${item.device_tp_code}'                           
                        AND $ITEM_CHECK_CODE = '${item.item_check_code}'                           
                        AND $ITEM_CHECK_SEQ = '${item.item_check_seq}'                           
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    override fun addUpdate(item: GeOsDeviceItem?): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        var curAction = DaoObjReturn.INSERT_OR_UPDATE
        //
        openDB()
        //
        try {
            daoObjReturn.table = TABLE
            curAction = DaoObjReturn.UPDATE
            //
            db.beginTransaction()
            //
            //Where para update
            val sbWhere: StringBuilder = getWherePkClause(item)
            //Tenta update e armazena retorno
            addUpdateRet =
                db.update(TABLE, toContentValuesMapper.map(item), sbWhere.toString(), null).toLong()
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0L) {
                curAction = DaoObjReturn.INSERT
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(item))
            }
            //
            item?.materialList.let {
                daoObjReturn =
                    tryAddUpdateMaterials(item!!, it ?: mutableListOf<GeOsDeviceMaterial>(), db)
                //Se erro durante insert, dispara exception abortando o processamento.
                if (daoObjReturn.hasError()) {
                    throw java.lang.Exception(daoObjReturn.rawMessage)
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


    override fun addUpdate(items: MutableList<GeOsDeviceItem>?, status: Boolean): DaoObjReturn {
        return addUpdate(items, status, null)
    }

    fun addUpdate(
        items: MutableList<GeOsDeviceItem>?,
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
        //
        try {
            daoObjReturn.table = TABLE
            curAction = DaoObjReturn.UPDATE
            //
            if (dbInstance == null) {
                db.beginTransaction()
            }

            if (status) {
                db.delete(TABLE, null, null)
            }

            items?.forEach { item ->
                val sbWhere: StringBuilder = getWherePkClause(item)
                //Tenta update e armazena retorno
                addUpdateRet =
                    db.update(TABLE, toContentValuesMapper.map(item), sbWhere.toString(), null)
                        .toLong()
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0L) {
                    curAction = DaoObjReturn.INSERT
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(item))
                }
                daoObjReturn = tryAddUpdateMaterials(item!!, item.materialList, db)
                //Se erro durante insert, dispara exception abortando o processamento.
                if (daoObjReturn.hasError()) {
                    throw java.lang.Exception(daoObjReturn.rawMessage)
                }
            }
            //
            //Se db não foi passado, finaliza transaction com sucesso
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

    //
    private fun tryAddUpdateMaterials(
        geOsDeviceItem: GeOsDeviceItem,
        materials: MutableList<GeOsDeviceMaterial>,
        db: SQLiteDatabase
    ): DaoObjReturn {
        val geOsDeviceMaterialDao = geOsDeviceMaterialDao()
        //Tenta remover a lista atual
        val daoObjReturn = geOsDeviceMaterialDao.removeAllForGeOsDeviceItem(
            getWherePkClause(geOsDeviceItem).toString(),
            db
        )
        //Se erro, reporta
        if (daoObjReturn.hasError()) {
            return daoObjReturn
        }
        //Senão tem erro, atualiza.
        return geOsDeviceMaterialDao.addUpdate(materials, false, db)
    }


    private fun geOsDeviceMaterialDao(): GeOsDeviceMaterialDao {
        return GeOsDeviceMaterialDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        )
    }

    //
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

    override fun getByString(sQuery: String?): GeOsDeviceItem? {
        var item: GeOsDeviceItem? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery!!, null)
            while (cursor.moveToNext()) {
                item = toGeOsDeviceItemMapper.map(cursor)
                item?.apply {
                    getMaterialList(this)
                }
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

    private fun getMaterialList(geOsDeviceItem: GeOsDeviceItem) {
        val geOsDeviceMaterialDao = geOsDeviceMaterialDao()
        geOsDeviceItem.materialList.clear()
        //
        geOsDeviceItem
            .materialList
            .addAll(
                geOsDeviceMaterialDao.query(
                    GeOsDeviceMaterialSql_002(
                        geOsDeviceItem.customer_code,
                        geOsDeviceItem.custom_form_type,
                        geOsDeviceItem.custom_form_code,
                        geOsDeviceItem.custom_form_version,
                        geOsDeviceItem.custom_form_data,
                        geOsDeviceItem.product_code,
                        geOsDeviceItem.serial_code,
                        geOsDeviceItem.device_tp_code,
                        geOsDeviceItem.item_check_code,
                        geOsDeviceItem.item_check_seq
                    ).toSqlQuery()
                )
            )
    }

    override fun getByStringHM(sQuery: String?): HMAux? {
        var item: HMAux? = null
        openDB()
        try {
            val cursor = db.rawQuery(sQuery!!, null)
            while (cursor.moveToNext()) {
                item = CursorToHMAuxMapper.mapN(cursor)
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
        }
        closeDB()
        return item
    }

    override fun query(sQuery: String?): MutableList<GeOsDeviceItem> {
        val items = mutableListOf<GeOsDeviceItem>()
        openDB()
        try {
            val cursor = db.rawQuery(sQuery!!, null)
            while (cursor.moveToNext()) {
                val uAux = toGeOsDeviceItemMapper.map(cursor)
                uAux?.apply {
                    getMaterialList(this)
                }
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

    /**
     * Fun que remove o item e os materiais ela vinculados.
     */
    fun removeFull(geOsDeviceItem: GeOsDeviceItem): DaoObjReturn {
        var daoObjReturn = DaoObjReturn()
        var addUpdateRet: Long = 0
        val curAction = DaoObjReturn.DELETE
        daoObjReturn.table = TABLE
        //
        val wherePkClause = getWherePkClause(geOsDeviceItem).toString()

        openDB()
        try {
            db.beginTransaction()
            //
            addUpdateRet += db.delete(TABLE, wherePkClause, null)
            addUpdateRet += db.delete(GeOsDeviceMaterialDao.TABLE, wherePkClause, null)
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

    fun getListDeviceItemByVgCode(
        customFormType: Int,
        customFormCode: Int,
        customFormVersion: Int,
        customFormData: Long,
        vgCode: Int,
    ): List<GeOsDeviceItem> {
        return query(
            """
                SELECT * FROM $TABLE
                WHERE $VG_CODE = $vgCode
                AND $CUSTOM_FORM_TYPE = $customFormType
                AND $CUSTOM_FORM_CODE = $customFormCode
                AND $CUSTOM_FORM_VERSION = $customFormVersion
                AND $CUSTOM_FORM_DATA = $customFormData
            """.trimIndent()
        )
    }

    fun getListDeviceItemById(
        customerCode: Long,
        formType: Int,
        formCode: Int,
        formVersion: Int,
        formData: Long,
        productCode: Long,
        serialCode: Long,
        deviceTpCode: Int
    ): List<GeOsDeviceItem>? {

        return query(
            GeOsDeviceItem_Sql_006(
                customerCode,
                formType,
                formCode,
                formVersion,
                formData.toInt(),
                productCode,
                serialCode,
                deviceTpCode
            ).toSqlQuery()
        )

    }

    fun getListDeviceItemByForm(
        customerCode: Long,
        formType: Int,
        formCode: Int,
        formVersion: Int,
        formData: Long,
        productCode: Long,
        serialCode: Long,
    ): List<GeOsDeviceItem> {
        return query(
            sQuery = """
                            SELECT * FROM $TABLE
                            WHERE ${CUSTOMER_CODE} = $customerCode
                            AND $CUSTOM_FORM_TYPE = $formType
                            AND $CUSTOM_FORM_CODE = $formCode
                            AND $CUSTOM_FORM_VERSION = $formVersion
                            AND $CUSTOM_FORM_DATA = $formData
                            AND $PRODUCT_CODE = $productCode
                            AND $SERIAL_CODE = $serialCode
                        """.trimIndent()
        )
    }

    class CursorToGeOsDeviceItemMapper : Mapper<Cursor, GeOsDeviceItem> {
        @SuppressLint("Range")
        override fun map(cursor: Cursor?): GeOsDeviceItem? {
            cursor?.let {
                with(cursor) {
                    return GeOsDeviceItem(
                        customer_code = getLong(getColumnIndex(CUSTOMER_CODE)),
                        custom_form_type = getInt(getColumnIndex(CUSTOM_FORM_TYPE)),
                        custom_form_code = getInt(getColumnIndex(CUSTOM_FORM_CODE)),
                        custom_form_version = getInt(getColumnIndex(CUSTOM_FORM_VERSION)),
                        custom_form_data = getInt(getColumnIndex(CUSTOM_FORM_DATA)),
                        product_code = getInt(getColumnIndex(PRODUCT_CODE)),
                        serial_code = getInt(getColumnIndex(SERIAL_CODE)),
                        device_tp_code = getInt(getColumnIndex(DEVICE_TP_CODE)),
                        item_check_code = getInt(getColumnIndex(ITEM_CHECK_CODE)),
                        item_check_seq = getInt(getColumnIndex(ITEM_CHECK_SEQ)),
                        item_check_id = getString(getColumnIndex(ITEM_CHECK_ID)),
                        item_check_desc = getString(getColumnIndex(ITEM_CHECK_DESC)),
                        item_check_desc_alt_vg = getString(getColumnIndex(ITEM_CHECK_DESC_ALT_VG)),
                        item_check_group_code = getIntOrNull(getColumnIndex(ITEM_CHECK_GROUP_CODE)),
                        apply_material = getString(getColumnIndex(APPLY_MATERIAL)),
                        verification_instruction = getStringOrNull(
                            getColumnIndex(
                                VERIFICATION_INSTRUCTION
                            )
                        ),
                        require_justify_problem = getInt(getColumnIndex(REQUIRE_JUSTIFY_PROBLEM)),
                        critical_item = getInt(getColumnIndex(CRITICAL_ITEM)),
                        change_adjust = getInt(getColumnIndex(CHANGE_ADJUST)),
                        order_seq = getInt(getColumnIndex(ORDER_SEQ)),
                        structure = getInt(getColumnIndex(STRUCTURE)),
                        already_ok_hide = getInt(getColumnIndex(ALREADY_OK_HIDE)),
                        require_photo_fixed = getInt(getColumnIndex(REQUIRE_PHOTO_FIXED)),
                        require_photo_alert = getInt(getColumnIndex(REQUIRE_PHOTO_ALERT)),
                        require_photo_already_ok = getInt(getColumnIndex(REQUIRE_PHOTO_ALREADY_OK)),
                        require_photo_not_verified = getInt(
                            getColumnIndex(
                                REQUIRE_PHOTO_NOT_VERIFIED
                            )
                        ),
                        vg_code = getIntOrNull(getColumnIndex(VG_CODE)),
                        manual_desc = getStringOrNull(getColumnIndex(MANUAL_DESC)),
                        next_cycle_measure = getFloatOrNull(getColumnIndex(NEXT_CYCLE_MEASURE)),
                        next_cycle_measure_date = getStringOrNull(
                            getColumnIndex(
                                NEXT_CYCLE_MEASURE_DATE
                            )
                        ),
                        next_cycle_limit_date = getStringOrNull(getColumnIndex(NEXT_CYCLE_LIMIT_DATE)),
                        value_sufix = getStringOrNull(getColumnIndex(VALUE_SUFIX)),
                        restriction_decimal = getIntOrNull(getColumnIndex(RESTRICTION_DECIMAL)),
                        item_check_status = getString(getColumnIndex(ITEM_CHECK_STATUS)),
                        target_date = getStringOrNull(getColumnIndex(TARGET_DATE)),
                        exec_type = getStringOrNull(getColumnIndex(EXEC_TYPE)),
                        exec_date = getStringOrNull(getColumnIndex(EXEC_DATE)),
                        exec_comment = getStringOrNull(getColumnIndex(EXEC_COMMENT)),
                        exec_photo1 = getStringOrNull(getColumnIndex(EXEC_PHOTO1)),
                        exec_photo2 = getStringOrNull(getColumnIndex(EXEC_PHOTO2)),
                        exec_photo3 = getStringOrNull(getColumnIndex(EXEC_PHOTO3)),
                        exec_photo4 = getStringOrNull(getColumnIndex(EXEC_PHOTO4)),
                        status_answer = getStringOrNull(getColumnIndex(STATUS_ANSWER)),
                        has_expired_cycle = getInt(getColumnIndex(HAS_EXPIRED_CYCLE)),
                        hide_days_in_alert = getInt(getColumnIndex(HIDE_DAYS_IN_ALERT)),
                        partitioned_execution = getInt(getColumnIndex(PARTITIONED_EXECUTION)),
                        ticket_prefix = getIntOrNull(getColumnIndex(TICKET_PREFIX)),
                        ticket_code = getIntOrNull(getColumnIndex(TICKET_CODE)),
                        vg_action = getInt(getColumnIndex(VG_ACTION)),
                        is_visible = getInt(getColumnIndex(IS_VISIBLE)),
                        color_item = GeOsDeviceItemStatusColor.getStatusColor(
                            getStringOrNull(
                                getColumnIndex(COLOR_ITEM)
                            )
                        ),
                        status_modification_type = GeOsDeviceItemStatusModificationType.getModificationType(
                            getStringOrNull(getColumnIndex(STATUS_MODIFICATION_TYPE))
                        ),
                        automatic_selection_state = GeOsDeviceItemAutomaticSelectionState.getAutomaticSelection(
                            getStringOrNull(getColumnIndex(AUTOMATIC_SELECTION_STATE))
                        ),
                    )
                }
            }
            return null
        }
    }

    //
    class GeOsDeviceItemToContentValuesMapper : Mapper<GeOsDeviceItem, ContentValues> {
        override fun map(geOs: GeOsDeviceItem?): ContentValues {
            val contentValues = ContentValues()
            geOs?.let {
                with(contentValues) {
                    if (it.customer_code > -1) {
                        put(CUSTOMER_CODE, it.customer_code)
                    }
                    //
                    if (it.custom_form_type > -1) {
                        put(CUSTOM_FORM_TYPE, it.custom_form_type)
                    }
                    //
                    put(CUSTOM_FORM_CODE, it.custom_form_code)
                    //
                    put(CUSTOM_FORM_VERSION, it.custom_form_version)
                    //
                    put(CUSTOM_FORM_DATA, it.custom_form_data)
                    //
                    put(PRODUCT_CODE, it.product_code)
                    //
                    put(SERIAL_CODE, it.serial_code)
                    //
                    put(DEVICE_TP_CODE, it.device_tp_code)
                    //
                    put(ITEM_CHECK_CODE, it.item_check_code)
                    //
                    put(ITEM_CHECK_SEQ, it.item_check_seq)
                    //
                    put(ITEM_CHECK_ID, it.item_check_id)
                    //
                    put(ITEM_CHECK_DESC, it.item_check_desc)
                    //
                    put(ITEM_CHECK_DESC_ALT_VG, it.item_check_desc_alt_vg)
                    //
                    put(ITEM_CHECK_GROUP_CODE, it.item_check_group_code)
                    //
                    put(APPLY_MATERIAL, it.apply_material)
                    //
                    put(VERIFICATION_INSTRUCTION, it.verification_instruction)
                    //
                    put(REQUIRE_JUSTIFY_PROBLEM, it.require_justify_problem)
                    //
                    put(CRITICAL_ITEM, it.critical_item)
                    //
                    put(CHANGE_ADJUST, it.change_adjust)
                    //
                    put(ORDER_SEQ, it.order_seq)
                    //
                    put(STRUCTURE, it.structure)
                    //
                    if (it.already_ok_hide > -1) {
                        put(ALREADY_OK_HIDE, it.already_ok_hide)
                    }
                    if (it.require_photo_fixed > -1) {
                        put(REQUIRE_PHOTO_FIXED, it.require_photo_fixed)
                    }
                    if (it.require_photo_alert > -1) {
                        put(REQUIRE_PHOTO_ALERT, it.require_photo_alert)
                    }
                    if (it.require_photo_already_ok > -1) {
                        put(REQUIRE_PHOTO_ALREADY_OK, it.require_photo_already_ok)
                    }
                    if (it.require_photo_not_verified > -1) {
                        put(REQUIRE_PHOTO_NOT_VERIFIED, it.require_photo_not_verified)
                    }

                    put(VG_CODE, it.vg_code)
                    //
                    put(MANUAL_DESC, it.manual_desc)
                    //
                    put(NEXT_CYCLE_MEASURE, it.next_cycle_measure)
                    //
                    put(NEXT_CYCLE_MEASURE_DATE, it.next_cycle_measure_date)
                    //
                    put(NEXT_CYCLE_LIMIT_DATE, it.next_cycle_limit_date)
                    //
                    put(VALUE_SUFIX, it.value_sufix)
                    //
                    put(RESTRICTION_DECIMAL, it.restriction_decimal)
                    //
                    put(ITEM_CHECK_STATUS, it.item_check_status)
                    //
                    put(TARGET_DATE, it.target_date)
                    //
                    put(EXEC_TYPE, it.exec_type)
                    //
                    put(EXEC_DATE, it.exec_date)
                    //
                    put(EXEC_COMMENT, it.exec_comment)
                    //
                    put(EXEC_PHOTO1, it.exec_photo1)
                    //
                    put(EXEC_PHOTO2, it.exec_photo2)
                    //
                    put(EXEC_PHOTO3, it.exec_photo3)
                    //
                    put(EXEC_PHOTO4, it.exec_photo4)
                    //
                    put(STATUS_ANSWER, it.status_answer)
                    //
                    put(HAS_EXPIRED_CYCLE, it.has_expired_cycle)
                    //
                    put(HIDE_DAYS_IN_ALERT, it.hide_days_in_alert)
                    //
                    put(PARTITIONED_EXECUTION, it.partitioned_execution)
                    //
                    put(TICKET_PREFIX, it.ticket_prefix)
                    //
                    put(TICKET_CODE, it.ticket_code)
                    if (it.vg_action > -1) {
                        put(VG_ACTION, it.vg_action)
                    }
                    //
                    if (it.is_visible > -1) {
                        put(IS_VISIBLE, it.is_visible)
                    }
                    //
                    put(COLOR_ITEM, it.color_item.toString())
                    //
                    put(STATUS_MODIFICATION_TYPE, it.status_modification_type.toString())
                    //
                    put(AUTOMATIC_SELECTION_STATE, it.automatic_selection_state.toString())
                    //
                }
            }
            return contentValues
        }
    }
}