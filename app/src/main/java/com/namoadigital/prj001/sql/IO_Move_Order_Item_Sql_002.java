package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

public class IO_Move_Order_Item_Sql_002 implements Specification {
    private long customer_code;
    public static final String PENDING_QTY = "PENDING_QTY";

    public IO_Move_Order_Item_Sql_002(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "   COUNT(*) " + PENDING_QTY + "\n"+
                        " FROM\n" +
                        IO_MoveDao.TABLE + " \n" +
                        " WHERE\n" +
                        "   customer_code = '"+customer_code+"'\n" +
                        "   and status = '"+ Constant.SYS_STATUS_PENDING +'\'' )
                .toString();
    }
}
