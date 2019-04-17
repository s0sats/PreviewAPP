package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 09/04/2019
 *
 * Atualiza update_required para  0 da inbound informada
 *
 */
public class IO_Inbound_Sql_004 implements Specification {

    private long customer_code;
    private long inbound_prefix;
    private long inbound_code;
    private long update_required;

    public IO_Inbound_Sql_004(long customer_code, long inbound_prefix, long inbound_code, long update_required) {
        this.customer_code = customer_code;
        this.inbound_prefix = inbound_prefix;
        this.inbound_code = inbound_code;
        this.update_required = update_required;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
            .append(" UPDATE "+ IO_InboundDao.TABLE+" set\n" +
                    "   update_required = '"+update_required+"'\n" +
                    " WHERE\n" +
                    "  customer_code = '"+customer_code+"'\n" +
                    "  and inbound_prefix = '"+inbound_prefix+"'\n" +
                    "  and inbound_code = '"+inbound_code+"'")
            .toString();
    }
}
