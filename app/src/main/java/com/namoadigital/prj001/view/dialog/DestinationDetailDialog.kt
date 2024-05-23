package com.namoadigital.prj001.view.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.databinding.DestinationDialogInfoBinding
import com.namoadigital.prj001.databinding.DialogUserEditBinding
import com.namoadigital.prj001.extensions.applyVisibilityIfTextExists
import com.namoadigital.prj001.extensions.callNavigationIntent
import com.namoadigital.prj001.extensions.callPhoneIntent
import com.namoadigital.prj001.extensions.getFormattedAddress
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.ui.act094.domain.model.SelectionDestinationAvailable
import com.namoadigital.prj001.ui.base.BaseDialog
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class DestinationDetailDialog(
    private val context: Context,
    private val item: SelectionDestinationAvailable,
) {
    private var resourceName: String = "trip_destination_detail_dialog"

    private lateinit var hmTranslation: HMAux
    lateinit var binding: DestinationDialogInfoBinding
    var dialog: BaseDialog<DestinationDialogInfoBinding>

    init {
        dialog = BaseDialog.Builder(context, DestinationDialogInfoBinding.inflate(LayoutInflater.from(context)))
                .isCancelable(true)
                .content { _, binding ->
                    this.binding = binding
                    loadTranslation()
                    setLabels()
                    setActions()
                }.build()
    }

    private fun loadTranslation() {

        listOf(
            DIALOG_DETAIL_TITLE,
            DIALOG_DETAIL_ADDRESS,
            DIALOG_DETAIL_DISTRICT,
            DIALOG_DETAIL_CITY,
            DIALOG_DETAIL_CONTACT,
            DIALOG_DETAIL_CALL,
            DIALOG_DETAIL_CANCEL,
            DIALOG_DETAIL_OPEN_MAPS,
            ALERT_NO_NAVIGATION_APP_FOUND_TTL,
            ALERT_NO_NAVIGATION_APP_FOUND_MSG,
            ALERT_NO_CONTACT_APP_FOUND_TTL,
            ALERT_NO_CONTACT_APP_FOUND_MSG,
            EXTERNAL_ADDRESS
        ).let { list ->

            ToolBox_Inf.getResourceCode(
                context,
                ConstantBaseApp.APP_MODULE,
                resourceName
            ).let { code ->
                hmTranslation = ToolBox_Inf.setLanguage(
                    context,
                    ConstantBaseApp.APP_MODULE,
                    code,
                    ToolBox_Con.getPreference_Translate_Code(context),
                    list
                )
            }
        }

    }


    private fun setLabels() {
        with(binding) {
            val isTicket = item.destinationType == FsTripDestination.TICKET_DESTINATION_TYPE
            tvTitle.text = hmTranslation[DIALOG_DETAIL_TITLE]
            tvSite.text = if(isTicket) hmTranslation[EXTERNAL_ADDRESS] else item.siteDesc

            if (!item.street.isNullOrEmpty()) {
                layoutAddress.visibility = View.VISIBLE

                tvAddress.text = hmTranslation[DIALOG_DETAIL_ADDRESS]
                val number = if (item.streetnumber.isNullOrEmpty()) "" else ", ${item.streetnumber}"
                tvAddressName.applyVisibilityIfTextExists("${item.street} $number")
                tvComplement.applyVisibilityIfTextExists(item.complement)
            } else {
                layoutAddress.visibility = View.GONE
            }

            if (!item.district.isNullOrEmpty()) {
                layoutDistrict.visibility = View.VISIBLE

                tvDistrict.text = hmTranslation[DIALOG_DETAIL_DISTRICT]
                tvDistrictName.applyVisibilityIfTextExists(item.district)
            } else {
                layoutDistrict.visibility = View.GONE
            }

            if (!item.city.isNullOrEmpty() || !item.zipCode.isNullOrEmpty()) {
                layoutCity.visibility = View.VISIBLE

                tvCity.text = hmTranslation[DIALOG_DETAIL_CITY]
                val stateFormatted =  if(!item.state.isNullOrBlank()) """ - ${item.state}""" else ""
                tvCityName.applyVisibilityIfTextExists(item.city + stateFormatted )
                tvZipcode.applyVisibilityIfTextExists(item.zipCode)
            } else {
                layoutCity.visibility = View.GONE
            }

            openMaps.apply {
                if(item.street.isNullOrEmpty()){
                    visibility = View.INVISIBLE
                }else{
                    visibility = View.VISIBLE
                    text = hmTranslation[DIALOG_DETAIL_OPEN_MAPS]
                }
            }

            if (!item.contactName.isNullOrEmpty()) {
                layoutContact.visibility = View.VISIBLE

                tvContact.text = hmTranslation[DIALOG_DETAIL_CONTACT]
                tvContactName.applyVisibilityIfTextExists(item.contactName)

                if (!item.contactPhone.isNullOrEmpty()) {
                    callContact.apply {
                        text = hmTranslation[DIALOG_DETAIL_CALL]
                        visibility = View.VISIBLE
                    }
                    tvPhoneContact.applyVisibilityIfTextExists(item.contactPhone)
                } else {
                    callContact.visibility = View.INVISIBLE
                }

            } else {
                layoutContact.visibility = View.GONE
                callContact.visibility = View.INVISIBLE
            }


        }
    }

    private fun setActions() {
        binding.apply {
            callContact.setOnClickListener {
                context.callPhoneIntent( "tel:${item.contactPhone}",
                    hmTranslation[ALERT_NO_CONTACT_APP_FOUND_TTL]!!,
                    hmTranslation[ALERT_NO_CONTACT_APP_FOUND_MSG]!!,
                )
            }
            //
            openMaps.apply {
                setOnClickListener { _ ->
                    val address = getFormattedAddress(item.getMapsAddress())

                    context.callNavigationIntent(
                        "geo:${item.lat},${item.lon}?q=$address",
                        hmTranslation[ALERT_NO_NAVIGATION_APP_FOUND_TTL]!!,
                        hmTranslation[ALERT_NO_NAVIGATION_APP_FOUND_MSG]!!
                        )
                }
            }
            //
            ivClose.setOnClickListener {
                dialog.dismiss()
            }
        }
    }


    fun show() = dialog.show()
    fun dismiss() = dialog.dismiss()

    companion object {
        const val DIALOG_DETAIL_TITLE = "dialog_detail_address_title"
        const val EXTERNAL_ADDRESS = "external_address"
        const val DIALOG_DETAIL_ADDRESS = "dialog_detail_address"
        const val DIALOG_DETAIL_DISTRICT = "dialog_detail_district"
        const val DIALOG_DETAIL_CITY = "dialog_detail_city"
        const val DIALOG_DETAIL_CONTACT = "dialog_detail_contact"
        const val DIALOG_DETAIL_CALL = "dialog_detail_call"
        const val DIALOG_DETAIL_CANCEL = "dialog_detail_cancel"
        const val DIALOG_DETAIL_OPEN_MAPS = "dialog_detail_open_maps"
        const val ALERT_NO_NAVIGATION_APP_FOUND_TTL = "alert_no_navigation_app_found_ttl"
        const val ALERT_NO_NAVIGATION_APP_FOUND_MSG = "alert_no_navigation_app_found_msg"
        const val ALERT_NO_CONTACT_APP_FOUND_TTL = "alert_no_contact_app_found_ttl"
        const val ALERT_NO_CONTACT_APP_FOUND_MSG = "alert_no_contact_app_found_msg"
    }
}
