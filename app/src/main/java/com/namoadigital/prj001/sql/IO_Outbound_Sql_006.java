package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_OutboundDao;
import com.namoadigital.prj001.database.Specification;

public class IO_Outbound_Sql_006 implements Specification {
    private long customer_code;
    private int outbound_prefix;
    private int outbound_code;
    private int update_required;
    private int scn;

    public IO_Outbound_Sql_006(long customer_code, int outbound_prefix, int outbound_code, int update_required, int scn) {
        this.customer_code = customer_code;
        this.outbound_prefix = outbound_prefix;
        this.outbound_code = outbound_code;
        this.update_required = update_required;
        this.scn = scn;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        String resetToken = update_required == 0 ?  " , token = '' \n" : "";
        return sb
                .append(" UPDATE "+ IO_OutboundDao.TABLE+" set\n" +
                        "   update_required = '"+update_required+"'\n," +
                        "   scn = '"+scn+"'\n" +
                        resetToken +
                        " WHERE\n" +
                        "  customer_code = '"+customer_code+"'\n" +
                        "  and outbound_prefix = '"+outbound_prefix+"'\n" +
                        "  and outbound_code = '"+outbound_code+"'")
                .toString();
    }
}
