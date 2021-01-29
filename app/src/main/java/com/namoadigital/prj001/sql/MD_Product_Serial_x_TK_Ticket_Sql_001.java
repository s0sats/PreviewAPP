package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 03/11/2020
 * query que verifica se algum serial vinculado ao ticket precisa de atualização.
 */

public class MD_Product_Serial_x_TK_Ticket_Sql_001 implements Specification {

    private long customer_code;
    private int ticket_prefix;
    private int ticket_code;

    public MD_Product_Serial_x_TK_Ticket_Sql_001(long customer_code, int ticket_prefix, int ticket_code) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
            .append(" SELECT\n" +
                    "  s.*\n" +
                    " FROM\n" +
                    "  md_product_serials s,\n" +
                    "  (\n" +
                    "      SELECT\n" +
                    "       t.customer_code, \n" +
                    "       t.open_product_code product_code,\n" +
                    "       t.open_serial_id serial_id            \n" +
                    "     FROM \n" +
                    "      tk_ticket t\n" +
                    "     WHERE\n" +
                    "       t.customer_code = '"+customer_code+"'" +
                    "       and t.ticket_prefix = '"+ticket_prefix+"'\n" +
                    "       and t.ticket_code = '"+ticket_code+"'\n" +
                    "     \n" +
                    "     UNION\n" +
                    "       \n" +
                    "     SELECT\n" +
                    "       c.customer_code,\n" +
                    "       c.product_code product_code,\n" +
                    "       c.serial_id serial_id            \n" +
                    "     FROM \n" +
                    "      tk_ticket_ctrl C\n" +
                    "     WHERE\n" +
                    "       c.customer_code = '"+customer_code+"'" +
                    "       and c.ticket_prefix = '"+ticket_prefix+"'\n" +
                    "       and c.ticket_code = '"+ticket_code+"'\n" +
                    "  ) u\n" +
                    " WHERE\n" +
                    "  s.customer_code = u.customer_code\n" +
                    "  and s.product_code = u.product_code\n" +
                    "  and s.serial_id = u.serial_id\n" +
                    "  \n" +
                    "  and s.update_required = 1")
            .toString();
    }
}
