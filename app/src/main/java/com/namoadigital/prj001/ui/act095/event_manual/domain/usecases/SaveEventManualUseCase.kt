package com.namoadigital.prj001.ui.act095.event_manual.domain.usecases

import android.content.Context
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.extensions.getUserCode
import com.namoadigital.prj001.extensions.suspendResults
import com.namoadigital.prj001.ui.act095.event_manual.domain.repository.EventManualRepository
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventManualData
import com.namoadigital.prj001.util.ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT_GMT
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class SaveEventManualUseCase @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val repository: EventManualRepository
) : UseCases<SaveEventManualUseCase.Params, Unit> {

    data class Params(
        val data: EventManualData,
        val isEditMode: Boolean
    )

    override suspend fun invoke(input: Params): Flow<IResult<Unit>> = flow {
        val eventData = prepareEventData(input.data, input.isEditMode)

        repository.saveEvent(eventManualData = eventData).suspendResults(
            success = { event -> emit(success(Unit)) },
            failed = { emit(failed(it)) },
            loading = { isLoading, msg -> emit(loading(isLoading, msg)) }
        )

    }.catch { e -> emit(failed(e)) }

    private fun prepareEventData(data: EventManualData, isEditMode: Boolean): EventManualData {
        if (isEditMode) return data

        val date = SimpleDateFormat(FULL_TIMESTAMP_TZ_FORMAT_GMT).parse(data.startDate!!)!!
        val today = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date).toInt()
        val userCode = appContext.getUserCode().toInt()

        return data.copy(
            primaryData = data.primaryData.copy(
                eventDay = today,
                eventUser = userCode
            )
        )
    }
}
