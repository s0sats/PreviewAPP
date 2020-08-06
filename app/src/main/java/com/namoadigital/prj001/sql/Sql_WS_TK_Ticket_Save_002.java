package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Query que seta update required e update_required_product no cabeçalho do ticket
 * LUCCHE - 06/08/2020
 * Modificado data type das propriedades, criado novo construtor e add propriedade update_required_product
 */

public class Sql_WS_TK_Ticket_Save_002 implements Specification {

    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;
    private int update_required;
    private int update_required_product;
    private String syncUpdate ="";


    public Sql_WS_TK_Ticket_Save_002(long customer_code, int ticket_prefix, int ticket_code, int update_required, int update_required_product, int sync_required) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
        this.update_required = update_required;
        this.update_required_product = update_required_product;
        syncUpdate = ",\n sync_required = '"+sync_required+"'\n";
    }

    public Sql_WS_TK_Ticket_Save_002(long customer_code, int ticket_prefix, int ticket_code, int update_required, int update_required_product) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
        this.update_required = update_required;
        this.update_required_product = update_required_product;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" UPDATE "+ TK_TicketDao.TABLE+" set\n" +
                        "   update_required = '"+update_required+"',\n" +
                        "   update_required_product = '"+update_required+"'\n" +
                            syncUpdate +
                        " WHERE\n" +
                        "  customer_code = '"+customer_code+"'\n" +
                        "  and ticket_prefix = '"+ticket_prefix+"'\n" +
                        "  and ticket_code = '"+ticket_code+"'")
                .toString();
    }
}
