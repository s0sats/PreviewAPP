package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Outbound_ItemDao;
import com.namoadigital.prj001.database.Specification;

public class IO_Outbound_Item_Sql_007 implements Specification {
    private long customer_code;
    private int inbound_prefix;
    private int inbound_code;
    private int inbound_item;

    public IO_Outbound_Item_Sql_007(long customer_code, int inbound_prefix, int inbound_code, int inbound_item) {
        this.customer_code = customer_code;
        this.inbound_prefix = inbound_prefix;
        this.inbound_code = inbound_code;
        this.inbound_item = inbound_item;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" DELETE\n" +
                        " FROM\n" +
                        IO_Outbound_ItemDao.TABLE + " \n" +
                        " WHERE\n" +
                        "   customer_code = '"+customer_code+"'\n" +
                        "   and inbound_prefix = '"+inbound_prefix+"'\n" +
                        "   and inbound_code = '"+inbound_code+"'\n" +
                        "   and inbound_item = '"+inbound_item+"'\n")
                .toString();
    }
}
