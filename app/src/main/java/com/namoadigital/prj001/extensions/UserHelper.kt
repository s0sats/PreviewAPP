package com.namoadigital.prj001.extensions

import android.content.Context
import com.namoadigital.prj001.util.ToolBox_Con


fun Context.getUserCode() = ToolBox_Con.getPreference_User_Code(this)
fun Context.getUserNick() = ToolBox_Con.getPreference_User_Code_Nick(this)
fun Context.getUserName() = ToolBox_Con.getPreference_Last_User_Nick_Logged(this)
fun Context.getUserSessionAPP() = ToolBox_Con.getPreference_Session_App(this)