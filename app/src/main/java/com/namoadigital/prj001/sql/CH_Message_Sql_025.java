package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 30/11/2017.
 */

public class CH_Message_Sql_025 implements Specification {

    public static final String BADGE_MESSAGES_QTY = "messages_qty";

    public CH_Message_Sql_025() {
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   count(1) " + BADGE_MESSAGES_QTY + " \n" +
                        " FROM\n" +
                        "   ch_messages ch\n" +
                        " WHERE\n" +
                        "   ch.read = '0' \n")
                .append(";" + BADGE_MESSAGES_QTY)
                .toString();
    }
}
