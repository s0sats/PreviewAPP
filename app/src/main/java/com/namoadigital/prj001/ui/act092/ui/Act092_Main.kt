package com.namoadigital.prj001.ui.act092.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.ctls.SearchableSpinner
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.dao.GE_Custom_Form_BlobDao
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.dao.MdJustifyItemDao
import com.namoadigital.prj001.databinding.Act092MainBinding
import com.namoadigital.prj001.databinding.TicketNotExecutedDialogBinding
import com.namoadigital.prj001.extensions.isCurrentTrip
import com.namoadigital.prj001.extensions.logout
import com.namoadigital.prj001.model.MyActionFilterParam
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.model.MyActionsBase
import com.namoadigital.prj001.model.TranslateResource
import com.namoadigital.prj001.service.*
import com.namoadigital.prj001.ui.act005.Act005_Main
import com.namoadigital.prj001.ui.act070.Act070_Main
import com.namoadigital.prj001.ui.act092.Act092Presenter
import com.namoadigital.prj001.ui.act092.Act092_Contract
import com.namoadigital.prj001.ui.act092.model.SerialModel
import com.namoadigital.prj001.ui.act092.ui.adapter.Act092_Adapter
import com.namoadigital.prj001.ui.act092.ui.adapter.SerialViewItem
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class Act092_Main : BaseActivityMvp
<Act092_Contract.Presenter,
        Act092MainBinding>(),
    Act092_Contract.View {
    override lateinit var bundle: Bundle
    var clearDueToOtherActionBtn = false
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

    override fun setItemAsDownloaded(position: Int, myActions: MyActions) {
        mAdapter.filterList[position] = SerialViewItem.ContentItem(myActions)
        mAdapter.notifyItemChanged(position)
    }

    override fun getContext(): Context {
        return context
    }

    private val _focusState = MutableStateFlow(FilterFocusUser())
    override val focusState = _focusState.asStateFlow()

    private var firstScroll = true

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
            ),
            showProductOnFilter = bundle.getBoolean(ConstantBaseApp.SHOW_PRODUCT_IN_ACT006, false)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(binding) {
            setContentView(root)
            setSupportActionBar(topAppBar)
            bundle = (savedInstanceState ?: intent.extras ?: Bundle())
            getBundle()
        }

        initView {
            presenter.setView(this)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getBundle() {
        val filterParam = bundle.getSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM)
        myActionFilterParam.value =
            filterParam?.let { it as MyActionFilterParam } ?: MyActionFilterParam()

    }

    override fun processLogin() {
        super.processLogin()
        //
        this.logout(false)
    }

    override fun processError_1(mLink: String?, mRequired: String?) {
        super.processError_1(mLink, mRequired)
        if (wsProcess.value == WS_Product_Serial_Structure::class.java.name) {
            ToolBox.alertMSG(
                context,
                hmAux_Trans["alert_serial_structure_error_ttl"],
                hmAux_Trans["alert_serial_structure_error_msg"],
                null,
                0
            )
        }
        presenter.newActionClick = false
        presenter.actionSelectedPosition = -1
        progressDialog.dismiss()
    }

    override fun processCustom_error(mLink: String?, mRequired: String?) {
        super.processCustom_error(mLink, mRequired)
        presenter.newActionClick = false
        presenter.actionSelectedPosition = -1
        progressDialog.dismiss()
    }


    override fun processCloseACT(mLink: String?, mRequired: String?, hmAux: HMAux) {
        when (wsProcess.value) {

            WsScheduleNotExecuted::class.java.name -> {
                wsProcess.value = ""

                progressDialog.dismiss()

                updateList()

            }

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
                if (presenter.hasSerialStructureOutdate(context)) {
                    presenter.updateSerialStrucutreAfterWsSave(context)
                } else {
                    presenter.otherActionFlow(context)
                }
            }

             WS_Product_Serial_Structure::class.java.simpleName -> {
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
                if (hmAux[WS_UnfocusAndHistoric.RESULT_LIST_SIZE].equals("0")) {
                    ToolBox.toastMSG(
                        context,
                        hmAux_Trans[Act092Translate.DIALOG_OTHER_ACTIONS_EMPTY_LIST_MSG]
                    )
                } else {
                    ToolBox.toastMSG(context, hmAux_Trans[Act092Translate.DIALOG_UPDATE_MSG])
                    _focusState.value.mainUser = context.isCurrentTrip()
                    presenter.getMyActionList()
                }
                return
            }

            WS_Serial_Search::class.java.name -> {
                wsProcess.value = ""
                progressDialog.dismiss()
                presenter.extractSearchResult(mLink, presenter.getActionSelected())
            }
            WS_Generate_NForm_PDF::class.java.name ->{
                //
                progressDialog.dismiss()
                if (hmAux != null && hmAux.hasConsistentValue(WS_Generate_NForm_PDF.NFORM_PK_KEY)
                    && hmAux.hasConsistentValue(GE_Custom_Form_BlobDao.BLOB_URL)
                ) {
                    //
                    val position = presenter.actionSelectedPosition
                    if(position >0) {
                        var myActionSelected = mAdapter.getActionByPosition(position)
                        myActionSelected?.let{
                            myActionSelected.pdfUrl = hmAux[GE_Custom_Form_BlobDao.BLOB_URL]
                            presenter.executeNFormPDFDownload(context,myActionSelected,position)
                        }?: ToolBox.alertMSG(
                                context,
                                hmAux_Trans["alert_generate_form_pdf_error_ttl"],
                                hmAux_Trans["alert_generate_form_pdf_error_msg"],
                                null,
                                0
                            )
                    }else{
                        ToolBox.alertMSG(
                            context,
                            hmAux_Trans["alert_generate_form_pdf_error_ttl"],
                            hmAux_Trans["alert_generate_form_pdf_error_msg"],
                            null,
                            0
                        )
                    }
                } else {
                    ToolBox.alertMSG(
                        context,
                        hmAux_Trans["alert_generate_form_pdf_error_ttl"],
                        hmAux_Trans["alert_generate_form_pdf_error_msg"],
                        null,
                        0
                    )
                }
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

    private fun updateList() {

        with(binding) {
            val filterTexts = editSerialFilter.text.toString()


            ToolBox.alertMSG(
                context,
                hmAux_Trans["alert_not_execute_justify_success_ttl"],
                hmAux_Trans["alert_not_execute_justify_success_msg"],
                { dialog, _ ->
                    dialog.dismiss()
                    presenter.getMyActionList()
                    if (::mAdapter.isInitialized) {
                        mAdapter.filter.filter(filterTexts)
                        filterText.value = filterTexts
                    }
                    mAdapter.userMainFilter = _focusState.value.mainUser
                },
                0
            )


        }

    }

    private fun updateTitleActionSerial(serialModel: SerialModel) {
        with(binding) {
            if (serialModel.classColor.isNullOrEmpty()) {
                iconClassColor.visibility = View.GONE
            } else {
                iconClassColor.visibility = View.VISIBLE
                iconClassColor.setColorFilter(Color.parseColor(serialModel.classColor))
            }
            serialTitle.text = serialModel.serialId
            productDescription.text = serialModel.productDesc


            filterText.value = serialModel.editFilter ?: ""
            if (filterText.value.isNotEmpty()) {
                binding.editSerialFilter.setText(filterText.value)
            }

        }
    }

    override fun initVars() {
        with(binding) {
            topAppBar.title = hmAux_Trans["act092_title"]
            act092TextLayout.apply {
                hint = hmAux_Trans[Act092Translate.HINT_FILTER]
            }
            btnOtherSerial.text = hmAux_Trans["btn_other_actions"]
            btnCreateAction.text = hmAux_Trans["btn_new_action"]
        }
        iniUIFooter(Constant.ACT092, hmAux_Trans)
    }


    override fun initAction() {
        with(binding) {

            infoSerial.setOnClickListener {
                presenter.goToInfoSerial(bundle)
            }

            mainUserSelection.setOnClickListener {
                if (mainUserSelection.isEnabled) {
                    _focusState.value = focusState.value.copy(
                        mainUser = !_focusState.value.mainUser
                    )
                    onEvent(Act092UiEvent.FilterMainUser)
                    presenter.getMyActionList()
                }
            }

            btnOtherSerial.setOnClickListener {
                _focusState.value = focusState.value.copy(
                    mainUser = if(context.isCurrentTrip()) true else false,
                    userFocus = !focusState.value.userFocus
                )

                if (::mAdapter.isInitialized) {
                    mAdapter.userMainFilter = _focusState.value.mainUser
                }

                clearTextFilter()

                disableMainAndOtherActions()

                if (_focusState.value.userFocus && !context.isCurrentTrip()) {
                    presenter.getMyActionList()
                } else {
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
                    if (::mAdapter.isInitialized) {
                        mAdapter.filter.filter(text)
                        filterText.value = text ?: ""
                    }
                }
            })
        }
    }

    /*
        BARRRIONUEVO 20-12-2022
        Desabilita antes de limpar para não forçar o filtro do adapter.
     */
    private fun Act092MainBinding.clearTextFilter() {
        editSerialFilter.isEnabled = false
        editSerialFilter.text?.clear()
        filterText.value = ""
        editSerialFilter.isEnabled = true
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
    override fun onEvent(state: Act092UiEvent) {
        CoroutineScope(Dispatchers.Main).launch {
            when (state) {
                is Act092UiEvent.UpdateTitleActionSerial -> {
                    updateTitleActionSerial(
                        state.serialModel
                    )
                }

                is Act092UiEvent.IsLoading -> {
                    isLoading(state.isLoading, state.message)
                }

                is Act092UiEvent.EmptyOrError -> {
                    emptyList(state.sizeList)
                }

                is Act092UiEvent.ListingSerialSteels -> {
                    if (state.list.isEmpty()) {
                        emptyList(state.list.size)
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
                    presenter.saveFilterWhenLeftActivity()
                    callAct(state.classe, state.bundle)
                }

                is Act092UiEvent.UpdateFooterInfos -> {
                    updateFooterInfos()
                }

                is Act092UiEvent.CallActForResult -> {
                    startActivityForResult(Intent(context, state.classe).also {
                        it.putExtras(state.bundle ?: Bundle())
                    }, state.code)
                }

                is Act092UiEvent.UpdateOtherAction -> {
                    if (!state.isOnline) {
                        _focusState.value = _focusState.value.copy(
                            mainUser = focusState.value.mainUser,
                            userFocus = !focusState.value.userFocus
                        )
                    }
                    disableMainAndOtherActions()
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

    private fun disableMainAndOtherActions() {


        val focusState = _focusState.value.userFocus

        val btnBackground =
            if (!focusState) {
                ColorStateList.valueOf(resources.getColor(R.color.m3_namoa_inverseSurface))
            } else {
                ColorStateList.valueOf(resources.getColor(com.namoa_digital.namoa_library.R.color.padrao_TRANSPARENT))
            }

        val mainUserCircle =
            if (!focusState) {
                resources.getDrawable(
                    R.drawable.my_action_toogle_disable_inset
                )
            } else {
                resources.getDrawable(R.drawable.my_action_toogle_default_inset)
            }
        val mainUserPerson =
            if (focusState) {
                R.drawable.ic_person_black_24dp
            } else {
                R.drawable.ic_person_disable_24dp
            }

        val textColor =
            if (!focusState) {
                resources.getColor(
                    R.color.m3_namoa_surface
                )
            } else {
                resources.getColor(R.color.m3_namoa_inverseSurface)
            }

        with(binding) {
            btnOtherSerial.apply {
                backgroundTintList = btnBackground
                iconTint = ColorStateList.valueOf(textColor)
                setTextColor(textColor)
                strokeWidth = if (focusState) 1 else 0
            }

            mainUserSelection.apply {
                isEnabled = focusState && !context.isCurrentTrip()
                background = mainUserCircle
                if(context.isCurrentTrip() && focusState){
                    selectMainUserLayout()
                }else {
                    setImageResource(mainUserPerson)
                    setPadding(ToolBox.convertPixelsToDpIndeed(context, 12f).toInt())
                }
                isClickable = focusState && !context.isCurrentTrip()
            }

        }
    }

    private fun initRecyclerView(
        list: List<MyActionsBase>,
        mainUser: Boolean = _focusState.value.mainUser
    ) {
        with(binding) {

            mAdapter = Act092_Adapter(
                list,
                hmAux_Trans,
                myActionClickListener = { myAction, position ->
                    presenter.processActionClick(
                        myAction,
                        context,
                        position
                    )
                },
                {
                    onEvent(Act092UiEvent.EmptyOrError(sizeList = it))
                },
                cancelSerialSchedule = { item ->
                    createNotExecuteDialog(item)
                }
            )

            emptyList.visibility = View.GONE
            progressLoading.visibility = View.GONE
            mAdapter.userMainFilter = mainUser
            recyclerSerialList.apply {
                visibility = View.VISIBLE
                adapter = mAdapter
                layoutManager = LinearLayoutManager(context)
            }
            scrollToLastSelectedItem()
            //
            if (!editSerialFilter.text.isNullOrEmpty()) {
                mAdapter.filter.filter(editSerialFilter.text)
                mAdapter.notifyDataSetChanged()
            }
            //


        }
    }

    private fun createNotExecuteDialog(myAction: MyActions) {
        val dm = context.resources.displayMetrics
        val dmW = dm.widthPixels.toFloat() * 0.95f
        //        float dmH = (float) dm.heightPixels * 0.95f;
        with(TicketNotExecutedDialogBinding.inflate(layoutInflater)) {
            Dialog(context).let { dialog ->

                dialog.setContentView(root)
                dialog.window!!.setLayout(dmW.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
                dialog.setCancelable(false)

                act070IvJustifyPhotoBtn.visibility = View.GONE

                setLabel(myAction)
                setActions(myAction,
                    closeDialog = {
                        dialog.hide()
                    }
                )

                dialog.show()
            }
        }
    }

    private fun validateNotExecuteFormEntry(binding: TicketNotExecutedDialogBinding): String {
        var errorMsg: String? = ""
        val hmAux = binding.act070NotExecuteDialogJustifyOptionSs.getmValue()
        if (hmAux.hasConsistentValue(MdJustifyItemDao.REQUIRED_COMMENT) && "1" == hmAux[MdJustifyItemDao.REQUIRED_COMMENT]) {
            if (binding.act070NotExecuteDialogJustifyCommentsActv.text == null || binding.act070NotExecuteDialogJustifyCommentsActv.text.toString()
                    .isEmpty()
            ) {
                errorMsg = hmAux_Trans["alert_not_execute_justify_comment_required_msg"]
            }
        }
        if (binding.act070NotExecuteDialogJustifyOptionSs.visibility == View.VISIBLE && !hmAux.hasConsistentValue(
                SearchableSpinner.CODE
            )
        ) {
            errorMsg += hmAux_Trans["alert_not_execute_justify_option_required_msg"]
        }
        return errorMsg ?: ""
    }

    private fun showAlert(
        ttl: String?,
        msg: String?,
        clickListner: DialogInterface.OnClickListener? = null,
        negativeBtn: Int = 0
    ) {
        ToolBox.alertMSG(
            context, ttl, msg, clickListner, negativeBtn
        )
    }

    private fun TicketNotExecutedDialogBinding.setActions(
        item: MyActions,
        closeDialog: () -> Unit,
    ) {

        var dateReschedule = ""

        act070NotExecuteDialogJustifyBtnCancel.setOnClickListener {
            val hasJustify = (act070NotExecuteDialogJustifyOptionSs.visibility == View.VISIBLE
                    && act070NotExecuteDialogJustifyOptionSs.getmValue()
                .hasConsistentValue(SearchableSpinner.CODE))
            val hasComments: Boolean =
                act070NotExecuteDialogJustifyCommentsActv.text.toString().isNotEmpty()
            val dateKey = (act070NotExecuteDialogJustifyDate.tvDateVal.visibility == View.VISIBLE
                    && act070NotExecuteDialogJustifyDate.tvDateVal.mketContents.hasConsistentValue("DATE_KEY")
                    && !act070NotExecuteDialogJustifyDate.tvDateVal.mketContents["DATE_KEY"].isNullOrEmpty())
            val hourKey = (act070NotExecuteDialogJustifyDate.tvDateVal.visibility == View.VISIBLE
                    && act070NotExecuteDialogJustifyDate.tvDateVal.mketContents.hasConsistentValue("HOUR_KEY")
                    && !act070NotExecuteDialogJustifyDate.tvDateVal.mketContents["HOUR_KEY"].isNullOrEmpty())
            if (hasJustify || hasComments || dateKey || hourKey) {
                showAlert(
                    hmAux_Trans["alert_not_execute_justify_lost_data_ttl"],
                    hmAux_Trans["alert_not_execute_justify_lost_data_msg"],
                    { _, _ -> closeDialog() },
                    1
                )
            } else {
                closeDialog()
            }
        }

        act070NotExecuteDialogJustifyBtnSave.setOnClickListener {

            with(act070NotExecuteDialogJustifyDate) {

                fun dateInvalid(msg: String) {
                    Toast.makeText(
                        this@Act092_Main,
                        hmAux_Trans[msg],
                        Toast.LENGTH_SHORT
                    ).show()
                }

                if (root.isVisible) {

                    val dateKey =
                        tvDateVal.mketContents.hasConsistentValue("DATE_KEY") && !tvDateVal.mketContents["DATE_KEY"].isNullOrEmpty()
                    val hourKey =
                        tvDateVal.mketContents.hasConsistentValue("HOUR_KEY") && !tvDateVal.mketContents["HOUR_KEY"].isNullOrEmpty()

                    if (dateKey || hourKey) {

                        if (tvDateVal.isValid) {
                            val isFuture = ToolBox_Inf.isFutureDate(tvDateVal.getmValue())

                            if (!isFuture) {
                                dateInvalid("warning_not_execute_justify_future_date_hour")
                                return@setOnClickListener
                            }

                            if (!tvDateVal.getmValue().isNullOrEmpty()) {
                                dateReschedule = tvDateVal.getmValue()
                            }
                        } else {
                            dateInvalid("warning_not_execute_justify_future_date_hour")
                            return@setOnClickListener
                        }

                    }
                }
            }

            val errorMsg = validateNotExecuteFormEntry(this)
            val justifyOption = act070NotExecuteDialogJustifyOptionSs.getmValue()
            errorMsg.ifEmpty {
                closeDialog()
                presenter.justifyNotExecuteSchedule(
                    processPk = item.processPk,
                    comments = act070NotExecuteDialogJustifyCommentsActv.text.toString(),
                    justify_group_code = item.hasNotExecuted!!,
                    justify_item_code = justifyOption[SearchableSpinner.CODE]?.toInt() ?: -1,
                    reschedule_date = dateReschedule,
                    context = context,
                )
                return@setOnClickListener
            }
            Toast.makeText(
                this@Act092_Main,
                errorMsg,
                Toast.LENGTH_SHORT
            ).show()
        }


    }


    private fun TicketNotExecutedDialogBinding.setLabel(
        item: MyActions
    ) {
        act070NotExecuteDialogTtl.text = hmAux_Trans["alert_not_execute_ttl"]
        act070NotExecuteDialogMsg.text = hmAux_Trans["alert_not_execute_msg"]
        //

        val justifyItems = presenter.getJustifyItems(item.hasNotExecuted!!, context)


        act070NotExecuteDialogJustifyOptionSs.apply {

            if (justifyItems.isEmpty()) {
                visibility = View.GONE
                return@apply
            }

            setmRequired(true)
            setmShowLabel(true)
            setmCanClean(true)
            setmOption(justifyItems)
            setmLabel(hmAux_Trans["alert_not_execute_justify_option_lbl"])


            setOnItemSelectedListener(object : SearchableSpinner.OnItemSelectedListener {
                override fun onItemPreSelected(p0: HMAux?) {


                    act070NotExecuteDialogJustifyCommentsTil.clearFocus()

                }

                override fun onItemPostSelected(hmAux: HMAux?) {

                    val requiredReschedule = hmAux?.get(MdJustifyItemDao.RESCHEDULE)?.toInt() == 1
                    val hmAux = act070NotExecuteDialogJustifyOptionSs.getmValue()
                    val states = arrayOf(
                        intArrayOf(android.R.attr.state_enabled), // enabled
                        intArrayOf(android.R.attr.state_pressed), // pressed
                        intArrayOf(android.R.attr.state_focused)  // focused
                    )


                    val colorsRequired = intArrayOf(
                        resources.getColor(com.namoa_digital.namoa_library.R.color.customff_required_on_color),
                        resources.getColor(com.namoa_digital.namoa_library.R.color.customff_required_on_color),
                        resources.getColor(com.namoa_digital.namoa_library.R.color.customff_required_on_color),
                    )

                    val colorsDefault = intArrayOf(
                        resources.getColor(R.color.m3_namoa_outline),
                        resources.getColor(R.color.m3_namoa_primary),
                        resources.getColor(R.color.m3_namoa_primary),
                    )

                    val colorRequiredState = ColorStateList(states, colorsRequired)
                    val colorDefaultState = ColorStateList(states, colorsDefault)


                    if (hmAux.hasConsistentValue(MdJustifyItemDao.REQUIRED_COMMENT) && "1" == hmAux[MdJustifyItemDao.REQUIRED_COMMENT]) {
                        act070NotExecuteDialogJustifyCommentsTil.hintTextColor = colorRequiredState
                        act070NotExecuteDialogJustifyCommentsTil.setBoxStrokeColorStateList(
                            colorRequiredState
                        )
                    } else {
                        act070NotExecuteDialogJustifyCommentsTil.hintTextColor = colorDefaultState
                        act070NotExecuteDialogJustifyCommentsTil.setBoxStrokeColorStateList(
                            colorDefaultState
                        )
                    }

                    if (!requiredReschedule) {
                        act070NotExecuteJustifyDateTtl.visibility = View.GONE
                        act070NotExecuteDialogJustifyDate.root.visibility = View.GONE
                        act070NotExecuteDialogJustifyDate.tvDateVal.setmValue("")
                        return
                    }

                    act070NotExecuteDialogJustifyDate.root.visibility = View.VISIBLE
                    act070NotExecuteDialogJustifyDate.tvDateVal.setmHighlightWhenInvalid(true)
                    //
                    act070NotExecuteJustifyDateTtl.visibility = View.VISIBLE
                    act070NotExecuteJustifyDateTtl.text =
                        hmAux_Trans["alert_not_execute_justify_date_ttl"]
                }

            })
        }

        //
        act070NotExecuteDialogJustifyDate.tvDateVal.setmLabel("")
        act070NotExecuteDialogJustifyDate.chkShiftStep.visibility = View.GONE
        act070NotExecuteDialogJustifyDate.chkShiftTicketDate.visibility = View.GONE
        act070NotExecuteDialogJustifyDate.guideline6.visibility = View.GONE
        act070NotExecuteDialogJustifyDate.tvDateLbl.visibility = View.GONE
        act070NotExecuteDialogJustifyDate.tvTimeLbl.visibility = View.GONE

        act070NotExecuteDialogJustifyCommentsTil.hint =
            hmAux_Trans["alert_not_execute_justify_comment_lbl"]
        act070NotExecuteDialogJustifyBtnCancel.text = hmAux_Trans["sys_alert_btn_cancel"]
        act070NotExecuteDialogJustifyBtnSave.text =
            hmAux_Trans["alert_not_execute_save_btn"]
    }

    private fun toggleMainUserFilter(
        value: Boolean = _focusState.value.mainUser
    ) {

        with(binding.mainUserSelection) {
            if (value) {
                selectMainUserLayout()
            } else {
                background = resources.getDrawable(R.drawable.my_action_toogle_default_inset)
                setImageResource(R.drawable.ic_person_black_24dp)
                setPadding(ToolBox.convertPixelsToDpIndeed(context, 12f).toInt())
            }
        }
    }

    private fun ImageView.selectMainUserLayout() {
        setImageResource(R.drawable.ic_person_white_24dp)
        background = resources.getDrawable(R.drawable.my_action_toogle_pressed_inset)
        setPadding(ToolBox.convertPixelsToDpIndeed(context, 12f).toInt())
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

            is DialogType.CUSTOM_OK -> {
                ToolBox.alertMSG(
                    context,
                    hmAux_Trans[dialogType.title],
                    dialogType.message,
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
            mainUserSelection.isEnabled = !isLoading || !context.isCurrentTrip()
            btnOtherSerial.isEnabled = !isLoading
            editSerialFilter.isEnabled = !isLoading
            //progress
            progressLoading.visibility = progressVisible
            //
            recyclerSerialList.visibility = itemsVisible
            emptyList.visibility = itemsVisible
        }
    }

    private fun emptyList(
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

    private fun scrollToLastSelectedItem() {
        if (firstScroll) {
            firstScroll = false
            val actionPkPosition = mAdapter.getActionPkPosition(
                presenter.serialModel.value.lastSelectActionType,
                presenter.serialModel.value.lastSelectedPk,
            )
            if (actionPkPosition >= 0) {
                //Tenta fazer scroll com offset, se crashar, tenta scroll sem offset
                try {
                    val linearLayoutManager =
                        binding.recyclerSerialList.layoutManager as LinearLayoutManager
                    val offset = ToolBox.dbToPixel(context, 50)
                    linearLayoutManager.scrollToPositionWithOffset(actionPkPosition, offset)
                } catch (e: Exception) {
                    binding.recyclerSerialList.scrollToPosition(
                        actionPkPosition
                    )
                }
            }
        }
    }
}