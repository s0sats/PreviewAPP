package com.namoadigital.prj001.extensions.hmaux

import com.namoa_digital.namoa_library.util.HMAux
import java.text.Collator
import java.text.Normalizer

fun sortResults(itemsForSort: ArrayList<HMAux>) {
    val collator = Collator.getInstance()

    val comparator = Comparator<HMAux> { p1, p2 ->
        //
        val score1 = p1["SCORE"]?.toIntOrNull() ?: 0
        val score2 = p2["SCORE"]?.toIntOrNull() ?: 0

        val scoreCompare = score2.compareTo(score1)
        if (scoreCompare != 0) {
            return@Comparator scoreCompare
        }

        //
        fun normalize(text: String?): String =
            Normalizer.normalize(
                text?.trim()?.lowercase() ?: "",
                Normalizer.Form.NFD
            )

        val desc1 = normalize(p1["desc"])
        val desc2 = normalize(p2["desc"])

        collator.compare(desc1, desc2)
    }
    itemsForSort.sortWith(comparator)
}