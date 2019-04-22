package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 09/04/2019
 *
 * Query que retorna toda as Inbound pendente de envio
 *
 */
public class IO_Inbound_Sql_007 implements Specification {
    private long customer_code;
    private int inbound_prefix;
    private int inbound_code;
    private String token;

    public IO_Inbound_Sql_007(long customer_code, int inbound_prefix, int inbound_code, String token) {
        this.customer_code = customer_code;
        this.inbound_prefix = inbound_prefix;
        this.inbound_code = inbound_code;
        this.token = token;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" UPDATE "+ IO_InboundDao.TABLE + " SET\n" +
                        "   token = '"+token+"'\n" +
                        " WHERE\n" +
                        "   customer_code = '"+customer_code+"'\n" +
                        "   and inbound_prefix = '"+inbound_prefix+"'\n" +
                        "   and inbound_code = '"+inbound_code+"'\n" +
                        "; \n"
                )
                .toString();
    }
}
