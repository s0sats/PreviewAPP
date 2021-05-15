package com.namoadigital.prj001.ui.act083

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.R
import com.namoadigital.prj001.adapter.MyActionsAdapter
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.databinding.Act083MainBinding
import com.namoadigital.prj001.model.MD_Schedule_Exec
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.service.WS_Sync
import com.namoadigital.prj001.service.WS_TK_Ticket_Download
import com.namoadigital.prj001.ui.act005.Act005_Main
import com.namoadigital.prj001.ui.act011.Act011_Main
import com.namoadigital.prj001.ui.act017.Act017_Main
import com.namoadigital.prj001.ui.act033.Act033_Main
import com.namoadigital.prj001.ui.act038.Act038_Main
import com.namoadigital.prj001.ui.act070.Act070_Main
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import com.namoadigital.prj001.view.dialog.ScheduleRequestSerialDialog
import com.namoadigital.prj001.view.dialog.ScheduleRequestSerialDialog.OnScheduleRequestSerialDialogListeners
import com.namoadigital.prj001.view.dialog.ScheduleRequestSerialDialog2

class Act083_Main : Base_Activity() {
    private lateinit var binding: Act083MainBinding
    private lateinit var mAdapter: MyActionsAdapter
    private lateinit var bundle: Bundle
    private var wsProcess =""
    private var hmAuxTicketDownload: HMAux = HMAux()
    private val CHANGE_ZONE_RESULT_CODE = 10
    private var serialDialog: ScheduleRequestSerialDialog2? = null

