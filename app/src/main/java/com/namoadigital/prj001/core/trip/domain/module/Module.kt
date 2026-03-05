package com.namoadigital.prj001.core.trip.domain.module

import android.content.Context
import com.namoadigital.prj001.core.data.local.repository.ticket.TicketCacheRepository
import com.namoadigital.prj001.core.data.local.repository.ticket.TicketRepository
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepositoryImp
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.core.trip.data.trip.TripRepositoryImp
import com.namoadigital.prj001.core.trip.domain.usecase.CheckStatusForReadOnlyUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.GetTicketActionUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.GetTicketCacheActionUseCase
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.dao.trip.FsTripDestinationDao
import com.namoadigital.prj001.dao.trip.FsTripPositionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped


@Module
@InstallIn(ActivityComponent::class)
object Module {

    @Provides
    @ActivityScoped
    fun providesTripRepository(
        @ApplicationContext app: Context,
        dao: FSTripDao
    ): TripRepository = TripRepositoryImp(app, dao)

    @Provides
    @ActivityScoped
    fun providesCheckReadOnly(
        @ApplicationContext app: Context,
        repository: TripRepository,
        destinationRepository: TripDestinationRepository
    ): CheckStatusForReadOnlyUseCase {
        return CheckStatusForReadOnlyUseCase(app, repository, destinationRepository)
    }
    @Provides
    @ActivityScoped
    fun providesGetTicketAction(
        repository: TicketRepository,
    ): GetTicketActionUseCase {
        return GetTicketActionUseCase(repository)
    }
    @Provides
    @ActivityScoped
    fun providesGetTicketCacheAction(
        repositoryCache: TicketCacheRepository,
    ): GetTicketCacheActionUseCase {
        return GetTicketCacheActionUseCase( repositoryCache)
    }


    @Provides
    @ActivityScoped
    fun providesTripDestinationRepository(
        @ApplicationContext app: Context,
        dao: FsTripDestinationDao,
        tripDao: FSTripDao,
        fsTripPositionDao: FsTripPositionDao,
    ): TripDestinationRepository {
        return TripDestinationRepositoryImp(app, dao, tripDao, fsTripPositionDao)
    }

}