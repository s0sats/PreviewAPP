package com.namoadigital.prj001.ui.act011.frags

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.CheckBox
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.adapter.Act011InspectionFormAdapter
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao
import com.namoadigital.prj001.databinding.Act011FrgIncludeHeaderBinding
import com.namoadigital.prj001.databinding.Act011InspectionListFragmentBinding
import com.namoadigital.prj001.extensions.setCheckedJumpingAnimation
import com.namoadigital.prj001.model.AcessoryFormView
import com.namoadigital.prj001.model.Act011FormTab
import com.namoadigital.prj001.model.Act011FormTab.Act011FormCounter
import com.namoadigital.prj001.model.Act011FormTabStatus
import com.namoadigital.prj001.model.InspectionCell
import com.namoadigital.prj001.model.InspectionCell.Companion.NORMAL
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItemStatusColor
import com.namoadigital.prj001.ui.act011.model.FormTicketInfo
import com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.MeasureItemBottomSheet
import com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.MeasureItemBottomSheetActions
import com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.model.MeasureBottomSheetContext
import com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.model.MeasureItemArguments
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val ARG_ACESSORY_FORM_VIEW_IDX = "ARG_ACESSORY_FORM_VIEW_IDX"

class Act011FrgInspection : Act011BaseFrg<Act011InspectionListFragmentBinding>() {

    private lateinit var mLayoutManager: LinearLayoutManager
    private var tabItemSelectedIndex: Int = -1
    private lateinit var mAdapter: Act011InspectionFormAdapter
    private var acessoryFormViewIdx = -1
    lateinit var acessoryFormView: AcessoryFormView
    private var _mFrgListener: InspectionListFragmentInteraction? = null
    private val mFrgListener get() = _mFrgListener!!

