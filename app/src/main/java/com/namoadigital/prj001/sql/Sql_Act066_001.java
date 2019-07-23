package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_OutboundDao;
import com.namoadigital.prj001.dao.IO_Outbound_ItemDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

public class Sql_Act066_001 implements Specification {
    public static final String PERC_DONE = "perc_done";

    private long customer_code;
    private boolean filterPendent;
    private boolean filterInProcess;

    public Sql_Act066_001(long customer_code, boolean filterPendent, boolean filterInProcess) {
        this.customer_code = customer_code;
        this.filterPendent = filterPendent;
        this.filterInProcess = filterInProcess;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append("   SELECT\n" +
                        "          i.*,\n" +
                        "          IFNULL(\n" +
                        "           (SELECT   \n" +
                        "              round((((t.conf_parcial + t.picking_parcial) / t.tot) * 100),1) "+PERC_DONE+" \n" +
                        "           FROM(\n" +
                        "               SELECT            \n" +
                        "                 round( COUNT(1) * (i.picking_process + 1) , 2) tot,         \n" +
                        "                 SUM(\n" +
                        "                       CASE WHEN it.status = '"+ ConstantBaseApp.SYS_STATUS_DONE +"' OR  it.status = '"+ ConstantBaseApp.SYS_STATUS_PICKING_DONE +"' \n" +
                        "                            THEN 1\n" +
                        "                            ELSE 0\n" +
                        "                       END\n" +
                        "                 ) conf_parcial,\n" +
                        "                 SUM(\n" +
                        "                       CASE WHEN it.status = '"+ ConstantBaseApp.SYS_STATUS_DONE +"' AND i.picking_process = 1\n" +
                        "                            THEN 1\n" +
                        "                            ELSE 0\n" +
                        "                       END\n" +
                        "                 )\n" +
                        "                  picking_parcial\n" +
                        "                 \n" +
                        "               FROM\n" +
                        IO_Outbound_ItemDao.TABLE +" it\n" +
                        "               WHERE\n" +
                        "                it.customer_code = i.customer_code \n" +
                        "                and it.outbound_prefix = i.outbound_prefix \n" +
                        "                and it.outbound_code =  i.outbound_code\n" +
                        "                and it.status not in ('"+ ConstantBaseApp.SYS_STATUS_CANCELLED +"','"+ ConstantBaseApp.SYS_STATUS_INCONSISTENT +"')\n" +
                        "            ) T)       \n" +
                        "          ,0) perc_done\n" +
                        "   FROM\n" +
                        IO_OutboundDao.TABLE + " i\n" +
                        "   WHERE\n" +
                        "    i.customer_code = '"+customer_code+"'\n" +
                        "    and i.status in('"+ConstantBaseApp.SYS_STATUS_PENDING+"','"+ConstantBaseApp.SYS_STATUS_PROCESS+"')\n" +
                        "  ORDER BY \n" +
                        "    i.outbound_prefix,\n" +
                        "    i.outbound_code\n"
                )
                .toString();
    }
}
