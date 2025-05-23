package com.namoadigital.prj001.ui.act011.finish_os.di.modules

import android.content.Context
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao
import com.namoadigital.prj001.dao.GeOsDao
import com.namoadigital.prj001.dao.GeOsDeviceDao
import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.dao.MeMeasureTpDao
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_custom_form.GeCustomFormRepository
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_custom_form.GeCustomFormRepositoryImpl
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_os.GeOsRepository
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_os.GeOsRepositoryImpl
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.measure_tp.MeasureTpRepository
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.measure_tp.MeasureTpRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object FormRepositoryModule {

    @Provides
    @ViewModelScoped
    fun providesGeCustomFormLocalRepository(
        @ApplicationContext context: Context,
        localDao: GE_Custom_Form_LocalDao,
        dataDao: GE_Custom_Form_DataDao,
        geOsDao: GeOsDao,
    ): GeCustomFormRepository = GeCustomFormRepositoryImpl(context, localDao, dataDao, geOsDao)

    @Provides
    @ViewModelScoped
    fun providesGeOsRepository(
        @ApplicationContext context: Context,
        dao: GeOsDao,
        deviceDao: GeOsDeviceDao,
        deviceItemDao: GeOsDeviceItemDao,
    ): GeOsRepository = GeOsRepositoryImpl(context, dao, deviceDao, deviceItemDao)

    @Provides
    @ViewModelScoped
    fun providesMeasureTpRepository(
        @ApplicationContext context: Context,
        dao: MeMeasureTpDao,
    ): MeasureTpRepository {
        return MeasureTpRepositoryImpl(context, dao)
    }
}