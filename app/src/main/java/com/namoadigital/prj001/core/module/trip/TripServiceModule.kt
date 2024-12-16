package com.namoadigital.prj001.core.module.trip

import android.content.Context
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.R
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepositoryImp
import com.namoadigital.prj001.core.trip.data.position.TripPositionRepository
import com.namoadigital.prj001.core.trip.data.position.TripPositionRepositoryImp
import com.namoadigital.prj001.core.trip.data.preference.CurrentTripPref
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.core.trip.data.trip.TripRepositoryImp
import com.namoadigital.prj001.core.trip.domain.usecase.destination.CheckNextStatusWhenNewDestinationUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.destination.SaveDestinationUseCase
import com.namoadigital.prj001.dao.GE_FileDao
import com.namoadigital.prj001.dao.MD_SiteDao
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.dao.trip.FSTripEventDao
import com.namoadigital.prj001.dao.trip.FSTripUserDao
import com.namoadigital.prj001.dao.trip.FsTripDestinationDao
import com.namoadigital.prj001.dao.trip.FsTripPositionDao
import com.namoadigital.prj001.service.location.FsTripLocationService
import com.namoadigital.prj001.service.location.FsTripLocationService.Companion.TRIP_NOTIFICATION_DESCRIPTION
import com.namoadigital.prj001.service.location.FsTripLocationService.Companion.TRIP_NOTIFICATION_TITLE
import com.namoadigital.prj001.service.location.util.LocationServiceConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)

object TripServiceModule {

    @ServiceScoped
    @Provides
    fun provideFusedLocationProviderClient(
        @ApplicationContext app: Context
    ) = LocationServices.getFusedLocationProviderClient(app)


    @ServiceScoped
    @Provides
    fun provideBaseNotificationBuilder(
        @ApplicationContext app: Context
    ): NotificationCompat.Builder {
        val hmAuxTranslate = FsTripLocationService.loadTranslate(app)

        return NotificationCompat.Builder(app, LocationServiceConstants.NOTIFICATION_TRACKING_CHANNEL)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_location_on_24)
            .setContentTitle(hmAuxTranslate[TRIP_NOTIFICATION_TITLE])
            .setContentText(hmAuxTranslate[TRIP_NOTIFICATION_DESCRIPTION])


    }


    @ServiceScoped
    @Provides
    fun providesPositionRepository(
        @ApplicationContext app: Context,
        dao: FsTripPositionDao,
        tripDao: FSTripDao,
        currentTripPref: CurrentTripPref
    ): TripPositionRepository = TripPositionRepositoryImp(app, dao, tripDao, currentTripPref)

    @ServiceScoped
    @Provides
    fun providesCurrentTripPref(
        @ApplicationContext app: Context
    ) = CurrentTripPref(app.getSharedPreferences(
        "current_trip",
        Base_Activity.MODE_PRIVATE
    ))

    @ServiceScoped
    @Provides
    fun providesPositionDao(
        @ApplicationContext app: Context
    ) = FsTripPositionDao(app)

    @ServiceScoped
    @Provides
    fun providesTripDao(
        @ApplicationContext app: Context
    ) = FSTripDao(app)

    @ServiceScoped
    @Provides
    fun providesDestinationDao(
        @ApplicationContext app: Context
    ) = FsTripDestinationDao(app)

    @ServiceScoped
    @Provides
    fun providesRepositoryTrip(
        @ApplicationContext app: Context,
        dao: FSTripDao,
        tripDestinationDao: FsTripDestinationDao,
        ): TripRepository =
        TripRepositoryImp(
            app,
            dao,
            GE_FileDao(app),
            MD_SiteDao(app),
            FSTripEventDao(app),
            FSTripUserDao(app),
            tripDestinationDao,
        )

    @ServiceScoped
    @Provides
    fun providesRepositoryDestination(
        @ApplicationContext app: Context,
        dao: FsTripDestinationDao,
        tripDao: FSTripDao
    ): TripDestinationRepository {
        return TripDestinationRepositoryImp(app, dao, tripDao)
    }

    @ServiceScoped
    @Provides
    fun providesSaveDestinationUseCase(
        tripRepository: TripRepository,
        fsTripDestinationRepository: TripDestinationRepository,
    ): SaveDestinationUseCase {
        return SaveDestinationUseCase(
            fsTripDestinationRepository,
            tripRepository,
            CheckNextStatusWhenNewDestinationUseCase(fsTripDestinationRepository, tripRepository),
        )
    }


}