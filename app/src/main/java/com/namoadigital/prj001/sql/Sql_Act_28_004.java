package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 23/02/17.
 */

public class Sql_Act_28_004 implements Specification {

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


    public Sql_Act_28_004(long customer_code, int so_prefix, int so_code, int price_list_code, int pack_code, int pack_seq, int category_price_code, int service_code, int service_seq, long exec_tmp) {
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
                .append("UPDATE sm_so_service_execs set \n" +
                        "       \n" +
                        "       partner_code = null,\n" +
                        "       partner_id = null,\n" +
                        "       partner_desc = null\n" +
                        "WHERE \n" +
                        "      (customer_code || '|' || so_prefix || '|' || so_code || '|' || price_list_code || '|' || pack_code || '|' || pack_seq || '|' || category_price_code || '|' || service_code || '|' || service_seq || '|' || exec_tmp) = \n" +
                        "            (\n" +
                        "              SELECT\n" +
                        "               t.customer_code || '|' || \n" +
                        "               t.so_prefix || '|' || \n" +
                        "               t.so_code || '|' || \n" +
                        "               t.price_list_code || '|' || \n" +
                        "               t.pack_code || '|' || \n" +
                        "               t.pack_seq || '|' || \n" +
                        "               t.category_price_code || '|' || \n" +
                        "               t.service_code || '|' || \n" +
                        "               t.service_seq || '|' || \n" +
                        "               t.exec_tmp\n" +
                        "              FROM\n" +
                        "                sm_so_services s ,\n" +
                        "                (select\n" +
                        "                      s.customer_code,\n" +
                        "                  \t\ts.so_prefix,\n" +
                        "                  \t\ts.so_code,\n" +
                        "                  \t\ts.price_list_code ,\n" +
                        "                  \t\ts.pack_code,\n" +
                        "                  \t\ts.pack_seq,\n" +
                        "                  \t\ts.category_price_code,\n" +
                        "                  \t\ts.service_code,\n" +
                        "                  \t\ts.service_seq,\n" +
                        "                      s.exec_tmp, \n" +
                        "                    sum(\n" +
                        "                        CASE WHEN s.status <> 'CANCELLED' \n" +
                        "                        THEN\n" +
                        "                          1\n" +
                        "                        ELSE\n" +
                        "                          0\n" +
                        "                        END  \n" +
                        "                    ) qtd_not_cancelled\n" +
                        "                  from\n" +
                        "                    sm_so_service_exec_tasks s\n" +
                        "                  where\n" +
                        "                      s.customer_code = '" + customer_code + "' and\n" +
                        "                  \t\ts.so_prefix = '" + so_prefix + "' and\n" +
                        "                  \t\ts.so_code = '" + so_code + "' and\n" +
                        "                  \t\ts.price_list_code = '" + price_list_code + "' and\n" +
                        "                  \t\ts.pack_code = '" + pack_code + "' and\n" +
                        "                  \t\ts.pack_seq = '" + pack_seq + "' and\n" +
                        "                  \t\ts.category_price_code = '" + category_price_code + "' and\n" +
                        "                  \t\ts.service_code = '" + service_code + "' and\n" +
                        "                  \t\ts.service_seq = '" + service_seq + "' and \n" +
                        "                      s.exec_tmp = '" + exec_tmp + "'  \n" +
                        "                  group by\n" +
                        "                      s.customer_code,\n" +
                        "                  \t\ts.so_prefix,\n" +
                        "                  \t\ts.so_code,\n" +
                        "                  \t\ts.price_list_code ,\n" +
                        "                  \t\ts.pack_code,\n" +
                        "                  \t\ts.pack_seq,\n" +
                        "                  \t\ts.category_price_code,\n" +
                        "                  \t\ts.service_code,\n" +
                        "                  \t\ts.service_seq ) T\n" +
                        "                     \n" +
                        "              WHERE\n" +
                        "                  s.customer_code = t.customer_code\n" +
                        "                \tand s.so_prefix = t.so_prefix\n" +
                        "                \tand s.so_code = t.so_code\n" +
                        "                \tand s.price_list_code = t.price_list_code\n" +
                        "                \tand s.pack_code = t.pack_code\n" +
                        "                \tand s.pack_seq = t.pack_seq\n" +
                        "                \tand s.category_price_code = t.category_price_code\n" +
                        "                \tand s.service_code = t.service_code\n" +
                        "                \tand s.service_seq = t.service_seq \n" +
                        "                 \n" +
                        "                  and s.partner_code is null\n" +
                        "                  and t.qtd_not_cancelled = 0 \n" +
                        "      )")
                .append(";")
                .toString();
    }
}
