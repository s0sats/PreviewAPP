package com.namoadigital.prj001.ui.act005.trip.manager

import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import com.namoadigital.prj001.R
import com.namoadigital.prj001.extensions.fromJsonToList
import com.namoadigital.prj001.extensions.getFragment
import com.namoadigital.prj001.extensions.popBackStack
import com.namoadigital.prj001.model.location.Coordinates
import com.namoadigital.prj001.model.trip.AvailableUsersRec
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripBaseFragment
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripBaseFragment.Companion.WS_TRIP_DOWNLOAD
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripBaseFragment.Companion.WS_TRIP_SEND_UPDATE
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripBaseFragment.Companion.WS_TRIP_START_DATE_SET
import com.namoadigital.prj001.ui.act005.trip.fragment.extract.TripExtractFragment
import com.namoadigital.prj001.ui.act005.trip.fragment.home.TripHomeFragment
import com.namoadigital.prj001.ui.act005.trip.fragment.onsite.TripOnSiteFragment
import com.namoadigital.prj001.ui.act005.trip.fragment.onsite.TripOnSiteFragment.Companion.WS_TRIP_DOWNLOAD_TICKET
import com.namoadigital.prj001.ui.act005.trip.fragment.transfer.TripTransferFragment
import com.namoadigital.prj001.ui.act005.trip.repository.mapping.toListAdapter

class TripServiceCallbackManager constructor(
    private val fragmentNav: Fragment,
    private val wsProcess: String,
    private val response: String,
    private val setTripFragment: () -> Unit,
    private val selectTrip: () -> Unit,
    private val flowDownloadTrip: () -> Unit,
    private val flowCloud: () -> Unit,
) {
    fun tripServiceSuccess() {
        when (wsProcess) {
            TripBaseFragment.WS_TRIP_GET_LOCATION -> {
                fragmentNav.getFragment<TripHomeFragment>()?.let { homeFragment ->
                    val gson = GsonBuilder().create()
                    homeFragment.handleLatLonNullError(
                        false,
                        gson.fromJson(response, Coordinates::class.java)
                    )
                }
            }

            TripBaseFragment.WS_TRIP_ABORT_PENDING,
            TripBaseFragment.WS_TRIP_END -> {
                fragmentNav.popBackStack(R.id.frgMainHome)
                selectTrip()
            }

            TripBaseFragment.WS_TRIP_SAVE_FLEET -> {
                fragmentNav.getFragment<TripBaseFragment<*>>()?.dismissDialog()
            }

            TripBaseFragment.WS_TRIP_SAVE_FLEET_END_TRIP -> {
                fragmentNav.getFragment<TripBaseFragment<*>>()?.dismissDialog()
                fragmentNav.getFragment<TripTransferFragment>()?.callEndTrip()
            }

            WS_TRIP_START_DATE_SET,
            TripBaseFragment.WS_TRIP_ORIGIN_SET -> {
                fragmentNav.getFragment<TripBaseFragment<*>>()?.dismissDialog()
            }

            TripBaseFragment.WS_TRIP_GET_AVAILABLE_USERS -> {
                fragmentNav.getFragment<TripBaseFragment<*>>()?.showUsersAvailable(
                    response.fromJsonToList<AvailableUsersRec>()?.map { it.toListAdapter() }
                        ?: emptyList())
            }

            TripBaseFragment.WS_TRIP_SAVE_USER -> {
                fragmentNav.getFragment<TripBaseFragment<*>>()?.dismissUserDialog(response)
            }

            TripBaseFragment.WS_TRIP_EVENT_SET -> {
                fragmentNav.getFragment<TripBaseFragment<*>>()?.dismissDialog()
            }

            TripBaseFragment.WS_TRIP_CREATE_NEW,
            TripBaseFragment.WS_TRIP_DESTINATION_CHANGE,
            TripBaseFragment.WS_TRIP_START,
            TripBaseFragment.WS_TRIP_TRANSFER,
            TripBaseFragment.WS_TRIP_OVER_NIGHT -> {
                setTripFragment()
            }

            TripBaseFragment.WS_TRIP_SAVE_FLEET_AND_ORIGIN -> {
                fragmentNav.getFragment<TripExtractFragment>()?.editSaveFleet()
            }

            TripBaseFragment.WS_TRIP_DESTINATION_EDIT_DATE -> {
                fragmentNav.getFragment<TripExtractFragment>()?.dismissDialog()
            }

            TripBaseFragment.WS_TRIP_DESTINATION_EDIT_CHAIN -> {
                fragmentNav.getFragment<TripExtractFragment>()?.saveFleetData()
            }

            WS_TRIP_DOWNLOAD_TICKET -> {
                fragmentNav.getFragment<TripOnSiteFragment>()?.ticketFlow()
            }

            WS_TRIP_DOWNLOAD -> {
                fragmentNav.getFragment<TripBaseFragment<*>>()?.update()
                flowDownloadTrip()
            }

            WS_TRIP_SEND_UPDATE -> {
                setTripFragment()
                flowCloud()
            }

            else -> {
                fragmentNav.getFragment<TripBaseFragment<*>>()?.update()
            }
        }
    }


    fun tripServiceFailed() {
        when (wsProcess) {
            TripBaseFragment.WS_TRIP_GET_LOCATION -> {
                fragmentNav.getFragment<TripHomeFragment>()?.let { homeFragment ->

//                    val gson = GsonBuilder().create()
//                    homeFragment.handleLatLonNullError(
//                        false,
//                        gson.fromJson(response, Coordinates::class.java)
//                    )
                    homeFragment.handleLatLonNullError(false, null)
                }
            }

            TripBaseFragment.WS_TRIP_CREATE_NEW -> {
                //tratar erro 406
//                fragmentNav.getFragment<TripHomeFragment>()?.let(TripHomeFragment::handleLatLonNullError)
            }

            TripBaseFragment.WS_TRIP_ABORT_PENDING -> {
                fragmentNav.popBackStack(R.id.frgMainHome)
                selectTrip()
            }

            TripBaseFragment.WS_TRIP_SAVE_FLEET,
            TripBaseFragment.WS_TRIP_ORIGIN_SET,
            TripBaseFragment.WS_TRIP_SAVE_USER,
            TripBaseFragment.WS_TRIP_EVENT_SET -> {
                fragmentNav.getFragment<TripBaseFragment<*>>()?.errorDialog()
            }

            TripBaseFragment.WS_TRIP_GET_AVAILABLE_USERS -> {
                fragmentNav.getFragment<TripBaseFragment<*>>()?.showUsersAvailable(
                    response.fromJsonToList<AvailableUsersRec>()?.map { it.toListAdapter() }
                        ?: emptyList())
            }

            WS_TRIP_SEND_UPDATE -> {
                fragmentNav.popBackStack(R.id.frgMainHome)
                selectTrip()
            }

            else -> {
                fragmentNav.getFragment<TripBaseFragment<*>>()?.errorDialog()
            }
        }
    }

}