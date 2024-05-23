package com.namoadigital.prj001.core.trip.domain.usecase

import com.namoa_digital.namoa_library.ctls.SearchableSpinner
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.trip.TripRepository

class ListSitesUseCase constructor(
    private val repository: TripRepository,
) : UseCaseWithoutFlow<Unit, List<HMAux>>{
    override fun invoke(input: Unit): List<HMAux> {
        val list = mutableListOf<HMAux>()

        repository.getListSites().map {
            val hmAux = HMAux()
            hmAux[SearchableSpinner.ID] = it.siteId
            hmAux[SearchableSpinner.CODE] = "${it.siteCode}"
            hmAux[SearchableSpinner.DESCRIPTION] = it.siteName
            list.add(hmAux)
        }

        return list
    }

}
