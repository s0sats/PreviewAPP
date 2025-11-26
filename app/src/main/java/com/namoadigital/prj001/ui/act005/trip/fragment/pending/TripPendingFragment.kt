package com.namoadigital.prj001.ui.act005.trip.fragment.pending

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.namoa_digital.namoa_library.R.color
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.FrgPendingTripBinding
import com.namoadigital.prj001.extensions.date.FormatDateType
import com.namoadigital.prj001.extensions.date.formatDate
import com.namoadigital.prj001.extensions.showMaterialAlert
import com.namoadigital.prj001.model.trip.TripOrigin
import com.namoadigital.prj001.model.trip.TripStatus
import com.namoadigital.prj001.model.trip.TripTarget
import com.namoadigital.prj001.ui.act005.Act005_Main
import com.namoadigital.prj001.ui.act005.trip.fragment.base.OnFrgTripInteract
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripBaseFragment
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_TRIP_START_ERROR_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_TRIP_START_ERROR_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripWsProgress
import com.namoadigital.prj001.ui.act005.trip.fragment.component.notification.TripNotification
import com.namoadigital.prj001.ui.act005.trip.fragment.component.notification.closeNotification
import com.namoadigital.prj001.ui.act005.trip.fragment.component.notification.showNotification
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class TripPendingFragment : TripBaseFragment<FrgPendingTripBinding>() {

    override lateinit var binding: FrgPendingTripBinding
    protected var tripActionListener: OnFrgTripInteract? = null
    private val pendingViewModel: TripPendingViewModel by viewModels()

    override fun showGPSWarning(isVisible: Int) {
        binding.cardPositionAlert.root.visibility = isVisible
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FrgPendingTripBinding.inflate(inflater, container, false)

        (activity as Act005_Main).registerGpsReceiver()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVars()
        setActions()

        viewModel.state.onEach { tripState ->
            tripState.trip?.let {
                binding.apply {
                    //
                    tvTripLbl.text =
                        """${hmAuxTranslate[TripTranslate.TRIP_LBL]} ${it.tripPrefix}.${it.tripCode}"""
                    //
                    llTripOrigin.apply {
                        it.originDate?.let { date ->
                            tvLeftVal.text =
                                requireContext().formatDate(FormatDateType.DateAndHour(date))
                        }
                        //
                        val originDesc = if (it.originType == TripOrigin.SITE.toString()) {
                            it.originSiteDesc
                        } else {
                            hmAuxTranslate[TripTranslate.TRIP_ORIGIN_GPS]
                        }
                        tvCenterVal.text = originDesc
                    }
                    //
                    if (it.fleetLicencePlate.isNullOrBlank() && it.fleetStartOdometer == null) {
                        llTripFleet.root.visibility = View.INVISIBLE
                        btnEditTripFleet.visibility = View.VISIBLE
                        styleButtonDestination(style = StyleDestination.OUTLINED)
                    } else {
                        styleButtonDestination(style = StyleDestination.FILLED)
                        llTripFleet.root.visibility = View.VISIBLE
                        btnEditTripFleet.visibility = View.INVISIBLE
                        //
                        llTripFleet.tvLeftVal.text = it.fleetLicencePlate
                        llTripFleet.tvCenterVal.text = it.fleetStartOdometer?.toString() ?: ""
                        //
                    }
                    //
                    llFooter.apply {
                        val isEnable =
                            pendingViewModel.checkStartTrip(it, tripState.destination)
                        btnRightAction.isEnabled = isEnable
                        btnFilledRightAction.isEnabled = isEnable
                    }
                }
            }
            //
            if (tripState.destination != null) {
                binding.btnAddDestination.visibility = View.GONE
                binding.llDestinationInfo.root.visibility = View.VISIBLE
                binding.llDestinationInfo.dashboardLayout.isVisible = false
            } else {
                binding.btnAddDestination.visibility = View.VISIBLE
                binding.llDestinationInfo.root.visibility = View.GONE
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
                root.visibility = View.VISIBLE
                btnLeftAction.visibility = View.VISIBLE
                btnLeftAction.text = hmAuxTranslate[TripTranslate.TRIP_CANCEL_LBL]
                btnLeftAction.icon =
                    requireContext().getDrawable(R.drawable.ic_close_circle_black_24dp)

                btnFilledRightAction.icon =
                    requireContext().getDrawable(R.drawable.ic_start_destination_trip)

                btnRightAction.icon =
                    requireContext().getDrawable(R.drawable.ic_start_destination_trip)

                if (viewModel.state.value.destination != null) {
                    btnFilledRightAction.visibility = View.VISIBLE
                    btnFilledRightAction.isEnabled = true
                    btnFilledRightAction.text = hmAuxTranslate[TripTranslate.TRIP_START_LBL]
                } else {
                    btnRightAction.visibility = View.VISIBLE
                    btnRightAction.isEnabled = true
                    btnRightAction.text = hmAuxTranslate[TripTranslate.TRIP_START_LBL]
                }
            }
            //
            llTripOrigin.apply {
                tvLeftLbl.text = hmAuxTranslate[TripTranslate.TRIP_CREATE_AT_LBL]
                tvCenterLbl.text = hmAuxTranslate[TripTranslate.TRIP_ORIGIN_POINT_LBL]
            }
            //
            llTripFleet.apply {
                //
                tvLeftLbl.text = hmAuxTranslate[TripTranslate.TRIP_FLEET_PLATE_LBL]
                tvCenterLbl.text = hmAuxTranslate[TripTranslate.TRIP_FLEET_KILOMETERS_LBL]
                //
                root.visibility = View.INVISIBLE
                btnEditTripFleet.visibility = View.VISIBLE
                btnEditTripFleet.text = hmAuxTranslate[TripTranslate.TRIP_FLEET_EDIT_BTN]
            }
            btnAddDestination.text = hmAuxTranslate[TripTranslate.TRIP_PLAN_DESTINATION_BTN]
            //
            btnReport.text = hmAuxTranslate[TripTranslate.TRIP_REPORT_BTN]
        }
    }

    fun setActions() {

        binding.apply {
            btnReport.setOnClickListener {
                showBottomSheet()
            }
            //
            llTripOrigin.apply {
                this.btnEdit.setOnClickListener {
                    showOriginDialog()
                }
            }
            //
            llTripFleet.apply {
                btnEdit.setOnClickListener {
                    showDialogInfoFleet(TripTarget.START)
                }
            }
            //
            btnEditTripFleet.apply {
                setOnClickListener {
                    showDialogInfoFleet(TripTarget.START)
                }
            }
            //
            llFooter.apply {
                btnLeftAction.setOnClickListener {
                    context?.showMaterialAlert(
                        title = hmAuxTranslate[TripTranslate.ALERT_ABORT_TRIP_TTL] ?: "",
                        msg = hmAuxTranslate[TripTranslate.ALERT_ABORT_TRIP_MSG] ?: "",
                        actionPositiveLbl = hmAuxTranslate[TripTranslate.ALERT_TRIP_ABORT_CONFIRM_BTN]
                            ?: "",
                        actionNeutralLbl = hmAuxTranslate[TripTranslate.ALERT_TRIP_ABORT_CANCEL_BTN]
                            ?: "",
                        actionPositive = { _, _ ->
                            viewModel.state.value.trip?.let {
                                if (!it.hasUpdateRequired
                                    && ToolBox_Con.isOnline(context)
                                ) {
                                    viewModel.setTripStatus(
                                        TripStatus.CANCELLED,
                                        tripWsProgress = TripWsProgress(
                                            process = WS_TRIP_ABORT_PENDING,
                                            title = hmAuxTranslate[TripTranslate.PROGRESS_ABORT_PENDING_TTL]!!,
                                            message = hmAuxTranslate[TripTranslate.PROGRESS_ABORT_PENDING_MSG]!!,
                                        )
                                    )
                                } else {
                                    ToolBox_Inf.showNoConnectionDialog(
                                        context,
                                    )
                                }
                            }

                        },
                        actionNeutral = { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }
                    )?.show()
                }
                //
                btnRightAction.setOnClickListener {
                    showConfirmDialog(
                        hmAuxTranslate[ALERT_CONFIRM_START_TRIP_TTL],
                        hmAuxTranslate[ALERT_CONFIRM_START_TRIP_MSG],
                        onConfirm = {
                            startTripClickListener()
                        }
                    )
                }
                //
                btnFilledRightAction.setOnClickListener {
                    showConfirmDialog(
                        hmAuxTranslate[ALERT_CONFIRM_START_TRIP_TTL],
                        hmAuxTranslate[ALERT_CONFIRM_START_TRIP_MSG],
                        onConfirm = {
                            startTripClickListener()
                        }
                    )
                }
                //
            }
            //
            btnAddDestination.setOnClickListener {
                tripActionListener?.addDestination()
            }
        }
    }

    private fun startTripClickListener() {
        val trip = viewModel.state.value.trip
        val destination = viewModel.state.value.destination
        if (pendingViewModel.checkStartTrip(
                trip,
                destination
            )
        ) {
            //
            viewModel.setTripStatus(
                TripStatus.START,
                tripWsProgress = TripWsProgress(
                    process = WS_TRIP_START,
                    title = hmAuxTranslate[TripTranslate.PROGRESS_TRIP_START_TTL]!!,
                    message = hmAuxTranslate[TripTranslate.PROGRESS_TRIP_START_MSG]!!,
                )
            )
            //
        } else {
            ToolBox.alertMSG(
                context,
                hmAuxTranslate[ALERT_TRIP_START_ERROR_TTL],
                hmAuxTranslate[ALERT_TRIP_START_ERROR_MSG],
                null,
                0
            )
        }
    }


    private sealed class StyleDestination(
        @ColorRes val background: Int,
        @ColorRes val contentColor: Int
    ) {
        object OUTLINED : StyleDestination(
            color.m3_namoa_surface,
            R.color.m3_namoa_primary
        )

        object FILLED : StyleDestination(
            R.color.m3_namoa_primary,
            color.m3_namoa_surface
        )
    }

    private fun styleButtonDestination(style: StyleDestination) = with(binding.btnAddDestination) {
        val background = ResourcesCompat.getColorStateList(resources, style.background, null)
        val content = ResourcesCompat.getColorStateList(resources, style.contentColor, null)

        backgroundTintList = background
        iconTint = content
        setTextColor(content)
    }

    companion object {
        const val ALERT_CONFIRM_START_TRIP_TTL = "alert_confirm_start_trip_ttl"
        const val ALERT_CONFIRM_START_TRIP_MSG = "alert_confirm_start_trip_msg"
    }
}