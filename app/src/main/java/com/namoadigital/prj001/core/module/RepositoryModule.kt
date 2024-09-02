package com.namoadigital.prj001.core.module

import android.content.Context
import com.namoadigital.prj001.core.data.local.repository.serial.SerialRepository
import com.namoadigital.prj001.core.data.local.repository.serial.SerialRepositoryImp
import com.namoadigital.prj001.core.data.local.repository.ticket.TicketCacheRepository
import com.namoadigital.prj001.core.data.local.repository.ticket.TicketCacheRepositoryImp
import com.namoadigital.prj001.core.data.local.repository.ticket.TicketRepository
import com.namoadigital.prj001.core.data.local.repository.ticket.TicketRepositoryImp
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepositoryImp
import com.namoadigital.prj001.core.trip.data.destination.action.TripDestinationActionRepository
import com.namoadigital.prj001.core.trip.data.destination.action.TripDestinationActionRepositoryImp
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.core.trip.data.trip.TripRepositoryImp
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao
import com.namoadigital.prj001.dao.GE_FileDao
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.dao.MD_SiteDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.dao.trip.FSEventTypeDao
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.dao.trip.FSTripEventDao
import com.namoadigital.prj001.dao.trip.FSTripUserDao
import com.namoadigital.prj001.dao.trip.FsTripDestinationActionDao
import com.namoadigital.prj001.dao.trip.FsTripDestinationDao
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.ui.act005.trip.repository.event.TripEventRepository
import com.namoadigital.prj001.ui.act005.trip.repository.event.TripEventRepositoryImp
import com.namoadigital.prj001.ui.act005.trip.repository.users.TripUserRepository
import com.namoadigital.prj001.ui.act005.trip.repository.users.TripUserRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @ViewModelScoped
    @Provides
    fun providesRepositoryUsers(
        @ApplicationContext context: Context,
        tripDao: FSTripDao,
        dao: FSTripUserDao
    ): TripUserRepository = TripUserRepositoryImp(context, tripDao, dao)

    @ViewModelScoped
    @Provides
    fun providesRepositoryTrip(
        @ApplicationContext app: Context,
        dao: FSTripDao,
        fileDao: GE_FileDao,
        siteDao: MD_SiteDao,
        eventDao: FSTripEventDao,
        userDao: FSTripUserDao,
        destinationDao: FsTripDestinationDao
    ): TripRepository = TripRepositoryImp(
        context = app,
        dao = dao,
        fileDao = fileDao,
        siteDao = siteDao,
        eventDao = eventDao,
        userDao = userDao,
        destinationDao = destinationDao
    )

    @ViewModelScoped
    @Provides
    fun providesRepositoryTripDestination(
        @ApplicationContext app: Context,
        dao: FsTripDestinationDao,
        fsTripDao: FSTripDao
    ): TripDestinationRepository = TripDestinationRepositoryImp(app, dao, fsTripDao)

    @ViewModelScoped
    @Provides
    fun providesRepositoryTicket(
        @ApplicationContext app: Context,
        dao: TK_TicketDao
    ): TicketRepository = TicketRepositoryImp(app, dao)

    @ViewModelScoped
    @Provides
    fun providesRepositoryTicketCache(
        @ApplicationContext app: Context,
        dao: TkTicketCacheDao
    ): TicketCacheRepository = TicketCacheRepositoryImp(app, dao)

    @ViewModelScoped
    @Provides
    fun providesTripDestinationActionRepository(
        @ApplicationContext app: Context,
        dao: FsTripDestinationActionDao,
        tripDao: FSTripDao,
        daoFormLocal: GE_Custom_Form_LocalDao
    ): TripDestinationActionRepository =
        TripDestinationActionRepositoryImp(app, tripDao, dao, daoFormLocal)

    @ViewModelScoped
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

    @ViewModelScoped
    @Provides
    fun providesSerialRepository(
        dao: MD_Product_SerialDao
    ): SerialRepository {
        return SerialRepositoryImp(dao)
    }


}