    //LUCHE - 08/11/2021 - Var que identifica se existe não previstos.
    //Já existe uma metodo que faz isso, porem, como checagem precisa ser feita a cada caracter filtrado,
    //criei a variante val pra evitar processamento desnecessario ja que até hj esse valor e final
    //uma vez que para responder um nao previsto, é necessairo navegar para tela de verificacao
    private val hasNonForecast by lazy {
        val find = acessoryFormView.inspections.find {
            it.status == NORMAL && it.answerStatus == null
        }
        //Se encontrou um não previsto sem resposta
        find != null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Log.d("Form_Refresh", "--------------onCreate-------------------")
        arguments?.let {
            tabIndex = it.getInt(GE_Custom_Form_Field_LocalDao.PAGE)
            tabLastIndex = it.getInt(PARAM_LAST_INDEX)
            formStatus = it.getString(GE_Custom_Form_DataDao.CUSTOM_FORM_STATUS, "")
            scheduleDesc = it.getString(MD_Schedule_ExecDao.SCHEDULE_DESC)
            scheduleComments = it.getString(GE_Custom_Form_Field_LocalDao.COMMENT)
            isFormOs = it.getBoolean(GE_Custom_Form_LocalDao.IS_SO, false)
            acessoryFormViewIdx = it.getInt(ARG_ACESSORY_FORM_VIEW_IDX, -1)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        Log.d("Form_Refresh", "--------------onViewCreated-------------------")
        super.onViewCreated(view, savedInstanceState)
        //Caso seja um recuperação do frg, chama interface que resgata acessoryFormView da act.
        savedInstanceState?.let {
            acessoryFormView = mFrgListener.getObjectView(acessoryFormViewIdx)
        }
//        Log.d("Form_Refresh", "acessoryName: ${acessoryFormView.acessoryName}")
        mAdapter =  Act011InspectionFormAdapter(
            acessoryFormView,
            hmAuxTrans,
            ::onItemSelected,
            ::onAlreadyOkItemSelected,
            ::onAdapterFilterApplied
        )
        //
        setLabels()
        //
        setActions()
        //
        setChkHideNonForecast()
        //
        setInspectionList()
        //
        setVisibility()
        //
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        Log.d("Form_Refresh", "--------------onSaveInstanceState-------------------")
        outState.putInt(ARG_ACESSORY_FORM_VIEW_IDX, acessoryFormViewIdx)
    }

    private fun setChkHideNonForecast() {
        binding.apply {

            if (getNonForecastItem() <= 0) {
                chkNonForecastItem.setCheckedJumpingAnimation(false)
                acessoryFormView.forecastFilter = false
                mAdapter.applyNonForecastFilter(false)
                chkNonForecastItem.isEnabled = false
                tvNonForecastCount.isEnabled = false
                handleAddNewProcessVisibility()
                handleForecastEmptyList()
            } else {
                chkNonForecastItem.setCheckedJumpingAnimation(acessoryFormView.forecastFilter)
                chkNonForecastItem.isEnabled = true
                tvNonForecastCount.isEnabled = true
                /*
                 binding.chkNonForecastItem.isEnabled = false
                */
                mAdapter.applyNonForecastFilter(binding.chkNonForecastItem.isChecked)
                handleAddNewProcessVisibility()
            }
        }
    }

    private fun setActions() {
        binding.apply {
            edtInspectionFilter.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.isNullOrEmpty()) {
                        hideNonForecastCheckBoxFilter(false)
                        mAdapter.applyNonForecastFilter(chkNonForecastItem.isChecked)
                    } else {
                        hideNonForecastCheckBoxFilter(true)
                    }
                    //LUCHE - 08/11/2021 - Valida visibilidade do btn
                    handleAddNewProcessVisibility()
                    mAdapter.filter.filter(s)
                }
            })

            chkNonForecastItem.setOnClickListener {
                handleCheckboxClick(it)
            }
            clAddNewItemBtn.setOnClickListener {
                mFrgListener.onInspectionSelected(
                    acessoryFormView,
                    true,
                    acessoryFormView.inspections.size,
                    edtInspectionFilter.text.toString(),
                    chkNonForecastItem.isChecked,
                    "",
                )
            }
        }
    }

    private fun Act011InspectionListFragmentBinding.handleCheckboxClick(
        it: View?
    ) {
        acessoryFormView.forecastFilter = (it as CheckBox).isChecked
        mAdapter.applyNonForecastFilter((it as CheckBox).isChecked)
        handleAddNewProcessVisibility()
        handleForecastEmptyList()
    }

    private fun Act011InspectionListFragmentBinding.handleForecastEmptyList() {
        val forecastItens = acessoryFormView.inspections.size - getNonForecastItem()
        if (!acessoryFormView.forecastFilter) {
            if (forecastItens == 0) {
                rvInspections.visibility = View.INVISIBLE
                tvPlaceholder.visibility = View.VISIBLE
            } else {
                rvInspections.visibility = View.VISIBLE
                tvPlaceholder.visibility = View.GONE
            }
        } else {
            if ((acessoryFormView.inspections.size - forecastItens) > 0) {
                rvInspections.visibility = View.VISIBLE
                tvPlaceholder.visibility = View.GONE
            }
        }
    }

    fun resetTextFilter() {
        acessoryFormView.filterVal = ""
        binding.edtInspectionFilter.setText("")

        binding.apply {
            chkNonForecastItem.setCheckedJumpingAnimation(false)
            mAdapter.applyNonForecastFilter(false)
            if (getNonForecastItem() <= 0) {
                acessoryFormView.forecastFilter = false
                mAdapter.applyNonForecastFilter(false)
                chkNonForecastItem.isEnabled = false
                tvNonForecastCount.isEnabled = false
                handleForecastEmptyList()
            } else {
                chkNonForecastItem.isEnabled = true
                tvNonForecastCount.isEnabled = true
                /*
                 binding.chkNonForecastItem.isEnabled = false
                */
                mAdapter.applyNonForecastFilter(binding.chkNonForecastItem.isChecked)
            }
            handleAddNewProcessVisibility()
            updateNonForecastCounter()
        }
    }

    private fun hideNonForecastCheckBoxFilter(hideCheckbox: Boolean) {
        if (hideCheckbox) {
            binding.chkNonForecastItem.visibility = View.GONE
            binding.tvNonForecastCount.visibility = View.GONE
        } else {
            if (!acessoryFormView.isReadOnly) {
                binding.chkNonForecastItem.visibility = View.VISIBLE
                binding.tvNonForecastCount.visibility = View.VISIBLE
            }
        }
    }

    private fun setVisibility() {
        binding.apply {
            if (acessoryFormView.isReadOnly) {
                clAddNewItemBtn.visibility = View.GONE
                //chkNonForecastItem.visibility = View.GONE
                //tvNonForecastCount.visibility = View.GONE
            } else {
                handleAddNewProcessVisibility()
            }
        }
        //
        setListContentAndPlaceholder(acessoryFormView.inspections.isEmpty())
        //
    }

    private fun handleAddNewProcessVisibility() {
        if (showAddNewItem()) {
            binding.clAddNewItemBtn.visibility = View.VISIBLE
        } else {
            binding.clAddNewItemBtn.visibility = View.GONE
        }
    }

    private fun setListContentAndPlaceholder(isEmptyList: Boolean) {
        binding.apply {
            if (isEmptyList) {
                tvPlaceholder.visibility = View.VISIBLE
                rvInspections.visibility = View.GONE
            } else {
                tvPlaceholder.visibility = View.GONE
                rvInspections.visibility = View.VISIBLE
            }
            handleForecastEmptyList()
        }
    }

    private fun showAddNewItem(): Boolean {
        return ToolBox_Inf.profileExists(
            context,
            ConstantBaseApp.PROFILE_PRJ001_CHECKLIST,
            ConstantBaseApp.PROFILE_PRJ001_CHECKLIST_PARAM_ITEM_CHECK_NEW
        ) && !acessoryFormView.isReadOnly
                && (binding.chkNonForecastItem.isChecked
                || binding.edtInspectionFilter.text.toString().trim().isNotEmpty()
                || getNonForecastItem() == 0
                )
    }

    private fun setInspectionList() {
        mLayoutManager = LinearLayoutManager(context)
        binding.rvInspections.apply {
            layoutManager = mLayoutManager
            adapter = mAdapter
            ViewCompat.hasNestedScrollingParent(this)
            if (tabItemSelectedIndex >= 0) {
                binding.nsvMain.post {
                    //LUCHE - 05/11/2021
                    //Movido a aplicação do filtro pra fora do when, pois deve ser aplicado em ambos
                    //os casos.Correção de bug , pois no caso de desistencia da criação de form manual
                    //Int.MAX_VALUE, não aplicava o filtro.
                    if (!acessoryFormView.filterVal.isEmpty()) {
                        binding.edtInspectionFilter.setText(acessoryFormView.filterVal)
                    }
                    //
                    when (tabItemSelectedIndex) {
                        Int.MAX_VALUE -> {
                            binding.nsvMain.fullScroll(View.FOCUS_DOWN)
                        }

                        else -> {
                            mAdapter.highlightedItemPosition = tabItemSelectedIndex
                            mAdapter.notifyItemChanged(tabItemSelectedIndex)
                            //Calcula posicao inicial do Recycler + posicao final do item seleciona - o tamanho do item.
                            //LUCHE - 04/11/2021 - Add tratativa de null, pois ao limpar reposta de um item e volta,
                            //aconteceu um crash
                            val childHeight = this.getChildAt(tabItemSelectedIndex)?.let {
                                it.getY() - it.measuredHeight
                            } ?: 0f
                            val y: Float = this.getY() + childHeight
                            binding.nsvMain.scrollTo(0, y.toInt())
                        }
                    }
                }
            }
        }
    }

    private fun setLabels() {

        binding.edtInspectionFilterLayout.hint = hmAuxTrans.get("inspection_filter_list_hint")
        binding.tvAcesoryVal.text = acessoryFormView.acessoryName
        if (acessoryFormView.acessoryTracking.isNullOrEmpty()) {
            binding.tvTrackingVal.visibility = View.GONE
        } else {
            binding.tvTrackingVal.text = acessoryFormView.acessoryTracking
            binding.tvTrackingVal.visibility = View.VISIBLE
        }
        updateNonForecastCounter()
        binding.clAddNewItemBtn.text = hmAuxTrans.get("inspection_add_new_process_btn")
        binding.chkNonForecastItem.text = hmAuxTrans.get("inspection_show_no_forecast_item_chk")

        //
        if (acessoryFormView.inspections.isEmpty()) {
            binding.tvPlaceholder.text = hmAuxTrans.get("inspection_empty_list_placeholder")
        } else {
            binding.tvPlaceholder.text = hmAuxTrans.get("inspection_empty_list_filtered")
        }
    }

    private fun updateNonForecastCounter() {
        val count = getNonForecastItem()
        //
        with(binding.tvNonForecastCount) {
            if (count > 0) {
                text = "+$count"
                isEnabled = true
            } else {
                text = "0"
                isEnabled = false
            }
        }
    }


    private fun getNonForecastItem() = acessoryFormView.inspections.count {
        !it.isVisible && !it.isDone
    }

    companion object {
        @JvmStatic
        fun newInstance(
            hmAux_Trans: HMAux,
            tabIndex: Int = 0,
            tabLastIndex: Int = 0,
            tabItemSelectedIndex: Int = -1,
            formStatus: String,
            scheduleDesc: String?,
            scheduleComments: String?,
            isFormOs: Boolean,
            acessoryFormView: AcessoryFormView,
            acessoryFormViewIdx: Int
        ) =
            Act011FrgInspection().apply {
                this.hmAuxTrans = hmAux_Trans
                this.formStatus = formStatus
                this.tabIndex = tabIndex
                this.tabLastIndex = tabLastIndex
                this.scheduleDesc = scheduleDesc
                this.scheduleComments = scheduleComments
                this.isFormOs = isFormOs
                arguments = Bundle().apply {
                    putString(GE_Custom_Form_DataDao.CUSTOM_FORM_STATUS, formStatus)
                    putInt(GE_Custom_Form_Field_LocalDao.PAGE, tabIndex)
                    putInt(PARAM_LAST_INDEX, tabLastIndex)
                    putString(MD_Schedule_ExecDao.SCHEDULE_DESC, scheduleDesc)
                    putString(GE_Custom_Form_Field_LocalDao.COMMENT, scheduleComments)
                    putBoolean(GE_Custom_Form_LocalDao.IS_SO, isFormOs)
                    putInt(ARG_ACESSORY_FORM_VIEW_IDX, acessoryFormViewIdx)
                }
                this.acessoryFormView = acessoryFormView
                this.tabItemSelectedIndex = acessoryFormView.lastPositionSelected
                this.acessoryFormViewIdx = acessoryFormViewIdx
            }

        fun getFragTranslationsVars(): List<String> {
            return listOf(
                "alert_filter_applied_msg",
                "inspection_missing_days",
                "inspection_alert_days",
                "inspection_ongoing_action_lbl",
                "inspection_add_new_process_btn",
                "inspection_verify_action_lbl",
                "inspection_filter_list_hint",
                "inspection_status_answered_item_lbl",
                "inspection_status_non_forecast_item_lbl",
                "inspection_status_manual_alert_item_lbl",
                "inspection_status_critical_forecast_item_lbl",
                "inspection_status_forecast_item_lbl",
                "inspection_not_verify_action_lbl",
                "inspection_hide_non_forecast_item_chk",
                "inspection_show_no_forecast_item_chk",
                "inspection_empty_list_placeholder",
                "inspection_empty_list_filtered",
                "inspection_already_ok_action_lbl",
                "inspection_visualize_action_lbl"
            )
        }
    }

    override fun getViewBinding() = Act011InspectionListFragmentBinding.inflate(layoutInflater)
    override fun getNavegationInclude() = binding.incNavegation

    override fun getTabErrorCount(validHighlight: Boolean): Int {
//        val problemReportedCount = acessoryFormView.inspections.count {
//            it.status == MANUAL_ALERT && !it.answerStatus.equals(ConstantBaseApp.SYS_STATUS_DONE)
//        }
//        //
//        val criticalForecastCount = acessoryFormView.inspections.count {
//            it.status == CRITICAL_FORECAST && !it.answerStatus.equals(ConstantBaseApp.SYS_STATUS_DONE)
//        }
//        //
//        val forecastCount = acessoryFormView.inspections.count {
//            it.status == FORECAST && !it.answerStatus.equals(ConstantBaseApp.SYS_STATUS_DONE)
//        }
        val onGoingCount = acessoryFormView.inspections.count {
            ConstantBaseApp.SYS_STATUS_PROCESS.equals(it.answerStatus)
//                    && it.status == NORMAL
        }

        return onGoingCount
    }

    override fun getTabCount(): Int {
        return acessoryFormView.inspections.count {
            it.isVisible || it.isDone || it.answerStatus != null
        }
    }

    override fun getTabInteractionCount(): Int? {
        return acessoryFormView.inspections.count {
            it.isDone
        }
    }

    override fun getTabObj(skipFieldValidation: Boolean, validHighLight: Boolean): Act011FormTab {
        val colorCounts = acessoryFormView.inspections
            .asSequence()
            .groupBy { it.statusColor }
            .mapValues { (_, items) ->
                val done = items.count { it.isVisible && it.isDone }
                val total =
                    items.count { (it.isVisible && !it.isPartitioned()) || (it.isPartitioned() && it.isDone) }
                if (done < total) Act011FormCounter(done, total) else null
            }
        val requiredByTicket = acessoryFormView.inspections.count {
            it.ticketFormType == FormTicketInfo.TicketFormType.SAME_TICKET
                    && !it.isDone
        }
        //
        return Act011FormTab(
            page = tabIndex,
            name = mTabName,
            tracking = acessoryFormView.acessoryTracking,
            fieldCount = mTabItemCount,
            problemReportedCount = colorCounts[GeOsDeviceItemStatusColor.RED],
            forecastCount = colorCounts[GeOsDeviceItemStatusColor.BLUE],
            criticalForecastCount = colorCounts[GeOsDeviceItemStatusColor.YELLOW],
            nonForecastCount = colorCounts[GeOsDeviceItemStatusColor.GRAY],
            requiredByTicketCount = requiredByTicket,
            status = if (skipFieldValidation) Act011FormTabStatus.PENDING else getTabStatus(
                validHighLight
            )
        )
    }

    override fun getTabStatus(validHighlight: Boolean): Act011FormTabStatus {
        return if (getTabErrorCount(validHighlight) == 0) {
            Act011FormTabStatus.OK
        } else {
            Act011FormTabStatus.ERROR
        }
    }

    override fun getTabName(): String {
        return acessoryFormView.acessoryName
    }

    override fun applyAutoAnswer(): Int {
        return -1;
    }

    override fun getHeaderInclude(): Act011FrgIncludeHeaderBinding = binding.incHeader

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is InspectionListFragmentInteraction) {
            _mFrgListener = context
        } else {
            throw RuntimeException("${context.toString()} must implement FrgFFInteraction")
        }

    }

    override fun onDetach() {
        super.onDetach()
//        Log.d("Form_Refresh", "--------------onDetach-------------------")
        _mFrgListener = null
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isAdded) {
            if (isVisibleToUser) {
                binding.edtInspectionFilter.text?.clear()
            } else {
                mAdapter.highlightedItemPosition = -1
                binding.nsvMain.fullScroll(View.FOCUS_UP)
                //Limpa filtro texto ao sair.
            }
            resetTextFilter()
        }

    }

    //
    fun onItemSelected(
        position: Int,
        itemPk: String,
        partitioned_execution: Int,
        measureBottomSheetContext: MeasureBottomSheetContext? = null,
        ticketFormType: FormTicketInfo.TicketFormType = FormTicketInfo.TicketFormType.NO_TICKET
    ) {

        if (ticketFormType != FormTicketInfo.TicketFormType.OTHER_TICKET  && (measureBottomSheetContext != null &&
                    measureBottomSheetContext.arguments.value == null &&
                    !measureBottomSheetContext.arguments.isReadOnly)
        ) {
            showMeasureItemBottomSheet(
                measureBottomSheetContext,
                position,
                itemPk,
                partitioned_execution,
                ticketFormType
            )
        } else {
            mFrgListener.onInspectionSelected(
                acessoryFormView,
                false,
                position,
                binding.edtInspectionFilter.text.toString(),
                binding.chkNonForecastItem.isChecked,
                itemPk,
                partitioned_execution,
                ticketFormType
            )
        }
    }

    private fun showMeasureItemBottomSheet(
        measureBottomSheetContext: MeasureBottomSheetContext,
        position: Int,
        itemPk: String,
        partitioned_execution: Int,
        ticketFormType: FormTicketInfo.TicketFormType
    ) {
        MeasureItemBottomSheet(
            measureItemBottomSheet = measureBottomSheetContext,
            actions = object : MeasureItemBottomSheetActions {
                override fun onNavigateReadOnly() {
                    mFrgListener.onInspectionSelected(
                        acessoryFormView,
                        false,
                        position,
                        binding.edtInspectionFilter.text.toString(),
                        binding.chkNonForecastItem.isChecked,
                        itemPk,
                        partitioned_execution,
                        ticketFormType
                    )
                }

                override fun onSaveMeasurement(
                    newMeasure: Double?,
                    newID: String?,
                    state: MeasureItemArguments.State
                ) {
                    _mFrgListener?.onSaveInitialMeasurement(
                        acessoryFormView.devicePkPrefix + "." + itemPk,
                        newMeasure,
                        newID
                    )

                    mFrgListener.onInspectionSelected(
                        acessoryFormView,
                        false,
                        position,
                        binding.edtInspectionFilter.text.toString(),
                        binding.chkNonForecastItem.isChecked,
                        itemPk,
                        partitioned_execution,
                        ticketFormType
                    )
                }
            }
        ).show(requireActivity().supportFragmentManager, "MeasureItemBottomSheet")
    }

    //
    fun onAlreadyOkItemSelected(
        position: Int,
        item: InspectionCell
    ) {
        val onAlreadyOkActionItem = mFrgListener.onAlreadyOkAction(
            acessoryFormView.devicePkPrefix + "." + item.itemCodeAndSeq
        )
        //
        for (i in 0..acessoryFormView.inspections.size - 1) {
            if (acessoryFormView.inspections[i].itemCodeAndSeq.equals(item.itemCodeAndSeq)) {
                acessoryFormView.inspections.set(i, onAlreadyOkActionItem)
                break
            }
        }
        //
        if (item.requirePhotoAlreadyOk) {
            onItemSelected(
                position,
                item.itemCodeAndSeq,
                item.partitionedExecution,
                null,
                item.ticketFormType
            )
        } else {
            updateNonForecastCounter()
            mAdapter.refreshItemList(position, onAlreadyOkActionItem)
            mFrgListener.onRefreshTabCounter(acessoryFormView.tabIndex)
        }
    }

    /**
     * Fun disparada pelo adapter ao filtrar itens passadno a qtd de itens existentes no filtro.
     * Define a visibilidade do recycler e visibilidade e lbl do placeholder
     * Se qtyItensFiltered 0:
     *  - Esconde o recycle e exibe o placeholder
     * Se qtyItensFiltered 0 , não houver filtro texto e existem itens não previstos, então significa
     * que o adapter é 0 , pois não existem previstos, mas existem não previstos oculto pelo filtro
     * e por isso a msg é de que existem itens ocultos
     */
    fun onAdapterFilterApplied(qtyItensFiltered: Int) {
        with(binding) {
            rvInspections.visibility = if (qtyItensFiltered == 0) {
                View.GONE
            } else {
                View.VISIBLE
            }
            tvPlaceholder.visibility = if (qtyItensFiltered == 0) {
                View.VISIBLE
            } else {
                View.GONE
            }
            tvPlaceholder.text = if (qtyItensFiltered == 0
                && edtInspectionFilter.text.toString().trim().isEmpty()
                && hasNonForecast
            ) {
                hmAuxTrans["inspection_empty_list_filtered"]
            } else {
                hmAuxTrans["inspection_empty_list_placeholder"]
            }
        }
    }

    fun refreshInspection(acessoryFormView: AcessoryFormView) {
//        Log.d("Form_Refresh", "--------------refreshInspection-------------------")
        this.acessoryFormView = acessoryFormView
        CoroutineScope(Dispatchers.Main).launch {
            mAdapter.refreshList(acessoryFormView)
            mAdapter.applyNonForecastFilter(acessoryFormView.forecastFilter)
            binding.rvInspections.post {
                mAdapter.notifyDataSetChanged()
            }
        }
    }

}