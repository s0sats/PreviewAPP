package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by DANIEL.LUCHE on 23/02/2018.
 */

public class GE_Custom_Form_Ap_Sql_001 implements Specification {

    public static final String BADGE_IN_PROCESSING_QTY = "in_processing_qty";
    private long customer_code;

    public GE_Custom_Form_Ap_Sql_001(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append("SELECT\n" +
                        "    count(1) "+BADGE_IN_PROCESSING_QTY+" \n" +
                        "FROM\n" +
                        "    "+GE_Custom_Form_ApDao.TABLE+" a\n" +
                        "WHERE\n" +
                        "   a.customer_code = '"+customer_code+"'\n" +
                        "   and a.ap_status not in ('"+ Constant.SO_STATUS_DONE+"','"+Constant.SO_STATUS_CANCELLED+"')\n")
                .append(";" + BADGE_IN_PROCESSING_QTY )

                .toString();
    }
}
