package com.namoadigital.prj001.service.location.util

object LocationServiceConstants {

    //Actions
    const val START_LOCATION = "ACTION_START_OR_RESUME_LOCATION"
    const val STOP_LOCATION = "ACTION_STOP_LOCATION"
    const val GET_LOCATION = "ACTION_GET_LOCATION"
    const val WAITING_DESTINATION = "WAITING_DESTINATION"
    const val DEPARTED = "DEPARTED"
    const val ARRIVED = "ARRIVED"

    //Location Service
    const val LOCATION_INTERVAL = 60 * 1000L//60000L

    //Notification
    const val NOTIFICATION_TRACKING_CHANNEL = "TRACKING_CHANNEL"
    const val NOTIFICATION_TRACKING_NAME = "TRACKING"
    const val NOTIFICATION_TRACKING_ID = 500
}
