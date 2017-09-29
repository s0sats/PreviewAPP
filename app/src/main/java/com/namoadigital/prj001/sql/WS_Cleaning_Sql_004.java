package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by neomatrix on 23/02/17.
 */

public class WS_Cleaning_Sql_004 implements Specification {

    public WS_Cleaning_Sql_004() {
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();

        return sb
                .append("update sm_so_service_execs set partner_code = null, partner_id = null, partner_desc = null where\n" +
                        "\n" +
                        "\t(customer_code || '|' || so_prefix || '|' || so_code || '|' || price_list_code || '|' || pack_code || '|' || pack_seq || '|' || category_price_code || '|' || service_code || '|' || service_seq || '|' || exec_tmp)in\n" +
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
                        "\t\t\t\t    e.customer_code = s.customer_code and\n" +
                        "\n" +
                        "\t\t\t\t\te.so_prefix = s.so_prefix and\n" +
                        "\n" +
                        "\t\t\t\t\te.so_code = s.so_code and\n" +
                        "\n" +
                        "\t\t\t\t\te.price_list_code = s.price_list_code and\n" +
                        "\n" +
                        "\t\t\t\t\te.pack_code = s.pack_code and\n" +
                        "\n" +
                        "\t\t\t\t\te.pack_seq = s.pack_seq and\n" +
                        "\n" +
                        "\t\t\t\t\te.category_price_code = s.category_price_code and\n" +
                        "\n" +
                        "\t\t\t\t\te.service_code = s.service_code and\n" +
                        "\n" +
                        "\t\t\t\t\te.service_seq = s.service_seq\n" +
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
                        "\t\t\t\t\t\t\te.customer_code = t.customer_code and\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\te.so_prefix = t.so_prefix and\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\te.so_code = t.so_code and\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\te.price_list_code = t.price_list_code and\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\te.pack_code = t.pack_code and\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\te.pack_seq = t.pack_seq and\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\te.category_price_code = t.category_price_code and\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\te.service_code = t.service_code and\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\te.service_seq = t.service_seq and\n" +
                        "\n" +
                        "\t\t\t\t\t\t\t\te.exec_tmp = t.exec_tmp and \n" +
                        "\t\t\t\t\t\t\t\t\n" +
                        "\t\t\t\t\t\t\t\tt.status != '" + Constant.SO_STATUS_CANCELLED + "' )\n" +
                        "\t\t\t\t)\n" +
                        "\n" +
                        "\t\t= 0\n" +
                        "\t)")
                .append(";")
                .toString();
    }
}
