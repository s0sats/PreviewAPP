package com.namoadigital.prj001.ui.act094.ui

import android.R
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.namoa_digital.namoa_library.ctls.MKEditTextNM.IMKEditTextChangeText
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.adapter.SelectDestinationAdapter
import com.namoadigital.prj001.core.data.domain.model.SiteInventory
import com.namoadigital.prj001.core.data.domain.usecase.serial.site.inventory.SerialSiteInventoryUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.destination.DestinationUseCase
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.dao.trip.FsTripDestinationDao
import com.namoadigital.prj001.databinding.Act094MainBinding
import com.namoadigital.prj001.databinding.DestinationDialogFilterBinding
import com.namoadigital.prj001.extensions.fromJsonToList
import com.namoadigital.prj001.extensions.logout
import com.namoadigital.prj001.model.MyActionFilterParam
import com.namoadigital.prj001.model.MyActionFilterParam.Companion.toActionFilter
import com.namoadigital.prj001.model.TranslateResource
import com.namoadigital.prj001.service.WS_TK_Ticket_Download
import com.namoadigital.prj001.service.trip.WsAvailablesDestinations
import com.namoadigital.prj001.service.trip.WsSelectDestination
import com.namoadigital.prj001.ui.act005.Act005_Main
import com.namoadigital.prj001.ui.act083.Act083_Main
import com.namoadigital.prj001.ui.act083.data.local.preferences.MyActionsFilterParamPreferences
import com.namoadigital.prj001.ui.act094.Act094Presenter
import com.namoadigital.prj001.ui.act094.Contract
import com.namoadigital.prj001.ui.act094.destination.domain.destination_availables.AvailableDestinationFilter
import com.namoadigital.prj001.ui.act094.destination.domain.destination_availables.DestinationAvailables
import com.namoadigital.prj001.ui.act094.domain.model.SelectionDestinationAvailable
import com.namoadigital.prj001.ui.act094.domain.toAdapterList
import com.namoadigital.prj001.ui.act094.util.Act094Translate
import com.namoadigital.prj001.ui.act094.util.Act094Translate.ALERT_DESTINATION_SELECTED_MSG
import com.namoadigital.prj001.ui.act094.util.Act094Translate.ALERT_DESTINATION_TICKET_DOWNLOADED_ERROR_MSG
import com.namoadigital.prj001.ui.act094.util.Act094Translate.ALERT_DESTINATION_TICKET_DOWNLOADED_ERROR_TTL
import com.namoadigital.prj001.ui.act094.util.Act094Translate.ALERT_DESTINATION_TICKET_DOWNLOADED_MSG
import com.namoadigital.prj001.ui.act094.util.Act094Translate.DIALOG_FILTER_APPLY
import com.namoadigital.prj001.ui.act094.util.Act094Translate.DIALOG_FILTER_CANCEL
import com.namoadigital.prj001.ui.act094.util.Act094Translate.DIALOG_FILTER_HIDE_OS_PREVENTIVE
import com.namoadigital.prj001.ui.act094.util.Act094Translate.DIALOG_FILTER_SHOW_PLANNED
import com.namoadigital.prj001.ui.act094.util.Act094Translate.DIALOG_FILTER_SHOW_TODAY
import com.namoadigital.prj001.ui.act094.util.Act094Translate.DIALOG_FILTER_SHOW_URGENT
import com.namoadigital.prj001.ui.act094.util.Act094Translate.DIALOG_FILTER_TITLE
import com.namoadigital.prj001.ui.act094.util.Act094Translate.EDIT_TEXT_FILTER
import com.namoadigital.prj001.ui.act094.util.SelectDestinationUiEvent
import com.namoadigital.prj001.ui.act094.util.SelectDestinationUiEvent.OpenDialog.DialogType
import com.namoadigital.prj001.ui.base.BaseActivityMvp
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import com.namoadigital.prj001.view.dialog.DestinationDetailDialog


