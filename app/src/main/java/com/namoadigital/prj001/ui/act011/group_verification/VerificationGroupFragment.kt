package com.namoadigital.prj001.ui.act011.group_verification

import android.os.Bundle
import android.view.View
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.setViewTreeLifecycleOwner
import com.google.gson.Gson
import com.namoa_digital.namoa_library.compose.theme.NamoaApplicationTheme
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao
import com.namoadigital.prj001.databinding.Act011FrgIncludeHeaderBinding
import com.namoadigital.prj001.databinding.Act011FrgIncludeNavegationBinding
import com.namoadigital.prj001.databinding.FragmentVerificationGroupBinding
import com.namoadigital.prj001.model.Act011FormTab
import com.namoadigital.prj001.model.Act011FormTabStatus
import com.namoadigital.prj001.model.masterdata.ge_os.GeOs
import com.namoadigital.prj001.ui.act011.frags.Act011BaseFrg
import com.namoadigital.prj001.ui.act011.group_verification.ui.VerificationGroupScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerificationGroupFragment : Act011BaseFrg<FragmentVerificationGroupBinding>() {

    var onProgressDialog: () -> Boolean? = { null }
    var onErrorGetVerificationGroup: () -> Unit = { }

    override fun getViewBinding(): FragmentVerificationGroupBinding =
        FragmentVerificationGroupBinding.inflate(layoutInflater)

    override fun getHeaderInclude(): Act011FrgIncludeHeaderBinding = binding.incHeader

    override fun getNavegationInclude(): Act011FrgIncludeNavegationBinding = binding.incNavegation

    override fun getTabErrorCount(validHighlight: Boolean): Int = 0

    override fun getTabCount(): Int = 0

    override fun getTabObj(skipFieldValidation: Boolean, validHighLight: Boolean): Act011FormTab = Act011FormTab(
        page = this.tabIndex,
        name = mTabName,
        tracking = null,
        fieldCount = -1,
        problemReportedCount = null,
        forecastCount = null,
        criticalForecastCount = null,
        nonForecastCount = null,
        requiredByTicketCount = null,
        status = Act011FormTabStatus.OK
    )

    override fun getTabStatus(validHighLight: Boolean): Act011FormTabStatus = Act011FormTabStatus.OK

    override fun getTabName(): String =
        hmAuxTrans[TAB_VERIFICATION_GROUP_LBL] ?: TAB_VERIFICATION_GROUP_LBL

    override fun applyAutoAnswer(): Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tabIndex = it.getInt(GE_Custom_Form_Field_LocalDao.PAGE)
            tabLastIndex = it.getInt(PARAM_LAST_INDEX)
            formStatus = it.getString(GE_Custom_Form_DataDao.CUSTOM_FORM_STATUS, "")
            isFormOs = it.getBoolean(GE_Custom_Form_LocalDao.IS_SO, true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val jsonGeOs = arguments?.getString(ARG_GE_OS, "")
        val longProductCode = arguments?.getLong(ARG_PRODUCT_CODE, 0)
        val longSerialCode = arguments?.getLong(ARG_SERIAL_CODE, 0)
        val isContinousForm = arguments?.getBoolean(ARG_IS_CONTINOUS, false)

        initScreen(
            jsonGeOs = jsonGeOs,
            productCode = longProductCode ?: 0,
            serialCode = longSerialCode ?: 0,
            isContinuousForm = isContinousForm == true
        )
        super.onViewCreated(view, savedInstanceState)
    }


    private fun initScreen(
        jsonGeOs: String?,
        productCode: Long,
        serialCode: Long,
        isContinuousForm: Boolean
    ) {

        val gson = Gson()
        val modelGeOs = gson.fromJson(jsonGeOs, GeOs::class.java)

        binding.composeView.apply {
            this.setViewTreeLifecycleOwner(this@VerificationGroupFragment)
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                NamoaApplicationTheme {
                    VerificationGroupScreen(
                        geOs = modelGeOs,
                        productCode = productCode,
                        translateMap = hmAuxTrans,
                        serialCode = serialCode,
                        isReadOnly = readOnlyStatus(),
                        isContinuousForm = isContinuousForm,
                        onProgressDialog = onProgressDialog,
                        onErrorGetGroupList = {
                            onErrorGetVerificationGroup()
                        }
                    )
                }
            }
        }
    }

    companion object {
        private const val ARG_GE_OS = "arg_ge_os"
        private const val ARG_PRODUCT_CODE = "arg_product_code"
        private const val ARG_SERIAL_CODE = "arg_serial_code"
        private const val ARG_IS_CONTINOUS = "arg_is_continous"

        const val TAB_VERIFICATION_GROUP_LBL = "tab_verification_group_lbl"
        const val SECTION_SELECT_VERIFICATION_GROUP_LBL = "section_select_verification_group_lbl"
        const val SECTION_ITEM_WITHOUT_GROUP_LBL = "section_item_without_group_lbl"
        const val REQUIRED_BY_TICKET_LBL = "required_by_ticket_lbl"
        const val TOAST_SUCCESS_UPDATE_INSPECTION_LIST = "toast_success_update_inspection_list"
        const val TOAST_ERROR_UPDATE_INSPECTION_LIST = "toast_error_update_inspection_list"
        const val TITLE_ERROR_GET_LIST_VERIFICATION_GROUP =
            "title_error_get_list_verification_group"
        const val LABEL_ERROR_GET_LIST_VERIFICATION_GROUP =
            "label_error_get_list_verification_group"
        const val LOADING_LBL = "loading_lbl"
        const val LOADING_UPDATE_GROUP_LBL = "loading_update_group_lbl"
        const val LOADING_UPDATE_INSPECTION_LIST_LBL = "loading_update_inspection_list_lbl"
        const val ERROR_SAVE_SWITCH_LBL = "error_save_switch_lbl"
        const val GROUP_EXPIRED_VERIFICATION_GROUP_LBL = "group_expired_verification_group_lbl"
        const val GROUP_PREDICTED_DATE_VERIFICATION_GROUP_LBL =
            "group_predicted_date_verification_group_lbl"
        const val GROUP_IN_EXECUTION_VERIFICATION_GROUP_LBL =
            "group_in_execution_verification_group_lbl"
        const val ITEM_WITH_TICKET_VERIFICATION_GROUP_LBL =
            "item_with_ticket_verification_group_lbl"
        const val ITEM_USER_VERIFICATION_GROUP_LBL = "item_user_verification_group_lbl"

        fun loadTranslation(): List<String> {
            return listOf(
                TAB_VERIFICATION_GROUP_LBL,
                SECTION_SELECT_VERIFICATION_GROUP_LBL,
                SECTION_ITEM_WITHOUT_GROUP_LBL,
                REQUIRED_BY_TICKET_LBL,
                TOAST_SUCCESS_UPDATE_INSPECTION_LIST,
                TOAST_ERROR_UPDATE_INSPECTION_LIST,
                TITLE_ERROR_GET_LIST_VERIFICATION_GROUP,
                LABEL_ERROR_GET_LIST_VERIFICATION_GROUP,
                LOADING_LBL,
                LOADING_UPDATE_GROUP_LBL,
                LOADING_UPDATE_INSPECTION_LIST_LBL,
                ERROR_SAVE_SWITCH_LBL,
                GROUP_EXPIRED_VERIFICATION_GROUP_LBL,
                GROUP_PREDICTED_DATE_VERIFICATION_GROUP_LBL,
                GROUP_IN_EXECUTION_VERIFICATION_GROUP_LBL,
                ITEM_WITH_TICKET_VERIFICATION_GROUP_LBL,
                ITEM_USER_VERIFICATION_GROUP_LBL
            )

        }

        fun newInstance(
            hmAuxTrans: HMAux,
            tabIndex: Int = 0,
            tabCount: Int = 0,
            geOs: GeOs,
            formStatus: String = "",
            productCode: Long,
            serialCode: Long,
            isFormContinuous: Boolean
        ): VerificationGroupFragment {
            val fragment = VerificationGroupFragment().apply {
                this.hmAuxTrans = hmAuxTrans
                this.hmAuxTrans.filter { loadTranslation().contains(it.key) }
                this.isFormOs = true
                this.tabIndex = tabIndex
                this.tabLastIndex = tabCount
                this.formStatus = formStatus

                val gson = Gson()
                val jsonGeOs = gson.toJson(geOs)

                val args = Bundle().apply {
                    putString(ARG_GE_OS, jsonGeOs)
                    putLong(ARG_PRODUCT_CODE, productCode)
                    putLong(ARG_SERIAL_CODE, serialCode)
                    putBoolean(ARG_IS_CONTINOUS, isFormContinuous)
                    putInt(GE_Custom_Form_Field_LocalDao.PAGE, tabIndex)
                    putInt(PARAM_LAST_INDEX, tabCount)
                    putBoolean(GE_Custom_Form_LocalDao.IS_SO, isFormOs)
                    putString(GE_Custom_Form_DataDao.CUSTOM_FORM_STATUS, formStatus)
                }

                arguments = args
            }

            return fragment
        }
    }
}
