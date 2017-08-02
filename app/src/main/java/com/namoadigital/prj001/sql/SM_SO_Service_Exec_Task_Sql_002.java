package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_Service_Exec_TaskDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 06/07/17.
 */

public class SM_SO_Service_Exec_Task_Sql_002 implements Specification {

    private long customer_code;
    private int so_prefix;
    private int so_code;
    private int price_list_code;
    private int pack_code;
    private int pack_seq;
    private int category_price_code;
    private int service_code;
    private int service_seq;
    //private int exec_code;
    private long exec_tmp;


    public SM_SO_Service_Exec_Task_Sql_002(long customer_code, int so_prefix, int so_code, int price_list_code, int pack_code, int pack_seq, int category_price_code, int service_code, int service_seq, long exec_tmp) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
        this.price_list_code = price_list_code;
        this.pack_code = pack_code;
        this.pack_seq = pack_seq;
        this.category_price_code = category_price_code;
        this.service_code = service_code;
        this.service_seq = service_seq;
        this.exec_tmp = exec_tmp;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "      S.*\n" +
                        " FROM\n" +
                        SM_SO_Service_Exec_TaskDao.TABLE + " S\n" +
                        " WHERE\n" +
                        "    S.customer_code =              '" + customer_code + "'\n" +
                        "    AND S.so_prefix =              '" + so_prefix + "'\n" +
                        "    AND S.so_code =                '" + so_code + "'\n" +
                        "    AND S.price_list_code =        '" + price_list_code + "'\n" +
                        "    AND S.pack_code =              '" + pack_code + "'\n" +
                        "    AND S.pack_seq =               '" + pack_seq + "'\n" +
                        "    AND S.category_price_code =    '" + category_price_code + "'\n" +
                        "    AND S.service_code =           '" + service_code + "'\n" +
                        "    AND S.service_seq =            '" + service_seq + "'\n" +
                        "    AND S.exec_tmp =              '" + exec_tmp + "' ")
                .toString();
    }
}
