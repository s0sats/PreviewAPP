package com.namoadigital.prj001.core.blockchain.model

import com.namoadigital.prj001.core.translate.TranslateWildCard
import com.namoadigital.prj001.core.trip.domain.model.blockchain.TimelineBlockType
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventConflict

sealed class ExternalConflict(
    val data: EventConflict,
    val blockType: TimelineBlockType,
    val message: TranslateWildCard,
    val placeholder: Map<String, String> = emptyMap()
) {
    class TripEvent(
        data: EventConflict,
        blockType: TimelineBlockType,
        translate: TranslateWildCard,
        placeholders: Map<String, String> = emptyMap()
    ) : ExternalConflict(data, blockType, translate, placeholders)

    class EventManual(
        data: EventConflict,
        translate: TranslateWildCard,
        blockType: TimelineBlockType,
        placeholders: Map<String, String> = emptyMap()
    ) : ExternalConflict(data, blockType, translate, placeholders)

    class Form(
        data: EventConflict,
        translate: TranslateWildCard,
        blockType: TimelineBlockType,
        placeholders: Map<String, String> = emptyMap()
    ) : ExternalConflict(data, blockType, translate, placeholders)

    class Action(
        data: EventConflict,
        translate: TranslateWildCard,
        blockType: TimelineBlockType,
        placeholders: Map<String, String> = emptyMap()
    ) : ExternalConflict(data, blockType, translate, placeholders)
}