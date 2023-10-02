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
 * BARRIONUEVO 27/09/2023
 *     - Otimização de query.
 *     - Remocao de param nao utilizado, o filtro de texto  eh apenas para os produtos.
 */

public class Sql_Act027_Product_Selection_001 implements Specification {

    private String s_customer_code;
    private String s_recursive_code_father;


    public Sql_Act027_Product_Selection_001(String s_customer_code, String s_recursive_code_father) {
        this.s_customer_code = s_customer_code;
        this.s_recursive_code_father = s_recursive_code_father;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append("SELECT\n" +
                        "       pg.group_code code,\n" +
                        "       pg.group_id id,\n" +
                        "       pg.group_desc desc,\n" +
                        "       pg.group_desc full_desc,\n" +
                        "       pg.recursive_code recursive,\n" +
                        "       ifnull(pg.recursive_code_father,0) recursive_code_father,\n" +
                        "       'group' type\n" +
                        "                  \n" +
                        "        FROM\n" +
                        "          "+ MD_All_Product_GroupDao.TABLE + "  pg \n" +
                        "        WHERE\n" +
                        "              pg.customer_code = " + s_customer_code  + "\n" +
                        "          and (" +
                        "                  ( "+ s_recursive_code_father  + " = 0 and pg.recursive_code_father is null)" +
                        "                   or \n" +
                        "                  (pg.recursive_code_father = " + s_recursive_code_father  + ")\n" +
                        "              )"+
                        "        ORDER BY trim(pg.group_desc);")
                .toString().replace("'%null%'","null").replace("'null'","null");

    }
}
