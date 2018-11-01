package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 01/03/2017.
 */

public class FCMMessage_Sql_003 implements Specification {

    public static final String BADGE_MESSAGES_QTY = "messages_qty";

    public FCMMessage_Sql_003() {
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   count(1) " + BADGE_MESSAGES_QTY + " \n" +
                        " FROM\n" +
                        "   fcmmessages f\n" +
                        " WHERE\n" +
                        "   f.status = '0' \n")
                .append(";")
                //.append(BADGE_MESSAGES_QTY)
                .toString();
    }
}
