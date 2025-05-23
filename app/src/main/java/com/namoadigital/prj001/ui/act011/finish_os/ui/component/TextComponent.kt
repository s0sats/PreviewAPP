package com.namoadigital.prj001.ui.act011.finish_os.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.namoa_digital.namoa_library.compose.theme.NamoaTheme


@Composable
fun TitleSection(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        style = NamoaTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold
        ),
        color = NamoaTheme.colors.onSurface
    )
}
