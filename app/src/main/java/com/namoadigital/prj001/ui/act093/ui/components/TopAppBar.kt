package com.namoadigital.prj001.ui.act093.ui.components

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.namoadigital.prj001.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NamoaAppBar(
    context: Context,
    title: @Composable () -> Unit,
    onBack: (() -> Unit)? = null
) {
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color(context.resources.getColor(R.color.m3_namoa_primary)),
            titleContentColor = Color.White
        ),
        title = title,
        navigationIcon = {
            onBack?.let {
                NamoaIconButton(
                    modifier = Modifier.padding(start = 3.dp),
                    icon = Icons.Filled.ArrowBack,
                    color = Color.White
                ) {
                    it()
                }
            }
        }
    )
}