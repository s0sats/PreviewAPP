package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 04/02/2020
 *
 * Query que seleciona os serials id de um ticket
 *
 */

public class Sql_Act069_003 implements Specification {

    private long customer_code;
    private String ticket_prefix;
    private String ticket_code;

    public Sql_Act069_003(long customer_code, String ticket_prefix, String ticket_code) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
            .append(" SELECT\n" +
                    "       distinct c.serial_id serial_id\n" +
                    " FROM\n" +
                    "     "+ TK_Ticket_CtrlDao.TABLE +" c\n" +
                    " WHERE\n" +
                    "       c.customer_code = '"+customer_code+"'\n" +
                    "       and c.ticket_prefix = '"+ticket_prefix+"'\n" +
                    "       and c.ticket_code = '"+ticket_code+"'\n" +
                    " ORDER BY \n" +
                    "       c.ticket_prefix,\n" +
                    "       c.ticket_code\n"
                    )
            .toString();
    }
}
