package com.namoadigital.prj001.core.database.base

import android.content.Context
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con

abstract class NamoaCustomDatabase<T> constructor(
    context: Context,
    tableName: String,
) : BaseDaoWithReturn<T>(
    context = context,
    tableName = tableName,
    mDBName = ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
    mDBVersion = Constant.DB_VERSION_CUSTOM,
    mDBMode = Constant.DB_MODE_MULTI
)