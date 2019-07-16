package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

public class Sql_Act067_002 implements Specification {
    private long customer_code;
    private int outbound_prefix;
    private int outbound_code;

    public Sql_Act067_002(long customer_code, int outbound_prefix, int outbound_code) {
        this.customer_code = customer_code;
        this.outbound_prefix = outbound_prefix;
        this.outbound_code = outbound_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "     IFNULL(MAX(CASE WHEN t.qtd_item_waiting = t.qtd_move_waiting AND t.qtd_item_waiting = t.qtd_item\n" +
                        "           THEN 1\n" +
                        "           ELSE 0\n" +
                        "      END),0) "+ ConstantBaseApp.SYS_STATUS_WAITING_SYNC+"  \n" +
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
                        "                END) qtd_move_waiting \n" +
                        "    FROM\n" +
                        "          io_outbound i\n" +
                        "    LEFT JOIN\n" +
                        "          io_outbound_item i2 on i.customer_code = i2.customer_code\n" +
                        "                                and i.outbound_prefix = i2.outbound_prefix\n" +
                        "                                and i.outbound_code = i2.outbound_code \n" +
                        "    LEFT JOIN\n" +
                        "          io_move m on m.customer_code = m.customer_code\n" +
                        "                       and m.outbound_prefix = i2.outbound_prefix\n" +
                        "                       and m.outbound_code = i2.outbound_code \n" +
                        "                       and m.outbound_item = i2.outbound_item\n" +
                        "                                                          \n" +
                        "    WHERE\n" +
                        "         i.customer_code = "+customer_code+"\n" +
                        "         and i.outbound_prefix = "+outbound_prefix+"\n" +
                        "         and i.outbound_code = "+outbound_code+" \n" +
                        "    GROUP BY\n" +
                        "         i.customer_code,\n" +
                        "         i.outbound_prefix ,\n" +
                        "         i.outbound_code\n" +
                        "    )T\n")
                .toString();
    }
}
