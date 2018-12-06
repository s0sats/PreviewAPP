package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_Service_Exec_Task_FileDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 06/07/17.
 *
 * Seleciona lista de task_iles pendentes de download
 */

public class SM_SO_Service_Exec_Task_File_Sql_003 implements Specification {
    public static final String FILE_LOCAL_NAME = "file_local_name";
    public static final String FILE_TYPE = "file_type";

    private long customer_code;

    public SM_SO_Service_Exec_Task_File_Sql_003(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "  f.customer_code,\n" +
                        "  f.so_prefix,\n" +
                        "  f.so_code,\n" +
                        "  f.price_list_code,\n" +
                        "  f.pack_code,\n" +
                        "  f.pack_seq,\n" +
                        "  f.category_price_code,\n" +
                        "  f.service_code,\n" +
                        "  f.service_seq,\n" +
                        "  f.exec_code,\n" +
                        "  f.task_code,\n" +
                        "  f.file_code,\n" +
                        "  f.file_url,\n" +
                        "  'SM_SO_'||f.customer_code||\"_\"||f.so_prefix||\"_\"||f.so_code||\"_\"||f.price_list_code||\"_\"||f.pack_code||\"_\"||f.pack_seq||\"_\"||f.category_price_code||\"_\"||f.service_code||\"_\"||f.service_seq||\"_\"||f.exec_code||\"_\"||f.task_code||\"_\"||f.file_code "+FILE_LOCAL_NAME+",\n" +
                        "  '"+SM_SO_Service_Exec_Task_FileDao.TABLE +"' "+FILE_TYPE +"\n"+
                        "  \n" +
                        " FROM\n" +
                        SM_SO_Service_Exec_Task_FileDao.TABLE +" f\n" +
                        " WHERE\n" +
                        "  f.customer_code = '"+customer_code+"'\n" +
                        "  and f.file_url <> ''" +
                        "  and f.file_url_local = ''")
                .append(";")
                //.append("customer_code#so_prefix#so_code#price_list_code#pack_code#pack_seq#category_price_code#service_code#service_seq#exec_code#task_code#file_code#file_url#"+FILE_LOCAL_NAME+"#"+FILE_TYPE)
                .toString();
    }
}
