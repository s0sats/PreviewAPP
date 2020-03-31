package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by DANIEL.LUCHE on 23/03/2017.
 */

/**
 *
 *
 *   Query pega qtd de checklists WAITING_SYNC
 *
 */

public class GE_Custom_Form_Local_Sql_009 implements Specification {

    public static final String FINALIZED_QTY = "finalized_qty";

    private long s_customer_code;

    public GE_Custom_Form_Local_Sql_009(long s_customer_code) {
        this.s_customer_code = s_customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "     count(1) "+ FINALIZED_QTY +
                        " FROM\n" +
                        GE_Custom_Form_LocalDao.TABLE+" l\n " +
                        " WHERE\n" +
                        "   l."+GE_Custom_Form_LocalDao.CUSTOMER_CODE+" = '"+s_customer_code+"' " +
                        "   AND l."+GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS+"" +
                        "    in('"+ Constant.SYS_STATUS_WAITING_SYNC+"');")
                //.append(FINALIZED_QTY)
                .toString();
    }
}
