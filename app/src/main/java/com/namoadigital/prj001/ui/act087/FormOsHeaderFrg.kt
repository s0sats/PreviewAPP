package com.namoadigital.prj001.ui.act087

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.Base_Activity_Frag
import com.namoadigital.prj001.R
import com.namoadigital.prj001.adapter.GenericSerialListDialog
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao
import com.namoadigital.prj001.databinding.FormOsHeaderFrgBinding
import com.namoadigital.prj001.databinding.FormOsHeaderFrgErrorDialogBinding
import com.namoadigital.prj001.databinding.FormSupplierDialogBinding
import com.namoadigital.prj001.databinding.BackupSerialSearchListDialogBinding
import com.namoadigital.prj001.databinding.IncSerialInitialStateBinding
import com.namoadigital.prj001.extensions.date.getDateDiferenceInMinutes
import com.namoadigital.prj001.extensions.date.isDateBefore
import com.namoadigital.prj001.extensions.formatTo
import com.namoadigital.prj001.extensions.setAsRequired
import com.namoadigital.prj001.extensions.setPrefix
import com.namoadigital.prj001.model.Act011FormTab
import com.namoadigital.prj001.model.Act011FormTabStatus
import com.namoadigital.prj001.model.BaseSerialSearchItem
import com.namoadigital.prj001.model.masterdata.ge_os.GeOs
import com.namoadigital.prj001.model.MD_Product
import com.namoadigital.prj001.model.MdOrderType
import com.namoadigital.prj001.model.MeMeasureTp
import com.namoadigital.prj001.ui.act011.FormOsHeaderFrgMeasureInteraction
import com.namoadigital.prj001.ui.act011.finish_os.di.model.ResponsibleStop
import com.namoadigital.prj001.ui.act011.frags.Act011BaseFrg
import com.namoadigital.prj001.ui.act087.model.InitialSerialState
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ConstantBaseApp.ONE_DAY_IN_MILLISECOND
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import com.namoadigital.prj001.view.act.product_selection.Act_Product_Selection
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.ceil

class FormOsHeaderFrg : Act011BaseFrg<FormOsHeaderFrgBinding>(), FormOsHeaderFrgInfr {

    private val PARAM_IS_CREATION_OS = "PARAM_IS_CREATION_OS"

    private var isOsCreation: Boolean = false
    private lateinit var formOsHeader: GeOs
    private var measureTpListener: FormOsHeaderFrgMeasureInteraction? = null
    private var mCreationListener: FormOsHeaderFrgCreationInteraction? = null
    private var orderTypeList: ArrayList<MdOrderType> = arrayListOf()
    private val spinnerAdapter: ArrayAdapter<String> by lazy {
        ArrayAdapter(
            requireContext(),
            R.layout.form_os_header_frg_spinner_item,
            getSpinnerOrderStringList()
        )
    }

    private val labelsView = mutableListOf<TextView>()
    private var defaultBkpMachineProduct: MD_Product? = null
    private var selectedBkpMachineProduct: MD_Product? = null
    private var selectedBkpMachineSerialCode: Int? = null
    private var selectedBkpMachineSerialId: String? = null
    private val controlsSta = arrayListOf<MKEditTextNM>()
    private var formSerialId: String? = null
    private var mainMeasureTp: MeMeasureTp? = null
    private var calculatedExecMeasureValue: Float = -1f
    private var calculatedExecCycle: Float = -1f
    private var bkpMachineDialog: AlertDialog? = null
    private var isBarcodeRead: Boolean = false
    private var initialSerialState: InitialSerialState? = null

    //Var usada somente na criação, se isso mudar, deve ser revisto sua inicialização.
    private val formRequiresGPS: Boolean by lazy {
        mCreationListener?.getFormRequiresGPS() ?: false
    }
    private val ticketForm by lazy {
        mCreationListener?.getTkTicketContinousForm()
            ?: measureTpListener?.getTkTicketFormContinuous()
    }

    companion object {
        @JvmStatic
        fun newInstance(
            hmAuxTrans: HMAux,
            tabIndex: Int = 0,
            tabLastIndex: Int = 1,
            formStatus: String,
            scheduleDesc: String?,
            scheduleComments: String?,
            formOsHeader: GeOs,
            isOsCreation: Boolean = false,
            initialSerialState: InitialSerialState? = null,
        ) = FormOsHeaderFrg()
            .apply {
                this.hmAuxTrans = hmAuxTrans
                this.formStatus = formStatus
                this.tabIndex = tabIndex
                this.tabLastIndex = tabLastIndex
                this.scheduleDesc = scheduleDesc
                this.scheduleComments = scheduleComments
                this.formOsHeader = formOsHeader
                this.isOsCreation = isOsCreation
                this.isFormOs = true
                this.initialSerialState = initialSerialState
                //
                arguments = Bundle().apply {
                    putString(GE_Custom_Form_DataDao.CUSTOM_FORM_STATUS, formStatus)
                    putInt(GE_Custom_Form_Field_LocalDao.PAGE, tabIndex)
                    putInt(PARAM_LAST_INDEX, tabLastIndex)
                    putString(MD_Schedule_ExecDao.SCHEDULE_DESC, scheduleDesc)
                    putString(GE_Custom_Form_Field_LocalDao.COMMENT, scheduleComments)
                    putSerializable(GeOs::javaClass.name, formOsHeader)
                    putBoolean(PARAM_IS_CREATION_OS, isOsCreation)
                    putBoolean(GE_Custom_Form_LocalDao.IS_SO, isFormOs)
                }
            }

        val mResource_Name = "form_os_header"

        fun getFragTranslationsVars(): List<String> {
            return listOf(
                "form_os_info_lbl",
                "order_type_lbl",
                "use_backup_lbl",
                "start_date_lbl",
                "backup_serial_hint",
                "backup_serial_help_lbl",
                "measure_current_value_hint",
                "measure_last_value_lbl",
                "btn_save",
                "btn_continue",
                "erro_dialog_ttl",
                "alert_invalid_order_type_error_msg",
                "alert_empty_bkp_machine_error_msg",
                "alert_same_serial_bkp_machine_error_msg",
                "alert_invalid_star_date_error_msg",
                "alert_invalid_measure_value_error_msg",
                "alert_invalid_measure_zero_cycle_error_msg",
                "alert_invalid_measure_cycle_error_msg",
                "alert_invalid_initial_serial_state_date_error_msg",
                "alert_invalid_initial_serial_state_responsable_error_msg",
                "alert_last_cycle_lbl",
                "alert_current_cycle_lbl",
                "alert_form_os_creation_ttl",
                "alert_form_os_creation_confirm",
                "alert_form_os_continues_ttl",
                "alert_form_os_continues_confirm",
                "alert_bkp_serial_ttl",
                "toast_serial_auto_selected_msg",
                "alert_qty_records_exceeded_msg",
                "records_display_limit_lbl",
                "records_found_lbl",
                "form_os_header_lbl",
                "alert_form_turn_gps_on_title",
                "alert_form_turn_gps_on_msg",
                "allow_measure_in_the_past_lbl",
                "alert_horimeter_type_lbl",
                "alert_measure_error_ttl",
                "alert_measure_invalid_value_msg",
                "form_os_partition_headline_lbl",
                "alert_invalid_start_date_partition_error_msg",
                "initial_serial_state_ttl",
                "initial_serial_state_stopped_ttl",
                "initial_serial_state_switch_lbl",
                "initial_serial_state_ticket_stopped_lbl",
                "initial_serial_state_date_lbl",
                "initial_serial_state_responsable_lbl",
                "initial_serial_state_maintenance_opt",
                "initial_serial_state_third_party_error_opt",
                "initial_serial_state_running_opt",
                "initial_serial_state_stopped_opt",
                "telemetry_horimeter_lbl",
                "telemetry_horimeter_date_lbl",
                "telemetry_supplier_uid_lbl",
                "telemetry_supplier_desc_lbl",
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tabIndex = it.getInt(GE_Custom_Form_Field_LocalDao.PAGE)
            tabLastIndex = it.getInt(PARAM_LAST_INDEX)
            formStatus = it.getString(GE_Custom_Form_DataDao.CUSTOM_FORM_STATUS, "")
            scheduleDesc = it.getString(MD_Schedule_ExecDao.SCHEDULE_DESC)
            scheduleComments = it.getString(GE_Custom_Form_Field_LocalDao.COMMENT)
            isOsCreation = it.getBoolean(PARAM_IS_CREATION_OS)
            formOsHeader = it.getSerializable(GeOs::javaClass.name) as GeOs
            isFormOs = it.getBoolean(GE_Custom_Form_LocalDao.IS_SO, false)
        }
    }

    override fun getViewBinding() = FormOsHeaderFrgBinding.inflate(layoutInflater)
    override fun getHeaderInclude() = binding.incHeader
    override fun getNavegationInclude() = binding.incNavegation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNavegationIncludeVisibility()
        setCtrlsStaList()
        setLabels()
        iniVars()
        iniActions()
    }

