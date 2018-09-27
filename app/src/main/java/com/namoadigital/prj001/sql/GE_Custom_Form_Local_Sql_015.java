package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by DANIEL.LUCHE on 23/03/2017.
 */


/**
 * Query pega qtd de checklists IN_PROCESSING
 */
public class GE_Custom_Form_Local_Sql_015 implements Specification {

    public static final String PENDING_QTY = "pending_qty";

    private String customer_code;

    public GE_Custom_Form_Local_Sql_015(String customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   customer_code,\n" +
                        "   count(1) " + PENDING_QTY + "\n" +
                        " FROM\n" +
                        GE_Custom_Form_LocalDao.TABLE + " l\n " +
                        " WHERE\n" +
                        "   l.customer_code = '"+customer_code+"' \n" +
                        "   and l." + GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS + "" +
                        "    in('" + Constant.SYS_STATUS_IN_PROCESSING + "')\n" +
                        ";")
                .append(GE_Custom_Form_LocalDao.CUSTOMER_CODE + "#" + PENDING_QTY)
                .toString();
    }
}
