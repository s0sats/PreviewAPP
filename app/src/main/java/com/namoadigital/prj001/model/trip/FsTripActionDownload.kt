package com.namoadigital.prj001.model.trip

data class FsTripActionDownload(
    val customerCode : Long?,
    val tripPrefix : Int?,
    val tripCode : Int?,
    val destinationSeq : Int?,
    val actionSeq : Int?,
    val fileUrl : String?,
    var fileNameLocal : String?,
    var fileNameTmp : String,
)