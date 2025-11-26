package com.namoadigital.prj001.ui.act095.event_manual.composable.controller

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.namoa_digital.namoa_library.util.ConstantBase
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventManualData.PhotoData
import com.namoadigital.prj001.util.ConstantBaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

/**
 * Controller para gerenciamento de fotos.
 *
 * Fluxo:
 * 1. Abre dialog com event_photo.jpg → carrega origin
 * 2. Usuário troca foto → salva em temp_event_photo_NOVO.jpg
 * 3. Usuário salva → deleta event_photo.jpg, renomeia temp para event_photo.jpg
 * 4. Próxima abertura → carrega a nova event_photo.jpg
 *
 * Recomendado para ser controlado pelo ViewModel.
 */
class PhotoController(private val context: Context) {

    var photoBitmap by mutableStateOf<Bitmap?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var downloadError by mutableStateOf(false)
        private set

    fun hasBitmap() = photoBitmap != null
    private var originFile: File? = null      // event_photo.jpg (atual/persistido)
    private var tempFile: File? = null        // temp_event_photo_NOVO.jpg (edição)
    private var lastModifiedTime: Long? = null

    /**
     * Inicializa os caminhos e cria uma cópia temp da foto origin.
     * Isso permite editar sem comprometer o original até salvar.
     *
     * @param fileName nome do arquivo origin (ex: "event_photo.jpg")
     * @param copyToTemp se true, copia origin para temp imediatamente
     */
    fun initializePaths(fileName: String, copyToTemp: Boolean = false) {
        val cacheDir = File(ConstantBase.CACHE_PATH_PHOTO)
        cacheDir.mkdirs()

        originFile = File(cacheDir, fileName)

        if (copyToTemp && originFile?.exists() == true) {
            tempFile = File(cacheDir, "temp_${fileName}")

            try {
                originFile!!.copyTo(tempFile!!, overwrite = true)
                lastModifiedTime = tempFile?.lastModified()
            } catch (e: Exception) {
                e.printStackTrace()
                tempFile = null
            }
        } else {
            tempFile = null
        }
    }

    /**
     * Gerencia o carregamento inicial da foto.
     * Se não existir, tenta baixar com o URL.
     *
     * @param createTempCopy se true, cria uma cópia temp do origin para edição
     */
    fun handlePhoto(
        photo: PhotoData?,
        createTempCopy: Boolean = false,
        onRequireNewPhoto: (() -> Unit)? = null
    ) {
        if (photo == null) {
            onRequireNewPhoto?.invoke()
            return
        }

        isLoading = true


        val cacheDir = File(ConstantBase.CACHE_PATH_PHOTO)
        val localFile = photo.localPath?.let { File(cacheDir, it) }

        when {
            localFile?.exists() == true -> {
                initializePaths(localFile.name, copyToTemp = createTempCopy)
                isLoading = false
                if (createTempCopy && tempFile?.exists() == true) {
                    loadPhotoFromTemp()
                } else {
                    loadExistingPhoto()
                }
            }

            !photo.url.isNullOrEmpty() && !photo.name.isNullOrEmpty() -> {
                initializePaths(photo.name, copyToTemp = false)
                downloadRemotePhoto(photo.url, originFile!!)
            }

            else -> {
                isLoading = false
                onRequireNewPhoto?.invoke()
            }
        }
    }

    /**
     * Carrega uma foto existente do origin.
     */
    private fun loadExistingPhoto() {
        originFile?.let { file ->
            if (file.exists()) {
                photoBitmap = BitmapFactory.decodeFile(file.path)
                lastModifiedTime = file.lastModified()
                isLoading = false

            }
        }
    }

    /**
     * Usuário quer trocar a foto (câmera ou galeria).
     * Cria um novo arquivo temp com timestamp único.
     * Se já existe temp (cópia do origin), substitui ele.
     */
    fun createNewPhotoTemp(): String? {
        if (originFile == null) return null

        val cacheDir = originFile!!.parentFile ?: return null
        val baseFileName = originFile!!.nameWithoutExtension
        val timestamp = System.currentTimeMillis()

        // Deleta temp anterior se existir
        tempFile?.delete()

        tempFile = File(cacheDir, "temp_${baseFileName}_${timestamp}.jpg")

        return tempFile?.name
    }

