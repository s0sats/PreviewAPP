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
    val comment: String?
)