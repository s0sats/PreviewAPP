package com.namoadigital.prj001.view.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act005FilterDialogBinding
import com.namoadigital.prj001.util.ConstantBaseApp.*
import com.namoadigital.prj001.util.ToolBox_Con

class ActionByTagFilterDialog(context: Context,
                              val hmAux_trans: HMAux,
                              val listener: OnApplyFilterListener): AlertDialog(context) {

    private var _binding: Act005FilterDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act005_filter_dialog)
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
                R.id.rb_period_next_action -> PREFERENCE_HOME_PERIOD_NEXT_ACTION_OPTION
                R.id.rb_period_until_today -> PREFERENCE_HOME_UNTIL_TODAY_OPTION
                R.id.rb_period_next_week -> PREFERENCE_HOME_NEXT_WEEK_OPTION
               else -> PREFERENCE_HOME_ALL_TIME_OPTION
            }
            //
            val siteFilterPreference =when(binding.rgPeriod.checkedRadioButtonId){
                R.id.rb_all_sites -> PREFERENCE_HOME_PERIOD_NEXT_ACTION_OPTION
                else -> PREFERENCE_HOME_UNTIL_TODAY_OPTION
            }
            //
            val focusFilterPreference =when(binding.rgPeriod.checkedRadioButtonId){
                R.id.rb_focusable_actions -> PREFERENCE_HOME_PERIOD_NEXT_ACTION_OPTION
                else -> PREFERENCE_HOME_ALL_TIME_OPTION
            }
            //
            listener.onApply(periodFilterPreference, siteFilterPreference, focusFilterPreference )
        }
        //
        binding.btnFilterCancel.setOnClickListener{
            dismiss();
        }
        //
    }


    private fun initValues() {
        val periodFilter = ToolBox_Con.getStringPreferencesByKey(context, PREFERENCE_HOME_PERIOD_FILTER, PREFERENCE_HOME_PERIOD_NEXT_ACTION_OPTION)
        val sitesFilter =  ToolBox_Con.getStringPreferencesByKey(context, PREFERENCE_HOME_SITES_FILTER, PREFERENCE_HOME_ALL_SITE_OPTION)
        val focusFilter =  ToolBox_Con.getStringPreferencesByKey(context, PREFERENCE_HOME_FOCUS_FILTER, PREFERENCE_HOME_ONLY_MY_ACTIONS_OPTION)
        //
        if(periodFilter.equals(PREFERENCE_HOME_UNTIL_TODAY_OPTION)){
            binding.rgSite.check(R.id.rb_period_until_today)
        }else if(periodFilter.equals(PREFERENCE_HOME_NEXT_WEEK_OPTION)){
            binding.rgSite.check(R.id.rb_period_next_week)
        }else if(periodFilter.equals(PREFERENCE_HOME_ALL_TIME_OPTION)){
            binding.rgSite.check(R.id.rb_all_action_periods)
        }else{
            binding.rgSite.check(R.id.rb_period_next_action)
        }
        //
        if(sitesFilter.equals(PREFERENCE_HOME_ALL_SITE_OPTION)){
            binding.rgSite.check(R.id.rb_all_sites)
        }else{
            binding.rgSite.check(R.id.rb_current_site)
        }
        //
        if(focusFilter.equals(PREFERENCE_HOME_ONLY_MY_ACTIONS_OPTION)){
            binding.rgSite.check(R.id.rb_focusable_actions)
        }else{
            binding.rgSite.check(R.id.rb_all_actions)
        }
        //
    }

    private fun setLabels() {
        binding.tvFilterLbl.text = hmAux_trans.get("filter_lbl")
        //
        binding.rbPeriodNextAction.text = hmAux_trans.get("until_next_action_opt")
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
        binding.rbAllActions.text = hmAux_trans.get("all_action_opt")
        binding.rbAllActions.text = hmAux_trans.get("all_action_opt")
        //
    }

    interface OnApplyFilterListener {
        fun onApply(periodFilterPreference: String, siteFilterPreference: String, focusFilterPreference: String)
    }
}

