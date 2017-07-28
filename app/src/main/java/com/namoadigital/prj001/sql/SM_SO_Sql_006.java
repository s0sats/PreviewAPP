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

public class SM_SO_Sql_006 implements Specification {

    private long customer_code;
    private int so_prefix;
    private int so_code;
    private int so_scn;

    public SM_SO_Sql_006(long customer_code, int so_prefix, int so_code, int so_scn) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
        this.so_scn = so_scn;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" UPDATE "+ SM_SODao.TABLE+" set\n" +
                        "   so_scn = '"+so_scn+"',\n" +
                        "   update_required = 0\n," +
                        "   token = ''\n," +
                        " WHERE\n" +
                        "  customer_code = '"+customer_code+"'\n" +
                        "  and so_prefix = '"+so_prefix+"'\n" +
                        "  and so_code = '"+so_code+"'")
                .toString();
    }
}
