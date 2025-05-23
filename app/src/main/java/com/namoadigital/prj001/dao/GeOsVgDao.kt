package com.namoadigital.prj001.dao

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import androidx.core.database.getFloatOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.namoadigital.prj001.dao.md.MDProductSerialVGDao
import com.namoadigital.prj001.dao.md.MDVerificationGroupDao
import com.namoadigital.prj001.dao.util.BaseDaoWithReturn
import com.namoadigital.prj001.model.masterdata.ge_os.vg.FormVgs
import com.namoadigital.prj001.model.masterdata.ge_os.vg.GeOsVg
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con

class GeOsVgDao(context: Context) :
    BaseDaoWithReturn<GeOsVg>(
        context,
        TABLE_NAME,
        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
        Constant.DB_VERSION_CUSTOM,
        Constant.DB_MODE_MULTI
    ) {

    companion object {
        const val TABLE_NAME = "ge_os_vg"
        const val CUSTOMER_CODE = "customer_code"
        const val CUSTOM_FORM_TYPE = "custom_form_type"
        const val CUSTOM_FORM_CODE = "custom_form_code"
        const val CUSTOM_FORM_VERSION = "custom_form_version"
        const val CUSTOM_FORM_DATA = "custom_form_data"
        const val PRODUCT_CODE = "product_code"
        const val SERIAL_CODE = "serial_code"
        const val VG_CODE = "vg_code"
        const val VG_ID = "vg_id"
        const val VG_DESC = "vg_desc"
        const val NEXT_CYCLE_MEASURE = "next_cycle_measure"
        const val NEXT_CYCLE_MEASURE_DATE = "next_cycle_measure_date"
        const val NEXT_CYCLE_LIMIT_DATE = "next_cycle_limit_date"
        const val LAST_EXECUTION_MEASURE = "last_execution_measure"
        const val LAST_EXECUTION_DATE = "last_execution_date"
        const val VG_STATUS = "vg_status"
        const val TARGET_DATE = "target_date"
        const val MANUAL_DATE = "manual_date"
        const val VALUE_SUFFIX = "value_suffix"
        const val RESTRICTION_DECIMAL = "restriction_decimal"
        const val PARTITIONED_TICKET_PREFIX = "partitioned_ticket_prefix"
        const val PARTITIONED_TICKET_CODE = "partitioned_ticket_code"
        const val PARTITIONED_USER = "partitioned_user"
        const val PARTITIONED_EXECUTION = "partitioned_execution"
        const val IS_ACTIVE = "is_active"
        const val HAS_EXPIRED = "has_expired"

    }

    override fun getWherePkClause(item: GeOsVg?): StringBuilder {
        item?.let {
            return StringBuilder().append(
                """
                $CUSTOMER_CODE = ${it.customerCode}
                AND $CUSTOM_FORM_TYPE = ${it.customFormType}
                AND $CUSTOM_FORM_CODE = ${it.customFormCode}
                AND $CUSTOM_FORM_VERSION = ${it.customFormVersion}
                AND $CUSTOM_FORM_DATA = ${it.customFormData}
                AND $PRODUCT_CODE = ${it.productCode}
                AND $SERIAL_CODE = ${it.serialCode}
                AND $VG_CODE = ${it.vgCode}
                """.trimIndent()
            )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    override fun modelToContentValues(
        model: GeOsVg?,
        contentValues: ContentValues
    ): ContentValues {
        model?.let {
            with(contentValues) {

                put(CUSTOMER_CODE, it.customerCode)
                put(CUSTOM_FORM_TYPE, it.customFormType)
                put(CUSTOM_FORM_CODE, it.customFormCode)
                put(CUSTOM_FORM_VERSION, it.customFormVersion)
                put(CUSTOM_FORM_DATA, it.customFormData)
                put(PRODUCT_CODE, it.productCode)
                put(SERIAL_CODE, it.serialCode)
                put(VG_CODE, it.vgCode)
                put(VG_ID, it.vgId)
                put(VG_DESC, it.vgDesc)
                put(NEXT_CYCLE_MEASURE, it.nextCycleMeasure)
                put(NEXT_CYCLE_MEASURE_DATE, it.nextCycleMeasureDate)
                put(NEXT_CYCLE_LIMIT_DATE, it.nextCycleLimitDate)
                put(LAST_EXECUTION_MEASURE, it.lastExecutionMeasure)
                put(LAST_EXECUTION_DATE, it.lastExecutionDate)
                put(VG_STATUS, it.vgStatus)
                put(TARGET_DATE, it.targetDate)
                put(MANUAL_DATE, it.manualDate)
                put(VALUE_SUFFIX, it.valueSuffix)
                put(RESTRICTION_DECIMAL, it.restriction_decimal)
                put(PARTITIONED_TICKET_PREFIX, it.partitionedTicketPrefix)
                put(PARTITIONED_TICKET_CODE, it.partitionedTicketCode)
                put(PARTITIONED_USER, it.partitionedUser)
                put(PARTITIONED_EXECUTION, it.partitionedExecution)
                put(IS_ACTIVE, it.isActive)
                put(HAS_EXPIRED, it.hasExpired)
            }
        }
        return contentValues
    }

    @SuppressLint("Range")
    override fun cursorToModel(cursor: Cursor): GeOsVg {
        with(cursor) {
            return GeOsVg(
                customerCode = getLong(getColumnIndex(CUSTOMER_CODE)),
                customFormType = getInt(getColumnIndex(CUSTOM_FORM_TYPE)),
                customFormCode = getInt(getColumnIndex(CUSTOM_FORM_CODE)),
                customFormVersion = getInt(getColumnIndex(CUSTOM_FORM_VERSION)),
                customFormData = getInt(getColumnIndex(CUSTOM_FORM_DATA)),
                productCode = getInt(getColumnIndex(PRODUCT_CODE)),
                serialCode = getInt(getColumnIndex(SERIAL_CODE)),
                vgCode = getInt(getColumnIndex(VG_CODE)),
                nextCycleMeasure = getFloatOrNull(getColumnIndex(NEXT_CYCLE_MEASURE)),
                nextCycleMeasureDate = getStringOrNull(getColumnIndex(NEXT_CYCLE_MEASURE_DATE)),
                nextCycleLimitDate = getStringOrNull(getColumnIndex(NEXT_CYCLE_LIMIT_DATE)),
                lastExecutionMeasure = getFloatOrNull(getColumnIndex(LAST_EXECUTION_MEASURE)),
                lastExecutionDate = getStringOrNull(getColumnIndex(LAST_EXECUTION_DATE)),
                vgStatus = getString(getColumnIndex(VG_STATUS)),
                targetDate = getStringOrNull(getColumnIndex(TARGET_DATE)),
                manualDate = getStringOrNull(getColumnIndex(MANUAL_DATE)),
                partitionedTicketPrefix = getIntOrNull(getColumnIndex(PARTITIONED_TICKET_PREFIX)),
                partitionedTicketCode = getIntOrNull(getColumnIndex(PARTITIONED_TICKET_CODE)),
                partitionedUser = getStringOrNull(getColumnIndex(PARTITIONED_USER)),
                partitionedExecution = getIntOrNull(getColumnIndex(PARTITIONED_EXECUTION)),
                vgId = getStringOrNull(getColumnIndex(VG_ID)),
                vgDesc = getStringOrNull(getColumnIndex(VG_DESC)),
                valueSuffix = getStringOrNull(getColumnIndex(VALUE_SUFFIX)),
                restriction_decimal = getStringOrNull(getColumnIndex(RESTRICTION_DECIMAL)),
                isActive = getInt(getColumnIndex(IS_ACTIVE)),
                hasExpired = getInt(getColumnIndex(HAS_EXPIRED)),
            )
        }
    }

    fun getGeOsVgByForm(
        customerCode: Long,
        customFormType: Int,
        customFormCode: Int,
        customFormVersion: Int,
        customFormData: Long,
        vgCode: Int,
    ): GeOsVg? {
        return query(
            """
                SELECT *
                FROM $TABLE_NAME
                WHERE $VG_CODE = '$vgCode'
                AND $CUSTOMER_CODE = '$customerCode'
                AND $CUSTOM_FORM_TYPE = '$customFormType'
                AND $CUSTOM_FORM_CODE = '$customFormCode'
                AND $CUSTOM_FORM_VERSION = '$customFormVersion'
                AND $CUSTOM_FORM_DATA = '$customFormData'
               
            """.trimIndent()
        ).first()
    }

    fun getGeOsVgByFormCode(
        customerCode: Long,
        customFormType: Int,
        customFormCode: Int,
        customFormVersion: Int,
        customFormData: Long,
        productCode: Long,
        serialCode: Long,
    ): List<GeOsVg> {
        return query(
            """
                SELECT *
                FROM $TABLE_NAME
                WHERE $CUSTOMER_CODE = '$customerCode'
                AND $CUSTOM_FORM_TYPE = '$customFormType'
                AND $CUSTOM_FORM_CODE = '$customFormCode'
                AND $CUSTOM_FORM_VERSION = '$customFormVersion'
                AND $CUSTOM_FORM_DATA = '$customFormData'
                AND $PRODUCT_CODE = '$productCode'
                AND $SERIAL_CODE = '$serialCode'
                ORDER BY 
                    COALESCE($MANUAL_DATE, $TARGET_DATE, $VG_DESC) ASC
                    
            """.trimIndent()
        )
    }

    fun getGeOsByFormCode(
        customerCode: Long,
        customFormType: Int,
        customFormCode: Int,
        customFormVersion: Int,
        customFormData: Long
    ): List<FormVgs> {
        return query(
            """
                SELECT vg.*
                FROM $TABLE_NAME vg
                WHERE vg.$CUSTOMER_CODE = '$customerCode'
                AND vg.$CUSTOM_FORM_TYPE = '$customFormType'
                AND vg.$CUSTOM_FORM_CODE = '$customFormCode'
                AND vg.$CUSTOM_FORM_VERSION = '$customFormVersion'
                AND vg.$CUSTOM_FORM_DATA = '$customFormData'
                AND vg.$IS_ACTIVE = 1
            """.trimIndent()
        ).map { map ->
            FormVgs(
                customerCode = map.customerCode,
                customFormType = map.customFormType,
                customFormCode = map.customFormCode,
                customFormVersion = map.customFormVersion,
                customFormData = map.customFormData,
                vgCode = map.vgCode
            )
        }
    }

    fun createGeOsVg(
        customerCode: Long,
        customFormType: Int,
        customFormCode: Int,
        customFormVersion: Int,
        customFormData: Int,
        productCode: Int,
        serialCode: Int,
        valueSufix: String?,
        restrictionDecimal: Int?,
    ): MutableList<GeOsVg> {

        return query(
            """
               SELECT           
                        si.*,
                        $customFormType $CUSTOM_FORM_TYPE ,                   
                        $customFormCode  $CUSTOM_FORM_CODE ,                  
                        $customFormVersion  $CUSTOM_FORM_VERSION ,            
                        $customFormData  $CUSTOM_FORM_DATA ,                  
                        i.$VG_ID,                                          
                        i.$VG_DESC, 
                        '$valueSufix' $VALUE_SUFFIX,
                        $restrictionDecimal $RESTRICTION_DECIMAL,
                        0 $IS_ACTIVE,
                        0 $HAS_EXPIRED
                    FROM
                        ${MDProductSerialVGDao.TABLE_NAME} si,
                        ${MDVerificationGroupDao.TABLE} i
                    WHERE
                        si.${CUSTOMER_CODE} = i.${CUSTOMER_CODE}
                        AND si.${VG_CODE} = i.${VG_CODE}                                                                        
                        AND si.${CUSTOMER_CODE} = '$customerCode'                            
                        AND si.${PRODUCT_CODE} = '$productCode'                           
                        AND si.${SERIAL_CODE} = '$serialCode'
                        AND si.${MDProductSerialVGDao.VG_CODE} 
                                IN ( SELECT distinct serial.${MD_Product_Serial_Tp_Device_ItemDao.VG_CODE}
                                       from ${MD_Product_Serial_Tp_Device_ItemDao.TABLE} serial 
                                      where serial.${MDProductSerialVGDao.CUSTOMER_CODE} = '$customerCode'                            
                                        AND serial.${MDProductSerialVGDao.PRODUCT_CODE} = '$productCode'                           
                                        AND serial.${MDProductSerialVGDao.SERIAL_CODE} = '$serialCode' 
                                        AND serial.${MDProductSerialVGDao.VG_CODE} is not null
                                 )
                    ORDER BY 
                        si.${MDProductSerialVGDao.VG_CODE}                    
            """.trimIndent()
        )
    }

    fun getByVgCode(
        customer_code: Long,
        product_code: Int,
        serial_code: Int,
        vg_code: Int,
    ): GeOsVg? {
        return getByString(
            """SELECT * 
                 FROM ${TABLE_NAME} 
                WHERE ${CUSTOMER_CODE} = $customer_code 
                  AND ${PRODUCT_CODE} = $product_code 
                  AND ${SERIAL_CODE} = $serial_code 
                  AND ${VG_CODE} = $vg_code 
                LIMIT 1""".trimMargin()
        )
    }

}