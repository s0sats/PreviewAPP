package com.namoadigital.prj001.extensions

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import java.text.Normalizer
import java.util.regex.Pattern


object SpannableStringStyle {

    var customText: List<String?>? = null

    fun spanStyleWith(text: String, block: SpannableString.() -> Unit) : SpannableString {
        return SpannableString(text).also { block(it) }
    }


    inline fun SpannableString.applyColor(color: () -> Int){
        customText?.forEach { text ->
            text?.let {
                setSpan(
                    ForegroundColorSpan(color()),
                    (this.indexOf(it)),
                    (this.indexOf(it) + it.length),
                    SpannableString.SPAN_INCLUSIVE_INCLUSIVE
                )
            }
        }
    }

    inline fun SpannableString.fontSize(textSize: () -> Float){
        val size = textSize()
        customText?.forEach { text ->
            text?.let {
                setSpan(
                    RelativeSizeSpan(size),
                    (this.indexOf(it)),
                    (this.indexOf(it) + it.length),
                    SpannableString.SPAN_INCLUSIVE_INCLUSIVE
                )
            }
        }
    }

    inline fun SpannableString.textStyle(textStyle: () -> Int){
        customText?.forEach { text ->
            text?.let {
                setSpan(
                    StyleSpan(textStyle()),
                    (this.indexOf(it)),
                    (this.indexOf(it) + it.length),
                    SpannableString.SPAN_INCLUSIVE_INCLUSIVE
                )
            }
        }
    }

}

fun highlightText(desc: String, filteredWords: List<String>): SpannableString {
    val spannable = SpannableString(desc)
    val descNormalized = desc.removeAccents()
    // Para cada palavra da lista
    filteredWords.filter { it.isNotEmpty() }.forEach { words ->

        val wordsNormalized = words.removeAccents()

        // Cria um padrão case-insensitive para encontrar a wordsNormalized
        val pattern = Pattern.compile(Pattern.quote(wordsNormalized), Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(descNormalized)

        // Encontra todas as ocorrências da wordsNormalized na descrição
        while (matcher.find()) {
            val inicio = matcher.start()
            val fim = matcher.end()

            // Aplica negrito na wordsNormalized encontrada
            spannable.setSpan(
                BackgroundColorSpan(
                    Color.parseColor("#80FFFF00")),
                    inicio,
                    fim,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
        }
    }

    return spannable
}
fun String.removeAccents(): String {
    val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
    return normalized.replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")
}
