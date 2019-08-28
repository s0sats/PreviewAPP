package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * LUCHE - 05/07/2019
 * Query que valida se o Inbound deve ser alterada para o status WAINTING_SYNC
 */

public class Sql_Act061_002 implements Specification {

    private long customer_code;
    private int inbound_prefix;
    private int inbound_code;

    public Sql_Act061_002(long customer_code, int inbound_prefix, int inbound_code) {
        this.customer_code = customer_code;
        this.inbound_prefix = inbound_prefix;
        this.inbound_code = inbound_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "     IFNULL(MAX(CASE WHEN t.qtd_item_waiting = t.qtd_item AND (T.put_away_process = 0 OR t.qtd_item_waiting = t.qtd_move_waiting) \n" +
                        "           THEN 1\n" +
                        "           ELSE 0\n" +
                        "      END),0) "+ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"  \n" +
                        " FROM (\n" +
                        "    SELECT\n" +
                        "          sum(CASE WHEN i2.status not in ('"+ ConstantBaseApp.SYS_STATUS_CANCELLED + "','"+ConstantBaseApp.SYS_STATUS_INCONSISTENT+"') \n" +
                        "                   THEN 1\n" +
                        "                   ELSE 0\n" +
                        "                END) qtd_item ,      \n" +
                        "          sum(CASE WHEN i2.status in ('"+ ConstantBaseApp.SYS_STATUS_WAITING_SYNC + "','"+ConstantBaseApp.SYS_STATUS_DONE+"')\n" +
                        "                   THEN 1\n" +
                        "                   ELSE 0\n" +
                        "                END) qtd_item_waiting,\n" +
                        "          sum(CASE WHEN m.status not in ('"+ ConstantBaseApp.SYS_STATUS_CANCELLED + "','"+ConstantBaseApp.SYS_STATUS_INCONSISTENT+"') \n" +
                        "                   THEN 1\n" +
                        "                   ELSE 0\n" +
                        "                END)qtd_move ,   \n" +
                        "          sum(CASE WHEN m.status in ('"+ ConstantBaseApp.SYS_STATUS_WAITING_SYNC + "','"+ConstantBaseApp.SYS_STATUS_DONE+"') \n" +
                        "                   THEN 1\n" +
                        "                   ELSE 0\n" +
                        "                END) qtd_move_waiting, \n" +
                        "                i.put_away_process \n" +
                        "    FROM\n" +
                        "          io_inbound i\n" +
                        "    LEFT JOIN\n" +
                        "          io_inbound_item i2 on i.customer_code = i2.customer_code\n" +
                        "                                and i.inbound_prefix = i2.inbound_prefix\n" +
                        "                                and i.inbound_code = i2.inbound_code \n" +
                        "    LEFT JOIN\n" +
                        "          io_move m on m.customer_code = m.customer_code\n" +
                        "                       and m.inbound_prefix = i2.inbound_prefix\n" +
                        "                       and m.inbound_code = i2.inbound_code \n" +
                        "                       and m.inbound_item = i2.inbound_item\n" +
                        "                       and m.status not in ('"+ ConstantBaseApp.SYS_STATUS_CANCELLED + "','"+ConstantBaseApp.SYS_STATUS_INCONSISTENT+"') \n" +
                        "                                                          \n" +
                        "    WHERE\n" +
                        "         i.customer_code = "+customer_code+"\n" +
                        "         and i.inbound_prefix = "+inbound_prefix+"\n" +
                        "         and i.inbound_code = "+inbound_code+" \n" +
                        "    GROUP BY\n" +
                        "         i.customer_code,\n" +
                        "         i.inbound_prefix ,\n" +
                        "         i.inbound_code\n" +
                        "    )T\n")
                .toString();
    }
}
