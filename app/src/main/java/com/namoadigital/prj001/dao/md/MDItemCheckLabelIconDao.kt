package com.namoadigital.prj001.dao.md

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.namoadigital.prj001.core.database.base.NamoaCustomDatabase
import com.namoadigital.prj001.model.masterdata.label.MDItemCheckLabelIcon
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MDItemCheckLabelIconDao  @Inject constructor(
    @ApplicationContext context: Context,
) : NamoaCustomDatabase<MDItemCheckLabelIcon>(
    context = context,
    tableName = TABLE_NAME,
) {
    override fun cursorToModel(cursor: Cursor): MDItemCheckLabelIcon? {
        return  with(cursor) {
            MDItemCheckLabelIcon(
                labelIconId = getString(getColumnIndex(LABEL_ICON_ID)),
                labelIcon = getString(getColumnIndex(LABEL_ICON)),
                labelIconDesc = getString(getColumnIndex(LABEL_ICON_DESC)),
            )
        }
    }

    override fun modelToContentValues(
        model: MDItemCheckLabelIcon?,
        contentValues: ContentValues
    ): ContentValues {
        model?.let {
            with(contentValues) {
                put(LABEL_ICON_ID, it.labelIconId)
                put(LABEL_ICON, it.labelIcon)
                put(LABEL_ICON_DESC, it.labelIconDesc)
            }
        }
        return contentValues
    }

    override fun getWherePkClause(item: MDItemCheckLabelIcon?): StringBuilder {
        item?.let {
            return java.lang.StringBuilder()
                .append(
                    """
                        $LABEL_ICON_ID = '${item.labelIconId}'                                                    
                        """.trimIndent()
                )
        }
        throw Exception("NULL_OBJ_RECEIVED")
    }

    companion object {
        const val TABLE_NAME = "md_item_check_label_icon"
        const val LABEL_ICON_ID = "label_icon_id"
        const val LABEL_ICON = "label_icon"
        const val LABEL_ICON_DESC = "label_icon_desc"
    }



}