class Act094_Main : BaseActivityMvp<Contract.Presenter, Act094MainBinding>(), Contract.View {

    override val binding by lazy {
        Act094MainBinding.inflate(layoutInflater)
    }

    override var wsProcess: String = ""

    override val presenter by lazy {
        Act094Presenter(
            TranslateResource(
                context,
                mModule_Code,
                mResource_Code
            ),
            DestinationUseCase.selectDestinationUseCase(context),
            FSTripDao(context),
            FsTripDestinationDao(context),
            TK_TicketDao(context)
        )
    }
    var selectedDestination: SelectionDestinationAvailable? = null
    private lateinit var serialSiteInventoryUseCase: SerialSiteInventoryUseCase

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                callAct005()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        serialSiteInventoryUseCase =
            SerialSiteInventoryUseCase.Companion.SiteInventoryUseCaseFactory(this)
                .savePreferenceAndDeleteFileUseCase()
        initView()
        presenter.setView(this)
        presenter.getListDestinationAvailable()
    }

    override fun processCloseACT(mLink: String?, mRequired: String?, hmAux: HMAux?) {
        super.processCloseACT(mLink, mRequired, hmAux)

        when (wsProcess) {
            WsAvailablesDestinations.NAME -> {
                wsProcess = ""
                val list = mLink?.fromJsonToList<DestinationAvailables>()?.map {
                    it.toAdapterList()
                }
                onEvent(SelectDestinationUiEvent.ListingDestinations(list))
                progressDialog.dismiss()
            }

            WsSelectDestination.NAME -> {
                wsProcess = ""
                progressDialog.dismiss()
                Toast.makeText(
                    context,
                    hmAux_Trans[ALERT_DESTINATION_SELECTED_MSG],
                    Toast.LENGTH_SHORT
                ).show()
                selectedDestination?.let {
                    presenter.saveDestination(context, mLink, it)
                }
            }

            WS_TK_Ticket_Download::class.java.name -> {
                wsProcess = ""
                progressDialog.dismiss()
                Toast.makeText(
                    context,
                    hmAux_Trans[ALERT_DESTINATION_TICKET_DOWNLOADED_MSG],
                    Toast.LENGTH_SHORT
                ).show()
                callAct005()
            }
        }
    }

    override fun processLogin() {
        super.processLogin()
        progressDialog.dismiss()
        logout()
    }

    override fun processError_1(mLink: String?, mRequired: String?) {
        super.processError_1(mLink, mRequired)
        progressDialog?.dismiss()
        if (wsProcess == WS_TK_Ticket_Download::class.java.name) {
            ToolBox.alertMSG(
                context,
                hmAux_Trans[ALERT_DESTINATION_TICKET_DOWNLOADED_ERROR_TTL],
                hmAux_Trans[ALERT_DESTINATION_TICKET_DOWNLOADED_ERROR_MSG],
                { dialogInterface, i ->
                    dialogInterface.dismiss()
                    callAct005()
                },
                0
            )
        } else if (wsProcess == WsSelectDestination::class.java.name) {
            wsProcess = ""
            progressDialog.dismiss()
            Toast.makeText(
                context,
                hmAux_Trans[ALERT_DESTINATION_SELECTED_MSG],
                Toast.LENGTH_SHORT
            ).show()
            selectedDestination?.let {
                presenter.saveDestination(context, mLink?.ifEmpty { null }, it, true)
            }
        }
    }

    override fun onEvent(state: SelectDestinationUiEvent) {
        when (state) {

            is SelectDestinationUiEvent.ListingDestinations -> {
//                changeBadgeVisibility()
                loadingState(false)
                state.list?.takeIf { it.isNotEmpty() }?.let { initRecyclerView(it) }
                    ?: checkSizeDestinationList()
            }

            is SelectDestinationUiEvent.Loading -> {
                loadingState(true)
            }

            is SelectDestinationUiEvent.OpenDialog -> {
                openDialog(state.dialogType)
            }

        }
    }

    private fun loadingState(show: Boolean) {
        with(binding) {
            if (show) {
                progressBar.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                emptyList.visibility = View.GONE
                edittextFilter.isEnabled = false
            } else {
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                emptyList.visibility = View.GONE
                edittextFilter.isEnabled = true
            }
        }
    }

    private fun openDialog(dialog: DialogType) {
        when (dialog) {

            is DialogType.PROCESS -> {
                enableProgressDialog(
                    hmAux_Trans[dialog.title],
                    hmAux_Trans[dialog.message],
                    hmAux_Trans["sys_alert_btn_cancel"],
                    hmAux_Trans["sys_alert_btn_ok"]
                )
            }

            is DialogType.DEFAULT_OK -> {
                showMsg(dialog.title, dialog.message)
            }

            is DialogType.ACTION -> {
                showDialogAction(dialog)
            }

            else -> {}
        }
    }

    private fun showDialogAction(dialog: DialogType.ACTION) {
        ToolBox.alertMSG(
            context,
            hmAux_Trans[dialog.title],
            hmAux_Trans[dialog.message],
            dialog.action,
            dialog.negativeBtn
        )
    }

    fun showMsg(ttl: String?, msg: String?) {
        ToolBox.alertMSG(
            context,
            ttl,
            msg,
            null,
            0
        )
    }


    private lateinit var mAdapter: SelectDestinationAdapter
    private fun initRecyclerView(
        list: List<SelectionDestinationAvailable>
    ) {

        with(binding) {

            mAdapter = SelectDestinationAdapter(
                list,
                onSelectItem = { item ->
                    selectedDestination = item
                    execSelectionItem(item)
                },
                onDetailItem = {
                    DestinationDetailDialog(
                        context,
                        it,
                    ).show()
                },
                notifyFilterApplied = { size ->
                    checkSizeDestinationList(size)
                },
                goToAct083 = this@Act094_Main::goToAct083
            )

            recyclerView.apply {
                adapter = mAdapter
                layoutManager = LinearLayoutManager(context)
                visibility = View.VISIBLE
            }

            emptyList.visibility = View.GONE
        }

    }

    private fun goToAct083(item: SelectionDestinationAvailable) {
        serialSiteInventoryUseCase.deleteFile?.invoke()
        serialSiteInventoryUseCase.savePreference?.invoke(
            SiteInventory(
                item.siteCode ?: -1,
                item.siteDesc ?: "",
                true
            )
        )
        val actionFilterParam = MyActionFilterParam(
            siteCode = "${item.siteCode}"
        )
        //
        actionFilterParam.paramItemSelectedTab = 1
        val sharedPreferences = MyActionsFilterParamPreferences(
            getSharedPreferences("act083_filter", MODE_PRIVATE)
        )
        sharedPreferences.clear()
        sharedPreferences.write(actionFilterParam.toActionFilter())
        //
        Intent(context, Act083_Main::class.java).also { intent ->
            Bundle().apply {
                this.putString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW, ConstantBaseApp.ACT094)
                this.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM, actionFilterParam)
                this.putString(SELECT_DESTINATION_MODEL, Gson().toJson(item))
                intent.putExtras(this)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun createDialogFilter() {
        val dm = context.resources.displayMetrics
        val dmW = dm.widthPixels.toFloat() * 0.95f
        //        float dmH = (float) dm.heightPixels * 0.95f;
        with(DestinationDialogFilterBinding.inflate(layoutInflater)) {
            Dialog(context).let { dialog ->

                dialog.setContentView(root)
                dialog.window!!.setLayout(dmW.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
                dialog.setCancelable(false)
                setLabels(dialog)

                dialog.show()
            }
        }
    }

    private fun DestinationDialogFilterBinding.setLabels(dialog: Dialog) {
        tvTitle.text = hmAux_Trans[DIALOG_FILTER_TITLE]
        tvShowOnlySitePlanned.text = hmAux_Trans[DIALOG_FILTER_SHOW_PLANNED]
        tvShowOnlySiteToday.text = hmAux_Trans[DIALOG_FILTER_SHOW_TODAY]
        tvShowOnlySiteUrgent.text = hmAux_Trans[DIALOG_FILTER_SHOW_URGENT]
        tvHideOsPreventive.text = hmAux_Trans[DIALOG_FILTER_HIDE_OS_PREVENTIVE]


        presenter.getDestinationFilter().let { filter ->
            switchShowOnlySitePlanned.isChecked = filter.showOnlySiteWithPlanning
            switchShowOnlySiteToday.isChecked = filter.showOnlyToday
            switchShowOnlySiteUrgent.isChecked = filter.showOnlyUrgent
            switchHideOsPreventive.isChecked = filter.hidePreventive
        }

        cancelFilter.apply {
            text = hmAux_Trans[DIALOG_FILTER_CANCEL]
            setOnClickListener { _ ->
                dialog.dismiss()
            }
        }

        applyFilter.apply {
            text = hmAux_Trans[DIALOG_FILTER_APPLY]
            setOnClickListener { _ ->
                dialog.dismiss()
                presenter.getListDestinationAvailable(
                    AvailableDestinationFilter(
                        showOnlySiteWithPlanning = switchShowOnlySitePlanned.isChecked,
                        showOnlyToday = switchShowOnlySiteToday.isChecked,
                        showOnlyUrgent = switchShowOnlySiteUrgent.isChecked,
                        hidePreventive = switchHideOsPreventive.isChecked,
                    )
                )
            }
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        callAct005()
    }

    override fun callAct005() {
        val mIntent = Intent(context, Act005_Main::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(mIntent)
        finish()
    }

    private fun execSelectionItem(
        selection: SelectionDestinationAvailable
    ) {
        presenter.execSelectDestination(context, selection)
    }


    private fun checkSizeDestinationList(listSize: Int = 0) {
        with(binding) {
            recyclerView.visibility = if (listSize == 0) View.GONE else View.VISIBLE
            emptyList.visibility = if (listSize == 0) View.VISIBLE else View.GONE
        }
    }

    override fun initSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
            context,
            mModule_Code,
            ConstantBaseApp.ACT094
        )
    }

    override fun initTrans() {
        hmAux_Trans = presenter.getTranslation()
    }

    override fun initVars() {
        iniUIFooter(
            ConstantBaseApp.ACT094,
            hmAux_Trans
        )
//        changeBadgeVisibility()
        with(binding) {
            emptyList.text = hmAux_Trans[Act094Translate.EMPTY_LIST]

        }
    }

    override fun initAction() {

        with(binding) {


//            filterDestination.setOnClickListener {
//                createDialogFilter()
//            }

            edittextFilter.apply {
                hint = hmAux_Trans[EDIT_TEXT_FILTER]
                setOnReportTextChangeListner(object : IMKEditTextChangeText {
                    override fun reportTextChange(p0: String?) {
                    }

                    override fun reportTextChange(text: String?, p1: Boolean) {
                        mAdapter.filter.filter(text)
                    }

                })
            }

        }

    }

//    private fun changeBadgeVisibility() {
//        val filter = presenter.getDestinationFilter()
//        if (!filter.isDefault()) {
//            binding.badgeIcon.visibility = View.VISIBLE
//        } else {
//            binding.badgeIcon.visibility = View.GONE
//        }
//    }

    override fun onNavigateUp(): Boolean {
        return super.onNavigateUp()
    }

    override fun footerCreateDialog() {
        ToolBox_Inf.buildFooterDialog(context)
    }


    companion object {
        const val SELECT_DESTINATION_MODEL = "select_destination_model"
    }

}