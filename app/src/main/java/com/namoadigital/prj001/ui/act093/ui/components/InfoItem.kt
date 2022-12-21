package com.namoadigital.prj001.ui.act093.ui.components

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.namoadigital.prj001.R

@Composable
fun InfoItem(
    context: Context,
    modifier: Modifier = Modifier,
    overlined: String,
    ballColor: String,
    title: String,
    inputs: List<InputsModels>
) {
    with(context) {
        Row(
            modifier = modifier,
        ) {

            Canvas(modifier = Modifier.size(24.dp),
                onDraw = {
                    drawCircle(Color(ballColor.toColorInt()))
                })

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = overlined,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = Color(resources.getColor(R.color.m3_namoa_onSurfaceVariant))
                    )
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color(resources.getColor(R.color.m3_namoa_onSurface))
                    )
                )

                inputs.forEach { value ->

                    val formatSupportText = if (value.showMaterialId)
                        value.materialDesc + " (" + value.materialId + "): " + value.material_planned_qty + " " + value.unit
                    else value.materialDesc + ": " + value.material_planned_qty + " " + value.unit


                    Text(
                        text = formatSupportText,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(resources.getColor(R.color.m3_namoa_onSurfaceVariant))
                        )
                    )
                }
            }
        }
    }
}

data class InputsModels(
    val materialDesc: String,
    val materialId: String,
    val showMaterialId: Boolean,
    val material_planned_qty: Int,
    val unit: String
)
