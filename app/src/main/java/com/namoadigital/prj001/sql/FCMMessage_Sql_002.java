package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 01/03/2017.
 */

public class FCMMessage_Sql_002 implements Specification {

    public static final String FCMMESSAGE_CODE = "fcmmessage_code";

    public FCMMessage_Sql_002() {
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();

        return sb
                .append("SELECT \n" +
                        "\tfcmmessage_code \n" +
                        "FROM   \n" +
                        "\tfcmmessages\n" +
                        " WHERE   \n" +
                        "\tfcmmessage_code = (SELECT MAX(fcmmessage_code)  FROM fcmmessages)")
                .append(";")
                .append("fcmmessage_code")
                .toString();
    }
}
