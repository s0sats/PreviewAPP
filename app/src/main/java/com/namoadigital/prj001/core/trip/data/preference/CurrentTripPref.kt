package com.namoadigital.prj001.core.trip.data.preference

import android.content.Context
import android.content.SharedPreferences
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.core.data.domain.preference.ModelPreferences
import com.namoadigital.prj001.model.trip.preference.CurrentTripPrefModel

class CurrentTripPref constructor(
    private val pref: SharedPreferences
) : ModelPreferences<CurrentTripPrefModel> {
    override fun write(model: CurrentTripPrefModel) {
        with(pref) {
            edit()
                .putLong(
                    CurrentTripPrefModel.CUSTOMER_CODE,
                    model.customer_code ?: -1L
                )
                .putInt(
                    CurrentTripPrefModel.TRIP_PREFIX,
                    model.trip_prefix ?: -1
                )
                .putInt(
                    CurrentTripPrefModel.TRIP_CODE,
                    model.trip_code ?: -1
                )
                .putInt(
                    CurrentTripPrefModel.TRIP_SCN,
                    model.trip_scn ?: -1
                )
                .putInt(
                    CurrentTripPrefModel.DESTINATION_SEQ,
                    model.destination_seq ?: -1
                )
                .putInt(
                    CurrentTripPrefModel.POSITION_SEQ,
                    model.position_seq ?: -1
                )
                .putString(
                    CurrentTripPrefModel.TRIP_LAST_LATITUDE,
                    model.last_latitude.toString()
                )
                .putString(
                    CurrentTripPrefModel.TRIP_LAST_LONGITUDE,
                    model.last_longitude.toString()
                )
                .putString(
                    CurrentTripPrefModel.TRIP_LAST_LOCATION_DATE,
                    model.last_location_date
                )
                .putString(
                    CurrentTripPrefModel.TRIP_LAST_ALERT_TYPE,
                    model.last_alert_type
                )
                .putInt(
                    CurrentTripPrefModel.TRIP_POSITION_COUNTER,
                    model.position_counter
                )
                .putInt(
                    CurrentTripPrefModel.TRIP_TRANSMISSION_COUNTER,
                    model.transmission_counter
                )
                .putInt(
                    CurrentTripPrefModel.IS_REF,
                    model.isRef
                ).putString(
                    CurrentTripPrefModel.REF_LATITUDE,
                    model.ref_latitude.toString()
                )
                .putString(
                    CurrentTripPrefModel.REF_LONGITUDE,
                    model.ref_longitude.toString()
                )
                .putString(
                    CurrentTripPrefModel.REF_LOCATION_DATE,
                    model.ref_location_date
                )
                 .putString(
                    CurrentTripPrefModel.TRANSMISSION_DATE,
                    model.transmission_date
                )
                .putString(
                    CurrentTripPrefModel.TRIP_WAITING_DESTINATION_DATE,
                    model.waiting_destination_date
                )
                .apply()
        }
    }

    override fun read(): CurrentTripPrefModel {
        with(pref) {
            return CurrentTripPrefModel(
                getLong(
                    CurrentTripPrefModel.CUSTOMER_CODE,
                    -1
                ),
                getInt(
                    CurrentTripPrefModel.TRIP_PREFIX,
                    -1
                ),
                getInt(
                    CurrentTripPrefModel.TRIP_CODE,
                    -1
                ),
                getInt(
                    CurrentTripPrefModel.TRIP_SCN,
                    -1
                ),
                getInt(
                    CurrentTripPrefModel.DESTINATION_SEQ,
                    -1
                ),
                getInt(CurrentTripPrefModel.POSITION_SEQ, -1),
                getString(CurrentTripPrefModel.TRIP_LAST_LATITUDE, "0.0")?.toDouble() ?: 0.0,
                getString(CurrentTripPrefModel.TRIP_LAST_LONGITUDE, "0.0")?.toDouble() ?: 0.0,
                getString(CurrentTripPrefModel.TRIP_LAST_LOCATION_DATE, null),
                getString(CurrentTripPrefModel.TRIP_LAST_ALERT_TYPE, null),
                getInt(CurrentTripPrefModel.TRIP_POSITION_COUNTER, 0),
                getInt(CurrentTripPrefModel.TRIP_TRANSMISSION_COUNTER, 0),
                getInt(CurrentTripPrefModel.IS_REF, 0),
                getString(CurrentTripPrefModel.REF_LATITUDE, "0.0")?.toDouble() ?: 0.0,
                getString(CurrentTripPrefModel.REF_LONGITUDE, "0.0")?.toDouble() ?: 0.0,
                getString(CurrentTripPrefModel.REF_LOCATION_DATE, null),
                getString(CurrentTripPrefModel.TRANSMISSION_DATE, null),
                getString(CurrentTripPrefModel.TRIP_WAITING_DESTINATION_DATE, null),
            )
        }
    }
    fun clear(){
        pref.edit().clear().apply()
    }

    companion object INSTANCE {

        operator fun invoke(context: Context) =
            CurrentTripPref(
                context.getSharedPreferences(
                    "current_trip",
                    Base_Activity.MODE_PRIVATE
                )
            )

    }

}