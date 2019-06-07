package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Blind_MoveDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

public class IO_Blind_Move_Sql_006 implements Specification {
    private long customer_code;

    public IO_Blind_Move_Sql_006(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "   COUNT(*) " + IO_Blind_MoveDao.PENDING_QTY + "\n"+
                        " FROM\n" +
                        IO_Blind_MoveDao.TABLE + " \n" +
                        " WHERE\n" +
                        "   customer_code = '"+customer_code+"'\n" +
                        "   and status = '"+ Constant.SYS_STATUS_WAITING_SYNC +'\'' )
                .toString();
    }
}
