package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoadigital.prj001.dao.IO_OutboundDao;
import com.namoadigital.prj001.database.Specification;

public class IO_Outbound_Item_Sql_002 implements Specification {

    public static final String PENDENCY_QTY = "PENDENCY_QTY";

    private long customer_code;

    public IO_Outbound_Item_Sql_002(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "   IFNULL(count(1),0) "+PENDENCY_QTY+"\n" +
                        " FROM\n" +
                        IO_OutboundDao.TABLE + " t \n" +
                        " WHERE\n" +
                        "   t.customer_code = '"+customer_code+"'\n" +
                        "   and t.status in (" +
                        "                       '"+ ConstantBase.SYS_STATUS_PENDING +"'," +
                        "                       '"+ ConstantBase.SYS_STATUS_PROCESS +"'," +
                        "                       '"+ ConstantBase.SYS_STATUS_EDIT +"'" +
                        " ); \n"
                )
                .toString();
    }
}
