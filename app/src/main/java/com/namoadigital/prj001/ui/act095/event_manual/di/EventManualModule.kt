package com.namoadigital.prj001.ui.act095.event_manual.di

import android.content.Context
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao
import com.namoadigital.prj001.dao.GE_FileDao
import com.namoadigital.prj001.dao.GeOsDao
import com.namoadigital.prj001.dao.event.EventManualDao
import com.namoadigital.prj001.dao.trip.FSEventTypeDao
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_custom_form.GeCustomFormRepository
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_custom_form.GeCustomFormRepositoryImpl
import com.namoadigital.prj001.ui.act095.event_manual.data.EventManualRepositoryImpl
import com.namoadigital.prj001.ui.act095.event_manual.domain.repository.EventManualRepository
import com.namoadigital.prj001.ui.act095.event_manual.domain.usecases.GetEventManualUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityRetainedComponent::class)
object EventManualModule {

    @Provides
    fun providesEventManualRepository(
        @ApplicationContext appContext: Context,
        fsEventTypeDao: FSEventTypeDao,
        eventDao: EventManualDao,
        geFileDao: GE_FileDao
    ): EventManualRepository = EventManualRepositoryImpl(
        appContext = appContext,
        typeDao = fsEventTypeDao,
        eventDao = eventDao,
        fileDao = geFileDao
    )


    @Provides
    fun provideGeCustomFormRepository(
        @ApplicationContext appContext: Context,
        localDao: GE_Custom_Form_LocalDao,
        dataDao: GE_Custom_Form_DataDao,
        geOsDao: GeOsDao,
    ): GeCustomFormRepository = GeCustomFormRepositoryImpl(appContext, localDao, dataDao, geOsDao)

    @Provides
    fun provideGetEventManualUseCase(
        repository: EventManualRepository
    ) = GetEventManualUseCase(repository)

}


@Module
@InstallIn(ServiceComponent::class)
object EventManualServiceModule {


    @Provides
    fun providesEventManualRepository(
        @ApplicationContext appContext: Context,
        fsEventTypeDao: FSEventTypeDao,
        eventDao: EventManualDao,
        geFileDao: GE_FileDao
    ): EventManualRepository = EventManualRepositoryImpl(
        appContext = appContext,
        typeDao = fsEventTypeDao,
        eventDao = eventDao,
        fileDao = geFileDao
    )


}