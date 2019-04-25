package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 09/04/2019
 *
 * Verifica se existe itens pendentes de envio para um inbound
 * Retorna Array de HmAux
 *
 */
public class IO_Inbound_Sql_010 implements Specification {
    public static final String HAS_UPDATE_TO_DO = "HAS_UPDATE_TO_DO";

    private long customer_code;
    private int inbound_prefix;
    private int inbound_code;

    public IO_Inbound_Sql_010(long customer_code, int inbound_prefix, int inbound_code) {
        this.customer_code = customer_code;
        this.inbound_prefix = inbound_prefix;
        this.inbound_code = inbound_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "   CASE WHEN count(1) = 0\n" +
                        "        THEN 0\n" +
                        "        ELSE 1\n" +
                        "   END "+HAS_UPDATE_TO_DO+" \n" +
                        "         FROM\n" +
                        IO_InboundDao.TABLE + "  i,\n" +
                        IO_Inbound_ItemDao.TABLE +" it \n" +
                        "  WHERE\n" +
                        "    i.customer_code = it.customer_code \n" +
                        "    and i.inbound_prefix =  it.inbound_prefix\n" +
                        "    and i.inbound_code =  it.inbound_code\n" +
                        "    \n" +
                        "    and it.customer_code = '"+customer_code+"'\n" +
                        "    and it.inbound_prefix = '"+inbound_prefix+"' \n" +
                        "    and it.inbound_code = '"+inbound_code+"'\n" +
                        "    and it.update_required = 1\n"
                )
                .toString();
    }
}
