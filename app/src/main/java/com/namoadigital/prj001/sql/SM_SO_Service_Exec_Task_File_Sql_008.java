package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_Service_Exec_Task_FileDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by dluche on 19/07/17.
 *
 * Seleciona todas task files sem url e para processar a url_local
 */

public class SM_SO_Service_Exec_Task_File_Sql_008 implements Specification {

    private long customer_code;
    private int so_prefix;
    private int so_code;
    private int price_list_code;
    private int pack_code;
    private int pack_seq;
    private int category_price_code;
    private int service_code;
    private int service_seq;
    private long exec_tmp;
    private long task_tmp;
    private long file_tmp;

    public SM_SO_Service_Exec_Task_File_Sql_008(long customer_code, int so_prefix, int so_code, int price_list_code, int pack_code, int pack_seq, int category_price_code, int service_code, int service_seq, long exec_tmp, long task_tmp, long file_tmp) {
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
        this.task_tmp = task_tmp;
        this.file_tmp = file_tmp;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" DELETE \n" +
                        " FROM\n" +
                        SM_SO_Service_Exec_Task_FileDao.TABLE + " \n" +
                        " WHERE\n" +
                        "  customer_code = '"+customer_code+"'\n" +
                        "  AND so_prefix = '"+so_prefix+"'\n" +
                        "  AND so_code = '"+so_code+"' \n" +
                        "  AND price_list_code = '"+price_list_code+"'\n" +
                        "  AND pack_code = '"+pack_code+"' \n" +
                        "  AND pack_seq = '"+pack_seq+"' \n" +
                        "  AND category_price_code = '"+category_price_code+"'\n" +
                        "  AND service_code = '"+service_code+"'\n" +
                        "  AND service_seq = '"+service_seq+"'\n" +
                        "  AND exec_code = '"+exec_tmp+"' \n" +
                        "  AND task_code ='"+task_tmp+"' \n" +
                        "  AND file_code = '"+file_tmp+"'")
                .toString();
    }
}
