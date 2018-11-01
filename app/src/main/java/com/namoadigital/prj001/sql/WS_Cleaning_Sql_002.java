package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 23/02/17.
 */

public class WS_Cleaning_Sql_002 implements Specification {

    private String s_date;

    public WS_Cleaning_Sql_002(String s_date) {
        this.s_date = s_date;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();

        return sb
                .append("select * FROM ge_files where file_status = 'SENT' and Date(file_date) <")
                .append("Date('")
                .append(s_date)
                .append("');")
                //.append(";file_code#file_path#file_status#file_date")
                .toString();
    }
}
