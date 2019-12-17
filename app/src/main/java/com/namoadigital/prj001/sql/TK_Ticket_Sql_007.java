package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Query ERA USADA PARA CHECKIN_REQUIRED QUE NÃO EXISTE MAIS
 *
 */
public class TK_Ticket_Sql_007 implements Specification {

    private long customer_code;

    public TK_Ticket_Sql_007(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
            .append("")
            .toString();
    }
}
