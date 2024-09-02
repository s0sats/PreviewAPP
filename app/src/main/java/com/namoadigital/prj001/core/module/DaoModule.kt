package com.namoadigital.prj001.core.module

import android.content.Context
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
import com.namoadigital.prj001.model.GE_Custom_Form_Local
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object DaoModule {

    @ViewModelScoped
    @Provides
    fun providesEventTypeDao(
        @ApplicationContext app: Context
    ) = FSEventTypeDao(app)

    @ViewModelScoped
    @Provides
    fun providesTripDao(
        @ApplicationContext app: Context
    ) = FSTripDao(app)

    @ViewModelScoped
    @Provides
    fun providesTripUserDao(
        @ApplicationContext app: Context
    ) = FSTripUserDao(app)

    @ViewModelScoped
    @Provides
    fun providesTripDestinationDao(
        @ApplicationContext app: Context
    ) = FsTripDestinationDao(app)

    @ViewModelScoped
    @Provides
    fun providesGEFilesDao(
        @ApplicationContext app: Context
    ) = GE_FileDao(app)

    @ViewModelScoped
    @Provides
    fun providesMDSitesDao(
        @ApplicationContext app: Context
    ) = MD_SiteDao(app)

    @ViewModelScoped
    @Provides
    fun providesFsTripEventDao(
        @ApplicationContext app: Context
    ) = FSTripEventDao(app)


    @ViewModelScoped
    @Provides
    fun providesTkTicketDao(
        @ApplicationContext app: Context
    ) = TK_TicketDao(app)

    @ViewModelScoped
    @Provides
    fun providesTkTicketCacheDao(
        @ApplicationContext app: Context
    ) = TkTicketCacheDao(app)

    @ViewModelScoped
    @Provides
    fun providesFsTripDestinationActionDao(
        @ApplicationContext app: Context
    ) = FsTripDestinationActionDao(app)

    @ViewModelScoped
    @Provides
    fun proviesGeCustomFormLocalDao(
        @ApplicationContext app: Context
    ) = GE_Custom_Form_LocalDao(app)

    @ViewModelScoped
    @Provides
    fun providesMdProductSerialDao(
        @ApplicationContext app: Context
    ) = MD_Product_SerialDao(app)
}