package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_OutboundDao;
import com.namoadigital.prj001.dao.IO_Outbound_ItemDao;
import com.namoadigital.prj001.database.Specification;

public class IO_Outbound_Sql_005 implements Specification {
    private long customer_code;
    private int pending;

    public IO_Outbound_Sql_005(long customer_code, int pending) {
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
                        IO_OutboundDao.TABLE+" i,\n" +
                        IO_Outbound_ItemDao.TABLE+" it\n" +
                        " WHERE\n" +
                        "  i.customer_code = it.customer_code\n" +
                        "  and i.outbound_prefix = it.outbound_prefix\n" +
                        "  and i.outbound_code = it.outbound_code\n" +
                        "  and i.customer_code = '"+customer_code+"'\n" +
                        tokenFilter +
                        "  and it.outbound_item = 0\n" +
                        "  and it.update_required = 1\n" +
                        " ORDER BY" +
                        "   i.customer_code,\n"+
                        "   i.outbound_prefix,\n" +
                        "   i.outbound_code \n")
                .toString();
        //
        return retSql;
    }
}
