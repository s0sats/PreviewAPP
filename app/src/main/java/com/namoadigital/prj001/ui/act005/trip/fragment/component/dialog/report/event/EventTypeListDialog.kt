package com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.event

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.databinding.DialogSimpleAdapterBinding
import com.namoadigital.prj001.extensions.getResourceCode
import com.namoadigital.prj001.model.TranslateResource
import com.namoadigital.prj001.model.trip.FSEventType
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.ui.act005.trip.fragment.base.BaseTripDialog
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.ReportBottomSheet
import com.namoadigital.prj001.ui.base.BaseDialog

class EventTypeListDialog constructor(
    context: Context,
    trip: FSTrip,
    private val source: List<FSEventType>,
    private val osSelectType: (type: FSEventType) -> Unit,
    private val hasFormInProcess: Boolean,
) : BaseTripDialog<DialogSimpleAdapterBinding>(trip) {

    private lateinit var eventTypeAdapter: EventTypeListAdapter
    private val hmAuxTranslate = loadTranslation(context)

    init {

        dialog = BaseDialog.Builder(
            context = context,
            contentView = DialogSimpleAdapterBinding.inflate(LayoutInflater.from(context)),
            margin = true
        ).content { _, binding ->
            this@EventTypeListDialog.binding = binding;
            initializeViews()
            initializeListeners()

        }.build()
    }

    private fun initializeViews() {
        with(binding) {
            tvTitle.text = hmAuxTranslate[DIALOG_TITLE]
            edittextFilterLayout.hint = hmAuxTranslate[DIALOG_FILTER_HINT]
            tvEmptyList.text = hmAuxTranslate[DIALOG_EMPTY_LIST]


            cardWarning.isVisible = hasFormInProcess
            tvWarning.text = hmAuxTranslate[DIALOG_HAS_FORM_IN_PROCESS_LBL]

            if (source.isEmpty()) {
                recyclerView.visibility = View.GONE
                tvEmptyList.visibility = View.VISIBLE
                return
            }

            eventTypeAdapter = EventTypeListAdapter(
                source = source.toMutableList(),
                selectEventType = { eventType ->
                    this@EventTypeListDialog.dismiss()
                    osSelectType(eventType)
                },
                hasFormInProcess = hasFormInProcess,
                updateSizeList = {
                    if (it == 0) {
                        recyclerView.visibility = View.GONE
                        tvEmptyList.visibility = View.VISIBLE
                    } else {
                        recyclerView.visibility = View.VISIBLE
                        tvEmptyList.visibility = View.GONE
                    }
                }
            )

            edittextFilter.setOnReportTextChangeListner(object :
                MKEditTextNM.IMKEditTextChangeText {
                override fun reportTextChange(text: String?) {
                }

                override fun reportTextChange(text: String?, p1: Boolean) {
                    eventTypeAdapter.filter(text ?: "")
                }

            })

            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                visibility = View.VISIBLE
                adapter = eventTypeAdapter
            }
        }
    }

    private fun initializeListeners() {
        with(binding) {
            closeDialog.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    override fun show() {
        dialog.show()
    }

    override fun dismiss() {
        dialog.dismiss()
    }

    override fun errorSendData() {}

    companion object TRANSLATE {

        const val DIALOG_TITLE = "list_event_type_title_ttl"
        const val DIALOG_FILTER_HINT = "list_event_type_filter_hint"
        const val DIALOG_EMPTY_LIST = "list_event_type_empty_list"
        const val DIALOG_HAS_FORM_IN_PROCESS_LBL = "dialog_has_form_in_process_lbl"


        fun loadTranslation(context: Context): HMAux = listOf(
            DIALOG_TITLE,
            DIALOG_FILTER_HINT,
            DIALOG_EMPTY_LIST,
            DIALOG_HAS_FORM_IN_PROCESS_LBL
        ).let(
            TranslateResource(
                context = context,
                mResoure_code = context.getResourceCode(ReportBottomSheet.RESOURCE_DIALOG_EVENT_TRIP)
            )::setLanguage
        )
    }
}