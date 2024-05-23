package com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util

sealed class SaveDestinationEdit {

    data class ALL(
        val dateStart: String,
        val dateEnd: String,
        val odometer: Long?,
        val destinationSeq: Int,
        val photoUpdate: PhotoUpdate
    ) : SaveDestinationEdit()

    data class DATE(
        val dateStart: String,
        val dateEnd: String,
        val destinationSeq: Int,
    ) : SaveDestinationEdit()

    data class ODOMETER(
        val odometer: Long?,
        val photoUpdate: PhotoUpdate,
        val destinationSeq: Int,
    ) : SaveDestinationEdit()

    object NOTHING : SaveDestinationEdit()

}