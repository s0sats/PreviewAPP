package com.namoadigital.prj001.core.trip.domain.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.model.location.Coordinates
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.origin.enums.OriginType

class SaveOriginUseCase constructor(
    private val repository: TripRepository
) : UseCaseWithoutFlow<SaveOriginUseCase.SaveOriginParam, Unit>{

    data class SaveOriginParam(
        val date: String,
        val type: OriginType,
        val siteCode: Int? = null,
        val siteDesc: String? = null,
        val coordinates: Coordinates? = null
    )

    override fun invoke(input: SaveOriginParam) {
        repository.execOriginSet(
            input.date,
            input.type,
            input.coordinates,
            input.siteCode,
            input.siteDesc
        )
    }

}
