package com.namoadigital.prj001.extensions

import android.content.Context
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

fun Context.imgFileAbsolutePath(file_name: String?): String {
    val file = File(ConstantBaseApp.CACHE_PATH_PHOTO + "/", file_name)
    try {
        return file.absolutePath
    } catch (e: Exception) {
        ToolBox_Inf.registerException(javaClass.getName(), e)
        return ""
    }
}