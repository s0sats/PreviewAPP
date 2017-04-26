package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.FCMMessageDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Data_FieldDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 08/02/17.
 */

public class FCMMessage_Sql_006 implements Specification {

    private String s_date_create_ms;

    public FCMMessage_Sql_006(String s_date_create_ms) {
        this.s_date_create_ms = s_date_create_ms;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" DELETE\n" +
                        " FROM\n" +
                        FCMMessageDao.TABLE + " \n " +
                        " WHERE\n " +
                        "   date_create_ms < '" + s_date_create_ms + "'\n " +
                        "   AND status = '1' \n")
                .append((";"))
                .toString();
    }
}
