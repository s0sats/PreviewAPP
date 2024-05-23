package com.namoadigital.prj001.ui.act005.trip.fragment.gps

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.TripGpsFragmentBinding
import com.namoadigital.prj001.extensions.hasLocationPermission
import com.namoadigital.prj001.extensions.sendCommandToServiceTripLocation
import com.namoadigital.prj001.service.location.FsTripLocationService.Companion.isTracking
import com.namoadigital.prj001.service.location.util.LocationServiceConstants.STOP_LOCATION
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripBaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TripGpsFragment : TripBaseFragment<TripGpsFragmentBinding>() {

    override lateinit var binding: TripGpsFragmentBinding
    override fun showGPSWarning(isVisible: Int) {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TripGpsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isTracking.value) {
            requireContext().sendCommandToServiceTripLocation(STOP_LOCATION)
        }
        initVars()
        initActions()
    }


    private fun initVars() {
        headerBinding.llTrip.isEnabled = false
        headerBinding.llExtract.isEnabled = false
        binding.apply {
            btnConfig.text = getBtnLabel()
            llPlaceholder.apply {
                ivPlaceholder.setImageResource(R.drawable.location_not_found)
                tvPlaceholderTtl.text = getPlaceholderTtl()
                tvPlaceholderSubTtl.text = getPlaceholderSubTtl()
            }
        }
    }

    private fun getPlaceholderTtl(): String {
        return if (!requireContext().hasLocationPermission()) {
            hmAuxTranslate[TRIP_LOCATION_PERMISSION_TITLE]!!
        } else {
            hmAuxTranslate[TRIP_GPS_TITLE]!!
        }
    }
    private fun getPlaceholderSubTtl(): String {
        return if (!requireContext().hasLocationPermission()) {
            hmAuxTranslate[TRIP_LOCATION_PERMISSION_SUBTITLE]!!
        } else {
            hmAuxTranslate[TRIP_GPS_SUBTITLE]!!
        }
    }
    private fun getBtnLabel(): String {
        return if (!requireContext().hasLocationPermission()) {
            hmAuxTranslate[TRIP_LOCATION_PERMISSION_CONFIG_BTN]!!
        } else {
            hmAuxTranslate[TRIP_GPS_CONFIG_BTN]!!
        }
    }

    private fun initActions() {
        binding.btnConfig.setOnClickListener {
            if(!requireContext().hasLocationPermission()){
                 listener?.checkGPSPermission()
            }else {
                requireContext().startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        }
    }

    companion object {
        const val TRIP_GPS_TITLE = "trip_gps_title"
        const val TRIP_GPS_SUBTITLE = "trip_gps_subtitle"
        const val TRIP_GPS_CONFIG_BTN = "trip_gps_config_btn"
        const val TRIP_LOCATION_PERMISSION_TITLE = "trip_location_permission_title"
        const val TRIP_LOCATION_PERMISSION_SUBTITLE = "trip_location_permission_subtitle"
        const val TRIP_LOCATION_PERMISSION_CONFIG_BTN = "trip_location_permission_config_btn"
    }
}
