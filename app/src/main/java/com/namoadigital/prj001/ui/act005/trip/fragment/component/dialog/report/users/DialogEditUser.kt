package com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.users

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.textfield.TextInputLayout
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.DialogUserEditBinding
import com.namoadigital.prj001.extensions.date.FormatDateType
import com.namoadigital.prj001.extensions.date.convertDateToFullTimeStampGMT
import com.namoadigital.prj001.extensions.date.formatDate
import com.namoadigital.prj001.extensions.getColorStateListId
import com.namoadigital.prj001.extensions.getDrawableId
import com.namoadigital.prj001.extensions.getResourceCode
import com.namoadigital.prj001.extensions.parseDatePair
import com.namoadigital.prj001.extensions.parseFullDate
import com.namoadigital.prj001.extensions.setBoxStrokeColorState
import com.namoadigital.prj001.extensions.setHintTextColor
import com.namoadigital.prj001.extensions.showMaterialAlert
import com.namoadigital.prj001.model.TranslateResource
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.ui.act005.trip.di.enums.UserAction
import com.namoadigital.prj001.ui.act005.trip.di.model.TripUserEdit
import com.namoadigital.prj001.ui.act005.trip.di.usecase.user.OutputParams
import com.namoadigital.prj001.ui.act005.trip.fragment.base.BaseTripDialog
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.ReportBottomSheet
import com.namoadigital.prj001.ui.base.BaseDialog
import com.namoadigital.prj001.util.ToolBox_Inf

