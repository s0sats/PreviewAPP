package com.namoadigital.prj001.ui.act005.trip.fragment.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.FrgMainTripBinding
import com.namoadigital.prj001.extensions.sendCommandToServiceTripLocation
import com.namoadigital.prj001.extensions.showMaterialAlert
import com.namoadigital.prj001.model.location.Coordinates
import com.namoadigital.prj001.service.location.util.LocationServiceConstants
import com.namoadigital.prj001.ui.act005.trip.fragment.base.OnFrgTripInteract
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripBaseFragment
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_HAS_TRIP_UPDATE_REQUIRED_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_HAS_TRIP_UPDATE_REQUIRED_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_NEW_TRIP_CANCEL_BTN
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_NEW_TRIP_CREATE_BTN
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_NEW_TRIP_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.BTN_NEW_TRIP
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.CANCEL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PLACEHOLDER_TRIP_TTL_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_CREATE_NEW_TRIP_GET_LOCATION_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_CREATE_NEW_TRIP_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_CREATE_NEW_TRIP_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.SEND_BTN
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TripHomeFragment : TripBaseFragment<FrgMainTripBinding>() {


    override lateinit var binding: FrgMainTripBinding
    private val homeViewModel: TripHomeViewModel by viewModels()
    private var tripInteract: OnFrgTripInteract? = null

    override fun showGPSWarning(isVisible: Int) {
        tripCardWarningBinding?.cardWarning?.visibility = View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FrgMainTripBinding.inflate(inflater, container, false)

        binding.btnNewTrip.visibility = View.VISIBLE

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        initVars()
        setActions()
        //
    }

    private fun initVars() {
        headerBinding.llTrip.isEnabled = false
        binding.apply {
            btnNewTrip.text = hmAuxTranslate[BTN_NEW_TRIP]
            llPlaceholder.apply {
                tvPlaceholderTtl.text = hmAuxTranslate[PLACEHOLDER_TRIP_TTL_LBL]
            }

            this.btnNewTrip.apply {
                val hasEventManual = homeViewModel.hasEventManual()
                val hasFormInProcess = homeViewModel.hasFormInProcess()
                val btnEnabled = !(hasEventManual || hasFormInProcess)
                isEnabled = btnEnabled

                val color = if (btnEnabled) R.color.m3_namoa_primary  else android.R.color.darker_gray

                backgroundTintList = ResourcesCompat.getColorStateList(resources, color, null)

            }
        }
    }

    fun setActions() {

        binding.apply {

            btnNewTrip.setOnClickListener {

                if (tripInteract?.isEnabledGps() == false) {
                    requireContext().showMaterialAlert(
                        title = hmAuxTranslate[TRIP_GPS_OFF_TTL]!!,
                        msg = hmAuxTranslate[TRIP_GPS_OFF_MSG]!!,
                        actionPositiveLbl = hmAuxTranslate[TRIP_GPS_OFF_ACTIVE_BTN],
                        actionNeutralLbl = hmAuxTranslate[TRIP_GPS_OFF_CANCEL_BTN],
                        actionPositive = { _, _ ->
                            requireContext().startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        },
                        actionNeutral = { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }
                    ).show()
                    return@setOnClickListener
                }

                if (viewModel.hasTripWithUpdateRequired()) {
                    requireContext().showMaterialAlert(
                        title = hmAuxTranslate[ALERT_HAS_TRIP_UPDATE_REQUIRED_TTL] ?: "",
                        msg = hmAuxTranslate[ALERT_HAS_TRIP_UPDATE_REQUIRED_MSG] ?: "",
                        actionPositiveLbl = hmAuxTranslate[SEND_BTN] ?: "",
                        actionNeutralLbl = hmAuxTranslate[CANCEL] ?: "",
                        actionPositive = { _, _ ->
                            if (ToolBox_Con.isOnline(context)) {
                                listener?.sendTripUpdateRequired()
                            } else {
                                ToolBox_Inf.showNoConnectionDialog(context)
                            }
                        },
                        actionNeutral = { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }
                    ).show()
                    return@setOnClickListener
                }

                requireContext().showMaterialAlert(
                    msg = hmAuxTranslate[ALERT_NEW_TRIP_MSG] ?: "",
                    actionPositiveLbl = hmAuxTranslate[ALERT_NEW_TRIP_CREATE_BTN] ?: "",
                    actionNeutralLbl = hmAuxTranslate[ALERT_NEW_TRIP_CANCEL_BTN] ?: "",
                    actionPositive = { _, _ ->
                        listener?.checkGPSPermission()
                    },
                    actionNeutral = { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                ).show()
            }
        }
    }

    private fun callCreateNewTrip(coordinates: Coordinates? = null) {
        listener?.callTripWS(
            WS_TRIP_CREATE_NEW,
            hmAuxTranslate[PROGRESS_CREATE_NEW_TRIP_TTL]!!,
            hmAuxTranslate[PROGRESS_CREATE_NEW_TRIP_MSG]!!,
        )
        homeViewModel.createNewTrip(coordinates)
    }

    fun handleLatLonNullError(askFirst: Boolean = true, coordinates: Coordinates? = null) {
        if (askFirst) {
            listener?.callTripWS(
                WS_TRIP_GET_LOCATION,
                hmAuxTranslate[PROGRESS_CREATE_NEW_TRIP_TTL]!!,
                hmAuxTranslate[PROGRESS_CREATE_NEW_TRIP_GET_LOCATION_MSG]!!,
            )
            context?.sendCommandToServiceTripLocation(LocationServiceConstants.GET_LOCATION)
        } else {
            callCreateNewTrip(coordinates)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFrgTripInteract) {
            tripInteract = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        tripInteract = null
    }

    companion object {
        const val TRIP_GPS_OFF_TTL = "trip_gps_active_ttl"
        const val TRIP_GPS_OFF_MSG = "trip_gps_active_msg"
        const val TRIP_GPS_OFF_ACTIVE_BTN = "trip_gps_off_active_btn"
        const val TRIP_GPS_OFF_CANCEL_BTN = "trip_gps_off_cancel_btn"
    }
}