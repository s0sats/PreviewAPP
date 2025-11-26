package com.namoadigital.prj001.ui.act011.group_verification.module

import android.content.Context
import com.namoadigital.prj001.core.data.local.repository.ge_os.GeOsRepositoryImpl
import com.namoadigital.prj001.core.form_os.domain.repository.GeOsRepository
import com.namoadigital.prj001.dao.GeOsDao
import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.dao.GeOsVgDao
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_custom_form.GeCustomFormRepository
import com.namoadigital.prj001.ui.act011.group_verification.domain.usecase.MapToVerificationGroupsUseCase
import com.namoadigital.prj001.ui.act011.group_verification.domain.usecase.UpdateGroupActiveUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseVerificationGroupModule {

    @ViewModelScoped
    @Provides
    fun providesMapToVerificationGroupsUseCase(
        repository: GeOsRepository,
        formRepository: GeCustomFormRepository
    ): MapToVerificationGroupsUseCase =
        MapToVerificationGroupsUseCase(
            repository = repository,
            formRepository = formRepository
        )

    @Provides
    @ViewModelScoped
    fun providesGeOsRepository(
        @ApplicationContext context: Context,
        dao: GeOsDao,
        deviceItemDao: GeOsDeviceItemDao,
        geOsVg: GeOsVgDao,
    ): GeOsRepository = GeOsRepositoryImpl(context, dao, deviceItemDao, geOsVg)

    @Provides
    @ViewModelScoped
    fun providesUpdateGroupActiveUseCase(
        repository: GeOsRepository,
    ): UpdateGroupActiveUseCase = UpdateGroupActiveUseCase(repository)

}