package com.namoadigital.prj001.ui.act083

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.ctls.SearchableSpinner
import com.namoa_digital.namoa_library.ctls.SearchableSpinner.OnItemSelectedListener
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.R
import com.namoadigital.prj001.adapter.SelectDestinationAdapter.Companion.BTN_SELECT_DESTINATION
import com.namoadigital.prj001.adapter.act083.MyActionsAdapter
import com.namoadigital.prj001.core.data.domain.usecase.serial.site.inventory.SerialSiteInventoryUseCase.Companion.SiteInventoryUseCaseFactory
import com.namoadigital.prj001.core.trip.domain.usecase.CheckStatusForReadOnlyUseCase
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao
import com.namoadigital.prj001.dao.MD_ProductDao
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao
import com.namoadigital.prj001.dao.MD_SiteDao
import com.namoadigital.prj001.dao.MdJustifyItemDao
import com.namoadigital.prj001.dao.MdJustifyItemDao.Companion.RESCHEDULE
import com.namoadigital.prj001.dao.Sync_ChecklistDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao
import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.databinding.Act083MainBinding
import com.namoadigital.prj001.databinding.TicketNotExecutedDialogBinding
import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.model.MyActionsBase
import com.namoadigital.prj001.model.MyActionsFormButton
import com.namoadigital.prj001.model.SerialSiteInventory
import com.namoadigital.prj001.service.WS_Product_Serial_Structure
import com.namoadigital.prj001.service.WS_Serial_Search
import com.namoadigital.prj001.service.WS_Sync
import com.namoadigital.prj001.service.WS_TK_Ticket_Download
import com.namoadigital.prj001.service.WsScheduleNotExecuted
import com.namoadigital.prj001.service.WsSerialSiteInventory
import com.namoadigital.prj001.service.trip.WsSelectDestination
import com.namoadigital.prj001.ui.act005.Act005_Main
import com.namoadigital.prj001.ui.act006.Act006_Main
import com.namoadigital.prj001.ui.act009.Act009_Main
import com.namoadigital.prj001.ui.act011.Act011_Main
import com.namoadigital.prj001.ui.act016.Act016_Main
import com.namoadigital.prj001.ui.act020.Act020_Main
import com.namoadigital.prj001.ui.act033.Act033_Main
import com.namoadigital.prj001.ui.act038.Act038_Main
import com.namoadigital.prj001.ui.act068.Act068_Main
import com.namoadigital.prj001.ui.act070.Act070_Main
import com.namoadigital.prj001.ui.act071.Act071_Main
import com.namoadigital.prj001.ui.act083.data.local.preferences.MyActionsFilterParamPreferences
import com.namoadigital.prj001.ui.act083.model.TypeSerial
import com.namoadigital.prj001.ui.act092.ui.Act092_Main
import com.namoadigital.prj001.ui.act092.usecases.ActionPreferenceUseCases
import com.namoadigital.prj001.ui.act092.usecases.FlowScheduleFromMyActionUseCase.Companion.SITE_RESTRICTION_NO_ACCESS
import com.namoadigital.prj001.ui.act092.usecases.FlowTicketAccessUseCase
import com.namoadigital.prj001.ui.act092.usecases.FlowTicketAccessUseCase.Companion
import com.namoadigital.prj001.ui.act092.usecases.FlowTicketAccessUseCase.FlowTicketAccessError
import com.namoadigital.prj001.ui.act092.utils.Act092Translate
import com.namoadigital.prj001.ui.act092.utils.Act092UiEvent
import com.namoadigital.prj001.ui.act092.utils.Act092UiEvent.OpenDialog.DialogType
import com.namoadigital.prj001.ui.act093.ui.Act093_Main
import com.namoadigital.prj001.ui.act094.ui.Act094_Main
import com.namoadigital.prj001.ui.act094.util.Act094Translate
import com.namoadigital.prj001.ui.act094.util.Act094Translate.ALERT_DESTINATION_SELECTED_MSG
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Act083_Main : Base_Activity(), Act083_Main_Contract.I_View {

    @Inject
    lateinit var tripCheckStatusReadOnly: CheckStatusForReadOnlyUseCase

    companion object {
        const val MODULE_CHECKLIST_FORM_IN_PROCESSING = "checklist_form_in_processing"
        const val MODULE_CHECKLIST_START_FORM = "checklist_start_form"
        const val MODULE_SCHEDULE_DATE_REF = "module_schedule_date_ref"
        const val MODULE_SCHEDULE_FORM_DATA_CREATION_ERROR =
            "module_schedule_form_data_creation_error"
        const val EMPTY_SERIAL_SEARCH = "empty_serial_search"
        const val SERIAL_CREATION_DENIED = "serial_creation_denied"
        const val MODULE_TICKET_EXEC_CONFIRM = "module_ticket_exec_confirm"
        const val MODULE_SCHEDULE_TICKET_CREATION_ERROR = "module_schedule_ticket_creation_error"
        const val MODULE_SCHEDULE_STATUS_PREVENTS_TO_OPEN = "module_schedule_status_prevent_to_open"
        const val PROFILE_PRJ001_AP_NOT_FOUND = "profile_prj001_ap_not_found"
        const val PROFILE_MENU_TICKET_NOT_FOUND = "profile_menu_ticket_not_found"
        const val FREE_EXECUTION_BLOCKED = "free_execution_blocked"
        const val WS_SCHEDULE_NOT_EXECUTED = "WS_SCHEDULE_NOT_EXECUTED"
        const val SITE_RESTRICTION_NO_ACCESS = "SITE_RESTRICTION_NO_ACCESS"
    }

    private var hasConnectionFail: Boolean = false
    private var serialActionSelected: Int = -1
    private lateinit var binding: Act083MainBinding
    private lateinit var mAdapter: MyActionsAdapter
    private lateinit var bundle: Bundle
    private lateinit var mainRequesting: String
    private var wsProcess = ""
    private var hmAuxTicketDownload: HMAux = HMAux()
    private val CHANGE_ZONE_RESULT_CODE = 10
    private var firstScroll = true
    private var applyMainUserFilter = false
    var typeSerial: TypeSerial? = null

    //
    private var isReadOnly = false

    private val mPresenter by lazy {
        Act083_Main_Presenter(
            context, this, bundle, TK_TicketDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            ), TkTicketCacheDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            ), MD_Schedule_ExecDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            ), GE_Custom_Form_ApDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            ), GE_Custom_Form_LocalDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            ), MD_SiteDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            ), TK_Ticket_CtrlDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            ), MD_Product_SerialDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            ), MD_ProductDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            ), Sync_ChecklistDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            ), MdJustifyItemDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            ), MyActionsFilterParamPreferences(
                getSharedPreferences("act083_filter", MODE_PRIVATE)
            ),
            hmAux_Trans,
            SiteInventoryUseCaseFactory(context).getAndcheckAndExecUseCase(),
            ActionPreferenceUseCases.ActionUseCasesPreferenceFactory(context).build(),
            FlowTicketAccessUseCase.Companion.Factory(context).build()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Act083MainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //
        initBundle(savedInstanceState)
        iniSetup()
        iniTrans()
        initVars()
        iniUIFooter()
        initActions()
