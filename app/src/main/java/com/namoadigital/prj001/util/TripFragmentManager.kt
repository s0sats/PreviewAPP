package com.namoadigital.prj001.util

import android.content.Context
import com.namoadigital.prj001.R
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.extensions.isGpsEnabled
import com.namoadigital.prj001.model.trip.TripStatus
import com.namoadigital.prj001.model.trip.toTripStatus

class TripFragmentManager(
    private val context: Context,
    private val fsTripDao: FSTripDao
) {

    fun getInitialTripFrag(): Int? {
        val trip = getCurrentTrip()
        val isGpsEnabled: Boolean = context.isGpsEnabled()

        return trip?.let {
            if(!isGpsEnabled && it.tripStatus.toTripStatus() != TripStatus.OVER_NIGHT){
                R.id.action_frgMainHome_to_onGpsFragment
            }
            when (it.tripStatus.toTripStatus()) {
                TripStatus.PENDING -> {
                    getPendingNavAction()
                }

                TripStatus.TRANSIT -> {
                    getTransitNavAction()
                }

                TripStatus.TRANSFER -> {
                    getTransferNavAction()
                }

                TripStatus.OVER_NIGHT -> {
                    getOverNightNavAction()
                }

                TripStatus.ON_SITE -> {
                    getOnSiteNavAction()
                }

                TripStatus.WAITING_DESTINATION -> {
                    getWaitingDestinationNavAction()
                }

                else -> {
                    R.id.action_frgMainHome_to_homeTripFragment
                }
            }
        }
    }

    fun getCurrentTrip() = fsTripDao.getTrip()

    private fun getWaitingDestinationNavAction(): Int {
        return R.id.action_frgMainHome_to_waitingDestinationFragment
    }

    private fun getOnSiteNavAction(): Int {
        return R.id.action_frgMainHome_to_onSiteFragment
    }

    private fun getOverNightNavAction(): Int {
        return R.id.action_frgMainHome_to_overNightFragment
    }

    private fun getTransferNavAction(): Int {
        return R.id.action_frgMainHome_to_transferFragment
    }

    private fun getTransitNavAction(): Int {
        return R.id.action_frgMainHome_to_transitFragment
    }


    private fun getPendingNavAction(): Int {
        return R.id.action_frgMainHome_to_pendingFragment
    }


}