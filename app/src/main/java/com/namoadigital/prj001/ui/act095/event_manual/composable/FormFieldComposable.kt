package com.namoadigital.prj001.ui.act095.event_manual.composable

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.namoa_digital.namoa_library.compose.theme.NamoaTheme
import com.namoadigital.prj001.core.translate.TranslateMap
import com.namoadigital.prj001.core.translate.textOf
import com.namoadigital.prj001.ui.act095.event_manual.translate.EventManualKey

@Composable
fun EventFormField(
    modifier: Modifier = Modifier,
    hint: String,
    value: String?,
    onValueChange: (String) -> Unit,
    isRequired: Boolean,
    isError: Boolean,
    errorMessage: String = "",
    singleLine: Boolean = true,
    maxLines: Int = 20,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    icon: ImageVector
) {
    OutlinedTextField(
        modifier = modifier,
        value = value.orEmpty(),
        onValueChange = onValueChange,
        label = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(hint)
                if (isRequired) {
                    Text(" *", color = Color.Red)
                }
            }
        },
        leadingIcon = { Icon(imageVector = icon, contentDescription = null) },
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        maxLines = maxLines,
        isError = isError,
        supportingText = {
            if (isError) Text(errorMessage, color = Color.Red)
        }
    )
}


@Composable
fun PhotoSection(
    translateMap: TranslateMap,
    photoBitmap: Bitmap?,
    isLoading: Boolean = false,
    downloadError: Boolean = false,
    onAddPhoto: () -> Unit,
    onPreviewPhoto: () -> Unit,
    onRemove: () -> Unit,
    onRetryDownload: () -> Unit = {},
    isError: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = NamoaTheme.spacing.small),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            color = NamoaTheme.colors.surfaceVariant,
                            shape = RoundedCornerShape(NamoaTheme.spacing.small)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(NamoaTheme.spacing.small)
                    ) {
                        CircularProgressIndicator(
                            color = NamoaTheme.colors.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = translateMap.textOf(EventManualKey.DownloadingPhotoLbl),
                            style = NamoaTheme.typography.bodyMedium,
                            color = NamoaTheme.colors.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            downloadError -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = NamoaTheme.colors.surfaceVariant,
                            shape = RoundedCornerShape(NamoaTheme.spacing.small)
                        )
                        .border(
                            width = 1.dp,
                            color = NamoaTheme.colors.error,
                            shape = RoundedCornerShape(NamoaTheme.spacing.small)
                        )
                        .padding(NamoaTheme.spacing.medium)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(NamoaTheme.spacing.small)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Warning,
                            contentDescription = null,
                            tint = NamoaTheme.colors.error,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = translateMap.textOf(EventManualKey.NoInternetPhotoLbl),
                            style = NamoaTheme.typography.bodySmall,
                            color = Color(0xFFE65100),
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = onRetryDownload,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NamoaTheme.colors.error.copy(
                                    alpha = 0.8f
                                )
                            ),
                            modifier = Modifier.padding(top = NamoaTheme.spacing.small)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(NamoaTheme.spacing.extraSmall))
                            Text(translateMap.textOf(EventManualKey.RetryButtonLbl))
                        }
                    }
                }
            }

            // Estado: Foto carregada
            photoBitmap != null && !isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(NamoaTheme.spacing.small))
                        .clickable(onClick = onPreviewPhoto)
                ) {
                    Image(
                        bitmap = photoBitmap.asImageBitmap(),
                        contentDescription = "Foto do evento",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(NamoaTheme.spacing.small)
                            .background(
                                color = Color.Black.copy(alpha = 0.5f),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remover foto",
                            tint = Color.White
                        )
                    }
                }
            }

            // Estado: Sem foto
            else -> {
                Button(
                    onClick = onAddPhoto,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isError)
                            Color(0xFFFFEBEE)
                        else
                            NamoaTheme.colors.surfaceVariant
                    ),
                    border = if (isError) BorderStroke(1.dp, Color.Red)
                    else null
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = null,
                        tint = if (isError) Color.Red else NamoaTheme.colors.primary
                    )
                    Spacer(modifier = Modifier.width(NamoaTheme.spacing.small))
                    Text(
                        text = translateMap.textOf(EventManualKey.AddPhotoBtn),
                        color = if (isError) Color.Red else NamoaTheme.colors.onSurfaceVariant
                    )
                }

                if (isError) {
                    Text(
                        text = translateMap.textOf(EventManualKey.ErrorRequiredFieldMsg),
                        style = NamoaTheme.typography.bodySmall,
                        color = Color.Red,
                        modifier = Modifier.padding(top = NamoaTheme.spacing.extraSmall)
                    )
                }
            }
        }
    }
}



