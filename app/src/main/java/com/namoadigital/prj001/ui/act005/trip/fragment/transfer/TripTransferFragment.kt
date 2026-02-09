package com.namoadigital.prj001.ui.act005.trip.fragment.transfer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.lifecycleScope
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.FrgTransferTripBinding
import com.namoadigital.prj001.extensions.getColorStateListId
import com.namoadigital.prj001.model.trip.TripStatus
import com.namoadigital.prj001.model.trip.TripTarget
import com.namoadigital.prj001.service.location.FsTripLocationService
import com.namoadigital.prj001.ui.act005.trip.fragment.base.OnFrgTripInteract
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripBaseFragment
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_TRIP_OVER_NIGHT_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_TRIP_OVER_NIGHT_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripWsProgress
import com.namoadigital.prj001.ui.act005.trip.fragment.component.notification.TripNotification
import com.namoadigital.prj001.ui.act005.trip.fragment.component.notification.closeNotification
import com.namoadigital.prj001.ui.act005.trip.fragment.component.notification.showNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class TripTransferFragment : TripBaseFragment<FrgTransferTripBinding>() {
    override lateinit var binding: FrgTransferTripBinding
    protected var tripActionListener: OnFrgTripInteract? = null
    override fun showGPSWarning(isVisible: Int) {
        binding.cardPositionAlert.root.visibility = isVisible
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FrgTransferTripBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        headerBinding.llTrip.isEnabled = false
        //
        viewModel.state.onEach { tripState ->
            tripState.trip?.let {
                binding.apply {
                    //
                    tvTripLbl.text =
                        """${hmAuxTranslate[TripTranslate.TRIP_LBL]} ${it.tripPrefix}.${it.tripCode}"""
                    //
                    tvTripStatusVal.text = hmAuxTranslate[TripTranslate.TRIP_TRANSFER_STATUS_LBL]
                }
                //
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
        //
        initVars()
        setActions()
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
        binding.apply {
            val drawable =
                requireContext().getDrawable(R.drawable.ic_transfer_placeholder)
                    ?.let { DrawableCompat.wrap(it) }
            llPlaceholder.apply {
                //
                ivPlaceholder.setImageDrawable(drawable)
                tvPlaceholderTtl.text =
                    hmAuxTranslate[TripTranslate.PLACEHOLDER_TRIP_TRANSFER_TTL_LBL]
            }
            //
            btnReport.text = hmAuxTranslate[TripTranslate.TRIP_REPORT_BTN]

            //
            llFooter.apply {
                root.visibility = View.VISIBLE

                btnRightAction.apply {
                    visibility = View.VISIBLE
                    iconTint = requireContext().getColorStateListId(R.color.m3_namoa_primary)
                    icon = requireContext().getDrawable(R.drawable.ic_location_on_24)
                    text = hmAuxTranslate[TripTranslate.TRIP_SELECT_DESTINATION_BTN]
                    isEnabled = true
                }

                btnLeftAction.visibility = View.VISIBLE
                btnLeftAction.isEnabled = true
                btnLeftAction.icon =
                    requireContext().getDrawable(R.drawable.baseline_hotel_24)
                btnLeftAction.text = hmAuxTranslate[TripTranslate.TRIP_TO_OVER_NIGHT_LBL]
                //
                btnFilledRightAction.visibility = View.VISIBLE
                btnFilledRightAction.isEnabled = true
                btnFilledRightAction.icon =
                    requireContext().getDrawable(R.drawable.ic_end_trip)
                        ?.let { DrawableCompat.wrap(it) }
                btnFilledRightAction.text = hmAuxTranslate[TripTranslate.TRIP_TO_END_LBL]
            }
        }
    }

    private fun setActions() {
        binding.apply {
            btnReport.setOnClickListener {
                showBottomSheet()
            }
            //
            //
            llFooter.apply {
                btnLeftAction.setOnClickListener {
                    showConfirmDialog(
                        hmAuxTranslate[ALERT_CONFIRM_TRANSFER_TRIP_TTL],
                        hmAuxTranslate[ALERT_CONFIRM_TRANSFER_TRIP_MSG],
                        onConfirm = {
                            callOverNightTrip()
                        }
                    )
                }
                //
                btnRightAction.setOnClickListener {
                    tripActionListener?.addDestination()
                }
                //
                btnFilledRightAction.setOnClickListener {
                    val state = viewModel.state.value
                    if (state.containsEvent) {
                        showConfirmDialog(
                            hmAuxTranslate[ALERT_CONTAINS_EVENT_TTL],
                            hmAuxTranslate[ALERT_CONTAINS_EVENT_MSG],
                            0,
                            onConfirm = {}
                        )
                    } else {
                        showConfirmDialog(
                            hmAuxTranslate[ALERT_CONFIRM_END_TRIP_TTL],
                            hmAuxTranslate[ALERT_CONFIRM_END_TRIP_MSG],
                            onConfirm = {
                                showDialogInfoFleet(TripTarget.END)
                            }
                        )
                    }
                }
            }
            //
        }
    }

    private fun callOverNightTrip() {
        if (FsTripLocationService.LatLog.value.latitude != null
            && FsTripLocationService.LatLog.value.longitude != null
        ) {
            viewModel.addDestinationOverNight(
                hmAuxTranslate[PROGRESS_TRIP_OVER_NIGHT_TTL],
                hmAuxTranslate[PROGRESS_TRIP_OVER_NIGHT_MSG],
            )
        } else {
            ToolBox.alertMSG(
                context,
                hmAuxTranslate[ALERT_GPS_POSITION_NOT_FOUND_TTL],
                hmAuxTranslate[ALERT_GPS_POSITION_NOT_FOUND_MSG],
                null,
                0
            )
        }
    }

    fun callEndTrip() {
        //
        viewModel.setTripStatus(
            TripStatus.DONE,
            endDate = viewModel.state.value.endTripDate,
            tripWsProgress = TripWsProgress(
                process = WS_TRIP_END,
                title = hmAuxTranslate[TripTranslate.PROGRESS_TRIP_END_TTL]!!,
                message = hmAuxTranslate[TripTranslate.PROGRESS_TRIP_END_MSG]!!,
            )
        )
    }

    companion object {
        const val ALERT_CONFIRM_TRANSFER_TRIP_TTL = "alert_confirm_transfer_trip_ttl"
        const val ALERT_CONFIRM_TRANSFER_TRIP_MSG = "alert_confirm_transfer_trip_msg"
        const val ALERT_CONFIRM_END_TRIP_TTL = "alert_confirm_end_trip_ttl"
        const val ALERT_CONFIRM_END_TRIP_MSG = "alert_confirm_end_trip_msg"
        const val ALERT_CONTAINS_EVENT_TTL = "alert_contains_event_ttl"
        const val ALERT_CONTAINS_EVENT_MSG = "alert_contains_event_msg"
        const val ALERT_GPS_POSITION_NOT_FOUND_TTL = "alert_gps_position_not_found_ttl"
        const val ALERT_GPS_POSITION_NOT_FOUND_MSG = "alert_gps_position_not_found_msg"

    }

}