    private fun setCtrlsStaList() {
        if (isOsCreation) {
            controlsSta.clear()
            controlsSta.add(binding.mketMachineSerialEdit)
            controlsSta.add(binding.mketOsMainMeasureVal)
            mCreationListener?.delegateControlSta(controlsSta, true)
        }
    }

    private fun setNavegationIncludeVisibility() {
        if (isOsCreation) {
            binding.incNavegation.root.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setLabels() {
        with(binding) {
            tvInfoOsLbl.text = hmAuxTrans["form_os_info_lbl"]
            tvOsTypeLbl.text = hmAuxTrans["order_type_lbl"]
            tvOsMachineLbl.text = hmAuxTrans["use_backup_lbl"]
            tvOsStartDateLbl.text = hmAuxTrans["start_date_lbl"]
            tilMketSerial.hint = hmAuxTrans["backup_serial_hint"]
            tilMketSerial.error = hmAuxTrans["backup_serial_help_lbl"]
//            mketOsMainMeasureVal.hint = hmAuxTrans["measure_current_value_hint"]
            tvOsLastMeasureLbl.text = hmAuxTrans["measure_last_value_lbl"]
            swAllowFormSoInThePast.text = hmAuxTrans["allow_measure_in_the_past_lbl"]
            tvHorimeterAlertLbl.text = hmAuxTrans["alert_horimeter_type_lbl"]
            clSerialInitialState.apply {
                setInitialStateSerialLbl()
            }

            ticketForm?.let {
                notificationPartial.apply {
                    val date = it.start_date.formatTo(
                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                    )
                    tvMessage.text = "${hmAuxTrans["form_os_partition_headline_lbl"]} $date"
                    root.visibility = View.VISIBLE
                }
                btnSave.text = hmAuxTrans["btn_continue"]
            } ?: run {
                btnSave.text = hmAuxTrans["btn_save"]
            }
        }
    }

    private fun iniVars() {
        iniOrderTypeSpinner()
        iniInitialStateSerial()
        iniBkpMachine()
        iniStartDate()
        iniMainMeasure()
        iniLastMeasureInfo()
        iniSwAllowInThePast()
        iniSaveBtn()
//        addIdxToVisibleLabels()
    }

    private fun iniInitialStateSerial() {
        binding.clSerialInitialState.apply {

            if (ConstantBaseApp.SYS_STATUS_CANCELLED == formStatus
                || ConstantBaseApp.SYS_STATUS_DONE == formStatus
            ) {
                root.visibility = View.GONE
            }
            swInitialStateMachine.visibility = View.VISIBLE
            tvTitle.visibility = View.VISIBLE
            tvQuestionLbl.visibility = View.VISIBLE
            viewSeparator.visibility = View.GONE
            initialSerialState?.let {
                //
                mkDateVal.setmEnabled(isOsCreation)
                rbMachineRunning.isEnabled = isOsCreation
                rbMachineStopped.isEnabled = isOsCreation
                rbMaintenance.isEnabled = isOsCreation
                rbThirdPartyError.isEnabled = isOsCreation
                //
                setInitialStateSerialResponsableVisibility(it.showResponsableStopMachine)
                //
                if (it.isTicketSerialStopped) {
                    tvSerialStoppedDateLbl.visibility = View.GONE
                    swInitialStateMachine.visibility = View.GONE
                    tvTitle.text = hmAuxTrans["initial_serial_state_stopped_ttl"]
                } else {
                    swInitialStateMachine.isChecked = it.stoppedDate != null
                    swInitialStateMachine.isEnabled = isOsCreation
                }
                //
                when (it.responsibleStop) {
                    ResponsibleStop.THIRD_PARTY -> {
                        rbThirdPartyError.isChecked = true
                        if (it.isTicketSerialStopped) {
                            rbMachineStopped.isChecked = true
                            setInitialStateSerialStoppedDateVisibility(false)
                            setInitialStateSerialStoppedTicketDateVisibility(true)
                        } else {
                            swInitialStateMachine.isChecked = true
                            setInitialStateSerialStoppedTicketDateVisibility(false)
                            setInitialStateSerialStoppedDateVisibility(true)
                        }
                        setInitialStateSerialResponsableVisibility(true)
//                        rbMaintenance.isChecked = false
                    }

                    ResponsibleStop.MAINTENANCE -> {
//                        rbThirdPartyError.isChecked = false
                        if (it.isTicketSerialStopped) {
                            rbMachineStopped.isChecked = true
                            setInitialStateSerialStoppedDateVisibility(false)
                            setInitialStateSerialStoppedTicketDateVisibility(true)
                        } else {
                            swInitialStateMachine.isChecked = true
                            setInitialStateSerialStoppedTicketDateVisibility(false)
                            setInitialStateSerialStoppedDateVisibility(true)
                        }
                        rbMaintenance.isChecked = true
                        setInitialStateSerialResponsableVisibility(true)
                    }

                    ResponsibleStop.STOPPED -> {
                        if (it.isTicketSerialStopped) {
                            rbMachineStopped.isChecked = true
                            setInitialStateSerialStoppedDateVisibility(false)
                            setInitialStateSerialStoppedTicketDateVisibility(true)
                        } else {
                            swInitialStateMachine.isChecked = true
                            setInitialStateSerialStoppedTicketDateVisibility(false)
                            setInitialStateSerialStoppedDateVisibility(true)
                        }
                    }

                    ResponsibleStop.NO_STOPPED -> {
                        if (it.isTicketSerialStopped) {
                            rbMachineRunning.isChecked = true
                            setInitialStateSerialStoppedTicketDateVisibility(true)
                        } else {
                            swInitialStateMachine.isChecked = false
                            setInitialStateSerialStoppedDateVisibility(false)
                        }
                        setInitialStateSerialResponsableVisibility(false)
                    }
                }
                //
                mkDateVal.setmCanClean(false)
                //
                if (it.stoppedDate != null) {
                    mkDateVal.setmValue(it.stoppedDate)
                }
                //
            }
        }
    }

    private fun IncSerialInitialStateBinding.setInitialStateSerialResponsableVisibility(showQuestion: Boolean) {
        tvResponsableStopMachineLbl.visibility = if (showQuestion) View.VISIBLE else View.GONE
        rgResponsableStopMachine.visibility = if (showQuestion) View.VISIBLE else View.GONE
    }

    private fun IncSerialInitialStateBinding.setInitialStateSerialStoppedDateVisibility(showQuestion: Boolean) {
        tvSerialStoppedDateLbl.visibility = if (showQuestion) View.VISIBLE else View.GONE
        mkDateVal.visibility = if (showQuestion) View.VISIBLE else View.GONE
    }

    private fun IncSerialInitialStateBinding.setInitialStateSerialStoppedTicketDateVisibility(
        showQuestion: Boolean
    ) {
        rgStopMachine.visibility = if (showQuestion) View.VISIBLE else View.GONE
        viewSeparator.visibility = if (showQuestion) View.VISIBLE else View.GONE
        mkDateVal.visibility = if (showQuestion) View.VISIBLE else View.GONE
    }


    private fun IncSerialInitialStateBinding.setInitialStateSerialLbl() {
        tvTitle.text = "${hmAuxTrans["initial_serial_state_ttl"]}"
        tvQuestionLbl.text = "${hmAuxTrans["initial_serial_state_switch_lbl"]}"
        tvResponsableStopMachineLbl.text = "${hmAuxTrans["initial_serial_state_responsable_lbl"]}"
        initialSerialState?.let {
            if (it.isTicketSerialStopped) {
                tvQuestionLbl.text = "${hmAuxTrans["initial_serial_state_ticket_stopped_lbl"]}"
            }
            if (it.isEditMode) {
                tvResponsableStopMachineLbl.setAsRequired(true)
            }
        }

        rbMaintenance.text = "${hmAuxTrans["initial_serial_state_maintenance_opt"]}"
        rbThirdPartyError.text = "${hmAuxTrans["initial_serial_state_third_party_error_opt"]}"
        rbMachineRunning.text = "${hmAuxTrans["initial_serial_state_running_opt"]}"
        rbMachineStopped.text = "${hmAuxTrans["initial_serial_state_stopped_opt"]}"
        tvSerialStoppedDateLbl.text = hmAuxTrans["initial_serial_state_date_lbl"]
        mkDateVal.setmLabel("")
        mkDateVal.setmCanClean(false)
    }

    private fun iniSwAllowInThePast() {
        with(binding) {
            swAllowFormSoInThePast.visibility = if (ToolBox_Inf.profileExists(
                    requireContext(),
                    ConstantBaseApp.PROFILE_PRJ001_CHECKLIST,
                    ConstantBaseApp.PROFILE_PRJ001_CHECKLIST_PARAM_ALLOW_FORM_SO_IN_THE_PAST
                )
                && isOsCreation
                && !isContinuosFormPartition()
            ) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    /**
     * Fun que transforma a lista de MdOrderType em lista de string.
     * Se não houver order type default, add item em branco
     */
    private fun getSpinnerOrderStringList(): List<String> {
        if (formOsHeader.so_order_type_code_default == null && isOsCreation) {
            orderTypeList.add(
                0,
                MdOrderType(
                    ToolBox_Con.getPreference_Customer_Code(requireContext()),
                    0,
                    "",
                    "",
                    "",
                    "",
                    null,
                    process_vg = null
                )
            )
        }
        return orderTypeList.map {
            it.orderTypeDesc
        }
    }

    private fun iniOrderTypeSpinner() {
        if (isOsCreation) {
            if (isContinuosFormPartition()) {
                orderTypeList = arrayListOf(
                    MdOrderType(
                        ticketForm!!.customer_code,
                        ticketForm!!.order_type_code,
                        ticketForm!!.order_type_id,
                        ticketForm!!.order_type_desc,
                        ticketForm!!.process_type,
                        ticketForm!!.display_option,
                        ticketForm!!.item_check_group_code,
                        process_vg = ticketForm!!.process_vg
                    )
                )
            } else {
                mCreationListener?.let {
                    orderTypeList = it.getOrderTypeList()
                }
            }
        } else {
            orderTypeList = arrayListOf(
                MdOrderType(
                    formOsHeader.customer_code,
                    formOsHeader.order_type_code,
                    formOsHeader.order_type_id,
                    formOsHeader.order_type_desc,
                    formOsHeader.process_type,
                    formOsHeader.display_option,
                    formOsHeader.item_check_group_code,
                    process_vg = formOsHeader.process_vg,
                )
            )
        }
        //
        binding.spOsType.apply {
            adapter = spinnerAdapter
            //Se existe order default seta
            if (isOsCreation
                && !isContinuosFormPartition()
            ) {
                formOsHeader.so_order_type_code_default?.let {
                    val orderTypeDefaultIdx = getOrderTypeIdx(it)
                    if (orderTypeDefaultIdx > -1) {
                        setSelection(orderTypeDefaultIdx)
                    }
                }
            } else {
                setSelection(0)
            }
            //Se order permite alterar e é um criação , libera edição
            isEnabled =
                formOsHeader.so_allow_change_order_type == 1 && isOsCreation && !isContinuosFormPartition()
        }
    }

    private fun getOrderTypeIdx(orderTypeCode: Int): Int {
        var orderTypeDefaultIdx = -1
        if (orderTypeCode != -1) {
            orderTypeList.forEachIndexed { idx, obj ->
                if (orderTypeCode == obj.orderTypeCode) {
                    orderTypeDefaultIdx = idx
                }
            }
        }
        return orderTypeDefaultIdx
    }


    private fun iniBkpMachine() {
        with(binding) {
            //Se permite maquina reserva exibe, caso contrario some tudo.
            if (formOsHeader.so_allow_backup == 1
                && (isOsCreation
                        || formStatus != ConstantBaseApp.SYS_STATUS_DONE
                        )
            ) {
                swMachine.isChecked = (formOsHeader.backup_product_code != null)
                updateBkpMachineVisibility()
                if (isOsCreation) {
                    mCreationListener?.let {
                        defaultBkpMachineProduct = it.getDefaultBkpMachineProduct()
//                        tvMachineProdEditLbl.text =
//                            defaultBkpMachineProduct?.product_desc?.uppercase(Locale.getDefault())
                    }
                } else {
                    /*formOsHeader.backup_product_code?.let {
                        tvMachineProdEditLbl.text = formOsHeader.backup_product_desc
                    }*/
                    formOsHeader.backup_serial_code?.let {
                        mketMachineSerialEdit.setText(formOsHeader.backup_serial_id)
                        tilMketSerial.helperText = formOsHeader.backup_product_desc
                    }
                    //
                    swMachine.isChecked = formOsHeader.backup_serial_id != null
                    tilMketSerial.isHelperTextEnabled = formOsHeader.backup_serial_id != null
                    swMachine.isEnabled = false
                    mketMachineSerialEdit.isEnabled = false
                    mketMachineSerialEdit.setmBARCODE(false)
                    tilMketSerial.isErrorEnabled = false
                    mketMachineSerialEdit.isError = false
                    ivSerialSearch.isEnabled = false
                    /*ivSwapMachine.visibility =
                        if (formOsHeader.backup_serial_code != null) View.INVISIBLE else View.GONE*/
                    ivSerialSearch.visibility =
                        if (formOsHeader.backup_serial_code != null) View.INVISIBLE else View.GONE
                }
            } else {
                clMachineEdit.visibility = View.GONE
                tvOsMachineLbl.visibility = View.GONE
                swMachine.isEnabled = false
            }
        }
    }

    private fun isContinuosFormPartition() = ticketForm != null

    private fun updateBkpMachineVisibility() {
        with(binding) {
            gpBkpMachineVal.visibility = if (swMachine.isChecked) {
                View.VISIBLE
            } else {
                clearMachineSerialValue()
                View.GONE
            }
        }
    }

    override fun reportSerialBkpMachineToFrag(
        serialBkpMachineList: List<BaseSerialSearchItem>,
        onlineSearch: Boolean
    ) {
        if (serialBkpMachineList.size == 1) {
            val serialBkp = serialBkpMachineList[0] as BaseSerialSearchItem.BackupMachineSerialItem
            if (serialBkp.serialId.equals(binding.mketMachineSerialEdit.text.toString(), true)) {
                setSelectedBkpMachineSerial(serialBkp, autoSelection = true)
            } else {
                showBkpMachineDialog(serialBkpMachineList, onlineSearch)
            }
        } else {
            if (isBarcodeRead) {
                val idx = hasBarcodeSerialMatch(
                    serialBkpMachineList,
                    selectedBkpMachineProduct!!.product_code.toInt(),
                    binding.mketMachineSerialEdit.text.toString().trim()
                )
                if (idx > -1) {
                    setSelectedBkpMachineSerial(
                        serialBkpMachineList[idx] as BaseSerialSearchItem.BackupMachineSerialItem,
                        autoSelection = true
                    )
                } else {
                    showBkpMachineDialog(serialBkpMachineList, onlineSearch)
                }
            } else {
                showBkpMachineDialog(serialBkpMachineList, onlineSearch)
            }
        }
        //reseta var
        isBarcodeRead = false
    }

    override fun isAnyDataChanged(): Boolean {
        return true
    }


    private fun hasBarcodeSerialMatch(
        serialBkpMachineList: List<BaseSerialSearchItem>,
        productCode: Int,
        serialIdSearched: String
    ): Int {
        serialBkpMachineList.forEachIndexed { idx, obj ->
            if (obj is BaseSerialSearchItem.BackupMachineSerialItem) {
                if (obj.productCode == productCode && obj.serialId.equals(serialIdSearched, true)) {
                    return idx
                }
            }
        }
        return -1
    }

    private fun setSelectedBkpMachineSerial(
        serialBkp: BaseSerialSearchItem.BackupMachineSerialItem,
        autoSelection: Boolean = false
    ) {
        with(binding) {
            mketMachineSerialEdit.setText(serialBkp.serialId)
            mketMachineSerialEdit.isEnabled = false
            mketMachineSerialEdit.setmBARCODE(false)
            tilMketSerial.isHelperTextEnabled = true
            tilMketSerial.helperText = serialBkp.productDesc
            clMachineEdit.background =
                ContextCompat.getDrawable(
                    requireContext(),
                    com.namoa_digital.namoa_library.R.drawable.shape_ok
                )
            tilMketSerial.isErrorEnabled = false
            mketMachineSerialEdit.isError = false
            selectedBkpMachineSerialCode = serialBkp.serialCode
            selectedBkpMachineSerialId = serialBkp.serialId
            ivSerialSearch.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_delete
                )
            )
        }
        selectedBkpMachineProduct = mCreationListener?.getMdProduct(serialBkp.productCode.toLong())

        //
        bkpMachineDialog?.dismiss()
        //Se auto selecao, exibe toast
        if (autoSelection) {
            Toast.makeText(
                requireContext(),
                hmAuxTrans["toast_serial_auto_selected_msg"],
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun iniStartDate() {
        with(binding) {
            mkdtStartDate.apply {
                setmLabel(null)
                setmCanClean(false)
                if (isOsCreation) {
                    setmValue(ToolBox.sDTFormat_Agora(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT))
                } else {
                    setmValue(formOsHeader.date_start)
                }
                setmEnabled(formOsHeader.so_edit_start_end == 1 && isOsCreation)
            }
        }
    }

    private fun iniMainMeasure() {
        with(binding) {
            mketOsMainMeasureVal.setEnableCommaLocale(true)
            if (isContinuosFormPartition()) {
                clMainMeasure.visibility =
                    if (ticketForm!!.measure_tp_code != null) View.VISIBLE else View.GONE
                ticketForm!!.measure_tp_code?.let {
                    mainMeasureTp = getMainMeasureTp(it, ticketForm!!.customer_code)
                }
                ticketForm!!.measure_tp_desc?.let {
                    tvOsMainMeasureLbl.hint = it
                }
                ticketForm?.measure_value?.let {
                    mketOsMainMeasureVal.setText(
                        ToolBox_Inf.convertFloatToBigDecimalString(
                            it,
                            true
                        )
                    )
                    mketOsMainMeasureVal.isEnabled = false
                    mketOsMainMeasureVal.setmBARCODE(false)

                } ?: run {
                    if (mainMeasureTp?.without_measure == 1) {

                        mketOsMainMeasureVal.setText(
                            formOsHeader.last_cycle_value?.let {
                                ToolBox_Inf.convertFloatToBigDecimalString(
                                    it,
                                    true
                                )
                            } ?: ToolBox_Inf.convertFloatToBigDecimalString(
                                0f,
                                true
                            )
                        )
                    }
                }

                mainMeasureTp?.let { measure ->
                    checkWithoutMeasure(measure)
                    mketOsMainMeasureVal.setmDecimal(
                        measure.restrictionDecimal
                            ?: ConstantBaseApp.FORM_OS_MEASURE_DECIMAL_DEFAULT
                    )
                }

            } else {
                clMainMeasure.visibility =
                    if (formOsHeader.measure_tp_code != null) View.VISIBLE else View.GONE
                formOsHeader.measure_tp_code?.let {
                    mainMeasureTp = getMainMeasureTp(it, formOsHeader.customer_code)
                }
                formOsHeader.measure_tp_desc?.let {
                    tvOsMainMeasureLbl.hint = it
                }
                //todo rever o save do float no obj
                formOsHeader.measure_value?.let {
                    mketOsMainMeasureVal.setText(
                        ToolBox_Inf.convertFloatToBigDecimalString(
                            it,
                            true
                        )
                    )
                    mketOsMainMeasureVal.isEnabled = isOsCreation
                    mketOsMainMeasureVal.setmBARCODE(isOsCreation)

                } ?: run {
                    if (mainMeasureTp?.without_measure == 1) {
                        mketOsMainMeasureVal.setText(
                            formOsHeader.last_cycle_value?.let {
                                ToolBox_Inf.convertFloatToBigDecimalString(
                                    it,
                                    true
                                )
                            } ?: ToolBox_Inf.convertFloatToBigDecimalString(
                                0f,
                                true
                            )
                        )
                    }
                }
                mainMeasureTp?.let { measure ->
                    checkWithoutMeasure(measure)
                    mketOsMainMeasureVal.setmDecimal(
                        measure.restrictionDecimal
                            ?: ConstantBaseApp.FORM_OS_MEASURE_DECIMAL_DEFAULT
                    )
                }
            }
            //
            ibTelemetry.visibility = getTelemetryVisibility()
            //
            initialSerialState?.let {
                if (isOsCreation) {
                    if (!isContinuosFormPartition()) {
                        it.horimeter?.let { horimeter ->
                            val validHorimeter = if (checkLateTelemetryMeasureDate(
                                    it.horimeter_date,
                                    formOsHeader.last_measure_date
                                )
                            ) {
                                formOsHeader.last_measure_value
                            } else {
                                horimeter.toFloat()
                            }
                            mketOsMainMeasureVal.setText(
                                ToolBox_Inf.formatLastMeaseureInfo(
                                    context,
                                    null,
                                    validHorimeter,
                                    null
                                )
                            )
                        }
                    }
                }
                val currentTime = ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z")

                val diffTime =
                    it.horimeter_date?.getDateDiferenceInMinutes(currentTime) ?: Long.MAX_VALUE

                if(initialSerialState?.horimeter_supplier_uid != null) {
                    it.measure_block_input_time?.let { block_input ->
                        if (isBlockInputTime(diffTime, block_input)) {
                            mketOsMainMeasureVal.isEnabled = false
                        }
                    }
                    it.measure_alert_input_time?.let { alert_input ->
                        if (diffTime >= alert_input
                            && isOsCreation
                            && !isContinuosFormPartition()
                        ) {
                            tvHorimeterAlertLbl.visibility = getTelemetryVisibility()
                            if (initialSerialState?.horimeter_supplier_uid != null) {
                                mketOsMainMeasureVal.setText("")
                            }
                        } else {
                            tvHorimeterAlertLbl.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun getTelemetryVisibility(): Int {
        return if(handleTelemetry())
                View.VISIBLE
            else
                View.GONE
    }

    private fun handleTelemetry() = ((isOsCreation
            || isContinuosFormPartition())
                && initialSerialState?.horimeter_supplier_uid != null)


    private fun checkLateTelemetryMeasureDate(
        horimeterDate: String?,
        lastMeasureDate: String?
    ): Boolean {
        if (horimeterDate != null && lastMeasureDate != null) {
            return ToolBox_Inf.getDateDiferenceInMilliseconds(horimeterDate, lastMeasureDate) < 0
        }
        return false
    }

    private fun isBlockInputTime(diffTime: Long, block_input: Long) = diffTime < block_input

    private fun getMainMeasureTp(it: Int, customer_code: Long) =
        mCreationListener?.getMeasure(it)
            ?: measureTpListener?.getMeasure(customerCode = customer_code, it)

    private fun FormOsHeaderFrgBinding.checkWithoutMeasure(measure: MeMeasureTp) {
        if (measure.without_measure == 0) {
            clMainMeasure.visibility = View.VISIBLE
            tvOsMainMeasureLbl.visibility = View.VISIBLE
            mketOsMainMeasureVal.visibility = View.VISIBLE
        } else {
            clMainMeasure.visibility = View.GONE
            tvOsMainMeasureLbl.visibility = View.GONE
            mketOsMainMeasureVal.visibility = View.GONE
        }
    }

    private fun iniLastMeasureInfo() {
        with(binding) {
            if (!isContinuosFormPartition()) {
                tvOsLastMeasureVal.text = ToolBox_Inf.formatLastMeaseureInfo(
                    context,
                    formOsHeader.value_sufix,
                    formOsHeader.last_measure_value,
                    formOsHeader.last_measure_date
                )
                clLastMeasure.visibility = if (!tvOsLastMeasureVal.text.toString()
                        .isNullOrEmpty() && mainMeasureTp != null && mainMeasureTp?.without_measure == 0
                ) View.VISIBLE else View.GONE
            } else {
                clLastMeasure.visibility = View.GONE
            }
        }
    }

    private fun iniSaveBtn() {
        binding.btnSave.apply {
            visibility = if (isOsCreation) View.VISIBLE else View.GONE
            if (isOsCreation) {
                setOnClickListener {
                    checkSave()
                }
            }
        }
    }

    private fun checkSave() {
        with(binding) {
            val measure = mketOsMainMeasureVal.commaFormatted

            if (!isMeasureValNumeric()
                || measure.toString().toFloat() < 0
            ) {
                ToolBox.alertMSG(
                    requireContext(),
                    hmAuxTrans["alert_measure_error_ttl"],
                    hmAuxTrans["alert_measure_invalid_value_msg"],
                    { dialog, _ ->
                        dialog.dismiss()
                    },
                    0
                )
                return
            }

            validateSave()
        }
    }

    private fun validateSave() {
        with(binding) {
            val isOrderTypeInvalid =
                (spOsType.selectedItemPosition > orderTypeList.lastIndex || orderTypeList[spOsType.selectedItemPosition].orderTypeCode <= 0)
            val isMachineEmpty =
                swMachine.isChecked && (selectedBkpMachineProduct == null || selectedBkpMachineSerialCode == null)
            val isMachineTheSame =
                (swMachine.isChecked && !isMachineEmpty && defaultBkpMachineProduct?.product_code == selectedBkpMachineProduct?.product_code && selectedBkpMachineSerialId == formSerialId)
            val isStartDateInvalid = if (!bypassMinValidation()) isValidStartDate().not() else false
            val isContinuousFormStartDateInvalid =
                if (isContinuosFormPartition()) isValidContinuosFormStartDate().not() else false
            clMachineEdit.background = if (isMachineEmpty || isMachineTheSame) {
                ContextCompat.getDrawable(
                    requireContext(),
                    com.namoa_digital.namoa_library.R.drawable.shape_error
                )
            } else {
                ContextCompat.getDrawable(
                    requireContext(),
                    com.namoa_digital.namoa_library.R.drawable.shape_ok
                )
            }
            val isInitialSerialStateDateInvalid = initialSerialState?.let {
                checkStoppedDate()
            } ?: false
            //
            val isInitialSerialStateResponsableInvalid = initialSerialState?.let {
                if (it.showResponsableStopMachine
                    && (clSerialInitialState.swInitialStateMachine.isChecked
                            || clSerialInitialState.rbMachineStopped.isChecked)
                ) {
                    !clSerialInitialState.rbMaintenance.isChecked
                            && !clSerialInitialState.rbThirdPartyError.isChecked
                } else {
                    false
                }
            } ?: false
            //
            val measureInvalid = mainMeasureTp?.let {
                if (!binding.mketOsMainMeasureVal.commaFormatted.isNullOrEmpty() && isMeasureValNumeric()) {
                    val typedMeasure = binding.mketOsMainMeasureVal.commaFormatted.toDouble()
                    it.isMeasureRestrictionInvalid(
                        bypassMinValidation(),
                        typedMeasure,
                        formOsHeader.last_measure_value?.toDouble(),
                        formOsHeader.last_measure_date,
                        binding.mkdtStartDate.getmValue()
                    )
                } else {
                    true
                }
            } ?: false
            //
            val preventiveCycleInvalid = isPreventiveCycleValid(isOrderTypeInvalid).not()
            //
            formOsHeader.allowFormInThePast = if (swAllowFormSoInThePast.isChecked) 1 else 0
            //Verifica se a OS é uma continuação e se for ele continua para a próxima tela
            //Se for continuação da OS e a data estiver invalida ele mostra um dialog pro user
            if (isContinuosFormPartition()) {
                if (isStartDateInvalid
                    || isContinuousFormStartDateInvalid
                    || isInitialSerialStateDateInvalid
                    || isInitialSerialStateResponsableInvalid
                ) {
                    showSaveErroDialog(
                        startDateInvalid = isStartDateInvalid,
                        continuousFormStartDateInvalid = isContinuousFormStartDateInvalid,
                        isInitialSerialStateDateInvalid = isInitialSerialStateDateInvalid,
                        isInitialSerialStateResponsableInvalid = isInitialSerialStateResponsableInvalid,
                    )
                    return
                }
                //
                ToolBox.alertMSG_YES_NO(
                    requireContext(),
                    hmAuxTrans["alert_form_os_continues_ttl"],
                    hmAuxTrans["alert_form_os_continues_confirm"],
                    { _, _ ->
                        createOs()
                    },
                    1
                )
            } else {
                val isInvalid =
                    isOrderTypeInvalid || isMachineEmpty || isMachineTheSame || isStartDateInvalid || measureInvalid || preventiveCycleInvalid || isInitialSerialStateDateInvalid || isInitialSerialStateResponsableInvalid
                if (isInvalid) {
                    showSaveErroDialog(
                        osTypeInvalid = isOrderTypeInvalid,
                        bkpMachineEmpty = isMachineEmpty,
                        bkpMachineEquals = isMachineTheSame,
                        startDateInvalid = isStartDateInvalid,
                        measureInvalid = measureInvalid,
                        lastCycleInvalid = preventiveCycleInvalid,
                        isInitialSerialStateDateInvalid = isInitialSerialStateDateInvalid,
                        isInitialSerialStateResponsableInvalid = isInitialSerialStateResponsableInvalid,
                        calculatedCycle = if (calculatedExecCycle >= 0) calculatedExecCycle else 0.0f,
                        lastCycleVal = formOsHeader.last_cycle_value ?: 0f,
                        measureSufix = mainMeasureTp?.valueSufix ?: ""
                    )
                } else {
                    if (isLocationRequiredAndEnabled()) {
                        ToolBox.alertMSG_YES_NO(
                            requireContext(),
                            hmAuxTrans["alert_form_os_creation_ttl"],
                            hmAuxTrans["alert_form_os_creation_confirm"],
                            { _, _ ->
                                createOs()
                            },
                            1
                        )
                    } else {
                        showActiveGPSAlert()
                    }
                }
            }
        }
    }

    private fun FormOsHeaderFrgBinding.checkStoppedDate(): Boolean {
        clSerialInitialState.apply {
            return if (mkDateVal.isVisible) {
                isSerialStoppedDateInvalid(mkDateVal.getmValue(), mkdtStartDate.getmValue())
            } else {
                false
            }
        }
    }

    private fun isSerialStoppedDateInvalid(
        stoppedDate: String?,
        formOsInitialDate: String?
    ): Boolean {
        return stoppedDate?.let {
            !isDateBefore(it, formOsInitialDate)
        } ?: true
    }

    private fun createOs() {
        setDataIntoFormOsObj()
        mCreationListener?.createOsHeader(formOsHeader)
    }

    private fun bypassMinValidation(): Boolean {
        return ToolBox_Inf.profileExists(
            requireContext(),
            ConstantBaseApp.PROFILE_PRJ001_CHECKLIST,
            ConstantBaseApp.PROFILE_PRJ001_CHECKLIST_PARAM_ALLOW_FORM_SO_IN_THE_PAST
        ) && binding.swAllowFormSoInThePast.isChecked
    }

    /**
     * Valida se data iniicio informada é valida, não é no futuro e é maior que a data da ultima medição.
     */
    private fun isValidStartDate(): Boolean {
        return with(binding) {
            (mkdtStartDate.isValid
                    && !ToolBox_Inf.isFutureDate(mkdtStartDate.getmValue())
                    && (formOsHeader.last_measure_date == null
                    || ToolBox_Inf.dateToMilliseconds(formOsHeader.last_measure_date) <= ToolBox_Inf.dateToMilliseconds(
                mkdtStartDate.getmValue()
            )
                    )
                    )
        }
    }

    private fun isValidContinuosFormStartDate(): Boolean {
        return with(binding) {
            ToolBox_Inf.dateToMilliseconds(mkdtStartDate.getmValue()) > ToolBox_Inf.dateToMilliseconds(
                ticketForm?.partition_min_date
            )
        }
    }

    private fun showActiveGPSAlert() {
        ToolBox.alertMSG(
            requireContext(),
            hmAuxTrans["alert_form_turn_gps_on_title"],
            hmAuxTrans["alert_form_turn_gps_on_msg"],
            { dialog, which -> checkSave() },
            1
        )
    }

    /**
     * Fun que verifica se gps é requerido e se esta ligado.
     */
    private fun isLocationRequiredAndEnabled(): Boolean {
        return !formRequiresGPS
                || (formRequiresGPS && ToolBox_Con.hasGPSResourceActive(context))

    }

    private fun setDataIntoFormOsObj() {
        val orderType = orderTypeList[binding.spOsType.selectedItemPosition]
        formOsHeader.apply {
            order_type_code = orderType.orderTypeCode
            order_type_id = orderType.orderTypeId
            order_type_desc = orderType.orderTypeDesc
            process_type = orderType.processType
            display_option = orderType.displayOption
            process_vg = orderType.process_vg
            item_check_group_code = orderType.itemCheckGroupCode
            selectedBkpMachineProduct?.let { product ->
                backup_product_code = product.product_code.toInt()
                backup_product_id = product.product_id
                backup_product_desc = product.product_desc
            }
            backup_serial_code = selectedBkpMachineSerialCode
            backup_serial_id = selectedBkpMachineSerialId
            mainMeasureTp?.let { measure ->
                measure_tp_code = measure.measureTpCode
                measure_tp_id = measure.measureTpId
                measure_tp_desc = measure.measureTpDesc
                measure_value = getFormattedMeasureValue()
                measure_cycle_value = getMeasureCycleValue(orderType)
            }
            date_start = binding.mkdtStartDate.getmValue()
            //
            binding.clSerialInitialState.swInitialStateMachine
            //
            initial_is_serial_stopped =
                if (binding.clSerialInitialState.swInitialStateMachine.visibility == View.VISIBLE) {
                    if (binding.clSerialInitialState.swInitialStateMachine.isChecked) 1 else 0
                } else {
                    if (binding.clSerialInitialState.rbMachineStopped.isChecked) 1 else 0
                }
            if (initial_is_serial_stopped == 1
                && (initialSerialState?.showResponsableStopMachine == true)
            ) {
                initial_unavailability_reason =
                    if (binding.clSerialInitialState.rbMaintenance.isChecked) ResponsibleStop.MAINTENANCE.value else ResponsibleStop.THIRD_PARTY.value
            } else {
                initial_unavailability_reason = if (initial_is_serial_stopped == 0) {
                    ResponsibleStop.NO_STOPPED.value
                } else {
                    ResponsibleStop.STOPPED.value
                }
            }
            //
            val stoppedDate = binding.clSerialInitialState.mkDateVal.getmValue()
            initial_stopped_date = if (stoppedDate.isNullOrBlank()) null else stoppedDate
            //
        }
    }

    private fun getFormattedMeasureValue(): Float {
        return BigDecimal(binding.mketOsMainMeasureVal.commaFormatted).setScale(
            mainMeasureTp?.restrictionDecimal ?: ConstantBaseApp.FORM_OS_MEASURE_DECIMAL_DEFAULT,
            RoundingMode.HALF_DOWN
        ).toFloat()
    }

    /**
     * Fun que retorno o valor de measeruCycleValue.
     * Se orderType for preventiva ciclica, retorna o valor de calculatedExecCycle,
     * se não retorna o valor de measure_value.
     * É importante retornar o valor de measure_value no else, pois essa var calculatedExecCycle
     * é usada nas varreduras na criação das tabelas de geOs.
     */
    private fun getMeasureCycleValue(orderType: MdOrderType): Float {
        return if (isPreventiveCycledOs(orderType)) {
            calculatedExecCycle
        } else {
            getFormattedMeasureValue()
        }
    }

    /**
     * Verifica se é uma preventiva ciclicada, baseada no tipo de order preventiva e
     * medição com ciclo.
     */
    private fun isPreventiveCycledOs(orderType: MdOrderType) =
        (orderType.processType.equals(MdOrderType.PROCESS_TYPE_PREVENTIVE, true)
                && mainMeasureTp != null
                && mainMeasureTp?.cycleTolerance != null
                && mainMeasureTp?.valueCycleSize != null)

    private fun isPreventiveCycleValid(isOrderTypeInvalid: Boolean): Boolean {
        //Se orderType invalida, não tem como validar
        if (isOrderTypeInvalid) {
            return true
        }
        val mdOrderType = orderTypeList[binding.spOsType.selectedItemPosition]
        if (isPreventiveCycledOs(mdOrderType)) {
            if (binding.mketOsMainMeasureVal.commaFormatted.isNullOrEmpty() || !isMeasureValNumeric()) {
                return false
            } else {
                var lastCycle = formOsHeader.last_cycle_value ?: 0f
                //Valor inserido
                calculatedExecMeasureValue = binding.mketOsMainMeasureVal.commaFormatted.toFloat()
                //divide valor atual pelo tam do ciclo e arredonda pra cima, pra identificar o fator
                //de multiplicação do proxmo ciclo
                var tamDoCiclo = mainMeasureTp!!.valueCycleSize!!
                var cycleTolerance = mainMeasureTp!!.cycleTolerance!!

                var fatorNextCycle = ceil(calculatedExecMeasureValue.div(tamDoCiclo))
                //Calcula a tolerancia para ver se o valor digitado será aceito para ela
                var realTolerance = tamDoCiclo * (cycleTolerance / 100f)
                //Calcula o proximo ciclo
                calculatedExecCycle = fatorNextCycle * tamDoCiclo
                //Calcula valor com a tolerancia (proximo ciclo - tolerancia)
                var valWithTolerance = calculatedExecCycle - realTolerance
                //Se o valor digitado for
                if (calculatedExecMeasureValue.compareTo(valWithTolerance) < 0) {
                    calculatedExecCycle -= tamDoCiclo
                }
                //
                return if (!bypassMinValidation()) calculatedExecCycle > lastCycle else true
            }
        }
        //
        return true
    }

//    private fun isMeasureRestrictionInvalid(): Boolean {
//        mainMeasureTp?.let{
//            return if(!binding.mketOsMainMeasureVal.commaFormatted.isNullOrEmpty() && isMeasureValNumeric()){
//                val typedMeasure = binding.mketOsMainMeasureVal.commaFormatted.toFloat()
//                when(it.restrictionType){
//                    MeMeasureTp.RESTRICTION_TYPE_VALUE -> it.isMeasureRestrictionValueValid(typedMeasure,formOsHeader.last_measure_value).not()
//                    MeMeasureTp.RESTRICTION_TYPE_VALUE_BY_DAY -> it.isMeasureRestrictionValueByDayValid(typedMeasure,it).not()
//                    MeMeasureTp.RESTRICTION_TYPE_MIN_MAX  -> it.isMeasureRestrictionMinMaxValid(typedMeasure,it).not()
//                    else-> false
//                }
//            }else{
//                true
//            }
//        }
//        return false
//    }

    /**
     * Valida se medição digita é um float ou não
     */
    private fun isMeasureValNumeric(): Boolean {
        return try {
            binding.mketOsMainMeasureVal.commaFormatted.toFloat()
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun isMeasureRestrictionValueByDayValid(
        typedMeasure: Float,
        measureTp: MeMeasureTp
    ): Boolean {
        if (formOsHeader.last_measure_value != null && formOsHeader.last_measure_date != null) {
            //Como considera a data e inicio para calculo, se ela for invalida, o value by day tb será, pois não há como calcular.
            if (!isValidStartDate()) {
                return false
            }
            val valPerDay = getDiffBetweenDatesInFloatDays(
                formOsHeader.last_measure_date!!,
                binding.mkdtStartDate.getmValue()
            )
            //Se o valor for menor do que 0, considerar 0
            val minConsider: Double? = measureTp.restrictionMin?.let { min ->
                val minToConsider = formOsHeader.last_measure_value!! - (min * valPerDay)
                if (minToConsider >= 0f) {
                    minToConsider
                } else {
                    0f
                }
            } as Double?
            val maxConsider: Double? = measureTp.restrictionMax?.let { max ->
                formOsHeader.last_measure_value!! + (max * valPerDay)
            }
            //
            if (minConsider != null && maxConsider != null) {
                return minConsider.compareTo(typedMeasure) <= 0 && maxConsider.compareTo(
                    typedMeasure
                ) >= 0
            } else if (minConsider != null || maxConsider != null) {
                return if (minConsider != null) {
                    minConsider.compareTo(typedMeasure) <= 0
                } else {
                    maxConsider!!.compareTo(typedMeasure) >= 0
                }
            }
        }
        return true
    }

    /**
     * Calcula a diferença de dias entre 2 datas como float
     */
    private fun getDiffBetweenDatesInFloatDays(lastMeasureDate: String, startDate: String): Float {
        //Data passada em MS
        val lastMeasureDateMs = ToolBox_Inf.dateToMilliseconds(lastMeasureDate)
        //Data atual em MS
        val startDateInMs = ToolBox_Inf.dateToMilliseconds(startDate)
        //Diferença entre das data em MS
        val diffInMs = startDateInMs - lastMeasureDateMs
        //Calc dias inteiros
        val calcDay = diffInMs / ONE_DAY_IN_MILLISECOND
        //Calc perc de dias...
        val modDay =
            (diffInMs % ONE_DAY_IN_MILLISECOND.toDouble()) / ONE_DAY_IN_MILLISECOND.toDouble()
        //Soma e devolve float com 4 casas.
        return BigDecimal(calcDay + modDay).setScale(
            ConstantBaseApp.FORM_OS_MEASURE_DECIMAL_DEFAULT,
            RoundingMode.HALF_DOWN
        ).toFloat()
    }

    /*private fun addIdxToVisibleLabels() {
        var lblIdx = 1
        labelsView.forEach { obj ->
            if (obj.visibility == View.VISIBLE) {
                obj.text = "$lblIdx. ${obj.text}"
                obj.setAsRequired()
                lblIdx++
            }
        }
    }*/

    private fun iniActions() {
        with(binding) {
            //
            clSerialInitialState.apply {

                if (isOsCreation) {
                    swInitialStateMachine.setOnCheckedChangeListener { _, _ ->
                        if (swInitialStateMachine.isChecked) {
                            mkDateVal.visibility = View.VISIBLE
                            tvSerialStoppedDateLbl.visibility = View.VISIBLE
                            setInitialStateSerialStoppedDateVisibility(true)
                            setInitialStateSerialResponsableVisibility(
                                initialSerialState?.showResponsableStopMachine ?: false
                            )
                        } else {
                            setInitialStateSerialStoppedDateVisibility(false)
                            setInitialStateSerialResponsableVisibility(false)
                            mkDateVal.setmValue(null)
                            rgResponsableStopMachine.visibility = View.GONE
                            rgResponsableStopMachine.clearCheck()
                        }
                    }
                    //
                    rgStopMachine.setOnCheckedChangeListener { group, checkedId ->

                        if (rbMachineStopped.isChecked) {
                            mkDateVal.visibility = View.VISIBLE
                            if (initialSerialState?.isTicketSerialStopped == true) {
                                tvSerialStoppedDateLbl.visibility = View.GONE
                            } else {
                                tvSerialStoppedDateLbl.visibility = View.VISIBLE
                            }
                            initialSerialState?.let {
                                setInitialStateSerialResponsableVisibility(it.showResponsableStopMachine)
                            }
                        } else {
                            setInitialStateSerialResponsableVisibility(false)
                            mkDateVal.visibility = View.GONE
                            tvSerialStoppedDateLbl.visibility = View.GONE
                        }
                    }
                    ivSerialSearch.setOnClickListener {
                        if (mketMachineSerialEdit.isEnabled) {
                            if (selectedBkpMachineProduct != null && mketMachineSerialEdit.text.toString()
                                    .isNotEmpty()
                            ) {
                                mCreationListener?.searchSerialClick(
                                    bkpSerialId = mketMachineSerialEdit.text.toString()
                                )
                            }
                        } else {
                            clearMachineSerialValue()
                        }
                    }
                    swMachine.setOnCheckedChangeListener { _, _ ->
                        updateBkpMachineVisibility()
                        resetBkpMachineProductSerial()
                    }
                }
                //

            }
            if (isOsCreation) {
                ibTelemetry.setOnClickListener {
                    ToolBox_Inf.hideSoftKeyboard(context, binding.root)
                    val popupView = FormSupplierDialogBinding.inflate(layoutInflater)

                    initLayout(popupView)
                    // Crie o diálogo
                    // Create the popup window
                    val popupWindow = PopupWindow(
                        popupView.root,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        true
                    )
                    val popupMargin = if (isOsCreation) {
                        -190
                    } else {
                        -125
                    }
                    popupWindow.showAsDropDown(
                        ibTelemetry,
                        0,
                        ToolBox.dbToPixel(context, popupMargin)
                    )

//                binding.svMain.fullScroll(View.FOCUS_DOWN)
                }
                //
                swAllowFormSoInThePast.setOnCheckedChangeListener { _, _ ->
                    if (swAllowFormSoInThePast.isChecked) {
                        mketOsMainMeasureVal.isEnabled = true
                    } else {
                        mketOsMainMeasureVal.isEnabled = checkOsMainMeasureValEnable()
                    }
                }
            }
            //
            if (isOsCreation
                && !isContinuosFormPartition()
            ) {
                /*ivSwapMachine.setOnClickListener {
                    callProductSelection()
                }*/
                //
                mketMachineSerialEdit.setOnReportTextChangeListner(object :
                    MKEditTextNM.IMKEditTextChangeText {
                    override fun reportTextChange(text: String?) {
                    }

                    override fun reportTextChange(text: String?, textNotEmpty: Boolean) {
                        with(binding) {
                            //Se existe um serial ja escolhido, reseta vars.
                            if (selectedBkpMachineSerialCode != null || selectedBkpMachineSerialId != null) {
                                selectedBkpMachineSerialCode = null
                                selectedBkpMachineSerialId = null
                            }
                            tilMketSerial.isErrorEnabled = true
                            tilMketSerial.errorIconDrawable = null
//                            mketMachineSerialEdit.isError = true
                            tilMketSerial.error = hmAuxTrans["backup_serial_help_lbl"]
                            clMachineEdit.background =
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    com.namoa_digital.namoa_library.R.drawable.shape_error
                                )
                        }
                        ivSerialSearch.isEnabled = textNotEmpty
                    }
                })
                //
                mketMachineSerialEdit.setDelegateTextBySpecialist {
                    isBarcodeRead = true
                    ivSerialSearch.performClick()
                }
                mketOsMainMeasureVal.setDelegateTextBySpecialist {
                    if (!mketOsMainMeasureVal.isValid) {
                        mketOsMainMeasureVal.text = null
                    }
                }
                //
            }
        }
    }

    private fun FormOsHeaderFrgBinding.clearMachineSerialValue() {
        mketMachineSerialEdit.text?.clear()
        mketMachineSerialEdit.setmBARCODE(true)
        mketMachineSerialEdit.isEnabled = true
        ivSerialSearch.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_baseline_search_24
            )
        )
    }

    private fun checkOsMainMeasureValEnable(): Boolean {
        val diffTime =
            initialSerialState?.horimeter_date?.getDateDiferenceInMinutes(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"))
                ?: Long.MAX_VALUE
        val isBlockInput = initialSerialState?.measure_block_input_time?.let {
            isBlockInputTime(diffTime, it) && initialSerialState?.horimeter_supplier_uid != null
        } ?: false
        //
        if (isBlockInput) {
            initialSerialState?.horimeter?.let { horimeter ->
                binding.mketOsMainMeasureVal.setText(
                    ToolBox_Inf.formatLastMeaseureInfo(
                        context,
                        null,
                        horimeter.toFloat(),
                        null
                    )
                )
            }
        }
        //
        return isOsCreation
                && ticketForm?.measure_value == null
                && !isBlockInput
    }

    private fun initLayout(popupView: FormSupplierDialogBinding) {
        initialSerialState?.let { initialSerialState ->

            val horimeterFormatted = if(isOsCreation) {
                initialSerialState.horimeter?.let {
                    val horimeterAndSfix = ToolBox_Inf.convertFloatToBigDecimalString(it, true) +" " +(mainMeasureTp?.valueSufix?: "")
                    if (initialSerialState.horimeter_date != null) {
                        "$horimeterAndSfix  - ${
                            ToolBox_Inf.millisecondsToString(
                                ToolBox_Inf.dateToMilliseconds(initialSerialState.horimeter_date),
                                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                            )
                        }"
                    } else {
                        horimeterAndSfix
                    }
                } ?: run {
                    ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(initialSerialState.horimeter_date),
                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                    )
                }
            }else{
                null
            }
            //
            addTelemetryInfo(
                popupView.tvHorimeterLabel,
                popupView.tvHorimeterValue,
                hmAuxTrans["telemetry_horimeter_lbl"],
                horimeterFormatted
            )
            //
            val supplierDescFormatted =initialSerialState.horimeter_supplier_uid?.let {
                if(initialSerialState.horimeter_supplier_desc != null ){
                    "${initialSerialState.horimeter_supplier_uid} - ${initialSerialState.horimeter_supplier_desc}"
                }else{
                    initialSerialState.horimeter_supplier_uid
                }
            }?: run{
                initialSerialState.horimeter_supplier_desc
            }
            //
            addTelemetryInfo(
                popupView.tvHorimeterSupplierDescLabel,
                popupView.tvHorimeterSupplierDescValue,
                hmAuxTrans["telemetry_supplier_desc_lbl"],
                supplierDescFormatted
            )
            //
        }
    }

    private fun addTelemetryInfo(
        tvLabel: TextView,
        tvValue: TextView,
        label: String?,
        value: String?
    ) {

        tvLabel.visibility = View.GONE
        tvValue.visibility = View.GONE
        value?.let {
            tvLabel.visibility = View.VISIBLE
            tvValue.visibility = View.VISIBLE
            tvLabel.text = label ?: "-"
            tvValue.text = value
        }

    }

    private fun callProductSelection() {
        val mIntent = Intent(context, Act_Product_Selection::class.java)
        val bundle = Bundle()
        //
        bundle.putBoolean(Act_Product_Selection.IS_ADD_PRODUCT_LIST, false)
        mIntent.putExtras(bundle)
        //
        startActivityForResult(mIntent, ConstantBaseApp.ACT_PRODUCT_SELECTION_REQUEST_CODE)
    }

    private fun resetBkpMachineProductSerial() {
        with(binding) {
//            tvMachineProdEditLbl.text = defaultBkpMachineProduct?.product_desc
            mketMachineSerialEdit.text = null
            mketMachineSerialEdit.setmBARCODE(true)
            selectedBkpMachineProduct = if (swMachine.isChecked) defaultBkpMachineProduct else null
            clMachineEdit.background = if (swMachine.isChecked) {
                ContextCompat.getDrawable(
                    requireContext(),
                    com.namoa_digital.namoa_library.R.drawable.shape_error
                )
            } else {
                ContextCompat.getDrawable(
                    requireContext(),
                    com.namoa_digital.namoa_library.R.drawable.shape_ok
                )
            }
        }
    }

    override fun getTabErrorCount(): Int {
        //Sem emodo edição, sem erros
        return 0
    }

    override fun getTabCount(): Int {
        return 0
    }

    override fun getTabObj(skipFieldValidation: Boolean): Act011FormTab {
        return Act011FormTab(
            page = tabIndex,
            name = mTabName,
            tracking = null,
            fieldCount = -1,
            problemReportedCount = null,
            forecastCount = null,
            criticalForecastCount = null,
            nonForecastCount = null,
            status = if (skipFieldValidation) Act011FormTabStatus.PENDING else getTabStatus()
        )

    }

    override fun getTabStatus(): Act011FormTabStatus {
        return Act011FormTabStatus.OK
    }

    override fun getTabName(): String {
        return hmAuxTrans["form_os_header_lbl"] ?: "form_os_header - trad"
    }

    override fun applyAutoAnswer(): Int {
        return 0
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FormOsHeaderFrgMeasureInteraction) {
            measureTpListener = context
        } else if (context is FormOsHeaderFrgCreationInteraction) {
            mCreationListener = context
        } else if (isOsCreation) {
            //Se criação e interface não definida, solta exception
            throw RuntimeException("$context must implement FormOsHeaderFrgCreationInteraction")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mCreationListener?.delegateControlSta(controlsSta, false)
        mCreationListener = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //
        if (requestCode == ConstantBaseApp.ACT_PRODUCT_SELECTION_REQUEST_CODE
            && resultCode == Base_Activity_Frag.RESULT_OK
        ) {
            data?.let {
                /*selectedBkpMachineProduct =
                    it.getSerializableExtra(MD_Product::class.java.name) as MD_Product?
                binding.tvMachineProdEditLbl.text = selectedBkpMachineProduct?.product_desc*/
                resetBkpSerialInfo()
            }
        }
    }

    private fun resetBkpSerialInfo() {
        binding.mketMachineSerialEdit.setText("")
        selectedBkpMachineSerialCode = null
        selectedBkpMachineSerialId = null
    }

    fun showSaveErroDialog(
        osTypeInvalid: Boolean = false,
        bkpMachineEmpty: Boolean = false,
        bkpMachineEquals: Boolean = false,
        startDateInvalid: Boolean = false,
        continuousFormStartDateInvalid: Boolean = false,
        measureInvalid: Boolean = false,
        lastCycleInvalid: Boolean = false,
        isInitialSerialStateDateInvalid: Boolean = false,
        isInitialSerialStateResponsableInvalid: Boolean = false,
        calculatedCycle: Float = 0f,
        lastCycleVal: Float = 0f,
        measureSufix: String = ""
    ) {
        val builder = AlertDialog.Builder(requireContext())
        val dialogBinding = FormOsHeaderFrgErrorDialogBinding.inflate(layoutInflater)
        with(dialogBinding) {
            tvOsTypeInvalidMsg.apply {
                visibility = if (osTypeInvalid) View.VISIBLE else View.GONE
                text = hmAuxTrans["alert_invalid_order_type_error_msg"]
                setPrefix(getString(R.string.unicode_bullet).plus(" "))
            }
            tvBkpMachineEmptyMsg.apply {
                visibility = if (bkpMachineEmpty) View.VISIBLE else View.GONE
                text = hmAuxTrans["alert_empty_bkp_machine_error_msg"]
                setPrefix(getString(R.string.unicode_bullet).plus(" "))
            }
            tvBkpMachineEqualMsg.apply {
                visibility = if (bkpMachineEquals) View.VISIBLE else View.GONE
                text = hmAuxTrans["alert_same_serial_bkp_machine_error_msg"]
                setPrefix(getString(R.string.unicode_bullet).plus(" "))
            }
            tvStarDateInvalidMsg.apply {
                visibility = if (startDateInvalid) View.VISIBLE else View.GONE
                text = hmAuxTrans["alert_invalid_star_date_error_msg"]
                setPrefix(getString(R.string.unicode_bullet).plus(" "))
            }
            tvContinuosFormStarDateInvalidMsg.apply {
                visibility = if (continuousFormStartDateInvalid) View.VISIBLE else View.GONE
                val min_date = ticketForm?.partition_min_date?.formatTo(
                    ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                )
                //
                text = "${hmAuxTrans["alert_invalid_start_date_partition_error_msg"]} $min_date"
                setPrefix(getString(R.string.unicode_bullet).plus(" "))
            }
            tvMeasureInvalidValMsg.apply {
                visibility = if (measureInvalid) View.VISIBLE else View.GONE
                text = hmAuxTrans["alert_invalid_measure_value_error_msg"]
                setPrefix(getString(R.string.unicode_bullet).plus(" "))
            }
            tvMeasureInvalidZeroCycleMsg.apply {
                visibility =
                    if (lastCycleInvalid && calculatedCycle == 0f) View.VISIBLE else View.GONE
                text = hmAuxTrans["alert_invalid_measure_zero_cycle_error_msg"]
                setPrefix(getString(R.string.unicode_bullet).plus(" "))
            }
            tvMeasureInvalidCycleMsg.apply {
                visibility =
                    if (lastCycleInvalid && calculatedCycle.compareTo(0) > 0) View.VISIBLE else View.GONE
                text = hmAuxTrans["alert_invalid_measure_cycle_error_msg"]
                setPrefix(getString(R.string.unicode_bullet).plus(" "))
            }
            tvInitialSerialStateDateInvalidMsg.apply {
                visibility =
                    if (isInitialSerialStateDateInvalid) View.VISIBLE else View.GONE
                text = hmAuxTrans["alert_invalid_initial_serial_state_date_error_msg"]
                setPrefix(getString(R.string.unicode_bullet).plus(" "))
            }
            tvInitialSerialStateResponsableInvalidMsg.apply {
                visibility =
                    if (isInitialSerialStateResponsableInvalid) View.VISIBLE else View.GONE
                text = hmAuxTrans["alert_invalid_initial_serial_state_responsable_error_msg"]
                setPrefix(getString(R.string.unicode_bullet).plus(" "))
            }
            tvLastCycleLbl.apply {
                visibility =
                    if (lastCycleInvalid && calculatedCycle.compareTo(0) > 0) View.VISIBLE else View.GONE
                text = hmAuxTrans["alert_last_cycle_lbl"].plus(" :")
            }
            tvLastCycleVal.apply {
                visibility =
                    if (lastCycleInvalid && calculatedCycle.compareTo(0) > 0) View.VISIBLE else View.GONE
                text = "$lastCycleVal $measureSufix"
            }
            tvCurrentCycleLb.apply {
                visibility =
                    if (lastCycleInvalid && calculatedCycle.compareTo(0) > 0) View.VISIBLE else View.GONE
                text = hmAuxTrans["alert_current_cycle_lbl"].plus(" :")
            }
            tvCurrentCycleVal.apply {
                visibility =
                    if (lastCycleInvalid && calculatedCycle.compareTo(0) > 0) View.VISIBLE else View.GONE
                text = "$calculatedCycle $measureSufix"
            }
        }
        //
        val dialog = builder.apply {
            setTitle(hmAuxTrans["erro_dialog_ttl"])
            setView(dialogBinding.root)
            setPositiveButton(
                hmAuxTrans["sys_alert_btn_ok"],
                null
            )
        }.setOnDismissListener {
            calculatedExecCycle = -1f
            calculatedExecMeasureValue = -1f
        }.create()
        //
        dialog.show()
        //LUCHE - 01/12/2021 - Ajuste necessario para Android 6 ¬¬
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    fun showBkpMachineDialog(
        serialBkpMachineList: List<BaseSerialSearchItem>,
        onlineSearch: Boolean
    ) {
        val builder = AlertDialog.Builder(requireContext())
        val dialogBinding = BackupSerialSearchListDialogBinding.inflate(layoutInflater)
        with(dialogBinding) {
            tvDialogTtl.text = hmAuxTrans["alert_bkp_serial_ttl"]
            //
            ivOffilineIcon.visibility = if (onlineSearch) {
                View.INVISIBLE
            } else {
                View.VISIBLE
            }
            //
            rvSerialList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = GenericSerialListDialog(
                    serialBkpMachineList,
                    ToolBox_Con.getPreference_Site_Code(context).toInt(),
                    ::setSelectedBkpMachineSerial
                )
            }
            btnCancel.apply {
                text = hmAuxTrans["sys_alert_btn_cancel"]
                setOnClickListener {
                    bkpMachineDialog?.dismiss()
                }
            }
        }
        //
        bkpMachineDialog = builder.apply {
            setView(dialogBinding.root)
            setCancelable(false)
        }.setOnDismissListener {
            bkpMachineDialog = null
        }.create()
        //
        bkpMachineDialog?.show()

    }
}