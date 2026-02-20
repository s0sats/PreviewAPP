package com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.origin

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.ctls.SearchableSpinner
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.core.translate.textOf
import com.namoadigital.prj001.core.trip.domain.model.blockchain.ValidationResult
import com.namoadigital.prj001.core.trip.domain.model.enums.TimelineBlockTranslate
import com.namoadigital.prj001.databinding.TripOriginDialogBinding
import com.namoadigital.prj001.extensions.date.getCurrentDateApi
import com.namoadigital.prj001.extensions.date.toFormattedDateAndTime
import com.namoadigital.prj001.extensions.date.toFormattedString
import com.namoadigital.prj001.extensions.getResourceCode
import com.namoadigital.prj001.extensions.parseFullDate
import com.namoadigital.prj001.extensions.setBoxStrokeColorState
import com.namoadigital.prj001.extensions.setHintTextColor
import com.namoadigital.prj001.extensions.showMaterialAlert
import com.namoadigital.prj001.model.TranslateResource
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.ui.act005.trip.fragment.base.BaseTripDialog
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripBaseFragment
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.DialogOrigin.PROGRESS_ORIGIN_TRIP_SET_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.DialogOrigin.PROGRESS_ORIGIN_TRIP_SET_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.origin.util.OriginOption
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs
import com.namoadigital.prj001.ui.base.BaseDialog
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import java.text.SimpleDateFormat
import java.util.Locale


