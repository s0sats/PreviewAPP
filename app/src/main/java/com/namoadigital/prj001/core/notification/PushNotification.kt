package com.namoadigital.prj001.core.notification

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import androidx.core.app.NotificationCompat
import com.namoadigital.prj001.core.notification.di.enums.NotificationPriority
import com.namoadigital.prj001.core.notification.di.enums.NotificationVisibility


data class PushNotification(
    val id: Int,
    val channelId: String,
    val channelName: String,
    val title: String,
    val message: String,
    val smallIconRes: Int,
    val largeIcon: Bitmap?,
    val priority: NotificationPriority,
    val visibility: NotificationVisibility,
    val autoCancel: Boolean,
    val ongoing: Boolean,
    val silent: Boolean,
    val badgeNumber: Int?,
    val group: String?,
    val groupSummary: Boolean,
    val contentIntent: PendingIntent?,
    val deleteIntent: PendingIntent?,
    val actions: List<NotificationCompat.Action>,
    val style: NotificationCompat.Style?,
    val timeoutAfterMs: Long?,
    val progressMax: Int?,
    val progressCurrent: Int?,
    val progressIndeterminate: Boolean,
    val category: String?,
    val tag: String?,
    val color: Int?,
    val vibrationPattern: LongArray?,
    val lights: Triple<Int, Int, Int>?
) {

    class Builder(
        private val context: Context,
        private val id: Int,
        private val channelId: String,
        private val channelName: String,
        private val smallIconRes: Int,
        private val title: String,
        private val message: String
    ) {
        private var largeIcon: Bitmap? = null
        private var priority: NotificationPriority = NotificationPriority.DEFAULT
        private var visibility: NotificationVisibility = NotificationVisibility.PRIVATE
        private var autoCancel: Boolean = true
        private var ongoing: Boolean = false
        private var silent: Boolean = false
        private var badgeNumber: Int? = null
        private var group: String? = null
        private var groupSummary: Boolean = false
        private var contentIntent: PendingIntent? = null
        private var deleteIntent: PendingIntent? = null
        private val actions: MutableList<NotificationCompat.Action> = mutableListOf()
        private var style: NotificationCompat.Style? = null
        private var timeoutAfterMs: Long? = null
        private var progressMax: Int? = null
        private var progressCurrent: Int? = null
        private var progressIndeterminate: Boolean = false
        private var category: String? = null
        private var tag: String? = null
        private var color: Int? = null
        private var vibrationPattern: LongArray? = null
        private var lights: Triple<Int, Int, Int>? = null

        // ── Aparência ──────────────────────────────

        fun largeIcon(bitmap: Bitmap) = apply { largeIcon = bitmap }
        fun color(colorRes: Int) = apply { color = colorRes }

        // ── Comportamento ──────────────────────────

        fun priority(p: NotificationPriority) = apply { priority = p }
        fun visibility(v: NotificationVisibility) = apply { visibility = v }
        fun autoCancel(value: Boolean = true) = apply { autoCancel = value }
        fun ongoing(value: Boolean = true) = apply { ongoing = value }
        fun silent(value: Boolean = true) = apply { silent = value }
        fun timeoutAfter(ms: Long) = apply { timeoutAfterMs = ms }

        // ── Badge / Grupo ──────────────────────────

        fun badgeNumber(number: Int) = apply { badgeNumber = number }
        fun group(groupKey: String, isSummary: Boolean = false) = apply {
            group = groupKey
            groupSummary = isSummary
        }

        // ── Intents ────────────────────────────────

        fun contentIntent(intent: PendingIntent) = apply { contentIntent = intent }
        fun deleteIntent(intent: PendingIntent) = apply { deleteIntent = intent }

        // ── Ações ──────────────────────────────────

        fun addAction(action: NotificationCompat.Action) = apply { actions.add(action) }
        fun addAction(iconRes: Int, label: String, intent: PendingIntent) = apply {
            actions.add(NotificationCompat.Action(iconRes, label, intent))
        }

        // ── Estilos ────────────────────────────────

        /** Texto longo */
        fun bigTextStyle(bigText: String, title: String? = null, summary: String? = null) = apply {
            style = NotificationCompat.BigTextStyle()
                .bigText(bigText)
                .also { s -> title?.let { s.setBigContentTitle(it) } }
                .also { s -> summary?.let { s.setSummaryText(it) } }
        }

        /** Imagem grande */
        fun bigPictureStyle(picture: Bitmap, summary: String? = null) = apply {
            style = NotificationCompat.BigPictureStyle()
                .bigPicture(picture)
                .also { s -> summary?.let { s.setSummaryText(it) } }
        }

        /** Lista de linhas (inbox) */
        fun inboxStyle(lines: List<String>, title: String? = null, summary: String? = null) = apply {
            style = NotificationCompat.InboxStyle()
                .also { s -> lines.forEach { s.addLine(it) } }
                .also { s -> title?.let { s.setBigContentTitle(it) } }
                .also { s -> summary?.let { s.setSummaryText(it) } }
        }

        /** Estilo de mensagem (chat) */
        fun messagingStyle(
            user: androidx.core.app.Person,
            messages: List<NotificationCompat.MessagingStyle.Message>
        ) = apply {
            style = NotificationCompat.MessagingStyle(user)
                .also { s -> messages.forEach { s.addMessage(it) } }
        }

        // ── Progresso ──────────────────────────────

        fun progress(max: Int, current: Int, indeterminate: Boolean = false) = apply {
            progressMax = max
            progressCurrent = current
            progressIndeterminate = indeterminate
        }

        // ── Extras ─────────────────────────────────

        fun category(cat: String) = apply { category = cat }
        fun tag(t: String) = apply { tag = t }
        fun vibrate(pattern: LongArray) = apply { vibrationPattern = pattern }
        fun lights(color: Int, onMs: Int, offMs: Int) = apply {
            lights = Triple(color, onMs, offMs)
        }

        // ── Build ──────────────────────────────────

        fun build(): PushNotification = PushNotification(
            id = id,
            channelId = channelId,
            channelName = channelName,
            title = title,
            message = message,
            smallIconRes = smallIconRes,
            largeIcon = largeIcon,
            priority = priority,
            visibility = visibility,
            autoCancel = autoCancel,
            ongoing = ongoing,
            silent = silent,
            badgeNumber = badgeNumber,
            group = group,
            groupSummary = groupSummary,
            contentIntent = contentIntent,
            deleteIntent = deleteIntent,
            actions = actions.toList(),
            style = style,
            timeoutAfterMs = timeoutAfterMs,
            progressMax = progressMax,
            progressCurrent = progressCurrent,
            progressIndeterminate = progressIndeterminate,
            category = category,
            tag = tag,
            color = color,
            vibrationPattern = vibrationPattern,
            lights = lights
        )

        /** Constrói e já envia a notificação */
        fun show() = NotificationDispatcher.dispatch(context, build())
    }
}

