package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 06/07/17.
 */

public class SM_SO_Service_Exec_Task_Sql_003 implements Specification {

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

    public SM_SO_Service_Exec_Task_Sql_003(long customer_code, int so_prefix, int so_code, int price_list_code, int pack_code, int pack_seq, int category_price_code, int service_code, int service_seq, long exec_tmp) {
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
                .append("select S.service_id, S.service_desc, S.status as service_status, S.exec_type, T.*, T.status as task_status,\n" +

                        "strftime('%Y-%m-%d %H:%M',T.start_date,'localtime') start_date_local,\n" +
                        "strftime('%Y-%m-%d %H:%M',T.end_date,'localtime') end_date_local\n" +

                        "\n" +

                        "\n" +
                        "from SM_SO_Services as S, SM_SO_Service_Exec_Tasks as T\n" +
                        "\n" +
                        "where \n" +
                        "\n" +
                        "S.customer_code = T.customer_code and \n" +
                        "S.so_prefix = T.so_prefix and \n" +
                        "S.so_code = T.so_code and \n" +
                        "S.price_list_code = T.price_list_code and \n" +
                        "S.pack_code = T.pack_code and \n" +
                        "S.pack_seq = T.pack_seq and \n" +
                        "S.category_price_code = T.category_price_code and \n" +
                        "S.service_code = T.service_code and \n" +
                        "S.service_seq = T.service_seq and \n" +
                        "( ")
                .append("    T.customer_code =              '" + customer_code + "'\n" +
                        "    AND T.so_prefix =              '" + so_prefix + "'\n" +
                        "    AND T.so_code =                '" + so_code + "'\n" +
                        "    AND T.price_list_code =        '" + price_list_code + "'\n" +
                        "    AND T.pack_code =              '" + pack_code + "'\n" +
                        "    AND T.pack_seq =               '" + pack_seq + "'\n" +
                        "    AND T.category_price_code =    '" + category_price_code + "'\n" +
                        "    AND T.service_code =           '" + service_code + "'\n" +
                        "    AND T.service_seq =            '" + service_seq + "'\n" +
                        "    AND T.exec_tmp =               '" + exec_tmp + "' ")
                .append(" ) ")
                .append(" Order by ")
//                .append("    customer_code DESC, " +
//                        "    so_prefix DESC, " +
//                        "    so_code DESC, " +
//                        "    price_list_code DESC, " +
//                        "    pack_code DESC, " +
//                        "    pack_seq DESC, " +
//                        "    category_price_code DESC, " +
//                        "    service_code DESC, " +
//                        "    service_seq DESC, " +
//                        "    exec_tmp DESC, " +
//                        "    task_tmp DESC, " +
                .append("    task_seq_oper DESC")
                .append(";service_id#service_desc#service_status#exec_type#task_status#customer_code#so_prefix#so_code#price_list_code#pack_code#pack_seq#category_price_code#service_code#service_seq#exec_code#task_code#exec_tmp#task_tmp#task_seq_oper#task_user#task_user_nick#start_date#start_date_local#end_date#end_date_local#exec_time#exec_time_format#task_perc#qty_people#status#site_code#site_id#site_desc#zone_code#zone_id#zone_desc#local_code#local_id#comments")
                .toString();
    }
}
