package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 20/04/2019
 *
 * Atualiza SCN , seta update_required e pode resetar token
 *
 */
public class IO_Inbound_Sql_006 implements Specification {

    private long customer_code;
    private int inbound_prefix;
    private int inbound_code;
    private int update_required;
    private int scn;

    public IO_Inbound_Sql_006(long customer_code, int inbound_prefix, int inbound_code, int update_required, int scn) {
        this.customer_code = customer_code;
        this.inbound_prefix = inbound_prefix;
        this.inbound_code = inbound_code;
        this.update_required = update_required;
        this.scn = scn;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        String resetToken = update_required == 0 ?  " , token = '' \n" : "";
        return sb
            .append(" UPDATE "+ IO_InboundDao.TABLE+" set\n" +
                    "   update_required = '"+update_required+"'\n," +
                    "   scn = '"+scn+"'\n" +
                    resetToken +
                    " WHERE\n" +
                    "  customer_code = '"+customer_code+"'\n" +
                    "  and inbound_prefix = '"+inbound_prefix+"'\n" +
                    "  and inbound_code = '"+inbound_code+"'")
            .toString();
    }
}
