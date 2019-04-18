package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Blind_Move_TrackingDao;
import com.namoadigital.prj001.database.Specification;

public class IO_Blind_Move_Tracking_Sql_001 implements Specification {

    private long customer_code;
    private int blind_tmp;

    public IO_Blind_Move_Tracking_Sql_001(long customer_code, int blind_tmp) {
        this.customer_code = customer_code;
        this.blind_tmp = blind_tmp;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        //
        return
             sb
            .append(" SELECT\n" +
                    "      t.*\n" +
                    " FROM\n" +
                    IO_Blind_Move_TrackingDao.TABLE + " t \n" +
                    " WHERE\n" +
                    "   t.customer_code = '"+customer_code+"'\n" +
                    "   and t.blind_tmp = '"+blind_tmp+"'\n" +
                    " ORDER BY\n" +
                    "   t.tracking   ")
            .toString();
    }
}
