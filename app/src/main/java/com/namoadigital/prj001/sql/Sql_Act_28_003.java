package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by neomatrix on 23/02/17.
 */

public class Sql_Act_28_003 implements Specification {

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


    public Sql_Act_28_003(long customer_code, int so_prefix, int so_code, int price_list_code, int pack_code, int pack_seq, int category_price_code, int service_code, int service_seq, long exec_tmp) {
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
                .append("update sm_so_service_execs set partner_code = null, partner_id = null, partner_desc = null where\n" +
                        "\n" +
                        "\t(customer_code || '|' || so_prefix || '|' || so_code || '|' || price_list_code || '|' || pack_code || '|' || pack_seq || '|' || category_price_code || '|' || service_code || '|' || service_seq || '|' || exec_tmp) = \n" +
                        "\n" +
                        "\t(\n" +
                        "\t\tselect e.customer_code || '|' || e.so_prefix || '|' || e.so_code || '|' || e.price_list_code || '|' || e.pack_code || '|' || e.pack_seq || '|' || e.category_price_code || '|' || e.service_code || '|' || e.service_seq || '|' || e.exec_tmp ekey\n" +
                        "\n" +
                        "\t\tfrom \n" +
                        "\t\t\tsm_so_service_execs e\n" +
                        "\n" +
                        "\t\twhere\n" +
                        "\t\t(\n" +
                        "\n" +
                        "\t\t\tselect partner_code \n" +
                        "\t\t\tfrom sm_so_services s\n" +
                        "\t\t\twhere(\n" +
                        "\n" +
                        "\t\t\t\t    s.customer_code = '" + customer_code + "' and\n" +
                        "\n" +
                        "\t\t\t\t\ts.so_prefix = '" + so_prefix + "' and\n" +
                        "\n" +
                        "\t\t\t\t\ts.so_code = '" + so_code + "' and\n" +
                        "\n" +
                        "\t\t\t\t\ts.price_list_code = '" + price_list_code + "' and\n" +
                        "\n" +
                        "\t\t\t\t\ts.pack_code = '" + pack_code + "' and\n" +
                        "\n" +
                        "\t\t\t\t\ts.pack_seq = '" + pack_seq + "' and\n" +
                        "\n" +
                        "\t\t\t\t\ts.category_price_code = '" + category_price_code + "' and\n" +
                        "\n" +
                        "\t\t\t\t\ts.service_code = '" + service_code + "' and\n" +
                        "\n" +
                        "\t\t\t\t\ts.service_seq = '" + service_seq + "'\n" +
                        "\t\t\t\t\t)\n" +
                        "\t\t)\n" +
                        "\n" +
                        "\t\tis null and\n" +
                        "\n" +
                        "\t\t\t\t(\n" +
                        "\t\t\t\t\tselect count(1)\n" +
                        "\t\t\t\t\t from sm_so_service_exec_tasks t \n" +
                        "\t\t\t\t\t where(\n" +
                        "\n" +
                        "\t\t\t\t\t\t\tt.customer_code = '" + customer_code + "' and\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\tt.so_prefix = '" + so_prefix + "' and\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\tt.so_code = '" + so_code + "' and\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\tt.price_list_code = '" + price_list_code + "' and\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\tt.pack_code = '" + pack_code + "' and\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\tt.pack_seq = '" + pack_seq + "' and\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\tt.category_price_code = '" + category_price_code + "' and\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\tt.service_code = '" + service_code + "' and\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\tt.service_seq = '" + service_seq + "' and\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\tt.exec_tmp = '" + exec_tmp + "' and\n" +
                        "\t\t\t\t\t\t\t\t\n" +
                        "\t\t\t\t\t\t\t\tt.status != '" + Constant.SO_STATUS_CANCELLED + "' )\n" +
                        "\t\t\t\t)\n" +
                        "\n" +
                        "\t\t= 0 and \n" +

                        "\t\t\t\t\t\t\te.customer_code = '" + customer_code + "' and\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\te.so_prefix = '" + so_prefix + "' and\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\te.so_code = '" + so_code + "' and\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\te.price_list_code = '" + price_list_code + "' and\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\te.pack_code = '" + pack_code + "' and\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\te.pack_seq = '" + pack_seq + "' and\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\te.category_price_code = '" + category_price_code + "' and\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\te.service_code = '" + service_code + "' and\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\te.service_seq = '" + service_seq + "' and\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\te.exec_tmp = '" + exec_tmp + "' \n" +
                        "\t)")
                .append(";")
                .toString();
    }
}
