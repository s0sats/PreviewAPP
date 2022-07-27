package com.namoadigital.prj001.ui.act091

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.google.gson.Gson
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.adapter.Act091_Item_Adapter
import com.namoadigital.prj001.databinding.Act091MainBinding
import com.namoadigital.prj001.model.Act091ServiceItem
import com.namoadigital.prj001.ui.act040.Act040_Main
import com.namoadigital.prj001.ui.act091.bottomstate.Act091_BottomSheet
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf

class Act091_Main : Base_Activity(), Act91_Contract.I_View {


    private val binding: Act091MainBinding by lazy {
        Act091MainBinding.inflate(layoutInflater)
    }

    private val mPresenter: Act091_Presenter by lazy {
        Act091_Presenter(
            context,
            mModule_Code,
            mResource_Code,
            bundleSaved!!
        )
    }
    private val mAdapter: Act091_Item_Adapter? by lazy {
        Act091_Item_Adapter(
            mPresenter.getListData(),
            ::notifyFilterApplied,
            ::openBottomSheet
        )
    }

    private var bundleSaved: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.act091TopAppBar)
        bundleSaved = intent?.extras


        //Starting
        initSetup()
        initTrans()
        //Setar titulo da AppBar
        mAct_Title = "act091_title"
        setTitleLanguage()

        //Starting
        initVars()
        initRecyclerView()
        initAction()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun callAct040() {
        val mIntent = Intent(context, Act040_Main::class.java)
        bundleSaved?.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT091)
        mIntent.putExtras(bundleSaved!!)
        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(mIntent)
        finish()
    }


    override fun openBottomSheet(item: Act091ServiceItem) {
        Act091_BottomSheet.getInstance(Gson().toJson(item))
            .show(supportFragmentManager, "bottomSheet")
    }

    private fun initVars(){

        with(binding){
            act091TextLayout.hint = hmAux_Trans["filter_hint"]
            act091TextLayout.placeholderText = hmAux_Trans["insert_filter_placeholder"]
        }

    }

    private fun initSetup(){
        mResource_Code = ToolBox_Inf.getResourceCode(
            context,
            mModule_Code,
            ConstantBaseApp.ACT091
        )

        window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
        )
    }

    private fun initTrans(){
        hmAux_Trans = mPresenter.getTranslation()
    }

    private fun initAction(){

        binding.act091FilterText.setOnReportTextChangeListner(object : MKEditTextNM.IMKEditTextChangeText {
            override fun reportTextChange(text: String?) {
            }

            override fun reportTextChange(text: String?, p1: Boolean) {
                applyTextFilter(text)
            }
        })

    }

    private fun applyTextFilter(text: String?) = mAdapter?.filter?.filter(text)

    private fun initRecyclerView(){
        if(mPresenter.getListData().isNotEmpty()){
            with(binding.act091RecyclerView){
                adapter = mAdapter
            }
            return
        }
        binding.act091RecyclerView.visibility = View.GONE
        binding.act091WithoutList.text = hmAux_Trans["empty_list_lbl"]
        binding.act091WithoutList.visibility = View.VISIBLE

    }

    private fun notifyFilterApplied(size: Int){
        size.takeIf { it < 1 }.apply {

        }
        if(size < 1){
            binding.act091WithoutList.visibility = View.VISIBLE
            binding.act091RecyclerView.visibility = View.GONE
            return
        }
        binding.act091WithoutList.visibility = View.GONE
        binding.act091RecyclerView.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        callAct040()
    }


}