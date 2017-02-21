package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class GE_File_Sql_003 implements Specification {


    public GE_File_Sql_003() {
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append("select ifnull(max(file_code),0) + 1 next_code from ge_files")
                .append(";next_code")

                .toString();
    }
}
