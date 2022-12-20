package com.namoadigital.prj001.ui.act093.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector


@Composable
fun NamoaIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    color: Color = MaterialTheme.colorScheme.primary,
    description: String? = null,
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick) {
        Icon(
            modifier = modifier,
            imageVector = icon,
            contentDescription = description ?: "Localized icon",
            tint = color,
        )
    }
}