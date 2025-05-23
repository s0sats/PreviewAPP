package com.namoadigital.prj001.ui.act087

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.core.form_os.domain.usecase.GeOsCreateFormOsStructureUseCase
import com.namoadigital.prj001.core.form_os.domain.usecase.GeOsCreateFormOsStructureUseCase.Input
import com.namoadigital.prj001.core.form_os.domain.usecase.GeOsSaveSerialStructureUseCase
import com.namoadigital.prj001.dao.GE_Custom_FormDao
import com.namoadigital.prj001.dao.GE_Custom_Form_Blob_LocalDao
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao
import com.namoadigital.prj001.dao.GeOsDao
import com.namoadigital.prj001.dao.MD_ProductDao
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao
import com.namoadigital.prj001.dao.MdOrderTypeDao
import com.namoadigital.prj001.dao.MeMeasureTpDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TK_Ticket_FormDao
import com.namoadigital.prj001.extensions.roundByRestrictionMeasure
import com.namoadigital.prj001.extensions.watchStatus
import com.namoadigital.prj001.model.BaseSerialSearchItem
import com.namoadigital.prj001.model.GE_Custom_Form
import com.namoadigital.prj001.model.GE_Custom_Form_Local
import com.namoadigital.prj001.model.MD_Product
import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.model.MD_Schedule_Exec
import com.namoadigital.prj001.model.MdOrderType
import com.namoadigital.prj001.model.MeMeasureTp
import com.namoadigital.prj001.model.TK_Ticket_Form
import com.namoadigital.prj001.model.T_MD_Product_Serial_Backup_Rec
import com.namoadigital.prj001.model.T_MD_Product_Serial_Backup_Record
import com.namoadigital.prj001.model.masterdata.ge_os.GeOs
import com.namoadigital.prj001.receiver.WBR_Product_Serial_Backup
import com.namoadigital.prj001.service.SO_PRODUCT_CODE
import com.namoadigital.prj001.service.SO_SERIAL_CODE
import com.namoadigital.prj001.service.WS_Product_Serial_Backup
import com.namoadigital.prj001.sql.Act087Sql_001
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_002
import com.namoadigital.prj001.sql.GE_Custom_Form_Sql_001_TT
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_002
import com.namoadigital.prj001.sql.MD_Product_Sql_001
import com.namoadigital.prj001.sql.MD_Schedule_Exec_Sql_001
import com.namoadigital.prj001.sql.MdOrderTypeSql_001
import com.namoadigital.prj001.sql.MdOrderTypeSql_002
import com.namoadigital.prj001.sql.MeMeasureTpSql_001
import com.namoadigital.prj001.sql.TK_Ticket_Form_Sql_002
import com.namoadigital.prj001.ui.act011.finish_os.di.model.ResponsibleStop
import com.namoadigital.prj001.ui.act087.model.InitialSerialState
import com.namoadigital.prj001.ui.act087.model.LastMeasureDataConsider
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ScheduleFormFatory
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale

