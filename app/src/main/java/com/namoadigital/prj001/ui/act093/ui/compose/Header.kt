package com.namoadigital.prj001.ui.act093.ui.compose

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.namoadigital.prj001.R

@Composable
fun Header(
    context: Context,
    modifier: Modifier = Modifier,
    colorSerial: String? = null,
    serialId: String? = null,
    productId: String? = null,
    marcaModel: String? = null,
    trackings: String? = null,
) {

    Column(
        modifier = modifier
            .padding(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            colorSerial?.let {
                Icon(
                    imageVector = Icons.Filled.ArrowRight,
                    contentDescription = null,
                    tint = Color(colorSerial.toColorInt())
                )
            }

            serialId?.let {
                Text(
                    text = serialId,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        productId?.let {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = productId,
                style = MaterialTheme.typography.bodySmall,
                color = Color(context.resources.getColor(R.color.m3_namoa_onSurfaceVariant))
            )
        }

        marcaModel?.let {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = marcaModel,
                style = MaterialTheme.typography.bodySmall,
                color = Color(context.resources.getColor(R.color.m3_namoa_onSurfaceVariant))
            )
        }

        trackings?.let {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                text = trackings,
                style = MaterialTheme.typography.bodySmall,
                color = Color(context.resources.getColor(R.color.m3_namoa_onSurfaceVariant))
            )
        }

    }

}