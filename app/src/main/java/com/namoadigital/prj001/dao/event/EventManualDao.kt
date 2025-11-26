package com.namoadigital.prj001.dao.event

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.namoadigital.prj001.core.database.base.NamoaCustomDatabase
import com.namoadigital.prj001.extensions.toBoolean
import com.namoadigital.prj001.extensions.toInt
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.event.local.EventManual
import com.namoadigital.prj001.ui.act005.trip.di.enums.EventStatus
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventConflict
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventConflictType
import com.namoadigital.prj001.ui.act095.event_manual.presentation.historic.domain.EventHistoricToMyActionsBase
import com.namoadigital.prj001.ui.act095.event_manual.translate.EventManualKey
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class EventManualDao @Inject constructor(
    @ApplicationContext context: Context,
) : NamoaCustomDatabase<EventManual>(
    context = context,
    tableName = TABLE_NAME,
) {

    companion object {
        const val TABLE_NAME = "event_manual"

        const val EVENT_USER = "event_user"
        const val EVENT_DAY = "event_day"
        const val EVENT_DAY_SEQ = "event_day_seq"
        const val EVENT_TYPE_CODE = "event_type_code"
        const val EVENT_DESCRIPTION = "event_description"
        const val EVENT_COST = "event_cost"
        const val EVENT_COMMENTS = "event_comments"
        const val PHOTO_URL = "photo_url"
        const val PHOTO_NAME = "photo_name"
        const val PHOTO_LOCAL = "photo_local"
        const val EVENT_START = "event_start"
        const val EVENT_END = "event_end"
        const val CHANGED_PHOTO = "changed_photo"
        const val EVENT_STATUS = "event_status"
        const val UPDATE_REQUIRED = "update_required"

        //tmp
        const val APP_ID = "app_id"
    }


    fun saveEvent(eventManual: EventManual) = addUpdate(eventManual)

    fun updateEventFromServer(
        appId: String?,
        eventUser: Int,
        eventDay: Int,
        eventDaySeq: Int,
    ): DaoObjReturn {
        val daoObjReturn = DaoObjReturn()
        return try {
            addUpdate(
                """
                UPDATE $TABLE_NAME 
                SET $EVENT_DAY_SEQ = $eventDaySeq,
                $APP_ID = null,
                $UPDATE_REQUIRED = 0,
                $CHANGED_PHOTO = 0
             
                WHERE ($EVENT_USER = $eventUser 
                AND $EVENT_DAY = $eventDay
                AND $EVENT_DAY_SEQ = $eventDaySeq
                AND $APP_ID IS NULL) 
                
                OR ($APP_ID = '$appId')
                
            """.trimIndent()
            )
            daoObjReturn
        } catch (e: Exception) {
            daoObjReturn.apply {
                description = EventManualKey.ErrorSaveEventMsg.key
                setError(true)
            }
        }
    }

    fun deleteEvent(eventManual: EventManual): DaoObjReturn {
        val daoObjReturn = DaoObjReturn()
        return try {
            remove(
                """
            DELETE FROM $TABLE_NAME
            WHERE $EVENT_USER = ${eventManual.user}
            AND $EVENT_DAY = ${eventManual.eventDay}
            AND $EVENT_DAY_SEQ = ${eventManual.daySeq}
        """.trimIndent()
            )
            daoObjReturn
        } catch (e: Exception) {
            daoObjReturn.apply {
                setError(true)
                description = e.message
            }
        }
    }


    fun getAllEvents(userCode: String, date: Int): List<EventManual> {
        return query(
            """
                SELECT * FROM $TABLE_NAME
                WHERE $EVENT_USER = $userCode
                AND $EVENT_DAY = $date
            """.trimIndent()
        )
    }

    fun getAllEventsPending(userCode: Int): List<EventManual> {
        return query(
            """
                SELECT * FROM $TABLE_NAME
                WHERE $EVENT_USER = $userCode
                AND $UPDATE_REQUIRED = 1
            """.trimIndent()
        )
    }

    fun getEvent(userCode: String, date: Int, daySeq: Int): EventManual {
        return query(
            """
                SELECT * FROM $TABLE_NAME
                WHERE $EVENT_USER = $userCode
                AND $EVENT_DAY = $date
                AND $EVENT_DAY_SEQ = $daySeq
            """.trimIndent()
        ).first()
    }


    @SuppressLint("Range")
    override fun cursorToModel(cursor: Cursor): EventManual {
        with(cursor) {
            return EventManual(
                appId = getStringOrNull(getColumnIndex(APP_ID)),
                user = getInt(getColumnIndex(EVENT_USER)),
                eventDay = getInt(getColumnIndex(EVENT_DAY)),
                daySeq = getIntOrNull(getColumnIndex(EVENT_DAY_SEQ)),
                typeCode = getInt(getColumnIndex(EVENT_TYPE_CODE)),
                description = getString(getColumnIndex(EVENT_DESCRIPTION)),
                cost = getStringOrNull(getColumnIndex(EVENT_COST)),
                comments = getStringOrNull(getColumnIndex(EVENT_COMMENTS)),
                photo = EventManual.Photo(
                    url = getStringOrNull(getColumnIndex(PHOTO_URL)),
                    name = getStringOrNull(getColumnIndex(PHOTO_NAME)),
                    local = getStringOrNull(getColumnIndex(PHOTO_LOCAL)),
                    isChanged = getIntOrNull(getColumnIndex(CHANGED_PHOTO)).toBoolean(),
                ),
                dateStart = getString(getColumnIndex(EVENT_START)),
                dateEnd = getStringOrNull(getColumnIndex(EVENT_END)),
                status = EventStatus.valueOf(getString(getColumnIndex(EVENT_STATUS))),
                isUpdateRequired = getInt(getColumnIndex(UPDATE_REQUIRED)).toBoolean(),
            )
        }
    }

    override fun modelToContentValues(
        model: EventManual?,
        contentValues: ContentValues,
    ): ContentValues {
        model?.let {
            contentValues.apply {
                put(APP_ID, it.appId)
                put(EVENT_USER, it.user)
                put(EVENT_DAY, it.eventDay)
                put(EVENT_DAY_SEQ, it.daySeq)
                put(EVENT_TYPE_CODE, it.typeCode)
                put(EVENT_DESCRIPTION, it.description)
                put(EVENT_COST, it.cost)
                put(EVENT_COMMENTS, it.comments)
                put(PHOTO_URL, it.photo.url)
                put(PHOTO_NAME, it.photo.name)
                put(PHOTO_LOCAL, it.photo.local)
                put(CHANGED_PHOTO, it.photo.isChanged.toInt())
                put(EVENT_START, it.dateStart)
                put(EVENT_END, it.dateEnd)
                put(EVENT_STATUS, it.status.name)
                put(UPDATE_REQUIRED, it.isUpdateRequired.toInt())
            }
        }

        return contentValues
    }

    override fun getWherePkClause(item: EventManual?): StringBuilder {
        item?.let {
            return StringBuilder().apply {
                append("$EVENT_USER = '${it.user}'")
                append(" AND $EVENT_DAY = '${it.eventDay}'")
                append(" AND $EVENT_DAY_SEQ = '${it.daySeq}'")
            }
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    fun getEventByDate(date: Int): List<EventManual> {
        return query(
            """
                SELECT * FROM $TABLE_NAME
                WHERE $EVENT_DAY = $date
            """.trimIndent()
        )
    }

    fun getLastSeq(
        userCode: Int,
        eventDay: Int
    ): Int {
        return getByStringHM(
            """
                SELECT COALESCE(MAX($EVENT_DAY_SEQ), 0) AS $EVENT_DAY_SEQ 
                FROM $TABLE_NAME 
                WHERE $EVENT_USER = $userCode 
                AND $EVENT_DAY = $eventDay
            """.trimIndent()
        )?.get(EVENT_DAY_SEQ)?.toInt() ?: 0
    }

    fun getEventActive(): EventManual? {
        return query(
            """
                SELECT * FROM $TABLE_NAME
                WHERE $EVENT_STATUS = '${EventStatus.WAITING.name}'
            """.trimIndent()
        ).firstOrNull()
    }

    fun getEventConflict(
        currentSeq: Int?,
        userCode: Int,
        newStart: String,
        newEnd: String?
    ): EventConflict? {

        // Conflito de início
        val startOverlap = query(
            """
            SELECT * FROM $TABLE_NAME
            WHERE $EVENT_USER = '$userCode'
            AND $EVENT_STATUS != '${EventStatus.CANCELLED.name}'
            AND $EVENT_DAY_SEQ != ${currentSeq ?: -1}
            AND $EVENT_START < '$newStart'
            AND ($EVENT_END IS NULL OR $EVENT_END > '$newStart')
            LIMIT 1
        """.trimIndent()
        ).firstOrNull()

        if (startOverlap != null) {
            return EventConflict(
                dateStart = startOverlap.dateStart,
                dateEnd = startOverlap.dateEnd,
                type = EventConflictType.START_OVERLAP
            )
        }

        // Conflito de término
        if (newEnd != null) {
            val endOverlap = query(
                """
                SELECT * FROM $TABLE_NAME
                WHERE $EVENT_USER = '$userCode'
                AND $EVENT_STATUS != '${EventStatus.CANCELLED.name}'
                AND $EVENT_DAY_SEQ != ${currentSeq ?: -1}
                AND $EVENT_START < '$newEnd'
                AND ($EVENT_END IS NULL OR $EVENT_END > '$newEnd')
                LIMIT 1
            """.trimIndent()
            ).firstOrNull()

            if (endOverlap != null) {
                return EventConflict(
                    dateStart = endOverlap.dateStart,
                    dateEnd = endOverlap.dateEnd,
                    type = EventConflictType.END_OVERLAP
                )
            }

            //  O evento atual engloba completamente outro evento
            val rangeOverlap = query(
                """
                SELECT * FROM $TABLE_NAME
                WHERE $EVENT_USER = '$userCode'
                AND $EVENT_STATUS != '${EventStatus.CANCELLED.name}'
                AND $EVENT_DAY_SEQ != ${currentSeq ?: -1}
                AND $EVENT_START > '$newStart'
                AND $EVENT_END < '$newEnd'
                LIMIT 1
            """.trimIndent()
            ).firstOrNull()

            if (rangeOverlap != null) {
                return EventConflict(
                    dateStart = rangeOverlap.dateStart,
                    dateEnd = rangeOverlap.dateEnd,
                    type = EventConflictType.RANGE_OVERLAP
                )
            }
        }

        return null
    }

    @SuppressLint("Range")
    fun getEventsToHistoric(): List<EventHistoricToMyActionsBase> {
        val columnEventDate = "event_date"
        val columnEventQuantity = "event_quantity"
        val columnEventDay = "event_day"

        return query(
            """
                SELECT 
                    e.$EVENT_START $columnEventDate,
                    e.$EVENT_DAY $columnEventDay,
                    COUNT(1) $columnEventQuantity
                FROM $TABLE_NAME e
                WHERE e.$EVENT_STATUS = '${EventStatus.DONE.name}'
                GROUP BY e.$EVENT_DAY
                ORDER BY $columnEventDate 
        """.trimIndent(),
            mapper = {
                EventHistoricToMyActionsBase(
                    date = it.getString(it.getColumnIndex(columnEventDate)),
                    eventDay = it.getInt(it.getColumnIndex(columnEventDay)),
                    quantity = it.getInt(it.getColumnIndex(columnEventQuantity))
                )
            }
        )

    }

    fun getHistoryEventsByDay(userCode: Int, eventDay: Int): List<EventManual> {
        return query(
            """
                SELECT * FROM $TABLE_NAME
                WHERE $EVENT_USER = '$userCode'
                AND $EVENT_DAY = '$eventDay'
                AND $EVENT_STATUS = '${EventStatus.DONE.name}'
                ORDER BY $EVENT_START DESC
            """.trimIndent()
        ).toList()
    }

    @Throws(Exception::class)
    fun removeEventByDays(qtyDaysToSubtract: Int) {
        val currentDay = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -qtyDaysToSubtract)
        val limitDate = currentDay.format(calendar.time)

        val query = """
        DELETE FROM $TABLE_NAME
        WHERE $EVENT_DAY <= '$limitDate'
        AND ($EVENT_STATUS = '${EventStatus.DONE.name}'
        OR $EVENT_STATUS = '${EventStatus.CANCELLED.name}')
    """.trimIndent()

        remove(query)
    }
}
