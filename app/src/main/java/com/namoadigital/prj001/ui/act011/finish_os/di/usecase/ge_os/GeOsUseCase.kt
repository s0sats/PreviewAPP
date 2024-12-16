package com.namoadigital.prj001.ui.act011.finish_os.di.usecase.ge_os

data class GeOsUseCase(
    val getGeOsById: GetGeOsByIdUseCase,
    val getMissingForecastAnswersUseCase: GetMissingForecastAnswersUseCase
)
