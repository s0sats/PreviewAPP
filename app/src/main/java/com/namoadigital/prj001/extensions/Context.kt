package com.namoadigital.prj001.extensions

import android.content.Context
import android.content.DialogInterface
import android.text.SpannableString
import androidx.appcompat.app.AlertDialog
import com.namoa_digital.namoa_library.util.ConstantBase
import com.namoadigital.prj001.R

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
            it)
    }

    actionYes?.let {
        setPositiveButton(
            ConstantBase.HMAUX_TRANS_LIB["sys_alert_btn_yes"] as CharSequence?,
            it)
    }

    show()
}