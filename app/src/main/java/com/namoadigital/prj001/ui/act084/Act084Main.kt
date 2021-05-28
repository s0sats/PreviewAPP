package com.namoadigital.prj001.ui.act084

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.adapter.MyActionsAdapter
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.databinding.Act084MainBinding
import com.namoadigital.prj001.databinding.Act084MainContentBinding
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.model.MyActionsFormButton
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class Act084Main : Base_Activity(), Act084MainContract.I_View {
    private lateinit var binding: Act084MainContentBinding
    private lateinit var mAdapter: MyActionsAdapter
    private lateinit var bundle: Bundle
    private val mPresenter by lazy {
        Act084MainPresenter(
                context,
                this,
                bundle,
                mModule_Code,
                mResource_Code,
                TK_TicketDao(
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
                )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding = Act084MainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        setSupportActionBar(mainBinding.toolbar)
        binding = mainBinding.act084MainContent
        //
        initBundle(savedInstanceState)
        iniSetup()
        iniTrans()
        initVars()
        iniUIFooter()
        initActions()
    }

    private fun initBundle(savedInstanceState: Bundle?) {
        bundle = (savedInstanceState?: intent.extras?: Bundle()) as Bundle
    }

    private fun iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                ConstantBaseApp.ACT084
        )
    }

    private fun iniTrans() {
        hmAux_Trans = mPresenter.hmAuxTrans
    }

    private fun initVars() {
        setLabels()
    }

    private fun setLabels() {
        with(binding) {
            act084MketFilter.hint = hmAux_Trans["filter_hint"]
            act084TabMyActions.text = hmAux_Trans["tab_done_lbl"]
            act084TabOtherActions.text = hmAux_Trans["tab_discard_lbl"]
            act084TvNoResult.text = hmAux_Trans["no_record_lbl"]
        }
    }

    override fun iniRecycler() {
        val myActionsList = mPresenter.myActionsList
        if(myActionsList.size > 0) {
            binding.act084RvActionsList.visibility = View.GONE
            //
            mAdapter = MyActionsAdapter(
                    myActionsList,
                    this::onMyActionClick,
                    this::onFormButtonClick
            )
            //
            with(binding.act084RvActionsList) {
                layoutManager = LinearLayoutManager(context)
                adapter = mAdapter
                visibility = View.VISIBLE
            }
            //
            if(!binding.act084MketFilter.text.isNullOrEmpty()){
                applyTextFilter(binding.act084MketFilter.text.toString())
            }
        }else{
            with(binding){
                act084TvNoResult.visibility = View.VISIBLE
                act084RvActionsList.visibility = View.INVISIBLE
            }
        }
    }


    fun onMyActionClick(myAction: MyActions): Unit{
       // mPresenter.processActionClick(myAction)
    }
    private fun onFormButtonClick(myActionsFormButton: MyActionsFormButton) {
     //   mPresenter.processActionFormButtonClick(myActionsFormButton)
    }

    private fun applyTextFilter(text: String?) {
        if(::mAdapter.isInitialized){
            mAdapter.filter.filter(text)
        }

    }

    private fun iniUIFooter() {
        iniFooter()
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context)
        mAct_Info = Constant.ACT084
        mAct_Title = Constant.ACT084 + "_" + "title"
        //
        val mFooter = ToolBox_Inf.loadFooterSiteOperationInfo(context)
        mSite_Value = mFooter[Constant.FOOTER_SITE]
        mOperation_Value = mFooter[Constant.FOOTER_OPERATION]
        //
        setUILanguage(hmAux_Trans)
        setMenuLanguage(hmAux_Trans)
        setTitleLanguage()
        setFooter()
    }

    private fun initActions() {
        binding.act084MketFilter.setOnReportTextChangeListner(object : MKEditTextNM.IMKEditTextChangeText {
            override fun reportTextChange(text: String?) {
            }

            override fun reportTextChange(text: String?, p1: Boolean) {
                binding.act084RvActionsList.stopScroll()
                applyTextFilter(text)
            }
        })
        binding.act084Tabs.setOnCheckedChangeListener { _, checkedId ->
            binding.act084RvActionsList.stopScroll();
            with(binding){
                when(checkedId){
                    act084TabMyActions.id -> updateMyActionList(1,binding.act084ChkNcFilter.isChecked)
                    else -> updateMyActionList(0,binding.act084ChkNcFilter.isChecked)
                }
            }
        }
        binding.act084ChkNcFilter.setOnCheckedChangeListener{
            _, isChecked ->
            val doneTab = if(binding.act084Tabs.checkedRadioButtonId == binding.act084TabMyActions.id){
                1
            }else{
                0
            }
            updateMyActionList(doneTab,isChecked)
        }

    }
    private fun updateMyActionList(userFocusFilter: Int, ncFilterOn:Boolean) {
        //Reseta visibilidade das views
        with(binding){
            act084TvNoResult.visibility = View.GONE
            act084RvActionsList.visibility = View.GONE
        }
        changeProgressBarVisility(true)
        mPresenter.updateMyActionList(userFocusFilter, ncFilterOn)
    }

    override fun changeProgressBarVisility(show: Boolean) {
        with(binding.act084PbLoad){
            visibility = if(show) View.VISIBLE else View.GONE
        }
    }
}