package com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.start_trip

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.namoadigital.prj001.R
import com.namoadigital.prj001.core.translate.textOf
import com.namoadigital.prj001.core.trip.domain.model.blockchain.ValidationResult
import com.namoadigital.prj001.core.trip.domain.usecase.destination.GetDestinationForThresholdValidationUseCase
import com.namoadigital.prj001.databinding.TripDialogEditStartBinding
import com.namoadigital.prj001.extensions.parseDate
import com.namoadigital.prj001.extensions.parseDatePair
import com.namoadigital.prj001.extensions.parseFullDate
import com.namoadigital.prj001.extensions.setBoxStrokeColorState
import com.namoadigital.prj001.extensions.setHintTextColor
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.ui.act005.trip.fragment.base.BaseTripDialog
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs
import com.namoadigital.prj001.ui.base.BaseDialog

class EditStartTripDialog(
    private val context: Context,
    private val trip: FSTrip,
    private val validateStartDate: (String) -> ValidationResult,
    getDestinationThresholds: (Long, Int, Int, Int?, GetDestinationForThresholdValidationUseCase.TripDestinationValidationType) -> Pair<FsTripDestination?, FsTripDestination?>,
    private val onSave: (String) -> Unit,
) : BaseTripDialog<TripDialogEditStartBinding>(trip, getDestinationThresholds) {


    private val isRequiredFleetData = trip.isRequiredFleetData
    private val hmAuxTranslate by lazy {
        TranslateInfoDialogs.loadTranslation(context)
    }

    init {
        setupDialog()
    }

    private fun setupDialog() {
        dialog = BaseDialog.Builder(
            context,
            contentView = TripDialogEditStartBinding.inflate(LayoutInflater.from(context))
        ).content { _, binding ->
            with(binding) {
                this@EditStartTripDialog.binding = this
                initializeViews()
                initializeListeners()
            }
        }.build()
    }


    private fun TripDialogEditStartBinding.initializeViews() {
        tvTitle.text = hmAuxTranslate[TranslateInfoDialogs.DIALOG_START_TRIP_TITLE_LBL]
        tvDateStart.text = hmAuxTranslate[TranslateInfoDialogs.DIALOG_DATE_START_LBL]
        etLayoutStartDate.hint = hmAuxTranslate[TranslateInfoDialogs.DIALOG_DATE_HINT]
        etLayoutStartHour.hint = hmAuxTranslate[TranslateInfoDialogs.DIALOG_HOUR_HINT]

        btnSave.text = hmAuxTranslate[TranslateInfoDialogs.DIALOG_SAVE_BTN]

        val (date, hour) = trip.startDate!!.parseDatePair()
        etStartDate.setText(date)
        etStartHour.setText(hour)


    }

    private fun TripDialogEditStartBinding.initializeListeners() {

        btnClose.setOnClickListener {
            dismiss()
        }

        etStartDate.setDelegatePickerChange {
            updateButtonState()
        }

        etStartHour.setDelegatePickerChange {
            updateButtonState()
        }


        btnSave.setOnClickListener {
            save()
        }

    }

    private fun updateButtonState() {
        with(binding) {
            btnSave.isEnabled =
                isValidStartDate() && checkFormState()
        }
    }


    private fun isValidStartDate(): Boolean {
        val dateStart = binding.etStartDate.text.toString()
        val hourStart = binding.etStartHour.text.toString()
        val fullDate = "$dateStart $hourStart".parseFullDate()

        // Validar timeline conflicts
        when (val validate = validateStartDate(fullDate)) {
            is ValidationResult.Conflict -> {
                binding.setStartDateError()
                binding.tvDateStartInvalid.text = hmAuxTranslate.textOf(
                    key = validate.message,
                    values = validate.message.placeholders.map { placeholder ->
                        validate.parameters[placeholder] ?: ""
                    }
                )
                return false
            }
            is ValidationResult.Success -> {
                binding.resetStartDate()
            }
        }

        // Validar se é data futura
        if (dateIsFuture(fullDate)) {
            binding.setStartDateError()
            binding.tvDateStartInvalid.text =
                hmAuxTranslate[TranslateInfoDialogs.DIALOG_ERROR_FUTURE_DATE]
            return false
        }

        // Tudo válido
        binding.resetStartDate()
        return true
    }

    private fun TripDialogEditStartBinding.checkNextDate(
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

    private fun TripDialogEditStartBinding.checkBeforeDate(
        nextStartDate: String?,
        dateStart: String,
        hourStart: String,
    ): Boolean {

        nextStartDate?.let {
            //
            if (dateBeforeTrip("$dateStart $hourStart")) {
                setStartDateError()
                tvDateStartInvalid.text =
                    "${hmAuxTranslate[TranslateInfoDialogs.DIALOG_VALUE_SHOULD_BE_HIGHER_THAN_DATE_LBL]} ${nextStartDate.parseDate()}"
                return false
            }
            //
        }
        return true
    }

    private fun TripDialogEditStartBinding.resetStartDate() {
        etLayoutStartDate.setBoxStrokeColorState(context, R.drawable.edittext_theme)
        etLayoutStartHour.setBoxStrokeColorState(context, R.drawable.edittext_theme)
        etLayoutStartDate.setHintTextColor(context, R.drawable.edittext_theme)
        etLayoutStartHour.setHintTextColor(context, R.drawable.edittext_theme)
        layoutDateStartInvalid.visibility = View.GONE
    }

    fun TripDialogEditStartBinding.setStartDateError() {
        etLayoutStartDate.setBoxStrokeColorState(context, R.drawable.edittext_error)
        etLayoutStartHour.setBoxStrokeColorState(context, R.drawable.edittext_error)
        etLayoutStartDate.setHintTextColor(context, R.drawable.edittext_error)
        etLayoutStartHour.setHintTextColor(context, R.drawable.edittext_error)
        layoutDateStartInvalid.visibility = View.VISIBLE
        btnSave.isEnabled = false
    }

    private fun checkFormState(): Boolean {
        with(binding) {
            val (savedDate, saveHour) = trip.startDate?.parseDatePair() ?: Pair("", "")
            val isEqualsLastDate = savedDate == etStartDate.text.toString()
            val isEqualsLastHour = saveHour == etStartHour.text.toString()

            return (!(isEqualsLastDate && isEqualsLastHour))

        }
    }


    private fun save() {
        with(binding) {
            val (date, hour) = trip.originDate!!.parseDatePair()
            etStartDate.text.toString() == date
            etStartHour.text.toString() == hour

            onSave("${etStartDate.text} ${etStartHour.text}".parseFullDate())
        }
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


