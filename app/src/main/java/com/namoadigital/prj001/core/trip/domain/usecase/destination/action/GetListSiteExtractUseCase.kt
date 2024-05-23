package com.namoadigital.prj001.core.trip.domain.usecase.destination.action

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.action.TripDestinationActionRepository
import com.namoadigital.prj001.core.trip.domain.model.FormStatus
import com.namoadigital.prj001.core.trip.domain.model.TripSiteExtract
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.util.ToolBox_Inf

class GetListSiteExtractUseCase constructor(
    private val repository: TripDestinationActionRepository,
) : UseCaseWithoutFlow<FsTripDestination, List<TripSiteExtract<*>>> {
    override fun invoke(input: FsTripDestination): List<TripSiteExtract<*>> {
        val listForm = mutableListOf<TripSiteExtract<*>>()
        repository.getFormInProcessExtract(
            customerCode = input.customerCode,
            tripPrefix = input.tripPrefix,
            tripCode = input.tripCode,
            destinationSeq = input.destinationSeq,
        ).let(listForm::addAll)


        repository.getDestinationActionExtract(
            customerCode = input.customerCode,
            tripPrefix = input.tripPrefix,
            tripCode = input.tripCode,
            destinationSeq = input.destinationSeq,
        ).let {
            listForm.addAll(it.sortedByDescending { action ->
                ToolBox_Inf.dateToMilliseconds(action.model.dateStart)
            })
        }

        listForm.sortBy {
            it.formStatus.weight
        }

        return listForm
    }

}
