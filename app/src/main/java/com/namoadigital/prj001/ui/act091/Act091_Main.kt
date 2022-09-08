/*
package com.namoadigital.prj001.ui.act091

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.adapter.Act091_Item_Adapter
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao
import com.namoadigital.prj001.databinding.Act091MainBinding
import com.namoadigital.prj001.model.SO_Pack_Express_Local
import com.namoadigital.prj001.model.SoPackExpressPacksLocal
import com.namoadigital.prj001.model.TSO_Service_Search_Obj
import com.namoadigital.prj001.ui.act040.Act040_Main
import com.namoadigital.prj001.ui.act091.bottomstate.Act091_BottomSheet
import com.namoadigital.prj001.ui.act091.mvp.Act091_Contract
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf

class Act091_MainS : Base_Activity(), Act091_Contract.I_View {


    private val binding: Act091MainBinding by lazy {
        Act091MainBinding.inflate(layoutInflater)
    }

    private val mPresenter: Act091_Presenter by lazy {
        Act091_Presenter(
            context,
            this,
            mModule_Code,
            mResource_Code,
            bundleSaved!!
        )
    }
    private val mAdapter: Act091_Item_Adapter? by lazy {
        Act091_Item_Adapter(
            mPresenter.getListData(),
            showPrice(),
            hmAux_Trans,
            ::notifyFilterApplied,
            ::openBottomSheet
        )
    }

    private fun showPrice() = ToolBox_Inf.profileExists(
        context,
        Constant.PROFILE_MENU_SO,
        Constant.PROFILE_MENU_SO_SHOW_SERVICE_PRICE
    )

    private val soPackExpressLocal: SO_Pack_Express_Local? by lazy {
        mPresenter.getSO_Pack_Express_Local()
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

    override fun callAct040(expressTmp: Long) {
        val mIntent = Intent(context, Act040_Main::class.java)
        bundleSaved?.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT091)
        if(expressTmp>0) {
            bundleSaved?.putLong(SO_Pack_Express_LocalDao.EXPRESS_TMP, expressTmp)
            bundleSaved?.putBoolean(Act040_Main.HAS_SERVICE_ADDED, true)
        }
        mIntent.putExtras(bundleSaved!!)
        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(mIntent)
        finish()
    }


    override fun openBottomSheet(item: TSO_Service_Search_Obj) {

        soPackExpressLocal?.let {
            SoPackExpressPacksLocal(context, item, it, -1).let { local ->
                Act091_BottomSheet.getInstance(Gson().toJson(local), false).apply {
                    onAddServices = { item -> mPresenter.savePackServices(item) }
                }.show(supportFragmentManager, "bottomSheet")
            }
        } ?: ToolBox.alertMSG(
            context,
            "alert_error_ttl", //Error
            "alert_error_msg", //Ocorreu um erro durante a passagem de parâmetros, volte para tela de OS Expressa e tente novamente.
            {dialog, _ ->
                dialog.dismiss()
            }, 0
        )
    }

*/
