package com.namoadigital.prj001.ui.act011.finish_os.di.usecase.measure

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.model.MeMeasureTp
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.measure_tp.MeasureTpRepository
import kotlinx.coroutines.flow.Flow

class GetMeasureTpByCode(
    private val repository: MeasureTpRepository
) : UseCases<Int, MeMeasureTp?> {

    override suspend fun invoke(input: Int): Flow<IResult<MeMeasureTp?>> {
        return repository.getMeasureTpByCode(input)
    }

}