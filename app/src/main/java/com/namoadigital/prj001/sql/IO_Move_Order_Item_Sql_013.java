package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.database.Specification;


/**
 * BARRIONUEVO - 17/06/2019
 *
 * Query usada pelo service WS_IO_Outbound_Item_Save para selecionar as
 * movimentações, picking, do outbound a ser enviada.
 *
 */
public class IO_Move_Order_Item_Sql_013 implements Specification {private long customer_code;
    private int outbound_prefix;
    private int outbound_code;

    public IO_Move_Order_Item_Sql_013(long customer_code, int outbound_prefix, int outbound_code) {
        this.customer_code = customer_code;
        this.outbound_prefix = outbound_prefix;
        this.outbound_code = outbound_code;
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
                                "  and m.outbound_prefix = '"+outbound_prefix+"'\n" +
                                "  and m.outbound_code = '"+outbound_code+"'\n" +
                                "  and m.update_required = 1\n")
                .toString();
    }
}
