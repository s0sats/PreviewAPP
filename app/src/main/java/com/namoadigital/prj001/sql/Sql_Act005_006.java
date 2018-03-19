package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by DANIEL.LUCHE on 07/03/2018.
 *
 * Seleciona qtd de ap "atrasados"
 * OBS: Por mais estranho que possa parecer,
 * Nesse caso,como o strftime pegará a data em millisegundos,
 * NÃO DEVEMOS USAR O MODIFICADOR 'localtime'
 */

public class Sql_Act005_006 implements Specification {

    public static final String BADGE_SCHEDULED_QTY = "scheduled_qty";
    private String customer_code;

    public Sql_Act005_006(String customer_code) {
        this.customer_code = customer_code;
    }


    @Override
    public String toSqlQuery() {
        StringBuilder sb =  new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   count(1) "+ BADGE_SCHEDULED_QTY +" \n" +
                        " FROM\n" +
                        GE_Custom_Form_ApDao.TABLE +" a\n" +
                        " WHERE\n" +
                        "   a.customer_code = '"+customer_code+"' \n" +
                        "   and a.ap_when is not null \n" +
                        "   and (strftime('%s', a.ap_when)  * 1000 ) < (strftime('%s', 'now')  * 1000 )\n" +
                        "   and a.ap_status not in('"+ Constant.SYS_STATUS_DONE+"','"+ Constant.SYS_STATUS_CANCELLED+"') \n")
                .append(";"+ BADGE_SCHEDULED_QTY)
                .toString();
    }
}
