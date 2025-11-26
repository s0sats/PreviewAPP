package com.namoadigital.prj001.ui.act095.event_manual.composable.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventManualData

/**
 * Hook Compose para gerenciar PhotoController com ciclo de vida.
 *
 * Fluxo:
 * 1. Abre dialog → copia event_photo.jpg para temp_event_photo_XXX.jpg
 * 2. Usuário edita → trabalha sempre no temp
 * 3. Salva → temp substitui origin
 * 4. Cancela → temp é deletado, origin permanece intacto
 *
 * @param eventState dados do evento
 * @param enableEditMode se true, cria cópia temp ao inicializar
 */
@Composable
fun rememberPhotoController(
    eventState: EventManualData,
    enableEditMode: Boolean = true
): PhotoController {
    val context = LocalContext.current
    val controller = remember {
        PhotoController(context).apply {
            clearCameraFile()
        }
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    val fileName = eventState.photo?.localPath ?: "event_${System.currentTimeMillis()}.jpg"

    LaunchedEffect(eventState) {
        controller.initializePaths(fileName, copyToTemp = enableEditMode)
        controller.handlePhoto(
            photo = eventState.photo,
            createTempCopy = enableEditMode
        )
    }

    // Atualiza foto ao voltar da câmera (onResume)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                controller.updatePhotoFromCamera()
            }

            if (event == Lifecycle.Event.ON_DESTROY) {
                controller.clearOrphanTempFiles()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    return controller
}