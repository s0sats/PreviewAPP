package com.namoadigital.prj001.ui.act005.trip.fragment.onsite

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.namoa_digital.namoa_library.util.ConstantBase
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.core.data.domain.model.SiteInventory
import com.namoadigital.prj001.core.trip.domain.model.FormStatus
import com.namoadigital.prj001.core.trip.domain.model.TripSiteExtract
import com.namoadigital.prj001.dao.GE_Custom_FormDao
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao
import com.namoadigital.prj001.dao.MD_ProductDao
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao
import com.namoadigital.prj001.databinding.CardDestinationActionOnsiteBinding
import com.namoadigital.prj001.databinding.FrgOnSiteTripBinding
import com.namoadigital.prj001.extensions.openFormPDF
import com.namoadigital.prj001.model.GE_Custom_Form_Local
import com.namoadigital.prj001.model.MyActionFilterParam
import com.namoadigital.prj001.model.TK_Ticket
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.FsTripDestinationAction
import com.namoadigital.prj001.model.trip.TripTarget
import com.namoadigital.prj001.ui.act005.trip.fragment.base.OnFrgTripInteract
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripBaseFragment
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_PDF_NOT_FOUND_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_PDF_NOT_FOUND_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_STARTING_PDF_NOT_SUPPORTED_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_STARTING_PDF_NOT_SUPPORTED_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_ACTION_FORM_OS_BTN
import com.namoadigital.prj001.ui.act005.trip.fragment.component.notification.TripNotification
import com.namoadigital.prj001.ui.act005.trip.fragment.component.notification.closeNotification
import com.namoadigital.prj001.ui.act005.trip.fragment.component.notification.showNotification
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class TripOnSiteFragment : TripBaseFragment<FrgOnSiteTripBinding>() {

    override lateinit var binding: FrgOnSiteTripBinding
    override fun showGPSWarning(isVisible: Int) {
        binding.cardPositionAlert.root.visibility = isVisible
    }

    private var tripActionListener: OnFrgTripInteract? = null
    private val onSiteViewModel: TripOnSiteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FrgOnSiteTripBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVars()
        setAction()
        viewModel.state.onEach { tripState ->
            binding.apply {
                tripState.trip?.let {
                    //
                    tvTripLbl.text =
                        """${hmAuxTranslate[TripTranslate.TRIP_LBL]} ${it.tripPrefix}.${it.tripCode}"""
                    //
                    tvTripStatusVal.text = hmAuxTranslate[TripTranslate.TRIP_ON_SITE_LBL]
                    //
                    val nextDestination = onSiteViewModel.getNextDestination(
                        ToolBox_Con.getPreference_Customer_Code(requireContext()),
                        it
                    )
                    //
                    cvNextDestinationPlaceholder.visibility = View.GONE
                    //
                    llFooter.btnLeftAction.apply {
                        visibility = View.VISIBLE
                        isEnabled = true
                        text = hmAuxTranslate[TripTranslate.TRIP_NEXT_DESTINATION_LBL]
                        icon = requireContext().getDrawable(R.drawable.ic_location_on_24)
                    }

                    nextDestination?.let {
                        cvNextDestinationPlaceholder.visibility = View.VISIBLE
                        cvNextDestinationPlaceholder.apply {
                            layoutNextDestinationPlaceholder.tvPlaceholderLbl.text =
                                hmAuxTranslate[TripTranslate.TRIP_NEXT_DESTINATION_LBL]

                            layoutNextDestinationPlaceholder.tvPlaceholderVal.text =
                                if (it.isTicket) it.getAddress()
                                else it.destinationSiteDesc
                        }
                        llFooter.btnLeftAction.visibility = View.GONE
                    }
                }
                //
                tripState.destination?.let { destination ->
                    if (!onSiteViewModel.checkDepartedAvailability(
                            destination,
                            viewModel.state.value.trip?.isRequireDestinationFleetData == true
                        )
                    ) {
                        llFooter.btnRightAction.isEnabled = false
                        //
                        cardOdometer.root.visibility = View.VISIBLE
                        cardOdometer.cardEvent.setCardBackgroundColor(
                            ColorStateList.valueOf(
                                ResourcesCompat.getColor(
                                    resources,
                                    R.color.m3_namoa_errorContainer,
                                    null
                                )
                            )
                        )
                        cardOdometer.tvEventMessage.text =
                            hmAuxTranslate[TripTranslate.TRIP_ON_SITE_ODOMETER_WARNING]
                        cardOdometer.tvEventTitle.text = ""
                        cardOdometer.tvEventMessage.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.m3_namoa_onErrorContainer
                            )
                        )
                        cardOdometer.tvEventTitle.visibility = View.INVISIBLE
                        cardOdometer.tvEventMessage.setTypeface(null, Typeface.BOLD)
                        cardOdometer.tvEventMessage.visibility = View.VISIBLE
                        //
                    }
                    val destinationActionList =
                        onSiteViewModel.getDestinationAction(destination)
                    val containsFormInProcess =
                        destinationActionList.find { it.formStatus == FormStatus.IN_PROCESS } != null
                    checkButtonFooter(destination, containsFormInProcess)

                    //
                    destinationBinding?.let { destinationInfoBinding ->
                        with(destinationInfoBinding) {
                            if (destination.isTicket) {
                                btnSearchSerial.visibility = View.INVISIBLE
                                btnDestinationTicket.apply {
                                    text =
                                        hmAuxTranslate[TripTranslate.TRIP_DESTINATION_TICKETS_BTN]
                                    visibility = View.VISIBLE
                                }
                            } else {
                                btnSearchSerial.apply {
                                    visibility = View.VISIBLE
                                    text =
                                        hmAuxTranslate[TripTranslate.TRIP_DESTINATION_SEARCH_SERIAL_LBL]
                                }
                                gpOnSiteInfo.visibility = View.VISIBLE
                                btnDestinationTicket.text =
                                    hmAuxTranslate[TripTranslate.TRIP_SITE_TICKETS_BTN]
                            }


                            gpOnSiteActions.visibility = View.GONE
                            if (destinationActionList.isNotEmpty()) {
                                tvTripDestinationActionsLbl.visibility =
                                    View.VISIBLE
                                tvTripDestinationActionsLbl.text =
                                    hmAuxTranslate[TripTranslate.TRIP_ON_SITE_EXECUTION_LBL]
                                gpOnSiteActions.visibility = View.VISIBLE


                                if (containsFormInProcess) {
                                    btnSearchSerial.strokeWidth = 1
                                    btnSearchSerial.backgroundTintList =
                                        ResourcesCompat.getColorStateList(
                                            requireContext().resources,
                                            R.drawable.namoa_theme_button_outlined,
                                            null
                                        )
                                    btnSearchSerial.setTextColor(
                                        ResourcesCompat.getColor(
                                            requireContext().resources,
                                            R.drawable.button_theme_primary,
                                            null
                                        )
                                    )
                                    btnSearchSerial.iconTint = ResourcesCompat.getColorStateList(
                                        requireContext().resources,
                                        R.color.m3_namoa_primary,
                                        null
                                    )

                                    btnDestinationTicket.strokeWidth = 1
                                    btnDestinationTicket.backgroundTintList =
                                        ResourcesCompat.getColorStateList(
                                            requireContext().resources,
                                            R.drawable.namoa_theme_button_outlined,
                                            null
                                        )
                                    btnDestinationTicket.setTextColor(
                                        ResourcesCompat.getColor(
                                            requireContext().resources,
                                            R.drawable.button_theme_primary,
                                            null
                                        )
                                    )
                                    btnDestinationTicket.iconTint =
                                        ResourcesCompat.getColorStateList(
                                            requireContext().resources,
                                            R.color.m3_namoa_primary,
                                            null
                                        )

                                }

                                checkButtonFooter(destination, containsFormInProcess)

                                llDestinationActions.removeAllViews()
                                destinationActionList.forEachIndexed { index, tripSiteExtract ->
                                    llDestinationActions.addView(
                                        getActionItemView(
                                            tripSiteExtract,
                                            destinationActionList.size,
                                            index == destinationActionList.size - 1
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
                llDestinationInfo.root.visibility = View.VISIBLE
            }
            //
            tripState.event?.let {
                viewModel.beforeTime.onEach { timeFormatted ->
                    binding.cardEvent.showNotification(
                        requireContext(),
                        TripNotification(
                            title = timeFormatted,
                            message = it.eventTypeDesc ?: "",
                            icon = R.drawable.ic_edit_black_24dp,
                            onClick = {
                                showDialogEvent()
                            }
                        )
                    )
                }.launchIn(lifecycleScope)

            } ?: run {
                binding.cardEvent.closeNotification()
            }

        }.launchIn(lifecycleScope)
    }

    private fun getActionItemView(
        destinationAction: TripSiteExtract<*>,
        listSize: Int,
        isLastItem: Boolean,
    ): View {
        val actionView = CardDestinationActionOnsiteBinding.inflate(LayoutInflater.from(context))
        val model = destinationAction.model
        actionView.apply {
            if (listSize == 1) {
                vLine.visibility = View.GONE
            }

            if (isLastItem) {
                vLine.visibility = View.GONE
            }

            btnOpenFormOs.text = hmAuxTranslate[TRIP_ACTION_FORM_OS_BTN]
            btnOpenTicket.text = hmAuxTranslate[TripTranslate.TRIP_ACTION_TICKET_BTN]

            val containsTicketPK: (Int?, Int?) -> Boolean = { prefix, code ->
                (prefix != null && prefix > -1) && (code != null && code > -1)
            }

            when (model) {
                is FsTripDestinationAction -> {
                    tvActionDate.text =
                        ToolBox_Inf.getMyActionStartEndDateFormated(
                            context,
                            model.dateStart,
                            model.dateEnd
                        )
                    tvActionDesc.text = model.actDesc
                    tvActionSerial.text = model.serialId
                    if (!containsTicketPK(model.ticketPrefix, model.ticketCode)) {
                        btnOpenTicket.visibility = View.GONE
                    } else {
                        btnOpenTicket.visibility = View.VISIBLE
                        btnOpenTicket.setOnClickListener {
                            ticketFlow(model.ticketPrefix!!, model.ticketCode!!)
                        }
                    }
                    btnOpenFormOs.setOnClickListener {
                        Log.d("TRIP_FILE", "destinationAction.actPDFLocal: ${model.actPDFLocal}")
                        model.actPDFLocal?.let { pdfLocal ->
                            requireContext().openFormPDF(
                                pdfLocal,
                                notFoundDialog = {
                                    ToolBox.alertMSG(
                                        context,
                                        hmAuxTranslate[ALERT_PDF_NOT_FOUND_TTL],
                                        hmAuxTranslate[ALERT_PDF_NOT_FOUND_MSG],
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
                                        hmAuxTranslate[ALERT_STARTING_PDF_NOT_SUPPORTED_TTL],
                                        hmAuxTranslate[ALERT_STARTING_PDF_NOT_SUPPORTED_MSG],
                                        null,
                                        0
                                    )
                                }
                            )
                        } ?: run {
                            model.actPDFName?.let { actPDFName ->
                                requireContext().openFormPDF(
                                    "form_$actPDFName",
                                    notFoundDialog = {
                                        ToolBox.alertMSG(
                                            context,
                                            hmAuxTranslate[ALERT_PDF_NOT_FOUND_TTL],
                                            hmAuxTranslate[ALERT_PDF_NOT_FOUND_MSG],
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
                                            hmAuxTranslate[ALERT_STARTING_PDF_NOT_SUPPORTED_TTL],
                                            hmAuxTranslate[ALERT_STARTING_PDF_NOT_SUPPORTED_MSG],
                                            null,
                                            0
                                        )
                                    }
                                )
                            } ?: run {
                                ToolBox.alertMSG(
                                    context,
                                    hmAuxTranslate[ALERT_PDF_NOT_FOUND_TTL],
                                    hmAuxTranslate[ALERT_PDF_NOT_FOUND_MSG],
                                    DialogInterface.OnClickListener { dialogInterface, i ->
                                        ToolBox_Inf.scheduleAllDownloadWorkers(context)
                                    },
                                    0
                                )
                            }

                        }
                    }
                }

                is GE_Custom_Form_Local -> {

                    if (!containsTicketPK(model.ticket_prefix, model.ticket_code)) {
                        btnOpenTicket.visibility = View.GONE
                    } else {
                        btnOpenTicket.setOnClickListener {
                            ticketFlow(model.ticket_prefix, model.ticket_code!!)
                        }
                    }
                    tvActionDate.text =
                        ToolBox_Inf.getMyActionStartEndDateFormated(
                            context,
                            destinationAction.date,
                            ""
                        )
                    tvActionDesc.text = model.custom_form_desc
                    tvActionSerial.text = model.serial_id
                    btnSaveToStyleFilled(this)

                    btnOpenFormOs.apply {
                        text =
                            if (model.custom_form_status == ConstantBase.SYS_STATUS_WAITING_SYNC) {
                                hmAuxTranslate[TRIP_ACTION_FORM_OS_BTN]
                            } else {
                                hmAuxTranslate[BTN_CONTINUE_OS]
                            }
                        setOnClickListener {
                            tripActionListener?.callActivityOs(makeBundleForm(model))
                        }
                    }
                }
            }


        }

        return actionView.root
    }

    private fun makeBundleForm(form: GE_Custom_Form_Local): Bundle {
        return Bundle().also { bundle ->
            bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT005)
            bundle.putString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW, Constant.ACT005)
            bundle.putString(MD_ProductDao.PRODUCT_CODE, form.custom_product_code.toString())
            bundle.putString(MD_ProductDao.PRODUCT_DESC, form.custom_product_desc)
            bundle.putString(MD_Product_SerialDao.SERIAL_ID, form.serial_id)
            bundle.putString(
                GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE,
                form.custom_form_type.toString()
            )
            bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_CODE, form.custom_form_code.toString())
            bundle.putString(
                GE_Custom_FormDao.CUSTOM_FORM_VERSION,
                form.custom_form_version.toString()
            )
            bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE_DESC, form.custom_form_desc)
            bundle.putString(
                GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA,
                form.custom_form_data.toString()
            )

            bundle.putString(Constant.ACT017_SCHEDULED_SITE, form.site_code.toString())
            if (form.ticket_prefix != null
                && form.ticket_code != null
            ) {
                bundle.putInt(TK_Ticket_CtrlDao.TICKET_PREFIX, form.ticket_prefix)
                bundle.putInt(TK_Ticket_CtrlDao.TICKET_CODE, form.ticket_code)
                bundle.putInt(TK_Ticket_CtrlDao.TICKET_SEQ, form.ticket_seq)
                bundle.putInt(TK_Ticket_CtrlDao.TICKET_SEQ_TMP, form.ticket_seq_tmp)
                bundle.putInt(TK_Ticket_CtrlDao.STEP_CODE, form.step_code)
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun btnSaveToStyleFilled(
        binding: CardDestinationActionOnsiteBinding
    ) {
        with(binding) {
            btnOpenFormOs.strokeWidth = 0
            btnOpenFormOs.backgroundTintList = ResourcesCompat.getColorStateList(
                context?.resources!!,
                R.drawable.button_theme_primary,
                null
            )
            btnOpenFormOs.setTextColor(
                ResourcesCompat.getColor(
                    context?.resources!!,
                    R.color.m3_namoa_surface,
                    null
                )
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFrgTripInteract) {
            tripActionListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        tripActionListener = null
    }

    private fun initVars() {
        headerBinding.llTrip.isEnabled = false
        binding.apply {
            //
            llFooter.apply {
                llFooter.btnRightAction.visibility = View.VISIBLE
                llFooter.btnRightAction.isEnabled = true
                llFooter.btnRightAction.icon =
                    requireContext().getDrawable(R.drawable.ic_site_departed)
                        ?.let { DrawableCompat.wrap(it) }
                llFooter.btnRightAction.text =
                    hmAuxTranslate[TripTranslate.TRIP_DEPARTED_LBL]
            }
            //
            btnReport.text = hmAuxTranslate[TripTranslate.TRIP_REPORT_BTN]
        }
        tripActionListener?.updateFooter()

    }

    private fun setAction() {
        binding.apply {
            btnReport.setOnClickListener {
                showBottomSheet()
            }
            //
            llFooter.apply {
                btnLeftAction.setOnClickListener {
                    tripActionListener?.addDestination()
                }
                //
                btnRightAction.setOnClickListener {
                    showConfirmDialog(
                        hmAuxTranslate[ALERT_CONFIRM_DEPARTED_TRIP_TTL],
                        hmAuxTranslate[ALERT_CONFIRM_DEPARTED_TRIP_MSG],
                        onConfirm = {
                            callDestinationChangeStatus(DestinationStatus.DEPARTED)
                        }
                    )
                }
                //
            }
            //
            cardOdometer.root.setOnClickListener {
                showDialogInfoFleet(TripTarget.DESTINATION, viewModel.getDestinationSeq())
            }
            //
            cardOdometer.iconButton.setOnClickListener {
                showDialogInfoFleet(TripTarget.DESTINATION, viewModel.getDestinationSeq())
            }
            destinationBinding?.btnDestinationTicket?.setOnClickListener {
                //
                val destination = viewModel.state.value.destination
                selectTicket(destination!!)
            }
            destinationBinding?.btnSearchSerial?.setOnClickListener {
                tripActionListener?.callSerialSearch()
            }
            cvNextDestinationPlaceholder.setOnClickListener {
                viewModel.state.value.trip?.let {
                    callDestinationChangeStatus(
                        DestinationStatus.CANCELLED,
                        onSiteViewModel.getNextDestination(
                            ToolBox_Con.getPreference_Customer_Code(requireContext()),
                            it
                        )
                    )
                }
            }
        }
    }


    private fun selectTicket(destination: FsTripDestination) {
        if (destination.isTicket) {
            ticketFlow(destination.ticketPrefix ?: 0, destination.ticketCode ?: 0)
        } else {
            val destinationSiteCode = viewModel.state.value.destination?.destinationSiteCode
            val destinationSiteDesc = viewModel.state.value.destination?.destinationSiteDesc
            //
            destinationSiteCode?.let {
                val actionFilterParam = MyActionFilterParam(
                    siteCode = "$it"
                )
                actionFilterParam.paramItemSelectedTab = 1
                tripActionListener?.callTripActions(
                    actionFilterParam,
                    SiteInventory(destinationSiteCode, destinationSiteDesc ?: "", true)
                )
            }
        }
    }

    private var saveTicketPk: Pair<Int, Int>? = null //Pair(Ticket-Prefix, Ticket-Code)
    fun ticketFlow(
        ticketPrefix: Int? = null,
        ticketCode: Int? = null
    ) {

        var ticket: TK_Ticket? = null

        saveTicketPk?.let { params ->
            val (prefix, code) = params
            ticket = onSiteViewModel.getTicket(
                prefix,
                code,
            )
        } ?: run {
            ticket = onSiteViewModel.getTicket(
                ticketPrefix!!,
                ticketCode!!,
            )
        }



        if (ticket == null) {
            saveTicketPk = Pair(ticketPrefix!!, ticketCode!!)
            listener?.callTripWS(
                WS_TRIP_DOWNLOAD_TICKET,
                hmAuxTranslate[ALERT_DOWNLOAD_TICKET_TTL] ?: "",
                hmAuxTranslate[ALERT_DOWNLOAD_TICKET_MSG] ?: ""
            )
            onSiteViewModel.downloadTicket(
                ticketPrefix,
                ticketCode
            )
            return
        }


        tripActionListener?.callActivityTicket(
            prepareOpenTicket(
                ticket!!.ticket_prefix,
                ticket!!.ticket_code
            )
        )

    }

    private fun checkButtonFooter(
        destination: FsTripDestination,
        formInProcess: Boolean
    ) {
        if (onSiteViewModel.checkDepartedAvailability(
                destination,
                viewModel.state.value.trip?.isRequireDestinationFleetData == true
            )
        ) {
            binding.llFooter.btnRightAction.isEnabled = !formInProcess
            binding.cardOdometer.root.visibility = View.GONE
        }
    }

    private fun prepareOpenTicket(
        prefix: Int,
        code: Int
    ): Bundle {
        return Bundle().also { bundle ->
            bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT005)
            bundle.putString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW, ConstantBaseApp.ACT005)
            bundle.putInt(TK_TicketDao.TICKET_PREFIX, prefix)
            bundle.putInt(TK_TicketDao.TICKET_CODE, code)
        }
    }

    companion object {
        const val ALERT_CONFIRM_DEPARTED_TRIP_TTL = "alert_confirm_departed_trip_ttl"
        const val ALERT_CONFIRM_DEPARTED_TRIP_MSG = "alert_confirm_departed_trip_msg"
        const val ALERT_DOWNLOAD_TICKET_TTL = "alert_download_ticket_ttl"
        const val ALERT_DOWNLOAD_TICKET_MSG = "alert_download_ticket_msg"
        const val WS_TRIP_DOWNLOAD_TICKET = "ws_trip_download_ticket"
        const val BTN_CONTINUE_OS = "btn_continue_os"
    }
}
