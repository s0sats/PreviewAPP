package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_Service_Exec_Task_FileDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by dluche on 19/07/17.
 *
 * Seleciona todas task files sem url e para processar a url_local
 */

public class SM_SO_Service_Exec_Task_File_Sql_007 implements Specification {

    private long customer_code;
    private int so_prefix;
    private int so_code;

    public SM_SO_Service_Exec_Task_File_Sql_007(long customer_code, int so_prefix, int so_code) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "      S.*\n" +
                        " FROM\n" +
                        SM_SO_Service_Exec_Task_FileDao.TABLE + " S\n" +
                        " WHERE\n" +
                        "    S.customer_code =              '" + customer_code + "'\n" +
                        "    AND S.so_prefix =              '" + so_prefix + "'\n" +
                        "    AND S.so_code =                '" + so_code + "'\n" +
                        "    AND (S.file_url is null OR S.file_url ='' )")
                .toString();
    }
}
