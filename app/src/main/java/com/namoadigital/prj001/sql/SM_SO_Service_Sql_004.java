package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 06/07/17.
 */

public class SM_SO_Service_Sql_004 implements Specification {

    private long customer_code;
    private int so_prefix;
    private int so_code;
    private int price_list_code;
    private int pack_code;
    private int pack_seq;
    private int category_price_code;
    private int service_code;
    private int service_seq;

    public SM_SO_Service_Sql_004(long customer_code, int so_prefix, int so_code, int price_list_code, int pack_code, int pack_seq, int category_price_code, int service_code, int service_seq) {
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
                        "   *, \n" +
                        "   case when partner_code_md1 != partner_code_md2 then '1' else '0' end as partner_restriction \n" +
                        "   from (\n" +
                        "            select \n" +
                        "            sss.*, \n" +
                        "            pkk.pack_id, \n" +
                        "            pkk.pack_desc, \n" +
                        "            ifnull(sss.partner_code,0) as partner_code_md1,  \n" +
                        "            ifnull(pr.partner_code,0) as partner_code_md2 \n" +
                        "            from sm_so_services as sss left join  md_partners as pr on sss.partner_code = pr.partner_code\n" +
                        "                                       inner join sm_so_packs as pkk on sss.pack_code = pkk.pack_code\n" +
                        "        ) ")
                .append(" as S WHERE\n" +
                        "    S.customer_code =              '" + customer_code + "'\n" +
                        "    AND S.so_prefix =              '" + so_prefix + "'\n" +
                        "    AND S.so_code =                '" + so_code + "'\n" +
                        "    AND S.price_list_code =        '" + price_list_code + "'\n" +
                        "    AND S.pack_code =              '" + pack_code + "'\n" +
                        "    AND S.pack_seq =               '" + pack_seq + "'\n" +
                        "    AND S.category_price_code =    '" + category_price_code + "'\n" +
                        "    AND S.service_code =           '" + service_code + "'\n" +
                        "    AND S.service_seq =            '" + service_seq + "' ")
                .append(";")
                //.append("customer_code#so_prefix#so_code#price_list_code#pack_code#pack_seq#category_price_code#service_code#service_seq#service_id#service_desc#service_oper_id#status#qty#optional#manual_price#express#time_exec_standard#price#cost#exec_type#exec_seq_oper#approval_budget_user#approval_budget_date#partner_code#partner_id#partner_desc#require_approval#partner_code_md1#partner_code_md2#partner_restriction#pack_id#pack_desc")
                .toString();
    }
}
