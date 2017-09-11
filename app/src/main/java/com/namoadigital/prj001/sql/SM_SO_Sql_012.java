package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by d.luche on 21/06/2017.
 *
 * Seta update required pra 1
 *
 *
 */

public class SM_SO_Sql_012 implements Specification {

    private long customer_code;
    private int so_prefix;
    private int so_code;

    public SM_SO_Sql_012(long customer_code, int so_prefix, int so_code) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" UPDATE "+ SM_SODao.TABLE+"\n" +
                        "  set approval_required = 1,\n"+
                        "  set status =\n" + Constant.SO_STATUS_WAITING_SYNC +
                        " WHERE\n" +
                        "  customer_code = '"+customer_code+"'\n" +
                        "  and so_prefix = '"+so_prefix+"'\n" +
                        "  and so_code = '"+so_code+"'")
                .toString();
    }
}
