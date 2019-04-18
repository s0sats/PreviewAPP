package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Move_ReasonDao;
import com.namoadigital.prj001.database.Specification;

public class IO_Move_Reason_Sql_001 implements Specification {
    private long customer_code;
    private int reason_code;

    public IO_Move_Reason_Sql_001(long customer_code, int reason_code) {
        this.customer_code = customer_code;
        this.reason_code = reason_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "   t.*\n" +
                        " FROM\n" +
                        IO_Move_ReasonDao.TABLE + " t \n" +
                        " WHERE\n" +
                        "   t.customer_code = '"+customer_code+"'\n" +
                        "   and t.reason_code = '"+reason_code+"'\n")
                .toString();
    }
}
