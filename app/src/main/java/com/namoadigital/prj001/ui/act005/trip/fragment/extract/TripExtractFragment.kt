package com.namoadigital.prj001.ui.act005.trip.fragment.extract

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.adapter.trip.ExtractAdapter
import com.namoadigital.prj001.databinding.FrgExtractTripBinding
import com.namoadigital.prj001.extensions.getColorStateListId
import com.namoadigital.prj001.extensions.openFormPDF
import com.namoadigital.prj001.model.TranslateResource
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.TripTarget
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripBaseFragment
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_FLEET_TRIP_SEND_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_FLEET_TRIP_SEND_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_ORIGIN_TRIP_SEND_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_ORIGIN_TRIP_SEND_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripWsProgress
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.destination.DestinationDialog
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.origin.EditOriginDialog
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.origin.enums.OriginType
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.start_trip.EditStartTripDialog
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.SaveDestinationEdit
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.SaveOriginEdit
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs.PROCESS_DIALOG_START_DATE_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs.PROCESS_DIALOG_START_DATE_TITLE
import com.namoadigital.prj001.util.ToolBox_Inf
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class TripExtractFragment : TripBaseFragment<FrgExtractTripBinding>() {


    override lateinit var binding: FrgExtractTripBinding
    override fun showGPSWarning(isVisible: Int) {
        tripCardWarningBinding?.cardWarning?.visibility = View.GONE
    }

    private var lastIndex: Int? = null
    private val hmAuxExtractTranslate by lazy {
        loadTranslation(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FrgExtractTripBinding.inflate(inflater, container, false)
        initializeViews()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(headerBinding) {
            llExtract.isEnabled = false
            ivExtract.apply {
                background =
                    AppCompatResources.getDrawable(requireContext(), R.drawable.frg_header_menu_btn)
                imageTintList = requireContext().getColorStateListId(R.color.m3_namoa_shadow)
            }

            ivTrip.apply {
                background = null
                imageTintList = requireContext().getColorStateListId(R.color.m3_namoa_surface)
            }
        }


    }


    @SuppressLint("SetTextI18n")
    private fun initializeViews() {
        viewModel.state.onEach { state ->

            if (state.trip == null) return@onEach

            val stateTrip = state.trip
            with(binding) {
                tripId.text =
                    "${hmAuxExtractTranslate[EXTRACT_TRIP_TITLE_LBL]} ${stateTrip.tripPrefix}.${stateTrip.tripCode}"
                emptyList.text = hmAuxExtractTranslate[EXTRACT_EMPTY_LIST_LBL]

                etLayoutFilter.hint = hmAuxExtractTranslate[EXTRACT_FILTER_LBL]


                state.listExtract?.let { listExtract ->
                    if (listExtract.isEmpty()) {
                        emptyList.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    } else {
                        val adapter = ExtractAdapter(
                            context = requireContext(),
                            hmAuxTranslate = hmAuxExtractTranslate,
                            source = listExtract,
                            onSelectUser = { item, position ->
                                lastIndex = position
                                showEditUser(item, true)
                            },
                            onSelectEvent = { item, position ->
                                lastIndex = position
                                showDialogEvent(item, true)
                            },
                            onSelectOrigin = { item, position ->
                                lastIndex = position
                                showEditOrigin(item)
                            },
                            onSelectDestination = { item, position ->
                                lastIndex = position
                                showEditDestination(item)
                            },
                            onSelectStartTrip = { item, position ->
                                lastIndex = position
                                showEditStartTrip(item)
                            },
                            onSelectAction = { item, position ->
                                item.actPDFLocal?.let {
                                    requireContext().openFormPDF(
                                        it,
                                        notFoundDialog = {
                                            ToolBox.alertMSG(
                                                context,
                                                hmAuxTranslate["alert_pdf_not_found_ttl"],
                                                hmAuxTranslate["alert_pdf_not_found_msg"],
                                                DialogInterface.OnClickListener { dialogInterface, i ->
                                                    ToolBox_Inf.scheduleAllDownloadWorkers(context)
                                                },
                                                0
                                            )
                                        },
                                        errorDialog = {
                                            //
                                            ToolBox.alertMSG(
                                                context,
                                                hmAuxTranslate["alert_starting_pdf_not_supported_ttl"],
                                                hmAuxTranslate["alert_starting_pdf_not_supported_msg"],
                                                null,
                                                0
                                            )
                                        }
                                    )
                                }
                            },
                            updateList = { size ->
                                if (size == 0) {
                                    recyclerView.visibility = View.GONE
                                    emptyList.visibility = View.VISIBLE
                                } else {
                                    emptyList.visibility = View.GONE
                                    recyclerView.visibility = View.VISIBLE
                                }
                            }

                        )

                        recyclerView.apply {
                            layoutManager = LinearLayoutManager(context)
                            this.adapter = adapter
                            lastIndex?.let(this::scrollToPosition)
                        }

                        etFilter.addTextChangedListener(onTextChanged = { text, start, before, count ->
                            adapter.filter(text.toString())
                        })
                    }
                }
            }
        }.launchIn(CoroutineScope(Dispatchers.Main))

    }


    fun showEditStartTrip(item: FSTrip) {
        val trip = viewModel.state.value.trip
        trip?.let {
            dialogActive = EditStartTripDialog(
                context = requireContext(),
                trip = trip,
                getDestinationThresholds = { customerCode, tripPrefix, tripCode, destinationSeq, type ->
                    viewModel.getDestinationThresholds(
                        customerCode,
                        tripPrefix,
                        tripCode,
                        destinationSeq,
                        type
                    )
                },
                validateStartDate = { customerCode, tripPrefix, tripCode, date ->
                    viewModel.validateStartTripDate(
                        customerCode,
                        tripPrefix,
                        tripCode,
                        date
                    )
                },
                onSave = { date ->
                    viewModel.saveStartTrip(
                        dateStart = date,
                        progressTranslate = TripWsProgress(
                            process = WS_TRIP_START_DATE_SET,
                            title = hmAuxTranslate[PROCESS_DIALOG_START_DATE_TITLE] ?: "",
                            message = hmAuxTranslate[PROCESS_DIALOG_START_DATE_MSG] ?: "",
                        )
                    )
                }
            )
            dialogActive?.show()
        }
    }


    private var chainOriginAndFleet: SaveOriginEdit.FLEET? = null
    private fun showEditOrigin(item: FSTrip) {
        dialogActive = EditOriginDialog(
            context = requireContext(),
            trip = item,
            getDestinationThresholds = { customerCode, tripPrefix, tripCode, destinationSeq, type ->
                viewModel.getDestinationThresholds(
                    customerCode,
                    tripPrefix,
                    tripCode,
                    destinationSeq,
                    type
                )
            },
            validateOriginDate = { customerCode, tripPrefix, tripCode ->
                viewModel.validateOriginDate(customerCode, tripPrefix, tripCode)
            },
            onSave = { typeSave ->

                when (typeSave) {
                    is SaveOriginEdit.NOTHING -> {}
                    is SaveOriginEdit.ALL -> {
                        chainOriginAndFleet = SaveOriginEdit.FLEET(
                            fleet = typeSave.fleet,
                            odometer = typeSave.odometer,
                            photoUpdate = typeSave.photoUpdate
                        )

                        editSaveOrigin(
                            wsProcess = WS_TRIP_SAVE_FLEET_AND_ORIGIN,
                            date = typeSave.date,
                            chainOriginAndFleet = chainOriginAndFleet!!
                        )
                    }

                    is SaveOriginEdit.FLEET -> {
                        chainOriginAndFleet = null
                        editSaveFleet(
                            fleetPlate = typeSave.fleet,
                            odometer = if (typeSave.odometer.isNotBlank()) {
                                typeSave.odometer.toLong()
                            } else {
                                null
                            },
                            pathImage = typeSave.photoUpdate.path,
                            changePhoto = typeSave.photoUpdate.isNew,
                            deletePhoto = typeSave.photoUpdate.deletePhoto,
                        )
                    }

                    is SaveOriginEdit.ORIGIN -> {
                        chainOriginAndFleet = null
                        editSaveOrigin(
                            wsProcess = WS_TRIP_ORIGIN_SET,
                            date = typeSave.date
                        )
                    }

                }

            },
            onOpenCamera = { id, path ->
                callCamera(id, path)
            }
        )
        dialogActive?.show()
    }


    fun editSaveFleet(
        fleetPlate: String = "",
        odometer: Long? = null,
        pathImage: String = "",
        changePhoto: Int = 0,
        deletePhoto: Boolean = false,
        target: TripTarget = TripTarget.START
    ) {
        val progressTranslate = TripWsProgress(
            process = if (target == TripTarget.END) WS_TRIP_SAVE_FLEET_END_TRIP
            else WS_TRIP_SAVE_FLEET,
            title = hmAuxTranslate[PROGRESS_FLEET_TRIP_SEND_TTL]!!,
            message = hmAuxTranslate[PROGRESS_FLEET_TRIP_SEND_MSG]!!
        )
        chainOriginAndFleet?.let { chain ->
            viewModel.saveFleetData(
                fleetPlate = chain.fleet,
                odometer = chain.odometer.toLong(),
                path = chain.photoUpdate.path,
                changePhoto = chain.photoUpdate.isNew,
                deletePhoto = chain.photoUpdate.deletePhoto,
                target = target,
                progressTranslate = progressTranslate
            )
            return
        }

        viewModel.saveFleetData(
            fleetPlate = fleetPlate,
            odometer = odometer,
            path = pathImage,
            changePhoto = changePhoto,
            target = target,
            deletePhoto = deletePhoto,
            progressTranslate = progressTranslate
        )
    }

    private fun editSaveOrigin(
        wsProcess: String,
        date: String,
        originType: OriginType = OriginType.EDIT,
        chainOriginAndFleet: SaveOriginEdit.FLEET? = null
    ) {
        viewModel.saveOriginSet(
            date = date,
            originType = originType,
            chainOriginAndFleet = chainOriginAndFleet,
            progressTranslate = TripWsProgress(
                process = wsProcess,
                title = hmAuxTranslate[PROGRESS_ORIGIN_TRIP_SEND_TTL]!!,
                message = hmAuxTranslate[PROGRESS_ORIGIN_TRIP_SEND_MSG]!!
            ),
            progressTranslateFleet = TripWsProgress(
                process = WS_TRIP_SAVE_FLEET,
                title = hmAuxTranslate[PROGRESS_FLEET_TRIP_SEND_TTL]!!,
                message = hmAuxTranslate[PROGRESS_FLEET_TRIP_SEND_MSG]!!
            ),
            locationNotFound = {}
        )
    }

    private lateinit var chainDestinationAndFleet: SaveDestinationEdit.ODOMETER
    private fun showEditDestination(item: FsTripDestination) {
        dialogActive = DestinationDialog(
            context = requireContext(),
            trip = viewModel.state.value.trip!!,
            destination = item,
            getDestinationThresholds = { customerCode, tripPrefix, tripCode, destinationSeq, type ->
                viewModel.getDestinationThresholds(
                    customerCode,
                    tripPrefix,
                    tripCode,
                    destinationSeq,
                    type
                )
            },
            onSave = { save ->


                when (save) {
                    is SaveDestinationEdit.DATE -> {
                        viewModel.saveDestinationDate(
                            dateStart = save.dateStart,
                            dateEnd = save.dateEnd,
                            destinationSeq = save.destinationSeq,
                            progressTranslate = TripWsProgress(
                                WS_TRIP_DESTINATION_EDIT_DATE,
                                hmAuxTranslate[TripTranslate.PROGRESS_TRIP_DESTINATION_EDIT_TLL]
                                    ?: "",
                                hmAuxTranslate[TripTranslate.PROGRESS_TRIP_DESTINATION_EDIT_MSG]
                                    ?: ""
                            )
                        )
                    }

                    is SaveDestinationEdit.ODOMETER -> {

                        viewModel.saveFleetData(
                            odometer = save.odometer,
                            path = save.photoUpdate.path,
                            changePhoto = save.photoUpdate.isNew,
                            destinationSeq = save.destinationSeq,
                            target = TripTarget.DESTINATION,
                            deletePhoto = save.photoUpdate.deletePhoto,
                            progressTranslate = TripWsProgress(
                                process = WS_TRIP_SAVE_FLEET,
                                title = hmAuxTranslate[PROGRESS_FLEET_TRIP_SEND_TTL] ?: "",
                                message = hmAuxTranslate[PROGRESS_FLEET_TRIP_SEND_MSG] ?: "",
                            )
                        )
                    }

                    is SaveDestinationEdit.ALL -> {


                        chainDestinationAndFleet = SaveDestinationEdit.ODOMETER(
                            odometer = save.odometer,
                            photoUpdate = save.photoUpdate,
                            destinationSeq = save.destinationSeq,
                        )

                        viewModel.saveDestinationDate(
                            dateStart = save.dateStart,
                            dateEnd = save.dateEnd,
                            destinationSeq = save.destinationSeq,
                            chainDestinationAndFleet = chainDestinationAndFleet,
                            progressTranslate = TripWsProgress(
                                process = WS_TRIP_DESTINATION_EDIT_CHAIN,
                                title = hmAuxTranslate[PROGRESS_FLEET_TRIP_SEND_TTL] ?: "",
                                message = hmAuxTranslate[PROGRESS_FLEET_TRIP_SEND_MSG] ?: "",
                            ),
                            progressTranslateFleet = TripWsProgress(
                                WS_TRIP_DESTINATION_EDIT_DATE,
                                hmAuxTranslate[TripTranslate.PROGRESS_TRIP_DESTINATION_EDIT_TLL]
                                    ?: "",
                                hmAuxTranslate[TripTranslate.PROGRESS_TRIP_DESTINATION_EDIT_MSG]
                                    ?: ""
                            )
                        )

                    }

                    is SaveDestinationEdit.NOTHING -> {}
                }
            },
            onOpenCamera = { id, path ->
                callCamera(id, path)
            }
        )
        dialogActive?.show()
    }

    fun saveFleetData() {

        viewModel.saveFleetData(
            odometer = chainDestinationAndFleet.odometer,
            path = chainDestinationAndFleet.photoUpdate.path,
            changePhoto = chainDestinationAndFleet.photoUpdate.isNew,
            destinationSeq = chainDestinationAndFleet.destinationSeq,
            target = TripTarget.DESTINATION,
            progressTranslate = TripWsProgress(
                process = WS_TRIP_SAVE_FLEET,
                title = hmAuxTranslate[PROGRESS_FLEET_TRIP_SEND_TTL]!!,
                message = hmAuxTranslate[PROGRESS_FLEET_TRIP_SEND_MSG]!!
            )
        )
    }

    companion object {
        const val EXTRACT_RESOURCE = "extract_screen_resource"
        const val EXTRACT_FILTER_LBL = "extract_filter_lbl"
        const val EXTRACT_TRIP_TITLE_LBL = "extract_trip_title_lbl"
        const val EXTRACT_CARD_NOT_FINALIZED_LBL = "extract_card_not_finalized_lbl"
        const val EXTRACT_CARD_EVENT_LBL = "extract_card_event_lbl"
        const val EXTRACT_CARD_EVENT_COST_LBL = "extract_card_event_cost_lbl"
        const val EXTRACT_CARD_USER_CURRENT_TRIP_LBL = "extract_card_user_current_trip_lbl"
        const val EXTRACT_CARD_START_TRIP_LBL = "extract_card_start_trip_lbl"
        const val EXTRACT_CARD_ORIGIN_GPS_LBL = "extract_card_origin_gps_lbl"
        const val EXTRACT_CARD_USER_RESPONSIBLE_LBL = "extract_card_user_responsible_lbl"
        const val EXTRACT_CARD_PLATE_FLEET_LBL = "extract_card_plate_fleet_lbl"
        const val EXTRACT_CARD_ODOMETER_LBL = "extract_card_odometer_lbl"
        const val EXTRACT_CARD_KM_LBL = "extract_card_km_lbl"
        const val EXTRACT_CARD_NOT_INFORMED_LBL = "extract_card_not_informed_lbl"
        const val EXTRACT_CARD_OVER_NIGHT_LBL = "extract_card_over_night_lbl"
        const val EXTRACT_EMPTY_LIST_LBL = "extract_empty_list_lbl"
        const val ALERT_PDF_NOT_FOUND_TTL = "alert_pdf_not_found_ttl"
        const val ALERT_PDF_NOT_FOUND_MSG = "alert_pdf_not_found_msg"
        private fun Context.getExtractResource(): String = ToolBox_Inf.getResourceCode(
            this,
            MODULE_CODE,
            EXTRACT_RESOURCE
        )


        fun loadTranslation(context: Context): HMAux {
            listOf(
                EXTRACT_FILTER_LBL,
                EXTRACT_TRIP_TITLE_LBL,
                EXTRACT_CARD_NOT_FINALIZED_LBL,
                EXTRACT_CARD_EVENT_LBL,
                EXTRACT_CARD_USER_CURRENT_TRIP_LBL,
                EXTRACT_CARD_ORIGIN_GPS_LBL,
                EXTRACT_CARD_USER_RESPONSIBLE_LBL,
                EXTRACT_CARD_PLATE_FLEET_LBL,
                EXTRACT_CARD_ODOMETER_LBL,
                EXTRACT_CARD_ODOMETER_LBL,
                EXTRACT_CARD_KM_LBL,
                EXTRACT_CARD_OVER_NIGHT_LBL,
                ALERT_PDF_NOT_FOUND_TTL,
                ALERT_PDF_NOT_FOUND_MSG,
                EXTRACT_EMPTY_LIST_LBL,
                EXTRACT_CARD_START_TRIP_LBL
            ).let { list ->
                return TranslateResource(
                    context = context,
                    mResoure_code = context.getExtractResource()
                ).setLanguage(list)
            }
        }

        const val WS_DESTINATION_EDIT = "ws_destination_edit"
    }

}