package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 08/08/2017.
 * Seleciona todos Seriais com flag de atualização
 */

public class MD_Product_Serial_Sql_015 implements Specification {

    public static final String IN_TOKEN_UR_QTY = "in_token_ur_qty";

    private long customer_code;

    public MD_Product_Serial_Sql_015(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   count(1) " + IN_TOKEN_UR_QTY + " \n" +
                        " FROM\n" +
                        MD_Product_SerialDao.TABLE + " s\n" +
                        " WHERE\n" +
                        "  s.customer_code = '" + customer_code + "'\n" +
                        "  and s.update_required = 1  ")
                .append(";" + IN_TOKEN_UR_QTY)
                .toString();
    }
}
