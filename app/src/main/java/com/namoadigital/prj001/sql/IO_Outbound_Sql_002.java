package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_OutboundDao;
import com.namoadigital.prj001.database.Specification;

public class IO_Outbound_Sql_002 implements Specification {

    private long customer_code;
    private int outbound_prefix;
    private int outbound_code;

    public IO_Outbound_Sql_002(long customer_code, int outbound_prefix, int outbound_code) {
        this.customer_code = customer_code;
        this.outbound_prefix = outbound_prefix;
        this.outbound_code = outbound_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "   i.*\n" +
                        " FROM\n" +
                        IO_OutboundDao.TABLE + " i \n" +
                        " WHERE\n" +
                        "   i.customer_code = '"+customer_code+"'\n" +
                        "   and i.outbound_prefix = '"+outbound_prefix+"'\n"+
                        "   and i.outbound_code = '"+ outbound_code+"'\n"+
                        "; \n"
                )
                .toString();
    }
}
