package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_Ticket_ProductDao;
import com.namoadigital.prj001.database.Specification;

/**
 *  BARRIONUEVO 22-09-2020
 *  Query responsável por listar os produtos para aprovacao de retirada.
 *      - Nao deve ser exibidos produtos com qty igual a zero.
 */
public class TK_Ticket_Product_Sql_003 implements Specification {
    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;

    public TK_Ticket_Product_Sql_003(long customer_code, int ticket_prefix, int ticket_code) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "       p.*\n" +
                        " FROM\n" +
                        "   " + TK_Ticket_ProductDao.TABLE +" p\n" +
                        " WHERE\n" +
                        "      p.customer_code = '" + customer_code +"'\n" +
                        "      and p.ticket_prefix = '" + ticket_prefix +"'\n" +
                        "      and p.ticket_code = '" + ticket_code +"'\n" +
                        "      and p.qty > 0" + "\n;"
                )
                .toString();
    }
}