fun NotificationPriority.toCompat(): Int = when (this) {
    NotificationPriority.LOW     -> NotificationCompat.PRIORITY_LOW
    NotificationPriority.DEFAULT -> NotificationCompat.PRIORITY_DEFAULT
    NotificationPriority.HIGH    -> NotificationCompat.PRIORITY_HIGH
    NotificationPriority.MAX     -> NotificationCompat.PRIORITY_MAX
}

fun NotificationVisibility.toCompat(): Int = when (this) {
    NotificationVisibility.PUBLIC  -> NotificationCompat.VISIBILITY_PUBLIC
    NotificationVisibility.PRIVATE -> NotificationCompat.VISIBILITY_PRIVATE
    NotificationVisibility.SECRET  -> NotificationCompat.VISIBILITY_SECRET
}

// ─────────────────────────────────────────────
// Exemplos de uso
// ─────────────────────────────────────────────

/*

// 1. Notificação simples
PushNotification.Builder(
    context      = context,
    id           = 1,
    channelId    = GENERIC_CHANNEL_ID,
    channelName  = NAMOA_NOTIF_INFO,
    smallIconRes = R.drawable.ic_notification,
    title        = "Olá!",
    message      = "Você tem uma nova mensagem."
).show()


// 2. Persistente de localização (não sai com swipe)
PushNotification.Builder(
    context      = context,
    id           = 99,
    channelId    = PENDENCY_CHANNEL_ID,
    channelName  = NAMOA_PEND_INFO,
    smallIconRes = R.drawable.ic_location_on_24,
    title        = "Rastreando localização",
    message      = "Conclua a ação no app para encerrar."
)
.ongoing(true)
.autoCancel(false)
.priority(NotificationPriority.LOW)
.visibility(NotificationVisibility.PUBLIC)
.show()

// Para cancelar de dentro do app:
NotificationDispatcher.cancel(context, 99)


// 3. Heads-up com ação
PushNotification.Builder(
    context      = context,
    id           = 2,
    channelId    = HIGH_IMPORTANCE_CHANNEL_ID,
    channelName  = NAMOA_NOTIF_INFO,
    smallIconRes = R.drawable.ic_alert,
    title        = "Atenção!",
    message      = "Seu pedido precisa de confirmação."
)
.priority(NotificationPriority.HIGH)
.contentIntent(myPendingIntent)
.addAction(R.drawable.ic_check, "Confirmar", confirmIntent)
.show()


// 4. Progresso (ex: upload)
PushNotification.Builder(
    context      = context,
    id           = 3,
    channelId    = PENDENCY_CHANNEL_ID,
    channelName  = NAMOA_PEND_INFO,
    smallIconRes = R.drawable.ic_upload,
    title        = "Enviando arquivo",
    message      = "70%"
)
.progress(max = 100, current = 70)
.ongoing(true)
.show()


// 5. Só construir sem disparar
val notification = PushNotification.Builder(...)
    .priority(NotificationPriority.HIGH)
    .build() // ← retorna PushNotification sem enviar

*/