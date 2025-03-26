package com.namoadigital.prj001.extensions

import android.content.Context
import com.namoa_digital.namoa_library.ctls.audio.ui.AudioRecorderFF.Companion.MEDIA_EXTENSION
import com.namoa_digital.namoa_library.util.ConstantBase
import com.namoadigital.prj001.ui.act011.Act011_Main
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.File

fun Context.copyAndCheckFile(originalFileName: String, copiedFileName: String): Boolean {
    try {
        val originalFile = File(ConstantBaseApp.CACHE_PATH_PHOTO + "/", originalFileName)
        val copiedFile = File(ConstantBaseApp.CACHE_PATH_PHOTO + "/", copiedFileName)
        //
        ToolBox_Inf.copyAndRenameFile(originalFile, copiedFile)
        //
        return ToolBox_Inf.verifyDownloadFileInf(
            copiedFileName,
            ConstantBaseApp.CACHE_PATH_PHOTO
        )
    } catch (e: java.lang.Exception) {
        ToolBox_Inf.registerException(javaClass.name, e)
        e.printStackTrace()
        return false
    }
}
//
fun Context.imgFileAbsolutePath(file_name: String?): String {
    val file = File(ConstantBaseApp.CACHE_PATH_PHOTO + "/", file_name)
    try {
        return file.absolutePath
    } catch (e: Exception) {
        ToolBox_Inf.registerException(javaClass.getName(), e)
        return ""
    }
}
//
fun hasFileForFileName(filename: String?): Boolean {
    filename?.let {
        if (filename.endsWith(Act011_Main.PNG_EXTENSION) || filename.endsWith(Act011_Main.JPG_EXTENSION) || filename.endsWith(MEDIA_EXTENSION)) {
            val sFile = File(ConstantBase.CACHE_PATH_PHOTO + "/" + filename)
            if (sFile.exists()
                && sFile.isFile
            ) {
                return true
            }
        }
    }
    return false
}
