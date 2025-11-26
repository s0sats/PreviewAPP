package com.namoadigital.prj001.ui.act095.event_manual.presentation.balloon

import androidx.core.view.isVisible
import com.google.android.material.button.MaterialButton
import com.namoadigital.prj001.core.translate.TranslateMap
import com.namoadigital.prj001.core.translate.textOf
import com.namoadigital.prj001.databinding.TripCardNotificationBinding
import com.namoadigital.prj001.model.event.local.EventManual
import com.namoadigital.prj001.ui.act095.event_manual.translate.EventManualKey
import com.namoadigital.prj001.util.ConstantBaseApp
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Responsável por atualizar visualmente o balão de evento manual
 * conforme o estado [EventManualBalloonState].
 */
class EventManualBalloonBinder(
    private val translateMap: TranslateMap,
    private val binding: TripCardNotificationBinding,
    private val btnReport: MaterialButton,
    private val actions: EventManualBalloonActions? = null
) {

    /**
     * Interface para callbacks de ações do balão
     */
    interface EventManualBalloonActions {
        fun onBalloonClicked(event: EventManual)
        fun onBalloonIconClicked(event: EventManual)
    }

    /**
     * Renderiza o layout conforme o estado atual do evento manual.
     */
    fun bind(state: EventManualBalloonState) = with(binding) {

        btnReport.isVisible = state.currentEvent == null

        if (state.currentEvent == null) {
            this.cardEvent.isVisible = false
            return@with
        }

        this.cardEvent.isVisible = true

        val event = state.currentEvent
        val isLoading = state.isLoading

        cardNotification.isVisible = !isLoading

        val elapsedTitle = getElapsedTimeTitle(event.dateStart)
        if (isLoading) return@with

        val title = elapsedTitle
        val message = event.description

        tvEventTitle.apply {
            text = title
            isVisible = title.isNotEmpty()
        }

        tvEventMessage.apply {
            text = message
            isVisible = message.isNotEmpty()
        }


        iconButton.apply {
            setOnClickListener { actions?.onBalloonIconClicked(event) }
        }

        cardEvent.setOnClickListener { actions?.onBalloonClicked(event) }
    }


    /**
     * Retorna o tempo decorrido (em horas e minutos) desde o início do evento.
     * Usa SimpleDateFormat para parsing da string de data.
     */
    private fun getElapsedTimeTitle(eventStart: String): String {
        return try {
            val sdf =
                SimpleDateFormat(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT_GMT, Locale.getDefault())
            val startDate = sdf.parse(eventStart)
                ?: return translateMap.textOf(key = EventManualKey.ElapsedJustNowLbl)

            val diffMillis = System.currentTimeMillis() - startDate.time
            if (diffMillis < 0) return translateMap.textOf(key = EventManualKey.ElapsedJustNowLbl)

            val totalMinutes = diffMillis / (1000 * 60)
            val hours = totalMinutes / 60
            val minutes = totalMinutes % 60

            when {
                totalMinutes < 1 -> translateMap.textOf(key = EventManualKey.ElapsedJustNowLbl)
                hours == 0L -> translateMap.textOf(
                    key = EventManualKey.ElapsedMinutesLbl,
                    values = listOf("$minutes")
                )

                minutes == 0L -> translateMap.textOf(
                    key = EventManualKey.ElapsedHoursLbl,
                    values = listOf("$hours")
                )

                else -> translateMap.textOf(
                    key = EventManualKey.ElapsedHoursMinutesLbl,
                    values = listOf("$hours", "$minutes")
                )
            }

        } catch (_: Exception) {
            translateMap.textOf(key = EventManualKey.ElapsedJustNowLbl)
        }
    }

}
