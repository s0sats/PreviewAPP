package com.namoadigital.prj001.ui.act005.trip.fragment.overnight

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.lifecycleScope
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.FrgOverNightTripBinding
import com.namoadigital.prj001.extensions.getDrawableId
import com.namoadigital.prj001.extensions.hasLocationPermission
import com.namoadigital.prj001.extensions.isGpsEnabled
import com.namoadigital.prj001.extensions.showMaterialAlert
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.ui.act005.trip.fragment.base.OnFrgTripInteract
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripBaseFragment
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripWsProgress
import com.namoadigital.prj001.ui.act005.trip.fragment.component.notification.TripNotification
import com.namoadigital.prj001.ui.act005.trip.fragment.component.notification.closeNotification
import com.namoadigital.prj001.ui.act005.trip.fragment.component.notification.showNotification
import com.namoadigital.prj001.ui.act005.trip.fragment.home.TripHomeFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class TripOverNightFragment : TripBaseFragment<FrgOverNightTripBinding>() {
    override lateinit var binding: FrgOverNightTripBinding
    protected var tripActionListener: OnFrgTripInteract? = null

    override fun showGPSWarning(isVisible: Int) {
        tripCardWarningBinding?.cardWarning?.visibility = View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FrgOverNightTripBinding.inflate(inflater, container, false)
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
                        "${hmAuxTranslate[TripTranslate.TRIP_LBL]} ${it.tripPrefix}.${it.tripCode}"
                    //
                    tvTripStatusVal.text = hmAuxTranslate[TripTranslate.TRIP_OVER_NIGHT_LBL]
                }
                //
            }
            //
            binding.apply {
                tripState.destination?.let {
                    btnAddDestination.visibility = View.GONE
                    cvNextDestinationPlaceholder.visibility = View.VISIBLE
                    cvNextDestinationPlaceholder.apply {
                        layoutNextDestinationPlaceholder.tvPlaceholderLbl.text =
                            hmAuxTranslate[TripTranslate.TRIP_NEXT_DESTINATION_LBL]
                        layoutNextDestinationPlaceholder.tvPlaceholderVal.text =
                            if (it.isTicket) it.getAddress()
                            else it.destinationSiteDesc
                    }
                    llFooter.apply {
                        root.visibility = View.VISIBLE
                        btnFilledRightAction.apply {
                            visibility = View.VISIBLE
                            isEnabled = true
                            icon = context.getDrawableId(R.drawable.ic_baseline_directions_car_24)
                            text = hmAuxTranslate[TripTranslate.TRIP_RETURN_TO_TRANSIT_LBL]
                            iconTint = ResourcesCompat.getColorStateList(
                                context.resources,
                                com.namoa_digital.namoa_library.R.color.padrao_WHITE,
                                null
                            )
                        }
                    }
                } ?: run {
                    btnAddDestination.visibility = View.VISIBLE
                    cvNextDestinationPlaceholder.visibility = View.GONE
                    llFooter.apply {
                        btnFilledRightAction.visibility = View.GONE
                        root.visibility = View.VISIBLE
                        btnLeftAction.apply {
                            visibility = View.VISIBLE
                            isEnabled = true
                            text = hmAuxTranslate[TripTranslate.TRIP_TO_END_OVER_NIGHT_LBL]
                            icon = context.getDrawableId(R.drawable.baseline_home_repair_service_24)
                            iconTint = ResourcesCompat.getColorStateList(
                                context.resources,
                                R.color.m3_namoa_primary,
                                null
                            )
                        }
                    }
                }
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
                requireContext().getDrawable(R.drawable.ic_over_night_placeholder)
                    ?.let { DrawableCompat.wrap(it) }
            llPlaceholder.apply {
                //
                ivPlaceholder.setImageDrawable(drawable)
                tvPlaceholderTtl.text =
                    hmAuxTranslate[TripTranslate.PLACEHOLDER_TRIP_OVER_NIGHT_TTL]
            }
            //
            btnReport.text = hmAuxTranslate[TripTranslate.TRIP_REPORT_BTN]
            btnAddDestination.text = hmAuxTranslate[TripTranslate.TRIP_PLAN_DESTINATION_BTN]
            //
        }
    }

    private fun checkGpsOrChangeDestinationStatus() {
        if (!requireContext().isGpsEnabled()) {
            requireContext().showMaterialAlert(
                title = hmAuxTranslate[TripHomeFragment.TRIP_GPS_OFF_TTL]!!,
                msg = hmAuxTranslate[TripHomeFragment.TRIP_GPS_OFF_MSG]!!,
                actionPositiveLbl = hmAuxTranslate[TripHomeFragment.TRIP_GPS_OFF_ACTIVE_BTN],
                actionNeutralLbl = hmAuxTranslate[TripHomeFragment.TRIP_GPS_OFF_CANCEL_BTN],
                actionPositive = { _, _ ->
                    requireContext().startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                },
                actionNeutral = { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
            ).show()
            return
        }

        if (!requireContext().hasLocationPermission()) {
            listener?.checkGPSPermission()
            return
        }

        returnToTrip()
    }

    fun returnToTrip() {
        showConfirmDialog(
            hmAuxTranslate[ALERT_CONFIRM_END_OVER_NIGHT_TRIP_TTL],
            hmAuxTranslate[ALERT_CONFIRM_END_OVER_NIGHT_TRIP_MSG],
            onConfirm = {
                callDepartedFromOvernight()
            }
        )
    }

    private fun setActions() {
        binding.apply {
            btnReport.setOnClickListener {
                showBottomSheet()
            }
            btnAddDestination.setOnClickListener {
                tripActionListener?.addDestination()
            }
            llFooter.btnFilledRightAction.setOnClickListener {
                checkGpsOrChangeDestinationStatus()
            }
            llFooter.btnLeftAction.setOnClickListener {
                checkGpsOrChangeDestinationStatus()
            }
            cvNextDestinationPlaceholder.setOnClickListener {
                ToolBox.alertMSG(
                    requireContext(),
                    hmAuxTranslate[TripTranslate.ALERT_TRIP_DESTINATION_DELETE_TTL],
                    hmAuxTranslate[TripTranslate.ALERT_TRIP_DESTINATION_DELETE_MSG],
                    { dialogInterface, i ->
                        dialogInterface.dismiss()
                        callDestinationChangeStatus(
                            DestinationStatus.CANCELLED,
                            viewModel.state.value.destination
                        )
                    },
                    1
                )

            }
        }
    }

    private fun callDepartedFromOvernight() {
        //
        viewModel.state.value.trip?.let {
            val overNightDestination = viewModel.getOverNightDestination(it)
            overNightDestination?.let { destination ->
                viewModel.setDestinationStatus(
                    destinationStatus = DestinationStatus.DEPARTED,
                    destination = destination,
                    TripWsProgress(
                        process = WS_TRIP_DESTINATION_CHANGE,
                        title = hmAuxTranslate[TripTranslate.PROGRESS_TRIP_DESTINATION_CHANGE_TTL]!!,
                        message = hmAuxTranslate[TripTranslate.PROGRESS_TRIP_DESTINATION_CHANGE_MSG]!!,
                    )
                )
            } ?: ToolBox.alertMSG(
                context,
                hmAuxTranslate[TripTranslate.PROGRESS_TRIP_ERROR_SYNC_APP_TTL],
                hmAuxTranslate[TripTranslate.PROGRESS_TRIP_ERROR_SYNC_APP_MSG],
                null,
                0
            )
        } ?: ToolBox.alertMSG(
            context,
            hmAuxTranslate[TripTranslate.PROGRESS_TRIP_ERROR_SYNC_APP_TTL],
            hmAuxTranslate[TripTranslate.PROGRESS_TRIP_ERROR_SYNC_APP_MSG],
            null,
            0
        )

    }

    companion object {
        const val ALERT_CONFIRM_OVER_NIGHT_TRIP_TTL = "alert_confirm_over_night_trip_ttl"
        const val ALERT_CONFIRM_OVER_NIGHT_TRIP_MSG = "alert_confirm_over_night_trip_msg"

        const val ALERT_CONFIRM_END_OVER_NIGHT_TRIP_TTL = "alert_confirm_end_over_night_trip_ttl"
        const val ALERT_CONFIRM_END_OVER_NIGHT_TRIP_MSG = "alert_confirm_end_over_night_trip_msg"
    }

}