package com.namoadigital.prj001.core.blockchain

import android.content.Context
import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.blockchain.model.ExternalConflict
import com.namoadigital.prj001.core.translate.TranslateWildCard
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.data.destination.action.TripDestinationActionRepository
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.core.trip.domain.model.blockchain.FindBlockResult
import com.namoadigital.prj001.core.trip.domain.model.blockchain.TimelineBlockType
import com.namoadigital.prj001.core.trip.domain.model.blockchain.TimelineValidationAction
import com.namoadigital.prj001.core.trip.domain.model.blockchain.TripTimelineBlock
import com.namoadigital.prj001.core.trip.domain.model.blockchain.ValidationResult
import com.namoadigital.prj001.core.trip.domain.model.enums.TimelineBlockTranslate
import com.namoadigital.prj001.extensions.date.FormatDateType
import com.namoadigital.prj001.extensions.date.formatDate
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.ui.act005.trip.repository.event.TripEventRepository
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_custom_form.GeCustomFormRepository
import com.namoadigital.prj001.ui.act095.event_manual.domain.repository.EventManualRepository
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventConflict
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventConflictType
import com.namoadigital.prj001.util.ToolBox_Inf
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ValidateTimelineBlockUseCase @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val createTripTimelineBlocksUseCase: CreateTripTimelineBlocksUseCase,
    private val findTimelineBlockUseCase: FindTimelineBlockUseCase,
    private val tripEventRepository: TripEventRepository,
    private val formRepository: GeCustomFormRepository,
    private val actionRepository: TripDestinationActionRepository,
    private val eventManualRepository: EventManualRepository,
    private val tripRepository: TripRepository,
    private val tripDestinationRepository: TripDestinationRepository,
) : UseCaseWithoutFlow<TimelineValidationAction, ValidationResult> {

    private val dateCache = mutableMapOf<String, Long>()

    override fun invoke(input: TimelineValidationAction): ValidationResult {
        try {
            val currentTrip = tripRepository.getTrip()

            val timeline by lazy {
                currentTrip?.let { createTripTimelineBlocksUseCase(Unit) } ?: run {
                    emptyList()
                }
            }

            return when (input) {
                is TimelineValidationAction.DestinationEdit ->
                    validateDestinationEdit(input, timeline, currentTrip!!)

                is TimelineValidationAction.TripStartDateEdit ->
                    validateTripStartEdit(input, timeline, currentTrip!!)

                is TimelineValidationAction.TripEndDateEdit ->
                    validateTripEndEdit(input, timeline, currentTrip!!)

                is TimelineValidationAction.TripOriginDateEdit ->
                    validateTripOriginEdit(input, timeline, currentTrip!!)

                is TimelineValidationAction.ValidateEvent ->
                    validateEventOrForm(
                        blockType = TimelineBlockType.EVENT(
                            startDate = input.startDate,
                            endDate = input.endDate,
                            eventSeq = input.eventSeq,
                            withWaiting = input.withWaiting
                        ),
                        timeline = timeline,
                        currentTrip = currentTrip!!
                    )

                is TimelineValidationAction.ValidateForm ->
                    validateEventOrForm(
                        blockType = TimelineBlockType.FORM(
                            startDate = input.startDate,
                            endDate = input.endDate,
                            destinationSeq = tripDestinationRepository.getCurrentDestination()?.destinationSeq,
                            formPK = input.formPK
                        ),
                        timeline = timeline,
                        currentTrip = currentTrip
                    )
            }
        } finally {
           dateCache.clear()
        }
    }

    private fun validateDestinationEdit(
        action: TimelineValidationAction.DestinationEdit,
        timeline: List<TripTimelineBlock>,
        currentTrip: FSTrip,
    ): ValidationResult {

        val startMs = ToolBox_Inf.dateToMilliseconds(action.newArrivedDate)
        val endMs = action.newDepartedDate?.let { ToolBox_Inf.dateToMilliseconds(it) } ?: Long.MAX_VALUE

        if(startMs == endMs){
            return createConflict(null, TimelineBlockTranslate.ERROR_DATE_RANGE_LBL)
        }

        val blockToIgnore = timeline.findOnSiteBlock(action.destinationSeq)

        val findResult = findTimelineBlock(
            startDate = action.newArrivedDate,
            endDate = action.newDepartedDate,
            timeline = timeline,
            blockToIgnore = blockToIgnore
        )

        val blockType = TimelineBlockType.ON_SITE(
            startDate = action.newArrivedDate,
            endDate = action.newDepartedDate,
            destinationSeq = action.destinationSeq
        )

        //1 - Validação de códigos
        validateTimelineBlock(findResult, currentTrip, blockToIgnore)
            .takeUnless { it is ValidationResult.Success }
            ?.let { return it }


        //2 - Validação de consistência de dados com formulário
        validateConsistencyOfDatas(currentTrip, action)
            .takeIf { it is ValidationResult.Conflict }
            ?.let { return it }

        //3 - Validação de conflito externo (evento, trip, form)
        checkExternalConflicts(currentTrip, blockType)
            .takeUnless { it is ValidationResult.Success }
            ?.let { return it }

        //4 - Validação de sequência de destino
        return checkDestinationSequence(timeline, blockType)
    }

    private fun validateConsistencyOfDatas(
        currentTrip: FSTrip,
        action: TimelineValidationAction.DestinationEdit,
    ): ValidationResult.Conflict? {
       return actionRepository.getDestinationFormDateConflict(
            tripPrefix = currentTrip.tripPrefix,
            tripCode = currentTrip.tripCode,
            destinationSeq = action.destinationSeq,
            newArrivedDate = action.newArrivedDate,
            newDepartedDate = action.newDepartedDate
        )?.let { conflict ->
            ValidationResult.Conflict(
                conflictingBlock = null,
                message = TimelineBlockTranslate.ERROR_DESTINATION_CONFLICT_WITH_FORM_LBL,
            ).addPlaceholders(
                mapOf(
                    "date_start" to formatDateTime(conflict.dateStart!!),
                    "date_end" to formatDateRange(conflict.dateEnd)
                )
            ) as ValidationResult.Conflict
        }
    }

    private fun validateTripStartEdit(
        action: TimelineValidationAction.TripStartDateEdit,
        timeline: List<TripTimelineBlock>,
        currentTrip: FSTrip,
    ): ValidationResult {
        val originDate = currentTrip.originDate ?: return ValidationResult.Success
        val blockToIgnore = timeline.find { it.type is TimelineBlockType.PRE_TRIP }

        val findResult = findTimelineBlock(
            startDate = originDate,
            endDate = action.newStartDate,
            timeline = timeline,
            blockToIgnore = blockToIgnore
        )

        val validationResult = when (findResult) {
            is FindBlockResult.NotFound ->
                validateTripBoundary(findResult.next, action.newStartDate, isStart = true)

            is FindBlockResult.InvalidDateRange ->
                createConflict(
                    block = null,
                    message = TimelineBlockTranslate.CREATE_TRIP_DESCRIPTION,
                    placeholders = mapOf("trip_create_date" to formatDateTime(originDate))
                )

            is FindBlockResult.OverlapError ->
                handleOverlapForTripBoundary(findResult.overlapStart, currentTrip)

            else -> ValidationResult.Success
        }

        return validationResult.takeUnless { it is ValidationResult.Success }
            ?: checkExternalConflicts(
                currentTrip = currentTrip,
                blockType = TimelineBlockType.PRE_TRIP(
                    startDate = originDate,
                    endDate = action.newStartDate
                )
            )
    }

    private fun validateTripEndEdit(
        action: TimelineValidationAction.TripEndDateEdit,
        timeline: List<TripTimelineBlock>,
        currentTrip: FSTrip,
    ): ValidationResult {

        fun toMillis(value: String) = ToolBox_Inf.dateToMilliseconds(value)

        val startTrip = currentTrip.startDate
            ?: return ValidationResult.Success

        val lastDate = tripRepository.getLastDateFromTrip(
            tripPrefix = currentTrip.tripPrefix,
            tripCode = currentTrip.tripCode
        )

        val startDate = lastDate
            ?.takeIf { toMillis(it) >= toMillis(startTrip) }
            ?: startTrip

        val blockToIgnore = TripTimelineBlock(
            type = TimelineBlockType.RETURN_TRIP(
                startDate = startDate,
                endDate = action.newEndDate
            )
        )

        val findResult = findTimelineBlock(
            startDate = startDate,
            endDate = action.newEndDate,
            timeline = timeline,
            blockToIgnore = blockToIgnore
        )

        return when (findResult) {
            is FindBlockResult.NotFound ->
                validateTripBoundary(findResult.next, action.newEndDate, isStart = false)

            is FindBlockResult.InvalidDateRange ->
                createConflict(
                    block = blockToIgnore,
                    message = TimelineBlockTranslate.ERROR_CONFLICT_TRIP_DATE_END_LBL,
                    placeholders = mapOf(
                        "date_start" to formatDateTime(blockToIgnore.type.startDate),
                        "date_end" to formatDateTime(blockToIgnore.type.endDate!!)
                    )
                )

            is FindBlockResult.Success -> ValidationResult.Success

            else -> createTripCreateConflict(blockToIgnore, startTrip, currentTrip.startDate!!)
        }
    }

    private fun validateTripOriginEdit(
        action: TimelineValidationAction.TripOriginDateEdit,
        timeline: List<TripTimelineBlock>,
        currentTrip: FSTrip,
    ): ValidationResult {
        val blockToIgnore = timeline.find { it.type is TimelineBlockType.PRE_TRIP }
        val endDate = currentTrip.startDate

        val findResult = findTimelineBlock(
            startDate = action.newOriginDate,
            endDate = endDate,
            timeline = timeline,
            blockToIgnore = blockToIgnore
        )

        val validationResult = when (findResult) {
            is FindBlockResult.NotFound ->
                validateOriginNotFound(
                    findResult.previous,
                    findResult.next,
                    action.newOriginDate,
                    currentTrip
                )

            is FindBlockResult.InvalidDateRange ->
                createConflict(
                    block = null,
                    message = TimelineBlockTranslate.ERROR_CONFLICT_WITH_START_TRIP_LBL,
                    placeholders = mapOf("trip_start_date" to formatDateTime(currentTrip.startDate!!))
                )

            is FindBlockResult.Success -> ValidationResult.Success

            else -> createConflict(null, TimelineBlockTranslate.ERROR_CONFLICT_GENERIC_LBL)
        }

        return validationResult.takeUnless { it is ValidationResult.Success }
            ?: checkExternalConflicts(
                currentTrip = currentTrip,
                blockType = TimelineBlockType.PRE_TRIP(
                    startDate = action.newOriginDate,
                    endDate = endDate
                )
            )
    }

    private fun validateEventOrForm(
        blockType: TimelineBlockType,
        timeline: List<TripTimelineBlock>,
        currentTrip: FSTrip?,
    ): ValidationResult {
        if (blockType is TimelineBlockType.EVENT && !blockType.withWaiting) {
            return currentTrip?.let { validateEventWithoutWaiting(blockType, it) }
                ?: ValidationResult.Success
        }

        if(blockType is TimelineBlockType.EVENT) {
            if (blockType.startMs() == blockType.endMs()) {
                return ValidationResult.Conflict(
                    conflictingBlock = null,
                    message = TimelineBlockTranslate.DIALOG_EVENT_DATE_EQUAL_ERROR_LBL,
                )
            }
        }

        return if (currentTrip == null) {
            if(blockType.endDate != null && blockType.startMs() > blockType.endMs()!!){
                return createConflict(null, TimelineBlockTranslate.ERROR_DATE_RANGE_LBL)
            }

            checkExternalConflicts(blockType = blockType)
        } else {
            processTripValidation(blockType, timeline, currentTrip)
        }
    }

    private fun processTripValidation(
        blockType: TimelineBlockType,
        timeline: List<TripTimelineBlock>,
        currentTrip: FSTrip,
    ): ValidationResult {
        val blockToIgnore = TripTimelineBlock(type = blockType)

        val findResult = findTimelineBlock(
            startDate = blockType.startDate,
            endDate = blockType.endDate,
            timeline = timeline,
            blockToIgnore = blockToIgnore
        )

        val result = when (findResult) {
            is FindBlockResult.NotFound ->
                validateEventFormNotFound(findResult, blockType, currentTrip)
            else ->
                validateTimelineBlock(findResult, currentTrip, blockToIgnore)
        }

        return result.takeUnless { it is ValidationResult.Success }
            ?: checkExternalConflicts(currentTrip, blockType)
    }

    private fun validateTimelineBlock(
        findResult: FindBlockResult,
        currentTrip: FSTrip?,
        blockToIgnore: TripTimelineBlock? = null
    ): ValidationResult = when (findResult) {
        is FindBlockResult.Success ->
            handleSuccessResult(findResult, blockToIgnore)

        is FindBlockResult.NotFound ->
            handleNotFoundResult(findResult, currentTrip)

        is FindBlockResult.OverlapError ->
            handleOverlapError(findResult, currentTrip, blockToIgnore)

        is FindBlockResult.InvalidDateRange ->
            createConflict(null, TimelineBlockTranslate.ERROR_DATE_RANGE_LBL)
    }

    private fun handleSuccessResult(
        findResult: FindBlockResult.Success,
        blockToIgnore: TripTimelineBlock?
    ): ValidationResult {
        val foundBlock = findResult.block

        return when {
            isConflictingOnSiteBlock(foundBlock, blockToIgnore) ->
                createDestinationConflict(
                    block = foundBlock,
                    message = TimelineBlockTranslate.ERROR_CONFLICT_WITH_ON_SITE_LBL
                )

            isFormOutsideOnSite(foundBlock, blockToIgnore) ->
                createDestinationConflict(
                    block = foundBlock,
                    message = TimelineBlockTranslate.ERROR_CONFLICT_OUT_OF_ON_SITE_LBL
                )

            else -> ValidationResult.Success
        }
    }

    private fun handleNotFoundResult(
        findResult: FindBlockResult.NotFound,
        currentTrip: FSTrip?
    ): ValidationResult {
        val nextBlock = findResult.next ?: return ValidationResult.Success

        return if (nextBlock.type is TimelineBlockType.PRE_TRIP) {
            createTripCreateConflict(
                block = nextBlock,
                tripCreateDate = nextBlock.type.startDate,
                tripStartDate = currentTrip?.startDate
            )
        } else {
            ValidationResult.Success
        }
    }

    private fun handleOverlapError(
        findResult: FindBlockResult.OverlapError,
        currentTrip: FSTrip?,
        blockToIgnore: TripTimelineBlock?
    ): ValidationResult {
        val block = findResult.overlapStart ?: return ValidationResult.Success

        return when (block.type) {
            is TimelineBlockType.PRE_TRIP ->
                createTripCreateConflict(block, block.type.startDate, block.type.endDate)

            is TimelineBlockType.ON_SITE ->
                validateOnSiteOverlap(block, blockToIgnore)

            else -> ValidationResult.Success
        }
    }

    private fun handleOverlapForTripBoundary(
        block: TripTimelineBlock?,
        currentTrip: FSTrip
    ): ValidationResult {
        block ?: return ValidationResult.Success

        return when (block.type) {
            is TimelineBlockType.ON_SITE ->
                createDestinationConflict(
                    block = block,
                    message = TimelineBlockTranslate.ERROR_CONFLICT_WITH_ON_SITE_LBL
                )

            is TimelineBlockType.EVENT ->
                createEventConflict(block, currentTrip)

            else -> ValidationResult.Success
        }
    }

    private fun validateOnSiteOverlap(
        block: TripTimelineBlock,
        blockToIgnore: TripTimelineBlock?
    ): ValidationResult {
        val newStartMs = dateToMs(blockToIgnore?.type?.startDate) ?: return ValidationResult.Success
        val newEndMs = dateToMs(blockToIgnore?.type?.endDate)

        return when {
            // Caso especial para FORM
            blockToIgnore?.type is TimelineBlockType.FORM ->
                createDestinationConflict(
                    block = block,
                    message = TimelineBlockTranslate.ERROR_CONFLICT_OUT_OF_ON_SITE_LBL)

            // Conflitos de início
            newStartMs <= block.startMs() ->
                createDestinationConflict(
                    block = block,
                    message = TimelineBlockTranslate.ERROR_CONFLICT_WITH_ON_SITE_LBL )

            // Conflitos de fim
            block.endMs() <= newStartMs ||
                    (newEndMs != null && (newEndMs >= block.endMs() || block.startMs() <= newEndMs)) ->
                createDestinationConflict(
                    block = block,
                    message = TimelineBlockTranslate.ERROR_CONFLICT_WITH_ON_SITE_LBL)

            else -> ValidationResult.Success
        }
    }

    private fun validateTripBoundary(
        boundaryBlock: TripTimelineBlock?,
        newDate: String,
        isStart: Boolean
    ): ValidationResult {
        boundaryBlock ?: return ValidationResult.Success

        val newDateMs = dateToMs(newDate) ?: return ValidationResult.Success
        val boundaryMs = if (isStart) boundaryBlock.startMs() else boundaryBlock.endMs()

        val hasConflict = if (isStart) newDateMs > boundaryMs else newDateMs < boundaryMs

        return if (hasConflict) {
            createDestinationConflict(
                block = boundaryBlock,
                message = TimelineBlockTranslate.ERROR_CONFLICT_WITH_FIRST_DESTINATION_LBL
            )
        } else {
            ValidationResult.Success
        }
    }

    private fun validateOriginNotFound(
        previousBlock: TripTimelineBlock?,
        nextBlock: TripTimelineBlock?,
        newOriginDate: String,
        currentTrip: FSTrip
    ): ValidationResult {
        val newStartMs = dateToMs(newOriginDate) ?: return ValidationResult.Success

        return when {
            previousBlock != null && newStartMs < previousBlock.endMs() ->
                createTripCreateConflict(
                    block = previousBlock,
                    tripCreateDate = previousBlock.type.startDate,
                    tripStartDate = currentTrip.startDate
                )

            nextBlock != null && newStartMs > nextBlock.startMs() ->
                createDestinationConflict(
                    block = nextBlock,
                    message = TimelineBlockTranslate.ERROR_CONFLICT_WITH_FIRST_DESTINATION_LBL
                )

            else -> ValidationResult.Success
        }
    }

    private fun validateEventFormNotFound(
        findResult: FindBlockResult.NotFound,
        blockType: TimelineBlockType,
        currentTrip: FSTrip?
    ): ValidationResult {
        val newStartMs = dateToMs(blockType.startDate) ?: return ValidationResult.Success
        val newEndMs = dateToMs(blockType.endDate) ?: return ValidationResult.Success

        return when {
            findResult.previous != null &&  findResult.previous.endMs() <= newStartMs ->
                createEventConflict(findResult.previous, currentTrip)

            findResult.next != null && findResult.next.startMs() >= newEndMs ->
                createEventConflict(findResult.next, currentTrip)

            else -> ValidationResult.Success
        }
    }

    private fun validateEventWithoutWaiting(
        blockType: TimelineBlockType.EVENT,
        currentTrip: FSTrip
    ): ValidationResult {
        val eventStartMs = dateToMs(blockType.startDate) ?: return ValidationResult.Success
        val tripOriginMs = dateToMs(currentTrip.originDate) ?: return ValidationResult.Success

        return if (eventStartMs >= tripOriginMs) {
            ValidationResult.Success
        } else {
            createConflict(
                block = null,
                message = TimelineBlockTranslate.ERROR_CONFLICT_WITH_CREATE_TRIP_LBL,
                placeholders = mapOf(
                    "trip_create_date" to formatDateTime(currentTrip.originDate!!),
                    "trip_start_date" to (currentTrip.startDate?.let {
                        formatDateTime(it)
                    } ?: "")
                )
            )
        }
    }

    private fun checkDestinationSequence(
        timeline: List<TripTimelineBlock>,
        blockType: TimelineBlockType.ON_SITE,
    ): ValidationResult {
        val newStartMs = dateToMs(blockType.startDate) ?: return ValidationResult.Success
        val newDestSeq = blockType.destinationSeq

        // Pega todos os blocos ON_SITE existentes na timeline
        val existingOnSiteBlocks = timeline
            .mapNotNull { block ->
                (block.type as? TimelineBlockType.ON_SITE)?.let {
                    block to it
                }
            }
            .sortedBy { it.first.startMs() }

        // Verifica blocos ANTES do novo bloco
        val blocksBefore = existingOnSiteBlocks.filter { it.first.startMs() < newStartMs }
        if (blocksBefore.isNotEmpty()) {
            val maxSeqBefore = blocksBefore.maxOf { it.second.destinationSeq }

            // O novo destino deve ser >= ao último destino antes dele
            if (newDestSeq < maxSeqBefore) {
                return createDestinationConflict(
                    block = blocksBefore.last().first,
                    message = TimelineBlockTranslate.ERROR_CONFLICT_WITH_ON_SITE_LBL
                )
            }
        }

        // Verifica blocos DEPOIS do novo bloco
        val blocksAfter = existingOnSiteBlocks.filter { it.first.startMs() > newStartMs }
        if (blocksAfter.isNotEmpty()) {
            val minSeqAfter = blocksAfter.minOf { it.second.destinationSeq }

            // O novo destino deve ser <= ao primeiro destino depois dele
            if (newDestSeq > minSeqAfter) {
                return createDestinationConflict(
                    block = blocksAfter.first().first,
                    message = TimelineBlockTranslate.ERROR_CONFLICT_WITH_ON_SITE_LBL
                )
            }
        }

        return ValidationResult.Success
    }

    private fun checkExternalConflicts(
        currentTrip: FSTrip? = null,
        blockType: TimelineBlockType
    ): ValidationResult {
        val conflict = findFirstExternalConflict(currentTrip, blockType)

        return conflict?.let {
            createConflict(
                block = TripTimelineBlock(type = it.blockType),
                message = it.message,
                placeholders = it.placeholder
            )
        } ?: ValidationResult.Success
    }

    private fun findFirstExternalConflict(
        currentTrip: FSTrip?,
        blockType: TimelineBlockType
    ): ExternalConflict? {
        return sequenceOf(
            { if (currentTrip == null) checkTripConflict(blockType) else null },
            { checkTripEventConflict(blockType) },
            { checkEventManualConflict(blockType) },
            { if (blockType !is TimelineBlockType.FORM) checkFormConflict(blockType) else null },
            {
                if (currentTrip != null && blockType !is TimelineBlockType.FORM) checkActionConflict(
                    currentTrip,
                    blockType
                ) else null
            }
        ).firstNotNullOfOrNull { it() }
    }

    private fun checkTripEventConflict(blockType: TimelineBlockType): ExternalConflict? {
        val conflict = tripEventRepository.getTripEventConflict(
            dateStart = blockType.startDate,
            dateEnd = blockType.endDate,
            eventSeq = (blockType as? TimelineBlockType.EVENT)?.eventSeq ?: -1,
            validateStartDateEquals = blockType.isBlock
        ) ?: return null

        // Ignora RANGE_OVERLAP para PRE_TRIP e RETURN_TRIP e Destination
        if (isIgnorableRangeOverlap(blockType, conflict)) {
            return null
        }
        
        return ExternalConflict.TripEvent(
            data = conflict,
            translate = TimelineBlockTranslate.ERROR_CONFLICT_WITH_EVENT,
            placeholders = createDescPlaceholders(conflict),
            blockType = blockType
        )
    }

    private fun checkTripConflict(blockType: TimelineBlockType): ExternalConflict? {
        return tripRepository.getTripConflict(
            startDate = blockType.startDate,
            endDate = blockType.endDate,
        )?.let {
            ExternalConflict.TripEvent(
                data = it,
                translate = TimelineBlockTranslate.ERROR_CONFLICT_TRIP_LBL,
                placeholders = mapOf(
                    "conflict_description" to (it.description ?: ""),
                    "date_start" to formatDateTime(it.dateStart!!),
                    "date_end" to formatDateRange(it.dateEnd)
                ),
                blockType = blockType
            )
        }
    }

    private fun checkFormConflict(
        blockType: TimelineBlockType,
    ): ExternalConflict? {
        val formBlock = blockType as? TimelineBlockType.FORM

        return formRepository.getFormConflict(
            startDate = blockType.startDate,
            endDate = blockType.endDate,
            validRange = !blockType.isBlock,
            formData = formBlock?.formPK?.formData,
            typeCode = formBlock?.formPK?.typeCode,
            formCode = formBlock?.formPK?.code,
            formVersion = formBlock?.formPK?.versionCode,
            validateStartDateEquals = blockType.isBlock
        )?.let {
            ExternalConflict.Form(
                data = it,
                blockType = blockType,
                translate = TimelineBlockTranslate.ERROR_CONFLICT_WITH_FORM_LBL,
                placeholders = createDescPlaceholders(it, "form_desc")
            )
        }
    }

    private fun checkEventManualConflict(blockType: TimelineBlockType): ExternalConflict? {
        return eventManualRepository.getEventConflict(
            startDate = blockType.startDate,
            endDate = blockType.endDate,
        )?.let {
            ExternalConflict.EventManual(
                data = it,
                blockType = blockType,
                translate = TimelineBlockTranslate.ERROR_CONFLICT_WITH_EVENT,
                placeholders = createDescPlaceholders(it)
            )
        }
    }

    private fun checkActionConflict(
        currentTrip: FSTrip,
        blockType: TimelineBlockType,
    ): ExternalConflict? {
        return actionRepository.checkDateConflict(
            tripPrefix = currentTrip.tripPrefix,
            tripCode = currentTrip.tripCode,
            destinationSeq = (blockType as? TimelineBlockType.ON_SITE)?.destinationSeq,
            newStart = blockType.startDate,
            newEnd = blockType.endDate,
            validateStartDateEquals = blockType.isBlock
        )?.let {
            ExternalConflict.Action(
                data = it,
                blockType = blockType,
                translate = TimelineBlockTranslate.ERROR_CONFLICT_WITH_FORM_LBL,
                placeholders = createDescPlaceholders(it, "form_desc")
            )
        }
    }

    private fun findTimelineBlock(
        startDate: String,
        endDate: String?,
        timeline: List<TripTimelineBlock>,
        blockToIgnore: TripTimelineBlock?
    ): FindBlockResult = findTimelineBlockUseCase(
        FindTimelineBlockUseCase.Input(
            startDate = startDate,
            endDate = endDate,
            timeline = timeline,
            blockToIgnore = blockToIgnore
        )
    )

    private fun List<TripTimelineBlock>.findOnSiteBlock(destinationSeq: Int): TripTimelineBlock? =
        find { it.type is TimelineBlockType.ON_SITE && it.type.destinationSeq == destinationSeq }

    private fun isConflictingOnSiteBlock(
        foundBlock: TripTimelineBlock,
        blockToIgnore: TripTimelineBlock?
    ): Boolean = foundBlock.type is TimelineBlockType.ON_SITE &&
            (blockToIgnore?.type is TimelineBlockType.ON_SITE ||
                    blockToIgnore?.type is TimelineBlockType.PRE_TRIP)

    private fun isFormOutsideOnSite(
        foundBlock: TripTimelineBlock,
        blockToIgnore: TripTimelineBlock?
    ): Boolean {
        if (blockToIgnore?.type !is TimelineBlockType.FORM) return false

        return when (val foundType = foundBlock.type) {
            !is TimelineBlockType.ON_SITE -> true
            else -> {
                val formBlock = blockToIgnore.type as TimelineBlockType.FORM
                foundType.destinationSeq != formBlock.destinationSeq
            }
        }
    }

    private fun isIgnorableRangeOverlap(
        blockType: TimelineBlockType,
        conflict: EventConflict
    ): Boolean = (blockType is TimelineBlockType.PRE_TRIP ||
            blockType is TimelineBlockType.RETURN_TRIP ||
            blockType is TimelineBlockType.ON_SITE) &&
            conflict.type == EventConflictType.RANGE_OVERLAP

    private fun dateToMs(date: String?): Long? {
        return date?.let {
            dateCache.getOrPut(it) { ToolBox_Inf.dateToMilliseconds(it) }
        }
    }

    private fun formatDateTime(date: String): String =
        appContext.formatDate(FormatDateType.DateAndHour(date))

    private fun formatDateRange(endDate: String?): String =
        endDate?.takeIf { it.isNotEmpty() }?.let { "- ${formatDateTime(it)}" } ?: ""


    private fun createConflict(
        block: TripTimelineBlock?,
        message: TranslateWildCard,
        placeholders: Map<String, String> = emptyMap()
    ): ValidationResult = ValidationResult.Conflict(
        conflictingBlock = block,
        message = message
    ).addPlaceholders(placeholders)

    private fun createDestinationConflict(
        block: TripTimelineBlock,
        message: TranslateWildCard
    ): ValidationResult = createConflict(
        block = block,
        message = message,
        placeholders = mapOf(
            "destination_desc" to (block.description ?: ""),
            "arrived_date" to formatDateTime(block.type.startDate),
            "departed_date" to formatDateRange(block.type.endDate)
        )
    )

    private fun createTripCreateConflict(
        block: TripTimelineBlock?,
        tripCreateDate: String,
        tripStartDate: String?
    ): ValidationResult {
        val placeholders = if (tripStartDate != null) {
            mapOf(
                "trip_create_date" to formatDateTime(tripCreateDate),
                "trip_start_date" to formatDateTime(tripStartDate)
            )
        } else {
            mapOf("trip_create_date" to formatDateTime(tripCreateDate))
        }

        val message = if (tripStartDate == null) {
            TimelineBlockTranslate.ERROR_CONFLICT_WITH_DATE_CREATE_TRIP_LBL
        } else {
            TimelineBlockTranslate.ERROR_CONFLICT_WITH_CREATE_TRIP_LBL
        }

        return createConflict(block, message, placeholders)
    }

    private fun createEventConflict(
        block: TripTimelineBlock,
        currentTrip: FSTrip?
    ): ValidationResult {
        return when (block.type) {
            is TimelineBlockType.PRE_TRIP -> createConflict(
                block = block,
                message = TimelineBlockTranslate.ERROR_CONFLICT_WITH_CREATE_TRIP_LBL,
                placeholders = mapOf(
                    "trip_create_date" to formatDateTime(block.type.startDate),
                    "trip_start_date" to formatDateTime(currentTrip?.startDate ?: "")
                )
            )

            else -> createConflict(
                block = block,
                message = TimelineBlockTranslate.ERROR_CONFLICT_WITH_EVENT,
                placeholders = mapOf(
                    "event_desc" to (block.description ?: ""),
                    "date_start" to formatDateTime(block.type.startDate),
                    "date_end" to formatDateRange(block.type.endDate)
                )
            )
        }
    }

    private fun createDescPlaceholders(
        conflict: EventConflict,
        descKey: String = "event_desc"
    ): Map<String, String> = mapOf(
        descKey to (conflict.description ?: ""),
        "date_start" to formatDateTime(conflict.dateStart!!),
        "date_end" to formatDateRange(conflict.dateEnd)
    )
}