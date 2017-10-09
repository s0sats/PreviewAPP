package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 03/08/2017.
 * <p>
 * Atualiza origin_change para APP
 */

public class SM_SO_Sql_019 implements Specification {

    private long customer_code;


    public SM_SO_Sql_019(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "       S.so_prefix  || \".\" ||   S.so_code as so\n" +
                        " FROM\n" +
                        SM_SODao.TABLE + " S\n" +
                        " WHERE\n" +
                        "    S.customer_code =  '" + customer_code + "'\n" +
                        "    AND (S.sync_required = 1)")
                .append(";")
                .append("so")
                .toString();
    }
}
