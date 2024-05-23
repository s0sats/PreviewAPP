package com.namoadigital.prj001.extensions

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.namoa_digital.namoa_library.util.ConstantBase
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.Camera_Activity
import com.namoadigital.prj001.R
import com.namoadigital.prj001.service.location.FsTripLocationService
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

fun Context.showAlertWithYesOrNot(
    title: SpannableString,
    msg: SpannableString,
    cancelable: Boolean = false,
    actionYes: DialogInterface.OnClickListener? = null,
    actionNo: DialogInterface.OnClickListener? = null
) = AlertDialog.Builder(this, R.style.AlertDialogTheme).apply {
    setTitle(title)
    setMessage(msg)
    setCancelable(cancelable)
    actionNo?.let {
        setNegativeButton(
            ConstantBase.HMAUX_TRANS_LIB["sys_alert_btn_no"] as CharSequence?,
            it
        )
    }

    actionYes?.let {
        setPositiveButton(
            ConstantBase.HMAUX_TRANS_LIB["sys_alert_btn_yes"] as CharSequence?,
            it
        )
    }

    show()
}


fun Context.getToken() = ToolBox_Inf.getToken(this)
fun Context.getCustomerCode() = ToolBox_Con.getPreference_Customer_Code(this)

fun Context.sendToast(message: String, duration: Int = Toast.LENGTH_LONG) =
    Toast.makeText(this, message, duration).show()

fun Context.callCameraAct(
    imageId: Int,
    path: String,
    path2: String = "",
    type: Int = 1,
    preMode: Boolean = false,
    editMode: Boolean = false,
    takeAnotherPhoto: Boolean = true,
    allowGallery: Boolean = false,
    allowHighResolution: Boolean = false,
    fileAuthorities: String = ConstantBase.AUTHORITIES_FOR_PROVIDER
) {
    //
    val bundle = Bundle()
    bundle.putInt(ConstantBase.PID, imageId)
    bundle.putInt(ConstantBase.PTYPE, type) //TIPO
    bundle.putString(ConstantBase.PPATH, path) //PATH DA FOTO
    bundle.putString(ConstantBase.PPATH2, path2) //PATH2 DA FOTO
    bundle.putBoolean(ConstantBase.PEDIT, editMode) //EDITAR FOTO
    bundle.putBoolean(ConstantBase.MPREMODE, preMode) //PREMODE
    bundle.putBoolean(ConstantBase.PENABLED, takeAnotherPhoto) //TIRAR OUTRA FOTO
    bundle.putBoolean(ConstantBase.P_ALLOW_GALLERY, allowGallery)
    bundle.putBoolean(ConstantBase.P_ALLOW_HIGH_RESOLUTION, allowHighResolution)
    bundle.putString(ConstantBase.FILE_AUTHORITIES, fileAuthorities)
    //
    //
    val mIntent = Intent(this, Camera_Activity::class.java)
    mIntent.putExtras(bundle)
    //
    //
    this.startActivity(mIntent)
}

fun Context.sendCommandToServiceTripLocation(action: String) {
    Intent(this, FsTripLocationService::class.java).also {
        it.action = action
        this.startService(it)
    }
}

fun Context.showMaterialAlert(
    title: String = "",
    msg: String,
    cancelable: Boolean = false,
    actionPositiveIcon: Int? = null,
    actionNeutralIcon: Int? = null,
    actionPositiveLbl: String? = null,
    actionNeutralLbl: String? = null,
    actionPositive: DialogInterface.OnClickListener? = null,
    actionNeutral: DialogInterface.OnClickListener? = null,
    icon: Drawable? = null
) = AlertDialog.Builder(this).apply {
    if (title.isNotEmpty()) {
        setTitle(title)
    }
    setMessage(msg)
    setCancelable(cancelable)
    icon?.let(this::setIcon)
    actionPositive?.let {
        actionPositiveIcon?.let { icon ->
            val iconDrawable = ContextCompat.getDrawable(context, icon)
            setPositiveButtonIcon(iconDrawable)
        }
        setPositiveButton(
            actionPositiveLbl,
            it
        )
    }

    actionNeutral?.let {
        actionNeutralIcon?.let { icon ->
            val iconDrawable = ContextCompat.getDrawable(context, icon)
            setNeutralButtonIcon(iconDrawable)
        }
        setNeutralButton(
            actionNeutralLbl,
            it
        )
    }
    this
}

fun Context.getResourceCode(resourceName: String): String = ToolBox_Inf.getResourceCode(
    this,
    ConstantBaseApp.APP_MODULE,
    resourceName
)

fun Context.callPhoneIntent(phone: String, errorTtl: String, errorMsg: String) {
    val phoneNumber = Uri.parse(phone)
    val intent = Intent(Intent.ACTION_DIAL, phoneNumber)
    try {
        this.startActivity(intent)
    } catch (exception: ActivityNotFoundException) {
        //
        ToolBox_Inf.registerException(
            this.javaClass.name,
            exception
        )
        //
        ToolBox.alertMSG(
            this,
            errorTtl,
            errorMsg,
            { dialogInterface, i ->
                dialogInterface.dismiss()
            },
            0
        )
    }
}

fun Context.isGpsEnabled(): Boolean {
    val location = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return location.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun Context.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

fun Context.callNavigationIntent(navigationInfo: String, errorTtl: String, errorMsg: String) {
    val geoLocation = Uri.parse(navigationInfo)
    val intent = Intent(Intent.ACTION_VIEW, geoLocation)
    try {
        this.startActivity(intent)
    } catch (exception: ActivityNotFoundException) {
        //
        ToolBox_Inf.registerException(
            this.javaClass.name,
            exception
        )
        //
        ToolBox.alertMSG(
            this,
            errorTtl,
            errorMsg,
            { dialogInterface, i ->
                dialogInterface.dismiss()
            },
            0
        )
    }
}

fun Context.getDrawableId(@DrawableRes id: Int, theme: Resources.Theme? = null) = ResourcesCompat
    .getDrawable(this.resources, id, theme)

fun Context.getColorStateListId(@ColorRes id: Int, theme: Resources.Theme? = null) = ResourcesCompat
    .getColorStateList(this.resources, id, theme)