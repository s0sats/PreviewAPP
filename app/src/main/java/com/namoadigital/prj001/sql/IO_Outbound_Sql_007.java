package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_OutboundDao;
import com.namoadigital.prj001.database.Specification;

public class IO_Outbound_Sql_007 implements Specification {
    private long customer_code;
    private int outbound_prefix;
    private int outbound_code;
    private String token;

    public IO_Outbound_Sql_007(long customer_code, int outbound_prefix, int outbound_code, String token) {
        this.customer_code = customer_code;
        this.outbound_prefix = outbound_prefix;
        this.outbound_code = outbound_code;
        this.token = token;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" UPDATE "+ IO_OutboundDao.TABLE + " SET\n" +
                        "   token = '"+token+"'\n" +
                        " WHERE\n" +
                        "   customer_code = '"+customer_code+"'\n" +
                        "   and outbound_prefix = '"+outbound_prefix+"'\n" +
                        "   and outbound_code = '"+outbound_code+"'\n" +
                        "; \n"
                )
                .toString();
    }
}
