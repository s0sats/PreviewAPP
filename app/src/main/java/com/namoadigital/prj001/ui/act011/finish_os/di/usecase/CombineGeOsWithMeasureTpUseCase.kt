package com.namoadigital.prj001.ui.act011.finish_os.di.usecase

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.extensions.suspendResults
import com.namoadigital.prj001.model.MeMeasureTp
import com.namoadigital.prj001.model.masterdata.ge_os.GeOs
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.ge_os.GetGeOsByIdUseCase
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.measure.GetMeasureTpByCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CombineGeOsWithMeasureTpUseCase(
    private val getGeOsByIdUseCase: GetGeOsByIdUseCase,
    private val getMeasureTpByCode: GetMeasureTpByCode,
) : UseCases<CombineGeOsWithMeasureTpUseCase.Input, CombineGeOsWithMeasureTpUseCase.Output> {
    data class Input(
        val formType: Int,
        val formCode: Int,
        val formVersion: Int,
        val formData: Long,
    )

    data class Output(
        val geos: GeOs?,
        val measureTp: MeMeasureTp?
    )

    override suspend fun invoke(input: Input): Flow<IResult<Output>> {
        return flow {
            getGeOsByIdUseCase.invoke(
                GetGeOsByIdUseCase.Param(
                    formType = input.formType,
                    formCode = input.formCode,
                    formVersion = input.formVersion,
                    formData = input.formData,
                )
            ).suspendResults(
                success = { geOs ->
                    getMeasureTpByCode.invoke(geOs?.measure_tp_code ?: -1).suspendResults(
                        success = { measure ->
                            emit(IResult.success(Output(geOs, measure)))
                        },
                        failed = {
                            emit(IResult.failed(it))
                        }
                    )
                }
            )
        }
    }
}