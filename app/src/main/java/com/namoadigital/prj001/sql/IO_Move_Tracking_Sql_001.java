package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Move_TrackingDao;
import com.namoadigital.prj001.database.Specification;

public class IO_Move_Tracking_Sql_001 implements Specification {
    private long customer_code;
    private int move_prefix;
    private int move_code;

    public IO_Move_Tracking_Sql_001(long customer_code, int move_prefix, int move_code) {
        this.customer_code = customer_code;
        this.move_prefix = move_prefix;
        this.move_code = move_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "   t.*\n" +
                        " FROM\n" +
                        IO_Move_TrackingDao.TABLE + " t \n" +
                        " WHERE\n" +
                        "   t.customer_code = '"+customer_code+"'\n" +
                        "   and t.move_prefix = '"+move_prefix+"'\n" +
                        "   and t.move_code = '"+move_code+"'\n" +
                        " ORDER BY\n" +
                        "    t.tracking")
                .toString();
    }
}
