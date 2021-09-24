package com.namoadigital.prj001.ui.act087

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.view.isVisible
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.Base_Activity_Frag
import com.namoadigital.prj001.R
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao
import com.namoadigital.prj001.databinding.FormOsHeaderFrgBinding
import com.namoadigital.prj001.extensions.setAsRequired
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.ui.act011.frags.Act011BaseFrg
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.math.BigDecimal

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

        fun getFragTranslationsVars() : List<String>{
            return listOf(
                "order_type_lbl",
                "use_backup_lbl",
                "start_date_lbl",
                "backup_serial_hint",
                "backup_serial_help_lbl",
                "measure_current_value_hint",
                "measure_last_value_lbl",
                "btn_save"
            )
        }
    }

    override fun getViewBinding() = FormOsHeaderFrgBinding.inflate(layoutInflater)
    override fun getHeaderInclude() = binding.incHeader
    override fun getNavegationInclude() = binding.incNavegation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNavegationIncludeVisibility()
        setLabels()
        iniVars()
        iniActions()
    }

    private fun iniActions() {
        with(binding){
            swMachine.setOnCheckedChangeListener { _, _ ->
                updateBkpMachineVisibility()
            }
            //
            ivSwapMachine.setOnClickListener {
                mCreationListener?.callProductSelection()
            }
            //
            ivSerialSearch.setOnClickListener {
                mCreationListener?.searchSerialClick()
            }
        }
    }

    private fun updateBkpMachineVisibility() {
        with(binding){
            gpBkpMachine.visibility = if (swMachine.isChecked) View.VISIBLE else View.GONE
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
        addIndxToVisibleLabels()


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
            swMachine.isChecked = (formOsHeader.backup_product_code != null)
            updateBkpMachineVisibility()
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
                        lastMeasureDate,
                        ConstantBaseApp.DATE_TO_MILLISECOND_TYPE_IGNORE_SECOND
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
        mCreationListener?.createOsHeader(formOsHeader)
    }

    private fun addIndxToVisibleLabels() {
        var lblIdx = 1
        labelsView.forEach{ obj->
            if(obj.visibility == View.VISIBLE){
                obj.text = "$lblIdx. ${obj.text}"
                obj.setAsRequired()
                lblIdx++
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
        mCreationListener = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //
        if(requestCode == ConstantBaseApp.ACT_PRODUCT_SELECTION_REQUEST_CODE
            && resultCode == Base_Activity_Frag.RESULT_OK
        ){
        //TODO CONTINUAR DAQUI
        //mPresenter.processProductSelecionResult(data)
        }
    }
}