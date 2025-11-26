package com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.namoa_digital.namoa_library.compose.theme.NamoaApplicationTheme
import com.namoadigital.prj001.R
import com.namoadigital.prj001.model.event.local.EventManual
import dagger.hilt.android.AndroidEntryPoint


interface OnEventManualDialogInteract {

    fun showProgressDialog(
        title: String,
        message: String,
    )

    fun refreshEventCard()

    companion object {
        const val SAVE_EVENT = "ws_save_event_manual"
        const val CLOUD_SAVE_EVENT = "ws_cloud_save_event"
    }
}

@AndroidEntryPoint
class EventManualDialog : DialogFragment() {

    private var listener: OnEventManualDialogInteract? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = context as? OnEventManualDialogInteract
            ?: throw ClassCastException("$context deve implementar EventManualDialogInteract")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), R.style.CustomDialogStyle)
        dialog.setContentView(composeView())
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
            setLayout(
                (resources.displayMetrics.widthPixels * 0.9).toInt(),
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            setGravity(Gravity.CENTER)
        }
        return dialog
    }

    private fun composeView(): ComposeView {

        val eventToEdit = arguments?.getString(ARG_EVENT, null)
        var eventManual: EventManual? = null

        if (eventToEdit != null) {
            eventManual = Gson().fromJson(eventToEdit, EventManual::class.java)
        }
        return ComposeView(requireContext()).apply {
            setContent {
                NamoaApplicationTheme {
                    EventUIRoute(
                        eventToEdit = eventManual,
                        onShowProgress = { title, message ->
                            listener?.showProgressDialog(title, message)
                        },
                        onClose = {
                            dismiss()
                        }
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    companion object {
        private const val ARG_EVENT = "arg_event"

        /**
         * Cria uma nova instância do Dialog, passando um evento opcional para edição.
         *
         * @param event O evento para editar. Se for nulo, o fluxo de criação de um novo evento será iniciado.
         *              Importante: a classe [EventManual] e suas aninhadas devem ser [Serializable].
         * @return Uma nova instância de EventManualDialog.
         */
        fun newInstance(event: EventManual?): EventManualDialog {
            var args: Bundle? = null

            if (event != null) {
                args = Bundle().apply {
                    val gson = Gson().toJson(event)
                    putString(ARG_EVENT, gson)
                }
            }

            return EventManualDialog().apply {
                if (args != null) arguments = args
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}