package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_FileDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by dluche on 17/07/2017.
 *
 * Seleciona file pendentes do cabeçalho de Sum SO especifica
 *
 */

public class SM_SO_File_Sql_005 implements Specification {
    public static final String FILE_LOCAL_NAME = "file_local_name";
    public static final String FILE_TYPE = "file_type";
    private long customer_code;
    private int so_prefix;
    private int so_code;

    public SM_SO_File_Sql_005(long customer_code, int so_prefix, int so_code) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "  f.customer_code,\n" +
                        "  f.so_prefix,\n" +
                        "  f.so_code,\n" +
                        "  f.file_code,\n" +
                        "  f.file_name,\n" +
                        "  f.file_url,\n" +
                        "  'SM_SO_'||f.customer_code||\"_\"||f.so_prefix||\"_\"||f.so_code||\"_\"||f.file_code "+FILE_LOCAL_NAME+",\n" +
                        "'"+SM_SO_FileDao.TABLE +"' "+FILE_TYPE +"\n"+
                        " \n" +
                        " FROM\n" +
                        SM_SO_FileDao.TABLE +" f\n" +
                        " WHERE\n" +
                        "  f.customer_code = '"+customer_code+"'\n" +
                        "  and f.so_prefix = '"+so_prefix+"'\n" +
                        "  and f.so_code = '"+so_code+"'\n" +
                        "  and f.file_url_local = ''")
                .append(";customer_code#so_prefix#so_code#file_code#file_name#file_url#"+FILE_LOCAL_NAME+"#"+FILE_TYPE)
                .toString();
    }
}
