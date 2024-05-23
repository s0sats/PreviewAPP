package com.namoadigital.prj001.model.trip.preference

import com.namoadigital.prj001.model.location.Coordinates

data class CurrentTripPrefModel(
    var customer_code: Long = -1L,
    var trip_prefix: Int = -1,
    var trip_code: Int = -1,
    var trip_scn: Int? = -1,
    var destination_seq: Int? = -1,
    var position_seq: Int = -1,
    var last_latitude: Double = 0.0,
    var last_longitude: Double = 0.0,
    var last_location_date: String? = null,
    var last_alert_type: String? = null,
    var position_counter: Int = 0,
    var transmission_counter: Int = 0,
    var isRef: Int = 0,
    var ref_latitude: Double = 0.0,
    var ref_longitude: Double = 0.0,
    var ref_location_date: String? = null,
    var transmission_date: String? = null,
    var waiting_destination_date: String? = null,
) {

    val coordinates = Coordinates(last_latitude , last_longitude, last_location_date)
    val coordinatesRef = Coordinates(ref_latitude , ref_longitude, ref_location_date)
    val isValid = customer_code != -1L && trip_prefix != -1 && trip_code != -1 && trip_scn != -1

    companion object {
        const val CUSTOMER_CODE = "customer_code"
        const val TRIP_PREFIX = "trip_prefix"
        const val TRIP_CODE = "trip_code"
        const val TRIP_SCN = "trip_scn"
        const val DESTINATION_SEQ = "destination_seq"
        const val POSITION_SEQ = "position_seq"
        const val TRIP_LAST_LATITUDE = "last_latitude"
        const val TRIP_LAST_LONGITUDE = "last_longitude"
        const val TRIP_LAST_LOCATION_DATE = "last_location_date"
        const val TRIP_LAST_ALERT_TYPE = "trip_last_alert_type"
        const val TRIP_POSITION_COUNTER = "trip_position_counter"
        const val TRIP_TRANSMISSION_COUNTER = "trip_transmission_counter"
        const val TRIP_WAITING_DESTINATION_DATE = "trip_waiting_destination_date"
        const val IS_REF = "is_ref"
        const val REF_LATITUDE = "ref_latitude"
        const val REF_LONGITUDE = "ref_longitude"
        const val REF_LOCATION_DATE = "ref_location_date"
        const val TRANSMISSION_DATE = "transmission_date"
    }
}