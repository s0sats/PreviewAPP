package com.namoadigital.prj001.sql

import android.content.Context
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao
import com.namoadigital.prj001.dao.MdTagDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp.PREFERENCE_HOME_CURRENT_SITE_OPTION
import com.namoadigital.prj001.util.ConstantBaseApp.PREFERENCE_HOME_ONLY_MY_ACTIONS_OPTION
import com.namoadigital.prj001.util.ToolBox_Con

class SqlAct005FormAp001(private val context: Context,
                          private val customerCode: Int,
                          private val periodFilter: String,
                          private val sitesFilter: String,
                          private val focusFilter: String
                          ) : Specification {

    private val ticketFilter:String  by lazy{
        var filter =  ""
        filter = when(sitesFilter){
            PREFERENCE_HOME_CURRENT_SITE_OPTION -> "tk.site_code = " + ToolBox_Con.getPreference_Site_Code(context)
            else -> ""
        }
        //
        filter += when (focusFilter) {
            PREFERENCE_HOME_ONLY_MY_ACTIONS_OPTION -> if (filter.isEmpty()) "tk.user_focus = 1" else "\n and tk.user_focus = 1"
            else -> if (filter.isEmpty()) "tk.user_focus = 0" else "\n and tk.user_focus = 0"
        }

        //
        if(periodFilter.equals(PREFERENCE_HOME_ONLY_MY_ACTIONS_OPTION)){
            filter = "strftime('%s' ,tk.forecast_date ||' \"+03:00+\"') * 1000 > DATE('now')"
        }
        //

        filter
    }

    private val formFilter:String  by lazy{
        var filter =  ""
        if(sitesFilter.equals(PREFERENCE_HOME_CURRENT_SITE_OPTION)){
            filter = "geform.site_code = " + ToolBox_Con.getPreference_Site_Code(context)
        }
        //
        if(periodFilter.equals(PREFERENCE_HOME_ONLY_MY_ACTIONS_OPTION)){
            filter = "strftime('%s' ,tk.forecast_date ||' \"+03:00+\"') * 1000 > DATE('now')"
        }
        //
        filter
    }

    private val scheduleFilter:String  by lazy{
        var filter =  ""
        if(sitesFilter.equals(PREFERENCE_HOME_CURRENT_SITE_OPTION)){
            filter = "geform.site_code = " + ToolBox_Con.getPreference_Site_Code(context)
        }
        //
        if(periodFilter.equals(PREFERENCE_HOME_ONLY_MY_ACTIONS_OPTION)){
            filter = "strftime('%s' ,tk.forecast_date ||' \"+03:00+\"') * 1000 > DATE('now')"
        }
        //
        filter
    }

    private val formApFilter:String  by lazy{
        var filter = """"""
        filter
    }


    override fun toSqlQuery(): String? {
        val sb = StringBuilder()
        return sb.append(
                """select 
                        mdt.${MdTagDao.TAG_CODE}, 
                        mdt.${MdTagDao.TAG_DESC}, 
                        count(geap.${GE_Custom_Form_ApDao.TAG_OPERATIONAL_CODE}),
                         0 updated_required,
                         0 sync_required
                          from ${MdTagDao.TABLE} mdt
                        inner join ${GE_Custom_Form_ApDao.TABLE} geap
                        
                            on  mdt.${MdTagDao.TAG_CODE} = geap.${GE_Custom_Form_ApDao.TAG_OPERATIONAL_CODE}
                         where 
                        geap.${GE_Custom_Form_ApDao.CUSTOMER_CODE} = '$customerCode'
                        and geap.${GE_Custom_Form_ApDao.AP_STATUS} not in ('${Constant.SYS_STATUS_DONE}','${Constant.SYS_STATUS_CANCELLED}')
                        group by mdt.${MdTagDao.TAG_CODE} """

//                "select mdt.tag_desc, count(mdt.tag_code) \n" +
//                "  from " + MdTagDao.TABLE +" mdt\n" +
//                " inner join " + GE_Custom_Form_LocalDao.TABLE +" geform \n" +
//                "    on  mdt.tag_code = geform.tag_operational_code \n" +
//                " where " +
//                formFilter +
//                " group by mdt.tag_code"
//              + "union \n" +
//                "select mdt.tag_desc, count(mdt.tag_code)\n" +
//                "  from md_tag mdt\n" +
//                " inner join "+ TK_TicketDao.TABLE + " tk\n" +
//                "    on  mdt.tag_code = tk.tag_operational_code\n" +
//                " where tk.ticket_status = " + ConstantBaseApp.SYS_STATUS_PROCESS + "\n"+
//                ticketFilter +
//                " group by mdt.tag_code\n"
        )
                .append(";")
                .toString()
    }
}
