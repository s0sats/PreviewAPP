package com.namoadigital.prj001.ui.act011.finish_os.di.usecase.ge_os

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.isSuccess
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.extensions.suspendResults
import com.namoadigital.prj001.model.GeOsDeviceItem
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_os.GeOsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class GetMissingForecastAnswersUseCase @Inject constructor(
    private val repository: GeOsRepository
) : UseCases<GetGeOsByIdUseCase.Param, List<GeOsDeviceItem>> {


    override suspend fun invoke(input: GetGeOsByIdUseCase.Param): Flow<IResult<List<GeOsDeviceItem>>> {
        return flow {
            emit(loading())
            repository.getAllDeviceById(
                input.formType,
                input.formCode,
                input.formVersion,
                input.formData
            ).suspendResults(
                success = { devices ->
                    val missingAnswers = mutableListOf<GeOsDeviceItem>()


                    devices.forEach { item ->
                        repository.getListDeviceItemById(
                            item.custom_form_type,
                            item.custom_form_code,
                            item.custom_form_version,
                            item.custom_form_data.toLong(),
                            item.product_code.toLong(),
                            item.serial_code.toLong(),
                            item.device_tp_code
                        ).suspendResults(
                            success = { list ->
                                if(list.isNotEmpty()){
                                    missingAnswers.addAll(list)
                                }
                            },
                            failed = {
                                this@flow.emit(failed(it))
                            }
                        )
                    }

                    emit(loading(false))
                    emit(success(missingAnswers))
                },
                failed = { emit(failed(it)) }
            )
        }
    }
}