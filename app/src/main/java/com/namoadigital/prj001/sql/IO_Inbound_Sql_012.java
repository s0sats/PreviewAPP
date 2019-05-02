package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 02/05/2019
 *
 * Query executada pelo service do FCM e que verifica a necessidade de setar a Inbound como sync_required.
 */
public class IO_Inbound_Sql_012 implements Specification {
    private long customer_code;
    private int inbound_prefix;
    private int inbound_code;
    private int scn;

    public IO_Inbound_Sql_012(long customer_code, int inbound_prefix, int inbound_code, int scn) {
        this.customer_code = customer_code;
        this.inbound_prefix = inbound_prefix;
        this.inbound_code = inbound_code;
        this.scn = scn;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        String sResp = "";

        if (scn != 0) {
            sResp = "  and scn < '" + scn + "'";
        }

        return sb
            .append(
                " UPDATE " + IO_InboundDao.TABLE + " set\n" +
                "   sync_required = '" + "1" + "'\n" +
                " WHERE\n" +
                "  customer_code = '" + customer_code + "'\n" +
                "  and inbound_prefix = '" + inbound_prefix + "'\n" +
                "  and inbound_code = '" + inbound_code + "'\n")
            .append(sResp)
            .toString();
    }
}
