package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.database.Specification;

public class IO_Move_Order_Item_Sql_014 implements Specification {
    private long customer_code;
    private String outbound_prefix;
    private String outbound_code;
    private String outbound_item;

    public IO_Move_Order_Item_Sql_014(long customer_code, String outbound_prefix, String outbound_code, String outbound_item) {
        this.customer_code = customer_code;
        this.outbound_prefix = outbound_prefix;
        this.outbound_code = outbound_code;
        this.outbound_item = outbound_item;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "   t.*\n" +
                        " FROM\n" +
                        IO_MoveDao.TABLE + " t \n" +
                        " WHERE\n" +
                        "   t.customer_code = '"+customer_code+"'\n" +
                        "   and t.outbound_prefix = '"+outbound_prefix+"'\n" +
                        "   and t.outbound_code = '"+outbound_code+"'\n" +
                        "   and t.outbound_item = '"+outbound_item+"'\n" +
                        " ORDER BY\n" +
                        "    t.customer_code,\n" +
                        "    t.outbound_prefix,\n" +
                        "    t.outbound_code")
                .toString();
    }
}
