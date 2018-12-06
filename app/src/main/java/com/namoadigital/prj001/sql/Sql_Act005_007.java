package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 07/03/2018.
 *
 * Seleciona qtd de ap "upload_required"
 */

public class Sql_Act005_007 implements Specification {

    public static final String BADGE_TO_SEND_QTY = "to_send_qty";
    private String customer_code;

    public Sql_Act005_007(String customer_code) {
        this.customer_code = customer_code;
    }


    @Override
    public String toSqlQuery() {
        StringBuilder sb =  new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   count(1) "+ BADGE_TO_SEND_QTY +" \n" +
                        " FROM\n" +
                        GE_Custom_Form_ApDao.TABLE +" a\n" +
                        " WHERE\n" +
                        "   a.customer_code = '"+customer_code+"' \n" +
                        "   and a.upload_required  = 1 \n")
                .append(";")
                //.append(BADGE_TO_SEND_QTY)
                .toString();
    }
}
