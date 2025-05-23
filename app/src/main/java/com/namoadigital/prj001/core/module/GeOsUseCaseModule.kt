package com.namoadigital.prj001.core.module

import com.namoadigital.prj001.core.form_os.domain.repository.GeOsRepository
import com.namoadigital.prj001.core.form_os.domain.usecase.GeOsApplyVGVisibilityUseCase
import com.namoadigital.prj001.core.form_os.domain.usecase.GeOsCreateFormOsStructureUseCase
import com.namoadigital.prj001.core.form_os.domain.usecase.GeOsMeasureScanUseCase
import com.namoadigital.prj001.core.form_os.domain.usecase.GeOsOrderTypeScanUseCase
import com.namoadigital.prj001.core.form_os.domain.usecase.GeOsSaveSerialStructureUseCase
import com.namoadigital.prj001.core.form_os.domain.usecase.GeOsScanVerificationGroupUseCase
import com.namoadigital.prj001.core.form_os.domain.usecase.GeOsStatusScanUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object GeOsUseCaseModule {

    @Provides
    @ActivityScoped
    fun providesGeOsCreateFormOsStructureUseCase(
        repository: GeOsRepository,
        geOsStatusScanUseCase: GeOsStatusScanUseCase,
    ): GeOsCreateFormOsStructureUseCase {
        return GeOsCreateFormOsStructureUseCase(
            GeOsScanVerificationGroupUseCase(repository),
                    GeOsMeasureScanUseCase(repository, geOsStatusScanUseCase),
                    GeOsOrderTypeScanUseCase(),
                    GeOsApplyVGVisibilityUseCase(),
        )
    }

    @Provides
    @ActivityScoped
    fun providesGeOsSaveSerialStructureUseCase(
        repository: GeOsRepository,
    ): GeOsSaveSerialStructureUseCase {
        return GeOsSaveSerialStructureUseCase(repository)
    }

    @Provides
    @ActivityScoped
    fun providesGeOsStatusScanUseCase(
    ): GeOsStatusScanUseCase {
        return GeOsStatusScanUseCase()
    }


}