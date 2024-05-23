package com.namoadigital.prj001.service.location.util

import com.namoadigital.prj001.extensions.roundOffDecimal
import com.namoadigital.prj001.model.location.Coordinates
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Inf
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Calculates the distance between two geographical coordinates.
 *
 * @param start The starting coordinates.
 * @param destination The destination coordinates.
 * @return The distance between the two coordinates in kilometers.
 */
fun calculateDistance(
    start: Coordinates,
    destination: Coordinates
): Double {
    // Value of PI\
    val pi = 3.141592653589793238462643383279502884197169399375
    val vPi = pi / 180
    return roundOffDecimal(
        abs(
            6371 * acos(
                cos(vPi * (90 - destination.latitude)) * cos((90 - start.latitude) * vPi) +
                        sin((90 - destination.latitude) * vPi) * sin((90 - start.latitude) * vPi) *
                        cos((start.longitude - destination.longitude) * vPi)
            )
        )
    )
}

fun calculateDistanceHaversine(
    start: Coordinates,
    destination: Coordinates
): Double {
    // Value of PI\
    val pi = 3.141592653589793238462643383279502884197169399375
    val vPi = pi / 180
    // Convert latitude and longitude from degrees to radians
    val lat1 = Math.toRadians(start.latitude)
    val lon1 = Math.toRadians(start.longitude)
    val lat2 = Math.toRadians(destination.latitude)
    val lon2 = Math.toRadians(destination.longitude)

    // Calculate differences in latitude and longitude
    val dlat = lat2 - lat1
    val dlon = lon2 - lon1
//    //Revisar formula da distancia. todo17032024
//    return abs(
//        6371 * acos(
//            cos(vPi * (90 - destination.latitude)) * cos((90 - start.latitude) * vPi) +
//                    sin((90 - destination.latitude) * vPi) * sin((90 - start.latitude) * vPi) *
//                    cos((start.longitude - destination.longitude) * vPi
//                    )
//        )
//    )

    // Haversine formula
    val a = sin(dlat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(dlon / 2).pow(2)
    val c = 2 * asin(sqrt(a))

    // Earth radius in kilometers
    val radius = 6371.0

    // Calculate the distance
    return roundOffDecimal(radius * c)
}


fun calculateSpeed(currentLocation: Coordinates, lastLocation: Coordinates): Double {
    val format = SimpleDateFormat(Constant.FULL_TIMESTAMP_TZ_FORMAT_GMT, Locale.getDefault())
    try {
        val date1 = format.parse(lastLocation.date!!)
        val date2 = format.parse(currentLocation.date!!)

        val timeInMillis1 = date1?.time ?: 0
        val timeInMillis2 = date2?.time ?: 0

        val distance = calculateDistance(currentLocation, lastLocation) * 1000
        val timeInSeconds = roundOffDecimal((timeInMillis2 - timeInMillis1).div(1000.0), "0.0")

        if (timeInSeconds > 0.0) {
            return roundOffDecimal((distance * 3.6) / timeInSeconds)
        }
        return 0.0
    } catch (e: Exception) {
        ToolBox_Inf.registerException("TrackingUtility - Speed", e)
        return 0.0
    }
}

fun checkDepartedDistance(
    originCoordinates: Coordinates,
    currentCoordinates: Coordinates,
    minDistance: Double,
): Boolean {
    val calculateDistance = calculateDistance(
        originCoordinates,
        currentCoordinates
    )
    return calculateDistance.absoluteValue > minDistance
}

fun checkArrivedDistance(
    originCoordinates: Coordinates,
    currentCoordinates: Coordinates,
    minDistance: Double,
): Boolean {
    val calculateDistance = calculateDistance(
        originCoordinates,
        currentCoordinates
    )
    return calculateDistance.absoluteValue <= minDistance
}
