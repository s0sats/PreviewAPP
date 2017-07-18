package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_Service_Exec_Task_FileDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by dluche on 18/07/17.
 * <p>
 * Max + 1
 */

public class SM_SO_Service_Exec_Task_File_Sql_005 implements Specification {

    public static final String NEXT_TMP = "next_tmp";

    private String customer_code;
    private String so_prefix;
    private String so_code;
    private String price_list_code;
    private String pack_code;
    private String pack_seq;
    private String category_price_code;
    private String service_code;
    private String service_seq;
    private String exec_tmp;
    private String task_tmp;

    public SM_SO_Service_Exec_Task_File_Sql_005(String customer_code, String so_prefix, String so_code, String price_list_code, String pack_code, String pack_seq, String category_price_code, String service_code, String service_seq, String exec_tmp, String task_tmp) {
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
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   IFNULL(max(file_tmp),300) + 1 "+NEXT_TMP+"\n" +
                        " FROM   " +
                        SM_SO_Service_Exec_Task_FileDao.TABLE + "\n" +
                        " WHERE\n" +
                        "  customer_code = '" + customer_code + "'\n" +
                        "  AND so_prefix = '" + so_prefix + "'\n" +
                        "  AND so_code = '" + so_code + "' \n" +
                        "  AND price_list_code = '" + price_list_code + "'\n" +
                        "  AND pack_code = '" + pack_code + "' \n" +
                        "  AND pack_seq = '" + pack_seq + "' \n" +
                        "  AND category_price_code = '" + category_price_code + "'\n" +
                        "  AND service_code = '" + service_code + "'\n" +
                        "  AND service_seq = '" + service_seq + "'\n" +
                        "  AND exec_tmp = '" + exec_tmp + "' \n" +
                        "  AND task_tmp ='" + task_tmp + "' \n"
                        )
                .append(";"+NEXT_TMP)
                .toString();
    }
}
