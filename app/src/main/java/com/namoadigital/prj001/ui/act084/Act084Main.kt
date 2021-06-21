package com.namoadigital.prj001.ui.act084

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.adapter.MyActionsAdapter
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.databinding.Act084MainBinding
import com.namoadigital.prj001.databinding.Act084MainContentBinding
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.model.MyActionsFormButton
import com.namoadigital.prj001.ui.act005.Act005_Main
import com.namoadigital.prj001.ui.act011.Act011_Main
import com.namoadigital.prj001.ui.act038.Act038_Main
import com.namoadigital.prj001.ui.act070.Act070_Main
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.lang.Exception

class Act084Main : Base_Activity(), Act084MainContract.I_View {
    private lateinit var binding: Act084MainContentBinding
    private lateinit var mAdapter: MyActionsAdapter
    private lateinit var bundle: Bundle
    private var firstScroll = true
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
        //10/06/2021 - Add recolhimento do teclado
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    private fun iniTrans() {
        hmAux_Trans = mPresenter.hmAuxTrans
    }

    private fun initVars() {
        //LUCHE - 21/06/2021
        //Desabilita os cliques nas abas, pois só serão habilitado após corroutine retornar.
        toggleTabEnableStatus(false)
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

    /**
     * LUCHE - 11/06/2021
     * Fun chamada pelo presenter para atualizar o label da tab com o contador.
     */
    override fun setTabsCounters(selectedTabCounter: Int, otherTabCounter: Int) {
        with(binding){
            when(act084Tabs.checkedRadioButtonId){
                act084TabMyActions.id -> {
                    act084TabMyActions.text = hmAux_Trans["tab_done_lbl"].plus(" ($selectedTabCounter)")
                    act084TabOtherActions.text = hmAux_Trans["tab_discard_lbl"].plus(" ($otherTabCounter)")
                }
                else -> {
                    act084TabMyActions.text = hmAux_Trans["tab_done_lbl"].plus(" ($otherTabCounter)")
                    act084TabOtherActions.text = hmAux_Trans["tab_discard_lbl"].plus(" ($selectedTabCounter)")
                }
            }
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
                    this::onFormButtonClick,
                    this::onAdapterFilterApplied
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
            }else{
                scrollToLastSelectedItem()
            }
        }else{
            with(binding){
                act084TvNoResult.visibility = View.VISIBLE
                act084RvActionsList.visibility = View.INVISIBLE
            }
        }
    }


    fun onMyActionClick(myAction: MyActions){
        mPresenter.processActionClick(myAction)
    }
    private fun onFormButtonClick(myActionsFormButton: MyActionsFormButton) {
     //   mPresenter.processActionFormButtonClick(myActionsFormButton)
    }

    /**
     * Fun acionada pelo adapter como callback após finalizar a filtragem
     */
    private fun onAdapterFilterApplied(qtyItensFiltered: Int){
        if(qtyItensFiltered > 0){
            scrollToLastSelectedItem()
        }
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
                    val linearLayoutManager = binding.act084RvActionsList.layoutManager as LinearLayoutManager
                    val offset = ToolBox.dbToPixel(context,50)
                    linearLayoutManager.scrollToPositionWithOffset(actionPkPosition,offset)
                }catch (e: Exception){
                    binding.act084RvActionsList.scrollToPosition(
                            actionPkPosition
                    )
                }
            }
        }
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

    override fun footerCreateDialog() {
        //super.footerCreateDialog()
        ToolBox_Inf.buildFooterDialog(context)
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
        //
        toggleTabEnableStatus(!show)
    }

    /**
     * LUCHE - 21/06/2021
     * Fun que habilita de dasabilita as tabs.
     */
    private fun toggleTabEnableStatus(enable: Boolean) {
        with(binding) {
            act084TabMyActions.isEnabled = enable
            act084TabOtherActions.isEnabled = enable
        }
    }

    override fun getMketFilter(): String? {
        val textFilter = binding.act084MketFilter.text.toString()
        //
        return if(textFilter.isBlank() || textFilter.isEmpty()){
            null
        }else{
            textFilter
        }
    }

    override fun getCurrentTab(): Int {
        return when (binding.act084Tabs.checkedRadioButtonId){
            binding.act084TabMyActions.id -> 1
            else -> 0
        }
    }

    override fun getNcFilterStatus() : Boolean{
        return binding.act084ChkNcFilter.isChecked
    }

    /**
     * Fun que seta os params de filtro texto e aba recuperados do bundle
     */

    override fun setViewFiltersParam(mketFilter: String?, tabToLoad: Int, ncFilter: Boolean) {
        binding.act084MketFilter.setText(mketFilter)
        if(tabToLoad == 0){
            binding.act084TabOtherActions.performClick()
        }
        //
        if(ncFilter){
            binding.act084ChkNcFilter.isChecked = ncFilter
        }
    }


    override fun showMsg(ttl: String?, msg: String?) {
        ToolBox.alertMSG(
                context,
                ttl,
                msg,
                null,
                0
        )
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        callAct005()
    }

    private fun callAct005() {
        val mIntent = Intent(context, Act005_Main::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        mIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
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

    override fun callAct070(bundle: Bundle) {
        val mIntent = Intent(context, Act070_Main::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        mIntent.putExtras(bundle)
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
}