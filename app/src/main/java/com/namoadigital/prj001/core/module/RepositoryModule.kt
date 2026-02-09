package com.namoadigital.prj001.core.module

import android.content.Context
import com.namoadigital.prj001.core.trip.data.destination.action.TripDestinationActionRepository
import com.namoadigital.prj001.core.trip.data.destination.action.TripDestinationActionRepositoryImp
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao
import com.namoadigital.prj001.dao.GE_FileDao
import com.namoadigital.prj001.dao.trip.FSEventTypeDao
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.dao.trip.FSTripEventDao
import com.namoadigital.prj001.dao.trip.FsTripDestinationActionDao
import com.namoadigital.prj001.ui.act005.trip.repository.event.TripEventRepository
import com.namoadigital.prj001.ui.act005.trip.repository.event.TripEventRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext


@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    fun providesTripDestinationActionRepository(
        @ApplicationContext app: Context,
        dao: FsTripDestinationActionDao,
        tripDao: FSTripDao,
        daoFormLocal: GE_Custom_Form_LocalDao
    ): TripDestinationActionRepository =
        TripDestinationActionRepositoryImp(app, tripDao, dao, daoFormLocal)

    @Provides
    fun providesEventRepository(
        @ApplicationContext app: Context,
        dao: FSEventTypeDao,
        eventDao: FSTripEventDao,
        tripDao: FSTripDao,
        fileDao: GE_FileDao
    ): TripEventRepository {
        return TripEventRepositoryImp(app, dao, eventDao, tripDao, fileDao)
    }

}