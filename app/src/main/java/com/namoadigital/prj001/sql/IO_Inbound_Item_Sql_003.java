package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 09/04/2019
 *
 * Query que retorna toda as Inbound pendente de envio
 *
 */
public class IO_Inbound_Item_Sql_003 implements Specification {

    private long customer_code;

    public IO_Inbound_Item_Sql_003(long customer_code) {
        this.customer_code = customer_code;
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
                        "   and i.update_required = 1\n" +
                        " ORDER BY" +
                        "   i.customer_code,"+
                        "   i.inbound_prefix,"+
                        "   i.inbound_code"+
                        "; \n"
                )
                .toString();
    }
}
