package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.database.Specification;

public class IO_Move_Order_Item_Sql_006 implements Specification {
    private long customer_code;
    private String inbound_prefix;
    private String inbound_code;
    private String inbound_item;

    public IO_Move_Order_Item_Sql_006(long customer_code, String inbound_prefix, String inbound_code, String inbound_item) {
        this.customer_code = customer_code;
        this.inbound_prefix = inbound_prefix;
        this.inbound_code = inbound_code;
        this.inbound_item = inbound_item;
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
                        "   and t.inbound_prefix = '"+inbound_prefix+"'\n" +
                        "   and t.inbound_code = '"+inbound_code+"'\n" +
                        "   and t.inbound_item = '"+inbound_item+"'\n" +
                        " ORDER BY\n" +
                        "    t.customer_code,\n" +
                        "    t.inbound_prefix,\n" +
                        "    t.inbound_code")
                .toString();
    }
}
