package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 08/02/17.
 */

public class FCMMessage_Sql_004 implements Specification {

    private String s_fcmmessage_code;

    public FCMMessage_Sql_004(String s_fcmmessage_code) {
        this.s_fcmmessage_code = s_fcmmessage_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append("update \n" +
                        "\n" +
                        "fcmmessages \n" +
                        "\n" +
                        "set\n" +
                        "\n")
                .append("status = '").append(1).append("' \n" +
                        "\n" +
                        "where \n" +
                        "\n")
                .append("fcmmessage_code = '").append(s_fcmmessage_code).append("' \n")
                .append(";")
                .toString();
    }
}
