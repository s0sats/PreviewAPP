package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Outbound_ItemDao;
import com.namoadigital.prj001.database.Specification;

public class IO_Outbound_Item_Sql_007 implements Specification {
    private long customer_code;
    private int outbound_prefix;
    private int outbound_code;
    private int outbound_item;

    public IO_Outbound_Item_Sql_007(long customer_code, int outbound_prefix, int outbound_code, int outbound_item) {
        this.customer_code = customer_code;
        this.outbound_prefix = outbound_prefix;
        this.outbound_code = outbound_code;
        this.outbound_item = outbound_item;
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
                        "   and outbound_prefix = '"+outbound_prefix+"'\n" +
                        "   and outbound_code = '"+outbound_code+"'\n" +
                        "   and outbound_item = '"+outbound_item+"'\n")
                .toString();
    }
}
