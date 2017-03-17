package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 09/01/17.
 */

public class Sql_Act007_001 implements Specification {

    private String s_customer_code;
    private String s_recursive_code_father;
    private String s_filter;

    public Sql_Act007_001(String s_customer_code, String s_recursive_code_father, String s_filter) {
        this.s_customer_code = s_customer_code;
        this.s_recursive_code_father = s_recursive_code_father;
        this.s_filter = s_filter;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

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
                        "        md_product_groups pg \n" +
                        "     WHERE\n" +
                        "        pg.customer_code = " + s_customer_code  + "\n" +
                        "     ) T\n" +
                        "WHERE\n" +
                        "   t.recursive_code_father = " + s_recursive_code_father  + "\n" +
                        "   and ( '" + s_filter  + "' IS NULL OR  t.group_id like '%" + s_filter  + "%' OR t.group_desc like '%" + s_filter  + "%'  )\n" +
                        "ORDER BY\n" +
                        "   t.group_id;")
                .append("group_code#group_id#group_desc#full_group_desc#type#recursive_code")
                .toString().replace("'%null%'","null").replace("'null'","null");
    }
}
