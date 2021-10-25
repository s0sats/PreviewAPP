package com.namoadigital.prj001.ui.act011.frags

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.CheckBox
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.adapter.Act011InspectionFormAdapter
import com.namoadigital.prj001.databinding.Act011FrgIncludeHeaderBinding
import com.namoadigital.prj001.databinding.Act011InspectionListFragmentBinding
import com.namoadigital.prj001.model.AcessoryFormView
import com.namoadigital.prj001.model.Act011FormTab
import com.namoadigital.prj001.model.Act011FormTabStatus
import com.namoadigital.prj001.model.InspectionCell
import com.namoadigital.prj001.model.InspectionCell.Companion.CRITICAL_FORECAST
import com.namoadigital.prj001.model.InspectionCell.Companion.FORECAST
import com.namoadigital.prj001.model.InspectionCell.Companion.MANUAL_ALERT
import com.namoadigital.prj001.model.InspectionCell.Companion.NORMAL
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf

private const val ARG_VIEW_OBJECT = "ARG_VIEW_OBJECT"
private const val MAIN_HMAUX_TRANS_KEY = "MAIN_HMAUX_TRANS_KEY"

class Act011FrgInspection : Act011BaseFrg<Act011InspectionListFragmentBinding>() {

    private lateinit var mLayoutManager: LinearLayoutManager
    private var tabItemSelectedIndex: Int = -1
    private val mAdapter by lazy {
        Act011InspectionFormAdapter(acessoryFormView, hmAuxTrans, ::onItemSelected, ::onNotVerifyItemSelected)
    }
    private lateinit var acessoryFormView: AcessoryFormView
    private var _mFrgListener: InspectionListFragmentInteraction? = null
    private val mFrgListener get() = _mFrgListener!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        setLabels()
        //
        setInspectionList()
        //
        setVisibility()
        //
        setActions()
        //
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
                    mAdapter.filter.filter(s)
                }
            })

            chkNonForecastItem.setOnClickListener {
                acessoryFormView.nonForecastFilter = (it as CheckBox).isChecked
                mAdapter.applyNonForecastFilter((it as CheckBox).isChecked)
                handleAddNewProcessVisibility()
            }
            clAddNewItemBtn.setOnClickListener {
                mFrgListener.onInspectionSelected(acessoryFormView, true, -1, edtInspectionFilter.text.toString(), chkNonForecastItem.isChecked, "")
            }
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
                chkNonForecastItem.visibility = View.GONE
                tvNonForecastCount.visibility = View.GONE
            } else {
                handleAddNewProcessVisibility()
            }
        }
        //
        setListContentAndPlaceholder(acessoryFormView.inspections.isEmpty())
        //
    }

    private fun Act011InspectionListFragmentBinding.handleAddNewProcessVisibility() {
        if (showAddNewItem()) {
            clAddNewItemBtn.visibility = View.VISIBLE
        } else {
            clAddNewItemBtn.visibility = View.GONE
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
        }
    }

    private fun showAddNewItem(): Boolean {
        return ToolBox_Inf.profileExists(
            context,
            ConstantBaseApp.PROFILE_PRJ001_CHECKLIST,
            ConstantBaseApp.PROFILE_PRJ001_CHECKLIST_PARAM_DONE_NEW
        ) && !binding.chkNonForecastItem.isChecked
    }

    private fun setInspectionList() {
        mLayoutManager = LinearLayoutManager(context)
        binding.rvInspections.apply {
            layoutManager = mLayoutManager
            adapter = mAdapter
            ViewCompat.hasNestedScrollingParent(this )
            if(tabItemSelectedIndex >= 0) {
                binding.nsvMain.post {
                    //Calcula posicao inicial do Recycler + posicao final do item seleciona - o tamanho do item.
                    val y: Float = this.getY() + this.getChildAt(tabItemSelectedIndex).getY() - this.getChildAt(tabItemSelectedIndex).measuredHeight
                    binding.nsvMain.smoothScrollTo(0, y.toInt())
                    binding.nsvMain.scrollTo(0, binding.rvInspections.getChildAt(tabItemSelectedIndex).height)
                }
            }
        }
    }

    private fun setLabels() {
        binding.edtInspectionFilter.hint = hmAuxTrans.get("inspection_filter_list_hint")
        binding.tvAcesoryVal.text = acessoryFormView.acessoryName
        if(acessoryFormView.acessoryTracking.isNullOrEmpty()) {
            binding.tvTrackingVal.visibility = View.GONE
        }else{
            binding.tvTrackingVal.text = acessoryFormView.acessoryTracking
            binding.tvTrackingVal.visibility = View.VISIBLE
        }
        binding.tvNonForecastCount.text =  acessoryFormView.inspections.count {
            it.status == NORMAL && !ConstantBaseApp.SYS_STATUS_DONE.equals(it.answerStatus)
        }.toString()
        binding.tvAddNewItemVal.text = hmAuxTrans.get("inspection_add_new_process_btn")
        binding.chkNonForecastItem.text = hmAuxTrans.get("inspection_hide_non_forecast_item_chk")
        if(!acessoryFormView.nonForecastFilter){
            binding.chkNonForecastItem.apply {
                post {
                    performClick()
                }
            }
        }else{
            binding.apply {
                mAdapter.applyNonForecastFilter(chkNonForecastItem.isChecked)
                handleAddNewProcessVisibility()
            }
        }
        //
        if(acessoryFormView.inspections.isEmpty()) {
            binding.tvPlaceholder.text = hmAuxTrans.get("inspection_empty_list_placeholder")
        }else{
            binding.tvPlaceholder.text = hmAuxTrans.get("inspection_empty_list_filtered")
        }

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
            isFormOs: Boolean
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

                }
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
                "inspection_empty_list_placeholder",
                "inspection_empty_list_filtered"
            )
        }
    }
    fun setViewObject(viewObject: AcessoryFormView){
        acessoryFormView = viewObject
        acessoryFormView.tabIndex = this.tabIndex
        this.tabItemSelectedIndex = viewObject.lastPositionSelected
    }

    override fun getViewBinding() = Act011InspectionListFragmentBinding.inflate(layoutInflater)
    override fun getNavegationInclude() = binding.incNavegation



    override fun getTabErrorCount(): Int {
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
            it.status != NORMAL
        }
    }

    override fun getTabObj(skipFieldValidation: Boolean): Act011FormTab {
        val problemReportedCount = acessoryFormView.inspections.count {
            it.status == MANUAL_ALERT && !ConstantBaseApp.SYS_STATUS_DONE.equals(it.answerStatus)
        }
        //
        val criticalForecastCount = acessoryFormView.inspections.count {
            it.status == CRITICAL_FORECAST && !ConstantBaseApp.SYS_STATUS_DONE.equals(it.answerStatus)
        }
        //
        val forecastCount = acessoryFormView.inspections.count {
            it.status == FORECAST && !ConstantBaseApp.SYS_STATUS_DONE.equals(it.answerStatus)
        }
        //
        val nonForecastCount = acessoryFormView.inspections.count {
            it.status == NORMAL && !ConstantBaseApp.SYS_STATUS_DONE.equals(it.answerStatus)
        }
        //
        return Act011FormTab(
            page = tabIndex,
            name = mTabName,
            tracking = acessoryFormView.acessoryTracking,
            mTabItemCount,
            problemReportedCount,
            forecastCount,
            criticalForecastCount,
            nonForecastCount,
            status = if (skipFieldValidation) Act011FormTabStatus.PENDING else getTabStatus()
        )
    }

    override fun getTabStatus(): Act011FormTabStatus {
        return if (getTabErrorCount() == 0) {
            Act011FormTabStatus.OK
        } else {
            Act011FormTabStatus.ERROR
        }
    }

    override fun getTabName(): String {
        return acessoryFormView.acessoryName
    }

    override fun applyAutoAnswer(): Int {
        TODO("Not yet implemented")
    }

    override fun getHeaderInclude(): Act011FrgIncludeHeaderBinding = binding.incHeader

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is InspectionListFragmentInteraction){
            _mFrgListener = context
        }else{
            throw RuntimeException("${context.toString()} must implement FrgFFInteraction")
        }

    }
    //
    fun onItemSelected(position: Int,
                       itemPk: String){
        binding.apply {
            mFrgListener.onInspectionSelected(acessoryFormView, false, position, edtInspectionFilter.text.toString(), chkNonForecastItem.isChecked, itemPk)
        }
    }
    //
    fun onNotVerifyItemSelected(position: Int,
                                item: InspectionCell
    ){
        val onNotVerifyActionItem = mFrgListener.onNotVerifyAction(
            acessoryFormView.devicePkPrefix + "." + item.itemCodeAndSeq
        )

        for(i in 0..acessoryFormView.inspections.size -1 ){
            if(acessoryFormView.inspections[i].itemCodeAndSeq.equals(item.itemCodeAndSeq)){
                acessoryFormView.inspections.set(i, onNotVerifyActionItem)
                break
            }
        }

        mAdapter.refreshList(position, onNotVerifyActionItem)
        mFrgListener.onRefreshTabCounter(acessoryFormView.tabIndex)
    }
    //
}