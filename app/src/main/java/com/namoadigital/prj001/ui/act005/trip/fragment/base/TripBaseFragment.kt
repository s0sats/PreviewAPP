package com.namoadigital.prj001.ui.act005.trip.fragment.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewbinding.ViewBinding
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.BaseFragment
import com.namoadigital.prj001.R
import com.namoadigital.prj001.core.trip.data.preference.CurrentTripPref
import com.namoadigital.prj001.databinding.FrgMainFooterBinding
import com.namoadigital.prj001.databinding.FrgMainHeaderBinding
import com.namoadigital.prj001.databinding.FrgTripDestinationInfoBinding
import com.namoadigital.prj001.databinding.TripCardWarningBinding
import com.namoadigital.prj001.extensions.applyVisibilityIfTextExists
import com.namoadigital.prj001.extensions.callCameraAct
import com.namoadigital.prj001.extensions.callNavigationIntent
import com.namoadigital.prj001.extensions.date.FormatDateType
import com.namoadigital.prj001.extensions.date.formatDate
import com.namoadigital.prj001.extensions.getColorStateListId
import com.namoadigital.prj001.extensions.getFormattedAddress
import com.namoadigital.prj001.extensions.hasLocationPermission
import com.namoadigital.prj001.extensions.highlightItem
import com.namoadigital.prj001.extensions.isGpsEnabled
import com.namoadigital.prj001.extensions.sendCommandToServiceTripLocation
import com.namoadigital.prj001.extensions.sendToast
import com.namoadigital.prj001.extensions.setLateColor
import com.namoadigital.prj001.extensions.setNextColor
import com.namoadigital.prj001.extensions.showMaterialAlert
import com.namoadigital.prj001.model.TranslateResource
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.model.trip.FSEventType
import com.namoadigital.prj001.model.trip.FSTripEvent
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.FsTripDestination.Companion.TICKET_DESTINATION_TYPE
import com.namoadigital.prj001.model.trip.TripStatus
import com.namoadigital.prj001.model.trip.TripTarget
import com.namoadigital.prj001.model.trip.toDescription
import com.namoadigital.prj001.model.trip.toDestinationStatus
import com.namoadigital.prj001.model.trip.toTripStatus
import com.namoadigital.prj001.service.location.FsTripLocationService
import com.namoadigital.prj001.service.location.util.LocationServiceConstants
import com.namoadigital.prj001.ui.act005.OnResfreshUI
import com.namoadigital.prj001.ui.act005.trip.TripViewModel
import com.namoadigital.prj001.ui.act005.trip.di.enums.EventStatus
import com.namoadigital.prj001.ui.act005.trip.di.enums.UserAction
import com.namoadigital.prj001.ui.act005.trip.di.model.TripUserEdit
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_ABORT_TRIP_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_ABORT_TRIP_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_HAS_TRIP_UPDATE_REQUIRED_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_NEW_TRIP_CANCEL_BTN
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_NEW_TRIP_CREATE_BTN
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_NEW_TRIP_LATLON_ERROR_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_NEW_TRIP_LATLON_ERROR_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_NEW_TRIP_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_NO_NAVIGATION_APP_FOUND_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_NO_NAVIGATION_APP_FOUND_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_PDF_NOT_FOUND_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_PDF_NOT_FOUND_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_POSITION_NOT_FOUND_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_POSITION_NOT_FOUND_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_STARTING_PDF_NOT_SUPPORTED_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_STARTING_PDF_NOT_SUPPORTED_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_TRIP_ABORT_CANCEL_BTN
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_TRIP_ABORT_CONFIRM_BTN
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_TRIP_DESTINATION_DELETE_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_TRIP_DESTINATION_DELETE_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_TRIP_OFFLINE_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ALERT_TRIP_OFFLINE_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.BTN_NEW_TRIP
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.CANCEL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.DESTINATION_EXTERNAL_ADDRESS_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.DESTINATION_LATE_COUNTER_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.DESTINATION_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.DESTINATION_NEXT_COUNTER_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.DESTINATION_PRIORITY_COUNTER_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.DESTINATION_SERIAL_COUNTER_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.DESTINATION_TODAY_COUNTER_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.DIALOG_ERROR_CLOSE_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.DialogOrigin
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ERROR_ADD_USER_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ERROR_ADD_USER_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ERROR_OFFLINE_GENERIC_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ERROR_OFFLINE_GENERIC_TITLE
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ERROR_SAVE_OFFLINE_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ERROR_SAVE_OFFLINE_TITLE
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ERROR_TRY_SAVE_ONLINE_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ERROR_TRY_SAVE_ONLINE_TITLE
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.FLEET_DIALOG_DESTINATION_TITLE
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.FLEET_DIALOG_END_DESC
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.FLEET_DIALOG_END_TITLE
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.FLEET_DIALOG_ODOMETER
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.FLEET_DIALOG_ORIGIN_TITLE
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.FLEET_DIALOG_PHOTO
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.FLEET_DIALOG_PLATE
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.FLEET_EDITTEXT_ERROR
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.FLEET_LOW_ODOMETER_ERROR
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.FLEET_PHOTO_DIALOG_ERROR_DOWNLOAD
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.FLEET_PHOTO_DIALOG_REQUIRED
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.ODOMETER_EDITTEXT_ERROR
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PLACEHOLDER_TRIP_OVER_NIGHT_SUB_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PLACEHOLDER_TRIP_OVER_NIGHT_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PLACEHOLDER_TRIP_SUB_TTL_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PLACEHOLDER_TRIP_TRANSFER_SUB_TTL_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PLACEHOLDER_TRIP_TRANSFER_TTL_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PLACEHOLDER_TRIP_TTL_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PLACEHOLDER_TRIP_WAITING_DESTINATION_SUB_TTL_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PLACEHOLDER_TRIP_WAITING_DESTINATION_TTL_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_ABORT_PENDING_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_ABORT_PENDING_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_CREATE_NEW_TRIP_GET_LOCATION_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_CREATE_NEW_TRIP_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_CREATE_NEW_TRIP_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_FLEET_TRIP_SEND_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_FLEET_TRIP_SEND_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_ORIGIN_TRIP_SEND_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_ORIGIN_TRIP_SEND_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_TRIP_DESTINATION_CHANGE_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_TRIP_DESTINATION_CHANGE_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_TRIP_DESTINATION_EDIT_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_TRIP_DESTINATION_EDIT_TLL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_TRIP_END_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_TRIP_END_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_TRIP_ERROR_SYNC_APP_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_TRIP_ERROR_SYNC_APP_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_TRIP_OVER_NIGHT_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_TRIP_OVER_NIGHT_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_TRIP_START_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_TRIP_START_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_TRIP_TRANSFER_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_TRIP_TRANSFER_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_TRIP_WAITING_DESTINATION_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.PROGRESS_TRIP_WAITING_DESTINATION_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.SAVE
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.SAVE_END_TRIP_BTN
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.SAVE_TRIP_OFFLINE_TOAST
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.SEND_BTN
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_ACTION_FORM_OS_BTN
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_ACTION_TICKET_BTN
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_CALL_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_CANCEL_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_CREATE_AT_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_DEPARTED_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_DESTINATION_ARRIVED_DATE_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_DESTINATION_DELETE_BTN
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_DESTINATION_MY_TICKETS_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_DESTINATION_SEARCH_SERIAL_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_DESTINATION_TICKETS_BTN
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_FLEET_EDIT_BTN
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_FLEET_KILOMETERS_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_FLEET_PLATE_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_MAP_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_NEXT_DESTINATION_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_ON_SITE_EXECUTION_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_ON_SITE_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_ON_SITE_ODOMETER_WARNING
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_ORIGIN_GPS
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_ORIGIN_POINT_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_OVER_NIGHT_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_REPORT_BTN
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_RETURN_TO_TRANSIT_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_SITE_TICKETS_BTN
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_START_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_SUCCESS_SEND_DATA
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_SUCCESS_SEND_USER_DATA
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_TO_END_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_TO_END_OVER_NIGHT_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_TO_ON_SITE_BTN
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_TO_OVER_NIGHT_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_TO_TRANSFER_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_TRANSFER_STATUS_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_TRANSIT_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_WAITING_DESTINATION_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_WARNING_ARRIVED_TO_SITE_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_WARNING_ARRIVED_TO_SITE_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_WARNING_DEPARTED_FROM_ORIGIN_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_WARNING_DEPARTED_FROM_ORIGIN_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_WARNING_DEPARTED_FROM_SITE_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_WARNING_DEPARTED_FROM_SITE_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_WARNING_WAITING_DESTINATION_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripTranslate.TRIP_WARNING_WAITING_DESTINATION_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.FleetDialog
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.destination.DestinationDialog
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.origin.EditOriginDialog
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.origin.OriginDialog
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.origin.util.OriginOption
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs.DIALOG_DATE_END_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs.DIALOG_DATE_HINT
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs.DIALOG_DATE_START_EXCEEDED_TRIP_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs.DIALOG_ERROR_DATE_END_TRIP_FUTURE_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs.DIALOG_ERROR_DATE_END_TRIP_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs.DIALOG_ERROR_FUTURE_DATE
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs.DIALOG_HOUR_HINT
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs.PROCESS_DIALOG_START_DATE_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs.PROCESS_DIALOG_START_DATE_TITLE
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.ReportBottomSheet
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.event.DialogEventTrip
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.event.DialogEventTrip.Companion.PROGRESS_EVENT_TRIP_DELETE_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.event.DialogEventTrip.Companion.PROGRESS_EVENT_TRIP_DELETE_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.event.DialogEventTrip.Companion.PROGRESS_EVENT_TRIP_SEND_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.event.DialogEventTrip.Companion.PROGRESS_EVENT_TRIP_SEND_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.event.EventTypeListDialog
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.users.DialogEditUser
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.users.DialogListUsers
import com.namoadigital.prj001.ui.act005.trip.fragment.extract.TripExtractFragment
import com.namoadigital.prj001.ui.act005.trip.fragment.gps.TripGpsFragment
import com.namoadigital.prj001.ui.act005.trip.fragment.gps.TripGpsFragment.Companion.TRIP_GPS_CONFIG_BTN
import com.namoadigital.prj001.ui.act005.trip.fragment.gps.TripGpsFragment.Companion.TRIP_GPS_SUBTITLE
import com.namoadigital.prj001.ui.act005.trip.fragment.gps.TripGpsFragment.Companion.TRIP_GPS_TITLE
import com.namoadigital.prj001.ui.act005.trip.fragment.gps.TripGpsFragment.Companion.TRIP_LOCATION_PERMISSION_CONFIG_BTN
import com.namoadigital.prj001.ui.act005.trip.fragment.gps.TripGpsFragment.Companion.TRIP_LOCATION_PERMISSION_SUBTITLE
import com.namoadigital.prj001.ui.act005.trip.fragment.gps.TripGpsFragment.Companion.TRIP_LOCATION_PERMISSION_TITLE
import com.namoadigital.prj001.ui.act005.trip.fragment.home.TripHomeFragment.Companion.TRIP_GPS_OFF_ACTIVE_BTN
import com.namoadigital.prj001.ui.act005.trip.fragment.home.TripHomeFragment.Companion.TRIP_GPS_OFF_CANCEL_BTN
import com.namoadigital.prj001.ui.act005.trip.fragment.home.TripHomeFragment.Companion.TRIP_GPS_OFF_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.home.TripHomeFragment.Companion.TRIP_GPS_OFF_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.onsite.TripOnSiteFragment
import com.namoadigital.prj001.ui.act005.trip.fragment.onsite.TripOnSiteFragment.Companion.ALERT_CONFIRM_DEPARTED_TRIP_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.onsite.TripOnSiteFragment.Companion.ALERT_CONFIRM_DEPARTED_TRIP_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.onsite.TripOnSiteFragment.Companion.ALERT_DOWNLOAD_TICKET_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.onsite.TripOnSiteFragment.Companion.ALERT_DOWNLOAD_TICKET_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.overnight.TripOverNightFragment.Companion.ALERT_CONFIRM_END_OVER_NIGHT_TRIP_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.overnight.TripOverNightFragment.Companion.ALERT_CONFIRM_END_OVER_NIGHT_TRIP_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.overnight.TripOverNightFragment.Companion.ALERT_CONFIRM_OVER_NIGHT_TRIP_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.overnight.TripOverNightFragment.Companion.ALERT_CONFIRM_OVER_NIGHT_TRIP_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.pending.TripPendingFragment.Companion.ALERT_CONFIRM_START_TRIP_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.pending.TripPendingFragment.Companion.ALERT_CONFIRM_START_TRIP_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.transfer.TripTransferFragment
import com.namoadigital.prj001.ui.act005.trip.fragment.transfer.TripTransferFragment.Companion.ALERT_CONFIRM_END_TRIP_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.transfer.TripTransferFragment.Companion.ALERT_CONFIRM_TRANSFER_TRIP_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.transfer.TripTransferFragment.Companion.ALERT_CONFIRM_TRANSFER_TRIP_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.transfer.TripTransferFragment.Companion.ALERT_CONTAINS_EVENT_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.transfer.TripTransferFragment.Companion.ALERT_CONTAINS_EVENT_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.transfer.TripTransferFragment.Companion.ALERT_GPS_POSITION_NOT_FOUND_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.transfer.TripTransferFragment.Companion.ALERT_GPS_POSITION_NOT_FOUND_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.transit.TripTransitFragment.Companion.ALERT_CONFIRM_TRANSIT_TRIP_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.transit.TripTransitFragment.Companion.ALERT_CONFIRM_TRANSIT_TRIP_TTL
import com.namoadigital.prj001.ui.act005.trip.fragment.waiting_destination.TripWaitingDestinationFragment.Companion.ALERT_CONFIRM_WAITING_DESTINATION_TRIP_MSG
import com.namoadigital.prj001.ui.act005.trip.fragment.waiting_destination.TripWaitingDestinationFragment.Companion.ALERT_CONFIRM_WAITING_DESTINATION_TRIP_TTL
import com.namoadigital.prj001.ui.act005.trip.util.ProgressState
import com.namoadigital.prj001.ui.act094.domain.model.SelectionDestinationAvailable.Companion.convertZeroToLine
import com.namoadigital.prj001.ui.act094.domain.toDestinationDetailDialog
import com.namoadigital.prj001.ui.act094.util.Act094Translate.ALERT_TRIP_NOT_FOUND_MSG
import com.namoadigital.prj001.ui.act094.util.Act094Translate.ALERT_TRIP_NOT_FOUND_TTL
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ConstantBaseApp.FRG_MAIN_HOME
import com.namoadigital.prj001.util.NetworkConnectionException
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import com.namoadigital.prj001.util.TripUserException
import com.namoadigital.prj001.view.dialog.DestinationDetailDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.sql.SQLException


