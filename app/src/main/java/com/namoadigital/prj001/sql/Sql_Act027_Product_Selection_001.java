package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_All_Product_GroupDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 07/11/2017.
 *
 * Query que seleciona grupo de produtos dos produtos que aceitam novo serial
 *
 * DANIEL LUCHE ON 03/05/2018
 * Modificado query para usarem as novas tabelas MD_All+* *
 */

public class Sql_Act027_Product_Selection_001 implements Specification {

    private String s_customer_code;
    private String s_recursive_code_father;
    private String s_filter;

    public Sql_Act027_Product_Selection_001(String s_customer_code, String s_recursive_code_father, String s_filter) {
        this.s_customer_code = s_customer_code;
        this.s_recursive_code_father = s_recursive_code_father;
        this.s_filter = s_filter;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append("   SELECT\n" +
                        "         T.*     \n" +
                        "   FROM (\n" +
                        "        SELECT\n" +
                        "           pg.group_code,\n" +
                        "           pg.group_id,\n" +
                        "           pg.group_desc,\n" +
                        "           pg.group_id || ' - ' || pg.group_desc full_group_desc,\n" +
                        "           pg.recursive_code,\n" +
                        "           ifnull(pg.recursive_code_father,0) recursive_code_father,\n" +
                        "                                  'group' type\n" +
                        "                  \n" +
                        "        FROM\n" +
                        "          "+ MD_All_Product_GroupDao.TABLE + "  pg \n" +
                        "        WHERE\n" +
                        "           pg.customer_code = " + s_customer_code  + "\n" +
                        "        ) T\n" +
                        "   WHERE\n" +
                        "      t.recursive_code_father = " + s_recursive_code_father  + "\n" +
                        "      and ( '" + s_filter  + "' IS NULL OR 1 = 0)\n" +
                        "   ORDER BY\n" +
                        "      t.group_id;")
                .append("group_code#group_id#group_desc#full_group_desc#type#recursive_code")
                .toString().replace("'%null%'","null").replace("'null'","null");

    }
}