class Act087MainPresenter(
    private val context: Context,
    private val mView: Act087MainContract.I_View,
    private val mModule_Code: String,
    private val mResource_Code: String,
    private val customFormType: Int,
    private val customFormCode: Int,
    private val customFormVersion: Int,
    private val productCode: Int,
    private val serialId: String,
    private val productDao: MD_ProductDao,
    private val serialDao: MD_Product_SerialDao,
    private val formDao: GE_Custom_FormDao,
    private val geOsDao: GeOsDao,
    private val orderTypeDao: MdOrderTypeDao,
    private val measureTpDao: MeMeasureTpDao,
    private val schedulePrefix: Int?,
    private val scheduleCode: Int?,
    private val scheduleExec: Int?,
    private val scheduleDao: MD_Schedule_ExecDao,
    private val ticketDao: TK_TicketDao,
    private val originFlow: String = ConstantBaseApp.ACT005,
    private var custom_form_dataDao: GE_Custom_Form_DataDao,
    private var ticketPrefix: Int?,
    private var ticketCode: Int?,
    private var ticketSeqTmp: Int?,
    private var stepCode: Int?,
    private var mCustomFormDataPartition: Int?,
    val geOsCreateFormOsStructureUseCase: GeOsCreateFormOsStructureUseCase,
    val geOsSaveSerialStructureUseCase: GeOsSaveSerialStructureUseCase,
) : Act087MainContract.I_Presenter {

    private val hmAuxTrans: HMAux by lazy {
        loadTranslation()
    }

    private val serialObj: MD_Product_Serial by lazy {
        getSerialObj(productCode, serialId)
    }

    private val mScheduleObj: MD_Schedule_Exec? by lazy {
        scheduleDao.getByString(
            MD_Schedule_Exec_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                schedulePrefix!!,
                scheduleCode!!,
                scheduleExec!!
            ).toSqlQuery()
        )
    }

    private val _mCustomForm: GE_Custom_Form? by lazy {
        getForm(customFormCode, customFormType, customFormVersion)
    }

    //Retorna o form sem ser null. Como só chega aqui se ele for existir, evita tratativa de safe call
    private val mCustomForm get() = _mCustomForm!!

    private fun loadTranslation(): HMAux {
        val transList: MutableList<String> = mutableListOf(
            "act087_title",
            "alert_error_on_create_os_form_ttl",
            "alert_error_on_create_os_form_msg",
            "alert_bkp_serial_error_ttl",
            "alert_no_bkp_serial_found_offline_msg",
            "alert_no_bkp_serial_found_msg",
            "alert_error_on_open_bkp_list_msg",
            "alert_error_no_data_return_msg",
            "dialog_bkp_machine_search_ttl",
            "dialog_bkp_machine_search_start",
            "alert_form_parameter_error_ttl",
            "alert_form_parameter_error_msg",
            "alert_unsaved_data_will_be_lost_ttl",
            "alert_unsaved_data_will_be_lost_confirm",
            "alert_schedule_not_found_ttl",
            "alert_schedule_not_found_msg",
            "alert_error_on_create_schedule_ttl",
            "alert_error_on_create_schedule_msg",
            "alert_form_not_found_ttl",
            "alert_form_not_found_msg",
        )
        //
        val actAuxTrans = ToolBox_Inf.setLanguage(
            context,
            mModule_Code,
            mResource_Code,
            ToolBox_Con.getPreference_Translate_Code(context),
            transList
        )
        //
        val formOsFragTransient = ToolBox_Inf.setLanguage(
            context,
            mModule_Code,
            ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                FormOsHeaderFrg.mResource_Name
            ),
            ToolBox_Con.getPreference_Translate_Code(context),
            FormOsHeaderFrg.getFragTranslationsVars()
        )
        //
        actAuxTrans.putAll(formOsFragTransient)
        //
        return actAuxTrans
    }

    override fun getTranslation(): HMAux {
        return hmAuxTrans
    }

    override fun validateBundleParams(): Boolean {
        if (customFormType > -1 && customFormCode > -1 && customFormVersion > -1 && productCode > -1 && serialId.isNotEmpty()) {
            return true
        }
        return false
    }

    override fun isSchedule(): Boolean {
        return schedulePrefix ?: -1 > 0
                && scheduleCode ?: -1 > 0
                && scheduleExec ?: -1 > 0
    }

    override fun getScheduleExecObj(): MD_Schedule_Exec? {
        return mScheduleObj
    }

    /**
     * Fun que resgata do form se ele requer localização na finalização.
     */
    override fun getFormRequiresGPSInfo(): Boolean {
        mCustomForm.let {
            return it.require_location == 1
        }
    }

    override fun checkFormExists(): Boolean {
        return _mCustomForm != null
    }

    override fun getSerialInfo() = serialObj

    private fun getSerialObj(productCode: Int, serialId: String): MD_Product_Serial {
        return serialDao.getByString(
            MD_Product_Serial_Sql_002(
                ToolBox_Con.getPreference_Customer_Code(context),
                productCode.toLong(),
                serialId
            ).toSqlQuery()
        )
    }


    override fun getProductIcon(): Bitmap? {
        getProductInfo(productCode)?.let {
            if (!it.product_icon_name.isNullOrEmpty()) {
                if (ToolBox_Inf.verifyDownloadFileInf(it.product_icon_name, Constant.CACHE_PATH)) {
                    val imgFile = File(Constant.CACHE_PATH + "/" + it.product_icon_name)
                    if (imgFile.exists()) {
                        return BitmapFactory.decodeFile(imgFile.absolutePath)
                    }
                }
            }
        }
        return null
    }

    override fun getProductInfo(productCode: Int): MD_Product? = productDao.getByString(
        MD_Product_Sql_001(
            ToolBox_Con.getPreference_Customer_Code(context),
            productCode.toLong()
        ).toSqlQuery()
    )

    override fun getOsHeaderObj(): GeOs {
        var measureTp: MeMeasureTp? = null
        //
        getSerialInfo()
        //
        serialObj.measure_tp_code?.let {
            measureTp = getMeasureTp(serialObj.customer_code, serialObj.measure_tp_code)
        }
        val tkTicketForm = getTkTicketForm()
        //
        val orderType = getFormOrderType(tkTicketForm)
        val lastDataConsider = getLastDataConsider(measureTp, tkTicketForm)
        //
        return GeOs(
            customer_code = mCustomForm.customer_code,
            custom_form_type = mCustomForm.custom_form_type,
            custom_form_code = mCustomForm.custom_form_code,
            custom_form_version = mCustomForm.custom_form_version,
            custom_form_data = 0,
            order_type_code = orderType?.orderTypeCode ?: -1,
            order_type_id = orderType?.orderTypeId ?: "",
            order_type_desc = orderType?.orderTypeDesc ?: "",
            process_type = orderType?.processType ?: "",
            display_option = orderType?.displayOption ?: "",
            item_check_group_code = orderType?.itemCheckGroupCode,
            backup_product_code = null,
            backup_product_id = null,
            backup_product_desc = null,
            backup_serial_code = null,
            backup_serial_id = null,
            measure_tp_code = measureTp?.measureTpCode,
            measure_tp_id = measureTp?.measureTpId,
            measure_tp_desc = measureTp?.measureTpDesc,
            measure_value = null,
            measure_cycle_value = lastDataConsider.last_cycle_value,
            value_sufix = lastDataConsider.value_sufix,
            restriction_decimal = measureTp?.restrictionDecimal,
            value_cycle_size = measureTp?.valueCycleSize,
            cycle_tolerance = measureTp?.cycleTolerance,
            date_start = null,
            last_cycle_value = lastDataConsider.last_cycle_value,
            last_measure_value = lastDataConsider.last_measure_value,
            last_measure_date = lastDataConsider.last_measure_date,
            so_edit_start_end = mCustomForm.so_edit_start_end,
            so_order_type_code_default = mCustomForm.so_order_type_code_default,
            so_allow_change_order_type = mCustomForm.so_allow_change_order_type,
            device_tp_code_main = serialObj.device_tp_code_main,
            so_allow_backup = mCustomForm.so_allow_backup,
        )
    }

    private fun getLastDataConsider(
        measureTp: MeMeasureTp?,
        tkTicketForm: TK_Ticket_Form?
    ): LastMeasureDataConsider {
        return LastMeasureDataConsider(
            last_measure_value = getLastMeasureValueConsider(measureTp, tkTicketForm),
            measure_cycle_value = getMeasureCycleValue(tkTicketForm),
            last_measure_date = getLast_measure_date(tkTicketForm),
            last_cycle_value = getLast_cycle_value(tkTicketForm),
            value_sufix = getLast_value_sufix(measureTp, tkTicketForm),
        )
    }

    private fun getLast_value_sufix(
        measureTp: MeMeasureTp?,
        tkTicketForm: TK_Ticket_Form?
    ): String? {
        return if (tkTicketForm != null
            && tkTicketForm.custom_form_data_partition == 1
        ) {
            tkTicketForm.value_sufix
        } else {
            measureTp?.valueSufix
        }
    }

    private fun getLast_cycle_value(
        tkTicketForm: TK_Ticket_Form?
    ): Float? {
        return if (tkTicketForm != null
            && tkTicketForm.custom_form_data_partition == 1
        ) {
            tkTicketForm.measure_cycle_value
        } else {
            serialObj.last_cycle_value
        }
    }

    private fun getLast_measure_date(
        tkTicketForm: TK_Ticket_Form?
    ): String? {
        return if (tkTicketForm != null
            && tkTicketForm.custom_form_data_partition != null
        ) {
            tkTicketForm.start_date
        } else {
            serialObj.last_measure_date
        }
    }

    private fun getMeasureCycleValue(
        tkTicketForm: TK_Ticket_Form?
    ): Float? {
        return if (tkTicketForm != null
            && tkTicketForm.custom_form_data_partition == 1
        ) {
            tkTicketForm.measure_cycle_value
        } else {
            serialObj.last_cycle_value
        }
    }

    private fun getLastMeasureValueConsider(
        measureTp: MeMeasureTp?,
        tkTicketForm: TK_Ticket_Form?
    ): Float? {
        if (tkTicketForm != null
            && tkTicketForm.custom_form_data_partition == 1
        ) {
            return tkTicketForm.start_date?.let {
                val decimal = measureTp?.let { measureTp ->
                    measureTp.restrictionDecimal ?: ConstantBaseApp.FORM_OS_MEASURE_DECIMAL_DEFAULT
                }
                tkTicketForm.measure_value.roundByRestrictionMeasure(decimal).toFloat()
            }
            //
        } else {
            return serialObj.last_measure_value?.let {
                val decimal = measureTp?.let { measureTp ->
                    measureTp.restrictionDecimal ?: ConstantBaseApp.FORM_OS_MEASURE_DECIMAL_DEFAULT
                }
                it.roundByRestrictionMeasure(decimal).toFloat()
            }
        }
    }

    private fun getFormOrderType(tkTicketForm: TK_Ticket_Form?): MdOrderType? {
        if (tkTicketForm != null
            && tkTicketForm.custom_form_data_partition == 1
        ) {
            return MdOrderType(
                tkTicketForm.customer_code,
                tkTicketForm.order_type_code,
                tkTicketForm.order_type_id,
                tkTicketForm.order_type_desc,
                tkTicketForm.process_type,
                tkTicketForm.display_option,
                tkTicketForm.item_check_group_code
            )
        } else {
            mCustomForm.so_order_type_code_default?.let {
                return getOrderType(
                    mCustomForm.customer_code,
                    mCustomForm.so_order_type_code_default
                )
            }
        }
        return null
    }


    private fun getMeasureTp(customerCode: Long, measureTpCode: Int): MeMeasureTp? {
        return measureTpDao.getByString(
            MeMeasureTpSql_001(
                customerCode,
                measureTpCode
            ).toSqlQuery()
        )
    }

    private fun getOrderType(customerCode: Long, soOrderTypeCodeDefault: Int): MdOrderType? {
        return orderTypeDao.getByString(
            MdOrderTypeSql_001(
                customerCode,
                soOrderTypeCodeDefault
            ).toSqlQuery()
        )
    }

    /**
     * Fun que retorna o form ou null
     */
    private fun getForm(
        customFormCode: Int,
        customFormType: Int,
        customFormVersion: Int
    ): GE_Custom_Form? {
        return formDao.getByString(
            GE_Custom_Form_Sql_001_TT(
                ToolBox_Con.getPreference_Customer_Code(context).toString(),
                customFormType.toString(),
                customFormCode.toString(),
                customFormVersion.toString()
            ).toSqlQuery()
        )
    }

    /**
     *
     * Fun que retorna lista complete a de order type ou especifico caso seu code seja passado.
     *
     * @param orderTypeCode - Codigo do order type
     */
    override fun getOrderTypeList(orderTypeCode: Int): ArrayList<MdOrderType> {
        val orderTypeQuery =
            if (orderTypeCode == -1) {
                MdOrderTypeSql_002(
                    ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
            } else {
                MdOrderTypeSql_001(
                    ToolBox_Con.getPreference_Customer_Code(context),
                    orderTypeCode
                ).toSqlQuery()
            }
        //
        return orderTypeDao.query(orderTypeQuery) as ArrayList<MdOrderType>
    }

    /**
     * Fun que busca a medicao principal do serial.
     *
     * @param measureCode - Codigo da medição a ser buscada
     * @return Medicao encontrada ou null.
     */
    override fun getMeasure(measureCode: Int): MeMeasureTp? {
        return getMeasureTp(ToolBox_Con.getPreference_Customer_Code(context), measureCode)
    }

    /**
     * Fun que busca a maquina reserva no servidor.
     * Se não tiver conexao, faz a busca no device.
     *
     * @param bkpProductCode Codigo do produto da maquina reserva
     * @param bkpSerialId Id do serial digitado
     */
    override fun executeWsBkpMachine(bkpSerialId: String) {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_Product_Serial_Backup::class.java.name)
            //
            mView.showPD(
                ttl = hmAuxTrans["dialog_bkp_machine_search_ttl"],
                msg = hmAuxTrans["dialog_bkp_machine_search_start"]
            )
            //
            val mIntent = Intent(context, WBR_Product_Serial_Backup::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                putExtras(
                    Bundle().apply {
                        putLong(SO_PRODUCT_CODE, serialObj.product_code)
                        putLong(SO_SERIAL_CODE, serialObj.serial_code)
//                        putLong(MD_Product_SerialDao.PRODUCT_CODE, bkpProductCode)
                        putString(MD_Product_SerialDao.SERIAL_ID, bkpSerialId)
                        putInt(
                            MD_Product_SerialDao.SITE_CODE,
                            ToolBox_Con.getPreference_Site_Code(context).toInt()
                        )
                    }
                )
            }
            //
            context.sendBroadcast(mIntent)
        } else {
            searchBkpMachineInDb(bkpSerialId)
        }
    }

    /**
     * Fun que busca no banco a maquina reserva.
     * Exibe msg de não encontrado ou reporta para fragment lista de serial encontrados
     *
     * @param bkpProductCode Codigo do produto da maquina reserva
     * @param bkpSerialId Id do serial digitado
     */
    private fun searchBkpMachineInDb(bkpSerialId: String) {
        //
        val bkpSerialItemList: List<BaseSerialSearchItem.BackupMachineSerialItem>? =
            serialDao.query(
                Act087Sql_001(
                    serialObj.customer_code,
                    serialObj.product_code,
                    serialObj.serial_id,
                    ToolBox_Inf.getNoAccentStringForGlobSql(bkpSerialId),
                    ToolBox_Con.getPreference_Site_Code(context).toInt()
                ).toSqlQuery()
            )?.map { bkpOffline ->
                BaseSerialSearchItem.BackupMachineSerialItem(
                    bkpOffline.product_code.toInt(),
                    bkpOffline.product_id,
                    bkpOffline.product_desc,
                    bkpOffline.serial_code.toInt(),
                    bkpOffline.serial_id,
                    bkpOffline.site_code,
                    bkpOffline.site_desc
                )
            }
        //
        if (bkpSerialItemList.isNullOrEmpty()) {
            mView.showAlert(
                hmAuxTrans["alert_bkp_serial_error_ttl"],
                hmAuxTrans["alert_no_bkp_serial_found_offline_msg"],
            )
        } else {
            mView.reportSerialBkpMachineToFrag(
                serialBkpMachineList = bkpSerialItemList,
                onlineSearch = false
            )
        }
    }

    /**
     * Fun que extraid do json o retorno do Ws de consulta de maquina reserva
     *
     * @param mLink Json com dados recebidos pelo WS
     */
    override fun processWsBkpMachineResult(mLink: String?) {
        if (mLink != null && mLink.isNotEmpty()) {
            try {
                val rec = GsonBuilder().serializeNulls().create().fromJson(
                    mLink,
                    T_MD_Product_Serial_Backup_Rec::class.java
                )
                //
                if (rec.records != null && rec.records.isNotEmpty()) {
                    processSerialBkpMachine(
                        rec.records,
                        page = rec.record_page ?: -1,
                        foundQty = rec.record_count ?: -1,
                        true
                    )
                } else {
                    mView.showAlert(
                        hmAuxTrans["alert_bkp_serial_error_ttl"],
                        hmAuxTrans["alert_no_bkp_serial_found_msg"],
                    )
                }
            } catch (e: Exception) {
                ToolBox_Inf.registerException(javaClass.name, e)
                mView.showAlert(
                    hmAuxTrans["alert_bkp_serial_error_ttl"],
                    hmAuxTrans["alert_error_on_open_bkp_list_msg"],
                )
            }
        } else {
            mView.showAlert(
                hmAuxTrans["alert_bkp_serial_error_ttl"],
                hmAuxTrans["alert_error_no_data_return_msg"],
            )
        }
    }

    /**
     * Fun que efetivamente processa o retorno de maquina reserva.
     * Transforma lista envia pelo server em lists de obj do adapter e define se adicionar card de
     * qtd de itens encontradas maior que a paginação.
     * Por fim, reporta lista ao fragment
     *
     * @param records Lista retornada
     * @param page Qtd de itens por paginação
     * @param foundQty Qtd de itens encontrado nos servidor
     * @param onlineSearch Inforam se busca foi online ou não
     *
     */
    private fun processSerialBkpMachine(
        records: List<T_MD_Product_Serial_Backup_Record>,
        page: Int,
        foundQty: Int,
        onlineSearch: Boolean
    ) {
        val bkpSerialItemList: MutableList<BaseSerialSearchItem> = records.map { bkp ->
            BaseSerialSearchItem.BackupMachineSerialItem(
                bkp.productCode,
                bkp.productId,
                bkp.productDesc,
                bkp.serialCode,
                bkp.serialId,
                bkp.siteCode,
                bkp.siteDesc
            )
        }.toMutableList()
        //
        if (foundQty > records.size) {
            bkpSerialItemList.add(
                BaseSerialSearchItem.SerialSearchExceededItem(
                    hmAuxTrans["alert_qty_records_exceeded_msg"]!!,
                    hmAuxTrans["records_display_limit_lbl"]!!,
                    page,
                    hmAuxTrans["records_found_lbl"]!!,
                    foundQty
                )
            )
        }
        //
        mView.reportSerialBkpMachineToFrag(
            serialBkpMachineList = bkpSerialItemList.toList(),
            onlineSearch = onlineSearch
        )
    }

    /**
     * Fun que faz a criação das tabelas geOs e cria customFormLocal caso seja agendamento.
     * Trata erro na criação exibindo msg de erro
     * @param formOsHeader Obj GeOs ja validado com dados coletados da tela
     */
    override fun createOsHeader(formOsHeader: GeOs) {
        formOsHeader.custom_form_data = getNextFormData(formOsHeader)
        val tkTicketForm = getTkTicketForm()
        val isContinuousForm = tkTicketForm?.let {
            it.custom_form_data_partition != null && it.custom_form_data_partition > 0
        } ?: false
//        val daoObjReturn = geOsDao.createGeOsStructure(formOsHeader, serialObj, isContinuousForm)
        //
        CoroutineScope(Dispatchers.IO).launch {
            geOsCreateFormOsStructureUseCase(
                Input(
                    formOsHeader,
                    serialObj,
                    isContinuousForm,
                    tkTicketForm?.ticket_prefix,
                    tkTicketForm?.ticket_code
                )
            ).collect {
                it.watchStatus(
                    success = { output ->

                        val result = geOsSaveSerialStructureUseCase.invoke(
                            GeOsSaveSerialStructureUseCase.Input(
                                formOsHeader,
                                serialObj,
                                output.deviceItems,
                                output.geosVgs
                            )
                        )

                        //
                        CoroutineScope(Dispatchers.Main).launch {
                            if (result) {
                                checkNavigationSave(formOsHeader)
                            } else {
                                mView.showAlert(
                                    ttl = hmAuxTrans["alert_error_on_create_os_form_ttl"],
                                    msg = hmAuxTrans["alert_error_on_create_os_form_msg"]
                                )
                            }
                        }
                    },
                    error = { message, _ ->
                        CoroutineScope(Dispatchers.Main).launch {
                            mView.showAlert(
                                ttl = hmAuxTrans["alert_error_on_create_os_form_ttl"],
                                msg = hmAuxTrans["alert_error_on_create_os_form_msg"]
                            )
                        }
                    }
                )
            }
        }
        //
//        if(!daoObjReturn.hasError()){
//            if(!isSchedule()){
//                mView.callAct011(
//                    getAct011Bundle(
//                        formOsHeader
//                    )
//                )
//            }else{
//                if(mScheduleObj != null){
//                    if (tryCreateScheduleCustomFormLocal(formOsHeader.custom_form_data) != null) {
//                        mView.callAct011(
//                            getAct011Bundle(
//                                formOsHeader
//                            )
//                        )
//                    } else {
//                        removeGeOsAndReportScheduleError(formOsHeader)
//                    }
//                }else{
//                    removeGeOsAndReportScheduleError(formOsHeader)
//                }
//            }
//        }else{
//            mView.showAlert(
//                ttl =hmAuxTrans["alert_error_on_create_os_form_ttl"],
//                msg= hmAuxTrans["alert_error_on_create_os_form_msg"]
//            )
//        }
    }

    override fun checkNavigationSave(formOsHeader: GeOs) {
        if (!isSchedule()) {
            mView.callAct011(getAct011Bundle(formOsHeader))
        } else {
            if (mScheduleObj != null) {
                if (tryCreateScheduleCustomFormLocal(formOsHeader.custom_form_data) != null) {
                    mView.callAct011(getAct011Bundle(formOsHeader))
                } else {
                    removeGeOsAndReportScheduleError(formOsHeader)
                }
            } else {
                removeGeOsAndReportScheduleError(formOsHeader)
            }
        }
    }

    /**
     * Apaga as tabelas geOs* criadas e exibe msg de erro
     */
    private fun removeGeOsAndReportScheduleError(formOsHeader: GeOs) {
        //Remove do banco o formOsInserido em caso de erro
        geOsDao.removeFull(formOsHeader)
        //
        mView.showAlert(
            ttl = hmAuxTrans["alert_error_on_create_schedule_ttl"],
            msg = hmAuxTrans["alert_error_on_create_schedule_msg"]
        )
    }

    /**
     * Fun que criar o customFormLocal para o agendamento.
     *
     * @param customFormData CustomFormData definido na criacao das tabelas GeOs.
     * @return GE_Custom_Form_Local criado ou null em caso de erro
     *
     */
    private fun tryCreateScheduleCustomFormLocal(customFormData: Int): GE_Custom_Form_Local? {
        val custom_formDao = GE_Custom_FormDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        )
        val custom_form_LocalDao = GE_Custom_Form_LocalDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        )
        val custom_form_field_LocalDao = GE_Custom_Form_Field_LocalDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        )
        val custom_form_fieldDao = GE_Custom_Form_FieldDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        )
        val custom_form_blob_localDao = GE_Custom_Form_Blob_LocalDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        )
        //

        return ScheduleFormFatory().buildInitialScheduleFormLocal(
            context = context,
            scheduleExec = mScheduleObj!!,
            custom_formDao = custom_formDao,
            custom_form_fieldDao = custom_form_fieldDao,
            custom_form_field_LocalDao = custom_form_field_LocalDao,
            custom_form_blob_localDao = custom_form_blob_localDao,
            formLocalDao = custom_form_LocalDao,
            formData = customFormData.toLong()
        )
    }

    /**
     * Fun que gera o bunda para chamada da act011
     *
     * @param formOsHeader
     */
    private fun getAct011Bundle(formOsHeader: GeOs): Bundle {
        return Bundle().apply {
            putString(MD_ProductDao.PRODUCT_CODE, serialObj.product_code.toString())
            putString(MD_ProductDao.PRODUCT_DESC, serialObj.product_desc)
            putString(MD_ProductDao.PRODUCT_ID, serialObj.product_id)
            putString(MD_Product_SerialDao.SERIAL_ID, serialObj.serial_id)
            putString(
                GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE,
                formOsHeader.custom_form_type.toString()
            )
            putString(GE_Custom_FormDao.CUSTOM_FORM_CODE, formOsHeader.custom_form_code.toString())
            putString(
                GE_Custom_FormDao.CUSTOM_FORM_VERSION,
                formOsHeader.custom_form_version.toString()
            )
            putString(
                GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA,
                formOsHeader.custom_form_data.toString()
            )
            putString(
                GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA,
                formOsHeader.custom_form_data.toString()
            )
            val tkTicketForm = getTkTicketForm()
            tkTicketForm?.let {
                if (it.custom_form_data_partition != null
                    && it.custom_form_version_partition != null
                ) {
                    putInt(
                        GE_Custom_Form_DataDao.CUSTOM_FORM_DATA_PARTITION,
                        it.custom_form_data_partition
                    )

                    putInt(
                        GE_Custom_Form_DataDao.CUSTOM_FORM_VERSION_PARTITION,
                        it.custom_form_version_partition
                    )
                }
            }

            //Após finalizar a criação da O.S, além de navegar para a act011, o usr deve ser direcionado
            //para a primeira aba depois do cabeçalho. O bundle abaixo tem os parametros para essa navegação.
            putBundle(
                ConstantBaseApp.DEVICE_BUNDLE,
                Bundle().apply {
                    putInt(ConstantBaseApp.DEVICE_ITEM_TAB_INDEX, 1)
                    putInt(ConstantBaseApp.DEVICE_ITEM_LIST_INDEX, -1)
                    putString(ConstantBaseApp.DEVICE_ITEM_LIST_FILTER, "")
                }

            )
            //putString(Constant.ACT010_CUSTOM_FORM_CODE_DESC, formOsHeader.custom_form_type.toString())
            //putString(ConstantBaseApp.MAIN_REQUESTING_ACT,ConstantBaseApp.ACT005);

        }
    }

    /**
     * Fun que busca qual o proximo customFormData do banco
     *
     * @param geOs Obj com dados pra criacao da o.s e que tem a pk do form
     * @return Proximo formData
     */
    private fun getNextFormData(geOs: GeOs): Int {
        val nextDataAux = formDao.getByStringHM(
            GE_Custom_Form_Local_Sql_002(
                geOs.customer_code.toString(),
                geOs.custom_form_type.toString(),
                geOs.custom_form_code.toString(),
                geOs.custom_form_version.toString()
            ).toSqlQuery().lowercase(Locale.ENGLISH)
        )
        //
        return nextDataAux[GE_Custom_Form_Local_Sql_002.ID]!!.toInt()
    }

    /**
     * Fun que trata o clique de voltar da tela. Se houve alteracao, pedi confirma para acao de voltar
     *
     * @param anyDataChanged Flag que indica se houve alteracao nas infos
     *
     */
    override fun onBackPressedClicked(anyDataChanged: Boolean) {
        when (anyDataChanged) {
            true -> {
                mView.showAlert(
                    hmAuxTrans["alert_unsaved_data_will_be_lost_ttl"],
                    hmAuxTrans["alert_unsaved_data_will_be_lost_confirm"],
                    DialogInterface.OnClickListener { _, _ ->
                        checkBackFLow()
                    },
                    1
                )
            }

            else -> {
                checkBackFLow()
            }
        }

    }

    /**
     * Fun que valida qual o fluxo de volta e o executa
     */
    private fun checkBackFLow() {
        if (mView.isTicketBackFLow()) {
            mView.callAct070()
        } else {
            when (originFlow) {
                ConstantBaseApp.ACT083 -> {
                    mView.callAct083()
                }

                ConstantBaseApp.ACT092 -> {
                    mView.callAct092()
                }

                ConstantBaseApp.ACT006 -> {
                    mView.callAct092()
                }

                else -> {
                    mView.callAct083()
                }
            }
        }
    }


    override fun hasPassedDay(): Int {
        return custom_form_dataDao.getDaysPassedDateEnd(
            ToolBox_Con.getPreference_Customer_Code(
                context
            )
        )
    }

    override fun getTkTicketForm(): TK_Ticket_Form? {
        val tkTicketFormdao = TK_Ticket_FormDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        )
        if (ticketPrefix != null
            && ticketCode != null
            && ticketSeqTmp != null
            && stepCode != null
        ) {
            return tkTicketFormdao.getByString(
                TK_Ticket_Form_Sql_002(
                    ToolBox_Con.getPreference_Customer_Code(context),
                    ticketPrefix!!,
                    ticketCode!!,
                    ticketSeqTmp!!,
                    stepCode!!
                ).toSqlQuery()
            )
        } else {
            return null
        }
    }

    override fun getInitialSerialState(): InitialSerialState? {
        ticketPrefix?.let { prefix ->
            ticketCode?.let { code ->
                val ticket = ticketDao.getTicket(
                    ToolBox_Con.getPreference_Customer_Code(context),
                    prefix,
                    code,
                )
                ticket?.let {
                    val resposibleStop =
                        if (ticket.isSerialStopped != null && ticket.isSerialStopped == 1) {
                            ResponsibleStop.STOPPED
                        } else {
                            ResponsibleStop.NO_STOPPED
                        }

                    return InitialSerialState(
                        ticket.desiredDate,
                        ticket.stoppedDate,
                        resposibleStop,
                        serialObj.unavailability_reason_option == 1,
                        ticket.isSerialStopped == 1,
                        true,
                        serialObj.horimeter,
                        serialObj.horimeter_date,
                        serialObj.horimeter_supplier_uid,
                        serialObj.horimeter_supplier_desc,
                        serialObj.measure_block_input_time,
                        serialObj.measure_alert_input_time,
                    )
                }
            }
        }
        return InitialSerialState(
            null,
            null,
            ResponsibleStop.NO_STOPPED,
            serialObj.unavailability_reason_option == 1,
            isTicketSerialStopped = false,
            isEditMode = true,
            horimeter = serialObj.horimeter,
            horimeter_date = serialObj.horimeter_date,
            horimeter_supplier_uid = serialObj.horimeter_supplier_uid,
            horimeter_supplier_desc = serialObj.horimeter_supplier_desc,
            measure_block_input_time = serialObj.measure_block_input_time,
            measure_alert_input_time = serialObj.measure_alert_input_time,
        )
    }

    override fun getMdProduct(customerCode: Long, code: Long): MD_Product? {
        return productDao.getProductById(customerCode, code)
    }
}