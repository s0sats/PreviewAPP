package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.dao.IO_OutboundDao;
import com.namoadigital.prj001.dao.IO_Outbound_ItemDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

public class IO_Outbound_Sql_011 implements Specification {
    public static final String HAS_UPDATE_TO_DO = "HAS_UPDATE_TO_DO";

    private long customer_code;
    private int outbound_prefix;
    private int outbound_code;

    public IO_Outbound_Sql_011(long customer_code, int outbound_prefix, int outbound_code) {
        this.customer_code = customer_code;
        this.outbound_prefix = outbound_prefix;
        this.outbound_code = outbound_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT" +
                        "  CASE WHEN count(1) = 0\n" +
                        "       THEN 0\n" +
                        "       ELSE 1\n" +
                        "  END " +HAS_UPDATE_TO_DO+" \n" +
                        " FROM (\n " +
                        "        SELECT\n" +
                        "             it.customer_code, \n" +
                        "             it.outbound_prefix, \n" +
                        "             it.outbound_code, \n" +
                        "             i.scn scn \n" +
                        "         FROM\n" +
                        IO_OutboundDao.TABLE + "  i,\n" +
                        IO_Outbound_ItemDao.TABLE +" it \n" +
                        "         WHERE\n" +
                        "             i.customer_code = it.customer_code \n" +
                        "             and i.outbound_prefix =  it.outbound_prefix\n" +
                        "             and i.outbound_code =  it.outbound_code\n" +
                        "             \n" +
                        "             and it.customer_code = '"+customer_code+"'\n" +
                        "             and it.outbound_prefix = '"+outbound_prefix+"'\n" +
                        "             and it.outbound_code = '"+outbound_code+"'\n" +
                        "             and it.update_required = 1 \n" +
                        "        \n" +
                        "         UNION\n" +
                        "                     \n" +
                        "         SELECT\n" +
                        "             m.customer_code, \n" +
                        "             m.outbound_prefix, \n" +
                        "             m.outbound_code,\n" +
                        "             ifnull(i.scn,0) scn\n" +
                        "         FROM\n" +
                        IO_MoveDao.TABLE +" m  \n" +
                        "         LEFT JOIN \n" +
                        IO_OutboundDao.TABLE + " i on m.customer_code = i.customer_code  \n" +
                        "                                  and m.outbound_prefix = i.outbound_prefix\n" +
                        "                                  and m.outbound_code = i.outbound_code \n" +
                        "         WHERE\n" +
                        "             m.customer_code = '"+customer_code+"'  \n" +
                        "             and m.outbound_prefix = '"+outbound_prefix+"'\n" +
                        "             and m.outbound_code = '"+outbound_code+"'\n" +
                        "             --define put_away\n" +
                        "             and m.move_type = '"+ ConstantBaseApp.IO_OUTBOUND +"' \n" +
                        "             and m.outbound_prefix is not null \n" +
                        "             and m.outbound_code is not null \n" +
                        "             and m.outbound_item is not null  \n" +
                        "             and m.update_required = 1 \n" +
                        "       ) t\n" +
                        "    \n" +
                        " GROUP BY\n" +
                        "  customer_code, \n" +
                        "  outbound_prefix, \n" +
                        "  outbound_code\n"
                )
                .toString();
    }
}