//        checkSerialSiteInventory()
    }

    private fun initBundle(savedInstanceState: Bundle?) {
        bundle = (savedInstanceState ?: intent.extras) as Bundle

    }

    private fun checkSerialSiteInventory() {
        mPresenter.checkSerialSiteInv(getCurrentTab())
    }


    private var serialSiteSizeInt = 0
    override fun visibleTabSerialSiteInventory(
        serialSiteSize: String,
        showSize: Boolean
    ) {
//        if (autoClick) binding.act083MainContent.act083TabSerial.performClick()
        with(binding.act083MainContent) {
            act083TabSerial.visibility = View.VISIBLE
            serialSiteSizeInt = serialSiteSize.toInt()
            act083TabSerial.text = hmAux_Trans["tab_serial_site_lbl"].plus(
                if (showSize) " ($serialSiteSize)"
                else ""
            )
        }
    }

    private fun iniTrans() {
        hmAux_Trans = loadTranslation()
    }

    private fun iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
            context, mModule_Code, ConstantBaseApp.ACT083
        )
        //06/06/2021 - Add recolhimento do teclado
        window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )
    }

    private fun initVars() {
        setLabels()
        val originFlow = bundle.getString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW, "")
        isReadOnly = tripCheckStatusReadOnly(originFlow)
        supportActionBar?.title = mPresenter.getActTitle()
        binding.act083MainContent.act083TilFilter.apply {
            hint = hmAux_Trans[Act092Translate.HINT_FILTER]
        }
        //reseta preferencia do toggle da origem
        ToolBox_Con.setBooleanPreference(
            context, ConstantBaseApp.PREFERENCE_PIPELINE_HEADER_FORM_INFO_TOGGLE, true
        )
        //LUCHE - 21/06/2021
        //Desabilita os cliques nas abas, pois só serão habilitado após corroutine retornar.
//        toggleTabEnableStattus(false)
//        setLabels()
//        setChips()

        if (isReadOnly && originFlow == ConstantBaseApp.ACT094) {
            binding.act083MainContent.llFooter.apply {
                footerMain.visibility = View.VISIBLE
                btnFilledRightAction.apply {
                    text = hmAux_Trans[BTN_SELECT_DESTINATION]
                    icon = ResourcesCompat.getDrawable(resources, R.drawable.baseline_add_location_alt_24, null)
                    iconTint = ResourcesCompat.getColorStateList(resources, R.color.m3_namoa_surface, null)
                    visibility = View.VISIBLE
                    setOnClickListener {
                        mPresenter.selectDestination()
                    }
                }
            }
            binding.act083MainContent.include.visibility = View.GONE
        }else{
            binding.act083MainContent.include.visibility = View.VISIBLE
            binding.act083MainContent.llFooter.footerMain.visibility = View.GONE
        }
    }

