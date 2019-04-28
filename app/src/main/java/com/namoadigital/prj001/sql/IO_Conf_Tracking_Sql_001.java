package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Conf_TrackingDao;
import com.namoadigital.prj001.database.Specification;

public class IO_Conf_Tracking_Sql_001 implements Specification {

    private long customer_code;
    private int prefix;
    private int code;
    private int item;
    private String type;

    public IO_Conf_Tracking_Sql_001(long customer_code, int prefix, int code, int item, String type) {
        this.customer_code = customer_code;
        this.prefix = prefix;
        this.code = code;
        this.item = item;
        this.type = type;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "   t.*\n" +
                        " FROM\n" +
                        IO_Conf_TrackingDao.TABLE + " t \n" +
                        " WHERE\n" +
                        "   t.customer_code = '"+customer_code+"'\n" +
                        "   and t.prefix = '"+prefix+"'\n" +
                        "   and t.code = '"+code+"'\n" +
                        "   and t.item = '"+item+"'\n" +
                        "   and t.type = '"+type+"'\n" +
                        " ORDER BY\n" +
                        "    t.tracking\n")
                .toString();
    }
}
