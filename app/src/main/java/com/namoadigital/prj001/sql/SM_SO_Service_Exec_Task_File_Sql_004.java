package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_Service_Exec_Task_FileDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 06/07/17.
 *
 * Atualiza file_url_local de task_file baixado
 */

public class SM_SO_Service_Exec_Task_File_Sql_004 implements Specification {

    private String customer_code;
    private String so_prefix;
    private String so_code;
    private String price_list_code;
    private String pack_code;
    private String pack_seq;
    private String category_price_code;
    private String service_code;
    private String service_seq;
    private String exec_code;
    private String task_code;
    private String file_code;
    private String file_url_local;

    public SM_SO_Service_Exec_Task_File_Sql_004(String customer_code, String so_prefix, String so_code, String price_list_code, String pack_code, String pack_seq, String category_price_code, String service_code, String service_seq, String exec_code, String task_code, String file_code, String file_url_local) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
        this.price_list_code = price_list_code;
        this.pack_code = pack_code;
        this.pack_seq = pack_seq;
        this.category_price_code = category_price_code;
        this.service_code = service_code;
        this.service_seq = service_seq;
        this.exec_code = exec_code;
        this.task_code = task_code;
        this.file_code = file_code;
        this.file_url_local = file_url_local;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" UPDATE " + SM_SO_Service_Exec_Task_FileDao.TABLE +"\n" +
                        "  SET file_url_local = '"+file_url_local+"'\n" +
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
                        "  AND exec_code = '"+exec_code+"' \n" +
                        "  AND task_code ='"+task_code+"' \n" +
                        "  AND file_code = '"+file_code+"'")
                .toString();
    }
}