abstract class TripBaseFragment<BINDING : ViewBinding> : BaseFragment(), TripInteract,
    OnResfreshUI {


    abstract var binding: BINDING
    protected lateinit var headerBinding: FrgMainHeaderBinding
    protected var tripCardWarningBinding: TripCardWarningBinding? = null
    private var footerBinding: FrgMainFooterBinding? = null
    protected var destinationBinding: FrgTripDestinationInfoBinding? = null

    val viewModel: TripViewModel by viewModels()
    val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    protected var listener: OnFrgMainHomeInteract? = null

    var dialogActive: BaseTripDialog<*>? = null
    private lateinit var bottomSheet: ReportBottomSheet
    private val tripReceiver by lazy {
        TripLocationReceiver()
    }
    val hmAuxTranslate: HMAux
        get() = loadingTranslate()

    private val mainTranslate: HMAux
        get() = loadingMainTranslate()

    override fun update() {
        viewModel.getCurrentTrip()
    }

    abstract fun showGPSWarning(isVisible: Int)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        headerBinding = FrgMainHeaderBinding.bind(view)
        //
        tripCardWarningBinding = try {
            TripCardWarningBinding.bind(view)
        } catch (e: Exception) {
            null
        }
        //
        footerBinding = try {
            FrgMainFooterBinding.bind(view)
        } catch (e: Exception) {
            null
        }
        //
        destinationBinding = try {
            FrgTripDestinationInfoBinding.bind(view)
        } catch (e: Exception) {
            null
        }
        //
        handleTripState()
        onObserverLoadingState()
        initializeLayoutVisibility()
        updateUI()
        setLabels()
        setActions()
    }

    private fun handleTripState() {
        viewModel.state.onEach { tripState ->
            //
            if (tripState.updateTripScreen) {
                tripState.updateTripScreen = false
                listener?.onSelectTrip()
                return@onEach
            }
            //
            tripState.destination?.let { destination ->
                binding.apply {
                    //
                    destinationBinding?.let {
                        it.tvDestinationLbl.text = hmAuxTranslate[DESTINATION_LBL]


                        it.ivDestinationInfoBtn.visibility = if (destination.containsAddress()) {
                            View.VISIBLE
                        } else {
                            View.INVISIBLE
                        }
                        it.siteActionName.applyVisibilityIfTextExists(destination.destinationSiteDesc)
                        it.serialLayout.visibility = View.GONE
                        if (destination.destinationType == TICKET_DESTINATION_TYPE) {
                            it.siteActionName.text = destination.ticketPk
                            val ticket = viewModel.getTicketInfo(
                                destination.ticketPrefix,
                                destination.ticketCode
                            )
                            ticket?.let { ticket ->
                                it.serialLayout.visibility = View.VISIBLE
                                it.serialId.applyVisibilityIfTextExists(ticket.open_serial_id)
                                it.serialSiteValue.applyVisibilityIfTextExists(ticket.open_site_desc)
                            }
                        }
                        if (tripState.tripStatus != TripStatus.ON_SITE) {
                            it.tvAddressLocation.applyVisibilityIfTextExists(destination.getFullAddress())
                            it.tvHeaderDestination.applyVisibilityIfTextExists(destination.destinationRegionDesc)
                            it.dashboardLayout.isVisible = false
                            it.btnDestinationMapsInfo.apply {
                                text = hmAuxTranslate[TRIP_MAP_LBL]
                                isVisible = destination.containsAddress()

                            }
                        } else {
                            it.dashboardLayout.isVisible = true
                            it.btnDestinationMapsInfo.isVisible = false
                            it.tvHeaderDestination.applyVisibilityIfTextExists(
                                requireContext().formatDate(
                                    FormatDateType.DateAndHour(destination.arrivedDate!!)
                                )
                            )
                            it.tvAddressLocation.apply {
                                maxLines = 1
                                ellipsize = TextUtils.TruncateAt.END
                                applyVisibilityIfTextExists(destination.getAddress())
                            }
                        }
                        //
                        it.serialsLabel.text = hmAuxTranslate[DESTINATION_SERIAL_COUNTER_LBL]
                        //
                        it.serialsValue.apply {
                            text = destination.serialCnt.toString()
                            if (destination.serialCnt > 0
                                && tripState.tripStatus == TripStatus.ON_SITE &&
                                destination.destinationType != TICKET_DESTINATION_TYPE
                            ) {
                                this.highlightItem(
                                    destination.serialCnt,
                                    R.color.m3_namoa_onSurfaceVariant
                                )
                                it.serialsLabel.highlightItem(
                                    destination.serialCnt,
                                    R.color.m3_namoa_onSurfaceVariant
                                )
                                isVisible = true
                                it.serialsLabel.visibility = View.VISIBLE
                            }
                            it.serialsLabel.visibility = View.GONE
                            isVisible = false
                        }

                        //
                        it.root.visibility = View.VISIBLE
                        it.tvDestinationLbl.visibility = View.VISIBLE
                        footerBinding?.btnOutlinedRightAction?.visibility = View.VISIBLE
                        it.btnSearchSerial.visibility = View.GONE
                        it.btnDestinationTicket.visibility = View.GONE
                        //
                        destination.destinationStatus?.let { status ->
                            if (status.toDestinationStatus() == DestinationStatus.ARRIVED) {
                                it.gpOnSiteActions.visibility = View.GONE
                                it.tvDestinationLbl.visibility = View.GONE
                                footerBinding?.btnOutlinedRightAction?.visibility = View.GONE
                                //
                                val arrivedDate = requireContext().formatDate(
                                    FormatDateType.DateAndHour(destination.arrivedDate!!)
                                )
                                destination.destinationSiteCode?.let { siteCode ->
                                    if (siteCode != ToolBox_Con.getPreference_Site_Code(
                                            requireContext()
                                        ).toInt()
                                    ) {
                                        ToolBox_Con.setPreference_Site_Code(
                                            requireContext(),
                                            siteCode.toString()
                                        )
                                    }
                                }
                                //
                                it.tvHeaderDestination.text =
                                    "${hmAuxTranslate[TRIP_DESTINATION_ARRIVED_DATE_LBL]}: $arrivedDate"
                                it.gpOnSiteActions.visibility = View.VISIBLE
                            } else {
                                it.gpOnSiteInfo.visibility = View.GONE
                                it.gpOnSiteActions.visibility = View.GONE
                                footerBinding?.btnOutlinedRightAction?.apply {
                                    text = hmAuxTranslate[TRIP_DESTINATION_DELETE_BTN]
                                    icon =
                                        requireContext().getDrawable(com.namoa_digital.namoa_library.R.drawable.ic_delete)
                                    iconTint =
                                        requireContext().getColorStateList(com.namoa_digital.namoa_library.R.color.m3_namoa_primary)
                                    setOnClickListener {
                                        ToolBox.alertMSG(
                                            requireContext(),
                                            hmAuxTranslate[ALERT_TRIP_DESTINATION_DELETE_TTL],
                                            hmAuxTranslate[ALERT_TRIP_DESTINATION_DELETE_MSG],
                                            { dialogInterface, i ->
                                                callDestinationChangeStatus(DestinationStatus.CANCELLED)
                                                dialogInterface.dismiss()
                                            },
                                            1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //
            tripState.counter?.let { counter ->
                destinationBinding?.let { destinationInfoBinding ->
                    destinationInfoBinding.urgentLabel.text =
                        hmAuxTranslate[DESTINATION_PRIORITY_COUNTER_LBL]
                    destinationInfoBinding.urgentValue.text =
                        counter.priorityCnt?.toInt()?.convertZeroToLine()
                    destinationInfoBinding.urgentValue.apply {
                        counter.priorityCnt?.let { cnt ->
                            destinationInfoBinding.urgentLabel.highlightItem(
                                cnt.toInt(),
                                R.color.m3_namoa_onSurface
                            )
                            this.highlightItem(cnt.toInt(), R.color.m3_namoa_onSurface)
                        }
                    }
                    destinationInfoBinding.forTodayLabel.text =
                        hmAuxTranslate[DESTINATION_TODAY_COUNTER_LBL]
                    destinationInfoBinding.forTodayValue.text =
                        counter.todayCnt?.toInt()?.convertZeroToLine()
                    destinationInfoBinding.forTodayValue.apply {
                        counter.todayCnt?.let { cnt ->
                            destinationInfoBinding.forTodayLabel.highlightItem(
                                cnt.toInt(),
                                R.color.namoa_destination_tag_today
                            )
                            this.highlightItem(cnt.toInt(), R.color.namoa_destination_tag_today)
                        }
                    }
                    destinationInfoBinding.lateLabel.text =
                        hmAuxTranslate[DESTINATION_LATE_COUNTER_LBL]
                    destinationInfoBinding.lateValue.text =
                        counter.lateCnt?.toInt()?.convertZeroToLine()
                    destinationInfoBinding.lateValue.apply {
                        counter.lateCnt?.let { cnt ->
                            destinationInfoBinding.lateLabel.setLateColor(
                                counter.todayCnt?.toInt()
                                    ?: 0, cnt.toInt()
                            )
                        }
                        counter.lateCnt?.let { cnt ->
                            destinationInfoBinding.lateValue.setLateColor(
                                counter.todayCnt?.toInt()
                                    ?: 0, cnt.toInt()
                            )
                        }
                    }
                    destinationInfoBinding.nextLabel.text =
                        hmAuxTranslate[DESTINATION_NEXT_COUNTER_LBL]
                    destinationInfoBinding.nextValue.text =
                        counter.next?.toInt()?.convertZeroToLine()
                    destinationInfoBinding.nextValue.apply {
                        counter.next?.let { cnt ->
                            destinationInfoBinding.nextValue.setNextColor(
                                counter.todayCnt?.toInt() ?: 0,
                                counter.lateCnt?.toInt() ?: 0,
                                cnt.toInt()
                            )
                            destinationInfoBinding.nextLabel.setNextColor(
                                counter.todayCnt?.toInt() ?: 0,
                                counter.lateCnt?.toInt() ?: 0,
                                cnt.toInt()
                            )
                        }
                    }

                    if (tripState.destination?.isTicket == true) {
                        destinationInfoBinding.urgentValue.visibility = View.GONE
                        destinationInfoBinding.forTodayValue.visibility = View.GONE
                        destinationInfoBinding.lateValue.visibility = View.GONE
                        destinationInfoBinding.nextValue.visibility = View.GONE
                        destinationInfoBinding.serialsValue.visibility = View.GONE
                    }
                }
            } ?: run {
                destinationBinding?.let {
                    it.urgentValue.visibility = View.GONE
                    it.forTodayValue.visibility = View.GONE
                    it.lateValue.visibility = View.GONE
                    it.nextValue.visibility = View.GONE
                    it.serialsValue.visibility = View.GONE
                    it.serialsLabel.visibility = View.GONE
                }
            }
            //
            if (!isGPSFragment()) {
                tripState.trip?.let {
                    checkStatusForLocation(it.tripStatus)
                } ?: checkStatusForLocation(TripStatus.NULL.toDescription())
            }
            //
            val tripRunning = tripState.trip != null
            headerBinding.llExtract.visibility = if (tripRunning) View.VISIBLE else View.GONE
            listener?.let {
                headerBinding.llCalendar.visibility =
                    if (it.showHomeBtn()) View.VISIBLE else View.GONE
            }

            setAlertTypeCard(
                viewModel.getAlertTypeFromPreference(
                    CurrentTripPref.invoke(requireContext()),
                    tripState.tripStatus ?: TripStatus.NULL
                )
            )

        }.launchIn(lifecycleScope)
    }

    private fun checkStatusForLocation(tripStatus: String) {
        //
        when (tripStatus) {
            TripStatus.NULL.toDescription(),
            TripStatus.OVER_NIGHT.toDescription(),
                -> {
                if (FsTripLocationService.isTracking.value) {
                    context?.sendCommandToServiceTripLocation(LocationServiceConstants.STOP_LOCATION)
                }
            }

            else -> {
                if (!FsTripLocationService.isTracking.value
                    && requireContext().isGpsEnabled()
                ) {
                    if (requireContext().hasLocationPermission()) {
                        context?.sendCommandToServiceTripLocation(LocationServiceConstants.START_LOCATION)
                    } else {
                        listener?.checkGPSPermission()
                    }
                }
            }
        }
    }

    fun showToast(message: String) {
        requireContext().sendToast(message)
    }

    fun callCamera(
        imageId: Int,
        imagePath: String
    ) {
        requireContext().callCameraAct(
            imageId = imageId,
            path = imagePath
        )
    }


    fun showDialogInfoFleet(target: TripTarget, destinationSeq: Int? = null) {
        viewModel.state.value.trip?.let { trip ->
            dialogActive = FleetDialog(
                context = requireContext(),
                hmAuxTranslate = hmAuxTranslate,
                trip = trip,
                destination = viewModel.state.value.destination,
                target = target,
                validateDate = { date ->
                    viewModel.validateEndTripDate(date = date)
                },
                onSave = { endDate, fleetPlate, odometer, photoUpdate ->
                    val wsProcess = if (target == TripTarget.END) {
                        WS_TRIP_SAVE_FLEET_END_TRIP
                    } else {
                        WS_TRIP_SAVE_FLEET
                    }

                    viewModel.saveFleetData(
                        endDate = endDate,
                        fleetPlate = fleetPlate,
                        odometer = odometer,
                        path = photoUpdate.path,
                        changePhoto = photoUpdate.isNew,
                        target = target,
                        destinationSeq = destinationSeq,
                        deletePhoto = photoUpdate.deletePhoto,
                        progressTranslate = TripWsProgress(
                            process = wsProcess,
                            title = hmAuxTranslate[PROGRESS_FLEET_TRIP_SEND_TTL]!!,
                            message = hmAuxTranslate[PROGRESS_FLEET_TRIP_SEND_MSG]!!
                        )
                    )
                },
                getDestinationThresholds = { customerCode, tripPrefix, tripCode, destinationSeq: Int, type ->
                    viewModel.getDestinationThresholds(
                        customerCode,
                        tripPrefix,
                        tripCode,
                        destinationSeq,
                        type
                    )
                },
                onOpenCamera = { id, path ->
                    callCamera(id, path)
                }
            )

            dialogActive?.show()
        } ?: showDialogTripNotFound()
    }

    fun showOriginDialog() {
        viewModel.state.value.trip?.let { trip ->
            dialogActive = OriginDialog(
                requireContext(),
                trip,
                validateOriginDate = { customerCode, tripPrefix, tripCode ->
                    viewModel.validateOriginDate(customerCode, tripPrefix, tripCode)
                },
                viewModel.getListSites(),
                onSave = { originAux, date, option ->

                    var siteCode: Int? = null
                    var siteDesc: String? = null


//                    if (option is OriginOption.GPS) {
//                        requireContext().sendCommandToServiceTripLocation(GET_LOCATION)
//                    }

                    if (option is OriginOption.SITE) {
                        siteCode = option.code
                        siteDesc = option.desc
                    }
                    viewModel.saveOriginSet(
                        date = date,
                        siteCode = siteCode,
                        siteDesc = siteDesc,
                        originType = option.originType,
                        progressTranslate = TripWsProgress(
                            process = WS_TRIP_ORIGIN_SET,
                            title = originAux[DialogOrigin.PROGRESS_ORIGIN_TRIP_SET_TTL] ?: "",
                            message = originAux[DialogOrigin.PROGRESS_ORIGIN_TRIP_SET_MSG] ?: "",
                        ),
                        progressTranslateFleet = TripWsProgress(
                            process = WS_TRIP_SAVE_FLEET,
                            title = hmAuxTranslate[PROGRESS_FLEET_TRIP_SEND_TTL]!!,
                            message = hmAuxTranslate[PROGRESS_FLEET_TRIP_SEND_MSG]!!
                        ),
                        locationNotFound = {
                            showDialogError(
                                hmAuxTranslate[ALERT_POSITION_NOT_FOUND_TTL]!!,
                                hmAuxTranslate[ALERT_POSITION_NOT_FOUND_MSG]!!,
                                onClose = { dialog ->
                                    dialogActive?.errorSendData()
                                    update()
                                    dialog.dismiss()
                                }
                            )
                        },
                    )
                }
            )

            dialogActive?.show()
        } ?: showDialogTripNotFound()
    }


    fun dismissUserDialog(response: String) {
        dialogActive?.closeDialog("""${hmAuxTranslate[TRIP_SUCCESS_SEND_USER_DATA] ?: ""} $response""")
    }

    fun dismissDialog(onlyClose: Boolean = false) {
        dialogActive?.let { dialog ->
            if (onlyClose) {
                dialogActive = null
                dialog.dismiss()
                return
            }
            dialog.closeDialog(hmAuxTranslate[TRIP_SUCCESS_SEND_DATA] ?: "")
        }
    }

    fun errorDialog(closeDialog: Boolean = false) {
        dialogActive?.let { dialog ->
            listener?.hideProgressWs()
            if (closeDialog) {
                dialog.dismiss()
                viewModel.getCurrentTrip()
                return
            }
            dialog.errorSendData()
        }
    }

    fun showBottomSheet() {
        viewModel.state.value.trip?.let {
            bottomSheet = ReportBottomSheet(
                trip = it,
                context = requireContext(),
                callWs = { process, title, message ->
                    listener?.callTripWS(process, title, message)
                },
                callServiceUsers = {
                    viewModel.getUsersAvailable()
                },
                openDialogEvent = {
                    showDialogEvent()
                },
                showTripOffline = {
                    showDialogError(
                        hmAuxTranslate[ALERT_TRIP_OFFLINE_TTL] ?: "",
                        hmAuxTranslate[ALERT_TRIP_OFFLINE_MSG] ?: ""
                    )
                }
            )
            bottomSheet.show(this.activity!!.supportFragmentManager, "fsTripBottomSheet")
        } ?: showDialogTripNotFound()
    }

    fun showDialogEvent(
        event: FSTripEvent? = viewModel.state.value.event,
        isEditMode: Boolean = false
    ) {

        event?.let {
            val eventType = viewModel.getListEventType(
                it.customerCode,
                it.eventTypeCode
            )
            eventType?.let { type ->
                callEventTypeFormDialog(type, event, isEditMode)
            }
        } ?: kotlin.run {
            dialogActive = EventTypeListDialog(
                context = requireContext(),
                trip = viewModel.state.value.trip!!,
                source = viewModel.getListEventType(),
                osSelectType = { eventType ->
                    callEventTypeFormDialog(eventType, null, false)
                },
            )
            dialogActive?.show()
        }
    }

    private fun callEventTypeFormDialog(
        eventType: FSEventType?,
        event: FSTripEvent?,
        isEditMode: Boolean
    ) {
        eventType?.let {
            dialogActive = DialogEventTrip(
                context = this@TripBaseFragment.context!!,
                trip = viewModel.state.value.trip!!,
                event = event,
                isExtractFlow = isEditMode,
                onOpenCamera = { id, path ->
                    callCamera(id, path)
                },
                eventType = eventType,
                onSave = { eventAux, event ->

                    val tripWsProgress = if (event.eventStatus == EventStatus.CANCELLED) {
                        TripWsProgress(
                            WS_TRIP_SAVE_FLEET,
                            eventAux[PROGRESS_EVENT_TRIP_DELETE_TTL] ?: "",
                            eventAux[PROGRESS_EVENT_TRIP_DELETE_MSG] ?: "",
                        )
                    } else {
                        TripWsProgress(
                            WS_TRIP_SAVE_FLEET,
                            eventAux[PROGRESS_EVENT_TRIP_SEND_TTL] ?: "",
                            eventAux[PROGRESS_EVENT_TRIP_SEND_MSG] ?: "",
                        )
                    }

                    viewModel.updateEvent(event, tripWsProgress)
                },
                checkEventIntersectionDate = { startDateInMilis, endDateInMilis, tripEvent, waiting ->
                    viewModel.getEventError(startDateInMilis, endDateInMilis, tripEvent, waiting)
                }
            )
            dialogActive?.show()
        }
    }

    fun showUsersAvailable(list: List<TripUserEdit>) {
        viewModel.state.value.trip?.let {
            dialogActive =
                DialogListUsers(
                    requireContext(),
                    it,
                    list,
                    onSaveUser = { user, action, processMessage ->
                        saveUser(user, action, processMessage)
                    },
                    checkUserIntersectionDate = { userCode,
                                                  userSeq,
                                                  startDateInMilis,
                                                  endDateInMilis ->
                        viewModel.checkUserIntersection(
                            userCode,
                            userSeq,
                            startDateInMilis,
                            endDateInMilis
                        )
                    }
                )
            dialogActive?.show()
        } ?: showDialogTripNotFound()
    }

    fun showEditUser(user: TripUserEdit, isEditMode: Boolean) {
        val trip = viewModel.state.value.trip
        trip?.let {
            dialogActive = DialogEditUser(
                context = requireContext(),
                trip = trip,
                user = user,
                isEditMode = isEditMode,
                onSaveUser = { user, action, message ->
                    saveUser(user, action, message)
                },
                checkUserIntersectionDate = viewModel::checkUserIntersection
            )
            dialogActive?.show()
        }
    }

    private fun saveUser(
        user: TripUserEdit,
        userAction: UserAction,
        message: Pair<String, String>
    ) {

        viewModel.editUser(
            user = user,
            userAction = userAction,
            tripWsProgress = TripWsProgress(
                process = WS_TRIP_SAVE_USER,
                title = message.first,
                message = message.second
            )
        )
    }

    private fun showDialogTripNotFound() {
        showDialogError(
            hmAuxTranslate[ALERT_TRIP_NOT_FOUND_TTL] ?: "",
            hmAuxTranslate[ALERT_TRIP_NOT_FOUND_MSG] ?: "",
            onClose = { dialog ->
                update()
                dialog.dismiss()
            }
        )
    }

    private fun showDialogError(
        title: String,
        message: String,
        onClose: ((dialog: DialogInterface) -> Unit)? = null
    ) {
        requireContext().showMaterialAlert(
            title = title,
            msg = message,
            icon = ResourcesCompat.getDrawable(
                requireContext().resources,
                R.drawable.baseline_error_outline_24,
                null
            ),
            actionPositiveLbl = hmAuxTranslate[DIALOG_ERROR_CLOSE_LBL],
            actionPositive = { dialog, _ ->
                onClose?.invoke(dialog) ?: dialog.dismiss()
            }
        ).show()
    }

    private inline fun <reified T : BaseTripDialog<*>> T.closeDialog(
        messageSuccess: String
    ) {
        update()
        showToast(messageSuccess)
        dialogActive?.dismiss()
        dialogActive = null
    }

    override fun onResume() {
        super.onResume()

        setTripReceiver()

        dialogActive?.let {
            when (val dialog = it) {
                is FleetDialog -> dialog.updatePhotoDialog()
                is DialogEventTrip -> dialog.updatePhotoDialog()
                is EditOriginDialog -> dialog.updatePhotoDialog()
                is DestinationDialog -> dialog.updatePhotoDialog()
            }
        } ?: run {
            if (cameFromOnPause && this !is TripExtractFragment) {
                listener?.onSelectTrip()
            }
        }
        cameFromOnPause = false
        if (this::headerBinding.isInitialized) setDatetimeVisibility()
    }

    private var cameFromOnPause = false
    override fun onPause() {
        super.onPause()
        cameFromOnPause = true
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(tripReceiver)
    }

    private fun setTripReceiver() {
        val filter = IntentFilter()
        filter.addAction(TRIP_WARNING_RECEIVER)
        filter.addCategory(Intent.CATEGORY_DEFAULT)
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(tripReceiver, filter)
    }

    private fun initializeLayoutVisibility() {
        listener?.let {
            refreshChatBadge(it.getChatBadgeQty())
        }
    }

    private fun setDatetimeVisibility() {
        headerBinding.cvInvalidDatetimeCard.apply {
            visibility = if (!ToolBox_Inf.isLocalDatetimeOk(context)) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

    }

    private fun setLabels() {
        headerBinding.apply {
            tvDatetimeWarning.text = listener?.getDatetimeWarning()
            //
            llHome.visibility = View.GONE
            listener?.let {
                if (it.showHomeBtn()) {
                    llHome.visibility = View.VISIBLE
                    tvHome.text = mainTranslate["sys_main_menu_home_lbl"]
                    ivHome.background = null
                    ivHome.imageTintList = ColorStateList(
                        arrayOf(intArrayOf(android.R.attr.state_enabled)),
                        intArrayOf(Color.WHITE)
                    )
                }
            }
            //
            tvExtract.text = mainTranslate["sys_main_menu_extract_lbl"]
            tvTrip.text = mainTranslate["sys_main_menu_trip_lbl"]
            ivTrip.background =
                AppCompatResources.getDrawable(requireContext(), R.drawable.frg_header_menu_btn)
            ivTrip.imageTintList = requireContext().getColorStateListId(R.color.m3_namoa_shadow)

            //
            tvCalendar.text = mainTranslate["sys_main_menu_calendar_lbl"]
            tvMessenger.text = mainTranslate["messenger_lbl"]
            tvSearch.text = mainTranslate["sys_main_menu_search_lbl"]
        }
    }

    private fun setActions() {
        headerBinding.apply {
            llHome.setOnClickListener {
                if (it.visibility == View.VISIBLE) {
                    listener?.onSelectHome()
                }
            }
            llTrip.setOnClickListener {
                listener?.onSelectTrip()
            }
            llExtract.setOnClickListener {
                listener?.onSelectExtract()
            }
            llCalendar.setOnClickListener {
                listener?.onSelectCalendar()
            }
            //
            llSearch.setOnClickListener {
                listener?.onSelectSearch()
            }
            //
            llMessenger.setOnClickListener {
                listener?.onSelectMessenger()
            }
        }
        //
        destinationBinding?.apply {
            val state = viewModel.state.value
            ivDestinationInfoBtn.setOnClickListener {
                viewModel.state.value.destination?.toDestinationDetailDialog()
                    ?.let { destinationDetail ->
                        val destinationDetailDialog = DestinationDetailDialog(
                            context = requireContext(),
                            destinationDetail,
                        )
                        //
                        destinationDetailDialog.show()
                        //
                    }
            }

            btnDestinationMapsInfo.setOnClickListener {
                val item = state.destination
                if (item == null) {
                    ToolBox.alertMSG(
                        requireContext(),
                        hmAuxTranslate[ALERT_NO_NAVIGATION_APP_FOUND_TTL]!!,
                        hmAuxTranslate[ALERT_NO_NAVIGATION_APP_FOUND_MSG]!!,
                        { dialogInterface, i ->
                            dialogInterface.dismiss()
                        },
                        0
                    )
                    return@setOnClickListener
                }

                val address = getFormattedAddress(item.getAddress())

                context?.callNavigationIntent(
                    "geo:${item.latitude},${item.longitude}?q=${address}",
                    hmAuxTranslate[ALERT_NO_NAVIGATION_APP_FOUND_TTL]!!,
                    hmAuxTranslate[ALERT_NO_NAVIGATION_APP_FOUND_MSG]!!
                )
            }

        }
        //
    }

    protected fun callDestinationChangeStatus(
        destinationStatus: DestinationStatus,
        destination: FsTripDestination? = viewModel.state.value.destination
    ) {
        viewModel.setDestinationStatus(
            destinationStatus,
            destination,
            tripWsProgress = TripWsProgress(
                process = WS_TRIP_DESTINATION_CHANGE,
                title = hmAuxTranslate[PROGRESS_TRIP_DESTINATION_CHANGE_TTL]!!,
                message = hmAuxTranslate[PROGRESS_TRIP_DESTINATION_CHANGE_MSG]!!,
            )
        )
    }

    override fun refreshChatBadge(chatBadgeQty: Int) {
        headerBinding.tvMessengerBadge.visibility = View.GONE
        if (chatBadgeQty > 0) {
            headerBinding.tvMessengerBadge.text = chatBadgeQty.toString()
            headerBinding.tvMessengerBadge.visibility = View.VISIBLE
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFrgMainHomeInteract) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun handleLocationService() {
        if (!FsTripLocationService.isTracking.value && requireContext().isGpsEnabled()) {
            context?.sendCommandToServiceTripLocation(LocationServiceConstants.START_LOCATION)
        }
    }

    fun showConfirmDialog(
        title: String?,
        message: String?,
        negBtn: Int = 1,
        onConfirm: () -> Unit
    ) {
        ToolBox.alertMSG(
            context,
            title,
            message,
            { dialog, _ ->
                dialog.dismiss()
                onConfirm()
            },
            negBtn
        )
    }

    inner class TripLocationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("TRIP_Receiver", "onReceive")
            val bundle = intent.extras
            val tripWarning: Int? = bundle?.getInt(TRIP_WARNING_KEY, -1)
            Log.d("TRIP_Receiver", "tripWarning? $tripWarning")
            //
            setAlertTypeCard(tripWarning)
        }
    }

    fun setWarningCard(
        card: TripCardWarningBinding,
        warningTtl: String,
        warningMsg: String
    ) {
        card.tvWarningTitle.text = warningTtl
        card.tvWarningMessage.text = warningMsg
    }

    fun setAlertTypeCard(
        tripWarning: Int?
    ) {
        tripCardWarningBinding?.let {
            when (tripWarning) {
                //
                TRIP_WARNING_DEPARTED_FROM_ORIGIN -> {
                    setWarningCard(
                        it,
                        hmAuxTranslate[TRIP_WARNING_DEPARTED_FROM_ORIGIN_TTL] ?: "",
                        hmAuxTranslate[TRIP_WARNING_DEPARTED_FROM_ORIGIN_MSG] ?: ""
                    )
                    showGPSWarning(View.VISIBLE)
                }
                //
                TRIP_WARNING_ARRIVED_TO_SITE -> {
                    setWarningCard(
                        it,
                        hmAuxTranslate[TRIP_WARNING_ARRIVED_TO_SITE_TTL]!!,
                        hmAuxTranslate[TRIP_WARNING_ARRIVED_TO_SITE_MSG]!!
                    )
                    showGPSWarning(View.VISIBLE)
                }
                //
                TRIP_WARNING_DEPARTED_FROM_SITE -> {
                    setWarningCard(
                        it,
                        hmAuxTranslate[TRIP_WARNING_DEPARTED_FROM_SITE_TTL] ?: "",
                        hmAuxTranslate[TRIP_WARNING_DEPARTED_FROM_SITE_MSG] ?: ""
                    )
                    showGPSWarning(View.VISIBLE)
                }
                //
                TRIP_WARNING_WAITING_DESTINATION -> {
                    setWarningCard(
                        it,
                        hmAuxTranslate[TRIP_WARNING_WAITING_DESTINATION_TTL] ?: "",
                        hmAuxTranslate[TRIP_WARNING_WAITING_DESTINATION_MSG] ?: ""
                    )
                    showGPSWarning(View.VISIBLE)
                }
                //
                else -> {
                    showGPSWarning(View.GONE)
                }
            }
        }
    }

    fun isGPSFragment(): Boolean {
        return this is TripGpsFragment
    }

    fun isTripOverNightStatus(): Boolean {
        return viewModel.state.value.trip?.tripStatus?.toTripStatus() == TripStatus.OVER_NIGHT
    }

    private fun onObserverLoadingState() {
        viewModel.state.onEach { trip ->
            listener?.invalidateMenuOptions()
            when (val state = trip.progressState) {
                is ProgressState.Online -> {
                    listener?.callTripWS(
                        state.process,
                        state.title,
                        state.message
                    )
                }

                is ProgressState.Offline -> {
                    listener?.hideProgressWs()
                    showToast(hmAuxTranslate[SAVE_TRIP_OFFLINE_TOAST] ?: "")
                }

                is ProgressState.Hide -> {
                    dismissDialog(state.onlyClose)
                }


                is ProgressState.Error -> {
                    listener?.hideProgressWs()

                    val defaultOnClick: (dialog: DialogInterface) -> Unit = { dialog ->
                        dialog.dismiss()
                        errorDialog(state.closeDialog)
                    }

                    when (state.throwable) {
                        is SQLException -> {
                            showDialogError(
                                title = hmAuxTranslate[ERROR_SAVE_OFFLINE_TITLE] ?: "",
                                message = hmAuxTranslate[ERROR_SAVE_OFFLINE_MSG] ?: ""
                            )
                        }

                        is NetworkConnectionException -> {
                            showDialogError(
                                title = hmAuxTranslate[ERROR_TRY_SAVE_ONLINE_TITLE] ?: "",
                                message = state.errorMsg
                                    ?: hmAuxTranslate[ERROR_TRY_SAVE_ONLINE_MSG] ?: "",
                                onClose = {
                                    it.dismiss()
                                    errorDialog(state.closeDialog)

                                }
                            )
                        }

                        is TripUserException -> {
                            showDialogError(
                                title = hmAuxTranslate[ERROR_ADD_USER_TTL] ?: "",
                                message = hmAuxTranslate[ERROR_ADD_USER_MSG] ?: "",
                                onClose = {
                                    it.dismiss()
                                    errorDialog()
                                }
                            )
                        }

                        else -> {
                            showDialogError(
                                title = hmAuxTranslate[ERROR_OFFLINE_GENERIC_TITLE] ?: "",
                                message = "${hmAuxTranslate[ERROR_OFFLINE_GENERIC_MSG]}",
                                onClose = defaultOnClick
                            )
                        }
                    }

                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun loadingTranslate(): HMAux {
        listOf(
            SAVE,
            SAVE_END_TRIP_BTN,
            CANCEL,
            SEND_BTN,
            TripTranslate.ALERT_HAS_TRIP_UPDATE_REQUIRED_TTL,
            ALERT_HAS_TRIP_UPDATE_REQUIRED_MSG,
            FLEET_DIALOG_ORIGIN_TITLE,
            FLEET_DIALOG_DESTINATION_TITLE,
            FLEET_DIALOG_END_TITLE,
            FLEET_DIALOG_END_DESC,
            FLEET_DIALOG_PLATE,
            FLEET_DIALOG_ODOMETER,
            FLEET_DIALOG_PHOTO,
            ODOMETER_EDITTEXT_ERROR,
            FLEET_LOW_ODOMETER_ERROR,
            FLEET_EDITTEXT_ERROR,
            FLEET_PHOTO_DIALOG_REQUIRED,
            FLEET_PHOTO_DIALOG_ERROR_DOWNLOAD,
            PROGRESS_FLEET_TRIP_SEND_TTL,
            PROGRESS_FLEET_TRIP_SEND_MSG,
            PROGRESS_ORIGIN_TRIP_SEND_TTL,
            PROGRESS_ORIGIN_TRIP_SEND_MSG,
            PROGRESS_CREATE_NEW_TRIP_TTL,
            PROGRESS_CREATE_NEW_TRIP_MSG,
            PROGRESS_CREATE_NEW_TRIP_GET_LOCATION_MSG,
            ALERT_NEW_TRIP_LATLON_ERROR_TTL,
            ALERT_NEW_TRIP_LATLON_ERROR_MSG,
            ALERT_NEW_TRIP_CREATE_BTN,
            ALERT_NEW_TRIP_CANCEL_BTN,
            ALERT_NEW_TRIP_MSG,
            BTN_NEW_TRIP,
            PLACEHOLDER_TRIP_TTL_LBL,
            PLACEHOLDER_TRIP_SUB_TTL_LBL,
            TRIP_LBL,
            TRIP_START_LBL,
            TRIP_CANCEL_LBL,
            TRIP_CREATE_AT_LBL,
            TRIP_ORIGIN_POINT_LBL,
            TRIP_ORIGIN_GPS,
            TRIP_FLEET_PLATE_LBL,
            TRIP_FLEET_KILOMETERS_LBL,
            TRIP_REPORT_BTN,
            TRIP_FLEET_EDIT_BTN,
            ALERT_ABORT_TRIP_TTL,
            ALERT_ABORT_TRIP_MSG,
            ALERT_TRIP_ABORT_CONFIRM_BTN,
            ALERT_TRIP_ABORT_CANCEL_BTN,
            PROGRESS_ABORT_PENDING_TTL,
            PROGRESS_ABORT_PENDING_MSG,
            PROGRESS_TRIP_START_TTL,
            PROGRESS_TRIP_START_MSG,
            PROGRESS_TRIP_OVER_NIGHT_TTL,
            PROGRESS_TRIP_OVER_NIGHT_MSG,
            PROGRESS_TRIP_TRANSFER_TTL,
            PROGRESS_TRIP_TRANSFER_MSG,
            PROGRESS_TRIP_WAITING_DESTINATION_TTL,
            PROGRESS_TRIP_WAITING_DESTINATION_MSG,
            DESTINATION_EXTERNAL_ADDRESS_LBL,
            DESTINATION_LBL,
            DESTINATION_PRIORITY_COUNTER_LBL,
            DESTINATION_TODAY_COUNTER_LBL,
            DESTINATION_LATE_COUNTER_LBL,
            DESTINATION_NEXT_COUNTER_LBL,
            DESTINATION_SERIAL_COUNTER_LBL,
            TRIP_SUCCESS_SEND_DATA,
            TRIP_SUCCESS_SEND_USER_DATA,
            DIALOG_ERROR_CLOSE_LBL,
            PROGRESS_TRIP_DESTINATION_CHANGE_TTL,
            PROGRESS_TRIP_DESTINATION_CHANGE_MSG,
            ALERT_TRIP_DESTINATION_DELETE_TTL,
            ALERT_TRIP_DESTINATION_DELETE_MSG,
            TRIP_CALL_LBL,
            TRIP_MAP_LBL,
            TRIP_ON_SITE_LBL,
            TRIP_TO_ON_SITE_BTN,
            TRIP_TRANSIT_LBL,
            TRIP_WAITING_DESTINATION_LBL,
            TRIP_TO_TRANSFER_LBL,
            PLACEHOLDER_TRIP_WAITING_DESTINATION_TTL_LBL,
            PLACEHOLDER_TRIP_WAITING_DESTINATION_SUB_TTL_LBL,
            PLACEHOLDER_TRIP_TRANSFER_TTL_LBL,
            PLACEHOLDER_TRIP_TRANSFER_SUB_TTL_LBL,
            TRIP_TRANSFER_STATUS_LBL,
            TRIP_TO_OVER_NIGHT_LBL,
            TRIP_TO_END_LBL,
            TRIP_OVER_NIGHT_LBL,
            PLACEHOLDER_TRIP_OVER_NIGHT_TTL,
            PLACEHOLDER_TRIP_OVER_NIGHT_SUB_TTL,
            TRIP_TO_END_OVER_NIGHT_LBL,
            TRIP_NEXT_DESTINATION_LBL,
            TRIP_RETURN_TO_TRANSIT_LBL,
            PROGRESS_TRIP_ERROR_SYNC_APP_TTL,
            PROGRESS_TRIP_ERROR_SYNC_APP_MSG,
            ALERT_NO_NAVIGATION_APP_FOUND_TTL,
            ALERT_NO_NAVIGATION_APP_FOUND_MSG,
            TripTranslate.ALERT_NO_CONTACT_APP_FOUND_TTL,
            TripTranslate.ALERT_NO_CONTACT_APP_FOUND_MSG,
            TRIP_DEPARTED_LBL,
            TRIP_DESTINATION_ARRIVED_DATE_LBL,
            TRIP_DESTINATION_SEARCH_SERIAL_LBL,
            TRIP_DESTINATION_MY_TICKETS_LBL,
            TRIP_ON_SITE_ODOMETER_WARNING,
            TRIP_ON_SITE_EXECUTION_LBL,
            TRIP_ACTION_FORM_OS_BTN,
            TRIP_ACTION_TICKET_BTN,
            TRIP_DESTINATION_TICKETS_BTN,
            TRIP_SITE_TICKETS_BTN,
            PROGRESS_TRIP_END_TTL,
            PROGRESS_TRIP_END_MSG,
            PROGRESS_TRIP_DESTINATION_EDIT_TLL,
            PROGRESS_TRIP_DESTINATION_EDIT_MSG,
            ALERT_CONFIRM_START_TRIP_TTL,
            ALERT_CONFIRM_START_TRIP_MSG,
            ALERT_CONFIRM_DEPARTED_TRIP_TTL,
            ALERT_CONFIRM_DEPARTED_TRIP_MSG,
            ALERT_CONFIRM_WAITING_DESTINATION_TRIP_TTL,
            ALERT_CONFIRM_WAITING_DESTINATION_TRIP_MSG,
            ALERT_CONFIRM_TRANSIT_TRIP_TTL,
            ALERT_CONFIRM_TRANSIT_TRIP_MSG,
            ALERT_CONFIRM_OVER_NIGHT_TRIP_TTL,
            ALERT_CONFIRM_OVER_NIGHT_TRIP_MSG,
            ALERT_CONFIRM_TRANSFER_TRIP_TTL,
            ALERT_CONFIRM_TRANSFER_TRIP_MSG,
            ALERT_CONFIRM_END_OVER_NIGHT_TRIP_TTL,
            ALERT_CONFIRM_END_OVER_NIGHT_TRIP_MSG,
            ALERT_STARTING_PDF_NOT_SUPPORTED_TTL,
            ALERT_STARTING_PDF_NOT_SUPPORTED_MSG,
            ALERT_PDF_NOT_FOUND_TTL,
            ALERT_PDF_NOT_FOUND_MSG,
            TripOnSiteFragment.BTN_CONTINUE_OS,
            ALERT_DOWNLOAD_TICKET_TTL,
            ALERT_DOWNLOAD_TICKET_MSG,
            ALERT_CONFIRM_END_TRIP_TTL,
            TripTransferFragment.ALERT_CONFIRM_END_TRIP_MSG,
            ALERT_CONTAINS_EVENT_TTL,
            ALERT_CONTAINS_EVENT_MSG,
            ALERT_GPS_POSITION_NOT_FOUND_TTL,
            ALERT_GPS_POSITION_NOT_FOUND_MSG,
            TranslateInfoDialogs.DIALOG_VALUE_SHOULD_BE_HIGHER_THAN_LBL,
            TranslateInfoDialogs.DIALOG_VALUE_SHOULD_BE_LOWER_THAN_LBL,
            TranslateInfoDialogs.DIALOG_ERROR_ODOMETER_LBL,
            ALERT_POSITION_NOT_FOUND_TTL,
            ALERT_POSITION_NOT_FOUND_MSG,
            TRIP_GPS_OFF_TTL,
            TRIP_GPS_OFF_MSG,
            TRIP_GPS_OFF_ACTIVE_BTN,
            TRIP_GPS_OFF_CANCEL_BTN,
            TRIP_GPS_TITLE,
            TRIP_GPS_SUBTITLE,
            TRIP_GPS_CONFIG_BTN,
            TRIP_LOCATION_PERMISSION_TITLE,
            TRIP_LOCATION_PERMISSION_SUBTITLE,
            TRIP_LOCATION_PERMISSION_CONFIG_BTN,
            SAVE_TRIP_OFFLINE_TOAST,
            ERROR_SAVE_OFFLINE_TITLE,
            ERROR_SAVE_OFFLINE_MSG,
            ERROR_OFFLINE_GENERIC_TITLE,
            ERROR_OFFLINE_GENERIC_MSG,
            ALERT_TRIP_NOT_FOUND_TTL,
            ALERT_TRIP_NOT_FOUND_MSG,
            ALERT_TRIP_OFFLINE_TTL,
            ALERT_TRIP_OFFLINE_MSG,
            ERROR_TRY_SAVE_ONLINE_TITLE,
            ERROR_TRY_SAVE_ONLINE_MSG,
            ERROR_ADD_USER_TTL,
            ERROR_ADD_USER_MSG,
            PROCESS_DIALOG_START_DATE_TITLE,
            PROCESS_DIALOG_START_DATE_MSG,
            DIALOG_DATE_END_LBL,
            DIALOG_ERROR_DATE_END_TRIP_LBL,
            DIALOG_ERROR_DATE_END_TRIP_FUTURE_LBL,
            DIALOG_DATE_HINT,
            DIALOG_HOUR_HINT,
            DIALOG_ERROR_FUTURE_DATE,
            DIALOG_DATE_START_EXCEEDED_TRIP_LBL,
            TRIP_DESTINATION_DELETE_BTN
        ).let { list ->
            return TranslateResource(
                requireContext(),
                MODULE_CODE,
                requireContext().getTripResourceCode()
            ).setLanguage(list)
        }
    }

    private fun loadingMainTranslate(): HMAux {
        listOf(
            "sys_main_menu_calendar_lbl",
            "sys_main_menu_home_lbl",
            "sys_main_menu_trip_lbl",
            "sys_main_menu_extract_lbl",
            "empty_list_lbl",
            "messenger_lbl",
            "sys_main_menu_search_lbl",
            "tag_list_lbl",
            "tag_item_qty",
            "tag_item_form_in_execution",
            "all_tag_list_item"
        ).let { list ->
            return TranslateResource(
                requireContext(),
                MODULE_CODE,
                requireContext().getMainResourceCode()
            ).setLanguage(list)
        }
    }


    companion object {

        const val MODULE_CODE = ConstantBaseApp.APP_MODULE
        fun Context.getTripResourceCode(): String = ToolBox_Inf.getResourceCode(
            this,
            MODULE_CODE,
            TRIP_MAIN_SCREENS
        )

        fun Context.getMainResourceCode(): String = ToolBox_Inf.getResourceCode(
            this,
            MODULE_CODE,
            FRG_MAIN_HOME
        )

        const val TRIP_WARNING_RECEIVER = "TRIP_WARNING_RECEIVER"
        const val TRIP_WARNING_KEY = "TRIP_WARNING_KEY"
        const val TRIP_WARNING_REMOVE = 0
        const val TRIP_WARNING_DEPARTED_FROM_ORIGIN = 1
        const val TRIP_WARNING_ARRIVED_TO_SITE = 2
        const val TRIP_WARNING_DEPARTED_FROM_SITE = 3
        const val TRIP_WARNING_WAITING_DESTINATION = 4

        private const val TRIP_MAIN_SCREENS = "trip_main_screens"
        const val WS_TRIP_PREFIX = "ws_trip_"
        const val WS_TRIP_DOWNLOAD = "ws_trip_trip_download"
        const val WS_TRIP_SEND_UPDATE = WS_TRIP_PREFIX + "trip_update"
        const val WS_TRIP_CREATE_NEW = WS_TRIP_PREFIX + "create_new"
        const val WS_TRIP_GET_LOCATION = WS_TRIP_PREFIX + "get_location"
        const val WS_TRIP_SAVE_FLEET = WS_TRIP_PREFIX + "save_fleet"
        const val WS_TRIP_SAVE_FLEET_END_TRIP = WS_TRIP_PREFIX + "save_fleet_end_trip"
        const val WS_TRIP_SAVE_FLEET_AND_ORIGIN = WS_TRIP_PREFIX + "save_fleet_and_origin"
        const val WS_TRIP_ORIGIN_SET = WS_TRIP_PREFIX + "origin_set"
        const val WS_TRIP_START_DATE_SET = WS_TRIP_PREFIX + "start_date_set"
        const val WS_TRIP_ABORT_PENDING = WS_TRIP_PREFIX + "abort_pending"
        const val WS_TRIP_START = WS_TRIP_PREFIX + "start"
        const val WS_TRIP_START_WITHOUT_DESTINATION = WS_TRIP_PREFIX + "start_without_destination"
        const val WS_TRIP_GET_AVAILABLE_USERS = WS_TRIP_PREFIX + "get_available_users"
        const val WS_TRIP_SAVE_USER = WS_TRIP_PREFIX + "save_user"
        const val WS_TRIP_EVENT_SET = WS_TRIP_PREFIX + "event"
        const val WS_TRIP_DESTINATION_CHANGE = WS_TRIP_PREFIX + "destination_change"
        const val WS_TRIP_TRANSFER = WS_TRIP_PREFIX + "transfer"
        const val WS_TRIP_END = WS_TRIP_PREFIX + "end"
        const val WS_TRIP_OVER_NIGHT = WS_TRIP_PREFIX + "over_night"
        const val WS_TRIP_DESTINATION_EDIT_DATE = WS_TRIP_PREFIX + "destination_edit_date"
        const val WS_TRIP_DESTINATION_EDIT_CHAIN = WS_TRIP_PREFIX + "destination_edit_chain"
    }
}
