package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_Service_ExecDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by neomatrix on 31/07/17.
 */

public class Act027_Services_Adapter_Sql_001 implements Specification {
    public static final String SET_FLAG = "set_flag";

    private String customer_code;
    private String so_prefix;
    private String so_code;
    private String price_list_code;
    private String pack_code;
    private String pack_seq;
    private String category_price_code;
    private String service_code;
    private String service_seq;

    public Act027_Services_Adapter_Sql_001(String customer_code, String so_prefix, String so_code, String price_list_code, String pack_code, String pack_seq, String category_price_code, String service_code, String service_seq) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
        this.price_list_code = price_list_code;
        this.pack_code = pack_code;
        this.pack_seq = pack_seq;
        this.category_price_code = category_price_code;
        this.service_code = service_code;
        this.service_seq = service_seq;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT \n" +
                        "   count(1) "+SET_FLAG+" \n" +
                        " FROM " +
                        SM_SO_Service_ExecDao.TABLE +" e \n " +
                        " WHERE\n" +
                        "   e.customer_code =              '" + customer_code + "'\n" +
                        "    AND e.so_prefix =              '" + so_prefix + "'\n" +
                        "    AND e.so_code =                '" + so_code + "'\n" +
                        "    AND e.price_list_code =        '" + price_list_code + "'\n" +
                        "    AND e.pack_code =              '" + pack_code + "'\n" +
                        "    AND e.pack_seq =               '" + pack_seq + "'\n" +
                        "    AND e.category_price_code =    '" + category_price_code + "'\n" +
                        "    AND e.service_code =           '" + service_code + "'\n" +
                        "    AND e.service_seq =            '" + service_seq + "' " +
                        "    AND e.status = '"+ Constant.SYS_STATUS_PROCESS+"'")
                .append(";")
                //.append(SET_FLAG)
                .toString();
    }
}
