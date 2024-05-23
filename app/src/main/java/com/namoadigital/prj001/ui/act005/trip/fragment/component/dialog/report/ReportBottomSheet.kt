package com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.TripReportBottomsheetBinding
import com.namoadigital.prj001.extensions.getResourceCode
import com.namoadigital.prj001.model.TranslateResource
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripBaseFragment.Companion.WS_TRIP_GET_AVAILABLE_USERS
import com.namoadigital.prj001.ui.act005.trip.fragment.base.BaseTripDialog

class ReportBottomSheet constructor(
    private val context: Context,
    private val callServiceUsers: () -> Unit,
    private val callWs: (process: String, title: String, message: String) -> Unit,
    private val openDialogEvent: () -> Unit
) : BottomSheetDialogFragment() {

    private val binding: TripReportBottomsheetBinding by lazy {
        TripReportBottomsheetBinding.inflate(layoutInflater)
    }


    private val hmAuxTranslate: HMAux by lazy {
        loadTranslation(context)
    }

    override fun getTheme() = R.style.BottomSheetDialog_Rounded;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLabels()
        setActions()
    }

    private fun setLabels() {
        with(binding) {
            this.tvTitle.text = hmAuxTranslate[DIALOG_EVENT_TRIP_TITLE]
            this.btnEvent.text =
                hmAuxTranslate[DIALOG_EVENT_TRIP_BTN_EVENT]
            this.btnTechnical.text =
                hmAuxTranslate[DIALOG_EVENT_TRIP_BTN_TECHNICAL]
        }
    }

    private fun setActions() {
        with(binding) {
            this.btnEvent.setOnClickListener {
                openDialogEvent()
                this@ReportBottomSheet.dismiss()
            }

            this.btnTechnical.setOnClickListener {
                this@ReportBottomSheet.dismiss()
                callWs(
                    WS_TRIP_GET_AVAILABLE_USERS,
                    hmAuxTranslate[PROCESS_TRIP_GET_AVAILABLE_USERS_TTL] ?: "",
                    hmAuxTranslate[PROCESS_TRIP_GET_AVAILABLE_USERS_MSG] ?: "",
                    )
                callServiceUsers()
            }
        }
    }


    companion object TRANSLATE {
        const val DIALOG_EVENT_TRIP_TITLE = "dialog_event_trip_title"
        const val DIALOG_EVENT_TRIP_BTN_EVENT = "dialog_event_trip_btn_event"
        const val DIALOG_EVENT_TRIP_BTN_TECHNICAL = "dialog_event_trip_btn_technical"
        const val PROCESS_TRIP_GET_AVAILABLE_USERS_TTL = "process_trip_get_available_users_ttl"
        const val PROCESS_TRIP_GET_AVAILABLE_USERS_MSG = "process_trip_get_available_users_msg"
        const val RESOURCE_DIALOG_EVENT_TRIP = "resource_dialog_event_trip"

        fun loadTranslation(context: Context): HMAux = listOf(
            DIALOG_EVENT_TRIP_TITLE,
            DIALOG_EVENT_TRIP_BTN_EVENT,
            DIALOG_EVENT_TRIP_BTN_TECHNICAL,
            PROCESS_TRIP_GET_AVAILABLE_USERS_TTL,
            PROCESS_TRIP_GET_AVAILABLE_USERS_MSG,
        ).let(
            TranslateResource(
                context = context,
                mResoure_code = context.getResourceCode(RESOURCE_DIALOG_EVENT_TRIP)
            )::setLanguage
        )
    }
}