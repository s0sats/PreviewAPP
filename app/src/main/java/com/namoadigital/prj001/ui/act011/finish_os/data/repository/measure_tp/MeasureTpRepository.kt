package com.namoadigital.prj001.ui.act011.finish_os.data.repository.measure_tp

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.model.MeMeasureTp
import kotlinx.coroutines.flow.Flow

interface MeasureTpRepository {

    fun getMeasureTpByCode(code: Int): Flow<IResult<MeMeasureTp?>>

}