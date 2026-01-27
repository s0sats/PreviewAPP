package com.namoadigital.prj001.dao

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.namoadigital.prj001.core.database.base.NamoaCustomDatabase
import com.namoadigital.prj001.model.TkTicketVG
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TkTicketVGDao @Inject constructor(
    @ApplicationContext context: Context,
) : NamoaCustomDatabase<TkTicketVG>(
    context = context,
    tableName = TABLE_NAME,
) {

    companion object {
        const val TABLE_NAME = "tk_ticket_vg"

        const val CUSTOMER_CODE = "customer_code"
        const val TICKET_PREFIX = "ticket_prefix"
        const val TICKET_CODE = "ticket_code"
        const val VG_CODE = "vg_code"
    }

    @SuppressLint("Range")
    override fun cursorToModel(cursor: Cursor): TkTicketVG {
        with(cursor) {
            return TkTicketVG(
                customerCode = getLong(getColumnIndex(CUSTOMER_CODE)),
                ticketPrefix = getInt(getColumnIndex(TICKET_PREFIX)),
                ticketCode = getInt(getColumnIndex(TICKET_CODE)),
                vgCode = getInt(getColumnIndex(VG_CODE)),
            )
        }
    }

    override fun modelToContentValues(
        model: TkTicketVG?,
        contentValues: ContentValues,
    ): ContentValues {
        model?.let {
            contentValues.apply {
                put(CUSTOMER_CODE, it.customerCode)
                put(TICKET_PREFIX, it.ticketPrefix)
                put(TICKET_CODE, it.ticketCode)
                put(VG_CODE, it.vgCode)
            }
        }

        return contentValues
    }

    override fun getWherePkClause(item: TkTicketVG?): StringBuilder {
        item?.let {
            return StringBuilder().apply {
                append("$CUSTOMER_CODE= '${it.customerCode}'")
                append(" AND $TICKET_PREFIX= '${it.ticketPrefix}'")
                append(" AND $TICKET_CODE= '${it.ticketCode}'")
                append(" AND $VG_CODE= '${it.vgCode}'")
            }
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    fun getTicketVGByGroupCode(
        customerCode: Long,
        ticketPrefix: Int,
        ticketCode: Int,
        groupCode: Int,
    ) : TkTicketVG? {
        return query(
            """
                SELECT * FROM $TABLE_NAME
                WHERE $CUSTOMER_CODE = $customerCode
                AND $TICKET_PREFIX = $ticketPrefix
                AND $TICKET_CODE = $ticketCode
                AND $VG_CODE = $groupCode
            """.trimIndent()
        ).firstOrNull()
    }
}
