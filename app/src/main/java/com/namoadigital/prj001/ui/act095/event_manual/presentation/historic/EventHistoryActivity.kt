package com.namoadigital.prj001.ui.act095.event_manual.presentation.historic

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import com.namoa_digital.namoa_library.compose.theme.NamoaApplicationTheme
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.core.translate.textOf
import com.namoadigital.prj001.model.event.local.EventManual
import com.namoadigital.prj001.receiver.event.WBREventManualSave
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.ui.EventManualDialog
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.ui.OnEventManualDialogInteract
import com.namoadigital.prj001.ui.act095.event_manual.presentation.historic.domain.EventHistoryEvent
import com.namoadigital.prj001.ui.act095.event_manual.presentation.historic.ui.EventHistoryScreen
import com.namoadigital.prj001.ui.act095.event_manual.translate.EventManualKey
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventHistoryActivity : Base_Activity(), OnEventManualDialogInteract {


    val viewModel: EventHistoryViewModel by viewModels()

    private var currentEventDay: Int = -1

    private var translateMap = emptyMap<String, String>()

    private var eventDialog: EventManualDialog? = null

    companion object {
        const val ARG_EVENT_DAY = "arg_event_day"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentEventDay = intent.extras?.getInt(ARG_EVENT_DAY, -1) ?: -1

        refreshEventCard()

        setContent {
            NamoaApplicationTheme {
                EventHistoryScreen(
                    viewModel = viewModel,
                    onEdit = { openDialog(it) },
                    callbackTranslateMap = { translateMap = it },
                )
            }
        }
    }

    private fun openDialog(item: EventManual) {
        val existingDialog = supportFragmentManager.findFragmentByTag("EventManualDialog")

        val isAlreadyShowing =
            existingDialog != null && (existingDialog as? DialogFragment)?.dialog?.isShowing == true

        if (!isAlreadyShowing) {
            eventDialog = EventManualDialog.newInstance(item).also {
                it.show(supportFragmentManager, "EventManualDialog")
            }
        }
    }

    override fun processCloseACT(mLink: String?, mRequired: String?, hmAux: HMAux?) {

        progressDialog.dismiss()
        eventDialog?.dismiss()

        refreshEventCard()

    }

    override fun processError_1(mLink: String?, mRequired: String?) {
        super.processError_1(mLink, mRequired)
        progressDialog.dismiss()
        eventDialog?.dismiss()

        refreshEventCard()

        Toast.makeText(
            applicationContext,
            translateMap.textOf(EventManualKey.SavingEventOfflineMsg),
            Toast.LENGTH_LONG
        ).show()

    }

    override fun processCustom_error(mLink: String?, mRequired: String?) {
        super.processCustom_error(mLink, mRequired)

        eventDialog?.dismiss()
        progressDialog.dismiss()

        refreshEventCard()
    }

    override fun showProgressDialog(title: String, message: String) {
        enableProgressDialog(
            title,
            message,
            translateMap.textOf(key = "sys_alert_btn_cancel"),
            translateMap.textOf(key = "sys_alert_btn_ok")
        )

        val mIntent = Intent(context, WBREventManualSave::class.java)
        context.sendBroadcast(mIntent)
    }

    override fun refreshEventCard() {
        viewModel.onEvent(EventHistoryEvent.GetListHistory(currentEventDay))
    }

}