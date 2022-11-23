package com.namoadigital.prj001.ui.act092.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.databinding.Act092MainBinding
import com.namoadigital.prj001.model.MyActionFilterParam
import com.namoadigital.prj001.model.MyActionsBase
import com.namoadigital.prj001.service.WS_UnfocusAndHistoric
import com.namoadigital.prj001.ui.act005.Act005_Main
import com.namoadigital.prj001.ui.act092.Act092Presenter
import com.namoadigital.prj001.ui.act092.Act092_Contract
import com.namoadigital.prj001.ui.act092.ui.adapter.Act092_Adapter
import com.namoadigital.prj001.ui.act092.usecases.ActionUseCases.Companion.ActionUseCasesFactory
import com.namoadigital.prj001.ui.act092.utils.Act092UiEvent
import com.namoadigital.prj001.ui.base.BaseActivityMvp
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class Act092_Main : BaseActivityMvp
<Act092_Contract.Presenter,
        Act092MainBinding>(),
    Act092_Contract.View {
    private lateinit var bundle: Bundle

    private val _mainUserFilter = MutableStateFlow(false)
    private val serialCode by lazy{
        bundle.getLong(MD_Product_SerialDao.SERIAL_CODE)
    }

    override var wsProcess: MutableStateFlow<String> = MutableStateFlow("")

    override val binding: Act092MainBinding by lazy {
        Act092MainBinding.inflate(layoutInflater)
    }

    private var myActionFilterParam = MutableStateFlow(MyActionFilterParam())

    override val presenter: Act092Presenter by lazy {
        Act092Presenter(
            myActionFilterParam.value,
            bundle.getString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW, ConstantBaseApp.ACT005),
            hmAux_Trans,
            ActionUseCasesFactory(context).build()
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
        }
    }

    private fun getBundle() {
        val filterParam = bundle.getSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM)
        myActionFilterParam.value =
            filterParam?.let { it as MyActionFilterParam } ?: MyActionFilterParam()

    }

    override fun footerCreateDialog() {
        //super.footerCreateDialog();
        ToolBox_Inf.buildFooterDialog(context)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.act092_main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_info -> {
                showSnackbar("Em manutenção")
            }
        }
        return true
    }

    override fun initSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
            context,
            mModule_Code,
            ConstantBaseApp.ACT092
        )
    }

    override fun initTrans() {
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
                _mainUserFilter.value = !_mainUserFilter.value
                onState(Act092UiEvent.FilterMainUser)
            }

            btnOtherSerial.setOnClickListener {
                presenter.getUnfocusHistoricalList(context, serialCode)

            }

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

        } else if(wsProcess.value == WS_UnfocusAndHistoric.javaClass.simpleName){
            progressDialog.dismiss()
            onState(
                Act092UiEvent.OpenDialog(
                    title = "Atualizado",
                    message = "Atualizado_com_sucesso"
                )
            )
            presenter.getMyActionList(_mainUserFilter.value)
        }

        wsProcess.value = ""
        if (progressDialog.isShowing) progressDialog.dismiss()

    }

    override fun processCloseACT(mLink: String?, mRequired: String?, hmAux: HMAux?) {
        super.processCloseACT(mLink, mRequired, hmAux)
        if(wsProcess.value == WS_UnfocusAndHistoric.javaClass.simpleName){
            wsProcess.value = ""
            if (progressDialog.isShowing) progressDialog.dismiss()

            presenter.getMyActionList(_mainUserFilter.value)

        }
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
                    errorOrEmpty(state.isError)
                }

                is Act092UiEvent.ListingSerialSteels -> {
                    if (state.list.isEmpty()) {
                        onState(Act092UiEvent.EmptyOrError())
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

            emptyList.visibility = View.GONE
            progressLoading.visibility = View.GONE
            recyclerSerialList.apply {
                visibility = View.VISIBLE
                adapter = Act092_Adapter(
                    list,
                    hmAux_Trans,
                    {}
                )
                layoutManager =
                    LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }

        }
    }

    private fun toggleMainUserFilter(
        value: Boolean = _mainUserFilter.value
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
        title: String,
        message: String
    ) {
        when (isProcess) {
            true -> {
                enableProgressDialog(
                    hmAux_Trans[title],
                    hmAux_Trans[message],
                    hmAux_Trans["sys_alert_btn_cancel"],
                    hmAux_Trans["sys_alert_btn_ok"]
                )
            }
            false -> {
                ToolBox.alertMSG(
                    context,
                    hmAux_Trans[title],
                    hmAux_Trans[message],
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
        isError: Boolean
    ) {
        with(binding) {
            if (!isError) {
                emptyList.apply {
                    visibility = View.VISIBLE
                    text = /*hmAux_Trans["empty_list_lbl"]*/ "Nenhum serial encontrado."
                }
            }
            recyclerSerialList.visibility = View.GONE
            progressLoading.visibility = View.GONE
        }
    }

    override fun processError_1(mLink: String?, mRequired: String?) {
        super.processError_1(mLink, mRequired)
        progressDialog.dismiss()
    }

    override fun processCustom_error(mLink: String?, mRequired: String?) {
        super.processCustom_error(mLink, mRequired)
        progressDialog.dismiss()
    }

}