    /**
     * Carrega a foto capturada de temp (após voltar da câmera).
     */
    fun loadPhotoFromTemp() {
        if (tempFile?.exists() != true) {
            return
        }

        tempFile?.let { file ->
            val bmp = BitmapFactory.decodeFile(file.path)
            bmp?.let {
                photoBitmap = it
                lastModifiedTime = file.lastModified()
                isLoading = false
            }
        }
    }

    /**
     * Atualiza a foto se foi modificada na câmera (onResume).
     * Só recarrega se o arquivo mudou desde a última leitura.
     */
    fun updatePhotoFromCamera() {
        if (tempFile?.exists() != true) {
            isLoading = false
            return
        }

        val lastModified = tempFile?.lastModified() ?: return

        if (lastModifiedTime != lastModified) {
            lastModifiedTime = lastModified
            loadPhotoFromTemp()
        }
    }

    /**
     * Salva a foto:
     * - Se temp existe → deleta origin, move temp para origin, retorna PhotoData
     * - Se temp não existe → retorna null (nada mudou)
     */
    fun saveImageToOrigin(): PhotoData? {
        return try {
            if (tempFile?.exists() != true || originFile == null) {
                return null
            }

            // Delete old origin
            if (originFile!!.exists()) {
                originFile!!.delete()
            }

            // Move temp para origin
            tempFile!!.renameTo(originFile!!)
            lastModifiedTime = originFile?.lastModified()
            tempFile = null

            PhotoData(
                localPath = originFile?.name,
                isChangedPhoto = true
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Descarta mudanças e restaura do origin.
     * Útil quando usuário cancela a edição.
     */
    fun restoreFromOrigin() {
        // Deleta temp
        tempFile?.delete()
        tempFile = null

        // Recarrega do origin
        if (originFile?.exists() == true) {
            photoBitmap = BitmapFactory.decodeFile(originFile!!.path)
            lastModifiedTime = originFile?.lastModified()
        } else {
            photoBitmap = null
            lastModifiedTime = null
        }
    }

    /**
     * Remove a foto sendo editada (descarta mudanças).
     * Só deleta temp, origin continua intacto.
     */
    fun clearTempPhoto() {
        photoBitmap = null
        tempFile?.delete()
        tempFile = null
        lastModifiedTime = null
    }

    /**
     * Remove a foto completamente (origin + temp).
     * Usado quando usuário remove a foto do evento.
     */
    fun clearAllPhoto() {
        photoBitmap = null
        tempFile?.delete()
        tempFile = null
        originFile?.delete()
        originFile = null
        lastModifiedTime = null
    }

    fun removePhotoBitmap() {
        photoBitmap = null
    }

    /**
     * Remove todos os arquivos temporários "temp_" do cache.
     * Executar ao descartar a tela (cleanup).
     */
    fun clearOrphanTempFiles() {
        try {
            val cacheDir = File(ConstantBase.CACHE_PATH_PHOTO)
            cacheDir.listFiles()?.forEach { file ->
                if (file.name.startsWith("temp_")) {
                    file.delete()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Retorna o melhor arquivo disponível para preview.
     * Prioriza temp (em edição) sobre origin (salvo).
     */
    fun bestPreviewFile(): File? {
        return when {
            tempFile?.exists() == true -> tempFile
            originFile?.exists() == true -> originFile
            else -> null
        }
    }

    fun bestPreviewPath(): String? = bestPreviewFile()?.path
    fun bestPreviewName(): String? = bestPreviewFile()?.name

    /**
     * Baixa uma imagem remota e salva diretamente em origin.
     */
    private fun downloadRemotePhoto(url: String, targetFile: File) {

        Glide.with(context)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            targetFile.outputStream().use { output ->
                                resource.compress(Bitmap.CompressFormat.JPEG, 100, output)
                            }
                            lastModifiedTime = targetFile.lastModified()
                            photoBitmap = resource
                            downloadError = false
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        isLoading = false
                    }
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    isLoading = false
                    downloadError = true
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    fun clearDownloadError() {
        downloadError = false
    }

    fun retryDownload(photo: PhotoData?) {
        if (photo?.url != null && originFile != null) {
            downloadRemotePhoto(photo.url, originFile!!)
        }
    }

    fun hasValidPhoto(): Boolean {
        return photoBitmap != null
    }

    fun hasDownloadError(): Boolean {
        return downloadError
    }

    /**
     * Remove o arquivo de câmera.
     */
    fun clearCameraFile() {
        try {
            val camFile = File(ConstantBaseApp.CAM_TEST_PATH, "img.jpg")
            if (camFile.exists()) {
                camFile.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}