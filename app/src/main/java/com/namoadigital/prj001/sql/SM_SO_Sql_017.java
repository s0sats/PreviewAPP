package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 03/08/2017.
 *
 * Atualiza origin_change para APP
 *
 *
 */

public class SM_SO_Sql_017 implements Specification {

    private long customer_code;
    private int so_prefix;
    private int so_code;
    private String origin_change ="";

    public SM_SO_Sql_017(long customer_code, int so_prefix, int so_code, String origin_change) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
        this.origin_change = origin_change;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" UPDATE "+ SM_SODao.TABLE+" set\n" +
                        "   origin_change = '"+origin_change+"'\n" +
                        " WHERE\n" +
                        "  customer_code = '"+customer_code+"'\n" +
                        "  and so_prefix = '"+so_prefix+"'\n" +
                        "  and so_code = '"+so_code+"'")
                .toString();
    }
}
