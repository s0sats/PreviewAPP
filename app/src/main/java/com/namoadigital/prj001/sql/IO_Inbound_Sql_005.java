package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 2/04/2019
 *
 * Seleciona cabeaçalho o item a ser adicionado
 *
 */
public class IO_Inbound_Sql_005 implements Specification {

    private long customer_code;
    private int pending;

    public IO_Inbound_Sql_005(long customer_code, int pending) {
        this.customer_code = customer_code;
        this.pending = pending;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        String tokenFilter = pending == 1 ?  "  and i.token != '' \n"  :  "  and (i.token = '' or i.token is null)";
        //Apenas para Debug
        String retSql = sb
            .append(" SELECT\n" +
                    "   i.*\n " +
                    " FROM \n" +
                    IO_InboundDao.TABLE+" i,\n" +
                    IO_Inbound_ItemDao.TABLE+" it\n" +
                    " WHERE\n" +
                    "  i.customer_code = it.customer_code\n" +
                    "  and i.inbound_prefix = it.inbound_prefix\n" +
                    "  and i.inbound_code = it.inbound_code\n" +
                    "  and i.customer_code = '"+customer_code+"'\n" +
                    tokenFilter +
                    "  and it.inbound_item = 0\n" +
                    "  and it.update_required = 1\n" +
                    " ORDER BY" +
                    "   i.customer_code,\n"+
                    "   i.inbound_prefix,\n" +
                    "   i.inbound_code \n")
            .toString();
        //
        return retSql;
    }
}
