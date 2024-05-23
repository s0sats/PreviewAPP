package com.namoadigital.prj001.ui.act005.trip.di.usecase.extract

import com.namoadigital.prj001.adapter.trip.model.Extract
import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.data.destination.action.TripDestinationActionRepository
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.extensions.date.convertDateToFullTimeStampGMT
import com.namoadigital.prj001.ui.act005.trip.repository.event.TripEventRepository
import com.namoadigital.prj001.ui.act005.trip.repository.users.TripUserRepository
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf

class ListExtractUseCase constructor(
    private val repositoryUser: TripUserRepository,
    private val repositoryEvent: TripEventRepository,
    private val repositoryTrip: TripRepository,
    private val repositoryDestination: TripDestinationRepository,
    private val repositoryDestinationAction: TripDestinationActionRepository
) : UseCaseWithoutFlow<Unit, List<Extract<*>>> {
    override fun invoke(input: Unit): List<Extract<*>> {
        val list = mutableListOf<Extract<*>>()
        val trip = repositoryTrip.getTrip()
        repositoryTrip.getExtract(trip)?.let(list::add)
        repositoryUser.getExtract(trip).let(list::addAll)
        repositoryEvent.getExtract(trip).let(list::addAll)
        repositoryDestination.getExtract(trip).let(list::addAll)
        repositoryDestinationAction.getExtract(trip).let(list::addAll)



        return list.sortedWith(compareBy<Extract<*>> {
            it.dateStart?.let{ dateStart ->
                ToolBox_Inf.dateToMilliseconds(dateStart.convertDateToFullTimeStampGMT(
                    ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT_GMT,
                    "yyyy-MM-dd HH:mm:00 ZZZZZ"
                    )
                )
            }?: ToolBox_Inf.dateToMilliseconds(it.dateStart) }.thenBy { it.type})


    }
}