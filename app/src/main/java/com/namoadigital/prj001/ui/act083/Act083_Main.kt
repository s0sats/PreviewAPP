package com.namoadigital.prj001.ui.act083

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.R
import com.namoadigital.prj001.adapter.MyActionsAdapter
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.databinding.Act083MainBinding
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.service.WS_Serial_Search
import com.namoadigital.prj001.service.WS_Sync
import com.namoadigital.prj001.service.WS_TK_Ticket_Download
import com.namoadigital.prj001.ui.act005.Act005_Main
import com.namoadigital.prj001.ui.act011.Act011_Main
import com.namoadigital.prj001.ui.act017.Act017_Main
import com.namoadigital.prj001.ui.act020.Act020_Main
import com.namoadigital.prj001.ui.act033.Act033_Main
import com.namoadigital.prj001.ui.act038.Act038_Main
import com.namoadigital.prj001.ui.act070.Act070_Main
import com.namoadigital.prj001.ui.act071.Act071_Main
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import com.namoadigital.prj001.view.dialog.ScheduleRequestSerialDialog2

class Act083_Main : Base_Activity(), Act083_Main_Contract.I_View {
    private lateinit var binding: Act083MainBinding
    private lateinit var mAdapter: MyActionsAdapter
    private lateinit var bundle: Bundle
    private var wsProcess =""
    private var hmAuxTicketDownload: HMAux = HMAux()
    private val CHANGE_ZONE_RESULT_CODE = 10
    private var serialDialog: ScheduleRequestSerialDialog2? = null

    private val mPresenter by lazy {
        Act083_Main_Presenter(
                context,
                this,
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
                MD_SiteDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                TK_Ticket_CtrlDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                mModule_Code,
                mResource_Code
        )
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
        hmAux_Trans = mPresenter.hmAux_Trans
    }

