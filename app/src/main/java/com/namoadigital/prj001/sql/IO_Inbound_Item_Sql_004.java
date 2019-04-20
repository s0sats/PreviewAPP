package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 09/04/2019
 *
 * Query que retorna toda as Inbound pendente de envio
 *
 */
public class IO_Inbound_Item_Sql_004 implements Specification {

    private long customer_code;
    private int inbound_prefix;
    private int inbound_code;

    public IO_Inbound_Item_Sql_004(long customer_code, int inbound_prefix, int inbound_code) {
        this.customer_code = customer_code;
        this.inbound_prefix = inbound_prefix;
        this.inbound_code = inbound_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "   i.*\n" +
                        " FROM\n" +
                            IO_Inbound_ItemDao.TABLE + " i \n" +
                        " WHERE\n" +
                        "   i.customer_code = '"+customer_code+"'\n" +
                        "   and i.inbound_prefix = '"+inbound_prefix+"'\n" +
                        "   and i.inbound_code = '"+inbound_code+"'\n" +
                        "   and i.update_required = 1\n" +
                        " ORDER BY" +
                        "   i.inbound_item"+
                        "; \n"
                )
                .toString();
    }
}
