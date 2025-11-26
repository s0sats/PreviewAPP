package com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.adapter.trip.model.ExtractType
import com.namoadigital.prj001.core.translate.textOf
import com.namoadigital.prj001.core.trip.domain.usecase.destination.GetDestinationForThresholdValidationUseCase
import com.namoadigital.prj001.databinding.TripDialogFleetBinding
import com.namoadigital.prj001.extensions.configureToRequiredInput
import com.namoadigital.prj001.extensions.date.FormatDateType
import com.namoadigital.prj001.extensions.date.formatDate
import com.namoadigital.prj001.extensions.date.getCurrentDateApi
import com.namoadigital.prj001.extensions.parseDatePair
import com.namoadigital.prj001.extensions.parseFullDate
import com.namoadigital.prj001.extensions.setBoxStrokeColorState
import com.namoadigital.prj001.extensions.setHintTextColor
import com.namoadigital.prj001.extensions.watchStatus
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.TripStatus
import com.namoadigital.prj001.model.trip.TripTarget
import com.namoadigital.prj001.model.trip.toTripStatus
import com.namoadigital.prj001.ui.act005.trip.fragment.base.BaseTripDialog
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.CANCEL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.FLEET_DIALOG_DESTINATION_TITLE
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.FLEET_DIALOG_END_DESC
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.FLEET_DIALOG_END_TITLE
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.FLEET_DIALOG_ODOMETER
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.FLEET_DIALOG_ORIGIN_TITLE
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.FLEET_DIALOG_PHOTO
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.FLEET_DIALOG_PLATE
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.FLEET_EDITTEXT_ERROR
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ODOMETER_EDITTEXT_ERROR
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.SAVE
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.SAVE_END_TRIP_BTN
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.PhotoUpdate
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs.DIALOG_DATE_END_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs.DIALOG_DATE_HINT
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs.DIALOG_DATE_START_EXCEEDED_TRIP_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs.DIALOG_ERROR_DATE_END_TRIP_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs.DIALOG_ERROR_FUTURE_DATE
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs.DIALOG_HOUR_HINT
import com.namoadigital.prj001.ui.act005.trip.fragment.component.util.OpenCamera
import com.namoadigital.prj001.ui.base.BaseDialog
import com.namoadigital.prj001.util.TextWatcherHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class FleetDialog(
    private val context: Context,
    private val hmAuxTranslate: HMAux,
    private val trip: FSTrip,
    private val destination: FsTripDestination?,
    private val target: TripTarget,
    private val onSave: (endDate: String, fleetPlate: String, odometer: Long?, photoUpdate: PhotoUpdate) -> Unit,
    private val validateDate: (String) -> Pair<String, ExtractType>?,
    private val onOpenCamera: OpenCamera,
    getDestinationThresholds: (Long, Int, Int, Int, GetDestinationForThresholdValidationUseCase.TripDestinationValidationType) -> Pair<FsTripDestination?, FsTripDestination?>,
) : BaseTripDialog<TripDialogFleetBinding>(trip, getDestinationThresholds) {

    private val isRequiredFields = trip.isRequiredFleetData

    init {
        getFleetPhotoName(target)
        dialog = BaseDialog.Builder(
            context = context,
            contentView = TripDialogFleetBinding.inflate(LayoutInflater.from(context))
        ).content { _, binding ->
            this.binding = binding
            with(binding) {
                initializeViews()

                statePhotoPath.onEach { path ->
                    path?.watchStatus(
                        success = { pathString ->
                            binding.photoLoading.visibility = View.GONE
                            binding.updatePhotoViews(getPhoto(), true)
                        },
                        loading = { _, _ ->
                            with(binding) {
                                photoLoading.visibility = View.VISIBLE
                                buttonOdometerPhoto.visibility = View.GONE
                                buttonRetryDownloadImage.visibility = View.GONE
                            }
                        },
                        error = { _, _ ->
                            buttonOdometerPhoto.visibility = View.VISIBLE
                            photoLoading.visibility = View.GONE
                            buttonRetryDownloadImage.visibility = View.GONE
                        },
                        failed = {
                            binding.photoLoading.visibility = View.GONE
                            binding.buttonOdometerPhoto.visibility = View.GONE
                            binding.buttonRetryDownloadImage.apply {
                                visibility = View.VISIBLE
                                text =
                                    hmAuxTranslate[TripTranslate.FLEET_PHOTO_DIALOG_ERROR_DOWNLOAD]
                                setOnClickListener {
                                    getFleetPhotoName(target)
                                }
                            }

                        }
                    )

                }.launchIn(CoroutineScope(Dispatchers.Main + SupervisorJob()))

                initializeListeners()

            }
        }.build()
    }

    override fun show() {
        dialog.show()
    }

    override fun dismiss() {
        removeTempPath()
        this.dialog.dismiss()
    }

    override fun errorSendData() {
        changeEnabledSave(true)
    }


    @SuppressLint("ResourceType", "SetTextI18n")
    private fun initializeViews() {
        with(binding) {
            val dialogTtl: String?
            val dialogDesc: String?

            description.visibility = View.GONE

            when (target) {
                TripTarget.START -> {
                    dialogTtl = hmAuxTranslate[FLEET_DIALOG_ORIGIN_TITLE]
                    dialogDesc = null
                }

                TripTarget.DESTINATION -> {
                    dialogTtl = hmAuxTranslate[FLEET_DIALOG_DESTINATION_TITLE]
                    dialogDesc = null
                }

                TripTarget.END -> {
                    dialogTtl = hmAuxTranslate[FLEET_DIALOG_END_TITLE]
                    dialogDesc = hmAuxTranslate[FLEET_DIALOG_END_DESC]
                }
            }
            //
            title.visibility = View.GONE
            description.visibility = View.GONE
            //
            dialogTtl?.let {
                title.visibility = View.VISIBLE
                title.text = it
            }
            //
            dialogDesc?.let {
                description.visibility = View.VISIBLE
                description.text = it
            }
            //
            trip.fleetLicencePlate?.let {
                etFleetPlate.setText(it)
                val color = if (isRequiredFields && it.isEmpty()) {
                    R.color.edit_text_color_required
                } else {
                    R.drawable.edittext_theme
                }

                edittextFleetPlateLayout.setBoxStrokeColorState(context, color)
                edittextFleetPlateLayout.setHintTextColor(context, color)
            }
            //
            if (trip.tripStatus.toTripStatus() == TripStatus.PENDING) {
                val startOdometer = trip.fleetStartOdometer
                //
                etOdometer.setText(startOdometer?.toString())
                etOdometer.text.toString().let {
                    val color = if (isRequiredFields && it.isEmpty()) {
                        R.color.edit_text_color_required
                    } else {
                        R.drawable.edittext_theme
                    }

                    edittextOdometerLayout.setBoxStrokeColorState(context, color)
                    edittextOdometerLayout.setHintTextColor(context, color)
                }
            }
            //
            edittextFleetPlateLayout.apply {
                if (isRequiredFields) {
                    configureToRequiredInput(
                        context,
                        hmAuxTranslate[FLEET_DIALOG_PLATE] ?: "",
                        etFleetPlate
                    )
                } else {
                    hint = hmAuxTranslate[FLEET_DIALOG_PLATE]
                }
                isEnabled = trip.tripStatus.toTripStatus() == TripStatus.PENDING
                visibility = if (target == TripTarget.START) View.VISIBLE else View.GONE
            }
            //
            if (isRequiredFields) {
                setRequiredOdometerPhotoButton()
            } else {
                setOptionalOdometerPhotoButton()
            }
            //
            btnCancel.text = hmAuxTranslate[CANCEL]
            btnSave.apply {
                text = if (target == TripTarget.END) {
                    hmAuxTranslate[SAVE_END_TRIP_BTN]
                } else {
                    hmAuxTranslate[SAVE]
                }
            }
            //
            buttonOdometerPhoto.visibility = View.VISIBLE
            if (checkDataRequired()) {
                setRequiredOdometerPhotoButton()
            } else {
                if (target == TripTarget.END) {
                    btnSave.isEnabled = true
                }
            }
            setEditTextListeners()
        }
        finalizeTripDate()

    }

    private fun TripDialogFleetBinding.setRequiredOdometerPhotoButton() {
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
        buttonOdometerPhoto.text = getOdometerPhotoLabel(true, hmAuxTranslate[FLEET_DIALOG_PHOTO]!!)
        edittextOdometerLayout.configureToRequiredInput(
            context,
            hmAuxTranslate[FLEET_DIALOG_ODOMETER] ?: "",
            etOdometer
        )
    }

    private fun TripDialogFleetBinding.setOptionalOdometerPhotoButton() {
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
        buttonOdometerPhoto.text =
            getOdometerPhotoLabel(false, hmAuxTranslate[FLEET_DIALOG_PHOTO]!!)
        edittextOdometerLayout.apply {
            hint = hmAuxTranslate[FLEET_DIALOG_ODOMETER]
            setBoxStrokeColorState(context, R.drawable.edittext_theme_outlined)
            setHintTextColor(context, R.drawable.edittext_theme)
        }
    }

    private fun checkDataRequired() =
        (((target == TripTarget.START || target == TripTarget.END) && isRequiredFields)
                || (target == TripTarget.DESTINATION && trip.requireDestinationFleetData == 1))

    private fun TripDialogFleetBinding.setEditTextListeners() {
        etOdometer.addTextChangedListener(TextWatcherHelper(object :
            TextWatcherHelper.TextChangedListener {
            override fun onTextChanged(text: String) {
                val color = if (isRequiredFields && text.isEmpty()) {
                    R.color.edit_text_color_required
                } else {
                    R.drawable.edittext_theme
                }

                edittextOdometerLayout.setBoxStrokeColorState(context, color)
                edittextOdometerLayout.setHintTextColor(context, color)


                isOdometerTouched = etOdometer.text.isNullOrEmpty()

                etOdometer.text.toString().let { odometerValue ->

                    when (trip.tripStatus.toTripStatus()) {
                        TripStatus.PENDING -> {
                            updateFieldErrors()
                        }

                        else -> {
                            if (odometerValue.isNotBlank()) {
                                val inputOdometerValidErrorMsg =
                                    isInputOdometerValid(
                                        destination,
                                        odometerValue.toBigInteger().toLong(),
                                        hmAuxTranslate,
                                        GetDestinationForThresholdValidationUseCase.TripDestinationValidationType.PREVIOUS
                                    )

                                //
                                val isVisible: Int = if (inputOdometerValidErrorMsg.isNotBlank()) {
                                    changeEnabledSave(false)
                                    View.VISIBLE
                                } else {
                                    edittextOdometerLayout.setBoxStrokeColorState(
                                        context,
                                        R.drawable.edittext_theme
                                    )
                                    edittextOdometerLayout.setHintTextColor(
                                        context,
                                        R.drawable.edittext_theme
                                    )
                                    updateFieldErrors()
                                    View.GONE
                                }
                                //
                                setOdometerErrorLayout(
                                    context,
                                    edittextOdometerLayout,
                                    layoutOdometerInvalid,
                                    tvOdometerInvalid,
                                    inputOdometerValidErrorMsg,
                                    isVisible
                                )
                                //
                            }
                            updateFieldErrors()
                        }
                    }


                }

            }
        }))

        etFleetPlate.addTextChangedListener(
            onTextChanged = { text, start, before, count ->
                val color = if (isRequiredFields && text?.isEmpty() == true) {
                    R.color.edit_text_color_required
                } else {
                    R.drawable.edittext_theme
                }

                isFleetPlateTouched = etFleetPlate.text.isNullOrEmpty()
                edittextFleetPlateLayout.setBoxStrokeColorState(context, color)
                edittextFleetPlateLayout.setHintTextColor(context, color)
                updateFieldErrors()
            }
        )
    }

    private fun TripDialogFleetBinding.updatePhotoViews(
        bitmap: Bitmap?,
        isFirstPhotoUpdate: Boolean = false
    ) {
        ivPhoto.visibility = if (bitmap == null) View.GONE else View.VISIBLE
        ivPhoto.setImageBitmap(bitmap)

        buttonOdometerPhoto.visibility = if (bitmap == null) View.VISIBLE else View.GONE
        if (!isFirstPhotoUpdate) updateFieldErrors()
    }

    private fun initializeListeners() {
        with(binding) {
            ivPhoto.setOnClickListener {

                onOpenCamera(
                    trip.tripStatus.hashCode(),
                    tempPathName
                )
            }

            btnCancel.setOnClickListener {
                dismiss()
            }

            buttonOdometerPhoto.setOnClickListener {
                onOpenCamera(
                    trip.tripStatus.hashCode(),
                    tempPathName
                )
            }

            btnSave.setOnClickListener {
                clearErrorsAndSave()
            }
        }
    }

    private var isFleetPlateTouched = false
    private var isOdometerTouched = false
    fun updateFieldErrors() {
        if (isRequiredFields) {
            handleRequiredFields()
        } else {
            handleOptionalFields()
        }
    }


    private fun isEqualsFields(): Boolean {
        with(binding) {
            val fleetPlateVisible = edittextFleetPlateLayout.isVisible
            val visiblePhoto = ivPhoto.isVisible
            val isFleetPlateSameAsBefore =
                fleetPlateVisible && (etFleetPlate.text.toString() == (trip.fleetLicencePlate
                    ?: ""))
            val isOdometerSameAsBefore =
                etOdometer.text.toString().toLongOrNull() == trip.fleetStartOdometer
            val isPhotoNotNew = !visiblePhoto || !isNewPhoto

            return isFleetPlateSameAsBefore && isOdometerSameAsBefore && isPhotoNotNew
        }
    }

    private fun handleRequiredFields() {
        with(binding) {
            val isValidFleetPlate = !etFleetPlate.text.isNullOrEmpty()
            val isValidOdometer = !etOdometer.text.isNullOrEmpty()
            val visiblePhoto = ivPhoto.isVisible
            val fleetPlateVisible = edittextFleetPlateLayout.isVisible

            val shouldShowBothErrors = (fleetPlateVisible && !isValidFleetPlate) && !isValidOdometer

            val shouldShowFleetPlateError = fleetPlateVisible && !isValidFleetPlate

            if (shouldShowFleetPlateError && isFleetPlateTouched) {
                showFleetPlateError()
            } else {
                layoutFleetPlateInvalid.visibility = View.GONE
            }

            if (!isValidOdometer && isOdometerTouched) {
                showOdometerError()
            } else {
                layoutOdometerInvalid.visibility = View.GONE
            }
            //
            val odometerRestrictionValid = checkOdometerRestriction()
            //
            when {

                dateEditLayout.isVisible && !isValidEndDate() -> {
                    changeEnabledSave(false)
                }

                isValidOdometer && visiblePhoto.not() -> changeEnabledSave(false)
                visiblePhoto && isValidOdometer.not() -> changeEnabledSave(false)
                fleetPlateVisible.not() && visiblePhoto.not() && isValidOdometer.not() -> changeEnabledSave(
                    false
                )

                shouldShowBothErrors -> showBothErrors()
                isEqualsFields() -> changeEnabledSave(false)
                !shouldShowFleetPlateError && (visiblePhoto.not() || isValidOdometer.not()) -> changeEnabledSave(
                    false
                )

                shouldShowFleetPlateError && (visiblePhoto || isValidOdometer) -> changeEnabledSave(
                    false
                )

                edittextOdometerLayout.isVisible && odometerRestrictionValid.not() -> {
                    setRequiredOdometerPhotoButton()
                    changeEnabledSave(false)
                }


                else -> changeEnabledSave(true)
            }
        }
    }

    private fun TripDialogFleetBinding.checkOdometerRestriction(): Boolean {

        if (!etOdometer.text.isNullOrEmpty()
            && trip.tripStatus.toTripStatus() != TripStatus.PENDING
        ) {

            val inputOdometerValidErrorMsg =
                isInputOdometerValid(
                    destination,
                    etOdometer.text.toString().toBigInteger().toLong(),
                    hmAuxTranslate,
                    GetDestinationForThresholdValidationUseCase.TripDestinationValidationType.PREVIOUS
                )
            val odometerValid = inputOdometerValidErrorMsg.isBlank()
            if (odometerValid.not()) {
                setOdometerErrorLayout(
                    context,
                    edittextOdometerLayout,
                    layoutOdometerInvalid,
                    tvOdometerInvalid,
                    inputOdometerValidErrorMsg,
                    View.VISIBLE
                )
            }
            return odometerValid
        }
        //
        return true
    }


    private fun handleOptionalFields() {
        with(binding) {
            val isValidOdometer = !etOdometer.text.isNullOrEmpty()
            val visiblePhoto = ivPhoto.isVisible

            when {
                isValidOdometer && !visiblePhoto -> {
                    setRequiredOdometerPhotoButton()
                    changeEnabledSave(false)
                }

                visiblePhoto && !isValidOdometer -> {
                    setRequiredOdometerPhotoButton()
                    setOdometerErrorLayout(
                        context,
                        edittextOdometerLayout,
                        layoutOdometerInvalid,
                        tvOdometerInvalid,
                        hmAuxTranslate[ODOMETER_EDITTEXT_ERROR] ?: "",
                        View.VISIBLE
                    )
                    changeEnabledSave(false)
                }

                isEqualsFields() -> {
                    setOdometerErrorLayout(
                        context,
                        edittextOdometerLayout,
                        layoutOdometerInvalid,
                        tvOdometerInvalid,
                        hmAuxTranslate[ODOMETER_EDITTEXT_ERROR] ?: "",
                        View.GONE
                    )
                    setOptionalOdometerPhotoButton()
                    changeEnabledSave(false)
                }

                else -> {
                    setOdometerErrorLayout(
                        context,
                        edittextOdometerLayout,
                        layoutOdometerInvalid,
                        tvOdometerInvalid,
                        hmAuxTranslate[ODOMETER_EDITTEXT_ERROR] ?: "",
                        View.GONE
                    )
                    if (!isRequiredFields && isValidOdometer) {
                        setRequiredOdometerPhotoButton()
                    } else {
                        setOptionalOdometerPhotoButton()
                    }
                    changeEnabledSave(true)
                }
            }
        }
    }

    private fun showBothErrors() {
        with(binding) {
            edittextFleetPlateLayout.setFieldError(
                layout = layoutFleetPlateInvalid,
                textView = tvFleetPlateInvalid,
                errorKey = FLEET_EDITTEXT_ERROR
            )
            edittextOdometerLayout.setFieldError(
                layout = layoutOdometerInvalid,
                textView = tvOdometerInvalid,
                errorKey = ODOMETER_EDITTEXT_ERROR,
            )
            changeEnabledSave(false)
        }
    }

    private fun showFleetPlateError() {
        with(binding) {
            edittextFleetPlateLayout.setFieldError(
                layout = layoutFleetPlateInvalid,
                textView = tvFleetPlateInvalid,
                errorKey = FLEET_EDITTEXT_ERROR
            )
            changeEnabledSave(false)
        }
    }

    private fun showOdometerError() {
        with(binding) {
            binding.edittextOdometerLayout.setFieldError(
                layout = layoutOdometerInvalid,
                textView = tvOdometerInvalid,
                errorKey = ODOMETER_EDITTEXT_ERROR,
            )
            changeEnabledSave(false)
        }
    }

    private fun TextInputLayout.setFieldError(
        layout: LinearLayout,
        textView: TextView,
        errorKey: String
    ) {
        setBoxStrokeColorState(context, R.color.m3_namoa_error)
        setHintTextColor(context, R.color.m3_namoa_error)
        layout.visibility = View.VISIBLE
        textView.text = hmAuxTranslate[errorKey]
    }


    private fun TripDialogFleetBinding.clearErrorsAndSave() {
        layoutOdometerInvalid.visibility = View.GONE
        layoutFleetPlateInvalid.visibility = View.GONE

        val fleetPlate = etFleetPlate.text.toString()
        val odometer = etOdometer.text.toString()

        val date = etEndDate.text.toString()
        val hour = etEndHour.text.toString()
        val fullDate = "$date $hour".parseFullDate(false)


        saveImage()
        onSave(
            fullDate,
            fleetPlate,
            odometer.takeIf { it.isNotEmpty() }?.toLong(),
            PhotoUpdate(
                path = originPath.name,
                isNew = isNewPhotoInt(),
                deletePhoto = !ivPhoto.isVisible
            )
        )
    }


    private fun changeEnabledSave(isEnabled: Boolean) {
        binding.btnSave.isEnabled = isEnabled
    }

    fun updatePhotoDialog() {
        binding.updatePhotoViews(updatePhoto())
    }


    private fun getFleetPhotoName(target: TripTarget) {

        val (prefix, code) = trip.prefixAndCode
        val newPathFile = generateNewFilePath(context, prefix, code)
        val startImage = trip.photoFleetStart
        val endImage = trip.photoEnd

        photoPathLoading()

        when (target) {
            TripTarget.START -> handlePhoto(context, startImage, newPathFile)
            TripTarget.DESTINATION -> {
                val destinationImage = destination!!.arrivedDestinationPhoto
                handlePhoto(context, destinationImage, newPathFile)
            }

            TripTarget.END -> handlePhoto(context, endImage, newPathFile)
        }
    }

    private fun finalizeTripDate() = with(binding) {
        if (target != TripTarget.END) {
            dateEditLayout.isVisible = false
            return@with
        }

        dateEditLayout.isVisible = true

        val currentDateApi = getCurrentDateApi(false)
        val (date, hour) = currentDateApi.parseDatePair()


        etLayoutEndDate.hint = hmAuxTranslate.textOf(DIALOG_DATE_HINT)
        etLayoutEndHour.hint = hmAuxTranslate.textOf(DIALOG_HOUR_HINT)
        tvEndDate.text = hmAuxTranslate.textOf(DIALOG_DATE_END_LBL)


        etEndDate.setText(date)
        etEndHour.setText(hour)

        etEndDate.setDelegatePickerChange {
            handleRequiredFields()
        }

        etEndHour.setDelegatePickerChange {
            handleRequiredFields()
        }
    }

    private fun isValidEndDate(): Boolean = with(binding) {
        val date = etEndDate.text.toString()
        val hour = etEndHour.text.toString()
        val fullDate = "$date $hour"

        val parsedDate = fullDate.parseFullDate(false)

        if (dateIsFuture(fullDate)) {
            showDateEndError(
                hmAuxTranslate.textOf(DIALOG_ERROR_FUTURE_DATE)
            )
            return false
        }

        val validationResult = validateDate(parsedDate)

        if (validationResult == null) {
            clearDateEndError()
            return true
        }

        val (invalidDate, type) = validationResult
        if(type == ExtractType.START_TRIP){
            showDateEndError(
                hmAuxTranslate.textOf(DIALOG_DATE_START_EXCEEDED_TRIP_LBL)
            )
        }else{
            val formattedDate = context.formatDate(FormatDateType.DateAndHour(invalidDate))
            showDateEndError(
                "${hmAuxTranslate.textOf(DIALOG_ERROR_DATE_END_TRIP_LBL)} $formattedDate"
            )
        }
        return false
    }

    private fun showDateEndError(message: String) = with(binding) {
        setStartDateError()
        tvDateEndInvalid.text = message
        layoutDateEndInvalid.isVisible = true
        tvDateEndInvalid.isVisible = true
    }

    private fun clearDateEndError() = with(binding) {
        resetStartDate()
        layoutDateEndInvalid.isVisible = false
        tvDateEndInvalid.isVisible = false
    }

    private fun resetStartDate() = with(binding) {
        etLayoutEndDate.setBoxStrokeColorState(context, R.drawable.edittext_theme)
        etLayoutEndHour.setBoxStrokeColorState(context, R.drawable.edittext_theme)
        etLayoutEndDate.setHintTextColor(context, R.drawable.edittext_theme)
        etLayoutEndHour.setHintTextColor(context, R.drawable.edittext_theme)
    }

    fun setStartDateError() = with(binding) {
        etLayoutEndDate.setBoxStrokeColorState(context, R.drawable.edittext_error)
        etLayoutEndHour.setBoxStrokeColorState(context, R.drawable.edittext_error)
        etLayoutEndDate.setHintTextColor(context, R.drawable.edittext_error)
        etLayoutEndHour.setHintTextColor(context, R.drawable.edittext_error)
    }

}