    private fun iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                ConstantBaseApp.ACT083
        )
    }

    private fun initVars() {
        supportActionBar?.title = mPresenter.getActTitle()
        setLabels()
        setChips()
    }

    private fun setChips() {
        mPresenter.getChipList().forEach {
            binding.act083MainContent.act083CgFilter.addView(createTvChip(it))
        }
    }

    override fun iniRecycler() {
        val myActionsList = mPresenter.myActionsList
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
        mPresenter.processActionClick(myAction)
    }


    override fun showPD(ttl: String?, msg: String?) {
        enableProgressDialog(
                ttl,
                msg,
                hmAux_Trans["sys_alert_btn_cancel"],
                hmAux_Trans["sys_alert_btn_ok"]
        )
    }

    override fun addControlToActivity(mketSerial: MKEditTextNM) {
        controls_sta.add(mketSerial)
    }

    override fun removeControlFromActivity(mketSerial: MKEditTextNM) {
        controls_sta.remove(mketSerial)
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

    override fun setProcess(wsProcess: String) {
        this.wsProcess = wsProcess
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
                if (mPresenter.verifyProductOutdateForForm(hmAux)) {
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
                callAct070(
                        mPresenter.getCacheTicketBundle(hmAuxTicketDownload)
                )
            }
            WS_Serial_Search::class.java.name -> {
                wsProcess = ""
                progressDialog.dismiss()
                mPresenter.extractSearchResult(mLink)
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
            binding.act083MainContent.act083TvNoResult.visibility = View.GONE
            //
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
        changeProgressBarVisility(true)
        mPresenter.updateMyActionList(userFocusFilter)
    }

    override fun changeProgressBarVisility(show: Boolean) {
        with(binding.act083MainContent.act083PbLoad){
            visibility = if(show) View.VISIBLE else View.GONE
        }
    }

    private fun callAct005() {
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
        if (bundle != null) {
            bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT017)
            mIntent.putExtras(bundle)
        }
        startActivity(mIntent)
        finish()
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        callAct005()
    }

    override fun showMsg(type: String, item: MyActions) {
        var title: String? = ""
        var msg: String? = ""
        var listener: DialogInterface.OnClickListener? = null
        var btnNegative: Int? = null

        when (type) {
            Act017_Main.MODULE_CHECKLIST_FORM_IN_PROCESSING -> {
                title = hmAux_Trans["alert_ttl_exists_in_processing"]
                msg = hmAux_Trans["alert_msg_exists_in_processing"]
                btnNegative = 0
            }
            Act017_Main.MODULE_CHECKLIST_START_FORM -> {
                title = hmAux_Trans["alert_ttl_start_new_processing"]
                msg = hmAux_Trans["alert_msg_start_new_processing"]
                btnNegative = 1
                listener = DialogInterface.OnClickListener { dialogInterface, i ->
                    mPresenter.checkFormFlow(item)
                }
            }
            Act017_Main.MODULE_SCHEDULE_FORM_DATA_CREATION_ERROR -> {
                title = hmAux_Trans["alert_error_on_create_form_ttl"]
                msg = hmAux_Trans["alert_error_on_create_form_msg"]
                btnNegative = 0
            }
            Act017_Main.EMPTY_SERIAL_SEARCH -> {
                title = hmAux_Trans["alert_no_serial_found_ttl"]
                msg = hmAux_Trans["alert_no_serial_found_msg"]
                btnNegative = 0
            }
            Act017_Main.SERIAL_CREATION_DENIED -> {
                title = hmAux_Trans["alert_no_serial_found_ttl"]
                msg = hmAux_Trans["alert_product_no_allow_new_serial_msg"]
                btnNegative = 0
            }
            Act017_Main.MODULE_TICKET_EXEC_CONFIRM -> {
                title = hmAux_Trans["alert_ticket_action_start_ttl"]
                msg = hmAux_Trans["alert_ticket_action_start_confirm"]
                btnNegative = 1
                listener = DialogInterface.OnClickListener { dialog, which -> mPresenter.checkTicketFlow(item) }
            }
            Act017_Main.MODULE_SCHEDULE_TICKET_CREATION_ERROR -> {
                title = hmAux_Trans["alert_error_on_create_ticket_action_ttl"]
                msg = hmAux_Trans["alert_error_on_create_ticket_action_msg"]
                btnNegative = 0
            }
            Act017_Main.MODULE_SCHEDULE_STATUS_PREVENTS_TO_OPEN -> {
                title = hmAux_Trans["alert_schedule_status_prevents_to_open_ttl"]
                msg = hmAux_Trans["alert_schedule_status_prevents_to_open_msg"]
                btnNegative = 0
            }
            Act017_Main.PROFILE_PRJ001_AP_NOT_FOUND -> {
                title = hmAux_Trans["alert_menu_app_profile_not_found_ttl"]
                msg = hmAux_Trans["alert_form_ap_menu_profile_not_found_msg"]
                btnNegative = 0
            }
            Act017_Main.PROFILE_MENU_TICKET_NOT_FOUND -> {
                title = hmAux_Trans["alert_menu_app_profile_not_found_ttl"]
                msg = hmAux_Trans["alert_ticket_menu_profile_not_found_msg"]
                btnNegative = 0
            }
            Act017_Main.FREE_EXECUTION_BLOCKED -> {
                title = hmAux_Trans["alert_free_execution_blocked_ttl"]
                msg = hmAux_Trans["alert_free_execution_blocked_msg"]
                btnNegative = 0
            }
        }

        if (btnNegative != null) {
            ToolBox.alertMSG(
                    this,
                    title,
                    msg,
                    listener,
                    btnNegative
            )
        }
    }

    override fun showAlertMsg(ttl: String, msg: String) {
        ToolBox.alertMSG(
                context,
                ttl,
                msg,
                null,
                0
        )
    }

    override fun showToast(msg: String) {
        ToolBox.toastMSG(context, msg)
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
            //O unico efeito da troca na lista é visibilidade da informação de site
            //sendo assim, somente o notify deve dar conta
            mAdapter.notifyDataSetChanged()
            //Atualiza dados no footer
            iniUIFooter()
            //Clica novamente no item
            mPresenter.actionSelected?.let {
                mPresenter.checkScheduleFlow(it)
            }
        } else {
            ToolBox_Con.setPreference_Site_Code(context, mPresenter.siteCodeBack)
            ToolBox_Con.setPreference_Zone_Code(context, mPresenter.zoneCodeBack)
        }
    }
}

