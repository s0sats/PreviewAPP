package com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.destination

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.namoadigital.prj001.R
import com.namoadigital.prj001.core.translate.textOf
import com.namoadigital.prj001.core.trip.domain.model.blockchain.ValidationResult
import com.namoadigital.prj001.core.trip.domain.usecase.destination.GetDestinationForThresholdValidationUseCase
import com.namoadigital.prj001.databinding.TripDialogInfoEditBinding
import com.namoadigital.prj001.extensions.configureToRequiredInput
import com.namoadigital.prj001.extensions.parseDate
import com.namoadigital.prj001.extensions.parseDatePair
import com.namoadigital.prj001.extensions.parseFullDate
import com.namoadigital.prj001.extensions.setBoxStrokeColorState
import com.namoadigital.prj001.extensions.setHintTextColor
import com.namoadigital.prj001.extensions.watchStatus
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.FsTripDestination.Companion.OVER_NIGHT_DESTINATION_TYPE
import com.namoadigital.prj001.ui.act005.trip.fragment.base.BaseTripDialog
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.PhotoUpdate
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.SaveDestinationEdit
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs.DIALOG_DATE_END_EXCEEDED_START_DATE_DESTINATION_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs.DIALOG_DATE_START_EXCEEDED_END_DATE_DESTINATION_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs.DIALOG_ERROR_FUTURE_DATE
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs.DIALOG_RETRY_IMAGE_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.component.util.OpenCamera
import com.namoadigital.prj001.ui.base.BaseDialog
import com.namoadigital.prj001.util.TextWatcherHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DestinationDialog(
    private val context: Context,
    private val trip: FSTrip,
    private val destination: FsTripDestination,
    private val onSave: (SaveDestinationEdit) -> Unit,
    private val validateDateFromDestination: (destinationSeq: Int?, startDate: String, endDate: String?) -> ValidationResult?,
    getDestinationThresholds: (Long, Int, Int, Int?, GetDestinationForThresholdValidationUseCase.TripDestinationValidationType) -> Pair<FsTripDestination?, FsTripDestination?>,
    private val onOpenCamera: OpenCamera
) : BaseTripDialog<TripDialogInfoEditBinding>(trip, getDestinationThresholds) {


    private val hmAuxTranslate by lazy {
        TranslateInfoDialogs.loadTranslation(context)
    }


    init {
        if (destination.destinationType != OVER_NIGHT_DESTINATION_TYPE) {
            getOdometerPhotoName()
        }
        setupDialog()
    }

    private fun setupDialog() {
        dialog = BaseDialog.Builder(
            context = context,
            contentView = TripDialogInfoEditBinding.inflate(LayoutInflater.from(context))
        ).content { _, binding ->
            this.binding = binding

            with(binding) {
                initializeViews()
                if (destination.destinationType != OVER_NIGHT_DESTINATION_TYPE) {
                    observerPhoto()
                }
                initializeListener()
            }

        }.build()
    }

    private fun TripDialogInfoEditBinding.initializeViews() {
        tvTitle.text = hmAuxTranslate[TranslateInfoDialogs.DIALOG_DESTINATION_TITLE_LBL]
        tvDateStart.text = hmAuxTranslate[TranslateInfoDialogs.DIALOG_DATE_ARRIVAL_LBL]
        tvDateEnd.text = hmAuxTranslate[TranslateInfoDialogs.DIALOG_DATE_DEPARTED_LBL]
        tvPointStart.text = hmAuxTranslate[TranslateInfoDialogs.DIALOG_LOCAL_LBL]
        etLayoutStartDate.hint = hmAuxTranslate[TranslateInfoDialogs.DIALOG_DATE_HINT]
        etLayoutStartHour.hint = hmAuxTranslate[TranslateInfoDialogs.DIALOG_HOUR_HINT]
        etLayoutEndDate.hint = hmAuxTranslate[TranslateInfoDialogs.DIALOG_DATE_HINT]
        etLayoutEndHour.hint = hmAuxTranslate[TranslateInfoDialogs.DIALOG_HOUR_HINT]
        tvFleetInfo.text = hmAuxTranslate[TranslateInfoDialogs.DIALOG_FLEET_INFO_LBL]
        edittextOdometerLayout.hint = hmAuxTranslate[TranslateInfoDialogs.DIALOG_ODOMETER_HINT]
        if (trip.isRequireDestinationFleetData) {
            setRequiredOdometerPhotoButton()
        } else {
            setOptionalOdometerPhotoButton()
        }
        btnSave.text = hmAuxTranslate[TranslateInfoDialogs.DIALOG_SAVE_BTN]
        tvOdometerInvalid.text = hmAuxTranslate[TranslateInfoDialogs.DIALOG_ERROR_ODOMETER_LBL]


        if (trip.isRequireDestinationFleetData) {
            edittextOdometerLayout.configureToRequiredInput(
                context,
                hmAuxTranslate[TranslateInfoDialogs.DIALOG_ODOMETER_HINT]!!,
                etOdometer
            )
        }

        if (destination.destinationType == OVER_NIGHT_DESTINATION_TYPE) {
            tvName.text = hmAuxTranslate[TranslateInfoDialogs.DIALOG_DESTINATION_OVER_NIGHT_LBL]

            buttonOdometerPhoto.visibility = View.GONE
            tvFleetInfo.visibility = View.GONE
            edittextOdometerLayout.visibility = View.GONE
            photoLoading.visibility = View.GONE
        } else {
            tvName.text = destination.destinationSiteDesc
        }

        val (date, hour) = destination.arrivedDate?.parseDatePair() ?: Pair("", "")

        if (date.isNotEmpty()) {
            etStartDate.setText(date)
        }
        if (hour.isNotEmpty()) {
            etStartHour.setText(hour)
        }

        destination.departedDate?.let { departedDate ->
            if (departedDate.isNotEmpty()) {
                val (endDate, endHour) = departedDate.parseDatePair()
                etEndDate.setText(endDate)
                etEndHour.setText(endHour)
                tvDateEnd.visibility = View.VISIBLE
                etLayoutEndDate.visibility = View.VISIBLE
                etLayoutEndHour.visibility = View.VISIBLE
            }
        }

        destination.arrivedFleetOdometer?.let { odometer ->
            etOdometer.setText(odometer.toString())
        }

        edittextFleetPlateLayout.visibility = View.GONE

    }

    private fun TripDialogInfoEditBinding.setRequiredOdometerPhotoButton() {
        buttonOdometerPhoto.iconTint = ResourcesCompat.getColorStateList(
            context.resources,
            R.color.m3_namoa_onPrimary,
            null
        )
        buttonOdometerPhoto.setTextColor(
            ResourcesCompat.getColorStateList(
                context.resources,
                R.color.m3_namoa_onPrimary,
                null
            )
        )
        buttonOdometerPhoto.backgroundTintList = ResourcesCompat.getColorStateList(
            context.resources,
            R.color.m3_namoa_primary,
            null
        )

        buttonOdometerPhoto.text = getOdometerPhotoLabel(
            true,
            hmAuxTranslate[TranslateInfoDialogs.DIALOG_ODOMETER_PHOTO_BTN]!!
        )
    }

    private fun TripDialogInfoEditBinding.setOptionalOdometerPhotoButton() {
        buttonOdometerPhoto.iconTint = ResourcesCompat.getColorStateList(
            context.resources,
            R.color.m3_namoa_primary,
            null
        )
        buttonOdometerPhoto.setTextColor(
            ResourcesCompat.getColorStateList(
                context.resources,
                R.color.m3_namoa_primary,
                null
            )
        )
        buttonOdometerPhoto.backgroundTintList = ResourcesCompat.getColorStateList(
            context.resources,
            android.R.color.transparent,
            null
        )

        buttonOdometerPhoto.text = getOdometerPhotoLabel(
            false,
            hmAuxTranslate[TranslateInfoDialogs.DIALOG_ODOMETER_PHOTO_BTN]!!
        )
    }

    private fun TripDialogInfoEditBinding.initializeListener() {
        btnClose.setOnClickListener {
            dismiss()
        }

        etStartDate.setDelegatePickerChange {
            binding.isValidStartDate()

        }

        etStartHour.setDelegatePickerChange {
            binding.isValidStartDate()
        }

        etEndDate.setDelegatePickerChange {
            binding.isValidEndDate()

        }

        etEndHour.setDelegatePickerChange {
            binding.isValidEndDate()
        }

        etOdometer.addTextChangedListener(TextWatcherHelper(object :
            TextWatcherHelper.TextChangedListener {
            override fun onTextChanged(text: String) {
                etOdometer.text?.let {
                    if (it.toString().isNotBlank()) {
                        if (isOdometerInvalid(it.toString().toLong())) {
                            updateButtonState()
                            return
                        }

                        if (buttonOdometerPhoto.isVisible && !trip.isRequireDestinationFleetData) {
                            setRequiredOdometerPhotoButton()
                            edittextOdometerLayout.hint = getOdometerPhotoLabel(
                                true,
                                hmAuxTranslate[TranslateInfoDialogs.DIALOG_ODOMETER_HINT]!!
                            )
                        }

                        setOdometerErrorLayout(
                            context,
                            edittextOdometerLayout,
                            layoutOdometerInvalid,
                            tvOdometerInvalid,
                            "",
                            View.GONE
                        )
                    } else {
                        if (trip.isRequireDestinationFleetData && etLayoutEndDate.isVisible ||
                            !trip.isRequireDestinationFleetData && ivPhoto.isVisible
                        ) {
                            setOdometerErrorLayout(
                                context,
                                edittextOdometerLayout,
                                layoutOdometerInvalid,
                                tvOdometerInvalid,
                                hmAuxTranslate[TranslateInfoDialogs.DIALOG_ERROR_ODOMETER_LBL]!!,
                                View.VISIBLE
                            )
                            updateButtonState()
                            return
                        } else {
                            if (buttonOdometerPhoto.isVisible && !trip.isRequireDestinationFleetData) {
                                setOptionalOdometerPhotoButton()
                                edittextOdometerLayout.hint = getOdometerPhotoLabel(
                                    false,
                                    hmAuxTranslate[TranslateInfoDialogs.DIALOG_ODOMETER_HINT]!!
                                )
                            }
                            setOdometerErrorLayout(
                                context,
                                edittextOdometerLayout,
                                layoutOdometerInvalid,
                                tvOdometerInvalid,
                                "",
                                View.GONE
                            )
                            updateButtonState()
                        }
                    }
                }

                edittextOdometerLayout.setBoxStrokeColorState(context, R.drawable.edittext_theme)
                edittextOdometerLayout.setBoxStrokeColorState(context, R.drawable.edittext_theme)
                edittextOdometerLayout.setHintTextColor(context, R.drawable.edittext_theme)
                edittextOdometerLayout.setHintTextColor(context, R.drawable.edittext_theme)
                updateButtonState()
            }
        }))

        buttonOdometerPhoto.setOnClickListener {
            onOpenCamera(
                trip.tripStatus.hashCode(),
                tempPathName
            )
        }

        ivPhoto.setOnClickListener {
            onOpenCamera(
                trip.tripStatus.hashCode(),
                tempPathName
            )
        }

        btnSave.setOnClickListener {
            save()
        }
    }

    private fun TripDialogInfoEditBinding.isOdometerInvalid(it: Long): Boolean {
        val inputOdometerValidErrorMsg = isInputOdometerValid(
            destination,
            it,
            hmAuxTranslate,
            GetDestinationForThresholdValidationUseCase.TripDestinationValidationType.ODOMETER_BOTH
        )

        if (inputOdometerValidErrorMsg.isNotBlank()) {
            setOdometerErrorLayout(
                context,
                edittextOdometerLayout,
                layoutOdometerInvalid,
                tvOdometerInvalid,
                inputOdometerValidErrorMsg,
                View.VISIBLE
            )
            return true
        }
        return false
    }


    private fun TripDialogInfoEditBinding.observerPhoto() {

        statePhotoPath.onEach { path ->
            path?.watchStatus(
                success = {
                    photoLoading.visibility = View.GONE
                    buttonOdometerPhoto.visibility = View.GONE
                    updatePhotoView(getPhoto())
                },
                loading = { _, _ ->
                    photoLoading.visibility = View.VISIBLE
                    buttonOdometerPhoto.visibility = View.GONE
                    buttonRetryDownloadImage.visibility = View.GONE
                },
                error = { _, _ ->
                    buttonOdometerPhoto.visibility = View.VISIBLE
                    photoLoading.visibility = View.GONE
                    buttonRetryDownloadImage.visibility = View.GONE
                },
                failed = {
                    photoLoading.visibility = View.GONE
                    buttonOdometerPhoto.visibility = View.GONE
                    buttonRetryDownloadImage.apply {
                        visibility = View.VISIBLE
                        text = hmAuxTranslate[DIALOG_RETRY_IMAGE_LBL]
                        setOnClickListener {
                            getOdometerPhotoName()
                        }
                    }
                }
            )
        }.launchIn(CoroutineScope(Dispatchers.Main + SupervisorJob()))

    }

    private fun updateButtonState() {
        with(binding) {
            val startDate = "${etStartDate.text.toString()} ${etStartHour.text.toString()}"
            val endDate = "${etEndDate.text.toString()} ${etEndHour.text.toString()}"
            val odometer: Long? =
                if (edittextOdometerLayout.isVisible && !etOdometer.text.isNullOrEmpty()) {
                    etOdometer.text.toString().toLong()
                } else {
                    null
                }


            val saveStartDate = destination.arrivedDate?.parseDate() ?: ""
            val saveEndDate = destination.departedDate?.parseDate() ?: ""
            val savedOdometer = destination.arrivedFleetOdometer

            val isEqualsStartDate = startDate.trim() == saveStartDate
            val isEqualsEndDate = endDate.trim() == saveEndDate
            val isEqualsOdometer = odometer == savedOdometer

            val endDateIsVisible = etLayoutEndDate.isVisible && etLayoutEndHour.isVisible

            if (endDateIsVisible) {
                if (edittextOdometerLayout.isVisible && etOdometer.text.isNullOrEmpty() && trip.isRequireDestinationFleetData) {
                    btnSave.isEnabled = false
                    return
                }

                if (trip.isRequireDestinationFleetData && buttonOdometerPhoto.isVisible) {
                    btnSave.isEnabled = false
                    return
                }
            }

            if ((odometer != null && buttonOdometerPhoto.isVisible)
                || (odometer == null && ivPhoto.isVisible)
                || (odometer != null && isOdometerInvalid(odometer))
            ) {
                btnSave.isEnabled = false
                return
            }

            if (!isValidStartDate(false) || endDateIsVisible && !isValidEndDate(false)) {
                btnSave.isEnabled = false
                return
            }

            if (!trip.isRequireDestinationFleetData &&
                ivPhoto.isVisible &&
                etOdometer.text.isNullOrEmpty()
            ) {
                btnSave.isEnabled = false
                return
            }

            btnSave.isEnabled =
                !(isEqualsStartDate && isEqualsEndDate && isEqualsOdometer) || (isNewPhoto && ivPhoto.isVisible)

        }
    }

    private fun TripDialogInfoEditBinding.updatePhotoView(
        bitmap: Bitmap?,
        isUpdatePhoto: Boolean = false
    ) {
        ivPhoto.apply {
            if (bitmap == null) {
                visibility = View.GONE
                if (isUpdatePhoto && !trip.isRequireDestinationFleetData && etOdometer.text.isNullOrEmpty()) {
                    edittextOdometerLayout.hint = getOdometerPhotoLabel(
                        false,
                        hmAuxTranslate[TranslateInfoDialogs.DIALOG_ODOMETER_HINT]!!
                    )
                }
            } else {
                visibility = View.VISIBLE
                setImageBitmap(bitmap)
                if (isUpdatePhoto && !trip.isRequireDestinationFleetData) {
                    edittextOdometerLayout.hint = getOdometerPhotoLabel(
                        true,
                        hmAuxTranslate[TranslateInfoDialogs.DIALOG_ODOMETER_HINT]!!
                    )
                }
            }
        }

        buttonOdometerPhoto.visibility = if (bitmap == null) View.VISIBLE else View.GONE

        if (bitmap == null) {
            if (!trip.isRequireDestinationFleetData && layoutOdometerInvalid.isVisible || etOdometer.text.isNullOrEmpty()) {
                setOdometerErrorLayout(
                    context,
                    edittextOdometerLayout,
                    layoutOdometerInvalid,
                    tvOdometerInvalid,
                    "",
                    View.GONE
                )
                edittextOdometerLayout.setBoxStrokeColorState(context, R.drawable.edittext_theme)
                edittextOdometerLayout.setBoxStrokeColorState(context, R.drawable.edittext_theme)
                edittextOdometerLayout.setHintTextColor(context, R.drawable.edittext_theme)
                edittextOdometerLayout.setHintTextColor(context, R.drawable.edittext_theme)
            } else if (isUpdatePhoto && !etOdometer.text.isNullOrEmpty()) {
                setRequiredOdometerPhotoButton()
            }
        } else if (etOdometer.text.isNullOrEmpty() && (!trip.isRequireDestinationFleetData && isUpdatePhoto && ivPhoto.isVisible)) {
            setOdometerErrorLayout(
                context,
                edittextOdometerLayout,
                layoutOdometerInvalid,
                tvOdometerInvalid,
                hmAuxTranslate[TranslateInfoDialogs.DIALOG_ERROR_ODOMETER_LBL]!!,
                View.VISIBLE
            )
        }
        updateButtonState()
    }

    @SuppressLint("SetTextI18n")
    private fun TripDialogInfoEditBinding.isValidStartDate(stateButtonValid: Boolean = true): Boolean {
        var isSuccess = true
        val dateStart = etStartDate.text.toString()
        val hourStart = etStartHour.text.toString()

        with(binding) {

            if (dateIsFuture("$dateStart $hourStart".parseFullDate())) {
                setStartDateError()
                tvDateStartInvalid.text =
                    hmAuxTranslate[DIALOG_ERROR_FUTURE_DATE]
                btnSave.isEnabled = false
                isSuccess = false
            }
            if (isSuccess) {
                val validateDestination = validateDateFromDestination(
                    destination.destinationSeq,
                    "${etStartDate.text} ${etStartHour.text}".parseFullDate(),
                    if(etLayoutEndDate.isVisible) "${etEndDate.text} ${etEndHour.text}".parseFullDate() else null,
                )


                when(validateDestination){
                    is ValidationResult.Conflict -> {
                        isSuccess = false
                        setStartDateError()
                        tvDateStartInvalid.text = hmAuxTranslate.textOf(
                            key = validateDestination.message,
                            values = validateDestination.parameters.values.toList()
                        )
                    }

                    else -> {
                        setStartDateLayoutSuccess()
                        setEndLayoutSuccess()
                        if (stateButtonValid) updateButtonState()
                    }
                }
            }
            //
            if (etLayoutEndDate.isVisible) {

                compareDates(
                    "$dateStart $hourStart",
                    "${etEndDate.text.toString()} ${etEndHour.text.toString()}"
                ) { startDate, endDate ->
                    startDate.after(endDate) || startDate == endDate
                }.let { isAfter ->
                    if (isAfter) {
                        setStartDateError()
                        tvDateStartInvalid.text =
                            hmAuxTranslate[DIALOG_DATE_START_EXCEEDED_END_DATE_DESTINATION_LBL]

                        isSuccess = false
                    }
                }
            }
            if (isSuccess) {
                setStartDateLayoutSuccess()
            } else {
                return false
            }
        }
        if (stateButtonValid) updateButtonState()
        return true
    }

    private fun TripDialogInfoEditBinding.setStartDateLayoutSuccess() {
        etLayoutStartDate.setBoxStrokeColorState(context, R.drawable.edittext_theme)
        etLayoutStartHour.setBoxStrokeColorState(context, R.drawable.edittext_theme)
        etLayoutStartDate.setHintTextColor(context, R.drawable.edittext_theme)
        etLayoutStartHour.setHintTextColor(context, R.drawable.edittext_theme)
        layoutDateStartInvalid.visibility = View.GONE
    }

    private fun TripDialogInfoEditBinding.setStartDateError() {
        etLayoutStartDate.setBoxStrokeColorState(context, R.drawable.edittext_error)
        etLayoutStartHour.setBoxStrokeColorState(context, R.drawable.edittext_error)
        etLayoutStartDate.setHintTextColor(context, R.drawable.edittext_error)
        etLayoutStartHour.setHintTextColor(context, R.drawable.edittext_error)
        layoutDateStartInvalid.visibility = View.VISIBLE
        btnSave.isEnabled = false
    }


    @SuppressLint("SetTextI18n")
    private fun TripDialogInfoEditBinding.isValidEndDate(stateButtonValid: Boolean = true): Boolean {
        val dateEnd = etEndDate.text.toString()
        val hourEnd = etEndHour.text.toString()

        with(binding) {

            "${etStartDate.text} ${etStartHour.text}".parseFullDate()
            "${etEndDate.text} ${etEndHour.text}".parseFullDate()

            if (dateIsFuture("$dateEnd $hourEnd".parseFullDate())) {
                setEndDateError()
                tvDateEndInvalid.text = hmAuxTranslate[DIALOG_ERROR_FUTURE_DATE]
                return false
            }

            val validateDestination = validateDateFromDestination(
                destination.destinationSeq,
                "${etStartDate.text} ${etStartHour.text}".parseFullDate(),
                if(etEndDate.isVisible) "${etEndDate.text} ${etEndHour.text}".parseFullDate() else null,
            )


            when(validateDestination) {
                is ValidationResult.Conflict -> {
                    setEndDateError()
                    tvDateEndInvalid.text = hmAuxTranslate.textOf(
                        key = validateDestination.message,
                        values = validateDestination.parameters.values.toList()
                    )
                    return false
                }

                else -> {
                    setEndLayoutSuccess()
                    if (stateButtonValid) updateButtonState()
                }
            }

            compareDates(
                "$dateEnd $hourEnd",
                "${etStartDate.text.toString()} ${etStartHour.text.toString()}"
            ) { startDate, endDate ->
                startDate.before(endDate)
            }.let { isBefore ->
                if (isBefore) {
                    setEndDateError()
                    tvDateEndInvalid.text =
                        hmAuxTranslate[DIALOG_DATE_END_EXCEEDED_START_DATE_DESTINATION_LBL]
                    return false
                }
            }

            if (stateButtonValid) updateButtonState()
        }

        return true
    }

    private fun TripDialogInfoEditBinding.setEndLayoutSuccess() {
        etLayoutEndDate.setBoxStrokeColorState(context, R.drawable.edittext_theme)
        etLayoutEndHour.setBoxStrokeColorState(context, R.drawable.edittext_theme)
        etLayoutEndDate.setHintTextColor(context, R.drawable.edittext_theme)
        etLayoutEndHour.setHintTextColor(context, R.drawable.edittext_theme)
        layoutDateEndInvalid.visibility = View.GONE
    }

    private fun TripDialogInfoEditBinding.setEndDateError() {
        etLayoutEndDate.setBoxStrokeColorState(context, R.drawable.edittext_error)
        etLayoutEndHour.setBoxStrokeColorState(context, R.drawable.edittext_error)
        etLayoutEndDate.setHintTextColor(context, R.drawable.edittext_error)
        etLayoutEndHour.setHintTextColor(context, R.drawable.edittext_error)
        layoutDateEndInvalid.visibility = View.VISIBLE
        btnSave.isEnabled = false
    }


    private fun checkSaveWithOdometer(): SaveDestinationEdit {
        with(binding) {
            val startDate = "${etStartDate.text} ${etStartHour.text}".trim()
            val endDate = "${etEndDate.text} ${etEndHour.text}".trim()

            var odometer: Long? = null
            if (edittextOdometerLayout.isVisible && etOdometer.text.toString().isNotEmpty()) {
                odometer = etOdometer.text.toString().toLong()
            }

            val savedStartDate = destination.arrivedDate?.parseDate() ?: ""
            val savedEndDate = destination.departedDate?.parseDate() ?: ""
            val savedOdometer = destination.arrivedFleetOdometer ?: 0L

            val isEqualsStartDate = savedStartDate == startDate
            val isEqualsEndDate =
                (savedEndDate.isNotBlank() && savedEndDate == endDate) || savedEndDate.isBlank()
            val isEqualsOdometer =
                if (etOdometer.text?.isEmpty() == true) savedOdometer == 0L else savedOdometer == etOdometer.text.toString()
                    .toLong()

            val newPhoto = isNewPhoto && ivPhoto.isVisible
            return when {
                //Tudo diferente
                (!isEqualsStartDate || !isEqualsEndDate) && (!isEqualsOdometer || newPhoto) -> {
                    if (newPhoto) saveImage()
                    SaveDestinationEdit.ALL(
                        dateStart = startDate.parseFullDate(),
                        dateEnd = if (endDate.isNotEmpty()) "${etEndDate.text} ${etEndHour.text}".parseFullDate() else "",
                        odometer = odometer,
                        photoUpdate = PhotoUpdate(
                            originPath.name,
                            isNewPhotoInt(),
                            buttonOdometerPhoto.isVisible
                        ),
                        destinationSeq = destination.destinationSeq
                    )
                }

                //Odometro diferente
                (isEqualsStartDate || isEqualsEndDate) && (!isEqualsOdometer || newPhoto) -> {
                    if (newPhoto) saveImage()
                    SaveDestinationEdit.ODOMETER(
                        odometer = odometer,
                        photoUpdate = PhotoUpdate(
                            originPath.name,
                            isNewPhotoInt(),
                            buttonOdometerPhoto.isVisible
                        ),
                        destinationSeq = destination.destinationSeq
                    )
                }
                //Data igual, odometro ou foto diferente
                !isEqualsStartDate || !isEqualsEndDate -> {
                    SaveDestinationEdit.DATE(
                        dateStart = startDate.parseFullDate(),
                        dateEnd = endDate.parseFullDate(),
                        destinationSeq = destination.destinationSeq
                    )
                }

                else -> {
                    SaveDestinationEdit.NOTHING
                }
            }
        }
    }

    private fun checkSaveWithoutOdometer(): SaveDestinationEdit {
        with(binding) {
            val startDate = "${etStartDate.text} ${etStartHour.text}"
            val endDate = "${etEndDate.text} ${etEndHour.text}".trim()

            return SaveDestinationEdit.DATE(
                dateStart = startDate.parseFullDate(),
                dateEnd = if (endDate.isNotEmpty()) endDate.parseFullDate() else "",
                destinationSeq = destination.destinationSeq
            )
        }
    }

    private fun save() {
        with(binding) {
            btnSave.isEnabled = false
            if (edittextOdometerLayout.isVisible) {
                onSave(checkSaveWithOdometer())
            } else {
                onSave(checkSaveWithoutOdometer())
            }
        }
    }

    private fun getOdometerPhotoName() {

        val (prefix, code) = trip.prefixAndCode
        val newPathFile = generateNewFilePath(context, prefix, code)
        val image = destination.arrivedDestinationPhoto

        photoPathLoading()

        handlePhoto(context, image, newPathFile)
    }

    fun updatePhotoDialog() {
        binding.updatePhotoView(updatePhoto(), true)
    }

    override fun show() {
        dialog.show()
    }

    override fun dismiss() {
        dialog.dismiss()
    }

    override fun errorSendData() {
        binding.btnSave.isEnabled = true
    }
}