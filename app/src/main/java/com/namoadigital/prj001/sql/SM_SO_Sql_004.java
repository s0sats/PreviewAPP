package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by d.luche on 21/06/2017.
 *
 * Seleciona qtd de S.O's pendentes
 *
 *
 */

public class SM_SO_Sql_004 implements Specification {

    public static final String PENDING_QTY = "pending_qty";

    private long customer_code;

    public SM_SO_Sql_004(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "       count(1) "+PENDING_QTY+" \n" +
                        " FROM\n" +
                        SM_SODao.TABLE + " S\n" +
                        " WHERE\n" +
                        "    S.customer_code =  '" + customer_code + "'\n" +
                        "    AND s.status not in ('"+ Constant.SYS_STATUS_CANCELLED+"','"+ Constant.SYS_STATUS_DONE+"')" +
                        ";")
                .append(PENDING_QTY)
                .toString();
    }
}
