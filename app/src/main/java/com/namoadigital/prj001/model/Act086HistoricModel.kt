package com.namoadigital.prj001.model

data class Act086HistoricModel(
    val icon: Pair<Int, Int>,
    var titleLbl: String,
    val date: String,
    val exec_type: String,
    val measureLbl: String,
    val measure: String,
    val materialLbl: String,
    val material: String,
    val comment: String?,
    val manualInstruction: String?,
    val materialList: List<MaterialHistItemModel>?,
    val photo1: String?,
    val photo2: String?,
    val photo3: String?,
    val photo4: String?,
)