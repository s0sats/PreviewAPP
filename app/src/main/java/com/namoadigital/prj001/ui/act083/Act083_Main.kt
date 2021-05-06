package com.namoadigital.prj001.ui.act083

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.R
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.databinding.Act083MainBinding
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.util.*


class Act083_Main : Base_Activity() {

    private lateinit var binding: Act083MainBinding

    private val viewModel by lazy {
        val factory = Act083ViewModelFactory(
                TK_TicketDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                TkTicketCacheDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                MD_Schedule_ExecDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                GE_Custom_Form_ApDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                GE_Custom_Form_LocalDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                )
        )
        //
        ViewModelProvider(this,factory).get(Act083ViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Act083MainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        //
        iniSetup()
        initVars()
        iniUIFooter()
        initActions()
    }

    private fun iniSetup() {
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                ConstantBaseApp.ACT083
        )
        //
        loadTranslation()
    }

    private fun loadTranslation() {
        val transList: MutableList<String> = ArrayList()
        transList.add("act083_title")
        transList.add("tab_my_actions_lbl")
        transList.add("tab_other_actions_lbl")
        transList.add("filter_hint")
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        )
    }

    private fun initVars() {
        //fakeChipsGenerator()
        setLabels()
    }

    private fun fakeChipsGenerator() {
        for(i in 1..5){
            binding.act083MainContent.act083CgFilter.addView(
                    createTvChip("banheiro $i")
            )
        }
        //
       // binding.act083MainContent.act083CgFilter.requestLayout()
    }

    private fun createTvChip(chipLabel: String) : TextView {
        val tvChip = TextView(ContextThemeWrapper(context, R.style.TextViewChips))
        tvChip.apply {
            text = chipLabel
        }
        //
        return tvChip
    }

    private fun setLabels() {
        with(binding.act083MainContent){
            this.act083MketFilter.setText(hmAux_Trans["filter_hint"])
            this.act083TabMyActions.text = hmAux_Trans["tab_my_actions_lbl"]
            this.act083TabOtherActions.text = hmAux_Trans["tab_other_actions_lbl"]
        }
    }

    private fun iniUIFooter() {
        iniFooter()
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context)
        mAct_Info = Constant.ACT083
        mAct_Title = Constant.ACT083 + "_" + "title"
        //
        val mFooter = ToolBox_Inf.loadFooterSiteOperationInfo(context)
        mSite_Value = mFooter[Constant.FOOTER_SITE]
        mOperation_Value = mFooter[Constant.FOOTER_OPERATION]
        //
        setUILanguage(hmAux_Trans)
        setMenuLanguage(hmAux_Trans)
        setTitleLanguage()
        setFooter()
    }

    private fun initActions() {
    }
}

