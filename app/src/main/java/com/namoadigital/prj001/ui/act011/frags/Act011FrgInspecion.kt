package com.namoadigital.prj001.ui.act011.frags

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.CheckBox
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.adapter.Act011InspectionFormAdapter
import com.namoadigital.prj001.databinding.Act011FrgIncludeHeaderBinding
import com.namoadigital.prj001.databinding.Act011InspectionListFragmentBinding
import com.namoadigital.prj001.model.AcessoryFormView
import com.namoadigital.prj001.model.Act011FormTab
import com.namoadigital.prj001.model.Act011FormTabStatus
import com.namoadigital.prj001.model.InspectionCell.Companion.CRITICAL_FORECAST
import com.namoadigital.prj001.model.InspectionCell.Companion.FORECAST
import com.namoadigital.prj001.model.InspectionCell.Companion.MANUAL_ALERT
import com.namoadigital.prj001.model.InspectionCell.Companion.NORMAL
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf

private const val ARG_VIEW_OBJECT = "ARG_VIEW_OBJECT"
private const val MAIN_HMAUX_TRANS_KEY = "MAIN_HMAUX_TRANS_KEY"

class Act011FrgInspection : Act011BaseFrg<Act011InspectionListFragmentBinding>() {

    private val mAdapter by lazy {
        Act011InspectionFormAdapter(acessoryFormView, hmAuxTrans, mFrgListener)
    }
    private lateinit var acessoryFormView: AcessoryFormView
    private var _mFrgListener: InspectionListFragmentInteraction? = null
    private val mFrgListener get() = _mFrgListener!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

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
    }

    private fun setActions() {
        binding.apply {
            edtInspectionFilter.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s != null) {
                        if (s.toString().isNullOrEmpty()) {
                            hideNonForecastCheckBoxFilter(false)
                        } else {
                            hideNonForecastCheckBoxFilter(true)
                        }
                    } else {
                        hideNonForecastCheckBoxFilter(false)
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    mAdapter.filter.filter(s)
                }
            })

            chkNonForecastItem.setOnClickListener {
                mAdapter.applyNonForecastFilter((it as CheckBox).isChecked)
                handleAddNewProcessVisibility()
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
                rvInspections.visibility = View.GONE
                tvPlaceholder.visibility = View.VISIBLE
            } else {
                rvInspections.visibility = View.VISIBLE
                tvPlaceholder.visibility = View.GONE
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
        binding.rvInspections.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
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
            it.status == NORMAL
        }.toString()
        binding.tvAddNewItemVal.text = hmAuxTrans.get("inspection_add_new_process_btn")
    }

    companion object {
        @JvmStatic
        fun newInstance(hmAux_Trans: HMAux,
                        tabIndex: Int = 0,
                        tabLastIndex: Int = 0,
                        formStatus: String,
                        scheduleDesc: String?,
                        scheduleComments: String?) =
            Act011FrgInspection().apply {
                this.hmAuxTrans = hmAux_Trans
                this.formStatus = formStatus
                this.tabIndex = tabIndex
                this.tabLastIndex = tabLastIndex
                this.scheduleDesc = scheduleDesc
                this.scheduleComments = scheduleComments
                arguments = Bundle().apply {

                }
            }

        fun getFragTranslationsVars(): List<String> {
            return listOf(
                "alert_filter_applied_msg",
                "inspection_missing_days",
                "inspection_alert_days",
                "inpection_ongoing_action_lbl",
                "inspection_add_new_process_btn",
                "inpection_verify_action_lbl",
                "inspection_filter_list_hint"
            )
        }
    }
    fun setViewObject(viewObject: AcessoryFormView){
        acessoryFormView = viewObject
        acessoryFormView.tabIndex = this.tabIndex
    }

    override fun getViewBinding() = Act011InspectionListFragmentBinding.inflate(layoutInflater)
    override fun getNavegationInclude() = binding.incNavegation


    override fun getTabErrorCount(): Int {
        val problemReportedCount = acessoryFormView.inspections.count {
            it.status == MANUAL_ALERT
        }
        //
        val criticalForecastCount = acessoryFormView.inspections.count {
            it.status == CRITICAL_FORECAST
        }
        //
        val forecastCount = acessoryFormView.inspections.count {
            it.status == FORECAST
        }
        return problemReportedCount + criticalForecastCount + forecastCount
    }

    override fun getTabCount(): Int {
        return acessoryFormView.inspections.count {
            it.status != NORMAL
        }
    }

    override fun getTabObj(skipFieldValidation: Boolean): Act011FormTab {
        val problemReportedCount = acessoryFormView.inspections.count {
            it.status == MANUAL_ALERT
        }
        //
        val criticalForecastCount = acessoryFormView.inspections.count {
            it.status == CRITICAL_FORECAST
        }
        //
        val forecastCount = acessoryFormView.inspections.count {
            it.status == FORECAST
        }
        //
        val nonForecastCount = acessoryFormView.inspections.count {
            it.status == NORMAL
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
}