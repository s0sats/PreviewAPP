package com.namoadigital.prj001.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import java.io.IOException

@Throws(IOException::class)
fun getBitmapWithOrientationFixed(path: String?): Bitmap? {
    val exifReader = ExifInterface(path!!)
    val source = BitmapFactory.decodeFile(path)
    var bitmap: Bitmap? = null
    val matrix = Matrix()
    //
    when (exifReader.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)) {
        ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
        ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
    }
    //
    bitmap = Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    return bitmap
}