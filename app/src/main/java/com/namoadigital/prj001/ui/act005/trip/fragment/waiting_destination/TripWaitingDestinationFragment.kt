package com.namoadigital.prj001.ui.act005.trip.fragment.waiting_destination

import android.content.Context
import com.namoadigital.prj001.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.lifecycleScope
import com.namoadigital.prj001.databinding.FrgWaitingDestinationTripBinding
import com.namoadigital.prj001.model.trip.TripStatus
import com.namoadigital.prj001.ui.act005.trip.fragment.base.OnFrgTripInteract
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripBaseFragment
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate
import com.namoadigital.prj001.ui.act005.trip.fragment.component.notification.TripNotification
import com.namoadigital.prj001.ui.act005.trip.fragment.component.notification.closeNotification
import com.namoadigital.prj001.ui.act005.trip.fragment.component.notification.showNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class TripWaitingDestinationFragment : TripBaseFragment<FrgWaitingDestinationTripBinding>() {
    override lateinit var binding: FrgWaitingDestinationTripBinding
    protected var tripActionListener: OnFrgTripInteract? = null

    override fun showGPSWarning(isVisible: Int) {
        binding.cardPositionAlert.root.visibility = isVisible
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FrgWaitingDestinationTripBinding.inflate(inflater, container, false)

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
                    tvTripStatusVal.text =
                        hmAuxTranslate[TripTranslate.TRIP_WAITING_DESTINATION_LBL]
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
                }.launchIn(CoroutineScope(Dispatchers.Main + SupervisorJob()))

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
                requireContext().getDrawable(R.drawable.ic_waiting_destination)
                    ?.let { DrawableCompat.wrap(it) }
            llPlaceholder.apply {
                //
                ivPlaceholder.setImageDrawable(drawable)
                tvPlaceholderTtl.text =
                    hmAuxTranslate[TripTranslate.PLACEHOLDER_TRIP_WAITING_DESTINATION_TTL_LBL]
                tvPlaceholderSubTtl.text =
                    hmAuxTranslate[TripTranslate.PLACEHOLDER_TRIP_WAITING_DESTINATION_SUB_TTL_LBL]
            }
            //
            btnReport.text = hmAuxTranslate[TripTranslate.TRIP_REPORT_BTN]
            btnAddDestination.text = hmAuxTranslate[TripTranslate.TRIP_SELECT_DESTINATION_BTN]
            //
            llFooter.apply {
                root.visibility = View.VISIBLE
                btnLeftAction.visibility = View.VISIBLE
                btnLeftAction.isEnabled = true
                btnLeftAction.icon =
                    requireContext().getDrawable(R.drawable.ic_transfer_trip)
                        ?.let { DrawableCompat.wrap(it) }
                btnLeftAction.text = hmAuxTranslate[TripTranslate.TRIP_TO_TRANSFER_LBL]
            }
        }
    }

    private fun setActions() {
        binding.apply {
            btnReport.setOnClickListener {
                showBottomSheet()
            }
            btnAddDestination.setOnClickListener {
                tripActionListener?.addDestination()
            }
            llFooter.btnLeftAction.setOnClickListener {
                showConfirmDialog(
                    hmAuxTranslate[ALERT_CONFIRM_WAITING_DESTINATION_TRIP_TTL],
                    hmAuxTranslate[ALERT_CONFIRM_WAITING_DESTINATION_TRIP_MSG],
                    onConfirm = {
                        listener?.callTripWS(
                            WS_TRIP_TRANSFER,
                            hmAuxTranslate[TripTranslate.PROGRESS_TRIP_TRANSFER_TTL]!!,
                            hmAuxTranslate[TripTranslate.PROGRESS_TRIP_TRANSFER_MSG]!!,
                        )
                        //
                        viewModel.setTripStatus(TripStatus.TRANSFER)
                    }
                )

            }
        }
    }

    companion object {
        const val ALERT_CONFIRM_WAITING_DESTINATION_TRIP_TTL = "alert_confirm_waiting_destination_trip_ttl"
        const val ALERT_CONFIRM_WAITING_DESTINATION_TRIP_MSG = "alert_confirm_waiting_destination_trip_msg"
    }
}