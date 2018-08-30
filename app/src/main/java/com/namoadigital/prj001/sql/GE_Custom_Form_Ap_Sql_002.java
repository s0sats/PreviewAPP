package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 23/02/2018.
 */

public class GE_Custom_Form_Ap_Sql_002 implements Specification {

    public static final String BADGE_SYNC_REQUIRED_QTY = "SYNC_REQUIRED_QTY";
    private long customer_code;

    public GE_Custom_Form_Ap_Sql_002(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append("SELECT\n" +
                        "    count(1) "+BADGE_SYNC_REQUIRED_QTY+" \n" +
                        "FROM\n" +
                        "    "+GE_Custom_Form_ApDao.TABLE+" a\n" +
                        "WHERE\n" +
                        "   a.customer_code = '"+customer_code+"'\n" +
                        "   and a.sync_required = 1\n")
                .append(";")
                .append(BADGE_SYNC_REQUIRED_QTY)
                .toString();
    }
}
