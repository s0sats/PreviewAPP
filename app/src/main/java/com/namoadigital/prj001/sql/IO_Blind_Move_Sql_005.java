package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Blind_MoveDao;
import com.namoadigital.prj001.database.Specification;


/**
 * LUCHE 18/04/2019
 *
 * Query que seleciona blind move passada.
 *
 */

public class IO_Blind_Move_Sql_005 implements Specification {
    private long customer_code;
    private int blind_tmp;

    public IO_Blind_Move_Sql_005(long customer_code, int blind_tmp) {
        this.customer_code = customer_code;
        this.blind_tmp = blind_tmp;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
            .append(
                " SELECT\n" +
                "   b.* " + "\n"+
                " FROM\n" +
                IO_Blind_MoveDao.TABLE + " b \n" +
                " WHERE\n" +
                "   b.customer_code = '" + customer_code+"'\n" +
                "   and b.blind_tmp = '"+ blind_tmp +"'\n"
            )
            .toString();
    }
}
