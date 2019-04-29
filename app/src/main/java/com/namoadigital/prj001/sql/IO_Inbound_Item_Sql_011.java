package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

public class IO_Inbound_Item_Sql_011 implements Specification {
    private long customer_code;

    public IO_Inbound_Item_Sql_011(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "   COUNT(*) " + IO_Inbound_ItemDao.PENDING_QTY + "\n"+
                        " FROM\n" +
                        IO_Inbound_ItemDao.TABLE + " \n" +
                        " WHERE\n" +
                        "   customer_code = '"+customer_code+"'\n" +
                        "   and update_required = 1" )
                .toString();
    }

}
