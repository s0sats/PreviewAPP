package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_OutboundDao;
import com.namoadigital.prj001.dao.IO_Outbound_ItemDao;
import com.namoadigital.prj001.database.Specification;

public class IO_Outbound_Sql_010 implements Specification {
    public static final String HAS_UPDATE_TO_DO = "HAS_UPDATE_TO_DO";

    private long customer_code;
    private int outbound_prefix;
    private int outbound_code;

    public IO_Outbound_Sql_010(long customer_code, int outbound_prefix, int outbound_code) {
        this.customer_code = customer_code;
        this.outbound_prefix = outbound_prefix;
        this.outbound_code = outbound_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "   CASE WHEN count(1) = 0\n" +
                        "        THEN 0\n" +
                        "        ELSE 1\n" +
                        "   END "+HAS_UPDATE_TO_DO+" \n" +
                        "         FROM\n" +
                        IO_OutboundDao.TABLE + "  i,\n" +
                        IO_Outbound_ItemDao.TABLE +" it \n" +
                        "  WHERE\n" +
                        "    i.customer_code = it.customer_code \n" +
                        "    and i.outbound_prefix =  it.outbound_prefix\n" +
                        "    and i.outbound_code =  it.outbound_code\n" +
                        "    \n" +
                        "    and it.customer_code = '"+customer_code+"'\n" +
                        "    and it.outbound_prefix = '"+outbound_prefix+"' \n" +
                        "    and it.outbound_code = '"+outbound_code+"'\n" +
                        "    and it.update_required = 1\n"
                )
                .toString();
    }
}
