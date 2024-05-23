package com.namoadigital.prj001.service.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.core.util.WsTypeStatus
import com.namoadigital.prj001.core.util.sendBCStatus
import com.namoadigital.prj001.extensions.getResourceCode
import com.namoadigital.prj001.model.TranslateResource
import com.namoadigital.prj001.model.location.Coordinates
import com.namoadigital.prj001.model.trip.TripPositionAlertType
import com.namoadigital.prj001.model.trip.toAlertType
import com.namoadigital.prj001.service.location.usecase.PositionUseCase
import com.namoadigital.prj001.service.location.util.LocationServiceConstants.GET_LOCATION
import com.namoadigital.prj001.service.location.util.LocationServiceConstants.LOCATION_INTERVAL
import com.namoadigital.prj001.service.location.util.LocationServiceConstants.NOTIFICATION_TRACKING_CHANNEL
import com.namoadigital.prj001.service.location.util.LocationServiceConstants.NOTIFICATION_TRACKING_ID
import com.namoadigital.prj001.service.location.util.LocationServiceConstants.NOTIFICATION_TRACKING_NAME
import com.namoadigital.prj001.service.location.util.LocationServiceConstants.START_LOCATION
import com.namoadigital.prj001.service.location.util.LocationServiceConstants.STOP_LOCATION
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripBaseFragment
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


