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

    public SM_SO_Service_Exec_Task_File_Sql_005(long customer_code, int so_prefix, int so_code, int price_list_code, int pack_code, int pack_seq, int category_price_code, int service_code, int service_seq, long exec_tmp, long task_tmp) {
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
