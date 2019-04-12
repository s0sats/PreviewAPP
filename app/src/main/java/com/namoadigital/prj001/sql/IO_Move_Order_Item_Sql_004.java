package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.database.Specification;

public class IO_Move_Order_Item_Sql_004 implements Specification {
    private long customer_code;
    private int move_prefix;
    private int move_code;
    private String status;

    public IO_Move_Order_Item_Sql_004(long customer_code, int move_prefix, int move_code, String status) {
        this.customer_code = customer_code;
        this.move_prefix = move_prefix;
        this.move_code = move_code;
        this.status = status;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb.append(" UPDATE " + IO_MoveDao.TABLE +
                " set " + IO_MoveDao.STATUS + " = " + status +
                " WHERE " + IO_MoveDao.CUSTOMER_CODE + " = " + customer_code +
                " and " + IO_MoveDao.MOVE_PREFIX + " = " + move_prefix +
                " and " + IO_MoveDao.MOVE_CODE + " = " + move_code)
                .toString();
    }
}
