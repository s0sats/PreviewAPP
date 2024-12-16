package com.namoadigital.prj001.ui.act011.finish_os.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.namoadigital.prj001.design.compose.ApplicationTheme


@Composable
fun TitleSection(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        style = ApplicationTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold
        ),
        color = ApplicationTheme.colors.onSurface
    )
}
