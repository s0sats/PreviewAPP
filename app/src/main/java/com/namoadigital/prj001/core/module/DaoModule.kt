package com.namoadigital.prj001.core.module

import android.content.Context
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao
import com.namoadigital.prj001.dao.GE_FileDao
import com.namoadigital.prj001.dao.GeOsDao
import com.namoadigital.prj001.dao.GeOsDeviceDao
import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.dao.GeOsVgDao
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.dao.MD_SiteDao
import com.namoadigital.prj001.dao.MeMeasureTpDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TK_Ticket_FormDao
import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.dao.event.EventManualDao
import com.namoadigital.prj001.dao.trip.FSEventTypeDao
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.dao.trip.FSTripEventDao
import com.namoadigital.prj001.dao.trip.FSTripUserDao
import com.namoadigital.prj001.dao.trip.FsTripDestinationActionDao
import com.namoadigital.prj001.dao.trip.FsTripDestinationDao
import com.namoadigital.prj001.dao.trip.FsTripPositionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityRetainedComponent::class)
object DaoModule {

    @Provides
    fun providesGEFilesDao(
        @ApplicationContext app: Context
    ) = GE_FileDao(app)

    @Provides
    fun providesMDSitesDao(
        @ApplicationContext app: Context
    ) = MD_SiteDao(app)

    @Provides
    fun providesTkTicketDao(
        @ApplicationContext app: Context
    ) = TK_TicketDao(app)

    @Provides
    fun providesTkTicketFormDao(
        @ApplicationContext app: Context
    ) = TK_Ticket_FormDao(app)

    @Provides
    fun providesTkTicketCacheDao(
        @ApplicationContext app: Context
    ) = TkTicketCacheDao(app)

    @Provides
    fun providesFsTripDestinationActionDao(
        @ApplicationContext app: Context
    ) = FsTripDestinationActionDao(app)

    @Provides
    fun providesGeCustomFormLocalDao(
        @ApplicationContext app: Context
    ) = GE_Custom_Form_LocalDao(app)

    @Provides
    fun providesGeCustomFormDataDao(
        @ApplicationContext app: Context
    ) = GE_Custom_Form_DataDao(app)

    @Provides
    fun providesGeOsDao(
        @ApplicationContext app: Context
    ) = GeOsDao(app)

    @Provides
    fun providesGeOsDeviceDao(
        @ApplicationContext app: Context
    ) = GeOsDeviceDao(app)

    @Provides
    fun providesGeOsDeviceItemDao(
        @ApplicationContext app: Context
    ) = GeOsDeviceItemDao(app)

    @Provides
    fun providesGeOsVgDao(
        @ApplicationContext app: Context
    ) = GeOsVgDao(app)

    @Provides
    fun providesMdProductSerialDao(
        @ApplicationContext app: Context
    ) = MD_Product_SerialDao(app)

    @Provides
    fun providesMeMeasureTpDao(
        @ApplicationContext app: Context
    ) = MeMeasureTpDao(app)


    @Provides
    fun providesEventTypeDao(
        @ApplicationContext app: Context
    ) = FSEventTypeDao(app)


    @Provides
    fun providesTripDao(
        @ApplicationContext app: Context
    ) = FSTripDao(app)


    @Provides
    fun providesTripUserDao(
        @ApplicationContext app: Context
    ) = FSTripUserDao(app)


    @Provides
    fun providesTripDestinationDao(
        @ApplicationContext app: Context
    ) = FsTripDestinationDao(app)


    @Provides
    fun providesFsTripEventDao(
        @ApplicationContext app: Context
    ) = FSTripEventDao(app)


    @Provides
    fun providesEventManualDao(
        @ApplicationContext app: Context
    ) = EventManualDao(app)

    @Provides
    fun providesPositionDao(
        @ApplicationContext app: Context
    ) = FsTripPositionDao(app)
}

@Module
@InstallIn(ServiceComponent::class)
object DaoServiceModule {

    @Provides
    fun providesPositionDao(
        @ApplicationContext app: Context
    ) = FsTripPositionDao(app)

    @Provides
    fun providesGEFilesDao(
        @ApplicationContext app: Context
    ) = GE_FileDao(app)

    @Provides
    fun providesMDSitesDao(
        @ApplicationContext app: Context
    ) = MD_SiteDao(app)

    @Provides
    fun providesTkTicketDao(
        @ApplicationContext app: Context
    ) = TK_TicketDao(app)

    @Provides
    fun providesTkTicketFormDao(
        @ApplicationContext app: Context
    ) = TK_Ticket_FormDao(app)

    @Provides
    fun providesTkTicketCacheDao(
        @ApplicationContext app: Context
    ) = TkTicketCacheDao(app)

    @Provides
    fun providesFsTripDestinationActionDao(
        @ApplicationContext app: Context
    ) = FsTripDestinationActionDao(app)

    @Provides
    fun providesGeCustomFormLocalDao(
        @ApplicationContext app: Context
    ) = GE_Custom_Form_LocalDao(app)

    @Provides
    fun providesGeCustomFormDataDao(
        @ApplicationContext app: Context
    ) = GE_Custom_Form_DataDao(app)

    @Provides
    fun providesGeOsDao(
        @ApplicationContext app: Context
    ) = GeOsDao(app)

    @Provides
    fun providesGeOsDeviceDao(
        @ApplicationContext app: Context
    ) = GeOsDeviceDao(app)

    @Provides
    fun providesGeOsDeviceItemDao(
        @ApplicationContext app: Context
    ) = GeOsDeviceItemDao(app)

    @Provides
    fun providesGeOsVgDao(
        @ApplicationContext app: Context
    ) = GeOsVgDao(app)

    @Provides
    fun providesMdProductSerialDao(
        @ApplicationContext app: Context
    ) = MD_Product_SerialDao(app)

    @Provides
    fun providesMeMeasureTpDao(
        @ApplicationContext app: Context
    ) = MeMeasureTpDao(app)


    @Provides
    fun providesEventTypeDao(
        @ApplicationContext app: Context
    ) = FSEventTypeDao(app)


    @Provides
    fun providesTripDao(
        @ApplicationContext app: Context
    ) = FSTripDao(app)


    @Provides
    fun providesTripUserDao(
        @ApplicationContext app: Context
    ) = FSTripUserDao(app)


    @Provides
    fun providesTripDestinationDao(
        @ApplicationContext app: Context
    ) = FsTripDestinationDao(app)


    @Provides
    fun providesFsTripEventDao(
        @ApplicationContext app: Context
    ) = FSTripEventDao(app)


    @Provides
    fun providesEventManualDao(
        @ApplicationContext app: Context
    ) = EventManualDao(app)

}