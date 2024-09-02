package com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.origin

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.namoadigital.prj001.R
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
import com.namoadigital.prj001.model.trip.TripOrigin
import com.namoadigital.prj001.ui.act005.trip.fragment.base.BaseTripDialog
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.PhotoUpdate
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.SaveOriginEdit
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.event.DialogEventTrip.Companion.DIALOG_EVENT_RETRY_IMAGE_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.component.util.OpenCamera
import com.namoadigital.prj001.ui.base.BaseDialog
import com.namoadigital.prj001.util.TextWatcherHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EditOriginDialog constructor(
    private val context: Context,
    private val trip: FSTrip,
    private val validateOriginDate: (Long, Int, Int) -> String?,
    getDestinationThresholds: (Long, Int, Int, Int?, GetDestinationForThresholdValidationUseCase.TripDestinationValidationType) -> Pair<FsTripDestination?, FsTripDestination?>,
    private val onSave: (SaveOriginEdit) -> Unit,
    private val onOpenCamera: OpenCamera,
) : BaseTripDialog<TripDialogInfoEditBinding>(trip, getDestinationThresholds) {


    private val isRequiredFleetData = trip.isRequiredFleetData
    private val hmAuxTranslate by lazy {
        TranslateInfoDialogs.loadTranslation(context)
    }

    init {
        getOdometerPhotoName()
        setupDialog()
    }

    private fun setupDialog() {
        dialog = BaseDialog.Builder(
            context,
            contentView = TripDialogInfoEditBinding.inflate(LayoutInflater.from(context))
        ).content { _, binding ->
            with(binding) {
                this@EditOriginDialog.binding = this;
                initializeViews()
                observerPhoto()
                initializeListeners()
            }
        }.build()
    }


    private fun TripDialogInfoEditBinding.initializeViews() {
        tvTitle.text = hmAuxTranslate[TranslateInfoDialogs.DIALOG_ORIGIN_TITLE_LBL]
        tvDateStart.text = hmAuxTranslate[TranslateInfoDialogs.DIALOG_DATE_START_LBL]
        tvPointStart.text = hmAuxTranslate[TranslateInfoDialogs.DIALOG_ORIGIN_START_LBL]
        etLayoutStartDate.hint = hmAuxTranslate[TranslateInfoDialogs.DIALOG_DATE_HINT]
        etLayoutStartHour.hint = hmAuxTranslate[TranslateInfoDialogs.DIALOG_HOUR_HINT]
        tvFleetInfo.text = hmAuxTranslate[TranslateInfoDialogs.DIALOG_FLEET_INFO_LBL]
        edittextFleetPlateLayout.hint = hmAuxTranslate[TranslateInfoDialogs.DIALOG_FLEET_PLATE_HINT]
        edittextOdometerLayout.hint = hmAuxTranslate[TranslateInfoDialogs.DIALOG_ODOMETER_HINT]
        if (isRequiredFleetData) {
            setRequiredOdometerPhotoButton()
        } else {
            setOptionalOdometerPhotoButton()
        }
        btnSave.text = hmAuxTranslate[TranslateInfoDialogs.DIALOG_SAVE_BTN]
        tvFleetplateInvalid.text = hmAuxTranslate[TranslateInfoDialogs.DIALOG_ERROR_FLEET_PLATE_LBL]
        tvOdometerInvalid.text = hmAuxTranslate[TranslateInfoDialogs.DIALOG_ERROR_ODOMETER_LBL]

        tvName.text =
            if (TripOrigin.valueOf(trip.originType!!) == TripOrigin.GPS) hmAuxTranslate[TranslateInfoDialogs.DIALOG_TYPE_GPS_LBL]
            else trip.originSiteDesc

        val (date, hour) = trip.originDate!!.parseDatePair()
        etStartDate.setText(date)
        etStartHour.setText(hour)

        etOdometer.setText("${trip.fleetStartOdometer ?: ""}")
        etFleetPlate.setText(trip.fleetLicencePlate ?: "")


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
                    photoLoading.visibility = View.GONE
                    buttonOdometerPhoto.visibility = View.VISIBLE
                    buttonRetryDownloadImage.visibility = View.GONE
                },
                failed = {
                    photoLoading.visibility = View.GONE
                    buttonOdometerPhoto.visibility = View.GONE
                    buttonRetryDownloadImage.apply {
                        visibility = View.VISIBLE
                        text = hmAuxTranslate[DIALOG_EVENT_RETRY_IMAGE_LBL]
                        setOnClickListener {
                            getOdometerPhotoName()
                        }
                    }
                }
            )
        }.launchIn(CoroutineScope(Dispatchers.Main + SupervisorJob()))

    }

    private fun TripDialogInfoEditBinding.initializeListeners() {

        btnClose.setOnClickListener {
            dismiss()
        }

        ivPhoto.setOnClickListener {
            onOpenCamera(
                trip.tripPrefix.hashCode(),
                tempPathName
            )
        }

        etStartDate.setDelegatePickerChange {
            updateButtonState()
        }

        etStartHour.setDelegatePickerChange {
            updateButtonState()
        }


        if (isRequiredFleetData) {
            edittextFleetPlateLayout.configureToRequiredInput(
                context,
                hmAuxTranslate[TranslateInfoDialogs.DIALOG_FLEET_PLATE_HINT] ?: "",
                etFleetPlate
            )
            edittextOdometerLayout.configureToRequiredInput(
                context,
                hmAuxTranslate[TranslateInfoDialogs.DIALOG_ODOMETER_HINT] ?: "",
                etOdometer
            )
        }

        etFleetPlate.addTextChangedListener(
            onTextChanged = { text, _, _, _ ->
                isValidPlate(text.toString())
            }
        )

        etOdometer.addTextChangedListener(TextWatcherHelper(object :
            TextWatcherHelper.TextChangedListener {
            override fun onTextChanged(text: String) {
                validateOdometer(text)
            }
        }))

        buttonOdometerPhoto.setOnClickListener {
            onOpenCamera(
                trip.tripStatus.hashCode(),
                tempPathName
            )
        }


        btnSave.setOnClickListener {
            save()
        }

    }


    private fun TripDialogInfoEditBinding.isValidPlate(plateText: String): Boolean {
        if (!isRequiredFleetData) {
            edittextFleetPlateLayout.setBoxStrokeColorState(context, R.drawable.edittext_theme)
            edittextFleetPlateLayout.setHintTextColor(context, R.drawable.edittext_theme)
            layoutFleetplateInvalid.visibility = View.GONE
            updateButtonState()
            return true
        }

        plateText.ifEmpty {
            edittextFleetPlateLayout.setBoxStrokeColorState(context, R.color.m3_namoa_error)
            edittextFleetPlateLayout.setHintTextColor(context, R.color.m3_namoa_error)
            layoutFleetplateInvalid.visibility = View.VISIBLE
            updateButtonState()
            return false
        }
        layoutFleetplateInvalid.visibility = View.GONE
        updateButtonState()
        return true
    }

    fun TripDialogInfoEditBinding.validateOdometer(odometer: String): Boolean {
        if (odometer.isNotBlank()) {
            val inputOdometerValid = isInputOdometerValid(
                null,
                odometer.toLong(),
                hmAuxTranslate,
                GetDestinationForThresholdValidationUseCase.TripDestinationValidationType.ODOMETER_NEXT
            )

            if (inputOdometerValid.isNotBlank()) {
                setOdometerErrorLayout(
                    context,
                    edittextOdometerLayout,
                    layoutOdometerInvalid,
                    tvOdometerInvalid,
                    inputOdometerValid,
                    View.VISIBLE
                )
                edittextOdometerLayout.setBoxStrokeColorState(context, R.color.m3_namoa_error)
                edittextOdometerLayout.setHintTextColor(context, R.color.m3_namoa_error)
                updateButtonState()
                return false
            }

            if (!trip.isRequireDestinationFleetData && buttonOdometerPhoto.isVisible) {
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
            edittextOdometerLayout.setBoxStrokeColorState(context, R.drawable.edittext_theme)
            edittextOdometerLayout.setHintTextColor(context, R.drawable.edittext_theme)
            updateButtonState()
            return true
        } else {
            if (!isRequiredFleetData && ivPhoto.isVisible) {
                setOdometerErrorLayout(
                    context,
                    edittextOdometerLayout,
                    layoutOdometerInvalid,
                    tvOdometerInvalid,
                    hmAuxTranslate[TranslateInfoDialogs.DIALOG_ERROR_ODOMETER_LBL]!!,
                    View.VISIBLE
                )
                edittextOdometerLayout.setBoxStrokeColorState(context, R.color.m3_namoa_error)
                edittextOdometerLayout.setHintTextColor(context, R.color.m3_namoa_error)
                updateButtonState()
                return false
            } else if (!isRequiredFleetData && buttonOdometerPhoto.isVisible) {
                setOptionalOdometerPhotoButton()
                edittextOdometerLayout.hint = getOdometerPhotoLabel(
                    false,
                    hmAuxTranslate[TranslateInfoDialogs.DIALOG_ODOMETER_HINT]!!
                )
            } else if (isRequiredFleetData) {
                setOdometerErrorLayout(
                    context,
                    edittextOdometerLayout,
                    layoutOdometerInvalid,
                    tvOdometerInvalid,
                    hmAuxTranslate[TranslateInfoDialogs.DIALOG_ERROR_ODOMETER_LBL]!!,
                    View.VISIBLE
                )
                edittextOdometerLayout.setBoxStrokeColorState(context, R.color.m3_namoa_error)
                edittextOdometerLayout.setHintTextColor(context, R.color.m3_namoa_error)
                updateButtonState()
                return false
            }

            setOdometerErrorLayout(
                context,
                edittextOdometerLayout,
                layoutOdometerInvalid,
                tvOdometerInvalid,
                "",
                View.GONE
            )
            edittextOdometerLayout.setBoxStrokeColorState(context, R.drawable.edittext_theme)
            edittextOdometerLayout.setHintTextColor(context, R.drawable.edittext_theme)
            updateButtonState()
            return true
        }
    }

    private fun updateButtonState() {
        with(binding) {
            btnSave.isEnabled =
                isValidStartDate() && isValidFleet() && isValidOdometer() && checkFormState()
        }
    }

    private fun isValidStartDate(): Boolean {
        binding.apply {
            val dateStart = binding.etStartDate.text.toString()
            val hourStart = binding.etStartHour.text.toString()
            validateOriginDate.let { invoke ->
                val dateError = invoke(
                    trip.customerCode,
                    trip.tripPrefix,
                    trip.tripCode,
                )
                //
                if (!checkNextDate(dateError, dateStart, hourStart)) {
                    return false
                }
                //
                if (dateIsFuture("$dateStart $hourStart")) {
                    setStartDateError()
                    tvDateStartInvalid.text =
                        hmAuxTranslate[TranslateInfoDialogs.DIALOG_ERROR_FUTURE_DATE]
                    return false
                }

                resetStartDate()
            }
        }
        return true
    }

    private fun TripDialogInfoEditBinding.checkNextDate(
        nextStartDate: String?,
        dateStart: String,
        hourStart: String
    ): Boolean {

        nextStartDate?.let {
            //
            if (isDateBefore(nextStartDate.parseDate(), "$dateStart $hourStart")) {
                setStartDateError()
                tvDateStartInvalid.text =
                    "${hmAuxTranslate[TranslateInfoDialogs.DIALOG_VALUE_SHOULD_BE_LOWER_THAN_DATE_LBL]} ${nextStartDate.parseDate()}"
                return false
            }
            //
        }
        return true
    }

    private fun TripDialogInfoEditBinding.resetStartDate() {
        etLayoutStartDate.setBoxStrokeColorState(context, R.drawable.edittext_theme)
        etLayoutStartHour.setBoxStrokeColorState(context, R.drawable.edittext_theme)
        etLayoutStartDate.setHintTextColor(context, R.drawable.edittext_theme)
        etLayoutStartHour.setHintTextColor(context, R.drawable.edittext_theme)
        layoutDateStartInvalid.visibility = View.GONE
    }

    fun TripDialogInfoEditBinding.setStartDateError() {
        etLayoutStartDate.setBoxStrokeColorState(context, R.drawable.edittext_error)
        etLayoutStartHour.setBoxStrokeColorState(context, R.drawable.edittext_error)
        etLayoutStartDate.setHintTextColor(context, R.drawable.edittext_error)
        etLayoutStartHour.setHintTextColor(context, R.drawable.edittext_error)
        layoutDateStartInvalid.visibility = View.VISIBLE
        btnSave.isEnabled = false
    }

    private fun isValidFleet(): Boolean {
        with(binding) {
            val fleetPlate = etFleetPlate.text.toString()

            return when {
                (!trip.fleetLicencePlate.isNullOrEmpty() && isRequiredFleetData && fleetPlate.isEmpty()) -> false
                (isRequiredFleetData && fleetPlate.isEmpty()) -> false
                else -> true
            }
        }
    }

    private fun isValidOdometer(): Boolean {
        with(binding) {
            val odometer = etOdometer.text.toString()

            return when {
                (trip.fleetStartOdometer != null && isRequiredFleetData && (odometer.isEmpty() || buttonOdometerPhoto.isVisible)) -> false
                (isRequiredFleetData && (odometer.isEmpty() || buttonOdometerPhoto.isVisible)) -> false
                layoutOdometerInvalid.isVisible -> false
                else -> true
            }
        }
    }

    private fun checkFormState(): Boolean {
        with(binding) {
            val (savedDate, saveHour) = trip.originDate?.parseDatePair() ?: Pair("", "")
            val isEqualsLastDate = savedDate == etStartDate.text.toString()
            val isEqualsLastHour = saveHour == etStartHour.text.toString()
            val isEqualsPlate = trip.fleetLicencePlate?.let {
                it == etFleetPlate.text.toString()
            } ?: etFleetPlate.text.toString().isBlank()
            val odometerValue =
                if (etOdometer.text.toString().isEmpty()) null else etOdometer.text.toString()
                    .toLong()
            val isEqualsOdometer = trip.fleetStartOdometer == odometerValue

            return ((odometerValue != null && ivPhoto.isVisible) || (odometerValue == null && buttonOdometerPhoto.isVisible))
                    && (!(isEqualsLastDate && isEqualsLastHour && isEqualsPlate && isEqualsOdometer)
                    || (isRequiredFleetData && isNewPhoto))

        }
    }

    private fun TripDialogInfoEditBinding.updatePhotoView(
        bitmap: Bitmap?,
        isUpdatePhoto: Boolean = false
    ) {
        ivPhoto.apply {
            if (bitmap == null) {
                visibility = View.GONE
                if(isUpdatePhoto && !trip.isRequireDestinationFleetData && etOdometer.text.isNullOrEmpty()){
                    edittextOdometerLayout.hint = getOdometerPhotoLabel(
                        false,
                        hmAuxTranslate[TranslateInfoDialogs.DIALOG_ODOMETER_HINT]!!
                    )
                }
            } else {
                visibility = View.VISIBLE
                setImageBitmap(bitmap)
                if(isUpdatePhoto && !trip.isRequireDestinationFleetData){
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


    private fun save() {
        with(binding) {
            val (date, hour) = trip.originDate!!.parseDatePair()
            val isEqualsDate = etStartDate.text.toString() == date
            val isEqualsHour = etStartHour.text.toString() == hour
            val isEqualsFleet = etFleetPlate.text.toString() == trip.fleetLicencePlate
            val isEqualsOdometer = etOdometer.text.toString() == trip.fleetStartOdometer.toString()


            val saveType = when {
                (!isEqualsDate || !isEqualsHour) && (!isEqualsFleet || (!isEqualsOdometer || isNewPhoto)) -> {
                    if (isNewPhoto) saveImage()
                    SaveOriginEdit.ALL(
                        date = "${etStartDate.text} ${etStartHour.text}".parseFullDate(),
                        fleet = etFleetPlate.text.toString(),
                        odometer = etOdometer.text.toString(),
                        photoUpdate = PhotoUpdate(
                            originPath.name,
                            isNewPhotoInt(),
                            buttonOdometerPhoto.isVisible
                        )
                    )
                }

                (isEqualsDate || isEqualsHour) && (!isEqualsFleet || (!isEqualsOdometer || isNewPhoto)) -> {
                    if (isNewPhoto) saveImage()
                    SaveOriginEdit.FLEET(
                        fleet = etFleetPlate.text.toString(),
                        odometer = etOdometer.text.toString(),
                        photoUpdate = PhotoUpdate(
                            originPath.name,
                            isNewPhotoInt(),
                            buttonOdometerPhoto.isVisible
                        )
                    )
                }

                !isEqualsDate || !isEqualsHour -> {
                    SaveOriginEdit.ORIGIN(
                        date = "${etStartDate.text} ${etStartHour.text}".parseFullDate()
                    )
                }

                else -> SaveOriginEdit.NOTHING
            }

            onSave(saveType)
        }
    }

    fun updatePhotoDialog() {
        binding.updatePhotoView(updatePhoto(), true)
    }


    private fun getOdometerPhotoName() {

        val (prefix, code) = trip.prefixAndCode
        val newPathFile = generateNewFilePath(context, prefix, code)
        val startImage = trip.photoFleetStart

        photoPathLoading()

        handlePhoto(context, startImage, newPathFile)
    }


    override fun show() {
        dialog.show()
    }

    override fun dismiss() {
        dialog.dismiss()
    }

    override fun errorSendData() {

    }

}


