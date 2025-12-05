package com.namoadigital.prj001.extensions.util

import android.Manifest
import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.namoa_digital.namoa_library.util.ConstantBase.LIB_SUPPORT_PATH
import com.namoadigital.prj001.model.big_file.BigFile
import java.io.File

fun saveText(content: String) {
    val file = File(LIB_SUPPORT_PATH, "debug.txt")
    file.appendText(content +"\n")
}


fun createChannel(
    context: Context,
    channelId: String,
    channelName: String,
){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Canal para notificações gerais"
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}


fun showNotification(
    context: Context,
    channelId: String,
    channelName: String,
    notificationId: Int,
    title: String,
    message: String
): Notification {
    // Criação do canal (necessário a partir do Android 8.0)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Canal para notificações gerais"
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    createChannel(
        context,
                channelId,
                channelName
    )

    // Criação da notificação
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_popup_reminder)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    // Exibe a notificação
    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return NotificationCompat.Builder(context, "worker_channel")
                .setContentTitle("Sincronizando...")
                .setSmallIcon(R.drawable.ic_popup_reminder)
                .build()
        }
        notify(notificationId, builder.build())
        return builder.build()
    }
}

fun cancelNotification(context: Context, notificationId: Int) {
    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.cancel(notificationId)
}

fun debugBigFile(label:String, bigFile: BigFile?=null){
//    if(BuildConfig.BUILD_TYPE == "debug") {
//        val label = """
//--------------$label-------------------
//        """
//        Log.d("BIG_FILE_PROCESS", "--------------$label-------------------")
//        val errorFormatted = bigFile?.let {
//            val error = label + """
//            --------------fileType: ${it.fileType}
//            --------------fileStatus: ${it.fileStatus}
//            --------------fileCode: ${it.fileCode}
//            --------------fileMd5: ${it.fileMd5}
//            --------------fileUrl: ${it.fileUrl}
//        """.trimIndent()
//            Log.d("BIG_FILE_PROCESS", "--------------fileType: ${it.fileType}")
//            Log.d("BIG_FILE_PROCESS", "--------------fileStatus: ${it.fileStatus}")
//            Log.d("BIG_FILE_PROCESS", "--------------fileCode: ${it.fileCode}")
//            Log.d("BIG_FILE_PROCESS", "--------------fileMd5: ${it.fileMd5}")
//            Log.d("BIG_FILE_PROCESS", "--------------fileUrl: ${it.fileUrl}")
//            error
//        } ?: label
//        val exception_file =
//            File(LIB_SUPPORT_PATH, "debug_big_file.txt")
//        try {
//            ToolBox.writeIn(errorFormatted, exception_file);
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
}