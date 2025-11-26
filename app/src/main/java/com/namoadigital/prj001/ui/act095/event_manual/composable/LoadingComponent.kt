package com.namoadigital.prj001.ui.act095.event_manual.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.namoa_digital.namoa_library.compose.theme.NamoaTheme

@Composable
fun CircularLoading(
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(NamoaTheme.spacing.large)
                .size(NamoaTheme.spacing.extraLarge),
            color = NamoaTheme.colors.primary,
            strokeWidth = NamoaTheme.spacing.extraExtraSmall
        )
    }

}