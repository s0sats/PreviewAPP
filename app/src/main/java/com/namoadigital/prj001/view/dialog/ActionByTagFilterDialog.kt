package com.namoadigital.prj001.view.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act005FilterDialogBinding
import com.namoadigital.prj001.util.ConstantBaseApp.*
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class ActionByTagFilterDialog(context: Context,
                              val listener: OnApplyFilterListener): AlertDialog(context) {
    private val mResourceName = "tag_filter_dialog"
    private val hmAux_trans  by lazy{
        var transListDialog = ArrayList<String>();
        //
        transListDialog.add("filter_lbl")
        transListDialog.add("rg_period_lbl")
        transListDialog.add("until_next_action_opt")
        transListDialog.add("until_next_week_opt")
        transListDialog.add("until_today_opt")
        transListDialog.add("all_periods_opt")
        transListDialog.add("current_site_opt")
        transListDialog.add("all_sites_opt")
        transListDialog.add("only_my_action_opt")
        transListDialog.add("all_action_opt")
        transListDialog.add("period_filter_lbl")
        transListDialog.add("site_filter_lbl")
        transListDialog.add("focus_filter_lbl")
        transListDialog.add("btn_save_lbl")
        //
        val mResource_Code_Frag = ToolBox_Inf.getResourceCode(
                context,
                APP_MODULE,
                mResourceName
        )
        //
        ToolBox_Inf.setLanguage(
                context,
                APP_MODULE,
                mResource_Code_Frag,
                ToolBox_Con.getPreference_Translate_Code(context),
                transListDialog
        )
    }
    private var _binding: Act005FilterDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //
        _binding = Act005FilterDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //
        setLabels()
        //
        initValues()
        //
        iniActions()
    }

    private fun iniActions() {
        //
        binding.btnFilterSave.setOnClickListener{
            //
            val periodFilterPreference = when(binding.rgPeriod.checkedRadioButtonId){
                R.id.rb_period_until_today -> PREFERENCE_HOME_UNTIL_TODAY_OPTION
                R.id.rb_period_next_week -> PREFERENCE_HOME_NEXT_WEEK_OPTION
                else -> PREFERENCE_HOME_ALL_TIME_OPTION
            }
            //
            val siteFilterPreference =when(binding.rgPeriod.checkedRadioButtonId){
                R.id.rb_all_sites -> PREFERENCE_HOME_ALL_SITE_OPTION
                else -> PREFERENCE_HOME_UNTIL_TODAY_OPTION
            }
            //
            val focusFilterPreference =when(binding.rgPeriod.checkedRadioButtonId){
                R.id.rb_focusable_actions -> PREFERENCE_HOME_ONLY_MY_ACTIONS_OPTION
                else -> PREFERENCE_HOME_ALL_ACTIONS_FILTER
            }
            //
            listener.onApply(periodFilterPreference, siteFilterPreference, focusFilterPreference)
            dismiss();
        }
        //
        binding.btnFilterCancel.setOnClickListener{
            dismiss();
        }
        //
    }


    private fun initValues() {
        val periodFilter = ToolBox_Con.getStringPreferencesByKey(context, PREFERENCE_HOME_PERIOD_FILTER, PREFERENCE_HOME_ALL_TIME_OPTION)
        val sitesFilter =  ToolBox_Con.getStringPreferencesByKey(context, PREFERENCE_HOME_SITES_FILTER, PREFERENCE_HOME_ALL_SITE_OPTION)
        val focusFilter =  ToolBox_Con.getStringPreferencesByKey(context, PREFERENCE_HOME_FOCUS_FILTER, PREFERENCE_HOME_ONLY_MY_ACTIONS_OPTION)
        //
        if(periodFilter.equals(PREFERENCE_HOME_UNTIL_TODAY_OPTION)){
            binding.rgPeriod.check(binding.rbPeriodUntilToday.id)
        }else if(periodFilter.equals(PREFERENCE_HOME_NEXT_WEEK_OPTION)){
            binding.rgPeriod.check(binding.rbPeriodNextWeek.id)
        }else { //PREFERENCE_HOME_ALL_TIME_OPTION
            binding.rgPeriod.check(binding.rbAllActionPeriods.id)
        }
        //
        if(sitesFilter.equals(PREFERENCE_HOME_ALL_SITE_OPTION)){
            binding.rgSite.check(R.id.rb_all_sites)
            binding.rbAllSites.isChecked = true
        }else{
            binding.rbCurrentSite.isChecked = true
            binding.rgSite.check(R.id.rb_current_site)
        }
        //
        if(focusFilter.equals(PREFERENCE_HOME_ONLY_MY_ACTIONS_OPTION)){
            binding.rgFocus.check(R.id.rb_focusable_actions)
        }else{
            binding.rgFocus.check(R.id.rb_all_actions)
        }
        //
    }

    private fun setLabels() {
        binding.tvFilterLbl.text = hmAux_trans.get("filter_lbl")
        //
        binding.rbPeriodNextWeek.text = hmAux_trans.get("until_next_week_opt")
        binding.rbPeriodUntilToday.text = hmAux_trans.get("until_today_opt")
        binding.rbAllActionPeriods.text = hmAux_trans.get("all_periods_opt")
        //
        binding.rbCurrentSite.text = hmAux_trans.get("current_site_opt")
        binding.rbAllSites.text = hmAux_trans.get("all_sites_opt")
        //
        binding.rbFocusableActions.text = hmAux_trans.get("only_my_action_opt")
        binding.rbAllActions.text = hmAux_trans.get("all_action_opt")
        //
        binding.rgPeriodLbl.text = hmAux_trans.get("period_filter_lbl")
        binding.rgSiteLbl.text = hmAux_trans.get("site_filter_lbl")
        binding.rgFocusLbl.text = hmAux_trans.get("focus_filter_lbl")
        //
        binding.btnFilterSave.text = hmAux_trans.get("btn_save_lbl")
        binding.btnFilterCancel.text = hmAux_trans.get("sys_alert_btn_cancel")
    }

    interface OnApplyFilterListener {
        fun onApply(periodFilter: String, siteFilter: String, focusFilter: String)
    }
}


