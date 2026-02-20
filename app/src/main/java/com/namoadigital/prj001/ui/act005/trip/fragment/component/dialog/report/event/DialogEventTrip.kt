package com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.event

import android.content.Context
import android.graphics.Bitmap
import android.text.InputFilter
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.widget.ScrollView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.core.translate.TranslateBuild
import com.namoadigital.prj001.core.translate.textOf
import com.namoadigital.prj001.core.trip.domain.model.blockchain.ValidationResult
import com.namoadigital.prj001.core.trip.domain.model.enums.TimelineBlockTranslate
import com.namoadigital.prj001.databinding.DialogEventTripBinding
import com.namoadigital.prj001.extensions.date.FormatDateType
import com.namoadigital.prj001.extensions.date.formatDate
import com.namoadigital.prj001.extensions.date.getCurrentDateApi
import com.namoadigital.prj001.extensions.getResourceCode
import com.namoadigital.prj001.extensions.parseDate
import com.namoadigital.prj001.extensions.parseDatePair
import com.namoadigital.prj001.extensions.parseFullDate
import com.namoadigital.prj001.extensions.roundOffDecimal
import com.namoadigital.prj001.extensions.setBoxStrokeColorState
import com.namoadigital.prj001.extensions.setHintTextColor
import com.namoadigital.prj001.extensions.watchStatus
import com.namoadigital.prj001.model.TranslateResource
import com.namoadigital.prj001.model.trip.FSEventType
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FSTripEvent
import com.namoadigital.prj001.model.trip.FSTripPhoto
import com.namoadigital.prj001.ui.act005.trip.di.enums.EventStatus
import com.namoadigital.prj001.ui.act005.trip.fragment.base.BaseTripDialog
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs.EXTRACT_DIALOG_INFO_RESOURCE
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.ReportBottomSheet
import com.namoadigital.prj001.ui.act005.trip.fragment.component.util.OpenCamera
import com.namoadigital.prj001.ui.base.BaseDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class DialogEventTrip(
    private val context: Context,
    private val trip: FSTrip,
    private val event: FSTripEvent?,
    private val isExtractFlow: Boolean = false,
    private val eventType: FSEventType,
    private val onOpenCamera: OpenCamera,
    private val onSave: (HMAux, FSSaveEvent) -> Unit,
    private val checkEventIntersectionDate: (startDate: String, endDate: String?, seq: Int?, waiting: Boolean) -> ValidationResult,
) : BaseTripDialog<DialogEventTripBinding>(trip) {


    private val hmAuxTranslate by lazy {
        loadTranslation(context)
    }

    private val isNewEvent = event == null
    private var eventTypeSelected: FSEventType? = null

    init {
        getEventPhotoName()
        setupDialog()
    }


    private fun setupDialog() {
        dialog = BaseDialog.Builder(
            context,
            contentView = DialogEventTripBinding.inflate(LayoutInflater.from(context))
        ).content { _, binding ->
            with(binding) {
                this@DialogEventTrip.binding = this

                initializeLabels()
                initializeListeners()
                if (!eventType.hidePhoto) {
                    observerPhoto()
                } else {
                    photoLoading.visibility = View.GONE
                    btnCamPhoto.visibility = View.GONE
                    buttonRetryDownloadImage.visibility = View.GONE
                    ivPhoto.visibility = View.GONE
                }
            }


        }.build()
    }

    private fun DialogEventTripBinding.initializeLabels() {
        tvTitle.text = eventType.eventTypeDesc
        //
        etLayoutCost.hint = hmAuxTranslate[DIALOG_EVENT_COST_HINT]
        etLayoutComment.hint = hmAuxTranslate[DIALOG_EVENT_COMMENT_HINT]
        btnCamPhoto.text = hmAuxTranslate[DIALOG_EVENT_BTN_PHOTO_LBL]
        etLayoutStartDate.hint = hmAuxTranslate[DIALOG_EVENT_DATE_HINT]
        etLayoutStartHour.hint = hmAuxTranslate[DIALOG_EVENT_HOUR_HINT]
        btnDelete.text = hmAuxTranslate[DIALOG_EVENT_BTN_DELETE]
        btnFinish.text = hmAuxTranslate[DIALOG_EVENT_BTN_FINISH]
        btnFinish.text =
            if (isSaveMode() || isExtractFlow) hmAuxTranslate[DIALOG_EVENT_BTN_SAVE]
            else hmAuxTranslate[DIALOG_EVENT_BTN_FINISH]
        tvStartDate.text = hmAuxTranslate[DIALOG_EVENT_TRIP_START_DATE_LBL]
        tvEndDate.text = hmAuxTranslate[DIALOG_EVENT_TRIP_END_DATE_LBL]
        etLayoutEndDate.hint = hmAuxTranslate[DIALOG_EVENT_DATE_HINT]
        etLayoutEndHour.hint = hmAuxTranslate[DIALOG_EVENT_HOUR_HINT]
        //
        eventType.let { fsEventType ->
            if (!fsEventType.isWaitAllowed) {
                tvStartDate.visibility = View.GONE
            }
            event?.let { event ->
                etCost.setText(event.cost?.let {
                    val formattedValue = if (it % 1 == 0.0) {
                        String.format("%.2f", it)
                    } else {
                        String.format("%.2f", it)
                    }
                    formattedValue.replace(".", ",")
                } ?: "")

                initializeComments(event, eventType)
                initializeCost(event, eventType)
                val (startDate, startHour) = event.eventStart?.parseDatePair() ?: Pair("", "")

                etStartDate.setText(startDate)
                etStartHour.setText(startHour)
                setEndDateSwitchLayout(fsEventType)
                if (eventType.isWaitAllowed
                    && isExtractFlow
                ) {
                    event.eventEnd?.let { date ->
                        val (endDate, endHour) = date.parseDatePair()
                        etEndDate.setText(endDate)
                        etEndHour.setText(endHour)
                    }
                }
                eventSelected(fsEventType)
            } ?: run {
//                stateDisable()
                initializeComments(null, eventType)
                initializeCost(null, eventType)
                setEndDateSwitchLayout(fsEventType)
                etLayoutEndDate.visibility = View.GONE
                etLayoutEndHour.visibility = View.GONE
                btnDelete.visibility = View.INVISIBLE
                tvEndDate.visibility = View.GONE
                eventSelected(eventType, true)
            }
        }

    }

    private fun DialogEventTripBinding.setEndDateSwitchLayout(fsEventType: FSEventType) {
        if (fsEventType.isWaitAllowed && !isExtractFlow) {
            clShowEndDate.visibility = View.VISIBLE
            tvShowEndDateLbl.text = hmAuxTranslate[DIALOG_EVENT_SHOW_END_DATE_OPT]
        } else {
            clShowEndDate.visibility = View.GONE
        }
    }

    private fun DialogEventTripBinding.initializeComments(
        event: FSTripEvent?,
        fsEventType: FSEventType
    ) {
        etLayoutComment.visibility = View.GONE
        if (!fsEventType.hideComment) {
            etLayoutComment.visibility = View.VISIBLE
            updateFieldHintForRequiredStatus(etLayoutComment, fsEventType.isRequiredComment)
            event?.let {
                etComment.setText(event.comment)
            }
            etLayoutComment.setBoxStrokeColorState(context, R.drawable.edittext_theme)
            etLayoutComment.setHintTextColor(context, R.drawable.edittext_theme)
        }
    }

    private fun DialogEventTripBinding.initializeCost(
        event: FSTripEvent?,
        fsEventType: FSEventType
    ) {
        etLayoutCost.visibility = View.GONE
        if (!fsEventType.hideCost) {
            etLayoutCost.visibility = View.VISIBLE
            updateFieldHintForRequiredStatus(etLayoutCost, fsEventType.isRequiredCost)
            event?.let {
                event.cost?.let {
                    val formattedValue = formatDouble(it)
                    formattedValue.replace(".", ",")
                    etCost.setText(formattedValue)
                }
            }
            etLayoutCost.setBoxStrokeColorState(context, R.drawable.edittext_theme)
            etLayoutCost.setHintTextColor(context, R.drawable.edittext_theme)
        }

    }

    private fun formatDouble(it: Double) = if (it % 1 == 0.0) {
        String.format("%.2f", it)
    } else {
        String.format("%.2f", it)
    }

    private fun DialogEventTripBinding.initializeListeners() {
        etCost.filters = arrayOf(object : InputFilter {
            override fun filter(
                source: CharSequence?,
                start: Int,
                end: Int,
                dest: Spanned?,
                dstart: Int,
                dend: Int
            ): CharSequence? {
                val newString = dest.toString().substring(0, dstart) +
                        source?.subSequence(start, end) +
                        dest.toString().substring(dend)

                if (newString.count { it == ',' } > 1) {
                    return ""
                }

                val dotIndex = newString.indexOf(',')
                if (dotIndex != -1 && newString.length - dotIndex > 3) {
                    return ""
                }

                return null
            }
        })

        btnClose.setOnClickListener { dismiss() }

        switchShowEndDate.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId) {
                setEndDateInputLayout(View.VISIBLE)
                btnFinish.text = hmAuxTranslate[DIALOG_EVENT_BTN_FINISH]
                val currentDate = getCurrentDateApi(true)
                val (endDate, endHour) = currentDate.parseDatePair()
                etEndDate.setText(endDate)
                etEndHour.setText(endHour)
                isValidEndDate(false)
                updateStateButtons()
            } else {
                clearInvalidEndDateLayout()
                setEndDateInputLayout(View.GONE)
                btnFinish.text = hmAuxTranslate[DIALOG_EVENT_BTN_SAVE]
                btnFinish.isEnabled = true
                etEndDate.setText("")
                etEndHour.setText("")
                updateFieldColorForRequiredStatus(etLayoutCost, false)
                updateFieldColorForRequiredStatus(etLayoutComment, false)
                updateStateButtons(false)
            }
            scrollView3.invalidate()
        }

        ivPhoto.setOnClickListener {
            onOpenCamera(trip.tripPrefix.hashCode(), tempPathName)
        }

        btnFinish.setOnClickListener {
            if (isExtractFlow) {
                val validEndDate = if (!eventType.isWaitAllowed) {
                    //
                    "${etStartDate.text.toString()} ${etStartHour.text.toString()}"
                } else {
                    "${etEndDate.text.toString()} ${etEndHour.text.toString()}"
                }
                showAlert(
                    title = hmAuxTranslate[DIALOG_EVENT_ALERT_FINISH_TTL],
                    message = hmAuxTranslate[DIALOG_EVENT_ALERT_FINISH_MSG],
                    onClick = {
                        sendEvent(EventStatus.DONE, validEndDate)
                    }
                )
            } else {
                if (isSaveMode()) {
                    showAlert(
                        title = hmAuxTranslate[DIALOG_EVENT_ALERT_SAVE_TTL],
                        message = hmAuxTranslate[DIALOG_EVENT_ALERT_SAVE_MSG],
                        onClick = {
                            sendEvent(EventStatus.WAITING)
                        }
                    )
                    return@setOnClickListener
                } else {
                    val validEndDate = if (!eventType.isWaitAllowed) {
                        "${etStartDate.text.toString()} ${etStartHour.text.toString()}"
                    } else {
                        "${etEndDate.text.toString()} ${etEndHour.text.toString()}"
                    }
                    showAlert(
                        title = hmAuxTranslate[DIALOG_EVENT_ALERT_FINISH_TTL],
                        message = hmAuxTranslate[DIALOG_EVENT_ALERT_FINISH_MSG],
                        onClick = {
                            sendEvent(EventStatus.DONE, validEndDate)
                        }
                    )
                }
            }

        }


        btnDelete.setOnClickListener {
            showAlert(
                title = hmAuxTranslate[DIALOG_EVENT_ALERT_DELETE_TTL],
                message = hmAuxTranslate[DIALOG_EVENT_ALERT_DELETE_MSG],
                onClick = {
                    if (isExtractFlow && event?.isFinalized == true) {
                        sendEvent(
                            status = EventStatus.CANCELLED,
                            dateEnd = "${etEndDate.text.toString()} ${etEndHour.text.toString()}"
                        )
                    } else {
                        sendEvent(status = EventStatus.CANCELLED)
                    }
                }
            )
        }

        btnCamPhoto.setOnClickListener {
            onOpenCamera(
                trip.tripPrefix.hashCode(),
                tempPathName
            )
        }

        etStartDate.setDelegatePickerChange {
            updateStateButtons(checkRequiredRules())
        }

        etStartHour.setDelegatePickerChange {
            updateStateButtons(checkRequiredRules())
        }

        etEndDate.setDelegatePickerChange {
            updateStateButtons(checkRequiredRules())
        }

        etEndHour.setDelegatePickerChange {
            updateStateButtons(checkRequiredRules())
        }

        etCost.addTextChangedListener(
            onTextChanged = { text, _, _, _ ->
                updateStateButtons(checkRequiredRules())
            }
        )

        etComment.addTextChangedListener(
            onTextChanged = { text, _, _, _ ->

                updateStateButtons(checkRequiredRules())
            }
        )

    }

    private fun DialogEventTripBinding.checkRequiredRules() =
        !eventType.isWaitAllowed
                || switchShowEndDate.isChecked
                || isExtractFlow

    private fun showAlert(
        title: String?,
        message: String?,
        onClick: () -> Unit,
    ) {
        ToolBox.alertMSG(
            context,
            title,
            message,
            { _, _ ->
                onClick()
            },
            1
        )
    }

    private fun DialogEventTripBinding.observerPhoto() {
        statePhotoPath.onEach { result ->
            result?.watchStatus(
                success = {
                    photoLoading.visibility = View.GONE
                    btnCamPhoto.visibility = View.GONE
                    buttonRetryDownloadImage.visibility = View.GONE
                    updatePhotoViews(getPhoto())
                },
                loading = { _, _ ->
                    photoLoading.visibility = View.VISIBLE
                    btnCamPhoto.visibility = View.GONE
                    buttonRetryDownloadImage.visibility = View.GONE
                },
                error = { _, _ ->
                    btnCamPhoto.visibility = View.VISIBLE
                    buttonRetryDownloadImage.visibility = View.GONE
                    photoLoading.visibility = View.GONE
                },
                failed = {
                    photoLoading.visibility = View.GONE
                    btnCamPhoto.visibility = View.GONE
                    buttonRetryDownloadImage.apply {
                        visibility = View.VISIBLE
                        text = hmAuxTranslate[DIALOG_EVENT_RETRY_IMAGE_LBL]
                        setOnClickListener {
                            getEventPhotoName()
                        }
                    }
                }
            )
        }.launchIn(CoroutineScope(Dispatchers.Main + SupervisorJob()))
    }

    private var isFirstSelected = false
    private fun DialogEventTripBinding.eventSelected(
        selected: FSEventType?,
        isFirstEvent: Boolean = false
    ) {
        selected?.let { type ->
            //
            if (etCost.text.toString().isNotEmpty() || isFirstEvent) {
                etLayoutCost.setBoxStrokeColorState(context, R.drawable.edittext_theme)
                etLayoutCost.setHintTextColor(context, R.drawable.edittext_theme)
            }
            //
            if (etComment.text.toString().isNotEmpty() || isFirstEvent) {
                etLayoutComment.setBoxStrokeColorState(context, R.drawable.edittext_theme)
                etLayoutComment.setHintTextColor(context, R.drawable.edittext_theme)
            }
            //
            eventTypeSelected = type
            etLayoutCost.isEnabled = true
            etLayoutComment.isEnabled = true
            etLayoutStartDate.isEnabled = true
            etLayoutStartHour.isEnabled = true
            etLayoutEndDate.isEnabled = true
            etLayoutEndHour.isEnabled = true
            //
            if (isExtractFlow && type.isWaitAllowed) {
                setEndDateInputLayout(View.VISIBLE)
            } else {
                setEndDateInputLayout(View.GONE)
            }

            btnFinish.visibility = View.VISIBLE

//            btnSave.visibility = when {
//                !type.isWaitAllowed && !isEditMode -> View.INVISIBLE
//                isEditMode -> View.INVISIBLE
//                else -> View.VISIBLE
//            }
//            btnSave.isEnabled = true

            btnDelete.visibility = when {
                isExtractFlow && event?.isFinalized ?: false -> View.VISIBLE
                !isNewEvent || (isExtractFlow && !type.isWaitAllowed) -> View.VISIBLE
                else -> View.INVISIBLE
            }
            //
            if (!type.hidePhoto) {
                btnCamPhoto.text = when {
                    type.isRequiredPhoto -> hmAuxTranslate[DIALOG_EVENT_BTN_PHOTO_LBL] + " *"
                    else -> hmAuxTranslate[DIALOG_EVENT_BTN_PHOTO_LBL]
                }
                //
                if (type.isRequiredPhoto) {
                    btnCamPhoto.backgroundTintList = ContextCompat.getColorStateList(
                        context,
                        R.color.m3_namoa_primary
                    )
                    btnCamPhoto.iconTint = ContextCompat.getColorStateList(
                        context,
                        com.namoa_digital.namoa_library.R.color.namoa_camera_white
                    )
                    btnCamPhoto.setTextColor(
                        ContextCompat.getColorStateList(
                            context,
                            com.namoa_digital.namoa_library.R.color.namoa_camera_white
                        )
                    )
                }
                //
                btnCamPhoto.backgroundTintList = when {
                    type.isRequiredPhoto -> ContextCompat.getColorStateList(
                        context,
                        R.color.m3_namoa_primary
                    )

                    else -> ContextCompat.getColorStateList(
                        context,
                        android.R.color.transparent
                    )
                }
                btnCamPhoto.isEnabled = true
            }

            updateStateButtons(checkRequiredRules())

            if (isNewEvent) {
                val currentDate = getCurrentDateApi(true)
                val (date, hour) = currentDate.parseDatePair()
                etStartDate.setText(date)
                etStartHour.setText(hour)
            }

            return
        }
        stateDisable()
    }

    private fun DialogEventTripBinding.setEndDateInputLayout(visibility: Int) {
        tvEndDate.visibility = visibility
        etLayoutEndDate.visibility = visibility
        etLayoutEndHour.visibility = visibility
    }

    private fun DialogEventTripBinding.updateStateButtons(checkRequired: Boolean = true) {

        eventTypeSelected?.let { type ->

            val cost = etCost.text.toString()
            val comment = etComment.text.toString()
            val isExistsPhoto = ivPhoto.isVisible
            val endDateExists = etLayoutEndDate.isVisible

            val startValid = isValidStartDate(false)
            val endValid = if (endDateExists) isValidEndDate(false) else true

            if (cost.isBlank() && checkRequired) {
                updateFieldColorForRequiredStatus(etLayoutCost, type.isRequiredCost)
            } else {
                if (cost.isNotBlank()) {
                    updateFieldColorForRequiredStatus(etLayoutCost, false)
                }
            }

            if (comment.isBlank() && checkRequired) {
                updateFieldColorForRequiredStatus(etLayoutComment, type.isRequiredComment)
            } else {
                if (comment.isNotBlank()) {
                    updateFieldColorForRequiredStatus(etLayoutComment, false)
                }
            }

            if (isSaveMode()) {
                btnFinish.isEnabled = when {
                    isNewEvent && isFirstSelected -> true
                    isNewEvent && !startValid -> false
                    !isNewEvent && (!startValid || endDateExists && !endValid) -> false
                    else -> if (!isNewEvent) checkFormState() else true
                }
            } else {
                btnFinish.isEnabled = when {
                    !startValid -> false
                    type.isRequiredCost && cost.isEmpty() -> false
                    type.isRequiredComment && comment.isEmpty() -> false
                    type.isRequiredPhoto && !isExistsPhoto -> false
                    endDateExists && !endValid -> false
                    else -> if (!isNewEvent && isExtractFlow) checkFormState() else true
                }
            }

            scrollView3.postInvalidate()
            return
        }
        scrollView3.postInvalidate()
        btnFinish.isEnabled = false
    }

    private fun DialogEventTripBinding.checkFormState(): Boolean {
        val cost = if (this.etCost.text.isNullOrEmpty()) 0.0
        else this.etCost.text.toString().replace(",", ".")
        val comment = this.etComment.text.toString()

        val startNewDate = (etStartDate.text.toString() + " " + etStartHour.text.toString())

        val dateEnd = event?.eventEnd?.let {
            if (it.isNotBlank()) it.parseDate() else ""
        } ?: ""

        val endNewDate = (etEndDate.text.toString() + " " + etEndHour.text.toString())

        val isEqualsLastEvent = (eventTypeSelected?.eventTypeDesc ?: "") == event?.eventTypeDesc
        val isEqualsCost = cost == (event?.cost?.let {
            val formatDouble = formatDouble(it)
            formatDouble.replace(",", ".")
        } ?: 0.0)
        val isEqualsComment = comment == (event?.comment ?: "")
        val isEqualsStartDate = startNewDate == (event?.eventStart?.parseDate() ?: "")
        val isEqualsEndDate =
            if (eventType.isWaitAllowed) {
                endNewDate == dateEnd
            } else {
                true
            }


        return if (dateEnd.isNotEmpty()) {
            !(isEqualsLastEvent && isEqualsCost && isEqualsComment && isEqualsStartDate && isEqualsEndDate) ||
                    isNewPhoto
        } else {
            !(isEqualsLastEvent && isEqualsCost && isEqualsComment && isEqualsStartDate) || isNewPhoto
        }

    }


    private fun DialogEventTripBinding.stateDisable() {
        btnFinish.visibility = View.VISIBLE
        btnFinish.isEnabled = false
        etStartDate.text?.clear()
        etStartHour.text?.clear()
        btnDelete.visibility = View.INVISIBLE
        btnCamPhoto.isEnabled = false
        btnCamPhoto.text = hmAuxTranslate[DIALOG_EVENT_BTN_PHOTO_LBL]
        etLayoutCost.isEnabled = false
        etLayoutComment.isEnabled = false
        etLayoutStartDate.isEnabled = false
        etLayoutStartHour.isEnabled = false
        etLayoutEndDate.isEnabled = false
        etLayoutEndHour.isEnabled = false
        layoutDateInvalid.visibility = View.GONE
        layoutDateEndInvalid.visibility = View.INVISIBLE
        isFirstSelected = false
        updateFieldForRequiredStatus(etLayoutComment, false)
        updateFieldForRequiredStatus(etLayoutCost, false)
        updateStateButtons()
    }

    private fun updateFieldForRequiredStatus(
        textInputLayout: TextInputLayout,
        isRequired: Boolean
    ) {

        updateFieldColorForRequiredStatus(textInputLayout, isRequired)
        updateFieldHintForRequiredStatus(textInputLayout, isRequired)
    }

    private fun updateFieldHintForRequiredStatus(
        textInputLayout: TextInputLayout,
        isRequired: Boolean
    ) {
        textInputLayout.apply {
            hint = if (isRequired) {
                "$hint *"
            } else {
                hint.toString().replace("*", "")
            }
        }
    }

    private fun updateFieldColorForRequiredStatus(
        textInputLayout: TextInputLayout,
        isRequired: Boolean
    ) {
        val requiredColor = if (isRequired) {
            R.color.edit_text_color_required
        } else {
            R.drawable.edittext_theme
        }

        textInputLayout.apply {
            setBoxStrokeColorState(context, requiredColor)
            setHintTextColor(context, requiredColor)
        }
    }

    private fun DialogEventTripBinding.updatePhotoViews(
        bitmap: Bitmap?,
    ) {
        ivPhoto.apply {
            visibility = if (bitmap == null || eventType.hidePhoto) View.GONE else View.VISIBLE
            setImageBitmap(bitmap)
        }
        btnCamPhoto.visibility =
            if (bitmap == null && !eventType.hidePhoto) View.VISIBLE else View.GONE
        scrollView3.invalidate()
        updateStateButtons(checkRequiredRules())

    }

    private fun getEventPhotoName() {
        val (prefix, code) = trip.prefixAndCode
        val newPathFile = generateNewFilePath(context, prefix, code)

        photoPathLoading()
        handlePhoto(context, event?.getEventPhoto() ?: FSTripPhoto(), newPathFile)
    }


    private fun DialogEventTripBinding.sendEvent(status: EventStatus, dateEnd: String? = null) {
        eventTypeSelected?.let { type ->

            val startDate = (etStartDate.text.toString() + " " + etStartHour.text.toString())

            val sendStartDate =
                if (event?.eventStart != null && event.eventStart?.parseDate() == startDate) {
                    event.eventStart
                } else {
                    startDate.parseFullDate()
                }

            var sendEndDate: String? = null

            dateEnd?.let { dateEnd ->
                val isEqualsDate =
                    event?.eventEnd != null && event.eventEnd?.takeIf { it.isNotEmpty() }
                        ?.parseDate() == dateEnd
                sendEndDate = if (isEqualsDate) event?.eventEnd else dateEnd.parseFullDate()
            }



            saveImage()
            FSSaveEvent(
                type = type,
                cost = when {
                    etCost.text.toString().isEmpty() -> null
                    else -> {
                        roundOffDecimal(etCost.text.toString().replace(",", ".").toDouble(), "#.##")
                    }
                },
                comment = etComment.text.toString(),
                photoPath = when {
                    ivPhoto.visibility == View.VISIBLE -> originPath.name
                    else -> null
                },
                changePhoto = isNewPhoto,
                seq = event?.eventSeq,
                eventStatus = status,
                dateStart = sendStartDate,
                dateEnd = sendEndDate
            ).let {
                onSave(hmAuxTranslate, it)
            }
        }
    }

    private fun DialogEventTripBinding.isValidStartDate(stateButtonValid: Boolean = true): Boolean {
        if (dateIsFuture(getStartDateFormatted().parseFullDate())) {
            etLayoutStartDate.setBoxStrokeColorState(context, R.drawable.edittext_error)
            etLayoutStartHour.setBoxStrokeColorState(context, R.drawable.edittext_error)
            etLayoutStartDate.setHintTextColor(context, R.drawable.edittext_error)
            etLayoutStartHour.setHintTextColor(context, R.drawable.edittext_error)
            tvDateInvalid.text = hmAuxTranslate[DIALOG_EVENT_DATE_FUTURE_ERROR_LBL]
            layoutDateInvalid.visibility = View.VISIBLE
//            scrollDownToError(scrollView3)
            btnFinish.isEnabled = false
            return false
        }
        //
        if (getStartDateFormatted().isNotBlank()) {
            //
            val validateResult =
                checkEventIntersectionDate(
                    getStartDateFormatted().parseFullDate(false),
                    when {
                        etLayoutEndDate.isVisible -> getEndDateFormatted().parseFullDate(false)
                        !eventType.isWaitAllowed ->  getStartDateFormatted().parseFullDate(false)
                        else -> event?.eventEnd?.ifEmpty { null }
                    },
                    event?.eventSeq,
                    eventType.isWaitAllowed
                )

            return when (validateResult) {
                is ValidationResult.Conflict -> {
                    btnFinish.isEnabled = false
                    setStartDateErrorLayout(
                        hmAuxTranslate.textOf(
                            key = validateResult.message,
                            values = validateResult.parameters.values.toList()
                        )
                    )
                    false
                }

                else -> {
                    clearInvalidStartDateLayout()
                    if (stateButtonValid) updateStateButtons(checkRequiredRules())
                    true
                }
            }
        }

        clearInvalidStartDateLayout()
        if (stateButtonValid) updateStateButtons(checkRequiredRules())
        return true
    }

    private fun DialogEventTripBinding.clearInvalidStartDateLayout() {
        updateFieldForRequiredStatus(etLayoutStartDate, false)
        updateFieldForRequiredStatus(etLayoutStartHour, false)
        layoutDateInvalid.visibility = View.GONE
    }

    private fun scrollDownToError(scrollView3: ScrollView) {
        scrollView3.postDelayed({ scrollView3.fullScroll(ScrollView.FOCUS_DOWN) }, 100)
    }

    private fun DialogEventTripBinding.isValidEndDate(stateButtonValid: Boolean = true): Boolean {
        val date = getEndDateFormatted()
        if (etEndDate.text.toString().isEmpty() || etEndHour.text.toString().isEmpty()) {
//            updateStateButtons()
            return false
        }

        if (dateIsFuture(date.parseFullDate())) {
            setEndDateErrorLayout(hmAuxTranslate[DIALOG_EVENT_DATE_FUTURE_ERROR_LBL]!!, true)
            btnFinish.isEnabled = false
            return false
        }
        //
        //
        val validateResult =
            checkEventIntersectionDate(
                getStartDateFormatted().parseFullDate(),
                getEndDateFormatted().parseFullDate(),
                event?.eventSeq,
                eventType.isWaitAllowed
            )

        return when (validateResult) {
            is ValidationResult.Conflict -> {
                setEndDateErrorLayout(
                    hmAuxTranslate.textOf(
                        key = validateResult.message,
                        values = validateResult.parameters.values.toList()
                    ), true
                )
                false
            }

            else -> {
                clearInvalidEndDateLayout()
                if (stateButtonValid) updateStateButtons(checkRequiredRules())
                true
            }
        }
    }

    private fun getEventEndDateErrorMsg(it: FSTripEvent): String {
        if (!it.eventStart.isNullOrBlank()
            && !it.eventEnd.isNullOrBlank()
        ) {
            compareDates(
                context.formatDate(
                    FormatDateType.DateAndHour(it.eventStart ?: "")
                ),
                context.formatDate(
                    FormatDateType.DateAndHour(it.eventEnd ?: "")
                )
            ) { startDate, endDate ->
                startDate == endDate
            }.let { isEqual ->
                if (isEqual) {
                    return ""
                }
            }
            return it.eventEnd?.let { end ->
                " - " + context.formatDate(
                    FormatDateType.DateAndHour(end)
                )
            } ?: ""
        } else {
            return it.eventEnd?.let { end ->
                " - " + context.formatDate(
                    FormatDateType.DateAndHour(end)
                )
            } ?: ""
        }

    }

    private fun DialogEventTripBinding.setStartDateErrorLayout(errorMsg: String) {
        etLayoutStartDate.setBoxStrokeColorState(context, R.drawable.edittext_error)
        etLayoutStartHour.setBoxStrokeColorState(context, R.drawable.edittext_error)
        etLayoutStartDate.setHintTextColor(context, R.drawable.edittext_error)
        etLayoutStartHour.setHintTextColor(context, R.drawable.edittext_error)
        tvDateInvalid.text = errorMsg
        layoutDateInvalid.visibility = View.VISIBLE
    }

    private fun DialogEventTripBinding.getEndDateFormatted() =
        "${etEndDate.text.toString()} ${etEndHour.text.toString()}"


    private fun DialogEventTripBinding.getStartDateFormatted() =
        "${etStartDate.text.toString()} ${etStartHour.text.toString()}"


    private fun DialogEventTripBinding.clearInvalidEndDateLayout() {
        etLayoutEndDate.isErrorEnabled = false
        etLayoutEndHour.isErrorEnabled = false
        layoutDateEndInvalid.visibility = View.INVISIBLE
        etLayoutEndDate.setBoxStrokeColorState(context, R.drawable.edittext_theme)
        etLayoutEndHour.setBoxStrokeColorState(context, R.drawable.edittext_theme)
        etLayoutEndDate.setHintTextColor(context, R.drawable.edittext_theme)
        etLayoutEndHour.setHintTextColor(context, R.drawable.edittext_theme)
    }

    private fun DialogEventTripBinding.setEndDateErrorLayout(
        errorLbl: String,
        isErrorEnabled: Boolean
    ) {
        tvDateEndInvalid.text = errorLbl
        etLayoutEndDate.setBoxStrokeColorState(context, R.drawable.edittext_error)
        etLayoutEndHour.setBoxStrokeColorState(context, R.drawable.edittext_error)
        etLayoutEndDate.setHintTextColor(context, R.drawable.edittext_error)
        etLayoutEndHour.setHintTextColor(context, R.drawable.edittext_error)
        etLayoutEndDate.isErrorEnabled = isErrorEnabled
        etLayoutEndHour.isErrorEnabled = isErrorEnabled
        layoutDateEndInvalid.visibility = View.VISIBLE
        scrollDownToError(scrollView3)
    }

    override fun show() {
        dialog.show()
    }

    override fun dismiss() {
//        if (this::dialogFinish.isInitialized) {
//            dialogFinish.dismiss()
//        }
        removeTempPath()
        dialog.dismiss()
    }

    override fun errorSendData() {
//        if (this::dialogFinish.isInitialized) {
//            dialogFinish.errorSendData()
//            return
//        }
        binding.updateStateButtons()
    }

    fun updatePhotoDialog() {
        binding.updatePhotoViews(updatePhoto())
    }

    fun DialogEventTripBinding.isSaveMode(): Boolean {
        return !isExtractFlow
                && eventType.isWaitAllowed
                && !switchShowEndDate.isChecked
    }

    companion object {
        const val DIALOG_EVENT_TITLE_LBL = "dialog_event_title_lbl"
        const val DIALOG_EVENT_HINT_EVENT_TYPE = "dialog_event_hint_event_type"
        const val DIALOG_EVENT_COST_HINT = "dialog_event_cost_hint"
        const val DIALOG_EVENT_COMMENT_HINT = "dialog_event_comment_hint"
        const val DIALOG_EVENT_BTN_PHOTO_LBL = "dialog_event_btn_photo_lbl"
        const val DIALOG_EVENT_DATE_HINT = "dialog_event_date_hint"
        const val DIALOG_DATE_ERROR_START_EXCEEDED_LBL = "dialog_date_error_start_exceeded_lbl"
        const val DIALOG_EVENT_HOUR_HINT = "dialog_event_hour_hint"
        const val DIALOG_EVENT_RETRY_IMAGE_LBL = "dialog_event_retry_image_lbl"
        const val DIALOG_EVENT_BTN_DELETE = "dialog_event_btn_delete"
        const val DIALOG_EVENT_BTN_SAVE = "dialog_event_btn_save"
        const val DIALOG_EVENT_BTN_FINISH = "dialog_event_btn_finish"
        const val DIALOG_EVENT_DATE_FUTURE_ERROR_LBL = "dialog_event_date_future_error_lbl"
        const val DIALOG_EVENT_TRIP_DATE_START_ERROR_LBL = "dialog_event_trip_date_start_error_lbl"
        const val DIALOG_EVENT_TRIP_DATE_END_ERROR_LBL = "dialog_event_trip_date_end_error_lbl"
        const val DIALOG_EVENT_TRIP_START_DATE_LBL = "dialog_event_trip_start_date_lbl"
        const val DIALOG_EVENT_TRIP_END_DATE_LBL = "dialog_event_trip_end_date_lbl"
        const val PROGRESS_EVENT_TRIP_SEND_TTL = "progress_event_trip_send_ttl"
        const val PROGRESS_EVENT_TRIP_SEND_MSG = "progress_event_trip_send_msg"
        const val PROGRESS_EVENT_TRIP_DELETE_TTL = "progress_event_trip_delete_ttl"
        const val PROGRESS_EVENT_TRIP_DELETE_MSG = "progress_event_trip_delete_msg"
        const val DIALOG_EVENT_ALERT_DELETE_TTL = "dialog_event_alert_delete_ttl"
        const val DIALOG_EVENT_ALERT_DELETE_MSG = "dialog_event_alert_delete_msg"
        const val DIALOG_EVENT_ALERT_SAVE_TTL = "dialog_event_alert_save_ttl"
        const val DIALOG_EVENT_ALERT_SAVE_MSG = "dialog_event_alert_save_msg"
        const val DIALOG_EVENT_ALERT_FINISH_TTL = "dialog_event_alert_finish_ttl"
        const val DIALOG_EVENT_ALERT_FINISH_MSG = "dialog_event_alert_finish_msg"
        const val DIALOG_EVENT_SHOW_END_DATE_OPT = "dialog_event_show_end_date_opt"
        const val DIALOG_EVENT_TRIP_DATE_CONFLICT_ERROR_LBL =
            "dialog_event_trip_date_conflict_error_lbl"
        const val DIALOG_EVENT_DATE_EQUAL_ERROR_LBL = "dialog_event_date_equal_error_lbl"
        fun loadTranslation(context: Context): HMAux = listOf(
            DIALOG_EVENT_TITLE_LBL,
            DIALOG_EVENT_HINT_EVENT_TYPE,
            DIALOG_EVENT_COST_HINT,
            DIALOG_EVENT_COMMENT_HINT,
            DIALOG_EVENT_BTN_PHOTO_LBL,
            DIALOG_EVENT_DATE_HINT,
            DIALOG_EVENT_HOUR_HINT,
            DIALOG_EVENT_RETRY_IMAGE_LBL,
            DIALOG_EVENT_BTN_DELETE,
            DIALOG_EVENT_BTN_SAVE,
            DIALOG_EVENT_BTN_FINISH,
            DIALOG_EVENT_DATE_FUTURE_ERROR_LBL,
            DIALOG_EVENT_TRIP_DATE_START_ERROR_LBL,
            DIALOG_EVENT_TRIP_DATE_END_ERROR_LBL,
            DIALOG_EVENT_TRIP_START_DATE_LBL,
            DIALOG_EVENT_TRIP_END_DATE_LBL,
            DIALOG_DATE_ERROR_START_EXCEEDED_LBL,
            PROGRESS_EVENT_TRIP_SEND_TTL,
            PROGRESS_EVENT_TRIP_SEND_MSG,
            PROGRESS_EVENT_TRIP_DELETE_TTL,
            PROGRESS_EVENT_TRIP_DELETE_MSG,
            DIALOG_EVENT_ALERT_DELETE_TTL,
            DIALOG_EVENT_ALERT_DELETE_MSG,
            DIALOG_EVENT_ALERT_SAVE_TTL,
            DIALOG_EVENT_ALERT_SAVE_MSG,
            DIALOG_EVENT_ALERT_FINISH_TTL,
            DIALOG_EVENT_ALERT_FINISH_MSG,
            DIALOG_EVENT_SHOW_END_DATE_OPT,
            DIALOG_EVENT_TRIP_DATE_CONFLICT_ERROR_LBL,
            DIALOG_EVENT_DATE_EQUAL_ERROR_LBL,
        ).let(
            TranslateResource(
                context = context,
                mResoure_code = context.getResourceCode(ReportBottomSheet.RESOURCE_DIALOG_EVENT_TRIP)
            )::setLanguage
        ).apply {
            val timelineTranslate = TranslateBuild(context)
                .resource(EXTRACT_DIALOG_INFO_RESOURCE)
                .listVarsKeys { TimelineBlockTranslate.entries }
                .build()

            this.putAll(timelineTranslate)
        }
    }


}

data class FSSaveEvent(
    val type: FSEventType,
    val cost: Double? = null,
    val comment: String? = null,
    val photoPath: String? = null,
    val changePhoto: Boolean? = null,
    val seq: Int? = null,
    val eventStatus: EventStatus,
    val dateStart: String? = null,
    val dateEnd: String? = null
)

