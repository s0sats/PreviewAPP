package com.namoadigital.prj001.ui.act011.finish_os.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.namoa_digital.namoa_library.compose.theme.NamoaTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinishAppBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackPressed: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "",
                    tint = NamoaTheme.colors.surface
                )
            }
        },
        title = {
            Text(
                text = title,
                color = NamoaTheme.colors.surface
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = NamoaTheme.colors.primary
        ),
    )
}