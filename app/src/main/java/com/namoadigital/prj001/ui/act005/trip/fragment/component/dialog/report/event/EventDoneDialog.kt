package com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.event

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.DialogEventDoneTripBinding
import com.namoadigital.prj001.extensions.date.getCurrentDateApi
import com.namoadigital.prj001.extensions.getResourceCode
import com.namoadigital.prj001.extensions.parseDatePair
import com.namoadigital.prj001.extensions.setBoxStrokeColorState
import com.namoadigital.prj001.model.TranslateResource
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.ui.act005.trip.fragment.base.BaseTripDialog
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.ReportBottomSheet
import com.namoadigital.prj001.ui.base.BaseDialog

class EventDoneDialog constructor(
    private val context: Context,
    trip: FSTrip,
    private val dateStart: String,
    private val onSave: (date: String) -> Unit
) : BaseTripDialog<DialogEventDoneTripBinding>(trip, null) {


    private val hmAuxTranslate by lazy {
        loadTranslation(context)
    }

    init {
        dialog = BaseDialog.Builder(
            context = context,
            contentView = DialogEventDoneTripBinding.inflate(LayoutInflater.from(context))
        ).content { _, binding ->
            this.binding = binding
            initializeView()
            initializeListener()
        }.build()
    }

    private fun initializeView() {
        with(binding) {
            tvTitle.text = hmAuxTranslate[DIALOG_EVENT_DONE_TITLE_LBL]
            etLayoutEndDate.hint = hmAuxTranslate[DIALOG_EVENT_DONE_START_DATE_HINT]
            etLayoutEndHour.hint = hmAuxTranslate[DIALOG_EVENT_DONE_START_HOUR_HINT]
            btnFinish.text = hmAuxTranslate[DIALOG_EVENT_DONE_BTN_SAVE]
            tvDesc.text = hmAuxTranslate[DIALOG_EVENT_DONE_DESC_LBL]
            val currentDate = getCurrentDateApi()
            val (date, hour) = currentDate.parseDatePair()
            etEndDate.setText(date)
            etEndHour.setText(hour)
        }
    }

    private fun initializeListener() {
        with(binding) {

            btnClose.setOnClickListener {
                dismiss()
            }

            etEndDate.setDelegatePickerChange { _ ->
                btnFinish.isEnabled = isValidDate()
            }

            etEndHour.setDelegatePickerChange { _ ->
                btnFinish.isEnabled = isValidDate()
            }

            btnFinish.setOnClickListener {
                onSave("${etEndDate.text.toString()} ${etEndHour.text.toString()}")
            }
        }
    }

    private fun isValidDate(): Boolean {
        with(binding) {
            val date = "${etEndDate.text.toString()} ${etEndHour.text.toString()}"

            if (etEndDate.text.toString().isEmpty() || etEndHour.text.toString().isEmpty()) {
                return false
            }

            if (dateIsFuture(date)) {
                tvDateInvalid.text = hmAuxTranslate[DIALOG_EVENT_DONE_INVALIDATE_FUTURE_LBL]
                layoutDateInvalid.visibility = View.VISIBLE
                return false
            }

            compareDates(date, dateStart) { userDate, startDate ->
                userDate.before(startDate)
            }.let { isBefore ->
                if (isBefore) {
                    tvDateInvalid.text =
                        hmAuxTranslate[DIALOG_EVENT_DONE_DATE_EXCEED_START_DATE_LBL]
                    etLayoutEndHour.setBoxStrokeColorState(context, R.drawable.edittext_error)
                    etLayoutEndDate.setBoxStrokeColorState(context, R.drawable.edittext_error)
                    layoutDateInvalid.visibility = View.VISIBLE
                    return false
                }
            }

            etLayoutEndHour.setBoxStrokeColorState(context, R.drawable.edittext_theme)
            etLayoutEndDate.setBoxStrokeColorState(context, R.drawable.edittext_theme)
            layoutDateInvalid.visibility = View.GONE
            return true
        }
    }

    override fun show() {
        dialog.show()
    }

    override fun dismiss() {
        dialog.dismiss()
    }

    override fun errorSendData() {
        with(binding) {
            btnFinish.isEnabled = true
        }
    }

    companion object {
        const val DIALOG_EVENT_DONE_TITLE_LBL = "dialog_event_done_title_lbl"
        const val DIALOG_EVENT_DONE_DESC_LBL = "dialog_event_done_desc_lbl"
        const val DIALOG_EVENT_DONE_START_DATE_HINT = "dialog_event_done_start_date_hint"
        const val DIALOG_EVENT_DONE_START_HOUR_HINT = "dialog_event_done_start_hour_hint"
        const val DIALOG_EVENT_DONE_BTN_SAVE = "dialog_event_done_btn_save"
        const val DIALOG_EVENT_DONE_INVALIDATE_FUTURE_LBL =
            "dialog_event_done_invalidate_future_lbl"
        const val DIALOG_EVENT_DONE_DATE_EXCEED_START_DATE_LBL =
            "dialog_event_done_date_exceed_start_date_lbl"
        const val DIALOG_EVENT_DONE_PROCESS_TITLE = "dialog_event_done_process_title"
        const val DIALOG_EVENT_DONE_PROCESS_MSG = "dialog_event_done_process_msg"


        fun loadTranslation(context: Context): HMAux = listOf(
            DIALOG_EVENT_DONE_TITLE_LBL,
            DIALOG_EVENT_DONE_DESC_LBL,
            DIALOG_EVENT_DONE_START_DATE_HINT,
            DIALOG_EVENT_DONE_START_HOUR_HINT,
            DIALOG_EVENT_DONE_BTN_SAVE,
            DIALOG_EVENT_DONE_PROCESS_TITLE,
            DIALOG_EVENT_DONE_PROCESS_MSG,
            DIALOG_EVENT_DONE_INVALIDATE_FUTURE_LBL,
            DIALOG_EVENT_DONE_DATE_EXCEED_START_DATE_LBL
        ).let(
            TranslateResource(
                context = context,
                mResoure_code = context.getResourceCode(ReportBottomSheet.RESOURCE_DIALOG_EVENT_TRIP)
            )::setLanguage
        )
    }

}