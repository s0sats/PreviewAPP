package com.namoadigital.prj001.ui.act011.finish_os.di.modules

import com.namoadigital.prj001.core.data.domain.usecase.serial.product.GetProductSerialByIdUseCase
import com.namoadigital.prj001.core.data.local.repository.serial.ProductSerialRepository
import com.namoadigital.prj001.core.data.local.repository.ticket.TicketRepository
import com.namoadigital.prj001.core.trip.domain.usecase.ticket.GetTicketByIdUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.ticket.GetTicketFormByIdUseCase
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_custom_form.GeCustomFormRepository
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_os.GeOsRepository
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.measure_tp.MeasureTpRepository
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.BackupMachineSaveUseCase
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.BackupMachineSearchUseCase
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.FinishOSUseCase
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.GetFinishOsDataUseCase
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.SaveFormOsUseCase
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.ValidateFinishOSUseCase
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.ge_custom.GeCustomFormUseCase
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.ge_custom.GetCustomFormDataByIdUseCase
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.ge_custom.GetCustomFormLocalByIdUseCase
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.ge_os.GeOsUseCase
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.ge_os.GetGeOsByIdUseCase
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.ge_os.GetMissingForecastAnswersUseCase
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.measure.GetMeasureTpByCode
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object FormUseCaseModule {

    @Provides
    @ViewModelScoped
    fun providesGeCustomFormUseCase(
        repository: GeCustomFormRepository
    ): GeCustomFormUseCase {
        return GeCustomFormUseCase(
            GetCustomFormDataByIdUseCase(repository),
            GetCustomFormLocalByIdUseCase(repository),
        )
    }

    @Provides
    @ViewModelScoped
    fun providesGeOsUseCase(
        repository: GeOsRepository
    ): GeOsUseCase {
        return GeOsUseCase(
            GetGeOsByIdUseCase(repository),
            GetMissingForecastAnswersUseCase(repository)
        )
    }

    @Provides
    @ViewModelScoped
    fun providesFinishOsUseCase(
        useCase: GeCustomFormUseCase,
        repository: GeCustomFormRepository,
        geOsRepository: GeOsRepository,
        ticketRepository: TicketRepository,
        productSerialRepository: ProductSerialRepository,
        geOSUseCase: GeOsUseCase,
    ): FinishOSUseCase {
        return FinishOSUseCase(
            getFinishOsData = GetFinishOsDataUseCase(
                getFormDataById = useCase.getFormDataById,
                getMissingForecastAnswersUseCase = geOSUseCase.getMissingForecastAnswersUseCase,
                geOsByIdUseCase = geOSUseCase.getGeOsById,
                ticketByIdUseCase = GetTicketByIdUseCase(ticketRepository),
                ticketFormByIdUseCase = GetTicketFormByIdUseCase(ticketRepository),
                productSerialByIdUseCase = GetProductSerialByIdUseCase(productSerialRepository)
            ),
            saveOs = SaveFormOsUseCase(repository, geOsRepository),
            backupMachineSearch = BackupMachineSearchUseCase(productSerialRepository),
            backupMachineSave = BackupMachineSaveUseCase(repository, geOsRepository),
        )
    }

    @Provides
    @ViewModelScoped
    fun providesGetMeasureTpByCode(
        repository: MeasureTpRepository
    ): GetMeasureTpByCode {
        return GetMeasureTpByCode(repository)
    }


    @Provides
    @ViewModelScoped
    fun providesValidateFormUseCase(
        getGeOsByIdUseCase: GetGeOsByIdUseCase,
        getMeasureTpByCode: GetMeasureTpByCode,
    ): ValidateFinishOSUseCase {
        return ValidateFinishOSUseCase(
            getGeOsByIdUseCase = getGeOsByIdUseCase,
            getMeasureTpByCode = getMeasureTpByCode,
        )
    }
}