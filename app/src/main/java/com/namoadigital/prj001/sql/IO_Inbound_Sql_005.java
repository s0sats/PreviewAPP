package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 09/04/2019
 *
 * Seleciona itens que token pendente de envio.
 *
 */
public class IO_Inbound_Sql_005 implements Specification {

    private long customer_code;

    public IO_Inbound_Sql_005(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
            .append(" SELECT\n" +
                    "   i.*\n " +
                    " FROM \n" +
                    IO_InboundDao.TABLE+" i\n" +
                    " WHERE\n" +
                    "  i.customer_code = '"+customer_code+"'\n" +
                    "  and i.token != ''" +
                    " ORBER BY" +
                    "   i.customer_code,\n"+
                    "   i.inbound_prefix,\n" +
                    "   i.inbound_code \n")
            .toString();
    }
}