    private val viewModel by lazy {
        val factory = Act083ViewModelFactory(
                application,
                bundle,
                TK_TicketDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                TkTicketCacheDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                MD_Schedule_ExecDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                GE_Custom_Form_ApDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                GE_Custom_Form_LocalDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                mModule_Code,
                mResource_Code
        )
        //
        ViewModelProvider(this, factory).get(Act083ViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Act083MainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //
        setSupportActionBar(binding.toolbar)
        //
        initBundle(savedInstanceState)
        iniSetup()
        iniTrans()
        initVars()
        iniUIFooter()
        initActions()
    }

    private fun initBundle(savedInstanceState: Bundle?) {
        bundle = (savedInstanceState?: intent.extras) as Bundle
    }

    private fun iniTrans() {
        hmAux_Trans = viewModel.hmAux_Trans
    }

    private fun iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                ConstantBaseApp.ACT083
        )
    }

    private fun initVars() {
        supportActionBar?.title = viewModel.getActTitle()
        setLabels()
        setChips()
        iniRecycler()
    }

    private fun setChips() {
        viewModel.getChipList().forEach {
            binding.act083MainContent.act083CgFilter.addView(createTvChip(it))
        }
    }

    private fun iniRecycler() {
        val myActionsList = viewModel.myActionsList
        if(myActionsList.size > 0) {
            binding.act083MainContent.act083TvNoResult.visibility = View.GONE
            //
            mAdapter = MyActionsAdapter(
                    myActionsList,
                    this::onMyActionClick
            )
            //
            with(binding.act083MainContent.act083RvActionsList) {
                layoutManager = LinearLayoutManager(context)
                adapter = mAdapter
                visibility = View.VISIBLE
            }
            //
            if(!binding.act083MainContent.act083MketFilter.text.isNullOrEmpty()){
                applyTextFilter(binding.act083MainContent.act083MketFilter.text.toString())
            }
        }else{
            with(binding.act083MainContent){
                act083TvNoResult.visibility = View.VISIBLE
                act083RvActionsList.visibility = View.INVISIBLE
            }
        }
    }

    fun onMyActionClick(myAction: MyActions): Unit{
        //viewModel.processActionClick(myAction)
        when(myAction.actionType){
            MyActions.MY_ACTION_TYPE_TICKET -> processLocalTicketClick(myAction)
            MyActions.MY_ACTION_TYPE_TICKET_CACHE -> processCachedTicketClick(myAction)
            MyActions.MY_ACTION_TYPE_SCHEDULE -> processScheduleClick(myAction)
            MyActions.MY_ACTION_TYPE_FORM_AP -> processFormApClick(myAction)
            MyActions.MY_ACTION_TYPE_FORM -> processFormClick(myAction)
        }
    }

    private fun processLocalTicketClick(myAction: MyActions) {
        callAct070(
                viewModel.getLocalTicket(
                        myAction
                )
        )
    }

    private fun processCachedTicketClick(myAction: MyActions) {
        if(ToolBox_Con.isOnline(context)){
            wsProcess = WS_TK_Ticket_Download::class.java.name
            showPD(
                    hmAux_Trans["dialog_download_ticket_ttl"],
                    hmAux_Trans["dialog_download_ticket_start"]
            )
            //
            viewModel.prepareWsTicketDownload(myAction)
        }else{
            ToolBox_Inf.showNoConnectionDialog(context)
        }
    }


    private fun showPD(ttl: String?, msg: String?) {
        enableProgressDialog(
                ttl,
                msg,
                hmAux_Trans["sys_alert_btn_cancel"],
                hmAux_Trans["sys_alert_btn_ok"]
        )
    }

    private fun processScheduleClick(myAction: MyActions) {
        if(viewModel.isScheduleStarted(myAction)){
            if(viewModel.isScheduleStatusPossibleToOpen(myAction)){
                if(viewModel.isScheduleFormType(myAction)){
                    scheduleFormFlow(myAction)
                }else{
                    scheduleTicketFlow(myAction)
                }
            }else{
                showAlert(
                        hmAux_Trans["alert_schedule_status_prevents_to_open_ttl"],
                        hmAux_Trans["alert_schedule_status_prevents_to_open_msg"]
                )
            }
        }else{
            if(ToolBox_Inf.isSiteBlockedOrLimitExecutionReached(context)){
                showAlert(
                        hmAux_Trans["alert_free_execution_blocked_ttl"],
                        hmAux_Trans["alert_free_execution_blocked_msg"]
                )
            }else{
                if(ToolBox_Inf.equalsToLoggedSite(context, myAction.siteCode.toString())){
                    if(viewModel.isScheduleFormType(myAction)){
                        if(viewModel.isAnyFormInProcessing(myAction)){
                            showAlert(
                                    hmAux_Trans["alert_ttl_exists_in_processing"],
                                    hmAux_Trans["alert_msg_exists_in_processing"]
                            )
                        }else{
                            showAlert(
                                    hmAux_Trans["alert_ticket_action_start_ttl"],
                                    hmAux_Trans["alert_ticket_action_start_confirm"],
                                    (DialogInterface.OnClickListener { _, _ ->
                                        scheduleFormFlow(myAction)
                                    }),
                                    1
                            )
                        }
                    }else{
                        showAlert(
                                hmAux_Trans["alert_ticket_action_start_ttl"],
                                hmAux_Trans["alert_ticket_action_start_confirm"],
                                (DialogInterface.OnClickListener { _, _ ->
                                    scheduleTicketFlow(myAction)
                                }),
                                1
                        )
                    }
                }else{
                    startSiteChangeFlow(myAction)
                }
            }
        }
    }

    private fun scheduleTicketFlow(myAction: MyActions) {
        if(viewModel.isScheduleStatusPossibleToOpen(myAction)){
            callAct070(viewModel.getScheduleTicketBundle(myAction))
        }else{
            prepareOpenTicket(myAction)
        }
    }

    private fun prepareOpenTicket(myAction: MyActions) {
        TODO("Not yet implemented")
    }

    private fun scheduleFormFlow(myAction: MyActions) {
        when {
            viewModel.isScheduleStatusPossibleToOpen(myAction) -> {
                prepareOpenForm(myAction)
            }
            viewModel.hasSerialDefined(myAction) -> {
                buildRequestSerialDialog(
                        myAction,
                        false
                )
                //
                viewModel.executeSerialSearch(
                        myAction.productCode,
                        myAction.productId,
                        myAction.serialId,
                        true
                )
            }
            else -> {
                //Cria e exibe dialog que requer serial.
                buildRequestSerialDialog(
                        myAction,
                        true
                )
            }
        }
    }

    private fun prepareOpenForm(myAction: MyActions) {
        callAct011(viewModel.getScheduleFormBundle(myAction))
    }

    private fun buildRequestSerialDialog(myAction: MyActions, showDialog: Boolean) {
        val scheduleExec: MD_Schedule_Exec = viewModel.getMdSchedule(myAction)
        val serialRule: String? = scheduleExec.serial_rule
        val serialMinLength = scheduleExec.serial_min_length
        val serialMaxLength = scheduleExec.serial_max_length
        //
        serialDialog = ScheduleRequestSerialDialog2(
                context,
                scheduleExec,
                serialRule,
                serialMinLength,
                serialMaxLength,
                object : ScheduleRequestSerialDialog2.OnScheduleRequestSerialDialogListeners {
                    override fun processToForm() {
                        val bundle = Bundle()
                        if (createFormLocalForSchedule(myAction, bundle)) {
                            //Atualiza fomr_data no item
//                            item.put(
//                                    GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA,
//                                    bundle.getString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA, "0")
//                            )
                            //
                            prepareOpenForm(myAction)
                        } else {
                            showAlert(
                                    hmAux_Trans["alert_error_on_create_form_ttl"],
                                    hmAux_Trans["alert_error_on_create_form_msg"]
                            )
                        }
                    }

                    override fun processToSearchSerial(serialID: String) {
                       viewModel.executeSerialSearch(
                                myAction.productCode,
                                myAction.productId,
                                serialID,
                                false)
                    }

                    override fun addMketControl(mketSerial: MKEditTextNM) {
                        addControlToActivity(mketSerial)
                    }

                    override fun removeMketControl(mketSerial: MKEditTextNM) {
                        removeControlFromActivity(mketSerial)
                    }
                }
        )
        //
        if (showDialog) {
            serialDialog?.show()
        }
    }

    private fun createFormLocalForSchedule(myAction: MyActions, bundle: Bundle): Boolean {
        TODO("Not yet implemented")
    }


    private fun addControlToActivity(mketSerial: MKEditTextNM) {
        controls_sta.add(mketSerial)
    }

    private fun removeControlFromActivity(mketSerial: MKEditTextNM) {
        controls_sta.remove(mketSerial)
    }

    private fun startSiteChangeFlow(myAction: MyActions) {
        if(viewModel.hasScheduleSiteAccess(myAction.siteCode)){
            ToolBox.alertMSG_YES_NO(
                    context,
                    hmAux_Trans["alert_form_site_restriction_ttl"],
                    hmAux_Trans["alert_form_site_restriction_confirm"],
                    { _, _ ->
                        if (!ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, null)
                                && !ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_OI, null)) {
                            ToolBox_Con.setPreference_Site_Code(context, myAction.siteCode.toString())
                            ToolBox_Con.setPreference_Zone_Code(context, -1)
                            //
                            processScheduleClick(myAction)
                        } else {
                            ToolBox_Con.setPreference_Site_Code(context, myAction.siteCode.toString())
                            ToolBox_Con.setPreference_Zone_Code(context, -1)
                            callAct033()
                        }
                    },
                    1
            )
        }else{
            showAlert(
                    hmAux_Trans["alert_form_site_restriction_ttl"],
                    hmAux_Trans["alert_form_site_restriction_no_access_msg"]
            )
        }
    }

    private fun showAlert(ttl: String?, msg: String?, clickListner: DialogInterface.OnClickListener? = null, negativeBtn: Int = 0){
        ToolBox.alertMSG(
                context,
                ttl,
                msg,
                clickListner,
                negativeBtn
        )
    }

    private fun processFormApClick(myAction: MyActions) {
        callAct038(
                viewModel.getFormApBundle(myAction)
        )
    }

    private fun processFormClick(myAction: MyActions) {
        callAct011(
                viewModel.getFormBundle(myAction)
        )
    }

    override fun processCloseACT(mLink: String?, mRequired: String?) {
        super.processCloseACT(mLink, mRequired)
        processCloseACT(mLink, mRequired, HMAux())
    }

    override fun processCloseACT(mLink: String?, mRequired: String?, hmAux: HMAux) {
        super.processCloseACT(mLink, mRequired, hmAux)
        when(wsProcess){
            WS_TK_Ticket_Download::class.java.name -> {
                wsProcess = ""
                if (viewModel.verifyProductOutdateForForm(hmAux)) {
                    progressDialog.dismiss()
                    //
                    if (ToolBox_Con.isOnline(context)) {
                        hmAuxTicketDownload = hmAux
                        wsProcess = WS_Sync::class.java.name
                        showPD(
                                hmAux_Trans["progress_sync_ttl"],
                                hmAux_Trans["progress_sync_msg"]
                        )
                        //
                        viewModel.prepareWsFormSync()
                    } else {
                        //
                        callAct070(
                                viewModel.getCacheTicketBundle(hmAux)
                        )
                    }
                } else {
                    progressDialog.dismiss()
                    callAct070(
                            viewModel.getCacheTicketBundle(hmAux)
                    )
                }
            }
            WS_Sync::class.java.name -> {
                wsProcess = ""
                progressDialog.dismiss()
                //
                callAct070(
                        viewModel.getCacheTicketBundle(hmAuxTicketDownload)
                )
            }
            else -> progressDialog?.dismiss()
        }
    }

    private fun createTvChip(chipLabel: String) : TextView {
        val tvChip = TextView(ContextThemeWrapper(context, R.style.TextViewChips))
        tvChip.apply {
            text = chipLabel
        }
        //
        return tvChip
    }

    private fun setLabels() {
        with(binding.act083MainContent){
            act083MketFilter.hint = hmAux_Trans["filter_hint"]
            act083TabMyActions.text = hmAux_Trans["tab_my_actions_lbl"]
            act083TabOtherActions.text = hmAux_Trans["tab_other_actions_lbl"]
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

    private fun initActions() {
        binding.act083MainContent.act083MketFilter.setOnReportTextChangeListner(object : MKEditTextNM.IMKEditTextChangeText {
            override fun reportTextChange(text: String?) {
            }

            override fun reportTextChange(text: String?, p1: Boolean) {
                applyTextFilter(text)
            }
        })
        binding.act083MainContent.act083Tabs.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                binding.act083MainContent.act083TabMyActions.id -> updateMyActionList(1)
                else -> updateMyActionList(0)
            }
        }
    }

    private fun applyTextFilter(text: String?) {
        mAdapter.filter.filter(text)
    }

    private fun updateMyActionList(userFocusFilter: Int) {
        viewModel.updateMyActionList(userFocusFilter)
        iniRecycler()

    }

    private fun callAct005() {
        val mIntent = Intent(context, Act005_Main::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        mIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        startActivity(mIntent)
        finish()
    }

    private fun callAct070(bundle: Bundle) {
        val mIntent = Intent(context, Act070_Main::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        mIntent.putExtras(bundle)
        startActivity(mIntent)
        finish()
    }

    private fun callAct038(bundle: Bundle) {
        val mIntent = Intent(context, Act038_Main::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        mIntent.putExtras(bundle)
        startActivity(mIntent)
        finish()
    }

    private fun callAct011(bundle: Bundle) {
        val mIntent = Intent(context, Act011_Main::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        mIntent.putExtras(bundle)
        context.startActivity(mIntent)
        finish()
    }

    private fun callAct033() {
        val mIntent = Intent(context, Act033_Main::class.java)
        val bundle = Bundle()
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT017)
        mIntent.putExtras(bundle)
        startActivityForResult(mIntent, CHANGE_ZONE_RESULT_CODE)
    }




    override fun onBackPressed() {
        //super.onBackPressed()
        callAct005()
    }


}

