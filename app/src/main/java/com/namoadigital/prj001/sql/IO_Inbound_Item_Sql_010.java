package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.database.Specification;

public class IO_Inbound_Item_Sql_010 implements Specification {
    private long customer_code;
    private int inbound_prefix;
    private int inbound_code;
    private int inbound_item;
    private String status;

    public IO_Inbound_Item_Sql_010(long customer_code, int inbound_prefix, int inbound_code, int inbound_item, String status) {
        this.customer_code = customer_code;
        this.inbound_prefix = inbound_prefix;
        this.inbound_code = inbound_code;
        this.inbound_item = inbound_item;
        this.status = status;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" UPDATE "+ IO_Inbound_ItemDao.TABLE + "  SET\n" +
                        "   status = '"+status+"'\n" +
                        " WHERE\n" +
                        "   customer_code = '"+customer_code+"'\n" +
                        "   and inbound_prefix = '"+inbound_prefix+"'\n" +
                        "   and inbound_code = '"+inbound_code+"'\n" +
                        "   and inbound_item = '"+inbound_item+"'\n" +
                        "; \n"
                )
                .toString();
    }
}
