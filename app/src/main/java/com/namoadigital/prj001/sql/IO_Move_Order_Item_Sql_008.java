package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 24/04/2019
 *
 * Query usada pelo service WS_IO_Inbound_Item_Save atualizar o update required de determinada
 * move
 *
 */

public class IO_Move_Order_Item_Sql_008 implements Specification {
    private long customer_code;
    private int move_prefix;
    private int move_code;
    private int update_required;

    public IO_Move_Order_Item_Sql_008(long customer_code, int move_prefix, int move_code, int update_required) {
        this.customer_code = customer_code;
        this.move_prefix = move_prefix;
        this.move_code = move_code;
        this.update_required = update_required;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
            .append(
                " UPDATE " + IO_MoveDao.TABLE+" SET\n" +
                "  update_required = '"+update_required+"'\n" +
                " WHERE\n" +
                "  customer_code = '"+customer_code+"'\n" +
                "  and move_prefix = '"+move_prefix+"'\n" +
                "  and move_code = '"+move_code+"'\n"
                )
            .toString();
    }
}
