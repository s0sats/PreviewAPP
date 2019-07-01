package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_OutboundDao;
import com.namoadigital.prj001.database.Specification;

public class IO_Outbound_Sql_004 implements Specification {

    private long customer_code;
    private long outbound_prefix;
    private long outbound_code;
    private long update_required;

    public IO_Outbound_Sql_004(long customer_code, long outbound_prefix, long outbound_code, long update_required) {
        this.customer_code = customer_code;
        this.outbound_prefix = outbound_prefix;
        this.outbound_code = outbound_code;
        this.update_required = update_required;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" UPDATE "+ IO_OutboundDao.TABLE+" set\n" +
                        "   update_required = '"+update_required+"'\n" +
                        " WHERE\n" +
                        "  customer_code = '"+customer_code+"'\n" +
                        "  and outbound_prefix = '"+outbound_prefix+"'\n" +
                        "  and outbound_code = '"+outbound_code+"'")
                .toString();
    }
}