class OriginDialog(
    private val context: Context,
    private val trip: FSTrip,
    private val validateOriginDate: (String) -> ValidationResult,
    private val listSites: List<HMAux>,
    private val onSave: (originAux: HMAux, date: String, originOptionSelected: OriginOption) -> Unit
) : BaseTripDialog<TripOriginDialogBinding>(trip) {

    private lateinit var currentDate: String
    private val hmAuxTranslate by lazy {
        loadingTranslate(context)
    }

    init {
        dialog = BaseDialog.Builder(
            context = context,
            contentView = TripOriginDialogBinding.inflate(LayoutInflater.from(context))
        ).content { _, binding ->
            this.binding = binding

            initializeViews()
            initializeListeners()


            when (trip.originType) {
                "SITE" -> selectOption(OriginOption.SITE())
                "GPS" -> selectOption(OriginOption.GPS)
            }

        }.build()
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

    private fun initializeViews() {
        with(binding) {
            title.text = hmAuxTranslate[TripTranslate.DialogOrigin.DIALOG_ORIGIN_TITLE]
            subTitle.text = hmAuxTranslate[TripTranslate.DialogOrigin.DIALOG_ORIGIN_CREATE_TRIP]

            edittextOriginDateLayout.hint =
                hmAuxTranslate[TripTranslate.DialogOrigin.DIALOG_ORIGIN_DATE_HINT]
            edittextOriginHourLayout.hint =
                hmAuxTranslate[TripTranslate.DialogOrigin.DIALOG_ORIGIN_HOUR_HINT]

            currentDate = getCurrentDateApi(true)
            val oldDate = SimpleDateFormat(
                ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT_GMT,
                Locale.getDefault()
            ).parse(
                trip.originDate ?: currentDate
            )
            val newDate = SimpleDateFormat("dd/MM/yy").format(oldDate)
            val newHour = SimpleDateFormat("HH:mm").format(oldDate)

            edittextDate.setText(newDate)
            edittextHour.setText(newHour)



            titleOptionPoint.text =
                hmAuxTranslate[TripTranslate.DialogOrigin.DIALOG_ORIGIN_STARTING_POINT]

            ssRadiobuttonOnSite.apply {
                setmShowBarcode(false)
                setmCanClean(false)
                setmShowLabel(true)
                setmLabel(hmAuxTranslate[TripTranslate.DialogOrigin.DIALOG_ORIGIN_SITE_OPTIONS_OPTION])
                setmOption(listSites as ArrayList<HMAux>)


                if (trip.originType == "SITE") {
                    HMAux().apply {
                        this[SearchableSpinner.DESCRIPTION] = trip.originSiteDesc
                        this[SearchableSpinner.CODE] = trip.originSiteCode.toString()
                        setmValue(this)
                    }
                }
            }

            tvRadiobuttonCurrentLocation.apply {
                text =
                    hmAuxTranslate[TripTranslate.DialogOrigin.DIALOG_ORIGIN_CURRENT_LOCATION_OPTION]
                setOnClickListener {
                    selectOption(OriginOption.GPS)
                }
            }

            btnSave.text = hmAuxTranslate[TripTranslate.SAVE]
            btnCancel.text = hmAuxTranslate[TripTranslate.CANCEL]
            updateStateButtonSave()
        }
    }

    private fun initializeListeners() {
        with(binding) {
            btnCancel.setOnClickListener {
                dismiss()
            }

            radiobuttonSiteSelected.setOnClickListener {
                selectOption(OriginOption.SITE(ssRadiobuttonOnSite.getmValue()[SearchableSpinner.CODE]?.toInt()))
                ssRadiobuttonOnSite.setmRequired(true)
            }

            radiobuttonCurrentLocation.setOnClickListener {
                selectOption(OriginOption.GPS)
                ssRadiobuttonOnSite.setmRequired(false)
            }


            edittextDate.apply {
                setOnReportTextChangeListner(object : MKEditTextNM.IMKEditTextChangeText {
                    override fun reportTextChange(p0: String?) {
                        updateStateButtonSave()
                    }

                    override fun reportTextChange(p0: String?, p1: Boolean) {
                    }

                })

                setDelegatePickerChange {
                    errorFutureDate(dateIsFuture((it + " " + edittextHour.text.toString()).parseFullDate()))
                }
            }

            edittextHour.apply {
                setOnReportTextChangeListner(object : MKEditTextNM.IMKEditTextChangeText {
                    override fun reportTextChange(p0: String?) {
                        updateStateButtonSave()
                    }

                    override fun reportTextChange(p0: String?, p1: Boolean) {
                    }

                })

                setDelegatePickerChange {
                    errorFutureDate(dateIsFuture(((edittextDate.text.toString() + " " + it)).parseFullDate()))

                }
            }

            ssRadiobuttonOnSite.apply {
                setOnItemSelectedListener(object :
                    SearchableSpinner.OnItemSelectedListener {
                    override fun onItemPreSelected(item: HMAux) {
                        selectOption(OriginOption.SITE(item[SearchableSpinner.CODE]?.toInt()))
                        updateStateButtonSave()
                    }

                    override fun onItemPostSelected(p0: HMAux?) {

                    }

                })
            }

            btnSave.setOnClickListener {
                saveOrigin()
            }
        }
    }

    private fun errorFutureDate(isFuture: Boolean) {
        with(binding) {
            if (isFuture) {
                layoutErrorDate.visibility = View.VISIBLE
                layoutErrorHour.visibility = View.VISIBLE
                tvDateError.text =
                    hmAuxTranslate[TripTranslate.DialogOrigin.DIALOG_ORIGIN_ERROR_FUTURE_DATE]
                tvHourError.text =
                    hmAuxTranslate[TripTranslate.DialogOrigin.DIALOG_ORIGIN_ERROR_FUTURE_DATE]
                edittextOriginDateLayout.apply {
                    setHintTextColor(context, R.drawable.edittext_error)
                    setBoxStrokeColorState(context, R.drawable.edittext_error)
                }
                edittextOriginHourLayout.apply {
                    setHintTextColor(context, R.drawable.edittext_error)
                    setBoxStrokeColorState(context, R.drawable.edittext_error)
                }
                btnSave.isEnabled = false
            } else {
                layoutErrorDate.visibility = View.GONE
                layoutErrorHour.visibility = View.GONE
                edittextOriginDateLayout.apply {
                    setHintTextColor(context, R.drawable.edittext_theme)
                    setBoxStrokeColorState(context, R.drawable.edittext_theme)
                }
                edittextOriginHourLayout.apply {
                    setHintTextColor(context, R.drawable.edittext_theme)
                    setBoxStrokeColorState(context, R.drawable.edittext_theme)
                }
                updateStateButtonSave()
            }
        }
    }


    @SuppressLint("SimpleDateFormat")
    private fun saveOrigin() {
        with(binding) {
            val formatDate = edittextDate.text.toString() + " " + edittextHour.text.toString()
            val simpleDate = SimpleDateFormat("dd/MM/yy HH:mm")
            val currentSimpleDate = SimpleDateFormat(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT_GMT)
            try {
                val oldDate = simpleDate.parse(formatDate)
                val newDate = currentSimpleDate.format(oldDate!!)
                btnSave.isEnabled = false
                onSave(
                    hmAuxTranslate,
                    newDate,
                    if (radiobuttonCurrentLocation.isChecked) {
                        OriginOption.GPS
                    } else {
                        val siteCode =
                            ssRadiobuttonOnSite.getmValue()[SearchableSpinner.CODE]?.toInt()
                        val siteDesc =
                            ssRadiobuttonOnSite.getmValue()[SearchableSpinner.DESCRIPTION]
                        OriginOption.SITE(siteCode, siteDesc)
                    }
                )
            } catch (e: Exception) {
                ToolBox_Inf.registerException(this::class.java.name, e)
                context.showMaterialAlert(
                    hmAuxTranslate[TripTranslate.DialogOrigin.DIALOG_ORIGIN_ERROR_SAVE_TTL] ?: "",
                    hmAuxTranslate[TripTranslate.DialogOrigin.DIALOG_ORIGIN_ERROR_SAVE_MSG] ?: "",
                    actionPositiveLbl = hmAuxTranslate[TripTranslate.TRIP_RETRY_AGAIN],
                    actionPositive = { dialog, _ ->
                        btnSave.isEnabled = true
                    }
                )
            }


        }
    }

    private fun updateStateButtonSave() {
        with(binding) {
            val (originDate, originType, originSiteCode) = Triple(
                trip.originDate ?: "",
                trip.originType ?: "",
                trip.originSiteCode ?: ""
            )
            var editTextDate = ""
            if (originDate.isNotEmpty()) {
                val formattedDate =
                    "${edittextDate.text} ${edittextHour.text}".toFormattedDateAndTime()
                editTextDate = formattedDate.toFormattedString()
            }
            val dateText = edittextDate.text.toString()
            val hourText = edittextHour.text.toString()
            val siteSelectedCode = siteSelected(SearchableSpinner.CODE).ifEmpty {
                "-1"
            }.toInt()
            val siteSelectedText = siteSelected(SearchableSpinner.DESCRIPTION)
            val optionSite = radiobuttonSiteSelected.isChecked
            val optionGps = radiobuttonCurrentLocation.isChecked
            val checkOriginDate = checkOriginDate(dateText, hourText)


            btnSave.isEnabled = (dateText.isNotEmpty() && hourText.isNotEmpty()
                    && dateHasChange(editTextDate, originDate)
                    && checkOriginDate)
                    || ((optionSite && siteSelectedText.isNotEmpty() && originSiteCode != siteSelectedCode) ||
                    (optionGps && originType != "GPS")
                    )
        }
    }


    fun checkOriginDate(dateStart: String, hourStart: String): Boolean {
        val fullDate = "$dateStart $hourStart".parseFullDate()

        return when (val validate = validateOriginDate(fullDate)) {
            is ValidationResult.Conflict -> {
                binding.apply {
                    layoutErrorDate.visibility = View.VISIBLE
                    layoutErrorHour.visibility = View.VISIBLE
                    tvDateError.text = hmAuxTranslate.textOf(
                        key = validate.message,
                        values = validate.message.placeholders.map { placeholder ->
                            validate.parameters[placeholder] ?: ""
                        }
                    )
                    tvHourError.text = tvDateError.text
                    edittextOriginDateLayout.apply {
                        setHintTextColor(context, R.drawable.edittext_error)
                        setBoxStrokeColorState(context, R.drawable.edittext_error)
                    }
                    edittextOriginHourLayout.apply {
                        setHintTextColor(context, R.drawable.edittext_error)
                        setBoxStrokeColorState(context, R.drawable.edittext_error)
                    }
                }
                false
            }

            is ValidationResult.Success -> {
                binding.apply {
                    layoutErrorDate.visibility = View.GONE
                    layoutErrorHour.visibility = View.GONE
                    edittextOriginDateLayout.apply {
                        setHintTextColor(context, R.drawable.edittext_theme)
                        setBoxStrokeColorState(context, R.drawable.edittext_theme)
                    }
                    edittextOriginHourLayout.apply {
                        setHintTextColor(context, R.drawable.edittext_theme)
                        setBoxStrokeColorState(context, R.drawable.edittext_theme)
                    }
                }
                true
            }
        }
    }

    private fun dateHasChange(editTextDate: String, originDate: String): Boolean {
        return ToolBox_Inf.dateToMilliseconds(editTextDate) != ToolBox_Inf.dateToMilliseconds(
            originDate
        )
    }

    private fun siteSelected(getHmAux: String): String {
        with(binding) {
            return ssRadiobuttonOnSite.getmValue()[getHmAux] ?: ""
        }
    }

    private fun selectOption(originOption: OriginOption) {
        with(binding) {
            when (originOption) {
                is OriginOption.SITE -> {
                    radiobuttonSiteSelected.isChecked = true
                    radiobuttonCurrentLocation.isChecked = false
                }

                is OriginOption.GPS -> {
                    radiobuttonSiteSelected.isChecked = false
                    radiobuttonCurrentLocation.isChecked = true
                }

                else -> {}
            }
            updateStateButtonSave()
        }
    }


    companion object TRANSLATE {
        private fun loadingTranslate(context: Context): HMAux {
            listOf(
                TripTranslate.DialogOrigin.DIALOG_ORIGIN_TITLE,
                TripTranslate.DialogOrigin.DIALOG_ORIGIN_CREATE_TRIP,
                TripTranslate.DialogOrigin.DIALOG_ORIGIN_DATE_HINT,
                TripTranslate.DialogOrigin.DIALOG_ORIGIN_DATE_HINT,
                TripTranslate.DialogOrigin.DIALOG_ORIGIN_HOUR_HINT,
                TripTranslate.DialogOrigin.DIALOG_ORIGIN_STARTING_POINT,
                TripTranslate.DialogOrigin.DIALOG_ORIGIN_SITE_OPTIONS_OPTION,
                TripTranslate.DialogOrigin.DIALOG_ORIGIN_CURRENT_LOCATION_OPTION,
                TripTranslate.DialogOrigin.DIALOG_ORIGIN_ERROR_FUTURE_DATE,
                PROGRESS_ORIGIN_TRIP_SET_TTL,
                PROGRESS_ORIGIN_TRIP_SET_MSG,
                TripTranslate.SAVE,
                TripTranslate.CANCEL,
                TranslateInfoDialogs.DIALOG_VALUE_SHOULD_BE_LOWER_THAN_DATE_LBL,
                TimelineBlockTranslate.ERROR_CONFLICT_WITH_EVENT.key,
                TimelineBlockTranslate.ERROR_CONFLICT_WITH_FORM_LBL.key,
            ).let { list ->
                return TranslateResource(
                    context,
                    TripBaseFragment.MODULE_CODE,
                    context.getResourceCode("origin_dialog")
                ).setLanguage(list)
            }
        }
    }

}

