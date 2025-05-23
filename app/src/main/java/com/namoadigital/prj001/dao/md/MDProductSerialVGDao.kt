package com.namoadigital.prj001.dao.md

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import androidx.core.database.getFloatOrNull
import androidx.core.database.getStringOrNull
import com.namoadigital.prj001.dao.util.BaseDaoWithReturn
import com.namoadigital.prj001.model.masterdata.product_serial.verification_group.MDProductSerialVg
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con

class MDProductSerialVGDao(context: Context) :
    BaseDaoWithReturn<MDProductSerialVg>(
        context,
        TABLE_NAME,
        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
        Constant.DB_VERSION_CUSTOM,
        Constant.DB_MODE_MULTI
    ) {

    companion object {
        const val TABLE_NAME = "md_product_serial_vg"
        const val CUSTOMER_CODE = "customer_code"
        const val PRODUCT_CODE = "product_code"
        const val SERIAL_CODE = "serial_code"
        const val VG_CODE = "vg_code"
        const val NEXT_CYCLE_MEASURE = "next_cycle_measure"
        const val NEXT_CYCLE_MEASURE_DATE = "next_cycle_measure_date"
        const val NEXT_CYCLE_LIMIT_DATE = "next_cycle_limit_date"
        const val LAST_EXECUTION_MEASURE = "last_execution_measure"
        const val LAST_EXECUTION_DATE = "last_execution_date"
        const val VG_STATUS = "vg_status"
        const val TARGET_DATE = "target_date"
        const val MANUAL_DATE = "manual_date"
        const val PARTITIONED_TICKET_PREFIX = "partitioned_ticket_prefix"
        const val PARTITIONED_TICKET_CODE = "partitioned_ticket_code"
        const val PARTITIONED_USER = "partitioned_user"
        const val PARTITIONED_EXECUTION = "partitioned_execution"

        fun instance(context: Context) = MDProductSerialVGDao(context)
    }

    override fun getWherePkClause(item: MDProductSerialVg?): StringBuilder {
        item?.let {
            return StringBuilder().append(
                """
                $CUSTOMER_CODE = ${it.customerCode}
                AND $PRODUCT_CODE = ${it.productCode}
                AND $SERIAL_CODE = ${it.serialCode}
                AND $VG_CODE = ${it.vgCode}
                """.trimIndent()
            )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    override fun modelToContentValues(
        model: MDProductSerialVg?,
        contentValues: ContentValues
    ): ContentValues {
        model?.let {
            with(contentValues) {
                put(CUSTOMER_CODE, it.customerCode)
                put(PRODUCT_CODE, it.productCode)
                put(SERIAL_CODE, it.serialCode)
                put(VG_CODE, it.vgCode)
                put(NEXT_CYCLE_MEASURE, it.nextCycleMeasure)
                put(NEXT_CYCLE_MEASURE_DATE, it.nextCycleMeasureDate)
                put(NEXT_CYCLE_LIMIT_DATE, it.nextCycleLimitDate)
                put(LAST_EXECUTION_MEASURE, it.lastExecutionMeasure)
                put(LAST_EXECUTION_DATE, it.lastExecutionDate)
                put(VG_STATUS, it.vgStatus)
                put(TARGET_DATE, it.targetDate)
                put(PARTITIONED_TICKET_PREFIX, it.partitionedTicketPrefix)
                put(PARTITIONED_TICKET_CODE, it.partitionedTicketCode)
                put(PARTITIONED_USER, it.partitionedUser)
                put(PARTITIONED_EXECUTION, it.partitionedExecution)
                put(MANUAL_DATE, it.manualDate)
            }
        }
        return contentValues
    }

    @SuppressLint("Range")
    override fun cursorToModel(cursor: Cursor): MDProductSerialVg {
        with(cursor) {
            return MDProductSerialVg(
                customerCode = getLong(getColumnIndex(CUSTOMER_CODE)),
                productCode = getInt(getColumnIndex(PRODUCT_CODE)),
                serialCode = getInt(getColumnIndex(SERIAL_CODE)),
                vgCode = getInt(getColumnIndex(VG_CODE)),
                nextCycleMeasure = getFloat(getColumnIndex(NEXT_CYCLE_MEASURE)),
                nextCycleMeasureDate = getString(getColumnIndex(NEXT_CYCLE_MEASURE_DATE)),
                nextCycleLimitDate = getString(getColumnIndex(NEXT_CYCLE_LIMIT_DATE)),
                lastExecutionMeasure = getFloatOrNull(getColumnIndex(LAST_EXECUTION_MEASURE)),
                lastExecutionDate = getStringOrNull(getColumnIndex(LAST_EXECUTION_DATE)),
                vgStatus = getString(getColumnIndex(VG_STATUS)),
                targetDate = getString(getColumnIndex(TARGET_DATE)),
                manualDate = getString(getColumnIndex(MANUAL_DATE)),
                partitionedTicketPrefix = getInt(getColumnIndex(PARTITIONED_TICKET_PREFIX)),
                partitionedTicketCode = getInt(getColumnIndex(PARTITIONED_TICKET_CODE)),
                partitionedUser = getString(getColumnIndex(PARTITIONED_USER)),
                partitionedExecution = getInt(getColumnIndex(PARTITIONED_EXECUTION)),
            )
        }
    }

    fun getByVgCode(
        customer_code: Long,
        product_code: Long,
        serial_code: Long,
        vg_code: Int,
    ): MDProductSerialVg? {
        return getByString(
            """SELECT * 
                 FROM $TABLE_NAME 
                WHERE $CUSTOMER_CODE = $customer_code 
                  AND $PRODUCT_CODE = $product_code 
                  AND $SERIAL_CODE = $serial_code 
                  AND $VG_CODE = $vg_code 
                LIMIT 1""".trimMargin()
        )
    }

    fun getAll(): List<MDProductSerialVg> {
        return query(
            "SELECT * FROM $TABLE_NAME ORDER BY $VG_CODE"
        )
    }
}