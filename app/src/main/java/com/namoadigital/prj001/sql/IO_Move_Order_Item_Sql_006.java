package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.database.Specification;

public class IO_Move_Order_Item_Sql_006 implements Specification {
    private long customer_code;
    private int move_prefix;
    private int move_code;
    private int inbound_item;

    public IO_Move_Order_Item_Sql_006(long customer_code, int move_prefix, int move_code, int inbound_item) {
        this.customer_code = customer_code;
        this.move_prefix = move_prefix;
        this.move_code = move_code;
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
                        "   and t.move_prefix = '"+move_prefix+"'\n" +
                        "   and t.move_code = '"+move_code+"'\n" +
                        "   and t.inbound_item = '"+inbound_item+"'\n" +
                        " ORDER BY\n" +
                        "    t.customer_code,\n" +
                        "    t.move_prefix,\n" +
                        "    t.move_code")
                .toString();
    }
}
