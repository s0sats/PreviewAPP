package com.namoadigital.prj001.dao.md

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.namoadigital.prj001.core.database.base.BaseDaoWithReturn
import com.namoadigital.prj001.model.region.MDRegion
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con

class MDRegionDao(
    context: Context,
) : BaseDaoWithReturn<MDRegion>(
    context,
    TABLE_NAME,
    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
    Constant.DB_VERSION_CUSTOM,
    Constant.DB_MODE_MULTI
) {

    companion object {
        const val TABLE_NAME = "md_region"
        const val REGION_ID = "region_id"
        const val CUSTOMER_CODE = "customer_code"
        const val REGION_CODE = "region_code"
        const val REGION_DESC = "region_desc"
    }

    override fun getWherePkClause(item: MDRegion?): StringBuilder {
        item?.let {
            return StringBuilder().append(
                "" +
                        "$CUSTOMER_CODE = '${item.customerCode}'" +
                        " AND $REGION_ID = '${item.code}'"
            )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    override fun modelToContentValues(
        model: MDRegion?,
        contentValues: ContentValues
    ): ContentValues {
        model?.let {
            contentValues.put(CUSTOMER_CODE, it.customerCode)
            contentValues.put(REGION_CODE, it.code)
            contentValues.put(REGION_ID, it.id)
            contentValues.put(REGION_DESC, it.desc)
        }
        return contentValues
    }

    @SuppressLint("Range")
    override fun cursorToModel(cursor: Cursor): MDRegion {
        with(cursor) {
            return MDRegion(
                customerCode = getInt(getColumnIndex(CUSTOMER_CODE)),
                code = getInt(getColumnIndex(REGION_CODE)),
                id = getString(getColumnIndex(REGION_ID)),
                desc = getString(getColumnIndex(REGION_DESC)),
            )
        }
    }

}

