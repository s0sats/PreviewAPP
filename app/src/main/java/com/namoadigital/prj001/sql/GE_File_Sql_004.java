package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by DANIEL.LUCHE on 22/02/2017.
 *
 * Seleciona todos os registros mais antigos que a 5 dias atras.
 */

public class GE_File_Sql_004 implements Specification {

    private String date;

    public GE_File_Sql_004() {
        Calendar cDate =  Calendar.getInstance();
        cDate.add(Calendar.DATE,-5);
        SimpleDateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-dd");
        this.date = dateFormat.format(cDate.getTime());
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   fs.file_code,\n" +
                        "   fs.file_path,\n" +
                        "   fs.file_status,\n" +
                        "   fs.file_date\n" +
                        " FROM\n " +
                        "   ge_files fs\n " +
                        " WHERE\n" +
                        "   Date(fs.file_date) <= Date('"+date+"');")
                .toString();
    }
}
