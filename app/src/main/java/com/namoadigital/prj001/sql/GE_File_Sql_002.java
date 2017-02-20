package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class GE_File_Sql_002 implements Specification {

    private String l_file_code;

    public GE_File_Sql_002(String l_file_code) {
        this.l_file_code = l_file_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append("update \n" +
                        "\n" +
                        "ge_files \n" +
                        "\n" +
                        "set \n" +
                        "\n")
                .append("file_status = '").append("SENT").append("' \n" +
                        "\n" +
                        "where \n" +
                        "\n")
                .append("file_code = '").append(l_file_code).append("' ")
                .append(";")

                .toString();
    }
}
