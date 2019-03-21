package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Outbound_ItemDao;
import com.namoadigital.prj001.database.Specification;

public class IO_Outbound_Item_Sql_001 implements Specification {

    private long customer_code;
    private int outbound_prefix;
    private int outbound_code;

    public IO_Outbound_Item_Sql_001(long customer_code, int outbound_prefix, int outbound_code) {
        this.customer_code = customer_code;
        this.outbound_prefix = outbound_prefix;
        this.outbound_code = outbound_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "   t.*\n" +
                        " FROM\n" +
                        IO_Outbound_ItemDao.TABLE + " t \n" +
                        " WHERE\n" +
                        "   t.customer_code = '"+customer_code+"'\n" +
                        "   and t.outbound_prefix = '"+outbound_prefix+"'\n" +
                        "   and t.outbound_code = '"+outbound_code+"'\n" +
                        " ORDER BY\n" +
                        "    t.customer_code,\n" +
                        "    t.outbound_prefix,\n" +
                        "    t.outbound_code,\n" +
                        "    t.outbound_item\n")
                .toString();
    }
}
