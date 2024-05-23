package com.namoadigital.prj001.ui.act005.trip.fragment.transit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.lifecycleScope
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.FrgTransitTripBinding
import com.namoadigital.prj001.extensions.callNavigationIntent
import com.namoadigital.prj001.extensions.callPhoneIntent
import com.namoadigital.prj001.extensions.getFormattedAddress
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripBaseFragment
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_NO_NAVIGATION_APP_FOUND_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_NO_NAVIGATION_APP_FOUND_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.component.notification.TripNotification
import com.namoadigital.prj001.ui.act005.trip.fragment.component.notification.closeNotification
import com.namoadigital.prj001.ui.act005.trip.fragment.component.notification.showNotification
import com.namoadigital.prj001.view.dialog.DestinationDetailDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class TripTransitFragment : TripBaseFragment<FrgTransitTripBinding>() {

    override lateinit var binding:FrgTransitTripBinding

    override fun showGPSWarning(isVisible: Int) {
        binding.cardPositionAlert.root.visibility = isVisible
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FrgTransitTripBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVars()
        initAction()
        viewModel.state.onEach { tripState ->
            tripState.trip?.let {
                binding.apply {
                    //
                    tvTripLbl.text =
                        """${hmAuxTranslate[TripTranslate.TRIP_LBL]} ${it.tripPrefix}.${it.tripCode}"""
                    //
                    tvTripStatusVal.text = hmAuxTranslate[TripTranslate.TRIP_TRANSIT_LBL]}
                    //
                }
            //
            tripState.destination?.let {
                binding.apply {
                    btnDestinationCall.visibility = View.GONE
                    btnMapNavigation.visibility = View.GONE
                    it.contactName?.let{
                        btnDestinationCall.visibility = View.VISIBLE
                    }

                    if (it.getFullAddress().isNotEmpty()) {
                        btnMapNavigation.visibility = View.VISIBLE
                    }
                }
            }
            binding.llDestinationInfo.root.visibility = View.VISIBLE
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
                }.launchIn(CoroutineScope(Dispatchers.Main + SupervisorJob()))

            } ?: run {
                binding.cardEvent.closeNotification()
            }

        }.launchIn(lifecycleScope)
    }

    private fun initAction() {

        binding.apply {
            btnReport.setOnClickListener {
                showBottomSheet()
            }
            btnDestinationCall.setOnClickListener {
                viewModel.state.value.destination?.let{ destination ->
                    context?.callPhoneIntent(
                        "tel:${destination.contactPhone}",
                        hmAuxTranslate[DestinationDetailDialog.ALERT_NO_CONTACT_APP_FOUND_TTL]!!,
                        hmAuxTranslate[DestinationDetailDialog.ALERT_NO_CONTACT_APP_FOUND_TTL]!!
                    )
                }
            }
            //
            btnMapNavigation.setOnClickListener {
                viewModel.state.value.destination?.let{ destination ->
                    context?.callNavigationIntent(
                        "geo:${destination.latitude},${destination.longitude}?q=${getFormattedAddress(destination.getAddress())}",
                        hmAuxTranslate[ALERT_NO_NAVIGATION_APP_FOUND_TTL]!!,
                        hmAuxTranslate[ALERT_NO_NAVIGATION_APP_FOUND_MSG]!!
                    )
                }
            }
            //
            llFooter.btnFilledRightAction.setOnClickListener {
                showConfirmDialog(
                    hmAuxTranslate[ALERT_CONFIRM_TRANSIT_TRIP_TTL],
                    hmAuxTranslate[ALERT_CONFIRM_TRANSIT_TRIP_MSG],
                    onConfirm = {
                        callDestinationChangeStatus(DestinationStatus.ARRIVED)
                    }
                )
            }
        }
    }

    private fun initVars() {
        headerBinding.llTrip.isEnabled = false
        binding.apply {
            //
            llFooter.apply {
                btnFilledRightAction.visibility = View.VISIBLE
                btnFilledRightAction.isEnabled = true
                btnFilledRightAction.icon =
                    requireContext().getDrawable(R.drawable.ic_on_site)
                        ?.let { DrawableCompat.wrap(it) }
                btnFilledRightAction.text = hmAuxTranslate[TripTranslate.TRIP_TO_ON_SITE_BTN]
            }
            btnDestinationCall.text = hmAuxTranslate[TripTranslate.TRIP_CALL_LBL]
            btnMapNavigation.text = hmAuxTranslate[TripTranslate.TRIP_MAP_LBL]
            //
            btnReport.text = hmAuxTranslate[TripTranslate.TRIP_REPORT_BTN]
        }
    }

    companion object {
        const val ALERT_CONFIRM_TRANSIT_TRIP_TTL = "alert_confirm_transit_trip_ttl"
        const val ALERT_CONFIRM_TRANSIT_TRIP_MSG = "alert_confirm_transit_trip_msg"
    }
}