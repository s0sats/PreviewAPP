package com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model

import com.namoadigital.prj001.model.event.local.EventManual
import com.namoadigital.prj001.model.trip.FSEventType
import com.namoadigital.prj001.ui.act005.trip.di.enums.EventStatus

/**
 * Define o tipo de configuração de campo que o evento permite:
 * - HIDE: campo oculto
 * - REQUIRED: campo obrigatório
 * - OPTIONAL: campo opcional
 */
enum class ConfigureFieldType(val value: String) {
    HIDE("HIDE"),
    REQUIRED("REQUIRED"),
    OPTIONAL("OPTIONAL");

    companion object {
        fun from(value: String?): ConfigureFieldType =
            entries.firstOrNull { it.value.equals(value, ignoreCase = true) } ?: OPTIONAL
    }
}

/**
 * Modelo de domínio que representa os dados de um evento manual,
 * usado na camada de UI (Compose).
 */
data class EventManualData(
    val primaryData: PrimaryData = PrimaryData(),
    val title: String = "",
    val cost: String? = null,
    val comment: String? = null,
    val status: EventStatus? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val photo: PhotoData? = null,
    val eventFieldConfig: EventFieldConfig = EventFieldConfig(),
) {

    /**
     * Dados primários do evento (identificadores)
     */
    data class PrimaryData(
        val customerCode: Long? = null,
        val appId: String? = null,
        val typeCode: Int? = null,
        val eventDaySeq: Int? = null,
        val eventUser: Int? = null,
        val eventDay: Int? = null
    )

    /**
     * Configuração dos campos de evento (vinda de FSEventType)
     */
    data class EventFieldConfig(
        var cost: ConfigureFieldType = ConfigureFieldType.OPTIONAL,
        var comment: ConfigureFieldType = ConfigureFieldType.OPTIONAL,
        var photo: ConfigureFieldType = ConfigureFieldType.OPTIONAL,
        var waitAllowed: Boolean = false,
        var waitMaxMinutes: Int? = null
    ) {

        fun isPhotoRequired() = photo == ConfigureFieldType.REQUIRED
        fun isPhotoHidden() = photo == ConfigureFieldType.HIDE

        fun isCostRequired() = cost == ConfigureFieldType.REQUIRED
        fun isCostHidden() = cost == ConfigureFieldType.HIDE

        fun isCommentRequired() = comment == ConfigureFieldType.REQUIRED
        fun isCommentHidden() = comment == ConfigureFieldType.HIDE


        fun setFieldConfig(eventType: FSEventType?) {
            eventType ?: return
            cost = ConfigureFieldType.from(eventType.confCost)
            comment = ConfigureFieldType.from(eventType.confComments)
            photo = ConfigureFieldType.from(eventType.confPhoto)
            waitAllowed = eventType.waitAllowed == 1
            waitMaxMinutes = eventType.waitMaxMinutes
        }
    }

    /**
     * Dados da foto associada ao evento
     */
    data class PhotoData(
        val url: String? = null,
        val name: String? = null,
        val localPath: String? = null,
        val isChangedPhoto: Boolean = false
    )

    fun toEntity(): EventManual {
        return EventManual(
            appId = primaryData.appId,
            user = primaryData.eventUser!!,
            eventDay = primaryData.eventDay!!,
            daySeq = primaryData.eventDaySeq,
            typeCode = primaryData.typeCode!!,
            description = title,
            cost = cost,
            comments = comment,
            photo = EventManual.Photo(
                url = photo?.url,
                local = photo?.localPath,
                isChanged = photo?.isChangedPhoto == true
            ),
            dateStart = startDate!!,
            dateEnd = endDate,
            status = status!!,
        )
    }
}

