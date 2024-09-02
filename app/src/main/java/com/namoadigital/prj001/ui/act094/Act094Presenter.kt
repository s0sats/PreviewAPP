package com.namoadigital.prj001.ui.act094

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.core.trip.domain.usecase.destination.DestinationUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.destination.SaveDestinationUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.destination.SelectDestinationUseCase.SelectDestinationParam
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.dao.trip.FsTripDestinationDao
import com.namoadigital.prj001.extensions.suspendResults
import com.namoadigital.prj001.model.TranslateResource
import com.namoadigital.prj001.model.trip.FsTripDestination.Companion.TICKET_DESTINATION_TYPE
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Download
import com.namoadigital.prj001.service.WS_TK_Ticket_Download
import com.namoadigital.prj001.service.trip.WsSelectDestination
import com.namoadigital.prj001.ui.act094.Contract.View
import com.namoadigital.prj001.ui.act094.destination.domain.destination_availables.AvailableDestinationFilter
import com.namoadigital.prj001.ui.act094.domain.model.SelectionDestinationAvailable
import com.namoadigital.prj001.ui.act094.domain.toAdapterList
import com.namoadigital.prj001.ui.act094.util.Act094Translate
import com.namoadigital.prj001.ui.act094.util.Act094Translate.ALERT_DESTINATION_SAVE_ERROR_MSG
import com.namoadigital.prj001.ui.act094.util.Act094Translate.ALERT_DESTINATION_SAVE_ERROR_TTL
import com.namoadigital.prj001.ui.act094.util.Act094Translate.ALERT_DESTINATION_SELECTED_MSG
import com.namoadigital.prj001.ui.act094.util.Act094Translate.ALERT_NO_CONTACT_APP_FOUND_MSG
import com.namoadigital.prj001.ui.act094.util.Act094Translate.ALERT_NO_CONTACT_APP_FOUND_TTL
import com.namoadigital.prj001.ui.act094.util.Act094Translate.ALERT_NO_NAVEGATION_APP_FOUND_MSG
import com.namoadigital.prj001.ui.act094.util.Act094Translate.ALERT_NO_NAVEGATION_APP_FOUND_TTL
import com.namoadigital.prj001.ui.act094.util.Act094Translate.ALERT_TRIP_NOT_FOUND_MSG
import com.namoadigital.prj001.ui.act094.util.Act094Translate.ALERT_TRIP_NOT_FOUND_TTL
import com.namoadigital.prj001.ui.act094.util.SelectDestinationUiEvent
import com.namoadigital.prj001.ui.act094.util.SelectDestinationUiEvent.OpenDialog.DialogType
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Act094Presenter constructor(
    private val translateResource: TranslateResource,
    private val destinationsUseCase: DestinationUseCase,
    private val fsTripDao: FSTripDao,
    private val fsTripDestinationDao: FsTripDestinationDao,
    private val ticketDao: TK_TicketDao,
) : Contract.Presenter {

    private lateinit var view: View
    override fun getListDestinationAvailable(filter: AvailableDestinationFilter?) {

        CoroutineScope(Dispatchers.IO).launch {
            destinationsUseCase.execGetAvailableDestination?.invoke(Unit)?.suspendResults(
                success = { list ->
                    withContext(Dispatchers.Main) {
                        view.onEvent(SelectDestinationUiEvent.ListingDestinations(
                            list.map { m -> m.toAdapterList() }
                        ))
                    }
                },
                loading = { _, _ ->
                    withContext(Dispatchers.Main){
                        view.onEvent(SelectDestinationUiEvent.Loading)
                    }
                }

            )
        }
    }

    override fun execSelectDestination(
        context: Context,
        selectionDestinationAvailable: SelectionDestinationAvailable
    ) {
        val trip = fsTripDao.getTrip()
        val gson = GsonBuilder().serializeNulls().create()
        trip?.let {
            if(!ToolBox_Con.isOnline(context)
                || it.hasUpdateRequired){
                saveDestination(
                    context = context,
                    destination = selectionDestinationAvailable,
                )
                return
            }
            view.wsProcess = WsSelectDestination.NAME
            view.onEvent(
                SelectDestinationUiEvent.OpenDialog(
                    DialogType.PROCESS(
                        Act094Translate.PROCESS_SELECTION_DESTINATION_TITLE,
                        Act094Translate.PROCESS_SELECTION_DESTINATION_MSG
                    )
                )
            )
            destinationsUseCase.execSelectDestination?.invoke(
                SelectDestinationParam(
                    trip.tripPrefix,
                    trip.tripCode,
                    trip.scn,
                    selectionDestinationAvailable.destinationType ?: "",
                    selectionDestinationAvailable.siteCode,
                    selectionDestinationAvailable.ticketPrefix,
                    selectionDestinationAvailable.ticketCode,
                )
            )
        } ?: view.onEvent(
            SelectDestinationUiEvent.OpenDialog(
                DialogType.ACTION(
                    hmAux_Trans[ALERT_TRIP_NOT_FOUND_TTL],
                    hmAux_Trans[ALERT_TRIP_NOT_FOUND_MSG],
                    { dialogInterface, i -> dialogInterface.dismiss() }
                )
            )
        )
    }

    override fun getDestinationFilter(): AvailableDestinationFilter {
        return destinationsUseCase.getFilterPreference?.invoke(Unit)!!
    }

    override fun saveDestination(
        context: Context,
        response: String?,
        destination: SelectionDestinationAvailable,
        forceOfflineFlow: Boolean
    ) {
        val trip = fsTripDao.getTrip()
        destinationsUseCase.saveDestination?.let {
            val result = it(
                SaveDestinationUseCase.GetDestinationParams(
                    ToolBox_Con.getPreference_Customer_Code(context),
                    response,
                    destination
                )
            )
            if (result) {
                if (destination.destinationType == TICKET_DESTINATION_TYPE) {
                    val ticket = if(destination.ticketPrefix != null
                                     &&  destination.ticketCode != null
                        ){
                        ticketDao.getTicket(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            destination.ticketPrefix,
                            destination.ticketCode
                        )
                    }else{
                        null
                    }

                    if(!ToolBox_Con.isOnline(context)
                        || trip?.hasUpdateRequired == true
                        || forceOfflineFlow
                        || (ticket!= null && ticket.sync_required == 0)
                        ){
                        view.callAct005()
                        return@let
                    }
                    //
                    view.wsProcess = WS_TK_Ticket_Download::class.java.name
                    view.onEvent(
                        SelectDestinationUiEvent.OpenDialog(
                            DialogType.PROCESS(
                                Act094Translate.PROCESS_DOWNLOAD_TICKET_DESTINATION_TTL,
                                Act094Translate.PROCESS_DOWNLOAD_TICKET_DESTINATION_MSG
                            )
                        )
                    )
                    //
                    val mIntent = Intent(context, WBR_TK_Ticket_Download::class.java)
                    val bundle = Bundle()
                    bundle.putString(
                        TK_TicketDao.TICKET_PREFIX,
                        ToolBox_Con.getPreference_Customer_Code(context)
                            .toString() + "|" + destination.ticketPrefix + "|" + destination.ticketCode + "|" + 0
                    )
                    mIntent.putExtras(bundle)
                    //
                    context.sendBroadcast(mIntent)
                } else {
                    view.callAct005()
                }
            } else {
                view.onEvent(
                    SelectDestinationUiEvent.OpenDialog(
                        DialogType.ACTION(
                            hmAux_Trans[ALERT_DESTINATION_SAVE_ERROR_TTL],
                            hmAux_Trans[ALERT_DESTINATION_SAVE_ERROR_MSG],
                            { dialogInterface, i -> dialogInterface.dismiss() }
                        )
                    )
                )
            }
        } ?: view.onEvent(
            SelectDestinationUiEvent.OpenDialog(
                DialogType.ACTION(
                    hmAux_Trans[ALERT_DESTINATION_SAVE_ERROR_TTL],
                    hmAux_Trans[ALERT_DESTINATION_SAVE_ERROR_MSG],
                    { dialogInterface, i -> dialogInterface.dismiss() }
                )
            )
        )
    }

    override fun setView(view: View) {
        this.view = view
    }

    override fun loadTranslation(): HMAux {
        mutableListOf(
            Act094Translate.ACT_TITLE,
            Act094Translate.EMPTY_LIST,
            Act094Translate.PROCESS_GET_LIST_TITLE,
            Act094Translate.START_PROCESS_GET_LIST,
            Act094Translate.PROCESS_SELECTION_DESTINATION_TITLE,
            Act094Translate.PROCESS_SELECTION_DESTINATION_MSG,
            Act094Translate.DIALOG_FILTER_TITLE,
            Act094Translate.DIALOG_FILTER_SHOW_PLANNED,
            Act094Translate.DIALOG_FILTER_SHOW_TODAY,
            Act094Translate.DIALOG_FILTER_SHOW_URGENT,
            Act094Translate.DIALOG_FILTER_HIDE_OS_PREVENTIVE,
            Act094Translate.DIALOG_FILTER_CANCEL,
            Act094Translate.DIALOG_FILTER_APPLY,
            ALERT_NO_CONTACT_APP_FOUND_TTL,
            ALERT_NO_CONTACT_APP_FOUND_MSG,
            ALERT_DESTINATION_SELECTED_MSG,
            ALERT_NO_NAVEGATION_APP_FOUND_TTL,
            ALERT_NO_NAVEGATION_APP_FOUND_MSG,
            ALERT_TRIP_NOT_FOUND_TTL,
            ALERT_TRIP_NOT_FOUND_MSG,
            ALERT_DESTINATION_SAVE_ERROR_TTL,
            ALERT_DESTINATION_SAVE_ERROR_MSG,
        ).let { list ->

            return ToolBox_Inf.setLanguage(
                translateResource.context,
                translateResource.mModule_code,
                translateResource.mResoure_code,
                ToolBox_Con.getPreference_Translate_Code(translateResource.context),
                list
            )
        }
    }

}