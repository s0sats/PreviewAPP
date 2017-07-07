package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_FileDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 06/07/17.
 */

public class SM_SO_File_Sql_001 implements Specification {

    private long customer_code;
    private int so_prefix;
    private int so_code;
    private int file_code;

    public SM_SO_File_Sql_001(long customer_code, int so_prefix, int so_code, int file_code) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
        this.file_code = file_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "      S.*\n" +
                        " FROM\n" +
                        SM_SO_FileDao.TABLE + " S\n" +
                        " WHERE\n" +
                        "    S.customer_code =  '" + customer_code + "'\n" +
                        "    AND S.so_prefix =  '" + so_prefix + "'\n" +
                        "    AND S.so_code =    '" + so_code + "'\n" +
                        "    AND S.file_code =  '" + file_code + "' ")
                .toString();
    }
}
