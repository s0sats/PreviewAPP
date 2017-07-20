package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 21/06/2017.
 *
 * Seleciona qtd de S.O's com update_required = 1
 *
 *
 */

public class SM_SO_Sql_005 implements Specification {

    private long customer_code;

    public SM_SO_Sql_005(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "       s.*\n" +
                        " FROM\n" +
                        SM_SODao.TABLE + " S\n" +
                        " WHERE\n" +
                        "    S.customer_code =  '" + customer_code + "'\n" +
                        "    AND s.update_required = 1")
                .toString();
    }
}
