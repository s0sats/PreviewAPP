package com.namoadigital.prj001.core.module

import android.content.Context
import com.namoadigital.prj001.core.data.local.repository.serial.SerialRepository
import com.namoadigital.prj001.core.data.local.repository.ticket.TicketCacheRepository
import com.namoadigital.prj001.core.data.local.repository.ticket.TicketRepository
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.data.destination.action.TripDestinationActionRepository
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.core.trip.domain.usecase.CheckNextDestinationStatusTripUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.CheckExistsTripUpdateUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.CreateTripUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.GetDestinationByStatusUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.GetDestinationUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.GetEventRestrictionDateUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.GetEventUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.GetTripUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.ListSitesUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.SaveDestinationDateUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.SaveFleetDataUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.SaveOriginUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.SavePreferenceTripUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.SendTripFullUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.SetDestinationStatusUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.TripStatusChangeUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.TripUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.destination.CheckNextStatusTripUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.destination.CheckNextStatusWhenNewDestinationUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.destination.DestinationUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.destination.GetDestinationCounter
import com.namoadigital.prj001.core.trip.domain.usecase.destination.GetDestinationForThresholdValidationUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.destination.SaveDestinationUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.destination.SaveOverNightDestinationUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.destination.SelectDestinationUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.destination.action.DestinationActionUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.destination.action.GetDestinationActionUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.destination.action.GetListSiteExtractUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.ticket.DownloadTicketUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.ticket.GetTicketUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.ticket.TicketUseCase
import com.namoadigital.prj001.ui.act005.trip.di.usecase.event.GetEventTypeUseCase
import com.namoadigital.prj001.ui.act005.trip.di.usecase.event.GetListEventTypeUseCase
import com.namoadigital.prj001.ui.act005.trip.di.usecase.event.SaveEventUseCase
import com.namoadigital.prj001.ui.act005.trip.di.usecase.event.TripEventUseCase
import com.namoadigital.prj001.ui.act005.trip.di.usecase.extract.ListExtractUseCase
import com.namoadigital.prj001.ui.act005.trip.di.usecase.origin.GetFirstDateOnTripUseCase
import com.namoadigital.prj001.ui.act005.trip.di.usecase.user.ExecEditUserUseCase
import com.namoadigital.prj001.ui.act005.trip.di.usecase.user.GetListTechnicalUseCase
import com.namoadigital.prj001.ui.act005.trip.di.usecase.user.TripUsersUseCase
import com.namoadigital.prj001.ui.act005.trip.di.usecase.user.UserCheckIntersectionUseCase
import com.namoadigital.prj001.ui.act005.trip.repository.event.TripEventRepository
import com.namoadigital.prj001.ui.act005.trip.repository.users.TripUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @ViewModelScoped
    @Provides
    fun providesTripUseCase(
        repository: TripRepository,
        destinationRepository: TripDestinationRepository,
    ): TripUseCase {
        return TripUseCase(
            trip = GetTripUseCase(repository),
            statusChange = TripStatusChangeUseCase(
                repository,
                CheckNextDestinationStatusTripUseCase(repository, destinationRepository)
            ),
            savePrefTrip = SavePreferenceTripUseCase(repository),
            saveFleet = SaveFleetDataUseCase(repository),
            listSites = ListSitesUseCase(repository),
            saveOrigin = SaveOriginUseCase(repository),
            getEvent = GetEventUseCase(repository),
            sendTripFull = SendTripFullUseCase(repository),
            hasTripWithUpdateRequired = CheckExistsTripUpdateUseCase(repository)
        )
    }

    @ViewModelScoped
    @Provides
    fun providesDestinationUseCase(
        @ApplicationContext app: Context,
        repository: TripDestinationRepository,
        ticketRepository: TicketRepository,
        ticketCacheRepository: TicketCacheRepository,
        tripRepository: TripRepository,
        serialRepository: SerialRepository
    ): DestinationUseCase {
        return DestinationUseCase(
            destination = GetDestinationUseCase(repository),
            destinationByStatus = GetDestinationByStatusUseCase(repository),
            getDestinationCounter = GetDestinationCounter(
                app,
                repository,
                ticketRepository,
                ticketCacheRepository,
                serialRepository
            ),
            setDestinationStatusUseCase = SetDestinationStatusUseCase(
                repository,
                CheckNextStatusTripUseCase(tripRepository, repository)
            ),
            execSelectDestination = SelectDestinationUseCase(repository),
            saveDestinationDateUseCase = SaveDestinationDateUseCase(repository),
            getDestinationForThresholdValidationUseCase = GetDestinationForThresholdValidationUseCase(
                repository
            ),
            saveOverNightDestinationUseCase = SaveOverNightDestinationUseCase(
                repository,
                tripRepository,
                SelectDestinationUseCase(repository),
                SaveDestinationUseCase(repository, tripRepository, CheckNextStatusWhenNewDestinationUseCase(repository, tripRepository))
            )
        )
    }

    @ViewModelScoped
    @Provides
    fun providesCreateTripUseCase(repository: TripRepository): CreateTripUseCase {
        return CreateTripUseCase(repository)
    }

    @ViewModelScoped
    @Provides
    fun providesDestinationActionUseCase(
        repository: TripDestinationActionRepository
    ): DestinationActionUseCase {
        return DestinationActionUseCase(
            GetDestinationActionUseCase(repository),
            GetListSiteExtractUseCase(repository)
        )
    }

    @ViewModelScoped
    @Provides
    fun providesEventUseCase(repository: TripEventRepository): TripEventUseCase {
        return TripEventUseCase(
            getEventType = GetEventTypeUseCase(repository),
            listEventTypes = GetListEventTypeUseCase(repository),
            save = SaveEventUseCase(repository),
            getEventRestrictionDate = GetEventRestrictionDateUseCase(repository)
        )
    }


    @ViewModelScoped
    @Provides
    fun providesTripUsersUseCase(repository: TripUserRepository) = TripUsersUseCase(
        getUsers = GetListTechnicalUseCase(repository),
        edit = ExecEditUserUseCase(repository),
        intersection = UserCheckIntersectionUseCase(repository)
    )


    @ViewModelScoped
    @Provides
    fun providesTripExtractUseCase(
        event: TripEventRepository,
        user: TripUserRepository,
        trip: TripRepository,
        destination: TripDestinationRepository,
        action: TripDestinationActionRepository
    ) = ListExtractUseCase(user, event, trip, destination, action)


    @ViewModelScoped
    @Provides
    fun providesTicketUseCase(
        repository: TicketRepository
    ) = TicketUseCase(
        get = GetTicketUseCase(repository),
        download = DownloadTicketUseCase(repository)
    )

    @ViewModelScoped
    @Provides
    fun providesValidateOriginUseCase(
        event: TripEventRepository,
        destination: TripDestinationRepository,
        user: TripUserRepository,
    ) = GetFirstDateOnTripUseCase(
        event,
        destination,
        user,
    )
}
