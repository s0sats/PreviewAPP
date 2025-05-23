package com.namoadigital.prj001.core.module

import android.content.Context
import com.namoadigital.prj001.core.data.local.repository.ge_os.GeOsRepositoryImpl
import com.namoadigital.prj001.core.form_os.domain.repository.GeOsRepository
import com.namoadigital.prj001.dao.GeOsDao
import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.dao.GeOsVgDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object GeOsRepositoryModule {
    @Provides
    @ActivityScoped
    fun providesGeOsRepository(
        @ApplicationContext context: Context,
        dao: GeOsDao,
        deviceItemDao: GeOsDeviceItemDao,
        geOsVg: GeOsVgDao,
    ): GeOsRepository = GeOsRepositoryImpl(context, dao, deviceItemDao, geOsVg)
}