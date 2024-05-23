package com.namoadigital.prj001.extensions

import android.content.ActivityNotFoundException
import android.content.Context
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.Log
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.File
import java.io.IOException

fun Context.openFormPDF( pdfUrlLocal: String, notFoundDialog: ()-> Unit, errorDialog: ()-> Unit) {
    val pdfFile = File(ConstantBaseApp.CACHE_PATH + "/" + pdfUrlLocal.toLowerCase())
    Log.d("TRIP_FILE", "f.exists(): " + pdfFile.path)
    if (!isValidPDF(pdfFile)){
        notFoundDialog()
    }else {
        try {
            ToolBox_Inf.deleteAllFOD(Constant.CACHE_PDF)
            ToolBox_Inf.copyFile(
                pdfFile,
                File(Constant.CACHE_PDF)
            )
        } catch (e: Exception) {
            ToolBox_Inf.registerException(this.javaClass.name, e)
        }

        val pdfIntent =
            ToolBox_Inf.getOpenPdfIntent(this, ConstantBaseApp.CACHE_PDF + "/" + pdfUrlLocal)
        //
        try {
            this.startActivity(pdfIntent)
        } catch (e: ActivityNotFoundException) {
            ToolBox_Inf.registerException(this.javaClass.name, e)
            //
            errorDialog()
        }
    }
}

fun isValidPDF(file: File?): Boolean {
    Log.d("TRIP_FILE", "---------isValidPDF-----------")
    //
    try {
        Log.d(
            "TRIP_FILE",
            "file: " + file?.path
        )

        // Abra o arquivo PDF com o PdfRenderer
        val parcelFileDescriptor =
            ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        val renderer = PdfRenderer(parcelFileDescriptor)

        // Verifique se o PDF possui pelo menos uma página
        if (renderer.pageCount > 0) {
            renderer.close()
            Log.d("TRIP_FILE", "Return TRUE")
            return true
        }
        renderer.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    Log.d("TRIP_FILE", "Return FALSE")
    return false
}