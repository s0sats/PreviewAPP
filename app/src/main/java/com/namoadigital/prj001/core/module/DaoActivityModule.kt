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
import com.namoadigital.prj001.dao.trip.FsTripDestinationActionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object DaoActivityModule {

    @ActivityScoped
    @Provides
    fun providesGEFilesDao(
        @ApplicationContext app: Context
    ) = GE_FileDao(app)

    @ActivityScoped
    @Provides
    fun providesMDSitesDao(
        @ApplicationContext app: Context
    ) = MD_SiteDao(app)

    @ActivityScoped
    @Provides
    fun providesTkTicketDao(
        @ApplicationContext app: Context
    ) = TK_TicketDao(app)

    @ActivityScoped
    @Provides
    fun providesTkTicketFormDao(
        @ApplicationContext app: Context
    ) = TK_Ticket_FormDao(app)

    @ActivityScoped
    @Provides
    fun providesTkTicketCacheDao(
        @ApplicationContext app: Context
    ) = TkTicketCacheDao(app)

    @ActivityScoped
    @Provides
    fun providesFsTripDestinationActionDao(
        @ApplicationContext app: Context
    ) = FsTripDestinationActionDao(app)

    @ActivityScoped
    @Provides
    fun providesGeCustomFormLocalDao(
        @ApplicationContext app: Context
    ) = GE_Custom_Form_LocalDao(app)

    @ActivityScoped
    @Provides
    fun providesGeCustomFormDataDao(
        @ApplicationContext app: Context
    ) = GE_Custom_Form_DataDao(app)

    @ActivityScoped
    @Provides
    fun providesGeOsDao(
        @ApplicationContext app: Context
    ) = GeOsDao(app)

    @ActivityScoped
    @Provides
    fun providesGeOsDeviceDao(
        @ApplicationContext app: Context
    ) = GeOsDeviceDao(app)

    @ActivityScoped
    @Provides
    fun providesGeOsDeviceItemDao(
        @ApplicationContext app: Context
    ) = GeOsDeviceItemDao(app)

    @ActivityScoped
    @Provides
    fun providesGeOsVgDao(
        @ApplicationContext app: Context
    ) = GeOsVgDao(app)

    @ActivityScoped
    @Provides
    fun providesMdProductSerialDao(
        @ApplicationContext app: Context
    ) = MD_Product_SerialDao(app)

    @ActivityScoped
    @Provides
    fun providesMeMeasureTpDao(
        @ApplicationContext app: Context
    ) = MeMeasureTpDao(app)

}