package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 03/04/2019
 *
 * Query que retorna inbound baseada na PK
 *
 */
public class IO_Inbound_Sql_002 implements Specification {

    private long customer_code;
    private int inbound_prefix;
    private int inbound_code;

    public IO_Inbound_Sql_002(long customer_code, int inbound_prefix, int inbound_code) {
        this.customer_code = customer_code;
        this.inbound_prefix = inbound_prefix;
        this.inbound_code = inbound_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "   i.*\n" +
                        " FROM\n" +
                            IO_InboundDao.TABLE + " i \n" +
                        " WHERE\n" +
                        "   i.customer_code = '"+customer_code+"'\n" +
                        "   and i.inbound_prefix = '"+inbound_prefix+"'\n"+
                        "   and i.inbound_code = '"+ inbound_code+"'\n"+
                        "; \n"
                )
                .toString();
    }
}
