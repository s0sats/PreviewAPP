package com.namoadigital.prj001.ui.act070.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.setViewTreeLifecycleOwner
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.SerialSearchListDialogBinding
import com.namoadigital.prj001.design.compose.NamoaTheme
import com.namoadigital.prj001.model.BaseSerialSearchItem
import com.namoadigital.prj001.model.BaseSerialSearchItem.SerialSearchExceededItem
import com.namoadigital.prj001.model.ticket.TkSerialSearchRequest.Companion.DEFAULT_PAGE_SIZE
import com.namoadigital.prj001.ui.act070.model.ListSerialSearchItemsArguments
import com.namoadigital.prj001.ui.act070.view.composable.OnActionListSerialSearchItems
import com.namoadigital.prj001.ui.act070.view.composable.SerialListLayout


class DialogSerialSearchFragment : DialogFragment() {

    private lateinit var binding: SerialSearchListDialogBinding

    private var onActionListSerialSearchItems: OnActionListSerialSearchItems? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NORMAL, R.style.AppTheme_NoActionBar)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SerialSearchListDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initScreen()
    }

    override fun onAttach(context: Context) {
        if(context is OnActionListSerialSearchItems){
            onActionListSerialSearchItems = context
        }
        super.onAttach(context)
    }

    private fun initScreen() {
        val serialSearchValue = processSerialSearchList()
        //
        binding.serialSearchCompose.apply {
            setViewTreeLifecycleOwner(this@DialogSerialSearchFragment)
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                NamoaTheme(false) {
                    SerialListLayout(
                        modifier = Modifier.fillMaxSize(),
                        arguments = serialSearchValue,
                        onClose = { dismiss() },
                        onSelectItem = {
                            onActionListSerialSearchItems?.selectSerial(it)
                            dismiss()
                        }
                    )
                }
            }

        }
    }

    private fun processSerialSearchList(): ListSerialSearchItemsArguments {
        val gson = Gson()
        val list = gson.fromJson<List<BaseSerialSearchItem>>(
            arguments?.getString(ListSerialSearchItemsArguments.ARGUMENTS.LIST) ?: "[]",
            object : TypeToken<List<BaseSerialSearchItem.SerialSearchItem>>() {}.type
        ).toMutableList()

        val translateMap = gson.fromJson<HMAux>(
            arguments?.getString(ListSerialSearchItemsArguments.ARGUMENTS.TRANSLATE_MAP) ?: "[]",
            object : TypeToken<HMAux>() {}.type
        )

        val lineCount = arguments?.getInt(LINE_COUNT) ?: -1


        if (list.size == DEFAULT_PAGE_SIZE) {
            list.add(
                SerialSearchExceededItem(
                    translateMap[DIALOG_LABEL_LIST_EXCEEDED_SERIAL_SEARCH],
                    translateMap[DIALOG_LABEL_EXCEEDED_LIMIT_SERIAL_SEARCH],
                    1,
                    translateMap[DIALOG_LABEL_EXCEEDED_FOUNDS_SERIAL_SEARCH],
                    lineCount
                )
            )
        }
        return ListSerialSearchItemsArguments(list, translateMap)
    }


    companion object {

        const val TAG = "DialogSerialSearchFragment"

        const val HINT_SERIAL_SEARCH = "hint_serial_search"
        const val TITLE_SERIAL_SEARCH = "title_serial_search"
        const val DIALOG_TITLE_SERIAL_SEARCH = "dialog_title_serial_search"
        const val BTN_CONFIRM_SERIAL_SEARCH = "btn_confirm_serial_search"
        const val DIALOG_HINT_SERIAL_SEARCH = "dialog_hint_serial_search"
        const val DIALOG_LABEL_SECTION_SERIAL_SEARCH = "dialog_label_section_serial_search"
        const val DIALOG_LABEL_EMPTY_LIST_SERIAL_SEARCH = "dialog_label_empty_list_serial_search"
        const val DIALOG_LABEL_LIST_EXCEEDED_SERIAL_SEARCH =
            "dialog_label_list_exceeded_serial_search"
        const val DIALOG_LABEL_EXCEEDED_LIMIT_SERIAL_SEARCH =
            "dialog_label_exceeded_limit_serial_search"
        const val DIALOG_LABEL_EXCEEDED_FOUNDS_SERIAL_SEARCH =
            "dialog_label_exceeded_founds_serial_search"
        const val DIALOG_LABEL_CARD_SITE_SERIAL_SEARCH = "dialog_label_card_site_serial_search"
        const val DIALOG_LABEL_CARD_TICKET_OPEN_SERIAL_SEARCH =
            "dialog_label_card_ticket_open_serial_search"
        const val TITLE_SERVICE_PROCESS_GET_LIST_SERIAL_SEARCH =
            "title_service_process_get_list_serial_search";
        const val TITLE_SERVICE_PROCESS_CONFIRM_SERIAL_SEARCH =
            "title_service_process_confirm_serial_search";
        const val MSG_SERVICE_PROCESS_GET_LIST_SERIAL_SEARCH =
            "msg_service_process_get_list_serial_search";
        const val MSG_SERVICE_PROCESS_CONFIRM_SERIAL_SEARCH =
            "msg_service_process_confirm_serial_search";
        const val TITLE_SERVICE_ERROR_SERIAL_SEARCH = "title_service_error_serial_search";
        const val MSG_SERVICE_ERROR_SERIAL_SEARCH = "msg_service_error_serial_search";
        const val TITLE_SERVICE_EMPTY_LIST_SERIAL_SEARCH = "title_service_empty_list_serial_search";
        const val MSG_SERVICE_EMPTY_LIST_SERIAL_SEARCH = "msg_service_empty_list_serial_search";
        const val LINE_COUNT = "line_count"

        private val listTranslate = listOf(
            HINT_SERIAL_SEARCH,
            TITLE_SERIAL_SEARCH,
            DIALOG_TITLE_SERIAL_SEARCH,
            BTN_CONFIRM_SERIAL_SEARCH,
            DIALOG_HINT_SERIAL_SEARCH,
            DIALOG_LABEL_SECTION_SERIAL_SEARCH,
            DIALOG_LABEL_EMPTY_LIST_SERIAL_SEARCH,
            DIALOG_LABEL_LIST_EXCEEDED_SERIAL_SEARCH,
            DIALOG_LABEL_EXCEEDED_LIMIT_SERIAL_SEARCH,
            DIALOG_LABEL_EXCEEDED_FOUNDS_SERIAL_SEARCH,
            DIALOG_LABEL_CARD_SITE_SERIAL_SEARCH,
            DIALOG_LABEL_CARD_TICKET_OPEN_SERIAL_SEARCH,
            TITLE_SERVICE_PROCESS_GET_LIST_SERIAL_SEARCH,
            TITLE_SERVICE_PROCESS_CONFIRM_SERIAL_SEARCH,
            MSG_SERVICE_PROCESS_GET_LIST_SERIAL_SEARCH,
            MSG_SERVICE_PROCESS_CONFIRM_SERIAL_SEARCH,
            TITLE_SERVICE_ERROR_SERIAL_SEARCH,
            MSG_SERVICE_ERROR_SERIAL_SEARCH,
            TITLE_SERVICE_EMPTY_LIST_SERIAL_SEARCH,
            MSG_SERVICE_EMPTY_LIST_SERIAL_SEARCH,
        )

        fun newInstance(
            list: MutableList<out BaseSerialSearchItem>,
            lineCount: Int?,
            hmAux_Trans: HMAux,
        ): DialogSerialSearchFragment {

            val filterHMAux = hmAux_Trans.filterKeys { it in listTranslate }

            val bundle = Bundle().apply {
                val gson = Gson()
                putString(ListSerialSearchItemsArguments.ARGUMENTS.LIST, gson.toJson(list))
                putInt(LINE_COUNT, lineCount?:-1)
                putString(
                    ListSerialSearchItemsArguments.ARGUMENTS.TRANSLATE_MAP,
                    gson.toJson(filterHMAux)
                )
            }

            return DialogSerialSearchFragment().apply {
                arguments = bundle
            }
        }
    }
}
