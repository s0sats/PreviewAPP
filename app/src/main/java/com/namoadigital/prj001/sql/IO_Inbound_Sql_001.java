package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 27/03/2019
 *
 * Query que retorna qtd de inbound pendentes
 */
public class IO_Inbound_Sql_001 implements Specification {
    public static final String PENDENCY_QTY = "PENDENCY_QTY";

    private long customer_code;

    public IO_Inbound_Sql_001(long customer_code, int inbound_prefix, int inbound_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "   IFNULL(count(1),0) "+PENDENCY_QTY+"\n" +
                        " FROM\n" +
                            IO_InboundDao.TABLE + " t \n" +
                        " WHERE\n" +
                        "   t.customer_code = '"+customer_code+"'\n" +
                        " ORDER BY\n" +
                        "    t.customer_code,\n" +
                        "    t.inbound_prefix,\n" +
                        "    t.inbound_code\n"
                )
                .toString();
    }
}
