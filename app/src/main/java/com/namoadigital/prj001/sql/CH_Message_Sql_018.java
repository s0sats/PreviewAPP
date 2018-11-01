package com.namoadigital.prj001.sql;

import android.content.Context;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Con;

/**
 * Created by d.luche on 16/01/2018.
 * Seleciona as msg do remetente que tem all_delivered ou all_read 0
 *
 */

public class CH_Message_Sql_018 implements Specification {

    private String customer_filter_list;
    private String user_code;
    private String room_codeFilter = "";
    private String translateMsgStr = "\"type\":\"TRANSLATE\"";

    public CH_Message_Sql_018(Context context,String customer_filter_list, String user_code, String room_code) {
        if(customer_filter_list != null && customer_filter_list.length() > 0){
            this.customer_filter_list = " r.customer_code in ("+customer_filter_list+") \n";
        }else{
            this.customer_filter_list = " r.customer_code in ("+ ToolBox_Con.getPreference_Customer_Code(context)+")\n";
        }
        this.user_code = user_code;
        this.room_codeFilter = "   and m.room_code = '"+room_code+"'\n";
    }

    public CH_Message_Sql_018(Context context,String customer_filter_list, String user_code) {
        if(customer_filter_list != null && customer_filter_list.length() > 0){
            this.customer_filter_list = " r.customer_code in ("+customer_filter_list+") \n";
        }else{
            this.customer_filter_list = " r.customer_code in ("+ ToolBox_Con.getPreference_Customer_Code(context)+")\n";
        }
        this.user_code = user_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "  m.msg_prefix,\n" +
                        "  m.msg_code,\n" +
                        "  m.msg_obj\n" +
                        " FROM\n" +
                        "   "+ CH_RoomDao.TABLE+" r\n" +
                        " LEFT JOIN  \n" +
                        "   "+ CH_MessageDao.TABLE+" m on m.room_code = r.room_code\n" +
                        " WHERE\n" +
                            customer_filter_list +
                        "   and m.user_code = '"+user_code+"'\n" +
                                room_codeFilter +
                        "   and (m.all_delivered = 0 OR m.all_read = 0)\n" +
                        "   and instr(m.msg_obj, '"+translateMsgStr+"') = 0\n" +
                        " ORDER BY\n" +
                        "   m.msg_pk ")
                .append(";")
                //.append(CH_MessageDao.MSG_PREFIX+"#"+CH_MessageDao.MSG_CODE)
                .toString();
    }
}
