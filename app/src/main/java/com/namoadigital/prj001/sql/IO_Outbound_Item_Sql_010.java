package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Outbound_ItemDao;
import com.namoadigital.prj001.database.Specification;

public class IO_Outbound_Item_Sql_010 implements Specification {

    private final long customer_code;
    private int outbound_prefix;
    private int outbound_code;
    private int outbound_item;
    private String status;

    public IO_Outbound_Item_Sql_010(long customer_code, Integer outbound_prefix, Integer outbound_code, Integer outbound_item, String waitingSync) {
        this.customer_code = customer_code;
        this.outbound_prefix = outbound_prefix;
        this.outbound_code = outbound_code;
        this.outbound_item = outbound_item;
        this.status = status;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" UPDATE "+ IO_Outbound_ItemDao.TABLE + "  SET\n" +
                        "   status = '"+status+"'\n" +
                        " WHERE\n" +
                        "   customer_code = '"+customer_code+"'\n" +
                        "   and outbound_prefix = '"+outbound_prefix+"'\n" +
                        "   and outbound_code = '"+outbound_code+"'\n" +
                        "   and outbound_item = '"+outbound_item+"'\n" +
                        "; \n"
                )
                .toString();
    }
}
