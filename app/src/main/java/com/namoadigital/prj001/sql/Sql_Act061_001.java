package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * LUCHE - 15/04/2019
 * - Criado query que retorno o percentual de conf e put_away da inbound carregada.
 *
 */

public class Sql_Act061_001 implements Specification {

    public static final String CONF_PERC = "conf_perc";
    public static final String PUT_AWAY_PERC = "put_away_perc";

    private long customer_code;
    private int inbound_prefix;
    private int inbound_code;

    public Sql_Act061_001(long customer_code, int inbound_prefix, int inbound_code) {
        this.customer_code = customer_code;
        this.inbound_prefix = inbound_prefix;
        this.inbound_code = inbound_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "    ROUND(((t.conf_parcial / t.tot) * 100),1)  "+CONF_PERC+" ,\n" +
                        "    ROUND(((t.put_away_parcial / t.tot)* 100),1) "+PUT_AWAY_PERC+" \n" +
                        " FROM(\n" +
                        "     SELECT\n" +
                        "       ROUND(COUNT(1),2) tot,         \n" +
                        "       SUM(\n" +
                        "             CASE WHEN i.status in( '"+ConstantBaseApp.SYS_STATUS_DONE +"',\n" +
                                                             "'"+ConstantBaseApp.SYS_STATUS_PUT_AWAY+"',\n" +
                                                             "'"+ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"') \n" +
                        "                  THEN 1\n" +
                        "                  ELSE 0\n" +
                        "             END\n" +
                        "       ) conf_parcial,\n" +
                        "       SUM(\n" +
                        "             CASE WHEN i.status = '"+ConstantBaseApp.SYS_STATUS_DONE +"'\n" +
                        "                                   OR ( i.status ='"+ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"'\n" +
                        "                                        AND m.status ='"+ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"')\n" +
                        "                  THEN 1\n" +
                        "                  ELSE 0\n" +
                        "             END\n" +
                        "       ) put_away_parcial\n" +
                        "       \n" +
                        "     FROM\n" +
                        "       "+ IO_Inbound_ItemDao.TABLE + " i\n" +
                        "     LEFT JOIN\n" +
                        "       "+  IO_MoveDao.TABLE+" m on i.customer_code = m.customer_code\n" +
                        "                      AND i.inbound_prefix  = m.inbound_prefix\n" +
                        "                      AND i.inbound_code = m.inbound_code \n" +
                        "                      AND i.inbound_item = m.inbound_item \n " +
                        "     WHERE\n" +
                        "       i.customer_code = '"+customer_code+"'\n" +
                        "       and i.inbound_prefix = '"+inbound_prefix+"'\n" +
                        "       and i.inbound_code = '"+inbound_code+"'\n" +
                        "       and i.status not in ('"+ ConstantBaseApp.SYS_STATUS_CANCELLED +"',\n" +
                        "                            '"+ ConstantBaseApp.SYS_STATUS_INCONSISTENT +"'\n" +
                        "          )\n" +
                        " ) T \n")
                .toString();
    }
}
