package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 22/02/2017.
 *
 * Delete varios registro de um vez, desde que enviados concatenados por ,
 * Ex.: 1,2,3,45,89
 */

public class GE_File_Sql_005 implements Specification {

    private String  file_code_list;

    public GE_File_Sql_005(String file_code_list) {
        this.file_code_list = file_code_list;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();

        return sb
                .append(" DELETE   \n" +
                        " FROM\n" +
                        "   ge_files \n" +
                        " WHERE\n" +
                        "   file_code in("+file_code_list+");\n")
                .toString();
    }
}
