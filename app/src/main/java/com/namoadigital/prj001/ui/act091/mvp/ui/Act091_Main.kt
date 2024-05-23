package com.namoadigital.prj001.ui.act091.mvp.ui

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.adapter.Act091_Item_Adapter
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao
import com.namoadigital.prj001.databinding.Act091MainBinding
import com.namoadigital.prj001.model.TranslateResource
import com.namoadigital.prj001.ui.act040.Act040_Main
import com.namoadigital.prj001.ui.act091.bottomstate.Act091_BottomSheet
import com.namoadigital.prj001.ui.act091.mvp.Act091_Contract
import com.namoadigital.prj001.ui.act091.mvp.Act091_Presenter
import com.namoadigital.prj001.ui.act091.mvp.Utils.Act091_Translate
import com.namoadigital.prj001.ui.act091.mvp.Utils.onHide
import com.namoadigital.prj001.ui.act091.mvp.Utils.onVisible
import com.namoadigital.prj001.ui.act091.mvp.model.Act091State
import com.namoadigital.prj001.ui.base.BaseActivityMvp
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf

class Act091_Main : BaseActivityMvp<Act091_Contract.I_Presenter, Act091MainBinding>(),
    Act091_Contract.I_View {


    private var bundleSaved: Bundle? = null

    private val mAdapter: Act091_Item_Adapter by lazy {
        Act091_Item_Adapter(
            presenter.getListData(),
            presenter.hasPermissionShowPrice(),
            hmAux_Trans,
            { size -> onEvent(Act091EventUI.CheckSizeList(size)) },
            { presenter.openBottomSheet(it) })
    }

    override val presenter: Act091_Contract.I_Presenter by lazy {
        Act091_Presenter(
            Act091State(bundleSaved),
            TranslateResource(
                context,
                mModule_Code,
                mResource_Code
            )
        )
    }

    override val binding: Act091MainBinding by lazy {  Act091MainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(binding) {
            setContentView(root)
            setSupportActionBar(act091TopAppBar)
        }
        bundleSaved = intent?.extras

        initView {
            presenter.setView(this)
            mAct_Title = Act091_Translate.ACT_TITLE
            setTitleLanguage()
            initRecyclerView()
        }

    }

    private fun initRecyclerView(){
        with(binding) {
            if (presenter.getListData().isNotEmpty()) {
                act091RecyclerView.apply {
                    visibility = View.VISIBLE
                    adapter = mAdapter
                }
                return
            }

            act091RecyclerView.onHide()
            act091WithoutList.onVisible()
        }
    }

    override fun initSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
            context,
            mModule_Code,
            ConstantBaseApp.ACT091
        )
    }

    override fun initTrans() {
        hmAux_Trans = presenter.getTranslation()
    }

    override fun initVars() {
        with(binding){
            act091WithoutList.text = hmAux_Trans[Act091_Translate.EMPTY_LIST]
            act091TextLayout.hint = hmAux_Trans[Act091_Translate.FILTER_HINT]
            act091TextLayout.placeholderText = hmAux_Trans[Act091_Translate.FILTER_PLACEHOLDER]
        }
    }

    override fun initAction() {
        binding.act091FilterText.setOnReportTextChangeListner(object : MKEditTextNM.IMKEditTextChangeText {
            override fun reportTextChange(text: String?) {
            }

            override fun reportTextChange(text: String?, p1: Boolean) {
                applyTextFilter(text)
            }
        })
    }


    override fun onEvent(state: Act091EventUI) {
        when (state) {
            is Act091EventUI.OpenBottomSheet -> {
                Act091_BottomSheet.getInstance(state.localJson, state.updatePackageServices).apply {
                    onAddServices = { item ->
                        presenter.savePackServices(item)
                    }
                }.show(supportFragmentManager, "bottomSheet")
            }

            is Act091EventUI.ShowAlertDialogOk -> {
                showAlertDialogWithOK(
                    state.title,
                    state.msg,
                    state.positiveButton)
            }

            is Act091EventUI.CheckSizeList -> {
                with(binding) {
                    if (state.size == 0) {
                        act091WithoutList.onVisible()
                        act091RecyclerView.onHide()
                        return
                    }
                    act091WithoutList.onHide()
                    act091RecyclerView.onVisible()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        callAct040(-1)
    }

    override fun callAct040(expressTmp: Long) {
        Intent(context, Act040_Main::class.java).also {
            bundleSaved?.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT091)

            if(expressTmp > 0){
                bundleSaved?.putLong(SO_Pack_Express_LocalDao.EXPRESS_TMP, expressTmp)
                bundleSaved?.putBoolean(Act040_Main.HAS_SERVICE_ADDED, true)
            }

            it.putExtras(bundleSaved!!)
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(it)
            finish()
        }
    }

    private fun showAlertDialogWithOK(
        title: String,
        msg: String,
        onPositiveButton: (DialogInterface) -> Unit,
        showNegativeButton: Int = 0,
    ){
        ToolBox.alertMSG(
            context,
            title,
            msg,
            { dialog, _ ->
                onPositiveButton(dialog)
            },
            showNegativeButton
        )
    }

    private fun applyTextFilter(text: String?) = mAdapter?.filter?.filter(text)


    private fun notifyFilterApplied(size: Int) {
        with(binding) {
            if (size == 0) {
                act091WithoutList.onVisible()
                act091RecyclerView.onHide()
                return
            }
            act091WithoutList.onHide()
            act091RecyclerView.onVisible()
        }
    }
}