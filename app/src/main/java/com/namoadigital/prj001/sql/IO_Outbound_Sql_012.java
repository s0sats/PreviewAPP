package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_OutboundDao;
import com.namoadigital.prj001.database.Specification;

public class IO_Outbound_Sql_012 implements Specification {
    private long customer_code;
    private int outbound_prefix;
    private int outbound_code;
    private int scn;

    public IO_Outbound_Sql_012(long customer_code, int outbound_prefix, int outbound_code, int scn) {
        this.customer_code = customer_code;
        this.outbound_prefix = outbound_prefix;
        this.outbound_code = outbound_code;
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
                        " UPDATE " + IO_OutboundDao.TABLE + " set\n" +
                                "   sync_required = '" + "1" + "'\n" +
                                " WHERE\n" +
                                "  customer_code = '" + customer_code + "'\n" +
                                "  and outbound_prefix = '" + outbound_prefix + "'\n" +
                                "  and outbound_code = '" + outbound_code + "'\n")
                .append(sResp)
                .toString();
    }
}