class DialogEditUser(
    private val context: Context,
    private val trip: FSTrip,
    private val user: TripUserEdit,
    private val isEditMode: Boolean = false,
    private val onSaveUser: (user: TripUserEdit, action: UserAction, processMessage: Pair<String, String>) -> Unit,
    private val checkUserIntersectionDate: (
        userCode: Int,
        userSeq: Int?,
        startDateMillis: Long,
        endDateMillis: Long?
    ) -> OutputParams,
) : BaseTripDialog<DialogUserEditBinding>(trip, null) {

    private val hmAuxTranslate by lazy {
        loadTranslation(context)
    }

    init {
        dialog =
            BaseDialog.Builder(context, DialogUserEditBinding.inflate(LayoutInflater.from(context)))
                .isCancelable(true)
                .content { _, binding ->
                    this.binding = binding

                    initializeViews()
                    initializeListener()

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
            userName.text = user.userName

            etLayoutStartDate.hint = hmAuxTranslate[DIALOG_EDIT_DATE_HINT]
            etLayoutStartHour.hint = hmAuxTranslate[DIALOG_EDIT_HOUR_HINT]
            etLayoutEndDate.hint = hmAuxTranslate[DIALOG_EDIT_DATE_HINT]
            etLayoutEndHour.hint = hmAuxTranslate[DIALOG_EDIT_HOUR_HINT]
            tvParticipantSince.text = hmAuxTranslate[DIALOG_PARTICIPANT_SINCE_LBL]
            tvPartipantUntil.text = hmAuxTranslate[DIALOG_PARTICIPANT_UNTIL_LBL]

            if (isEditMode) {
                btnClose.visibility = View.VISIBLE
            }

            btnCancel.apply {
                if (isEditMode) {
                    text = hmAuxTranslate[DIALOG_PARTICIPANT_DELETE_LBL]
                    icon = context.getDrawableId(com.namoa_digital.namoa_library.R.drawable.ic_baseline_delete_24)
                    iconTint = context.getColorStateListId(R.color.m3_namoa_primary)
                } else {
                    text = hmAuxTranslate[DIALOG_EDIT_BTN_CANCEL]
                }

            }

            val date = context.formatDate(FormatDateType.OnlyDate(user.dateStart ?: ""))
            etStartDate.apply {
                setText(date)
                setOnReportTextChangeListner(object :
                    MKEditTextNM.IMKEditTextChangeText {
                    override fun reportTextChange(p0: String?) {
                        updateBtnSaveState()
                    }

                    override fun reportTextChange(p0: String?, p1: Boolean) {
                    }

                })
            }

            val hour = context.formatDate(FormatDateType.OnlyHour(user.dateStart ?: ""))
            etStartHour.apply {
                setText(hour)
                setOnReportTextChangeListner(object :
                    MKEditTextNM.IMKEditTextChangeText {
                    override fun reportTextChange(p0: String?) {
                        updateBtnSaveState()
                    }

                    override fun reportTextChange(p0: String?, p1: Boolean) {
                    }

                })
            }


            user.dateEnd?.let {
                if (it.isEmpty()) return@let
                val dateOut = context.formatDate(FormatDateType.OnlyDate(it))
                val hourOut = context.formatDate(FormatDateType.OnlyHour(it))
                etEndDate.setText(dateOut)
                etEndHour.setText(hourOut)
            }

            etEndDate.apply {
                setOnReportTextChangeListner(object :
                    MKEditTextNM.IMKEditTextChangeText {
                    override fun reportTextChange(p0: String?) {
                        updateBtnSaveState()
                    }

                    override fun reportTextChange(p0: String?, p1: Boolean) {
                    }

                })
                showOptionClearText = true
            }
            etEndHour.apply {
                setOnReportTextChangeListner(object :
                    MKEditTextNM.IMKEditTextChangeText {
                    override fun reportTextChange(p0: String?) {
                        updateBtnSaveState()
                    }

                    override fun reportTextChange(p0: String?, p1: Boolean) {
                    }
                })
                showOptionClearText = true
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateBtnSaveState() {
        with(binding) {
            if (!isSaveStateValid()) {
                btnSave.isEnabled = false
                return
            }

            if (!checkDateIsFuture()) return

            val startDateToMilliseconds =
                ToolBox_Inf.dateToMilliseconds(getStartDateFormatted().parseFullDate())

            val endDateToMilliseconds = if (getEndDateFormatted().trim().isNotBlank()) {
                ToolBox_Inf.dateToMilliseconds(getEndDateFormatted().parseFullDate())
            } else {
                null
            }

            checkUserIntersectionDate(
                user.userCode,
                user.userSeq,
                startDateToMilliseconds,
                endDateToMilliseconds
            ).let {

                val textStartError =
                    hmAuxTranslate[DIALOG_USER_DATE_CONFLICT_ERROR_LBL] + " " + context.formatDate(
                        FormatDateType.DateAndHour(it.user?.dateStart ?: "")
                    ) + if (!it.user?.dateEnd.isNullOrEmpty()) {
                        " - " + context.formatDate(
                            if (it.user?.dateStart?.parseDatePair()?.first == it.user?.dateEnd?.parseDatePair()?.first) {
                                FormatDateType.OnlyHour(it.user?.dateEnd ?: "")
                            } else {
                                FormatDateType.DateAndHour(it.user?.dateEnd ?: "")
                            }
                        )
                    } else {
                        ""
                    }

                val textEndError =
                    hmAuxTranslate[DIALOG_USER_DATE_CONFLICT_ERROR_LBL] + " " + context.formatDate(
                        FormatDateType.DateAndHour(it.user?.dateStart ?: "")
                    ) + if (!it.user?.dateEnd.isNullOrEmpty()) {
                        " - " + context.formatDate(
                            if (it.user?.dateStart?.parseDatePair()?.first == it.user?.dateEnd?.parseDatePair()?.first) {
                                FormatDateType.OnlyHour(it.user?.dateEnd ?: "")
                            } else {
                                FormatDateType.DateAndHour(it.user?.dateEnd ?: "")
                            }
                        )
                    } else {
                        ""
                    }

                when {
                    it.startDateError && it.endDateError -> {
                        tvStartDateError.text = textStartError
                        etLayoutStartDate.changeStrokeColor(etLayoutStartHour, true)
                        showError(layoutErrorStartDate)

                        tvEndDateError.text = textEndError
                        etLayoutEndDate.changeStrokeColor(etLayoutEndHour, true)
                        showError(layoutErrorEndDate)
                    }

                    it.startDateError -> {
                        tvStartDateError.text = textStartError
                        etLayoutEndDate.changeStrokeColor(etLayoutEndHour, false)
                        hideError(layoutErrorEndDate)

                        etLayoutStartDate.changeStrokeColor(etLayoutStartHour, true)
                        showError(layoutErrorStartDate)
                    }

                    it.endDateError -> {
                        tvEndDateError.text = textEndError
                        etLayoutStartDate.changeStrokeColor(etLayoutStartHour, false)
                        hideError(layoutErrorStartDate)

                        etLayoutEndDate.changeStrokeColor(etLayoutEndHour, true)
                        showError(layoutErrorEndDate)
                    }

                    else -> {
                        etLayoutStartDate.changeStrokeColor(etLayoutStartHour, false)
                        etLayoutEndDate.changeStrokeColor(etLayoutEndHour, false)
                        hideError(layoutErrorStartDate)
                        hideError(layoutErrorEndDate)
                    }
                }
            }
        }
    }

    private fun DialogUserEditBinding.getStartDateFormatted() =
        "${etStartDate.text.toString()} ${etStartHour.text.toString()}"

    private fun DialogUserEditBinding.getEndDateFormatted() =
        "${etEndDate.text.toString()} ${etEndHour.text.toString()}"


    private fun isSaveStateValid(): Boolean {
        with(binding) {
            val inDateIsValid = etStartDate.text?.isNotEmpty() == true
            val inHourIsValid = etStartHour.text?.isNotEmpty() == true
            val outDateIsValid = etEndDate.text?.isNotEmpty() == true
            val outHourIsValid = etEndHour.text?.isNotEmpty() == true


            val (dateStart, hourStart) = user.dateStart?.parseDatePair() ?: Pair("", "")
            val (dateEnd, hourEnd) = user.dateEnd?.takeIf { it.isNotEmpty() }?.parseDatePair() ?: Pair("", "")
            val dateStartIsEquals =
                "$dateStart $hourStart" == "${etStartDate.text} ${etStartHour.text}"
            val dateEndIsEquals = "$dateEnd $hourEnd" == "${etEndDate.text} ${etEndHour.text}"

            return when {
                (inDateIsValid && inHourIsValid && outDateIsValid && !outHourIsValid) ||
                        (inDateIsValid && inHourIsValid && !outDateIsValid && outHourIsValid) ||
                        !(inDateIsValid && inHourIsValid) -> false

                dateStartIsEquals && dateEndIsEquals -> false


                else -> true
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkDateIsFuture(): Boolean {
        with(binding) {

            val startDate = "${etStartDate.text} ${etStartHour.text}"
            val endDate = "${etEndDate.text} ${etEndHour.text}"

            if (dateBeforeTrip(startDate)) {
                tvStartDateError.text =
                    hmAuxTranslate[DIALOG_DATE_ERROR_START_BEFORE_TRIP_LBL] + ": " + context.formatDate(
                        FormatDateType.DateAndHour(trip.originDate ?: "")
                    )
                etLayoutStartDate.changeStrokeColor(etLayoutStartHour, true)
                showError(layoutErrorStartDate)
                return false
            } else {
                etLayoutStartDate.changeStrokeColor(etLayoutStartHour, false)
                hideError(layoutErrorStartDate)
            }

            if (dateStartIsNotLonger(startDate, endDate)) {
                tvStartDateError.text = hmAuxTranslate[DIALOG_DATE_ERROR_START_EXCEEDED_LBL]
                tvEndDateError.text = hmAuxTranslate[DIALOG_DATE_ERROR_END_EXCEEDED_LBL]
                etLayoutStartDate.changeStrokeColor(etLayoutStartHour, true)
                etLayoutEndDate.changeStrokeColor(etLayoutEndHour, true)
                showError(layoutErrorEndDate)
                showError(layoutErrorStartDate)
                return false
            } else {
                etLayoutStartDate.changeStrokeColor(etLayoutStartHour, false)
                etLayoutEndDate.changeStrokeColor(etLayoutEndHour, false)
                hideError(layoutErrorStartDate)
                hideError(layoutErrorEndDate)
            }

            if (dateIsFuture(startDate.parseFullDate())) {
                tvStartDateError.text = hmAuxTranslate[DIALOG_DATE_ERROR_END_NOT_FUTURE_LBL]
                etLayoutStartDate.changeStrokeColor(etLayoutStartHour, true)
                showError(layoutErrorStartDate)
                return false
            } else {
                etLayoutEndDate.changeStrokeColor(etLayoutEndHour, false)
                hideError(layoutErrorEndDate)
            }

            if (dateIsFuture(endDate.parseFullDate())) {
                tvEndDateError.text = hmAuxTranslate[DIALOG_DATE_ERROR_END_NOT_FUTURE_LBL]
                etLayoutEndDate.changeStrokeColor(etLayoutEndHour, true)
                showError(layoutErrorEndDate)
                return false
            } else {
                etLayoutEndDate.changeStrokeColor(etLayoutEndHour, false)
                hideError(layoutErrorEndDate)
            }

            val checkDataEquals = compareDates(startDate, endDate) { date1, date2 ->
                date1.time == date2.time
            }

            if(checkDataEquals){
                tvStartDateError.text = hmAuxTranslate[DIALOG_USER_DATE_EQUALS_ERROR_LBL]
                etLayoutStartDate.changeStrokeColor(etLayoutStartHour, true)
                showError(layoutErrorStartDate)

                tvEndDateError.text = hmAuxTranslate[DIALOG_USER_DATE_EQUALS_ERROR_LBL]
                etLayoutEndDate.changeStrokeColor(etLayoutEndHour, true)
                showError(layoutErrorEndDate)
                return false
            }else{
                etLayoutStartDate.changeStrokeColor(etLayoutStartHour, false)
                etLayoutEndDate.changeStrokeColor(etLayoutEndHour, false)
                hideError(layoutErrorStartDate)
                hideError(layoutErrorEndDate)
            }

            return true
        }
    }

    private fun TextInputLayout.changeStrokeColor(
        inputLayoutHour: TextInputLayout,
        isError: Boolean
    ) {
        with(binding) {
            if (isError) {
                setBoxStrokeColorState(context, R.drawable.edittext_error)
                inputLayoutHour.setBoxStrokeColorState(context, R.drawable.edittext_error)
                setHintTextColor(context, R.drawable.edittext_error)
                inputLayoutHour.setHintTextColor(context, R.drawable.edittext_error)
            } else {
                setBoxStrokeColorState(context, R.drawable.edittext_theme)
                inputLayoutHour.setBoxStrokeColorState(context, R.drawable.edittext_theme)
                setHintTextColor(context, R.drawable.edittext_theme)
                inputLayoutHour.setHintTextColor(context, R.drawable.edittext_theme)
            }
        }
    }

    private fun dateStartIsNotLonger(startDate: String, endDate: String): Boolean {
        return compareDates(
            startDate,
            endDate
        ) { date1, date2 -> date1.time != date2.time && date1.after(date2) }
    }

    private fun showError(errorLayout: View) {
        updateErrorState(errorLayout, true)
    }

    private fun hideError(errorLayout: View) {
        updateErrorState(errorLayout, false)
    }

    private fun updateErrorState(errorLayout: View, showError: Boolean) {
        errorLayout.apply {
            visibility = if (showError) View.VISIBLE else View.GONE
        }
        binding.btnSave.isEnabled = !showError
    }


    private fun initializeListener() {
        with(binding) {
            if (isEditMode) {
                btnClose.setOnClickListener {
                    dialog.dismiss()
                }
            }

            btnCancel.setOnClickListener {
                if (isEditMode) {
                    //
                    context.showMaterialAlert(
                        title = hmAuxTranslate[PROCESS_TRIP_DELETE_USERS_TTL] ?: "",
                        msg = (hmAuxTranslate[PROCESS_TRIP_DELETE_USERS_MSG] + "\n" + user.userName)
                            ?: "",
                        actionPositiveLbl = hmAuxTranslate[PROCESS_TRIP_DELETE_USERS_CONFIRM]
                            ?: "",
                        actionNeutralLbl = hmAuxTranslate[PROCESS_TRIP_DELETE_USERS_ABORT]
                            ?: "",
                        actionPositive = { _, _ ->
                            onSaveUser(
                                user.copy(
                                    dateStart = "${etStartDate.text} ${etStartHour.text}".convertDateToFullTimeStampGMT(
                                        inputFormat = "dd/MM/yy HH:mm"
                                    )
                                ),
                                UserAction.DELETE,
                                Pair(
                                    hmAuxTranslate[PROCESS_TRIP_SAVE_USERS_TTL] ?: "",
                                    hmAuxTranslate[PROCESS_TRIP_SAVE_USERS_MSG] ?: ""
                                )
                            )
                        },
                        actionNeutral = { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }
                    ).show()

                } else {
                    dialog.dismiss()
                }
            }

            btnSave.apply {
                text = hmAuxTranslate[DIALOG_EDIT_BTN_SAVE]
                setOnClickListener {
                    isEnabled = false
                    onSaveUser(
                        user.copy(
                            dateStart = "${etStartDate.text} ${etStartHour.text}".convertDateToFullTimeStampGMT(
                                inputFormat = "dd/MM/yy HH:mm"
                            ),
                            dateEnd = "${etEndDate.text} ${etEndHour.text}".convertDateToFullTimeStampGMT(
                                inputFormat = "dd/MM/yy HH:mm"
                            )
                        ),
                        if(isEditMode) UserAction.EDIT else UserAction.INSERT,
                        Pair(
                            hmAuxTranslate[PROCESS_TRIP_SAVE_USERS_TTL] ?: "",
                            hmAuxTranslate[PROCESS_TRIP_SAVE_USERS_MSG] ?: ""
                        )
                    )
                }
            }
        }
    }

    companion object {
        const val DIALOG_EDIT_BTN_CANCEL = "dialog_edit_btn_cancel"
        const val DIALOG_EDIT_BTN_SAVE = "dialog_edit_btn_save"
        const val DIALOG_EDIT_DATE_HINT = "dialog_edit_date_hint"
        const val DIALOG_EDIT_HOUR_HINT = "dialog_edit_hour_hint"
        const val DIALOG_PARTICIPANT_SINCE_LBL = "dialog_participant_since_lbl"
        const val DIALOG_PARTICIPANT_UNTIL_LBL = "dialog_participant_until_lbl"
        const val DIALOG_DATE_ERROR_START_EXCEEDED_LBL = "dialog_date_error_start_exceeded_lbl"
        const val DIALOG_BTN_CLEAN_DATE_LBL = "dialog_btn_clean_date_lbl"
        const val DIALOG_DATE_ERROR_END_EXCEEDED_LBL = "dialog_date_error_end_exceeded_lbl"
        const val DIALOG_DATE_ERROR_END_NOT_FUTURE_LBL = "dialog_date_error_end_not_future_lbl"
        const val DIALOG_DATE_ERROR_START_BEFORE_TRIP_LBL =
            "dialog_date_error_start_before_trip_lbl"
        const val DIALOG_PARTICIPANT_DELETE_LBL = "dialog_participant_delete_lbl"
        const val PROCESS_TRIP_SAVE_USERS_TTL = "process_trip_save_users_ttl"
        const val PROCESS_TRIP_SAVE_USERS_MSG = "process_trip_save_users_msg"
        const val PROCESS_TRIP_DELETE_USERS_TTL = "process_trip_delete_users_ttl"
        const val PROCESS_TRIP_DELETE_USERS_MSG = "process_trip_delete_users_msg"
        const val PROCESS_TRIP_DELETE_USERS_CONFIRM = "process_trip_delete_users_confirm"
        const val PROCESS_TRIP_DELETE_USERS_ABORT = "process_trip_delete_users_abort"
        const val DIALOG_USER_DATE_CONFLICT_ERROR_LBL = "dialog_user_date_conflict_error_lbl"
        const val DIALOG_USER_DATE_EQUALS_ERROR_LBL = "dialog_user_date_equals_error_lbl"


        fun loadTranslation(context: Context): HMAux = listOf(
            DIALOG_EDIT_BTN_CANCEL,
            DIALOG_EDIT_BTN_SAVE,
            DIALOG_EDIT_DATE_HINT,
            DIALOG_EDIT_HOUR_HINT,
            DIALOG_PARTICIPANT_SINCE_LBL,
            DIALOG_PARTICIPANT_UNTIL_LBL,
            DIALOG_DATE_ERROR_START_EXCEEDED_LBL,
            DIALOG_DATE_ERROR_END_EXCEEDED_LBL,
            DIALOG_DATE_ERROR_START_BEFORE_TRIP_LBL,
            DIALOG_DATE_ERROR_END_NOT_FUTURE_LBL,
            PROCESS_TRIP_SAVE_USERS_TTL,
            PROCESS_TRIP_SAVE_USERS_MSG,
            DIALOG_BTN_CLEAN_DATE_LBL,
            DIALOG_PARTICIPANT_DELETE_LBL,
            PROCESS_TRIP_DELETE_USERS_TTL,
            PROCESS_TRIP_DELETE_USERS_MSG,
            PROCESS_TRIP_DELETE_USERS_CONFIRM,
            PROCESS_TRIP_DELETE_USERS_ABORT,
            DIALOG_USER_DATE_CONFLICT_ERROR_LBL,
            DIALOG_USER_DATE_EQUALS_ERROR_LBL
        ).let(
            TranslateResource(
                context = context,
                mResoure_code = context.getResourceCode(ReportBottomSheet.RESOURCE_DIALOG_EVENT_TRIP)
            )::setLanguage
        )
    }
}