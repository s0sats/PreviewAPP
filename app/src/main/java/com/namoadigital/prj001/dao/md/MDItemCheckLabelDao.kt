package com.namoadigital.prj001.dao.md

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.namoadigital.prj001.core.database.base.NamoaCustomDatabase
import com.namoadigital.prj001.dao.md.MDItemCheckLabelIconDao.Companion.LABEL_ICON
import com.namoadigital.prj001.model.masterdata.label.MDItemCheckLabel
import com.namoadigital.prj001.ui.act086.frg_verification.form_utils.FormItemCheckLabelIcon
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MDItemCheckLabelDao @Inject constructor(
    @ApplicationContext context: Context,
) : NamoaCustomDatabase<MDItemCheckLabel>(
    context = context,
    tableName = TABLE_NAME,
) {
    override fun cursorToModel(cursor: Cursor): MDItemCheckLabel {
        return  with(cursor) {
            MDItemCheckLabel(
                customerCode = getLong(getColumnIndex(CUSTOMER_CODE)),
                labelCode = getInt(getColumnIndex(LABEL_CODE)),
                labelType = getString(getColumnIndex(LABEL_TYPE)),
                labelId = getString(getColumnIndex(LABEL_ID)),
                labelDesc = getString(getColumnIndex(LABEL_DESC)),
                labelIconId = getString(getColumnIndex(LABEL_ICON_ID)),
                active = getInt(getColumnIndex(ACTIVE)),
            )
        }
    }

    override fun modelToContentValues(
        model: MDItemCheckLabel?,
        contentValues: ContentValues
    ): ContentValues {
        model?.let {
            with(contentValues) {
                put(CUSTOMER_CODE, it.customerCode)
                put(LABEL_CODE, it.labelCode)
                put(LABEL_TYPE, it.labelType)
                put(LABEL_ID, it.labelId)
                put(LABEL_DESC, it.labelDesc)
                put(LABEL_ICON_ID, it.labelIconId)
                put(ACTIVE, it.active)
            }
        }
        return contentValues
    }

    override fun getWherePkClause(item: MDItemCheckLabel?): StringBuilder {
        item?.let {
            return java.lang.StringBuilder()
                .append(
                    """
                        $CUSTOMER_CODE = '${item.customerCode}'  
                        AND $LABEL_CODE = '${item.labelCode}'                                                      
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    companion object {
        const val TABLE_NAME = "md_item_check_label"
        const val CUSTOMER_CODE = "customer_code"
        const val LABEL_CODE = "label_code"
        const val LABEL_TYPE = "label_type"
        const val LABEL_ID = "label_id"
        const val LABEL_DESC = "label_desc"
        const val LABEL_ICON_ID = "label_icon_id"
        const val ACTIVE = "active"
    }

    fun getItemCheckLabelIcons(labelCode: Int): FormItemCheckLabelIcon {
        val query = getByStringHM(
            sQuery = """
                SELECT l.${LABEL_DESC},
                       i.${LABEL_ICON}
                  FROM ${MDItemCheckLabelIconDao.TABLE_NAME} i,
                       $TABLE_NAME l
                  WHERE i.${MDItemCheckLabelIconDao.LABEL_ICON_ID} = l.${LABEL_ICON_ID} 
                    AND l.${LABEL_CODE}  = $labelCode
            """.trimIndent()
        )
        return FormItemCheckLabelIcon(
            labelCode = labelCode,
            itemCheckLabel = query?.get(LABEL_DESC) ,
            labelIcon = query?.get(LABEL_ICON)
        )
    }

    fun getItemCheckLabelIconsList(): List<FormItemCheckLabelIcon> {
        val query = query_HM(
            sQuery = """
                SELECT l.${LABEL_CODE},
                       l.${LABEL_DESC},
                       i.${LABEL_ICON}
                  FROM $TABLE_NAME l
            INNER JOIN ${MDItemCheckLabelIconDao.TABLE_NAME} i
                    ON l.${LABEL_ICON_ID} = i.${MDItemCheckLabelIconDao.LABEL_ICON_ID}      
              GROUP BY l.${LABEL_CODE}
              ORDER BY l.${LABEL_CODE}
            """.trimIndent()
        )
        return query.map{
            FormItemCheckLabelIcon(
                labelCode =  it.get(LABEL_CODE)!!.toInt(),
                itemCheckLabel = it.get(LABEL_DESC) ,
                labelIcon = it.get(LABEL_ICON)
            )
        }
    }


}
