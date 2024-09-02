package com.namoadigital.prj001.core.trip.domain.usecase

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.error
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.extensions.suspendResults
import com.namoadigital.prj001.service.location.FsTripLocationService
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.origin.enums.OriginType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SaveOriginUseCase constructor(
    private val repository: TripRepository
) : UseCases<SaveOriginUseCase.SaveOriginParam, Unit> {

    data class SaveOriginParam(
        val date: String,
        val type: OriginType,
        val siteCode: Int? = null,
        val siteDesc: String? = null,
//        val coordinates: Coordinates? = null
    )

    override suspend fun invoke(input: SaveOriginParam): Flow<IResult<Unit>> {
        return flow {
            val (date, originType, siteCode, siteDesc) = input
            val currentCoordinates = FsTripLocationService.LatLog.value
            if(originType == OriginType.GPS){
                if(currentCoordinates.isDefault){
                    emit(failed(LocationNotFound()))
                    return@flow
                }
            }


            repository.execOriginSet(
                date = date,
                originType = originType,
                coordinates = currentCoordinates,
                siteCode = siteCode,
                siteDesc = siteDesc
            ).suspendResults(
                success = {
                    emit(success(it))
                },
                failed = {
                    emit(failed(it))
                },
                loading = { isLoading, message ->
                    emit(loading(isLoading, message))
                },
                error = { string, throwable ->
                    emit(error(string, throwable))
                }
            )
        }
    }


}

class LocationNotFound() : Exception("Location not found.")