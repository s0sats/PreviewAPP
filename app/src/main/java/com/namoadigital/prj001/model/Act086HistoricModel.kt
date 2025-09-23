package com.namoadigital.prj001.model

import java.io.Serializable

data class Act086HistoricModel(
    val icon: Pair<Int, Int>,
    var titleLbl: String,
    val date: String,
    val exec_type: String,
    val measureLbl: String,
    val measure: String,
    val measureItemHist: MeasureItemHist? = null,
    val materialRequestLbl: String,
    val materialAppliedLbl: String,
    val comment: String?,
    val manualInstruction: String?,
    val materialList: List<MaterialHistItemModel>?,
    val photo1: String?,
    val photo2: String?,
    val photo3: String?,
    val photo4: String?,
) : Serializable {

    data class MeasureItemHist(
        val unit: String?,
        val labelMeasure: String?,
        val labelAfterMeasure: String?,
        val initialValue: Double?,
        val initialId: String?,
        val initialAlert: Boolean?,
        val finalValue: Double?,
        val finalId: String?,
        val finalAlert: Boolean?
    ) : Serializable

}