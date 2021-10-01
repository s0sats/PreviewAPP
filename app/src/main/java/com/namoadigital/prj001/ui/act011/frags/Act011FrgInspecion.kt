package com.namoadigital.prj001.ui.act011.frags

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
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
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import com.namoadigital.prj001.view.frag.frg_main_home.FrgMainHome
import java.util.*

private const val ARG_VIEW_OBJECT = "ARG_VIEW_OBJECT"
private const val MAIN_HMAUX_TRANS_KEY = "MAIN_HMAUX_TRANS_KEY"

class InspectionListFragment : Act011BaseFrg<Act011InspectionListFragmentBinding>() {

    private val mAdapter by lazy {
        Act011InspectionFormAdapter(acessoryFormView.inspections, onInspectionSelected)
    }
    private lateinit var acessoryFormView: AcessoryFormView
    var onInspectionSelected: (inspection: InspectionCell) -> Unit =
        { inspection: InspectionCell -> }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            acessoryFormView = (it.getSerializable(ARG_VIEW_OBJECT) as AcessoryFormView?)!!
            hmAuxTrans =
                HMAux.getHmAuxFromHashMap(it.getSerializable(Constant.MAIN_HMAUX_TRANS_KEY) as HashMap<String?, String?>)
        }
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
            edtInspectionFilter.addTextChangedListener( object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if(s != null){
                        if(s.toString().isNullOrEmpty()){
                            hideNonForecastCheckBoxFilter(false)
                        }else{
                            hideNonForecastCheckBoxFilter(true)
                        }
                    }else{
                        hideNonForecastCheckBoxFilter(false)
                    }

                    s?.let {

                    } ?: kotlin.run {
                        hideNonForecastCheckBoxFilter(false)
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    mAdapter.filter.filter(s)
                }
            } )
        }
    }

    private fun hideNonForecastCheckBoxFilter(hideCheckbox: Boolean) {
        if(hideCheckbox){
            binding.chkNonForecastItem.visibility = View.GONE
            binding.tvNonForecastCount.visibility = View.GONE
        }else{
            binding.chkNonForecastItem.visibility = View.VISIBLE
            binding.tvNonForecastCount.visibility = View.VISIBLE
        }
    }

    private fun setVisibility() {
        binding.apply {
            if (acessoryFormView.isReadOnly) {
                clAddNewItemBtn.visibility = View.GONE
                chkNonForecastItem.visibility = View.GONE
            } else {
                if (hideAddNewItem()) {
                    clAddNewItemBtn.visibility = View.GONE
                } else {
                    tvAddNewItemVal.visibility = View.VISIBLE
                }
            }
        }
        //
        setListContentAndPlaceholder(acessoryFormView.inspections.isEmpty())
        //
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

    private fun hideAddNewItem(): Boolean {
        return !ToolBox_Inf.profileExists(
            context,
            ConstantBaseApp.PROFILE_PRJ001_CHECKLIST,
            ConstantBaseApp.PROFILE_PRJ001_CHECKLIST_PARAM_DONE_NEW
        ) || binding.chkNonForecastItem.isChecked
    }

    private fun setInspectionList() {
        binding.rvInspections.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
    }

    private fun setLabels() {
        binding.edtInspectionFilter.hint = hmAuxTrans.get("inspection_filter_list_hint")
    }

    companion object {
        @JvmStatic
        fun newInstance(viewObject: AcessoryFormView, hmAux_Trans: HMAux?) =
            FrgMainHome().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_VIEW_OBJECT, viewObject)
                    putSerializable(MAIN_HMAUX_TRANS_KEY, hmAux_Trans)
                }
            }

        fun getFragTranslationsVars(): List<String> {
            return listOf(
                "alert_filter_applied_msg",
                "inspection_filter_list_hint"
            )
        }
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
            it.status == ""
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

}