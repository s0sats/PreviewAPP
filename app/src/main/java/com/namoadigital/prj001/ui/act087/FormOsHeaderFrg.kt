package com.namoadigital.prj001.ui.act087

import android.content.Context
import android.content.DialogInterface
import android.content.Intent

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.Base_Activity_Frag
import com.namoadigital.prj001.R
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao
import com.namoadigital.prj001.databinding.FormOsHeaderFrgBinding
import com.namoadigital.prj001.databinding.FormOsHeaderFrgErrorDialogBinding
import com.namoadigital.prj001.extensions.setAsRequired
import com.namoadigital.prj001.extensions.setPrefix
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.ui.act011.frags.Act011BaseFrg
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import com.namoadigital.prj001.view.act.product_selection.Act_Product_Selection
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.ceil

class FormOsHeaderFrg : Act011BaseFrg<FormOsHeaderFrgBinding>() {

    private var isOsCreation: Boolean = false
    private lateinit var formOsHeader: GeOs
    private var mCreationListener: FormOsHeaderFrgCreationInteraction? = null
    private var orderTypeList: ArrayList<MdOrderType> = arrayListOf()
    private val spinnerAdapter: ArrayAdapter<String> by lazy{
            ArrayAdapter(
                requireContext(),
                R.layout.form_os_header_frg_spinner_item,
                orderTypeList.map {
                    it.orderTypeDesc
                }
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
    private var calculatedExecCycle: Int = -1

    companion object{
        @JvmStatic
        fun newInstance(
            hmAuxTrans: HMAux,
            tabIndex: Int = 1,
            tabLastIndex: Int = 1,
            formStatus: String,
            scheduleDesc: String?,
            scheduleComments: String?,
            formOsHeader: GeOs,
            isOsCreation: Boolean = false
        ) = FormOsHeaderFrg()
            .apply{
                this.hmAuxTrans = hmAuxTrans
                this.formStatus = formStatus
                this.tabIndex = tabIndex
                this.tabLastIndex = tabLastIndex
                this.scheduleDesc = scheduleDesc
                this.scheduleComments = scheduleComments
                this.formOsHeader = formOsHeader
                this.isOsCreation = isOsCreation
                //
                arguments = Bundle().apply {
                    putSerializable(Constant.MAIN_HMAUX_TRANS_KEY, hmAuxTrans)
                    putString(GE_Custom_Form_DataDao.CUSTOM_FORM_STATUS,formStatus)
                    putInt(GE_Custom_Form_Field_LocalDao.PAGE,tabIndex)
                    putInt(PARAM_LAST_INDEX,tabLastIndex)
                    putString(MD_Schedule_ExecDao.SCHEDULE_DESC,scheduleDesc)
                    putString(GE_Custom_Form_Field_LocalDao.COMMENT,scheduleComments)
                }
            }

        val mResource_Name = "form_os_header"

        fun getFragTranslationsVars() : List<String>{
            return listOf(
                "order_type_lbl",
                "use_backup_lbl",
                "start_date_lbl",
                "backup_serial_hint",
                "backup_serial_help_lbl",
                "measure_current_value_hint",
                "measure_last_value_lbl",
                "btn_save",
                "erro_dialog_ttl",
                "alert_invalid_order_type_error_msg",
                "alert_empty_bkp_machine_error_msg",
                "alert_same_serial_bkp_machine_error_msg",
                "alert_invalid_star_date_error_msg",
                "alert_invalid_measure_value_error_msg",
                "alert_invalid_measure_cycle_error_msg",
                "alert_last_cycle_lbl",
                "alert_current_cycle_lbl"
            )
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
        if(isOsCreation) {
            controlsSta.clear()
            controlsSta.add(binding.mketMachineSerialEdit)
            controlsSta.add(binding.mketOsMainMeasureVal)
            mCreationListener?.delegateControlSta(controlsSta,true)
        }
    }

    private fun setNavegationIncludeVisibility() {
        if (isOsCreation) {
            binding.incNavegation.root.visibility = View.GONE
        }
    }

    private fun setLabels() {
        with(binding){
            tvOsTypeLbl.text = hmAuxTrans["order_type_lbl"]
            tvOsMachineLbl.text = hmAuxTrans["use_backup_lbl"]
            tvOsStartDateLbl.text = hmAuxTrans["start_date_lbl"]
            mketMachineSerialEdit.hint = hmAuxTrans["backup_serial_hint"]
            tilMketSerial.helperText = hmAuxTrans["backup_serial_help_lbl"]
            mketOsMainMeasureVal.hint = hmAuxTrans["measure_current_value_hint"]
            tvOsLastMeasureLbl.text = hmAuxTrans["measure_last_value_lbl"]
            //Lista com os textView que será usado para add colocar contadore nos campos
            labelsView.add(tvOsTypeLbl)
            labelsView.add(tvOsMachineLbl)
            labelsView.add(tvOsStartDateLbl)
            labelsView.add(tvOsMainMeasureLbl)
        }
    }

    private fun iniVars() {
        iniOrderTypeSpinner()
        iniBkpMachine()
        iniStartDate()
        iniMainMeasure()
        iniLastMeasureInfo()
        iniSaveBtn()
        addIdxToVisibleLabels()
    }

    private fun iniOrderTypeSpinner() {
        if(isOsCreation) {
            mCreationListener?.let {
                orderTypeList = it.getOrderTypeList()
            }
            binding.spOsType.apply {
                adapter = spinnerAdapter
                //Se existe order default seta
                formOsHeader.so_order_type_code_default?.let {
                    val orderTypeDefaultIdx = getOrderTypeIdx(it)
                    if(orderTypeDefaultIdx > -1) {
                        setSelection(orderTypeDefaultIdx)
                    }
                }
                //Se order não é permitido altera, desabilita.
                isEnabled = formOsHeader.so_allow_change_order_type == 1
            }
        }
    }

    private fun getOrderTypeIdx(orderTypeCode: Int): Int {
        var orderTypeDefaultIdx = -1
        if(orderTypeCode != -1) {
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
            if(formOsHeader.so_allow_backup == 1) {
                swMachine.isChecked = (formOsHeader.backup_product_code != null)
                updateBkpMachineVisibility()
                if (isOsCreation) {
                    mCreationListener?.let {
                        defaultBkpMachineProduct = it.getDefaultBkpMachineProduct()
                        tvMachineProdEditLbl.text =
                            defaultBkpMachineProduct?.product_desc?.toUpperCase()
                    }
                } else {
                    formOsHeader.backup_product_code?.let {
                        tvMachineProdEditLbl.text = formOsHeader.backup_product_desc
                    }
                    formOsHeader.backup_serial_code?.let {
                        mketMachineSerialEdit.setText(formOsHeader.backup_serial_id)
                    }
                    swMachine.isEnabled = false
                }
            }else{
                clMachineEdit.visibility = View.GONE
                tvOsMachineLbl.visibility = View.GONE
                swMachine.isEnabled = false
            }
        }
    }

    private fun updateBkpMachineVisibility() {
        with(binding){
            gpBkpMachineVal.visibility = if (swMachine.isChecked) View.VISIBLE else View.GONE
        }
    }

    private fun iniStartDate() {
        with(binding){
            mkdtStartDate.apply {
                setmLabel(null)
                setmCanClean(false)
                if(isOsCreation){
                    setmValue(ToolBox.sDTFormat_Agora(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT))
                }
                setmEnabled(formOsHeader.so_edit_start_end == 1)
            }
        }
    }

    private fun iniMainMeasure() {
            with(binding){
                clMainMeasure.visibility = if(formOsHeader.measure_tp_code != null) View.VISIBLE else View.GONE
                formOsHeader.measure_tp_desc?.let{
                    tvOsMainMeasureLbl.text = it
                }
                formOsHeader.measure_value?.let {
                    mketOsMainMeasureVal.setText(it.toString())
                }
                formOsHeader.measure_tp_code?.let{
                    mainMeasureTp = mCreationListener?.getMeasure(it)
                }
                mainMeasureTp?.let { measure->
                    measure.restrictionDecimal?.let{ decimal ->
                        mketOsMainMeasureVal.setmDecimal(decimal)
                    }
                }
            }
    }

    private fun iniLastMeasureInfo() {
        with(binding){
            tvOsLastMeasureVal.text = formatLastMeaseureInfo(formOsHeader.last_measure_value,formOsHeader.last_measure_date)
            clLastMeasure.visibility = if(!tvOsLastMeasureVal.text.toString().isNullOrEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun formatLastMeaseureInfo(
        lastMeasureValue: Float?,
        lastMeasureDate: String?
    ): String {
        if(lastMeasureValue != null && lastMeasureDate != null){
            return "$lastMeasureValue - ${
                ToolBox_Inf.millisecondsToString(
                    ToolBox_Inf.dateToMilliseconds(
                        lastMeasureDate
                    ),
                    ToolBox_Inf.nlsDateFormat(requireContext()) + " HH:mm"
                )}"
        }else{
            var info = ""
            lastMeasureValue?.let{
                info += it.toString()
            }
            lastMeasureDate?.let{
                info += ToolBox_Inf.millisecondsToString(
                    ToolBox_Inf.dateToMilliseconds(
                        it,
                        ConstantBaseApp.DATE_TO_MILLISECOND_TYPE_IGNORE_SECOND
                    ),
                    ToolBox_Inf.nlsDateFormat(requireContext()) + " HH:mm"
                )
            }
            return info
        }
    }

    private fun iniSaveBtn() {
        if(isOsCreation){
            binding.btnSave.apply {
                visibility = View.VISIBLE
                text = hmAuxTrans["btn_save"]
                setOnClickListener {
                    validateSave()
                }
            }
        }
    }

    private fun validateSave() {
        with(binding){
            val isOrderTypeInvalid = (spOsType.selectedItemPosition > orderTypeList.lastIndex || orderTypeList[spOsType.selectedItemPosition].orderTypeCode <= 0)
            val isMachineEmpty =  swMachine.isChecked && (selectedBkpMachineProduct == null || selectedBkpMachineSerialCode == null)
            val isMachineTheSame = (swMachine.isChecked && !isMachineEmpty && defaultBkpMachineProduct?.product_code == selectedBkpMachineProduct?.product_code && selectedBkpMachineSerialId == formSerialId)
            val isStartDateInvalid = ( mkdtStartDate.isValid
                                       && !ToolBox_Inf.isFutureDate(mkdtStartDate.getmValue())
                                       && ( formOsHeader.last_measure_date == null
                                            || ToolBox_Inf.dateToMilliseconds(formOsHeader.last_measure_date) <= ToolBox_Inf.dateToMilliseconds(mkdtStartDate.getmValue())
                                       )
                                    ).not()
            clMachineEdit.background = if (isMachineEmpty || isMachineTheSame) {
                ContextCompat.getDrawable(requireContext(), R.drawable.shape_error)
            } else {
                ContextCompat.getDrawable(requireContext(), R.drawable.shape_ok)
            }
            val measureInvalid = isMeasureRestrictionInvalid()
            val preventiveCycleInvalid = isPreventiveCycleValid(isOrderTypeInvalid).not()
            //
            if(isOrderTypeInvalid || isMachineEmpty || isMachineTheSame || isStartDateInvalid || measureInvalid || preventiveCycleInvalid ){
                showSaveErroDialog(
                    osTypeInvalid = isOrderTypeInvalid,
                    bkpMachineEmpty= isMachineEmpty,
                    bkpMachineEquals = isMachineTheSame,
                    startDateInvalid = isStartDateInvalid,
                    measureInvalid = measureInvalid,
                    lastCycleInvalid = preventiveCycleInvalid,
                    calculatedCycle = calculatedExecCycle,
                    lastCycleVal = formOsHeader.last_cycle_value?:0,
                    measureSufix = mainMeasureTp?.valueSufix?:""
                )
            }else{
                ToolBox.alertMSG(
                    requireContext(),
                    hmAuxTrans["alert_form_os_creation_ttl"],
                    hmAuxTrans["alert_form_os_creation_confirm"],
                    DialogInterface.OnClickListener { _, _ ->
                       setDataIntoFormOsObj()
                       mCreationListener?.createOsHeader(formOsHeader)
                   },
                    0
                )

            }
        }
    }

    private fun setDataIntoFormOsObj() {
        val orderType = orderTypeList[binding.spOsType.selectedItemPosition]
        formOsHeader.apply {
            order_type_code = orderType.orderTypeCode
            order_type_id = orderType.orderTypeId
            order_type_desc = orderType.orderTypeDesc
            selectedBkpMachineProduct?.let{ product ->
                backup_product_code = product.product_code.toInt()
                backup_product_id = product.product_id
                backup_product_desc = product.product_desc
            }
            backup_serial_code = selectedBkpMachineSerialCode
            backup_serial_id = selectedBkpMachineSerialId
            mainMeasureTp?.let{ measure ->
                measure_tp_code = measure.measureTpCode
                measure_tp_id = measure.measureTpId
                measure_tp_desc = measure.measureTpDesc
                measure_value = binding.mketOsMainMeasureVal.text.toString().toFloat()
                measure_cycle_value = calculatedExecCycle

            }
            start_date = binding.mkdtStartDate.getmValue()
        }
    }

    private fun isPreventiveCycleValid(isOrderTypeInvalid: Boolean): Boolean {
        //Se orderType invalida, não tem como validar
        if(isOrderTypeInvalid){
            return true
        }
        val mdOrderType = orderTypeList[binding.spOsType.selectedItemPosition]
        if( mdOrderType.processType == MdOrderType.ProcessType.PREVENTIVE
            && mainMeasureTp != null
            && mainMeasureTp?.cycleTolerance != null
            && mainMeasureTp?.valueCycleSize != null
        ) {
            if (binding.mketOsMainMeasureVal.text.isNullOrEmpty()) {
                return false
            }else{
                var lastCycle = formOsHeader.last_cycle_value?:0
                //Valor inserido
                calculatedExecMeasureValue = binding.mketOsMainMeasureVal.text.toString().toFloat()
                //divide valor atual pelo tam do ciclo e arredonda pra cima, pra identificar o fator
                //de multiplicação do proxmo ciclo
                var tamDoCiclo = mainMeasureTp!!.valueCycleSize!!
                var cycleTolerance = mainMeasureTp?.cycleTolerance!!

                var fatorNextCycle = ceil(calculatedExecMeasureValue.div(tamDoCiclo)).toInt()
                //Calcula a tolerancia para ver se o valor digitado será aceito para ela
                var realTolerance = tamDoCiclo * (cycleTolerance / 100f)
                //Calcula o proximo ciclo
                calculatedExecCycle = fatorNextCycle * tamDoCiclo
                //Calcula valor com a tolerancia (proximo ciclo - tolerancia)
                var valWithTolerance = calculatedExecCycle - realTolerance
                //Se o valor digitado for
                if(calculatedExecMeasureValue.compareTo(valWithTolerance) < 0){
                    calculatedExecCycle -= tamDoCiclo
                }
                //
                return calculatedExecCycle > lastCycle
            }
        }
        //
        return true
    }

    private fun isMeasureRestrictionInvalid(): Boolean {
        mainMeasureTp?.let{
            return if(!binding.mketOsMainMeasureVal.text.isNullOrEmpty()){
                val typedMeasure = binding.mketOsMainMeasureVal.text.toString().toFloat()
                when(it.restrictionType){
                    MeMeasureTp.RESTRICTION_TYPE_VALUE -> isMeasureRestrictionValueValid(typedMeasure,it).not()
                    MeMeasureTp.RESTRICTION_TYPE_VALUE_BY_DAY -> isMeasureRestrictionValueByDayValid(typedMeasure,it).not()
                    MeMeasureTp.RESTRICTION_TYPE_MIN_MAX  -> isMeasureRestrictionMinMaxValid(typedMeasure,it).not()
                    else-> false
                }
            }else{
                true
            }
        }
        return false
    }

    private fun isMeasureRestrictionValueByDayValid(
        typedMeasure: Float,
        measureTp: MeMeasureTp
    ): Boolean {
        if(formOsHeader.last_measure_value != null && formOsHeader.last_measure_date != null) {
            val valPerDay = getDiffBetweenDatesInFloatDays(formOsHeader.last_measure_date!!)
            //Se o valor for menor do que 0, considerar 0
            val minConsider : Float? = measureTp.restrictionMin?.let { min->
                    val minToConsider = formOsHeader.last_measure_value!! - (min * valPerDay)
                    if(minToConsider >= 0f){
                        minToConsider
                    } else {
                        0f
                    }
            }
            val maxConsider : Float? = measureTp.restrictionMax?.let { max->
                formOsHeader.last_measure_value!! + (max * valPerDay)
            }
            //
            if(minConsider != null && maxConsider != null){
                return minConsider.compareTo(typedMeasure) <= 0 && maxConsider.compareTo(typedMeasure) >= 0
            }else if (minConsider != null || maxConsider != null ){
                return if(minConsider != null){
                    minConsider.compareTo(typedMeasure) <= 0
                }else{
                    maxConsider!!.compareTo(typedMeasure) >= 0
                }
            }
        }
        return true
    }

    /**
     * Calcula a diferença de dias entre 2 datas como float
     */
    private fun getDiffBetweenDatesInFloatDays(lastMeasureDate: String): Float {
        //Qtd de ms em um dias
        val ONE_DAY_IN_MILLISECOND = 86400000
        //Data passada em MS
        val lastMeasureDateMs = ToolBox_Inf.dateToMilliseconds(lastMeasureDate)
        //Data atual em MS
        val nowInMs = Calendar.getInstance().timeInMillis
        //Diferença entre das data em MS
        val diffInMs = nowInMs - lastMeasureDateMs
        //Calc dias inteiros
        val calcDay = diffInMs / ONE_DAY_IN_MILLISECOND
        //Calc perc de dias...
        val modDay = (diffInMs % ONE_DAY_IN_MILLISECOND.toDouble()) / ONE_DAY_IN_MILLISECOND.toDouble()
        //Soma e devolve float com 2 casas.
        return BigDecimal(calcDay + modDay).setScale(2,RoundingMode.HALF_DOWN).toFloat()
    }

    private fun isMeasureRestrictionValueValid(
        typedMeasure: Float,
        measureTp: MeMeasureTp,
    ): Boolean {
        formOsHeader.last_measure_value?.let { lastMeasure->
            val tesVl = lastMeasure
            val minConsider: Float? = if(measureTp.restrictionMin != null){
                    lastMeasure.minus(measureTp.restrictionMin)
                } else {
                    null
                }
            val maxConsider: Float? = if(measureTp.restrictionMax != null){
                lastMeasure.plus(measureTp.restrictionMax)
            } else {
                null
            }
            //
            if(minConsider != null && maxConsider != null ){
                return minConsider.compareTo(typedMeasure) <= 0 && maxConsider.compareTo(typedMeasure) >= 0
            }else if (minConsider != null || maxConsider != null ){
                return if(minConsider != null){
                    minConsider.compareTo(typedMeasure) <= 0
                }else{
                    maxConsider!!.compareTo(typedMeasure) >= 0
                }
            }
        }
        //
        return true
    }

    private fun isMeasureRestrictionMinMaxValid(
        typedMeasure: Float,
        it: MeMeasureTp,
    ): Boolean {
        return if (it.restrictionMin != null && it.restrictionMax != null) {
            it.restrictionMin <= typedMeasure && typedMeasure <= it.restrictionMax
        } else if (it.restrictionMin != null || it.restrictionMax != null) {
            if (it.restrictionMin != null) {
                it.restrictionMin <= typedMeasure
            } else {
                typedMeasure <= it.restrictionMax!!
            }
        } else {
            true
        }
    }

    private fun addIdxToVisibleLabels() {
        var lblIdx = 1
        labelsView.forEach{ obj->
            if(obj.visibility == View.VISIBLE){
                obj.text = "$lblIdx. ${obj.text}"
                obj.setAsRequired()
                lblIdx++
            }
        }
    }

    private fun iniActions() {
        with(binding){
            swMachine.setOnCheckedChangeListener { _, _ ->
                updateBkpMachineVisibility()
                resetBkpMachineProductSerial()
            }
            //
            ivSwapMachine.setOnClickListener {
                callProductSelection()
            }
            //
            ivSerialSearch.setOnClickListener {
                mCreationListener?.searchSerialClick()
            }
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
        selectedBkpMachineProduct = null
        with(binding){
            tvMachineProdEditLbl.text = defaultBkpMachineProduct?.product_desc
            mketMachineSerialEdit.text = null
            clMachineEdit.background = if(swMachine.isChecked){
                ContextCompat.getDrawable(requireContext(),R.drawable.shape_error)
            }else{
                ContextCompat.getDrawable(requireContext(),R.drawable.shape_ok)
            }
        }
    }

    override fun getTabErrorCount(): Int {
        return 0
    }

    override fun getTabCount(): Int {
        return 0
    }

    override fun getTabObj(skipFieldValidation: Boolean): Act011FormTab {
       return Act011FormTab(
            0,
            "",
            "",
            0,
            null,
            null,
            null,
            null,
            Act011FormTabStatus.PENDING
        )
    }

    override fun getTabStatus(): Act011FormTabStatus {
        return Act011FormTabStatus.PENDING
    }

    override fun getTabName(): String {
        return ""
    }

    override fun applyAutoAnswer(): Int {
        return 0
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is FormOsHeaderFrgCreationInteraction){
            mCreationListener = context
        } else if (isOsCreation){
            //Se criação e interface não definida, solta exception
            throw RuntimeException("${context.toString()} must implement FormOsHeaderFrgCreationInteraction")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mCreationListener?.delegateControlSta(controlsSta,false)
        mCreationListener = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //
        if(requestCode == ConstantBaseApp.ACT_PRODUCT_SELECTION_REQUEST_CODE
            && resultCode == Base_Activity_Frag.RESULT_OK
        ){
            data?.let {
                selectedBkpMachineProduct = it.getSerializableExtra(MD_Product::class.java.name) as MD_Product?
                binding.tvMachineProdEditLbl.text = selectedBkpMachineProduct?.product_desc
            }
        }
    }

    fun showSaveErroDialog(
        osTypeInvalid: Boolean = false,
        bkpMachineEmpty: Boolean = false,
        bkpMachineEquals: Boolean = false,
        startDateInvalid: Boolean = false,
        measureInvalid: Boolean = false,
        lastCycleInvalid: Boolean = false,
        calculatedCycle: Int = 0,
        lastCycleVal: Int = 0,
        measureSufix: String = ""
    ){
        val builder = AlertDialog.Builder(requireContext())
        val dialogBinding = FormOsHeaderFrgErrorDialogBinding.inflate(layoutInflater)
        with(dialogBinding){
            tvOsTypeInvalidMsg.apply {
                visibility = if(osTypeInvalid) View.VISIBLE else View.GONE
                text = hmAuxTrans["alert_invalid_order_type_error_msg"]
                setPrefix(getString(R.string.unicode_bullet).plus(" "))
            }
            tvBkpMachineEmptyMsg.apply {
              visibility = if(bkpMachineEmpty) View.VISIBLE else View.GONE
              text = hmAuxTrans["alert_empty_bkp_machine_error_msg"]
              setPrefix(getString(R.string.unicode_bullet).plus(" "))
            }
            tvBkpMachineEqualMsg.apply {
                visibility = if(bkpMachineEquals) View.VISIBLE else View.GONE
                text = hmAuxTrans["alert_same_serial_bkp_machine_error_msg"]
                setPrefix(getString(R.string.unicode_bullet).plus(" "))
            }
            tvStarDateInvalidMsg.apply {
                visibility = if(startDateInvalid) View.VISIBLE else View.GONE
                text = hmAuxTrans["alert_invalid_star_date_error_msg"]
                setPrefix(getString(R.string.unicode_bullet).plus(" "))
            }
            tvMeasureInvalidValMsg.apply {
                visibility = if(measureInvalid) View.VISIBLE else View.GONE
                text = hmAuxTrans["alert_invalid_measure_value_error_msg"]
                setPrefix(getString(R.string.unicode_bullet).plus(" "))
            }
            tvMeasureInvalidCycleMsg.apply {
                visibility = if(lastCycleInvalid) View.VISIBLE else View.GONE
                text = hmAuxTrans["alert_invalid_measure_cycle_error_msg"]
                setPrefix(getString(R.string.unicode_bullet).plus(" "))
            }
            tvLastCycleLbl.apply {
                visibility = if(lastCycleInvalid) View.VISIBLE else View.GONE
                text = hmAuxTrans["alert_last_cycle_lbl"].plus(" :")
            }
            tvLastCycleVal.apply {
                visibility = if(lastCycleInvalid) View.VISIBLE else View.GONE
                text = "$lastCycleVal $measureSufix"
            }
            tvCurrentCycleLb.apply {
                visibility = if(lastCycleInvalid) View.VISIBLE else View.GONE
                text = hmAuxTrans["alert_current_cycle_lbl"].plus(" :")
            }
            tvCurrentCycleVal.apply {
                visibility = if(lastCycleInvalid) View.VISIBLE else View.GONE
                text = "$calculatedCycle $measureSufix"
            }
        }
        //
        builder.apply {
            setTitle(hmAuxTrans["erro_dialog_ttl"])
            setView(dialogBinding.root)
            setPositiveButton(
                hmAuxTrans["sys_alert_btn_ok"],
                null
            )
        }.setOnDismissListener {
            calculatedExecCycle = 0
            calculatedExecMeasureValue = -1f
        }.create()
        .show()
    }
}