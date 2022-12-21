package com.namoadigital.prj001.ui.act093.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.ui.act093.InfoSerialViewModel
import com.namoadigital.prj001.ui.act093.ui.components.NamoaAppBar
import com.namoadigital.prj001.ui.act093.ui.compose.Header
import com.namoadigital.prj001.ui.act093.ui.compose.MeasureUI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoSerialUI(
    applicationContext: Context,
    hmAux_Trans: HMAux,
    viewModel: InfoSerialViewModel,
    changeActivity: () -> Unit
) {
    with(applicationContext) {

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
                    changeActivity()
                }
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
                    .background(Color(resources.getColor(R.color.m3_namoa_background))),
            ) {

                Header(
                    applicationContext,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(
                            Color(resources.getColor(R.color.m3_namoa_surface2))
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

                Spacer(modifier = Modifier.height(16.dp))


            }

        }
    }
}