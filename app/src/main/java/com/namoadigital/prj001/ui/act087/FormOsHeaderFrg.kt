package com.namoadigital.prj001.ui.act087

import android.content.Context
import android.os.Bundle
import android.view.View
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao
import com.namoadigital.prj001.databinding.FormOsHeaderFrgBinding
import com.namoadigital.prj001.extensions.setAsRequired
import com.namoadigital.prj001.model.Act011FormTab
import com.namoadigital.prj001.model.Act011FormTabStatus
import com.namoadigital.prj001.model.GeOs
import com.namoadigital.prj001.model.TK_Ticket_Measure
import com.namoadigital.prj001.ui.act011.frags.Act011BaseFrg
import com.namoadigital.prj001.ui.act011.frags.Act011FrgFF
import com.namoadigital.prj001.util.Constant

class FormOsHeaderFrg : Act011BaseFrg<FormOsHeaderFrgBinding>() {

    private var isOsCreation: Boolean = false
    private lateinit var formOsHeader: GeOs
    private var mCreationListener: FormOsHeaderFrgCreationInteraction? = null

    companion object{
        @JvmStatic
        fun newInstance(
            hmAuxTrans: HMAux,
            tabIndex: Int = 0,
            tabLastIndex: Int = 0,
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
    }
    private fun setNavegationIncludeVisibility() {
        if (isOsCreation) {
            binding.incNavegation.root.visibility = View.GONE
        }
    }

    private fun setLabels() {
        with(binding){
            tvOsTypeLbl.text = hmAuxTrans["order_type_lbl"]
            tvOsTypeLbl.setAsRequired()
            tvOsMachineLbl.text = hmAuxTrans["use_backup_lbl"]
            tvOsMachineLbl.setAsRequired()
            tvOsStartDateLbl.text = hmAuxTrans["start_date_lbl"]
            tvOsStartDateLbl.setAsRequired()
            mketMachineSerialEdit.hint = hmAuxTrans["backup_serial_hint"]
            tilMketSerial.helperText = hmAuxTrans["backup_serial_help_lbl"]
            mketOsMainMeasureVal.hint = hmAuxTrans["measure_current_value_hint"]
            tvOsLastMeasureLbl.text = hmAuxTrans["measure_last_value_lbl"]
            tvOsTypeLbl.setAsRequired(false)
        }
    }

    private fun iniVars() {

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

}