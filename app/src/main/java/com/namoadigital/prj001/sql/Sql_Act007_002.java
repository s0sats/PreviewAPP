package com.namoadigital.prj001.sql;


import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 09/01/17.
 */

public class Sql_Act007_002 implements Specification {

    private String s_customer_code;
    private String s_group_code;
    private String s_filter;

    private int iType;

    public Sql_Act007_002(String s_customer_code, String s_group_code, String s_filter, int iType) {
        this.s_customer_code = s_customer_code;
        this.s_group_code = s_group_code;
        this.s_filter = s_filter;
        this.iType = iType;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        if (iType == 0) {

            return sb
                    .append(" SELECT\n" +
                            "  DISTINCT " +
                            "    p.product_code,\n" +
                            "    p.product_id,\n" +
                            "    p.product_desc,\n" +
                            "    p.product_id || ' - ' || p.product_desc full_product_desc ,\n" +
                            "    'product' type \n" +
                            " FROM\n" +
                            "    md_products p\n" +
                            " LEFT JOIN\n" +
                            "    md_product_group_products as pgp on p.customer_code = pgp.customer_code and p.product_code = pgp.product_code\n" +
                            " WHERE\n" +
                            "    p.customer_code= " + s_customer_code + " \n" +
                            "    and pgp.product_code is null and '" + s_filter + "' IS NULL  \n" +
                            "    or ( '" + s_filter + "' IS NOT NULL and ( p.product_id like '%" + s_filter + "%' OR p.product_desc like '%" + s_filter + "%' ) )" +
                            "  ORDER BY \n" +
                            "     p.product_id;")
                    //.append("product_code#product_id#product_desc#full_product_desc#type")
                    .toString().replace("'%null%'","null").replace("'null'","null");
        } else {
            return sb
                    .append("     SELECT\n" +
                            "       DISTINCT" +
                            "        p.product_code,\n" +
                            "        p.product_id,\n" +
                            "        p.product_desc,\n" +
                            "        p.product_id || ' - ' || p.product_desc full_product_desc ,\n" +
                            "                               'product' type                          \n" +
                            "     FROM\n" +
                            "        md_products p\n" +
                            "     LEFT JOIN\n" +
                            "        md_product_group_products as pgp on p.customer_code = pgp.customer_code and p.product_code = pgp.product_code\n" +
                            "     WHERE   \n" +
                            "        pgp.customer_code= " + s_customer_code + "   \n" +
                            "        and pgp.group_code = " + s_group_code + " AND pgp.product_code IS NOT NULL AND '" + s_filter + "' IS NULL \n" +
                            "        OR( '" + s_filter + "' IS NOT NULL\n" +
                            "              AND (p.product_id like '%" + s_filter + "%' OR p.product_desc like '%" + s_filter + "%')" +
                            "           ) \n" +
                            "     ORDER BY\n" +
                            "        P.product_id;")
                    //.append("product_code#product_id#product_desc#full_product_desc#type")
                    .toString().replace("'%null%'","null").replace("'null'","null");
        }
    }
}
