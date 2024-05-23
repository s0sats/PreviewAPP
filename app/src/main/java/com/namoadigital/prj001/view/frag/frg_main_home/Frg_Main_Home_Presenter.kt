package com.namoadigital.prj001.view.frag.frg_main_home

import android.content.Context
import com.namoadigital.prj001.dao.EV_User_CustomerDao
import com.namoadigital.prj001.model.EV_User_Customer
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_002
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con

class Frg_Main_Home_Presenter(
    private val context: Context,
    private val mView: Frg_Main_Home_Contract.View,
) : Frg_Main_Home_Contract.Presenter {
    override fun hasFieldServiceEnable(): Boolean {
        val userCustomer: EV_User_Customer? = getEvUserCustomer();
        return userCustomer != null && userCustomer.field_service == 1;
    }

    private fun getEvUserCustomer(): EV_User_Customer? {
        val userCustomerDao = EV_User_CustomerDao(
            context,
            Constant.DB_FULL_BASE,
            Constant.DB_VERSION_BASE
        )
        //
        return userCustomerDao.getByString(
            EV_User_Customer_Sql_002(
                ToolBox_Con.getPreference_User_Code(context),
                ToolBox_Con.getPreference_Customer_Code(context).toString()
            ).toSqlQuery()
        )
    }


}