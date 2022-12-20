package com.namoadigital.prj001.ui.act093.ui.compose

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R

@Composable
fun MeasureUI(
    modifier: Modifier = Modifier,
    context: Context,
    hmAux: HMAux,
    lastMeasure: String? = null,
    lastDate: String? = null,
    lastCicle: String? = null,
) {

    Row(
        modifier = modifier
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            Text(
                text = hmAux["last_measure_lbl"] ?: "last measure",
                style = MaterialTheme.typography.bodySmall,
                color = Color(context.resources.getColor(R.color.m3_namoa_onBackground))
            )

            Text(
                text = (lastMeasure ?: "-") + " " + (lastDate ?: "-"),
                style = MaterialTheme.typography.bodyLarge,
                color = Color(context.resources.getColor(R.color.m3_namoa_onBackground))
            )
        }


        Column {
            Text(
                text = hmAux["last_cicle_lbl"] ?: "last cicle",
                style = MaterialTheme.typography.bodySmall,
                color = Color(context.resources.getColor(R.color.m3_namoa_onBackground))
            )

            Text(
                text = lastCicle ?: "-",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(context.resources.getColor(R.color.m3_namoa_onBackground))
            )
        }

    }

}