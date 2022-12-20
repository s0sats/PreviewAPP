package com.namoadigital.prj001.ui.act093.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.R.*
import com.namoadigital.prj001.databinding.Act093MainBinding
import com.namoadigital.prj001.ui.act092.ui.Act092_Main
import com.namoadigital.prj001.ui.act093.InfoSerialViewModel
import com.namoadigital.prj001.ui.act093.ui.components.NamoaAppBar
import com.namoadigital.prj001.ui.act093.ui.compose.Header
import com.namoadigital.prj001.ui.act093.ui.compose.MeasureUI
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class Act093_Main : Base_Activity() {


    private val binding: Act093MainBinding by lazy {
        Act093MainBinding.inflate(layoutInflater)
    }

    private val InfoViewModel: InfoSerialViewModel by viewModels {
        InfoSerialViewModel.Factory(
            context
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.composeView.apply {
            setContent {
                MaterialTheme {
                    Surface() {
                        MainUI()
                    }
                }
            }
        }

        iniUIFooter(Constant.ACT093, hmAux_Trans)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        Intent(applicationContext, Act092_Main::class.java).also {
            startActivity(it)
            finish()
        }
    }

    override fun footerCreateDialog() {
        //super.footerCreateDialog();
        ToolBox_Inf.buildFooterDialog(context)
    }

    private fun iniUIFooter(act: String, hmAux: HMAux) {
        iniFooter()
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context)
        mAct_Info = act
        mAct_Title = act + "_" + "title"
        //
        val mFooter = ToolBox_Inf.loadFooterSiteOperationInfo(context)
        mSite_Value = mFooter[Constant.FOOTER_SITE]
        mOperation_Value = mFooter[Constant.FOOTER_OPERATION]
        //
        setUILanguage(hmAux)
        setMenuLanguage(hmAux)
        setFooter()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainUI(
        viewModel: InfoSerialViewModel = InfoViewModel,
    ) {

        val state = viewModel.state.serialInfo

/*        if(state == null){
            onBackPressed()
        }*/

        Scaffold(
            topBar = {
                NamoaAppBar(
                    applicationContext,
                    title = {
                        Text(
                            text = "Mais info do serial",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                ) {
                    Intent(applicationContext, Act092_Main::class.java).also {
                        startActivity(it)
                        finish()
                    }
                }
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
                    .background(Color(resources.getColor(color.m3_namoa_background))),
            ) {

                Header(
                    applicationContext,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(
                            Color(resources.getColor(color.m3_namoa_surface2))
                        ),
                    colorSerial = state.iconColor,
                    serialId = state.serialId,
                    productId = state.product,
                    marcaModel = state.model,
                    trackings = state.trackings
                )

                MeasureUI(
                    modifier = Modifier.fillMaxWidth(),
                    context = applicationContext,
                    hmAux = hmAux_Trans,
                    lastMeasure = state.last_measure_value,
                    lastDate = state.last_measure_date,
                    lastCicle = state.last_cycle_value
                )

            }

        }
    }
}