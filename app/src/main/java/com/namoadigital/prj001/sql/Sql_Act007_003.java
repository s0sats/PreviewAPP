package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.model.TK_Ticket_Product;

import java.util.ArrayList;

public class Sql_Act007_003 implements Specification {

    private String s_customer_code;
    private String s_recursive_code_father;
    private String s_filter;
    private String removeProducts;
    public Sql_Act007_003(String s_customer_code, String s_recursive_code_father, ArrayList<TK_Ticket_Product> tk_ticket_products, String s_filter) {
        this.s_customer_code = s_customer_code;
        this.s_recursive_code_father = s_recursive_code_father;
        removeProducts = "";
        if(!tk_ticket_products.isEmpty()) {
            removeProducts = "            and product_code not in (";
            for (int i = 0; i < tk_ticket_products.size(); i++) {
                if (i == 0) {
                    removeProducts += String.valueOf(tk_ticket_products.get(i).getProduct_code());
                } else {
                    removeProducts += ", " + tk_ticket_products.get(i);
                }
            }
            removeProducts += ") \n";
        }
        this.s_filter = s_filter;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        //String teste =
        return sb
                .append("SELECT\n" +
                        "      T.*     \n" +
                        "FROM (\n" +
                        "     SELECT\n" +
                        "        pg.group_code,\n" +
                        "        pg.group_id,\n" +
                        "        pg.group_desc,\n" +
                        "        pg.group_id || ' - ' || pg.group_desc full_group_desc,\n" +
                        "        pg.recursive_code,\n" +
                        "        ifnull(pg.recursive_code_father,0) recursive_code_father,\n" +
                        "                               'group' type\n" +
                        "               \n" +
                        "     FROM\n" +
                        "        md_product_groups pg, md_product_group_products pivot \n" +
                        "     WHERE\n" +
                        "        pg.customer_code = " + s_customer_code  + "\n" +
                        "        and pivot.customer_code = " + s_customer_code  + "\n" +
                        "        and pg.group_code = pivot.group_code \n" +
                        "        and pivot.product_code in ( \n" +
                        "         select product_code\n" +
                        "            from md_products p\n" +
                        "            where p.spare_part = 1\n" +
                        "              and p.customer_code = " + s_customer_code  + "\n" +
                                     removeProducts + "\n" +
                                     ")" + "\n"+
                        "     ) T\n" +
                        "WHERE\n" +
                        "   t.recursive_code_father = " + s_recursive_code_father  + "\n" +
                        "   and ( '" + s_filter  + "' IS NULL OR 1 = 0)\n" +
                        "ORDER BY\n" +
                        "   t.group_id;")
                //.append("group_code#group_id#group_desc#full_group_desc#type#recursive_code")
                .toString().replace("'%null%'","null").replace("'null'","null");

        // return teste;
    }
}
