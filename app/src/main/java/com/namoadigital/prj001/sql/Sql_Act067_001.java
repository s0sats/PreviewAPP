package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Outbound_ItemDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

public class Sql_Act067_001 implements Specification {

    public static final String CONF_PERC = "conf_perc";
    public static final String PICKING_PERC = "picking_perc";

    private long customer_code;
    private int outbound_prefix;
    private int outbound_code;

    public Sql_Act067_001(long customer_code, int outbound_prefix, int outbound_code) {
        this.customer_code = customer_code;
        this.outbound_prefix = outbound_prefix;
        this.outbound_code = outbound_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "    ROUND(((t.conf_parcial / t.tot) * 100),1)  " + CONF_PERC + " ,\n" +
                        "    ROUND(((t.picking_parcial / t.tot)* 100),1) " + PICKING_PERC + " \n" +
                        " FROM(\n" +
                        "     SELECT\n" +
                        "       ROUND(COUNT(1),2) tot,         \n" +
                        "       SUM(\n" +
                        "             CASE WHEN i.status = '"+ConstantBaseApp.SYS_STATUS_DONE +"' OR  i.status = '"+ConstantBaseApp.SYS_STATUS_PICKING+"' \n" +
                        "                  THEN 1\n" +
                        "                  ELSE 0\n" +
                        "             END\n" +
                        "       ) conf_parcial,\n" +
                        "       SUM(\n" +
                        "             CASE WHEN i.status = '"+ConstantBaseApp.SYS_STATUS_DONE +"'\n" +
                        "                  THEN 1\n" +
                        "                  ELSE 0\n" +
                        "             END\n" +
                        "       ) picking_parcial\n" +
                        "       \n" +
                        "     FROM\n" +
                        IO_Outbound_ItemDao.TABLE + " i\n" +
                        "     WHERE\n" +
                        "       i.customer_code = '"+customer_code+"'\n" +
                        "       and i.outbound_prefix = '"+outbound_prefix+"'\n" +
                        "       and i.outbound_code = '"+outbound_code+"'\n" +
                        "       and i.status not in ('"+ ConstantBaseApp.SYS_STATUS_CANCELLED +"',\n" +
                        "                            '"+ ConstantBaseApp.SYS_STATUS_INCONSISTENT +"'\n" +
                        "          )\n" +
                        " ) T \n")
                .toString();
    }
}
