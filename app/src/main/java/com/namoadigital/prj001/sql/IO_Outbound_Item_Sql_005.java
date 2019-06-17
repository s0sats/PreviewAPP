package com.namoadigital.prj001.sql;


import com.namoadigital.prj001.dao.IO_Outbound_ItemDao;
import com.namoadigital.prj001.database.Specification;

/**
 * BARRIONUEVO - 17/06/2019
 *
 * Query que retorna toda as Outbound pendente de envio
 *
 */
public class IO_Outbound_Item_Sql_005 implements Specification {
    private long customer_code;
    private int inbound_prefix;
    private int inbound_code;
    private int update_required;

    public IO_Outbound_Item_Sql_005(long customer_code, int inbound_prefix, int inbound_code, int update_required) {
        this.customer_code = customer_code;
        this.inbound_prefix = inbound_prefix;
        this.inbound_code = inbound_code;
        this.update_required = update_required;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" UPDATE "+ IO_Outbound_ItemDao.TABLE + "  SET\n" +
                        "   update_required = '"+update_required+"'\n" +
                        " WHERE\n" +
                        "   customer_code = '"+customer_code+"'\n" +
                        "   and inbound_prefix = '"+inbound_prefix+"'\n" +
                        "   and inbound_code = '"+inbound_code+"'\n" +
                        "; \n"
                )
                .toString();
    }
}
