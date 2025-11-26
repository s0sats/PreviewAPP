package com.namoadigital.prj001.dao.md

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.namoadigital.prj001.core.database.base.BaseDaoWithReturn
import com.namoadigital.prj001.model.MDVerificationGroup
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con

class MDVerificationGroupDao(
    context: Context,
) : BaseDaoWithReturn<MDVerificationGroup>(
    context,
    TABLE,
    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
    Constant.DB_VERSION_CUSTOM,
    Constant.DB_MODE_MULTI,
) {
    companion object {
        const val TABLE = "md_verification_group"
        const val CUSTOMER_CODE = "customer_code"
        const val VG_CODE = "vg_code"
        const val VG_ID = "vg_id"
        const val VG_DESC = "vg_desc"
        const val EXEC_ONLY_PREVENTIVE = "exec_only_preventive"

    }


    override fun cursorToModel(cursor: Cursor): MDVerificationGroup? {
        with(cursor) {
            return MDVerificationGroup(
                customerCode = getInt(getColumnIndex(CUSTOMER_CODE)),
                vgCode = getInt(getColumnIndex(VG_CODE)),
                vgId = getString(getColumnIndex(VG_ID)),
                vgDesc = getString(getColumnIndex(VG_DESC)),
                execOnlyPreventive = getInt(getColumnIndex(EXEC_ONLY_PREVENTIVE)),
            )
        }
    }

    override fun modelToContentValues(
        model: MDVerificationGroup?,
        contentValues: ContentValues
    ): ContentValues {
        model?.let {
            with(contentValues) {
                //
                if (model.customerCode > -1) {
                    put(CUSTOMER_CODE, model.customerCode)
                }
                //
                if (model.vgCode > -1) {
                    put(VG_CODE, model.vgCode)
                }
                //
                put(VG_ID, model.vgId)
                //
                put(VG_DESC, model.vgDesc)
                //
                if (model.execOnlyPreventive > -1) {
                    put(EXEC_ONLY_PREVENTIVE, model.execOnlyPreventive)
                }
            }
        }
        //
        return contentValues
    }

    @Throws(java.lang.Exception::class)
    override fun getWherePkClause(item: MDVerificationGroup?): StringBuilder {
        item?.let {
            return java.lang.StringBuilder()
                .append(
                    """
                        $CUSTOMER_CODE = '${item.customerCode}'  
                        AND ${VG_CODE} = '${item.vgCode}'                           
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    fun getVgDesc(customerCode: Long, vgCode: Int): String? {
        val verificationGroup = getByString(
            """ SELECT * 
                  FROM $TABLE
                 WHERE $CUSTOMER_CODE = $customerCode
                   AND $VG_CODE = $vgCode
            """.trimIndent()
        )
        return verificationGroup?.vgDesc
    }


}