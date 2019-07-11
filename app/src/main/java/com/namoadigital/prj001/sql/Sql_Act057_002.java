package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

public class Sql_Act057_002 implements Specification {
    public static final String PERC_DONE = "perc_done";

    private long customer_code;
    private boolean filterPendent;
    private boolean filterInProcess;

    public Sql_Act057_002(long customer_code, boolean filterPendent, boolean filterInProcess) {
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
                        "              round((((t.conf_parcial + t.put_away_parcial) / t.tot) * 100),1) "+PERC_DONE+" \n" +
                        "           FROM(\n" +
                        "               SELECT            \n" +
                        "                 round( COUNT(1) * (i.put_away_process + 1) , 2) tot,         \n" +
                        "                 SUM(\n" +
                        "                       CASE WHEN it.status = '"+ ConstantBaseApp.SYS_STATUS_DONE +"' OR  it.status = '"+ ConstantBaseApp.SYS_STATUS_PUT_AWAY +"' \n" +
                        "                            THEN 1\n" +
                        "                            ELSE 0\n" +
                        "                       END\n" +
                        "                 ) conf_parcial,\n" +
                        "                 SUM(\n" +
                        "                       CASE WHEN it.status = '"+ ConstantBaseApp.SYS_STATUS_DONE +"' AND i.put_away_process = 1\n" +
                        "                            THEN 1\n" +
                        "                            ELSE 0\n" +
                        "                       END\n" +
                        "                 )\n" +
                        "                  put_away_parcial\n" +
                        "                 \n" +
                        "               FROM\n" +
                        IO_Inbound_ItemDao.TABLE +" it\n" +
                        "               WHERE\n" +
                        "                it.customer_code = i.customer_code \n" +
                        "                and it.inbound_prefix = i.inbound_prefix \n" +
                        "                and it.inbound_code =  i.inbound_code\n" +
                        "                and it.status not in ('"+ ConstantBaseApp.SYS_STATUS_CANCELLED +"','"+ ConstantBaseApp.SYS_STATUS_INCONSISTENT +"')\n" +
                        "            ) T)       \n" +
                        "          ,0) perc_done\n" +
                        "   FROM\n" +
                        IO_InboundDao.TABLE + " i\n" +
                        "   WHERE\n" +
                        "    i.customer_code = '"+customer_code+"'\n" +
                        "    and i.status in('"+ConstantBaseApp.SYS_STATUS_DONE+"','"+ConstantBaseApp.SYS_STATUS_CANCELLED+"')\n" +
                        "  ORDER BY \n" +
                        "    i.inbound_prefix,\n" +
                        "    i.inbound_code\n"
                )
                .toString();
    }
}
