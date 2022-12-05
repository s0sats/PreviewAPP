package com.namoadigital.prj001.ui.act092.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.databinding.Act092MainBinding
import com.namoadigital.prj001.model.MyActionFilterParam
import com.namoadigital.prj001.model.MyActionsBase
import com.namoadigital.prj001.service.*
import com.namoadigital.prj001.ui.act005.Act005_Main
import com.namoadigital.prj001.ui.act070.Act070_Main
import com.namoadigital.prj001.ui.act091.mvp.model.TranslateResource
import com.namoadigital.prj001.ui.act092.Act092Presenter
import com.namoadigital.prj001.ui.act092.Act092_Contract
import com.namoadigital.prj001.ui.act092.ui.adapter.Act092_Adapter
import com.namoadigital.prj001.ui.act092.usecases.ActionUseCases.Companion.ActionUseCasesFactory
import com.namoadigital.prj001.ui.act092.utils.Act092Translate
import com.namoadigital.prj001.ui.act092.utils.Act092UiEvent
import com.namoadigital.prj001.ui.act092.utils.Act092UiEvent.OpenDialog
import com.namoadigital.prj001.ui.act092.utils.Act092UiEvent.OpenDialog.DialogType
import com.namoadigital.prj001.ui.act092.utils.FilterFocusUser
import com.namoadigital.prj001.ui.base.BaseActivityMvp
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class Act092_Main : BaseActivityMvp
<Act092_Contract.Presenter,
        Act092MainBinding>(),
    Act092_Contract.View {
    override lateinit var bundle: Bundle
    override fun showPD(ttl: String?, msg: String?) {
        enableProgressDialog(
            ttl,
            msg,
            hmAux_Trans["sys_alert_btn_cancel"],
            hmAux_Trans["sys_alert_btn_ok"]
        )
    }

    override fun disablePD() {
        progressDialog.dismiss()
    }

    override fun setItemAsDownloaded(position: Int) {
        mAdapter.notifyItemChanged(position)
    }

    private val _mainUserFilter = MutableStateFlow(false)

    private val _focusState = MutableStateFlow(FilterFocusUser())
    override val focusState: StateFlow<FilterFocusUser> = _focusState

    private var hmAuxTicketDownload: HMAux = HMAux()

    override var wsProcess: MutableStateFlow<String> = MutableStateFlow("")

    override val filterText: MutableStateFlow<String> = MutableStateFlow("")

    override val binding: Act092MainBinding by lazy {
        Act092MainBinding.inflate(layoutInflater)
    }

    private var myActionFilterParam = MutableStateFlow(MyActionFilterParam())

    private lateinit var mAdapter: Act092_Adapter


    override val presenter: Act092Presenter by lazy {
        Act092Presenter(
            myActionFilterParam.value,
            bundle.getString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW, ConstantBaseApp.ACT005),
            bundle.getString(MD_Product_SerialDao.CLASS_COLOR, ""),
            ActionUseCasesFactory(context).build(),
            TranslateResource(
                context,
                mModule_Code,
                mResource_Code
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(binding) {
            setContentView(root)
            bundle = (savedInstanceState ?: intent.extras) as Bundle
            setSupportActionBar(topAppBar)
            setTitleLanguage()
            mAct_Title = "act92_title"
            getBundle()
            iniUIFooter(Constant.ACT092, hmAux_Trans)
        }

        initView {
            presenter.setView(this)
            presenter.getMyActionList(_focusState.value.mainUser)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun getBundle() {
        val filterParam = bundle.getSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM_ACT092)
        myActionFilterParam.value =
            filterParam?.let { it as MyActionFilterParam } ?: MyActionFilterParam()

    }


    override fun processError_1(mLink: String?, mRequired: String?) {
        super.processError_1(mLink, mRequired)
        presenter.newActionClick = false
        progressDialog.dismiss()
    }

    override fun processCustom_error(mLink: String?, mRequired: String?) {
        super.processCustom_error(mLink, mRequired)
        presenter.newActionClick = false
        progressDialog.dismiss()
    }


    override fun processCloseACT(mLink: String?, mRequired: String?, hmAux: HMAux) {
        when (wsProcess.value) {
            WS_TK_Ticket_Download::class.java.name -> {
                wsProcess.value = ""
                progressDialog.dismiss()
                if (presenter.verifyProductOutdateForForm(hmAux, context)) {

                    if (ToolBox_Con.isOnline(context)) {
                        hmAuxTicketDownload = hmAux
                        presenter.syncFilesForm()
                    }
                }
                callAct(
                    Act070_Main::class.java,
                    presenter.getCacheTicketBundle(hmAux)
                )
            }

            WS_Sync::class.java.name -> {
                wsProcess.value = ""
                progressDialog.dismiss()
                //
                presenter.processWsReturnSync(hmAuxTicketDownload)
            }

            WS_Save::class.java.simpleName -> {
                wsProcess.value = ""
                progressDialog.dismiss()
                //
                presenter.otherActionFlow(context)
            }

            WS_TK_Ticket_Save::class.java.simpleName -> {
                wsProcess.value = ""
                progressDialog.dismiss()
                //
                presenter.otherActionFlow(context)
            }

            WS_UnfocusAndHistoric::class.java.simpleName -> {
                wsProcess.value = ""
                if (progressDialog.isShowing) progressDialog.dismiss()
                if(hmAux.get(WS_UnfocusAndHistoric.RESULT_LIST_SIZE).equals("0")){
                    ToolBox.toastMSG(context, hmAux_Trans[Act092Translate.DIALOG_OTHER_ACTIONS_EMPTY_LIST_MSG])
                }else {
                    ToolBox.toastMSG(context, hmAux_Trans[Act092Translate.DIALOG_UPDATE_MSG])
                    presenter.getMyActionList()
                }
                return
            }

            WS_Serial_Search::class.java.name -> {
                wsProcess.value = ""
                progressDialog.dismiss()
                presenter.extractSearchResult(mLink, presenter.getActionSelected())
            }

            else -> {
                wsProcess.value = ""
                if (progressDialog.isShowing) progressDialog.dismiss()
            }
        }
    }

    override fun footerCreateDialog() {
        //super.footerCreateDialog();
        ToolBox_Inf.buildFooterDialog(context)
    }


    override fun initSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
            context,
            mModule_Code,
            ConstantBaseApp.ACT092
        )
    }

    override fun initTrans() {
        hmAux_Trans = presenter.getTranslation()
    }

    private fun updateTitleActionSerial() {
        with(binding) {
            val color = presenter.serialModel.value.classColor
            if (color.isNotEmpty()) {
                iconClassColor.setColorFilter(Color.parseColor(color))
            } else {
                iconClassColor.visibility = View.GONE
            }
            serialTitle.text = presenter.serialModel.value.serialId
            productDescription.text = presenter.serialModel.value.productDesc
        }
    }

    override fun initVars() {

    }


    override fun initAction() {
        with(binding) {


            mainUserSelection.setOnClickListener {
                if (btnOtherSerial.isEnabled) {
                    _focusState.value = focusState.value.copy(
                        mainUser = !_focusState.value.mainUser
                    )
                    onState(Act092UiEvent.FilterMainUser)
                }
            }

            btnOtherSerial.setOnClickListener {
                if (btnOtherSerial.isEnabled) {
                    presenter.otherActionFlow(context)
                }

            }
            btnCreateAction.setOnClickListener {
                presenter.processNewFormClick(context)
            }

            editSerialFilter.setOnReportTextChangeListner(object :
                MKEditTextNM.IMKEditTextChangeText {
                override fun reportTextChange(text: String?) {
                }

                override fun reportTextChange(text: String?, p1: Boolean) {
                    if (editSerialFilter.isEnabled) {
                        mAdapter.filter.filter(text)
                        filterText.value = text ?: ""
                    }
                }
            })
        }
    }

    override fun processCloseACT(mLink: String?, mRequired: String?) {
        super.processCloseACT(mLink, mRequired)

        if (wsProcess.value == Act005_Main.WS_PROCESS_SYNC) {
            wsProcess.value = ""
            progressDialog.dismiss()
            //LUCHE - 30/06/2020
            //Substituido o metodo antigo pelo metodo que agenda todos os workers.
            ToolBox_Inf.scheduleAllDownloadWorkers(context)
            ToolBox_Inf.updateUserCustomerSync(
                context,
                ToolBox_Con.getPreference_Customer_Code(context).toString(),
                ToolBox_Con.getPreference_User_Code(applicationContext),
                0
            )

        }

        processCloseACT(mLink, mRequired, HMAux())

    }


    override fun onBackPressed() {
        //super.onBackPressed()
        presenter.onBackPressedClicked(bundle)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onState(state: Act092UiEvent) {
        CoroutineScope(Dispatchers.Main).launch {
            when (state) {
                is Act092UiEvent.UpdateTitleActionSerial -> {
                    updateTitleActionSerial()
                }

                is Act092UiEvent.IsLoading -> {
                    isLoading(state.isLoading, state.message)
                }

                is Act092UiEvent.EmptyOrError -> {
                    errorOrEmpty(
                        state.sizeList
                    )
                }

                is Act092UiEvent.ListingSerialSteels -> {
                    if (state.list.isEmpty()) {
                        onState(Act092UiEvent.EmptyOrError(sizeList = state.list.size))
                        return@launch
                    }
                    initRecyclerView(state.list)

                }

                is Act092UiEvent.ShowSnackbar -> {
                    showSnackbar(state.message)
                }

                is Act092UiEvent.FilterMainUser -> {
                    toggleMainUserFilter()
                }
                is OpenDialog -> {
                    openDialog(state.dialogType)
                }
                is Act092UiEvent.CallAct -> {
                    callAct(state.classe, state.bundle)
                }

                is Act092UiEvent.CheckIfFileExists -> {
                    disableMainAndOtherActions(state.exists)
                }

                is Act092UiEvent.UpdateFooterInfos -> {
                    updateFooterInfos()
                }

                is Act092UiEvent.CallActForResult -> {
                    startActivityForResult(Intent(context, state.classe).also {
                        it.putExtras(state.bundle ?: Bundle())
                    }, state.code)
                }
            }
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

    private fun processChanceZoneResult(code: Int) {
        if (code == RESULT_OK) {
            updateFooterInfos()
            presenter.getActionSelected()?.let {
                presenter.checkScheduleFlow(it)
            }
        } else {
            ToolBox_Con.setPreference_Site_Code(context, presenter.serialModel.value.siteCode)
            ToolBox_Con.setPreference_Zone_Code(context, presenter.serialModel.value.zoneCodeBack)
        }
    }

    private fun updateFooterInfos() {
        mAdapter.notifyDataSetChanged()
        iniUIFooter(Constant.ACT092, hmAux_Trans)
    }

    private fun disableMainAndOtherActions(isEmpty: Boolean) {

        val btnBackground =
            if (!isEmpty) ColorStateList.valueOf(resources.getColor(R.color.padrao_TRANSPARENT))
            else ColorStateList.valueOf(resources.getColor(R.color.m3_namoa_inverseSurface))

        val mainUserCircle =
            if (!isEmpty) resources.getDrawable(R.drawable.my_action_toogle_default) else resources.getDrawable(
                R.drawable.my_action_toogle_disable
            )
        val mainUserPerson =
            if (isEmpty) R.drawable.ic_person_disable_24dp
            else R.drawable.ic_person_black_24dp

        val textColor =
            if (!isEmpty) resources.getColor(R.color.m3_namoa_inverseSurface) else resources.getColor(
                R.color.m3_namoa_surface
            )

        with(binding) {
            btnOtherSerial.apply {
                backgroundTintList = btnBackground
                isEnabled = !isEmpty
                iconTint = ColorStateList.valueOf(textColor)
                setTextColor(textColor)
                strokeWidth = if (!isEmpty) 1 else 0
                isClickable = !isEmpty
            }

            mainUserSelection.apply {
                isEnabled = !isEmpty
                background = mainUserCircle
                setImageResource(mainUserPerson)
                isClickable = !isEmpty
            }

            mainUserSelection.isEnabled = !isEmpty

        }
    }

    private fun initRecyclerView(
        list: List<MyActionsBase>
    ) {
        with(binding) {

            mAdapter = Act092_Adapter(
                list,
                hmAux_Trans,
                myActionClickListener = { myAction, position -> presenter.processActionClick(myAction, context, position) },
                {
                    onState(Act092UiEvent.EmptyOrError(sizeList = it))
                }
            )

            emptyList.visibility = View.GONE
            progressLoading.visibility = View.GONE
            recyclerSerialList.apply {
                visibility = View.VISIBLE
                adapter = mAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }

        }
    }

    private fun toggleMainUserFilter(
        value: Boolean = _focusState.value.mainUser
    ) {
        with(binding.mainUserSelection) {
            if (value) {
                setImageResource(R.drawable.ic_person_white_24dp)
                background = resources.getDrawable(R.drawable.my_action_toogle_pressed)
            } else {
                background = resources.getDrawable(R.drawable.my_action_toogle_default)
                setImageResource(R.drawable.ic_person_black_24dp)
            }
        }
        presenter.getMyActionList(value)
    }

    private fun openDialog(
        dialogType: DialogType,
    ) {
        when (dialogType) {
            is DialogType.PROCESS -> {
                enableProgressDialog(
                    hmAux_Trans[dialogType.title],
                    hmAux_Trans[dialogType.message],
                    hmAux_Trans["sys_alert_btn_cancel"],
                    hmAux_Trans["sys_alert_btn_ok"]
                )
            }

            is DialogType.ACTION -> {
                ToolBox.alertMSG(
                    context,
                    hmAux_Trans[dialogType.title],
                    hmAux_Trans[dialogType.message],
                    dialogType.action,
                    dialogType.negativeBtn
                )
            }

            is DialogType.DEFAULT_OK -> {
                ToolBox.alertMSG(
                    context,
                    hmAux_Trans[dialogType.title],
                    hmAux_Trans[dialogType.message],
                    { dialog, _ ->
                        dialog.dismiss()
                    }, 0
                )
            }
        }

    }

    private fun isLoading(
        isLoading: Boolean,
        message: String
    ) {


        val progressVisible = if (isLoading) View.VISIBLE else View.GONE
        val itemsVisible = if (isLoading) View.GONE else View.VISIBLE

        with(binding) {
            mainUserSelection.isEnabled = !isLoading
            btnOtherSerial.isEnabled = !isLoading
            editSerialFilter.isEnabled = !isLoading
            //progress
            progressLoading.visibility = progressVisible
            //
            recyclerSerialList.visibility = itemsVisible
            emptyList.visibility = itemsVisible
        }
    }

    private fun errorOrEmpty(
        sizeList: Int
    ) {
        with(binding) {
            emptyList.apply {
                visibility = if (sizeList == 0) View.VISIBLE else View.GONE
                text = hmAux_Trans[Act092Translate.EMPTY_LIST]
            }
            recyclerSerialList.visibility = if (sizeList >= 1) View.VISIBLE else View.GONE
            progressLoading.visibility = View.GONE
        }
    }

}