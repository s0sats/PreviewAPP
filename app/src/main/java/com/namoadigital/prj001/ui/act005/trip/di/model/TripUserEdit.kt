package com.namoadigital.prj001.ui.act005.trip.di.model

data class TripUserEdit(
    val userCode: Int,
    val userNick: String,
    val userName: String,
    val userSeq: Int? = null,
    val dateStart: String? = null,
    val dateEnd: String? = null
){

    val isFinalized = !dateEnd.isNullOrEmpty()

}