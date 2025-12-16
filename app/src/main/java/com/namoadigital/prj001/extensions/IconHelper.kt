package com.namoadigital.prj001.extensions

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.graphics.drawable.PictureDrawable
import android.util.Base64
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.caverock.androidsvg.SVG
import com.namoadigital.prj001.util.ToolBox_Inf

fun applySvgFromBase64(context: Context, base64: String, @ColorRes color:Int?, width: Int = 24, height: Int = 24): Drawable? {
    try {
        // 1) Decodifica Base64  bytes
        val svgBytes = Base64.decode(base64, Base64.DEFAULT)
        val svgString = String(svgBytes, Charsets.UTF_8)
        val formattedSvg = color?.let{
            svgString.replace("#848484", getColorHex(context, color))
        }?: svgString
        // 2) Converte string SVG  PictureDrawable
        val svg = SVG.getFromString(formattedSvg)
        svg.documentWidth = width.dp.toFloat()
        svg.documentHeight = height.dp.toFloat()
        val picture = svg.renderToPicture()
        val drawable = PictureDrawable(picture)
        // 3) Define bounds para evitar problemas de layout
        drawable.setBounds(0, 0, width.dp, height.dp)
        return drawable
    } catch (e: Exception) {
        ToolBox_Inf.registerException("RadioGroup.setIconsFromBase64", e)
        e.printStackTrace()
    }
    return null
}

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun getColorHex(context: Context, @ColorRes colorResId: Int): String {
    val colorInt = ContextCompat.getColor(context, colorResId)
    // Formata o inteiro da cor para o formato hexadecimal #RRGGBB, ignorando o canal alfa.
    val hex = String.format("#%06X", 0xFFFFFF and colorInt)
    return hex
}