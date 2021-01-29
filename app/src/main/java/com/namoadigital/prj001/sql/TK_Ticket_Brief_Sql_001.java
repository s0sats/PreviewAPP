package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_Ticket_BriefDao;
import com.namoadigital.prj001.database.Specification;

public class TK_Ticket_Brief_Sql_001 implements Specification {
    private long customer_code;
    private String site_code;

    public TK_Ticket_Brief_Sql_001(long customerCode, String site_code) {
        this.customer_code = customerCode;
        this.site_code = site_code;
    }

    //
    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        " t.customer_code,\n" +
                        "       t.scn,\n" +
                        "       t.ticket_prefix,\n" +
                        "       t.ticket_code,\n" +
                        "       t.current_step_order,\n" +
                        "       t.ticket_status,\n" +
                        "       CASE WHEN t.open_site_code <> '" + site_code + "'\n" +
                        "            THEN t.open_site_desc\n" +
                        "            ELSE null\n" +
                        "       END open_site_desc,\n" +
                        "       t.open_product_desc,\n" +
                        "       t.open_serial_id,\n" +
                        "       t.origin_desc,\n" +
                        "       1 user_focus,\n" +
                        "       t.step_desc,\n" +
                        "       t.forecast_start,\n" +
                        "       t.forecast_end,\n" +
                        "       t.step_count STEP_QTY, " +
                        "       t.fcm " +
                        " FROM\n" +
                        "   " + TK_Ticket_BriefDao.TABLE +"  t\n" +
                        " WHERE\n" +
                        "      t.customer_code = '" + customer_code +"'\n" +
                        "  and t.fcm = 0 "
                        )
                .toString();
    }
}