//    private fun setChips() {
//        mPresenter.getChipList().forEach {
//            binding.act083MainContent.act083CgFilter.addView(createTvChip(it))
//        }
//    }

    /**
     * LUCHE - 11/06/2021
     * Fun que altera os labes das abas, adicionando o contador.
     */
    override fun setTabsCounters(selectedTabCounter: Int, otherTabCounter: Int) {
        with(binding.act083MainContent) {
            when (act083Tabs.checkedRadioButtonId) {
                act083TabMyActions.id -> {
                    act083TabMyActions.text =
                        hmAux_Trans["tab_my_actions_lbl"].plus(" ($selectedTabCounter)")
                    act083TabOtherActions.text =
                        hmAux_Trans["tab_other_actions_lbl"].plus(" ($otherTabCounter)")

                    if (act083TabSerial.isVisible) {
                        act083TabSerial.text =
                            hmAux_Trans["tab_serial_site_lbl"].plus(" ($serialSiteSizeInt)")
                    }
                }


                act083TabSerial.id -> {
                    act083TabMyActions.text =
                        hmAux_Trans["tab_my_actions_lbl"].plus(" ($selectedTabCounter)")
                    act083TabOtherActions.text =
                        hmAux_Trans["tab_other_actions_lbl"].plus(" ($otherTabCounter)")
                    act083TabSerial.text =
                        hmAux_Trans["tab_serial_site_lbl"].plus(" ($serialSiteSizeInt)")
                }


                else -> {
                    act083TabMyActions.text =
                        hmAux_Trans["tab_my_actions_lbl"].plus(" ($otherTabCounter)")
                    act083TabOtherActions.text =
                        hmAux_Trans["tab_other_actions_lbl"].plus(" ($selectedTabCounter)")
                    if (act083TabSerial.isVisible) {
                        act083TabSerial.text =
                            hmAux_Trans["tab_serial_site_lbl"].plus(" ($serialSiteSizeInt)")
                    }
                }
            }
        }
    }

    override fun iniRecycler(myActionsList: MutableList<MyActionsBase>) {

        if (myActionsList.size > 0) {
            binding.act083MainContent.act083TvNoResult.visibility = View.GONE
            //
            mAdapter = MyActionsAdapter(
                myActionsList,
                hmAux_Trans,
                supportActionBar?.title?.toString(),
                isReadOnly,
                this::onMyActionClick,
                null,
                this::onSerialButtonClick,
                this::onAdapterFilterApplied,
                this::createNotExecuteDialog,
                this::onSerialButtonFromSerialSite

            )
            //
            with(binding.act083MainContent.act083RvActionsList) {
                layoutManager = LinearLayoutManager(context)
                adapter = mAdapter
            }
            //

            mAdapter.userMainFilterOn = applyMainUserFilter

            if (!binding.act083MainContent.act083MketFilter.text.isNullOrEmpty()) {
                applyTextFilter(binding.act083MainContent.act083MketFilter.text.toString())
            } else {
                applyTextFilter("")
            }
            changeProgressBarVisility(false)
        } else {
            changeProgressBarVisility(false)
            with(binding.act083MainContent) {
                if ((getCurrentTab() == 2) &&
                    (!ToolBox_Con.isOnline(context)
                            || hasConnectionFail
                            || ToolBox_Con.getBooleanPreferencesByKey(
                        context,
                        ConstantBaseApp.PREFERENCE_SERIAL_OFFLINE_FLOW,
                        false
                    )
                            )
                ) {
                    act083TabSerial.visibility = View.VISIBLE
                    act083TvNoResult.text = hmAux_Trans["no_connection_try_again_lbl"]
                    if (ToolBox_Con.getBooleanPreferencesByKey(
                            context,
                            ConstantBaseApp.PREFERENCE_SERIAL_OFFLINE_FLOW,
                            false
                        )
                    ) {
                        act083TvNoResult.text = hmAux_Trans["offline_mode_on_sync_required_lbl"]
                    }
                } else {
                    if (applyMainUserFilter) {
                        act083TvNoResult.text = hmAux_Trans["no_record_for_filter_lbl"]
                    } else {
                        act083TvNoResult.text = hmAux_Trans["no_record_lbl"]
                    }
                }
                act083TvNoResult.visibility = View.VISIBLE
                act083RvActionsList.visibility = View.GONE
            }
        }
    }

    private fun createNotExecuteDialog(myAction: MyActions) {
        val dm = context.resources.displayMetrics
        val dmW = dm.widthPixels.toFloat() * 0.95f
        //        float dmH = (float) dm.heightPixels * 0.95f;
        with(TicketNotExecutedDialogBinding.inflate(layoutInflater)) {
            Dialog(context).let { dialog ->

                dialog.setContentView(root)
                dialog.window!!.setLayout(dmW.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
                dialog.setCancelable(false)

                act070IvJustifyPhotoBtn.visibility = View.GONE

                setLabel(myAction)
                setActions(myAction,
                    closeDialog = {
                        dialog.hide()
                    }
                )

                dialog.show()
            }
        }
    }

    private fun validateNotExecuteFormEntry(binding: TicketNotExecutedDialogBinding): String {
        var errorMsg: String? = ""
        val hmAux = binding.act070NotExecuteDialogJustifyOptionSs.getmValue()
        if (hmAux.hasConsistentValue(MdJustifyItemDao.REQUIRED_COMMENT) && "1" == hmAux[MdJustifyItemDao.REQUIRED_COMMENT]) {
            if (binding.act070NotExecuteDialogJustifyCommentsActv.text == null || binding.act070NotExecuteDialogJustifyCommentsActv.text.toString()
                    .isEmpty()
            ) {
                errorMsg = hmAux_Trans["alert_not_execute_justify_comment_required_msg"]
            }
        }
        if (binding.act070NotExecuteDialogJustifyOptionSs.visibility == View.VISIBLE && !hmAux.hasConsistentValue(
                SearchableSpinner.CODE
            )
        ) {
            errorMsg += hmAux_Trans["alert_not_execute_justify_option_required_msg"]
        }
        return errorMsg ?: ""
    }


    private fun TicketNotExecutedDialogBinding.setActions(
        item: MyActions,
        closeDialog: () -> Unit,
    ) {

        var dateReschedule = ""

        act070NotExecuteDialogJustifyBtnCancel.setOnClickListener {
            val hasJustify = (act070NotExecuteDialogJustifyOptionSs.visibility == View.VISIBLE
                    && act070NotExecuteDialogJustifyOptionSs.getmValue()
                .hasConsistentValue(SearchableSpinner.CODE))
            val hasComments: Boolean =
                act070NotExecuteDialogJustifyCommentsActv.text.toString().isNotEmpty()
            val dateKey = (act070NotExecuteDialogJustifyDate.tvDateVal.visibility == View.VISIBLE
                    && act070NotExecuteDialogJustifyDate.tvDateVal.mketContents.hasConsistentValue("DATE_KEY")
                    && !act070NotExecuteDialogJustifyDate.tvDateVal.mketContents["DATE_KEY"].isNullOrEmpty())
            val hourKey = (act070NotExecuteDialogJustifyDate.tvDateVal.visibility == View.VISIBLE
                    && act070NotExecuteDialogJustifyDate.tvDateVal.mketContents.hasConsistentValue("HOUR_KEY")
                    && !act070NotExecuteDialogJustifyDate.tvDateVal.mketContents["HOUR_KEY"].isNullOrEmpty())
            if (hasJustify || hasComments || dateKey || hourKey) {
                showAlert(
                    hmAux_Trans["alert_not_execute_justify_lost_data_ttl"],
                    hmAux_Trans["alert_not_execute_justify_lost_data_msg"],
                    { _, _ -> closeDialog() },
                    1
                )
            } else {
                closeDialog()
            }
        }

        act070NotExecuteDialogJustifyBtnSave.setOnClickListener {

            with(act070NotExecuteDialogJustifyDate) {

                fun dateInvalid(msg: String) {
                    Toast.makeText(
                        this@Act083_Main,
                        hmAux_Trans[msg],
                        Toast.LENGTH_SHORT
                    ).show()
                }

                if (root.isVisible) {

                    val dateKey =
                        tvDateVal.mketContents.hasConsistentValue("DATE_KEY") && !tvDateVal.mketContents["DATE_KEY"].isNullOrEmpty()
                    val hourKey =
                        tvDateVal.mketContents.hasConsistentValue("HOUR_KEY") && !tvDateVal.mketContents["HOUR_KEY"].isNullOrEmpty()

                    if (dateKey || hourKey) {

                        if (tvDateVal.isValid) {
                            val isFuture = ToolBox_Inf.isFutureDate(tvDateVal.getmValue())

                            if (!isFuture) {
                                dateInvalid("warning_not_execute_justify_future_date_hour")
                                return@setOnClickListener
                            }

                            if (!tvDateVal.getmValue().isNullOrEmpty()) {
                                dateReschedule = tvDateVal.getmValue()
                            }
                        } else {
                            dateInvalid("warning_not_execute_justify_future_date_hour")
                            return@setOnClickListener
                        }

                    }
                }
            }

            val errorMsg = validateNotExecuteFormEntry(this)
            val justifyOption = act070NotExecuteDialogJustifyOptionSs.getmValue()
            errorMsg.ifEmpty {
                closeDialog()
                mPresenter.justifyNotExecuteSchedule(
                    processPk = item.processPk,
                    comments = act070NotExecuteDialogJustifyCommentsActv.text.toString(),
                    justify_group_code = item.hasNotExecuted!!,
                    justify_item_code = justifyOption[SearchableSpinner.CODE]?.toInt() ?: -1,
                    reschedule_date = dateReschedule
                )
                return@setOnClickListener
            }
            Toast.makeText(
                this@Act083_Main,
                errorMsg,
                Toast.LENGTH_SHORT
            ).show()
        }


    }


    private fun TicketNotExecutedDialogBinding.setLabel(
        item: MyActions
    ) {
        act070NotExecuteDialogTtl.text = hmAux_Trans["alert_not_execute_ttl"]
        act070NotExecuteDialogMsg.text = hmAux_Trans["alert_not_execute_msg"]
        //

        val justifyItems = mPresenter.getJustifyItems(item.hasNotExecuted!!)


        act070NotExecuteDialogJustifyOptionSs.apply {

            if (justifyItems.isEmpty()) {
                visibility = View.GONE
                return@apply
            }

            setmRequired(true)
            setmShowLabel(true)
            setmCanClean(true)
            setmOption(justifyItems)
            setmLabel(hmAux_Trans["alert_not_execute_justify_option_lbl"])


            setOnItemSelectedListener(object : OnItemSelectedListener {
                override fun onItemPreSelected(p0: HMAux?) {

                    act070NotExecuteDialogJustifyCommentsTil.clearFocus()

                }

                override fun onItemPostSelected(hmAux: HMAux?) {

                    val requiredReschedule = hmAux?.get(RESCHEDULE)?.toInt() == 1
                    val hmAux = act070NotExecuteDialogJustifyOptionSs.getmValue()
                    val states = arrayOf(
                        intArrayOf(android.R.attr.state_enabled), // enabled
                        intArrayOf(android.R.attr.state_pressed), // pressed
                        intArrayOf(android.R.attr.state_focused)  // focused
                    )


                    val colorsRequired = intArrayOf(
                        resources.getColor(com.namoa_digital.namoa_library.R.color.customff_required_on_color),
                        resources.getColor(com.namoa_digital.namoa_library.R.color.customff_required_on_color),
                        resources.getColor(com.namoa_digital.namoa_library.R.color.customff_required_on_color),
                    )

                    val colorsDefault = intArrayOf(
                        resources.getColor(com.namoa_digital.namoa_library.R.color.m3_namoa_outline),
                        resources.getColor(com.namoa_digital.namoa_library.R.color.m3_namoa_primary),
                        resources.getColor(com.namoa_digital.namoa_library.R.color.m3_namoa_primary),
                    )


                    val colorRequiredState = ColorStateList(states, colorsRequired)
                    val colorDefaultState = ColorStateList(states, colorsDefault)


                    if (hmAux.hasConsistentValue(MdJustifyItemDao.REQUIRED_COMMENT) && "1" == hmAux[MdJustifyItemDao.REQUIRED_COMMENT]) {
                        act070NotExecuteDialogJustifyCommentsTil.hintTextColor = colorRequiredState
                        act070NotExecuteDialogJustifyCommentsTil.setBoxStrokeColorStateList(
                            colorRequiredState
                        )
                    } else {
                        act070NotExecuteDialogJustifyCommentsTil.hintTextColor = colorDefaultState
                        act070NotExecuteDialogJustifyCommentsTil.setBoxStrokeColorStateList(
                            colorDefaultState
                        )
                    }

                    if (!requiredReschedule) {
                        act070NotExecuteJustifyDateTtl.visibility = View.GONE
                        act070NotExecuteDialogJustifyDate.root.visibility = View.GONE
                        act070NotExecuteDialogJustifyDate.tvDateVal.setmValue("")
                        return
                    }

                    act070NotExecuteDialogJustifyDate.root.visibility = View.VISIBLE
                    act070NotExecuteDialogJustifyDate.tvDateVal.setmHighlightWhenInvalid(true)

                    //
                    act070NotExecuteJustifyDateTtl.visibility = View.VISIBLE
                    act070NotExecuteJustifyDateTtl.text =
                        hmAux_Trans["alert_not_execute_justify_date_ttl"]
                }

            })
        }

        //
        act070NotExecuteDialogJustifyDate.tvDateVal.setmLabel("")
        act070NotExecuteDialogJustifyDate.chkShiftStep.visibility = View.GONE
        act070NotExecuteDialogJustifyDate.chkShiftTicketDate.visibility = View.GONE
        act070NotExecuteDialogJustifyDate.guideline6.visibility = View.GONE
        act070NotExecuteDialogJustifyDate.tvDateLbl.visibility = View.GONE
        act070NotExecuteDialogJustifyDate.tvTimeLbl.visibility = View.GONE

        act070NotExecuteDialogJustifyCommentsTil.hint =
            hmAux_Trans["alert_not_execute_justify_comment_lbl"]
        act070NotExecuteDialogJustifyBtnCancel.text = hmAux_Trans["sys_alert_btn_cancel"]
        act070NotExecuteDialogJustifyBtnSave.text =
            hmAux_Trans["alert_not_execute_save_btn"]
    }


    private fun onSerialButtonClick(myAction: MyActions, position: Int) {
        serialActionSelected = position
        typeSerial = TypeSerial.MY_ACTIONS
        mPresenter.processSerialClick(
            serialId = myAction.serialId ?: "",
            productCode = myAction.productCode,
            productId = myAction.productId ?: "",
            myAction = myAction
        )
    }

    private fun onSerialButtonFromSerialSite(clickType: SerialSiteInventory.Companion.OnClickType) {
        when (clickType) {

            is SerialSiteInventory.Companion.OnClickType.OnSerialClick -> {
                typeSerial = TypeSerial.MORE_ACTIONS(clickType.model)
                serialActionSelected = clickType.position
                mPresenter.processSerialClick(
                    serialId = clickType.model.serialId,
                    productCode = clickType.model.productCode,
                    productId = "",
                    myAction = null,
                    typeSerial = typeSerial
                )
            }

            is SerialSiteInventory.Companion.OnClickType.OnStatusClick -> {
                typeSerial = TypeSerial.INFO_SERIAL(clickType.model)
                serialActionSelected = clickType.position
                mPresenter.processSerialClick(
                    serialId = clickType.model.serialId,
                    productCode = clickType.model.productCode,
                    productId = "",
                    myAction = null,
                    typeSerial = typeSerial
                )
            }


            else -> {}
        }


    }

    private fun onFormButtonClick(myActionsFormButton: MyActionsFormButton) {
        mPresenter.processActionFormButtonClick(myActionsFormButton)
    }

    private fun onMyActionClick(myAction: MyActions) {
        typeSerial = TypeSerial.MY_ACTIONS
        mPresenter.processActionClick(myAction)
    }

    /**
     * Fun acionada pelo adapter como callback após finalizar a filtragem
     */
    private fun onAdapterFilterApplied(qtyItensFiltered: Int) {
        if (qtyItensFiltered > 0) {
            scrollToLastSelectedItem()
        }
        setPlaceholderTextAndVisibility(mPresenter.myActionsList.size)
    }

    /**
     * Fun que faz o scroll para o item navegado anterormente, caso exista
     * Só executa o scroll uma vez ao carregara tela.
     * Tenta o scroll com offset, como envolve um cast, no catch faz o scroll padrao
     */
    private fun scrollToLastSelectedItem() {
        if (firstScroll) {
            firstScroll = false
            val actionPkPosition = mAdapter.getActionPkPosition(
                mPresenter.lastSelectedActionType,
                mPresenter.lastSelectedActionPk
            )
            if (actionPkPosition >= 0) {
                //Tenta fazer scroll com offset, se crashar, tenta scroll sem offset
                try {
                    val linearLayoutManager =
                        binding.act083MainContent.act083RvActionsList.layoutManager as LinearLayoutManager
                    val offset = ToolBox.dbToPixel(context, 50)
                    linearLayoutManager.scrollToPositionWithOffset(actionPkPosition, offset)
                } catch (e: Exception) {
                    binding.act083MainContent.act083RvActionsList.scrollToPosition(
                        actionPkPosition
                    )
                }
            }
        }
    }

    override fun showPD(ttl: String?, msg: String?) {
        enableProgressDialog(
            ttl, msg, hmAux_Trans["sys_alert_btn_cancel"], hmAux_Trans["sys_alert_btn_ok"]
        )
    }

    override fun addControlToActivity(mketSerial: MKEditTextNM) {
        controls_sta.add(mketSerial)
    }

    override fun removeControlFromActivity(mketSerial: MKEditTextNM) {
        controls_sta.remove(mketSerial)
    }

    private fun showAlert(
        ttl: String?,
        msg: String?,
        clickListner: DialogInterface.OnClickListener? = null,
        negativeBtn: Int = 0
    ) {
        ToolBox.alertMSG(
            context, ttl, msg, clickListner, negativeBtn
        )
    }

    override fun setProcess(wsProcess: String) {
        this.wsProcess = wsProcess
    }

    /**
     * Fun que retorna a qual aba esta sendo usada no momento que o item e selecionado
     */
    override fun getCurrentTab(): Int {
        with(binding.act083MainContent) {
            return when (act083Tabs.checkedRadioButtonId) {
                act083TabMyActions.id -> 1
                act083TabSerial.id -> 2
                else -> 0
            }
        }
    }

    /**
     * Fun que retorna o filtro digitado no mket
     * Null se vazio
     */
    override fun getMketFilter(): String? {
        val textFilter = binding.act083MainContent.act083MketFilter.text.toString()
        //
        return if (textFilter.isBlank() || textFilter.isEmpty()) {
            null
        } else {
            textFilter
        }
    }

    override fun getMainUserFilter(): Boolean {
        return applyMainUserFilter
    }

    /**
     * Fun que seta os params de filtro texto e aba recuperados do bundle
     */
    override fun setViewFiltersParam(
        textFilter: String?, initialTabToLoad: Int, mainUserFilterState: Boolean
    ) {
        binding.act083MainContent.act083MketFilter.setText(textFilter)
        when (initialTabToLoad) {
            0 -> {
                binding.act083MainContent.act083TabOtherActions.isChecked = true
                otherActionTabSelection()
            }

            1 -> {
                binding.act083MainContent.act083TabMyActions.isChecked = true
                myActionTabSelection()
            }

            2 -> {
                binding.act083MainContent.act083TabSerial.isChecked = true
                serialTabSelection()
            }

            else -> {
                binding.act083MainContent.act083TabOtherActions.isChecked = true
                otherActionTabSelection()
            }
        }
        applyMainUserFilter = mainUserFilterState
        setIvMainUserSelection()
    }

    override fun processCloseACT(mLink: String?, mRequired: String?) {
        super.processCloseACT(mLink, mRequired)
        processCloseACT(mLink, mRequired, HMAux())
    }


    override fun processCloseACT(mLink: String?, mRequired: String?, hmAux: HMAux) {
        super.processCloseACT(mLink, mRequired, hmAux)
        when (wsProcess) {

            WsSerialSiteInventory::class.java.name -> {
                wsProcess = ""
                progressDialog.dismiss()
                hasConnectionFail = false
                serialSiteSizeInt = mLink!!.toInt()
                mPresenter.getSerialSiteInventoryList(getCurrentTab())
            }

            WsScheduleNotExecuted::class.java.name -> {
                wsProcess = ""

                progressDialog.dismiss()

                showAlert(
                    ttl = hmAux_Trans["alert_not_execute_justify_success_ttl"],
                    msg = hmAux_Trans["alert_not_execute_justify_success_msg"],
                    clickListner = { dialog, _ ->
                        dialog.dismiss()
                        updateMyActionList(userFocusFilter)
                        mPresenter.updateMyActionList(userFocusFilter)
                    }
                )

            }

            WS_TK_Ticket_Download::class.java.name -> {
                wsProcess = ""
                if (mPresenter.verifyProductOutdateForForm(hmAux)) {
                    progressDialog.dismiss()
                    //
                    if (ToolBox_Con.isOnline(context)) {
                        hmAuxTicketDownload = hmAux
                        //
                        mPresenter.prepareWsFormSync()
                    } else {
                        //
                        callAct070(
                            mPresenter.getCacheTicketBundle(hmAux)
                        )
                    }
                } else {
                    progressDialog.dismiss()
                    callAct070(
                        mPresenter.getCacheTicketBundle(hmAux)
                    )
                }
            }

            WS_Sync::class.java.name -> {
                wsProcess = ""
                progressDialog.dismiss()
                //
                mPresenter.processWsSyncReturn(hmAuxTicketDownload)
            }

            WS_Serial_Search::class.java.name -> {
                wsProcess = ""
                progressDialog.dismiss()
                handleSerialSearchReturn(mLink)

            }

            WS_Product_Serial_Structure::class.java.name -> {
                wsProcess = ""
                progressDialog.dismiss()
                //
                val gson = GsonBuilder().serializeNulls().create()
                val serial = gson.fromJson(
                    mLink, MD_Product_Serial::class.java
                )
                //
                handleStructureReturn(serial)
            }

            WsSelectDestination.NAME -> {
                wsProcess = ""
                progressDialog.dismiss()
                Toast.makeText(context, hmAux_Trans[Act094Translate.ALERT_DESTINATION_SELECTED_MSG] ?: "", Toast.LENGTH_SHORT).show()
                mPresenter.selectionDestinationAvailable?.let {
                    mPresenter.saveDestination(context, mLink, it)
                }
            }

            else -> progressDialog?.dismiss()
        }
    }

    private fun handleStructureReturn(serial: MD_Product_Serial) {
        when (typeSerial) {
            is TypeSerial.MY_ACTIONS -> {
                val myAction = mAdapter.getMyActionByPosition(serialActionSelected)
                mPresenter.extractStructureResult(
                    serial,
                    myAction?.actionType,
                    myAction?.processPk,
                    typeSerial as TypeSerial.MY_ACTIONS
                )
            }

            is TypeSerial.MORE_ACTIONS -> {
                mPresenter.extractStructureResult(
                    serial,
                    TypeSerial.SERIAL_SITE_ACTION_BASE,
                    typeSerial = typeSerial as TypeSerial.MORE_ACTIONS,
                    processPk = "${serial.product_code}.${serial.serial_code}"
                )
            }

            is TypeSerial.INFO_SERIAL -> {
                mPresenter.extractStructureResult(
                    serial,
                    TypeSerial.SERIAL_SITE_ACTION_BASE,
                    typeSerial = typeSerial as TypeSerial.INFO_SERIAL,
                    processPk = "${serial.product_code}.${serial.serial_code}"
                )

            }

            else -> {
                ToolBox_Inf.registerException(
                    javaClass.name,
                    Exception("$typeSerial not found")
                )
            }
        }
        resetActionPosition()
    }

    private fun handleSerialSearchReturn(mLink: String?) {
        when (typeSerial) {

            is TypeSerial.MORE_ACTIONS -> {
                val serialSite = mAdapter.getMySerialSiteInvByPosition(serialActionSelected)
                mPresenter.extractSearchResult(
                    mLink,
                    serialSite?.productCode,
                    serialSite?.serialId,
                    TypeSerial.SERIAL_SITE_ACTION_BASE,
                    processPk = "${serialSite?.productCode}.${serialSite?.serialCode}"
                )
            }

            is TypeSerial.INFO_SERIAL -> {
                val serialSite = mAdapter.getMySerialSiteInvByPosition(serialActionSelected)
                mPresenter.extractSearchResult(
                    mLink,
                    serialSite?.productCode,
                    serialSite?.serialId,
                    TypeSerial.SERIAL_SITE_ACTION_BASE,
                    processPk = "${serialSite?.productCode}.${serialSite?.serialCode}"
                )
            }

            else -> {
                val myAction = mAdapter.getMyActionByPosition(serialActionSelected)
                mPresenter.extractSearchResult(
                    mLink,
                    myAction?.productCode,
                    myAction?.serialId,
                    myAction?.actionType,
                    myAction?.processPk
                )
            }
        }
    }

    private fun createTvChip(chipLabel: String): TextView {
        val tvChip = TextView(ContextThemeWrapper(context, R.style.TextViewChips))
        tvChip.apply {
            text = chipLabel
        }
        //
        return tvChip
    }

    private fun setLabels() {
        with(binding.act083MainContent) {
//            act083MketFilter.hint = hmAux_Trans["filter_hint"]
            act083TabMyActions.text = hmAux_Trans["tab_my_actions_lbl"]
            act083TabOtherActions.text = hmAux_Trans["tab_other_actions_lbl"]
            act083TabSerial.text = hmAux_Trans["tab_serial_site_lbl"]
            act083TvNoResult.text = hmAux_Trans["no_record_lbl"]
        }
    }

    private fun iniUIFooter() {
        iniFooter()
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context)
        mAct_Info = Constant.ACT083
        mAct_Title = Constant.ACT083 + "_" + "title"
        //
        val mFooter = ToolBox_Inf.loadFooterSiteOperationInfo(context)
        mSite_Value = mFooter[Constant.FOOTER_SITE]
        mOperation_Value = mFooter[Constant.FOOTER_OPERATION]
        //
        setUILanguage(hmAux_Trans)
        setMenuLanguage(hmAux_Trans)
        //LUCHE - 12/05/2021 - Comentaod pois nessa pagina o titulo será o tema escolhido
        //setTitleLanguage()
        setFooter()
    }


    override fun footerCreateDialog() {
        //super.footerCreateDialog()
        ToolBox_Inf.buildFooterDialog(context)
    }

    private var userFocusFilter = 1

    private fun initActions() {
        binding.act083MainContent.act083MketFilter.setOnReportTextChangeListner(object :
            MKEditTextNM.IMKEditTextChangeText {
            override fun reportTextChange(text: String?) {
            }

            override fun reportTextChange(text: String?, p1: Boolean) {
                binding.act083MainContent.act083RvActionsList.stopScroll()
                applyTextFilter(text)
            }
        })
        binding.act083MainContent.act083Tabs.setOnCheckedChangeListener { _, checkedId ->
            binding.act083MainContent.act083RvActionsList.stopScroll()
            with(binding.act083MainContent) {
                when (checkedId) {
                    act083TabMyActions.id -> {
                        myActionTabSelection()
                        mPresenter.updateMyActionList(1)
                    }
                    //
                    act083TabSerial.id -> {
                        serialTabSelection()
                        checkSerialSiteInventory()
                    }
                    //
                    else -> {
                        otherActionTabSelection()
                        mPresenter.updateMyActionList(0)
                    }
                }
            }
        }

        binding.act083MainContent.act083IbMainUserSelection.setOnClickListener {
            applyMainUserFilter = !applyMainUserFilter
            setIvMainUserSelection()
            if (getCurrentTab() < 2) {
                if (::mAdapter.isInitialized) {
                    mAdapter.userMainFilterOn = applyMainUserFilter
                }
                applyTextFilter(binding.act083MainContent.act083MketFilter.text.toString())
            }
        }

    }

    private fun serialTabSelection() {
        if (!firstClickSerialTab) {
            serialClick()
        }
    }

    private fun otherActionTabSelection() {
        userFocusFilter = 0
        updateMyActionList(userFocusFilter)
    }

    private fun myActionTabSelection() {
        userFocusFilter = 1
        updateMyActionList(userFocusFilter)
    }


    var firstClickSerialTab = true
    private fun serialClick() {
        with(binding.act083MainContent) {
            act083TvNoResult.visibility = View.GONE
            act083RvActionsList.visibility = View.GONE
        }
        changeProgressBarVisility(true)
    }

    private fun setIvMainUserSelection() {
        if (applyMainUserFilter) {
            binding.act083MainContent.act083IbMainUserSelection.setImageDrawable(null)
            binding.act083MainContent.act083IbMainUserSelection.setImageResource(R.drawable.ic_person_white_24dp)
            binding.act083MainContent.act083IbMainUserSelection.background =
                context.getDrawable(R.drawable.my_action_toogle_pressed)
            binding.act083MainContent.act083IbMainUserSelection.postInvalidate()
        } else {
            binding.act083MainContent.act083IbMainUserSelection.setImageDrawable(null)
            binding.act083MainContent.act083IbMainUserSelection.background =
                context.getDrawable(R.drawable.my_action_toogle_default)
            var drawable = DrawableCompat.wrap(
                ContextCompat.getDrawable(
                    context, R.drawable.ic_person_black_24dp
                )!!
            )!!
            DrawableCompat.setTint(
                drawable.mutate(), ContextCompat.getColor(context, R.color.my_action_toogle_circle)
            )
            binding.act083MainContent.act083IbMainUserSelection.setImageDrawable(drawable)
            binding.act083MainContent.act083IbMainUserSelection.postInvalidate()
        }
    }

    private fun applyTextFilter(text: String?) {
        if (::mAdapter.isInitialized) {
            mAdapter.filter.filter(text)
        }
    }

    private fun updateMyActionList(userFocusFilter: Int) {
        //Reseta visibilidade das views
        with(binding.act083MainContent) {
            act083TvNoResult.visibility = View.GONE
            act083RvActionsList.visibility = View.GONE
        }
        changeProgressBarVisility(true)
    }

    /**
     * LUCHE
     * Fun que controla a visibilidade do progress e também o estado de habilitado ou não das tabs
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun changeProgressBarVisility(show: Boolean) {

        val isLoading = if (show) View.VISIBLE else View.GONE
        val removeLoading = if (!show) View.VISIBLE else View.GONE


        with(binding.act083MainContent) {
            act083PbLoad.apply {
                visibility = isLoading
            }

            act083RvActionsList.apply {
                visibility = removeLoading
            }

            act083IbMainUserSelection.apply {
                isEnabled = !show
            }

            act083MketFilter.apply {
                isEnabled = !show
            }
        }


        //
        toggleTabEnableStattus(!show)
    }

    /**
     * LUCHE - 21/06/2021
     * Fun que habilita de dasabilita as tabs.
     */
    private fun toggleTabEnableStattus(enable: Boolean) {
        with(binding.act083MainContent) {
            act083TabMyActions.isEnabled = enable
            act083TabOtherActions.isEnabled = enable
            act083TabSerial.isEnabled = enable
        }
    }

    override fun callAct005() {
        val mIntent = Intent(context, Act005_Main::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        mIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        startActivity(mIntent)
        finish()
    }

    override fun callAct070(bundle: Bundle) {
        val mIntent = Intent(context, Act070_Main::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        mIntent.putExtras(bundle)
        startActivity(mIntent)
        finish()
    }

    override fun callAct071(bundle: Bundle) {
        val mIntent = Intent(context, Act071_Main::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (bundle != null) {
            mIntent.putExtras(bundle)
        }
        startActivity(mIntent)
        finish()
    }

    override fun callAct038(bundle: Bundle) {
        val mIntent = Intent(context, Act038_Main::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        mIntent.putExtras(bundle)
        startActivity(mIntent)
        finish()
    }

    override fun callAct011(bundle: Bundle) {
        val mIntent = Intent(context, Act011_Main::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        mIntent.putExtras(bundle)
        context.startActivity(mIntent)
        finish()
    }

    override fun callAct033() {
        val mIntent = Intent(context, Act033_Main::class.java)
        val bundle = Bundle()
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT017)
        mIntent.putExtras(bundle)
        startActivityForResult(mIntent, CHANGE_ZONE_RESULT_CODE)
    }

    override fun callAct020(bundle: Bundle) {
        val mIntent = Intent(context, Act020_Main::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT083)
        mIntent.putExtras(bundle)
        startActivity(mIntent)
        finish()
    }

    override fun callAct009(bundle: Bundle) {
        val mIntent = Intent(context, Act009_Main::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        mIntent.putExtras(bundle)
        startActivity(mIntent)
        finish()
    }

    override fun callAct006(bundle: Bundle) {
        val mIntent = Intent(context, Act006_Main::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        mIntent.putExtras(bundle)
        startActivity(mIntent)
        finish()
    }

    override fun callAct016(bundle: Bundle) {
        val mIntent = Intent(context, Act016_Main::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        mIntent.putExtras(bundle)
        startActivity(mIntent)
        finish()
    }

    override fun callAct068(bundle: Bundle) {
        val mIntent = Intent(context, Act068_Main::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        mIntent.putExtras(bundle)
        startActivity(mIntent)
        finish()
    }

    override fun callAct094() {
        val mIntent = Intent(context, Act094_Main::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(mIntent)
        finish()
    }


    override fun onBackPressed() {
        //super.onBackPressed()
        mPresenter.onBackPressedClicked()

    }

    override fun showMsg(type: String, item: MyActions, isDownloadedTicket: Boolean) {
        var title: String? = ""
        var msg: String? = ""
        var listener: DialogInterface.OnClickListener? = null
        var btnNegative: Int? = null

        when (type) {
            MODULE_CHECKLIST_FORM_IN_PROCESSING -> {
                title = hmAux_Trans["alert_ttl_exists_in_processing"]
                msg = hmAux_Trans["alert_msg_exists_in_processing"]
                btnNegative = 0
            }

            MODULE_CHECKLIST_START_FORM -> {
                title = hmAux_Trans["alert_ttl_start_new_processing"]
                msg = hmAux_Trans["alert_msg_start_new_processing"]
                btnNegative = 1
                listener = DialogInterface.OnClickListener { dialogInterface, i ->
                    mPresenter.checkFormFlow(item)
                }
            }

            SITE_RESTRICTION_NO_ACCESS -> {
                title = hmAux_Trans["alert_form_site_restriction_ttl"]
                msg = hmAux_Trans["alert_form_site_restriction_no_access_msg"]
                btnNegative = 0
            }

            FlowTicketAccessError.SITE_ACCESS_CONFIRM -> {
                title = hmAux_Trans["alert_form_site_restriction_ttl"]
                msg = hmAux_Trans["alert_form_site_restriction_confirm"]
                listener = DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                    mPresenter.flowTicketSiteRestriction(item, isDownloadedTicket)
                }
                btnNegative = 1
            }

            MODULE_SCHEDULE_FORM_DATA_CREATION_ERROR -> {
                title = hmAux_Trans["alert_error_on_create_form_ttl"]
                msg = hmAux_Trans["alert_error_on_create_form_msg"]
                btnNegative = 0
            }

            EMPTY_SERIAL_SEARCH -> {
                title = hmAux_Trans["alert_no_serial_found_ttl"]
                msg = hmAux_Trans["alert_no_serial_found_msg"]
                btnNegative = 0
            }

            SERIAL_CREATION_DENIED -> {
                title = hmAux_Trans["alert_no_serial_found_ttl"]
                msg = hmAux_Trans["alert_product_no_allow_new_serial_msg"]
                btnNegative = 0
            }

            MODULE_TICKET_EXEC_CONFIRM -> {
                title = hmAux_Trans["alert_ticket_action_start_ttl"]
                msg = hmAux_Trans["alert_ticket_action_start_confirm"]
                btnNegative = 1
                listener = DialogInterface.OnClickListener { dialog, which ->
                    mPresenter.checkTicketFlow(item)
                }
            }

            MODULE_SCHEDULE_TICKET_CREATION_ERROR -> {
                title = hmAux_Trans["alert_error_on_create_ticket_action_ttl"]
                msg = hmAux_Trans["alert_error_on_create_ticket_action_msg"]
                btnNegative = 0
            }

            MODULE_SCHEDULE_STATUS_PREVENTS_TO_OPEN -> {
                title = hmAux_Trans["alert_schedule_status_prevents_to_open_ttl"]
                msg = hmAux_Trans["alert_schedule_status_prevents_to_open_msg"]
                btnNegative = 0
            }

            PROFILE_PRJ001_AP_NOT_FOUND -> {
                title = hmAux_Trans["alert_menu_app_profile_not_found_ttl"]
                msg = hmAux_Trans["alert_form_ap_menu_profile_not_found_msg"]
                btnNegative = 0
            }

            PROFILE_MENU_TICKET_NOT_FOUND -> {
                title = hmAux_Trans["alert_menu_app_profile_not_found_ttl"]
                msg = hmAux_Trans["alert_ticket_menu_profile_not_found_msg"]
                btnNegative = 0
            }

            FREE_EXECUTION_BLOCKED -> {
                title = hmAux_Trans["alert_free_execution_blocked_ttl"]
                msg = hmAux_Trans["alert_free_execution_blocked_msg"]
                btnNegative = 0
            }
        }

        if (btnNegative != null) {
            ToolBox.alertMSG(
                this, title, msg, listener, btnNegative
            )
        }
    }

    override fun showAlertMsg(ttl: String, msg: String) {
        ToolBox.alertMSG(
            context, ttl, msg, null, 0
        )
    }

    override fun showToast(msg: String) {
        ToolBox.toastMSG(context, msg)
    }

    /**
     * LUCHE - 18/06/2021
     * Metodo que atualiza os dados após troca de site no agendamento.
     */
    override fun updateFooterInfos() {
        //O unico efeito da troca na lista é visibilidade da informação de site
        //sendo assim, somente o notify deve dar conta
        mAdapter.notifyDataSetChanged()
        //Atualiza dados no footer
        iniUIFooter()
    }

    override fun setPlaceholderTextAndVisibility(currentTabCounter: Int) {
        if (currentTabCounter > 0) {
            if (mAdapter.itemCount == 0) {
                binding.act083MainContent.apply {
                    act083TvNoResult.text = hmAux_Trans["no_record_for_filter_lbl"]
                    act083TvNoResult.visibility = View.VISIBLE
                    act083RvActionsList.visibility = View.GONE
                }
            } else {
                binding.act083MainContent.apply {
                    act083TvNoResult.visibility = View.GONE
                    act083RvActionsList.visibility = View.VISIBLE
                }
            }
        } else {
            binding.act083MainContent.act083TvNoResult.text = hmAux_Trans["no_record_lbl"]
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CHANGE_ZONE_RESULT_CODE -> processChanceZoneResult(resultCode)
            else -> {
            }
        }
    }

    private fun processChanceZoneResult(resultCode: Int) {
        if (resultCode == RESULT_OK) {
            updateFooterInfos()
            //Clica novamente no item
            mPresenter.actionSelected?.let {
                mPresenter.checkScheduleFlow(it)
            }
        } else {
            ToolBox_Con.setPreference_Site_Code(context, mPresenter.siteCodeBack)
            ToolBox_Con.setPreference_Zone_Code(context, mPresenter.zoneCodeBack)
        }
    }

    //TRATA MSG SESSION NOT FOUND
    override fun processLogin() {
        super.processLogin()
        //
        ToolBox_Con.cleanPreferences(context)
        //
        ToolBox_Inf.call_Act001_Main(context)
        //
        finish()
    }

    //TRATAVIA QUANDO VERSÃO RETORNADO É EXPIRED OU VERSÃO INVALIDA
    override fun processUpdateSoftware(mLink: String?, mRequired: String?) {
        super.processUpdateSoftware(mLink, mRequired)
        //
        if (progressDialog != null) {
            progressDialog.dismiss()
        }
        //
        ToolBox_Inf.executeLogoffAndUpdateSoftware(context)
    }


    override fun processError_1(mLink: String?, mRequired: String?) {
        super.processError_1(mLink, mRequired)
        if (wsProcess == WsSerialSiteInventory::class.java.name) {
            handleConnectionProblemsForSiteInvetoryService()
        } else if (wsProcess == WsSelectDestination::class.java.name) {
            wsProcess = ""
            progressDialog.dismiss()
            Toast.makeText(
                context,
                hmAux_Trans[ALERT_DESTINATION_SELECTED_MSG],
                Toast.LENGTH_SHORT
            ).show()
            mPresenter.selectionDestinationAvailable?.let {
                mPresenter.saveDestination(context, null, it)
            }

        } else {
            mPresenter.formButtonData = null
            progressDialog.dismiss()
            if (serialActionSelected > -1) {
                resetActionPosition()
            }
        }
    }

    private fun handleConnectionProblemsForSiteInvetoryService() {
        wsProcess = ""
        progressDialog.dismiss()
        hasConnectionFail = true
        mPresenter.updateRefreshSerialSiteFile(true)
        iniRecycler(emptyArray<MyActionsBase>().toMutableList())
    }

    override fun processCustom_error(mLink: String?, mRequired: String?) {
        super.processCustom_error(mLink, mRequired)
        if (wsProcess == WsSerialSiteInventory::class.java.name) {
            handleConnectionProblemsForSiteInvetoryService()
        } else {
            mPresenter.formButtonData = null
            progressDialog.dismiss()
            if (serialActionSelected > -1) {
                resetActionPosition()
            }
        }
    }

    override fun processError_http() {
        //Super realiza o mesmo comportamento do error_1
//        super.processError_http();
        progressDialog.dismiss()

        if (binding.act083MainContent.act083TabSerial.visibility != View.VISIBLE) {
            ToolBox_Con.setBooleanPreference(
                applicationContext, ConstantBaseApp.PREFERENCE_SERIAL_OFFLINE_FLOW, true
            )
        }

        if (serialActionSelected > -1) {

            when (typeSerial) {

                is TypeSerial.MORE_ACTIONS -> {
                        ToolBox_Inf.showNoConnectionDialog(context)
                }

                is TypeSerial.INFO_SERIAL -> {
                        ToolBox_Inf.showNoConnectionDialog(context)
                }

                else -> {

                    //
                    mAdapter.getMyActionByPosition(serialActionSelected)?.let {
                        mPresenter.processLocalSearchForSerialAction(
                            productCode = it.productCode,
                            serialId = it.serialId,
                            mdProductSerial = null,
                            actionType = it.actionType,
                            processPk = it.processPk
                        )
                    }
                }
            }
            //
            resetActionPosition()
        } else {
            mPresenter.offlineSerialSearch()
        }
    }

    override fun resetActionPosition() {
        serialActionSelected = -1
    }

    override fun callAct092(bundle: Bundle) {
        mPresenter.clear092Preference()
        val mIntent = Intent(context, Act092_Main::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        //
        mIntent.putExtras(bundle)
        startActivity(mIntent)
        finish()
    }

    override fun callAct093(bundle: Bundle) {
        Intent(context, Act093_Main::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtras(bundle)
            startActivity(this)
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun loadTranslation(): HMAux? {
        val transList: MutableList<String> = java.util.ArrayList()
        transList.add("act083_title")
        transList.add("tab_my_actions_lbl")
        transList.add("tab_other_actions_lbl")
        transList.add("filter_hint")
        transList.add("form_lbl")
        transList.add("IN_PROCESSING")
        transList.add("no_record_lbl")
        transList.add("no_record_for_filter_lbl")
        transList.add("no_connection_try_again_lbl")
        transList.add("offline_mode_on_sync_required_lbl")
        transList.add("other_steps_available_lbl")
        transList.add("cell_step_lbl")
        transList.add("dialog_download_ticket_ttl")
        transList.add("dialog_download_ticket_start")
        transList.add("progress_sync_ttl")
        transList.add("progress_sync_msg")
        transList.add("site_desc_not_found_lbl")
        transList.add("cell_waiting_approval")
        //
        transList.add("alert_ttl_exists_in_processing")
        transList.add("alert_msg_exists_in_processing")
        transList.add("alert_ttl_start_new_processing")
        transList.add("alert_msg_start_new_processing")
        transList.add("alert_error_on_create_form_ttl")
        transList.add("alert_error_on_create_form_msg")
        transList.add("alert_no_serial_found_ttl")
        transList.add("alert_no_serial_found_msg")
        transList.add("alert_product_no_allow_new_serial_msg")
        transList.add("alert_ticket_action_start_ttl")
        transList.add("alert_ticket_action_start_confirm")
        transList.add("alert_error_on_create_ticket_action_ttl")
        transList.add("alert_error_on_create_ticket_action_msg")
        transList.add("alert_schedule_status_prevents_to_open_ttl")
        transList.add("alert_schedule_status_prevents_to_open_msg")
        transList.add("alert_menu_app_profile_not_found_ttl")
        transList.add("alert_form_ap_menu_profile_not_found_msg")
        transList.add("alert_menu_app_profile_not_found_ttl")
        transList.add("alert_ticket_menu_profile_not_found_msg")
        transList.add("alert_free_execution_blocked_ttl")
        transList.add("alert_free_execution_blocked_msg")
        //
        transList.add("alert_form_site_restriction_ttl")
        transList.add("alert_form_site_restriction_confirm")
        transList.add("dialog_serial_search_ttl")
        transList.add("dialog_serial_search_start")
        //
        transList.add("sys_main_menu_assets_local_lbl")
        transList.add("sys_main_menu_calendar_lbl")
        transList.add("sys_main_menu_search_lbl")
        //
        transList.add("new_form_lbl")
        transList.add("alert_no_form_lbl")
        transList.add("alert_no_form_for_product_msg")
        transList.add("alert_no_form_for_operation_msg")
        transList.add("alert_no_form_for_site_msg")
        transList.add("alert_no_form_ttl")
        transList.add("alert_product_or_serial_not_found_ttl")
        transList.add("alert_product_or_serial_not_found_msg")
        //
        transList.add("alert_form_os_requires_serial_ttl")
        transList.add("alert_form_os_requires_serial_msg")
        //
        transList.add("alert_not_execute_ttl")
        transList.add("alert_not_execute_msg")
        transList.add("alert_not_execute_justify_date_ttl")
        transList.add("alert_not_execute_justify_option_lbl")
        transList.add("alert_not_execute_justify_comment_lbl")
        transList.add("sys_alert_btn_cancel")
        transList.add("alert_not_execute_save_btn")
        transList.add("alert_not_execute_justify_required_ttl")
        transList.add("alert_not_execute_justify_option_required_msg")
        transList.add("alert_not_execute_justify_comment_required_msg")
        transList.add("alert_not_execute_justify_success_ttl")
        transList.add("alert_not_execute_justify_success_msg")
        transList.add("btn_cancel_schedule")
        transList.add("warning_not_execute_justify_required_date_hour")
        transList.add("warning_not_execute_justify_future_date_hour")
        transList.add("alert_not_execute_justify_lost_data_ttl")
        transList.add("alert_not_execute_justify_lost_data_msg")
        transList.add("warning_not_execute_justify_future_date_hour")
        transList.add("progress_n_form_sync_ttl")
        //
        transList.add("btn_open_action_lbl")
        transList.add("btn_download_action_lbl")
        transList.add("btn_continue_action_lbl")
        transList.add("btn_select_serial_info_lbl")
        //
        transList.add("progress_serial_structure_ttl")
        transList.add("progress_serial_structure_msg")
        //
        transList.add("item_in_process_lbl")
        //
        transList.add("cell_justify_lbl")
        transList.add("progress_n_form_sync_ttl")
        transList.add("progress_n_form_sync_msg")
        //
        transList.add("progress_site_search_ttl")
        transList.add("progress_site_search_msg")
        //
        transList.add("tab_serial_site_lbl")
        transList.add("serial_site_measure_lbl")
        transList.add("serial_site_preventive_cycle_lbl")
        transList.add("serial_site_next_cycle_lbl")
        transList.add("btn_serial_site_status_lbl")
        transList.add("btn_serial_site_select_serial_lbl")
        //
        transList.add("dialog_serial_outdate_ttl")
        transList.add("dialog_serial_outdate_msg")
        //
        transList.add(Act094Translate.ALERT_DESTINATION_SELECTED_MSG)
        transList.add(Act094Translate.PROCESS_SELECTION_DESTINATION_TITLE)
        transList.add(Act094Translate.PROCESS_SELECTION_DESTINATION_MSG)
        transList.add(Act094Translate.ALERT_TRIP_NOT_FOUND_TTL)
        transList.add(Act094Translate.ALERT_TRIP_NOT_FOUND_MSG)
        transList.add(BTN_SELECT_DESTINATION)
        return ToolBox_Inf.setLanguage(
            context,
            mModule_Code,
            mResource_Code,
            ToolBox_Con.getPreference_Translate_Code(context),
            transList
        )
    }

}