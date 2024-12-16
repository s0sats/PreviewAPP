package com.namoadigital.prj001.ui.act011.finish_os.data.repository.measure_tp

import android.content.Context
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.dao.MeMeasureTpDao
import com.namoadigital.prj001.extensions.getCustomerCode
import com.namoadigital.prj001.model.MeMeasureTp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MeasureTpRepositoryImpl constructor(
    private val context: Context,
    private val dao: MeMeasureTpDao
) : MeasureTpRepository {
    override fun getMeasureTpByCode(code: Int): Flow<IResult<MeMeasureTp?>> {
        return flow {
            val model = dao.getMeasureTpByCode(context.getCustomerCode(), code)
            emit(success(model))
        }
    }
}