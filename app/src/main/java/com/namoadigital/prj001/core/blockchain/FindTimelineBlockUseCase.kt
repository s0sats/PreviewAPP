package com.namoadigital.prj001.core.blockchain

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.domain.model.blockchain.FindBlockResult
import com.namoadigital.prj001.core.trip.domain.model.blockchain.TimelineBlockType
import com.namoadigital.prj001.core.trip.domain.model.blockchain.TripTimelineBlock
import com.namoadigital.prj001.core.trip.domain.model.blockchain.isReal
import com.namoadigital.prj001.core.trip.domain.model.blockchain.isVirtual
import com.namoadigital.prj001.util.ToolBox_Inf
import javax.inject.Inject

class FindTimelineBlockUseCase @Inject constructor() :
    UseCaseWithoutFlow<FindTimelineBlockUseCase.Input, FindBlockResult> {

    data class Input(
        val startDate: String,
        val endDate: String?,
        val timeline: List<TripTimelineBlock>,
        val blockToIgnore: TripTimelineBlock? = null
    )

    override operator fun invoke(input: Input): FindBlockResult {
        val (startDate, endDate, timeline, blockToIgnore) = input

        val startMs = ToolBox_Inf.dateToMilliseconds(startDate)
        val endMs = endDate?.let { ToolBox_Inf.dateToMilliseconds(it) } ?: Long.MAX_VALUE

        if (startMs > endMs) return FindBlockResult.InvalidDateRange

        val newTimeline = if (blockToIgnore != null) {
            timeline.filterNot { it == blockToIgnore || it.type is TimelineBlockType.RETURN_TRIP && blockToIgnore.type is TimelineBlockType.RETURN_TRIP }
        } else {
            timeline
        }

        val containing = newTimeline.filter { block ->
            startMs >= block.startMs() && endMs <= block.endMs()
        }

        if (containing.size == 1) {
            return FindBlockResult.Success(containing.first())
        }

        val intersecting = newTimeline.filter { block ->
            if(blockToIgnore?.type is TimelineBlockType.ON_SITE){
                block.startMs() <= endMs && block.endMs() >= startMs
            }else{
                block.startMs() < endMs && block.endMs() > startMs
            }
        }

        if (intersecting.isNotEmpty()) {

            val solids = intersecting.filter { it.type.isReal() }
            val virtualBlocks = intersecting.filter { it.type.isVirtual() }

            if (solids.isEmpty() && virtualBlocks.isNotEmpty()) {
                return buildNotFound(startMs, endMs, newTimeline)
            }

            if (solids.size > 1 && virtualBlocks.isEmpty()) {
                val sorted = solids.sortedBy { it.startMs() }
                return FindBlockResult.OverlapError(sorted.first(), sorted.last())
            }

            if (solids.isNotEmpty()) {
                return FindBlockResult.OverlapError(solids.last(), null)
            }
        }

        return buildNotFound(startMs, endMs, newTimeline)
    }

    private fun buildNotFound(
        startMs: Long,
        endMs: Long,
        timeline: List<TripTimelineBlock>
    ): FindBlockResult {

        val previous = timeline
            .filter { it.endMs() <= startMs }
            .maxByOrNull { it.endMs() }

        val next = timeline
            .filter { it.startMs() >= endMs }
            .minByOrNull { it.startMs() }

        return FindBlockResult.NotFound(previous, next)
    }
}
