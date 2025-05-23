package com.namoadigital.prj001.ui.act011.finish_os

import android.os.Bundle
import android.view.View
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.setViewTreeLifecycleOwner
import com.namoa_digital.namoa_library.compose.theme.NamoaApplicationTheme
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.databinding.Act011FrgIncludeHeaderBinding
import com.namoadigital.prj001.databinding.Act011FrgIncludeNavegationBinding
import com.namoadigital.prj001.databinding.FragmentFinishOsBinding
import com.namoadigital.prj001.extensions.getBooleanArguments
import com.namoadigital.prj001.extensions.getIntArguments
import com.namoadigital.prj001.extensions.getLongArguments
import com.namoadigital.prj001.model.Act011FormTab
import com.namoadigital.prj001.model.Act011FormTabStatus
import com.namoadigital.prj001.ui.act011.finish_os.ui.FinishOSScreen
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishScreenArguments
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishScreenArguments.ARGUMENTS
import com.namoadigital.prj001.ui.act011.frags.Act011BaseFrg
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FinishOSFragment constructor(
    private val hmAuxLib: HMAux,
) : Act011BaseFrg<FragmentFinishOsBinding>() {


    override fun getViewBinding(): FragmentFinishOsBinding =
        FragmentFinishOsBinding.inflate(layoutInflater)

    override fun getHeaderInclude(): Act011FrgIncludeHeaderBinding = binding.incHeader

    override fun getNavegationInclude(): Act011FrgIncludeNavegationBinding = binding.incNavegation

    override fun getTabErrorCount(): Int = 0

    override fun getTabCount(): Int = 0

    override fun getTabObj(skipFieldValidation: Boolean): Act011FormTab = Act011FormTab(
        page = this.tabIndex,
        name = mTabName,
        tracking = null,
        fieldCount = mTabItemCount,
        problemReportedCount = null,
        forecastCount = null,
        criticalForecastCount = null,
        nonForecastCount = null,
        status = Act011FormTabStatus.OK
    )

    override fun getTabStatus(): Act011FormTabStatus = Act011FormTabStatus.OK

    override fun getTabName(): String = hmAuxTrans["finish_os_tab_name"] ?: ""

    override fun applyAutoAnswer(): Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initScreen()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initScreen() {
        binding.composeView.apply {
            this.setViewTreeLifecycleOwner(this@FinishOSFragment)
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                NamoaApplicationTheme {
                    FinishOSScreen(
                        translateMapLib = hmAuxLib,
                        arguments = FinishScreenArguments(
                            typeCode = arguments.getIntArguments(ARGUMENTS.TYPE_CODE),
                            code = arguments.getIntArguments(ARGUMENTS.CODE),
                            versionCode = arguments.getIntArguments(ARGUMENTS.VERSION_CODE),
                            formData = arguments.getLongArguments(ARGUMENTS.FORM_DATA),
                            isReadOnly = arguments.getBooleanArguments(ARGUMENTS.IS_READ_ONLY)
                        ),
                        onDialogDismiss = {},
                        onBarcodeMkEditText = {},
                        onDialogClose = {}
                    )
                }
            }
        }
    }


    companion object {
        fun newInstance(
            hmAuxTrans: HMAux,
            hmAuxLib: HMAux,
            formStatus: String,
            tabIndex: Int,
            tabLastIndex: Int,
        ) = FinishOSFragment(hmAuxLib).apply {
            this.hmAuxTrans = hmAuxTrans
            this.formStatus = formStatus
            this.tabIndex = tabIndex
            this.tabLastIndex = tabLastIndex
            this.isFormOs = true
        }
    }
}