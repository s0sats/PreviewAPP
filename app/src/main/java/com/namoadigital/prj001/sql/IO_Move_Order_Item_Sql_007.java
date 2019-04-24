package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 24/04/2019
 *
 * Query usada pelo service WS_IO_Inbound_Item_Save para selecionar as
 * movimentações,put_away, do inbound a ser enviada.
 *
 */

public class IO_Move_Order_Item_Sql_007 implements Specification {
    private long customer_code;
    private int inbound_prefix;
    private int inbound_code;

    public IO_Move_Order_Item_Sql_007(long customer_code, int inbound_prefix, int inbound_code) {
        this.customer_code = customer_code;
        this.inbound_prefix = inbound_prefix;
        this.inbound_code = inbound_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
            .append(
                " SELECT \n" +
                "   M.* \n" +
                " FROM \n "
                + IO_MoveDao.TABLE+" m\n" +
                " WHERE\n" +
                "  m.customer_code = '"+customer_code+"'\n" +
                "  and m.inbound_prefix = '"+inbound_prefix+"'\n" +
                "  and m.inbound_code = '"+inbound_code+"'\n" +
                "  and m.update_required = 1\n")
            .toString();
    }
}
