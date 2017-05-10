package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.FCMMessageDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 08/02/17.
 */

public class FCMMessage_Sql_005 implements Specification {

    private String s_fcmmessage_code;

    public FCMMessage_Sql_005(String s_fcmmessage_code) {
        this.s_fcmmessage_code = s_fcmmessage_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT " +
                        "      *, customer_name " +
                        " FROM ")
                .append(FCMMessageDao.TABLE + " inner join ev_user_customers on fcmmessages.customer = ev_user_customers.customer_code ")
                .append(" WHERE " +
                        FCMMessageDao.FCMMESSAGE_CODE + " = '" + s_fcmmessage_code + "' ")
                .append(";fcmmessage_code#customer#type#title#msg_short#msg_long#module#sender#status#date_create_ms#customer_name")
                .toString();
    }
}
