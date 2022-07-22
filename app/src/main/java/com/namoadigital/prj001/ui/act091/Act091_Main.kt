package com.namoadigital.prj001.ui.act091

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isVisible
import com.google.gson.Gson
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.adapter.Act085WorkgroupAddAdapter
import com.namoadigital.prj001.adapter.Act091_Item_Adapter
import com.namoadigital.prj001.databinding.Act091MainBinding
import com.namoadigital.prj001.model.Act091ServiceItem
import com.namoadigital.prj001.ui.act040.Act040_Main
import com.namoadigital.prj001.util.Constant

class Act091_Main : Base_Activity(), Act91_Contract.I_View {


    private val binding: Act091MainBinding by lazy {
        Act091MainBinding.inflate(layoutInflater)
    }

    private lateinit var mPresenter: Act091_Presenter
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

        mPresenter = Act091_Presenter()

        bundleSaved = intent?.extras


        //Starting
        initSetup()
        iniVars()
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
    }

    private fun initSetup(){
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