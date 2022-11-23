package com.namoadigital.prj001.ui.act092.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act092MainBinding
import com.namoadigital.prj001.model.MyActionFilterParam
import com.namoadigital.prj001.model.MyActionsBase
import com.namoadigital.prj001.service.WS_Sync
import com.namoadigital.prj001.service.WS_TK_Ticket_Download
import com.namoadigital.prj001.ui.act005.Act005_Main
import com.namoadigital.prj001.ui.act070.Act070_Main
import com.namoadigital.prj001.ui.act091.mvp.model.TranslateResource
import com.namoadigital.prj001.ui.act092.Act092Presenter
import com.namoadigital.prj001.ui.act092.Act092_Contract
import com.namoadigital.prj001.ui.act092.ui.adapter.Act092_Adapter
import com.namoadigital.prj001.ui.act092.usecases.ActionUseCases.Companion.ActionUseCasesFactory
import com.namoadigital.prj001.ui.act092.utils.Act092Translate
import com.namoadigital.prj001.ui.act092.utils.Act092UiEvent
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
    private lateinit var bundle: Bundle

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
            hmAux_Trans,
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
            mAct_Title = "act92_title"
            setTitleLanguage()
            getBundle()
            iniUIFooter(Constant.ACT092, hmAux_Trans)
        }

        initView {
            presenter.setView(this)
            presenter.getMyActionList(_focusState.value.mainUser)
        }
    }

    private fun getBundle() {
        val filterParam = bundle.getSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM)
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

            else -> {
                wsProcess.value = ""
                progressDialog.dismiss()
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

    override fun initVars() {
        with(binding) {
            serialTitle.text = myActionFilterParam.value.serialId
            productDescription.text = myActionFilterParam.value.productDesc
        }

    }

    override fun initAction() {
        with(binding) {
            mainUserSelection.setOnClickListener {
                _focusState.value = focusState.value.copy(
                    mainUser = !_focusState.value.mainUser
                )
                onState(Act092UiEvent.FilterMainUser)
            }

            btnOtherSerial.setOnClickListener {
                presenter.syncFiles(context)
            }


            btnCreateAction.setOnClickListener {
                presenter.processNewFormClick(context)
            }

            editSerialFilter.setOnReportTextChangeListner(object :
                MKEditTextNM.IMKEditTextChangeText {
                override fun reportTextChange(text: String?) {
                }

                override fun reportTextChange(text: String?, p1: Boolean) {
                    mAdapter.filter.filter(text)
                    filterText.value = text ?: ""
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

            onState(
                Act092UiEvent.OpenDialog(
                    title = hmAux_Trans[Act092Translate.DIALOG_UPDATE_TTL],
                    message = hmAux_Trans[Act092Translate.DIALOG_UPDATE_MSG]
                )
            )

            presenter.getMyActionList(_focusState.value.mainUser)
            return
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
                is Act092UiEvent.OpenDialog -> {
                    openDialog(
                        state.process,
                        state.title,
                        state.message
                    )
                }
                is Act092UiEvent.CallAct -> {
                    callAct(state.classe, state.bundle)
                }
            }
        }
    }

    private fun initRecyclerView(
        list: List<MyActionsBase>
    ) {
        with(binding) {


            mAdapter = Act092_Adapter(
                list,
                hmAux_Trans,
                {
                    presenter.processActionClick(it, context)
                },
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
        with(binding) {
            if (value) {
                mainUserSelection.setImageResource(R.drawable.ic_person_white_24dp)
                mainUserSelection.setBackgroundDrawable(resources.getDrawable(R.drawable.my_action_toogle_pressed))
            } else {
                mainUserSelection.setBackgroundDrawable(resources.getDrawable(R.drawable.my_action_toogle_default))
                mainUserSelection.setImageResource(R.drawable.ic_person_black_24dp)
            }
            presenter.getMyActionList(value)
        }
    }

    private fun openDialog(
        isProcess: Boolean,
        title: String?,
        message: String?
    ) {
        when (isProcess) {
            true -> {
                enableProgressDialog(
                    title,
                    message,
                    hmAux_Trans["sys_alert_btn_cancel"],
                    hmAux_Trans["sys_alert_btn_ok"]
                )
            }
            false -> {
                ToolBox.alertMSG(
                    context,
                    title,
                    message,
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