@AndroidEntryPoint
class FsTripLocationService : LifecycleService() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var positionUseCase: PositionUseCase

    private lateinit var curNotificationBuilder: NotificationCompat.Builder

    private var isFirstRun = true
    private var serviceKilled = false
    private var isGetLocation = false

    private val hmAuxTrans by lazy {
        loadTranslate(applicationContext)
    }
    private val locationCallback = object : LocationCallback() {

        override fun onLocationAvailability(availability: LocationAvailability) {
            super.onLocationAvailability(availability)
        }

        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            //
            if (isTracking.value) {
                result.locations.let { loc ->
                    locationFound = true
                    handler.removeCallbacks(handlerGetLocationTimeout)
                    for (location in loc) {
                        LatLog.value = Coordinates(location.latitude, location.longitude)
                    }
                    if (!isGetLocation) {
                        positionUseCase.checkStatusLocation(LatLog.value)
                    } else {
                        val gson = GsonBuilder().create()
                        /*
                            Usado para pegar a primeira posicao na criacao de viagem
                         */
                        isGetLocation = false
                        ToolBox.sendBCStatus(
                            applicationContext,
                            "CLOSE_ACT",
                            hmAuxTrans[TRIP_GPS_LOCATION_ACQUIRED],
                            HMAux(),
                            gson.toJson(LatLog.value),
                            "0"
                        )
                    }
                }
            }
        }
    }
    private var locationFound = false
    private var handler = Handler()
    private val handlerGetLocationTimeout = Runnable {
        if (!locationFound) {
            sendBCStatus(
                WsTypeStatus.CUSTOM_ERROR(
                    message = hmAuxTrans[TRIP_GPS_LOCATION_NOT_ACQUIRED]
                        ?: TRIP_GPS_LOCATION_NOT_ACQUIRED,
                    value = "data is null",
                    required = "0"
                )
            )
            updateTracking(false)
            killService()
        }
    }

    override fun onCreate() {
        super.onCreate()
        curNotificationBuilder = baseNotificationBuilder
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        isTracking.value = true
    }

    private fun killService() {
        isFirstRun = true
        isTracking.value = false
        isGetLocation = false
        serviceKilled = true
        LatLog.value = Coordinates(0.0, 0.0)
        stopForeground(true)
        stopSelf()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                START_LOCATION -> {
                    if (isFirstRun && hasLocationPermission()) {
                        updateTracking(true)
                        startForegroundService()
                        isFirstRun = false
                    }
                }

                STOP_LOCATION -> {
                    updateTracking(false)
                    if(ToolBox_Con.getPreference_Customer_Code(applicationContext) != -1L) {
                        positionUseCase.execLastPositionPackage.invoke(Unit)
                    }
                    killService()
                }

                GET_LOCATION -> {
                    isGetLocation = true
                    if (isFirstRun && hasLocationPermission()) {
                        updateTracking(true)
                        startForegroundService()
                        isFirstRun = false
                    }
                }

                else -> {
                    updateNotification(it.action.toAlertType())
                }
            }

        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun updateNotification(alertType: TripPositionAlertType) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        when (alertType) {
            TripPositionAlertType.PENDING -> {
                val notification = curNotificationBuilder
                    .setContentText(hmAuxTrans[TRIP_NOTIFICATION_PENDING_DESCRIPTION])
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_RECOMMENDATION)
                notificationManager.notify(NOTIFICATION_TRACKING_ID, notification.build())
                val receiverAction =
                    setReceiverAction(TripBaseFragment.TRIP_WARNING_DEPARTED_FROM_ORIGIN)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(receiverAction)
            }

            TripPositionAlertType.WAITING_DESTINATION -> {
                val notification = curNotificationBuilder
                    .setContentText(hmAuxTrans[TRIP_NOTIFICATION_WAITING_DESTINATION_DESCRIPTION])
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_RECOMMENDATION)
                notificationManager.notify(NOTIFICATION_TRACKING_ID, notification.build())
                val receiverAction =
                    setReceiverAction(TripBaseFragment.TRIP_WARNING_WAITING_DESTINATION)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(receiverAction)
            }

            TripPositionAlertType.DEPARTED -> {
                val notification = curNotificationBuilder
                    .setContentText(hmAuxTrans[TRIP_NOTIFICATION_DEPARTED_DESCRIPTION])
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_RECOMMENDATION)
                notificationManager.notify(NOTIFICATION_TRACKING_ID, notification.build())
                val receiverAction =
                    setReceiverAction(TripBaseFragment.TRIP_WARNING_DEPARTED_FROM_SITE)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(receiverAction)
            }

            TripPositionAlertType.ARRIVED -> {
                val notification = curNotificationBuilder
                    .setContentText(hmAuxTrans[TRIP_NOTIFICATION_ARRIVED_DESCRIPTION])
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_RECOMMENDATION)
                notificationManager.notify(NOTIFICATION_TRACKING_ID, notification.build())
                //
                val receiverAction =
                    setReceiverAction(TripBaseFragment.TRIP_WARNING_ARRIVED_TO_SITE)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(receiverAction)
            }

            else -> {
                val notification = curNotificationBuilder
                    .setContentText(hmAuxTrans[TRIP_NOTIFICATION_DESCRIPTION])
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_RECOMMENDATION)
                notificationManager.notify(NOTIFICATION_TRACKING_ID, notification.build())
                val receiverAction =
                    setReceiverAction(TripBaseFragment.TRIP_WARNING_REMOVE)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(receiverAction)
            }
        }
    }

    private fun setReceiverAction(alertType: Int): Intent {
        val mIntent = Intent()
        mIntent.action = TripBaseFragment.TRIP_WARNING_RECEIVER
        mIntent.addCategory(Intent.CATEGORY_DEFAULT)
        mIntent.putExtra(
            TripBaseFragment.TRIP_WARNING_KEY,
            alertType
        )
        return mIntent
    }


    private fun startForegroundService() {
        isTracking.value = true

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        startForeground(NOTIFICATION_TRACKING_ID, baseNotificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_TRACKING_CHANNEL,
            NOTIFICATION_TRACKING_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    @SuppressLint("MissingPermission")
    private fun updateTracking(isTracking: Boolean) {
        Companion.isTracking.value = isTracking
        if (isTracking) {
            if (hasLocationPermission()) {
                val request =
                    LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, LOCATION_INTERVAL)
                        .build()

                fusedLocationClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
                handler.removeCallbacks(handlerGetLocationTimeout)
                if (isGetLocation) {
                    handler.postDelayed(handlerGetLocationTimeout, 60 * 1000L)
                }
            }
        } else {
            fusedLocationClient.removeLocationUpdates(locationCallback)
            handler.removeCallbacks(handlerGetLocationTimeout)
        }
    }


    private fun hasLocationPermission() = ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    companion object {
        var isTracking = MutableStateFlow(false)
        var LatLog = MutableStateFlow(Coordinates(0.0, 0.0))

        private const val RESOURCE_TRIP_SERVICE = "trip_gps_service"
        const val TRIP_GPS_LOCATION_ACQUIRED = "trip_gps_location_acquired"
        const val TRIP_GPS_LOCATION_NOT_ACQUIRED = "trip_gps_location_not_acquired"
        const val TRIP_GPS_SEARCHING_LOCATION = "trip_gps_searching_location"
        const val TRIP_NOTIFICATION_TITLE = "trip_notification_title"
        const val TRIP_NOTIFICATION_DESCRIPTION = "trip_notification_description"
        const val TRIP_NOTIFICATION_ARRIVED_DESCRIPTION = "trip_notification_arrived_description"
        const val TRIP_NOTIFICATION_DEPARTED_DESCRIPTION = "trip_notification_departed_description"
        const val TRIP_NOTIFICATION_WAITING_DESTINATION_DESCRIPTION = "trip_notification_waiting_destination_description"
        const val TRIP_NOTIFICATION_PENDING_DESCRIPTION = "trip_notification_pending_description"


        fun loadTranslate(context: Context): HMAux {
            listOf(
                TRIP_GPS_LOCATION_ACQUIRED,
                TRIP_GPS_LOCATION_NOT_ACQUIRED,
                TRIP_GPS_SEARCHING_LOCATION,
                TRIP_NOTIFICATION_TITLE,
                TRIP_NOTIFICATION_DESCRIPTION,
                TRIP_NOTIFICATION_ARRIVED_DESCRIPTION,
                TRIP_NOTIFICATION_DEPARTED_DESCRIPTION,
                TRIP_NOTIFICATION_WAITING_DESTINATION_DESCRIPTION,
                TRIP_NOTIFICATION_PENDING_DESCRIPTION,
            ).let { list ->
                return TranslateResource(
                    context,
                    Constant.APP_MODULE,
                    context.getResourceCode(RESOURCE_TRIP_SERVICE)
                ).setLanguage(list)
            }
        }
    }
}
