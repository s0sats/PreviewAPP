package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.database.Specification;

public class IO_Inbound_Item_Sql_001 implements Specification {

    private long customer_code;
    private int inbound_prefix;
    private int inbound_code;

    public IO_Inbound_Item_Sql_001(long customer_code, int inbound_prefix, int inbound_code) {
        this.customer_code = customer_code;
        this.inbound_prefix = inbound_prefix;
        this.inbound_code = inbound_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "   t.*\n" +
                        " FROM\n" +
                        IO_Inbound_ItemDao.TABLE + " t \n" +
                        " WHERE\n" +
                        "   t.customer_code = '"+customer_code+"'\n" +
                        "   and t.inbound_prefix = '"+inbound_prefix+"'\n" +
                        "   and t.inbound_code = '"+inbound_code+"'\n" +
                        " ORDER BY\n" +
                        "    t.customer_code,\n" +
                        "    t.inbound_prefix,\n" +
                        "    t.inbound_code,\n" +
                        "    t.inbound_item\n")
                .toString();
    }
}
