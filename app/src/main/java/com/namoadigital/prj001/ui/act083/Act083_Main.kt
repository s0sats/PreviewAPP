package com.namoadigital.prj001.ui.act083

import android.content.Intent
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.R
import com.namoadigital.prj001.adapter.MyActionsAdapter
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.databinding.Act083MainBinding
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.ui.act005.Act005_Main
import com.namoadigital.prj001.ui.act011.Act011_Main
import com.namoadigital.prj001.ui.act038.Act038_Main
import com.namoadigital.prj001.ui.act070.Act070_Main
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class Act083_Main : Base_Activity() {
    private lateinit var binding: Act083MainBinding
    private lateinit var mAdapter: MyActionsAdapter
    private lateinit var bundle: Bundle

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
        ToolBox.toastMSG(context,"Em Dev")
    }

    private fun processScheduleClick(myAction: MyActions) {
        ToolBox.toastMSG(context,"Em Dev")
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

    private fun initActions() {
        binding.act083MainContent.act083MketFilter.setOnReportTextChangeListner(object : MKEditTextNM.IMKEditTextChangeText {
            override fun reportTextChange(text: String?) {
            }

            override fun reportTextChange(text: String?, p1: Boolean) {
                applyTextFilter(text)
            }
        })
        binding.act083MainContent.act083Tabs.setOnCheckedChangeListener {
            _, checkedId ->
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


    override fun onBackPressed() {
        //super.onBackPressed()
        callAct005()
    }


}

