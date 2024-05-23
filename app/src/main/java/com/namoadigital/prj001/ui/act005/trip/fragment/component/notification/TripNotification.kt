package com.namoadigital.prj001.ui.act005.trip.fragment.component.notification

import android.content.Context
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.TripCardNotificationBinding
import com.namoadigital.prj001.extensions.applyVisibilityIfTextExists


fun TripCardNotificationBinding.showNotification(context: Context, notification: TripNotification){

    this.tvEventTitle.applyVisibilityIfTextExists(notification.title)
    this.tvEventMessage.applyVisibilityIfTextExists(notification.message)
    this.iconButton.apply {
        visibility = if(notification.icon == null) View.GONE else View.VISIBLE
        notification.icon?.let {
            icon = ResourcesCompat.getDrawable(context.resources, notification.icon, null)
            setOnClickListener {
                notification.onClick?.invoke()
            }
        }
    }
    this.cardEvent.apply {
        this.setCardBackgroundColor(ResourcesCompat.getColorStateList(context.resources, notification.backgroundColor, null))
        setOnClickListener {
            notification.onClick?.invoke()
        }
    }

    this.cardNotification.visibility = View.VISIBLE

}

fun TripCardNotificationBinding.closeNotification(){
    this.cardNotification.visibility = View.GONE
}



data class TripNotification(
    val title: String? = null,
    val message: String,
    val icon: Int? = null,
    val backgroundColor: Int = R.color.m3_namoa_primaryContainer,
    val onClick: (() -> Unit)? = null
)