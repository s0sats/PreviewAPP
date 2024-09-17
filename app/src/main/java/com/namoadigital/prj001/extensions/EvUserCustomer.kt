package com.namoadigital.prj001.extensions

import android.content.Context
import com.namoadigital.prj001.dao.EV_User_CustomerDao
import com.namoadigital.prj001.util.ToolBox_Con

fun Context.hasAutomaticOperationRestriction():Boolean{
    val dao = EV_User_CustomerDao(this)
    val loggedUserCustomer = dao.loggedUserCustomer
    return loggedUserCustomer.automatic_operation_restriction == 1
            && loggedUserCustomer.automatic_operation_code  == ToolBox_Con.getPreference_Operation_Code(this)
}