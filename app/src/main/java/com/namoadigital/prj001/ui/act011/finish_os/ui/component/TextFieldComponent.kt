package com.namoadigital.prj001.ui.act011.finish_os.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import com.namoa_digital.namoa_library.compose.theme.NamoaTheme

@Composable
fun TextFieldIconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    value: String,
    label: String? = null,
    isEnabled: Boolean = true,
    isReadOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    icon: Painter? = null,
    onValueChange: (String) -> Unit,
    onIconClick: (() -> Unit)? = null,
    onButtonClick: () -> Unit,
) {

    Row(
        modifier = modifier.padding(start = NamoaTheme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(if (!isReadOnly) 1f else 0.8f),
            value = value,
            onValueChange = onValueChange,
            label = { label?.let { Text(text = label) } },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = NamoaTheme.colors.surface,
                unfocusedContainerColor = NamoaTheme.colors.surface,
                disabledContainerColor = NamoaTheme.colors.surface,
                errorContainerColor = NamoaTheme.colors.surface,
                focusedLabelColor = NamoaTheme.colors.primary
            ),
            singleLine = true,
            readOnly = isReadOnly,
            enabled = isEnabled,
            keyboardOptions = keyboardOptions,
            leadingIcon = {
                icon?.let {
                    IconButton(onClick = {
                        onIconClick?.invoke()
                    }) {
                        Icon(painter = it, contentDescription = null)
                    }
                }
            }
        )

        if (!isReadOnly) {
            Spacer(modifier = Modifier.width(NamoaTheme.spacing.mediumLarge))

            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .clip(CircleShape)
                    .border(
                        width = NamoaTheme.spacing.extraSmall,
                        color = NamoaTheme.colors.primary,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center,
            ) {
                IconButton(
                    enabled = isEnabled,
                    onClick = onButtonClick
                ) {
                    Icon(
                        imageVector = imageVector,
                        contentDescription = null,
                        tint = if (isEnabled) NamoaTheme.colors.surface else Color.Gray
                    )
                }
            }
        }
    }

}